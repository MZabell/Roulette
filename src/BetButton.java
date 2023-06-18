import javax.swing.*;

public class BetButton extends JButton {

    public int getBetVal() {
        return betVal;
    }

    private int betVal;

    public int getArg2() {
        return arg2;
    }

    public void setArg2(int arg2) {
        this.arg2 = arg2;
    }

    private int arg2;
    public BetButton(int betVal, int x, int y, int w, int h) {
        this.betVal = betVal;
        setBounds(x, y, w, h);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
    }
}
