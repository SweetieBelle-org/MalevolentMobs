package org.sweetiebelle.malevolentmobs.test.config;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.sweetiebelle.malevolentmobs.config.IProperty;
import org.sweetiebelle.malevolentmobs.config.Property;

class PropertyTest
{
    @Test
    void testDefaultCtor()
    {
        final IProperty property = new Property();

        validate(property, "", "", "");
    }
    @Test
    void testPathCtor()
    {
        final IProperty propertyA = new Property("");
        final IProperty propertyB = new Property("name");
        final IProperty propertyC = new Property("root.name");

        validate(propertyA, "", "", "");
        validate(propertyB, "name", "", "name");
        validate(propertyC, "root.name", "root", "name");
    }
    @Test
    void testRootNameCtor()
    {
        final IProperty propertyA = new Property("", "");
        final IProperty propertyB = new Property("", "name");
        final IProperty propertyC = new Property("root", "name");

        validate(propertyA, "", "", "");
        validate(propertyB, "name", "", "name");
        validate(propertyC, "root.name", "root", "name");
    }
    @Test
    void testFileCtor()
    {
        final IProperty propertyA = new Property(new File("name.yml"));
        final IProperty propertyB = new Property(new File("folder/file.yml"));
        final IProperty propertyC = new Property(new File("root", "name.yml"));

        validate(propertyA, "name", "", "name");
        validate(propertyB, "folder.file", "folder", "file");
        validate(propertyC, "root.name", "root", "name");
    }

    @Test
    void testFile()
    {
        final IProperty property = new Property("some.property.path");

        assertEquals("some\\property\\path.yml", property.file("yml").getPath());
    }

    @Test
    void testParent()
    {
        final IProperty propertyA = new Property("child");
        final IProperty propertyB = new Property("parent.child");
        final IProperty propertyC = new Property("root.parent.child");

        validate(propertyA.parent(), "", "", "");
        validate(propertyB.parent(), "parent", "", "parent");
        validate(propertyC.parent(), "root.parent", "root", "parent");
    }
    @Test
    void testChild()
    {
        final IProperty property = new Property("parent");
        final IProperty childA = new Property("child");
        final IProperty childB = new Property("root", "child");

        validate(property.child("child"), "parent.child", "parent", "child");
        validate(property.child(childA), "parent.child", "parent", "child");
        validate(property.child(childB), "parent.root.child", "parent.root", "child");
    }

    @Test
    void testEquals()
    {
        final IProperty propertyA = new Property("root.name");
        final IProperty propertyB = new Property("root.name");
        final IProperty propertyC = new Property("root.different");

        assertTrue(propertyA.equals(propertyB));
        assertFalse(propertyA.equals(propertyC));
    }

    // ...

    void validate(final IProperty property, final String path, final String root, final String name)
    {
        assertEquals(path, property.path());
        assertEquals(root, property.root());
        assertEquals(name, property.name());
    }
}
