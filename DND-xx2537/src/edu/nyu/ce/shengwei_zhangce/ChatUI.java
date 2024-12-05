package edu.nyu.ce.shengwei_zhangce;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.Random;

public class ChatUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JButton enterButton;
	private JButton exitButton;
	private JButton diceButton; // 掷骰子按钮
	private JScrollPane chatScroll;
	private JScrollPane typeScroll;
	private JScrollPane userScroll;
	private JLabel chatLabel;
	private JLabel typeLabel;
	private JLabel userLabel;
	public static JTextArea chatTXT;
	public static JTextArea typeTXT;
	public static JTextArea userTXT;

	public static String msg;
	public static String clientName;
	public static DatagramSocket ds;
	public static MulticastSocket clientSocket;
	public static InetAddress targetAddr;

	private Timer diceTimer; // 定义Timer用于骰子旋转动画

	public ChatUI() {
		setTitle("Chat Room");
		setSize(600, 500);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setBackground(new Color(245, 245, 245));
		setLocationRelativeTo(null);

		initUI();
		setVisible(true);
	}

	private void initUI() {
		enterButton = new JButton("Send");
		enterButton.setFont(new Font("Arial", Font.BOLD, 16));
		enterButton.setBackground(new Color(72, 187, 120));
		enterButton.setForeground(Color.WHITE);
		enterButton.addActionListener(this::sendMessage);

		exitButton = new JButton("Exit");
		exitButton.setFont(new Font("Arial", Font.BOLD, 16));
		exitButton.setBackground(new Color(220, 53, 69));
		exitButton.setForeground(Color.WHITE);
		exitButton.addActionListener(this::exitChat);

		// 初始化掷骰子按钮
		diceButton = new JButton("Roll Dice");
		diceButton.setFont(new Font("Arial", Font.BOLD, 16));
		diceButton.setBackground(new Color(66, 135, 245));
		diceButton.setForeground(Color.WHITE);
		diceButton.addActionListener(this::startDiceAnimation); // 设置点击事件

		chatTXT = new JTextArea();
		chatTXT.setEditable(false);
		chatTXT.setFont(new Font("SansSerif", Font.PLAIN, 14));
		chatScroll = new JScrollPane(chatTXT);
		chatScroll.setBorder(BorderFactory.createTitledBorder("Chat"));

		typeTXT = new JTextArea(3, 30);
		typeTXT.setFont(new Font("SansSerif", Font.PLAIN, 14));
		typeScroll = new JScrollPane(typeTXT);
		typeScroll.setBorder(BorderFactory.createTitledBorder("Type your message"));

		userTXT = new JTextArea();
		userTXT.setEditable(false);
		userTXT.setFont(new Font("SansSerif", Font.PLAIN, 12));
		userScroll = new JScrollPane(userTXT);
		userScroll.setBorder(BorderFactory.createTitledBorder("Online Users"));

		chatLabel = new JLabel("Chat Room");
		chatLabel.setFont(new Font("Arial", Font.BOLD, 24));
		chatLabel.setHorizontalAlignment(SwingConstants.CENTER);

		// 使用 GroupLayout 布局
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.CENTER)
				.addComponent(chatLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
						.addComponent(diceButton, 100, 100, 100) // 添加掷骰子按钮
						.addGap(10)
						.addComponent(exitButton, 80, 80, 80)
						.addGap(10))
		);

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGap(10)
				.addComponent(chatLabel)
				.addGap(10)
				.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addComponent(chatScroll, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
						.addComponent(userScroll, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
				.addGap(10)
				.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(typeScroll, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(enterButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(diceButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
				ds.send(packet);
				chatTXT.append(msg + "\n");
				typeTXT.setText("");
			} catch (Exception exception) {
				JOptionPane.showMessageDialog(this, "Failed to send message", "Error", JOptionPane.ERROR_MESSAGE);
				exception.printStackTrace();
			}
		}
	}

	private void startDiceAnimation(ActionEvent e) {
		Random random = new Random();
		int[] counter = {0}; // 用于控制动画持续时间

		diceTimer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				int diceResult = random.nextInt(6) + 1;
				chatTXT.append(clientName + " is rolling... " + diceResult + "\n");
				counter[0]++;

				// 停止动画，显示最终结果
				if (counter[0] >= 10) { // 旋转10次后停止
					diceTimer.stop();
					showDiceResult(diceResult);
				}
			}
		});

		diceTimer.start(); // 启动动画
	}

	private void showDiceResult(int diceResult) {
		String resultMsg = clientName + " rolled a dice and got: " + diceResult;
		chatTXT.append(resultMsg + "\n");

		try {
			byte[] buffer = resultMsg.getBytes();
			InetAddress hostAddr = InetAddress.getByName(ChatRoomConstants.IP_ADDR_1);
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, hostAddr, ChatRoomConstants.PORT_1);
			ds.send(packet);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void exitChat(ActionEvent e) {
		try {
			String exitMsg = clientName + " has left the chat";
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
