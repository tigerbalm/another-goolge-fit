package com.moondog.anothergoogelfit;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        setupNaviDrawer();
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
                    case R.id.history :
                        Toast.makeText(getApplicationContext(), "History menu", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.recording :
                        Toast.makeText(getApplicationContext(), "Recording menu", Toast.LENGTH_LONG)
                                .show();
                        break;
                    case R.id.session :
                        Toast.makeText(getApplicationContext(), "Session menu", Toast.LENGTH_LONG)
                                .show();
                        break;
                    case R.id.sensor :
                        Toast.makeText(getApplicationContext(), "Sensor menu", Toast.LENGTH_LONG)
                                .show();
                        break;
                    case R.id.config :
                        Toast.makeText(getApplicationContext(), "Setting menu", Toast.LENGTH_LONG)
                                .show();
                        break;
                    default :
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

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }
}
