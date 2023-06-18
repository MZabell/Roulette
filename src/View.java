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
import java.util.ArrayList;

public class View extends JPanel {

    BufferedImage wheelImg, backgroundImg;
    AffineTransformOp op;
    AffineTransform transform;
    ArrayList<JButton> addButtons;

    ArrayList<JButton> betButtons;
    ArrayList<JTextArea> textAreas;

    ArrayList<JLabel> playerChipLabels;
    PopupFactory pf;
    BettingTable bettingTable;

    public View() {
        setVisible(true);
        setPreferredSize(new Dimension(1920, 1080));
        setLayout(null);

        addButtons = new ArrayList<>();
        betButtons = new ArrayList<>();
        textAreas = new ArrayList<>();
        playerChipLabels = new ArrayList<>();

        pf = PopupFactory.getSharedInstance();

        int x = 300, y = 100, w = 100, h = 50;
        for (int i = 0; i < 4; i++) {
            JButton button = new JButton("Add Player");
            button.setBounds(x, y, w, h);
            addButtons.add(button);

            y += 800;

            if (i == 1) {
                x = 1500;
                y = 100;
            }
        }

        for (JButton b : addButtons) {
            b.addActionListener(e -> {
                String playerName = JOptionPane.showInputDialog("Enter Player Name:");
                JTextArea textArea = new JTextArea(playerName + "'s History");
                textArea.setLocation(b.getLocation());
                textArea.setSize(200,200);
                textArea.setEditable(false);
                textArea.setCaretColor(textArea.getBackground());
                textArea.setFont(new Font("Robotto", Font.BOLD, 15));

                if (!textArea.getText().contains("null")) {
                    textAreas.add(textArea);
                    b.setVisible(false);
                    add(textArea);
                }

                JButton button = new JButton("Bet");
                button.setBounds(b.getX() + 50, b.getY() + 220, 100, 50);
                betButtons.add(button);
                add(button);

                for (JButton bb : betButtons) {
                    bb.addActionListener(e1 -> {
                        JFrame popupFrame = new JFrame("Place your bet");
                        bettingTable = new BettingTable();
                        popupFrame.add(bettingTable);
                        popupFrame.pack();
                        popupFrame.setVisible(true);

                        try {
                            Popup popup = pf.getPopup(null, popupFrame, 0, 0);
                            popup.show();
                        } catch (IllegalArgumentException ignored) {}
                        popupFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    });
                }

                JLabel label = new JLabel("Credit: 0");
                label.setFont(new Font("Robotto", Font.BOLD, 15));
                label.setForeground(Color.WHITE);
                label.setBounds(textArea.getX() + 50, textArea.getY() - 50, 200, 50);
                add(label);
            });
            add(b);
        }

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