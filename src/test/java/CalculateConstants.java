import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculateConstants {

    private static final int SCALE = 100;//Scaling factor, we only want to use ints

@Test
    public void run() {
    for (int i = 0; i <=30; i++) {
        System.out.println(String.format("Math cos(%d)=%f, Math.sin(%d)=%f", i,Math.cos(Math.toRadians(90-i)),i,Math.sin(Math.toRadians(90-i))));
    }
}

}
