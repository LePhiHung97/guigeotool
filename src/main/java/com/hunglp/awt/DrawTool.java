package com.hunglp.awt;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class DrawTool extends JFrame implements ActionListener{
	public DrawTool(){
		super("Draw Tool");
		addMenu();
		setSize(800,800);
		setVisible(true);
		setLocation(600, 200);
	}
	
	public static void main(String[] args) {
		DrawTool drawTool = new DrawTool();
	}
	
	private void addMenu(){
		Menu shape = new Menu("Shape", true);
		shape.add("Line");
		shape.add("Circle");
		shape.addActionListener(this);

		MenuBar mb = new MenuBar();
		mb.add(shape);
		setMenuBar(mb);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
