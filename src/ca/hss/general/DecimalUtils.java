/***********************************************************************
 * REDapp - DecimalUtils.java
 * Copyright (C) 2015-2016 The REDapp Development Team
 * Homepage: http://redapp.org
 * 
 * REDapp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * REDapp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with REDapp. If not see <http://www.gnu.org/licenses/>. 
 **********************************************************************/

package ca.hss.general;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * A helper class for double precision values.
 * 
 * @author Travis Redpath
 *
 */
public class DecimalUtils {
	private static Locale _loc = Locale.getDefault();

	private DecimalUtils() { }

	public static void setLocale(Locale loc) {
		_loc = loc;
	}
	
	/**
	 * Parse a floating point value from a string. Returns <code>null</code> if
	 * a double precision value could be not be obtained from the string.
	 * 
	 * @param value The string value to parse.
	 * @return the floating point value or <code>null</code> if one couldn't be found.
	 */
	public static Double valueOf(String value) {
		Double retval = null;
		try {
			retval = Double.valueOf(value);
		}
		catch (NumberFormatException ex) { }
		return retval;
	}

	/**
	 * Get a decimal formatter for the type of data specified.
	 * 
	 * @param type the type of data to get a formatter for
	 * @return a formatter for the specified data type
	 */
	public static DecimalFormat getFormat(DataType type) {
		DecimalFormat format = (DecimalFormat)NumberFormat.getNumberInstance(_loc);
		switch (type.getNumDecimals()) {
		case 0:
			format.applyPattern("0");
			break;
		case 1:
			format.applyPattern("0.0");
			break;
		case -2:
			format.applyPattern("0.##");
			break;
		default:
			format.applyPattern("0.00");
			break;
		}
		return format;
	}

	/**
	 * Get a decimal formatter for the type of data specified. Don't apply the locale
	 * to the formatter.
	 * 
	 * @param type the type of data to get a formatter for
	 * @return a formatter for the specified data type without a locale
	 */
	public static DecimalFormat getFormatLocaleless(DataType type) {
		DecimalFormat format = (DecimalFormat)NumberFormat.getNumberInstance(Locale.ENGLISH);
		switch (type.getNumDecimals()) {
		case 0:
			format.applyPattern("0");
			break;
		case 1:
			format.applyPattern("0.0");
			break;
		case -2:
			format.applyPattern("0.##");
			break;
		default:
			format.applyPattern("0.00");
			break;
		}
		return format;
	}

	/**
	 * Format the value as a string using the default format.
	 * 
	 * @param value the value to format
	 * @return a string representation of the value
	 */
	public static String format(Double value) {
		return format(value, DataType.UNKNOWN);
	}

	/**
	 * Format the value as a string using the specified format.
	 * 
	 * @param value the value to format
	 * @param type the data type to format as
	 * @return a string representation of the value
	 */
	public static String format(Double value, DataType type) {
		if (value == null)
			value = 0.0;
		switch (type.getNumDecimals()) {
		case -2:
			String s = String.format(_loc, "%.2f", value);
			while (s.charAt(s.length() - 1) == '0')
				s = s.substring(0, s.length() - 1);
			if (s.charAt(s.length() - 1) == ',' || s.charAt(s.length() - 1) == '.')
				s = s.substring(0, s.length() - 1);
			return s;
		case 0:
			return String.format(_loc, "%.0f", value);
		case 1:
			return String.format(_loc, "%.1f", value);
		default:
			return String.format(_loc, "%.2f", value);
		}
	}

	/**
	 * Format the value as a string using the default format. Formats
	 * without a locale.
	 * 
	 * @param value the value to format
	 * @return a string representation of the value
	 */
	public static String formatLocaleless(Double value) {
		return formatLocaleless(value, DataType.UNKNOWN);
	}

	/**
	 * Format the value as a string using the specified format. Formats
	 * without a locale.
	 * 
	 * @param value the value to format
	 * @param type the data type to format as
	 * @return a string representation of the value
	 */
	public static String formatLocaleless(Double value, DataType type) {
		if (value == null)
			value = 0.0;
		switch (type.getNumDecimals()) {
		case -2:
			String s = String.format(Locale.ENGLISH, "%.2f", value);
			while (s.charAt(s.length() - 1) == '0')
				s = s.substring(0, s.length() - 1);
			if (s.charAt(s.length() - 1) == ',' || s.charAt(s.length() - 1) == '.')
				s = s.substring(0, s.length() - 1);
			return s;
		case 0:
			return String.format(Locale.ENGLISH, "%.0f", value);
		case 1:
			return String.format(Locale.ENGLISH, "%.1f", value);
		default:
			return String.format(Locale.ENGLISH, "%.2f", value);
		}
	}

	/**
	 * Truncate the number of decimal places in a floating point
	 * value using the default data type.
	 * 
	 * @param value the value to truncate decimal places in
	 * @return a value with the specified number of decimal places
	 */
	public static Double formatNumber(Double value) {
		return formatNumber(value, DataType.UNKNOWN);
	}

	/**
	 * Truncate the number of decimal places in a floating point
	 * value using the specified data type.
	 * 
	 * @param value the value to truncate decimal places in
	 * @return a value with the specified number of decimal places
	 */
	public static Double formatNumber(Double value, DataType type) {
		if (value == null)
			value = 0.0;
		switch (type.getNumDecimals()) {
		case 0:
			return ((double)(Math.round(value)));
		case 1:
			return ((double)(Math.round(value * 10.0))) / 10.0;
		default:
			return ((double)(Math.round(value * 100.0))) / 100.0;
		}
	}

	public static enum DataType {
		/**
		 * 1 decimal place.
		 */
		TEMPERATURE,
		/**
		 * 0 decimal places.
		 */
		RH,
		/**
		 * 2 decimal places.
		 */
		PRECIP,
		/**
		 * 1 decimal place.
		 */
		WIND_SPEED,
		/**
		 * 0 decimal places.
		 */
		WIND_DIR,
		/**
		 * 1 decimal place.
		 */
		FFMC,
		/**
		 * 1 decimal place.
		 */
		DMC,
		/**
		 * 1 decimal place.
		 */
		DC,
		/**
		 * 1 decimal place.
		 */
		ISI,
		/**
		 * 1 decimal place.
		 */
		BUI,
		/**
		 * 1 decimal place.
		 */
		FWI,
		UNKNOWN,

		/**
		 * 0 decimal places.
		 */
		FORCE_0,
		/**
		 * 1 decimal place.
		 */
		FORCE_1,
		/**
		 * 2 decimal places.
		 */
		FORCE_2,
		/**
		 * At most 2 decimal places (fewer if there are 0's).
		 */
		FORCE_ATMOST_2;

		public int getNumDecimals() {
			switch (this) {
			case FFMC:
			case DMC:
			case DC:
			case ISI:
			case BUI:
			case FWI:
			case WIND_SPEED:
			case TEMPERATURE:
			case FORCE_1:
				return 1;
			case WIND_DIR:
			case RH:
			case FORCE_0:
				return 0;
			case FORCE_ATMOST_2:
				return -2;
			default:
				return 2;
			}
		}
	}
}
