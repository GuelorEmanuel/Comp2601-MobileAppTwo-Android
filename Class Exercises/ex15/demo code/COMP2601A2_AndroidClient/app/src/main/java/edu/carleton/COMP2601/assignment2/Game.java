package edu.carleton.COMP2601.assignment2;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/*
 *  Name: Sean Benjamin
 *
 *  Game class is the Model for the Tic Tac Toe game.
 *  It is also intended to play the role of an "Observable", or subject
 *  in an observer pattern.
 *  The "Observable" role is that intended by class java.util.Observable.
 *  This would allow any UI to observe its state in a way consistent with
 *  the Observer design pattern.
 *
 *  The tic tac toe board is represented as a List of Strings.  The "X" represents player 1, the "O" represents player 2,
 *  and "" is an EMPTYTILE.
 *  
 *  The Game keeps track of the current board values in mBoard and the available spaces left to play in the Integer List availSpot
 *  It also keeps track of the currentPlayer, gameWinner (1=Player1/YOU 2=Player2/COMPUTER or 0=DRAW), number of tiles filed 
 *  (numberOfPlays) and the last tile played.
 *  
 */


public class Game extends  Observable{

	private List<String> mBoard;
	private List<Integer> availSpot;
	private final String PLAYERONE = "X";
	private final String PLAYERTWO = "O";
	private final String EMPTYTILE = "";
	private final int BOARDSIZE = 9;
	private int currentPlayer = 1;
	private int gameWinner = 0; //0 = Draw, 1 = Player One, and 2 = Player Two
	private int numberOfPlays = 0;
	private int lastPlayedTile = 0;
	
	public Game(){
		
		mBoard = new ArrayList<String>(BOARDSIZE);
		availSpot = new ArrayList<Integer>(BOARDSIZE);
		//[0]=r1c1, [1]=r1c2, [2]=r1c3
		//[3]=r2c1, [4]=r2c2, [5]=r2c3
		//[6]=r3c1, [7]=r3c2, [8]=r3c3
		
		for (int x = 0; x < BOARDSIZE; x++){
			mBoard.add(EMPTYTILE);
		}
		for (int y = 0; y < BOARDSIZE; y++){
			availSpot.add(y);
		}
				
	}
	
	public int getLastPlayedTile(){
		return lastPlayedTile;
	}
	public List<Integer> getAvailableSpot(){
		return availSpot;
	}
	
	public int getCurrentPlayer(){
		return currentPlayer;
	}
	
	
	public void setGameBoard(int mPosition, int mPlayer) throws IndexOutOfBoundsException {
		
		lastPlayedTile = mPosition;
		numberOfPlays++;
		
		//reset the current player
		if (mPlayer == 1){
			currentPlayer = 2;	
		} else {
			currentPlayer = 1;
		}
		
		//remove value at board position
		mBoard.remove(mPosition);
		
		//add "X" or "O" to board at position
		if (mPlayer == 1){
			mBoard.add(mPosition, PLAYERONE);
		} else {
			mBoard.add(mPosition, PLAYERTWO);
		}	
		
		//removed used tile position from available spot list
		availSpot.remove(Integer.valueOf(mPosition));
		
		//state has been modified, observers should be notified
		// mark as value changed
		setChanged();
		// trigger notification
		notifyObservers();
	}
	
	public List<String> getGameBoard(){
		//current board status
		return mBoard;
	}
	
	public void resetGameBoard()  throws IndexOutOfBoundsException{

		//starting the game over, reset values
		currentPlayer = 1;
		gameWinner = 0;
		numberOfPlays = 0;
		lastPlayedTile = 0;
		
		//empty board values from previous game
		mBoard.clear();
		//reset the board values to empty
		for (int x = 0; x < BOARDSIZE; x++){
			mBoard.add(x,EMPTYTILE);
		}
		
		//make all the board title available for play
		availSpot.clear();

		for (int y = 0; y < BOARDSIZE; y++){
			availSpot.add(y,y);
		}

		//state has been modified, observers should be notified
	}
	
