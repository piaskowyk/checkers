package application;

import java.awt.Point;
import java.util.ArrayList;

public class Game {
	public Pawn[][] GampePlayPawns = new Pawn[8][8];
	public int pointA = 8;
	public int pointB = 8;	
	public Pawn.Color tourIs = Pawn.Color.A;
	public Point mustMovePt = new Point(0, 0);
	public boolean mustMove = false;
	public ArrayList<Pawn> attackers = null;
}