package com.example.fitnesscalendar.logic.calendar;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarManager {
    private final Calendar currentDate = Calendar.getInstance();

    private LocalDate selectedDate;

    public CalendarManager() {
        this.selectedDate = LocalDate.now();
    }

//    public String getMonthYearString() {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
//        return selectedDate.format(formatter);
//    }

    public List<String> getDaysOfMonthList() {
        List<String> daysList = new ArrayList<>();

        // set a clone to the first day of the currently selected month
        Calendar cal = (Calendar) currentDate.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);

        // calculate leading empty slots (Adjusting for Monday start)
        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 2;
        if (firstDayOfWeek < 0) firstDayOfWeek = 6;

        // add empty strings for the previous month's trailing days
        for (int i = 0; i < firstDayOfWeek; i++) {
            daysList.add("");
        }

        // add the actual days of the current month
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= daysInMonth; i++) {
            daysList.add(String.valueOf(i));
        }

        return daysList;
    }

    public void goToNextMonth() {
        currentDate.add(Calendar.MONTH, 1);
    }

    public void goToPrevMonth() {
        currentDate.add(Calendar.MONTH, -1);
    }

    public String getHeaderString() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMMM yyyy", java.util.Locale.getDefault());
        return sdf.format(currentDate.getTime());
    }

    /**
     * Returns the raw Calendar instance if needed for database queries
     */
    public Calendar getCurrentCalendar() {
        return (Calendar) currentDate.clone();
    }

    public String getDateKeyForDay(String dayText) {
        if (dayText == null || dayText.isEmpty()) {
            return null;
        }

        try {
            // calendar clone used to avoid accidentally change the main date
            java.util.Calendar tempCal = (java.util.Calendar) currentDate.clone();

            // set the specific day provided by the adapter
            int day = Integer.parseInt(dayText);
            tempCal.set(java.util.Calendar.DAY_OF_MONTH, day);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
            return sdf.format(tempCal.getTime());
        } catch (Exception e) {
            return null;
        }
    }

}
