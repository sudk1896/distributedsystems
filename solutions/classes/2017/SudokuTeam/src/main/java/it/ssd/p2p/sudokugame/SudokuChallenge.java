package main.java.it.ssd.p2p.sudokugame;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

import net.tomp2p.peers.PeerAddress;

/**
 * The class that describes the Sudoku challenge.
 * The SudokuChallenge class contains:
 * <ul>
 * 	<li> the {@link Sudoku} </li>
 * 	<li> the list of peers that joined the Sudoku challenge </li>
 * 	<li> the list of scores of peers</li>
 * </ul>
 * 
 * @author Alessandra Orsi, Giulio Imperato, Vincenzo Nastro
 */
public class SudokuChallenge implements Serializable
{
	private static final long serialVersionUID = -605118408191390788L;

	private String challenge_name;

	private Solver solver;

	private Sudoku sudoku;
	private Sudoku sudokuSolved;

	private HashMap<PeerAddress, String> peers_on_game = new HashMap<PeerAddress, String>();
	private HashMap<PeerAddress, Integer> peers_score = new HashMap<PeerAddress, Integer>();

	/**
	 * Constructs a new SudokuChallenge instance.
	 * Create a Sudoku instance and a copy of this solved (sudokuSolved) 
	 * for compare the value that the user insert in it with the right value.
	 * @param _game_name the name of challenge
	 */
	public SudokuChallenge(String _game_name)
	{
		this.challenge_name = _game_name;

		int seed = 71; // default seed for testing

		// the snippet of code if you want to use a random seed for create a different Sudoku
		/*
		Random random = new Random();
		int seed = random.nextInt();
		*/
		
		sudoku = new Sudoku(_game_name, seed);
		sudokuSolved = new Sudoku(_game_name, seed);

		solver = sudoku.getSolver();
		solver.solve(sudokuSolved.getGrid());
	}

	/**
	 * The method adds a user into the challenge with initial score = 0.
	 * @param _nickname the nickname of the user that wants to join into the challenge
	 * @param peerAddress the address of the user that wants to join into the challenge
	 * @return return true if the nickname isn't used in this challenge, false otherwise
	 */
	public Boolean add(String _nickname, PeerAddress peerAddress)
	{
		if (peers_on_game.containsValue(_nickname))
			return false;

		peers_on_game.put(peerAddress, _nickname);
		peers_score.put(peerAddress, 0);
		return true;
	}

	public Sudoku getSudoku() {
		return sudoku;
	}

	public String getChallenge_name() {
		return challenge_name;
	}

	public Grid getSudokuSolved() {
		return sudokuSolved.getGrid();
	}

	public HashMap<PeerAddress, String> getPeersOnGame() {
		return peers_on_game;
	}

	public Integer getScoreOfPeer(PeerAddress peerAddress) {
		return peers_score.get(peerAddress);
	}
	
	public HashMap<PeerAddress, Integer> getPeersScore() {
		return peers_score;
	}

	public Integer setScoreOfPeer(PeerAddress peerAddress, Integer value)
	{
		Integer newScore = peers_score.get(peerAddress) + value;
		peers_score.put(peerAddress, newScore);
		return newScore;
	}
}
