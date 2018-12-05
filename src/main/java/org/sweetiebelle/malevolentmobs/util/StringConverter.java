package org.sweetiebelle.malevolentmobs.util;

import java.util.ArrayList;
import java.util.function.Function;

import org.apache.commons.lang3.ArrayUtils;

public final class StringConverter
{
    private static <T> T toGeneric(final String string, final T def,
            final Function<String, T> converter)
    {
        try
        {
            return converter.apply(string);
        }
        catch (final Exception e)
        {
            return def;
        }
    }
    private static <T> ArrayList<T> toGenerics(final String string, final T def, final String regex,
            final Function<String, T> converter)
    {
        final ArrayList<T> data = new ArrayList<>();
        for (final String part : string.split(regex))
            data.add(toGeneric(part, def, converter));
        return data;
    }

    // ...

    /**
     * Converts the given string to a boolean value, if possible. If the string could not be
     * converted, the default value {@code false} is returned.
     *
     * @param string The string to convert to a boolean value
     * @return The boolean value of the string or the default value
     */
    public static boolean toBool(final String string)
    {
        return toBool(string, false);
    }
    /**
     * Converts the given string to a boolean value, if possible. If the string could not be
     * converted, the default value is returned.
     *
     * @param string The string to convert to a boolean value
     * @param def    The default value if the string could not be converted
     * @return The boolean value of the string or the default value
     */
    public static boolean toBool(final String string, final boolean def)
    {
        return toGeneric(string, def, Boolean::parseBoolean);
    }
    /**
     * Converts the given string to an array of boolean values. If any element of the string could
     * not be converted, the default value {@code false} is used.
     *
     * @param string The string to convert to boolean values
     * @param regex  The regex separating two values from each other
     * @return The boolean array of the string values
     */
    public static boolean[] toBools(final String string, final String regex)
    {
        return toBools(string, false, regex);
    }
    /**
     * Converts the given string to an array of boolean values. If any element of the string could
     * not be converted, the default value is used.
     *
     * @param string The string to convert to boolean values
     * @param def    The default value if the string could not be converted
     * @param regex  The regex separating two values from each other
     * @return The boolean array of the string values
     */
    public static boolean[] toBools(final String string, final boolean def, final String regex)
    {
        return ArrayUtils.toPrimitive(
                toGenerics(string, def, regex, StringConverter::toBool).toArray(new Boolean[0]));
    }

    /**
     * Converts the given string to a byte value, if possible. If the string could not be converted,
     * the default value {@code 0} is returned.
     *
     * @param string The string to convert to a byte value
     * @return The byte value of the string or the default value
     */
    public static byte toByte(final String string)
    {
        return toByte(string, (byte) 0);
    }
    /**
     * Converts the given string to a byte value, if possible. If the string could not be converted,
     * the default value is returned.
     *
     * @param string The string to convert to a byte value
     * @param def    The default value if the string could not be converted
     * @return The byte value of the string or the default value
     */
    public static byte toByte(final String string, final byte def)
    {
        return toGeneric(string, def, Byte::parseByte);
    }
    /**
     * Converts the given string to an array of byte values. If any element of the string could not
     * be converted, the default value {@code 0} is used.
     *
     * @param string The string to convert to byte values
     * @param regex  The regex separating two values from each other
     * @return The byte array of the string values
     */
    public static byte[] toBytes(final String string, final String regex)
    {
        return toBytes(string, (byte) 0, regex);
    }
    /**
     * Converts the given string to an array of byte values. If any element of the string could not
     * be converted, the default value is used.
     *
     * @param string The string to convert to byte values
     * @param def    The default value if the string could not be converted
     * @param regex  The regex separating two values from each other
     * @return The byte array of the string values
     */
    public static byte[] toBytes(final String string, final byte def, final String regex)
    {
        return ArrayUtils.toPrimitive(
                toGenerics(string, def, regex, StringConverter::toByte).toArray(new Byte[0]));
    }

    /**
     * Converts the given string to a double value, if possible. If the string could not be
     * converted, the default value {@code 0} is returned.
     *
     * @param string The string to convert to a double value
     * @return The double value of the string or the default value
     */
    public static double toDouble(final String string)
    {
        return toDouble(string, 0.0);
    }
    /**
     * Converts the given string to a double value, if possible. If the string could not be
     * converted, the default value is returned.
     *
     * @param string The string to convert to a double value
     * @param def    The default value if the string could not be converted
     * @return The double value of the string or the default value
     */
    public static double toDouble(final String string, final double def)
    {
        return toGeneric(string, def, Double::parseDouble);
    }
    /**
     * Converts the given string to an array of double values. If any element of the string could
     * not be converted, the default value {@code 0} is used.
     *
     * @param string The string to convert to double values
     * @param regex  The regex separating two values from each other
     * @return The double array of the string values
     */
    public static double[] toDoubles(final String string, final String regex)
    {
        return toDoubles(string, 0.0, regex);
    }
    /**
     * Converts the given string to an array of double values. If any element of the string could
     * not be converted, the default value is used.
     *
     * @param string The string to convert to double values
     * @param def    The default value if the string could not be converted
     * @param regex  The regex separating two values from each other
     * @return The double array of the string values
     */
    public static double[] toDoubles(final String string, final double def, final String regex)
    {
        return ArrayUtils.toPrimitive(
                toGenerics(string, def, regex, StringConverter::toDouble).toArray(new Double[0]));
    }

