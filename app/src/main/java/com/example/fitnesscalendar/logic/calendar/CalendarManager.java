package com.example.fitnesscalendar.logic.calendar;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CalendarManager {
    private LocalDate selectedDate;

    public CalendarManager() {
        this.selectedDate = LocalDate.now();
    }

    public void nextMonth() { selectedDate = selectedDate.plusMonths(1); }
    public void prevMonth() { selectedDate = selectedDate.minusMonths(1); }
    public String getMonthYearString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return selectedDate.format(formatter);
    }

    public List<String> daysInMonthArray() {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(selectedDate);
        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue(); // 1 (Mon) to 7 (Sun)

        // Fill empty slots before the 1st of the month
        for (int i = 1; i < dayOfWeek; i++) {
            daysInMonthArray.add("");
        }
        // Fill actual dates
        for (int i = 1; i <= daysInMonth; i++) {
            daysInMonthArray.add(String.valueOf(i));
        }
        return daysInMonthArray;
    }

    public LocalDate getSelectedDate() { return selectedDate; }
}
