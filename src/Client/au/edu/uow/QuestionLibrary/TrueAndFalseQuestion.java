package Client.au.edu.uow.QuestionLibrary;

import java.util.*;

/**
 * This class implements the Question interface while taking the questions, 
 * and answers for the true and false questions.
 * 
 *       
 * @author Mitchell Stanford
 * 
 */
public class TrueAndFalseQuestion implements Question {
    private String question;
    private int answer;
    List<String> list;

    TrueAndFalseQuestion(String question, int answer) {
            this.question = question;
            this.answer = answer;
        }
   
        /**
         * This method returns the question text in a <String> list.
         * 
	 * @return list The question text in a list
         */
	public List<String> getQuestion() {
            list = new ArrayList<String>();
            list.add(question);
            return list;
        }
	
	/**
	 * This method returns the answer choices in a list. 
         * 
	 * @return list The list of choices
	 */
	public List<String> getChoices() {
            list = new ArrayList<String>();
            list.add("True");
            list.add("False");
            return list;
        }

	/**
	 * This method compares the student's answer to the standard answer.
         * 
	 * @param ans The student's answer
	 * @return True for the correct answer; false for incorrect answers.
	 */
	public boolean compareAnswer(int ans) {
            boolean check;
            if (answer == ans)
                check = true;
            else 
                check = false;
            return check;
        }
}
