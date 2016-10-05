package web.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.CharBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class StreamServerNIOBlocking {
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			WebSocketUtils.log("Usage: java EchoServer <port number>"); System.exit(1);
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

	private static void handle(SocketChannel socketChannel) {
//		CharBuffer buffer = CharBuffer.allocate(100);
////		try (CharBuffer buffer = new CharBuffer(80)) {
//			String inputLine;
//			while ((inputLine = in.readLine()) != null) {
//				WebSocketUtils.log(String.format("Client request (%s). Message : %s.", WebSocketUtils.getCurrentTimeInUTC(), inputLine));
//				out.println(transform(inputLine));
////			}
//		} catch (IOException e) {
//			WebSocketUtils.log("Exception caught when trying to listen on port " + socketChannel.getLocalAddress() + " or listening for a connection");
//			WebSocketUtils.log(e.getMessage());
//		}
	}
}
