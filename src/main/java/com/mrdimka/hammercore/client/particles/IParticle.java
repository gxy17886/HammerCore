package com.mrdimka.hammercore.client.particles;

public interface IParticle
{
	void setVel(double x, double y, double z);
	void spawnAt(double x, double y, double z);
	
	/** Should be equal to call "spawnAt(posX, posY, posZ)", but "posX", "posY", "posZ" are protected. */
	void spawn();
	
	void setData(ParticleParam data);
}