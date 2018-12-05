package org.sweetiebelle.malevolentmobs.test.config;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sweetiebelle.malevolentmobs.config.IProperty;
import org.sweetiebelle.malevolentmobs.config.Property;
import org.sweetiebelle.malevolentmobs.config.YAMLConfig;
import org.sweetiebelle.malevolentmobs.test.TestBase;

class YAMLConfigTest extends TestBase
{
    static final File fileA = new File("test_config_a.yml");
    static final File fileB = new File("test_config_b.yml");
    static final IProperty property = new Property("property");

    @BeforeEach
    void setUp() throws Exception
    {
        fileA.createNewFile();
    }
    @AfterEach
    void tearDown() throws Exception
    {
        fileA.delete();
        fileB.delete();
    }

    @Test
    void testExists()
    {
        YAMLConfig configA = new YAMLConfig(fileA);
        YAMLConfig configB = new YAMLConfig(fileB);

        assertTrue(configA.exists());
        assertFalse(configB.exists());
    }
    @Test
    void testCreate()
    {
        YAMLConfig configA = new YAMLConfig(fileA);
        YAMLConfig configB = new YAMLConfig(fileB);

        assertFalse(configA.create());
        assertTrue(configB.create());
    }
    @Test
    void testDelete()
    {
        YAMLConfig configA = new YAMLConfig(fileA);
        YAMLConfig configB = new YAMLConfig(fileB);

        assertTrue(configA.delete());
        assertFalse(configB.delete());
    }

    @Test
    void testSave() throws Exception
    {
        YAMLConfig config = new YAMLConfig(fileA);
        config.set(property.child("offspring"), 42);
        config.set(property.child("nested.A"), 3);
        config.set(property.child("nested.B"), 5);
        config.set(property.child("list"), strings("Foo", "Bar", "Hello World!"));
        config.save();

        fail("Not implemented");
    }
    @Test
    void testLoad()
    {
        fail("Not implemented");
    }
}
