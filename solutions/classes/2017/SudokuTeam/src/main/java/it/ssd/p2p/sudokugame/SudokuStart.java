package main.java.it.ssd.p2p.sudokugame;

public class SudokuStart
{
	public static void main (String args[]) throws Exception
	{
		Integer peerID = Integer.parseInt(args[0]);

		SudokuGameImpl peer = new SudokuGameImpl(peerID, "127.0.0.1", new MessageListenerImpl(peerID));

		if (peerID != 0)
		{
			String nickname = args[1];
			SudokuGUI gui = new SudokuGUI(peer, nickname);
			gui.createGUI();
		}	
	}
}