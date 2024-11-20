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

		String data = new String(datapacket.getData(), 0, datapacket.getLength());
		if (data.equals("exited"))
			arrayex = new ArrayList<>();
		if (!arrayex.contains(data) && !data.equals("exited")) {
			arrayex.add(data);

			if (ChatUI.userTXT.getText().equals(""))
				ChatUI.userTXT.setText(data);
			else {
				ChatUI.userTXT.setText("");
				for (Object obj : arrayex) {
					ChatUI.userTXT.setText(ChatUI.userTXT.getText() + obj.toString() + "\n");
				}
			}
		}
	}
}
