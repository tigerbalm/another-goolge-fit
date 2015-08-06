package com.moondog.anothergoogelfit;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;

import java.lang.ref.WeakReference;

/**
 * Created by sjun.lee on 2015-08-05.
 *
 * https://github.com/jpventura/google-play-services
 */
public class GoogleConnection implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static GoogleConnection mInstance;
    private WeakReference<Activity> mActivityWeakReference;
    private GoogleApiClient.Builder mGoogleApiClientBuilder;
    private GoogleApiClient mGoogleApiClient;

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
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
