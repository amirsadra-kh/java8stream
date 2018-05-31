import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Stat class handles the main logic of the program which is
 * to find minimum and maximum temperatures per month,
 * as well as the total amount of snow received in 2018.
 * @author Amir Sadra Khorramizadeh
 */
public class Stat {

    /**
     * This function uses Java Stream to process a 'CSV' file and returns a JSON object.
     * @param csvFileLoc This parameter is the address string of the 'CSV' file.
     * @return JSON object in the format of {"reports":[], "total"}. The "reports"
     * array includes "min", "max" and "month" which are the minimum temperature,
     * maximum temperature and the relevant month.
     */
    public JSONObject stats(String csvFileLoc) {

        class Report {
            public Report(int month, float max, float min, float total) {
                this.month = month;
                this.max = max;
                this.min = min;
                this.total = total;
            }

            int month;
            float max;
            float min;
            float total;


            public int getMonth() {
                return month;
            }


            public float getMax() {
                return max;
            }


            public float getMin() {
                return min;
            }

            @Override
            public String toString() {
                return  "{ 'month' : " + month +
                        ", 'max' : " + max +
                        ", 'min' : " + min + " }";
            }
        }

        ArrayList<Report> summary = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(csvFileLoc)).skip(26)){
            List<Report> reports =
                    stream
                    .map(x -> Arrays.asList(x.split(",")))
                    .filter(x -> !x.get(2).equals("\"\"") && !x.get(5).equals("\"\"") && !x.get(7).equals("\"\"") && !x.get(17).equals("\"\""))
                    .map(x -> new Report(Integer.parseInt(x.get(2).replace("\"","")),
                            Float.parseFloat(x.get(5).replace("\"","")),
                            Float.parseFloat(x.get(7).replace("\"","")),
                            Float.parseFloat(x.get(17).replace("\"",""))))
                    .collect(Collectors.toList());

            Optional<Float> total = reports.stream().map(x -> x.total).reduce((r1, r2) -> r1+r2);

            Map<Integer,List<Report>> grouped = reports.stream().collect(Collectors.groupingBy(x -> x.getMonth()));

            for( Map.Entry<Integer, List<Report>> e : grouped.entrySet()) {
                float max = e.getValue().stream().sorted(Comparator.comparing(r -> -r.getMax())).collect(Collectors.toList()).get(0).max;
                float min = e.getValue().stream().sorted(Comparator.comparing(r -> r.getMin())).collect(Collectors.toList()).get(0).min;
                Optional<Report> totalSnow = e.getValue().stream().reduce((r1, r2) -> new Report(e.getKey(), max, min, total.get()));
                summary.add(totalSnow.get());
            }

            stream.close();

        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }

        String s = "{ 'total' : " + summary.get(0).total + ", 'reports' : " + summary.toString() + "}";

        JSONObject json = new JSONObject(s);

        return json;
    }
}
