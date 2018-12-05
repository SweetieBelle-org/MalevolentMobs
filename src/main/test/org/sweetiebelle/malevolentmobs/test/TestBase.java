package org.sweetiebelle.malevolentmobs.test;

/**
 * Basic test unit which provides some helper function for tests. Makes tedious bits of code much
 * shorter and simpler.
 */
public abstract class TestBase
{
    protected final <T> T[] generics(final T... values)
    {
        return values;
    }
    protected final boolean[] bools(final boolean... values)
    {
        return values;
    }
    protected final byte[] bytes(final byte... values)
    {
        return values;
    }
    protected final double[] doubles(final double... values)
    {
        return values;
    }
    protected final float[] floats(final float... values)
    {
        return values;
    }
    protected final int[] ints(final int... values)
    {
        return values;
    }
    protected final long[] longs(final long... values)
    {
        return values;
    }
    protected final short[] shorts(final short... values)
    {
        return values;
    }
    protected final String[] strings(final String... values)
    {
        return values;
    }
    protected final <T extends Enum> T[] enums(final T... values)
    {
        return values;
    }
}