    /**
     * Converts the given string to a float value, if possible. If the string could not be
     * converted, the default value {@code 0} is returned.
     *
     * @param string The string to convert to a float value
     * @return The float value of the string or the default value
     */
    public static float toFloat(final String string)
    {
        return toFloat(string, 0.0f);
    }
    /**
     * Converts the given string to a float value, if possible. If the string could not be
     * converted, the default value is returned.
     *
     * @param string The string to convert to a float value
     * @param def    The default value if the string could not be converted
     * @return The float value of the string or the default value
     */
    public static float toFloat(final String string, final float def)
    {
        return toGeneric(string, def, Float::parseFloat);
    }
    /**
     * Converts the given string to an array of float values. If any element of the string could not
     * be converted, the default value {@code 0} is used.
     *
     * @param string The string to convert to float values
     * @param regex  The regex separating two values from each other
     * @return The float array of the string values
     */
    public static float[] toFloats(final String string, final String regex)
    {
        return toFloats(string, 0.0f, regex);
    }
    /**
     * Converts the given string to an array of float values. If any element of the string could not
     * be converted, the default value is used.
     *
     * @param string The string to convert to float values
     * @param def    The default value if the string could not be converted
     * @param regex  The regex separating two values from each other
     * @return The float array of the string values
     */
    public static float[] toFloats(final String string, final float def, final String regex)
    {
        return ArrayUtils.toPrimitive(
                toGenerics(string, def, regex, StringConverter::toFloat).toArray(new Float[0]));
    }

    /**
     * Converts the given string to a integer value, if possible. If the string could not be
     * converted, the default value {@code 0} is returned.
     *
     * @param string The string to convert to a integer value
     * @return The integer value of the string or the default value
     */
    public static int toInt(final String string)
    {
        return toInt(string, 0);
    }
    /**
     * Converts the given string to a integer value, if possible. If the string could not be
     * converted, the default value is returned.
     *
     * @param string The string to convert to a integer value
     * @param def    The default value if the string could not be converted
     * @return The integer value of the string or the default value
     */
    public static int toInt(final String string, final int def)
    {
        return toGeneric(string, def, Integer::parseInt);
    }
    /**
     * Converts the given string to an array of integer values. If any element of the string could
     * not be converted, the default value {@code 0} is used.
     *
     * @param string The string to convert to integer values
     * @param regex  The regex separating two values from each other
     * @return The integer array of the string values
     */
    public static int[] toInts(final String string, final String regex)
    {
        return toInts(string, 0, regex);
    }
    /**
     * Converts the given string to an array of integer values. If any element of the string could
     * not be converted, the default value is used.
     *
     * @param string The string to convert to integer values
     * @param def    The default value if the string could not be converted
     * @param regex  The regex separating two values from each other
     * @return The integer array of the string values
     */
    public static int[] toInts(final String string, final int def, final String regex)
    {
        return ArrayUtils.toPrimitive(
                toGenerics(string, def, regex, StringConverter::toInt).toArray(new Integer[0]));
    }

    /**
     * Converts the given string to a long value, if possible. If the string could not be converted,
     * the default value {@code 0} is returned.
     *
     * @param string The string to convert to a long value
     * @return The long value of the string or the default value
     */
    public static long toLong(final String string)
    {
        return toLong(string, 0);
    }
    /**
     * Converts the given string to a long value, if possible. If the string could not be converted,
     * the default value is returned.
     *
     * @param string The string to convert to a long value
     * @param def    The default value if the string could not be converted
     * @return The long value of the string or the default value
     */
    public static long toLong(final String string, final long def)
    {
        return toGeneric(string, def, Long::parseLong);
    }
    /**
     * Converts the given string to an array of long values. If any element of the string could not
     * be converted, the default value {@code 0} is used.
     *
     * @param string The string to convert to long values
     * @param regex  The regex separating two values from each other
     * @return The long array of the string values
     */
    public static long[] toLongs(final String string, final String regex)
    {
        return toLongs(string, 0, regex);
    }
    /**
     * Converts the given string to an array of long values. If any element of the string could not
     * be converted, the default value is used.
     *
     * @param string The string to convert to long values
     * @param def    The default value if the string could not be converted
     * @param regex  The regex separating two values from each other
     * @return The long array of the string values
     */
    public static long[] toLongs(final String string, final long def, final String regex)
    {
        return ArrayUtils.toPrimitive(
                toGenerics(string, def, regex, StringConverter::toLong).toArray(new Long[0]));
    }

