import java.io.PrintWriter;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

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
        this.inElevatorRequest = new ArrayList<Integer>();
        this.outElevatorRequest = new ArrayList<Integer>();
        this.inElevatorRequestBuffer = new ArrayList<Integer>();
        this.outElevatorRequestBuffer = new ArrayList<Integer>();
        this.printWriter = printWriter;
        this.clock = clock;
    }

    public int getNextFloor() {
        if (direction == 0) {
            return currentFloor;
        } else if (direction == 1) {
            return currentFloor + 1;
        } else {
            return currentFloor - 1;
        }
    }

//    public boolean reachMaximumWeight() {
//        return currentWeight >= weightLimit;
//    }

    public boolean reachMaximumHeight() {
        return currentFloor >= heightLimit;
    }

    public void requestStopIn(int floor) {
        this.inElevatorRequest.add(floor);
        printWriter.println(clock.instant() + ": in-elevator request floor: " + floor);
        printWriter.flush();
    }

    public void requestStopOut(int floor) {
        this.outElevatorRequest.add(floor);
        printWriter.println(clock.instant() + ": out-elevator request floor: " + floor);
        printWriter.flush();
    }

    public boolean shouldStop() {
        return outElevatorRequest.contains(currentFloor) || inElevatorRequest.contains(currentFloor);
    }

    public void stopComplete() {
        outElevatorRequest.remove(Integer.valueOf(currentFloor));
        inElevatorRequest.remove(Integer.valueOf(currentFloor));
        printWriter.println(clock.instant() + ": stop at floor: " + currentFloor);
        printWriter.flush();
    }

    public boolean shouldMove() {
        return !outElevatorRequest.isEmpty() || !inElevatorRequest.isEmpty();
    }

    public void MoveComplete() {
        printWriter.println(clock.instant() + ": pass floor: " + currentFloor);
        printWriter.flush();
        currentFloor = getNextFloor();
    }

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
