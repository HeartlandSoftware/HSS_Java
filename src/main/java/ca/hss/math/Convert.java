/**
 * Convert.java
 *
 * Copyright 2015-2018 Heartland Software Solutions Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the license at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the LIcense is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.hss.math;

import ca.hss.annotations.Source;

import static ca.hss.math.general.*;

/**
 * Methods for converting units.
 */
@Source(project="Lowlevel", sourceFile="convert.cpp")
public class Convert {
	
	protected final static double distance_translation[] = { 1e-3, 1e-2, 1.0, 1e3, 2.54e-2, 3.048e-1, 0.9144, 2.01168e1, 1.609344e3, 1.852e3, 1.853184e3 };
	protected final static double area_translation[] = { 1e-6, 1e-4, 1.0, 1e4, 1e6, 6.4516e-4, 9.290304e-2, 8.3612736e-1, 4.0468564224e3, 2.58998811e6 };
	protected final static double volume_translation[] = { 0.000000001, 0.000001, 0.001, 1.0, 1000000000.0, 1.6387064e-5, 2.8316846e-2, 7.64554858e-1, 4.168181825e9, 2.84131e-5, 5.68261e-4, 1.13652e-3, 4.54609e-3, 3.63687e-2, 3.69669e-6, 2.95735e-5, 4.73176e-4, 9.46353e-4, 3.785411784e-3, 1.58987e-1, 5.50610e-4, 1.10122e-3, 1.10122e-3 };
	protected final static double temp_translate1[] = { 0.0, 273.15, 459.67, 0.0 };
	protected final static double temp_translate2[] = { 1.0, 1.0, 9.0 / 5.0, 9.0 / 5.0 };
	protected final static double mass_translation[] = { 1e-6, 1e-3, 1.0, 1e3, 2.83495e-2, 0.45359237, 9.07185e2, 1.016047e3 };
	protected final static double energy_translation[] = { 1.0, 1.6021892E-19, 1E-7, 1.35582, 4.1868, 9.80665, 1.05506E3, 3.6E3 / 3600.0, 3.6E6 / 3600.0, 1.05506E8, 1000.0 };
	protected final static double pressure_translation[] = { 1.0, 6.895, 100, 101.325, 0.133322 };
	protected final static double intensity_translation[] = { 3.461, 1.0 };
	protected final static double consumption_translation[] = { 0.2242, 1.0 };

