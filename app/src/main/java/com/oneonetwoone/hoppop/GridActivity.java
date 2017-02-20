package com.oneonetwoone.hoppop;

import android.support.v4.app.Fragment;

public class GridActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){return GridFragment.newInstance();}
}
