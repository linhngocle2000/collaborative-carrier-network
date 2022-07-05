package Utils;

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
        assertFalse(Converter.checkPriceFormat("9999"),"Price: 9999");
        assertFalse(Converter.checkPriceFormat("9999.12"),"Price: 9999.12");
        assertFalse(Converter.checkPriceFormat("2.9"),"Price: 2.9");
        assertFalse(Converter.checkPriceFormat("2.12"),"Price: 2.12");
        assertTrue(Converter.checkPriceFormat("01"),"Price: 01");
        assertTrue(Converter.checkPriceFormat("2.123456"),"Price: 2.123456");
        assertTrue(Converter.checkPriceFormat("letters"),"Price: letters");
        assertTrue(Converter.checkPriceFormat("lettersWith0123"),"Price: lettersWith0123");
    }

    @Test
    public void testCheckDepotFormat() {
        assertFalse(Converter.checkDepotFormat("1"),"Depot: 1");
        assertFalse(Converter.checkDepotFormat("9999"),"Depot: 9999");
        assertFalse(Converter.checkDepotFormat("-9999"),"Depot: -9999");
        assertFalse(Converter.checkDepotFormat("2.9"),"Depot: 2.9");
        assertFalse(Converter.checkDepotFormat("2.123456789"),"Depot: 2.123456789");
        assertFalse(Converter.checkDepotFormat("123.123"),"Depot: 123.123");
        assertFalse(Converter.checkDepotFormat("-2.123456789"),"Depot: -2.123456789");
        assertTrue(Converter.checkDepotFormat("123.-123"),"Depot: 123.-123");
        assertTrue(Converter.checkDepotFormat("01"),"Depot: 01");
        assertTrue(Converter.checkDepotFormat("-letters"),"Depot: -letters");
        assertTrue(Converter.checkDepotFormat("letters"),"Depot: letters");
        assertTrue(Converter.checkDepotFormat("lettersWith0123"),"Depot: lettersWith0123");
    }

    @Test
    public void testCheckTRFormat() {
        assertFalse(Converter.checkTRFormat("<((1.233,2),(-23.1213,2.9)),((5,10.0),(-29,11.21))>"),"<((1.233,2),(-23.1213,2.9)),((5,10.0),(-29,11.21))>");
        assertTrue(Converter.checkTRFormat("<((1.233,02),(-23.1213,2.9)),((5,10.0),(-29,11.21))>"),"<((1.233,02),(-23.1213,2.9)),((5,10.0),(-29,11.21))>");
        assertTrue(Converter.checkTRFormat("<((1.233,2),(-23.1213,2.-9)),((5,10.0),(-29,11.21))>"),"<((1.233,02),(-23.1213,2.-9)),((5,10.0),(-29,11.21))>");
        assertTrue(Converter.checkTRFormat("<((1.233,abc),(-23.1213,2.9)),((5,10.0),(-29,11.21))>"),"<((1.233,abc),(-23.1213,2.9)),((5,10.0),(-29,11.21))>");
        assertTrue(Converter.checkTRFormat("<(1.233,2),(-23.1213,2.9)),((5,10.0),(-29,11.21))>"),"<(1.233,2),(-23.1213,2.9)),((5,10.0),(-29,11.21))>");
        assertTrue(Converter.checkTRFormat("<((1.233,2),(-23.1213,2.9),((5,10.0),(-29,11.21))>"),"<((1.233,2),(-23.1213,2.9),((5,10.0),(-29,11.21))>");
        assertTrue(Converter.checkTRFormat("<((1.233,2),(-23.1213,2.9)),(5,10.0),(-29,11.21))>"),"<((1.233,2),(-23.1213,2.9)),(5,10.0),(-29,11.21))>");
        assertTrue(Converter.checkTRFormat("<((1.233,2),(-23.1213,2.9)),((5,10.0),(-29,11.21)>"),"<((1.233,2),(-23.1213,2.9)),((5,10.0),(-29,11.21)>");
        assertTrue(Converter.checkTRFormat("<((1.233,2),(-23.1213,2.9))((5,10.0),(-29,11.21))>"),"<((1.233,2),(-23.1213,2.9))((5,10.0),(-29,11.21))>");
        assertTrue(Converter.checkTRFormat("<((1.233,2)(-23.1213,2.9)),((5,10.0),(-29,11.21))>"),"<((1.233,2)(-23.1213,2.9)),((5,10.0),(-29,11.21))>");
        assertTrue(Converter.checkTRFormat("((1.233,2),(-23.1213,2.9)),((5,10.0),(-29,11.21))>"),"((1.233,2),(-23.1213,2.9)),((5,10.0),(-29,11.21))>");
        assertTrue(Converter.checkTRFormat("<((1.233,2),(-23.1213,2.9)),((5,10.0),(-29,11.21))"),"<((1.233,2),(-23.1213,2.9)),((5,10.0),(-29,11.21))");
        assertTrue(Converter.checkTRFormat("<((1.233,2),-23.1213,2.9)),((5,10.0),(-29,11.21))>"),"<((1.233,2),-23.1213,2.9)),((5,10.0),(-29,11.21))>");
        assertTrue(Converter.checkTRFormat("<((1.233,2),(-23.1213,2.9)),((5,10.0,(-29,11.21))>"),"<((1.233,2),(-23.1213,2.9)),((5,10.0,(-29,11.21))>");
    }
}