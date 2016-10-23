package web.socket;

import static web.socket.WebSocketUtils.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerBlocking {
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			log("Usage: java EchoServer <port number>"); System.exit(1);
		}

		while (true) {
			try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0])); Socket clientSocket = serverSocket.accept();) {
				handle(clientSocket);
			} 
		}
	}

	private static String transform(String inputLine) {
		return inputLine.toUpperCase();
	}

	private static void handle(Socket s) {
		try (PrintWriter out = new PrintWriter(s.getOutputStream(), true); BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));) {
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				log(String.format("Client request (%s). Message : %s.", getCurrentTimeInUTC(), inputLine));
				out.println(transform(inputLine));
			}
		} catch (IOException e) {
			log("Exception caught when trying to listen on port " + s.getLocalPort() + " or listening for a connection");
			log(e.getMessage());
		}
	}
}
