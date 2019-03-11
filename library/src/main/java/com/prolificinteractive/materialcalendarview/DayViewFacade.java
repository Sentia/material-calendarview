package com.prolificinteractive.materialcalendarview;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstraction layer to help in decorating Day views
 */
public class DayViewFacade {

  private boolean isDecorated;

  private Drawable backgroundDrawable = null;
  private boolean isRangeStart = false;
  private boolean isRangeMiddle = false;
  private boolean isRangeEnd = false;
  private Drawable selectionDrawable = null;
  private final LinkedList<Span> spans = new LinkedList<>();
  private boolean daysDisabled = false;

  DayViewFacade() {
    isDecorated = false;
  }

  /**
   * Set a drawable to draw behind everything else
   *
   * @param drawable Drawable to draw behind everything
   */
  public void setBackgroundDrawable(@NonNull Drawable drawable) {
    if (drawable == null) {
      throw new IllegalArgumentException("Cannot be null");
    }
    this.backgroundDrawable = drawable;
    isDecorated = true;
  }

  public void setRangeStart(boolean isRangeStart) {
    this.isRangeStart = isRangeStart;
    isDecorated = true;
  }

  public void setRangeMiddle(boolean isRangeMiddle) {
    this.isRangeMiddle = isRangeMiddle;
    isDecorated = true;
  }

  public void setRangeEnd(boolean isRangeEnd) {
    this.isRangeEnd = isRangeEnd;
    isDecorated = true;
  }

  /**
   * Set a custom selection drawable
   * TODO: define states that can/should be used in StateListDrawables
   *
   * @param drawable the drawable for selection
   */
  public void setSelectionDrawable(@NonNull Drawable drawable) {
    if (drawable == null) {
      throw new IllegalArgumentException("Cannot be null");
    }
    selectionDrawable = drawable;
    isDecorated = true;
  }

  /**
   * Add a span to the entire text of a day
   *
   * @param span text span instance
   */
  public void addSpan(@NonNull Object span) {
    if (spans != null) {
      this.spans.add(new Span(span));
      isDecorated = true;
    }
  }

  /**
   * <p>Set days to be in a disabled state, or re-enabled.</p>
   * <p>Note, passing true here will <b>not</b> override minimum and maximum dates, if set.
   * This will only re-enable disabled dates.</p>
   *
   * @param daysDisabled true to disable days, false to re-enable days
   */
  public void setDaysDisabled(boolean daysDisabled) {
    this.daysDisabled = daysDisabled;
    this.isDecorated = true;
  }

  void reset() {
    backgroundDrawable = null;
    isRangeStart = false;
    isRangeMiddle = false;
    isRangeEnd = false;
    selectionDrawable = null;
    spans.clear();
    isDecorated = false;
    daysDisabled = false;
  }

  /**
   * Apply things set this to other
   *
   * @param other facade to apply our data to
   */
  void applyTo(DayViewFacade other) {
    if (selectionDrawable != null) {
      other.setSelectionDrawable(selectionDrawable);
    }
    if (backgroundDrawable != null) {
      other.setBackgroundDrawable(backgroundDrawable);
    }
    if (isRangeStart) {
      other.setRangeStart(true);
    }
    if (isRangeMiddle) {
      other.setRangeMiddle(true);
    }
    if (isRangeEnd) {
      other.setRangeEnd(true);
    }
    other.spans.addAll(spans);
    other.isDecorated |= this.isDecorated;
    other.daysDisabled = daysDisabled;
  }

  boolean isDecorated() {
    return isDecorated;
  }

  Drawable getSelectionDrawable() {
    return selectionDrawable;
  }

  Drawable getBackgroundDrawable() {
    return backgroundDrawable;
  }

  boolean isRangeStart() {
    return this.isRangeStart;
  }

  boolean isRagneMiddle() {
    return this.isRangeMiddle;
  }

  boolean isRangeEnd() {
    return this.isRangeEnd;
  }

  List<Span> getSpans() {
    return Collections.unmodifiableList(spans);
  }

  /**
   * Are days from this facade disabled
   *
   * @return true if disabled, false if not re-enabled
   */
  public boolean areDaysDisabled() {
    return daysDisabled;
  }

  static class Span {

    final Object span;

    public Span(Object span) {
      this.span = span;
    }
  }
}
