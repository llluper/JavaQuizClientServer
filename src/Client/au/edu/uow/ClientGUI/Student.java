package Client.au.edu.uow.ClientGUI;

/**
 * This class is used to create Student's and record their name and quiz 
 * results.
 *       
 * @author Mitchell Stanford
 * 
 */
public class Student {
    private String name;
    int finalQuizScore;
    int noOfQuestions;
    
    /**
	 * This method sets the student's name.
	 * 
	 * @param name The student's name;
	 */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
	 * This method returns the student's name.
	 * 
	 * @return name The student's name.
	 */
    public String getName() {
        return name;
    }  
    
    /**
	 * This method records the student's score and number of questions.
	 * 
	 * @param isCorrect Boolean to check correct.
	 */
    public void recordScore(boolean isCorrect) {
        if (isCorrect)
            finalQuizScore++;
        noOfQuestions++;
    }
    
    /**
	 * This method returns the student's score.
	 * 
	 * @return finalQuizScore The student's final quiz score.
	 */
    public int getScore() {
        return finalQuizScore;
    }
    
    /**
	 * This method returns the number of questions answered.
	 * 
	 * @return noOfQuestions The number of questions in the quiz;
	 */
    public int getNoOfQuestions() {
        return noOfQuestions;
    }
}

