package engine.util;

public class Vector3f extends org.lwjgl.util.vector.Vector3f
{
	float alpha = 1.0f;
	
	public Vector3f()
	{
		super(0.0f, 0.0f, 0.0f);	
	}
	
	public Vector3f(Vector3f vec)
	{
		super(vec);	
	}
	
	public Vector3f(float x, float y, float z)
	{
		super(x, y, z);	
	}
	
	public Vector3f(float x, float y, float z, float alpha)
	{
		super(x, y, z);
		this.alpha = alpha;	
	}
	
	public float getAlpha()
	{
		return this.alpha;
	}
	
	public float[] getFloats()
	{
		return new float[] {this.x, this.y, this.z, this.alpha};
	}
	
	public void setLocation(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float angle(Vector3f vec)
	{
		return org.lwjgl.util.vector.Vector3f.angle(this, vec);
	}
	
	public void cross(Vector3f vec)
	{
		org.lwjgl.util.vector.Vector3f.cross(this, vec, this);
	}
	
	public static Vector3f cross(Vector3f v1, Vector3f v2)
	{
		Vector3f vec = new Vector3f();
		org.lwjgl.util.vector.Vector3f.cross(v1, v2, vec);
		return vec;
	}
	
	public float dot(Vector3f vec)
	{
		return org.lwjgl.util.vector.Vector3f.dot(this, vec);
	}
	
	public double magnitude()
	{
		return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
	}
	
	public static double magnitude(Vector3f vec)
	{
		return new Vector3f(vec).magnitude();
	}
	
	public boolean isZero()
	{
		return (magnitude() == 0.0);
	}
	
	//MATH
	public void multiply(Vector3f vec)
	{
		this.x *= vec.getX();
		this.y *= vec.getY();
		this.z *= vec.getZ();
	}
	
	public void multiply(float value)
	{
		multiply(new Vector3f(value, value, value));
	}
	
	public static Vector3f multiply(Vector3f vec, float value)
	{
		Vector3f newVector = new Vector3f(vec);
		newVector.multiply(value);
		return newVector;
	}
	
	public static Vector3f multiply(Vector3f vec, Vector3f vec2)
	{
		Vector3f newVector = new Vector3f(vec);
		newVector.multiply(vec2);
		return newVector;
	}
	
	public void add(float value)
	{
		add(new Vector3f(value, value, value));
	}
	
	public void add(Vector3f vec)
	{
		org.lwjgl.util.vector.Vector3f.add(this, vec, this);
	}
	
	public static Vector3f add(Vector3f v1, Vector3f v2)
	{
		Vector3f vec = new Vector3f(v1);
		vec.add(v2);
		return vec;
	}
	
	public static Vector3f add(Vector3f v, float fl)
	{
		Vector3f vec = new Vector3f(v);
		vec.add(fl);
		return vec;
	}
	
	public void sub(float value)
	{
		sub(new Vector3f(value, value, value));
	}
	
	public void sub(Vector3f vec)
	{
		org.lwjgl.util.vector.Vector3f.sub(this, vec, this);
	}
	
	public static Vector3f sub(Vector3f v1, Vector3f v2)
	{
		Vector3f vec = new Vector3f(v1);
		vec.sub(v2);
		return vec;
	}
	
	public static Vector3f sub(Vector3f v, float fl)
	{
		Vector3f vec = new Vector3f(v);
		vec.sub(fl);
		return vec;
	}
	
	public Vector3f clone()
	{
		return new Vector3f(this);
	}
}
