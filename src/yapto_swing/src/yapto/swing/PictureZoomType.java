package yapto.swing;

/**
 * The type of zoom used to display the picture.
 * 
 * @author benobiwan
 * 
 */
public enum PictureZoomType
{
	/**
	 * Display the picture in it's real size.
	 */
	REAL_SIZE,

	/**
	 * Scale the picture to the window dimension.
	 */
	WINDOW_DIMENSION,

	/**
	 * Scale the picture to a specific size.
	 */
	SPECIFIC_SIZE,

	/**
	 * Scale the picture to a percentage of it's dimension.
	 */
	PICTURE_PERCENTAGE,

	/**
	 * Scale the picture to a percentage of the window dimension.
	 */
	WINDOW_PERCENTAGE,
}
