import java.io.*;
import java.net.*;

public class ServerMessageReceiver implements Runnable {
    private DatagramSocket socket;
    private static final int BUFFER_SIZE = 256;

    ServerMessageReceiver() {
        try {
            socket = new DatagramSocket(ChatRoomConstants.PORT_1);
        } catch (SocketException e) {
            System.err.println("Server socket creation failed.");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength()).trim();
                if (!message.isEmpty()) {
                    String updatedMessage = message + "\n";
                    ChatUI.chatTXT.append(updatedMessage);
                }
            } catch (IOException e) {
                System.err.println("Error while receiving packet.");
                e.printStackTrace();
            }
        }
    }
}