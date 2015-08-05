package com.moondog.anothergoogelfit;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;

/**
 * Created by sjun.lee on 2015-08-05.
 */
public class GoogleConnectionHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // rewrite code using https://github.com/jpventura/google-play-services

    private static GoogleConnectionHelper mInstance;

    private GoogleApiClient mClient;
    private Context mContext;

    public static GoogleConnectionHelper newInstance(Context context) {
        if (mInstance == null) {
            mInstance = new GoogleConnectionHelper(context);
        }

        return mInstance;
    }

    private GoogleConnectionHelper(Context context) {
        mClient = new GoogleApiClient.Builder(context)
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
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
