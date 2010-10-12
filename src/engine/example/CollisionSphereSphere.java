package engine.example;

import engine.util.*;
import engine.opengl.*;
import engine.core.*;

import java.util.*;
import org.lwjgl.*;
import org.lwjgl.input.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;

/*
 *  
 */
public class CollisionSphereSphere extends BasicGameState
{
	private ArrayList<Ball> balls = new ArrayList<Ball>();
	private float angle = 0.0f;
	private Ball myBall = null;
	
	private Grid plane = null;

	public CollisionSphereSphere()
	{
	}
	
	/*
	 *  
	 */
	public void init()
	{
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL11.GL_NORMALIZE);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		
		Light light = new Light(new Vector3f(0.5f, 0.5f, 0.5f, 1.0f));
		light.setLight(GL11.GL_LIGHT0, new Vector3f(1.0f, 0.2f, 0.0f, 0.0f), new Vector3f(0.7f, 0.7f, 0.7f, 1.0f));
		
		Artist.enabled3D();
		
		initObjects();
	}
	
	/*
	 *  
	 */
	public void initObjects()
	{
		plane = new Grid(-6.0f);
		plane.compile();
								   
		myBall = new Ball(randomVector(-4, 8), new Vector3f(), 0.1f * (float) Math.random() + 0.1f, randomVector(0.2f, 0.6f));
		myBall.compile();
		
		balls.add(myBall);
	}
	
	public Vector3f randomVector(float start, float max)
	{
		return new Vector3f(max * (float) Math.random() + start,  max * (float) Math.random() + start, max * (float) Math.random() + start);
	}
	
	public void keyPressed()
	{
		float speed = 0.01f;
		int keycode = Keyboard.getEventKey();
		switch(keycode)
		{
			case Keyboard.KEY_SPACE:
				for(int i = 0; i < 20; i++)
				{
					Ball ball = new Ball(randomVector(-4, 8), new Vector3f(), 0.1f * (float) Math.random() + 0.1f, randomVector(0.2f, 0.6f));
					ball.compile();
					balls.add(ball);
				}
				System.out.println("Balls count: " + balls.size());
				break;
				
			case Keyboard.KEY_A:
				myBall.setVelocity(new Vector3f(-speed, 0.0f, 0.0f)); 
				break;
				
			case Keyboard.KEY_S:
				myBall.setVelocity(new Vector3f(0.0f, 0.0f, speed));
				break;
			
			case Keyboard.KEY_D:
				myBall.setVelocity(new Vector3f(speed, 0.0f, 0.0f)); 
				break;
			
			case Keyboard.KEY_W:
				myBall.setVelocity(new Vector3f(0.0f, 0.0f, -speed)); 
				break;
				
			case Keyboard.KEY_Z:
				myBall.setVelocity(new Vector3f(0.0f, -speed, 0.0f)); 
				break;
			
			case Keyboard.KEY_X:
				myBall.setVelocity(new Vector3f(0.0f, speed, 0.0f)); 
				break;
				
			case Keyboard.KEY_F:
				System.out.println("FPS: " + FPS.get());
				break;
		}
	}
	
	public void keyReleased()
	{
		int keycode = Keyboard.getEventKey();
		switch(keycode)
		{
			case Keyboard.KEY_A:
			case Keyboard.KEY_S:
			case Keyboard.KEY_D:
			case Keyboard.KEY_W:
			case Keyboard.KEY_Z:
			case Keyboard.KEY_X:
				myBall.setVelocity(new Vector3f()); 
				break;
		}
	}
	
	public void render()
	{
		logic();
		
		GL11.glPushMatrix();
		
		GL11.glTranslatef(0.0f, 0.0f, -40.0f);
		//GL11.glRotatef(-_angle, 0.0f, 1.0f, 0.0f);
		
			plane.render();
			
			for(int i = 0; i < balls.size(); i++)
				balls.get(i).render();
			
			myBall.render();
		
		GL11.glPopMatrix();
	}
	
	public void logic()
	{
		// Go through all the balls
		for(int i = 0; i < balls.size(); i++)
		{	
			Ball ball = balls.get(i);
		//for(Ball b1 : balls)
			
			CollisionResponse response = collideWithWorld(ball, ball.getPosition(), ball.getVelocity(), ball.getRadius(), 0.0f);
			if(response.hasCollided())
			{
				ArrayList<Object> collisions = new ArrayList<Object>();
				//System.out.println("#" + i + " collided with " + collisions.size() + " objects");
				for(int j = 0; j < collisions.size(); j++)
				{
					
					//Ball b2 = b2.getPosition(
				}
			}
			
			ball.setPosition(response.getPosition());
		}
	}
	
	public CollisionResponse collideWithWorld(Ball b1, Vector3f position, Vector3f velocity, float radius, float gravity)
	{
		CollisionResponse response = new CollisionResponse();
		Vector3f gravityPosition = new Vector3f(position.x, position.y - gravity, position.z);
		
		if(velocity.magnitude() < 0.005f)
		{
			response.setPosition(position);
    		return response;
    	}
    	
		response.setPosition(Vector3f.add(gravityPosition, velocity));
		
		for(int j = 0; j < balls.size(); j++)
		{
			Ball b2 = balls.get(j);
			if(b1.hashCode() != b2.hashCode())
				if(intersectSphereSphere(response.getPosition(), radius, b2.getPosition(), b2.getRadius()))
					response.addObject(b2);
		}
		
		if(response.hasCollided())
			response.setPosition(position);
			
		return response;
	}
	
	public boolean intersectSphereSphere(Vector3f b1Position, float b1Radius, Vector3f b2Position, float b2Radius)
	{
		// (b1.pos - b2.pos).magnitude() < b1.radius + b2.radius
		float distance = (float) (Vector3f.sub(b1Position, b2Position)).magnitude() - (b1Radius + b2Radius);
		if(distance < 0.0f)
			return true;
		else
			return false;
	}
}

class CollisionResponse
{
	private ArrayList<Object> collision = new ArrayList<Object>();
	private Vector3f position = new Vector3f();
	
	public CollisionResponse()
	{
	}
	
	public boolean hasCollided()
	{
		return (this.collision.size() != 0);
	}
	
	public ArrayList<Object> getCollisions()
	{
		return this.collision;
	}
	
	public void addObject(Object obj)
	{
		this.collision.add(obj);
	}
	
	public Vector3f getPosition()
	{
		return this.position;
	}
	
	public void setPosition(Vector3f position)
	{
		this.position = position;
	}
}
