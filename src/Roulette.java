import javax.swing.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Roulette implements SerialConnectionHandler.SerialListener{

    private View view;

    private boolean isAccel = true, isDeaccel = false, isHit = false;

    Timer timer, timer2;

    Integer[] list = {0,26,3,35,12,28,7,29,18,22,9,31,14,20,1,33,16,24,5,10,23,8,30,11,36,13,27,6,34,17,25,2,21,4,19,15,32};

    int rng;
    private double i = 0.01, angle = 0.01, mul2 = Math.toRadians(360), factor = mul2;

    long startTime;
    public Roulette() {
        this.view = new View();
        Frame frame = new Frame();
        frame.getContentPane().add(view);
        frame.pack();

        Random random = new Random();
        rng = random.nextInt(36);
        System.out.println(rng);

        new Timer(1000, e1 -> {
            isAccel = false;
        }).start();

        startTime = System.currentTimeMillis();
        timer = new Timer(20, e -> {
            spin();
        });
        timer.start();

        new Timer(2000, e3 -> {
            isDeaccel = true;
        }).start();

        SerialConnectionHandler serialConn = new SerialConnectionHandler(this);
        //serialConn.read();

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

        if (mul2 < 0.0005) {
            //System.out.println((System.currentTimeMillis() - startTime) / 1000.0);
        }

    }
    @Override
    public void onValueRead(String s) {
            System.out.println("Abedula: " + s);
    }
}
