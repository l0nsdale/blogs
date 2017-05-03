package com.pashkevich.app.utils.theme;

public class ThemeBuilder {
    private static Theme themeBlack = new Theme("black","white","gray");
    private static Theme themeStandart = new Theme("#eee","black","silver");

    public static Theme getThemeBlack() {
        return themeBlack;
    }
    public static Theme getThemeStandart() {
        return themeStandart;
    }
}
