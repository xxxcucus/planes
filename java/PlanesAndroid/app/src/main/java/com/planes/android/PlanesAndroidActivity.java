package com.planes.android;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.planes.common.PlanesRoundJava;

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

        m_PlaneRound = new PlanesRoundJava();
        m_PlaneRound.createPlanesRound();

        m_PlanesLayout = (PlanesVerticalLayout)findViewById(R.id.planes_layout);

        boolean isTablet = false;
        boolean isHorizontal = false;
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.rootView);
        if (linearLayout.getTag().toString().contains("tablet")) {
            isTablet = true;
        }

        if (linearLayout.getTag().toString().contains("horizontal")) {
            isHorizontal = true;
        }

        if (isTablet) {
            GameBoard playerBoard = (GameBoard)findViewById(R.id.player_board);
            playerBoard.setGameSettings(m_PlaneRound, true);
            playerBoard.setPlayerBoard();
            GameBoard computerBoard = (GameBoard)findViewById(R.id.computer_board);

            computerBoard.setGameSettings(m_PlaneRound, true);
            computerBoard.setComputerBoard();
            m_GameBoards = new GameBoardsAdapter(playerBoard, computerBoard);
        } else {
            GameBoard gameBoard = (GameBoard)findViewById(R.id.game_boards);
            gameBoard.setGameSettings(m_PlaneRound, false);
            m_GameBoards = new GameBoardsAdapter(gameBoard);
        }

        //Board Editing Buttons
        Button upButton = (Button)findViewById(R.id.up_button);
        Button downButton = (Button)findViewById(R.id.down_button);
        Button leftButton = (Button)findViewById(R.id.left_button);
        Button rightButton = (Button)findViewById(R.id.right_button);
        Button doneButton = (Button)findViewById(R.id.done_button);
        Button rotateButton = (Button)findViewById(R.id.rotate_button);

        //Game Stage
        ColouredSurfaceWithText statsTitle = (ColouredSurfaceWithText)findViewById(R.id.stats_title_label);
        TwoLineTextButtonWithState viewComputerBoardButton1 = (TwoLineTextButtonWithState)findViewById(R.id.view_computer_board1);
        ColouredSurfaceWithText movesLabel = (ColouredSurfaceWithText)findViewById(R.id.moves_label);
        ColouredSurfaceWithText movesCount = (ColouredSurfaceWithText)findViewById(R.id.moves_count);
        ColouredSurfaceWithText missesLabel = (ColouredSurfaceWithText)findViewById(R.id.misses_label);
        ColouredSurfaceWithText missesCount = (ColouredSurfaceWithText)findViewById(R.id.misses_count);
        ColouredSurfaceWithText hitsLabel = (ColouredSurfaceWithText)findViewById(R.id.hits_label);
        ColouredSurfaceWithText hitsCount = (ColouredSurfaceWithText)findViewById(R.id.hits_count);
        ColouredSurfaceWithText deadsLabel = (ColouredSurfaceWithText)findViewById(R.id.dead_label);
        ColouredSurfaceWithText deadCount = (ColouredSurfaceWithText)findViewById(R.id.dead_count);

        //Start New Game Stage
        TwoLineTextButtonWithState viewComputerBoardButton2 = (TwoLineTextButtonWithState)findViewById(R.id.view_computer_board2);
        TwoLineTextButton startNewGameButton = (TwoLineTextButton)findViewById(R.id.start_new_game);
        ColouredSurfaceWithText computerWinsLabel = (ColouredSurfaceWithText)findViewById(R.id.computer_wins_label);
        ColouredSurfaceWithText computerWinsCount = (ColouredSurfaceWithText)findViewById(R.id.computer_wins_count);
        ColouredSurfaceWithText playerWinsLabel = (ColouredSurfaceWithText)findViewById(R.id.player_wins_label);
        ColouredSurfaceWithText playerWinsCount = (ColouredSurfaceWithText)findViewById(R.id.player_wins_count);
        ColouredSurfaceWithText winnerText = (ColouredSurfaceWithText)findViewById(R.id.winner_textview);

        m_GameControls = new GameControlsAdapter(this);
        m_GameControls.setBoardEditingControls(upButton, downButton, leftButton, rightButton, doneButton, rotateButton);
        if (!isTablet)
            m_GameControls.setGameControls(statsTitle, viewComputerBoardButton1, movesLabel, movesCount, missesLabel, missesCount, hitsLabel, hitsCount, deadsLabel, deadCount);
        m_GameControls.setStartNewGameControls(viewComputerBoardButton2, startNewGameButton, computerWinsLabel, computerWinsCount, playerWinsLabel, playerWinsCount, winnerText);

        m_GameControls.setGameSettings(m_PlaneRound, isTablet);
        m_GameControls.setGameBoards(m_GameBoards);
        m_GameControls.setPlanesLayout(m_PlanesLayout);
        m_GameBoards.setGameControls(m_GameControls);

        //TODO: should I recreate the plane round here and destroy in onSaveInstanceState?
        //TODO: enum coded differently than in GameStages - to correct
        switch(m_PlaneRound.getGameStage()) {
            case 0:
                m_GameBoards.setNewRoundStage();
                m_GameControls.setNewRoundStage();
                m_PlanesLayout.setComputerBoard();
                m_PlanesLayout.setNewRoundStage();

                break;
            case 1:
                m_GameBoards.setBoardEditingStage();
                m_GameControls.setBoardEditingStage();
                m_PlanesLayout.setBoardEditingStage();
                break;
            case 2:
                m_GameBoards.setGameStage();
                m_GameControls.setGameStage();
                m_PlanesLayout.setGameStage();
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
                m_GameBoard.setNewRoundStage();
                m_GameControls.setNewRoundStage();
                break;
            case 1:
                m_GameBoard.setBoardEditingStage();
                m_GameControls.setBoardEditingStage();
                break;
            case 2:
                m_GameBoard.setGameStage();
                m_GameControls.setGameStage();
                break;
        }*/

        Log.d("Planes","onRestoreInstanceState");
    }

    public void onButtonShowHelpWindowClick() {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.help_popup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(m_PlanesLayout, Gravity.CENTER, 0, 0);

        TextView helpTextView = (TextView)popupView.findViewById(R.id.popup_help_text);
        TextView helpTitleTextView = (TextView)popupView.findViewById(R.id.popup_help_title);

        if (helpTextView != null && helpTitleTextView != null) {
            switch (m_GameBoards.getGameStage()) {
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
                    helpTextView.setText("\nTouch on the computer's board to guess where the computer's planes are." +
                            "\nX means target destroyed. Disc means nothing was hit. Rhombus means a hit.");
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

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(m_PlanesLayout, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    private PlanesRoundInterface m_PlaneRound;
    private GameBoardsAdapter m_GameBoards;
    private GameControlsAdapter m_GameControls;
    private PlanesVerticalLayout m_PlanesLayout;
}
