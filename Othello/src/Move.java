import java.util.ArrayList;

public class Move {

	private int row;
	private int col;
	private char player;
	private Gameboard board;

	public Move(int row, int col, char team, Gameboard board) {
		this.row = row;
		this.col = col;
		this.player = team;
		this.board = board;
	}

	public char getTeam() {
		return player;
	}

	public int[] getCoordinates() {
		int[] coordinates = { row, col };
		return coordinates;
	}

	/**
	 * Returns true if the move is valid
	 */
	public boolean isValid() {
		return board.findPeiceAt(row, col) == '-' && flipAnyOver();
	}

	/**
	 * Checks if this move would flip over any of the opponent's pieces
	 * @return true if this move would flip over at least 1 opponent's piece
	 * false otherwise
	 */
	private boolean flipAnyOver() {
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (findPositsToFlip(i, j).size() > 0) {
					return true;
				}
			}
		}
		return false;
	}

	/** 
	 * Finds all of the positions that need to be flipped over as
	 * a result of this move
	 * @return
	 */
	public ArrayList<int[]> allPosToFlip() {
		ArrayList<int[]> positions = new ArrayList<int[]>();
		for (int i = -1; i < 2; i++) {
			for(int j = -1; j < 2; j++) {
				for (int[] position : findPositsToFlip(i, j)) {
					positions.add(position);
				}
			}
		}

		return positions;
	}

	/**
	 * Determines the positions of opponents pieces that need to be flipped over
	 * in a given direction out from this move.
	 * @param searchDown
	 *            +1 to go down, -1 to go up
	 * @param searchRight
	 *            +1 to go right, -1 to go left
	 * @return
	 */
	private ArrayList<int[]> findPositsToFlip(int searchDown, int searchRight) {
		ArrayList<int[]> positions = new ArrayList<int[]>();
		boolean tileOnBothSides = false;
		int r = row + searchDown;
		int c = col + searchRight;

		while (onBoard(r, c)) {
			int[] curPosition = { r, c };
			char peiceAtCurrentPos = board.findPeiceAt(r, c);
			
			// This position belongs to opponent
			if (peiceAtCurrentPos != player && peiceAtCurrentPos != '-') {
				positions.add(curPosition);
			}
			// This position belongs to this player
			else if (peiceAtCurrentPos == player) {
				tileOnBothSides = true;
				break;
			} 
			// This position doesn't belong to anyone
			else {
				break;
			}
			r += searchDown;
			c += searchRight;
		}
		// The player can only capture opponent pieces if the player has
		// a piece on both sides of this line of opponent pieces
		if (!tileOnBothSides) {
			positions.clear();
		}

		return positions;
	}
	
	/**
	 * Check if the given position is on the board
	 */
	private static boolean onBoard(int row, int col) {
		return row < 8 && col < 8 && row > -1 && col > -1;
	}

	/**
	 * A representation of this move
	 */
	public String toString() {
		return "Row: " + row + " Col: " + col;
	}
}