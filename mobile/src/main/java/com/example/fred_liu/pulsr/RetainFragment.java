package com.example.fred_liu.pulsr;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;


/**
 * Created by fredliu on 09/27/2018.
 */
public class RetainFragment extends Fragment {
//    private static final String TAG = "RetainFragment";
//    private Bundle mInstanceBundle = null;
//
//    public RetainFragment() { // This will only be called once be cause of setRetainInstance()
//        super();
//        setRetainInstance( true );
//    }
//
//    public RetainFragment pushData( Bundle instanceState )
//    {
//        if ( this.mInstanceBundle == null ) {
//            this.mInstanceBundle = instanceState;
//        }
//        else
//        {
//            this.mInstanceBundle.putAll( instanceState );
//        }
//        return this;
//    }
//
//    public Bundle popData()
//    {
//        Bundle out = this.mInstanceBundle;
//        this.mInstanceBundle = null;
//        return out;
//    }
//
//    public static final RetainFragment getInstance(FragmentManager fragmentManager )
//    {
//        RetainFragment out = (RetainFragment) fragmentManager.findFragmentByTag( TAG );
//
//        if ( out == null )
//        {
//            out = new RetainFragment();
//            fragmentManager.beginTransaction().add( out, TAG ).commit();
//        }
//        return out;
//    }
}
