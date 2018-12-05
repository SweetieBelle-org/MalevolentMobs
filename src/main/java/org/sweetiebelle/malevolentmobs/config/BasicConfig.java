package org.sweetiebelle.malevolentmobs.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

import org.sweetiebelle.malevolentmobs.util.StringConverter;

import com.google.common.primitives.Booleans;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.common.primitives.Shorts;

/**
 * Represents the basic configuration file layer which stores raw data.
 */
public class BasicConfig implements IConfig
{
    /**
     * Represents the internal data structure to hold the raw data in the configuration file.
     */
    protected class DataMap
    {
        protected final Map<String, Object> data = new HashMap<>();

        /**
         * Attempts to locate the deeply nested data map associated with the given path. If there is
         * no map associated with the path (not existing or the path specifies something beyond a
         * value), null is returned. If {@code create} is true, then the data maps will be created
         * as necessary, overwriting any values that was in the way.
         *
         * @param path   The path to resolve to a data map.
         * @param create Whether the data maps that are missing should be created or not.
         * @return The data map associated with the path or null if none were found.
         */
        private DataMap findDataMap(String path, boolean create)
        {
            final int index = path.indexOf('.');
            if (index == -1)
                return this;

            final String name = path.substring(0, index);
            final String rest = path.substring(index + 1);
            final Object object = data.get(name);

            if (object instanceof DataMap)
                return ((DataMap) object).findDataMap(rest, create);
            else if (create)
            {
                DataMap map = new DataMap();
                data.put(name, map);
                return map.findDataMap(rest, create);
            }
            return null;
        }
        /**
         * Retrieves the name of the value associated with the path. This will typically be the name
         * of the property.
         *
         * @param path The path to resolve to a value name.
         * @return The name of the value associated with the path.
         */
        private String getValueName(String path)
        {
            final int index = path.lastIndexOf('.');
            return index == -1 ? path : path.substring(index + 1);
        }

        /**
         * Retrieves the object stored at the specified path. If the path points to a data map, that
         * map is retrieved. Otherwise, the value pointed to by the path is returned.
         *
         * @param path The path to the object to retrieve.
         * @return The object which was found, or null if no object was found.
         */
        public Object get(String path)
        {
            final DataMap map = findDataMap(path, false);
            return map == null ? null : map.data.get(getValueName(path));
        }
        /**
         * Assigns a value to the object pointed to by the specific path. If the object did not
         * exist, it will be created.
         *
         * @param path  The path to the object to assign a value to.
         * @param value The value which should be assigned to the object.
         */
        public void set(String path, Object value)
        {
            final DataMap map = findDataMap(path, true);
            if (map != null)
                map.data.put(getValueName(path), value);
        }
        /**
         * Removes the object pointed to by the specific path.
         *
         * @param path The path to the object to remove.
         */
        public void remove(String path)
        {
            final DataMap map = findDataMap(path, false);
            if (map != null)
                map.data.remove(getValueName(path));
        }
    }

    protected final DataMap data = new DataMap();

    // ...

    @Override
    public boolean has(IProperty property)
    {
        return data.get(property.path()) != null;
    }

    @Override
    public void add(IProperty property, Object value)
    {
        if (!has(property))
            set(property, value);
    }
    @Override
    public void set(IProperty property, Object value)
    {
        // Enums can only be stored in string format
        if (value instanceof Enum)
            value = value.toString();

        // Convert all legal arrays to lists
        if (value instanceof boolean[])
            value = Booleans.asList((boolean[]) value);
        else if (value instanceof byte[])
            value = Bytes.asList((byte[]) value);
        else if (value instanceof double[])
            value = Doubles.asList((double[]) value);
        else if (value instanceof float[])
            value = Floats.asList((float[]) value);
        else if (value instanceof int[])
            value = Ints.asList((int[]) value);
        else if (value instanceof long[])
            value = Longs.asList((long[]) value);
        else if (value instanceof short[])
            value = Shorts.asList((short[]) value);
        else if (value instanceof String[])
            value = Arrays.asList((String[]) value);

        // Can only store certain types of data
        if (value instanceof IValue)
            ((IValue) value).save(this, property);
        else if (value instanceof Boolean || value instanceof Number || value instanceof String
                || value instanceof Iterable)
            data.set(property.path(), value);
        else
            throw new IllegalArgumentException("Cannot store object " + value.getClass());
    }

    @Override
    public void remove(IProperty property)
    {
        data.remove(property.path());
    }
    @Override
    public void clear()
    {
        data.data.clear();
    }

