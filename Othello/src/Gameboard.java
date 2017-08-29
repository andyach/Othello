import java.util.ArrayList;

public class Gameboard {

	private char[][] board = {  { '-', '-', '-', '-', '-', '-', '-', '-' },
								{ '-', '-', '-', '-', '-', '-', '-', '-' },
								{ '-', '-', '-', '-', '-', '-', '-', '-' }, 
								{ '-', '-', '-', 'O', 'X', '-', '-', '-' },
								{ '-', '-', '-', 'X', 'O', '-', '-', '-' }, 
								{ '-', '-', '-', '-', '-', '-', '-', '-' },
								{ '-', '-', '-', '-', '-', '-', '-', '-' }, 
								{ '-', '-', '-', '-', '-', '-', '-', '-' } };
	/**
	 * Creates a gameboard with an identical board as another gameboard
	 * @param original the gameboard to copy
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
		System.out.println("=============================");
	}
/**
 * Make a move.
 * @param row the row of the move
 * @param col the col of the move
 * @param team the team making the move
 * @return boolean true if the move was made
 */
	public boolean move(int row, int col, char team) {
		// TODO check if that position is valid
		if (isValidMove(row, col, team)) {
			board[row][col] = team;
			// TODO adjust all necessary positions
			for (int[] position : posAll(row, col, team)) {
				int r = position[0];
				int c = position[1];
				board[r][c] = team;
			}
			return true;
		} else {
			return false;
		}
	}
/**
 * Count how many times a character occurs on the board
 * @param team The char to look for
 * @return int the number of times the char occurs
 */
	private int count(char team) {
		int count = 0;
		for (char[] row : board) {
			for (char position : row) {
				if (position == team) count++;
			}
		}
		return count;
	}
/**
 * print out the current score
 */
	public void showScore() {
		System.out.println("X: " + count('X'));
		System.out.println("O: " + count('O'));
	}
/**
 * An AI technique that finds the move that would allow the opponent
 * the fewest number of options for places to go on their turn
 * @param team The char of the player making this move
 * @return An int[] of the move like {row, col}
 */
	public int[] findMoveWhichAllowsFewestOptions(char team) {
		ArrayList<int[]> possibleMoves = findPossibleMoves(team);
		int[] bestMove = possibleMoves.get(0);
		int lowestScore = 100;
		for (int[] move : possibleMoves) {
			Gameboard newBoard = new Gameboard(this);
			newBoard.move(move[0], move[1], team);
			char opponent = Othello.switchPlayer(team);
			int score = newBoard.findPossibleMoves(opponent).size();
			if (score < lowestScore) {
				lowestScore = score;
				bestMove = move;
			}
		}
		return bestMove;

	}
/**
 * An AI technique that finds the move that gives the greatest chance of winning
 * if all moves after this move were made randomly by both players.
 * @param team The char of the player making this move
 * @return An int[] of the move like {row, col}
 */
	public int[] findMoveWithMostWinningPaths(char team) {
		ArrayList<int[]> possibleMoves = findPossibleMoves(team);
		int[] bestMove = possibleMoves.get(0);
		double highScore = 0;
		for (int[] move : possibleMoves) {
			Gameboard newBoard = new Gameboard(this);
			newBoard.move(move[0], move[1], team);
			double moveScore = newBoard.countPathsToVictory(team);
			if (moveScore > highScore) {
				highScore = moveScore;
				bestMove = move;
			}
		}
		System.out.println("Estimated chance of winning: " + highScore + "%");
		return bestMove;
	}

