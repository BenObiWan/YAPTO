package yapto.datasource;

import java.util.List;

public interface IPicture
{
	/**
	 * Get the id of this {@link IPicture}.
	 * 
	 * @return the id of this {@link IPicture}.
	 */
	String getId();

	/**
	 * Get the list of {@link ITag}s associated with this {@link IPicture}.
	 * 
	 * @return the list of {@link ITag}s associated with this {@link IPicture}.
	 */
	List<ITag> getTagList();
}
