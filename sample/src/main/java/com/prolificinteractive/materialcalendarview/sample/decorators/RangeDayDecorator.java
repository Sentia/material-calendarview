package com.prolificinteractive.materialcalendarview.sample.decorators;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayView;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.sample.R;

import java.util.HashSet;

/**
 * Decorate 2 days.
 */
public class RangeDayDecorator implements DayViewDecorator {

  private final HashSet<CalendarDay> list = new HashSet<>();
  private final Drawable drawable;
  private CalendarDay firstDay;
  private CalendarDay lastDay;

  public RangeDayDecorator(final Context context) {
    drawable = context.getResources().getDrawable(R.drawable.my_selector);
  }

  private boolean inSameMonth() {
    return firstDay.getYear() == lastDay.getYear() && firstDay.getMonth() == lastDay.getMonth();
  }

  @Override
  public boolean shouldDecorate(DayView dayView) {
      CalendarDay day = dayView.getDate();
      if (firstDay != null && lastDay != null) {
        Log.d("wy2", "shouldDecorate: day=" + day + ", firstDay=" + firstDay + ", lastDay=" + lastDay);
          return (day.getDate().isAfter(firstDay.getDate()) || day.getDate().isEqual(firstDay.getDate()))
                  &&
                  (day.getDate().isBefore(lastDay.getDate()) || day.getDate().isEqual(lastDay.getDate()));
      } else {
          return false;
      }
    //return list.contains(day);
  }

  @Override
  public void decorate(DayViewFacade view) {
    //view.setBackgroundDrawable(drawable);
    view.setRangeStart(true);
    //view.setRangeEnd(true);
  }

  /**
   * We're changing the dates, so make sure to call {@linkplain MaterialCalendarView#invalidateDecorators()}
   */
  public void addFirstAndLast(final CalendarDay first, final CalendarDay last) {
    list.clear();
    list.add(first);
    list.add(last);
    firstDay = first;
    lastDay = last;
  }
}
