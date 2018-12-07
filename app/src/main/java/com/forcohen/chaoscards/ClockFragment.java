package com.forcohen.chaoscards;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class ClockFragment extends Fragment {

    Timer clock;
    int clockCounter = 0;
    TextView clockView;

    public ClockFragment() {

        clock = new Timer();
        this.clock = new Timer();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.clock_fragment, container, false);

        clockView = view.findViewById(R.id.clock_view);

        clock.schedule(new TimerTask() {
            @Override
            public void run() {
                clockView.setText(String.valueOf(clockCounter++));
            }
        }, 0, 1000);


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clock.cancel();
    }
}