	// recording of beating computer is with TRIALS = 200
	/**
	 * Count how many times the gameboard leads to a win when
	 * all moves are made randomly
	 * @param team Count the number of victories for this team
	 * @return int
	 */
	private double countPathsToVictory(char team) {
		double pathsToVictoryEstimate = 0;
		final int TRIALS = 500;
		for (int i = 0; i < TRIALS; i++) {
			Gameboard newBoard = new Gameboard(this);
			newBoard.finishRandomly(team);
			if (newBoard.winnerIs(team)) {
				pathsToVictoryEstimate+=1;
			}
		}
		return pathsToVictoryEstimate*100/TRIALS;
	}
	/**
	 * Makes random valid moves until the gameboard is finished
	 * and neither player can make a move
	 * @param playerJustWent The player that made the last move
	 */
	public void finishRandomly(char playerJustWent) {
		char player = Othello.switchPlayer(playerJustWent);
		ArrayList<int[]> possibleMoves = this.findPossibleMoves(player);
		while (possibleMoves.size() != 0 || this.findPossibleMoves(playerJustWent).size() != 0) {
			
			if (possibleMoves.size() > 0) {
				int[] move = selectRandom(possibleMoves);
				this.move(move[0], move[1], player);
			}
			
			playerJustWent = player;
			player = Othello.switchPlayer(player);
			possibleMoves = this.findPossibleMoves(player);
		}
	}

/**
 * Randomly selects and returns an element from the ArrayList
 * @param theList ArrayList
 */
	private <T> T selectRandom(ArrayList<T> theList) {
		int randNum = Othello.randGen.nextInt(theList.size());
		return theList.get(randNum);
	}
/**
 * @return true if the given player won the game
 */
	public boolean winnerIs(char player) {
		if (count(player) > count(Othello.switchPlayer(player))) {
			return true;
		}
		return false;
	}
/**
 * @return An ArrayList of all the possible moves the current team can make
 */
	public ArrayList<int[]> findPossibleMoves(char team) {
		ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (isValidMove(row, col, team)) {
					int[] position = { row, col };
					possibleMoves.add(position);
				}
			}
		}
		return possibleMoves;
	}
