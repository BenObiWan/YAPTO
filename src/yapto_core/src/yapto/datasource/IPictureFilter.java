package yapto.datasource;

/**
 * A filter used to filter the list of {@link IPicture} in a
 * {@link IPictureList}.
 * 
 * @author benobiwan
 * 
 */
public interface IPictureFilter
{
	/**
	 * Check whether an {@link IPicture} has to be kept by this filter.
	 * 
	 * @param picture
	 *            the {@link IPicture} to check.
	 * @return true if the {@link IPicture} is kept by the filter.
	 */
	boolean filterPicture(IPicture picture);
}
