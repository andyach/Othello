import java.util.ArrayList;
import java.util.Arrays;
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
	public static int[] getMove(char player, Gameboard theBoard) {
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
		int[] move = { row, col };
		if (theBoard.isValidMove(row, col, player)) {
			return move;
		} else {
			System.out.println("Invalid location");
			return getMove(player, theBoard);
		}
	}

	public static Scanner scnr = new Scanner(System.in);

	public static void main(String[] args) {
		boolean playAgain = true;
		while (playAgain) {
			Gameboard theBoard = new Gameboard();
			theBoard.show();
			System.out.println("Would you like to go first(y/n): ");
			char player;
			if (scnr.nextLine().contains("y")) {
				player = 'X';
			} else {
				player = 'O';
			}
			while (theBoard.findPossibleMoves('X').size() > 0 || theBoard.findPossibleMoves('O').size() > 0) {
				ArrayList<int[]> possibleMoves = theBoard.findPossibleMoves(player);
				if (player == 'X' && possibleMoves.size() > 0) {
					int[] chosenMove = getMove(player, theBoard);
					theBoard.move(chosenMove[0], chosenMove[1], player);
					System.out.println("You selected: " + Arrays.toString(chosenMove));
				} else if (player == 'O' && possibleMoves.size() > 0) {
					int[] chosenMove = theBoard.findMoveWithMostWinningPaths(player);
					theBoard.move(chosenMove[0], chosenMove[1], player);
					System.out.println("Computer selected: " + Arrays.toString(chosenMove));
				} else {
					String person = (player == 'X') ? "You" : "Computer";
					System.out.println(person + " could not go");
				}
				player = switchPlayer(player);
				theBoard.showScore();
				theBoard.show();
			}
			System.out.print("Play again?(y/n): ");
			playAgain = scnr.nextLine().contains("y");
		}
	}
}
