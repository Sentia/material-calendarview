package com.prolificinteractive.materialcalendarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

@SuppressLint("ViewConstructor")
public class DayBlankView extends DayView {

  public BlankPosition blankPosition;

  public enum BlankPosition {
    HEAD, TAIL
  }

  public DayBlankView(Context context, CalendarDay day, BlankPosition position) {
    super(context, day);
    this.blankPosition = position;
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
    setCustomBackground(facade.getBackgroundDrawable());
  }
}
