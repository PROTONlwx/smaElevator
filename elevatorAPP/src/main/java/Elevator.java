import static java.lang.Thread.sleep;

public class Elevator implements Runnable {
    public Sensor sensor;

    public Elevator(Sensor sensor) {
        this.sensor = sensor;
    }

    public void run() {
        while (true) {
            synchronized(sensor) {
                if (sensor.shouldStop()) {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    sensor.stopComplete();
                }
            }

            synchronized(sensor) {
                sensor.cycleComplete();
            }

            synchronized(sensor) {
                if (sensor.shouldMove()) {
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                    }
                    sensor.MoveComplete();
                }
            }

        }

    }
}
