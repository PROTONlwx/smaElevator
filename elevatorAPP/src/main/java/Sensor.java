import java.io.PrintWriter;
import java.time.Clock;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Sensor {
    public int weightLimit;
    public int heightLimit;
    public int direction; // 0 idle, 1 up, 2 down
    public int currentFloor;
    public int currentWeight;
    public List<Integer> inElevatorRequest;
    public List<Integer> outElevatorRequest;
    public List<Integer> inElevatorRequestBuffer;
    public List<Integer> outElevatorRequestBuffer;
    public PrintWriter printWriter;
    public Clock clock;

    public Sensor(PrintWriter printWriter, Clock clock) {
        this(Integer.MAX_VALUE, Integer.MAX_VALUE, 1, 0, 0, printWriter, clock);
    }

    public Sensor(int weightLimit, int heightLimit, int currentFloor, int currentWeight, int direction, PrintWriter printWriter, Clock clock) {
        this.currentFloor = currentFloor;
        this.weightLimit = weightLimit;
        this.direction = direction;
        this.currentWeight = currentWeight;
        this.heightLimit = heightLimit;
        this.inElevatorRequest = new CopyOnWriteArrayList<>();
        this.outElevatorRequest = new CopyOnWriteArrayList<Integer>();
        this.inElevatorRequestBuffer = new CopyOnWriteArrayList<Integer>();
        this.outElevatorRequestBuffer = new CopyOnWriteArrayList<Integer>();
        this.printWriter = printWriter;
        this.clock = clock;
    }

    /**
     * Return the next floor the elevator is moving toward
     * @return      the next floor number the elevator is going to move toward
     */
    public int getNextFloor() {
        if (direction == 0) {
            return currentFloor;
        } else if (direction == 1) {
            return currentFloor + 1;
        } else {
            return currentFloor - 1;
        }
    }

    /**
     * Decide whether the current floor reach or exceed the limit.
     * @return      True if the limit is reached, false otherwise
     */
    public boolean reachMaximumHeight() {
        return currentFloor >= heightLimit;
    }

    /**
     * Request from inside of the elevator to stop at a specific floor
     */
    public void requestStopIn(int floor) {
        this.inElevatorRequestBuffer.add(floor);
        String logMsg = clock.instant() + ": in-elevator request floor: " + floor;
        printWriter.println(logMsg);
        printWriter.flush();
    }

    /**
     * Request from outside of the elevator to stop at a specific floor
     */
    public void requestStopOut(int floor) {
        this.outElevatorRequestBuffer.add(floor);
        String logMsg = clock.instant() + ": out-elevator request floor: " + floor;
        printWriter.println(logMsg);
        printWriter.flush();
    }

    /**
     * Decide whether the elevator should stop at the current floor. If the current floor appears to be requested and
     * the request is made at least one floor in advance, then the elevator should stop.
     * @return      True if the elevator should stop, false otherwise
     */
    public boolean shouldStop() {
        return outElevatorRequest.contains(currentFloor) || inElevatorRequest.contains(currentFloor);
    }

    /**
     * Stop is done for requested floor, so requests should be removed.
     */
    public void stopComplete() {
        outElevatorRequest.remove(Integer.valueOf(currentFloor));
        inElevatorRequest.remove(Integer.valueOf(currentFloor));
        String logMsg = clock.instant() + ": stop at floor: " + currentFloor;
        printWriter.println(logMsg);
        printWriter.flush();
    }

    /**
     * Decide whether the elevator should move toward some direction. Move is only needed when stop request is made.
     * @return      True if the elevator should move, false otherwise
     */
    public boolean shouldMove() {
        return !outElevatorRequest.isEmpty() || !inElevatorRequest.isEmpty();
    }

    /**
     * Stop is done for current, the elevator is taken to the next level.
     */
    public void MoveComplete() {
        String logMsg = clock.instant() + ": pass floor: " + currentFloor;
        currentFloor = getNextFloor();
        printWriter.println(logMsg);
        printWriter.flush();
    }

    /**
     * Immediately fter a stop, or immediately pass a floor that doesn't need stop, this method admit all the requests
     * in the buffer and determine the new direction to move toward. The elevator must travel in one direction at a
     * time until it needs to go no further.
     */
    public void cycleComplete() {
        outElevatorRequest.addAll(outElevatorRequestBuffer);
        inElevatorRequest.addAll(inElevatorRequestBuffer);
        outElevatorRequestBuffer.clear();
        inElevatorRequestBuffer.clear();
        if (reachMaximumHeight()) {
            direction = 0;
        }
        boolean existHigherFloor = false;
        boolean existLowerFloor = false;
        for (int reqFloor : outElevatorRequest) {
            existHigherFloor = existHigherFloor || reqFloor > currentFloor;
            existLowerFloor = existLowerFloor || reqFloor < currentFloor;
        }
        for (int reqFloor : inElevatorRequest) {
            existHigherFloor = existHigherFloor || reqFloor > currentFloor;
            existLowerFloor = existLowerFloor || reqFloor < currentFloor;
        }

        if (direction == 0) {
            if (!outElevatorRequest.isEmpty()) {
                direction = outElevatorRequest.get(0) > currentFloor ? 1 : 2;
            }
            if (!inElevatorRequest.isEmpty()) {
                direction = inElevatorRequest.get(0) > currentFloor ? 1 : 2;
            }
        } else if (direction == 1) {
            if (existHigherFloor) {
            } else if (existLowerFloor) {
                direction = 2;
            } else {
                direction = 0;
            }
        } else if (direction == 2) {
            if (existLowerFloor) {
            } else if (existHigherFloor) {
                direction = 1;
            } else {
                direction = 0;
            }
        }
    }
}
