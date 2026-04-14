package com.example.fitnesscalendar.logic.utils;

import com.example.fitnesscalendar.R;

public class CategoryStyleHelper {

    public static class CategoryStyle {
        public final int backgroundColor;
        public final int strokeColor;

        public CategoryStyle(int backgroundColor, int strokeColor) {
            this.backgroundColor = backgroundColor;
            this.strokeColor = strokeColor;
        }
    }

    public static CategoryStyle getStyleForGroup(String group) {
        if (group == null) group = "";

        switch (group) {
            case "TYPE":
                return new CategoryStyle(
                        R.color.exercise_chip_type_bg_colour,
                        R.color.exercise_chip_type_stroke_colour
                );
            case "BASIC":
                return new CategoryStyle(
                        R.color.exercise_chip_basic_bg_colour,
                        R.color.exercise_chip_basic_stroke_colour
                );
            case "ADVANCED":
                return new CategoryStyle(
                        R.color.exercise_chip_adv_bg_colour,
                        R.color.exercise_chip_adv_stroke_colour
                );
            default:
                return new CategoryStyle(
                        R.color.exercise_chip_type_bg_colour,
                        R.color.exercise_chip_type_stroke_colour
                );
        }
    }
}