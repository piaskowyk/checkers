package application;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Render {
	
	Scene view;
	
	public void drawPlayGround() {
		Canvas ground = (Canvas) view.lookup("#ground");
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
		
		Circle circle = (Circle) view.lookup("#circle_tmp");
		circle.setTranslateX(circle.getCenterY() + 100);
	}
	
	public void setView(Scene scene) {
		this.view = scene;
	}
	
}
