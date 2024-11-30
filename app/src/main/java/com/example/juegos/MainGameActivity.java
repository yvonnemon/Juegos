package com.example.juegos;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.gridlayout.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//esta es la de los juegos

public class MainGameActivity extends AppCompatActivity {
    private GestureDetector gestureDetector;
    private final int GRID_SIZE = 4;
    private int[][] cuadricula = new int[GRID_SIZE][GRID_SIZE];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_game_activity);
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
        System.out.println("derecha");
        int[][] oldGrid = copyGrid(cuadricula);
        cuadricula = moveRight(cuadricula);
        if (!gridsAreEqual(oldGrid, cuadricula)) {
            addNewTile();
        }
        drawNumbers(cuadricula);
    }

    private void onSwipeLeft() {
        System.out.println("izquierda");

        int[][] oldGrid = copyGrid(cuadricula);
        cuadricula = moveLeft(cuadricula);
        if (!gridsAreEqual(oldGrid, cuadricula)) {
            addNewTile();
        }
        drawNumbers(cuadricula);
    }

    private void onSwipeUp() {
        System.out.println("arriba");

        int[][] oldGrid = copyGrid(cuadricula);
        cuadricula = moveUp(cuadricula);
        if (!gridsAreEqual(oldGrid, cuadricula)) {
            addNewTile();
        }
        drawNumbers(cuadricula);
    }

    private void onSwipeDown() {
        System.out.println("abajo");

        int[][] oldGrid = copyGrid(cuadricula);
        cuadricula = moveDown(cuadricula);
        if (!gridsAreEqual(oldGrid, cuadricula)) {
            addNewTile();
        }
        drawNumbers(cuadricula);
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

    //
    // METODOS DE MOVER LOS NUMEROS DE UN LADO A OTRO
    //
    private int[][] moveDown(int[][] grid) {
        grid = rotateClockwise(grid);
        grid = moveLeft(grid);
        return rotateCounterclockwise(grid);
    }
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

    // METODOS DE USO
    private void addNewTile() {
        List<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (cuadricula[i][j] == 0) {
                    emptyCells.add(new int[] {i, j});
                }
            }
        }
        if (emptyCells.isEmpty()) return;
        int[] cell = emptyCells.get(new Random().nextInt(emptyCells.size()));
        cuadricula[cell[0]][cell[1]] = Math.random() < 0.9 ? 2 : 4;
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
                    if (grid[i][j] != 0) {
                        textView.setText(String.valueOf(grid[i][j]));
                    } else {
                        textView.setText(""); // Clear the TextView for zero values
                    }
                } else {
                    Log.e("drawNumbers", "TextView with ID textView" + nro + " not found.");
                }
            }
        }
    }

}

