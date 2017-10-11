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
		int row = -1;
		while (row < 0 || row > 7) {
			System.out.print("Row: ");
			row = scnr.nextInt();
		}
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
			return getMove(player, theBoard);
		}
	}
	
	public static void showGameInfo(Gameboard board) {
		board.show();
		board.showScore();
		System.out.println("===============================");
	}

	public static Scanner scnr = new Scanner(System.in);

	public static void main(String[] args) {
		boolean playAgain;
		do{
			Gameboard theBoard = new Gameboard();
			
			// Give human the option to go first
			System.out.println("You are X's");
			System.out.print("Would you like to go first(y/n): ");
			char curPlayer;
			char otherPlayer;
			if (scnr.nextLine().contains("y")) {
				curPlayer = 'X';
				otherPlayer = 'O';
			} else {
				curPlayer = 'O';
				otherPlayer = 'X';
			}
			
			showGameInfo(theBoard);
			
			// Update the curPlayer status
			ArrayList<Move> curPlayerPosMoves = theBoard.findPossibleMoves(curPlayer);
			boolean curPlayerCanPlay = curPlayerPosMoves.size() > 0;
			
			// Update the otherPlayer status
			ArrayList<Move> otherPlayerPosMoves = theBoard.findPossibleMoves(otherPlayer);
			boolean otherPlayerCanPlay = otherPlayerPosMoves.size() > 0;
			
			// The turn loop
			do {
				if (!curPlayerCanPlay) {
					String person = (curPlayer == 'X') ? "You" : "Computer";
					System.out.println(person + " could not go");
				}
				else if (curPlayer == 'X') {// Human
					Move chosenMove = getMove(curPlayer, theBoard);
					theBoard.makeMove(chosenMove);
					System.out.println("You selected " + chosenMove);
				} 
				else if (curPlayer == 'O') {// Computer
					Move chosenMove = theBoard.findMoveWithMostWinningPaths(curPlayer);
					theBoard.makeMove(chosenMove);
					System.out.println("Computer selected " + chosenMove);
				} 
				
				showGameInfo(theBoard);
				
				// Update the curPlayer status
				curPlayer = switchPlayer(curPlayer);
				curPlayerPosMoves = theBoard.findPossibleMoves(curPlayer);
				curPlayerCanPlay = curPlayerPosMoves.size() > 0;
				
				// Update the otherPlayer status
				otherPlayer = switchPlayer(otherPlayer);
				otherPlayerPosMoves = theBoard.findPossibleMoves(otherPlayer);
				otherPlayerCanPlay = otherPlayerPosMoves.size() > 0;
				
			} while (curPlayerCanPlay || otherPlayerCanPlay);
			
			// Ask if the Human would like to play again
			System.out.print("Play again?(y/n): ");
			scnr.nextLine();
			playAgain = scnr.nextLine().contains("y");
		}while (playAgain);
	}
}
