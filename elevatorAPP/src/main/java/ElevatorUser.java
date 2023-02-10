import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Clock;

import static java.lang.Thread.sleep;

public class ElevatorUser {
    public static void main(String[] args) throws IOException {
        PrintWriter printWriter = new PrintWriter("elevatorLog.txt", StandardCharsets.UTF_8);
        Sensor sensor = new Sensor(printWriter, Clock.systemDefaultZone());
        Runnable elevator = new Elevator(sensor);
        new Thread(elevator).start();
        sensor.requestStopIn(10);
        try {
            sleep(15000);
        } catch (InterruptedException e) {}
        sensor.requestStopIn(3);
    }
}
