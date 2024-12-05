import java.net.*;

public class ChatPortal implements Runnable {

	private static final int BUFFER_SIZE = 256;
	private static final int OFFSET = 0;
	private static final String NEW_LINE = "\n";

	public ChatPortal() {
		try {
			// 使用 ChatUI 中的静态变量
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
		// 启动两个线程
		Thread onlineStatusThread = new Thread(new OnlineStatus());
		onlineStatusThread.start();
		Thread onlineStatusReceiverThread = new Thread(new OnlineStatusReceiver());
		onlineStatusReceiverThread.start();
		newUser(); // 发送欢迎信息

		// 接收消息并更新聊天界面
		while (true) {
			try {
				byte[] buffer = new byte[BUFFER_SIZE];
				DatagramPacket datagramPacket = new DatagramPacket(buffer, BUFFER_SIZE);
				ChatUI.clientSocket.receive(datagramPacket); // 接收消息
				String message = new String(datagramPacket.getData(), OFFSET, datagramPacket.getLength());
				ChatUI.chatTXT.setText(ChatUI.chatTXT.getText() + NEW_LINE + message);
				ChatUI.typeTXT.setText(""); // 清空输入框
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}

	private void newUser() {
		// 发送新用户加入的消息
		String notice = "Another adventurer comes! Welcome " + ChatUI.clientName + "!\n";
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
