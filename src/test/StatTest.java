import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * The main test class of the program.
 * @author Amir Sadra Khorramizadeh
 */
public class StatTest {

    /**
     * Tests and validates the accuracy of program.
     */
    @Test
    public void stats() {
        Stat stat = new Stat();
        JSONObject json = stat.stats("src/main/resources/eng-daily-01012018-12312018.csv");
        JSONObject json2 = stat.stats("src/main/resources/eng-daily-01012018-12312018.csv");
        assertNotSame(json, json2);
        assertEquals(69.40001, json.getDouble("total"), 0.001);
        assertEquals(json.getJSONArray("reports").getJSONObject(0).getDouble("min"), -23.5, 0.001);
        assertEquals(json.getJSONArray("reports").getJSONObject(0).getDouble("max"), 12.6, 0.001);
    }
}