package main.java.it.ssd.p2p.sudokugame;
import java.util.Random;

/**
 * A Generator to generate random {@link Sudoku} {@link Grid} instances.
 */
public class Generator
{
	private Solver solver;

	/**
	 * Constructs a new Generator instance.
	 * @param seed the seed for generate random numbers for {@link Sudoku}
	 */
	public Generator(int seed) {
		this.solver = new Solver(seed);
	}

	/**
	 * Generates a random {@link Grid} instance with the given number of empty {@link Grid.Cell}s.
	 * <br><br>
	 * Note: The complexity for a human player increases with an higher amount of empty {@link Grid.Cell}s.
	 * @param numberOfEmptyCells the number of empty {@link Grid.Cell}s
	 * @param seed the seed for generate random numbers for {@link Sudoku}
	 * @return a randomly filled Sudoku {@link Grid} with the given number of empty {@link Grid.Cell}s
	 */
	public Grid generate(int numberOfEmptyCells, int seed)
	{
		Grid grid = generate(seed);

		eraseCells(grid, numberOfEmptyCells, seed);

		return grid;
	}

	/**
	 * Erases {@link Grid.Cell}s from {@link Sudoku} given the number of empty cells
	 * @param grid the grid that describes the {@link Sudoku}
	 * @param numberOfEmptyCells the number of empty {@link Grid.Cell}s
	 * @param seed the seed for generate random numbers for {@link Sudoku}
	 */
	private void eraseCells(Grid grid, int numberOfEmptyCells, int seed)
	{
		Random random = new Random();
		random.setSeed(seed);
		for (int i = 0; i < numberOfEmptyCells; i++)
		{
			int randomRow = random.nextInt(9);
			int randomColumn = random.nextInt(9);

			Grid.Cell cell = grid.getCell(randomRow, randomColumn);
			if (!cell.isEmpty())
				cell.setValue(0);
			else
				i--;
		}
	}

	public Solver getSolver() {
		return solver;
	}

	private Grid generate(int seed)
	{
		Grid grid = Grid.emptyGrid();
		solver.solve(grid);
		return grid;
	}
}