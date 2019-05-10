
package com.hunglp.demo;
import java.io.*;

import javax.swing.JOptionPane;

import java.awt.*;
import java.awt.event.*;

public class Line extends Frame implements ActionListener {

   static Line line ;
   public static void main(String[] args) {
     line =  new Line();
      
   }
   SimpleDrawCanvasWithFiles canvas; 

   public Line() {
      super("Simple Draw");

      Menu fileMenu = new Menu("File",true);
      fileMenu.add("New");      
      fileMenu.add("Save...");      
      fileMenu.add("Load...");      
      fileMenu.addSeparator();
      fileMenu.add("Undo");   
      fileMenu.addSeparator();   
      fileMenu.add("Quit");
      fileMenu.addActionListener(this);

      Menu shape = new Menu("Shape",true);
      shape.add("Line");
      shape.add("Polygon");
      shape.add("circle");
      shape.add("CurveFan");
      shape.addActionListener(this);
     
      
      MenuBar mb = new MenuBar();
      mb.add(fileMenu);
      mb.add(shape);
      setMenuBar(mb);
      canvas = new SimpleDrawCanvasWithFiles();
      add(canvas);
     
      
      addWindowListener(
            new WindowAdapter() { 
               public void windowClosing(WindowEvent evt) {
                  dispose();
                  System.exit(0);
               }
            }
        );   
      pack();
      show();
   } 
   
   public void actionPerformed(ActionEvent evt) {
       
      String command = evt.getActionCommand();

      if (command.equals("Quit")) {
          dispose();  
          System.exit(0);
      }
      else if (command.equals("New"))
          canvas.doClear();
      else if (command.equals("Undo"))
          canvas.doUndo();
      else if (command.equals("Save..."))
          canvas.doSaveToFile(this);
      else if(command.equals("Line"))
      {
    	// canvas.setVisible(false);
      }
   }
} 

class ColoredLine { 
   int x1, y1;  
   int x2, y2;  
   int colorIndex; 

}

class SimpleDrawCanvasWithFiles extends Canvas implements MouseListener, MouseMotionListener {
       // A canvas where the user can draw lines in various colors.

   private int currentColorIndex;  // Color that is currently being used for drawing new lines,
                                   // given as an index in the ColoredLine.colorList array.
                                   
   private int currentBackgroundIndex;  // Current background color, given as an index in the
                                        // ColoredLine.colorList array.
                                   
   private ColoredLine[] lines;    // An array to hold all the lines that have been
                                   //        drawn on the canvas.
   private int lineCount;   // The number of lines that are in the array.

   SimpleDrawCanvasWithFiles() {
	  
      setBackground(Color.white);
      currentColorIndex = 0;
      currentBackgroundIndex = 12;
      lines = new ColoredLine[1000];
      addMouseListener(this);
      addMouseMotionListener(this);
   }
   
   void doClear() {
         // Clear all the lines from the picture.
      if (lineCount > 0) {
         lines = new ColoredLine[1000];
         lineCount = 0;
         repaint();
      }
   }

   void doUndo() { 
        // Remove most recently added line from the picture.
      if (lineCount > 0) {
         lineCount--;
         repaint();
      }
   }
   
   void doSaveToFile(Frame parentFrame) {
		FileDialog fd;

		fd = new FileDialog(parentFrame, "Save to File", FileDialog.SAVE);
		fd.show();

		String fileName = fd.getFile();

		if (fileName == null)
			return;

		String directoryName = fd.getDirectory();

		File file = new File(directoryName, fileName);

		PrintWriter out;
		try {
			out = new PrintWriter(new FileWriter(file));
		} catch (IOException e) {
		}
      
      // Write the data for the drawing to the file...

      /*out.println(currentBackgroundIndex);         // The index of the current background color.
      out.println(lineCount);                      // The number of lines in the data array.
      for (int i = 0; i < lineCount; i++) {        // Write the data for each indifvidual line.
         out.print(lines[i].x1);
         out.print(" ");
         out.print(lines[i].y1);
         out.print(" ");
         out.print(lines[i].x2);
         out.print(" ");
         out.print(lines[i].y2);
         out.print(" ");
         out.print(lines[i].colorIndex);
         out.println();
      }
      
      out.close();  // Close the output file.
*/      
      // Note that a PrintWriter never throws an exception. In order to make sure that
      // the date was written successfully, call the PrintWriter's checkError() method.
      // If out.checkError() returns a value of true, then an error occured while writing
      // the data and the user should be informed.
      
      //if (out.checkError())
         //new MessageDialog(parentFrame,"Some error occured while trying to save data to the file.");
      
   } // end doSaveToFile()

