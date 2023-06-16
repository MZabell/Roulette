import com.fazecast.jSerialComm.SerialPort;

import java.util.Arrays;

public class SerialConnectionHandler {

    private SerialPort serialPort;
    private Thread serialThread;
    private boolean isRunning = true;
    private static final String PORT_DESCRIPTION = "TTL232R-3V3";
    private static final byte START_MARKER = '<';

    public boolean isConnected() {
        return isConnected;
    }

    private boolean isConnected = false;

    public SerialConnectionHandler(SerialListener listener) {
        SerialPort[] ports = SerialPort.getCommPorts();

        for (SerialPort sp : ports) {
            if (sp.getPortDescription().equals(PORT_DESCRIPTION))
                serialPort = sp;
        }
        if (serialPort == null) {
            System.out.println("Device not found");
            return;
        } else
            isConnected = true;

        serialPort.openPort();
        serialThread = new Thread(() -> {
            serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);
            serialPort.setComPortParameters(115200,8, 1,0);
            while (isRunning) {
                byte[] readBuffer = new byte[1];
                serialPort.readBytes(readBuffer, readBuffer.length);
                //System.out.println(Arrays.toString(readBuffer));
                //if (readBuffer[0] != START_MARKER) continue;


                /*int data = 0;
                for (int i = 0; i < readBuffer.length; i++) {
                    data = data << 8;
                    data |= readBuffer[i] & 0xff;
                }*/
                System.out.flush();
                char c = (char) readBuffer[0];
                listener.onValueRead(c);
            }
        });
        serialThread.start();
    }

    public void stopSerial() {
        isRunning = false;
        try {
            serialThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        serialPort.closePort();
    }

    public interface SerialListener {
        void onValueRead(char c);
    }
}