    @Override
    public Set<IProperty> properties()
    {
        Set<IProperty> set = new TreeSet<>(
                (final IProperty lhs, final IProperty rhs) -> lhs.path().compareTo(rhs.path()));

        data.data.keySet().forEach(path -> set.add(new Property(path)));
        return Collections.unmodifiableSet(set);
    }
    @Override
    public Set<IProperty> properties(IProperty property)
    {
        Set<IProperty> set = new TreeSet<>(
                (final IProperty lhs, final IProperty rhs) -> lhs.path().compareTo(rhs.path()));

        final Object object = this.data.get(property.path());
        if (object instanceof DataMap)
            ((DataMap) object).data.keySet().forEach(path -> set.add(property.child(path)));
        return Collections.unmodifiableSet(set);
    }

    // ...

    @Override
    public boolean getBool(IProperty property)
    {
        return getBool(property, false);
    }
    @Override
    public boolean getBool(IProperty property, boolean def)
    {
        final Object value = data.get(property.path());
        if (value instanceof Boolean)
            return (Boolean) value;
        return def;
    }
    @Override
    public boolean[] getBools(IProperty property, boolean... def)
    {
        final Object value = data.get(property.path());
        if (value instanceof Collection)
            return Booleans.toArray((Collection) value);
        return def;
    }

    @Override
    public byte getByte(IProperty property)
    {
        return getByte(property, (byte) 0);
    }
    @Override
    public byte getByte(IProperty property, byte def)
    {
        final Object value = data.get(property.path());
        return value instanceof Number ? ((Number) value).byteValue() : def;
    }
    @Override
    public byte[] getBytes(IProperty property, byte... def)
    {
        final Object value = data.get(property.path());
        if (value instanceof Collection)
            return Bytes.toArray((Collection) value);
        return def;
    }

    @Override
    public double getDouble(IProperty property)
    {
        return getDouble(property, 0.0);
    }
    @Override
    public double getDouble(IProperty property, double def)
    {
        final Object value = data.get(property.path());
        return value instanceof Number ? ((Number) value).doubleValue() : def;
    }
    @Override
    public double[] getDoubles(IProperty property, double... def)
    {
        final Object value = data.get(property.path());
        if (value instanceof Collection)
            return Doubles.toArray((Collection) value);
        return def;
    }

    @Override
    public float getFloat(IProperty property)
    {
        return getFloat(property, 0.0f);
    }
    @Override
    public float getFloat(IProperty property, float def)
    {
        final Object value = data.get(property.path());
        return value instanceof Number ? ((Number) value).floatValue() : def;
    }
    @Override
    public float[] getFloats(IProperty property, float... def)
    {
        final Object value = data.get(property.path());
        if (value instanceof Collection)
            return Floats.toArray((Collection) value);
        return def;
    }

    @Override
    public int getInt(IProperty property)
    {
        return getInt(property, 0);
    }
    @Override
    public int getInt(IProperty property, int def)
    {
        final Object value = data.get(property.path());
        return value instanceof Number ? ((Number) value).intValue() : def;
    }
    @Override
    public int[] getInts(IProperty property, int... def)
    {
        final Object value = data.get(property.path());
        if (value instanceof Collection)
            return Ints.toArray((Collection) value);
        return def;
    }

    @Override
    public long getLong(IProperty property)
    {
        return getLong(property, 0L);
    }
    @Override
    public long getLong(IProperty property, long def)
    {
        final Object value = data.get(property.path());
        return value instanceof Number ? ((Number) value).longValue() : def;
    }
    @Override
    public long[] getLongs(IProperty property, long... def)
    {
        final Object value = data.get(property.path());
        if (value instanceof Collection)
            return Longs.toArray((Collection) value);
        return def;
    }

    @Override
    public short getShort(IProperty property)
    {
        return getShort(property, (short) 0);
    }
    @Override
    public short getShort(IProperty property, short def)
    {
        final Object value = data.get(property.path());
        return value instanceof Number ? ((Number) value).shortValue() : def;
    }
    @Override
    public short[] getShorts(IProperty property, short... def)
    {
        final Object value = data.get(property.path());
        if (value instanceof Collection)
            return Shorts.toArray((Collection) value);
        return def;
    }

    @Override
    public String getString(IProperty property)
    {
        return getString(property, "");
    }
    @Override
    public String getString(IProperty property, String def)
    {
        final Object value = data.get(property.path());
        return value instanceof String ? (String) value : def;
    }
    @Override
    public String[] getStrings(IProperty property, String... def)
    {
        final Object value = data.get(property.path());
        if (value instanceof Collection)
            return ((Collection<String>) value).toArray(new String[0]);
        return def;
    }

    @Override
    public <T extends Enum<T>> T getEnum(IProperty property, Function<String, T> parser)
    {
        return getEnum(property, null, parser);
    }
    @Override
    public <T extends Enum<T>> T getEnum(IProperty property, T def, Function<String, T> parser)
    {
        final Object value = data.get(property.path());
        if (value instanceof String)
            return StringConverter.toEnum((String) value, def, parser);
        return def;
    }

    @Override
    public <T extends IValue> T getValue(IProperty property, T value)
    {
        if (value != null)
            value.load(this, property);
        return value;
    }
}
