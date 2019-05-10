package shape;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;

public class DrawObjects extends JPanel{
	public int x1,x2,y1,y2;
	public int type = 1;//default draw type

	public DrawObjects()
	{
	    init();
	}

	public void init(){
	      setBackground(Color.WHITE);
	      addMouseListener(new MouseAdapter()
	       {
	          public void mousePressed(MouseEvent m)
	          {
	               x1 = m.getX();
	               y1 = m.getY();
	               repaint();
	          }
	          public void mouseReleased(MouseEvent m)
	          {
	               x2 = m.getX();
	               y2 = m.getY();
	               repaint();
	          }
	        });

	      addMouseMotionListener(new MouseMotionAdapter()
	        {
	           public void mouseDragged(MouseEvent m)
	              {
	                 x2 = m.getX();
	                 y2 = m.getY();
	                 repaint();
	              }
	          });
	}

	public void setType(int arg){
	    if(arg == 1){
	        type = 1;
	    }else if(arg == 2){
	        type = 2;
	    }
	}

	  public void paintComponent(Graphics g)
	  { 
	      super.paintComponent(g);
	      if(type == 1)
	      {
	          g.drawRect(x1,y1,x2,y2);
	      }
	      else if (type == 2)
	      {
	          g.drawOval(x1,y1,x2,y2);
	      }
	  }
}
