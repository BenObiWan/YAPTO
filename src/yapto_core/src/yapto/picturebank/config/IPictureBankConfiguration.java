package yapto.picturebank.config;

import yapto.picturebank.IPictureBank;

import common.config.IConfigurationBranch;

/**
 * Configuration for an {@link IPictureBank}.
 * 
 * @author benobiwan
 * 
 */
public interface IPictureBankConfiguration extends IConfigurationBranch
{
	/**
	 * Tag for the {@link IPictureBank} id.
	 */
	String PICTUREBANK_ID_TAG = "PictureBankId";

	/**
	 * Get the id of this {@link IPictureBank}.
	 * 
	 * @return the id of this {@link IPictureBank}.
	 */
	int getPictureBankId();
}
