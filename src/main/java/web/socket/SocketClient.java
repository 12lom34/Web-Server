package web.socket;

import static web.socket.WebSocketUtils.*;
import java.io.*;
import java.net.*;

public class SocketClient {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
        	log("Usage: java SocketClient <host name> <port number>");
            System.exit(1);
        }
 
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
 
        try ( Socket clientSocket = new Socket(hostName, portNumber);
              PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
              BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
              BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                String timeAsString = WebSocketUtils.getCurrentTime();
                log(in.readLine(), x -> String.format("Server response (%s). Message : %s.",  timeAsString /* free variable */, x));
            }
        } catch (UnknownHostException e) {
        	log("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
        	log("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        } 
    }
}
