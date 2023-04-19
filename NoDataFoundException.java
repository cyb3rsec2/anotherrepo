
public class NoDataFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	private String message;
	public NoDataFoundException(String message) {
		this.message = message;
	}
	
}
