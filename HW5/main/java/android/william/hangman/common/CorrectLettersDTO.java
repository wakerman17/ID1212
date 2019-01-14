package android.william.hangman.common;

import java.io.Serializable;

public class CorrectLettersDTO implements Serializable {
    private static final long serialVersionUID = 5501315617802534592L;
    final char LETTER;
    int[] indicesOfCorrectLetters;

    public CorrectLettersDTO(char letter, int[] indices) {
        LETTER = letter;
        indicesOfCorrectLetters = indices;
    }

    /**
     *
     * @return The letter that is important in this instance.
     */
    public char getLetter () {
        return LETTER;
    }

    /**
     *
     * @return All the indices the of this letter is the word.
     */
    public int[] getIndices () {
        return indicesOfCorrectLetters;
    }
}
