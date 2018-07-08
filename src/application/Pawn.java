package application;

public class Pawn{
	public MyCircle circle;
	public double x;
	public double y;
	public int indexX;
	public int indexY;
	public Color type;
	public boolean enable = true;
	
	public enum Color{
		A,
		B,
		A1,
		B1
	};
}