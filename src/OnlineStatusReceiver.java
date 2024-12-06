import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

public class OnlineStatusReceiver implements Runnable {
	InetAddress ipAddress = null;
	MulticastSocket socket = null;
	public static List<String> arrayex = new ArrayList<>();

	@SuppressWarnings("deprecation")
	OnlineStatusReceiver() {
		try {
			socket = new MulticastSocket(ChatRoomConstants.PORT_2);
			ipAddress = InetAddress.getByName(ChatRoomConstants.IP_ADDR_2);
			socket.joinGroup(ipAddress);
		} catch (Exception e) {
			System.err.println("join group get the error");
		}
	}

	@Override
	public void run() {
		arrayex = new ArrayList<>();
		while (true) {

			try {
				trycase();
			} catch (Exception e) {
				System.out.println("error in receiveonline status class");
			}
		}
	}

	public void trycase() throws IOException {
		DatagramPacket datapacket;
		byte[] mutibyte = new byte[256];
		datapacket = new DatagramPacket(mutibyte, mutibyte.length);
	
		socket.receive(datapacket);
	
		String data = new String(datapacket.getData(), 0, datapacket.getLength()).trim();
		if (data.endsWith("exits from the chatting room")) {
			String exitingUser = data.replace(" exits from the chatting room", "").trim();
			arrayex.remove(exitingUser);
		} else if (!arrayex.contains(data)) {
			arrayex.add(data);
		}
	
		StringBuilder userListBuilder = new StringBuilder("Online Users：");
		for (Object obj : arrayex) {
			userListBuilder.append(obj.toString()).append(" ");
		}
	
		ChatUI.usersLabel.setText(userListBuilder.toString().trim());
	}	
}
