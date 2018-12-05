package org.sweetiebelle.malevolentmobs.config;

import java.util.Set;
import java.util.function.Function;

/**
 * Users using the mod might often want to change configuration values. Such values may be stored in
 * a {@link Config} file, which represents configurations in a human-readable format. The data will
 * be uniquely identifiable through a single key ({@link IProperty}). The data structure is
 * hierarchical, such that one property may reference a whole collection of sub-properties.
 */
public interface IConfig
{
    /**
     * Checks if the configuration file has the specified property.
     *
     * @param property The property to look up
     * @return True iff the property exists within the configuration
     */
    boolean has(IProperty property);
    /**
     * Adds a new property with the given value to the configuration. If the property already
     * exists, the new value is not assigned to the configuration. Only booleans, bytes, double,
     * floats, integers, longs, shorts, strings, enums, plus their array/collection variants, as
     * well as {@link IValue}s are accepted.
     *
     * @param property The new property to add
     * @param value    The value to add
     */
    void add(IProperty property, Object value);
    /**
     * Assigns the value of the given property within the configuration. If the property does not
     * exist, it will be added. Only booleans, bytes, double, floats, integers, longs, shorts,
     * strings, enums, plus their array/collection variants, as well as {@link IValue}s are
     * accepted.
     *
     * @param property The property to assign to
     * @param value    The value to assign
     */
    void set(IProperty property, Object value);

    /**
     * Removes the property from the configuration if it exists.
     *
     * @param property The property to remove
     */
    void remove(IProperty property);
    /**
     * Removes all properties from the configuration.
     */
    void clear();

    /**
     * Retrieves all properties stored at the root level of the configuration.
     *
     * @return The properties at the root level
     */
    Set<IProperty> properties();
    /**
     * Retrieves all properties stored at the given property level of the configuration.
     *
     * @return The properties at the property level
     */
    Set<IProperty> properties(IProperty property);

    // ...

    /**
     * Retrieves a boolean value from the configuration, under the given property. If the property
     * does not exist, the default value {@code false} is returned.
     *
     * @param property The property to read from
     * @return The value stored in the configuration
     */
    boolean getBool(IProperty property);
    /**
     * Retrieves a boolean value from the configuration, under the given property. If the property
     * does not exist, the default value is returned.
     *
     * @param property The property to read from
     * @param def      The default value
     * @return The value stored in the configuration
     */
    boolean getBool(IProperty property, boolean def);
    /**
     * Retrieves multiple boolean values from the configuration, under the given property. If the
     * property does not exist, the default values are returned.
     *
     * @param property The property to read from
     * @param def      The default values
     * @return The values stored in the configuration
     */
    boolean[] getBools(IProperty property, boolean... def);

    /**
     * Retrieves a byte value from the configuration, under the given property. If the property does
     * not exist, the default value {@code 0} is returned.
     *
     * @param property The property to read from
     * @return The value stored in the configuration
     */
    byte getByte(IProperty property);
    /**
     * Retrieves a byte value from the configuration, under the given property. If the property does
     * not exist, the default value is returned.
     *
     * @param property The property to read from
     * @param def      The default value
     * @return The value stored in the configuration
     */
    byte getByte(IProperty property, byte def);
    /**
     * Retrieves multiple byte values from the configuration, under the given property. If the
     * property does not exist, the default values are returned.
     *
     * @param property The property to read from
     * @param def      The default values
     * @return The values stored in the configuration
     */
    byte[] getBytes(IProperty property, byte... def);

    /**
     * Retrieves a double value from the configuration, under the given property. If the property
     * does not exist, the default value {@code 0.0} is returned.
     *
     * @param property The property to read from
     * @return The value stored in the configuration
     */
    double getDouble(IProperty property);
    /**
     * Retrieves a double value from the configuration, under the given property. If the property
     * does not exist, the default value is returned.
     *
     * @param property The property to read from
     * @param def      The default value
     * @return The value stored in the configuration
     */
    double getDouble(IProperty property, double def);
    /**
     * Retrieves multiple double values from the configuration, under the given property. If the
     * property does not exist, the default values are returned.
     *
     * @param property The property to read from
     * @param def      The default values
     * @return The values stored in the configuration
     */
    double[] getDoubles(IProperty property, double... def);

