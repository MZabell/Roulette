import javax.swing.*;
import javax.swing.Timer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class Roulette implements SerialConnectionHandler.SerialListener{

    private View view;

    private boolean isAccel = true, isDeaccel = false, isHit = false, isStringsLoad = false;

    Timer timer, timer1, timer2;

    Integer[] list = {0,26,3,35,12,28,7,29,18,22,9,31,14,20,1,33,16,24,5,10,23,8,30,11,36,13,27,6,34,17,25,2,21,4,19,15,32};

    int rng;
    private double i = 0.01, angle = 0.01, mul2 = Math.toRadians(360), factor = mul2;

    public SerialConnectionHandler getSerialConn() {
        return serialConn;
    }

    private SerialConnectionHandler serialConn;

    Integer[] credList;

    Integer[] temp = new Integer[4];

    ArrayList<ArrayBlockingQueue<String>> spinHistory;

    String[] textAreaStrings;

    long startTime;
    public Roulette() {
        this.view = new View(this);
        Frame frame = new Frame();
        frame.getContentPane().add(view);
        frame.pack();

        //Random random = new Random();
        //rng = random.nextInt(36);
        //System.out.println(rng);

        credList = new Integer[4];
        textAreaStrings = new String[4];
        spinHistory =  new ArrayList<>();
        for (int k = 0; k < 4; k++) {
            spinHistory.add(new ArrayBlockingQueue<String>(10) {
            });
        }
        serialConn = new SerialConnectionHandler(this);

        /*serialConn.read();*/

        /*System.arraycopy(names.getBytes(), 0, data, 0, 24);
        System.arraycopy(bets,);*/

            /*Scanner scanner = new Scanner(System.in);
            byte[] data;
            String s;
            while (true) {
                s = scanner.nextLine() + "\n";
                data = new byte[s.length()];
                for (int j = 0; j < s.length(); j++) {
                    data[j] = (byte) s.charAt(j);
                }
                serialConn.write(data);
            }*/
    }

    private void spin() {

        double stopAngle = Math.toRadians((360.0 / 37.0) * (Arrays.asList(list).indexOf(rng)));

        if (isAccel) {
            i *= 1.08;
            //angle *= 1.08;
            //System.out.println("Hello1");
        } else if (!isDeaccel){
                i *= 1.04;
                //angle *= 1.04;
                //System.out.println("Hello2");
            } else if (!isHit) {
                i += factor;
                factor *= 0.98;
                } else {
                    mul2 *= 0.98;
                    i += mul2;
                }
                //angle /= 1.04;
                //System.out.println("Hello3");

            angle = i;

        if (angle >= Math.toRadians(360)) {
            angle = i % Math.toRadians(360);
        }

        view.transform(angle);

        if ((Math.abs(angle - stopAngle) < 0.1) && isDeaccel) {
            //System.out.println("Hit!");
            isHit = true;
        }

        if (mul2 < 0.0001) {
            System.out.println((System.currentTimeMillis() - startTime) / 1000.0);
            timer.stop();
            timer1.stop();
            timer2.stop();
            int j = 0;
            for (JLabel label : view.getPlayerChipLabels()) {
                label.setText("Credit: " + temp[j]);
                System.out.println(temp[j]);
                if (j == 1) {
                    j = 3;
                }
                if (j == 2) {
                    j = 1;
                }
                if (j == 0) {
                    j = 2;
                }
            }
            for (ArrayBlockingQueue<String> queue : spinHistory) {
                try {
                    queue.add("Tal:    " + rng + "    " + "Gevinst:    " + credList[spinHistory.indexOf(queue)] + "\n");
                } catch (IllegalStateException e) {
                    queue.poll();
                    queue.add("Gevinst:    " + credList[spinHistory.indexOf(queue)] + "\n");
                }

            }
            j = 0;
            int k = 0;
            for (JTextArea ta : view.getTextAreas()) {
                ta.setText(textAreaStrings[k] + spinHistory.get(j));
                if (j == 2) {
                    j--;
                } else {
                    j += 2;
                }
                k++;
            }
            view.repaint();
        }

    }
    @Override
    public void onValueRead(short s, int i) {
            System.out.println("Abedula: " + s);
            if (!isStringsLoad) {
                for (int k = 0; k < view.getTextAreas().size(); k++) {
                    textAreaStrings[k] = view.getTextAreas().get(k).getText();
                }
                isStringsLoad = true;
            }
            if (i == 2) {
                rng = s;
                isAccel = true;
                isDeaccel = false;
                timer1 = new Timer(1000, e1 -> {
                    isAccel = false;
                });
                timer1.start();

                startTime = System.currentTimeMillis();
                timer = new Timer(20, e -> {
                    spin();
                });
                timer.start();

                timer2 = new Timer(2000, e3 -> {
                    isDeaccel = true;
                });
                timer2.start();
            }
            if (i > 2 && i < 7) {
                credList[i - 3] = (int) s;
                if (i == 6) {
                    for (int k = 0; k < 4; k++) {
                            int deltaWager = 0;
                            for (int d = 0; d < 4; d++) {
                                deltaWager += view.getWagers2()[d + k * 4];
                            }
                            temp[k] = view.getPlayerChips()[k] + credList[k] - deltaWager;
                    }
                    view.setPlayerChips(temp);
                    isHit = false;
                    this.i = 0.01;
                    angle = 0.01;
                    mul2 = Math.toRadians(360);
                    factor = mul2;
                }
            }
    }
}
