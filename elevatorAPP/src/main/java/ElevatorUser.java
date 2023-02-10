import static java.lang.Thread.sleep;

public class ElevatorUser {
    public static void main(String[] args) {
        Sensor sensor = new Sensor();
        Runnable elevator = new Elevator(sensor);
        new Thread(elevator).start();
        sensor.requestStopIn(1);
        sensor.requestStopIn(7);
        try {
            sleep(100000);
        } catch (InterruptedException e) {}
        sensor.stopComplete();
        sensor.requestStopIn(3);
    }
}
