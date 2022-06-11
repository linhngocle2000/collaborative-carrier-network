package Utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Converter {

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

    public static ArrayList<Float> convertStringToTR(String s){
        ArrayList<Float> res = new ArrayList<>();
        String tmp = s.replaceAll("<", "").replaceAll("\\(","").replaceAll("\\)","").replaceAll(">","");
        for (String tr : tmp.split(",")) {
            Float n = Float.parseFloat(tr);
            res.add(n);
        }
        return res;
    }
}
