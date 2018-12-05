package org.sweetiebelle.malevolentmobs.test.config;

import org.sweetiebelle.malevolentmobs.config.IConfig;
import org.sweetiebelle.malevolentmobs.config.IProperty;
import org.sweetiebelle.malevolentmobs.config.IValue;

public class MockValue implements IValue
{
    public int number;
    public String text;

    public MockValue()
    {
        this.number = 0;
        this.text = "";
    }
    public MockValue(int number, String text)
    {
        this.number = number;
        this.text = text;
    }

    @Override
    public void save(IConfig config, IProperty property)
    {
        config.set(property.child("number"), number);
        config.set(property.child("text"), text);
    }
    @Override
    public void load(IConfig config, IProperty property)
    {
        number = config.getInt(property.child("number"));
        text = config.getString(property.child("text"));
    }
}
