package com.simple.weather.data;

public class AppConfig {

    private static boolean ENABLE_ADS = true;

    public static boolean ENABLE_MAIN_INTERSTITIAL = ENABLE_ADS && true;

    public static long DELAY_NEXT_INTERSTITIAL = 120; // in second

    public static boolean ENABLE_MAIN_BANNER = ENABLE_ADS && true;

    public static boolean ENABLE_SETTING_BANNER = ENABLE_ADS && true;

}