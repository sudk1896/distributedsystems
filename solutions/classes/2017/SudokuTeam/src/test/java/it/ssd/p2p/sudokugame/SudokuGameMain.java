package test.java.it.ssd.p2p.sudokugame;

import java.io.IOException;

import main.java.it.ssd.p2p.sudokugame.Grid;
import main.java.it.ssd.p2p.sudokugame.MessageListenerImpl;
import main.java.it.ssd.p2p.sudokugame.SudokuGameImpl;

/*
 * The Sudoku that we have used for our tests
╔═══╤═══╤═══╦═══╤═══╤═══╦═══╤═══╤═══╗
║   │   │   ║   │ 3 │   ║   │   │   ║
╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
║   │   │   ║   │   │   ║   │   │   ║
╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
║   │   │   ║   │   │   ║   │   │   ║
╠═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══╣
║   │   │ 6 ║ 4 │   │   ║   │   │   ║
╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
║   │   │   ║   │   │ 8 ║ 1 │   │ 6 ║
╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
║   │   │   ║   │ 7 │   ║   │   │ 3 ║
╠═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══╣
║   │   │   ║ 5 │   │   ║   │   │   ║
╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
║   │   │   ║   │ 8 │   ║   │   │   ║
╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
║   │ 8 │   ║   │   │   ║   │   │   ║
╚═══╧═══╧═══╩═══╧═══╧═══╩═══╧═══╧═══╝
   
	
 
 * Solved Sudoku
╔═══╤═══╤═══╦═══╤═══╤═══╦═══╤═══╤═══╗
║ 7 │ 1 │ 4 ║ 6 │ 3 │ 5 ║ 8 │ 2 │ 9 ║
╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
║ 6 │ 3 │ 5 ║ 8 │ 2 │ 9 ║ 7 │ 1 │ 4 ║
╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
║ 8 │ 2 │ 9 ║ 7 │ 1 │ 4 ║ 6 │ 3 │ 5 ║
╠═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══╣
║ 1 │ 7 │ 6 ║ 4 │ 5 │ 3 ║ 2 │ 9 │ 8 ║
╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
║ 4 │ 5 │ 3 ║ 2 │ 9 │ 8 ║ 1 │ 7 │ 6 ║
╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
║ 2 │ 9 │ 8 ║ 1 │ 7 │ 6 ║ 4 │ 5 │ 3 ║
╠═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══╣
║ 3 │ 4 │ 7 ║ 5 │ 6 │ 1 ║ 9 │ 8 │ 2 ║
╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
║ 5 │ 6 │ 1 ║ 9 │ 8 │ 2 ║ 3 │ 4 │ 7 ║
╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
║ 9 │ 8 │ 2 ║ 3 │ 4 │ 7 ║ 5 │ 6 │ 1 ║
╚═══╧═══╧═══╩═══╧═══╧═══╩═══╧═══╧═══╝
   

 * */

public class SudokuGameMain
{
	public static void main(String[] args) throws Exception
	{
		try
		{
			@SuppressWarnings("unused")
			SudokuGameImpl peer0 = new SudokuGameImpl(0, "127.0.0.1", new MessageListenerImpl(0));

			SudokuGameImpl peer1 = new SudokuGameImpl(1, "127.0.0.1", new MessageListenerImpl(1));

			SudokuGameImpl peer2 = new SudokuGameImpl(2, "127.0.0.1", new MessageListenerImpl(2));

			SudokuGameImpl peer3 = new SudokuGameImpl(3, "127.0.0.1", new MessageListenerImpl(3));

			Integer[][] matrix1 = peer1.generateNewSudoku("Challenge 1");

			Integer[][] matrix2 = peer1.generateNewSudoku("Challenge 2");

			if (matrix1 == null) 
				System.out.println("matrix1: the Sudoku already exists");
			else
				System.out.println("matrix1: the Sudoku not exists");

			if (matrix2 == null)
				System.out.println("matrix2: the Sudoku already exists");
			else
				System.out.println("matrix2: the Sudoku not exists");

			Grid sudoku = convert(matrix1);
			System.out.println(sudoku);

			// Successful join
			if (peer1.join("Challenge 1", "Vincenzo"))
				System.out.println("Challenge 1 esiste");

			// Join with failure (the challenge not exists)
			if (!peer1.join("Challenge 3", "Vincenzo"))
				System.out.println("Challenge 3 not exists");

			// Join with failure (the user with this nickname already exists)
			if (!peer1.join("Challenge 1", "Vincenzo"))
				System.out.println("In Challenge 1 the user with nickname 'Vincenzo' already exists");

			peer2.join("Challenge 1", "Alessandra");

			peer3.join("Challenge 1", "Giulio");


			// challenge that not exists
			if (peer1.placeNumber("Challenge 3", 0, 0, 1) == null)
				System.err.println("placeNumber: this challenge not exists");

			// invalid values
			if (peer1.placeNumber("Challenge 1", 0, 0, 3) == null)
				System.err.println("placeNumber: invalid value");
			if (peer1.placeNumber("Challenge 1", 3, 1, 6) == null)
				System.err.println("placeNumber: invalid value");

			// valid value but incorrect (-1)
			if (peer1.placeNumber("Challenge 1", 1, 4, 5) != null)
				System.err.println("placeNumber: valid value but incorrect (-1)");

			
			// valid value but incorrect (-1)
			if (peer1.placeNumber("Challenge 1", 0, 1, 6) != null)
				System.err.println("placeNumber: valid value but incorrect (-1)");

			// valid and correct value (1)
			if (peer1.placeNumber("Challenge 1", 1, 1, 3) != null)
				System.err.println("placeNumber: valid and correct value (1)");

			// valid and correct value (1)
			if (peer1.placeNumber("Challenge 1", 0, 0, 7) != null)
				System.err.println("placeNumber: valid and correct value (1)");

			// valid and correct value but already filled (0)
			if (peer2.placeNumber("Challenge 1", 1, 1, 3) != null)
				System.err.println("placeNumber: valid and correct value but already filled (0)");

			matrix1 = peer1.getSudoku("Challenge 1");
			sudoku = convert(matrix1);
			System.out.println("vincenzo:\n" + sudoku);

			matrix2 = peer2.getSudoku("Challenge 1");
			sudoku = convert(matrix2);
			System.out.println("alessandra:\n" + sudoku);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 
	 * This utility method converts the matrix of integers into a {@link Grid}.
	 * @param sudoku is a matrix of integers
	 * @return an instance of this {@link Grid}
	 */
	private static Grid convert(Integer[][] sudoku)
	{
		int[][] converted = new int[9][9];
		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
				converted[i][j] = sudoku[i][j];
		}
		Grid sudokuGrid = Grid.of(converted);
		
		return sudokuGrid;
	}

}
