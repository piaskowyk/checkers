package application;

public class Game {

	public MyCircle[] pionsW = new MyCircle[8];
	public MyCircle[] pionsB = new MyCircle[8];
	
	public Pawn[] pawnsA = new Pawn[8];
	public Pawn[] pawnsB = new Pawn[8];
	
	public int pointA = 8;
	public int pointB = 8;

	public Boolean[][] ground = new Boolean[8][8];
	
	public Pawn[][] pawnsGround = new Pawn[8][8];
	
	public void init() {
		for(int i=0; i<8; i++) {
			pawnsA[i] = new Pawn();
			pawnsB[i] = new Pawn();
		}
		
		for(int i=0; i<8; i++) {
			for(int k=0; k<8; k++) {
				if(i == 0 && k%2 == 1) ground[k][i] = true;
				else if(i == 1 && k%2 == 0) ground[k][i] = true;
				else if(i == 6 && k%2 == 1) ground[k][i] = true;
				else if(i == 7 && k%2 == 0) ground[k][i] = true;
				else ground[k][i] = false;
			}
		}
		
		for(int i=0; i<8; i++) {
			for(int k=0; k<8; k++) {
				pawnsGround[i][k] = null;
			}
		}
	}
		
}