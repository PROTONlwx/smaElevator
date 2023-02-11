import java.util.Scanner;

import static java.lang.Thread.sleep;

public class CLI implements Runnable {
    public Sensor sensor;

    public CLI(Sensor sensor) {
        this.sensor = sensor;
    }

    public void run() {
        System.out.println("Elevator is now running.");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter your request.");
            String command = scanner.nextLine();
            if (command.startsWith("in")) {
                int floor = Integer.parseInt(command.substring(2));
                synchronized(sensor) {
                    sensor.requestStopIn(floor);
                }
            } else if (command.startsWith("out")) {
                int floor = Integer.parseInt(command.substring(3));
                synchronized(sensor) {
                    sensor.requestStopOut(floor);
                }
            } else {
                System.out.println("Command format is incorrect.");
            }
        }

    }
}
