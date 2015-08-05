package com.moondog.anothergoogelfit;

import android.app.Application;

import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;

/**
 * Created by sjun.lee on 2015-08-05.
 */
public class AFGApplication extends Application {
    public static GoogleApiClient mGoogleApiClient = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    public void setGoogleApiClient(GoogleApiClient client) {
        mGoogleApiClient = client;
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }
}
