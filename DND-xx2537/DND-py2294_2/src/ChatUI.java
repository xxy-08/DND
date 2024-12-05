import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import java.net.*;
import java.util.Random;

public class ChatUI extends JFrame {

	public static final long serialVersionUID = 1L;

	public static JButton enterButton, exitButton, searchButton;
	public static JComboBox<String> diceComboBox;
	public static JScrollPane chatScroll, typeScroll, userScroll;
	public static JLabel typeLabel, usersLabel;
	public static JTextArea chatTXT, typeTXT, multiTXT;
	public static JTextField searchField, diceCountField;
	public static String msg;
	public static String clientName = "User";
	public static DatagramSocket ds;
	public static MulticastSocket clientSocket;
	public static InetAddress targetAddr;

	public ChatUI() {
		setTitle("Chat Room");
		setSize(700, 500);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setBackground(Color.BLACK);
		setLocationRelativeTo(null);

		initUI();
		setVisible(true);
	}
	// 创建按钮时设置相同的尺寸
	private JButton createButton(String text, int width, int height) {
		JButton button = new JButton(text);
		button.setFont(new Font("Serif", Font.ITALIC, 18));
		button.setBackground(Color.RED);
		button.setForeground(Color.BLACK);
		button.setPreferredSize(new Dimension(width, height)); // 设置相同的大小
		return button;
	}

	private void initUI() {
		// 使用 createButton 方法创建按钮并设置尺寸为 100x40
		enterButton = createButton("Send", 100, 40);
		enterButton.addActionListener(this::sendMessage);

		exitButton = createButton("Exit", 100, 40);
		exitButton.setBackground(Color.GRAY); // 可以设置每个按钮不同的颜色
		exitButton.addActionListener(this::exitChat);

		String[] diceOptions = {"4-sided", "6-sided", "8-sided", "10-sided", "12-sided", "20-sided"};
		diceComboBox = new JComboBox<>(diceOptions);
		diceComboBox.setFont(new Font("Serif", Font.ITALIC, 16));
		diceComboBox.setPreferredSize(new Dimension(100, 40)); // 设置 JComboBox 大小为 100x40

		diceCountField = new JTextField("1", 5);
		diceCountField.setFont(new Font("Serif", Font.ITALIC, 16));

		// 使用 createButton 创建骰子按钮
		JButton rollButton = createButton("Roll Dice", 100, 40);
		rollButton.addActionListener(this::rollDice);

		chatTXT = new JTextArea();
		chatTXT.setEditable(false);
		chatTXT.setFont(new Font("SansSerif", Font.PLAIN, 14));
		chatScroll = new JScrollPane(chatTXT);

		typeTXT = new JTextArea(3, 30);
		typeScroll = new JScrollPane(typeTXT);

		multiTXT = new JTextArea();
		multiTXT.setEditable(false);
		userScroll = new JScrollPane(multiTXT);

		usersLabel = new JLabel("Online Users:");
		usersLabel.setFont(new Font("Serif", Font.BOLD, 24));
		usersLabel.setForeground(Color.GRAY);

		searchField = new JTextField(15);
		searchButton = createButton("Search", 100, 40);
		searchButton.addActionListener(this::searchMessage);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(usersLabel)
				.addGroup(layout.createSequentialGroup()
						.addGap(10)
						.addComponent(chatScroll, 400, 400, 400)
						.addComponent(userScroll, 160, 160, 160)
						.addGap(10))
				.addGroup(layout.createSequentialGroup()
						.addComponent(searchField)
						.addComponent(searchButton))
				.addGroup(layout.createSequentialGroup()
						.addComponent(typeScroll)
						.addComponent(enterButton)
						.addComponent(diceComboBox)
						.addComponent(diceCountField)
						.addComponent(rollButton)
						.addComponent(exitButton))
		);

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(usersLabel)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(chatScroll)
						.addComponent(userScroll))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(searchField)
						.addComponent(searchButton))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
						.addComponent(typeScroll)
						.addComponent(enterButton)
						.addComponent(diceComboBox)
						.addComponent(diceCountField)
						.addComponent(rollButton)
						.addComponent(exitButton))
		);

		pack();
		setSize(700, 550);
		diceComboBox.setPreferredSize(new Dimension(100, 40));
		diceCountField.setFont(new Font("Serif", Font.ITALIC, 16));
	}

	private void searchMessage(ActionEvent e) {
		String keyword = searchField.getText().trim();
		if (keyword.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter a keyword.", "Info", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		try {
			Highlighter highlighter = chatTXT.getHighlighter();
			highlighter.removeAllHighlights();
			String text = chatTXT.getText();
			int index = text.indexOf(keyword);
			while (index >= 0) {
				int end = index + keyword.length();
				highlighter.addHighlight(index, end, new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW));
				index = text.indexOf(keyword, end);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error highlighting text.", "Error", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	private void sendMessage(ActionEvent e) {
		String txt = typeTXT.getText().trim();
		if (!txt.isEmpty()) {
			msg = clientName + ": " + txt;
			chatTXT.append(msg + "\n");
			typeTXT.setText("");
		}
	}

	private void rollDice(ActionEvent e) {
		String selectedDice = (String) diceComboBox.getSelectedItem();
		int diceType = Integer.parseInt(selectedDice.split("-")[0]);
		int times;
		try {
			times = Integer.parseInt(diceCountField.getText().trim());
			if (times <= 0) {
				throw new NumberFormatException("Non-positive number.");
			}
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Please enter a valid positive number for dice rolls.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		Random random = new Random();
		StringBuilder resultMsg = new StringBuilder(clientName + " rolled " + times + " times with a " + selectedDice + " dice: ");
		for (int i = 0; i < times; i++) {
			int diceResult = random.nextInt(diceType) + 1;
			resultMsg.append(diceResult).append(i == times - 1 ? "." : ", ");
		}
		chatTXT.append(resultMsg.toString() + "\n");
	}

	private void exitChat(ActionEvent e) {
		// Notify others that the user has left the chat
		String txt = clientName + " exits from the chatting room";
		byte[] buffer = txt.getBytes();

		try {
			// Send message to the first address
			InetAddress hostAddr1 = InetAddress.getByName(ChatRoomConstants.IP_ADDR_1);
			DatagramPacket p1 = new DatagramPacket(buffer, buffer.length, hostAddr1, ChatRoomConstants.PORT_1);
			ds.send(p1);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		try {
			// Send message to the second address
			InetAddress hostAddr2 = InetAddress.getByName(ChatRoomConstants.IP_ADDR_2);
			DatagramPacket p2 = new DatagramPacket(buffer, buffer.length, hostAddr2, ChatRoomConstants.PORT_2);
			ds.send(p2);

			// Leave the multicast group and close the socket
			clientSocket.leaveGroup(targetAddr);
			ds.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		// Hide the current chat window and show the main chat window (or wherever you want to redirect)
		setVisible(false);

		// Show the main chat room interface
		ChatRoomMain chatMain = new ChatRoomMain();
		chatMain.setVisible(true);
	}


	public static void main(String[] args) {
		SwingUtilities.invokeLater(ChatUI::new);
	}
}
