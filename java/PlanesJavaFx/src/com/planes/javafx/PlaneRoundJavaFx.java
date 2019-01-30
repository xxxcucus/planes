package com.planes.javafx;

//JNI class interfacing to the c++ game engine
public class PlaneRoundJavaFx {
	static {
	      System.loadLibrary("libCommon"); // Load native library 
	   }
	 
	//creates the PlaneRound object in the game engine
	//must be called a single time
	public native void createPlanesRound(); 
	
	//show the planes
	public native int getRowNo();
	public native int getColNo();
	public native int getPlaneNo();
	public native int getPlaneSquareType(int i, int j, int isComputer);
	
	//edit the board
	public native void movePlaneLeft(int idx);
	public native void movePlaneRight(int idx);
	public native void movePlaneUpwards(int idx);
	public native void movePlaneDownwards(int idx);
	public native void rotatePlane(int idx);
	public native void doneClicked();
	
	//play the game
	public native void playerGuess(int row, int col);
	public native boolean playerGuess_RoundEnds();
	public native boolean playerGuess_IsPlayerWinner();
	public native boolean playerGuess_ComputerMoveGenerated();
	public native int playerGuess_StatNoPlayerMoves();
	public native int playerGuess_StatNoPlayerHits();
	public native int playerGuess_StatNoPlayerMisses();
	public native int playerGuess_StatNoPlayerDead();
	public native int playerGuess_StatNoPlayerWins();
	public native int playerGuess_StatNoComputerMoves();
	public native int playerGuess_StatNoComputerHits();
	public native int playerGuess_StatNoComputerMisses();
	public native int playerGuess_StatNoComputerDead();
	public native int playerGuess_StatNoComputerWins();
	
	public native void roundEnds();
	public native void initRound();
	
	//show the guesses
	public native int getPlayerGuessesNo();
	public native int getPlayerGuessRow(int idx);
	public native int getPlayerGuessCol(int idx);
	public native int getPlayerGuessType(int idx);
	
	public native int getComputerGuessesNo();
	public native int getComputerGuessRow(int idx);
	public native int getComputerGuessCol(int idx);
	public native int getComputerGuessType(int idx);	
}
