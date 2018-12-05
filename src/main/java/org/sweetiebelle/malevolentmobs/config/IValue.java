package org.sweetiebelle.malevolentmobs.config;

/**
 * Configuration files may store arbitrary data in the form of {@code IValue}s. These construct
 * allow loading of composite data to be easily loaded and saved to configuration files. Any such
 * values will be stored in a human-readable form, allowing configuration files to be edited outside
 * of the game.
 */
public interface IValue
{
    /**
     * Stores the value to the provided configuration under the given property.
     *
     * @param config   The configuration to store the value in
     * @param property The property to store the value under
     */
    void save(IConfig config, IProperty property);
    /**
     * Loads the value from the provided configuration under the given property.
     *
     * @param config   The configuration to load the value from
     * @param property The property to load the value from
     */
    void load(IConfig config, IProperty property);
}