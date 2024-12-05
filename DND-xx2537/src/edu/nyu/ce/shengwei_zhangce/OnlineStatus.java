package edu.nyu.ce.shengwei_zhangce;

/**
 *
 * @author Ce Zhang
 * @netId cz2146
 * 
 */

import static java.lang.Thread.sleep;
import java.net.*;

public class OnlineStatus implements Runnable {
	DatagramSocket test;

	OnlineStatus() {
		try {
			test = new DatagramSocket();
		} catch (SocketException ex) {

		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				byte[] mutibyte;
				mutibyte = ChatUI.clientName.getBytes();
				InetAddress group = InetAddress.getByName(ChatRoomConstants.IP_ADDR_2);
				DatagramPacket packet = new DatagramPacket(mutibyte, mutibyte.length, group, ChatRoomConstants.PORT_2);
				test.send(packet);
				try {
					sleep((long) (Math.random() * 20000));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}