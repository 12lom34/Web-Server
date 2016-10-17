package web.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class StreamServerNIOBlocking {
	private static final Charset charset = Charset.forName("ISO-8859-1");
	private static final CharsetDecoder decoder = charset.newDecoder();
	private static final CharsetEncoder encoder = charset.newEncoder();

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			WebSocketUtils.log("Usage: java NIOServerBlockingImplementation <port number>");
			System.exit(1);
		}

		while (true) {
			try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
				serverSocketChannel.socket().bind(new InetSocketAddress(Integer.parseInt(args[0])));
				try (SocketChannel clientSocket = serverSocketChannel.accept()) {
					handle(clientSocket);
				}
			}
		}
	}

	private static String transform(String inputLine) {
		return inputLine.toUpperCase();
	}

	private static void handle(SocketChannel socketChannel) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		CharBuffer charBuffer;
		int byteData;
		while ((byteData = socketChannel.read(buffer)) != -1) {
			buffer.flip();
			// decoder.decode(buffer, charBuffer, false);
			charBuffer = charset.decode(buffer);
			// charBuffer.flip();
			WebSocketUtils.log(String.format("Client request (%s). Message : %s.", WebSocketUtils.getCurrentTimeInUTC(),
					charBuffer));
			// transform(charBuffer);
			System.out.println(charBuffer);
			byte[] byteArray = new byte[1024];
			buffer.get(byteArray);
			System.out.println("Server got from client the string: " + new String(byteArray));
			String an = new String(byteArray);
			buffer.clear();
			charBuffer.clear();
		}

	}

	//
	//
	// while ((data = s.read(buffer)) != -1) {
	//
	// buffer.flip();
	// transform(buffer);
	//
	// while (buffer.hasRemaining()) {
	// s.write(buffer);
	//
	// }
	// buffer.compact();
	// }
	// }
}
