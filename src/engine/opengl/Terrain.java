package engine.opengl;

import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

import engine.util.*;
import java.nio.*;

import org.lwjgl.*;
import org.lwjgl.input.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;

public class Terrain extends DrawableObject
{
	private boolean computedNormals = false;
	private float falloutRatio = 0.3f;
	private float renderingHeight = 0.0f;
	private float renderingWidth = 1.0f;
	
	private int width = 0;
	private int height = 0;
	
	private float verties[][] = null;
	private Vector3f normals[][] = null;
	
	private int compileID = 0;
	
	public Terrain(String filename, float width, float height, float falloutRatio)
	{
		this.falloutRatio = falloutRatio;
		this.renderingHeight = height;
		this.renderingWidth = width;
		
		load(filename);
		compile();
	}
	
	public float getFalloutRatio()
	{
		return this.falloutRatio;
	}
	
	public void setFalloutRatio(float value)
	{
		this.falloutRatio = value;
		this.computedNormals = false;
		computeNormals();
		compile();
	}
	
	public float getHeightRatio()
	{
		return this.renderingHeight;
	}
	
	public void setHeightRatio(float value)
	{
		this.renderingHeight = value;
		compile();
	}
	
	public boolean load(String filename)
	{
		long t1 = 0;
		long t2 = 0;
		try
		{
					t1 = System.currentTimeMillis();
			File file = new File(filename);
			BufferedImage image = ImageIO.read(file);
					t2 = System.currentTimeMillis();
					System.out.println("load::ImageIO.read(...): " + (t2 - t1) + " ms");
			
				t1 = System.currentTimeMillis();
			this.width = image.getWidth(null);
			this.height = image.getHeight(null);
			this.verties = new float[this.width][this.height];
			this.normals = new Vector3f[this.width][this.height];
					t2 = System.currentTimeMillis();
					System.out.println("load::variables(...): " + (t2 - t1) + " ms");
			
					t1 = System.currentTimeMillis();
			for(int y = 0; y < this.height; y++)
				for(int x = 0; x < this.width; x++)
				{
					int color = image.getRGB(x, y) & 0xff;
					float h = (float) this.height * ((color / 255.0f) - 0.5f);
					setHeightMap(x, y, h);
				}
				
					t2 = System.currentTimeMillis();
					System.out.println("load::for(...): " + (t2 - t1) + " ms");
			
					t1 = System.currentTimeMillis();
			computeNormals();
					t2 = System.currentTimeMillis();
					System.out.println("load::computeNormals(...): " + (t2 - t1) + " ms");
		}
		catch(IOException e)
		{
			return false;
		}

		return true;
	}
	
	public float getHeightMap(int x, int z)
	{
		return this.verties[z][x];
	}
	
	public void setHeightMap(int x, int z, float y)
	{
		this.verties[z][x] = y;
		computedNormals = false;
	}
	