  /* public void paint(Graphics g) {
         // Redraw all the lines.
      for (int i = 0; i < lineCount; i++) {
         int c = lines[i].colorIndex;
         g.setColor(Color.BLACK);
         g.drawLine(lines[i].x1,lines[i].y1,lines[i].x2,lines[i].y2);
      }
   }
   */
   public Dimension getPreferredSize() {
      return new Dimension(500,400);
   }
   
 
   int startX, startY;  // When the user presses the mouse button, the   location of the mouse is stored in these variables.
   int prevX, prevY;    // The most recent mouse location; a rubber band line has been drawn from (startX, startY) to (prevX, prevY)                   
   boolean dragging = false;  // For safety, this variable is set to true while a drag operation is in progress.                                                     
   Graphics gc;  // While dragging, gc is a graphics context that can be used to  draw to the canvas.  

   public void mousePressed(MouseEvent evt) {
         // This is called by the system when the user presses the mouse button.
         // Record the location at which the mouse was pressed.  This location
         // is one endpoint of the line that will be drawn when the mouse is
         // released.  This method is part of the MouseLister interface.
      startX = evt.getX();
      startY = evt.getY();
      prevX = startX;
      prevY = startY;
      dragging = true;
      gc = getGraphics();  // Get a graphics context for use while drawing.
      gc.setColor(Color.black);
      gc.setXORMode(getBackground());
      gc.drawLine(startX, startY, prevX, prevY);
   }
   
   public void mouseDragged(MouseEvent evt) {
         // This is called by the system when the user moves the mouse while holding
         // down a mouse button.  The previously drawn rubber band line is erased by
         // drawing it a second time, and a new rubber band line is drawn from the
         // start point to the current mouse location.
      if (!dragging)  // Make sure that the drag operation has been properly started.
         return;
      gc.drawLine(startX,startY,prevX,prevY);  // Erase the previous line.
      prevX = evt.getX();
      prevY = evt.getY();
      gc.drawLine(startX,startY,prevX,prevY);  // Draw the new line.
   }

   public void mouseReleased(MouseEvent evt) {
         // This is called by the system when the user releases the mouse button.
         // The previously drawn rubber band line is erased by drawing it a second
         // time.  Then a permanent line is drawn in the current drawing color,
         // and is added to the array of lines.
      if (!dragging)  // Make sure that the drag operation has been properly started.
         return;
      gc.drawLine(startX,startY,prevX,prevY);  // Erase the previous line.
      int endX = evt.getX();  // Where the mouse was released.
      int endY = evt.getY();
      gc.setPaintMode();
      gc.drawLine(startX, startY, endX, endY);  // Draw the permanent line in regular "paint" mode.
      gc.dispose();  // Free the graphics context, now that the draw operation is over.
      if (lineCount < lines.length) {  // Add the line to the array, if there is room.
         lines[lineCount] = new ColoredLine();
         lines[lineCount].x1 = startX;
         lines[lineCount].y1 = startY;
         lines[lineCount].x2 = endX;
         lines[lineCount].y2 = endY;
         lines[lineCount].colorIndex = currentColorIndex;
         lineCount++;
      }
   }

   public void mouseClicked(MouseEvent evt) { }  
   public void mouseEntered(MouseEvent evt) { }
   public void mouseExited(MouseEvent evt) { }
   public void mouseMoved(MouseEvent evt) { }  

} 