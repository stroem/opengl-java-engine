package engine.example;

import engine.util.*;
import engine.opengl.*;
import engine.core.*;

import java.util.*;
import org.lwjgl.*;
import org.lwjgl.input.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;

public class CollisionTest extends BasicGameState
{
	public static final int WALL_LEFT = 0;
	public static final int WALL_RIGHT = 1;
	public static final int WALL_FAR = 2;
	public static final int WALL_NEAR = 3;
	public static final int WALL_TOP = 4;
	public static final int WALL_BOTTOM = 5;
	
	final float GRAVITY = 8.0f;
	final float BOX_SIZE = 12.0f;
	final float TIME_BETWEEN_UPDATES = 0.01f;
	final int TIMER_MS = 25;
	
	private Timer timerTasken;
	ArrayList<Ball> _balls = new ArrayList<Ball>(); //All of the balls in play
	float _angle = 0.0f;
	float _timeUntilUpdate = 0;
	
	private Grid plane = null;

	public CollisionTest()
	{
	}
	
	class MyTimerTask extends TimerTask {
	  public void run()
	  {
		advance(_balls, (float)TIMER_MS / 1000.0f, _timeUntilUpdate);
		_angle += (float)TIMER_MS / 100.0f;
		if (_angle > 360)
			_angle -= 360;
		
		timerTasken.schedule(new MyTimerTask(), TIMER_MS);
	}
	}
	
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
	
	public void initObjects()
	{
		plane = new Grid(-6.0f);
		plane.compile();
		//timerTasken = new Timer();
		//timerTasken.schedule(new MyTimerTask(), 0);
	}
	
	public void keyPressed()
	{
		int keycode = Keyboard.getEventKey();
		switch(keycode)
		{
			case Keyboard.KEY_SPACE:
				for(int i = 0; i < 20; i++)
				{
					Vector3f pos = new Vector3f(8 * (float) Math.random() - 4,
											 8 * (float) Math.random() - 4,
											 8 * (float) Math.random() - 4);
					Vector3f v = new Vector3f(5 * (float) Math.random() - 4,
										   5 * (float) Math.random() - 4,
										   5 * (float) Math.random() - 4);
					float r = 0.1f * randomFloat() + 0.1f;
					Vector3f color = new Vector3f(0.6f * (float) Math.random() + 0.2f,
											   0.6f * (float) Math.random() + 0.2f,
											   0.6f * (float) Math.random() + 0.2f);
											   
					Ball ball = new Ball(pos, v, r, color);
					ball.compile();
					_balls.add(ball);
				}
				System.out.println("Balls count: " + _balls.size());
				break;
				
			case Keyboard.KEY_A:
				System.out.println("Ball->Ball collision: " + count);
				break;
				
			case Keyboard.KEY_S:
				System.out.println("Ball->Wall collision: " + count2);
				break;
			
			case Keyboard.KEY_D:
				System.out.println("FPS: " + FPS.get());
				break;
		}
	}
	
	
	
	public void render()
	{
		advance(_balls, (float) TIMER_MS / 1000.0f, _timeUntilUpdate);
		
		_angle += (float)TIMER_MS / 100.0f;
		if (_angle > 360)
			_angle -= 360;
				
		GL11.glPushMatrix();
		
		GL11.glTranslatef(0.0f, 0.0f, -20.0f);
		GL11.glRotatef(-_angle, 0.0f, 1.0f, 0.0f);
		
		plane.render();
		
		for(int i = 0; i < _balls.size(); i++)
			_balls.get(i).render();
		
		GL11.glPopMatrix();
	}
	
	// COPY PASTE CODE STARTS HERE
	float randomFloat()
	{
		return (float) Math.random();
	}
	
	//public ArrayList<Object> collisionList = new ArrayList<Object>();
	
	void potentialBallBallCollisions(ArrayList<BallPair> potentialCollisions, ArrayList<Ball> balls)
	{
		//Slow method
		for(int i = 0; i < balls.size(); i++)
		{
			for(int j = i + 1; j < balls.size(); j++)
			{
				BallPair bp = new BallPair();
				bp.ball1 = (Ball) balls.get(i);
				bp.ball2 = (Ball) balls.get(j);
				
				if(bp.ball2.hasVelocity())
					potentialCollisions.add(bp);
			}
		}
	}
	
	void potentialBallWallCollisions(ArrayList<BallWallPair> potentialCollisions, ArrayList<Ball> balls)
	{	
		//Slow method
		int walls[] = new int[] {WALL_LEFT, WALL_RIGHT, WALL_FAR, WALL_NEAR, WALL_TOP, WALL_BOTTOM};
		for(int i = 0; i < balls.size(); i++)
		{
			for(int j = 0; j < 6; j++)
			{
				BallWallPair bwp = new BallWallPair();
				bwp.ball = (Ball) balls.get(i);
				bwp.wall = j;
				
				if(bwp.ball.hasVelocity())
					potentialCollisions.add(bwp);
			}
		}
	}
	
	void moveBalls(ArrayList<Ball> balls, float dt)
	{
		for(int i = 0; i < balls.size(); i++)
		{
			Ball ball = balls.get(i);
			if(ball.hasVelocity())
				ball.move(dt);
		}	
			/*
			Vector3f oldPos = ball.getPosition();
			ball.getPosition().x += ball.getVelocity().x * dt;
			ball.getPosition().y += ball.getVelocity().y * dt;
			ball.getPosition().z += ball.getVelocity().z * dt;
		}*/
	}
	
