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
			WebSocketUtils.log("Usage: java NIOServerBlockingImplementation <port number>"); System.exit(1);
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
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		CharBuffer charBuffer = CharBuffer.allocate(1024);
//		try (CharBuffer buffer = new CharBuffer(80)) {
			int byteData;
			while ((byteData = socketChannel.read(buffer)) != -1) {
			      buffer.flip();
			      decoder.decode(buffer, charBuffer, false);
			      charBuffer.flip();
			      WebSocketUtils.log(String.format("Client request (%s). Message : %s.", WebSocketUtils.getCurrentTimeInUTC(), charBuffer));
//			      transform(charBuffer);
			      System.out.println(charBuffer);
			      buffer.clear();
			      charBuffer.clear();
			    }
			    
			    
//			}
//		} catch (IOException e) {
//			WebSocketUtils.log("Exception caught when trying to listen on port " + socketChannel.getLocalAddress() + " or listening for a connection");
//			WebSocketUtils.log(e.getMessage());
		}
		
//		
//
//        while ((data = s.read(buffer)) != -1) {
//
//            buffer.flip();
//            transform(buffer);
//
//            while (buffer.hasRemaining()) {
//                s.write(buffer);
//
//            }
//            buffer.compact();
//        }
//	}
}