	public static double ConvertUnit(double value, long to_format, long from_format) {
		if (from_format == to_format)
			return value;
		
		if (((from_format & 0xffffffff00000000L) != 0) || ((to_format & 0xffffffff00000000L) != 0)) {
			double val = ConvertUnit(value, (to_format >> 0x20) & 0x00000000ffffffffL, (from_format >> 0x20) & 0x00000000ffffffffL);
			val = ConvertUnit(val, from_format & 0x00000000ffffffffL, to_format & 0x00000000ffffffffL);
			return val;
		}
		
		from_format &= 0xffffffff;
		to_format &= 0xffffffffL;
		
		if (from_format != 0 && to_format != 0) {
			if (((from_format & STORAGE_FORMAT.TIME_MASK) != 0 && (from_format & (~STORAGE_FORMAT.TIME_MASK)) != 0) ||
					((to_format & STORAGE_FORMAT.TIME_MASK) != 0 && (to_format & (~STORAGE_FORMAT.TIME_MASK)) != 0)) {
				if ((from_format & STORAGE_FORMAT.TIME_MASK) != (to_format & STORAGE_FORMAT.TIME_MASK)) {
					if ((from_format & STORAGE_FORMAT.TIME_MULT) != 0) {
						long val = from_format & STORAGE_FORMAT.TIME_UNIT_MASK;
						if (val == STORAGE_FORMAT.MICROSECOND)
							value /= 1000.0;
						else if (val == STORAGE_FORMAT.MILLISECOND)
							value /= 1000000.0;
						else if (val == STORAGE_FORMAT.MINUTE)
							value *= 60.0;
						else if (val == STORAGE_FORMAT.HOUR)
							value *= (60.0 * 60.0);
						else if (val == STORAGE_FORMAT.DAY)
							value *= (24.0 * 60.0 * 60.0);
						else if (val == STORAGE_FORMAT.WEEK)
							value *= 604800.0;
						else if (val == STORAGE_FORMAT.MONTH)
							value *= 2629743.83;
						else if (val == STORAGE_FORMAT.YEAR)
							value *= 31556926.0;
						else if (val == STORAGE_FORMAT.DECADE)
							value *= 315569260.0;
						else if (val == STORAGE_FORMAT.CENTURY)
							value *= 3155692600.0;
					}
					else {
						long val = from_format & STORAGE_FORMAT.TIME_UNIT_MASK;
						if (val == STORAGE_FORMAT.MICROSECOND)
							value *= 1000.0;
						else if (val == STORAGE_FORMAT.MILLISECOND)
							value *= 1000000.0;
						else if (val == STORAGE_FORMAT.MINUTE)
							value /= 60.0;
						else if (val == STORAGE_FORMAT.HOUR)
							value /= (60.0 * 60.0);
						else if (val == STORAGE_FORMAT.DAY)
							value /= (24.0 * 60.0 * 60.0);
						else if (val == STORAGE_FORMAT.WEEK)
							value /= 604800.0;
						else if (val == STORAGE_FORMAT.MONTH)
							value /= 2629743.83;
						else if (val == STORAGE_FORMAT.YEAR)
							value /= 31556926.0;
						else if (val == STORAGE_FORMAT.DECADE)
							value /= 315569260.0;
						else if (val == STORAGE_FORMAT.CENTURY)
							value /= 3155692600.0;
					}
					if ((to_format & STORAGE_FORMAT.TIME_MULT) != 0) {
						long val = to_format & STORAGE_FORMAT.TIME_UNIT_MASK;
						if (val == STORAGE_FORMAT.MICROSECOND)
							value *= 1000.0;
						else if (val == STORAGE_FORMAT.MILLISECOND)
							value *= 1000000.0;
						else if (val == STORAGE_FORMAT.MINUTE)
							value /= 60.0;
						else if (val == STORAGE_FORMAT.HOUR)
							value /= (60.0 * 60.0);
						else if (val == STORAGE_FORMAT.DAY)
							value /= (24.0 * 60.0 * 60.0);
						else if (val == STORAGE_FORMAT.WEEK)
							value /= 604800.0;
						else if (val == STORAGE_FORMAT.MONTH)
							value /= 2629743.83;
						else if (val == STORAGE_FORMAT.YEAR)
							value /= 31556926.0;
						else if (val == STORAGE_FORMAT.DECADE)
							value /= 315569260.0;
						else if (val == STORAGE_FORMAT.CENTURY)
							value /= 3155692600.0;
					}
					else {
						long val = to_format & STORAGE_FORMAT.TIME_UNIT_MASK;
						if (val == STORAGE_FORMAT.MICROSECOND)
							value /= 1000.0;
						else if (val == STORAGE_FORMAT.MILLISECOND)
							value /= 1000000.0;
						else if (val == STORAGE_FORMAT.MINUTE)
							value *= 60.0;
						else if (val == STORAGE_FORMAT.HOUR)
							value *= (60.0 * 60.0);
						else if (val == STORAGE_FORMAT.DAY)
							value *= (24.0 * 60.0 * 60.0);
						else if (val == STORAGE_FORMAT.WEEK)
							value *= 604800.0;
						else if (val == STORAGE_FORMAT.MONTH)
							value *= 2629743.83;
						else if (val == STORAGE_FORMAT.YEAR)
							value *= 31556926.0;
						else if (val == STORAGE_FORMAT.DECADE)
							value *= 315569260.0;
						else if (val == STORAGE_FORMAT.CENTURY)
							value *= 3155692600.0;
					}
				}
			}
			
			from_format &= (~STORAGE_FORMAT.TIME_MASK);
			to_format &= (~STORAGE_FORMAT.TIME_MASK);
		}
		
		if ((from_format >= STORAGE.DISTANCE_START) && (from_format <= STORAGE.DISTANCE_END) &&
				(to_format >= STORAGE.DISTANCE_START) && (to_format <= STORAGE.DISTANCE_END))
			return value * distance_translation[(int)(from_format - STORAGE.DISTANCE_START)] /
					distance_translation[(int)(to_format - STORAGE.DISTANCE_START)];
		else if ((from_format >= STORAGE.TEMP_START) && (from_format <= STORAGE.TEMP_END) &&
				(to_format >= STORAGE.TEMP_START) && (to_format <= STORAGE.TEMP_END)) {
			double kelvin = (value + temp_translate1[(int)(from_format - STORAGE.TEMP_START)]) / temp_translate2[(int)(from_format - STORAGE.TEMP_START)];
			return kelvin * temp_translate2[(int)(to_format - STORAGE.TEMP_START)] - temp_translate1[(int)(to_format - STORAGE.TEMP_START)];
		}
		else if ((from_format >= STORAGE.VOLUME_START) && (from_format <= STORAGE.VOLUME_END) &&
				(to_format >= STORAGE.VOLUME_START) && (to_format <= STORAGE.VOLUME_END)) {
			return value * volume_translation[(int)(from_format - STORAGE.VOLUME_START)] /
					volume_translation[(int)(to_format - STORAGE.VOLUME_START)];
		}
		else if ((from_format >= STORAGE.AREA_START) && (from_format <= STORAGE.AREA_END) &&
				(to_format >= STORAGE.AREA_START) && (to_format <= STORAGE.AREA_END)) {
			return value * area_translation[(int)(from_format - STORAGE.AREA_START)] /
					area_translation[(int)(to_format - STORAGE.AREA_START)];
		}
		else if ((from_format >= STORAGE.PERCENT_START) && (from_format <= STORAGE.PERCENT_END) &&
				(to_format >= STORAGE.PERCENT_START) && (to_format <= STORAGE.PERCENT_END)) {
			if (from_format != to_format) {
				if (from_format == STORAGE_FORMAT.PERCENT) {
					value *= 0.01;
				}
				else if (from_format == STORAGE_FORMAT.PERCENT_INVERT) {
					value = (100.0 - value) * 0.01;
				}
				else if (from_format == STORAGE_FORMAT.DECIMAL_INVERT) {
					value = 1.0 - value;
				}
				if (to_format == STORAGE_FORMAT.PERCENT) {
					value *= 100.0;
				}
				else if (to_format == STORAGE_FORMAT.PERCENT_INVERT) {
					value = (100.0 - value) * 100.0;
				}
				else if (to_format == STORAGE_FORMAT.DECIMAL_INVERT) {
					value = 1.0 - value;
				}
			}
			return value;
		}
		else if ((from_format >= STORAGE.MASS_START) && (from_format <= STORAGE.MASS_END) &&
				(to_format >= STORAGE.MASS_START) && (to_format <= STORAGE.MASS_END)) {
			return value * mass_translation[(int)(from_format - STORAGE.MASS_START)] /
					mass_translation[(int)(to_format - STORAGE.MASS_START)];
		}
		else if ((from_format >= STORAGE.ENERGY_START) && (from_format <= STORAGE.ENERGY_END) &&
				(to_format >= STORAGE.ENERGY_START) && (to_format <= STORAGE.ENERGY_END)) {
			return value * energy_translation[(int)(from_format - STORAGE.ENERGY_START)] /
					energy_translation[(int)(to_format - STORAGE.ENERGY_START)];
		}
		else if ((from_format >= STORAGE.PRESSURE_START) && (from_format <= STORAGE.PRESSURE_END) &&
				(to_format >= STORAGE.PRESSURE_START) && (to_format <= STORAGE.PRESSURE_END)) {
			return value * pressure_translation[(int)(from_format - STORAGE.PRESSURE_START)] /
					pressure_translation[(int)(to_format - STORAGE.PRESSURE_START)];
		}
		else if ((from_format >= STORAGE.INTENSITY_START) && (from_format <= STORAGE.INTENSITY_END) &&
				(to_format >= STORAGE.INTENSITY_START) && (to_format <= STORAGE.INTENSITY_END)) {
			return value * intensity_translation[(int)(from_format - STORAGE.INTENSITY_START)] /
					intensity_translation[(int)(to_format - STORAGE.INTENSITY_START)];
		}
		else if ((from_format >= STORAGE.FUEL_CONSUMPTION_START) && (from_format <= STORAGE.FUEL_CONSUMPTION_END) &&
				(to_format >= STORAGE.FUEL_CONSUMPTION_START) && (to_format <= STORAGE.FUEL_CONSUMPTION_END)) {
			return value * consumption_translation[(int)(from_format - STORAGE.FUEL_CONSUMPTION_START)] /
					consumption_translation[(int)(to_format - STORAGE.FUEL_CONSUMPTION_START)];
		}
		else if (((from_format & (~STORAGE_FORMAT.ANGLE_MASK)) == STORAGE_FORMAT.ANGLE) &&
				((to_format & (~STORAGE_FORMAT.ANGLE_MASK)) == STORAGE_FORMAT.ANGLE)) {
			if ((from_format & STORAGE_FORMAT.ANGLE_MASK) != (to_format & STORAGE_FORMAT.ANGLE_MASK)) {
				if ((from_format & STORAGE_FORMAT.ANGLE_UNIT_MASK) == STORAGE_FORMAT.DEGREE) {
					value = DEGREE_TO_RADIAN(value);
				}
				else if ((from_format & STORAGE_FORMAT.ANGLE_UNIT_MASK) == STORAGE_FORMAT.ARCSECOND) {
					value = DEGREE_TO_RADIAN(value / 3600.0);
				}
				if ((from_format & STORAGE_FORMAT.ANGLE_ROTATION_MASK) == STORAGE_FORMAT.COMPASS) {
					value = COMPASS_TO_CARTESIAN_RADIAN(value);
				}
				if ((to_format & STORAGE_FORMAT.ANGLE_ROTATION_MASK) == STORAGE_FORMAT.COMPASS) {
					value = CARTESIAN_TO_COMPASS_RADIAN(value);
				}
				if ((to_format & STORAGE_FORMAT.ANGLE_UNIT_MASK) == STORAGE_FORMAT.DEGREE) {
					value = RADIAN_TO_DEGREE(value);
				}
				else if ((to_format & STORAGE_FORMAT.ANGLE_UNIT_MASK) == STORAGE_FORMAT.ARCSECOND) {
					value = RADIAN_TO_DEGREE(value) * 3600.0;
				}
			}
			return value;
		}
		else if ((from_format >= STORAGE.TIME_START) && (from_format <= STORAGE.TIME_END) &&
				(to_format >= STORAGE.TIME_START) && (to_format <= STORAGE.TIME_END)) {
			if ((from_format & STORAGE_FORMAT.TIME_MULT) != 0) {
				long v = from_format & STORAGE_FORMAT.TIME_UNIT_MASK;
				if (v == STORAGE_FORMAT.MICROSECOND)
					value /= 1000.0;
				else if (v == STORAGE_FORMAT.MILLISECOND)
					value /= 1000000.0;
				else if (v == STORAGE_FORMAT.MINUTE)
					value *= 60.0;
				else if (v == STORAGE_FORMAT.HOUR)
					value *= (60.0 * 60.0);
				else if (v == STORAGE_FORMAT.DAY)
					value *= (24.0 * 60.0 * 60.0);
				else if (v == STORAGE_FORMAT.WEEK)
					value *= 604800.0;
				else if (v == STORAGE_FORMAT.MONTH)
					value *= 2629743.83;
				else if (v == STORAGE_FORMAT.YEAR)
					value *= 315569260.0;
				else if (v == STORAGE_FORMAT.DECADE)
					value *= 315569260.0;
				else if (v == STORAGE_FORMAT.CENTURY)
					value *= 3155692600.0;
			}
			else {
				long v = from_format & STORAGE_FORMAT.TIME_UNIT_MASK;
				if (v == STORAGE_FORMAT.MICROSECOND)
					value *= 1000.0;
				else if (v == STORAGE_FORMAT.MILLISECOND)
					value *= 1000000.0;
				else if (v == STORAGE_FORMAT.MINUTE)
					value /= 60.0;
				else if (v == STORAGE_FORMAT.HOUR)
					value /= (60.0 * 60.0);
				else if (v == STORAGE_FORMAT.DAY)
					value /= (24.0 * 60.0 * 60.0);
				else if (v == STORAGE_FORMAT.WEEK)
					value /= 604800.0;
				else if (v == STORAGE_FORMAT.MONTH)
					value /= 2629743.83;
				else if (v == STORAGE_FORMAT.YEAR)
					value /= 315569260.0;
				else if (v == STORAGE_FORMAT.DECADE)
					value /= 315569260.0;
				else if (v == STORAGE_FORMAT.CENTURY)
					value /= 3155692600.0;
			}
			if ((to_format & STORAGE_FORMAT.TIME_MULT) != 0) {
				long v = to_format & STORAGE_FORMAT.TIME_UNIT_MASK;
				if (v == STORAGE_FORMAT.MICROSECOND)
					value *= 1000.0;
				else if (v == STORAGE_FORMAT.MILLISECOND)
					value *= 1000000.0;
				else if (v == STORAGE_FORMAT.MINUTE)
					value /= 60.0;
				else if (v == STORAGE_FORMAT.HOUR)
					value /= (60.0 * 60.0);
				else if (v == STORAGE_FORMAT.DAY)
					value /= (24.0 * 60.0 * 60.0);
				else if (v == STORAGE_FORMAT.WEEK)
					value /= 604800.0;
				else if (v == STORAGE_FORMAT.MONTH)
					value /= 2629743.83;
				else if (v == STORAGE_FORMAT.YEAR)
					value /= 315569260.0;
				else if (v == STORAGE_FORMAT.DECADE)
					value /= 315569260.0;
				else if (v == STORAGE_FORMAT.CENTURY)
					value /= 3155692600.0;
			}
			else {
				long v = to_format & STORAGE_FORMAT.TIME_UNIT_MASK;
				if (v == STORAGE_FORMAT.MICROSECOND)
					value /= 1000.0;
				else if (v == STORAGE_FORMAT.MILLISECOND)
					value /= 1000000.0;
				else if (v == STORAGE_FORMAT.MINUTE)
					value *= 60.0;
				else if (v == STORAGE_FORMAT.HOUR)
					value *= (60.0 * 60.0);
				else if (v == STORAGE_FORMAT.DAY)
					value *= (24.0 * 60.0 * 60.0);
				else if (v == STORAGE_FORMAT.WEEK)
					value *= 604800.0;
				else if (v == STORAGE_FORMAT.MONTH)
					value *= 2629743.83;
				else if (v == STORAGE_FORMAT.YEAR)
					value *= 315569260.0;
				else if (v == STORAGE_FORMAT.DECADE)
					value *= 315569260.0;
				else if (v == STORAGE_FORMAT.CENTURY)
					value *= 3155692600.0;
			}
		}
		return value;
	}
	
