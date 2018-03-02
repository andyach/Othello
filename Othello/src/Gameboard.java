import java.util.ArrayList;

public class Gameboard {

	private char[][] board = { { '-', '-', '-', '-', '-', '-', '-', '-' }, 
							   { '-', '-', '-', '-', '-', '-', '-', '-' },
							   { '-', '-', '-', '-', '-', '-', '-', '-' }, 
							   { '-', '-', '-', 'O', 'X', '-', '-', '-' },
							   { '-', '-', '-', 'X', 'O', '-', '-', '-' }, 
							   { '-', '-', '-', '-', '-', '-', '-', '-' },
							   { '-', '-', '-', '-', '-', '-', '-', '-' }, 
							   { '-', '-', '-', '-', '-', '-', '-', '-' } };

	/**
	 * Creates a gameboard with an identical board as another gameboard
	 * 
	 * @param original
	 *            the gameboard to copy
	 */
	public Gameboard(Gameboard original) {
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				this.board[row][col] = original.board[row][col];
			}
		}
	}

	public Gameboard() {

	}

	public char findPeiceAt(int row, int col) {
		return board[row][col];
	}

	/**
	 * Print out the gameboard
	 */
	public void show() {
		int r = 0;
		System.out.println("  0 1 2 3 4 5 6 7");
		for (char[] row : board) {
			System.out.print(r + " ");
			for (char position : row) {
				System.out.print(position + " ");
			}
			System.out.println();
			r++;
		}
	}

	/**
	 * Make a move.
	 * 
	 * @param row
	 *            the row of the move
	 * @param col
	 *            the col of the move
	 * @param team
	 *            the team making the move
	 * @return boolean true if the move was made
	 */
	public boolean makeMove(Move theMove) {
		// Check if that position is valid
		if (theMove.isValid()) {
			// Place the players piece
			int[] coordinates = theMove.getCoordinates();
			board[coordinates[0]][coordinates[1]] = theMove.getTeam();
			
			// Change all other necessary positions
			for (int[] position : theMove.allPosToFlip()) {
				int row = position[0];
				int col = position[1];
				board[row][col] = theMove.getTeam();
			}
			return true;
		} 
		else {
			return false;
		}
	}

	/**
	 * @return true if the given player has a larger score
	 */
	public boolean winnerIs(char player) {
		if (count(player) > count(Othello.switchPlayer(player))) {
			return true;
		}
		return false;
	}

	/**
	 * print out the current score
	 */
	public void showScore() {
		System.out.println("X: " + count('X'));
		System.out.println("O: " + count('O'));
	}

	/**
	 * Count how many times a character occurs on the board
	 * 
	 * @param team
	 *            The char to look for
	 * @return int the number of times the char occurs
	 */
	public int count(char team) {
		int count = 0;
		for (char[] row : board) {
			for (char position : row) {
				if (position == team)
					count++;
			}
		}
		return count;
	}

	public int scoreDifference(char player) {
		char otherPlayer = Othello.switchPlayer(player);
		return count(player) - count(otherPlayer);
	}

	/**
	 * Makes random valid moves until the gameboard is finished and neither player
	 * can make a move
	 * 
	 * @param playerJustWent
	 *            The player that made the last move
	 */
	public void finishRandomly(char playerJustWent) {
		char player = Othello.switchPlayer(playerJustWent);
		ArrayList<Move> possibleMoves = this.findPosMoves(player);
		
		// Keep make moves as long as one of the players can go
		while (possibleMoves.size() != 0 || this.findPosMoves(playerJustWent).size() != 0) {

			if (possibleMoves.size() > 0) {
				Move selectedMove = selectRandom(possibleMoves);
				this.makeMove(selectedMove);
			}

			playerJustWent = player;
			player = Othello.switchPlayer(player);
			possibleMoves = this.findPosMoves(player);
		}
	}

	/**
	 * Randomly selects and returns an element from the ArrayList
	 * 
	 * @param theList
	 *            ArrayList
	 */
	public <T> T selectRandom(ArrayList<T> theList) {
		int randNum = Othello.randGen.nextInt(theList.size());
		return theList.get(randNum);
	}

	/**
	 * @return An ArrayList of all the possible moves the given player can make
	 */
	public ArrayList<Move> findPosMoves(char player) {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				Move posMove = new Move(row, col, player, this);
				if (posMove.isValid()) {
					possibleMoves.add(posMove);
				}
			}
		}
		return possibleMoves;
	}

}