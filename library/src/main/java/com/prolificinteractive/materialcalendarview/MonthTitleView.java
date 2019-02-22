package com.prolificinteractive.materialcalendarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;

import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

import java.util.Calendar;

/**
 * Month Title View
 */
@SuppressLint("ViewConstructor") class MonthTitleView extends AppCompatTextView {

  private TitleFormatter monthYearFormatter = TitleFormatter.DEFAULT;
  private TitleFormatter monthOnlyFormatter = TitleFormatter.MONTH_ONLY;
  private CalendarDay currentMonth;

  public MonthTitleView(final Context context, final CalendarDay currentMonth) {
    super(context);

    setGravity(Gravity.CENTER);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      setTextAlignment(TEXT_ALIGNMENT_CENTER);
    }

    setCurrentMonth(currentMonth);
  }

  public void setTitleFormatter(@Nullable final TitleFormatter formatter) {
    this.monthYearFormatter = formatter == null ? TitleFormatter.DEFAULT : formatter;
    setCurrentMonth(currentMonth);
  }

  public void setCurrentMonth(final CalendarDay currentMonth) {
    this.currentMonth = currentMonth;
    CharSequence newTitle;
    if (isThisYear(currentMonth)) {
      newTitle = monthOnlyFormatter.format(currentMonth);
    } else {
      newTitle = monthYearFormatter.format(currentMonth);
    }
    setText(newTitle);
  }

  private boolean isThisYear(final CalendarDay month) {
    return Calendar.getInstance().get(Calendar.YEAR) == month.getYear();
  }
}
