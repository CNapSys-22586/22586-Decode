package org.firstinspires.ftc.teamcode.cnapsys.core.vars;

public class FieldState {
    public static Colors[] pattern = {Colors.UNKNOWN};
    private static int index = 0;
    public static void setPattern(int tagID) {
        switch (tagID) {
            case 21:
                pattern = new Colors[]{Colors.GREEN, Colors.PURPLE, Colors.PURPLE};
                break;
            case 22:
                pattern = new Colors[]{Colors.PURPLE, Colors.GREEN, Colors.PURPLE};
                break;
            case 23:
                pattern = new Colors[]{Colors.PURPLE, Colors.PURPLE, Colors.GREEN};
                break;
        }
    }
    public static boolean isInit() {
        return pattern[0] != Colors.UNKNOWN;
    }

    public static Colors getNext(){
        if (index > 2) index = 0;
        Colors color = pattern[index];
        ++index;
        return color;
    }

    public static void resetPattern() {
        index = 0;
    }
}
