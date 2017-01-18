package Client.au.edu.uow.QuestionLibrary;

import java.util.*;

/**
 * This class implements the Question interface while taking the questions, choices, 
 * and answers for the multiple choice questions.
 * 
 *       
 * @author Mitchell Stanford
 * 
 */
public class MultipleChoiceQuestion implements Question {
    private String question;
    private String[] choices;
    private int answer;
    List<String> list;
    
        MultipleChoiceQuestion(String question, String[] choices, int answer) {
            this.question = question;
            this.choices = new String[choices.length];
            for (int i = 0; i < choices.length; i++) {
                this.choices[i] = choices[i];
            }
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
	 * This method returns the answer choices in a <String> list. 
         * 
	 * @return list The list of choices
	 */
	public List<String> getChoices() {
            list = new ArrayList<String>();
            for (int i = 0; i < choices.length; i++) {
                list.add(choices[i]);
            }
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
