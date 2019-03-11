package com.prolificinteractive.materialcalendarview;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;

import java.util.Collection;
import java.util.List;

/**
 * Display a week of {@linkplain DayView}s and
 * seven {@linkplain WeekDayView}s.
 */
@SuppressLint("ViewConstructor") class WeekView extends CalendarPagerView {

  public WeekView(
      @NonNull final MaterialCalendarView view,
      final CalendarDay firstViewDay,
      final DayOfWeek firstDayOfWeek,
      final boolean showWeekDays,
      int titleHeight,
      int titleVerticalMargin) {
    super(view, firstViewDay, firstDayOfWeek, false, showWeekDays, titleHeight, titleVerticalMargin);
  }

  @Override protected void buildDayViews(
      final List<DayView> dayViews,
      final LocalDate calendar) {
    LocalDate temp = calendar;
    for (int i = 0; i < DEFAULT_DAYS_IN_WEEK; i++) {
      addDayView(dayViews, temp);
      temp = temp.plusDays(1);
    }
  }

  @Override protected boolean isDayEnabled(CalendarDay day) {
    return true;
  }

  @Override protected int getRows() {
    return showWeekDays ? DAY_NAMES_ROW + 1 : 1;
  }
}
