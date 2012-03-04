package yapto.datasource.sqlfile;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

/**
 * A {@link RemovalListener} used to save the modifications of an FsPicture when
 * it is removed from the cache.
 * 
 * @author benobiwan
 * 
 */
public final class FsPictureRemovalListener implements
		RemovalListener<String, FsPicture>
{
	/**
	 * Logger object.
	 */
	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(FsPictureRemovalListener.class);

	/**
	 * Object holding the connection to the database and the prepared
	 * statements.
	 */
	private final SQLFileListConnection _fileListConnection;

	/**
	 * Creates a new FsPictureRemovalListener.
	 * 
	 * @param fileListConnection
	 *            object holding the connection to the database and the prepared
	 *            statements.
	 */
	public FsPictureRemovalListener(
			final SQLFileListConnection fileListConnection)
	{
		_fileListConnection = fileListConnection;
	}

	@Override
	public void onRemoval(
			final RemovalNotification<String, FsPicture> notification)
	{
		if (notification.getValue().hasBeenModified())
		{
			try
			{
				_fileListConnection.updatePicture(notification.getValue());
			}
			catch (final SQLException e)
			{
				LOGGER.error(e.getMessage(), e);
			}
		}
	}
}
