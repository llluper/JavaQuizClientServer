package Client.au.edu.uow.ClientGUI;

import Server.au.edu.uow.QuestionLibrary.Question;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.LineBorder;

/**
 *
 *       
 * @author Mitchell Stanford
 * 
 */
public class QuizClientGUIFrame extends JFrame implements ActionListener {
    JLabel resultLabel;
    boolean open = false;
    Socket socket;
    PrintWriter out;
    Scanner in;
    ObjectInputStream inputFromServer; 
    Color backColor = new Color(0,153,153);
    private JFrame frame;
    private int noOfQuestions = 0;
    private String[] choices;
    private JPanel statusPanel;
    private JLabel statusLabel;
    private JMenuBar menuBar;
    String server = "localhost:40213";
    String host = "localhost";
    int port = 40213;
    
    private Student myStudent;
    protected JTextArea textArea;
    protected String newline = "\n";
    static final private String enterCommand = "enter";
    static final private String nextCommand = "next";
    private String choice1 = "1";
    private String choice2 = "2";
    private String choice3 = "3";
    private String choice4 = "4";
    private String choice5 = "5";
    private int answer = 0;
    Question currentQ;
    final private CardLayout CL = new CardLayout(); 
    private JTextArea questionText;
    private JPanel mainPanel;
    private JPanel myPanel2;
    private JPanel myPanel3;
    private JPanel myPanel4;
    private JButton next;
    private JTextField nameField;
    
    private boolean registered = false;
    private String noOfQuestionsStr;
    private int qCount = 0;
    private ButtonGroup group;
    JRadioButton choicesB[];
    JRadioButton oneB;
    JRadioButton twoB;
    JRadioButton threeB;
    JRadioButton fourB;
    JRadioButton fiveB;
    private boolean isPage2 = false;
    private String qFilename;
            
    JMenuItem connectItem; 
    JMenuItem disconnectItem;
    
    public QuizClientGUIFrame(String title) throws FileNotFoundException, ClassNotFoundException, IOException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        super(title);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //disconnect first
        addWindowListener(new WindowAdapter() {
            
            @Override
            public void windowClosing(WindowEvent event) {
                disconnectServer();
                System.exit(0);
            }
        });

        String frameWidthStr;
        String frameHeightStr;
        int frameWidth;
        int frameHeight;
        Properties prop = new Properties();
        String fileName = "JavaQuizGUI.conf";
        InputStream is = new FileInputStream(fileName);

        prop.load(is);

        frameWidthStr = prop.getProperty("frameWidth");
        frameWidth = Integer.parseInt(frameWidthStr);
        frameHeightStr = prop.getProperty("frameHeight");
        frameHeight = Integer.parseInt(frameHeightStr);
        String qFilename = prop.getProperty("questionFilename");
        String noOfQuestionsStr = prop.getProperty("noOfQuestions");
        noOfQuestions = Integer.parseInt(noOfQuestionsStr);
        String looknfeel = prop.getProperty("LookAndFeel");

        UIManager.setLookAndFeel(looknfeel);
        setSize(frameWidth, frameHeight);
        setLayout(new BorderLayout());
        
        menuBar = new JMenuBar();
        JMenu connectionMenu = new JMenu("Connection");
        JMenu helpMenu = new JMenu("Help");

        menuBar.add(connectionMenu);
        menuBar.add(helpMenu);
        
        JMenuItem menuItem;
        connectItem = new JMenuItem("Connect");
        connectItem.addActionListener(this);
        connectionMenu.add(connectItem);

        disconnectItem = new JMenuItem("Disconnect");
        disconnectItem.addActionListener(this);
        disconnectItem.setEnabled(false);
        connectionMenu.add(disconnectItem);
        
        connectionMenu.addSeparator();
        
        menuItem = new JMenuItem("Set Server");
        menuItem.addActionListener(this);
        connectionMenu.add(menuItem);
        
        connectionMenu.addSeparator();
        
        menuItem = new JMenuItem("Exit");
        menuItem.addActionListener(this);
        connectionMenu.add(menuItem);
        
        
        menuItem = new JMenuItem("About");
        menuItem.addActionListener(this);
        helpMenu.add(menuItem);
        
        setJMenuBar(menuBar);
  

