package org.sweetiebelle.malevolentmobs.config;

import java.io.File;

/**
 * Configuration files contains data which may be accessed through properties. A property points to
 * a specific data field stored inside a {@link IConfig}.
 */
public interface IProperty
{
    /**
     * Represents the full path for the property within the configuration
     *
     * @return The path of the property
     */
    String path();
    /**
     * Represents the root path for the property. If the property has no root, an empty string is
     * returned. The root path is identical to the path of the parent.
     *
     * @return The root of the property
     */
    String root();
    /**
     * Represents the name of the property.
     *
     * @return The name of the property
     */
    String name();
    /**
     * Represents the property as a file relative to the mod data directory.
     *
     * @param extension The extension of the file
     * @return The file of the property
     */
    File file(String extension);

    /**
     * Retrieves the parent of the property, if one exists. If no parent is available, an empty
     * property is returned.
     *
     * @return The parent of the property
     */
    IProperty parent();
    /**
     * Retrieves a child property with the given name.
     *
     * @param child The name of the child
     * @return A property instance of the child
     */
    IProperty child(String child);
    /**
     * Retrieves a child with the given relative path from the current property.
     *
     * @param child The path of the child
     * @return A property instance of the child
     */
    IProperty child(IProperty child);
}
