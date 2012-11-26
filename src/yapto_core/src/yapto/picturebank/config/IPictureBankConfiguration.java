package yapto.picturebank.config;

import yapto.picturebank.IPictureBank;

import common.config.IConfigurationBranch;

/**
 * Configuration for an {@link IPictureBank}.
 * 
 * @author benobiwan
 * 
 */
public interface IPictureBankConfiguration extends IConfigurationBranch,
		Comparable<IPictureBankConfiguration>
{
	/**
	 * Tag for the {@link IPictureBank} id.
	 */
	String PICTUREBANK_ID_TAG = "PictureBankId";

	/**
	 * Tag for the {@link IPictureBank} name.
	 */
	String PICTUREBANK_NAME_TAG = "PictureBankName";

	/**
	 * Get the id of this {@link IPictureBank}.
	 * 
	 * @return the id of this {@link IPictureBank}.
	 */
	int getPictureBankId();

	/**
	 * Get the name of this {@link IPictureBank}.
	 * 
	 * @return the name of this {@link IPictureBank}.
	 */
	String getPictureBankName();
}
