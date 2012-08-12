package yapto.datasource.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.LinkedList;

import org.im4java.process.OutputConsumer;

/**
 * An {@link OutputConsumer} which puts all the information in a
 * {@link LinkedList}.
 * 
 * @author benobiwan
 * 
 */
public class LinkedListOutputConsumer implements OutputConsumer
{
	/**
	 * The {@link LinkedList} containing the information.
	 */
	private final LinkedList<String> _outputLines = new LinkedList<>();

	/**
	 * The {@link Charset} object used to parse line output.
	 */
	private Charset _Charset = null;

	/**
	 * Creates a new LinkedListOutputConsumer using default {@link Charset} to
	 * parse the output.
	 */
	public LinkedListOutputConsumer()
	{
		// nothing to do
	}

	/**
	 * Creates a new LinkedListOutputConsumer using the specified
	 * {@link Charset} to parse the output.
	 * 
	 * @param charset
	 *            the {@link Charset} used to parse the output.
	 */
	public LinkedListOutputConsumer(final Charset charset)
	{
		_Charset = charset;
	}

	@Override
	public void consumeOutput(final InputStream pInputStream)
			throws IOException
	{
		BufferedReader reader = null;
		try
		{
			if (_Charset == null)
			{
				reader = new BufferedReader(new InputStreamReader(pInputStream));
			}
			else
			{
				reader = new BufferedReader(new InputStreamReader(pInputStream,
						_Charset));
			}
			String line;
			while ((line = reader.readLine()) != null)
			{
				_outputLines.add(line);
			}
		}
		finally
		{
			if (reader != null)
			{
				reader.close();
			}
		}
	}

	/**
	 * Get the output of the previous command executed.
	 * 
	 * @return the output.
	 */
	public LinkedList<String> getOutput()
	{
		return _outputLines;
	}

	/**
	 * Clear the output of the previous command.
	 */
	public void clearOutput()
	{
		_outputLines.clear();
	}
}
