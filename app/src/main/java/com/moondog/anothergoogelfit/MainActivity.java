package com.moondog.anothergoogelfit;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;

public class MainActivity extends AppCompatActivity implements GoogleConnection
        .ConnectionCallbacks {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_OAUTH = 1;

    /**
     *  Track whether an authorization activity is stacking over the current activity, i.e. when
     *  a known auth error is being resolved, such as showing the account chooser or presenting a
     *  consent dialog. This avoids common duplications as might happen on screen rotations, etc.
     */
    private static final String AUTH_PENDING = "auth_state_pending";

    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;

    private boolean authInProgress = false;
    private GoogleApiClient mClient = null;
    private GoogleConnection mConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        setupNaviDrawer();

        //mClient = ((AFGApplication)getApplication()).mGoogleApiClient;

        //buildGoogleClient();

        mConnection = GoogleConnection.newInstance(this);
        mClient = mConnection.getClient();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Connect to the Fitness API
        Log.i(TAG, "Connecting...");
        mConnection.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_OAUTH) {
//            authInProgress = false;
//            if (resultCode == RESULT_OK) {
//                // Make sure the app is not already connected or attempting to connect
//                if (!mClient.isConnecting() && !mClient.isConnected()) {
//                    mClient.connect();
//                }
//            }
//        }

        Log.d(TAG, "onActivityResult requestCode : " + requestCode + ", resultCode: " + resultCode);

        if (GoogleConnection.REQUEST_CODE == requestCode) {
            mConnection.onActivityResult(resultCode);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(AUTH_PENDING, authInProgress);
    }

    /**
     *  Build a {@link GoogleApiClient} that will authenticate the user and allow the application
     *  to connect to Fitness APIs. The scopes included should match the scopes your app needs
     *  (see documentation for details). Authentication will occasionally fail intentionally,
     *  and in those cases, there will be a known resolution, which the OnConnectionFailedListener()
     *  can address. Examples of this include the user never having signed in before, or having
     *  multiple accounts on the device and needing to specify which account to use, etc.
     */
    private void buildGoogleClient() {
        // Create the Google API Client
        mClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                .addConnectionCallbacks(
                        new GoogleApiClient.ConnectionCallbacks() {

                            @Override
                            public void onConnected(Bundle bundle) {
                                Log.i(TAG, "Connected!!!");

                                // TODO : this is insane!!
                                ((AFGApplication)getApplication()).setGoogleApiClient(mClient);

                                // Now you can make calls to the Fitness APIs.
                                // Put application specific code here.
                                //setupInitialFragment();
                                replaceFragment(new HistoryFragment());
                            }

                            @Override
                            public void onConnectionSuspended(int i) {
                                // If your connection to the sensor gets lost at some point,
                                // you'll be able to determine the reason and react to it here.
                                if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                    Log.i(TAG, "Connection lost.  Cause: Network Lost.");
                                } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                    Log.i(TAG, "Connection lost.  Reason: Service Disconnected");
                                }
                            }
                        }
                )
                .addOnConnectionFailedListener(
                        new GoogleApiClient.OnConnectionFailedListener() {
                            // Called whenever the API client fails to connect.
                            @Override
                            public void onConnectionFailed(ConnectionResult result) {
                                Log.i(TAG, "Connection failed. Cause: " + result.toString());
                                if (!result.hasResolution()) {
                                    // Show the localized error dialog
                                    GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(),
                                            MainActivity.this, 0).show();
                                    return;
                                }
                                // The failure has a resolution. Resolve it.
                                // Called typically when the app is not yet authorized, and an
                                // authorization dialog is displayed to the user.
                                if (!authInProgress) {
                                    try {
                                        Log.i(TAG, "Attempting to resolve failed connection");
                                        authInProgress = true;
                                        result.startResolutionForResult(MainActivity.this,
                                                REQUEST_OAUTH);
                                    } catch (IntentSender.SendIntentException e) {
                                        Log.e(TAG,
                                                "Exception while starting resolution activity", e);
                                    }
                                }
                            }
                        }
                )
                .build();
    }

    private void setupInitialFragment() {
        replaceFragment(new HistoryFragment());
    }

    private void setupNaviDrawer() {
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }

                mDrawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.history:
                        replaceFragment(new HistoryFragment());
                        break;
                    case R.id.recording:
                        Toast.makeText(getApplicationContext(), "Recording menu", Toast.LENGTH_LONG)
                                .show();
                        break;
                    case R.id.session:
                        Toast.makeText(getApplicationContext(), "Session menu", Toast.LENGTH_LONG)
                                .show();
                        break;
                    case R.id.sensor:
                        Toast.makeText(getApplicationContext(), "Sensor menu", Toast.LENGTH_LONG)
                                .show();
                        break;
                    case R.id.config:
                        Toast.makeText(getApplicationContext(), "Setting menu", Toast.LENGTH_LONG)
                                .show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Error: no menu", Toast.LENGTH_LONG)
                                .show();
                        break;
                }

                return true;
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R
                .string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        toggle.syncState();

        //calling sync state is necessary or else your hamburger icon wont show up
        mDrawerLayout.setDrawerListener(toggle);
    }

    private void replaceFragment(@NonNull Fragment fragment) {
        Toast.makeText(this, fragment.toString(), Toast.LENGTH_LONG).show();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, fragment)
                .commit();
    }

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    @Override
    public void onConnected(Bundle bundle) {
        setupInitialFragment();
    }

    @Override
    public void onConnectionFailed(int var1) {

    }
}
