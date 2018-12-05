package org.sweetiebelle.malevolentmobs.test.config;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.sweetiebelle.malevolentmobs.config.BasicConfig;
import org.sweetiebelle.malevolentmobs.config.IProperty;
import org.sweetiebelle.malevolentmobs.config.Property;
import org.sweetiebelle.malevolentmobs.test.MockEnum;
import org.sweetiebelle.malevolentmobs.test.TestBase;

class BaseConfigTest extends TestBase
{
    static final IProperty property = new Property("property");
    static final IProperty propertyEmpty = new Property();
    static final IProperty propertyNested = new Property("root", "nested");

    @Test
    void testHas()
    {
        BasicConfig config = new BasicConfig();

        assertFalse(config.has(property));
        assertFalse(config.has(propertyEmpty));
    }
    @Test
    void testHasNested()
    {
        BasicConfig config = new BasicConfig();
        config.add(property.child("A"), 0);
        config.add(property.child("B"), 0);

        assertTrue(config.has(property));
        assertTrue(config.has(property.child("A")));
        assertTrue(config.has(property.child("B")));
    }

    @Test
    void testAdd()
    {
        BasicConfig config = new BasicConfig();
        config.add(property, 0);
        config.add(property, 1);

        assertTrue(config.has(property));
        assertEquals(0, config.getInt(property));
    }
    @Test
    void testAddNested()
    {
        BasicConfig config = new BasicConfig();
        config.add(property.child("A"), 2);
        config.add(property.child("A"), 1);
        config.add(property.child("B"), 5);
        config.add(property.child("B"), 8);

        assertEquals(2, config.getInt(property.child("A")));
        assertEquals(5, config.getInt(property.child("B")));
    }

    @Test
    void testSet()
    {
        BasicConfig config = new BasicConfig();
        config.set(property, 0);
        config.set(property, 1);

        assertTrue(config.has(property));
        assertEquals(1, config.getInt(property));
    }
    @Test
    void testSetNested()
    {
        BasicConfig config = new BasicConfig();
        config.set(property.child("A"), 2);
        config.set(property.child("A"), 1);
        config.set(property.child("B"), 5);
        config.set(property.child("B"), 8);

        assertEquals(1, config.getInt(property.child("A")));
        assertEquals(8, config.getInt(property.child("B")));
    }

    @Test
    void testRemove()
    {
        BasicConfig config = new BasicConfig();
        config.add(property, 0);
        config.remove(property);

        assertFalse(config.has(property));
    }
    @Test
    void testRemoveNested()
    {
        BasicConfig config = new BasicConfig();
        config.add(propertyNested.child("A"), 0);
        config.add(propertyNested.child("B"), 0);
        config.remove(propertyNested);

        assertFalse(config.has(propertyNested));
        assertFalse(config.has(propertyNested.child("A")));
        assertFalse(config.has(propertyNested.child("B")));
    }

    @Test
    void testClear()
    {
        BasicConfig config = new BasicConfig();
        config.add(property, 0);
        config.add(propertyNested, 0);
        config.clear();

        assertFalse(config.has(property));
        assertFalse(config.has(propertyNested));
    }

    @Test
    void testProperties()
    {
        BasicConfig config = new BasicConfig();
        config.add(property, 0);
        config.add(propertyNested.child("A"), 0);
        config.add(propertyNested.child("B"), 0);
        config.add(propertyNested.child("C"), 0);

        Set<IProperty> propertiesA = config.properties();
        Set<IProperty> propertiesB = config.properties(propertyNested);

        assertEquals(2, propertiesA.size());
        assertTrue(propertiesA.contains(property));
        assertTrue(propertiesA.contains(propertyNested.parent()));
        assertEquals(3, propertiesB.size());
        assertTrue(propertiesB.contains(propertyNested.child("A")));
        assertTrue(propertiesB.contains(propertyNested.child("B")));
        assertTrue(propertiesB.contains(propertyNested.child("C")));
    }

    // ...

    @Test
    void testGet()
    {
        BasicConfig config = new BasicConfig();
        config.add(property.child("bool"), true);
        config.add(property.child("byte"), (byte) 42);
        config.add(property.child("double"), 3.14);
        config.add(property.child("float"), -9.4f);
        config.add(property.child("int"), 1337);
        config.add(property.child("long"), 0xC0FFEEDEADBEEFL);
        config.add(property.child("short"), (short) 0xF00);
        config.add(property.child("string"), "Hello World!");
        config.add(property.child("enum"), MockEnum.BAR);

        assertTrue(config.getBool(property.child("bool")));
        assertEquals((byte) 42, config.getByte(property.child("byte")));
        assertEquals(3.14, config.getDouble(property.child("double")));
        assertEquals(-9.4f, config.getFloat(property.child("float")));
        assertEquals(1337, config.getInt(property.child("int")));
        assertEquals(0xC0FFEEDEADBEEFL, config.getLong(property.child("long")));
        assertEquals((short) 0xF00, config.getShort(property.child("short")));
        assertEquals("Hello World!", config.getString(property.child("string")));
        assertEquals(MockEnum.BAR, config.getEnum(property.child("enum"), MockEnum::valueOf));
    }
    @Test
    void testGetArray()
    {
        BasicConfig config = new BasicConfig();
        config.add(property.child("bool"), bools(true, false));
        config.add(property.child("byte"), bytes((byte) 42, (byte) 120));
        config.add(property.child("double"), doubles(3.14, -5.001));
        config.add(property.child("float"), floats(-9.4f, 0.1f));
        config.add(property.child("int"), ints(1337, 0xC0FFEE));
        config.add(property.child("long"), longs(0xC0FFEEDEADBEEFL, 2L));
        config.add(property.child("short"), shorts((short) 5, (short) 4));
        config.add(property.child("string"), strings("Woo!", "Hi"));

        assertArrayEquals(bools(true, false), config.getBools(property.child("bool")));
        assertArrayEquals(bytes((byte) 42, (byte) 120), config.getBytes(property.child("byte")));
        assertArrayEquals(doubles(3.14, -5.001), config.getDoubles(property.child("double")));
        assertArrayEquals(floats(-9.4f, 0.1f), config.getFloats(property.child("float")));
        assertArrayEquals(ints(1337, 0xC0FFEE), config.getInts(property.child("int")));
        assertArrayEquals(longs(0xC0FFEEDEADBEEFL, 2L), config.getLongs(property.child("long")));
        assertArrayEquals(shorts((short) 5, (short) 4), config.getShorts(property.child("short")));
        assertArrayEquals(strings("Woo!", "Hi"), config.getStrings(property.child("string")));
    }
    @Test
    void testGetValue()
    {
        BasicConfig config = new BasicConfig();
        config.add(property, new MockValue(42, "Hello World!"));

        MockValue value = config.getValue(property, new MockValue());
        assertEquals(42, value.number);
        assertEquals("Hello World!", value.text);
    }
}
