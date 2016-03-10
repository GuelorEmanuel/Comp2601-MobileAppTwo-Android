package me.cubeguelor.android.assignment2threads;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static MainActivity instance; //globally accessible instance

    //Edit the NUMROW and NUMCOLUMN to change the size of the maze
    private int NUMROW    = 14; // Number of rows for the maze
    private int NUMCOLUMN = 10; // Number of columns for the maze
    private int [][] grid;

    private int PATH = 1;
    private int WALL =  0;
    private int STARTCELL = 2;
    private int ENDCELL   = 3;
    private int CELLVISITED = 4;

    private Handler handler;

    private Button gridButtons[][] = null;

    private Maze labyrinth;

    private GridLayout gridLayout;

    private Button solve_btn;


    //provide global access to main activity instance
    public static MainActivity getInstance(){return instance;}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        instance = this; //set globally accessible instance
        handler = new Handler();

        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        gridLayout.setUseDefaultMargins(false);
        gridLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridLayout.setRowOrderPreserved(false);

        if(Maze.getInstance() != null) {
            labyrinth = Maze.getInstance();
        } else {
            labyrinth = new Maze(this, NUMROW, NUMCOLUMN);
        }
        gridButtons = new Button[NUMROW][NUMCOLUMN];
        grid        = labyrinth.getGrid();

        displayMaze(); // Let diplay our default maze

        // Listener Handles button click for user maze building
        for (int rowCounter = 0; rowCounter < NUMROW; rowCounter++)
            for (int columnCounter = 0; columnCounter < NUMCOLUMN; columnCounter++) {
                final Button gridBtn = gridButtons[rowCounter][columnCounter];
                final int i = rowCounter;
                final int j = columnCounter;
                gridBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (labyrinth.isStartCell()) {
                            if (Integer.parseInt(gridBtn.getText().toString()) == STARTCELL) {
                                gridBtn.setText(Integer.toString(PATH));
                                labyrinth.setCell(i, j, PATH);
                                gridBtn.setBackgroundResource(R.drawable.grass);
                                labyrinth.setStartCell(false);
                            }else {
                                if (labyrinth.isDestinationCell()) {
                                    if (Integer.parseInt(gridBtn.getText().toString()) == ENDCELL) {
                                        gridBtn.setText(Integer.toString(WALL));
                                        labyrinth.setCell(i, j, WALL);
                                        gridBtn.setBackgroundResource(R.drawable.brick);
                                        labyrinth.setDestinationCell(false);
                                    } else {
                                        if (Integer.parseInt(gridBtn.getText().toString()) == WALL) {
                                            gridBtn.setText(Integer.toString(PATH));
                                            labyrinth.setCell(i, j, PATH);
                                            gridBtn.setBackgroundResource(R.drawable.grass);
                                        }else {
                                            gridBtn.setText(Integer.toString(WALL));
                                            labyrinth.setCell(i, j, WALL);
                                            gridBtn.setBackgroundResource(R.drawable.brick);
                                        }
                                    }

                                }else {
                                    if (!labyrinth.isDestinationCell()) {
                                        gridBtn.setText(Integer.toString(ENDCELL));
                                        labyrinth.setCell(i, j, ENDCELL);
                                        gridBtn.setBackgroundResource(R.drawable.pikachu);
                                        labyrinth.setDestinationCell(true);
                                    }
                                }
                            }
                        } else {
                            if (!labyrinth.isStartCell()) {

                                if (Integer.parseInt(gridBtn.getText().toString()) == ENDCELL) {
                                    labyrinth.setDestinationCell(false);
                                }
                                gridBtn.setText(Integer.toString(STARTCELL));
                                labyrinth.setCell(i,j, STARTCELL);
                                labyrinth.setIndexStartRow(i);
                                labyrinth.setIndexStartCol(j);
                                labyrinth.setStartCell(true);
                                gridBtn.setBackgroundResource(R.drawable.ash);
                            }
                        }
                    }
                });
            }

        //Solve Button event listener
        solve_btn = (Button)findViewById(R.id.solve_btn);
        solve_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                CharSequence solvedMazeMessage = "Pikachu was found!";
                CharSequence notSolvedMazeMessage = "Pikachu was not found, It's Over ~ Go Home ~ Go!";
                CharSequence noStartPointMessage = "Please pick a starting point.";
                int duration = Toast.LENGTH_SHORT;
                if (labyrinth.isIsolved()){
                    Toast toast = Toast.makeText(context, solvedMazeMessage, duration);
                    toast.show();
                } else if (labyrinth.isNotsolved()){
                    Toast toast = Toast.makeText(context, notSolvedMazeMessage, duration);
                    toast.show();
                }
                else if (labyrinth.isStartCell()) {
                    labyrinth.start();
                }else {
                    Toast toast = Toast.makeText(context, noStartPointMessage, duration);
                    toast.show();
                }
            }
        });
    }

    public Handler getHandler(){
        return this.handler;
    }

    //Method for updating the UI maze
    public void setGrid(int [][] maze) {
        grid = maze;

        for (int rowCounter = 0; rowCounter < NUMROW; rowCounter++)
            for (int columnCounter = 0; columnCounter < NUMCOLUMN; columnCounter++) {
                gridButtons[rowCounter][columnCounter].setEnabled(false);
                gridButtons[rowCounter][columnCounter].setText(""+maze[rowCounter][columnCounter]);

                if (Integer.parseInt(gridButtons[rowCounter][columnCounter].getText().toString()) == WALL) {
                    gridButtons[rowCounter][columnCounter].setBackgroundResource(R.drawable.brick);
                    gridButtons[rowCounter][columnCounter].setBackgroundResource(R.drawable.brick);
                }else if (Integer.parseInt(gridButtons[rowCounter][columnCounter].getText().toString()) == PATH) {
                    gridButtons[rowCounter][columnCounter].setBackgroundResource(R.drawable.grass);
                }
                if (Integer.parseInt(gridButtons[rowCounter][columnCounter].getText().toString()) == CELLVISITED) {

                    gridButtons[rowCounter][columnCounter].setBackgroundResource(R.drawable.pokeball);
                }
            }
    }

    //Method for Creating the individual buttons and adding them to an array of Butons
    public void displayMaze() {
        grid = labyrinth.getGrid();

        for (int rowCounter = 0; rowCounter < NUMROW; rowCounter++)
            for (int columnCounter = 0; columnCounter < NUMCOLUMN; columnCounter++) {
                Button b = new Button(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.setMargins(0, 0, 0,0);
                params.width =  70;
                params.height = 70;
                b.setText(Integer.toString(grid[rowCounter][columnCounter]));
                b.setLayoutParams(params);
                if (Integer.parseInt(b.getText().toString()) == WALL) {
                    b.setBackgroundResource(R.drawable.brick);
                }else if (Integer.parseInt(b.getText().toString()) == PATH) {
                    b.setBackgroundResource(R.drawable.grass);
                }else if(Integer.parseInt(b.getText().toString()) == CELLVISITED) {
                    b.setBackgroundResource(R.drawable.pokeball);
                } else if (Integer.parseInt(b.getText().toString()) == STARTCELL){
                    b.setBackgroundResource(R.drawable.ash);
                } else if (Integer.parseInt(b.getText().toString()) == ENDCELL){
                    b.setBackgroundResource(R.drawable.pikachu);
                }
                gridButtons[rowCounter][columnCounter] = b;

                gridLayout.addView(b);

            }
    }
}
