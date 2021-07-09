package domain;

public class DomainException extends Exception{
	public String message = "";
	Exception exc;
	
	public DomainException(String message, Exception exc) {
		this.message = message;
		this.exc = exc;
	}

	@Override
	public String getMessage() {
		return "Error in the Domain Layer. " + this.message + "\n" +
				exc.getMessage();
	}
}
