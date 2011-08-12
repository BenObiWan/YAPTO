package yapto.datasource;

public class OperationNotSupportedException extends Exception
{
	/**
	 * serialVersionUID for Serialization.
	 */
	private static final long serialVersionUID = 8690054180170377993L;

	public OperationNotSupportedException()
	{
		super();
	}

	public OperationNotSupportedException(final String arg0)
	{
		super(arg0);
	}

	public OperationNotSupportedException(final Throwable arg0)
	{
		super(arg0);
	}

	public OperationNotSupportedException(final String arg0,
			final Throwable arg1)
	{
		super(arg0, arg1);
	}

	public OperationNotSupportedException(final String arg0,
			final Throwable arg1, final boolean arg2, final boolean arg3)
	{
		super(arg0, arg1, arg2, arg3);
	}

}
