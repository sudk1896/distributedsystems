package main.java.it.ssd.p2p.sudokugame;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDirect;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;

/**
 * The class that implements the {@link SudokuGame} interface
 * @author Alessandra Orsi, Giulio Imperato, Vincenzo Nastro
 */
public class SudokuGameImpl implements SudokuGame
{
	final private Peer peer;
	final private PeerDHT dht;
	final private static int DEFAULT_MASTER_PORT = 4000;

	boolean result = false;

	// The hashmap that contains the local challenges of user, key = name of challenge & value = instance of SudokuChallenge
	final private HashMap<String, SudokuChallenge> challenges = new HashMap<String, SudokuChallenge>();

	final private ArrayList<Object> messages = new ArrayList<Object>();

	public SudokuGameImpl(int peerId, String _master_peer, final MessageListener listener) throws Exception
	{
		peer = new PeerBuilder(Number160.createHash(peerId)).ports(DEFAULT_MASTER_PORT + peerId).start();
		dht = new PeerBuilderDHT(peer).start();

		FutureBootstrap futureBootstrap = peer.bootstrap().inetAddress(InetAddress.getByName(_master_peer)).ports(DEFAULT_MASTER_PORT).start();
		futureBootstrap.awaitUninterruptibly();

		if (futureBootstrap.isSuccess())
		{
			peer.discover().peerAddress(futureBootstrap.bootstrapTo().iterator().next())
			.start().awaitUninterruptibly();
		}

		peer.objectDataReply(new ObjectDataReply()
		{
			public Object reply(PeerAddress sender, Object request) throws Exception {
				messages.add(request);
				return listener.parseMessage(request);
			}
		});
	}

