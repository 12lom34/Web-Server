package web.socket;

import static web.socket.WebSocketUtils.*;
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
			log("Usage: java SocketChannelClient <host name> <port number>");
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
				log(charBuffer, x -> String.format("Console input (time: %s). Message : %s.",  getCurrentTime(), x));
				socketChannel.write(encoder.encode(charBuffer));  // read from buffer into channel and send to server
				
				// 2. Get response form server and print it
				ByteBuffer byteBuffer = ByteBuffer.allocateDirect(8);
				while (socketChannel.read(byteBuffer) /* write data into a buffer */ != -1) {
					byteBuffer.flip(); // switches a buffer from writing mode to reading mode
					log(decoder.decode(byteBuffer), x -> String.format("Response from server (time: %s). Message : %s.",  getCurrentTime(), x));
					byteBuffer.clear(); // "position" is set back to 0 and the "limit" to "capacity"; 
										// the data in the buffer is not cleared, only the markers telling
										// where you can write data into the buffer are

				}
			}
		} catch (UnknownHostException e) {
			log(hostName, x -> "Don't know about host " + x);
			System.exit(1);
		} catch (IOException e) {
			log(hostName, x -> "Couldn't get I/O for the connection to " + x);
			System.exit(1);
		}
	}
}