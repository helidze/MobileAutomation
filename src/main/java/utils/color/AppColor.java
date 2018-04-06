package utils.color;

import java.awt.*;

public enum AppColor {

    /**
     * Portal colors.
     */

    PORTAL_INACTIVE_TAB_COLOR(new Color(234, 234, 234)),
    PORTAL_INACTIVE_TAB_FONT_COLOR(new Color(153, 153, 153)),
    PORTAL_ACTIVE_TAB_DEFAULT_COLOR(new Color(204, 0, 0)),
    PORTAL_ACTIVE_TAB_FONT_DEFAULT_COLOR(new Color(255, 255, 255)),
    PORTAL_HEADER_BACKGROUND_DEFAULT_COLOR(new Color(255, 255, 255)),
    PORTAL_MENU_DEFAULT_COLOR(new Color(0, 0, 0)),

    PORTAL_COLOR_PICKER_GREEN_3(new Color(142, 179, 63)),
    PORTAL_COLOR_PICKER_PURPLE_2(new Color(99, 39, 142)),
    PORTAL_COLOR_PICKER_YELLOW_4(new Color(253, 234, 126)),
    PORTAL_COLOR_PICKER_BLUE_1(new Color(0, 102, 180)),

    VALID_DATA_MESSAGE_COLOR_GREEN(new Color(0, 119, 0)),
    INVALID_DATA_MESSAGE_COLOR_RED(new Color(255, 0, 0)),

    DUPLICATE_REPORT_NAME_RED_COLOR(new Color(204, 0, 0)),
    REPORT_NAME_NORMAL_BLACK_COLOR(new Color(0, 0, 0));

    private Color color;

    AppColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public String getHexColorCode() {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }
}
