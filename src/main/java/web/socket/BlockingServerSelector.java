package web.socket;

import static web.socket.WebSocketUtils.log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlockingServerSelector {
	private static final int BUFFER_CAPACITY = 128;
	private static final Map<SocketChannel, ByteBuffer> SOCKETS = new ConcurrentHashMap<>();

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			log("Usage: java BlockingServerSelectorImplementation <port number>");
			System.exit(1);
		}
		try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open(); Selector selector = Selector.open();) {
			serverSocketChannel.socket().bind(new InetSocketAddress(Integer.parseInt(args[0])));
			serverSocketChannel.configureBlocking(false); // channel must be in non-blocking mode to be used with a selector
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			while (true) {
				selector.select(); // blocks until at least one channel is ready for the events you registered for
				for (Iterator<SelectionKey> iterator = selector.selectedKeys().iterator(); iterator.hasNext();) {
					SelectionKey key = iterator.next();
					iterator.remove();
					try {
						if (key.isValid()) {
							if (key.isAcceptable()) accept(key);
							if (key.isReadable()) read(key);
							if (key.isWritable()) write(key);
						}
					} catch (Exception e) {
						log(e.getMessage());
					}
					SOCKETS.keySet().removeIf(socketChannel -> !socketChannel.isOpen());
				}
				log("next while");
			}
		}
	}

	private static void write(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer buffer = SOCKETS.get(channel);
		channel.write(buffer);
		if (!buffer.hasRemaining()) {
			buffer.compact();
			key.interestOps(SelectionKey.OP_READ);
		}
	}

	private static void read(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer buffer = SOCKETS.get(channel);
		int data = channel.read(buffer);
		if (data == -1) {
			channel.close();
			SOCKETS.remove(channel);
		}
		buffer.flip();
		transform(buffer);
		key.interestOps(SelectionKey.OP_WRITE);
	}

	private static void accept(SelectionKey key) throws IOException {
		ServerSocketChannel server = (ServerSocketChannel) key.channel();
		SocketChannel channel = server.accept();
		log(channel.toString());
		channel.configureBlocking(false);
		channel.register(key.selector(), SelectionKey.OP_READ);
		SOCKETS.put(channel, ByteBuffer.allocateDirect(BUFFER_CAPACITY));
	}

	private static void transform(ByteBuffer data) {
		for (int i = 0; i < data.limit(); i++) {
			data.put(i, (byte) transform(data.get(i)));
		}
	}

	private static int transform(int data) {
		return Character.isLetter(data) ? data ^ 32 : data;
	}
}
