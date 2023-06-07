import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class View extends JPanel {

    BufferedImage img;
    AffineTransformOp op;
    AffineTransform transform;

    double i = 0;
    public View() {
        setVisible(true);
        setPreferredSize(new Dimension(800, 800));

        try {
            img = ImageIO.read(new File("resources/wheel.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        spin();
        Timer timer = new Timer(5, e -> {
            i += 0.10;
            spin();
        });
        timer.start();
    }

    private void spin() {
            transform = AffineTransform.getRotateInstance(i, img.getWidth() / 2, img.getHeight() / 2);
            op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
            repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);

            g2d.drawImage(op.filter(img, null), 100, 100, null);
    }
}