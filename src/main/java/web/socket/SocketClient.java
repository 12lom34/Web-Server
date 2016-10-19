package web.socket;

import java.io.*;
import java.net.*;

public class SocketClient {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
        	WebSocketUtils.log("Usage: java SocketClient <host name> <port number>");
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
                WebSocketUtils.log(in.readLine(), x -> String.format("Server response (%s). Message : %s.",  timeAsString /* free variable */, x));
            }
        } catch (UnknownHostException e) {
        	WebSocketUtils.log(hostName, x ->"Don't know about host " + x);
            System.exit(1);
        } catch (IOException e) {
        	WebSocketUtils.log(hostName, x ->"Couldn't get I/O for the connection to " + x);
            System.exit(1);
        } 
    }
}
