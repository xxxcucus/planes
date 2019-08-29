package com.planes.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.planes.javafx.PlaneRoundJavaFx;

public class PlanesAndroidActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_planes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_help) {
            onButtonShowHelpWindowClick();
            return true;
        } else if (id == R.id.menu_credits) {
            onButtonShowCreditsWindowClick();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        m_PlaneRound = new PlaneRoundJavaFx();
        m_PlaneRound.createPlanesRound();

        boolean isVertical = false;
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.rootView);
        if (linearLayout.getTag().toString().equals("horizontal")) {
            isVertical = false;
        } else if (linearLayout.getTag().toString().equals("vertical")) {
            isVertical = true;
        } else {
            Log.e("Planes", linearLayout.getTag().toString());
            //this.finishAffinity();
        }

        m_BoardWidgets = new BoardWidgets();
        boolean isTablet = m_BoardWidgets.init(this);
        m_BoardWidgets.setGameSettings(m_PlaneRound);

        m_GameControls = (BottomPane_Vertical)findViewById(R.id.bottom_pane);
        m_GameControls.setGameSettings(m_PlaneRound, isTablet, isVertical);
        m_GameControls.setBoardWidgets(m_BoardWidgets);
        m_BoardWidgets.setBoardControls(m_GameControls);

        switch(m_PlaneRound.getGameStage()) {
            case 0:
                m_BoardWidgets.setNewRoundStage();
                m_GameControls.setNewRoundStage();
                break;
            case 1:
                m_BoardWidgets.setBoardEditingStage();
                m_GameControls.setBoardEditingStage();
                break;
            case 2:
                m_BoardWidgets.setGameStage();
                m_GameControls.setGameStage();
                break;
        }


        Log.d("Planes", "onCreate");
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Log.d("Planes", "onStart");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d("Planes", "onResume");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d("Planes", "onPause");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.d("Planes", "onStop");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d("Planes", "onDestroy");

        if (isFinishing()) {
            // do stuff
            Log.d("Planes", "isFinishing");
        } else {
            Log.d("Planes", "orientationChange");
        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        /*switch(m_PlaneRound.getGameStage()) {
            case 0:
                m_BoardWidgetsPhone.setNewRoundStage();
                m_GameControls.setNewRoundStage();
                break;
            case 1:
                m_BoardWidgetsPhone.setBoardEditingStage();
                m_GameControls.setBoardEditingStage();
                break;
            case 2:
                m_BoardWidgetsPhone.setGameStage();
                m_GameControls.setGameStage();
                break;
        }*/

        Log.d("Planes","onRestoreInstanceState");
    }

    public void onButtonShowHelpWindowClick() {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.help_popup, null);

        LinearLayout main_layout = (LinearLayout)findViewById(R.id.main_layout);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(main_layout, Gravity.CENTER, 0, 0);

        TextView helpTextView = (TextView)popupView.findViewById(R.id.popup_help_text);
        TextView helpTitleTextView = (TextView)popupView.findViewById(R.id.popup_help_title);

        if (helpTextView != null && helpTitleTextView != null) {
            switch (m_GameControls.getGameStage()) {
                case GameNotStarted:
                    helpTitleTextView.setText(getResources().getString(R.string.game_not_started_stage));
                    helpTextView.setText("Touch on the \"Start New Game\" to start a new round.");
                    break;
                case BoardEditing:
                    helpTitleTextView.setText(getResources().getString(R.string.board_editing_stage));
                    helpTextView.setText("Touch on the plane's body to select it." +
                            "\nTouch on the control buttons to position the selected plane.");
                    break;
                case Game:
                    helpTitleTextView.setText(getResources().getString(R.string.game_stage));
                    helpTextView.setText("Touch on the \"View Computer Board\" to see the computer's board. " +
                            "\nTouch on the computer's board to guess where the computer's planes are." +
                            "\nTo view the computer's progress touch on the \"View Player Board\"." +
                            "\nX means target destroyed. Disc means nothing was hit. Square means a hit.");
                    break;
            }
        }

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public void onButtonShowCreditsWindowClick() {
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.credits_popup, null);

        LinearLayout main_layout = (LinearLayout)findViewById(R.id.main_layout);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(main_layout, Gravity.CENTER, 0, 0);
    }

    private PlaneRoundJavaFx m_PlaneRound;
    private BottomPane_Vertical m_GameControls;
    private BoardWidgets m_BoardWidgets;
}
