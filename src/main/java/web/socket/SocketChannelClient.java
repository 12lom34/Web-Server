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
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class SocketChannelClient {
	private static final Charset charset = Charset.forName("ISO-8859-1");
	private static final CharsetEncoder encoder = charset.newEncoder();
	private static final CharsetDecoder decoder = charset.newDecoder();
	
	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			WebSocketUtils.log("Usage: java SocketChannelClient <host name> <port number>");
			System.exit(1);
		}

		String hostName = args[0];
		int portNumber = Integer.parseInt(args[1]);

		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.connect(new InetSocketAddress(hostName, portNumber));

		try (BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));) {
			String userInput;
			while ((userInput = stdIn.readLine()) != null) {
				// 1. Read input from Console and send it as request to server
				CharBuffer charBuffer = CharBuffer.wrap(userInput);
				String currentTime = WebSocketUtils.getCurrentTime();
				WebSocketUtils.log(charBuffer, x -> String.format("Console input (time: %s). Message : %s.",  currentTime, x));
				socketChannel.write(encoder.encode(charBuffer));
				
				// 2. Get response form server and print it
				ByteBuffer byteBuffer = ByteBuffer.allocateDirect(8);
				while (socketChannel.read(byteBuffer) != -1) {
					byteBuffer.flip();
					WebSocketUtils.log(decoder.decode(byteBuffer), x -> String.format("Response from server (time: %s). Message : %s.",  currentTime, x));
					byteBuffer.clear();
				}
			}
		} catch (UnknownHostException e) {
			WebSocketUtils.log(hostName, x -> "Don't know about host " + x);
			System.exit(1);
		} catch (IOException e) {
			WebSocketUtils.log(hostName, x -> "Couldn't get I/O for the connection to " + x);
			System.exit(1);
		}
	}
}