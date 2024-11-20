import java.util.logging.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChatRoomMain extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates the Chat application
	 */
	public ChatRoomMain() {

		this.setTitle("ChatMainRoom");
		this.setUndecorated(false);  // 显示窗口边框
		start();
		this.setLocationRelativeTo(null);
		setVisible(true);
		setAlwaysOnTop(true);
	}

	// 定义按钮和标签
	private JButton loginButton;
	private JButton exitButton;
	private JLabel titleLabel;

	/**
	 * This method will be used in the constructor to initialize the form.
	 */
	private void start() {
		titleLabel = new JLabel("Welcome to  D&D Chat Room");
		loginButton = new JButton();
		exitButton = new JButton();

		// 设置登录按钮
		loginButton.setFont(new Font("Arial", Font.BOLD, 20));
		loginButton.setText("Login");
		loginButton.setPreferredSize(new Dimension(200, 50));
		loginButton.setBackground(new Color(78, 145, 78, 203));  // 设置按钮背景色
		loginButton.setForeground(Color.WHITE);  // 设置按钮字体颜色
		loginButton.setFocusPainted(false);  // 去掉按钮边框
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				setVisible(false);
				new UserClient();  // 登录后创建新的客户端窗口
			}
		});

		// 设置退出按钮
		exitButton.setFont(new Font("Arial", Font.PLAIN, 18));
		exitButton.setText("Exit");
		exitButton.setPreferredSize(new Dimension(200, 50));
		exitButton.setBackground(new Color(218, 95, 95));  // 设置按钮背景色
		exitButton.setForeground(Color.WHITE);  // 设置按钮字体颜色
		exitButton.setFocusPainted(false);  // 去掉按钮边框
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				System.exit(0);  // 退出应用程序
			}
		});

		// 设置标题样式
		titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
		titleLabel.setForeground(new Color(115, 115, 214));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

		// 使用 GroupLayout 布局管理器
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);

		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(titleLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(layout.createSequentialGroup()
								.addComponent(loginButton, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
								.addGap(30)
								.addComponent(exitButton, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
						)
		);

		layout.setVerticalGroup(
				layout.createSequentialGroup()
						.addGap(40)
						.addComponent(titleLabel)
						.addGap(30)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(loginButton)
								.addComponent(exitButton)
						)
						.addGap(30)
		);

		pack();
	}

	public static void main(String args[]) {
		System.out.println("Hello World!");

		try {
			for (UIManager.LookAndFeelInfo text : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(text.getName())) {
					UIManager.setLookAndFeel(text.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
			Logger.getLogger(ChatRoomMain.class.getName()).log(Level.SEVERE, null, ex);
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new ChatRoomMain().setVisible(true);
			}
		});
	}
}
