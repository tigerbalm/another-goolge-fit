package com.moondog.anothergoogelfit;

import android.app.Activity;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;

import java.lang.ref.WeakReference;

/**
 * Created by sjun.lee on 2015-08-05.
 * <p/>
 * https://github.com/jpventura/google-play-services
 */
public class GoogleConnection implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = GoogleConnection.class.getSimpleName();

    public static final int REQUEST_CODE = 9876;

    private static GoogleConnection mInstance;
    private WeakReference<Activity> mActivityWeakReference;
    private GoogleApiClient.Builder mGoogleApiClientBuilder;
    private GoogleApiClient mGoogleApiClient;
    private boolean authInProgress;
    private ConnectionCallbacks mCallbacks;

    public static GoogleConnection newInstance(Activity activity) {
        if (mInstance == null) {
            mInstance = new GoogleConnection(activity);
        }

        return mInstance;
    }

    private GoogleConnection(Activity activity) {
        mActivityWeakReference = new WeakReference<>(activity);

        mGoogleApiClientBuilder = new GoogleApiClient.Builder(mActivityWeakReference.get().getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ));

        mGoogleApiClient = mGoogleApiClientBuilder.build();

        mCallbacks = (ConnectionCallbacks) activity;
    }

    public void connect() {
        Log.d(TAG, "connect");
        if (mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting()) {
            Log.d(TAG, "client is connecting. return");
            return;
        }

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mCallbacks != null) {
            mCallbacks.onConnected(bundle);
        }
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

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Cause: " + connectionResult.toString());

        if (!connectionResult.hasResolution()) {
            Log.d(TAG, "onConnectionFailed has no resolution");
            // Show the localized error dialog
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(),
                    mActivityWeakReference.get(), 0).show();
            return;
        }

        Log.d(TAG, "onConnectionFailed authInProgress: " + authInProgress);

        // The failure has a resolution. Resolve it.
        // Called typically when the app is not yet authorized, and an
        // authorization dialog is displayed to the user.
        if (!authInProgress) {
            try {
                Log.i(TAG, "Attempting to resolve failed connection");
                authInProgress = true;
                connectionResult.startResolutionForResult(mActivityWeakReference.get(),
                        REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                Log.e(TAG,
                        "Exception while starting resolution activity", e);
            }
        }

    }

    public GoogleApiClient getClient() {
        return mGoogleApiClient;
    }

    public interface ConnectionCallbacks {
        void onConnected(Bundle bundle);

        void onConnectionFailed(int var1);
    }

    public void onActivityResult(int result) {
        Log.d(TAG, "onActivityResult result: " + result);

        authInProgress = false;
        if (result == Activity.RESULT_OK) {
            // If the error resolution was successful we should continue
            // processing errors.
            connect();
        } else {
            // If the error resolution was not successful or the user canceled,
            // we should stop processing errors.

            Log.d(TAG, "onActivityResult Activity.Result_not_ok");
        }
    }
}
