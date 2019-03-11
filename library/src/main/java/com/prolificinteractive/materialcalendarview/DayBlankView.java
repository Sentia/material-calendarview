package com.prolificinteractive.materialcalendarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

@SuppressLint("ViewConstructor")
public class DayBlankView extends DayView {

  public DayBlankView(Context context, CalendarDay day) {
    super(context, day);
  }

  @Override
  protected void setEnabled() {
    setEnabled(false);
    setVisibility(View.VISIBLE);
    setTextColor(Color.TRANSPARENT);
    setSelectionColor(Color.TRANSPARENT);
  }

  @Override
  void applyFacade(DayViewFacade facade) {
    if (facade.isRangeMiddle()) {
      setCustomBackground(generateRangeMiddleDrawable());
    } else {
      setCustomBackground(facade.getBackgroundDrawable());
    }
  }
}
