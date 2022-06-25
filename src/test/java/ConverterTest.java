import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import Utils.Converter;
import org.junit.Test;

import java.util.ArrayList;

public class ConverterTest {

    @Test
    public void testConvertTransportRequests() {
        ArrayList<Float> expected = new ArrayList<>() {
            {
                add((float)1.2);
                add((float)2);
                add((float)3.28738);
                add((float)4);
                add((float)5);
                add((float)6);
                add((float)7);
                add((float)8);
                add((float)9);
                add((float)10);
            }
        };
        assertEquals(Converter.convertStringToTR("<((1.2,2),(3.28738,4),(05,6)),((7,8),(9,10))>"),
                expected, "Transport requests are converted from string to array of float");
    }

    @Test
    public void testCheckPriceFormat() {
        assertFalse(Converter.checkPriceFormat("1"),"Price: 1");
        assertFalse(Converter.checkPriceFormat("9999"),"Price: 1");
        assertFalse(Converter.checkPriceFormat("2.9"),"Price: 2.9");
        assertFalse(Converter.checkPriceFormat("2.12"),"Price: 2.12");
        assertTrue(Converter.checkPriceFormat("01"),"Price: 01");
        assertTrue(Converter.checkPriceFormat("2.32342342"),"Price: 2.32342342");
        assertTrue(Converter.checkPriceFormat("letters"),"Price: letters");
        assertTrue(Converter.checkPriceFormat("lettersWith0123"),"Price: lettersWith0123");
    }
}