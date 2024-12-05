import static java.lang.Thread.sleep;
import java.net.*;

public class OnlineStatus implements Runnable {

	private static DatagramSocket test;

	static {
		try {
			test = new DatagramSocket();
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				byte[] mutibyte = ChatUI.clientName.getBytes();
				InetAddress group = InetAddress.getByName(ChatRoomConstants.IP_ADDR_2);
				DatagramPacket packet = new DatagramPacket(mutibyte, mutibyte.length, group, ChatRoomConstants.PORT_2);
				test.send(packet);
				try {
					sleep((long) (Math.random() * 20000)); 
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