	void applyGravity(ArrayList<Ball> balls)
	{
		for(int i = 0; i < balls.size(); i++)
		{
			Ball ball = balls.get(i);
			if(ball.hasVelocity())
				ball.getVelocity().sub(new Vector3f(0, GRAVITY * TIME_BETWEEN_UPDATES, 0));
		}
	}
	
	public boolean intersectSphereSphere(Ball b1, Ball b2)
	{
		//Check whether the balls are close enough
		if ((Vector3f.sub(b1.getPosition(), b2.getPosition())).magnitude() < b1.getRadius() + b2.getRadius())
		{
			//Check whether the balls are moving toward each other
			Vector3f netVelocity = Vector3f.sub(b1.getVelocity(), b2.getVelocity());
			Vector3f displacement = Vector3f.sub(b1.getPosition(), b2.getPosition());
			return netVelocity.dot(displacement) < 0;
		}
		else
			return false;
	}
	
	public int count = 0;
	void handleSphereSphereCollisions(ArrayList<Ball> balls)
	{
		ArrayList<BallPair> bps = new ArrayList<BallPair>();
		potentialBallBallCollisions(bps, balls);
		for(int i = 0; i < bps.size(); i++)
		{
			BallPair bp = (BallPair) bps.get(i);
			
			Ball b1 = bp.ball1;
			Ball b2 = bp.ball2;
			if (intersectSphereSphere(b1, b2))
			{
				//Make the balls reflect off of each other
				Vector3f displacement = Vector3f.sub(b1.getPosition(), b2.getPosition());
				displacement.normalise();
				
				Vector3f displacement2 = displacement.clone();
				
				displacement.multiply(b1.getVelocity().dot(displacement));
				displacement.multiply(2.0f);
				b1.getVelocity().sub(displacement);
				
				displacement2.multiply(b2.getVelocity().dot(displacement2));
				displacement2.multiply(2.0f);
				b2.getVelocity().sub(displacement);
				
				count++;
				//b1.setVelocity(new Vector3f());
				//b2.setVelocity(new Vector3f());
			}
		}
	}
	
	Vector3f wallDirection(int wall)
	{
		switch (wall)
		{
			case WALL_LEFT:
				return new Vector3f(-1, 0, 0);
				
			case WALL_RIGHT:
				return new Vector3f(1, 0, 0);
				
			case WALL_FAR:
				return new Vector3f(0, 0, -1);
				
			case WALL_NEAR:
				return new Vector3f(0, 0, 1);
				
			case WALL_TOP:
				return new Vector3f(0, 1, 0);
				
			case WALL_BOTTOM:
				return new Vector3f(0, -1, 0);
				
			default:
				return new Vector3f(0, 0, 0);
		}
	}
	
	public boolean intersectSpherePlane(Ball ball, int wall)
	{
		Vector3f dir = wallDirection(wall);
		//Check whether the ball is far enough in the "dir" direction, and whether
		//it is moving toward the wall
		return ball.getPosition().dot(dir) + ball.getRadius() > BOX_SIZE / 2 &&
				ball.getVelocity().dot(dir) > 0;
	}
	
	public int count2 = 0;
	void handleSpherePlaneCollisions(ArrayList<Ball> balls)
	{
		ArrayList<BallWallPair> bwps = new ArrayList<BallWallPair>();
		potentialBallWallCollisions(bwps, balls);
		for(int i = 0; i < bwps.size(); i++)
		{
			BallWallPair bwp = bwps.get(i);
			
			Ball b = bwp.ball;
			int w = bwp.wall;
			if (intersectSpherePlane(b, w))
			{
				//if(w == WALL_BOTTOM || w == WALL_TOP)
				//{
				//Make the ball reflect off of the wall
				Vector3f dir = wallDirection(w);
				dir.normalise();
				
				dir.multiply(b.getVelocity().dot(dir));
				dir.multiply(2.0f);
				
				b.getVelocity().sub(dir);
			
				//b.setVelocity(new Vector3f());
				count2++;
				//}
			}
		}
	}
	
	void performUpdate(ArrayList<Ball> balls)
	{
		applyGravity(balls);
		handleSphereSphereCollisions(balls);
		handleSpherePlaneCollisions(balls);
	}
	
	void advance(ArrayList<Ball> balls, float t, float timeUntilUpdate)
	{
		while (t > 0)
		{
			if (timeUntilUpdate <= t)
			{
				moveBalls(balls, timeUntilUpdate);
				performUpdate(balls);	
				t -= timeUntilUpdate;
				timeUntilUpdate = TIME_BETWEEN_UPDATES;
			}
			else
			{
				moveBalls(balls, t);
				timeUntilUpdate -= t;
				t = 0;
			}
		}
	}
}

class Quad
{
	public Quad()
	{
		
	}
}

//Stores a pair of balls
class BallPair
{
	public Ball ball1;
	public Ball ball2;
}

//Stores a ball and a wall
class BallWallPair
{
	public Ball ball;
	public int wall;
}
