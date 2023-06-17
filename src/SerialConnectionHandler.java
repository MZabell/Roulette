import com.fazecast.jSerialComm.SerialPort;

import java.util.Arrays;

public class SerialConnectionHandler {

    private SerialPort serialPort;
    private Thread serialThread;
    private boolean isRunning = false;
    private static final String PORT_DESCRIPTION = "TTL232R-3V3";
    private static final byte START_MARKER = '<';

    public boolean isConnected() {
        return isConnected;
    }
    private boolean isConnected = false;
    SerialListener listener;

    public SerialConnectionHandler(SerialListener listener) {
        this.listener = listener;

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
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING | SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
        serialPort.setComPortParameters(115200, 8, 1, 0);
    }

    public void read() {
        isRunning = true;
        serialThread = new Thread(() -> {
            while (isRunning) {
                byte[] readBuffer = new byte[16];
                serialPort.readBytes(readBuffer, readBuffer.length);
                //System.out.println(Arrays.toString(readBuffer));
                //if (readBuffer[0] != START_MARKER) continue;


                /*int data = 0;
                for (int i = 0; i < readBuffer.length; i++) {
                    data = data << 8;
                    data |= readBuffer[i] & 0xff;
                }*/
                System.out.flush();
                //char c = (char) readBuffer[0];
                String s = new String(readBuffer);
                listener.onValueRead(s);
            }
        });
        serialThread.start();
    }

    public void write(byte[] writeBuffer) {
        isRunning = true;
        serialThread = new Thread(() -> {
            while (isRunning) {
                serialPort.writeBytes(writeBuffer, writeBuffer.length);
                System.out.flush();
                isRunning = false;
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
        void onValueRead(String s);
    }
}
