import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class View extends JPanel {

    BufferedImage wheelImg, backgroundImg;
    AffineTransformOp op;
    AffineTransform transform;
    ArrayList<JButton> addButtons;
    ArrayList<JButton> betButtons;

    short[] wagers2;

    public short[] getWagers2() {
        return wagers2;
    }

    public byte[] getBets() {
        return bets;
    }

    public short[] getWagers() {
        return wagers;
    }

    public ArrayList<JTextArea> getTextAreas() {
        return textAreas;
    }

    ArrayList<JTextArea> textAreas;

    public ArrayList<JLabel> getPlayerChipLabels() {
        return playerChipLabels;
    }

    ArrayList<JLabel> playerChipLabels;
    PopupFactory pf;
    BettingTable bettingTable;

    Integer[] playerChips;

    public String[] getNames() {
        return names;
    }

    String[] names;

    Roulette roulette;

    public void setBets(byte[] bets) {
        this.bets = bets;
    }

    public void setARG2(byte[] ARG2) {
        this.ARG2 = ARG2;
    }

    public void setWagers(short[] wagers) {
        this.wagers = wagers;
    }

    byte[] bets = new byte[16];
    byte[] ARG2 = new byte[16];
    short[] wagers = new short[16];

    public Integer[] getPlayerChips() {
        return playerChips;
    }

    public void setPlayerChips(Integer[] playerChips) {
        this.playerChips = playerChips;
    }

    public View(Roulette roulette) {
        this.roulette = roulette;
        setVisible(true);
        setPreferredSize(new Dimension(1920, 1080));
        setLayout(null);

        addButtons = new ArrayList<>();
        betButtons = new ArrayList<>();
        textAreas = new ArrayList<>();
        playerChipLabels = new ArrayList<>();
        playerChips = new Integer[4];
        for (int i = 0; i < 4; i++) {
            playerChips[i] = 5000;
        }
        names = new String[4];

        pf = PopupFactory.getSharedInstance();

        int x = 300, y = 100, w = 100, h = 50;
        for (int i = 0; i < 4; i++) {
            JButton button = new JButton("Add Player");
            button.setBounds(x, y, w, h);
            addButtons.add(button);

            y += 500;

            if (i == 1) {
                x = 1500;
                y = 100;
            }
        }

        for (JButton b : addButtons) {
            b.addActionListener(e -> {
                StringBuilder playerName = new StringBuilder(JOptionPane.showInputDialog("Enter Player Name:"));
                while (playerName.length() < 6) {
                    playerName.append(" ");
                }
                names[addButtons.indexOf(b)] = playerName.toString();
                JTextArea textArea = new JTextArea(playerName + "'s History\n");
                textArea.setLocation(b.getLocation());
                textArea.setSize(200,300);
                textArea.setEditable(false);
                textArea.setCaretColor(textArea.getBackground());
                textArea.setFont(new Font("Robotto", Font.BOLD, 15));

                if (!textArea.getText().contains("null")) {
                    textAreas.add(textArea);
                    b.setVisible(false);
                    add(textArea);
                }

                JButton button = new JButton("Bet");
                button.setBounds(b.getX() + 50, b.getY() + 320, 100, 50);
                betButtons.add(button);
                add(button);

                for (JButton bb : betButtons) {
                    bb.addActionListener(e1 -> {
                        JFrame popupFrame = new JFrame("Place your bet");
                        bettingTable = new BettingTable(popupFrame, this, addButtons.indexOf(b));
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

                JLabel label = new JLabel("Credit: " + playerChips[0]);
                label.setFont(new Font("Robotto", Font.BOLD, 15));
                label.setForeground(Color.WHITE);
                label.setBounds(textArea.getX() + 50, textArea.getY() - 50, 200, 50);
                playerChipLabels.add(label);
                add(label);
                repaint();
            });
            add(b);
        }

        JButton confirmBet = new JButton("Place Bet");
        confirmBet.setBounds(900, 500, 100, 50);
        confirmBet.addActionListener(e -> {

            ByteBuffer bf = ByteBuffer.allocate(88);
            StringBuilder names = new StringBuilder();
            for (String s : this.names) {
                if (s == null || s.isBlank()) {
                    s = "\0\0\0\0\0\0";
                }
                names.append(s);
            }
            //String names = "hello1hello2hello3hello4";
            for (int i = 0; i < names.toString().length(); i++) {
                bf.put(i, (byte) names.toString().charAt(i));
            }

            for (int i = 0; i < bets.length; i++) {
                ARG2[i] = (byte) (0);
            }


            this.wagers2 = wagers;

            for (int i = 0; i < bets.length; i++) {
                bf.put(i + 24, bets[i]);
                bf.put(i + 40, ARG2[i]);
                bf.putShort(i * 2 + 56, wagers[i]);
            }

            roulette.getSerialConn().write(bf.array());
            System.out.println(names);
            System.out.println(Arrays.toString(bets));
            System.out.println(Arrays.toString(ARG2));
            System.out.println(Arrays.toString(wagers));
            System.out.println("WRITE DONE");
            roulette.getSerialConn().read();

        });
        add(confirmBet);

        try {
            wheelImg = ImageIO.read(new File("resources/wheel.png"));
            backgroundImg = ImageIO.read(new File("resources/background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        transform(0);

        for (int i = 0; i < bets.length; i++) {
            bets[i] = (byte) (41);
        }
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