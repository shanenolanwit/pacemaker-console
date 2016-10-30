package exceptions;

/**
 * ValidationException
 * @author Shane Nolan
 * Used to represent errors creating pacemaker models
 */
@SuppressWarnings("serial")
public class ValidationException extends Exception {

	public ValidationException(String str) {
		super(str);
	}

}
