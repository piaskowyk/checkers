package application;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class MainViewController {
	@FXML
	private Button btn;
	@FXML
	private Pane board;
	@FXML
	private Canvas ground;
	
	public void initialize() 
	{
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				((Button)event.getSource()).setText("mleko");
			}
		});
	}
	
	public void drawPlayGround() {
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
		
	public void makePawns(Game game) {
		game.init();
		
		double border = ground.getWidth();
		double itemSize = border/8;
		double space = itemSize/2;
		double R = 10;

		for(int i=0; i<2; i++) {
			for(int k=0; k<8; k++) {
				if(i == 0 && k%2 == 1) {
					game.pawnsA[k].pawn = new MyCircle(R*2);
					game.pawnsA[k].pawn.setFill(Color.DARKBLUE);
					board.getChildren().add(game.pawnsA[k].pawn);
					game.pawnsA[k].pawn.setCenterX(itemSize*k + space);
					game.pawnsA[k].pawn.setCenterY(itemSize*i + space);
					
					game.pawnsB[k].pawn = new MyCircle(R*2);
					game.pawnsB[k].pawn.setFill(Color.BROWN);
					board.getChildren().add(game.pawnsB[k].pawn);
					game.pawnsB[k].pawn.setCenterX(itemSize*k + space);
					game.pawnsB[k].pawn.setCenterY(border - itemSize*2 + space);
				} 
				else if(i == 1 && k%2 == 0) {
					game.pawnsA[k].pawn = new MyCircle(R*2);
					game.pawnsA[k].pawn.setFill(Color.DARKBLUE);
					board.getChildren().add(game.pawnsA[k].pawn);
					game.pawnsA[k].pawn.setCenterX(itemSize*k + space);
					game.pawnsA[k].pawn.setCenterY(itemSize*i + space);
					
					game.pawnsB[k].pawn = new MyCircle(R*2);
					game.pawnsB[k].pawn.setFill(Color.BROWN);
					board.getChildren().add(game.pawnsB[k].pawn);
					game.pawnsB[k].pawn.setCenterX(itemSize*k + space);
					game.pawnsB[k].pawn.setCenterY(border - itemSize + space);
				}
			}
		}
		
		for(Pawn item : game.pawnsA) {			
			item.pawn.setOnMousePressed(new EventHandler<Event>() {
				@Override
				public void handle(Event event)
				{
					PointerInfo a = MouseInfo.getPointerInfo();
					Point b = a.getLocation();
					item.pawn.x = (int) b.getX();
					item.pawn.y = (int) b.getY();
				}
			});
			item.pawn.setOnMouseDragged(new EventHandler<Event>() {
				@Override
				public void handle(Event event)
				{
					PointerInfo a = MouseInfo.getPointerInfo();
					Point b = a.getLocation();
					int x = (int) b.getX();
					int y = (int) b.getY();
					item.pawn.setCenterX(item.pawn.getCenterX() + x - item.pawn.x);
					item.pawn.setCenterY(item.pawn.getCenterY() + y - item.pawn.y);
					item.pawn.x = x;
					item.pawn.y = y;
				}
			});
			item.pawn.setOnMouseDragExited(new EventHandler<Event>() {
				@Override
				public void handle(Event event)
				{
					//sprawdczy mo¿na po³o¿yæ, czy nie sku³, czy nie wygra³, wyrównaj po³o¿enie
				}
			});
		}
		
		for(Pawn item : game.pawnsB) {			
			item.pawn.setOnMousePressed(new EventHandler<Event>() {
				@Override
				public void handle(Event event)
				{
					PointerInfo a = MouseInfo.getPointerInfo();
					Point b = a.getLocation();
					item.pawn.x = (int) b.getX();
					item.pawn.y = (int) b.getY();
				}
			});
			item.pawn.setOnMouseDragged(new EventHandler<Event>() {
				@Override
				public void handle(Event event)
				{
					PointerInfo a = MouseInfo.getPointerInfo();
					Point b = a.getLocation();
					int x = (int) b.getX();
					int y = (int) b.getY();
					item.pawn.setCenterX(item.pawn.getCenterX() + x - item.pawn.x);
					item.pawn.setCenterY(item.pawn.getCenterY() + y - item.pawn.y);
					item.pawn.x=x;
					item.pawn.y=y;
				}
			});
			item.pawn.setOnMouseDragExited(null);
		}
	}
	
	@FXML
	private void clickRafal(ActionEvent e)
	{
		MyCircle circle = new MyCircle(10);
		EventHandler<Event> pionStartMove = new EventHandler<Event>() {
			@Override
			public void handle(Event event)
			{
				PointerInfo a = MouseInfo.getPointerInfo();
				Point b = a.getLocation();
				circle.x = (int) b.getX();
				circle.y = (int) b.getY();
			}
		};
		
		EventHandler<Event> pionMove = new EventHandler<Event>() {
			@Override
			public void handle(Event event)
			{
				PointerInfo a = MouseInfo.getPointerInfo();
				Point b = a.getLocation();
				int x = (int) b.getX();
				int y = (int) b.getY();
				circle.setCenterX(circle.getCenterX()+x-circle.x);
				circle.setCenterY(circle.getCenterY()+y-circle.y);
				circle.x=x;
				circle.y=y;
			}
		};
		
		circle.setOnMousePressed(pionStartMove);
		circle.setOnMouseDragged(pionMove);
		
		board.getChildren().add(circle);
		circle.setCenterX(100);
		circle.setCenterY(100);
	}	
}
