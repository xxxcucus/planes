package com.planes.android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.planes.javafx.PlaneRoundJavaFx;


//bottom pane in a vertical layout
public class BottomPane_Vertical extends GridLayout {

    public enum GameStages {
        GameNotStarted, BoardEditing, Game
    }

    public BottomPane_Vertical(Context context) {
        super(context);
        //setGameSettings(nrows, ncols, nplanes);
        //m_PlaneRound = planeRound;
        m_Context = context;
    }

    public BottomPane_Vertical(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setGameSettings(nrows, ncols, nplanes);
        //m_PlaneRound = planeRound;
        m_Context = context;
    }

    public BottomPane_Vertical(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //setGameSettings(nrows, ncols, nplanes);
        //m_PlaneRound = planeRound;
        m_Context = context;
    }

    private void init(GameStages gameStage) {
        m_CurStage = gameStage;
        resetGUI();

        if (m_CurStage == GameStages.BoardEditing)
            showBoardEditing();

        if (m_CurStage == GameStages.Game)
            showGame();

        if (m_CurStage == GameStages.GameNotStarted)
            showGameNotStarted();
    }

    public void setGameStage() {
        init(GameStages.Game);
    }

    public void setBoardEditingStage() {
        init(GameStages.BoardEditing);
    }

    public void setNewRoundStage() {
        init(GameStages.GameNotStarted);
    }

    public void setGameSettings(PlaneRoundJavaFx planeRound) {
        m_PlaneRound = planeRound;
        resetGUI();
        init(GameStages.BoardEditing);
    }

    public void setTopPane(TopPane_Vertical top) {
        m_TopPane = top;
    }

    private void resetGUI() {
        removeAllViews();

        m_RotateButton = null;
        m_LeftButton = null;
        m_RightButton = null;
        m_UpButton = null;
        m_DownButton = null;
        m_DoneButton = null;

        m_ViewPlayerBoardButton = null;
        m_ViewComputerBoardButton = null;
        m_HitsTextView = null;
        m_DeadTextView = null;
        m_MissesTextView = null;
        m_MovesTextView = null;
        m_HitsLabel = null;
        m_DeadLabel = null;
        m_MissesLabel = null;
        m_MovesLabel = null;

        m_ComputerWins = null;
        m_PlayerWins = null;
        m_StartNewRound = null;
        m_WinnerTextView = null;

    }

