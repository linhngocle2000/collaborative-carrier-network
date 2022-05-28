package UIResource;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class UIData {

    private static Color background = new Color(1f, 1f, 1f);
    private static Color errorColor = new Color(1f, 0f, 0f);
    private static Color successColor = new Color(75, 181, 67, 255);
    private static int width = 300;
    private static int height = 370;
    private static Border emptyBorder = BorderFactory.createEmptyBorder();
    private static Font font = new Font("Dialog", Font.BOLD, 12);

    public static Color getBackground() {
        return background;
    }

    public static Color getErrorColor() {
        return errorColor;
    }

    public static Color getSuccessColor() {
        return successColor;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static Border getEmptyBorder() {
        return emptyBorder;
    }

    public static Font getFont() {
        return font;
    }
}