	public static abstract class UnitSystem {
		public static final int METRIC = 0x00000001;
		public static final int IMPERIAL = 0x00000002;
		
		public static long Temperature(int system) {
			if (system == IMPERIAL)
				return STORAGE_FORMAT.FAHRENHEIT;
			return STORAGE_FORMAT.CELSIUS;
		}
		
		public static long Speed(int system) {
			if (system == IMPERIAL)
				return STORAGE_FORMAT.MILE | STORAGE_FORMAT.TIME_DIV | STORAGE_FORMAT.HOUR;
			return STORAGE_FORMAT.KM | STORAGE_FORMAT.TIME_DIV | STORAGE_FORMAT.HOUR;
		}
		
		public static long FuelConsumpiton(int system) {
			if (system == IMPERIAL)
				return STORAGE_FORMAT.TONS_PER_ACRE;
			return STORAGE_FORMAT.KG_PER_M2;
		}
		
		public static long Intensity(int system) {
			if (system == IMPERIAL)
				return STORAGE_FORMAT.BTU_FT_S;
			return STORAGE_FORMAT.KILOWATT_PER_M;
		}
		
		public static long SpreadRate(int system) {
			if (system == IMPERIAL)
				return STORAGE_FORMAT.CHAIN | STORAGE_FORMAT.TIME_DIV | STORAGE_FORMAT.HOUR;
			return STORAGE_FORMAT.M | STORAGE_FORMAT.TIME_DIV | STORAGE_FORMAT.MINUTE;
		}
		
