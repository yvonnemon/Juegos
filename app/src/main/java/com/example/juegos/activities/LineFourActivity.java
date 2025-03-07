package com.example.juegos.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;
import androidx.room.Room;

import com.example.juegos.R;
import com.example.juegos.model.UserGame;
import com.example.juegos.model.UserSettings;
import com.example.juegos.repository.AppDatabase;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

public class LineFourActivity extends AppCompatActivity {
    private static final int ROWS = 6;
    private static final int COLS = 7;
    private boolean isAIEnabled = false; // Default: Player vs Player

    private AppDatabase db;
    private UserSettings settings;
    private boolean blindmode = false;
    private int[][] board = new int[ROWS][COLS]; // 0 = empty, 1 = player 1, 2 = player 2
    private TextView[][] cellViews = new TextView[ROWS][COLS];
    private int currentPlayer = 1; // Player 1 starts
    private int height;
    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line_four_activity);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "game_db").allowMainThreadQueries().build();

        settings = db.gameSettingsDao().getSettings(); // Runs in the background

        if (settings != null) {

            blindmode = settings.colorBlind;
            isAIEnabled = settings.soloPlayer;
            TextView ia = findViewById(R.id.playWithIa);
            ia.setVisibility(View.VISIBLE);
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.height = displayMetrics.heightPixels;
        this.width = displayMetrics.widthPixels;

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        gridLayout.setRowCount(6);
        gridLayout.setColumnCount(7);

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                TextView cell = new TextView(this);
                cell.setBackgroundResource(R.drawable.cell_connect_four);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = width / 9;
                params.height = width / 9;
                params.setMargins(4, 4, 4, 4); // Small margin to separate cells
                cell.setLayoutParams(params);

                gridLayout.addView(cell);
                cellViews[row][col] = cell;
            }
        }

        gridLayout.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                int column = getColumnFromTouch(event.getX());
                if (column != -1) {
                    makeMove(column);
                }
            }
            return true;
        });
    }

    private int getColumnFromTouch(float touchX) {
        GridLayout gridLayout = findViewById(R.id.gridLayout);

        // Get absolute position of GridLayout on the screen
        int[] location = new int[2];
        gridLayout.getLocationOnScreen(location);
        int gridStartX = location[0];  // Leftmost pixel of GridLayout

        // Ensure touch is within grid bounds
        if (touchX < gridStartX) return -1; // Ignore touches outside left edge
        if (touchX > gridStartX + gridLayout.getWidth())
            return -1; // Ignore touches outside right edge

        // Calculate cell width based on screen width
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int cellSize = screenWidth / 9; // Fixed cell width

        // Adjust touch position relative to GridLayout's starting X position
        int relativeTouchX = (int) (touchX - gridStartX);

        // Calculate the column index
        int column = relativeTouchX / cellSize; // Ensure accurate division

        // Ensure column index is within valid bounds
        column = Math.max(0, Math.min(column, COLS - 1));

        return column;
    }


    private void makeMove(int column) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][column] == 0) {
                board[row][column] = currentPlayer;
                updateUI();

                if (checkWin(row, column)) {
                    saveGameScore(currentPlayer);
                    showWinMessage();
                    return;
                }

                switchPlayer();

                // If AI mode is enabled and it's AI's turn, make AI move after delay
                if (isAIEnabled && currentPlayer == 2) {
                    new android.os.Handler().postDelayed(this::aiMove, 500);
                }
                return;
            }
        }
    }

    private boolean checkWin(int row, int col) {
        return checkDirection(row, col, 1, 0) ||  // Horizontal
                checkDirection(row, col, 0, 1) ||  // Vertical
                checkDirection(row, col, 1, 1) ||  // Diagonal \
                checkDirection(row, col, 1, -1);   // Diagonal /
    }

    private void saveGameScore(int player) {
        int userId = getCurrentUserId(); // Fetch user ID

        UserGame userGame = new UserGame();
        userGame.user_id = userId;
        userGame.game_id = 2;

        // añadir un ? de info abajo para que si gana el jugador 1 se le guarda el score al usuario logeado
        userGame.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        userGame.score = player == 1 ? 1 : 0;
        userGame.gameName = "Line Four";

        Executors.newSingleThreadExecutor().execute(() -> {
            db.userGameDao().insertGame(userGame);
            runOnUiThread(() -> Toast.makeText(this, "Score saved!", Toast.LENGTH_SHORT).show());
        });

    }

    private int getCurrentUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1); // Default to -1 if no user is found
    }

    // Checks if there are 4 pieces in a row in a given direction
    private boolean checkDirection(int row, int col, int deltaRow, int deltaCol) {
        int count = 1;

        // Check in the forward direction
        count += countPieces(row, col, deltaRow, deltaCol);
        // Check in the backward direction
        count += countPieces(row, col, -deltaRow, -deltaCol);

        return count >= 4; // Win if 4 or more in a row
    }

    // Helper method to count pieces in a given direction
    private int countPieces(int row, int col, int deltaRow, int deltaCol) {
        int count = 0;
        int player = board[row][col];

        while (true) {
            row += deltaRow;
            col += deltaCol;
            if (row < 0 || row >= ROWS || col < 0 || col >= COLS || board[row][col] != player) {
                break;
            }
            count++;
        }
        return count;
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == 1) ? 2 : 1;
    }

    private void updateUI() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                TextView cell = cellViews[row][col]; // Get the stored reference

                if (board[row][col] == 1) {
                    cell.setBackgroundResource(blindmode ? R.drawable.blind1 : R.drawable.player1_piece);
                } else if (board[row][col] == 2) {
                    cell.setBackgroundResource(blindmode ? R.drawable.blind2 : R.drawable.player2_piece);
                } else {
                    cell.setBackgroundResource(R.drawable.cell_connect_four);
                }
            }
        }
    }


    private void showWinMessage() {
        new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage("Player " + currentPlayer + " wins!")
                .setPositiveButton("Restart", (dialog, which) -> resetGame())
                .setCancelable(false)
                .show();
    }

    private void resetGame() {
        for (int i = 0; i < ROWS; i++) {
            Arrays.fill(board[i], 0);
        }
        currentPlayer = 1;
        updateUI();
    }

    private void aiMove() {
        int column;
        do {
            column = (int) (Math.random() * COLS); // Random column
        } while (!isColumnAvailable(column));

        makeMove(column);
    }

    // Check if a column is not full
    private boolean isColumnAvailable(int column) {
        return board[0][column] == 0; // If the top row is empty, column is available
    }

}
