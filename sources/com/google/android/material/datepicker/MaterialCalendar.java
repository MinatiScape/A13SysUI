package com.google.android.material.datepicker;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import androidx.core.util.Pair;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MaterialCalendar<S> extends PickerFragment<S> {
    public CalendarConstraints calendarConstraints;
    public CalendarSelector calendarSelector;
    public CalendarStyle calendarStyle;
    public Month current;
    public DateSelector<S> dateSelector;
    public View dayFrame;
    public RecyclerView recyclerView;
    public int themeResId;
    public View yearFrame;
    public RecyclerView yearSelector;
    public static final Object MONTHS_VIEW_GROUP_TAG = "MONTHS_VIEW_GROUP_TAG";
    public static final Object NAVIGATION_PREV_TAG = "NAVIGATION_PREV_TAG";
    public static final Object NAVIGATION_NEXT_TAG = "NAVIGATION_NEXT_TAG";
    public static final Object SELECTOR_TOGGLE_TAG = "SELECTOR_TOGGLE_TAG";

    /* renamed from: com.google.android.material.datepicker.MaterialCalendar$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass3 implements OnDayClickListener {
        public AnonymousClass3() {
        }
    }

    /* loaded from: classes.dex */
    public enum CalendarSelector {
        DAY,
        YEAR
    }

    /* loaded from: classes.dex */
    public interface OnDayClickListener {
    }

    @Override // androidx.fragment.app.Fragment
    public final View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        final int i;
        int i2;
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getContext(), this.themeResId);
        this.calendarStyle = new CalendarStyle(contextThemeWrapper);
        LayoutInflater cloneInContext = layoutInflater.cloneInContext(contextThemeWrapper);
        CalendarConstraints calendarConstraints = this.calendarConstraints;
        Objects.requireNonNull(calendarConstraints);
        Month month = calendarConstraints.start;
        if (MaterialDatePicker.isFullscreen(contextThemeWrapper)) {
            i2 = 2131624300;
            i = 1;
        } else {
            i2 = 2131624295;
            i = 0;
        }
        View inflate = cloneInContext.inflate(i2, viewGroup, false);
        Resources resources = requireContext().getResources();
        int dimensionPixelOffset = resources.getDimensionPixelOffset(2131166454) + resources.getDimensionPixelOffset(2131166456) + resources.getDimensionPixelSize(2131166455);
        int dimensionPixelSize = resources.getDimensionPixelSize(2131166439);
        int i3 = MonthAdapter.MAXIMUM_WEEKS;
        inflate.setMinimumHeight(dimensionPixelOffset + dimensionPixelSize + (resources.getDimensionPixelOffset(2131166453) * (i3 - 1)) + (resources.getDimensionPixelSize(2131166434) * i3) + resources.getDimensionPixelOffset(2131166431));
        GridView gridView = (GridView) inflate.findViewById(2131428451);
        ViewCompat.setAccessibilityDelegate(gridView, new AccessibilityDelegateCompat() { // from class: com.google.android.material.datepicker.MaterialCalendar.1
            @Override // androidx.core.view.AccessibilityDelegateCompat
            public final void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                accessibilityNodeInfoCompat.setCollectionInfo(null);
            }
        });
        gridView.setAdapter((ListAdapter) new DaysOfWeekAdapter());
        gridView.setNumColumns(month.daysInWeek);
        gridView.setEnabled(false);
        this.recyclerView = (RecyclerView) inflate.findViewById(2131428454);
        getContext();
        this.recyclerView.setLayoutManager(new SmoothCalendarLayoutManager(i) { // from class: com.google.android.material.datepicker.MaterialCalendar.2
            @Override // androidx.recyclerview.widget.LinearLayoutManager
            public final void calculateExtraLayoutSpace(RecyclerView.State state, int[] iArr) {
                if (i == 0) {
                    iArr[0] = MaterialCalendar.this.recyclerView.getWidth();
                    iArr[1] = MaterialCalendar.this.recyclerView.getWidth();
                    return;
                }
                iArr[0] = MaterialCalendar.this.recyclerView.getHeight();
                iArr[1] = MaterialCalendar.this.recyclerView.getHeight();
            }
        });
        this.recyclerView.setTag(MONTHS_VIEW_GROUP_TAG);
        final MonthsPagerAdapter monthsPagerAdapter = new MonthsPagerAdapter(contextThemeWrapper, this.dateSelector, this.calendarConstraints, new AnonymousClass3());
        this.recyclerView.setAdapter(monthsPagerAdapter);
        int integer = contextThemeWrapper.getResources().getInteger(2131493011);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(2131428457);
        this.yearSelector = recyclerView;
        if (recyclerView != null) {
            recyclerView.mHasFixedSize = true;
            recyclerView.setLayoutManager(new GridLayoutManager(integer, 0));
            this.yearSelector.setAdapter(new YearGridAdapter(this));
            this.yearSelector.addItemDecoration(new RecyclerView.ItemDecoration() { // from class: com.google.android.material.datepicker.MaterialCalendar.4
                public final Calendar startItem = UtcDates.getUtcCalendarOf(null);
                public final Calendar endItem = UtcDates.getUtcCalendarOf(null);

                @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
                public final void onDraw(Canvas canvas, RecyclerView recyclerView2) {
                    int i4;
                    int i5;
                    Objects.requireNonNull(recyclerView2);
                    RecyclerView.Adapter adapter = recyclerView2.mAdapter;
                    if (adapter instanceof YearGridAdapter) {
                        RecyclerView.LayoutManager layoutManager = recyclerView2.mLayout;
                        if (layoutManager instanceof GridLayoutManager) {
                            YearGridAdapter yearGridAdapter = (YearGridAdapter) adapter;
                            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                            for (Pair pair : MaterialCalendar.this.dateSelector.getSelectedRanges()) {
                                F f = pair.first;
                                if (!(f == 0 || pair.second == null)) {
                                    this.startItem.setTimeInMillis(((Long) f).longValue());
                                    this.endItem.setTimeInMillis(((Long) pair.second).longValue());
                                    int i6 = this.startItem.get(1);
                                    Objects.requireNonNull(yearGridAdapter);
                                    MaterialCalendar<?> materialCalendar = yearGridAdapter.materialCalendar;
                                    Objects.requireNonNull(materialCalendar);
                                    CalendarConstraints calendarConstraints2 = materialCalendar.calendarConstraints;
                                    Objects.requireNonNull(calendarConstraints2);
                                    int i7 = i6 - calendarConstraints2.start.year;
                                    int i8 = this.endItem.get(1);
                                    MaterialCalendar<?> materialCalendar2 = yearGridAdapter.materialCalendar;
                                    Objects.requireNonNull(materialCalendar2);
                                    CalendarConstraints calendarConstraints3 = materialCalendar2.calendarConstraints;
                                    Objects.requireNonNull(calendarConstraints3);
                                    int i9 = i8 - calendarConstraints3.start.year;
                                    View findViewByPosition = gridLayoutManager.findViewByPosition(i7);
                                    View findViewByPosition2 = gridLayoutManager.findViewByPosition(i9);
                                    int i10 = gridLayoutManager.mSpanCount;
                                    int i11 = i7 / i10;
                                    int i12 = i9 / i10;
                                    for (int i13 = i11; i13 <= i12; i13++) {
                                        View findViewByPosition3 = gridLayoutManager.findViewByPosition(gridLayoutManager.mSpanCount * i13);
                                        if (findViewByPosition3 != null) {
                                            int top = findViewByPosition3.getTop();
                                            CalendarItemStyle calendarItemStyle = MaterialCalendar.this.calendarStyle.year;
                                            Objects.requireNonNull(calendarItemStyle);
                                            int i14 = top + calendarItemStyle.insets.top;
                                            int bottom = findViewByPosition3.getBottom();
                                            CalendarItemStyle calendarItemStyle2 = MaterialCalendar.this.calendarStyle.year;
                                            Objects.requireNonNull(calendarItemStyle2);
                                            int i15 = bottom - calendarItemStyle2.insets.bottom;
                                            if (i13 == i11) {
                                                i4 = (findViewByPosition.getWidth() / 2) + findViewByPosition.getLeft();
                                            } else {
                                                i4 = 0;
                                            }
                                            if (i13 == i12) {
                                                i5 = (findViewByPosition2.getWidth() / 2) + findViewByPosition2.getLeft();
                                            } else {
                                                i5 = recyclerView2.getWidth();
                                            }
                                            canvas.drawRect(i4, i14, i5, i15, MaterialCalendar.this.calendarStyle.rangeFill);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            });
        }
        if (inflate.findViewById(2131428390) != null) {
            final MaterialButton materialButton = (MaterialButton) inflate.findViewById(2131428390);
            materialButton.setTag(SELECTOR_TOGGLE_TAG);
            ViewCompat.setAccessibilityDelegate(materialButton, new AccessibilityDelegateCompat() { // from class: com.google.android.material.datepicker.MaterialCalendar.5
                @Override // androidx.core.view.AccessibilityDelegateCompat
                public final void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                    String str;
                    super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                    if (MaterialCalendar.this.dayFrame.getVisibility() == 0) {
                        MaterialCalendar materialCalendar = MaterialCalendar.this;
                        Objects.requireNonNull(materialCalendar);
                        str = materialCalendar.requireContext().getResources().getString(2131952859);
                    } else {
                        MaterialCalendar materialCalendar2 = MaterialCalendar.this;
                        Objects.requireNonNull(materialCalendar2);
                        str = materialCalendar2.requireContext().getResources().getString(2131952857);
                    }
                    accessibilityNodeInfoCompat.mInfo.setHintText(str);
                }
            });
            MaterialButton materialButton2 = (MaterialButton) inflate.findViewById(2131428392);
            materialButton2.setTag(NAVIGATION_PREV_TAG);
            MaterialButton materialButton3 = (MaterialButton) inflate.findViewById(2131428391);
            materialButton3.setTag(NAVIGATION_NEXT_TAG);
            this.yearFrame = inflate.findViewById(2131428457);
            this.dayFrame = inflate.findViewById(2131428450);
            setSelector(CalendarSelector.DAY);
            materialButton.setText(this.current.getLongName(inflate.getContext()));
            this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() { // from class: com.google.android.material.datepicker.MaterialCalendar.6
                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public final void onScrollStateChanged(RecyclerView recyclerView2, int i4) {
                    if (i4 == 0) {
                        recyclerView2.announceForAccessibility(materialButton.getText());
                    }
                }

                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public final void onScrolled(RecyclerView recyclerView2, int i4, int i5) {
                    int i6;
                    if (i4 < 0) {
                        MaterialCalendar materialCalendar = MaterialCalendar.this;
                        Objects.requireNonNull(materialCalendar);
                        RecyclerView recyclerView3 = materialCalendar.recyclerView;
                        Objects.requireNonNull(recyclerView3);
                        i6 = ((LinearLayoutManager) recyclerView3.mLayout).findFirstVisibleItemPosition();
                    } else {
                        MaterialCalendar materialCalendar2 = MaterialCalendar.this;
                        Objects.requireNonNull(materialCalendar2);
                        RecyclerView recyclerView4 = materialCalendar2.recyclerView;
                        Objects.requireNonNull(recyclerView4);
                        i6 = ((LinearLayoutManager) recyclerView4.mLayout).findLastVisibleItemPosition();
                    }
                    MaterialCalendar materialCalendar3 = MaterialCalendar.this;
                    MonthsPagerAdapter monthsPagerAdapter2 = monthsPagerAdapter;
                    Objects.requireNonNull(monthsPagerAdapter2);
                    CalendarConstraints calendarConstraints2 = monthsPagerAdapter2.calendarConstraints;
                    Objects.requireNonNull(calendarConstraints2);
                    Month month2 = calendarConstraints2.start;
                    Objects.requireNonNull(month2);
                    Calendar dayCopy = UtcDates.getDayCopy(month2.firstOfMonth);
                    dayCopy.add(2, i6);
                    materialCalendar3.current = new Month(dayCopy);
                    MaterialButton materialButton4 = materialButton;
                    MonthsPagerAdapter monthsPagerAdapter3 = monthsPagerAdapter;
                    Objects.requireNonNull(monthsPagerAdapter3);
                    CalendarConstraints calendarConstraints3 = monthsPagerAdapter3.calendarConstraints;
                    Objects.requireNonNull(calendarConstraints3);
                    Month month3 = calendarConstraints3.start;
                    Objects.requireNonNull(month3);
                    Calendar dayCopy2 = UtcDates.getDayCopy(month3.firstOfMonth);
                    dayCopy2.add(2, i6);
                    materialButton4.setText(new Month(dayCopy2).getLongName(monthsPagerAdapter3.context));
                }
            });
            materialButton.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.material.datepicker.MaterialCalendar.7
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    MaterialCalendar materialCalendar = MaterialCalendar.this;
                    Objects.requireNonNull(materialCalendar);
                    CalendarSelector calendarSelector = CalendarSelector.DAY;
                    CalendarSelector calendarSelector2 = materialCalendar.calendarSelector;
                    CalendarSelector calendarSelector3 = CalendarSelector.YEAR;
                    if (calendarSelector2 == calendarSelector3) {
                        materialCalendar.setSelector(calendarSelector);
                    } else if (calendarSelector2 == calendarSelector) {
                        materialCalendar.setSelector(calendarSelector3);
                    }
                }
            });
            materialButton3.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.material.datepicker.MaterialCalendar.8
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    MaterialCalendar materialCalendar = MaterialCalendar.this;
                    Objects.requireNonNull(materialCalendar);
                    RecyclerView recyclerView2 = materialCalendar.recyclerView;
                    Objects.requireNonNull(recyclerView2);
                    int findFirstVisibleItemPosition = ((LinearLayoutManager) recyclerView2.mLayout).findFirstVisibleItemPosition() + 1;
                    RecyclerView recyclerView3 = MaterialCalendar.this.recyclerView;
                    Objects.requireNonNull(recyclerView3);
                    if (findFirstVisibleItemPosition < recyclerView3.mAdapter.getItemCount()) {
                        MaterialCalendar materialCalendar2 = MaterialCalendar.this;
                        MonthsPagerAdapter monthsPagerAdapter2 = monthsPagerAdapter;
                        Objects.requireNonNull(monthsPagerAdapter2);
                        CalendarConstraints calendarConstraints2 = monthsPagerAdapter2.calendarConstraints;
                        Objects.requireNonNull(calendarConstraints2);
                        Month month2 = calendarConstraints2.start;
                        Objects.requireNonNull(month2);
                        Calendar dayCopy = UtcDates.getDayCopy(month2.firstOfMonth);
                        dayCopy.add(2, findFirstVisibleItemPosition);
                        materialCalendar2.setCurrentMonth(new Month(dayCopy));
                    }
                }
            });
            materialButton2.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.material.datepicker.MaterialCalendar.9
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    MaterialCalendar materialCalendar = MaterialCalendar.this;
                    Objects.requireNonNull(materialCalendar);
                    RecyclerView recyclerView2 = materialCalendar.recyclerView;
                    Objects.requireNonNull(recyclerView2);
                    int findLastVisibleItemPosition = ((LinearLayoutManager) recyclerView2.mLayout).findLastVisibleItemPosition() - 1;
                    if (findLastVisibleItemPosition >= 0) {
                        MaterialCalendar materialCalendar2 = MaterialCalendar.this;
                        MonthsPagerAdapter monthsPagerAdapter2 = monthsPagerAdapter;
                        Objects.requireNonNull(monthsPagerAdapter2);
                        CalendarConstraints calendarConstraints2 = monthsPagerAdapter2.calendarConstraints;
                        Objects.requireNonNull(calendarConstraints2);
                        Month month2 = calendarConstraints2.start;
                        Objects.requireNonNull(month2);
                        Calendar dayCopy = UtcDates.getDayCopy(month2.firstOfMonth);
                        dayCopy.add(2, findLastVisibleItemPosition);
                        materialCalendar2.setCurrentMonth(new Month(dayCopy));
                    }
                }
            });
        }
        if (!MaterialDatePicker.isFullscreen(contextThemeWrapper)) {
            new PagerSnapHelper().attachToRecyclerView(this.recyclerView);
        }
        RecyclerView recyclerView2 = this.recyclerView;
        Month month2 = this.current;
        CalendarConstraints calendarConstraints2 = monthsPagerAdapter.calendarConstraints;
        Objects.requireNonNull(calendarConstraints2);
        Month month3 = calendarConstraints2.start;
        Objects.requireNonNull(month3);
        if (month3.firstOfMonth instanceof GregorianCalendar) {
            recyclerView2.scrollToPosition((month2.month - month3.month) + ((month2.year - month3.year) * 12));
            return inflate;
        }
        throw new IllegalArgumentException("Only Gregorian calendars are supported.");
    }

    @Override // androidx.fragment.app.Fragment
    public final void onSaveInstanceState(Bundle bundle) {
        bundle.putInt("THEME_RES_ID_KEY", this.themeResId);
        bundle.putParcelable("GRID_SELECTOR_KEY", this.dateSelector);
        bundle.putParcelable("CALENDAR_CONSTRAINTS_KEY", this.calendarConstraints);
        bundle.putParcelable("CURRENT_MONTH_KEY", this.current);
    }

    public final void setCurrentMonth(Month month) {
        boolean z;
        RecyclerView recyclerView = this.recyclerView;
        Objects.requireNonNull(recyclerView);
        MonthsPagerAdapter monthsPagerAdapter = (MonthsPagerAdapter) recyclerView.mAdapter;
        Objects.requireNonNull(monthsPagerAdapter);
        CalendarConstraints calendarConstraints = monthsPagerAdapter.calendarConstraints;
        Objects.requireNonNull(calendarConstraints);
        Month month2 = calendarConstraints.start;
        Objects.requireNonNull(month2);
        if (month2.firstOfMonth instanceof GregorianCalendar) {
            final int i = (month.month - month2.month) + ((month.year - month2.year) * 12);
            Month month3 = this.current;
            CalendarConstraints calendarConstraints2 = monthsPagerAdapter.calendarConstraints;
            Objects.requireNonNull(calendarConstraints2);
            Month month4 = calendarConstraints2.start;
            Objects.requireNonNull(month4);
            if (month4.firstOfMonth instanceof GregorianCalendar) {
                int i2 = i - ((month3.month - month4.month) + ((month3.year - month4.year) * 12));
                boolean z2 = true;
                if (Math.abs(i2) > 3) {
                    z = true;
                } else {
                    z = false;
                }
                if (i2 <= 0) {
                    z2 = false;
                }
                this.current = month;
                if (z && z2) {
                    this.recyclerView.scrollToPosition(i - 3);
                    this.recyclerView.post(new Runnable() { // from class: com.google.android.material.datepicker.MaterialCalendar.10
                        @Override // java.lang.Runnable
                        public final void run() {
                            MaterialCalendar.this.recyclerView.smoothScrollToPosition(i);
                        }
                    });
                } else if (z) {
                    this.recyclerView.scrollToPosition(i + 3);
                    this.recyclerView.post(new Runnable() { // from class: com.google.android.material.datepicker.MaterialCalendar.10
                        @Override // java.lang.Runnable
                        public final void run() {
                            MaterialCalendar.this.recyclerView.smoothScrollToPosition(i);
                        }
                    });
                } else {
                    this.recyclerView.post(new Runnable() { // from class: com.google.android.material.datepicker.MaterialCalendar.10
                        @Override // java.lang.Runnable
                        public final void run() {
                            MaterialCalendar.this.recyclerView.smoothScrollToPosition(i);
                        }
                    });
                }
            } else {
                throw new IllegalArgumentException("Only Gregorian calendars are supported.");
            }
        } else {
            throw new IllegalArgumentException("Only Gregorian calendars are supported.");
        }
    }

    public final void setSelector(CalendarSelector calendarSelector) {
        this.calendarSelector = calendarSelector;
        if (calendarSelector == CalendarSelector.YEAR) {
            RecyclerView recyclerView = this.yearSelector;
            Objects.requireNonNull(recyclerView);
            RecyclerView.LayoutManager layoutManager = recyclerView.mLayout;
            RecyclerView recyclerView2 = this.yearSelector;
            Objects.requireNonNull(recyclerView2);
            YearGridAdapter yearGridAdapter = (YearGridAdapter) recyclerView2.mAdapter;
            int i = this.current.year;
            Objects.requireNonNull(yearGridAdapter);
            MaterialCalendar<?> materialCalendar = yearGridAdapter.materialCalendar;
            Objects.requireNonNull(materialCalendar);
            CalendarConstraints calendarConstraints = materialCalendar.calendarConstraints;
            Objects.requireNonNull(calendarConstraints);
            layoutManager.scrollToPosition(i - calendarConstraints.start.year);
            this.yearFrame.setVisibility(0);
            this.dayFrame.setVisibility(8);
        } else if (calendarSelector == CalendarSelector.DAY) {
            this.yearFrame.setVisibility(8);
            this.dayFrame.setVisibility(0);
            setCurrentMonth(this.current);
        }
    }

    @Override // com.google.android.material.datepicker.PickerFragment
    public final boolean addOnSelectionChangedListener(MaterialDatePicker.AnonymousClass3 r1) {
        return super.addOnSelectionChangedListener(r1);
    }

    @Override // androidx.fragment.app.Fragment
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            bundle = this.mArguments;
        }
        this.themeResId = bundle.getInt("THEME_RES_ID_KEY");
        this.dateSelector = (DateSelector) bundle.getParcelable("GRID_SELECTOR_KEY");
        this.calendarConstraints = (CalendarConstraints) bundle.getParcelable("CALENDAR_CONSTRAINTS_KEY");
        this.current = (Month) bundle.getParcelable("CURRENT_MONTH_KEY");
    }
}
