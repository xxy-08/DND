import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.*;

public class ChatRoomMain extends JFrame {

    private static final long serialVersionUID = 1L;
    private Image backgroundImage;

    /**
     * Creates the Chat application
     */
    public ChatRoomMain() {
        this.setTitle("ChatMainRoom");
        this.setUndecorated(false);
        start();
        this.setLocationRelativeTo(null);
        setVisible(true);
        setAlwaysOnTop(true);
    }

    private JButton loginButton;
    private JButton exitButton;
    private JLabel titleLabel;

    /**
     * This method will be used in the constructor to initialize the form.
     */
    private void start() {
        titleLabel = new JLabel("Welcome to the Dungeons");
        loginButton = new JButton();
        exitButton = new JButton();

        // Load the background image (logo.png)
        backgroundImage = new ImageIcon("../logo.png").getImage();

        // Set font and background for buttons
        loginButton.setFont(new Font("Serif", Font.CENTER_BASELINE, 16));  // Smaller font size
        loginButton.setText("Login");
        loginButton.setPreferredSize(new Dimension(120, 40));  // Smaller button size
        loginButton.setBackground(Color.RED);
        loginButton.setForeground(Color.BLACK);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);  // Remove border
        loginButton.setSize(120, 40);  // Set explicit size
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                dispose();  // Close this window
                new UserClient();  // Continue with the UserClient logic
            }
        });

        exitButton.setFont(new Font("Serif", Font.CENTER_BASELINE, 16));  // Smaller font size
        exitButton.setText("Exit");
        exitButton.setPreferredSize(new Dimension(120, 40));  // Smaller button size
        exitButton.setBackground(Color.GRAY);
        exitButton.setForeground(Color.BLACK);
        exitButton.setFocusPainted(false);
        exitButton.setBorderPainted(false);  // Remove border
        exitButton.setSize(120, 40);  // Set explicit size
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                System.exit(0);  // Exit the program
            }
        });

        titleLabel.setFont(new Font("Serif", Font.ITALIC, 30));
        titleLabel.setForeground(Color.RED);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create a custom JPanel to display the background image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Set the background color to black
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());
                // Scale the background image to fit the window size
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        // Set the layout for the background panel
        GroupLayout layout = new GroupLayout(backgroundPanel);
        backgroundPanel.setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(titleLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(loginButton, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)  // Adjusted size
                    .addGap(30)
                    .addComponent(exitButton, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)  // Adjusted size
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

        setContentPane(backgroundPanel);  // Set the custom panel as the content pane

        // Set a fixed size for the window to prevent it from resizing
        setSize(400, 200);
        setResizable(false); // Prevent window resizing
    }

    public static void main(String args[]) {

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