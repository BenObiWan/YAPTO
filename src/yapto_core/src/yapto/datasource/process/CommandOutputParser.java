package yapto.datasource.process;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yapto.datasource.PictureInformation;

/**
 * Abstract class used to parse the output of external command.
 * 
 * @author benobiwan
 * 
 */
public abstract class CommandOutputParser
{
	/**
	 * Pattern matching the geometry line of the identify command. Example
	 * expected line : 'Geometry: 4368x2912+0+0'
	 */
	private static Pattern GEOMETRY_PATTERN = Pattern
			.compile("[\\s]*Geometry: ([0-9]+)x([0-9]+)\\+[0-9]+\\+[0-9]+[\\s]*");

	/**
	 * Pattern matching the exif date line of the identify command. Example
	 * expected line : 'exif:DateTime: 2009:11:15 12:11:13'
	 */
	private static Pattern EXIF_DATE_PATTERN = Pattern
			.compile("[\\s]*exif:DateTime: ([0-9]+:[0-9]+:[0-9]+ [0-9]+:[0-9]+:[0-9]+)[\\s]*");

	/**
	 * Pattern matching the exif make line of the identify command. Example
	 * expected line : 'exif:Make: Canon'
	 */
	private static Pattern EXIF_MAKE_PATTERN = Pattern
			.compile("[\\s]*exif:Make: (.+)[\\s]*");

	/**
	 * Pattern matching the exif model line of the identify command. Example
	 * expected line : 'exif:Model: Canon EOS 5D'
	 */
	private static Pattern EXIF_MODEL_PATTERN = Pattern
			.compile("[\\s]*exif:Model: (.+)[\\s]*");

	/**
	 * Pattern matching the exif orientation line of the identify command.
	 * Example expected line : 'exif:Orientation: 1'
	 */
	private static Pattern EXIF_ORIENTATION_PATTERN = Pattern
			.compile("[\\s]*exif:Orientation: ([0-9]+)[\\s]*");

	/**
	 * Pattern matching the exif exposure time line of the identify command.
	 * Example expected line : 'exif:ExposureTime: 1/60'
	 */
	private static Pattern EXIF_EXPOSURE_TIME_PATTERN = Pattern
			.compile("[\\s]*exif:ExposureTime: ([0-9]/[0-9]+)[\\s]*");

	/**
	 * Pattern matching the exif relative aperture line of the identify command.
	 * Example expected line : 'exif:FNumber: 4/1'
	 */
	private static Pattern EXIF_RELATIVE_APERTURE_PATTERN = Pattern
			.compile("[\\s]*exif:FNumber: ([0-9]/[0-9]+)[\\s]*");

	/**
	 * Pattern matching the exif FNumber line of the identify command. Example
	 * expected line : 'exif:FocalLength: 58/1'
	 */
	private static Pattern EXIF_FOCAL_LENGTH_PATTERN = Pattern
			.compile("[\\s]*exif:FocalLength: ([0-9]/[0-9]+)[\\s]*");

	// TODO other information to parse
	// exif:Flash: 9

	/**
	 * Date formatter for reading the creation date of the picture.
	 */
	private static final ThreadLocal<SimpleDateFormat> FORMATTER = new ThreadLocal<SimpleDateFormat>()
	{
		@Override
		protected SimpleDateFormat initialValue()
		{
			final SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy:MM:dd H:mm:ss");
			formatter.setLenient(false);
			return formatter;
		}
	};

	/**
	 * Parses the output of an identify command and creates a
	 * {@link PictureInformation} object.
	 * 
	 * @param strFileName
	 *            the original file name of the picture.
	 * @param informationList
	 *            the List of Strings to parse.
	 * @return a newly constructed {@link PictureInformation} object based on
	 *         the parsed informations.
	 * @throws ParseException
	 *             if a parse error occurs during the parsing of the date.
	 */
	public static PictureInformation readIdentifyOutput(
			final String strFileName, final List<String> informationList)
			throws ParseException
	{
		int iWidth = 0, iHeigth = 0, iOrientation = 0;
		long lCreationTimestamp = 0;
		String strMake = null, strModel = null, strExposureTime = null, strRelativeAperture = null, strFocalLength = null;
		Matcher matcher;

		for (final String strInfo : informationList)
		{
			matcher = GEOMETRY_PATTERN.matcher(strInfo);
			if (matcher.matches())
			{
				iWidth = Integer.parseInt(matcher.group(1));
				iHeigth = Integer.parseInt(matcher.group(2));
				continue;
			}

			matcher = EXIF_DATE_PATTERN.matcher(strInfo);
			if (matcher.matches())
			{
				lCreationTimestamp = FORMATTER.get().parse(matcher.group(1))
						.getTime();
				continue;
			}

			matcher = EXIF_MAKE_PATTERN.matcher(strInfo);
			if (matcher.matches())
			{
				strMake = matcher.group(1);
				continue;
			}

			matcher = EXIF_MODEL_PATTERN.matcher(strInfo);
			if (matcher.matches())
			{
				strModel = matcher.group(1);
				continue;
			}

			matcher = EXIF_ORIENTATION_PATTERN.matcher(strInfo);
			if (matcher.matches())
			{
				iOrientation = Integer.parseInt(matcher.group(1));
				continue;
			}

			matcher = EXIF_EXPOSURE_TIME_PATTERN.matcher(strInfo);
			if (matcher.matches())
			{
				strExposureTime = matcher.group(1);
				continue;
			}

			matcher = EXIF_RELATIVE_APERTURE_PATTERN.matcher(strInfo);
			if (matcher.matches())
			{
				strRelativeAperture = matcher.group(1);
				continue;
			}

			matcher = EXIF_FOCAL_LENGTH_PATTERN.matcher(strInfo);
			if (matcher.matches())
			{
				strFocalLength = matcher.group(1);
				continue;
			}
		}

		return new PictureInformation(strFileName, iWidth, iHeigth,
				lCreationTimestamp, iOrientation, strMake, strModel,
				strExposureTime, strRelativeAperture, strFocalLength);
	}

}
