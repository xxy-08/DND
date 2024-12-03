import java.net.*;

public class ChatPortal implements Runnable {

	private static final int BUFFER_SIZE = 256;
	private static final int OFFSET = 0;
	private static final String NEW_LINE = "\n";

	public ChatPortal() {
		try {
			ChatUI.clientSocket = new MulticastSocket(ChatRoomConstants.PORT_1);
			ChatUI.ds = new DatagramSocket();

			ChatUI.targetAddr = InetAddress.getByName(ChatRoomConstants.IP_ADDR_1);
			ChatUI.clientSocket.joinGroup(ChatUI.targetAddr);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void run() {
		Thread onlineStatusThread = new Thread(new OnlineStatus());
		onlineStatusThread.start();
		Thread onlineStatusReceiverThread = new Thread(new OnlineStatusReceiver());
		onlineStatusReceiverThread.start();
		newUser();

		while (true) {
			try {
				
				
				System.out.println("yes!!!!");
				byte[] buffer = new byte[BUFFER_SIZE];
				DatagramPacket datagramPacket = new DatagramPacket(buffer, BUFFER_SIZE);
				ChatUI.clientSocket.receive(datagramPacket);
				String message = new String(datagramPacket.getData(), OFFSET, datagramPacket.getLength());
				ChatUI.chatTXT.setText(ChatUI.chatTXT.getText() + NEW_LINE + message);
				ChatUI.typeTXT.setText("");
			} catch (Exception exception) {
				System.out.println("errr...");
				exception.printStackTrace();
			}
		}
	}

	private void newUser() {
		
		String notice = "Another adventurer comes! Welcome " + ChatUI.clientName + "!";
		byte[] buffer = notice.getBytes();
		try {
			InetAddress hostAddr = InetAddress.getByName(ChatRoomConstants.IP_ADDR_1);
			DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, hostAddr,
					ChatRoomConstants.PORT_1);
			ChatUI.ds.send(datagramPacket);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

}