		public static long Area(int system) {
			if (system == IMPERIAL)
				return STORAGE_FORMAT.ACRE;
			return STORAGE_FORMAT.HECTARE;
		}
		
		public static long DistanceSmall(int system) {
			if (system == IMPERIAL)
				return STORAGE_FORMAT.INCH;
			return STORAGE_FORMAT.MM;
		}
		
		public static long DistanceSmall2(int system) {
			if (system == IMPERIAL)
				return STORAGE_FORMAT.INCH;
			return STORAGE_FORMAT.CM;
		}
		
		public static long DistanceMedium(int system) {
			if (system == IMPERIAL)
				return STORAGE_FORMAT.FOOT;
			return STORAGE_FORMAT.M;
		}
		
		public static long DistanceMedium2(int system) {
			if (system == IMPERIAL)
				return STORAGE_FORMAT.CHAIN;
			return STORAGE_FORMAT.M;
		}
		
		public static long DistanceLarge(int system) {
			if (system == IMPERIAL)
				return STORAGE_FORMAT.MILE;
			return STORAGE_FORMAT.KM;
		}
	}
	
	private abstract class STORAGE {
		public static final long TIME_START = 0x00010000;
		public static final long TIME_END = 0x001f0000;
		public static final long DISTANCE_START = 0x00000001;
		public static final long DISTANCE_END = 0x0000000b;
		public static final long AREA_START = 0x00000100;
		public static final long AREA_END = 0x00000109;
		public static final long VOLUME_START = 0x00000200;
		public static final long VOLUME_END = 0x00000216;
		public static final long TEMP_START = 0x00000400;
		public static final long TEMP_END = 0x00000404;
		public static final long PRESSURE_START = 0x00000500;
		public static final long PRESSURE_END = 0x00000504;
		public static final long MASS_START = 0x00000600;
		public static final long MASS_END = 0x00000607;
		public static final long ENERGY_START = 0x00000700;
		public static final long ENERGY_END = 0x0000070a;
		public static final long PERCENT_START = 0x000004c0;
		public static final long PERCENT_END = 0x000004c3;
		@SuppressWarnings("unused")
		public static final long COORDINATE_START = 0x00000800;
		@SuppressWarnings("unused")
		public static final long COORDINATE_END = 0x00000804;
		public static final long FUEL_CONSUMPTION_START = 0x00000900;
		public static final long FUEL_CONSUMPTION_END = 0x00000909;
		public static final long INTENSITY_START = 0x00000910;
		public static final long INTENSITY_END = 0x00000919;
	}

