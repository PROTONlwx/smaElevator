import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.time.Clock;
import java.time.Instant;

import static java.lang.Thread.sleep;
import static org.mockito.Mockito.*;

public class IntegrationTest {
    PrintWriter spyPrintWriter = mock(PrintWriter.class);
    Clock spyClock = mock(Clock.class);

    @Test
    void OneToTen() {
        Sensor sensor = new Sensor(spyPrintWriter, spyClock);
        when(spyClock.instant()).thenReturn(Instant.parse("2018-11-30T18:35:24.00Z"));
        Runnable elevator = new Elevator(sensor);
        new Thread(elevator).start();
        sensor.requestStopIn(10);
        try {
            sleep(35000);
        } catch (InterruptedException e) {}
        verify(spyPrintWriter, times(1)).println("2018-11-30T18:35:24Z: in-elevator request floor: 10");
        verify(spyPrintWriter, times(1)).println("2018-11-30T18:35:24Z: pass floor: 1");
        verify(spyPrintWriter, times(1)).println("2018-11-30T18:35:24Z: pass floor: 2");
        verify(spyPrintWriter, times(1)).println("2018-11-30T18:35:24Z: pass floor: 3");
        verify(spyPrintWriter, times(1)).println("2018-11-30T18:35:24Z: pass floor: 4");
        verify(spyPrintWriter, times(1)).println("2018-11-30T18:35:24Z: pass floor: 5");
        verify(spyPrintWriter, times(1)).println("2018-11-30T18:35:24Z: pass floor: 6");
        verify(spyPrintWriter, times(1)).println("2018-11-30T18:35:24Z: pass floor: 7");
        verify(spyPrintWriter, times(1)).println("2018-11-30T18:35:24Z: pass floor: 8");
        verify(spyPrintWriter, times(1)).println("2018-11-30T18:35:24Z: pass floor: 9");
        verify(spyPrintWriter, times(1)).println("2018-11-30T18:35:24Z: stop at floor: 10");
    }

    @Test
    void OneToTenToThree() {
        Sensor sensor = new Sensor(spyPrintWriter, spyClock);
        when(spyClock.instant()).thenReturn(Instant.parse("2018-11-30T18:35:24.00Z"));
        Runnable elevator = new Elevator(sensor);
        new Thread(elevator).start();
        sensor.requestStopIn(10);
        try {
            sleep(15000);
        } catch (InterruptedException e) {}
        sensor.requestStopIn(3);
        try {
            sleep(50000);
        } catch (InterruptedException e) {}
        verify(spyPrintWriter, times(1)).println("2018-11-30T18:35:24Z: in-elevator request floor: 10");
        verify(spyPrintWriter, times(1)).println("2018-11-30T18:35:24Z: pass floor: 1");
        verify(spyPrintWriter, times(1)).println("2018-11-30T18:35:24Z: pass floor: 2");
        verify(spyPrintWriter, times(1)).println("2018-11-30T18:35:24Z: pass floor: 3");
        verify(spyPrintWriter, times(1)).println("2018-11-30T18:35:24Z: in-elevator request floor: 3");
        verify(spyPrintWriter, times(2)).println("2018-11-30T18:35:24Z: pass floor: 4");
        verify(spyPrintWriter, times(2)).println("2018-11-30T18:35:24Z: pass floor: 5");
        verify(spyPrintWriter, times(2)).println("2018-11-30T18:35:24Z: pass floor: 6");
        verify(spyPrintWriter, times(2)).println("2018-11-30T18:35:24Z: pass floor: 7");
        verify(spyPrintWriter, times(2)).println("2018-11-30T18:35:24Z: pass floor: 8");
        verify(spyPrintWriter, times(2)).println("2018-11-30T18:35:24Z: pass floor: 9");
        verify(spyPrintWriter, times(1)).println("2018-11-30T18:35:24Z: stop at floor: 10");
        verify(spyPrintWriter, times(1)).println("2018-11-30T18:35:24Z: pass floor: 10");
        verify(spyPrintWriter, times(1)).println("2018-11-30T18:35:24Z: stop at floor: 3");
    }
}
