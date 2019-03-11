package com.prolificinteractive.materialcalendarview;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import java.util.Collection;
import java.util.List;

import android.util.Log;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.temporal.WeekFields;

/**
 * Display a month of {@linkplain DayView}s and
 * seven {@linkplain WeekDayView}s.
 */
@SuppressLint("ViewConstructor") class MonthView extends CalendarPagerView {
  private static final String TAG = "MonthView";

  public MonthView(
      @NonNull final MaterialCalendarView view,
      final CalendarDay month,
      final DayOfWeek firstDayOfWeek,
      final boolean showMonthTitle,
      final boolean showWeekDays,
      int titleHeight,
      int titleVerticalMargin) {
    super(view, month, firstDayOfWeek, showMonthTitle, showWeekDays, titleHeight, titleVerticalMargin);
  }

  @Override protected void buildDayViews(
      final List<DayView> dayViews,
      final LocalDate calendar) {
    LocalDate temp = calendar;
    LocalDate firstDate = getFirstDate();
    LocalDate lastDate = getLastDate();
    int weekCount = getWeekCount();
    int dayCount = getDayCount();
    int blankDayCount = weekCount * DEFAULT_DAYS_IN_WEEK - dayCount;
    int blankDayIndex = 0;
    for (int r = 0; r < DEFAULT_MAX_WEEKS; r++) {
      for (int i = 0; i < DEFAULT_DAYS_IN_WEEK; i++) {
        CalendarDay day = CalendarDay.from(temp);
        // added a blank day view if the day is in other month to support blank selection, but ShowOtherDates won't be working.
        boolean isOtherMonth = day.getMonth() != getFirstViewDay().getMonth();
        if (isOtherMonth) {
          if (blankDayIndex < blankDayCount) {
            if (temp.isBefore(firstDate)) {
              addDayBlankView(dayViews, firstDate);
              blankDayIndex++;
            } else if (temp.isAfter(lastDate)) {
              addDayBlankView(dayViews, lastDate);
              blankDayIndex++;
            }
          }
        } else {
          addDayView(dayViews, temp);
        }
        temp = temp.plusDays(1);
      }
    }
  }

  public CalendarDay getMonth() {
    return getFirstViewDay();
  }

  @Override protected boolean isDayEnabled(final CalendarDay day) {
    return day.getMonth() == getFirstViewDay().getMonth();
  }

  @Override protected int getRows() {
    return getWeekCount() +
            (showMonthTitle ? MONTH_TITLE_ROW : 0) +
            (showWeekDays ? DAY_NAMES_ROW : 0);
  }

  // return week count of this month
  private int getWeekCount() {
    return getLastDate().get(WeekFields.of(getFirstDayOfWeek(), 1).weekOfMonth());
  }

  // return day count of this month
  private int getDayCount() {
    return getFirstViewDay().getDate().lengthOfMonth();
  }

  private LocalDate getFirstDate() {
    return getFirstViewDay().getDate();
  }

  private LocalDate getLastDate() {
    return getFirstDate().withDayOfMonth(getDayCount());
  }
}
