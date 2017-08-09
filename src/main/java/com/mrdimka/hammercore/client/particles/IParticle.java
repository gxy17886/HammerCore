package com.mrdimka.hammercore.client.particles;

public interface IParticle
{
	public void setVel(double x, double y, double z);
	
	public void spawnAt(double x, double y, double z);
	
	/**
	 * Should be equal to call "spawnAt(posX, posY, posZ)", but "posX", "posY",
	 * "posZ" are protected.
	 */
	public void spawn();
	
	public void setData(ParticleParam data);
}