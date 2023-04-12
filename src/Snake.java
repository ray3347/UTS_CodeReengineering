import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;

public class Snake extends JFrame {
	public Snake() {
        add(new Panel(new Board(600, 600, 20)));
        setResizable(false);
        pack();
        setTitle("Snake Game");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame ex = new Snake();
            ex.setVisible(true);
        });
    }
}
