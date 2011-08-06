package yapto.datasource;

public class OperationNotSupportedException extends Exception
{

	public OperationNotSupportedException()
	{
		super();
	}

	public OperationNotSupportedException(String arg0)
	{
		super(arg0);
	}

	public OperationNotSupportedException(Throwable arg0)
	{
		super(arg0);
	}

	public OperationNotSupportedException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}

	public OperationNotSupportedException(String arg0, Throwable arg1,
			boolean arg2, boolean arg3)
	{
		super(arg0, arg1, arg2, arg3);
	}

}
