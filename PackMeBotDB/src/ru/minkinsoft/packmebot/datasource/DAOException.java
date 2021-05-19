package ru.minkinsoft.packmebot.datasource;

public class DAOException extends Exception {
	public String message = "";
	Exception exc;
	
	public DAOException(String message, Exception exc) {
		this.message = message;
		this.exc = exc;
	}

	@Override
	public String getMessage() {
		return "Error in the DAOLayer. " + this.message + "\n" +
				exc.getMessage();
	}
	
	

}