        statusPanel = new JPanel();
        statusPanel.setBorder(new LineBorder(Color.BLACK));
        add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setPreferredSize(new Dimension(getWidth(), 16));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusLabel = new JLabel("Connect to the Server");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);
        
        choices = new String[5];
        choicesB = new JRadioButton[5];
        myStudent = new Student();		

        mainPanel = new JPanel(CL);
        myPanel2 = new JPanel(new BorderLayout());
       
        myPanel2.setBackground(backColor);
        
        myPanel3 = new JPanel(new GridLayout(6,1));
        myPanel3.setBackground(backColor);
        JLabel label = new JLabel("");
        
        myPanel3.add(label);
        
        label = new JLabel("<html>Java Quiz Client</html>", JLabel.CENTER);       
        label.setFont(new Font("Arial", Font.BOLD, 26));
        label.setForeground(Color.WHITE);   

        myPanel3.add(label);
        
        label = new JLabel("<html><center>Created By Mitchell Stanford<br>for<br>CSCI213 Assignment 4</center></html>", JLabel.CENTER); 
        label.setFont(new Font("Arial", Font.BOLD, 22));
        label.setForeground(Color.BLACK);  

        myPanel3.add(label);
        
        myPanel2.add(myPanel3, BorderLayout.CENTER);
        
        JPanel panel = new JPanel();
        
        JToolBar toolbar = new JToolBar();
        toolbar.setLayout(new GridLayout(1,3));
        toolbar.setFloatable(false);
        toolbar.setRollover(true);
        label = new JLabel("");
        toolbar.add(label);
        label = new JLabel("Your Name:");
        panel.add(label);
            
        nameField = new JTextField();
            
        nameField.setColumns(10);
        nameField.setActionCommand(enterCommand);
        nameField.addActionListener(this);
        panel.add(nameField);
        toolbar.add(panel);
        label = new JLabel("");
        toolbar.add(label);
        
        myPanel2.add(toolbar, BorderLayout.SOUTH);

        JPanel questionPanel = new JPanel(new BorderLayout());
        myPanel4 = new JPanel(new GridLayout(6,1));
        myPanel4.setBackground(backColor);
        label = new JLabel("");
        
        myPanel4.add(label);
        
        label = new JLabel("Your Score", JLabel.CENTER);       
        label.setFont(new Font("Arial", Font.BOLD, 26));
        label.setForeground(Color.WHITE);   

        myPanel4.add(label);

        resultLabel = new JLabel("", JLabel.CENTER); 
        resultLabel.setFont(new Font("Arial", Font.BOLD, 22));
        resultLabel.setForeground(Color.BLACK);  


        myPanel4.add(resultLabel);
        myPanel4.setBackground(backColor);
        JLabel newL = new JLabel(newline);

        questionText = new JTextArea();
        questionText.setColumns(10);
        questionText.setRows(20);

        questionPanel.add(questionText, BorderLayout.NORTH);

        choicesB[0] = new JRadioButton(choices[0]);
        choicesB[0].setActionCommand(choice1);
        choicesB[1] = new JRadioButton(choices[1]);
        choicesB[1].setActionCommand(choice2);
        choicesB[2] = new JRadioButton(choices[2]);
        choicesB[2].setActionCommand(choice3);
        choicesB[3] = new JRadioButton(choices[3]);
        choicesB[3].setActionCommand(choice4);
        choicesB[4] = new JRadioButton(choices[4]);
        choicesB[4].setActionCommand(choice5);

        group = new ButtonGroup();
        group.add(choicesB[0]);
        group.add(choicesB[1]);
        group.add(choicesB[2]);
        group.add(choicesB[3]);
        group.add(choicesB[4]);

        choicesB[0].addActionListener(this);
        choicesB[1].addActionListener(this);
        choicesB[2].addActionListener(this);
        choicesB[3].addActionListener(this);
        choicesB[4].addActionListener(this);

        JPanel radioPanel = new JPanel(new GridLayout(0, 1));
        radioPanel.add(choicesB[0]);
        radioPanel.add(choicesB[1]);
        radioPanel.add(choicesB[2]);
        radioPanel.add(choicesB[3]);
        radioPanel.add(choicesB[4]);

        JPanel choiceFlow = new JPanel();

        choiceFlow.add(radioPanel);

        questionPanel.add(choiceFlow, BorderLayout.CENTER);
        next = new JButton("Next");
        next.setActionCommand(nextCommand);
        next.setToolTipText("Next");
        next.addActionListener(this);
        JPanel myFlow = new JPanel();
        myFlow.add(next);
        questionPanel.add(myFlow, BorderLayout.SOUTH);

        mainPanel.add(myPanel2, "1");
        mainPanel.add(questionPanel, "2");
        mainPanel.add(myPanel4, "3");
        add(mainPanel, BorderLayout.CENTER);
        CL.show(mainPanel, "1");      
    }

    public void actionPerformed(ActionEvent e) {
       String cmd = e.getActionCommand();

            if ("Exit".equals(cmd)) {
                disconnectServer();
                System.exit(0);
            } else if ("About".equals(cmd)) {
                displayMessage("Java Quiz Client Ver 1.0\nbased on Java sockets\nby Mitchell Stanford");     
            } else if ("Connect".equals(cmd)) { 
                if (nameField.getText().isEmpty()) {
                    displayMessage("Register your name first");
                } else {
                    disconnectItem.setEnabled(true);
                    connectItem.setEnabled(false);
                    myStudent.setName(nameField.getText());
                    connectServer();
                    getQuestion();
                }
            } else if ("Disconnect".equals(cmd)) {
                setupPage3();
            } else if ("Set Server".equals(cmd)) {
                setServer();
                
            } else if (nextCommand.equals(cmd)) {
                    boolean correct = currentQ.compareAnswer(answer);
                    myStudent.recordScore(correct);
                    if (qCount == noOfQuestions-1)
                        next.setText("Get Score");
                    if (qCount != noOfQuestions) {
                        getQuestion();
                    }
                    else {
                        setupPage3();
                    }        
            } 

            if (choice1.equals(cmd)) {
                answer = 1;
            } else if (choice2.equals(cmd)) {
                answer = 2;
            } else if (choice3.equals(cmd)) {
                answer = 3;
            } else if (choice4.equals(cmd)) {
                answer = 4;
            } else if (choice5.equals(cmd)) {
                answer = 5;
            }
        }
    
        /**
	 * This method displays the error messages and score messages.
	 * @param msg The message to be displayed.
	 * 
	 */
        protected void displayMessage(String msg) {
            JOptionPane.showMessageDialog(frame, msg);
        }
        protected void setServer() {
            server = (String)JOptionPane.showInputDialog(null, "Server:port",
"Set Server", JOptionPane.PLAIN_MESSAGE,null,null,server);
            String[] parts = server.split("\\:");
            host = parts[0];
            port = Integer.parseInt(parts[1]);
        }
        protected void connectServer() {
            try {
                boolean done = false;
                
                socket = new Socket (host, port);

                out = new PrintWriter(socket.getOutputStream(), true /* autoFlush */);
                in = new Scanner(socket.getInputStream());
                open = true;
                out.println("REGISTER " + myStudent.getName());
                while (!done && in.hasNextLine()) {
                    String line = in.nextLine();
                    if (line.equals("OK")) {
                        done = true;
                    } 
                }
                statusLabel.setText("Connected to " + host + ":" + port);
               
                
            } catch (Exception e) {
                System.out.println("Make sure the server is running and try again");
            }
        }
        
        public void disconnectServer() {
            if (open) {
                out.println("BYE");
                out.close();
                in.close();           
                try {
                    inputFromServer.close();
                    socket.close();
                } catch (IOException ex) {
                    Logger.getLogger(QuizClientGUIFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            statusLabel.setText("Disconnected");
            disconnectItem.setEnabled(false);
            connectItem.setEnabled(true);
        }

        protected void getQuestion() {
            out.println("GET QUESTION");
            
            boolean done = false;
            Question theQ = null;
            
                try {
                    inputFromServer = new ObjectInputStream(socket.getInputStream()); 
                    Object object = inputFromServer.readObject(); 
                    theQ = (Question) object;
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(QuizClientGUIFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            setupPage2(theQ);
        }
        
        public void setupPage2(Question theQ) {
            group.clearSelection();
            for (int i = 0; i < 5; i++) {
                choicesB[i].setVisible(false);
            }
            int count = 0;
            questionText.setText("");
            
            currentQ = theQ;
            java.util.List<String> theQuestion = theQ.getQuestion();
            for (String ql : theQuestion) {
                questionText.setText(questionText.getText() + "\n\t " + ql);
            }
            java.util.List<String> theChoices = theQ.getChoices();
            for (String cl : theChoices) {
                choices[count] = cl;
                choicesB[count].setText(choices[count]);
                choicesB[count].setVisible(true); 
                count++;
            }

            qCount++;
            isPage2 = true;
            CL.show(mainPanel, "2");  
        }

        /**
	 * This method creates the third stage, the final result.
	 * 
	 */
        public void setupPage3() {
            resultLabel.setText(myStudent.getScore() + " out of " + noOfQuestions);
            disconnectServer();
            CL.show(mainPanel, "3");
        } 
}