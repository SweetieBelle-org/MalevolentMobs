package com.hepolite.mmob.dungeons;

public class DungeonSpawnCondition
{
	// Player specific conditions
	public float minPlayerLevel = -1.0f;
	public float maxPlayerLevel = -1.0f;

	public float minPlayerAverageLevel = -1.0f;
	public float maxPlayerAverageLevel = -1.0f;

	// Location specific conditions
	public float minPlayerDistance = -1.0f;
	public float maxPlayerDistance = -1.0f;

	public float minPlayerAverageDistance = -1.0f;
	public float maxPlayerAverageDistance = -1.0f;

	/** Returns true if the given parameters match the level requirements */
	public boolean isLevelRequirementMet(float minLevel, float maxLevel, float averageLevel)
	{
		if (minLevel < minPlayerLevel && minPlayerLevel != -1.0f)
			return false;
		if (maxLevel > maxPlayerLevel && maxPlayerLevel != -1.0f)
			return false;
		if (averageLevel < minPlayerAverageLevel && minPlayerAverageLevel != -1.0f)
			return false;
		if (averageLevel > maxPlayerAverageLevel && maxPlayerAverageLevel != -1.0f)
			return false;
		return true;
	}

	/** Returns true if the given parameters match the distance requirements */
	public boolean isDistanceRequirementMet(float minDistance, float maxDistance, float averageDistance)
	{
		if (minDistance < minPlayerDistance && minPlayerDistance != -1.0f)
			return false;
		if (maxDistance > maxPlayerDistance && maxPlayerDistance != -1.0f)
			return false;
		if (averageDistance < minPlayerAverageDistance && minPlayerAverageDistance != -1.0f)
			return false;
		if (averageDistance > maxPlayerAverageDistance && maxPlayerAverageDistance != -1.0f)
			return false;
		return true;
	}
}
