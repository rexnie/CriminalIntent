package com.bignerdranch.android.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by niedaocai on 16-6-14.
 */
public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        //return R.layout.activity_twopane;
        return R.layout.activity_fragment;
    }
}
