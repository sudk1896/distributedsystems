package test.java.it.ssd.p2p.sudokugame;

import main.java.it.ssd.p2p.sudokugame.MessageListenerImpl;
import main.java.it.ssd.p2p.sudokugame.SudokuGameImpl;
import junit.framework.TestCase;

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

/** 
 * This class is used to do our unit tests.
 * @author Alessandra Orsi, Giulio Imperato, Vincenzo Nastro
 */
public class SudokuGameTest extends TestCase
{
	@SuppressWarnings("unused")
	private static SudokuGameImpl peer0;
	private static SudokuGameImpl peer1;
	private static SudokuGameImpl peer2;
	private static SudokuGameImpl peer3;

	@Override
	protected void setUp() throws Exception
	{
		peer0 = new SudokuGameImpl(0, "127.0.0.1", new MessageListenerImpl(0));
		peer1 = new SudokuGameImpl(1, "127.0.0.1", new MessageListenerImpl(1));
		peer2 = new SudokuGameImpl(2, "127.0.0.1", new MessageListenerImpl(2));
		peer3 = new SudokuGameImpl(3, "127.0.0.1", new MessageListenerImpl(3));
	}

	/****************************************************************
	 * test for method generateNewSudoku
	 ***************************************************************/
	public void testGenerateDuplicateSudoku()
	{
		Integer[][] matrix1 = peer1.generateNewSudoku("Challenge 1");
		assertEquals(true, (matrix1!=null));
		Integer[][] matrix2 = peer1.generateNewSudoku("Challenge 1");
		assertEquals(false, (matrix2!=null));
	}

	/****************************************************************
	 * tests for method join
	 ***************************************************************/

	public void testJoin()
	{
		Integer[][] matrix = peer1.generateNewSudoku("Challenge 2");
		assertEquals(true, (matrix!=null));
		boolean isJoined = peer1.join("Challenge 2", "peer1");
		assertTrue(isJoined);
	}

	public void testSudokuNotExists()
	{
		boolean isJoined = peer1.join("Challenge 4", "peer1");
		assertFalse(isJoined);
	}

	public void testJoinNicknameDuplicated()
	{
		Integer[][] matrix = peer1.generateNewSudoku("Challenge 3");
		assertEquals(true, (matrix!=null));

		boolean isJoined = peer1.join("Challenge 3", "peer1");
		assertTrue(isJoined);

		boolean isJoined2 = peer2.join("Challenge 3", "peer1");
		assertFalse(isJoined2);
	}

	/****************************************************************
	 * tests for method placeNumber
	 ***************************************************************/

	public void testPlaceNumberChallengeNotExists()
	{
		boolean isJoined = peer1.join("ChallengeNotExists", "peer1");
		assertFalse(isJoined);
		assertNull(peer1.placeNumber("ChallengeNotExists", 0, 0, 1));
	}

	// placeNumber for invalid values
	public void testInvalidValueForCell()
	{
		Integer[][] matrix = peer1.generateNewSudoku("placeNumber1");
		assertEquals(true, (matrix!=null));

		boolean isJoined = peer3.join("placeNumber1", "peer3");
		assertTrue(isJoined);

		assertNull(peer3.placeNumber("placeNumber1", 0, 0, 3));
	}

	// placeNumber for valid value but incorrect (-1)
	public void testValidValueButNotCorrect()
	{
		Integer[][] matrix = peer1.generateNewSudoku("placeNumber2");
		assertEquals(true, (matrix!=null));

		boolean isJoined = peer3.join("placeNumber2", "peer3");
		assertTrue(isJoined);

		Integer value = -1;
		assertEquals(value, peer3.placeNumber("placeNumber2", 1, 4, 5));
	}

	// placeNumber for valid and correct value (1)
	public void testCorrectValue()
	{
		Integer[][] matrix = peer1.generateNewSudoku("placeNumber3");
		assertEquals(true, (matrix!=null));

		boolean isJoined = peer3.join("placeNumber3", "peer3");
		assertTrue(isJoined);

		Integer value = 1;
		assertEquals(value, peer3.placeNumber("placeNumber3", 1, 1, 3));
	}

	// placeNumber for valid and correct value but already filled (0)
	public void testCorrectValueforNotEmptyCell()
	{
		Integer[][] matrix = peer1.generateNewSudoku("placeNumber4");
		assertEquals(true, (matrix!=null));

		boolean isJoined = peer3.join("placeNumber4", "peer3");
		assertTrue(isJoined);

		// first time
		Integer value = 1;
		assertEquals(value, peer3.placeNumber("placeNumber4", 2, 0, 8));

		// second time
		value = 0;
		assertEquals(value, peer3.placeNumber("placeNumber4", 2, 0, 8));
	}
}
