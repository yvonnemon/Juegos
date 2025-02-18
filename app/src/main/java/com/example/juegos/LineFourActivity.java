package com.example.juegos;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

public class LineFourActivity extends AppCompatActivity {
    private static final int ROWS = 6;
    private static final int COLS = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line_four_activity);

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        gridLayout.setRowCount(6);
        gridLayout.setColumnCount(7);

        int cellSize = dpToPx(50); // Convert 90dp to pixels

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                TextView cell = new TextView(this);
                cell.setBackgroundResource(R.drawable.cell_connect_four);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = cellSize;
                params.height = cellSize;
                params.setMargins(4, 4, 4, 4); // Small margin to separate cells
                cell.setLayoutParams(params);

                gridLayout.addView(cell);
            }
        }
    }

    // Convert dp to pixels for different screen densities
    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }



}
