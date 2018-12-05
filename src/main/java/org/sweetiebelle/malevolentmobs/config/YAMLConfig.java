package org.sweetiebelle.malevolentmobs.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

/**
 * Stores a configuration file on disk in a YAML format.
 */
public final class YAMLConfig extends BasicConfig implements IPersistentConfig
{
    private final File file;

    /**
     * Constructs a new YAML configuration file from the specified file.
     *
     * @param file The file to load and store the configuration file to.
     */
    public YAMLConfig(final File file)
    {
        this.file = file;
    }

    // ...

    @Override
    public boolean exists()
    {
        try
        {
            return file.exists();
        }
        catch (final Exception e)
        {
            return false;
        }
    }
    @Override
    public boolean create()
    {
        try
        {
            return file.createNewFile();
        }
        catch (final Exception e)
        {
            return false;
        }
    }
    @Override
    public boolean delete()
    {
        try
        {
            return file.delete();
        }
        catch (final Exception e)
        {
            return false;
        }
    }

    private void save(final BufferedWriter writer, final DataMap data, int indentation)
            throws IOException
    {
        for (final Entry<String, Object> entry : data.data.entrySet())
        {
            final String key = entry.getKey();
            final Object value = entry.getValue();
            final String padding = StringUtils.repeat(' ', 2 * indentation);

            // Save all data maps recursively to the final configuration file
            if (value instanceof DataMap)
            {
                writer.write(padding + key + ":\n");
                save(writer, (DataMap) value, indentation + 1);
            }

            // Values may be saved either as a list or a single entry
            else
            {
                if (value instanceof Iterable)
                {
                    writer.write(padding + key + ":\n");
                    for (Object v : (Iterable) value)
                        writer.write(padding + "- " + v + "\n");
                }
                else
                    writer.write(padding + key + ": " + value.toString() + "\n");
            }
        }
    }
    @Override
    public void save() throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        save(writer, data, 0);
        writer.close();
    }

    @Override
    public void load() throws IOException
    {
    }
}
