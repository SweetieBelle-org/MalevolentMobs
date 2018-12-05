package org.sweetiebelle.malevolentmobs.test.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.sweetiebelle.malevolentmobs.test.MockEnum;
import org.sweetiebelle.malevolentmobs.test.TestBase;
import org.sweetiebelle.malevolentmobs.util.StringConverter;

class StringConverterTest extends TestBase
{
    @Test
    void testToBool()
    {
        assertEquals(true, StringConverter.toBool("true"));
        assertEquals(false, StringConverter.toBool("false"));
    }
    @Test
    void testToBools()
    {
        assertArrayEquals(bools(false, true, false),
                StringConverter.toBools("whatever true false", " "));
    }

    @Test
    void testToByte()
    {
        assertEquals((byte) 123, StringConverter.toByte("123"));
        assertEquals((byte) -42, StringConverter.toByte("-42"));
        assertEquals((byte) 0, StringConverter.toByte("invalid"));
        assertEquals((byte) 3, StringConverter.toByte("732516", (byte) 3));
    }
    @Test
    void testToBytes()
    {
        assertArrayEquals(bytes((byte) -6, (byte) 15, (byte) 0),
                StringConverter.toBytes("-6 15 invalid", " "));
    }

    @Test
    void testToDouble()
    {
        assertEquals(123.4, StringConverter.toDouble("123.4"));
        assertEquals(-42.0, StringConverter.toDouble("-42"));
        assertEquals(0.0, StringConverter.toDouble("invalid"));
        assertEquals(3.14, StringConverter.toDouble("73.251.6", 3.14));
    }
    @Test
    void testToDoubles()
    {
        assertArrayEquals(doubles(-6.0, 15.1, 0.0),
                StringConverter.toDoubles("-6 15.1 invalid", " "));
    }

    @Test
    void testToFloat()
    {
        assertEquals(123.4f, StringConverter.toFloat("123.4"));
        assertEquals(-42.0f, StringConverter.toFloat("-42"));
        assertEquals(0.0f, StringConverter.toFloat("invalid"));
        assertEquals(3.14f, StringConverter.toFloat("73.251.6", 3.14f));
    }
    @Test
    void testToFloats()
    {
        assertArrayEquals(floats(-6.0f, 15.1f, 0.0f),
                StringConverter.toFloats("-6 15.1 invalid", " "));
    }

    @Test
    void testToInt()
    {
        assertEquals(123, StringConverter.toInt("123"));
        assertEquals(-42, StringConverter.toInt("-42"));
        assertEquals(0, StringConverter.toInt("invalid"));
        assertEquals(3, StringConverter.toInt("73.2516", 3));
    }
    @Test
    void testToInts()
    {
        assertArrayEquals(ints(-6, 15, 0), StringConverter.toInts("-6 15 invalid", " "));
    }

    @Test
    void testToLong()
    {
        assertEquals(123L, StringConverter.toLong("123"));
        assertEquals(-42L, StringConverter.toLong("-42"));
        assertEquals(0L, StringConverter.toLong("invalid"));
        assertEquals(3L, StringConverter.toLong("73.2516", 3L));
    }
    @Test
    void testToLongs()
    {
        assertArrayEquals(longs(-6L, 15L, 0L), StringConverter.toLongs("-6 15 invalid", " "));
    }

    @Test
    void testToShort()
    {
        assertEquals((short) 123, StringConverter.toShort("123"));
        assertEquals((short) -42, StringConverter.toShort("-42"));
        assertEquals((short) 0, StringConverter.toShort("invalid"));
        assertEquals((short) 3, StringConverter.toShort("732516", (short) 3));
    }
    @Test
    void testToShorts()
    {
        assertArrayEquals(shorts((short) -6, (short) 15, (short) 0),
                StringConverter.toShorts("-6 15 invalid", " "));
    }

    @Test
    void testToString()
    {
        assertEquals("test", StringConverter.toString("test"));
        assertEquals("whatever", StringConverter.toString(null, "whatever"));
    }
    @Test
    void testToStrings()
    {
        assertArrayEquals(strings("Hello", "World", "!"),
                StringConverter.toStrings("Hello World !", " "));
    }

    @Test
    void testToEnum()
    {
        assertEquals(MockEnum.FOO, StringConverter.toEnum("foo", MockEnum::valueOf));
        assertEquals(MockEnum.BAR, StringConverter.toEnum("BAR", MockEnum::valueOf));
        assertEquals(null, StringConverter.toEnum("invalid", MockEnum::valueOf));
        assertEquals(MockEnum.BAR, StringConverter.toEnum("bah", MockEnum.BAR, MockEnum::valueOf));
    }
    @Test
    void testToEnums()
    {
        assertArrayEquals(enums(MockEnum.FOO, MockEnum.BAR, null),
                StringConverter.toEnums("foo bar bah", " ", MockEnum::valueOf, new MockEnum[0]));
    }
}
