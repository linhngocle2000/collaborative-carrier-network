package UIResource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static float[] convertTransportRequests(String s){
        float[] res = new float [4];
        Matcher m = Pattern.compile("\\(\\((.*?),(.*?)\\),\\((.*?),(.*?)\\)\\)").matcher(s);
        while (m.find()) {
            res[0] = Float.parseFloat(m.group(1));
            res[1] = Float.parseFloat(m.group(2));
            res[2] = Float.parseFloat(m.group(3));
            res[3] = Float.parseFloat(m.group(4));
        }
        return res;
    }
}
