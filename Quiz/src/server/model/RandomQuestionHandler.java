package server.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

/**
 * Reads from a file and chose a random value.
 *
 */
public class RandomQuestionHandler {
	int amountOfQuestionsAndAnsers = 8;
	String[] wordsInDictionary = new String[amountOfQuestionsAndAnsers]; 
	Random r = new Random();
	
	/**
	 * Stores all the words from a specific file into an array for fast access.
	 * @throws IOException 
	 */
	RandomQuestionHandler() throws IOException {
    	String fileName = "src\\server\\resources\\questionsAndAnswers.txt";
    		FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			int i = 0;
			line = bufferedReader.readLine();
		    while (line != null) {
		    	if (line.length() != 1) {
		    		wordsInDictionary[i] = line;
		    		i++;
		    	}
		    	line = bufferedReader.readLine();
		    }
		    bufferedReader.close();
		} 
	
	/**
	 * 
	 * @return A random word from the file. 
	 */
	QuestionAnswerDTO getQuestionAndAnswer() {
		int randomNumber = r.nextInt(amountOfQuestionsAndAnsers);
		String[] dividedQuestion = wordsInDictionary[randomNumber].split(": ");
		QuestionAnswerDTO questionAndAnswer;
		if (dividedQuestion[0].equals("A")) {
			String answer = dividedQuestion[1];
			dividedQuestion = wordsInDictionary[randomNumber - 1].split(": ");
			questionAndAnswer = new QuestionAnswerDTO(dividedQuestion[1], answer);
			return questionAndAnswer;
		} else {
			String question = dividedQuestion[1];
			dividedQuestion = wordsInDictionary[randomNumber + 1].split(": ");
			questionAndAnswer = new QuestionAnswerDTO(question, dividedQuestion[1]);
			return questionAndAnswer;
		}
	}
}
