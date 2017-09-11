package com.example.aluminumiron.mydrawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by aluminumiron on 21/08/2017.
 */

public class MainValve extends Fragment{

    private LayoutInflater layoutInflater;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){

        return inflater.inflate(R.layout.fragment_valve, (ViewGroup) container, false);
    }

}
