package com.example.juegos.activities;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;
import androidx.room.Room;

import com.example.juegos.R;
import com.example.juegos.model.UserGame;
import com.example.juegos.model.UserSettings;
import com.example.juegos.repository.AppDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Executors;

//2048

public class MainGameActivity extends AppCompatActivity {
    private final int GRID_SIZE = 4;
    private int[][] cuadricula = new int[GRID_SIZE][GRID_SIZE];
    private AppDatabase db;
    private UserSettings settings;
    private boolean originalTile = false;
    private GestureDetector gestureDetector;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_game_activity);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "game_db").allowMainThreadQueries().build();
        updateHighestScores();

        settings = db.gameSettingsDao().getSettings();

        if (settings != null) {
            System.out.println(settings.toString());
            originalTile = settings.originalTiles;
        }

        addNewTile();
        drawNumbers(cuadricula);
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true; // Required for GestureDetector to work
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                int SWIPE_THRESHOLD = 50; // Min distance for a swipe
                int SWIPE_VELOCITY_THRESHOLD = 50; // Min velocity for a swipe
                // Handle swipe gestures
                float diffX = e2.getX() - e1.getX();
                float diffY = e2.getY() - e1.getY();

                try {
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        // Horizontal swipe
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                        }
                    } else {
                        // Vertical swipe
                        if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffY > 0) {
                                onSwipeDown();
                            } else {
                                onSwipeUp();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        gridLayout.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));

        Button btnSaveScore = findViewById(R.id.btnSaveScore);
        btnSaveScore.setOnClickListener(v -> saveGameScore());
    }
    /*
     * [1,2,3,4],
     * [5,6,7,8],
     * [9,10,11,12],
     * [13,14,15,16]
     * */

    //
    //  METODOS DE MOVIMIENTO
    //
    private void onSwipeRight() {
        int[][] oldGrid = copyGrid(cuadricula);
        cuadricula = moveRight(cuadricula);

        // Animate only the moved tiles
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = GRID_SIZE - 2; j >= 0; j--) { // Right-to-left scan
                if (oldGrid[i][j] != 0) {
                    if (cuadricula[i][j + 1] == oldGrid[i][j] * 2) { // Check if a merge happened
                        animateTileMovement(i, j, i, j + 1, oldGrid[i][j], true);
                    } else if (cuadricula[i][j + 1] == oldGrid[i][j]) {
                        animateTileMovement(i, j, i, j + 1, oldGrid[i][j], false);
                    }
                }
            }
        }

        if (!gridsAreEqual(oldGrid, cuadricula)) {
            addNewTile();
        }
        drawNumbers(cuadricula);
        checkGameOver();
    }

    private void onSwipeLeft() {
        int[][] oldGrid = copyGrid(cuadricula);
        cuadricula = moveLeft(cuadricula);

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 1; j < GRID_SIZE; j++) { // Left-to-right scan
                if (oldGrid[i][j] != 0) {
                    if (cuadricula[i][j - 1] == oldGrid[i][j] * 2) {
                        animateTileMovement(i, j, i, j - 1, oldGrid[i][j], true);
                    } else if (cuadricula[i][j - 1] == oldGrid[i][j]) {
                        animateTileMovement(i, j, i, j - 1, oldGrid[i][j], false);
                    }
                }
            }
        }

        if (!gridsAreEqual(oldGrid, cuadricula)) {
            addNewTile();
        }
        drawNumbers(cuadricula);
        checkGameOver();
    }

    private void onSwipeUp() {
        int[][] oldGrid = copyGrid(cuadricula);
        cuadricula = moveUp(cuadricula);

        for (int j = 0; j < GRID_SIZE; j++) {
            for (int i = 1; i < GRID_SIZE; i++) { // Top-to-bottom scan
                if (oldGrid[i][j] != 0) {
                    if (cuadricula[i - 1][j] == oldGrid[i][j] * 2) {
                        animateTileMovement(i, j, i - 1, j, oldGrid[i][j], true);
                    } else if (cuadricula[i - 1][j] == oldGrid[i][j]) {
                        animateTileMovement(i, j, i - 1, j, oldGrid[i][j], false);
                    }
                }
            }
        }

        if (!gridsAreEqual(oldGrid, cuadricula)) {
            addNewTile();
        }
        drawNumbers(cuadricula);
        checkGameOver();
    }

    private void onSwipeDown() {
        int[][] oldGrid = copyGrid(cuadricula);
        cuadricula = moveDown(cuadricula); //

        for (int j = 0; j < GRID_SIZE; j++) {
            for (int i = GRID_SIZE - 2; i >= 0; i--) { // Bottom-to-top scan
                if (oldGrid[i][j] != 0) {
                    if (cuadricula[i + 1][j] == oldGrid[i][j] * 2) {
                        animateTileMovement(i, j, i + 1, j, oldGrid[i][j], true);
                    } else if (cuadricula[i + 1][j] == oldGrid[i][j]) {
                        animateTileMovement(i, j, i + 1, j, oldGrid[i][j], false);
                    }
                }
            }
        }

        if (!gridsAreEqual(oldGrid, cuadricula)) {
            addNewTile();
        }
        drawNumbers(cuadricula);
        checkGameOver();
    }

    private void checkGameOver() {
        if (!canMove()) {
            showGameOverDialog();
        }
    }

    //
    //  METODOS QUE REALIZAN LA ACCION CON EL GRID
    //

    private int[][] moveLeft(int[][] grid) {
        for (int i = 0; i < GRID_SIZE; i++) {
            grid[i] = slideAndMerge(grid[i]);
        }
        return grid;
    }

    private int[][] moveRight(int[][] grid) {
        for (int i = 0; i < GRID_SIZE; i++) {
            grid[i] = reverse(slideAndMerge(reverse(grid[i])));
        }
        return grid;
    }

    private int[][] moveUp(int[][] grid) {
        grid = rotateCounterclockwise(grid);
        grid = moveLeft(grid);
        return rotateClockwise(grid);
    }

    private int[][] moveDown(int[][] grid) {
        grid = rotateClockwise(grid);
        grid = moveLeft(grid);
        return rotateCounterclockwise(grid);
    }

    //
    // METODOS DE MOVER LOS NUMEROS DE UN LADO A OTRO
    //

    // Slide and merge a row
    private int[] slideAndMerge(int[] row) {
        row = slide(row);
        row = merge(row);
        row = slide(row);
        return row;
    }

    // Slide non-zero values to the left
    private int[] slide(int[] row) {
        int[] newRow = new int[row.length];
        int index = 0;
        for (int value : row) {
            if (value != 0) {
                newRow[index++] = value;
            }
        }
        return newRow;
    }

    // Merge adjacent tiles
    private int[] merge(int[] row) {
        for (int i = 0; i < row.length - 1; i++) {
            if (row[i] != 0 && row[i] == row[i + 1]) {
                row[i] *= 2;
                row[i + 1] = 0;
                score += row[i];
                updateScoreUI();
            }
        }
        return row;
    }

    // Reverse a row
    private int[] reverse(int[] row) {
        int[] reversed = new int[row.length];
        for (int i = 0; i < row.length; i++) {
            reversed[i] = row[row.length - i - 1];
        }
        return reversed;
    }

    //se le pasa el array[][] por parametro para invertirlo de arribabajo
    private int[][] rotateClockwise(int[][] grid) {
        int n = grid.length;
        int[][] rotated = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rotated[j][n - i - 1] = grid[i][j];
            }
        }
        return rotated;
    }

    private int[][] rotateCounterclockwise(int[][] grid) {
        int n = grid.length;
        int[][] rotated = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rotated[n - j - 1][i] = grid[i][j];
            }
        }
        return rotated;
    }

    //
    // METODOS DE USO
    //
    private void addNewTile() {
        List<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (cuadricula[i][j] == 0) {
                    emptyCells.add(new int[]{i, j});
                }
            }
        }
        if (emptyCells.isEmpty()) {
            if (!canMove()) {
                showGameOverDialog();
                return;
            }
        }
        int[] cell = emptyCells.get(new Random().nextInt(emptyCells.size()));
        cuadricula[cell[0]][cell[1]] = Math.random() < 0.9 ? 2 : 4;
    }

    private boolean canMove() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (cuadricula[i][j] == 0) {
                    System.out.println("move");
                    return true; // If there’s an empty cell, moves are possible
                }
                if (j < GRID_SIZE - 1 && cuadricula[i][j] == cuadricula[i][j + 1]) {
                    System.out.println("move horizontal");
                    return true; // Horizontal merge is possible
                }
                if (i < GRID_SIZE - 1 && cuadricula[i][j] == cuadricula[i + 1][j]) {
                    System.out.println("move vertical");
                    return true; // Vertical merge is possible
                }
            }
        }
        return false; // No moves left
    }

    private void showGameOverDialog() {
        saveGameScore();
        runOnUiThread(() -> {
            new AlertDialog.Builder(this)
                    .setTitle("Game Over")
                    .setMessage("No more moves available!")
                    .setPositiveButton("OK", (dialog, which) -> restartGame())
                    .setNegativeButton("Exit", (dialog, which) -> finish())
                    .setCancelable(false)
                    .show();
        });
    }

    private int[][] copyGrid(int[][] grid) {
        int[][] copy = new int[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            System.arraycopy(grid[i], 0, copy[i], 0, GRID_SIZE);
        }
        return copy;
    }

    private boolean gridsAreEqual(int[][] grid1, int[][] grid2) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid1[i][j] != grid2[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private void drawNumbers(int[][] grid) {
        int nro = 0;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                nro++;
                int resId = getResources().getIdentifier("textView" + nro, "id", getPackageName());
                TextView textView = findViewById(resId);

                if (textView != null) {
                    textView.setBackgroundTintList(setTileColor(grid[i][j]));
                    textView.setTypeface(null, Typeface.BOLD);
                    if (grid[i][j] != 0) {
                        textView.setText(String.valueOf(grid[i][j]));
                        if (grid[i][j] == 2 || grid[i][j] == 4) { // New tiles usually spawn as 2 or 4
                            textView.setScaleX(0f);
                            textView.setScaleY(0f);
                            textView.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setInterpolator(new android.view.animation.OvershootInterpolator()) // Smooth pop effect
                                    .setDuration(150)
                                    .start();
                        }
                        if (grid[i][j] == 2048) {
                            showWinDialog();
                        }
                    } else {
                        textView.setText(""); // Clear the TextView for zero values
                    }
                } else {
                    Log.e("drawNumbers", "TextView with ID textView" + nro + " not found.");
                }
            }
        }
    }

    private void showWinDialog() {
        runOnUiThread(() -> {
            new AlertDialog.Builder(this)
                    .setTitle("Congratulations!")
                    .setMessage("You reached 2048!")
                    .setPositiveButton("Continue", (dialog, which) -> dialog.dismiss())
                    .setNegativeButton("Restart", (dialog, which) -> restartGame())
                    .show();
        });
    }

    private void restartGame() {
        score = 0;
        cuadricula = new int[GRID_SIZE][GRID_SIZE];
        addNewTile();
        addNewTile();
        updateScoreUI();
        drawNumbers(cuadricula);
    }

    private void updateScoreUI() {
        TextView scoreView = findViewById(R.id.scoreTextView);
        scoreView.setText("Score: " + score);
    }

    private void updateHighestScores() {
        if (getCurrentUserId() == -1) {
            Toast.makeText(this, "User not found! Please log in.", Toast.LENGTH_SHORT).show();
        }
        int userId = getCurrentUserId(); // Get user ID from SharedPreferences or session
        int userBest = db.userGameDao().getUserHighestScore(userId);
        int globalBest = db.userGameDao().getHighestOverallScore();

        TextView highestScoreView = findViewById(R.id.highestScoreTextView);
        highestScoreView.setText("Highest Score: " + Math.max(userBest, globalBest));
    }

    private int getCurrentUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1); // Default to -1 if no user is found
    }

    private void saveGameScore() {
        int userId = getCurrentUserId(); // Fetch user ID

        UserGame userGame = new UserGame();
        userGame.user_id = userId;

        userGame.game_id = 1;
        userGame.score = score;
        userGame.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        userGame.gameName = "2048";

        Executors.newSingleThreadExecutor().execute(() -> {
            db.userGameDao().insertGame(userGame);
            runOnUiThread(() -> Toast.makeText(this, "Score saved!", Toast.LENGTH_SHORT).show());
        });
        updateHighestScores();
    }

    private ColorStateList setTileColor(int tileNumber) {

        System.out.println("originalTile " + originalTile);

        switch (tileNumber) {
            case 2:
                return getResources().getColorStateList(originalTile ? R.color.legit2 : R.color.celesteclaro, null);

            case 4:
                return getResources().getColorStateList(originalTile ? R.color.legit4 : R.color.game4, null);
            // return getResources().getColorStateList(R.color.game4, null);

            case 8:
                return getResources().getColorStateList(originalTile ? R.color.legit8 : R.color.game8, null);
            //return getResources().getColorStateList(R.color.game8, null);

            case 16:
                return getResources().getColorStateList(originalTile ? R.color.legit16 : R.color.game16, null);
            //return getResources().getColorStateList(R.color.game16, null);

            case 32:
                return getResources().getColorStateList(originalTile ? R.color.legit32 : R.color.game32, null);
            //return getResources().getColorStateList(R.color.game32, null);

            case 64:
                return getResources().getColorStateList(originalTile ? R.color.legit64 : R.color.game64, null);
            //return getResources().getColorStateList(R.color.game64, null);

            case 128:
                return getResources().getColorStateList(originalTile ? R.color.legit128 : R.color.game128, null);
            //return getResources().getColorStateList(R.color.game128, null);

            case 256:
                return getResources().getColorStateList(originalTile ? R.color.legit256 : R.color.game256, null);
            //return getResources().getColorStateList(R.color.game256, null);

            case 512:
                return getResources().getColorStateList(originalTile ? R.color.legit512 : R.color.game512, null);
            //return getResources().getColorStateList(R.color.game512, null);

            case 1024:
                //return getResources().getColorStateList(R.color.game1024, null);
                return getResources().getColorStateList(originalTile ? R.color.legit1024 : R.color.game1024, null);

            case 2048:
                return getResources().getColorStateList(originalTile ? R.color.legit2048 : R.color.game2048, null);
            //return getResources().getColorStateList(R.color.game2048, null);

            default:
                return getResources().getColorStateList(R.color.celesteclaro, null);
        }

        // return getResources().getColorStateList(R.color.pink, null);
    }

    //
    // FAKE MOVEMENT STUFF
    //
    private void animateTileMovement(int fromRow, int fromCol, int toRow, int toCol, int value, boolean merging) {
        FrameLayout overlay = findViewById(R.id.overlay);

        TextView fakeTile = new TextView(this);
        fakeTile.setText(String.valueOf(value));
        fakeTile.setTextSize(24);
        fakeTile.setGravity(Gravity.CENTER);
        fakeTile.setTypeface(null, Typeface.BOLD);
        fakeTile.setBackgroundTintList(setTileColor(value));

        int tileSize = getResources().getDimensionPixelSize(R.dimen.tile_size);
        int fromX = getTileX(fromCol);
        int fromY = getTileY(fromRow);
        int toX = getTileX(toCol);
        int toY = getTileY(toRow);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(tileSize, tileSize);
        params.leftMargin = fromX;
        params.topMargin = fromY;
        fakeTile.setLayoutParams(params);
        overlay.addView(fakeTile);

        // Animate movement with a slight delay for merges
        fakeTile.animate()
                .translationX(toX - fromX)
                .translationY(toY - fromY)
                .setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator())
                .setDuration(500)
                .withEndAction(() -> {
                    if (merging) {
                        fakeTile.animate()
                                .scaleX(5f)
                                .scaleY(5f)
                                .setDuration(200)
                                .withEndAction(() -> overlay.removeView(fakeTile))
                                .start();
                    } else {
                        overlay.removeView(fakeTile);
                    }
                })
                .start();
    }


    private int getTileX(int col) {
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        int[] location = new int[2];
        gridLayout.getLocationOnScreen(location); // Get top-left corner of GridLayout
        int gridX = location[0];

        int tileSize = gridLayout.getWidth() / GRID_SIZE; // Dynamic tile size
        return gridX + (col * tileSize);
    }

    private int getTileY(int row) {
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        int[] location = new int[2];
        gridLayout.getLocationOnScreen(location);
        int gridY = location[1];

        int tileSize = gridLayout.getHeight() / GRID_SIZE;
        return gridY + (row * tileSize);
    }
}