/**
 * Returns true if the move is valid
 */
	public boolean isValidMove(int row, int col, char team) {
		if (board[row][col] != '-' || !flipAnyOver(row, col, team)) {
			return false;
		} else {
			return true;
		}

	}

	private ArrayList<int[]> posAll(int row, int col, char team) {
		ArrayList<int[]> positions = new ArrayList<int[]>();
		ArrayList<ArrayList<int[]>> allDirections = new ArrayList<ArrayList<int[]>>();
		allDirections.add(posUp(row, col, team));
		allDirections.add(posUpRight(row, col, team));
		allDirections.add(posRight(row, col, team));
		allDirections.add(posDownRight(row, col, team));
		allDirections.add(posDown(row, col, team));
		allDirections.add(posDownLeft(row, col, team));
		allDirections.add(posLeft(row, col, team));
		allDirections.add(posUpLeft(row, col, team));
		for (ArrayList<int[]> direction : allDirections) {
			for (int[] position : direction) {
				positions.add(position);
			}
		}
		return positions;
	}

	private boolean flipAnyOver(int row, int col, char team) {
		if (posUp(row, col, team).size() != 0) {
			return true;
		} else if (posUpRight(row, col, team).size() != 0) {
			return true;
		} else if (posRight(row, col, team).size() != 0) {
			return true;
		} else if (posDownRight(row, col, team).size() != 0) {
			return true;
		} else if (posDown(row, col, team).size() != 0) {
			return true;
		} else if (posDownLeft(row, col, team).size() != 0) {
			return true;
		} else if (posLeft(row, col, team).size() != 0) {
			return true;
		} else if (posUpLeft(row, col, team).size() != 0) {
			return true;
		} else {
			return false;
		}
	}

	private ArrayList<int[]> posUp(int row, int col, char team) {
		ArrayList<int[]> positions = new ArrayList<int[]>();
		boolean tileAboveMove = false;
		for (int r = row - 1; r > -1; r--) {
			int[] curPosition = { r, col };
			if (board[r][col] != team && board[r][col] != '-') {
				positions.add(curPosition);
			} else if (board[r][col] == team) {
				tileAboveMove = true;
				break;
			} else if (board[r][col] == '-') {
				break;
			}
		}
		if (!tileAboveMove) {
			positions.clear();
		}

		return positions;
	}

	private ArrayList<int[]> posDown(int row, int col, char team) {
		ArrayList<int[]> positions = new ArrayList<int[]>();
		boolean tileAboveMove = false;
		for (int r = row + 1; r < 8; r++) {
			int[] curPosition = { r, col };
			if (board[r][col] != team && board[r][col] != '-') {
				positions.add(curPosition);
			} else if (board[r][col] == team) {
				tileAboveMove = true;
				break;
			} else if (board[r][col] == '-') {
				break;
			}
		}
		if (!tileAboveMove) {
			positions.clear();
		}

		return positions;
	}

	private ArrayList<int[]> posRight(int row, int col, char team) {
		ArrayList<int[]> positions = new ArrayList<int[]>();
		boolean tileAboveMove = false;
		for (int c = col + 1; c < 8; c++) {
			int[] curPosition = { row, c };
			if (board[row][c] != team && board[row][c] != '-') {
				positions.add(curPosition);
			} else if (board[row][c] == team) {
				tileAboveMove = true;
				break;
			} else if (board[row][c] == '-') {
				break;
			}
		}
		if (!tileAboveMove) {
			positions.clear();
		}

		return positions;
	}

	private ArrayList<int[]> posLeft(int row, int col, char team) {
		ArrayList<int[]> positions = new ArrayList<int[]>();
		boolean tileAboveMove = false;
		for (int c = col - 1; c > -1; c--) {
			int[] curPosition = { row, c };
			if (board[row][c] != team && board[row][c] != '-') {
				positions.add(curPosition);
			} else if (board[row][c] == team) {
				tileAboveMove = true;
				break;
			} else if (board[row][c] == '-') {
				break;
			}
		}
		if (!tileAboveMove) {
			positions.clear();
		}

		return positions;
	}

	private ArrayList<int[]> posUpLeft(int row, int col, char team) {
		ArrayList<int[]> positions = new ArrayList<int[]>();
		boolean tileAboveMove = false;
		int r = row - 1;
		int c = col - 1;
		while (r > -1 && c > -1) {
			int[] curPosition = { r, c };
			if (board[r][c] != team && board[r][c] != '-') {
				positions.add(curPosition);
			} else if (board[r][c] == team) {
				tileAboveMove = true;
				break;
			} else if (board[r][c] == '-') {
				break;
			}
			r--;
			c--;
		}
		if (!tileAboveMove) {
			positions.clear();
		}

		return positions;
	}

	private ArrayList<int[]> posUpRight(int row, int col, char team) {
		ArrayList<int[]> positions = new ArrayList<int[]>();
		boolean tileAboveMove = false;
		int r = row - 1;
		int c = col + 1;
		while (r > -1 && c < 8) {
			int[] curPosition = { r, c };
			if (board[r][c] != team && board[r][c] != '-') {
				positions.add(curPosition);
			} else if (board[r][c] == team) {
				tileAboveMove = true;
				break;
			} else if (board[r][c] == '-') {
				break;
			}
			r--;
			c++;
		}
		if (!tileAboveMove) {
			positions.clear();
		}

		return positions;
	}

	private ArrayList<int[]> posDownRight(int row, int col, char team) {
		ArrayList<int[]> positions = new ArrayList<int[]>();
		boolean tileAboveMove = false;
		int r = row + 1;
		int c = col + 1;
		while (r < 8 && c < 8) {
			int[] curPosition = { r, c };
			if (board[r][c] != team && board[r][c] != '-') {
				positions.add(curPosition);
			} else if (board[r][c] == team) {
				tileAboveMove = true;
				break;
			} else if (board[r][c] == '-') {
				break;
			}
			r++;
			c++;
		}
		if (!tileAboveMove) {
			positions.clear();
		}

		return positions;
	}

	private ArrayList<int[]> posDownLeft(int row, int col, char team) {
		ArrayList<int[]> positions = new ArrayList<int[]>();
		boolean tileAboveMove = false;
		int r = row + 1;
		int c = col - 1;
		while (r < 8 && c > -1) {
			int[] curPosition = { r, c };
			if (board[r][c] != team && board[r][c] != '-') {
				positions.add(curPosition);
			} else if (board[r][c] == team) {
				tileAboveMove = true;
				break;
			} else if (board[r][c] == '-') {
				break;
			}
			r++;
			c--;
		}
		if (!tileAboveMove) {
			positions.clear();
		}

		return positions;
	}
}