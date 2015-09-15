package com.quandoo.assignment.gameoflife;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.quandoo.assignment.gameoflife.views.BoardGridView;

public class MainActivity extends Activity {

    private FloatingActionButton startStopButton;
    private BoardGridView gridView;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        gridView = (BoardGridView) findViewById(R.id.grid_view);

        startStopButton = (FloatingActionButton) findViewById(R.id.start_stop_btn);
        startStopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (gridView.isRunning()) {
                    gridView.setMode(BoardGridView.PAUSE);
                    startStopButton.setImageResource(android.R.drawable.ic_media_play);
                } else {
                    gridView.setMode(BoardGridView.RUNNING);
                    startStopButton.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        // stop and remove animation thread
        super.onDestroy();
        gridView.setMode(BoardGridView.PAUSE);
        startStopButton.setImageResource(android.R.drawable.ic_media_play);
    }

}