package com.hunglp.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ShapeDemo implements ActionListener {
	JFrame frame;
	Graphics gc;
	Graphics2D g2d;
	JPanel defaultPanel ;
	

	public static void main(String[] args) {
		new ShapeDemo();
	}

	public ShapeDemo() {

		frame = new JFrame("Test draw shape");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setPreferredSize(new Dimension(800,800));

		//Container container = frame.getContentPane();
/*
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		//container.add(mainPanel);
*/
		

		/*JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new FlowLayout());
		mainPanel.add(btnPanel);
		btnPanel.add(btnLine);
		btnPanel.add(btnCircle);

		JPanel drawPanel = new JPanel();
		drawPanel.setLayout(new BorderLayout());
		drawPanel.setBackground(Color.WHITE);
		drawPanel.setPreferredSize(new Dimension(600, 600));*/
		
		
		/*JPanel btnPanel = new JPanel();
		JPanel drawPanel = new JPanel();
		
		btnPanel.setLayout(new FlowLayout());
		drawPanel.setLayout(new BorderLayout());
		
		JButton btnCircle = new JButton("Circle");
		JButton btnLine = new JButton("Line");
		
		btnPanel.add(btnCircle);
		btnPanel.add(btnLine);
		
		frame.add(btnPanel,BorderLayout.PAGE_START);
		frame.add(drawPanel,BorderLayout.PAGE_END);*/	
		 
		
		
		/*
		btnCircle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.add(new MyCircle());
			}
		});

		btnLine.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.add(new MyLine());
			}
		});
		*/
		
		
		
		
		 //frame.add(new MyLine());
		 //frame.add(new MyCircle());
		 //frame.add(new MyArc());
		
		
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		Menu menu = new Menu("Shape", true);
		menu.add("Line");
		menu.add("Circle");
		menu.addActionListener(this);

		MenuBar mb = new MenuBar();
		mb.add(menu);
		frame.setMenuBar(mb);

		gc = frame.getGraphics();
		g2d = (Graphics2D) frame.getGraphics();
	}

	public class MyCircle extends JPanel {

		private Point clickPoint;
		private Shape shape;
		private Rectangle box;
		

		public MyCircle() {
			MouseAdapter ma = new MouseAdapter() {

				@Override
				public void mouseDragged(MouseEvent e) {
					int minX = Math.min(e.getX(), clickPoint.x);
					int minY = Math.min(e.getY(), clickPoint.y);
					int maxX = Math.max(e.getX(), clickPoint.x);
					int maxY = Math.max(e.getY(), clickPoint.y);

					box = new Rectangle(minX, minY, maxX - minX, maxY - minY);
					int size = Math.min(maxX - minX, maxY - minY);
					if (minX < clickPoint.x) {
						minX = clickPoint.x - size;
					}
					if (minY < clickPoint.y) {
						minY = clickPoint.y - size;
					}

					shape = new Ellipse2D.Double(minX, minY, size, size);
					repaint();
				}

				@Override
				public void mousePressed(MouseEvent e) {
					clickPoint = new Point(e.getPoint());
				}

			};
			addMouseListener(ma);
			addMouseMotionListener(ma);
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(600, 600);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (shape != null) {
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.setColor(new Color(0, 0, 255, 64));
				// g2d.fill(shape);
				g2d.setColor(Color.BLUE);
				g2d.draw(shape);
				// g2d.draw(box);
				g2d.dispose();
			}
		}
	}

	public class MyLine extends JPanel {
		private ColoredLine[] lines = new ColoredLine[100];
		private int lineCount;
		int startX, startY;
		int prevX, prevY;
		boolean dragging = false;

		public MyLine() {
			setBackground(Color.gray);
			MouseAdapter ma = new MouseAdapter() {

				@Override
				public void mouseDragged(MouseEvent e) {
					if (!dragging)
						return;
					gc.drawLine(startX, startY, prevX, prevY);
					prevX = e.getX();
					prevY = e.getY();
					gc.drawLine(startX, startY, prevX, prevY);
				}

				@Override
				public void mousePressed(MouseEvent e) {
					startX = e.getX();
					startY = e.getY();
					prevX = startX;
					prevY = startY;
					dragging = true;
					gc = (Graphics2D) frame.getGraphics(); // Get a graphics
															// context for
															// use while
					// drawing
					gc.setColor(Color.black);
					gc.setXORMode(frame.getBackground());
					gc.drawLine(startX, startY, prevX, prevY);
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					if (!dragging)
						return;
					gc.drawLine(startX, startY, prevX, prevY); // Erase the
																// previous line
					int endX = e.getX();
					int endY = e.getY();
					gc.setPaintMode();
					gc.drawLine(startX, startY, endX, endY); // Draw new line
					gc.dispose(); // draw operation is over

					if (lineCount < lines.length) { // Add the line to the arr
						lines[lineCount] = new ColoredLine();
						lines[lineCount].x1 = startX;
						lines[lineCount].y1 = startY;
						lines[lineCount].x2 = endX;
						lines[lineCount].y2 = endY;

						lineCount++;
					}
				}
			};

			addMouseListener(ma);
			addMouseMotionListener(ma);
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(600, 600);
		}

		@Override
		protected void paintComponent(Graphics g) {
			for (int i = 0; i < lineCount; i++) {
				int c = lines[i].colorIndex;
				g.setColor(Color.BLACK);
				g.drawLine(lines[i].x1, lines[i].y1, lines[i].x2, lines[i].y2);
			}
		}

	}

	public class MyArc extends JPanel {

		private boolean dragging = false;

		private Point anchor;
		private Point target;

		private Shape fakePath;

		private List<Shape> shapes = new ArrayList<>(25);

		public MyArc() {
			addMouseMotionListener(new MouseAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
					dragging = true;
					target = new Point(e.getPoint());
					repaint();
				}

				@Override
				public void mouseMoved(MouseEvent e) {
					if (target != null && anchor != null) {
						fakePath = makePath(anchor, e.getPoint(), target);
						repaint();
					}
				}
			});

			addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (anchor == null) {
						target = null;
						anchor = new Point(e.getPoint());
					}
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					dragging = false;
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					if (anchor != null && target != null) {
						fakePath = null;

						shapes.add(makePath(anchor, e.getPoint(), target));

						anchor = null;
						target = null;

						repaint();
					}
				}
			});
		}

		protected Path2D makePath(Point p1, Point p2, Point p3) {
			Path2D p = new GeneralPath();
			p.moveTo(p1.getX(), p1.getY());
			p.curveTo(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY());

			return p;
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(600,600);
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
					RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
			g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
			g2d.setColor(Color.BLACK);
			for (Shape shape : shapes) {
				g2d.draw(shape);
			}
			if (anchor != null && target != null) {
				g2d.setColor(Color.GREEN);
				g2d.draw(new Line2D.Double(anchor, target));
			}
			if (fakePath != null) {
				g2d.setColor(Color.BLUE);
				g2d.draw(fakePath);
			}
			g2d.dispose();
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		  String command = e.getActionCommand();
	      if (command.equals("Line")) {
	    	  //defaultPanel.setBackground(Color.BLACK);
	    	  MyLine line = new MyLine();
	    	  frame.add(line);
	    	
	      }
	      else if (command.equals("Circle"))
	          frame.add(new MyCircle());
		
	}
}