package net.jlxip.sockswrapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocksRecursiveConnection {
	public static final byte socksVersionNumber = 0x05;	// SOCKS5
	public static final byte numberOfAuthenticationMethodsSupported = 0x01;	// Just one, the following
	public static final byte authenticationMethod = 0x00;	// No authentication
	
	public static final byte commandCode = 0x01;	// Establish a TCP stream
	public static final byte reserved = 0x00;	// Reserved byte
	
	public static Socket newRecursiveConnection(String IPs[], int PORTs[], String TARGET, int TARGET_PORT) throws IOException {
		if(IPs.length != PORTs.length) {
			System.err.println("SocksWrapper: IPs array size doesn't match PORTs array size.");
			return null;
		}
		
		@SuppressWarnings("resource")
		Socket s = new Socket();
		
		try {
			s.connect(new InetSocketAddress(IPs[0], PORTs[0]), SocksWrapper.timeout);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		InputStream is = s.getInputStream();
		OutputStream os = s.getOutputStream();
		
		if(!manageProtocol(is, os, IPs[1], PORTs[1])) return null;
		
		for(int i=1;i<IPs.length-1;i++) {
			if(!manageProtocol(is, os, IPs[i+1], PORTs[i+1])) return null;
		}
		
		if(!manageProtocol(is, os, TARGET, TARGET_PORT)) return null;
		
		return s;
	}
	
	private static boolean manageProtocol(InputStream is, OutputStream os, String TARGET, int TARGET_PORT) throws IOException {
		os.write(new byte[]{socksVersionNumber, numberOfAuthenticationMethodsSupported, authenticationMethod});
		
		
		byte[] response1 = new byte[2];
		is.read(response1);
		if(response1[0] != 0x05) {
			System.err.println("SocksWrapper: received socks version number is not 5.");
			return false;
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
		
		if(SocksPrintError.run(response2[1]) == false) return false;
		
		return true;
	}
}