    /**
     * Converts the given string to a short value, if possible. If the string could not be
     * converted, the default value {@code 0} is returned.
     *
     * @param string The string to convert to a short value
     * @return The short value of the string or the default value
     */
    public static short toShort(final String string)
    {
        return toShort(string, (short) 0);
    }
    /**
     * Converts the given string to a short value, if possible. If the string could not be
     * converted, the default value is returned.
     *
     * @param string The string to convert to a short value
     * @param def    The default value if the string could not be converted
     * @return The short value of the string or the default value
     */
    public static short toShort(final String string, final short def)
    {
        return toGeneric(string, def, Short::parseShort);
    }
    /**
     * Converts the given string to an array of short values. If any element of the string could not
     * be converted, the default value {@code 0} is used.
     *
     * @param string The string to convert to short values
     * @param regex  The regex separating two values from each other
     * @return The short array of the string values
     */
    public static short[] toShorts(final String string, final String regex)
    {
        return toShorts(string, (short) 0, regex);
    }
    /**
     * Converts the given string to an array of short values. If any element of the string could not
     * be converted, the default value is used.
     *
     * @param string The string to convert to short values
     * @param def    The default value if the string could not be converted
     * @param regex  The regex separating two values from each other
     * @return The short array of the string values
     */
    public static short[] toShorts(final String string, final short def, final String regex)
    {
        return ArrayUtils.toPrimitive(
                toGenerics(string, def, regex, StringConverter::toShort).toArray(new Short[0]));
    }

    /**
     * Converts the given string to a string value, if possible. If the string is null, the default
     * value {@code ""} is returned.
     *
     * @param string The string to convert to a string value
     * @return The string value of the string or the default value
     */
    public static String toString(final String string)
    {
        return toString(string, "");
    }
    /**
     * Converts the given string to a string value, if possible. If the string is null, the default
     * value is returned.
     *
     * @param string The string to convert to a string value
     * @param def    The default value if the string could not be converted
     * @return The string value of the string or the default value
     */
    public static String toString(final String string, final String def)
    {
        return string == null ? def : string;
    }
    /**
     * Converts the given string to an array of string values. If any element of the string could
     * not be converted, the default value {@code 0} is used.
     *
     * @param string The string to convert to string values
     * @param regex  The regex separating two values from each other
     * @return The string array of the string values
     */
    public static String[] toStrings(final String string, final String regex)
    {
        return toStrings(string, "", regex);
    }
    /**
     * Converts the given string to an array of string values. If any element of the string could
     * not be converted, the default value is used.
     *
     * @param string The string to convert to string values
     * @param def    The default value if the string could not be converted
     * @param regex  The regex separating two values from each other
     * @return The string array of the string values
     */
    public static String[] toStrings(final String string, final String def, final String regex)
    {
        return string.split(regex);
    }

    /**
     * Converts the given string to an enum value, if possible. If the string could not be
     * converted, the default value {@code null} is returned.
     *
     * @param           <T> The type of the enum to retrieve
     * @param string    The string to convert to an enum value
     * @param converter The function which converts the string to an enum value, usually T::valueOf
     * @return The string value of the enum or the default value
     */
    public static <T extends Enum<T>> T toEnum(final String string,
            final Function<String, T> converter)
    {
        return toEnum(string, null, converter);
    }
    /**
     * Converts the given string to an enum value, if possible. If the string could not be
     * converted, the default value is returned.
     *
     * @param           <T> The type of the enum to retrieve
     * @param string    The string to convert to an enum value
     * @param def       The default value if the string could not be converted
     * @param converter The function which converts the string to an enum value, usually T::valueOf
     * @return The enum value of the string or the default value
     */
    public static <T extends Enum<T>> T toEnum(final String string, final T def,
            final Function<String, T> converter)
    {
        return toGeneric(string.toUpperCase(), def, converter);
    }
    /**
     * Converts the given string to an array of enum values. If any element of the string could not
     * be converted, the default value {@code null} is used.
     *
     * @param string The string to convert to enum values
     * @param regex  The regex separating two values from each other
     * @return The enum array of the string values
     */
    public static <T extends Enum<T>> T[] toEnums(final String string, final String regex,
            final Function<String, T> converter, final T[] array)
    {
        return toEnums(string, null, regex, converter, array);
    }
    /**
     * Converts the given string to an array of enum values. If any element of the string could not
     * be converted, the default value is used.
     *
     * @param string The string to convert to enum values
     * @param def    The default value if the string could not be converted
     * @param regex  The regex separating two values from each other
     * @return The enum array of the string values
     */
    public static <T extends Enum<T>> T[] toEnums(final String string, final T def,
            final String regex, final Function<String, T> converter, final T[] array)
    {
        return (T[]) ArrayUtils.toPrimitive(
                toGenerics(string.toUpperCase(), def, regex, converter).toArray(array));
    }
}
