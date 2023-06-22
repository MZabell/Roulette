import com.fazecast.jSerialComm.SerialPort;

public class SerialConnectionHandler {

    private SerialPort serialPort;
    private Thread serialThread;
    private boolean isRunning = false;
    private static final String PORT_DESCRIPTION = "TTL232R-3V3";

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
            int i = 0;
            byte[] readBuffer;
            while (isRunning) {
                if (i > 1) {
                    readBuffer = new byte[2];
                } else {
                    readBuffer = new byte[1];
                }
                serialPort.readBytes(readBuffer, readBuffer.length);
                System.out.flush();
                short s = readBuffer[0];
                if (i > 1) {
                    s = (short) (s << 8);
                    s |= readBuffer[1] & 0xff;
                }
                i++;
                listener.onValueRead(s, i);
                if (i > 6) {
                    isRunning = false;
                }
            }
            System.out.println("READ DONE");
            try {
                serialThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        serialThread.start();
    }

    public void write(byte[] writeBuffer) {
        serialThread = new Thread(() -> {
            serialPort.writeBytes(writeBuffer, writeBuffer.length);
            System.out.flush();
            try {
                serialThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        serialThread.start();
    }

    public interface SerialListener {
        void onValueRead(short s, int i);
    }
}
