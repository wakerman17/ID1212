package client.view;
/**
* This class is responsible for showing error messages to the user.
*/
public class ErrorMessageHandler {
	/**
	 * Displays the specified error message.
	 * 
	 * @param msg The error message.
	 */
	void showErrorMsg(String msg) {
		StringBuilder errorMsgBuilder = new StringBuilder();
		errorMsgBuilder.append("\nERROR: ");
		errorMsgBuilder.append(msg);
		errorMsgBuilder.append("\n");
		System.out.println(errorMsgBuilder.toString());
	}
}