    private void showBoardEditing() {
        LayoutInflater layoutinflater = (LayoutInflater)m_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View window = layoutinflater.inflate(R.layout.board_editing_controls_vertical, null);
        setRowCount(1);
        setColumnCount(1);
        GridLayout.LayoutParams paramsWindow = new GridLayout.LayoutParams(GridLayout.spec(0, 1), GridLayout.spec(0, 1));
        paramsWindow.setGravity(Gravity.CENTER);
        addView(window, paramsWindow);
        m_RotateButton = (Button)findViewById(R.id.rotate_button);
        m_LeftButton = (Button)findViewById(R.id.left_button);
        m_RightButton = (Button)findViewById(R.id.right_button);
        m_UpButton = (Button)findViewById(R.id.up_button);
        m_DownButton = (Button)findViewById(R.id.down_button);
        m_DoneButton = (Button)findViewById(R.id.done_button);

        m_LeftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                m_TopPane.movePlaneLeft();
            }
        });

        m_RightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                m_TopPane.movePlaneRight();
            }
        });

        m_UpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                m_TopPane.movePlaneUp();
            }
        });

        m_DownButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                m_TopPane.movePlaneDown();
            }
        });

        m_RotateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                m_TopPane.rotatePlane();
            }
        });

        m_DoneButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                init(GameStages.Game);
                m_TopPane.setGameStage();
                m_PlaneRound.doneClicked();
            }
        });
    }

    private void showGame() {
        LayoutInflater layoutinflater = (LayoutInflater) m_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View window = layoutinflater.inflate(R.layout.game_stats_vertical, null);
        setRowCount(1);
        setColumnCount(1);
        GridLayout.LayoutParams paramsWindow = new GridLayout.LayoutParams(GridLayout.spec(0, 1), GridLayout.spec(0, 1));
        paramsWindow.setGravity(Gravity.CENTER);
        paramsWindow.setMargins(0, 0, 0,0);
        addView(window, paramsWindow);

        m_ViewPlayerBoardButton = (Button)findViewById(R.id.view_player_board1);
        m_ViewPlayerBoardButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                m_TopPane.setPlayerBoard();
                m_ViewComputerBoardButton.setEnabled(true);
                m_ViewPlayerBoardButton.setEnabled(false);

                m_HitsLabel.setText(getResources().getString(R.string.computer_hits));
                m_DeadLabel.setText(getResources().getString(R.string.computer_dead));
                m_MissesLabel.setText(getResources().getString(R.string.computer_misses));
                m_MovesLabel.setText(getResources().getString(R.string.computer_moves));
                int misses = m_PlaneRound.playerGuess_StatNoComputerMisses();
                int hits = m_PlaneRound.playerGuess_StatNoComputerHits();
                int dead = m_PlaneRound.playerGuess_StatNoComputerDead();
                int moves = m_PlaneRound.playerGuess_StatNoComputerMoves();
                m_MissesTextView.setText(Integer.toString(misses));
                m_HitsTextView.setText(Integer.toString(hits));
                m_DeadTextView.setText(Integer.toString(dead));
                m_MovesTextView.setText(Integer.toString(moves));
            }
        });
        m_ViewComputerBoardButton = (Button)findViewById(R.id.view_computer_board1);
        m_ViewComputerBoardButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                m_TopPane.setComputerBoard();
                m_ViewComputerBoardButton.setEnabled(false);
                m_ViewPlayerBoardButton.setEnabled(true);

                m_HitsLabel.setText(getResources().getString(R.string.player_hits));
                m_DeadLabel.setText(getResources().getString(R.string.player_dead));
                m_MissesLabel.setText(getResources().getString(R.string.player_misses));
                m_MovesLabel.setText(getResources().getString(R.string.player_moves));
                int misses = m_PlaneRound.playerGuess_StatNoPlayerMisses();
                int hits = m_PlaneRound.playerGuess_StatNoPlayerHits();
                int dead = m_PlaneRound.playerGuess_StatNoPlayerDead();
                int moves = m_PlaneRound.playerGuess_StatNoPlayerMoves();
                m_MissesTextView.setText(Integer.toString(misses));
                m_HitsTextView.setText(Integer.toString(hits));
                m_DeadTextView.setText(Integer.toString(dead));
                m_MovesTextView.setText(Integer.toString(moves));
          }
        });

        m_HitsTextView = (TextView)findViewById(R.id.hits_count);
        m_DeadTextView = (TextView)findViewById(R.id.dead_count);
        m_MissesTextView = (TextView)findViewById(R.id.misses_count);
        m_MovesTextView = (TextView)findViewById(R.id.moves_count);

        m_HitsLabel = (TextView)findViewById(R.id.hits_label);
        m_DeadLabel = (TextView)findViewById(R.id.dead_label);
        m_MissesLabel = (TextView)findViewById(R.id.misses_label);
        m_MovesLabel = (TextView)findViewById(R.id.moves_label);

        m_TopPane.setComputerBoard();
        m_ViewComputerBoardButton.setEnabled(false);
        m_ViewPlayerBoardButton.setEnabled(true);
        if (m_ViewComputerBoardButton.isEnabled())
            updateStats(false);
        else
            updateStats(true);
    }

    private void showGameNotStarted() {
        LayoutInflater layoutinflater = (LayoutInflater) m_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View window = layoutinflater.inflate(R.layout.start_new_game_vertical, null);
        setRowCount(1);
        setColumnCount(1);
        GridLayout.LayoutParams paramsWindow = new GridLayout.LayoutParams(GridLayout.spec(0, 1), GridLayout.spec(0, 1));
        paramsWindow.setGravity(Gravity.CENTER);
        addView(window, paramsWindow);

        m_ComputerWins = (TextView)findViewById(R.id.computer_wins_count);
        m_PlayerWins = (TextView)findViewById(R.id.player_wins_count);
        m_StartNewRound = (Button)findViewById(R.id.start_new_game);
        m_StartNewRound.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                m_PlaneRound.initRound();
                m_TopPane.setBoardEditingStage();
                init(GameStages.BoardEditing);
            }
        });
        m_WinnerTextView = (TextView)findViewById(R.id.winner_textview);

        m_ViewPlayerBoardButton = (Button)findViewById(R.id.view_player_board2);
        m_ViewComputerBoardButton = (Button)findViewById(R.id.view_computer_board2);

        m_ViewPlayerBoardButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                m_TopPane.setPlayerBoard();
                m_ViewComputerBoardButton.setEnabled(true);
                m_ViewPlayerBoardButton.setEnabled(false);
            }
        });
        m_ViewComputerBoardButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                m_TopPane.setComputerBoard();
                m_ViewComputerBoardButton.setEnabled(false);
                m_ViewPlayerBoardButton.setEnabled(true);
            }
        });

        int computer_wins = m_PlaneRound.playerGuess_StatNoComputerWins();
        int player_wins = m_PlaneRound.playerGuess_StatNoPlayerWins();
        m_PlayerWins.setText(Integer.toString(player_wins));
        m_ComputerWins.setText(Integer.toString(computer_wins));
    }

        public void updateStats(boolean isComputer) {
        if (isComputer) {
            int misses = m_PlaneRound.playerGuess_StatNoPlayerMisses();
            int hits = m_PlaneRound.playerGuess_StatNoPlayerHits();
            int dead = m_PlaneRound.playerGuess_StatNoPlayerDead();
            int moves = m_PlaneRound.playerGuess_StatNoPlayerMoves();
            m_MissesTextView.setText(Integer.toString(misses));
            m_HitsTextView.setText(Integer.toString(hits));
            m_DeadTextView.setText(Integer.toString(dead));
            m_MovesTextView.setText(Integer.toString(moves));
        } else {
            int misses = m_PlaneRound.playerGuess_StatNoComputerMisses();
            int hits = m_PlaneRound.playerGuess_StatNoComputerHits();
            int dead = m_PlaneRound.playerGuess_StatNoComputerDead();
            int moves = m_PlaneRound.playerGuess_StatNoComputerMoves();
            m_MissesTextView.setText(Integer.toString(misses));
            m_HitsTextView.setText(Integer.toString(hits));
            m_DeadTextView.setText(Integer.toString(dead));
            m_MovesTextView.setText(Integer.toString(moves));
        }
    }

    public void roundEnds(int playerWins, int computerWins, boolean isComputerWinner) {
        init(GameStages.GameNotStarted);
        if (isComputerWinner)
            m_WinnerTextView.setText(getResources().getText(R.string.computer_winner));
        else
            m_WinnerTextView.setText(getResources().getText(R.string.player_winner));

        int computer_wins = m_PlaneRound.playerGuess_StatNoComputerWins();
        int player_wins = m_PlaneRound.playerGuess_StatNoPlayerWins();
        m_PlayerWins.setText(Integer.toString(player_wins));
        m_ComputerWins.setText(Integer.toString(computer_wins));

    }

    private Context m_Context;
    private GameStages m_CurStage = GameStages.BoardEditing;
    private PlaneRoundJavaFx m_PlaneRound;
    private Button m_RotateButton;
    private Button m_LeftButton;
    private Button m_RightButton;
    private Button m_UpButton;
    private Button m_DownButton;
    private Button m_DoneButton;

    private TextView m_HitsTextView;
    private TextView m_MissesTextView;
    private TextView m_DeadTextView;
    private TextView m_MovesTextView;
    private TextView m_HitsLabel;
    private TextView m_MissesLabel;
    private TextView m_DeadLabel;
    private TextView m_MovesLabel;
    private Button m_ViewPlayerBoardButton;
    private Button m_ViewComputerBoardButton;

    private TextView m_WinnerTextView;
    private Button m_StartNewRound;
    private TextView m_ComputerWins;
    private TextView m_PlayerWins;

    private TopPane_Vertical m_TopPane;
}