    /**
     * Retrieves a float value from the configuration, under the given property. If the property
     * does not exist, the default value {@code 0.0f} is returned.
     *
     * @param property The property to read from
     * @return The value stored in the configuration
     */
    float getFloat(IProperty property);
    /**
     * Retrieves a float value from the configuration, under the given property. If the property
     * does not exist, the default value is returned.
     *
     * @param property The property to read from
     * @param def      The default value
     * @return The value stored in the configuration
     */
    float getFloat(IProperty property, float def);
    /**
     * Retrieves multiple float values from the configuration, under the given property. If the
     * property does not exist, the default values are returned.
     *
     * @param property The property to read from
     * @param def      The default values
     * @return The values stored in the configuration
     */
    float[] getFloats(IProperty property, float... def);

    /**
     * Retrieves an integer value from the configuration, under the given property. If the property
     * does not exist, the default value {@code 0} is returned.
     *
     * @param property The property to read from
     * @return The value stored in the configuration
     */
    int getInt(IProperty property);
    /**
     * Retrieves a integer value from the configuration, under the given property. If the property
     * does not exist, the default value is returned.
     *
     * @param property The property to read from
     * @param def      The default value
     * @return The value stored in the configuration
     */
    int getInt(IProperty property, int def);
    /**
     * Retrieves multiple integer values from the configuration, under the given property. If the
     * property does not exist, the default values are returned.
     *
     * @param property The property to read from
     * @param def      The default values
     * @return The values stored in the configuration
     */
    int[] getInts(IProperty property, int... def);

    /**
     * Retrieves a long value from the configuration, under the given property. If the property does
     * not exist, the default value {@code 0L} is returned.
     *
     * @param property The property to read from
     * @return The value stored in the configuration
     */
    long getLong(IProperty property);
    /**
     * Retrieves a long value from the configuration, under the given property. If the property does
     * not exist, the default value is returned.
     *
     * @param property The property to read from
     * @param def      The default value
     * @return The value stored in the configuration
     */
    long getLong(IProperty property, long def);
    /**
     * Retrieves multiple long values from the configuration, under the given property. If the
     * property does not exist, the default values are returned.
     *
     * @param property The property to read from
     * @param def      The default values
     * @return The values stored in the configuration
     */
    long[] getLongs(IProperty property, long... def);

    /**
     * Retrieves a short value from the configuration, under the given property. If the property
     * does not exist, the default value {@code 0} is returned.
     *
     * @param property The property to read from
     * @return The value stored in the configuration
     */
    short getShort(IProperty property);
    /**
     * Retrieves a short value from the configuration, under the given property. If the property
     * does not exist, the default value is returned.
     *
     * @param property The property to read from
     * @param def      The default value
     * @return The value stored in the configuration
     */
    short getShort(IProperty property, short def);
    /**
     * Retrieves multiple short values from the configuration, under the given property. If the
     * property does not exist, the default values are returned.
     *
     * @param property The property to read from
     * @param def      The default values
     * @return The values stored in the configuration
     */
    short[] getShorts(IProperty property, short... def);

    /**
     * Retrieves a string value from the configuration, under the given property. If the property
     * does not exist, the default value {@code ""} is returned.
     *
     * @param property The property to read from
     * @return The value stored in the configuration
     */
    String getString(IProperty property);
    /**
     * Retrieves a string value from the configuration, under the given property. If the property
     * does not exist, the default value is returned.
     *
     * @param property The property to read from
     * @param def      The default value
     * @return The value stored in the configuration
     */
    String getString(IProperty property, String def);
    /**
     * Retrieves multiple string values from the configuration, under the given property. If the
     * property does not exist, the default values are returned.
     *
     * @param property The property to read from
     * @param def      The default values
     * @return The values stored in the configuration
     */
    String[] getStrings(IProperty property, String... def);

    /**
     * Retrieves an enum value from the configuration, under the given property. If the property
     * does not exist, the default value {@code null} is returned.
     *
     * @param          <T> The type of the enum to retrieve
     * @param property The property to read from
     * @param parser   The converter function, which converts a string to an enum. This should
     *                 usually be T::valueOf
     * @return The value stored in the configuration
     */
    <T extends Enum<T>> T getEnum(IProperty property, Function<String, T> parser);
    /**
     * Retrieves an enum value from the configuration, under the given property. If the property
     * does not exist, the default value is returned.
     *
     * @param          <T> The type of the enum to retrieve
     * @param property The property to read from
     * @param parser   The converter function, which converts a string to an enum. This should
     *                 usually be T::valueOf
     * @return The value stored in the configuration
     */
    <T extends Enum<T>> T getEnum(IProperty property, T def, Function<String, T> parser);

    /**
     * Prompts the input value to load itself from the configuration.
     *
     * @param          <T> The type of the value to retrieve
     * @param property The property to read from
     * @param value    The input value to load up
     * @return The same value as passed in
     */
    <T extends IValue> T getValue(IProperty property, T value);
}