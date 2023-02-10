import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.PrintWriter;
import java.time.Clock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SensorTest {
    PrintWriter printWriter = mock(PrintWriter.class);

    @Test
    void getNextFloorWhenDirectionIdle() {
        Sensor sensor = new Sensor(printWriter, Clock.systemDefaultZone());
        Assertions.assertEquals(sensor.getNextFloor(), 1);
    }

    @Test
    void getNextFloorWhenDirectionUp() {
        Sensor sensor = new Sensor(printWriter, Clock.systemDefaultZone());
        sensor.direction = 1;
        Assertions.assertEquals(sensor.getNextFloor(), 2);
    }

    @Test
    void getNextFloorWhenDirectionDown() {
        Sensor sensor = new Sensor(printWriter, Clock.systemDefaultZone());
        sensor.direction = 2;
        Assertions.assertEquals(sensor.getNextFloor(), 0);
    }

    // There are so many boring tests
}
