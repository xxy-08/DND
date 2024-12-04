import javax.swing.*;

public class UserClient {

    private static ChatRoomMain chatRoomMainInstance; // Declare ChatRoomMain as a static member

    public UserClient() {

        ChatUI.clientName = JOptionPane.showInputDialog("What's your name?");
        int num = 0;

        while (ChatUI.clientName == null || ChatUI.clientName.equals("")) {
            if (ChatUI.clientName == null) {
                num++;
                break;
            } else if (ChatUI.clientName.equals("")) {
                JOptionPane.showMessageDialog(chatRoomMainInstance, "Hey, you can't be NULL!");
                ChatUI.clientName = JOptionPane.showInputDialog("SERIOUSLY What's your name?");
            }
        }

        if (num == 0) {
            new ChatUI().setVisible(true);
            Thread clientThread = new Thread(new ChatPortal());
            clientThread.start();
        }
    }
}
