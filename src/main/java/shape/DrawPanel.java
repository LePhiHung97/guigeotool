package shape;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DrawPanel extends JFrame {
	 private DrawObjects panel = new DrawObjects();
	 private JPanel BPanel = new JPanel();
	 private JFrame window = new JFrame();

	 //constructor
	 DrawPanel(){
	    buildGUI(); 
	 }

	 void buildGUI(){
	     window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     window.setLayout(new GridLayout(2,2));
	     window.add(panel);
	     window.add(BPanel);
	     BPanel.setBackground(Color.blue);

	      //define buttons and add to panel
	      JButton rect = new JButton("Rect");
	      JButton oval = new JButton("Oval");
	      BPanel.add(rect);
	      BPanel.add(oval);

	      rect.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent e)
	              {
	                  panel.setType(1);
	              }
	      });

	      oval.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent e)
	              {
	              panel.setType(2);
	              }
	          }); 

	      window.setVisible(true);
	      window.setSize(1024, 800);     
	}

	 public static void main(String[] args)
	  {
	     //create this object
	     new DrawPanel();
	  }
}
