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
import java.util.stream.IntStream;

public class StreamServerNIOBlocking {
	private static final Charset charset = Charset.forName("ISO-8859-1");
	private static final CharsetDecoder decoder = charset.newDecoder();
	private static final CharsetEncoder encoder = charset.newEncoder();

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			WebSocketUtils.log("Usage: java NIOServerBlockingImplementation <port number>");
			System.exit(1);
		}

		try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
			serverSocketChannel.socket().bind(new InetSocketAddress(Integer.parseInt(args[0])));
			while (true) {
				SocketChannel clientSocket = serverSocketChannel.accept();
				handle(clientSocket);
			}
		}
	}

	private static CharBuffer transform(CharBuffer buffer) {
//		String stuffOfBuffer = buffer.array();
		return null;
	}

	private static void handle(SocketChannel socketChannel) throws IOException {
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(8);
		while (socketChannel.read(byteBuffer) != -1) {
			byteBuffer.flip();
			CharBuffer charBuffer = decoder.decode(byteBuffer);
//			char[] chb = charBuffer.array();
//			WebSocketUtils.log(String.format("********** Char buffer stuff printing. Begin. ************"));
//			IntStream.range(0, chb.length).mapToObj(i -> chb[i]).forEach(System.out::println); // debug
//			WebSocketUtils.log(String.format("********** Char buffer stuff printing. End. **************"));
			byteBuffer.clear();
			byteBuffer = encoder.encode(charBuffer);
			byte[] hb = byteBuffer.array();
//			WebSocketUtils.log(String.format("********** Byte buffer stuff printing. Begin. ************"));
//			IntStream.range(0, hb.length).map(i -> hb[i]).forEach(System.out::println); // debug
//			WebSocketUtils.log(String.format("********** Byte buffer stuff printing. End. **************"));

			while (byteBuffer.hasRemaining()) {
				socketChannel.write(byteBuffer);
			}
			byteBuffer.compact();
		}
	}
}
