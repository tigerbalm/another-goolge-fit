package com.moondog.anothergoogelfit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by sangjunlee on 2015. 8. 5..
 */
public class HistoryFragment extends Fragment {
    // Todo:
    // 1. google connect
    // 2. Today's summary : step, distance, calorie
    // 3. list view : 24hrs, 7days
    public HistoryFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_main, container, false);
    }
}
