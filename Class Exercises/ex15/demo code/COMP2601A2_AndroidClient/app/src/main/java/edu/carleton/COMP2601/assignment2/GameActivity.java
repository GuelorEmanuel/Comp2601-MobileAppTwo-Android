package edu.carleton.COMP2601.assignment2;

import java.util.Observable;
import java.util.Observer;

import org.json.JSONException;
import org.json.JSONObject;

import edu.carleton.COMP2601.assignment2.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Observer;

/*
 *  Name: Sean Benjamin
 *
 *  GameActivity class acts as the View and Control.
 *  All the UI work is done in this class.
 *  It is also intended to participate as an Observer in the observer design pattern.
 *  The Observer role is the one intended by the java interface java.util.Observer.
 *  An Observer watches, or observes, instances of Observable.
 *  
 *  The OtherPlayer private class is an AsyncTask.  This class is a thread that plays the computer player and Player 1 (if not
 *  played in 2 seconds).  The do back ground task makes a move for the current player until the game ends or the game is 
 *  interrupted.
 *  
 */

public class GameActivity extends Activity  implements OnClickListener, Observer{


	public TextView mTextView;  //text view that shows the status of the game
    private ImageButton ibutton; //the tic tac toe image buttons
    public Button button;  //the start or running button
	public Game mGame;  //Game class which inherits from the Observerable class
	private int[] imageButtonID = new int[9];  //ids for each of the image buttons
	private final int BOARDSIZE = 9;
	private final String PLAYERONE_PIECE = "X";
	private final String PLAYERTWO_PIECE ="O";
	MainActivity instance;
	static GameActivity gInstance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_main);
	    instance = MainActivity.getInstance();
	    gInstance = this;
		
		mGame = new Game();
		mGame.addObserver(this);

		mTextView = (TextView) findViewById(R.id.textView1);
		
		//initialize and setup the image buttons, button and textview
        for (int r = 0; r < BOARDSIZE; r++) {

                imageButtonID[r] = getResources().getIdentifier("imageButton" + r, "id",
                        getPackageName());
                ibutton = (ImageButton) findViewById(imageButtonID[r]);
                ibutton.setEnabled(false);
                ibutton.setOnClickListener(this);
        }

        button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(this);
        button.setEnabled(true);

		
	}
	
	public static GameActivity getInstance(){
		return gInstance;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//When user leaves game it sends a game over message to server
	protected void onPause(){
		super.onPause();
		JSONObject msg = new JSONObject();
			try {
				msg.put("type", "GAME_OVER");
				msg.put("source", instance.name);
				msg.put("destination", instance.dest);					
				msg.put("play", "0");
				msg.put("player", "0");
				instance.new Write(msg).execute("");
				instance.player = 1;
			} catch (JSONException e) {
				e.printStackTrace();
			}
	}

	@Override
	public void onClick(View v) {
		//Action on the image buttons and button
		switch (v.getId())
		{
		case R.id.button1:
			if (!getResources().getString(R.string.button_run).equals(((Button) v).getText())){
				//if game is not running set the game to initial state and create the other player thread
				mTextView.setText(null);
				mGame.resetGameBoard();				
				button.setText(getResources().getString(R.string.button_run));
				if (instance.player == 1){
					enableBoard();
				}
				JSONObject msg = new JSONObject();
				//send Game on message to server
				try {
					msg.put("type", "GAME_ON");
					msg.put("source", instance.name);
					msg.put("destination", instance.dest);
					instance.new Write(msg).execute("");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			
			} else {
			    //terminated during a running game.  Send game over message server
				button.setText(getResources().getString(R.string.button_start));
				mTextView.setText(getResources().getString(R.string.game_end));
				mGame.resetGameBoard();
				disableBoard();
				JSONObject msg = new JSONObject();

				try {
					msg.put("type", "GAME_OVER");
					msg.put("source", instance.name);
					msg.put("destination", instance.dest);
					msg.put("play", "0");
					msg.put("player", "0");
					instance.new Write(msg).execute("");
					mTextView.setText(getResources().getString(R.string.i_quit));
					instance.player = 1;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			break;
		//if an image button is pressed then call madePlay which updates the setBoard method in Game
		//and notifies the observers to update their current states
		case R.id.imageButton0:
			madePlay(0);
			break;
		case R.id.imageButton1:
			madePlay(1);
			break;
		case R.id.imageButton2:
			madePlay(2);
			break;
		case R.id.imageButton3:
			madePlay(3);
			break;
		case R.id.imageButton4:
			madePlay(4);
			break;
		case R.id.imageButton5:
			madePlay(5);
			break;
		case R.id.imageButton6:
			madePlay(6);
			break;
		case R.id.imageButton7:
			madePlay(7);
			break;
		case R.id.imageButton8:
			madePlay(8);
			break;
		}
	
		
	}


	public void redrawGameBoard() {
		//redraw the game board.
		//This should happen whenever the Game object has been modified

		int i = 0;
		for(String item:mGame.getGameBoard()){
			ibutton = (ImageButton) findViewById(imageButtonID[i]);
			if(item.equals(PLAYERONE_PIECE)){
				ibutton.setImageResource(R.drawable.button_x);
			} else if (item.equals(PLAYERTWO_PIECE)){
				ibutton.setImageResource(R.drawable.button_o);
			} else {
				ibutton.setImageResource(R.drawable.button_empty);
			}
			i++;
		}
	}
	
	//Game has been completed in one of three ways, win, lose or draw.
	//image buttons are disables, win state is displayed and button has start text
	void GameComplete(int aPlayer){
		String aWinner;
		disableBoard();
		if (mGame.getGameWinner() == 0){
			aWinner = getResources().getString(R.string.a_draw);
		} else if (instance.player == aPlayer){
		  aWinner = getResources().getString(R.string.you_won);
		} else {
			aWinner = instance.dest + " " + getResources().getString(R.string.other_won);
		}	
		
		mTextView.setText(aWinner);
		button.setText(getResources().getString(R.string.button_start));
	}

	//calls the Game/model to update the board with the current choice
	//notify user which button was pressed

	void madePlay(int squarePlayed){
		
		String pressButton = "Button " + squarePlayed + " clicked by " + instance.name;
		mTextView.setText(pressButton);
		mGame.setGameBoard(squarePlayed, mGame.getCurrentPlayer());
		
		// Sends a move message to server when user clicks a key
		JSONObject msg = new JSONObject();
		
		try {
			msg.put("type", "MOVE_MESSAGE");
			msg.put("source", instance.name);
			msg.put("destination", instance.dest);
			msg.put("play", Integer.toString(squarePlayed));
			msg.put("player", Integer.toString(instance.player));
			mGame.isGameDone();
			instance.new Write(msg).execute("");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		disableBoard();
	}
	
	//disable all image buttons, happens when computer is playing, initial game state or when game has been terminated
	void disableBoard(){
		for(int x = 0; x < BOARDSIZE; x++){
			ibutton = (ImageButton) findViewById(imageButtonID[x]);	
			ibutton.setEnabled(false);	
		}
	}
	
	//enable the image buttons that are available to be played
	void enableBoard(){
		for(int v1 = 0; v1 < mGame.getAvailableSpot().size(); v1++){
			ibutton = (ImageButton) findViewById(imageButtonID[Integer.valueOf(mGame.getAvailableSpot().get(v1))]);	
			ibutton.setEnabled(true);	
		}
	}

	public void update(Observable o, Object arg) {
		redrawGameBoard();
	}

}
