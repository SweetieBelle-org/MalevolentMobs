package com.hepolite.mmob.projectiles;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public abstract class Projectile {
    protected static Random random = new Random();
    private boolean isAlive = true;

    protected Location position = new Location(null, 0.0, 0.0, 0.0);
    protected Vector velocity = new Vector();

    /** Called on each tick */
    public abstract void onTick();

    /** Called to kill the particle */
    public void kill() {
        isAlive = false;
    }

    /** Returns true for as long as the particle is alive */
    public boolean isAlive() {
        return isAlive;
    }

    // //////////////////////////////////////////////////////////////////////////////////

    /** Sets the velocity of the projectile */
    public void setVelocity(final Vector velocity) {
        this.velocity = velocity;
    }

    /** Moves the particle towards the given location. The speedTurnFactor should be between 0 and 1, 0 to instantly move towards the target, anything else makes the curve more smooth */
    public void moveTowardsLocation(final Location target, final float speed, final float speedTurnFactor) {
        final float distanceToTarget = (float) position.distanceSquared(target);
        if (distanceToTarget <= speed * speed)
            position = target.clone();
        else {
            final Vector delta = target.clone().subtract(position).toVector().normalize();
            velocity.setX((1.0f - speedTurnFactor) * velocity.getX() + speedTurnFactor * speed * delta.getX());
            velocity.setY((1.0f - speedTurnFactor) * velocity.getY() + speedTurnFactor * speed * delta.getY());
            velocity.setZ((1.0f - speedTurnFactor) * velocity.getZ() + speedTurnFactor * speed * delta.getZ());
            position.add(velocity);
        }
    }

    /** Checks if there is some solid blocks in the path between the two locations, or an entity blocks the path. Returns the location of the obstruction, if any was found; returns null otherwise */
    // @Deprecated
    // public Location getObstruction(Location start, Location end, LivingEntity entityToIgnore)
    // {
    // Check for entity collisions
    // LivingEntity entity = MathHelper.getEntityInLine(start.getWorld(), new Vector3D(start), new Vector3D(end), entityToIgnore);
    // if (entity != null)
    // return entity.getLocation();

    // Check for block collisions
    // Location origin = start.clone();
    // origin.setDirection(end.clone().subtract(start).toVector());

    // BlockIterator iterator = new BlockIterator(origin, 0.0, (int) Math.ceil(start.distance(end)));
    // while (iterator.hasNext())
    // {
    // Block block = iterator.next();
    // if (block.getType().isSolid())
    // return block.getLocation();
    // }
    // return null;
    // }
}
