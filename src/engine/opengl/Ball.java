package engine.opengl;

import engine.util.*;
import org.lwjgl.opengl.*;

public class Ball extends Sphere
{
	private Vector3f velocity = null;
	private Vector3f nextMove = null;
	
	public Ball(Vector3f position, Vector3f velocity, float radius, Vector3f color)
	{
		super(position, radius, color);
		this.velocity = velocity;
	}
	
	public Vector3f getVelocity()
	{
		return this.velocity;
	}
	
	public void setVelocity(Vector3f velocity)
	{
		this.velocity = velocity;
	}
	
	public boolean hasVelocity()
	{
		if(velocity.isZero())
			return false;
			
		return true;
	}
	
	public Vector3f getNextMove()
	{
		return Vector3f.add(this.position, this.velocity);
	}
	
	public Vector3f getNextMove(float dt)
	{
		return Vector3f.add(this.position, Vector3f.multiply(this.velocity, dt));
	}
	
	public void move(float dt)
	{
		this.position.add(Vector3f.multiply(this.velocity, dt));
	}
}