	public void computeNormals()
	{
		if(computedNormals)
			return;
			
		Vector3f normalsTemp[][] = new Vector3f[this.width][this.height];
		for(int z = 0; z < this.height; z++)
			for(int x = 0; x < this.width; x++)
			{
				Vector3f sum = new Vector3f(0.0f, 0.0f, 0.0f);
				
				Vector3f out = new Vector3f();
				if(z > 0)
					out.setLocation(0.0f, verties[z - 1][x] - verties[z][x], -1.0f);
				
				Vector3f in = new Vector3f();
				if (z < this.height - 1)
					in.setLocation(0.0f, verties[z + 1][x] - verties[z][x], 1.0f);
				
				Vector3f left = new Vector3f();
				if (x > 0)
					left.setLocation(-1.0f, verties[z][x - 1] - verties[z][x], 0.0f);
				
				Vector3f right = new Vector3f();
				if (x < this.width - 1)
					right.setLocation(1.0f, verties[z][x + 1] - verties[z][x], 0.0f);
				
				Vector3f cross = new Vector3f();
				if (x > 0 && z > 0)
				{
					Vector3f.cross(out, left, cross);
					cross.normalise();
					
					sum.add(cross);
				}
				
				if (x > 0 && z < this.height - 1)
				{
					Vector3f.cross(left, in, cross);
					cross.normalise();
					
					sum.add(cross);
				}
				
				if (x < this.width - 1 && z < this.height - 1)
				{
					Vector3f.cross(in, right, cross);
					cross.normalise();
					
					sum.add(cross);
				}
				
				if (x < this.width - 1 && z > 0)
				{
					Vector3f.cross(right, out, cross);
					cross.normalise();
					
					sum.add(cross);
				}
				
				normalsTemp[z][x] = sum;
			}
			
		for(int z = 0; z < this.height; z++)
			for(int x = 0; x < this.width; x++)
			{
				Vector3f sum = normalsTemp[z][x];
				
				if (x > 0)
				{
					Vector3f multi = Vector3f.multiply(normalsTemp[z][x - 1], falloutRatio);
					sum.add(multi);
				}
				
				if (x < this.width - 1)
				{
					Vector3f multi = Vector3f.multiply(normalsTemp[z][x + 1], falloutRatio);
					sum.add(multi);
				}
				
				if (z > 0)
				{
					Vector3f multi = Vector3f.multiply(normalsTemp[z - 1][x], falloutRatio);
					sum.add(multi);
				}
				
				if (z < 0)
				{
					Vector3f multi = Vector3f.multiply(normalsTemp[z + 1][x], falloutRatio);
					sum.add(multi);
				}
				
				if(sum.magnitude() == 0.0f)
					sum.setLocation(0.0f, 1.0f, 0.0f);
					
				normals[z][x] = sum;
			}
			
		computedNormals = true;
	}
	
	public Vector3f getNormal(int x, int z)
	{
		if (!computedNormals)
			computeNormals();
		
		return normals[z][x];
	}
	
	public void compile()
	{
		this.compileID = GL11.glGenLists(1);
		GL11.glNewList(this.compileID, GL11.GL_COMPILE);
		
		GL11.glPushMatrix();
		
		float scale = 5.0f / Math.max(this.width - 1, this.height - 1);
		GL11.glScalef(scale * renderingWidth, scale * renderingHeight, scale * renderingWidth);
		GL11.glTranslatef((float) -this.width / 2.0f, 0.0f, (float) -this.height / 2.0f);
					 
		for(int z = 0; z < this.height - 1; z++)
		{
			//Makes OpenGL draw a triangle at every three consecutive vertices
			GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
			for(int x = 0; x < this.width; x++)
			{
				Vector3f normal = getNormal(x, z);
				GL11.glNormal3f(normal.getX(), normal.getY(), normal.getZ());
				GL11.glVertex3f(x, getHeightMap(x, z), z);
				
				normal = getNormal(x, z + 1);
				GL11.glNormal3f(normal.getX(), normal.getY(), normal.getZ());
				GL11.glVertex3f(x, getHeightMap(x, z + 1), z + 1);
			}
			GL11.glEnd();
		}
		GL11.glPopMatrix();
		
		GL11.glEndList();
	}
	
	private float rotate = 0.0f;
	
	public void render()
	{
		rotate += 0.2f;
		if(compileID == 0)
			return;
		
		GL11.glPushMatrix();
			GL11.glTranslatef(0.0f, 0.0f, -8.0f);
			GL11.glRotatef(20, 1.0f, 0.0f, 0.0f);
		
			GL11.glPushMatrix();
				GL11.glRotatef(rotate, 0.0f, 1.0f, 0.0f);
			
				GL11.glColor3f(1.0f, 1.0f, 1.0f);
				GL11.glCallList(this.compileID);
			
			GL11.glPopMatrix();
			
		GL11.glPopMatrix();
	}
}
