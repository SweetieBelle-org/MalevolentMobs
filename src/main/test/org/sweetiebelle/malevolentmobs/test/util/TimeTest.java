package org.sweetiebelle.malevolentmobs.test.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.sweetiebelle.malevolentmobs.util.Time;

class TimeTest
{
    @Test
    void testConstructor()
    {
        assertEquals(1, Time.fromTicks(1).asTicks());
        assertEquals(1, Time.fromSeconds(1).asSeconds());
        assertEquals(1, Time.fromMinutes(1).asMinutes());
        assertEquals(1, Time.fromHours(1).asHours());
        assertEquals(1, Time.fromDays(1).asDays());
        assertEquals(1, Time.fromWeeks(1).asWeeks());
        assertEquals(1, Time.fromMonths(1).asMonths());
        assertEquals(1, Time.fromYears(1).asYears());
        assertEquals("8y 1M 2w 1d 8h 20m 10s 5t",
                Time.fromString("8y 1M 2w 1d 8h 20m 10s 5t").toString());
    }

    @Test
    void testIncrement()
    {
        assertEquals(1, Time.fromInstant().increment().asTicks());
        assertEquals(5, Time.fromInstant().increment(Time.fromTicks(5)).asTicks());
    }
    @Test
    void testDecrement()
    {
        assertEquals(4, Time.fromTicks(5).decrement().asTicks());
        assertEquals(0, Time.fromInstant().decrement().asTicks());
        assertEquals(3, Time.fromTicks(5).decrement(Time.fromTicks(2)).asTicks());
        assertEquals(0, Time.fromTicks(5).decrement(Time.fromTicks(20)).asTicks());
    }

    @Test
    void testToString()
    {
        assertEquals("2y", Time.fromYears(2).toString());
        assertEquals("1y 1M 5d", Time.fromDays(400).toString());
        assertEquals("2w 6d", Time.fromDays(20).toString());
        assertEquals("1d 12h", Time.fromHours(36).toString());
        assertEquals("1h 23m 20s", Time.fromSeconds(5000).toString());
        assertEquals("5s 10t", Time.fromTicks(110).toString());
        assertEquals("0t", Time.fromInstant().toString());
    }
}
