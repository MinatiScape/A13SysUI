package com.google.android.material.datepicker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.datepicker.MaterialCalendar;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
/* loaded from: classes.dex */
public final class YearGridAdapter extends RecyclerView.Adapter<ViewHolder> {
    public final MaterialCalendar<?> materialCalendar;

    /* loaded from: classes.dex */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;

        public ViewHolder(TextView textView) {
            super(textView);
            this.textView = textView;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final int getItemCount() {
        MaterialCalendar<?> materialCalendar = this.materialCalendar;
        Objects.requireNonNull(materialCalendar);
        CalendarConstraints calendarConstraints = materialCalendar.calendarConstraints;
        Objects.requireNonNull(calendarConstraints);
        return calendarConstraints.yearSpan;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final void onBindViewHolder(ViewHolder viewHolder, int i) {
        CalendarItemStyle calendarItemStyle;
        ViewHolder viewHolder2 = viewHolder;
        MaterialCalendar<?> materialCalendar = this.materialCalendar;
        Objects.requireNonNull(materialCalendar);
        CalendarConstraints calendarConstraints = materialCalendar.calendarConstraints;
        Objects.requireNonNull(calendarConstraints);
        final int i2 = calendarConstraints.start.year + i;
        String string = viewHolder2.textView.getContext().getString(2131952842);
        viewHolder2.textView.setText(String.format(Locale.getDefault(), "%d", Integer.valueOf(i2)));
        viewHolder2.textView.setContentDescription(String.format(string, Integer.valueOf(i2)));
        MaterialCalendar<?> materialCalendar2 = this.materialCalendar;
        Objects.requireNonNull(materialCalendar2);
        CalendarStyle calendarStyle = materialCalendar2.calendarStyle;
        Calendar todayCalendar = UtcDates.getTodayCalendar();
        if (todayCalendar.get(1) == i2) {
            calendarItemStyle = calendarStyle.todayYear;
        } else {
            calendarItemStyle = calendarStyle.year;
        }
        MaterialCalendar<?> materialCalendar3 = this.materialCalendar;
        Objects.requireNonNull(materialCalendar3);
        for (Long l : materialCalendar3.dateSelector.getSelectedDays()) {
            todayCalendar.setTimeInMillis(l.longValue());
            if (todayCalendar.get(1) == i2) {
                calendarItemStyle = calendarStyle.selectedYear;
            }
        }
        calendarItemStyle.styleItem(viewHolder2.textView);
        viewHolder2.textView.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.material.datepicker.YearGridAdapter.1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                int i3 = i2;
                MaterialCalendar<?> materialCalendar4 = YearGridAdapter.this.materialCalendar;
                Objects.requireNonNull(materialCalendar4);
                Month create = Month.create(i3, materialCalendar4.current.month);
                MaterialCalendar<?> materialCalendar5 = YearGridAdapter.this.materialCalendar;
                Objects.requireNonNull(materialCalendar5);
                CalendarConstraints calendarConstraints2 = materialCalendar5.calendarConstraints;
                Objects.requireNonNull(calendarConstraints2);
                if (create.firstOfMonth.compareTo(calendarConstraints2.start.firstOfMonth) < 0) {
                    create = calendarConstraints2.start;
                } else {
                    if (create.firstOfMonth.compareTo(calendarConstraints2.end.firstOfMonth) > 0) {
                        create = calendarConstraints2.end;
                    }
                }
                YearGridAdapter.this.materialCalendar.setCurrentMonth(create);
                YearGridAdapter.this.materialCalendar.setSelector(MaterialCalendar.CalendarSelector.DAY);
            }
        });
    }

    public YearGridAdapter(MaterialCalendar<?> materialCalendar) {
        this.materialCalendar = materialCalendar;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final RecyclerView.ViewHolder onCreateViewHolder(RecyclerView recyclerView, int i) {
        return new ViewHolder((TextView) LayoutInflater.from(recyclerView.getContext()).inflate(2131624301, (ViewGroup) recyclerView, false));
    }
}
