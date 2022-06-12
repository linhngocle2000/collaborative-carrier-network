package UIResource.scrollbar;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JScrollBar;

public class ScrollBarCustom extends JScrollBar {

    public ScrollBarCustom(int max, int elems) {
        ModernScrollBarUI scrollbar = new ModernScrollBarUI();
        scrollbar.setThumbSize(max, elems);
        setUI(scrollbar);
        setPreferredSize(new Dimension(8, 8));
        setForeground(new Color(48, 144, 216));
        setBackground(Color.WHITE);
    }
}