	public Integer[][] generateNewSudoku(String _game_name)
	{
		try
		{
			FutureGet futureGet = dht.get(Number160.createHash(_game_name)).start();
			futureGet.awaitUninterruptibly();

			if (futureGet.isSuccess() && futureGet.isEmpty())
			{
				SudokuChallenge sudokuChallenge = new SudokuChallenge(_game_name);
				dht.put(Number160.createHash(_game_name)).data(new Data(sudokuChallenge)).start().awaitUninterruptibly();
				return sudokuChallenge.getSudoku().getMatrix();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public boolean join(String _game_name, String _nickname)
	{
		try
		{
			FutureGet futureGet = dht.get(Number160.createHash(_game_name)).start();
			futureGet.awaitUninterruptibly();

			if (futureGet.isSuccess())
			{
				if(futureGet.isEmpty() )
					return false;

				SudokuChallenge sudokuChallenge;
				sudokuChallenge = (SudokuChallenge) futureGet.dataMap().values().iterator().next().object();

				if (!sudokuChallenge.add(_nickname, dht.peer().peerAddress()))
					return false;

				dht.put(Number160.createHash(_game_name)).data(new Data(sudokuChallenge)).start().awaitUninterruptibly();

				String message = _nickname + " join in " + _game_name;
				sendMessage(message, sudokuChallenge);

				challenges.put(_game_name, sudokuChallenge);
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public Integer[][] getSudoku(String _game_name)
	{
		try
		{
			FutureGet futureGet = dht.get(Number160.createHash(_game_name)).start();
			futureGet.awaitUninterruptibly();

			if (futureGet.isSuccess() && !futureGet.isEmpty() )
			{
				SudokuChallenge sudokuChallenge;
				sudokuChallenge = (SudokuChallenge) futureGet.dataMap().values().iterator().next().object();

				return sudokuChallenge.getSudoku().getMatrix();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public Integer placeNumber(String _game_name, int _i, int _j, int _number)
	{
		try
		{
			FutureGet futureGet = dht.get(Number160.createHash(_game_name)).start();
			futureGet.awaitUninterruptibly();

			if (futureGet.isSuccess() && !futureGet.isEmpty())
			{
				SudokuChallenge sudokuChallenge;
				sudokuChallenge = (SudokuChallenge) futureGet.dataMap().values().iterator().next().object();

				Grid grid = sudokuChallenge.getSudoku().getGrid();
				if (!grid.isValidValueForCell(grid.getCell(_i, _j), _number))
					return null;

				// if the _number is correct...
				if (sudokuChallenge.getSudokuSolved().getCell(_i, _j).getValue() == _number)
				{
					// if the cell is empty and value is correct
					if(sudokuChallenge.getSudoku().getMatrix()[_i][_j] == 0)
					{
						// local Sudoku challenge
						challenges.get(_game_name).getSudoku().getMatrix()[_i][_j] = _number;

						// Sudoku challenge in DHT
						sudokuChallenge.getSudoku().getMatrix()[_i][_j] = _number;

						// score update
						sudokuChallenge.setScoreOfPeer(peer.peerAddress(), 1);
						
						// decrements the number of empty cells to check if the game is over
						int currentNumberOfEmptyCells =  sudokuChallenge.getSudoku().getNumberOfEmptyCells();
						sudokuChallenge.getSudoku().setNumberOfEmptyCells(currentNumberOfEmptyCells - 1);

						// put the Sudoku challenge updated in DHT
						dht.put(Number160.createHash(_game_name)).data(new Data(sudokuChallenge)).start().awaitUninterruptibly();

						String nickname = sudokuChallenge.getPeersOnGame().get(peer.peerAddress());

						// send the message to the other peers
						sendMessage(_game_name + ": (+1) " + nickname + " score " + sudokuChallenge.getScoreOfPeer(peer.peerAddress()), sudokuChallenge);

						// check if the challenge is over and elect the winner
						if (verifyChallengeEnded(sudokuChallenge) == true)
						{
							String winner = victory(sudokuChallenge);
							sendMessage(_game_name + ": the winner is " + winner, sudokuChallenge);
						}

						return 1;
					}
					else // if the cell is not empty and value is correct
					{
						// local Sudoku challenge
						challenges.get(_game_name).getSudoku().getMatrix()[_i][_j] = _number;

						// the score isn't updated!

						return 0;
					}
				}
				else // if the _number is wrong...
				{
					// local Sudoku challenge
					challenges.get(_game_name).getSudoku().getMatrix()[_i][_j] = _number;

					// score update
					sudokuChallenge.setScoreOfPeer(peer.peerAddress(), -1);

					// put the Sudoku challenge updated in DHT
					dht.put(Number160.createHash(_game_name)).data(new Data(sudokuChallenge)).start().awaitUninterruptibly();

					String nickname = sudokuChallenge.getPeersOnGame().get(peer.peerAddress());

					// send the message to the other peers
					sendMessage(_game_name + ": (-1) " + nickname + " score " + sudokuChallenge.getScoreOfPeer(peer.peerAddress()), sudokuChallenge);
					return -1;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}


	/**
	 * 
	 * The method verify if the challenge is finished.
	 * @param sudokuChallenge the {@link SudokuChallenge} of {@link Sudoku}.
	 * @return true if the challenge is finished, false otherwise.
	 */
	private boolean verifyChallengeEnded(SudokuChallenge sudokuChallenge)
	{
		if (sudokuChallenge.getSudoku().getNumberOfEmptyCells() == 0)
			return true;

		return false;
	}

	/**
	 * The method for send message to other peers.
	 * @param message the message to send.
	 * @param sudokuChallenge the {@link SudokuChallenge} of {@link Sudoku}.
	 */
	private void sendMessage(String message, SudokuChallenge sudokuChallenge)
	{
		for (PeerAddress peerAddress: sudokuChallenge.getPeersOnGame().keySet())
		{
			if (dht.peer().peerAddress() != peerAddress)
			{
				FutureDirect futureDirect = dht.peer().sendDirect(peerAddress).object(message).start();
				futureDirect.awaitUninterruptibly();
			}
		}
	}

	public ArrayList<Object> getMessages() {
		return messages;
	}

	public HashMap<String, SudokuChallenge> getChallenges() {
		return challenges;
	}


	/**
	 * This method is used to elect the winner or winners (in case there are equal scores).
	 * @param sudokuChallenge the {@link SudokuChallenge} of {@link Sudoku}.
	 * @return the names of the winners.
	 */
	private String victory(SudokuChallenge sudokuChallenge)
	{
		String winner = "";
		boolean flag = true;
		int maxScore = 0;

		Set<Entry<PeerAddress, Integer>> allPlayers = sudokuChallenge.getPeersScore().entrySet();

		for(Entry<PeerAddress, Integer> player : allPlayers)
		{
			// first player
			if (flag)
			{
				winner = sudokuChallenge.getPeersOnGame().get(player.getKey());
				maxScore = player.getValue();
				flag = false;
			}
			else
			{
				// other player
				if (player.getValue() > maxScore)
				{
					winner = sudokuChallenge.getPeersOnGame().get(player.getKey());
					maxScore = player.getValue();
				}
				else if (player.getValue() == maxScore)
				{
					winner = winner + ", " + sudokuChallenge.getPeersOnGame().get(player.getKey());
				}
			}

		}
		return winner;
	}

}