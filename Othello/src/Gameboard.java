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
		System.out.println("=============================");
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
		// TODO check if that position is valid
		if (theMove.isValid()) {
			int[] coordinates = theMove.getCoordinates();
			board[coordinates[0]][coordinates[1]] = theMove.getTeam();
			// TODO adjust all necessary positions
			for (int[] position : theMove.allPosToFlip()) {
				int r = position[0];
				int c = position[1];
				board[r][c] = theMove.getTeam();
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Count how many times a character occurs on the board
	 * 
	 * @param team
	 *            The char to look for
	 * @return int the number of times the char occurs
	 */
	private int count(char team) {
		int count = 0;
		for (char[] row : board) {
			for (char position : row) {
				if (position == team)
					count++;
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
	 * An AI technique that finds the move that would allow the opponent the fewest
	 * number of options for places to go on their turn
	 * 
	 * @param team
	 *            The char of the player making this move
	 * @return An int[] of the move like {row, col}
	 */
	public Move findMoveWhichAllowsFewestOptions(char team) {
		ArrayList<Move> possibleMoves = findPossibleMoves(team);
		Move bestMove = possibleMoves.get(0);
		int lowestScore = 100;
		for (Move posMove : possibleMoves) {
			Gameboard newBoard = new Gameboard(this);
			newBoard.makeMove(posMove);
			char opponent = Othello.switchPlayer(team);
			int score = newBoard.findPossibleMoves(opponent).size();
			if (score < lowestScore) {
				lowestScore = score;
				bestMove = posMove;
			}
		}
		return bestMove;
	}

	/**
	 * An AI technique that finds the move that gives the greatest chance of winning
	 * if all moves after this move were made randomly by both players.
	 * 
	 * @param team
	 *            The char of the player making this move
	 * @return An int[] of the move like {row, col}
	 */
	public Move findMoveWithMostWinningPaths(char team) {
		ArrayList<Move> possibleMoves = findPossibleMoves(team);
		Move bestMove = possibleMoves.get(0);
		double highScore = 0;
		for (Move posMove : possibleMoves) {
			Gameboard newBoard = new Gameboard(this);
			newBoard.makeMove(posMove);
			double moveScore = newBoard.countPathsToVictory(team);
			if (moveScore > highScore) {
				highScore = moveScore;
				bestMove = posMove;
			}
		}
		System.out.println("Estimated chance Human Wins: " + (100-highScore) + "%");
		return bestMove;
	}

	/**
	 * Count how many times the gameboard leads to a win when all moves are made
	 * randomly
	 * 
	 * @param team
	 *            Count the number of victories for this team
	 * @return int
	 */
	private double countPathsToVictory(char team) {
		double pathsToVictoryEstimate = 0;
		final int TRIALS = 500;
		for (int i = 0; i < TRIALS; i++) {
			Gameboard newBoard = new Gameboard(this);
			newBoard.finishRandomly(team);
			if (newBoard.winnerIs(team)) {
				pathsToVictoryEstimate += 1;
			}
		}
		return pathsToVictoryEstimate * 100 / TRIALS;
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
		ArrayList<Move> possibleMoves = this.findPossibleMoves(player);
		while (possibleMoves.size() != 0 || this.findPossibleMoves(playerJustWent).size() != 0) {

			if (possibleMoves.size() > 0) {
				Move selectedMove = selectRandom(possibleMoves);
				this.makeMove(selectedMove);
			}

			playerJustWent = player;
			player = Othello.switchPlayer(player);
			possibleMoves = this.findPossibleMoves(player);
		}
	}

	/**
	 * Randomly selects and returns an element from the ArrayList
	 * 
	 * @param theList
	 *            ArrayList
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
	public ArrayList<Move> findPossibleMoves(char team) {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				Move posMove = new Move(row, col, team, this);
				if (posMove.isValid()) {
					possibleMoves.add(posMove);
				}
			}
		}
		return possibleMoves;
	}

}