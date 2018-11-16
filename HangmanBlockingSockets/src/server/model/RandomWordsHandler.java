package server.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

/**
 * Reads from a file and chose a random value.
 *
 */
public class RandomWordsHandler {
	int amountOfWordsInDictionary = 51477;
	String[] wordsInDictionary = new String[amountOfWordsInDictionary]; 
	Random r = new Random();
	
	/**
	 * Stores all the words from a specific file into an array for fast access.
	 * @throws IOException 
	 */
	RandomWordsHandler() throws IOException {
    	String fileName = "src\\server\\resources\\words.txt";
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
	String getWord() {
		return wordsInDictionary[r.nextInt(amountOfWordsInDictionary)];
	}
}
