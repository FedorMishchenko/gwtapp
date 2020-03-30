package com.gwtApp.util;

public final class Constants {
    public static final String INPUT_REGEX = "\\b[1-9]{1}\\d{0,2}\\b";
    public static final String ENTER = "Enter";
    public static final String SORT = "Sort";
    public static final String RESET = "Reset";
    public static final String HEADER = "header";
    public static final String INTRO_SCREEN =  "Intro screen";
    public static final String SORT_SCREEN = "Sort screen";
    public static final String USER_QUESTION = "How many numbers to display?";
    public static final String BLANK = "";
    public static final String INVALID_DATA_MASSAGE = "The input must be a number in the range from 1 to 999";
    public static final String SERVER_ERROR_MSG = "Communication failed";
    public static final String POPUP_MASSAGE = "<strong>Please select a value smaller or equal to 30</strong>";
    public static final String NUMBERS_PANEL_WIDTH = "1400";
    public static final String BUTTONS_PANEL_WIDTH = "400";
    public static final String BUTTONS_WIDTH = "60";
    public static final int PADDING = 5;
    public static final int LIMIT = 10;
    public static final int MAX = 1000;

    private Constants(){ throw new UnsupportedOperationException("Utility class");}
}