	public static abstract class STORAGE_FORMAT {
		public static final long MICROSECOND = 0x00080000;
 	    public static final long MILLISECOND = 0x00090000;
 	    public static final long SECOND = 0x00010000;
 	    public static final long MINUTE = 0x00020000;
 	    public static final long HOUR = 0x00030000;
 	    public static final long DAY = 0x00040000;
 	    public static final long WEEK = 0x00050000;
 	    public static final long MONTH = 0x00060000;
 	    public static final long YEAR = 0x00070000;
 	    public static final long DECADE = 0x000a0000;
 	    public static final long CENTURY = 0x000b0000;
 	    /**
 	     * if you need 'per' as in 'kph' (this is the default)
 	     */
 	    public static final long TIME_DIV = 0x00000000;
 	    /**
 	     * if you need 'in' instead of 'per'
 	     */
 	    public static final long TIME_MULT = 0x00100000;
 	    public static final long TIME_UNIT_MASK = 0x000f0000;
 	    public static final long TIME_MASK = 0x001f0000;
 	    
 	    public static final long MM = 0x00000001;
 	    public static final long CM = 0x00000002;
 	    public static final long M = 0x00000003;
 	    public static final long KM = 0x00000004;
 	    public static final long INCH = 0x00000005;
 	    public static final long FOOT = 0x00000006;
 	    public static final long YARD = 0x00000007;
 	    public static final long CHAIN = 0x00000008;
 	    public static final long MILE = 0x00000009;
 	    public static final long NAUTICAL_MILE = 0x0000000a;
 	    public static final long NAUTICAL_MILE_UK = 0x0000000b;
 	    
