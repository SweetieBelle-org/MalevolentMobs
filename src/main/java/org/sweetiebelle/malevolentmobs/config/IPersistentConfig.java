package org.sweetiebelle.malevolentmobs.config;

import java.io.IOException;

/**
 * A configuration file which should be written to disk should be marked as persistent. While such
 * configuration files will not be automatically loaded from disk or saved to disk when modified,
 * they provide the functionality to do so in an easy manner.
 */
public interface IPersistentConfig
{
    /**
     * Checks if the configuration exists on disk. The underlying OS might deny the mod from
     * checking if the file exists, in such case the file is treated as not existing.
     *
     * @return True iff the configuration exists on disk
     */
    boolean exists();
    /**
     * Creates the configuration on disk. The file will only be created if it does not already
     * exist. The file is not guaranteed to be created, the underlying OS may prevent the file from
     * being created.
     *
     * @return True iff the file was created.
     */
    boolean create();
    /**
     * Deletes the configuration from disk. The file is not guaranteed to be created, the underlying
     * OS may prevent the file from being deleted.
     *
     * @return True iff the file was deleted.
     */
    boolean delete();

    /**
     * Saves the configuration file to disk.
     *
     * @throws IOException If the file could not be saved fully.
     */
    void save() throws IOException;
    /**
     * Loads the configuration file from disk, discarding any unsaved changes already present within
     * it.
     *
     * @throws IOException If the file could not be loaded fully.
     */
    void load() throws IOException;
}
