package application;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.util.ArrayList;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class MainViewController {
	
	private double border;
	private double itemSize;
	private double space;
	private double R;
	
	GraphicsContext playGround;
	
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
		playGround = ground.getGraphicsContext2D();
		playGround.clearRect(0, 0, ground.getWidth(), ground.getHeight());
			
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
	
	private boolean isPossibleToKill(Game game, Pawn item, boolean getFromItem, int x, int y) {
		int posX; 
		int posY;
		boolean out = false;

		if(getFromItem) {			
			posX = item.indexX; 
			posY = item.indexY;
		} else {
			posX = x; 
			posY = y;
		}
		
		if(posX < 0 || posY < 0 || posX > 7 || posY > 7) return false;
		if(!getFromItem) {			
			if(game.GampePlayPawns[posX][posY] != null) return false;
		}
		
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
		
		if(item.type == Pawn.Color.A1 || item.type == Pawn.Color.B1) {
			int tmpX, tmpY;

			for(int i = 1; i < 8; i++) {
				// 45 deg
				tmpX = item.indexX + i;
				tmpY = item.indexY - i;
				if(tmpX > 0 && tmpX < 7 && tmpY > 0 && tmpY < 7) {
					if(item.type == Pawn.Color.A1) {
						if(game.GampePlayPawns[tmpX][y] != null
								&& (game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.B 
									|| game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.B1)
								&& game.GampePlayPawns[tmpX + 1][tmpY - 1] == null
								&& game.GampePlayPawns[tmpX - 1][tmpY + 1] == null) {
							out = true;
							break;
						}
					} else {
						if(game.GampePlayPawns[tmpX][tmpY] != null
								&& (game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.A 
									|| game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.A1)
								&& game.GampePlayPawns[tmpX + 1][tmpY - 1] == null
								&& game.GampePlayPawns[tmpX - 1][tmpY + 1] == null) {
							out = true;
							break;
						}
					}
				}
				
				// 135 deg
				tmpX = item.indexX + i;
				tmpY = item.indexY + i;
				if(tmpX > 0 && tmpX < 7 && tmpY > 0 && tmpY < 7) {
					if(item.type == Pawn.Color.A1) {
						if(game.GampePlayPawns[tmpX][tmpY] != null
								&& (game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.B 
									|| game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.B1)
								&& game.GampePlayPawns[tmpX + 1][tmpY + 1] == null
								&& game.GampePlayPawns[tmpX - 1][tmpY - 1] == null) {
							out = true;
							break;
						}
					} else {
						if(game.GampePlayPawns[tmpX][tmpY] != null
								&& (game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.A 
									|| game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.A1)
								&& game.GampePlayPawns[tmpX + 1][tmpY + 1] == null
								&& game.GampePlayPawns[tmpX - 1][tmpY - 1] == null) {
							out = true;
							break;
						}
					}
				}
				
				// 225 deg
				tmpX = item.indexX - i;
				tmpY = item.indexY + i;
				if(tmpX > 0 && tmpX < 7 && tmpY > 0 && tmpY < 7) {
					if(item.type == Pawn.Color.A1) {
						if(game.GampePlayPawns[tmpX][tmpY] != null
								&& (game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.B 
									|| game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.B1)
								&& game.GampePlayPawns[tmpX - 1][tmpY + 1] == null
								&& game.GampePlayPawns[tmpX + 1][tmpY - 1] == null){
							out = true;
							break;
						}
					} else {
						if(game.GampePlayPawns[tmpX][tmpY] != null
								&& (game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.A 
									|| game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.A1)
								&& game.GampePlayPawns[tmpX - 1][tmpY + 1] == null
								&& game.GampePlayPawns[tmpX + 1][tmpY - 1] == null){
							out = true;
							break;
						}
					}
				}
				
				// 315 deg
				tmpX = item.indexX - i;
				tmpY = item.indexY - i;
				if(tmpX > 0 && tmpX < 7 && tmpY > 0 && tmpY < 7) {
					if(item.type == Pawn.Color.A1) {
						if(game.GampePlayPawns[tmpX][tmpY] != null
								&& (game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.B 
									|| game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.B1)
								&& game.GampePlayPawns[tmpX - 1][tmpY - 1] == null
								&& game.GampePlayPawns[tmpX + 1][tmpY + 1] == null){
							out = true;
							break; 
						}
					} else {
						if(game.GampePlayPawns[tmpX][tmpY] != null
								&& (game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.A 
									|| game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.A1)
								&& game.GampePlayPawns[tmpX - 1][tmpY - 1] == null
								&& game.GampePlayPawns[tmpX + 1][tmpY + 1] == null){
							out = true;
							break; 
						}
					}
				}
				
			}
			
			// 45 deg
			tmpX = item.indexX + 1;
			tmpY = item.indexY - 1;
			if(tmpX > 0 && tmpX < 7 && tmpY > 0 && tmpY < 7) {
				if(item.type == Pawn.Color.A1) {
					if(game.GampePlayPawns[tmpX][tmpY] != null
							&& (game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.B 
								|| game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.B1)
							&& game.GampePlayPawns[tmpX + 1][tmpY - 1] == null) {
						out = true;
					}
				} else {
					if(game.GampePlayPawns[tmpX][tmpY] != null
							&& (game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.A 
								|| game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.A1)
							&& game.GampePlayPawns[tmpX + 1][tmpY - 1] == null) {
						out = true;
					}
				}
			}
			
			// 135 deg
			tmpX = item.indexX + 1;
			tmpY = item.indexY + 1;
			if(tmpX > 0 && tmpX < 7 && tmpY > 0 && tmpY < 7) {
				if(item.type == Pawn.Color.A1) {
					if(game.GampePlayPawns[tmpX][tmpY] != null
							&& (game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.B 
								|| game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.B1)
							&& game.GampePlayPawns[tmpX + 1][tmpY + 1] == null) {
						out = true;
					}
				} else {
					if(game.GampePlayPawns[tmpX][tmpY] != null
							&& (game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.A 
								|| game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.A1)
							&& game.GampePlayPawns[tmpX + 1][tmpY + 1] == null) {
						out = true;
					}
				}
			}
			
			// 225 deg
			tmpX = item.indexX - 1;
			tmpY = item.indexY + 1;
			if(tmpX > 0 && tmpX < 7 && tmpY > 0 && tmpY < 7) {
				if(item.type == Pawn.Color.A1) {
					if(game.GampePlayPawns[tmpX][tmpY] != null
							&& (game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.B 
								|| game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.B1)
							&& game.GampePlayPawns[tmpX - 1][tmpY + 1] == null){
						out = true;
					}
				} else {
					if(game.GampePlayPawns[tmpX][tmpY] != null
							&& (game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.A 
								|| game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.A1)
							&& game.GampePlayPawns[tmpX - 1][tmpY + 1] == null){
						out = true;
					}
				}
			}
			
			// 315 deg
			tmpX = item.indexX - 1;
			tmpY = item.indexY - 1;
			if(tmpX > 0 && tmpX < 7 && tmpY > 0 && tmpY < 7) {
				if(item.type == Pawn.Color.A1) {
					if(game.GampePlayPawns[tmpX][tmpY] != null
							&& (game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.B 
								|| game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.B1)
							&& game.GampePlayPawns[tmpX - 1][tmpY - 1] == null){
						out = true;
					}
				} else {
					if(game.GampePlayPawns[tmpX][tmpY] != null
							&& (game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.A 
								|| game.GampePlayPawns[tmpX][tmpY].type == Pawn.Color.A1)
							&& game.GampePlayPawns[tmpX - 1][tmpY - 1] == null){
						out = true;
					}
				}
			}
		}
		
		if(out) {			
			game.mustMovePt.x = posX;
			game.mustMovePt.y = posY;
		}
		game.mustMove = out;
		return out;
	}
	
	private ArrayList<Pawn> checkAllToPossibleKill(Game game) {
		ArrayList<Pawn> attackers = new ArrayList<Pawn>();
		
		for(int i = 0; i < 8; i++) {
			for(int k = 0; k < 8; k++) {
				if(game.tourIs == Pawn.Color.A) {
					if(game.GampePlayPawns[k][i] != null 
							&& (game.GampePlayPawns[k][i].type == Pawn.Color.A 
								|| game.GampePlayPawns[k][i].type == Pawn.Color.A1)) {
						if(isPossibleToKill(game, game.GampePlayPawns[k][i], true, -1, -1)) {
							attackers.add(game.GampePlayPawns[k][i]);
						}
					}
				} else {
					if(game.GampePlayPawns[k][i] != null 
							&& (game.GampePlayPawns[k][i].type == Pawn.Color.B 
								|| game.GampePlayPawns[k][i].type == Pawn.Color.B1)) {
						if(isPossibleToKill(game, game.GampePlayPawns[k][i], true, -1, -1)) {
							attackers.add(game.GampePlayPawns[k][i]);
						}
					}
				}
			}
		}
		
		if(attackers.isEmpty()) {
			attackers = null;
		}
		
		return attackers;
	}
	
	private void endGame(Game game) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("GAME OVER");
		alert.setHeaderText("Win: " + (game.winner == Pawn.Color.A ? "A" : "B"));
		alert.setContentText("A have " + (8 - game.pointB) + " poins and B have " + (8 - game.pointB) + " points.");
		alert.showAndWait();

		startGame(game);
	}
	
	private boolean gameOver(Game game) {
		boolean canMove = true;
		
		if(game.pointA == 0 || game.pointB == 0) return true;
		
		for(int i = 0; i < 8; i++) {
			for(int k = 0; k < 8; k++) {
				if(game.tourIs == Pawn.Color.A) {
					if(game.GampePlayPawns[k][i] != null 
						&& (game.GampePlayPawns[k][i].type == Pawn.Color.A 
							|| game.GampePlayPawns[k][i].type == Pawn.Color.A1)
						&& canMove(game, game.GampePlayPawns[k][i])) {
						canMove = false;
						break;
					}
				} else {
					if(game.GampePlayPawns[k][i] != null 
						&& (game.GampePlayPawns[k][i].type == Pawn.Color.B 
							|| game.GampePlayPawns[k][i].type == Pawn.Color.B1)
						&& canMove(game, game.GampePlayPawns[k][i])) {
						canMove = false;
						break;
					}
				}
			}
		}
		
		return canMove;
	}
	
	private boolean canMove(Game game, Pawn item) {
		int x, y;
		
		if(item.type == Pawn.Color.A) {
			if(item.indexX - 1 >= 0 && item.indexY + 1 <= 7
				&& game.GampePlayPawns[item.indexX - 1][item.indexY + 1] == null) return true;
			if(item.indexX + 1 <= 7 && item.indexY + 1 <= 7
				&& game.GampePlayPawns[item.indexX + 1][item.indexY + 1] == null) return true;
			if(isPossibleToKill(game, item, true, -1, -1)) return true;
			return false;
		}
		
		if(item.type == Pawn.Color.B) {
			if(item.indexX - 1 >= 0 && item.indexY - 1 >= 0 
				&& game.GampePlayPawns[item.indexX - 1][item.indexY - 1] == null) return true;
			if(item.indexX + 1 <= 7 && item.indexY - 1 >= 0 
				&& game.GampePlayPawns[item.indexX + 1][item.indexY - 1] == null) return true;
			if(isPossibleToKill(game, item, true, -1, -1)) return true;
			return false;
		}
		
		if(item.type == Pawn.Color.A1 || item.type == Pawn.Color.B1) {
			for(int i = 1; i < 8; i++) {
				// 45 deg
				x = item.indexX + i;
				y = item.indexY - i;
				if(x >= 0 && x <= 7 && y >= 0 && y <= 7) {
					if(game.GampePlayPawns[x][y] == null) {
						return true;
					}
				}
				
				// 135 deg
				x = item.indexX + i;
				y = item.indexY + i;
				if(x >= 0 && x <= 7 && y >= 0 && y <= 7) {
					if(game.GampePlayPawns[x][y] == null) {
						return true;
					}
				}
				
				// 225 deg
				x = item.indexX - i;
				y = item.indexY + i;
				if(x >= 0 && x <= 7 && y >= 0 && y <= 7) {
					if(game.GampePlayPawns[x][y] == null) {
						return true;
					}
				}
				
				// 315 deg
				if(x >= 0 && x <= 7 && y >= 0 && y <= 7) {
					if(game.GampePlayPawns[x][y] == null) {
						return true;
					}
				}
				
			}
			
			if(isPossibleToKill(game, item, true, -1, -1)) return true;
			return false;
		}
		
		return false;
	}
	
	private void movePawn(Game game, Pawn item, int biasX, int biasY) {
		boolean nextTour = false;
		boolean stop = false;
		boolean permission = false;
		boolean chanceType = false;
		boolean killEnemy = false;
		int enemyX = 0, enemyY = 0;
		boolean mustKill = game.mustMove;
		
		if(biasX < 0 || biasY < 0 || biasX > 7 || biasY > 7) stop = true;
		if(!stop && game.GampePlayPawns[biasX][biasY] != null) stop = true;
				
		if(!stop && !permission && item.type == Pawn.Color.A) {
			if(biasX == item.indexX - 1 && biasY == item.indexY + 1) permission = true;
			if(biasX == item.indexX + 1 && biasY == item.indexY + 1) permission = true;
			
			if((biasX == item.indexX - 2 && biasY == item.indexY + 2)
					&& game.GampePlayPawns[item.indexX - 1][item.indexY + 1] != null
					&& (game.GampePlayPawns[item.indexX - 1][item.indexY + 1].type == Pawn.Color.B 
						|| game.GampePlayPawns[item.indexX - 1][item.indexY + 1].type == Pawn.Color.B1)) {
				permission = true;
				killEnemy = true;
				enemyX = item.indexX - 1;
				enemyY = item.indexY + 1;
				if(isPossibleToKill(game, item, false, biasX, biasY)) nextTour = true;
			}
			
			if((biasX == item.indexX + 2 && biasY == item.indexY + 2)
					&& game.GampePlayPawns[item.indexX + 1][item.indexY + 1] != null
					&& (game.GampePlayPawns[item.indexX + 1][item.indexY + 1].type == Pawn.Color.B 
						|| game.GampePlayPawns[item.indexX + 1][item.indexY + 1].type == Pawn.Color.B1)) {
				permission = true;
				killEnemy = true;
				enemyX = item.indexX + 1;
				enemyY = item.indexY + 1;
				if(isPossibleToKill(game, item, false, biasX, biasY)) nextTour = true;
			}
		}
		
		if(!stop && !permission && item.type == Pawn.Color.B) {
			if(biasX == item.indexX - 1 && biasY == item.indexY - 1) permission = true;
			if(biasX == item.indexX + 1 && biasY == item.indexY - 1) permission = true;
			
			if((biasX == item.indexX - 2 && biasY == item.indexY - 2) 
					&& game.GampePlayPawns[item.indexX - 1][item.indexY - 1] != null
					&& (game.GampePlayPawns[item.indexX - 1][item.indexY - 1].type == Pawn.Color.A 
						|| game.GampePlayPawns[item.indexX - 1][item.indexY - 1].type == Pawn.Color.A1)) {
				permission = true;
				killEnemy = true;
				enemyX = item.indexX - 1;
				enemyY = item.indexY - 1;
				if(isPossibleToKill(game, item, false, biasX, biasY)) nextTour = true;
			}
			
			if((biasX == item.indexX + 2 && biasY == item.indexY - 2)
					&& game.GampePlayPawns[item.indexX + 1][item.indexY - 1] != null
					&& (game.GampePlayPawns[item.indexX + 1][item.indexY - 1].type == Pawn.Color.A 
					|| game.GampePlayPawns[item.indexX + 1][item.indexY - 1].type == Pawn.Color.A1)) {
				permission = true;
				killEnemy = true;
				enemyX = item.indexX + 1;
				enemyY = item.indexY - 1;
				if(isPossibleToKill(game, item, false, biasX, biasY)) nextTour = true;
			}
		}
		
		if(!stop && !permission && (item.type == Pawn.Color.A1 || item.type == Pawn.Color.B1)) {
			if(Math.abs(biasX - item.indexX) == Math.abs(biasY - item.indexY)) {

				int x, y;
				int countEnemy45deg = 0;
				int countEnemy135deg = 0;
				int countEnemy225deg = 0;
				int countEnemy315deg = 0;
				int countEnemyToKill = 0;
				int direction = 0;

				for(int i = 1; i < 8; i++) {
					// 45 deg
					x = item.indexX + i;
					y = item.indexY - i;
					if(x > 0 && x < 7 && y > 0 && y < 7) {
						if(item.type == Pawn.Color.A1) {
							if(game.GampePlayPawns[x][y] != null
									&& (game.GampePlayPawns[x][y].type == Pawn.Color.B 
										|| game.GampePlayPawns[x][y].type == Pawn.Color.B1)
									&& game.GampePlayPawns[x + 1][y - 1] == null
									&& game.GampePlayPawns[x - 1][y + 1] == null) {
								countEnemy45deg++;
							}
						} else {
							if(game.GampePlayPawns[x][y] != null
									&& (game.GampePlayPawns[x][y].type == Pawn.Color.A 
										|| game.GampePlayPawns[x][y].type == Pawn.Color.A1)
									&& game.GampePlayPawns[x + 1][y - 1] == null
									&& game.GampePlayPawns[x - 1][y + 1] == null) {
								countEnemy45deg++;
							}
						}
					}
					if(x == biasX && y == biasY) {
						direction = 1;
						countEnemyToKill = countEnemy45deg;
					}
					
					// 135 deg
					x = item.indexX + i;
					y = item.indexY + i;
					if(x > 0 && x < 7 && y > 0 && y < 7) {
						if(item.type == Pawn.Color.A1) {
							if(game.GampePlayPawns[x][y] != null
									&& (game.GampePlayPawns[x][y].type == Pawn.Color.B 
										|| game.GampePlayPawns[x][y].type == Pawn.Color.B1)
									&& game.GampePlayPawns[x + 1][y + 1] == null
									&& game.GampePlayPawns[x - 1][y - 1] == null) {
								countEnemy135deg++;
							}
						} else {
							if(game.GampePlayPawns[x][y] != null
									&& (game.GampePlayPawns[x][y].type == Pawn.Color.A 
										|| game.GampePlayPawns[x][y].type == Pawn.Color.A1)
									&& game.GampePlayPawns[x + 1][y + 1] == null
									&& game.GampePlayPawns[x - 1][y - 1] == null) {
								countEnemy135deg++;
							}
						}
					}
					if(x == biasX && y == biasY) {
						direction = 2;
						countEnemyToKill = countEnemy135deg;
					}
					
					// 225 deg
					x = item.indexX - i;
					y = item.indexY + i;
					if(x > 0 && x < 7 && y > 0 && y < 7) {
						if(item.type == Pawn.Color.A1) {
							if(game.GampePlayPawns[x][y] != null
									&& (game.GampePlayPawns[x][y].type == Pawn.Color.B 
										|| game.GampePlayPawns[x][y].type == Pawn.Color.B1)
									&& game.GampePlayPawns[x - 1][y + 1] == null
									&& game.GampePlayPawns[x + 1][y - 1] == null){
								countEnemy225deg++;
							}
						} else {
							if(game.GampePlayPawns[x][y] != null
									&& (game.GampePlayPawns[x][y].type == Pawn.Color.A 
										|| game.GampePlayPawns[x][y].type == Pawn.Color.A1)
									&& game.GampePlayPawns[x - 1][y + 1] == null
									&& game.GampePlayPawns[x + 1][y - 1] == null){
								countEnemy225deg++;
							}
						}
					}
					if(x == biasX && y == biasY) {
						direction = 3;
						countEnemyToKill = countEnemy225deg;
					}
					
					// 315 deg
					x = item.indexX - i;
					y = item.indexY - i;
					if(x > 0 && x < 7 && y > 0 && y < 7) {
						if(item.type == Pawn.Color.A1) {
							if(game.GampePlayPawns[x][y] != null
									&& (game.GampePlayPawns[x][y].type == Pawn.Color.B 
										|| game.GampePlayPawns[x][y].type == Pawn.Color.B1)
									&& game.GampePlayPawns[x - 1][y - 1] == null
									&& game.GampePlayPawns[x + 1][y + 1] == null){
								countEnemy315deg++; 
							}
						} else {
							if(game.GampePlayPawns[x][y] != null
									&& (game.GampePlayPawns[x][y].type == Pawn.Color.A 
										|| game.GampePlayPawns[x][y].type == Pawn.Color.A1)
									&& game.GampePlayPawns[x - 1][y - 1] == null
									&& game.GampePlayPawns[x + 1][y + 1] == null){
								countEnemy315deg++; 
							}
						}
					}
					if(x == biasX && y == biasY) {
						direction = 4;
						countEnemyToKill = countEnemy315deg;
					}
					
				}
				
				// 45 deg
				x = item.indexX + 1;
				y = item.indexY - 1;
				if(x > 0 && x < 7 && y > 0 && y < 7) {
					if(item.type == Pawn.Color.A1) {
						if(game.GampePlayPawns[x][y] != null
								&& (game.GampePlayPawns[x][y].type == Pawn.Color.B 
									|| game.GampePlayPawns[x][y].type == Pawn.Color.B1)
								&& game.GampePlayPawns[x + 1][y - 1] == null) {
							countEnemy45deg++;
							if(direction == 1) countEnemyToKill++;
						}
					} else {
						if(game.GampePlayPawns[x][y] != null
								&& (game.GampePlayPawns[x][y].type == Pawn.Color.A 
									|| game.GampePlayPawns[x][y].type == Pawn.Color.A1)
								&& game.GampePlayPawns[x + 1][y - 1] == null) {
							countEnemy45deg++;
							if(direction == 1) countEnemyToKill++;
						}
					}
				}
				
				// 135 deg
				x = item.indexX + 1;
				y = item.indexY + 1;
				if(x > 0 && x < 7 && y > 0 && y < 7) {
					if(item.type == Pawn.Color.A1) {
						if(game.GampePlayPawns[x][y] != null
								&& (game.GampePlayPawns[x][y].type == Pawn.Color.B 
									|| game.GampePlayPawns[x][y].type == Pawn.Color.B1)
								&& game.GampePlayPawns[x + 1][y + 1] == null) {
							countEnemy135deg++;
							if(direction == 2) countEnemyToKill++;
						}
					} else {
						if(game.GampePlayPawns[x][y] != null
								&& (game.GampePlayPawns[x][y].type == Pawn.Color.A 
									|| game.GampePlayPawns[x][y].type == Pawn.Color.A1)
								&& game.GampePlayPawns[x + 1][y + 1] == null) {
							countEnemy135deg++;
							if(direction == 2) countEnemyToKill++;
						}
					}
				}
				
				// 225 deg
				x = item.indexX - 1;
				y = item.indexY + 1;
				if(x > 0 && x < 7 && y > 0 && y < 7) {
					if(item.type == Pawn.Color.A1) {
						if(game.GampePlayPawns[x][y] != null
								&& (game.GampePlayPawns[x][y].type == Pawn.Color.B 
									|| game.GampePlayPawns[x][y].type == Pawn.Color.B1)
								&& game.GampePlayPawns[x - 1][y + 1] == null){
							countEnemy225deg++;
							if(direction == 3) countEnemyToKill++;
						}
					} else {
						if(game.GampePlayPawns[x][y] != null
								&& (game.GampePlayPawns[x][y].type == Pawn.Color.A 
									|| game.GampePlayPawns[x][y].type == Pawn.Color.A1)
								&& game.GampePlayPawns[x - 1][y + 1] == null){
							countEnemy225deg++;
							if(direction == 3) countEnemyToKill++;
						}
					}
				}
				
				// 315 deg
				x = item.indexX - 1;
				y = item.indexY - 1;
				if(x > 0 && x < 7 && y > 0 && y < 7) {
					if(item.type == Pawn.Color.A1) {
						if(game.GampePlayPawns[x][y] != null
								&& (game.GampePlayPawns[x][y].type == Pawn.Color.B 
									|| game.GampePlayPawns[x][y].type == Pawn.Color.B1)
								&& game.GampePlayPawns[x - 1][y - 1] == null){
							countEnemy315deg++;
							if(direction == 4) countEnemyToKill++;
						}
					} else {
						if(game.GampePlayPawns[x][y] != null
								&& (game.GampePlayPawns[x][y].type == Pawn.Color.A 
									|| game.GampePlayPawns[x][y].type == Pawn.Color.A1)
								&& game.GampePlayPawns[x - 1][y - 1] == null){
							countEnemy315deg++;
							if(direction == 4) countEnemyToKill++;
						}
					}
				}
				
				if(countEnemy45deg == 0
					&& countEnemy135deg == 0
					&& countEnemy225deg == 0
					&& countEnemy315deg == 0) {
					permission = true;
				}
				
				if(!permission) {
					switch(direction) {
					case 1:{
						if(countEnemy45deg != 0 && countEnemyToKill == 1) permission = true;
					}break;
					case 2:{
						if(countEnemy135deg != 0 && countEnemyToKill == 1) permission = true;
					}break;
					case 3:{
						if(countEnemy225deg != 0 && countEnemyToKill == 1) permission = true;
					}break;
					case 4:{
						if(countEnemy315deg != 0 && countEnemyToKill == 1) permission = true;
					}break;
					}
				}
				
				if(permission) {			
					x = item.indexX;
					y = item.indexY;
					while((x >= 0 && x <= 7 && y >= 0 && y <= 7)) {
						switch(direction) {
						case 1:
							x++;
							y--;
							break;
						case 2:
							x++;
							y++;
							break;
						case 3:
							x--;
							y++;
							break;
						case 4:
							x--;
							y--;
							break;
						}
						if(item.type == Pawn.Color.A1) {
							if((x >= 0 && x <= 7 && y >= 0 && y <= 7) 
									&& game.GampePlayPawns[x][y] != null
									&& (game.GampePlayPawns[x][y].type == Pawn.Color.B 
										|| game.GampePlayPawns[x][y].type == Pawn.Color.B1)) {
								killEnemy = true;
								enemyX = x;
								enemyY = y;
								if(isPossibleToKill(game, item, false, biasX, biasY)) nextTour = true;
								break;
							}
						} else {
							if((x >= 0 && x <= 7 && y >= 0 && y <= 7) 
									&& game.GampePlayPawns[x][y] != null
									&& (game.GampePlayPawns[x][y].type == Pawn.Color.A 
										|| game.GampePlayPawns[x][y].type == Pawn.Color.A1)) {
								killEnemy = true;
								enemyX = x;
								enemyY = y;
								if(isPossibleToKill(game, item, false, biasX, biasY)) nextTour = true;
								break;
							}
						}
					}
				}
				
				System.out.println("permission: "+permission);
				System.out.println("killEnemy: "+killEnemy);
				System.out.println("countEnemyToKill: "+countEnemyToKill);
				System.out.println("enemy pt: "+enemyX+","+enemyY);
				System.out.println("type: "+item.type);
				System.out.println("direction: "+direction);
				System.out.println("d: "+countEnemy45deg);
				System.out.println("d: "+countEnemy135deg);
				System.out.println("d: "+countEnemy225deg);
				System.out.println("d: "+countEnemy315deg);
				
			}
		}
		
		if((mustKill || game.attackers != null) && !killEnemy) permission = false;
		
		if(!stop && permission) {	
			if(biasY == 7) chanceType = true;
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
			if(chanceType && item.type == Pawn.Color.B) {
				item.type = Pawn.Color.B1;
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
				game.GampePlayPawns[enemyX][enemyY].circle.setCenterX(-1000);//TODO
				game.GampePlayPawns[enemyX][enemyY] = null;
			}

			if(!nextTour) {				
				if(game.tourIs == Pawn.Color.A) {
					game.tourIs = Pawn.Color.B;
					tourText.setText("B");
				} else {
					game.tourIs = Pawn.Color.A;
					tourText.setText("A");
				}
				game.attackers = checkAllToPossibleKill(game);
			} else {
				game.attackers = null;
			}
			
		} else {
			double ptX = item.indexX * itemSize + space;
			double ptY = item.indexY * itemSize + space;
			
			item.circle.setCenterX(ptX);
			item.circle.setCenterY(ptY);
			item.x = ptX;
			item.y = ptY;
		}	
		
		if(gameOver(game)) {
			endGame(game);
		}
	}
	
	private boolean checkPermissionToMove(Game game, Pawn item) {
		if(game.tourIs == Pawn.Color.A && item.type != Pawn.Color.A && item.type != Pawn.Color.A1) return false;
		if(game.tourIs == Pawn.Color.B && item.type != Pawn.Color.B && item.type != Pawn.Color.B1) return false;
		if(game.mustMove && game.attackers == null && (item.indexX != game.mustMovePt.x || item.indexY != game.mustMovePt.y)) return false;
		if(game.attackers != null) {
			boolean isInList = false;
			for(Pawn pt : game.attackers) {
				if(pt == item) {
					isInList = true;
					break;
				}
			}
			if(!isInList) return false;
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