 	    public static final long MM2 = 0x00000100;
 	    public static final long CM2 = 0x00000101;
 	    public static final long M2 = 0x00000102;
 	    public static final long HECTARE = 0x00000103;
 	    public static final long KM2 = 0x00000104;
 	    public static final long IN2 = 0x00000105;
 	    public static final long FT2 = 0x00000106;
 	    public static final long YD2 = 0x00000107;
 	    public static final long ACRE = 0x00000108;
 	    public static final long MILE2 = 0x00000109;
 	    
 	    public static final long MM3 = 0x00000200;
 	    public static final long CM3 = 0x00000201;
 	    public static final long LITRE = 0x00000202;
 	    public static final long M3 = 0x00000203;
 	    public static final long KM3 = 0x00000204;
 	    public static final long IN3 = 0x00000205;
 	    public static final long FT3 = 0x00000206;
 	    public static final long YD3 = 0x00000207;
 	    public static final long MILE3 = 0x00000208;
 	    public static final long UK_FL_OZ = 0x00000209;
 	    public static final long UK_PINT = 0x0000020a;
 	    public static final long UK_QUART = 0x0000020b;
 	    public static final long UK_GALLON = 0x0000020c;
 	    public static final long BUSHEL = 0x0000020d;
 	    public static final long US_DRAM = 0x0000020e;
 	    public static final long US_FL_OZ = 0x0000020f;
 	    public static final long US_FL_PINT = 0x00000210;
 	    public static final long US_FL_QUART = 0x00000211;
 	    public static final long US_GALLON = 0x00000212;
 	    public static final long US_FL_BARREL = 0x00000213;
 	    public static final long US_DRY_PINT = 0x00000214;
 	    public static final long US_DRY_QUART = 0x00000215;
 	    public static final long US_DRY_BARREL = 0x00000216;
 	    
