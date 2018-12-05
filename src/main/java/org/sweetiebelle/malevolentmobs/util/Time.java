package org.sweetiebelle.malevolentmobs.util;

public final class Time
{
    public static final long MONTHS_PER_YEAR = 12;
    public static final long WEEKS_PER_YEAR = 52;
    public static final long DAYS_PER_YEAR = 365;
    public static final long DAYS_PER_MONTH = 30;
    public static final long DAYS_PER_WEEK = 7;
    public static final long HOURS_PER_DAY = 24;
    public static final long MINUTES_PER_HOUR = 60;
    public static final long SECONDS_PER_MINUTE = 60;
    public static final long TICKS_PER_SECOND = 20;
    public static final long TICKS_PER_MINUTE = TICKS_PER_SECOND * SECONDS_PER_MINUTE;
    public static final long TICKS_PER_HOUR = TICKS_PER_MINUTE * MINUTES_PER_HOUR;
    public static final long TICKS_PER_DAY = TICKS_PER_HOUR * HOURS_PER_DAY;
    public static final long TICKS_PER_WEEK = TICKS_PER_DAY * DAYS_PER_WEEK;
    public static final long TICKS_PER_MONTH = TICKS_PER_DAY * DAYS_PER_MONTH;
    public static final long TICKS_PER_YEAR = TICKS_PER_DAY * DAYS_PER_YEAR;

    /**
     * Constructs a new time representing an instant
     *
     * @return The new time instance
     */
    public static Time fromInstant()
    {
        return new Time();
    }
    /**
     * Constructs a new time representing the given amount of ticks
     *
     * @param ticks The time measured in ticks
     * @return The new time instance
     */
    public static Time fromTicks(final long ticks)
    {
        return new Time(ticks);
    }
    /**
     * Constructs a new time representing the given amount of seconds
     *
     * @param seconds The time measured in seconds
     * @return The new time instance
     */
    public static Time fromSeconds(final long seconds)
    {
        return fromTicks(TICKS_PER_SECOND * seconds);
    }
    /**
     * Constructs a new time representing the given amount of minutes
     *
     * @param minutes The time measured in minutes
     * @return The new time instance
     */
    public static Time fromMinutes(final long minutes)
    {
        return fromSeconds(SECONDS_PER_MINUTE * minutes);
    }
    /**
     * Constructs a new time representing the given amount of hours
     *
     * @param hours The time measured in hours
     * @return The new time instance
     */
    public static Time fromHours(final long hours)
    {
        return fromMinutes(MINUTES_PER_HOUR * hours);
    }
    /**
     * Constructs a new time representing the given amount of days
     *
     * @param days The time measured in days
     * @return The new time instance
     */
    public static Time fromDays(final long days)
    {
        return fromHours(HOURS_PER_DAY * days);
    }
    /**
     * Constructs a new time representing the given amount of weeks
     *
     * @param weeks The time measured in weeks
     * @return The new time instance
     */
    public static Time fromWeeks(final long weeks)
    {
        return fromDays(DAYS_PER_WEEK * weeks);
    }
    /**
     * Constructs a new time representing the given amount of months, assuming
     * {@code DAYS_PER_MONTH} days per month
     *
     * @param months The time measured in months
     * @return The new time instance
     */
    public static Time fromMonths(final long months)
    {
        return fromDays(DAYS_PER_MONTH * months);
    }
    /**
     * Constructs a new time representing the given amount of years, assuming {@code DAYS_PER_YEAR}
     * days per year
     *
     * @param years The time measured in years
     * @return The new time instance
     */
    public static Time fromYears(final long years)
    {
        return fromDays(DAYS_PER_YEAR * years);
    }
    /**
     * Constructs a new time from the provided string, taking in years, months, weeks, days, hours,
     * minutes, seconds, and ticks. None of the quantities has to be specified, any one may be
     * omitted. If no values are specified, an instant is returned.<br>
     * <br>
     * Example:<br>
     * string: "1y 4M 2w 6d 20h 55m 20s 2t"
     *
     * @param string The string to convert to a time
     * @return The time represented by the string
     */
    public static Time fromString(final String string)
    {
        final Time time = Time.fromInstant();
        if (string == null || string.isEmpty())
            return time;
        for (final String part : string.split(" "))
            try
            {
                final long number = StringConverter.toLong(part.substring(0, part.length() - 1));

                switch (part.substring(part.length() - 1))
                {
                case "y":
                    time.increment(Time.fromYears(number));
                    break;
                case "M":
                    time.increment(Time.fromMonths(number));
                    break;
                case "w":
                    time.increment(Time.fromWeeks(number));
                    break;
                case "d":
                    time.increment(Time.fromDays(number));
                    break;
                case "h":
                    time.increment(Time.fromHours(number));
                    break;
                case "m":
                    time.increment(Time.fromMinutes(number));
                    break;
                case "s":
                    time.increment(Time.fromSeconds(number));
                    break;
                default:
                    time.increment(Time.fromTicks(number));
                    break;
                }
            }
            catch (final Exception e)
            {
            }
        return time;
    }

