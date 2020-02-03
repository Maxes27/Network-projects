import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static int id=1;
	public static String password;
    public static void main(String []args){
        
        System.out.print("Hello Server");
        //declare a TCP socket object and initialize it to null
        ServerSocket serverSocket=null;
        //create the port number
        int port = 4000;
  
        try {
            //create the TCP socket server
            serverSocket = new ServerSocket(port);
            System.out.println("Server created on port = "+port);
        } catch (IOException ex) {
            //will be executed when the server cannot be created
            System.out.println("Error: The server with port="+port+" cannot be created");
            return;
        }
        
        //starts an endless loop
        while(true){
            Socket clientSocket = null;
            try {
                //start listening to incoming client request (blocking function)
            	// create a stream between server and client
                System.out.println("[ECHO Server] waiting for the incoming request ...");
                clientSocket = serverSocket.accept();
                System.out.println("Connnected to client "+ id);
                
            } catch (IOException e) {
                System.out.println("Error: cannot accept client request. Exit program");
                return;
            }
            try {
                //create a new thread for each incoming message
            	
                //// create a stream between client and PC cracker
                new Thread(new PCCracker(clientSocket, id)).start();
                id++;
                
                //System.out.println(password+"Of client "+ id);
                
            } catch (Exception e) {
                //log exception and go on to next request.
            	return;
            }
           
        }
        
    }
    
 }