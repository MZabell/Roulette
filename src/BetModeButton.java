import javax.swing.*;

public class BetModeButton extends JButton {

    public int getMode() {
        return mode;
    }

    private int mode;
    public BetModeButton(String text, int mode, int x, int y, int w, int h) {
        this.mode = mode;
        setText(text);
        setBounds(x, y, w, h);
    }
}
