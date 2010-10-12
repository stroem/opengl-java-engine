package engine.example;

import engine.util.*;
import engine.opengl.*;
import engine.core.*;

import java.util.*;
import java.nio.*;
import org.lwjgl.*;
import org.lwjgl.input.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;

public class PickingTest extends BasicGameState
{
	private ArrayList<Ball> balls = new ArrayList<Ball>();
	private float angle = 0.0f;
	
	private Grid plane = null;

	public PickingTest()
	{
	}
	
	public void init()
	{
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
		
		Mouse.setGrabbed(false);
	}
	
	public Vector3f randomVector(float start, float max)
	{
		return new Vector3f(max * (float) Math.random() + start,  max * (float) Math.random() + start, max * (float) Math.random() + start);
	}
	
	public void keyPressed()
	{
		int keycode = Keyboard.getEventKey();
		switch(keycode)
		{
			case Keyboard.KEY_SPACE:
				for(int i = 0; i < 200; i++)
				{
					Ball ball = new Ball(randomVector(-4, 8), new Vector3f(), 0.1f * (float) Math.random() + 0.1f, randomVector(0.2f, 0.6f));
					ball.compile();
					balls.add(ball);
				}
				System.out.println("Balls count: " + balls.size());
				break;
				
			case Keyboard.KEY_F:
				System.out.println("FPS: " + FPS.get());
				break;
		}
	}
	
	public void render()
	{
		logic();
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
		
			renderRealtime();
	}
	
	public void renderRealtime()
	{
		GL11.glPushMatrix();
		
		GL11.glTranslatef(0.0f, 0.0f, -10.0f);
		//GL11.glRotatef(-_angle, 0.0f, 1.0f, 0.0f);
		
			plane.render();
			
			//GL11.glInitNames();
			for(int i = 0; i < balls.size(); i++)
			{
				GL11.glLoadName(i);
				balls.get(i).render();
				//GL11.glPopName();
			}
		
		GL11.glPopMatrix();
            
	}
	
	private void logic()
	{
		if(Mouse.isButtonDown(0))
			selection(Mouse.getX(), Display.getDisplayMode().getHeight() - Mouse.getY());
	}
	
	private void selection(int mouse_x, int mouse_y)
	{
		// The selection buffer
		IntBuffer selBuffer = ByteBuffer.allocateDirect(1024).order(ByteOrder.nativeOrder()).asIntBuffer();
		int buffer[] = new int[256];
		
		IntBuffer vpBuffer = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asIntBuffer();
		// The size of the viewport. [0] Is <x>, [1] Is <y>, [2] Is <width>, [3] Is <height>
            int[] viewport = new int[4];
		
		// The number of "hits" (objects within the pick area).
		int hits;

		// Get the viewport info
            GL11.glGetInteger(GL11.GL_VIEWPORT, vpBuffer);
            vpBuffer.get(viewport);
		
		// Set the buffer that OpenGL uses for selection to our buffer
		GL11.glSelectBuffer(selBuffer);
		
		// Change to selection mode
		GL11.glRenderMode(GL11.GL_SELECT);
		
		// Initialize the name stack (used for identifying which object was selected)
		GL11.glInitNames();
		GL11.glPushName(0);

		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		
		/*  create 5x5 pixel picking region near cursor location */
		GLU.gluPickMatrix( (float) mouse_x, (float) mouse_y, 5.0f, 5.0f, vpBuffer);
		
		//GL11.glOrtho(0.0, 8.0, 0.0, 8.0, -0.5, 2.5);
		renderRealtime();
		GL11.glPopMatrix();

		// Exit selection mode and return to render mode, returns number selected
		hits = GL11.glRenderMode(GL11.GL_RENDER);
		System.out.println("Number: " + hits + " (" + mouse_x + ", " + mouse_y + ")");
		
		selBuffer.get(buffer);
            // Objects Were Drawn Where The Mouse Was
            if (hits > 0) {
                  // If There Were More Than 0 Hits
                  int choose = buffer[3]; // Make Our Selection The First Object
                  int depth = buffer[1]; // Store How Far Away It Is
                  for (int i = 1; i < hits; i++) {
                        // Loop Through All The Detected Hits
				// If This Object Is Closer To Us Than The One We Have Selected
                        if (buffer[i * 4 + 1] < (int) depth) {
                              choose = buffer[i * 4 + 3]; // Select The Closer Object
                              depth = buffer[i * 4 + 1]; // Store How Far Away It Is
                        }
                  }

                  System.out.println("Chosen: " + choose);
            }
	}
	
	/*private int buffer[] = new int[512];
	
	public void startPicking(int cursorX, int cursorY)
	{
		int viewport[] = new int[4];

		IntBuffer nioSelectBuf = ByteBuffer.allocateDirect(2048).asIntBuffer();
		GL11.glSelectBuffer(nioSelectBuf);
		GL11.glRenderMode(GL11.GL_SELECT);
		nioSelectBuf.get(buffer);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();

		IntBuffer nioViewport = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asIntBuffer();
		nioViewport.order();
		GL11.glGetInteger(GL11.GL_VIEWPORT, nioViewport);
		nioViewport.get(viewport);
		
		GLU.gluPickMatrix((float) cursorX, (float) viewport[3] - cursorY, 5.0f, 5.0f, nioViewport);
		GLU.gluPerspective(45, (float) Launcher.getDisplay().getWidth() / (float) Launcher.getDisplay().getHeight(), 0.1f, 200.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glInitNames();
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
	}
	
	void stopPicking()
	{
		GL11.glMatrixMode(GL11.GL_PROJECTION);                                // Select The Projection Matrix
        GL11.glPopMatrix();                                              // Pop The Projection Matrix
        GL11.glMatrixMode(GL11.GL_MODELVIEW);                                 // Select The Modelview Matrix
        int hits = GL11.glRenderMode(GL11.GL_RENDER);                               // Switch To Render Mode, Find Out How Many
                                                                    // Objects Were Drawn Where The Mouse Was
        if(hits > 0)
        {                                               // If There Were More Than 0 Hits
			int choose = buffer[3];                                 // Make Our Selection The First Object
			int depth = buffer[1];                                  // Store How Far Away It Is
			
			for (int i = 1; i < hits; i++)
			{                // Loop Through All The Detected Hits
				// If This Object Is Closer To Us Than The One We Have Selected
				if (buffer[i * 4 + 1] < (int)depth)
				{
					choose = buffer[i * 4 + 3];                      // Select The Closer Object
					depth = buffer[i * 4 + 1];                       // Store How Far Away It Is
				}
			}
			
			System.out.println(choose);
		}
	}*/
}
