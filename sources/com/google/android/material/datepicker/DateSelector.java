package com.google.android.material.datepicker;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.datepicker.MaterialTextInputPicker;
import java.util.ArrayList;
/* loaded from: classes.dex */
public interface DateSelector<S> extends Parcelable {
    int getDefaultThemeResId(Context context);

    ArrayList getSelectedDays();

    ArrayList getSelectedRanges();

    S getSelection();

    String getSelectionDisplayString(Context context);

    boolean isSelectionComplete();

    View onCreateTextInputView(LayoutInflater layoutInflater, ViewGroup viewGroup, CalendarConstraints calendarConstraints, MaterialTextInputPicker.AnonymousClass1 r4);

    void select(long j);
}
