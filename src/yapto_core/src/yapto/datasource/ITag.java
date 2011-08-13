package yapto.datasource;

/**
 * An interface describing a tag associated to an {@link IPicture}.
 * 
 * @author benobiwan
 * 
 */
public interface ITag
{
	/**
	 * Get the parent of this {@link ITag}.
	 * 
	 * @return the parent of this {@link ITag}.
	 */
	ITag getParent();

	/**
	 * Get the name of this {@link ITag}.
	 * 
	 * @return the name of this {@link ITag}.
	 */
	String getName();

	/**
	 * Get the description of this {@link ITag}.
	 * 
	 * @return the description of this {@link ITag}.
	 */
	String getDescription();
}
