import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class View extends JPanel {

    BufferedImage wheelImg, backgroundImg;
    AffineTransformOp op;
    AffineTransform transform;

    public View() {
        setVisible(true);
        setPreferredSize(new Dimension(1920, 1080));

        try {
            wheelImg = ImageIO.read(new File("resources/wheel.png"));
            backgroundImg = ImageIO.read(new File("resources/background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        transform(0);
    }

    public void transform(double i) {
        transform = AffineTransform.getRotateInstance(i, wheelImg.getWidth() / 2, wheelImg.getHeight() / 2);
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

            g2d.drawImage(backgroundImg, 0, 0, (int) getPreferredSize().getWidth(), (int) getPreferredSize().getHeight(), null);
            g2d.drawImage(op.filter(wheelImg, null), (int) getPreferredSize().getWidth() / 2 - wheelImg.getWidth() / 2, (int) getPreferredSize().getHeight() / 2 - wheelImg.getHeight() / 2, null);
            g2d.setStroke(new BasicStroke(5));
            g2d.setColor(Color.ORANGE);
            g2d.drawLine((int) getPreferredSize().getWidth() / 2, (int) getPreferredSize().getHeight() / 2 - wheelImg.getHeight() / 2 - 10 , (int) getPreferredSize().getWidth() / 2, (int) getPreferredSize().getHeight() / 2 - wheelImg.getHeight() / 2 + 50);
    }
}