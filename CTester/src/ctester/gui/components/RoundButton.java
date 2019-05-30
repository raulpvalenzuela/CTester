package ctester.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import javax.swing.JButton;

/**
 *
 * @author dma@logossmartcard.com
 */
public class RoundButton extends JButton
{ 
    // Hit detection.
    private Shape shape;
    
    public RoundButton() 
    {
        super();
 
        initComponents();
    }
    
    private void initComponents()
    {
        setBackground(new Color(94, 237, 181));
        setFocusable(false);
        
        // These statements enlarge the button so that it 
        // becomes a circle rather than an oval.
        Dimension size = new Dimension(48, 48);
        size.width = size.height = Math.max(size.width, size.height);
        setPreferredSize(size);

        // This call causes the JButton not to paint the background.
        // This allows us to paint a round background.
        setContentAreaFilled(false);
    }
 
    @Override
    protected void paintComponent(Graphics g) 
    {
      if (getModel().isArmed()) 
      {
        g.setColor(Color.gray);
      } 
      else 
      {
        g.setColor(getBackground());
      }
      
      g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);

      super.paintComponent(g);
    }
 
    @Override
    protected void paintBorder(Graphics g) 
    {
      g.setColor(Color.darkGray);
      g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
    }
 
    @Override
    public boolean contains(int x, int y) 
    {
      // If the button has changed size,  make a new shape object.
      if (shape == null || !shape.getBounds().equals(getBounds())) 
      {
        shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
      }
      
      return shape.contains(x, y);
    }
}
