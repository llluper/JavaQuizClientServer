import Server.au.edu.uow.Networking.*;
import Server.au.edu.uow.QuestionLibrary.*;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 *      
 * @author Mitchell Stanford
 * 
 */        
public class JavaQuizServer {
    private static int port = 40213; 
    private static String hostname = "";
    private static String hostIP ="";
    private static QuestionLibrary myQuestionLB;
        
    public static void main(String[] args ) {  

        createLibrary();

        if (args.length == 0) {
            try {
                hostname = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException ex) {
                Logger.getLogger(JavaQuizServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (args.length == 1) {
            String[] parts = args[0].split("\\:");
            if (args[0].equals(parts[0])) {
                try {
                    int num = Integer.parseInt(args[0]);
                    port = num;
                } catch (NumberFormatException nfe) {
                    hostname = args[0];
                } 
            } else {
                    port = Integer.parseInt(parts[1]);
            }
        } else {
            System.out.println("Usage: JavaQuizServer || JavaQuizServer portNumber || JavaQuizServer serverName:portNumber");
			System.exit(0);
        }
        try {
            // get host information
            hostIP = InetAddress.getLocalHost().getHostAddress();

            // display server information
            System.out.println("JavaQuizServer listening at: " + port);

            ServerSocket serverSocket = new ServerSocket(port);

            while (true) {
                Socket incoming = serverSocket.accept();		
		Runnable clientHandler = new JavaClientHandler(incoming);
		new Thread(clientHandler).start();
            }			
        } catch (IOException e) {  
            e.printStackTrace();
	}
    }
    
    public static void createLibrary() {
        myQuestionLB = new QuestionLibrary();
        boolean isQuestionLibraryReady = myQuestionLB.buildLibrary("questions.xml");
    }
}