package Server.au.edu.uow.QuestionLibrary;

import java.io.*;
import java.util.*;

/**
 * This class creates the Library of questions, makes new quizzes of randomly 
 * selected questions from the Question List/Library.
 * 
 *       
 * @author Mitchell Stanford
 * 
 */
public class QuestionLibrary {
    private static List<Question> library = new ArrayList<Question>();
    private static Random randomGenerator;
    
    /**
	 * This method opens a file, and then reads each question, choices,
         * and answers which are added to a List/Library of questions.
         * 
	 * @param qFile String of filename to open
	 * @return ready True if the Library was built, false for otherwise
	 */
    public static boolean buildLibrary(String qFile) {
        library = new ArrayList<Question>();
        boolean ready = false;
        File file = new File(qFile);
        
        try {
            // Create a buffered reader to read each line from a file.
            Scanner in = new Scanner(new FileReader(file)); 
            String s;
            // Read each line from the file
            while(in.hasNextLine()){
                s = in.nextLine();
                //With each section ie. <question>, <choices> etc. Continue reading lines until closing tag is found such as </question>
                if ("<JavaQuestions>".equalsIgnoreCase(s)) {
                    while (!("</JavaQuestions>".equalsIgnoreCase(s = in.nextLine()))) { 
                        String question = "";
                        String[] choices = new String[5];
                        String[] choicesTwo = null;
                        int answer = 0;
                        String tfAnswer = "";
                        if ("<MQuestion>".equalsIgnoreCase(s)) {                        
                            while (!("</MQuestion>".equalsIgnoreCase(s = in.nextLine()))) {
                                if ("<question>".equalsIgnoreCase(s)) {
                                    while (!("</question>".equalsIgnoreCase(s = in.nextLine())))
                                    {
                                        question += s;
                                        question += "\n";
                                    }
                                }
                                else if("<answer>".equalsIgnoreCase(s)){
                                        answer = in.nextInt();
                                } else if("<choices>".equalsIgnoreCase(s)) {
                                        int count = 0;
                                        while (!("</choices>".equalsIgnoreCase(s = in.nextLine()))) {
                                            choices[count] = s;
                                            count++;
                                            
                                        }
                                        choicesTwo = new String[count];
                                        for (int j = 0; j < count; j++) {
                                            choicesTwo[j] = choices[j];
                                        }
                                }         
                            }
                            addQuestion(new MultipleChoiceQuestion(question, choicesTwo, answer));
                            ready = true;
                        }
                        else if ("<TFQuestion>".equalsIgnoreCase(s)) {
                            while (!("</TFQuestion>".equalsIgnoreCase(s = in.nextLine()))) {
                                if ("<question>".equalsIgnoreCase(s)) {
                                    while (!("</question>".equalsIgnoreCase(s = in.nextLine())))
                                    {
                                        question += s;
                                        question += "\n";
                                    }
                                }
                                else if("<answer>".equalsIgnoreCase(s)){
                                    tfAnswer = in.nextLine();
                                    if ("True".equalsIgnoreCase(tfAnswer)) {
                                        answer = 1;
                                    } else if ("False".equalsIgnoreCase(tfAnswer)) {
                                        answer = 2;
                                    }
                                }
                            }
                            addQuestion((new TrueAndFalseQuestion(question, answer)));
                            ready = true;
                        }
                    } 
                }   
            }
            // Close the buffered reader, which also closes the file reader.
            in.close();
        } catch (FileNotFoundException e1) {
            // If this file does not exist 
            System.err.println("File not found: " + qFile);
        } /*catch (IOException e2) {
            // Catch any other IO exceptions. 
            e2.printStackTrace();
        }*/
        
        return ready;
    }
    /**
	 * This method makes a new quiz, checks to make sure that each question
         * has not already been added.
         * 
	 * @param noOfQuestions The number of Questions to add to the list
	 * @return quiz List
	 */
    public static List<Question> makeQuiz(int noOfQuestions) {
        //create list and random number generator
        List<Question> quiz = new ArrayList<Question>();
        randomGenerator = new Random();
        int[] used = new int[noOfQuestions];
        boolean  check = false;
        for (int i = 0; i < noOfQuestions; i++) {
            int index = randomGenerator.nextInt(library.size());
            Question item = library.get(index);
            for (int j = 0; j < noOfQuestions; j++) {
                //check to see if question is already in quiz
                if (index == used[j]) {
                    //if so do not add
                    j = noOfQuestions;
                    i--;
                    check = false;
                } else {
                    check = true;
                }
            }
            //if check to see if question already in quiz is passed, then add to list
            if (check)
            {
                quiz.add(item);
                used[i] = index;
            } 
        }
        
        return quiz;
    }
    
    /**
	 * This method adds a question to the Question Library
         * 
	 * @param newQuestion The new Question to add to the list
	 * 
	 */
    public static void addQuestion(Question newQuestion) {
        library.add(newQuestion);
    }
}
