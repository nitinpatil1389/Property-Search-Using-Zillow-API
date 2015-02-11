package org.example.csci571homework9;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter{
	private String JSONResultString;
	public TabsPagerAdapter(FragmentManager fm, String JSONResultString) {
        super(fm);
        this.JSONResultString=JSONResultString;
    }
 
    @Override
    public Fragment getItem(int index) {
    	Bundle bundle=new Bundle();
    	bundle.putString("JSONresponse", JSONResultString);
    	Fragment curFragment = null;
        switch (index) {
        case 0:
        	curFragment = new SearchResultFragment();
        	curFragment.setArguments(bundle);
        	break;
        case 1:
        	curFragment = new ChartsFragment();
        	curFragment.setArguments(bundle);
        	break;
        }
        return curFragment;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }
}
