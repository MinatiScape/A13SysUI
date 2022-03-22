package com.google.android.material.datepicker;

import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MonthAdapter extends BaseAdapter {
    public static final int MAXIMUM_WEEKS = UtcDates.getUtcCalendarOf(null).getMaximum(4);
    public final CalendarConstraints calendarConstraints;
    public CalendarStyle calendarStyle;
    public final DateSelector<?> dateSelector;
    public final Month month;
    public Collection<Long> previouslySelectedDates;

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public final boolean hasStableIds() {
        return true;
    }

    public final int firstPositionInMonth() {
        return this.month.daysFromStartOfWeekToFirstOfMonth();
    }

    @Override // android.widget.Adapter
    public final int getCount() {
        return firstPositionInMonth() + this.month.daysInMonth;
    }

    @Override // android.widget.Adapter
    public final Long getItem(int i) {
        if (i < this.month.daysFromStartOfWeekToFirstOfMonth() || i > lastPositionInMonth()) {
            return null;
        }
        Month month = this.month;
        return Long.valueOf(month.getDay((i - month.daysFromStartOfWeekToFirstOfMonth()) + 1));
    }

    @Override // android.widget.Adapter
    public final long getItemId(int i) {
        return i / this.month.daysInWeek;
    }

    public final int lastPositionInMonth() {
        return (this.month.daysFromStartOfWeekToFirstOfMonth() + this.month.daysInMonth) - 1;
    }

    public final void updateSelectedState(TextView textView, long j) {
        CalendarItemStyle calendarItemStyle;
        if (textView != null) {
            CalendarConstraints calendarConstraints = this.calendarConstraints;
            Objects.requireNonNull(calendarConstraints);
            boolean z = false;
            if (calendarConstraints.validator.isValid(j)) {
                textView.setEnabled(true);
                Iterator it = this.dateSelector.getSelectedDays().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    if (UtcDates.canonicalYearMonthDay(j) == UtcDates.canonicalYearMonthDay(((Long) it.next()).longValue())) {
                        z = true;
                        break;
                    }
                }
                if (z) {
                    calendarItemStyle = this.calendarStyle.selectedDay;
                } else if (UtcDates.getTodayCalendar().getTimeInMillis() == j) {
                    calendarItemStyle = this.calendarStyle.todayDay;
                } else {
                    calendarItemStyle = this.calendarStyle.day;
                }
            } else {
                textView.setEnabled(false);
                calendarItemStyle = this.calendarStyle.invalidDay;
            }
            calendarItemStyle.styleItem(textView);
        }
    }

    public MonthAdapter(Month month, DateSelector<?> dateSelector, CalendarConstraints calendarConstraints) {
        this.month = month;
        this.dateSelector = dateSelector;
        this.calendarConstraints = calendarConstraints;
        this.previouslySelectedDates = dateSelector.getSelectedDays();
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x00b1  */
    @Override // android.widget.Adapter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final android.view.View getView(int r7, android.view.View r8, android.view.ViewGroup r9) {
        /*
            r6 = this;
            android.content.Context r0 = r9.getContext()
            com.google.android.material.datepicker.CalendarStyle r1 = r6.calendarStyle
            if (r1 != 0) goto L_0x000f
            com.google.android.material.datepicker.CalendarStyle r1 = new com.google.android.material.datepicker.CalendarStyle
            r1.<init>(r0)
            r6.calendarStyle = r1
        L_0x000f:
            r0 = r8
            android.widget.TextView r0 = (android.widget.TextView) r0
            r1 = 0
            if (r8 != 0) goto L_0x0027
            android.content.Context r8 = r9.getContext()
            android.view.LayoutInflater r8 = android.view.LayoutInflater.from(r8)
            r0 = 2131624292(0x7f0e0164, float:1.887576E38)
            android.view.View r8 = r8.inflate(r0, r9, r1)
            r0 = r8
            android.widget.TextView r0 = (android.widget.TextView) r0
        L_0x0027:
            int r8 = r6.firstPositionInMonth()
            int r8 = r7 - r8
            if (r8 < 0) goto L_0x00a2
            com.google.android.material.datepicker.Month r9 = r6.month
            int r2 = r9.daysInMonth
            if (r8 < r2) goto L_0x0036
            goto L_0x00a2
        L_0x0036:
            r2 = 1
            int r8 = r8 + r2
            r0.setTag(r9)
            android.content.res.Resources r9 = r0.getResources()
            android.content.res.Configuration r9 = r9.getConfiguration()
            java.util.Locale r9 = r9.locale
            java.lang.Object[] r3 = new java.lang.Object[r2]
            java.lang.Integer r4 = java.lang.Integer.valueOf(r8)
            r3[r1] = r4
            java.lang.String r4 = "%d"
            java.lang.String r9 = java.lang.String.format(r9, r4, r3)
            r0.setText(r9)
            com.google.android.material.datepicker.Month r9 = r6.month
            long r8 = r9.getDay(r8)
            com.google.android.material.datepicker.Month r3 = r6.month
            int r3 = r3.year
            com.google.android.material.datepicker.Month r4 = new com.google.android.material.datepicker.Month
            java.util.Calendar r5 = com.google.android.material.datepicker.UtcDates.getTodayCalendar()
            r4.<init>(r5)
            int r4 = r4.year
            if (r3 != r4) goto L_0x0084
            java.util.Locale r3 = java.util.Locale.getDefault()
            java.lang.String r4 = "MMMEd"
            android.icu.text.DateFormat r3 = com.google.android.material.datepicker.UtcDates.getAndroidFormat(r4, r3)
            java.util.Date r4 = new java.util.Date
            r4.<init>(r8)
            java.lang.String r8 = r3.format(r4)
            r0.setContentDescription(r8)
            goto L_0x009b
        L_0x0084:
            java.util.Locale r3 = java.util.Locale.getDefault()
            java.lang.String r4 = "yMMMEd"
            android.icu.text.DateFormat r3 = com.google.android.material.datepicker.UtcDates.getAndroidFormat(r4, r3)
            java.util.Date r4 = new java.util.Date
            r4.<init>(r8)
            java.lang.String r8 = r3.format(r4)
            r0.setContentDescription(r8)
        L_0x009b:
            r0.setVisibility(r1)
            r0.setEnabled(r2)
            goto L_0x00aa
        L_0x00a2:
            r8 = 8
            r0.setVisibility(r8)
            r0.setEnabled(r1)
        L_0x00aa:
            java.lang.Long r7 = r6.getItem(r7)
            if (r7 != 0) goto L_0x00b1
            goto L_0x00b8
        L_0x00b1:
            long r7 = r7.longValue()
            r6.updateSelectedState(r0, r7)
        L_0x00b8:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.datepicker.MonthAdapter.getView(int, android.view.View, android.view.ViewGroup):android.view.View");
    }

    public final void updateSelectedStateForDate(MaterialCalendarGridView materialCalendarGridView, long j) {
        if (Month.create(j).equals(this.month)) {
            Month month = this.month;
            Objects.requireNonNull(month);
            Calendar dayCopy = UtcDates.getDayCopy(month.firstOfMonth);
            dayCopy.setTimeInMillis(j);
            int i = dayCopy.get(5);
            MonthAdapter adapter = materialCalendarGridView.getAdapter2();
            Objects.requireNonNull(adapter);
            updateSelectedState((TextView) materialCalendarGridView.getChildAt((adapter.firstPositionInMonth() + (i - 1)) - materialCalendarGridView.getFirstVisiblePosition()), j);
        }
    }
}
