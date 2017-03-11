package net.jlxip.sockswrapper;

public class SocksPrintError {
	public static boolean run(byte response) {
		switch(response) {
			case 0x01:
				System.err.println("SocksWrapper: general failure.");
				return false;
			case 0x02:
				System.err.println("SocksWrapper: denied connection because of the ruleset.");
				return false;
			case 0x03:
				System.err.println("SocksWrapper: unreachable network.");
				return false;
			case 0x04:
				System.err.println("SocksWrapper: unreachable host.");
				return false;
			case 0x05:
				System.err.println("SocksWrapper: connection rejected by the remote host.");
				return false;
			case 0x06:
				System.err.println("SocksWrapper: expired TTL.");
				return false;
			case 0x07:
				System.err.println("SocksWrapper: protocol error.");
				return false;
			case 0x08:
				System.err.println("SocksWrapper: unsupported address type.");
				return false;
		}
		
		return true;
	}
}
