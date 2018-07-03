package application;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class MainViewController {
	
	private double border;
	private double itemSize;
	private double space;
	private double R;
	
	@FXML
	private Button btn;
	@FXML
	private Pane board;
	@FXML
	private Canvas ground;
	@FXML
	private Label tourText;
	@FXML
	private Label APointText;
	@FXML
	private Label BPointText;
	
	public void startGame(Game game) {
		drawPlayGround();
		drawPawns(game);
		eventInit(game);
	}
	
	private void drawPlayGround() {
		GraphicsContext playGround = ground.getGraphicsContext2D();
			
		playGround.setStroke(Color.BLUE);
		playGround.setLineWidth(5);
		playGround.strokeLine(40, 10, 10, 40);
		
		double border = ground.getWidth();
		double itemSize = border/8;
		
		playGround.setFill(Color.WHITE);
		for(int i=0; i<8; i++) {
			for(int k=0; k<8; k++) {
				if(i % 2 == 0 && k % 2 == 0) playGround.fillRect(itemSize*k, itemSize*i, itemSize, itemSize);
				else if(i % 2 == 1 && k % 2 == 1) playGround.fillRect(itemSize*k, itemSize*i, itemSize, itemSize);
			}
		}
		
		playGround.setFill(Color.GREEN);
		for(int i=0; i<8; i++) {
			for(int k=0; k<8; k++) {
				if(i % 2 == 0 && k % 2 != 0) playGround.fillRect(itemSize*k, itemSize*i, itemSize, itemSize);
				else if(i % 2 == 1 && k % 2 != 1) playGround.fillRect(itemSize*k, itemSize*i, itemSize, itemSize);
			}
		}
	}
	
	private void drawPawns(Game game) {
		border = ground.getWidth();
		itemSize = border/8;
		space = itemSize/2;
		R = 10;

		for(int i=0; i<8; i++) {
			for(int k=0; k<8; k++) {
				if((i == 0 && k%2 == 1) || (i == 1 && k%2 == 0)) {
					game.GampePlayPawns[k][i] = new Pawn();
					game.GampePlayPawns[k][i].type = Pawn.Color.A;
					game.GampePlayPawns[k][i].indexX = k;
					game.GampePlayPawns[k][i].indexY = i;
					game.GampePlayPawns[k][i].circle = new MyCircle(R*2);
					game.GampePlayPawns[k][i].circle.setFill(Color.DARKBLUE);
					board.getChildren().add(game.GampePlayPawns[k][i].circle);
					game.GampePlayPawns[k][i].circle.setCenterX(itemSize*k + space);
					game.GampePlayPawns[k][i].circle.setCenterY(itemSize*i + space);
				} 
				else if((i == 6 && k%2 == 1) || (i == 7 && k%2 == 0)) {		
					game.GampePlayPawns[k][i] = new Pawn();
					game.GampePlayPawns[k][i].type = Pawn.Color.B;
					game.GampePlayPawns[k][i].indexX = k;
					game.GampePlayPawns[k][i].indexY = i;
					game.GampePlayPawns[k][i].circle = new MyCircle(R*2);
					game.GampePlayPawns[k][i].circle.setFill(Color.BROWN);
					board.getChildren().add(game.GampePlayPawns[k][i].circle);
					game.GampePlayPawns[k][i].circle.setCenterX(itemSize*k + space);
					game.GampePlayPawns[k][i].circle.setCenterY(itemSize*i + space);
				}
				else {
					game.GampePlayPawns[k][i] = null;
				}
			}
		}
	}
	
	private void eventInit(Game game) {
		for(int i = 0; i < 8; i++) {
			for(int k = 0; k < 8; k++) {
				if(game.GampePlayPawns[k][i] != null) {
					eventInitPawn(game, game.GampePlayPawns[k][i]);
				}
			}
		}
	}
	
	private boolean isPossibleToKill(Game game, Pawn item, int posX, int posY) {
		boolean out = false;
		if(posX < 0 || posY < 0 || posX > 7 || posY > 7) return false;
		if(game.GampePlayPawns[posX][posY] != null) return false;
		
		if(item.type == Pawn.Color.A) {	
			if((posX - 2 >= 0 && posY + 2 <= 7)
					&& game.GampePlayPawns[posX - 1][posY + 1] != null 
					&& (game.GampePlayPawns[posX - 1][posY + 1].type == Pawn.Color.B 
						|| game.GampePlayPawns[posX - 1][posY + 1].type == Pawn.Color.B1)
					&& game.GampePlayPawns[posX - 2][posY + 2] == null) {
				out = true;
			}
			
			if((posX + 2 <= 7 && posY + 2 <= 7)
					&& game.GampePlayPawns[posX + 1][posY + 1] != null 
					&& (game.GampePlayPawns[posX + 1][posY + 1].type == Pawn.Color.B 
						|| game.GampePlayPawns[posX + 1][posY + 1].type == Pawn.Color.B1)
					&& game.GampePlayPawns[posX + 2][posY + 2] == null) {
				out = true;
			}
		}
		
		if(item.type == Pawn.Color.B) {	
			if((posX - 2 >= 0 && posY - 2 >= 0)
					&& game.GampePlayPawns[posX - 1][posY - 1] != null 
					&& (game.GampePlayPawns[posX - 1][posY - 1].type == Pawn.Color.A 
						|| game.GampePlayPawns[posX - 1][posY - 1].type == Pawn.Color.A1)
					&& game.GampePlayPawns[posX - 2][posY - 2] == null) {
				out = true;
			}
			
			if((posX + 2 <= 7 && posY - 2 >= 0)
					&& game.GampePlayPawns[posX + 1][posY - 1] != null 
					&& (game.GampePlayPawns[posX + 1][posY - 1].type == Pawn.Color.A 
						|| game.GampePlayPawns[posX + 1][posY - 1].type == Pawn.Color.A1)
					&& game.GampePlayPawns[posX + 2][posY - 2] == null) {
				out = true;
			}
		}
		
		if(item.type == Pawn.Color.A1) {
			//TODO
		}

		if(item.type == Pawn.Color.B1) {
			//TODO
		}
		
		if(out) {			
			game.mustMovePt.x = posX;
			game.mustMovePt.y = posY;
		}
		game.mustMove = out;
		return out;
	}
	
	private void checkAllToPossibleKill() {
		//TODO
	}
	
	private void endGame(Game game) {
		//TODO
	}
	
	private void movePawn(Game game, Pawn item, int biasX, int biasY) {
		boolean nextTour = false;
		boolean stop = false;
		boolean permission = false;
		boolean chanceType = false;
		boolean killEnemy = false;
		int enemyX = 0, enemyY = 0;
		
		if(biasX < 0 || biasY < 0 || biasX > 7 || biasY > 7) stop = true;
		if(!stop && game.GampePlayPawns[biasX][biasY] != null) stop = true;
				
		if(!stop && !permission && item.type == Pawn.Color.A) {
			if(biasX == item.indexX - 1 && biasY == item.indexY + 1) {
				permission = true;
				if(biasY == 7) chanceType = true;
			}
			
			if(biasX == item.indexX + 1 && biasY == item.indexY + 1) {
				permission = true;
				if(biasY == 7) chanceType = true;
			}
			
			if((biasX == item.indexX - 2 && biasY == item.indexY + 2)
					&& (game.GampePlayPawns[item.indexX - 1][item.indexY + 1].type == Pawn.Color.B 
						|| game.GampePlayPawns[item.indexX - 1][item.indexY + 1].type == Pawn.Color.B1)) {
				permission = true;
				killEnemy = true;
				enemyX = item.indexX - 1;
				enemyY = item.indexY + 1;
				if(isPossibleToKill(game, item, biasX, biasY)) nextTour = true;
			}
			
			if((biasX == item.indexX + 2 && biasY == item.indexY + 2)
					&& (game.GampePlayPawns[item.indexX + 1][item.indexY + 1].type == Pawn.Color.B 
						|| game.GampePlayPawns[item.indexX + 1][item.indexY + 1].type == Pawn.Color.B1)) {
				permission = true;
				killEnemy = true;
				enemyX = item.indexX + 1;
				enemyY = item.indexY + 1;
				if(isPossibleToKill(game, item, biasX, biasY)) nextTour = true;
			}
		}
		
		if(!stop && !permission && item.type == Pawn.Color.B) {
			if(biasX == item.indexX - 1 && biasY == item.indexY - 1) {
				permission = true;
				if(biasY == 0) chanceType = true;
			}
			
			if(biasX == item.indexX + 1 && biasY == item.indexY - 1) {
				permission = true;
				if(biasY == 0) chanceType = true;
			}
			
			if((biasX == item.indexX - 2 && biasY == item.indexY - 2) 
					&& (game.GampePlayPawns[item.indexX - 1][item.indexY - 1].type == Pawn.Color.A 
						|| game.GampePlayPawns[item.indexX - 1][item.indexY - 1].type == Pawn.Color.A1)) {
				permission = true;
				killEnemy = true;
				enemyX = item.indexX - 1;
				enemyY = item.indexY - 1;
				if(isPossibleToKill(game, item, biasX, biasY)) nextTour = true;
			}
			
			if((biasX == item.indexX + 2 && biasY == item.indexY - 2)
					&& (game.GampePlayPawns[item.indexX + 1][item.indexY - 1].type == Pawn.Color.A 
					|| game.GampePlayPawns[item.indexX + 1][item.indexY - 1].type == Pawn.Color.A1)) {
				permission = true;
				killEnemy = true;
				enemyX = item.indexX + 1;
				enemyY = item.indexY - 1;
				if(isPossibleToKill(game, item, biasX, biasY)) nextTour = true;
			}
		}
		
		if(!stop && !permission && item.type == Pawn.Color.A1) {
			//TODO
		}

		if(!stop && !permission && item.type == Pawn.Color.B1) {
			//TODO
		}
		
		if(!stop && permission) {		
			double ptX = biasX * itemSize + space;
			double ptY = biasY * itemSize + space;
			
			item.circle.setCenterX(ptX);
			item.circle.setCenterY(ptY);
			item.x = ptX;
			item.y = ptY;
			
			Point lastPt = new Point();
			lastPt.x = item.indexX;
			lastPt.y = item.indexY;
			game.GampePlayPawns[lastPt.x][lastPt.y].indexX = biasX;
			game.GampePlayPawns[lastPt.x][lastPt.y].indexY = biasY;
					
			game.GampePlayPawns[biasX][biasY] = game.GampePlayPawns[lastPt.x][lastPt.y];
			game.GampePlayPawns[lastPt.x][lastPt.y] = null;
			
			if(chanceType && item.type == Pawn.Color.A) {
				item.type = Pawn.Color.A1;
			}
			else if(chanceType && item.type == Pawn.Color.B) {
				item.type = Pawn.Color.B1;
			}

			if(!nextTour) {				
				if(game.tourIs == Pawn.Color.A) {
					game.tourIs = Pawn.Color.B;
					tourText.setText("B");
				} else {
					game.tourIs = Pawn.Color.A;
					tourText.setText("A");
				}
			}
			
			if(killEnemy) {
				if(game.GampePlayPawns[enemyX][enemyY].type == Pawn.Color.A 
					|| game.GampePlayPawns[enemyX][enemyY].type == Pawn.Color.A1) {
					game.pointA--;
					APointText.setText(Integer.toString(game.pointA));
				} else {
					game.pointB--;
					BPointText.setText(Integer.toString(game.pointB));
				}
				game.GampePlayPawns[enemyX][enemyY].circle.setCenterX(-1000);//???
				game.GampePlayPawns[enemyX][enemyY] = null;
			}
			
		} else {
			double ptX = item.indexX * itemSize + space;
			double ptY = item.indexY * itemSize + space;
			
			item.circle.setCenterX(ptX);
			item.circle.setCenterY(ptY);
			item.x = ptX;
			item.y = ptY;
		}	
		
		if(game.pointA == 0 || game.pointB == 0) {
			endGame(game);
		}
	}
	
	private void printTab(Game game) {
		for(int i = 0; i < 8; i++) {
			for(int k = 0; k < 8; k++) {
				if(game.GampePlayPawns[k][i] != null) {
					System.out.print(1);
					if(k != 7) System.out.print(",");
				} else {
					System.out.print(0);
					if(k != 7) System.out.print(",");
				}
			}
			System.out.println();
		}
	}
	
	private boolean checkPermissionToMove(Game game, Pawn item) {
		if(game.tourIs == Pawn.Color.A && item.type != Pawn.Color.A && item.type != Pawn.Color.A1) return false;
		if(game.tourIs == Pawn.Color.B && item.type != Pawn.Color.B && item.type != Pawn.Color.B1) return false;
		if(game.mustMove && (item.indexX != game.mustMovePt.x || item.indexY != game.mustMovePt.y)) {
			//return false;
		}
		return true;
	}
	
	private void eventInitPawn(Game game, Pawn item) {
		/////////////////////////////////////////////////////////
		item.circle.setOnMousePressed(new EventHandler<Event>() {
			@Override
			public void handle(Event event)
			{
				if(!checkPermissionToMove(game, item)) return;
				PointerInfo a = MouseInfo.getPointerInfo();
				Point b = a.getLocation();
				item.circle.x = (int) b.getX();
				item.circle.y = (int) b.getY();
				item.circle.toFront();
			}
		});
		////////////////////////////////////////////////////////
		item.circle.setOnMouseDragged(new EventHandler<Event>() {
			@Override
			public void handle(Event event)
			{
				if(!checkPermissionToMove(game, item)) return;
				PointerInfo a = MouseInfo.getPointerInfo();
				Point b = a.getLocation();
				int x = (int) b.getX();
				int y = (int) b.getY();
				item.circle.setCenterX(item.circle.getCenterX() + x - item.circle.x);
				item.circle.setCenterY(item.circle.getCenterY() + y - item.circle.y);
				item.circle.x = x;
				item.circle.y = y;
			}
		});
		////////////////////////////////////////////////////////
		item.circle.setOnMouseReleased(new EventHandler<Event>() {
			@Override
			public void handle(Event event)
			{	
				if(!checkPermissionToMove(game, item)) return;
				PointerInfo a = MouseInfo.getPointerInfo();
				Point b = a.getLocation();
				int x = (int) b.getX();
				int y = (int) b.getY();
				int biasX, biasY;
				
				biasX = (int)Math.floor((item.circle.getCenterX() + x - item.circle.x) / itemSize);
				biasY = (int)Math.floor((item.circle.getCenterY() + y - item.circle.y) / itemSize);
			
				movePawn(game, item, biasX, biasY);
			}
		});
	}
}
