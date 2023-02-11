import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.Scanner;
import static java.lang.Thread.sleep;

public class ElevatorUser {
    public static void main(String[] args) throws IOException {
        PrintWriter printWriter = new PrintWriter("elevatorLog.txt", StandardCharsets.UTF_8);
        Sensor sensor = new Sensor(printWriter, Clock.systemDefaultZone());
        Runnable elevator = new Elevator(sensor);
        Runnable cli = new CLI(sensor);
        new Thread(elevator).start();
        new Thread(cli).start();
    }
}
