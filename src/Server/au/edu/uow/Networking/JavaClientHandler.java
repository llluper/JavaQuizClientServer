package Server.au.edu.uow.Networking;

import Server.au.edu.uow.QuestionLibrary.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.List;

/**
 *
 *       
 * @author Mitchell Stanford
 * 
 */
public class JavaClientHandler implements Runnable {
    
    private Socket incoming;
    private List<Question> quiz;
    private int questionCount = 0;
    ObjectOutputStream toClient;
    String name = "";
    
    public JavaClientHandler(Socket income) {
        incoming = income;
    }
    
    public void run() {	
	try {
            List<Question> quiz = QuestionLibrary.makeQuiz(5);
            String remoteHost = "";
            remoteHost = incoming.getInetAddress().getHostName();
            
            InputStream inStream = incoming.getInputStream();
            OutputStream outStream = incoming.getOutputStream();

            Scanner in = new Scanner(inStream);         

            PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);
            boolean done = false;
            
            while(!done) {  
                String line = in.nextLine();
                
                if (line.startsWith("REGISTER")) {
                    out.println("OK");
                    String[] parts = line.split(" ");
                    name = parts[1];
                    System.out.println(name + " registered");
                }
                else if (line.trim().equals("GET QUESTION")) {
                    
                    toClient = new ObjectOutputStream(incoming.getOutputStream());
                    
                    Question q = quiz.get(questionCount++);
                    
                    toClient.writeObject(q);
                }                    
                else if (line.trim().equals("BYE")) {
                    done = true;
                }
            }
	} catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                incoming.close();
                System.out.println(name + " disconnected"); 
            } catch (IOException e) {
                e.printStackTrace();
            }
	}
    }		
}