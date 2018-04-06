package utils.color;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {

    public static Color getColorFromHexColorCode(String hexColorCode) {
        return Color.decode(hexColorCode);
    }

    public static Color getColorFromRGBAColorCode(String rgbaColorCode) {
        Pattern c = Pattern.compile("rgba *\\( *([0-9]+), *([0-9]+), *([0-9]+), *([0-9]+) *\\)");
        Matcher m = c.matcher(rgbaColorCode);
        if (m.matches()) {
            return new Color(
                    Integer.valueOf(m.group(1)),
                    Integer.valueOf(m.group(2)),
                    Integer.valueOf(m.group(3)));
        }
        return null;
    }

}
