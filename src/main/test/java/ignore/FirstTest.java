import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FirstTest {

    @Test
    @DisplayName("My 1st JUnit 5 test! 😎")
    void myFirstTest() {

        assertEquals(2, 1 + 1, "1 + 1 should equal 2");
    }

}