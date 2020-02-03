import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    
    public static void main(String args[]) throws IOException, InterruptedException { 
        System.out.print("Hello Client\n");
        Socket clientSocket =null;
        String serverHostName = "localhost";
        int port = 4000;
        Scanner scan = new Scanner(System.in);
     
           try {        //////////////connecting to server
        	clientSocket = new Socket(serverHostName, port);
        	System.out.println("Connected client");
            
                } catch (IOException ex) {
                    System.out.println("[TCP Client] cannot open the socket with the server");
                    
                }
           try {
           DataInputStream input = new DataInputStream(clientSocket.getInputStream());
  			DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
  			String serverReply = input.readUTF();   ////receives PCCracker request
  			System.out.println(serverReply);
  			
  			String hash = scan.nextLine();
			output.writeUTF(hash);  ///sends hash to PCCracker
  			
  			serverReply = input.readUTF();     ///receives another PCCracker request
  		    System.out.println(serverReply);
  		    
  		    int expectedLength = scan.nextInt();
			output.write(expectedLength);   ///sends expected password length to PCCracker
			scan.close();
  			
			while(true)
			{
			String PccrackerResponse = input.readUTF(); //Receives result from PCCracker
			System.out.println(PccrackerResponse);
			return;
			
			}
           }
           catch(IOException e)
           {
        	   System.out.println("Invaled Input");
        	   return;
           }     
     }
    
}