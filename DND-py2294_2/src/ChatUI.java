import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.util.Random;

public class ChatUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JButton enterButton;
	private JButton exitButton;
	private JComboBox<String> diceComboBox; // ComboBox for dice selection
	private JScrollPane chatScroll;
	private JScrollPane typeScroll;
	private JScrollPane userScroll;
	private JLabel typeLabel;
	public static JTextArea chatTXT;
	public static JTextArea typeTXT;
	public static JTextArea multiTXT;
	public static JLabel usersLabel;
	public static String msg;
	public static String clientName;
	public static DatagramSocket ds;
	public static MulticastSocket clientSocket;
	public static InetAddress targetAddr;

	// Constructor
	public ChatUI() {
		setTitle("Chat Room");
		setSize(600, 500);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setBackground(Color.BLACK);
		setLocationRelativeTo(null);

		initUI();
		setVisible(true);
	}

	private void initUI() {
		// Buttons initialization
		enterButton = new JButton("Send");
		enterButton.setFont(new Font("Serif", Font.ITALIC, 18));
		enterButton.setBackground(Color.RED);
		enterButton.setForeground(Color.BLACK);
		enterButton.addActionListener(this::sendMessage);

		exitButton = new JButton("Exit");
		exitButton.setFont(new Font("Serif", Font.ITALIC, 18));
		exitButton.setBackground(Color.GRAY);
		exitButton.setForeground(Color.BLACK);
		exitButton.addActionListener(this::exitChat);

		// ComboBox for selecting dice type
		String[] diceOptions = {"4-sided", "6-sided", "8-sided", "10-sided", "12-sided", "20-sided"};
		diceComboBox = new JComboBox<>(diceOptions);  // Create ComboBox with dice options
		diceComboBox.setFont(new Font("Serif", Font.ITALIC, 16));
		diceComboBox.setBackground(new Color(66, 135, 245));
		diceComboBox.setForeground(Color.BLACK);
		diceComboBox.addActionListener(this::rollDice); // Add listener for dice selection

		// Chat interface components
		chatTXT = new JTextArea();
		chatTXT.setEditable(false);
		chatTXT.setFont(new Font("SansSerif", Font.PLAIN, 14));
		chatScroll = new JScrollPane(chatTXT);
		chatScroll.setBorder(BorderFactory.createTitledBorder("Chat"));

		typeTXT = new JTextArea(3, 30);
		typeTXT.setFont(new Font("SansSerif", Font.PLAIN, 14));
		String placeholder = "Role play here...";
		typeTXT.setForeground(Color.GRAY);
		typeTXT.setText(placeholder);
		typeTXT.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (typeTXT.getText().equals(placeholder)) {
					typeTXT.setText("");
					typeTXT.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (typeTXT.getText().isEmpty()) {
					typeTXT.setForeground(Color.GRAY);
					typeTXT.setText(placeholder);
				}
			}
		});
		typeScroll = new JScrollPane(typeTXT);

		multiTXT = new JTextArea();
		multiTXT.setEditable(false);
		multiTXT.setFont(new Font("SansSerif", Font.PLAIN, 12));
		userScroll = new JScrollPane(multiTXT);

		usersLabel = new JLabel("Online Users:");
		usersLabel.setFont(new Font("Serif", Font.BOLD, 24));
		usersLabel.setHorizontalAlignment(SwingConstants.CENTER);
		usersLabel.setForeground(Color.GRAY);

		// Layout setup
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.CENTER)
				.addComponent(usersLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(layout.createSequentialGroup()
						.addGap(10)
						.addComponent(chatScroll, 400, 400, 400)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(userScroll, 160, 160, 160)
						.addGap(10))
				.addGroup(layout.createSequentialGroup()
						.addGap(10)
						.addComponent(typeScroll, 400, 400, 400)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(enterButton, 80, 80, 80)
						.addGap(10)
						.addComponent(diceComboBox, 100, 100, 100)  // Place ComboBox in layout
						.addGap(10)
						.addComponent(exitButton, 80, 80, 80)
						.addGap(10))
		);

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGap(10)
				.addComponent(usersLabel)
				.addGap(10)
				.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addComponent(chatScroll, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
						.addComponent(userScroll, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
				.addGap(10)
				.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(typeScroll, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(enterButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(diceComboBox, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE) // Add ComboBox to layout
						.addComponent(exitButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGap(10)
		);

		pack();
	}

	private void sendMessage(ActionEvent e) {
		String txt = typeTXT.getText().trim();
		if (!txt.isEmpty()) {
			msg = clientName + ": " + txt;
			try {
				byte[] buffer = msg.getBytes();
				InetAddress hostAddr = InetAddress.getByName(ChatRoomConstants.IP_ADDR_1);
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, hostAddr, ChatRoomConstants.PORT_1);
				chatTXT.append(msg + "\n");
				typeTXT.setText("");
			} catch (Exception exception) {
				JOptionPane.showMessageDialog(this, "Failed to send message", "Error", JOptionPane.ERROR_MESSAGE);
				exception.printStackTrace();
			}
		}
	}

	// Method to simulate rolling a dice
	private void rollDice(ActionEvent e) {
		// Get the selected dice type from the ComboBox
		String selectedDice = (String) diceComboBox.getSelectedItem();
		int diceType = Integer.parseInt(selectedDice.split("-")[0]); // Extract the number before the "-" and convert to integer

		// Roll the selected dice
		Random random = new Random();
		int diceResult = random.nextInt(diceType) + 1; // Random dice roll between 1 and diceType

		// Prepare the result message
		String resultMsg = clientName + " rolled a " + selectedDice + " dice and got: " + diceResult;
		chatTXT.append(resultMsg + "\n");

		// Send the dice result over the network (optional)
		try {
			byte[] buffer = resultMsg.getBytes();
			InetAddress hostAddr = InetAddress.getByName(ChatRoomConstants.IP_ADDR_1);
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, hostAddr, ChatRoomConstants.PORT_1);
			//ds.send(packet); // Uncomment this to send the message over the network
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void exitChat(ActionEvent e) {
		try {
			String exitMsg = clientName + " has left the room";
			byte[] buffer = exitMsg.getBytes();
			InetAddress hostAddr = InetAddress.getByName(ChatRoomConstants.IP_ADDR_1);
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, hostAddr, ChatRoomConstants.PORT_1);
			ds.send(packet);
			clientSocket.leaveGroup(targetAddr);
			ds.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		setVisible(false);
		new ChatRoomMain().setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(ChatUI::new);
	}
}
