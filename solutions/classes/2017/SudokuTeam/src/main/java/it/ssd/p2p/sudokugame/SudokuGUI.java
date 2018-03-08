package main.java.it.ssd.p2p.sudokugame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SudokuGUI
{
	final private SudokuGameImpl peer;
	private String nickname;

	private JFrame frame = new JFrame("SudokuGame");

	private JTextField cells[][] = new JTextField[9][9];
	public static final Font FONT_NUMBERS = new Font("Monospaced", Font.BOLD, 20);

	private GridPanel gridPanel = new GridPanel(new GridLayout(9, 9, 1, 1));

	private List listChallenge = new List();
	private List listChallengesJoined = new List();
	private List listMessages = new List();

	private String currentChallenge;

	public SudokuGUI(final SudokuGameImpl peer, String nickname)
	{
		this.peer = peer;
		this.nickname = nickname;

		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				cells[i][j] = new JTextField();
				cells[i][j].setFont(FONT_NUMBERS);
				cells[i][j].setForeground(Color.BLACK);
				cells[i][j].setBackground(Color.WHITE);
				cells[i][j].setHorizontalAlignment(JTextField.CENTER);
				cells[i][j].addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						((JTextField) e.getSource()).setBackground(Color.CYAN);
					}
				});
				cells[i][j].addFocusListener(new FocusListener() {

					public void focusLost(FocusEvent e) {
						((JTextField) e.getSource()).setBackground(Color.WHITE);
					}

					public void focusGained(FocusEvent e) { }
				});

				gridPanel.add(cells[i][j]);
			}
		}
	}

	public void createGUI()
	{
		JPanel mainPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();

		JPanel leftVerticalPanel = new JPanel(new GridBagLayout());

		gridBagConstraints.weighty = 1;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.anchor = GridBagConstraints.NORTH;

		JLabel labelSearchChallenge = new JLabel("Search Challenge", JLabel.CENTER);
		labelSearchChallenge.setOpaque(true);
		labelSearchChallenge.setFont(new Font("Helvetica", Font.BOLD, 15));
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.ipady = 20;
		leftVerticalPanel.add(labelSearchChallenge, gridBagConstraints);

		TextField searchField = new TextField();
		searchField.setText("name of challenge");
		searchField.setForeground(Color.gray);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.ipady = 20;
		leftVerticalPanel.add(searchField, gridBagConstraints);
		searchField.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String game_name = ((TextField) e.getSource()).getText();
				if (peer.getSudoku(game_name) != null)
					listChallenge.add(game_name);
				else	
					JOptionPane.showMessageDialog(frame, "Challenge not exists");
			}
		});

		JLabel labelCreate = new JLabel("Create a New Challenge", JLabel.CENTER);
		labelCreate.setFont(new Font("Helvetica", Font.BOLD, 15));
		labelCreate.setOpaque(true);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.ipady = 20;
		leftVerticalPanel.add(labelCreate, gridBagConstraints);

		JButton createButton = new JButton("Create Challenge");
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.ipadx = 20;
		leftVerticalPanel.add(createButton, gridBagConstraints);
		createButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String game_name = JOptionPane.showInputDialog(
						frame,
						"Insert the name of new challenge:",
						"Create Challenge",
						JOptionPane.PLAIN_MESSAGE);

				if (game_name != null && !game_name.equals(""))
				{
					if (peer.getSudoku(game_name) == null)
					{
						peer.generateNewSudoku(game_name);
						listChallenge.add(game_name);
					}
					else
					{
						JOptionPane.showMessageDialog(frame, "Challenge already exists");
					}
				}
			}
		});

		JLabel labelChallenges = new JLabel("Sudoku Challenges", JLabel.CENTER);
		labelChallenges.setFont(new Font("Helvetica", Font.BOLD, 15));
		labelChallenges.setOpaque(true);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.ipady = 20;
		leftVerticalPanel.add(labelChallenges, gridBagConstraints);

		listChallenge.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int index = listChallenge.getSelectedIndex();
				String game_name = listChallenge.getItems()[index];

				if (peer.join(game_name, nickname))
				{
					listChallengesJoined.add(game_name);
				}
				currentChallenge = game_name;

				int i = 0;
				int j = 0;
				for (Integer[] row : peer.getSudoku(game_name))
				{
					for (Integer value : row)
					{
						cells[i][j].setText("" + (value != 0?value: ""));
						cells[i][j].setBackground(value != 0 ? Color.LIGHT_GRAY: Color.WHITE);
						cells[i][j].setEditable(value != 0 ? false: true);
						cells[i][j].setFocusable(value != 0 ? false: true);
						j++;
					}
					j = 0;
					i++;
				}
			}
		});

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.ipady = 20;
		leftVerticalPanel.add(listChallenge, gridBagConstraints);

		JLabel labelChallengesJoined = new JLabel("Challenges you have joined", JLabel.CENTER);
		labelChallengesJoined.setOpaque(true);
		labelChallengesJoined.setFont(new Font("Helvetica", Font.BOLD, 15));
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		leftVerticalPanel .add(labelChallengesJoined, gridBagConstraints);

		listChallengesJoined = new List();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		leftVerticalPanel.add(listChallengesJoined, gridBagConstraints);
		listChallengesJoined.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int index = listChallengesJoined.getSelectedIndex();
				String game_name = listChallengesJoined.getItems()[index];

				currentChallenge = game_name;

				int i = 0;
				int j = 0;
				for (Integer[] row : peer.getSudoku(game_name))
				{
					for (Integer value : row)
					{
						cells[i][j].setText("" + (value != 0?value: ""));
						cells[i][j].setBackground(value != 0 ? Color.LIGHT_GRAY: Color.WHITE);
						cells[i][j].setEditable(value != 0 ? false: true);
						j++;
					}
					j = 0;
					i++;
				}
			}
		});

		JLabel labelMessages = new JLabel("Messages", JLabel.CENTER);
		labelMessages.setOpaque(true);
		labelMessages.setFont(new Font("Helvetica", Font.BOLD, 15));
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		leftVerticalPanel.add(labelMessages, gridBagConstraints);

		TimerTask tasknew = new TimerTask() {

			@Override
			public void run() {

				listMessages.removeAll();

				for (Object message : peer.getMessages()) {
					listMessages.add(message.toString());
				}

			}
		};

		Timer timer = new Timer();
		timer.schedule(tasknew, 5000, 5000);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		leftVerticalPanel.add(listMessages, gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		mainPanel.add(leftVerticalPanel, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.weighty = 3;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		mainPanel.add(gridPanel, gridBagConstraints);

		cells[0][0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 0, 0, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[0][1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 0, 1, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[0][2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 0, 2, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[0][3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 0, 3, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[0][4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 0, 4, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[0][5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 0, 5, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[0][6].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 0, 6, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[0][7].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 0, 7, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[0][8].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 0, 8, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[1][0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 1, 0, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[1][1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 1, 1, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[1][2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 1, 2, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[1][3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 1, 3, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[1][4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 1, 4, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[1][5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 1, 5, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[1][6].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 1, 6, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[1][7].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 1, 7, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[1][8].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 1, 8, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[2][0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 2, 0, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[2][1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 2, 1, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[2][2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 2, 2, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[2][3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 2, 3, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[2][4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 2, 4, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[2][5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 2, 5, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[2][6].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 2, 6, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[2][7].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 2, 7, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[2][8].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 2, 8, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[3][0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 3, 0, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[3][1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 3, 1, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[3][2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 3, 2, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[3][3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 3, 3, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[3][4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 3, 4, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[3][5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 3, 5, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[3][6].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 3, 6, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[3][7].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 3, 7, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[3][8].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 3, 8, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[4][0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 4, 0, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[4][1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 4, 1, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[4][2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 4, 2, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[4][3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 4, 3, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[4][4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 4, 4, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[4][5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 4, 5, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[4][6].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 4, 6, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[4][7].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 4, 7, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[4][8].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 4, 8, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[5][0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 5, 0, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[5][1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 5, 1, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[5][2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 5, 2, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[5][3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 5, 3, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[5][4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 5, 4, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[5][5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 5, 5, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[5][6].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 5, 6, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[5][7].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 5, 7, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[5][8].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 5, 8, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[6][0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 6, 0, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[6][1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 6, 1, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[6][2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 6, 2, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[6][3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 6, 3, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[6][4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 6, 4, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[6][5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 6, 5, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[6][6].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 6, 6, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[6][7].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 6, 7, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[6][8].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 6, 8, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[7][0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 7, 0, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[7][1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 7, 1, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[7][2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 7, 2, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[7][3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 7, 3, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[7][4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 7, 4, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[7][5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 7, 5, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[7][6].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 7, 6, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[7][7].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 7, 7, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[7][8].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 7, 8, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[8][0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 8, 0, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[8][1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 8, 1, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[8][2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 8, 2, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[8][3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 8, 3, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[8][4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 8, 4, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[8][5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 8, 5, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[8][6].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 8, 6, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[8][7].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 8, 7, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});
		cells[8][8].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer value = peer.placeNumber(currentChallenge, 8, 8, Integer.parseInt(((JTextField)e.getSource()).getText()));
				if (value == null) JOptionPane.showMessageDialog(frame, "Number not valid for cell");
			}
		});


		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1100,600);
		frame.setResizable(false);
		frame.getContentPane().add(mainPanel);
		frame.setLocationRelativeTo(null);
		frame.setMinimumSize(new Dimension(300,300));
		frame.setVisible(true);
	}

	public class GridPanel extends JPanel
	{
		private static final long serialVersionUID = 5524167100588248534L;

		GridPanel(GridLayout layout) {
			super(layout);
		}

		public void paintComponent(Graphics g)
		{
			g.fillRect(getWidth()/3 - 1, 0, 3, getHeight());
			g.fillRect(2*getWidth()/3 - 1, 0, 3, getHeight());
			g.fillRect(0, getHeight()/3 - 1, getWidth(), 3);
			g.fillRect(0, 2*getHeight()/3 - 2, getWidth(), 3);
		}
	}
}
