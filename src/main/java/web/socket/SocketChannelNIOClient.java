package web.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class SocketChannelNIOClient {
	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			WebSocketUtils.log("Usage: java EchoClient <host name> <port number>");
			System.exit(1);
		}

		String hostName = args[0];
		int portNumber = Integer.parseInt(args[1]);

		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.connect(new InetSocketAddress(hostName, portNumber));

		try (BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));) {
			String userInput;
			while ((userInput = stdIn.readLine()) != null) {
				CharBuffer c = CharBuffer.wrap(userInput);
				System.out.println("Sending date ...: " + c);
				ByteBuffer buf = Charset.forName("ISO-8859-1").encode(c);
				buf.flip();

				while (buf.hasRemaining()) {
					socketChannel.write(buf);
				}
			}
		} catch (UnknownHostException e) {
			WebSocketUtils.log("Don't know about host " + hostName);
			System.exit(1);
		} catch (IOException e) {
			WebSocketUtils.log("Couldn't get I/O for the connection to " + hostName);
			System.exit(1);
		}
	}
}
