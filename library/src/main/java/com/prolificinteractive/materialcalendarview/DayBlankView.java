package com.prolificinteractive.materialcalendarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
    Drawable backgroundDrawable;
    if (facade.isRangeMiddle()) {
      backgroundDrawable = generateRangeMiddleDrawable();
    } else {
      backgroundDrawable = facade.getBackgroundDrawable();
    }
    setCustomBackground(backgroundDrawable);
  }
}
