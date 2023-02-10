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
            }

            sensor.cycleComplete();
            if (sensor.shouldMove()) {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {}
                sensor.MoveComplete();
            }
        }
    }
}
