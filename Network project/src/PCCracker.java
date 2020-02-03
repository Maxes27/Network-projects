import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;
public class PCCracker implements Runnable
{   
	public String[] hashes = new String[20];
	public int passwordLength[] = new int[20];
	public MessageDigest md;
	public Scanner in=new Scanner(System.in);
	public String hash_1;
	public int pswLength = 8;
	public ArrayList<Character>chars;
	String hash;
	int expectedLetters;

protected Socket clientSocket = null;
protected int id = 0;

public PCCracker(Socket pccrackerSocket, int id) {
this.clientSocket = pccrackerSocket;
this.id   = id;
}
@Override
public void run() {
try {
	System.out.println("PCCracker connected");
	DataInputStream input = new DataInputStream(clientSocket.getInputStream());
	DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
	output.writeUTF("Please enter hash: "); //Sends message to client
	String reply = input.readUTF();
	hashes[id-1]=reply; //receives hash from client
	output.writeUTF("Please enter expected password length: "); 
	int reply_1 = input.read();
    passwordLength[id-1]=reply_1;//// receives expected length from client
    mainScreen(hashes[id-1],passwordLength[id-1]);	
	
} catch (IOException e) {
// TODO Auto-generated catch block
e.printStackTrace();
System.out.println("Invalid Input");
return;
}
}	
	public void mainScreen(String hash, int expectedLetters)
	{
		try {
			md=MessageDigest.getInstance("MD5");
		}catch(NoSuchAlgorithmException e)
		{
			mainScreen(hash, expectedLetters);
			return;
		} 
		breakhash(hash,expectedLetters);
		
	}
	public void breakhash(String hash,int Expected_length) {
		char [] charsTemp=new char[] {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
		chars = new ArrayList<Character>(charsTemp.length);
		for(int i=0;i<charsTemp.length;i++) {
			chars.add(charsTemp[i]);
		}
		char[]password=new char[pswLength];
		while(true)
		{
			if(new String(password).trim().length()<=Expected_length)
		    {
			password=incrementString(password,chars,Expected_length).toCharArray();
			String temp=new String (password).trim();
			if(hash.equals(Sample.convertToHexString(md.digest(temp.getBytes()))))
		     {
			    //System.out.println(new String(password).trim()+"=="+hash);
				
				try {
					DataOutputStream outputClient;
					outputClient = new DataOutputStream(clientSocket.getOutputStream());
					outputClient.writeUTF("The password is "+new String(password).trim()+ " for client "+id); //Sends password to the client
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			    break;
			 }
		    }
			else
		    	{
				//System.out.println("error,not found");
				
				try {
					DataOutputStream outputClient;
					outputClient = new DataOutputStream(clientSocket.getOutputStream());
					outputClient.writeUTF("Error password not found");  //Sends this if password wasnt found
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			    break;
		    	}
			
			 }
		
		return ;
		}
	public String incrementString(char[] password , ArrayList<Character> chars,int Expected_length)
	{
       
		char[] pswTemp=password; 
		if(pswTemp[0]==chars.get(chars.size()-1)) { //checks if character on the left equals Z to add another character to its right or increment the atmost right character
			pswTemp=increment(password,chars,0);
		}
		else pswTemp[0]=(char)chars.get((chars.indexOf(pswTemp[0]) + 1) % chars.size()); //increment character on the left
	    ///System.out.println(pswTemp);
	    return new String(pswTemp);
	}
	private char[] increment(char[] password, ArrayList<Character> chars2, int i) {
		password[i]=chars2.get(0); //sets character on left to zero
		if(password[(i+1)]==chars.get(chars.size()-1)) { //checks if character to the right is Z
			return increment(password,chars2,i+1);    //Calls function again to check the chracter on the right of current one
			
		}else
			password[i+1]=chars.get(chars.indexOf(password[i+1]) + 1); //increment character on the right if it doesn't equal Z
		return password;
	}
}   

