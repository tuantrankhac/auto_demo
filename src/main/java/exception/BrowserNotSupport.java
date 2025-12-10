package exception;

public class BrowserNotSupport extends IllegalArgumentException{

	private static final long serialVersionUID = 1L;
	
	public BrowserNotSupport (String brownserName) {
		super(String.format("Brownser with name = %s is not support", brownserName.toUpperCase()));
	}

}