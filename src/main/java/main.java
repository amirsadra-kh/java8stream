import org.json.JSONObject;

public class main {
    public static void main(String[] args) {

        JSONObject json = new Stat().stats("src/main/resources/eng-daily-01012018-12312018.csv");
        System.out.println(json);

    }
}