 	    public static final long KELVIN = 0x00000400;
 	    public static final long CELSIUS = 0x00000401;
 	    public static final long FAHRENHEIT = 0x00000402;
 	    public static final long RANKINE = 0x00000403;
 	    
 	    public static final long KPA = 0x00000500;
 	    public static final long PSI = 0x00000501;
 	    public static final long BAR = 0x00000502;
 	    public static final long ATM = 0x00000503;
 	    public static final long TORR = 0x00000504;
 	    
 	    public static final long MILLIGRAM = 0x00000600;
 	    public static final long GRAM = 0x00000601;
 	    public static final long KG = 0x00000602;
 	    public static final long TONNE = 0x00000603;
 	    public static final long OUNCE = 0x00000604;
 	    public static final long LB = 0x00000605;
 	    public static final long SHORT_TON = 0x00000606;
 	    public static final long TON = 0x00000607;
 	    
 	    public static final long JOULE = 0x00000700;
 	    public static final long KILOJOULE = 0x0000070a;
 	    public static final long ELECTRONVOLT = 0x00000701;
 	    public static final long ERG = 0x00000702;
 	    public static final long FT_LB = 0x00000703;
 	    public static final long CALORIE = 0x00000704;
 	    public static final long KG_METRE = 0x00000705;
 	    public static final long BTU = 0x00000706;
 	    public static final long WATT = 0x00000707;
 	    /**
 	     * this assumes hour in the conversion
 	     */
 	    public static final long WATT_SECOND = WATT | SECOND | TIME_MULT;
 	    /**
 	     * this assumes hour in the conversion
 	     */
 	    public static final long WATT_HOUR = WATT | HOUR | TIME_MULT;
 	    public static final long KILOWATT = 0x00000708;
 	    public static final long KILOWATT_SECOND = KILOWATT | SECOND | TIME_MULT;
 	    /**
 	     * this assumes hour in the conversion
 	     */
 	    public static final long KILOWATT_HOUR = KILOWATT | HOUR | TIME_MULT;
 	    public static final long THERM = 0x00000709;
 	    
 	    public static final long DECIMAL = 0x000004c0;
 	    public static final long PERCENT = 0x000004c1;
 	    /**
 	     * used if you store it as 15% but want it displayed at 85%
 	     */
 	    public static final long DECIMAL_INVERT = 0x000004c2;
 	    public static final long PERCENT_INVERT = 0x000004c3;
 	    
 	    /**
 	     * need this or one of the other 4x values (only this one is presently supported)
 	     */
 	    public static final long ANGLE = 0x000004b0;
 	    public static final long RADIAN = 0x00000000;
 	    public static final long CARTESIAN = 0x00000000;
 	    /**
 	     * can be OR'd with the other angle values
 	     */
 	    public static final long COMPASS = 0x01000000;
 	    /**
 	     * can be OR'd with the other angle values
 	     */
 	    public static final long DEGREE = 0x02000000;
 	    /**
 	     * can be OR'd with the other angle values
 	     */
 	    public static final long ARCSECOND = 0x04000000;
 	    public static final long ANGLE_ROTATION_MASK = 0x01000000;
 	    public static final long ANGLE_UNIT_MASK = 0x0e000000;
 	    public static final long ANGLE_MASK = 0x0f000000;
 	    
 	    public static final long COORDINATE_DEGREE = 0x00000800;
 	    public static final long COORDINATE_DEGREE_MINUTE = 0x00000801;
 	    public static final long COORDINATE_DEGREE_MINUTE_SECOND = 0x00000802;
 	    public static final long COORDINATE_UTM = 0x00000803;
 	    public static final long COORDINATE_RELATIVE_DISTANCE = 0x00000804;
 	    
 	    public static final long TONS_PER_ACRE = 0x00000900;
 	    public static final long KG_PER_M2 = 0x00000901;
 	    
 	    public static final long BTU_FT_S = 0x00000910;
 	    public static final long KILOWATT_PER_M = 0x00000911;
	}
}
