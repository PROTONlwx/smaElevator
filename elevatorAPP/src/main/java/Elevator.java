import static java.lang.Thread.sleep;

public class Elevator implements Runnable {
    public Sensor sensor;

    public Elevator(Sensor sensor) {
        this.sensor = sensor;
    }

    public void run() {
        while (true) {
            if (sensor.shouldStop()) {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {}
                sensor.stopComplete();
                System.out.println(System.currentTimeMillis() + ": stop at floor: " + sensor.currentFloor);
            }

            sensor.cycleComplete();
            if (sensor.shouldMove()) {
                System.out.println(System.currentTimeMillis() + ": pass floor: " + sensor.currentFloor);
                try {
                    sleep(3000);
                } catch (InterruptedException e) {}
                sensor.MoveComplete();
            }
        }
    }
}
