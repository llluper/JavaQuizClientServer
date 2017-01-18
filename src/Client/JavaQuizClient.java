package Client;
import Client.au.edu.uow.ClientGUI.*;
import java.io.*;
import javax.swing.*;

/**
 *
 *       
 * @author Mitchell Stanford
 * 
 */
public class JavaQuizClient {

    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        
        JFrame myFrame = new QuizClientGUIFrame("Java Quiz Client");
        myFrame.setLocationRelativeTo(null);
        myFrame.setVisible(true);
    } 
}