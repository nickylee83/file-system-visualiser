package FileSystemVisualizerV2;
import java.awt.*;
import javax.swing.*;

public class RectTest extends JPanel {
   private static final int RECT_X = 20;
   private static  int RECT_Y = 20;
   private static  int RECT_WIDTH;
   private static final int RECT_HEIGHT = 10;

   public RectTest(int n ){
     RECT_WIDTH = n;
   }

   public RectTest(){
     
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      // draw the rectangle here
      g.setColor(Color.RED);
      g.fillRect(RECT_X, RECT_Y, RECT_WIDTH, RECT_HEIGHT);
      g.drawRect(RECT_X, RECT_Y, RECT_WIDTH, RECT_HEIGHT);
   }
   
   @Override
   public Dimension getPreferredSize() {
      // so that our GUI is big enough
      return new Dimension(RECT_WIDTH + 2 * RECT_X, RECT_HEIGHT + 2 * RECT_Y);
   }
  

   public static void moveY(int n){
     RECT_Y += n;
   }

   // create the GUI explicitly on the Swing event thread
   private static void createAndShowGui() {
    RectTest mainPanel = new RectTest();

      JFrame frame = new JFrame("DrawRect");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(mainPanel);
      frame.pack();
      frame.setLocationByPlatform(true);
      frame.setVisible(true);
   }

   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            createAndShowGui();
         }
      });
   }
}