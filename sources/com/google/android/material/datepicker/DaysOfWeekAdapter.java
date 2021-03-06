package com.google.android.material.datepicker;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Locale;
/* loaded from: classes.dex */
public final class DaysOfWeekAdapter extends BaseAdapter {
    public final Calendar calendar;
    public final int daysInWeek;
    public final int firstDayOfWeek;

    @Override // android.widget.Adapter
    public final long getItemId(int i) {
        return 0L;
    }

    @Override // android.widget.Adapter
    @SuppressLint({"WrongConstant"})
    public final View getView(int i, View view, ViewGroup viewGroup) {
        TextView textView = (TextView) view;
        if (view == null) {
            textView = (TextView) LayoutInflater.from(viewGroup.getContext()).inflate(2131624293, viewGroup, false);
        }
        Calendar calendar = this.calendar;
        int i2 = i + this.firstDayOfWeek;
        int i3 = this.daysInWeek;
        if (i2 > i3) {
            i2 -= i3;
        }
        calendar.set(7, i2);
        textView.setText(this.calendar.getDisplayName(7, 4, textView.getResources().getConfiguration().locale));
        textView.setContentDescription(String.format(viewGroup.getContext().getString(2131952837), this.calendar.getDisplayName(7, 2, Locale.getDefault())));
        return textView;
    }

    @Override // android.widget.Adapter
    public final Object getItem(int i) {
        int i2 = this.daysInWeek;
        if (i >= i2) {
            return null;
        }
        int i3 = i + this.firstDayOfWeek;
        if (i3 > i2) {
            i3 -= i2;
        }
        return Integer.valueOf(i3);
    }

    public DaysOfWeekAdapter() {
        Calendar utcCalendarOf = UtcDates.getUtcCalendarOf(null);
        this.calendar = utcCalendarOf;
        this.daysInWeek = utcCalendarOf.getMaximum(7);
        this.firstDayOfWeek = utcCalendarOf.getFirstDayOfWeek();
    }

    @Override // android.widget.Adapter
    public final int getCount() {
        return this.daysInWeek;
    }
}
