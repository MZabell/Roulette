import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BettingTable extends JPanel {

    BufferedImage tableImg, chipImg;

    ArrayList<ArrayList<BetButton>> betButtons;

    ArrayList<Point> placedBets;

    byte[] betVals;

    short[] wagers;
    JTextField wagerField;
    int arg2 = 0, mode = 0, betNumber = 0;
    public BettingTable(JFrame frame, View view, int playerNum) {
        setLayout(null);
        setVisible(true);

        betButtons = new ArrayList<>();

        ArrayList<BetButton> straightUpList = new ArrayList<>();
        ArrayList<BetButton> bottomBarList = new ArrayList<>();
        ArrayList<BetButton> dusinList = new ArrayList<>();
        ArrayList<BetButton> colList = new ArrayList<>();
        ArrayList<BetButton> streetList = new ArrayList<>();
        ArrayList<BetButton> lineList = new ArrayList<>();
        ArrayList<BetModeButton> modeButtons = new ArrayList<>();

        placedBets = new ArrayList<>();

        betVals = new byte[4];
        wagers = new short[4];

        int x = 130, y = 180;

        for (int i = 0; i <= 36; i++) {

            if (i == 0) {
                x = 65;
                y = 97;
            }

            BetButton button = new BetButton(i, x, y, 50, 77);
            button.addActionListener(e -> {
                //System.out.println(button.getBetVal());
                betVals[betNumber] = (byte) button.getBetVal();
                wagers[betNumber] = Short.parseShort(wagerField.getText());
                betNumber++;
                if (betNumber == 4) {
                    byte[] temp = view.getBets();
                    short[] temp2 = view.getWagers();
                    for (int j = 0; j < betVals.length; j++) {
                            temp[j + playerNum * 4] = betVals[j];
                            temp2[j + playerNum * 4] = wagers[j];
                    }
                    view.setBets(temp);
                    view.setWagers(temp2);
                    frame.dispose();
                }
                button.setVisible(false);
                placedBets.add(button.getLocation());
                repaint();
            });
            straightUpList.add(button);
            add(button);

            if (i == 0) {
                x = 68;
                y = 180;
            }

            if (i % 3 == 0) {
                y = 180;
                x += button.getWidth() + 13;
            } else {
                y -= button.getHeight() + 7;
            }
        }

        betButtons.add(straightUpList);

        for (int i = 40; i <= 45; i++) {
            switch (i) {
                case 40:
                    x = 380;
                    y = 343;
                    break;
                case 41:
                    x += 126;
                    break;
                case 42:
                    x = 254;
                    break;
                case 43:
                    x = 632;
                    break;
                case 44:
                    x = 128;
                    break;
                case 45:
                    x = 757;
                    break;
            }
            BetButton button = new BetButton(i, x, y, 118, 55);
            button.addActionListener(e -> {
                //System.out.println(button.getBetVal());
                betVals[betNumber] = (byte) button.getBetVal();
                wagers[betNumber] = Short.parseShort(wagerField.getText());
                betNumber++;
                if (betNumber == 4) {
                    byte[] temp = view.getBets();
                    short[] temp2 = view.getWagers();
                    for (int j = 0; j < betVals.length; j++) {
                        temp[j + playerNum * 4] = betVals[j];
                        temp2[j + playerNum * 4] = wagers[j];
                    }
                    view.setBets(temp);
                    view.setWagers(temp2);
                    frame.dispose();
                }
                button.setVisible(false);
                placedBets.add(button.getLocation());
                repaint();
            });
            bottomBarList.add(button);
            add(button);
        }

        betButtons.add(bottomBarList);

        for (int i = 46; i <= 48; i++) {
            if (i == 46) {
                x = 128;
                y = 266;
            } else {
                x += 252;
            }
            BetButton button = new BetButton(i, x, y, 244, 67);
            button.addActionListener(e -> {
                //System.out.println(button.getBetVal());
                betVals[betNumber] = (byte) button.getBetVal();
                wagers[betNumber] = Short.parseShort(wagerField.getText());
                betNumber++;
                if (betNumber == 4) {
                    byte[] temp = view.getBets();
                    short[] temp2 = view.getWagers();
                    for (int j = 0; j < betVals.length; j++) {
                        temp[j + playerNum * 4] = betVals[j];
                        temp2[j + playerNum * 4] = wagers[j];
                    }
                    view.setBets(temp);
                    view.setWagers(temp2);
                    frame.dispose();
                }
                button.setVisible(false);
                placedBets.add(button.getLocation());
                repaint();
            });
            dusinList.add(button);
            add(button);
        }

        betButtons.add(dusinList);

        for (int i = 49; i <= 51; i++) {
            if (i == 49) {
                x = 885;
                y = 180;
            } else {
                y -= 84;
            }
            BetButton button = new BetButton(i, x, y, 50, 77);
            button.addActionListener(e -> {
                //System.out.println(button.getBetVal());
                betVals[betNumber] = (byte) button.getBetVal();
                wagers[betNumber] = Short.parseShort(wagerField.getText());
                betNumber++;
                if (betNumber == 4) {
                    byte[] temp = view.getBets();
                    short[] temp2 = view.getWagers();
                    for (int j = 0; j < betVals.length; j++) {
                        temp[j + playerNum * 4] = betVals[j];
                        temp2[j + playerNum * 4] = wagers[j];
                    }
                    view.setBets(temp);
                    view.setWagers(temp2);
                    frame.dispose();
                }
                button.setVisible(false);
                placedBets.add(button.getLocation());
                repaint();
            });
            colList.add(button);
            add(button);
        }

        betButtons.add(colList);

        BetModeButton modeButton = null;

        for (int i = 0; i < 6; i++) {
            if (i == 0) {
                x = 5;
                y = 270;
            } else {
                y += 23;
            }
            switch (i) {
                case 0:
                    modeButton = new BetModeButton("Straight Up", i, x, y, 115, 20);
                    break;
                case 1:
                    modeButton = new BetModeButton("Street", i, x, y, 115, 20);
                    break;
                case 2:
                    modeButton = new BetModeButton("Line", i, x, y, 115, 20);
                    break;
                case 3:
                    modeButton = new BetModeButton("Corner", i, x, y, 115, 20);
                    break;
                case 4:
                    modeButton = new BetModeButton("Row Split", i, x, y, 115, 20);
                    break;
                case 5:
                    modeButton = new BetModeButton("Column Split", i, x, y, 115, 20);
                    break;    
            }
            int mode2 = modeButton.getMode();
            modeButton.addActionListener(e -> {
                mode = mode2;
            });
            modeButtons.add(modeButton);
            add(modeButton);
        }

        wagerField = new JTextField("300");
        wagerField.setBounds(885, 350, 100, 30);
        add(wagerField);

        try {
            tableImg = ImageIO.read(new File("resources/bettingTable.png"));
            chipImg = ImageIO.read(new File("resources/chip.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setPreferredSize(new Dimension(tableImg.getWidth(), tableImg.getHeight()));
    }

    private void clearButtons() {
        for (ArrayList<BetButton> al : betButtons) {
            if (betButtons.indexOf(al) == 0) {
                for (BetButton b : al) {
                    b.setVisible(false);
                }
            }
        }
    }

    private void street(ArrayList<BetButton> streetList) {
        int x = 131, y = 12, w = 50, h = 252;
        for (int i = 1; i <= 12; i++) {
            if (i != 1) {
                x += 63;
            }

            BetButton button = new BetButton(52, x, y, w, h);
            button.setArg2(i);
            button.addActionListener(e -> {
                System.out.println("BetVal: " + button.getBetVal() + " , Arg2: " + button.getArg2());
                placedBets.add(new Point(button.getX(), button.getY() + 210));
                repaint();
            });
            streetList.add(button);
            add(button);
        }
    }

    private void line(ArrayList<BetButton> lineList) {
        int x = 168, y = 235, w = 40, h = 30;
        for (int i = 1; i <= 11; i++) {
            if (i != 1) {
                x += 63;
            }

            BetButton button = new BetButton(53, x, y, w, h);
            button.setArg2(i);
            button.addActionListener(e -> {
                System.out.println("BetVal: " + button.getBetVal() + " , Arg2: " + button.getArg2());
                placedBets.add(new Point(button.getX() - 6, button.getY() - 15));
                repaint();
            });
            lineList.add(button);
            add(button);
        }
    }

    private void corner() {

    }

    private void rSplit() {

    }

    private void cSplit() {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);

        g2d.drawImage(tableImg, null, null);

        for (Point p : placedBets) {
            g2d.drawImage(chipImg, (int) p.getX(), (int) p.getY() + 13, 50, 50, null);
        }
    }
}
