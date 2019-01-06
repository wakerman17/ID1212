package server.model;

/**
 * A DTO storing the question and answer.
 * 
 */
public class QuestionAnswerDTO {
	private String question;
	private String answer;
	
	/**
	 * Create a new instance of QuestionAnswerDTO
	 * 
	 * @param question The question
	 * @param answer The answer
	 */
	public QuestionAnswerDTO(String question, String answer) {
		this.question = question;
		this.answer = answer;
	}
	
	/**
	 * 
	 * @return The question
	 */
	public String getQuestion() {
		return question;
	}
	
	/**
	 * 
	 * @return The answer
	 */
	public String getAnswer() {
		return answer;
	}
}
