package main.java.it.ssd.p2p.sudokugame;

import java.io.Serializable;

/**
 * The Sudoku class that implements all informations about the Sudoku.
 * @author Alessandra Orsi, Giulio Imperato, Vincenzo Nastro
 */
public class Sudoku implements Serializable
{
	private static final long serialVersionUID = 9195841778776094048L;

	private String game_name;

	private Grid grid;
	private Integer[][] matrix9x9 = new Integer[9][9];

	private int numberOfEmptyCells = 70; // Default 70 cells empty
	// the number of empty cells can be created in random mode in a pre-set renge
	// to increase or decrease the difficulty of the game

	private Solver solver;

	/**
	 * Constructs a new Sudoku instance.
	 * @param _game_name the name of {@link SudokuChallenge}.
	 * @param seed the seed for generate random numbers for Sudoku.
	 */
	public Sudoku(String _game_name, int seed)
	{
		game_name = _game_name;
		Generator generator = new Generator(seed);

		grid = generator.generate(numberOfEmptyCells, seed);

		for (int row = 0; row < 9; row++)
			for(int column = 0; column < 9; column++)
				matrix9x9[row][column] = grid.getCell(row, column).getValue();

		solver = generator.getSolver();
	}


	public String getName() {
		return game_name;
	}

	public Grid getGrid() {
		return grid;
	}


	public Integer[][] getMatrix() {
		return matrix9x9;
	}

	public int getNumberOfEmptyCells() {
		return numberOfEmptyCells;
	}

	public void setNumberOfEmptyCells(int number) {
		this.numberOfEmptyCells = number;
	}

	public Solver getSolver() {
		return solver;
	}

	@Override
	public String toString()
	{
		String toReturn = "";
		for (Integer[] row : this.getMatrix())
		{
			for (Integer value : row) {
				toReturn += value + " ";
			}
			toReturn += "\n";
		}

		return toReturn;
	}
}
