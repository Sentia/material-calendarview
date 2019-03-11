package com.prolificinteractive.materialcalendarview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView.ShowOtherDates;
import com.prolificinteractive.materialcalendarview.format.DayFormatter;
import com.prolificinteractive.materialcalendarview.format.WeekDayFormatter;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.WeekFields;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SHOW_DEFAULTS;
import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.showOtherMonths;

abstract class CalendarPagerView extends ViewGroup
    implements View.OnClickListener, View.OnLongClickListener {

  protected static final int DEFAULT_DAYS_IN_WEEK = 7;
  protected static final int DEFAULT_MAX_WEEKS = 6;
  protected static final int MONTH_TITLE_ROW = 1;
  protected static final int DAY_NAMES_ROW = 1;
  private static final int DEFAULT_MONTH_TITLE_LEFT_PADDING = 16;

  private final MonthTitleView monthTitleView;
  private final ArrayList<WeekDayView> weekDayViews = new ArrayList<>();
  private final ArrayList<DecoratorResult> decoratorResults = new ArrayList<>();
  private final DayOfWeek firstDayOfWeek;
  @ShowOtherDates protected int showOtherDates = SHOW_DEFAULTS;
  private MaterialCalendarView mcv;
  private CalendarDay firstViewDay;
  private CalendarDay minDate = null;
  private CalendarDay maxDate = null;
  protected boolean showWeekDays;
  protected boolean showMonthTitle;
  private final int tileHeight;
  private final int tileVerticalMargin;

  private final List<DayView> dayViews = new ArrayList<>();

  public CalendarPagerView(
      @NonNull MaterialCalendarView view,
      CalendarDay firstViewDay,
      DayOfWeek firstDayOfWeek,
      boolean showMonthTitle,
      boolean showWeekDays,
      int tileHeight,
      int tileVerticalMargin) {
    super(view.getContext());

    this.mcv = view;
    this.firstViewDay = firstViewDay;
    this.firstDayOfWeek = firstDayOfWeek;
    this.showMonthTitle = showMonthTitle;
    this.showWeekDays = showWeekDays;
    this.tileHeight = tileHeight;
    this.tileVerticalMargin = tileVerticalMargin;

    setClipChildren(false);
    setClipToPadding(false);

    if (showMonthTitle) {
      this.monthTitleView = buildMonthTitle(firstViewDay);
    } else {
      this.monthTitleView = null;
    }
    if (showWeekDays) {
      buildWeekDays(resetAndGetWorkingCalendar());
    }
    buildDayViews(dayViews, resetAndGetWorkingCalendar());
  }

  private MonthTitleView buildMonthTitle(CalendarDay currentMonth) {
    MonthTitleView monthTitleView = new MonthTitleView(getContext(), currentMonth);
    addView(monthTitleView, new SectionLayout());
    return monthTitleView;
  }

  private void buildWeekDays(LocalDate calendar) {
    LocalDate local = calendar;
    for (int i = 0; i < DEFAULT_DAYS_IN_WEEK; i++) {
      WeekDayView weekDayView = new WeekDayView(getContext(), local.getDayOfWeek());
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        weekDayView.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
      }
      weekDayViews.add(weekDayView);
      addView(weekDayView);
      local = local.plusDays(1);
    }
  }

  protected void addDayView(List<DayView> dayViews, LocalDate temp) {
    CalendarDay day = CalendarDay.from(temp);
    DayView dayView = new DayView(getContext(), day);
    dayView.setOnClickListener(this);
    dayView.setOnLongClickListener(this);
    dayViews.add(dayView);
    addView(dayView, new LayoutParams());
  }

  protected void addDayBlankView(List<DayView> dayViews, LocalDate temp, DayBlankView.BlankPosition position) {
    CalendarDay day = CalendarDay.from(temp);
    DayView dayView = new DayBlankView(getContext(), day, position);
    dayViews.add(dayView);
    addView(dayView, new LayoutParams());
  }
  protected LocalDate resetAndGetWorkingCalendar() {
    final TemporalField firstDayOfWeek = WeekFields.of(this.firstDayOfWeek, 1).dayOfWeek();
    final LocalDate temp = getFirstViewDay().getDate().with(firstDayOfWeek, 1);
    int dow = temp.getDayOfWeek().getValue();
    int delta = getFirstDayOfWeek().getValue() - dow;
    //If the delta is positive, we want to remove a week
    boolean removeRow = showOtherMonths(showOtherDates) ? delta >= 0 : delta > 0;
    if (removeRow) {
      delta -= DEFAULT_DAYS_IN_WEEK;
    }
    return temp.plusDays(delta);
  }

  protected DayOfWeek getFirstDayOfWeek() {
    return firstDayOfWeek;
  }

  protected abstract void buildDayViews(List<DayView> dayViews, LocalDate calendar);

  protected abstract boolean isDayEnabled(CalendarDay day);

  void setDayViewDecorators(List<DecoratorResult> results) {
    this.decoratorResults.clear();
    if (results != null) {
      this.decoratorResults.addAll(results);
    }
    invalidateDecorators();
  }

  public void setWeekDayTextAppearance(int taId) {
    for (WeekDayView weekDayView : weekDayViews) {
      weekDayView.setTextAppearance(getContext(), taId);
    }
  }

  public void setDateTextAppearance(int taId) {
    for (DayView dayView : dayViews) {
      dayView.setTextAppearance(getContext(), taId);
    }
  }

  public void setMonthTitleTextAppearance(int taId) {
    if (monthTitleView != null) {
      monthTitleView.setTextAppearance(getContext(), taId);
    }
  }

  public void setShowOtherDates(@ShowOtherDates int showFlags) {
    this.showOtherDates = showFlags;
    updateUi();
  }

  public void setSelectionEnabled(boolean selectionEnabled) {
    for (DayView dayView : dayViews) {
      dayView.setOnClickListener(selectionEnabled ? this : null);
      dayView.setClickable(selectionEnabled);
    }
  }

  public void setSelectionColor(int color) {
    for (DayView dayView : dayViews) {
      dayView.setSelectionColor(color);
    }
  }

  public void setWeekDayFormatter(WeekDayFormatter formatter) {
    for (WeekDayView dayView : weekDayViews) {
      dayView.setWeekDayFormatter(formatter);
    }
  }

  public void setDayFormatter(DayFormatter formatter) {
    for (DayView dayView : dayViews) {
      dayView.setDayFormatter(formatter);
    }
  }

  public void setDayFormatterContentDescription(DayFormatter formatter) {
    for (DayView dayView : dayViews) {
      dayView.setDayFormatterContentDescription(formatter);
    }
  }

  public void setMinimumDate(CalendarDay minDate) {
    this.minDate = minDate;
    updateUi();
  }

  public void setMaximumDate(CalendarDay maxDate) {
    this.maxDate = maxDate;
    updateUi();
  }

  public void setSelectedDates(Collection<CalendarDay> dates) {
    for (DayView dayView : dayViews) {
      CalendarDay day = dayView.getDate();
      dayView.setChecked(dates != null && dates.contains(day));
    }
    postInvalidate();
  }

  protected void updateUi() {
    for (DayView dayView : dayViews) {
      CalendarDay day = dayView.getDate();
      dayView.setupSelection(showOtherDates, day.isInRange(minDate, maxDate), isDayEnabled(day));
    }
    postInvalidate();
  }

  protected void invalidateDecorators() {
    final DayViewFacade facadeAccumulator = new DayViewFacade();
    for (DayView dayView : dayViews) {
      facadeAccumulator.reset();
      for (DecoratorResult result : decoratorResults) {
        if (result.decorator.shouldDecorate(dayView)) {
          result.result.applyTo(facadeAccumulator);
        }
      }
      dayView.applyFacade(facadeAccumulator);
    }
  }

  @Override
  public void onClick(final View v) {
    if (v instanceof DayView) {
      final DayView dayView = (DayView) v;
      mcv.onDateClicked(dayView);
    }
  }

  @Override
  public boolean onLongClick(final View v) {
    if (v instanceof DayView) {
      final DayView dayView = (DayView) v;
      mcv.onDateLongClicked(dayView);
      return true;
    }
    return false;
  }

  /*
   * Custom ViewGroup Code
   */

  /**
   * {@inheritDoc}
   */
  @Override
  protected LayoutParams generateDefaultLayoutParams() {
    return new LayoutParams();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
    final int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);
    final int specWidthMode = MeasureSpec.getMode(widthMeasureSpec);
    final int specHeightSize = MeasureSpec.getSize(heightMeasureSpec);
    final int specHeightMode = MeasureSpec.getMode(heightMeasureSpec);

    //We expect to be somewhere inside a MaterialCalendarView, which should measure EXACTLY
    if (specHeightMode == MeasureSpec.UNSPECIFIED || specWidthMode == MeasureSpec.UNSPECIFIED) {
      throw new IllegalStateException("CalendarPagerView should never be left to decide it's size");
    }

    //The spec width should be a correct multiple
    final int measureTileWidth = specWidthSize / DEFAULT_DAYS_IN_WEEK;
    final int measureTileHeight;
    if (tileHeight > 0) {
      measureTileHeight = tileHeight;
    } else {
      measureTileHeight = specHeightSize / getRows();
    }

    //Just use the spec sizes
    setMeasuredDimension(specWidthSize, specHeightSize);

    int count = getChildCount();
    int i = 0;

    if (showMonthTitle) {
      final View child = getChildAt(i);
      child.measure(specWidthSize, specHeightSize);
      i++;
    }

    for (; i < count; i++) {
      final View child = getChildAt(i);

      int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
          measureTileWidth,
          MeasureSpec.EXACTLY
      );

      int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
          measureTileHeight,
          MeasureSpec.EXACTLY
      );

      child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }
  }

  /**
   * Return the number of rows to display per page
   */
  protected abstract int getRows();

  /**
   * {@inheritDoc}
   */
  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    final int parentWidth = getWidth();
    final int count = getChildCount();
    final int parentLeft = 0;
    final int parentRight = parentWidth;

    int childTop = 0;
    int childLeft = parentLeft;
    int childRight = parentRight;
    int verticalMargin = 0;
    if (tileVerticalMargin > 0) {
      verticalMargin = tileVerticalMargin;
    }

    int i = 0;

    if (showMonthTitle) {
      final View child = getChildAt(i);
      final int width = child.getMeasuredWidth();
      final int height = child.getMeasuredHeight();
      final int leftPadding = (int)convertDpToPixel(DEFAULT_MONTH_TITLE_LEFT_PADDING, getContext());
      child.layout(leftPadding, childTop, parentWidth, childTop + height);
      childTop += height + verticalMargin;
      i++;
    }

    for (; i < count; i++) {
      final View child = getChildAt(i);

      final int width = child.getMeasuredWidth();
      final int height = child.getMeasuredHeight();

      if (LocalUtils.isRTL()) {
        child.layout(childRight - width, childTop, childRight, childTop + height);
        childRight -= width;
      } else {
        child.layout(childLeft, childTop, childLeft + width, childTop + height);
        childLeft += width;
      }

      //We should warp every so many children
      if ((showMonthTitle ? (i-1) : i) % DEFAULT_DAYS_IN_WEEK == (DEFAULT_DAYS_IN_WEEK - 1)) {
        childLeft = parentLeft;
        childRight = parentRight;
        childTop += height + verticalMargin;
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public LayoutParams generateLayoutParams(AttributeSet attrs) {
    return new LayoutParams();
  }

  @Override
  public boolean shouldDelayChildPressedState() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
    return p instanceof LayoutParams;
  }

  @Override
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
    return new LayoutParams();
  }

  @Override
  public void onInitializeAccessibilityEvent(@NonNull AccessibilityEvent event) {
    super.onInitializeAccessibilityEvent(event);
    event.setClassName(CalendarPagerView.class.getName());
  }

  @Override
  public void onInitializeAccessibilityNodeInfo(@NonNull AccessibilityNodeInfo info) {
    super.onInitializeAccessibilityNodeInfo(info);
    info.setClassName(CalendarPagerView.class.getName());
  }

  protected CalendarDay getFirstViewDay() {
    return firstViewDay;
  }

  protected static class SectionLayout extends MarginLayoutParams {
    public SectionLayout() {
      super(MATCH_PARENT, WRAP_CONTENT);
    }
  }

  /**
   * Simple layout params class for MonthView, since every child is the same size
   */
  protected static class LayoutParams extends MarginLayoutParams {

    /**
     * {@inheritDoc}
     */
    public LayoutParams() {
      super(WRAP_CONTENT, WRAP_CONTENT);
    }
  }

  /**
   * This method converts dp unit to equivalent pixels, depending on device density.
   * https://stackoverflow.com/a/9563438/2722270
   */
  private static float convertDpToPixel(int dp, Context context){
    return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
  }
}
