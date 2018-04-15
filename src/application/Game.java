package application;

public class Game {

	public MyCircle[] pionsW = new MyCircle[8];
	public MyCircle[] pionsB = new MyCircle[8];
	
	public Pion[] playerA = new Pion[8];
	public Pion[] playerB = new Pion[8];
	
	public class Pion{
		MyCircle pion = new MyCircle(10);
	}

}