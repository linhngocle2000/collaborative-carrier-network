package Utils;

import java.util.ArrayList;

public class Converter {

    public static ArrayList<Float> convertStringToTR(String s){
        ArrayList<Float> res = new ArrayList<>();
        String tmp = s.replaceAll("<", "").replaceAll("\\(","").replaceAll("\\)","").replaceAll(">","");
        for (String tr : tmp.split(",")) {
            Float n = Float.parseFloat(tr);
            res.add(n);
        }
        return res;
    }

    public static boolean checkPriceFormat(String s) {
        String regex = "^[1-9][0-9]*?(\\.[0-9][0-9]?)?$";
        return !s.matches(regex);
    }

    public static boolean checkTRFormat(String s) {
        String regex = "^<\\(\\((-?)(0|([1-9][0-9]*))(\\.[0-9]+)?,(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?\\),\\((-?)(0|([1-9][0-9]*))(\\.[0-9]+)?,(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?\\)\\)(,\\(\\((-?)(0|([1-9][0-9]*))(\\.[0-9]+)?,(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?\\),\\((-?)(0|([1-9][0-9]*))(\\.[0-9]+)?,(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?\\)\\))*?>$";
        return !s.matches(regex);
    }


    public static boolean checkDepotFormat(String s) {
        String regex = "^-?[1-9][0-9]*?(\\.[0-9]+?)?$";
        return !s.matches(regex);
    }
}
