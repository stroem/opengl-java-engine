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
public class CollisionPointTriangle extends BasicGameState
{
	private ArrayList<Ball> balls = new ArrayList<Ball>();
	private float angle = 0.0f;
	private Ball myBall = null;
	private Triangle triangle = null;
	
	private Grid plane = null;
	private Camera camera = new Camera();
	private int lastPressed = 0;

	public CollisionPointTriangle()
	{
	}
	
	public void init()
	{
		Mouse.setGrabbed(true);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL11.GL_NORMALIZE);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		
		//GL11.glEnable(GL11.GL_CULL_FACE);
		//GL11.glCullFace(GL11.GL_BACK);
		
		Light light = new Light(new Vector3f(0.5f, 0.5f, 0.5f, 1.0f));
		light.setLight(GL11.GL_LIGHT0, new Vector3f(1.0f, 0.2f, 0.0f, 0.0f), new Vector3f(0.7f, 0.7f, 0.7f, 1.0f));
		
		Artist.enabled3D();
		
		initObjects();
	}
	
	public void initObjects()
	{
		plane = new Grid(-6.0f);
		plane.compile();
							//randomVector(-4, 8)	   
		myBall = new Ball(new Vector3f(0.0f, 0.0f, 1.0f), new Vector3f(), 0.1f * (float) Math.random() + 0.1f, randomVector(0.2f, 0.6f));
		myBall.compile();
		
		triangle = new Triangle();
		triangle.compile();
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
		
		lastPressed = keycode;
	}
	
	public void keyReleased()
	{
		lastPressed = 0;
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
	
	private void checkCameraMovements()
	{
		float speed = 0.01f;
		if(lastPressed != 0)
		switch(lastPressed)
		{
			case Keyboard.KEY_LEFT:
				camera.strafe(-speed);
				break;
				
			case Keyboard.KEY_RIGHT:
				camera.strafe(speed);
				break;
				
			case Keyboard.KEY_UP:
				camera.move(speed);
				break;
				
			case Keyboard.KEY_DOWN:
				camera.move(-speed);
				break;
		}
	}
	
	public void render()
	{
		checkCameraMovements();	
		camera.mouseMove1();
		camera.update();
		
		GL11.glPushMatrix();
		
		GL11.glTranslatef(0.0f, 0.0f, -10.0f);
		//GL11.glRotatef(-_angle, 0.0f, 1.0f, 0.0f);
			
			plane.render();
			logic();
			
			GL11.glColor3f(1.0f, 0.0f, 0.0f);
			triangle.render();
			
			myBall.render();
		
		GL11.glPopMatrix();
	}
	
	public void drawBall(Vector3f vec)
	{
		Ball ball = new Ball(vec, new Vector3f(), 0.1f * (float) Math.random() + 0.1f, randomVector(0.2f, 0.6f));
		ball.render();
	}
	
	public void logic()
	{
		Vector3f nextPosition = myBall.getNextMove();
		
		//System.out.println(triangle.isFrontFacingTo(nextPosition) + " distance: " + (Math.abs(triangle.signedDistanceTo(nextPosition)) - myBall.getRadius()));
		
		boolean collision = false;
		if(Math.abs(triangle.signedDistanceTo(nextPosition)) - myBall.getRadius() <= 0.0f)
		//if(!intersectPointTriangle(nextPosition, triangle))
		{
			Vector3f closestPoint = closestPointOnTriangle(nextPosition, triangle);
			drawBall(closestPoint);
			
			Vector3f normalisedVelocity = myBall.getVelocity().clone();
			if(myBall.getVelocity().isZero())
				return;
				
			normalisedVelocity.normalise();
			normalisedVelocity.multiply(-1.0f);
			
			double d = intersectRaySphere(closestPoint, normalisedVelocity, nextPosition, myBall.getRadius());
			//System.out.println(d);
			
			if (d > 0.0)
			{
				Vector3f ellipsoidIntersect = Vector3f.add(closestPoint, Vector3f.multiply(normalisedVelocity, (float) d));
				drawBall(ellipsoidIntersect);
				collision = true;
			}
			/*
			double distance = Vector3f.sub(closestPoint, nextPosition).magnitude();
			if(distance  - myBall.getRadius() <= 0.0f)
				System.out.println("WEE");*/
			//else
			//if(intersectPointTriangle(closestPoint, triangle))
			//	collision = true;
		}
		
		if(!collision)
			myBall.setPosition(nextPosition);
		
		/*if(intersectPointTriangle(nextPosition, triangle))
		{
			
				System.out.println(System.currentTimeMillis());
		}*/
		
	}
	
	public double intersectRayPlane(Vector3f rOrigin, Vector3f rVector, Vector3f pOrigin, Vector3f pNormal)
	{
		double d = - (pNormal.dot(pOrigin));

		double numer = pNormal.dot(pOrigin) + d;
		double denom = pNormal.dot(rVector);

		if (denom == 0)  // normal is orthogonal to vector, cant intersect
			return (-1.0f);

		return -(numer / denom);	
	}
	
	public double intersectRaySphere(Vector3f rO, Vector3f rV, Vector3f sO, double sR)
	{
		
	   Vector3f Q = Vector3f.sub(sO, rO);
	   double c = lengthOfVector(Q);
	   double v = Q.dot(rV);
	   double d = sR * sR - (c * c - v * v);

	   // If there was no intersection, return -1
	   if (d < 0.0)
	   	return (-1.0f);

	   // Return the distance to the [first] intersecting point
	   return (v - Math.sqrt(d));
	}
	
	public Vector3f closestPointOnLine(Vector3f p, Vector3f a, Vector3f b)
	{

		// a-b is the line, p the point in question
		Vector3f c = Vector3f.sub(p, a);
		Vector3f V = Vector3f.sub(b, a); 
		double d = lengthOfVector(V);

		V.normalise();  
		double t = V.dot(c);

		// Check to see if ‘t’ is beyond the extents of the line segment
		if (t < 0.0f) return (a);
		if (t > d) return (b);

		// Return the point between ‘a’ and ‘b’
		//set length of V to t. V is normalized so this is easy
		V.multiply((float) t);
		   
		return Vector3f.add(V, a);	
	}
	
	public Vector3f closestPointOnTriangle(Vector3f p, Triangle triangle)
	{
		Vector3f[] v = triangle.getVertices();

		Vector3f Rab = closestPointOnLine(p, v[0], v[1]);
		Vector3f Rbc = closestPointOnLine(p, v[1], v[2]);
		Vector3f Rca = closestPointOnLine(p, v[2], v[0]);

		double dAB = lengthOfVector(Vector3f.sub(p, Rab));
		double dBC = lengthOfVector(Vector3f.sub(p, Rbc));
		double dCA = lengthOfVector(Vector3f.sub(p, Rca));

		double min = dAB;
		Vector3f result = Rab;

		if(dBC < min)
		{
			min = dBC;
			result = Rbc;
		}

		if(dCA < min)
			result = Rca;

		return result;	
	}
	
	public double lengthOfVector(Vector3f vec)
	{
		return Math.sqrt(vec.getX() * vec.getX() + vec.getY() * vec.getY() + vec.getZ() * vec.getZ());
	}
	
	public boolean intersectPointTriangle(Vector3f point, Triangle triangle)
	{
		Vector3f[] vertices = triangle.getVertices();
		double angle = 0.0;

		Vector3f v1 = Vector3f.sub(point, vertices[0]);
		Vector3f v2 = Vector3f.sub(point, vertices[1]);
		Vector3f v3 = Vector3f.sub(point, vertices[2]);
		
		v1.normalise();
		v2.normalise();
		v3.normalise();

		angle += Math.acos(Vector3f.dot(v1, v2));   
		angle += Math.acos(Vector3f.dot(v2, v3));
		angle += Math.acos(Vector3f.dot(v3, v1)); 

		if (Math.abs(angle - (2 * Math.PI)) <= 0.005)
			return true;

		return false;
	}	
}

