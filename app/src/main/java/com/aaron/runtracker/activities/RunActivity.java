package com.aaron.runtracker.activities;

import android.support.v4.app.Fragment;

import com.aaron.runtracker.fragments.RunFragment;

/**
 * Created by Aaron on 15/7/8.
 */
public class RunActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return RunFragment.newInstance();
    }
}
