import java.util.ArrayList;

public class Move {

	private int row;
	private int col;
	private char team;
	private Gameboard board;

	public Move(int row, int col, char team, Gameboard board) {
		this.row = row;
		this.col = col;
		this.team = team;
		this.board = board;
	}

	public char getTeam() {
		return team;
	}

	public int[] getCoordinates() {
		int[] coordinates = { row, col };
		return coordinates;
	}

	/**
	 * Returns true if the move is valid
	 */
	public boolean isValid() {
		if (board.findPeiceAt(row, col) != '-' || !flipAnyOver()) {
			return false;
		} else {
			return true;
		}
	}

	private boolean flipAnyOver() {
		if (findPeicesToFlip(-1, -1).size() != 0) {
			return true;
		} else if (findPeicesToFlip(-1, 0).size() != 0) {
			return true;
		} else if (findPeicesToFlip(-1, 1).size() != 0) {
			return true;
		} else if (findPeicesToFlip(0, -1).size() != 0) {
			return true;
		} else if (findPeicesToFlip(0, 1).size() != 0) {
			return true;
		} else if (findPeicesToFlip(1, -1).size() != 0) {
			return true;
		} else if (findPeicesToFlip(1, 0).size() != 0) {
			return true;
		} else if (findPeicesToFlip(1, 1).size() != 0) {
			return true;
		} else {
			return false;
		}
	}

	public ArrayList<int[]> allPosToFlip() {
		ArrayList<int[]> positions = new ArrayList<int[]>();

		merge(positions, findPeicesToFlip(-1, -1));
		merge(positions, findPeicesToFlip(-1, 0));
		merge(positions, findPeicesToFlip(-1, 1));
		merge(positions, findPeicesToFlip(0, -1));
		merge(positions, findPeicesToFlip(0, 1));
		merge(positions, findPeicesToFlip(1, -1));
		merge(positions, findPeicesToFlip(1, 0));
		merge(positions, findPeicesToFlip(1, 1));

		return positions;
	}

	private <T> void merge(ArrayList<T> mainArray, ArrayList<T> subArray) {
		for (T item : subArray) {
			mainArray.add(item);
		}
	}

	/**
	 * 
	 * @param searchDown
	 *            +1 to go down one
	 * @param searchRight
	 *            +1 to go right one
	 * @return
	 */
	private ArrayList<int[]> findPeicesToFlip(int searchDown, int searchRight) {
		ArrayList<int[]> positions = new ArrayList<int[]>();
		boolean tileAboveMove = false;
		int r = row + searchDown;
		int c = col + searchRight;

		while (onBoard(r, c)) {
			int[] curPosition = { r, c };
			char peiceAtCurrentPos = board.findPeiceAt(r, c);
			if (peiceAtCurrentPos != team && peiceAtCurrentPos != '-') {
				positions.add(curPosition);
			} else if (peiceAtCurrentPos == team) {
				tileAboveMove = true;
				break;
			} else if (peiceAtCurrentPos == '-') {
				break;
			}
			r += searchDown;
			c += searchRight;
		}
		if (!tileAboveMove) {
			positions.clear();
		}

		return positions;
	}

	private static boolean onBoard(int r, int c) {
		return r < 8 && c < 8 && r > -1 && c > -1;
	}

	public String toString() {
		return "Row: " + row + " Col: " + col;
	}
}