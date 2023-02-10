import org.junit.jupiter.api.Test;
import java.io.PrintWriter;
import static java.lang.Thread.sleep;
import static org.mockito.Mockito.*;

class ElevatorTest {
    Sensor spySensor = mock(Sensor.class);
    PrintWriter printWriter = mock(PrintWriter.class);

    @Test
    void completeMethodCalledWhenShouldMethodReturnTrue() {
        when(spySensor.shouldStop()).thenReturn(true);
        when(spySensor.shouldMove()).thenReturn(true);
        Runnable elevator = new Elevator(spySensor);
        new Thread(elevator).start();
        try {
            sleep(5000);
        } catch (InterruptedException e) {}
        verify(spySensor, atLeast(1)).stopComplete();
        verify(spySensor, atLeast(1)).shouldMove();
        verify(spySensor, atLeast(1)).shouldStop();
        verify(spySensor, atLeast(1)).MoveComplete();
    }

    @Test
    void CompleteMethodNotCalledWhenShouldMethodReturnTrue() {
        when(spySensor.shouldStop()).thenReturn(false);
        when(spySensor.shouldMove()).thenReturn(false);
        Runnable elevator = new Elevator(spySensor);
        new Thread(elevator).start();
        try {
            sleep(5000);
        } catch (InterruptedException e) {}
        verify(spySensor, never()).stopComplete();
        verify(spySensor, atLeast(1)).shouldMove();
        verify(spySensor, atLeast(1)).shouldStop();
        verify(spySensor, never()).MoveComplete();
    }
}