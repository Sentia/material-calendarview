package com.prolificinteractive.materialcalendarview.format;

import com.prolificinteractive.materialcalendarview.CalendarDay;

/**
 * Used to format a {@linkplain com.prolificinteractive.materialcalendarview.CalendarDay} to a string for the month/year title
 */
public interface TitleFormatter {

  String DEFAULT_FORMAT = "LLLL yyyy";
  String MONTH_ONLY_FORMAT = "LLLL";

  TitleFormatter DEFAULT = new DateFormatTitleFormatter();
  TitleFormatter MONTH_ONLY = new DateFormatTitleFormatter(MONTH_ONLY_FORMAT);

  /**
   * Converts the supplied day to a suitable month/year title
   *
   * @param day the day containing relevant month and year information
   * @return a label to display for the given month/year
   */
  CharSequence format(CalendarDay day);
}
