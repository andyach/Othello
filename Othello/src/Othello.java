import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class Othello {
	public static Random randGen = new Random();

	public static char switchPlayer(char player) {
		return (player == 'X') ? 'O' : 'X';
	}
/**
 * Gets a move from the human player
 * @param player The char of the player
 * @param theBoard
 * @return
 */
	public static Move getMove(char player, Gameboard theBoard) {
		// Ask for row
		int row = -1;
		while (row < 0 || row > 7) {
			System.out.print("Row: ");
			row = scnr.nextInt();
		}
		// Ask for column
		int col = -1;
		while (col < 0 || col > 7) {
			System.out.print("Col: ");
			col = scnr.nextInt();
		}
		Move theMove = new Move(row, col, player, theBoard);
		if (theMove.isValid()) {
			return theMove;
		} else {
			System.out.println("Invalid location");
			// Ask again
			return getMove(player, theBoard);
		}
	}
	
	public static void showGameInfo(Gameboard board) {
		board.show();
		board.showScore();
		System.out.println("===============================");
	}

	public static Scanner scnr = new Scanner(System.in);
	
	private static boolean takeTurn(char curPlayer, char otherPlayer, Gameboard theBoard) {
		boolean gameOver = false;
		
		// Check if the current player can play
		ArrayList<Move> curPlayerMoves = theBoard.findPosMoves(curPlayer);
		boolean curPlayerCanPlay = curPlayerMoves.size() > 0;
		
		if (!curPlayerCanPlay) {
			String person = (curPlayer == 'X') ? "You" : "Computer";
			System.out.println(person + " could not go");
			
			// If neither player can go, the game is over
			ArrayList<Move> otherPlayerMoves = theBoard.findPosMoves(otherPlayer);
			gameOver = otherPlayerMoves.size() == 0;
		}
		// Current player is human
		else if (curPlayer == 'X') {
			Move chosenMove = getMove(curPlayer, theBoard);
			theBoard.makeMove(chosenMove);
			System.out.println("You selected " + chosenMove);
		} 
		// Current player is computer
		else if (curPlayer == 'O') {
			Move chosenMove;
			int openSpaces = 64-(theBoard.count('X') + theBoard.count('O'));
			if(openSpaces <= AI.MAX_SEARCH_DEPTH) {
				chosenMove = AI.alphaBetaSearch(theBoard, curPlayer);
			}
			else{
				chosenMove = AI.findMoveMonteCarlo(curPlayer, theBoard);
			}
			theBoard.makeMove(chosenMove);
			System.out.println("Computer selected " + chosenMove);
		}
		return gameOver;
	}
	
	public static void main(String[] args) {
		boolean playAgain;
		char curPlayer, otherPlayer;
		
		// Give human the option to go first
		System.out.println("You are X's");
		System.out.print("Would you like to go first(y/n): ");

		// Human is always X's
		// curPlayer will go first. 
		if (scnr.nextLine().contains("y")) {
			curPlayer = 'X';
			otherPlayer = 'O';
		} else {
			curPlayer = 'O';
			otherPlayer = 'X';
		}
		
		do{
			Gameboard theBoard = new Gameboard();
			boolean gameOver = false;
			showGameInfo(theBoard);
			// The turn loop
			do {
				gameOver = takeTurn(curPlayer, otherPlayer, theBoard);
				
				showGameInfo(theBoard);
				
				// Switch curPlayer and otherPlayer
				curPlayer = switchPlayer(curPlayer);
				otherPlayer = switchPlayer(otherPlayer);
			} while (!gameOver);
			
			// Ask if the Human would like to play again
			System.out.print("Play again?(y/n): ");
			scnr.nextLine();
			playAgain = scnr.nextLine().contains("y");

		}while (playAgain);
	}
}
