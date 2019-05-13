package com.hunglp.awt;
import java.awt.Graphics;
public abstract class Shapes {
public abstract void draw(Graphics g);

}

class Line extends Shapes{
	private int lineCount ;
	int startX, startY;
	int prevX, prevY;
	boolean dragging = false;
	@Override
	public void draw(Graphics g) {
		
		
	}
	
}

class Circle extends Shapes{
	
	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		
	}
	
}
