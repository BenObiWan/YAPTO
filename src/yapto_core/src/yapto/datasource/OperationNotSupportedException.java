package yapto.datasource;

/**
 * Exception telling that the asked operation is unsupported.
 * 
 * @author benobiwan
 * 
 */
public final class OperationNotSupportedException extends Exception
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = 8690054180170377993L;

	/**
	 * Creates a new OperationNotSupportedException.
	 */
	public OperationNotSupportedException()
	{
		super();
	}

	/**
	 * Creates a new OperationNotSupportedException.
	 * 
	 * @param message
	 *            the detail message of the exception.
	 */
	public OperationNotSupportedException(final String message)
	{
		super(message);
	}

	/**
	 * Creates a new OperationNotSupportedException.
	 * 
	 * @param cause
	 *            the cause of the exception.
	 */
	public OperationNotSupportedException(final Throwable cause)
	{
		super(cause);
	}

	/**
	 * Creates a new OperationNotSupportedException.
	 * 
	 * @param message
	 *            the detail message of the exception.
	 * @param cause
	 *            the cause of the exception.
	 */
	public OperationNotSupportedException(final String message,
			final Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Creates a new OperationNotSupportedException.
	 * 
	 * @param message
	 *            the detail message of the exception.
	 * @param cause
	 *            the cause of the exception.
	 * @param enableSuppression
	 *            whether or not suppression is enabled or disabled.
	 * @param writableStackTrace
	 *            whether or not the stack trace should be writable
	 */
	public OperationNotSupportedException(final String message,
			final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