    // ...

    private Long time;

    /**
     * Constructs a time of zero duration
     */
    public Time()
    {
        this(0L);
    }
    /**
     * Creates a new time lasting for the given duration
     *
     * @param time The time, measured in ticks
     */
    public Time(final long time)
    {
        this.time = time;
    }

    // ...

    /**
     * Increases the time by a single tick and returns the same instance
     *
     * @return Returns the same instance as this method is invoked on
     */
    public Time increment()
    {
        this.time++;
        return this;
    }
    /**
     * Increases the time by the specified amount and returns the same instance
     *
     * @return Returns the same instance as this method is invoked on
     */
    public Time increment(final Time time)
    {
        this.time += time.time;
        return this;
    }

    /**
     * Decreases the time by a single tick to a minimum of 0 and returns the same instance
     *
     * @return Returns the same instance as this method is invoked on
     */
    public Time decrement()
    {
        this.time = Math.max(0L, this.time - 1L);
        return this;
    }
    /**
     * Decreases the time by the specified amount to a minimum of 0 and returns the same instance
     *
     * @return Returns the same instance as this method is invoked on
     */
    public Time decrement(final Time time)
    {
        this.time = Math.max(0L, this.time - time.time);
        return this;
    }

    // ...

    /**
     * @return The time represented measured in ticks
     */
    public long asTicks()
    {
        return time;
    }
    /**
     * @return The time represented measured in seconds
     */
    public long asSeconds()
    {
        return asTicks() / TICKS_PER_SECOND;
    }
    /**
     * @return The time represented measured in minutes
     */
    public long asMinutes()
    {
        return asSeconds() / SECONDS_PER_MINUTE;
    }
    /**
     * @return The time represented measured in hours
     */
    public long asHours()
    {
        return asMinutes() / MINUTES_PER_HOUR;
    }
    /**
     * @return The time represented measured in days
     */
    public long asDays()
    {
        return asHours() / HOURS_PER_DAY;
    }
    /**
     * @return The time represented measured in weeks
     */
    public long asWeeks()
    {
        return asDays() / DAYS_PER_WEEK;
    }
    /**
     * @return The time represented measured in months, assuming {@code DAYS_PER_MONTH} days per
     *         month
     */
    public long asMonths()
    {
        return asDays() / DAYS_PER_MONTH;
    }
    /**
     * @return The time represented measured in months, assuming {@code DAYS_PER_YEAR} days per year
     */
    public long asYears()
    {
        return asDays() / DAYS_PER_YEAR;
    }

    // ...

    @Override
    public String toString()
    {
        long ticks = asTicks();
        final long years = ticks / TICKS_PER_YEAR;
        ticks -= years * TICKS_PER_YEAR;
        final long months = ticks / TICKS_PER_MONTH;
        ticks -= months * TICKS_PER_MONTH;
        final long weeks = ticks / TICKS_PER_WEEK;
        ticks -= weeks * TICKS_PER_WEEK;
        final long days = ticks / TICKS_PER_DAY;
        ticks -= days * TICKS_PER_DAY;
        final long hours = ticks / TICKS_PER_HOUR;
        ticks -= hours * TICKS_PER_HOUR;
        final long minutes = ticks / TICKS_PER_MINUTE;
        ticks -= minutes * TICKS_PER_MINUTE;
        final long seconds = ticks / TICKS_PER_SECOND;
        ticks -= seconds * TICKS_PER_SECOND;

        final StringBuilder builder = new StringBuilder();
        if (years != 0L)
            builder.append(years + "y ");
        if (months != 0L)
            builder.append(months + "M ");
        if (weeks != 0L)
            builder.append(weeks + "w ");
        if (days != 0L)
            builder.append(days + "d ");
        if (hours != 0L)
            builder.append(hours + "h ");
        if (minutes != 0L)
            builder.append(minutes + "m ");
        if (seconds != 0L)
            builder.append(seconds + "s ");
        if (ticks != 0L)
            builder.append(ticks + "t ");
        if (builder.length() == 0)
            builder.append("0t ");
        return builder.substring(0, builder.length() - 1);
    }

    @Override
    public boolean equals(final Object other)
    {
        if (other instanceof Time)
            return time == ((Time) other).time;
        return false;
    }
}