	public boolean isGameDone(){
		
		//case 1: Player one winner, case 2: Player two winner case 3: Draw ELSE NOT DONE
		if ((mBoard.get(0).equals(PLAYERONE)) && (mBoard.get(1).equals(PLAYERONE)) && (mBoard.get(2).equals(PLAYERONE))){
			//ROW ONE ALL Xs
			gameWinner = 1;
			return true;
		} else if ((mBoard.get(3).equals(PLAYERONE)) && (mBoard.get(4).equals(PLAYERONE)) && (mBoard.get(5).equals(PLAYERONE))){
			//ROW TWO ALL Xs
			gameWinner = 1;
			return true;
		} else if ((mBoard.get(6).equals(PLAYERONE)) && (mBoard.get(7).equals(PLAYERONE)) && (mBoard.get(8).equals(PLAYERONE))){
			//ROW THREE ALL Xs
			gameWinner = 1;
			return true;
		} else if ((mBoard.get(2).equals(PLAYERONE)) && (mBoard.get(4).equals(PLAYERONE)) && (mBoard.get(6).equals(PLAYERONE))){
			//DIAGONAL FROM RIGHT CORNER ALL Xs
			gameWinner = 1;
			return true;
		} else if ((mBoard.get(0).equals(PLAYERONE)) && (mBoard.get(4).equals(PLAYERONE)) && (mBoard.get(8).equals(PLAYERONE))){
			//DIAGONAL FROM LEFT CORNER ALL Xs
			gameWinner = 1;
			return true;
		} else if ((mBoard.get(0).equals(PLAYERONE)) && (mBoard.get(3).equals(PLAYERONE)) && (mBoard.get(6).equals(PLAYERONE))){
			//COLUMN ONE ALL Xs
			gameWinner = 1;
			return true;
		} else if ((mBoard.get(1).equals(PLAYERONE)) && (mBoard.get(4).equals(PLAYERONE)) && (mBoard.get(7).equals(PLAYERONE))){
			//COLUMN TWO ALL Xs
			gameWinner = 1;
			return true;
		} else if ((mBoard.get(2).equals(PLAYERONE)) && (mBoard.get(5).equals(PLAYERONE)) && (mBoard.get(8).equals(PLAYERONE))){
			//COLUMN THREE ALL Xs
			gameWinner = 1;
			return true;
		} else if ((mBoard.get(0).equals(PLAYERTWO)) && (mBoard.get(1).equals(PLAYERTWO)) && (mBoard.get(2).equals(PLAYERTWO))){
			//ROW ONE ALL Os
			gameWinner = 2;
			return true;
		} else if ((mBoard.get(3).equals(PLAYERTWO)) && (mBoard.get(4).equals(PLAYERTWO)) && (mBoard.get(5).equals(PLAYERTWO))){
			//ROW TWO ALL Os
			gameWinner = 2;
			return true;
		} else if ((mBoard.get(6).equals(PLAYERTWO)) && (mBoard.get(7).equals(PLAYERTWO)) && (mBoard.get(8).equals(PLAYERTWO))){
			//ROW THREE ALL Os
			gameWinner = 2;
			return true;
		} else if ((mBoard.get(2).equals(PLAYERTWO)) && (mBoard.get(4).equals(PLAYERTWO)) && (mBoard.get(6).equals(PLAYERTWO))){
			//DIAGONAL FROM RIGHT CORNER ALL Os
			gameWinner = 2;
			return true;
		} else if ((mBoard.get(0).equals(PLAYERTWO)) && (mBoard.get(4).equals(PLAYERTWO)) && (mBoard.get(8).equals(PLAYERTWO))){
			//DIAGONAL FROM LEFT CORNER ALL Os
			gameWinner = 2;
			return true;
		} else if ((mBoard.get(0).equals(PLAYERTWO)) && (mBoard.get(3).equals(PLAYERTWO)) && (mBoard.get(6).equals(PLAYERTWO))){
			//COLUMN ONE ALL Os
			gameWinner = 2;
			return true;
		} else if ((mBoard.get(1).equals(PLAYERTWO)) && (mBoard.get(4).equals(PLAYERTWO)) && (mBoard.get(7).equals(PLAYERTWO))){
			//COLUNM TWO ALL Os
			gameWinner = 2;
			return true;
		} else if ((mBoard.get(2).equals(PLAYERTWO)) && (mBoard.get(5).equals(PLAYERTWO)) && (mBoard.get(8).equals(PLAYERTWO))){
			//COLUMN THREE ALL Os
			gameWinner = 2;
			return true;
		} else if (numberOfPlays == 9){
			//GAME IS A DRAW
			gameWinner = 0;
			return true;
		} else {	
			//Still not done	
			return false;
		}
	}
	
	public int getGameWinner(){
		return gameWinner;
	}
	
	
}

