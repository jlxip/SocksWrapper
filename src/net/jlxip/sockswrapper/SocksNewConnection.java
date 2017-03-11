package net.jlxip.sockswrapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocksNewConnection {
	public static final byte socksVersionNumber = 0x05;	// SOCKS5
	public static final byte numberOfAuthenticationMethodsSupported = 0x01;	// Just one, the following
	public static final byte authenticationMethod = 0x00;	// No authentication
	
	public static final byte commandCode = 0x01;	// Establish a TCP stream
	public static final byte reserved = 0x00;	// Reserved byte
	
	public static Socket newConnection(String IP, int PORT, String TARGET, int TARGET_PORT) throws IOException {
		Socket s = new Socket();
		
		try {
			s.connect(new InetSocketAddress(IP, PORT), SocksWrapper.timeout);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		InputStream is = s.getInputStream();
		OutputStream os = s.getOutputStream();
		
		os.write(new byte[]{socksVersionNumber, numberOfAuthenticationMethodsSupported, authenticationMethod});
		
		
		byte[] response1 = new byte[2];
		is.read(response1);
		if(response1[0] != 0x05) {
			System.err.println("SocksWrapper: received socks version number is not 5.");
			return null;
		}
		
		os.write(new byte[]{socksVersionNumber, commandCode, reserved});
		
		os.write(0x03);
		os.write((byte)TARGET.length()+1);	// +1 because of the last byte (0x00).
		os.write(TARGET.getBytes());
		os.write(0x00);	// Null byte in order to end the target transmission.
		byte[] BTARGET_PORT = new byte[]{(byte)(TARGET_PORT >>> 8), (byte)TARGET_PORT};
		os.write(BTARGET_PORT);
		
		byte[] response2 = new byte[2];
		is.read(response2);
		byte[] dummyData = new byte[is.available()];
		is.read(dummyData);
		dummyData = null;
		
		if(SocksPrintError.run(response2[1]) == false) return null;
		
		return s;
	}
}
