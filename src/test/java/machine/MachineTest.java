package machine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MachineTest {

    @Test
    void codeProcessing() {
        String testString = "15";
        int expected = 15;
        assertEquals(expected, Machine.codeProcessing(testString));
    }
}