package engine.util;

import org.lwjgl.input.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;

public class Camera
{
	protected Vector3f vecPos = new Vector3f();
	protected Vector3f vecView = new Vector3f();
	protected Vector3f vecUp = new Vector3f();
	
	public Camera()
	{
		setViewport(0, 1.5f, 4.0f,	0, 1.5f, 0,   0, 1.0f, 0); // THIRD PERSON
		setViewport(0, 2.5f, 5, 0, 2.5f, 0, 0, 1, 0); // ONE PERSON
	}
	
	public void setViewport(float pos_x,  float pos_y,  float pos_z,
							float view_x, float view_y, float view_z,
							float up_x,   float up_y,   float up_z)
	{
		vecPos	= new Vector3f(pos_x,  pos_y,  pos_z ); // set position
		vecView	= new Vector3f(view_x, view_y, view_z); // set view
		vecUp	= new Vector3f(up_x,   up_y,   up_z  ); // set the up vector					
	}
	
	public void update()
	{
		GLU.gluLookAt(vecPos.getX(), vecPos.getY(), vecPos.getZ(),	
				 	  vecView.getX(), vecView.getY(), vecView.getZ(),	
					  vecUp.getX(), vecUp.getY(), vecUp.getZ());
	}
	
	public void thirdPersonTranslate()
	{
		GL11.glTranslatef(vecView.getX(), 0.0f, vecView.getZ());
	}
	
	private Vector3f getViewVector()
	{
		return Vector3f.sub(vecView, vecPos);
	}
	

	public void move(float speed)
	{
		Vector3f vec = getViewVector();	// Get the view vector
		
		// forward positive camera speed and backward negative camera speed.
		moveCamera(speed, vec);
	}
	
	public void strafe(float speed)
	{
		Vector3f vec = getViewVector();	// Get the view vector
		Vector3f vOrthoVector = new Vector3f(-vec.getZ(), 0.0f, vec.getX());   // Orthogonal vector for the view vector
		
		// left positive cameraspeed and right negative -cameraspeed.
		moveCamera(speed, vOrthoVector);
	}
	
	private void moveCamera(float speed, Vector3f vec)
	{
		vecPos.setX(vecPos.getX()  + vec.getX() * speed);
		vecPos.setY(vecPos.getY()  + vec.getY() * speed); /** enable Y movement */
		vecPos.setZ(vecPos.getZ()  + vec.getZ() * speed);
		vecView.setX(vecView.getX() + vec.getX() * speed);
		vecView.setY(vecView.getY() + vec.getY() * speed); /** enable Y movement */
		vecView.setZ(vecView.getZ() + vec.getZ() * speed);
	}
	
	
	public void rotateView(float speed)
	{
		rotate(vecView, speed, vecPos);
	}
	
	public void rotatePosition(float speed)
	{	
		rotate(vecPos, speed, vecView);
	}
	
	private void rotate(Vector3f set, float angle, Vector3f rotate)
	{
		Vector3f vec = getViewVector();
		
		set.setZ((float)(rotate.getZ() + Math.sin(angle) * vec.getX() + Math.cos(angle) * vec.getZ()));
		set.setX((float)(rotate.getX() + Math.cos(angle) * vec.getX() - Math.sin(angle) * vec.getZ()));
	}
	
	
	
	
	
	public void mouseMove1()
	{
		float angle = getMouseRotation();
		rotateView(angle);
	}
	
	public void mouseMove2()
	{
		float angle = getMouseRotation();
		rotatePosition(angle);
	}
	
	private float getMouseRotation()
	{
		int mousePosX = Mouse.getX();
		int mousePosY = Display.getDisplayMode().getHeight() - Mouse.getY();
		
		int mid_x = Display.getDisplayMode().getWidth()  >> 1;	
		int mid_y = Display.getDisplayMode().getHeight() >> 1;	
		float angle_y  = 0.0f;				
		float angle_z  = 0.0f;										
		
		if( (mousePosX == mid_x) && (mousePosY == mid_y) )
			return 0.0f;

		Mouse.setCursorPosition(mid_x, mid_y);	// Set the mouse cursor in the center of the window						

		// Get the direction from the mouse cursor, set a resonable maneuvering speed
		angle_y = (float)( (mid_x - mousePosX) ) / 1000;		
		angle_z = (float)( (mid_y - mousePosY) ) / 1000;

		// The higher the value is the faster the camera looks around.
		vecView.setY(vecView.getY() + angle_z * 2.0f);

		// limit the rotation around the x-axis
		if((vecView.getY() - vecPos.getY()) > 8)  vecView.setY(vecPos.getY() + 8.0f);
		if((vecView.getY() - vecPos.getY()) <-8)  vecView.setY(vecPos.getY() - 8.0f);
		
		return -angle_y; // Rotate
	}
}