class Triangle extends DrawableObject
{
	protected Vector3f[] vertices = new Vector3f[3];
	protected Vector3f normal = new Vector3f();
	protected Vector3f origin = new Vector3f();
	protected double equation = 0.0;
	
	public Triangle()
	{
		vertices[0] = new Vector3f(0.0f, 1.0f, 0.0f);
		vertices[1] = new Vector3f(-1.0f, -1.0f, 0.0f);
		vertices[2] = new Vector3f(1.0f, -1.0f, 0.0f);
		
		normal = Vector3f.cross(Vector3f.sub(vertices[1], vertices[0]), 
								 Vector3f.sub(vertices[2], vertices[0]));
		normal.normalise();
		origin = vertices[0];
		equation = -(normal.getX() * origin.getX() + normal.getY() * origin.getY() + normal.getX() * origin.getZ());

	}
	
	public void renderRealtime()
	{
		GL11.glBegin(GL11.GL_POLYGON);
			
			GL11.glNormal3f(normal.getX(), normal.getY(), normal.getZ());
			for(int i = 0; i < vertices.length; i++)
				GL11.glVertex3f(vertices[i].getX(), vertices[i].getY(), vertices[i].getZ());
				
		GL11.glEnd();
	}
	
	public boolean isFrontFacingTo(Vector3f direction)
	{
		return (normal.dot(direction) <= 0.0);
	}
	
	public double signedDistanceTo(Vector3f point)
	{
		return (point.dot(normal) + equation);
	}
	
	public Vector3f[] getVertices()
	{
		return vertices;
	}
	
	public Vector3f getNormal()
	{
		return normal;
	}
	
	public Vector3f getOrigin()
	{
		return origin;
	}
}
