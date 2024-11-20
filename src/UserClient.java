import javax.swing.*;

public class UserClient {
	public UserClient() {
		ChatUI.clientName = JOptionPane.showInputDialog("enter your name:");
		int num = 0;
		while (ChatUI.clientName == null || ChatUI.clientName.equals("")) {
			if (ChatUI.clientName == null) {
				new ChatRoomMain().setVisible(true);
				num++;
				break;
			} else if (ChatUI.clientName.equals("")) {
				JOptionPane.showMessageDialog(new ChatRoomMain(), "name is not in correct form, please enter again");
				ChatUI.clientName = JOptionPane.showInputDialog("enter the name here:");
			}
		}

		if (num == 0) {
			new ChatUI().setVisible(true);
			Thread clientThread = new Thread(new ChatPortal());
			clientThread.start();
		}
	}
}
