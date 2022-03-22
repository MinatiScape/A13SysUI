package com.google.android.material.datepicker;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.core.util.Pair;
import com.google.android.material.datepicker.MaterialTextInputPicker;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.textfield.TextInputLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import okio.Okio;
/* loaded from: classes.dex */
public class RangeDateSelector implements DateSelector<Pair<Long, Long>> {
    public static final Parcelable.Creator<RangeDateSelector> CREATOR = new Parcelable.Creator<RangeDateSelector>() { // from class: com.google.android.material.datepicker.RangeDateSelector.3
        @Override // android.os.Parcelable.Creator
        public final RangeDateSelector createFromParcel(Parcel parcel) {
            RangeDateSelector rangeDateSelector = new RangeDateSelector();
            rangeDateSelector.selectedStartItem = (Long) parcel.readValue(Long.class.getClassLoader());
            rangeDateSelector.selectedEndItem = (Long) parcel.readValue(Long.class.getClassLoader());
            return rangeDateSelector;
        }

        @Override // android.os.Parcelable.Creator
        public final RangeDateSelector[] newArray(int i) {
            return new RangeDateSelector[i];
        }
    };
    public String invalidRangeStartError;
    public Long selectedStartItem = null;
    public Long selectedEndItem = null;
    public Long proposedTextStart = null;
    public Long proposedTextEnd = null;

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public final ArrayList getSelectedDays() {
        ArrayList arrayList = new ArrayList();
        Long l = this.selectedStartItem;
        if (l != null) {
            arrayList.add(l);
        }
        Long l2 = this.selectedEndItem;
        if (l2 != null) {
            arrayList.add(l2);
        }
        return arrayList;
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public final ArrayList getSelectedRanges() {
        if (this.selectedStartItem == null || this.selectedEndItem == null) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new Pair(this.selectedStartItem, this.selectedEndItem));
        return arrayList;
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public final Pair<Long, Long> getSelection() {
        return new Pair<>(this.selectedStartItem, this.selectedEndItem);
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public final boolean isSelectionComplete() {
        boolean z;
        Long l = this.selectedStartItem;
        if (!(l == null || this.selectedEndItem == null)) {
            if (l.longValue() <= this.selectedEndItem.longValue()) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                return true;
            }
        }
        return false;
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public final View onCreateTextInputView(LayoutInflater layoutInflater, ViewGroup viewGroup, CalendarConstraints calendarConstraints, final MaterialTextInputPicker.AnonymousClass1 r21) {
        View inflate = layoutInflater.inflate(2131624314, viewGroup, false);
        final TextInputLayout textInputLayout = (TextInputLayout) inflate.findViewById(2131428469);
        final TextInputLayout textInputLayout2 = (TextInputLayout) inflate.findViewById(2131428468);
        Objects.requireNonNull(textInputLayout);
        EditText editText = textInputLayout.editText;
        Objects.requireNonNull(textInputLayout2);
        EditText editText2 = textInputLayout2.editText;
        if (Okio.isDateInputKeyboardMissingSeparatorCharacters()) {
            editText.setInputType(17);
            editText2.setInputType(17);
        }
        this.invalidRangeStartError = inflate.getResources().getString(2131952841);
        SimpleDateFormat textInputFormat = UtcDates.getTextInputFormat();
        Long l = this.selectedStartItem;
        if (l != null) {
            editText.setText(textInputFormat.format(l));
            this.proposedTextStart = this.selectedStartItem;
        }
        Long l2 = this.selectedEndItem;
        if (l2 != null) {
            editText2.setText(textInputFormat.format(l2));
            this.proposedTextEnd = this.selectedEndItem;
        }
        String textInputHint = UtcDates.getTextInputHint(inflate.getResources(), textInputFormat);
        textInputLayout.setPlaceholderText(textInputHint);
        textInputLayout2.setPlaceholderText(textInputHint);
        editText.addTextChangedListener(new DateFormatTextWatcher(textInputHint, textInputFormat, textInputLayout, calendarConstraints) { // from class: com.google.android.material.datepicker.RangeDateSelector.1
            @Override // com.google.android.material.datepicker.DateFormatTextWatcher
            public final void onInvalidDate() {
                RangeDateSelector rangeDateSelector = RangeDateSelector.this;
                rangeDateSelector.proposedTextStart = null;
                RangeDateSelector.access$100(rangeDateSelector, textInputLayout, textInputLayout2, r21);
            }

            @Override // com.google.android.material.datepicker.DateFormatTextWatcher
            public final void onValidDate(Long l3) {
                RangeDateSelector rangeDateSelector = RangeDateSelector.this;
                rangeDateSelector.proposedTextStart = l3;
                RangeDateSelector.access$100(rangeDateSelector, textInputLayout, textInputLayout2, r21);
            }
        });
        editText2.addTextChangedListener(new DateFormatTextWatcher(textInputHint, textInputFormat, textInputLayout2, calendarConstraints) { // from class: com.google.android.material.datepicker.RangeDateSelector.2
            @Override // com.google.android.material.datepicker.DateFormatTextWatcher
            public final void onInvalidDate() {
                RangeDateSelector rangeDateSelector = RangeDateSelector.this;
                rangeDateSelector.proposedTextEnd = null;
                RangeDateSelector.access$100(rangeDateSelector, textInputLayout, textInputLayout2, r21);
            }

            @Override // com.google.android.material.datepicker.DateFormatTextWatcher
            public final void onValidDate(Long l3) {
                RangeDateSelector rangeDateSelector = RangeDateSelector.this;
                rangeDateSelector.proposedTextEnd = l3;
                RangeDateSelector.access$100(rangeDateSelector, textInputLayout, textInputLayout2, r21);
            }
        });
        editText.requestFocus();
        editText.post(new ViewUtils.AnonymousClass1(editText));
        return inflate;
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public final void select(long j) {
        boolean z;
        Long l = this.selectedStartItem;
        if (l == null) {
            this.selectedStartItem = Long.valueOf(j);
            return;
        }
        if (this.selectedEndItem == null) {
            if (l.longValue() <= j) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                this.selectedEndItem = Long.valueOf(j);
                return;
            }
        }
        this.selectedEndItem = null;
        this.selectedStartItem = Long.valueOf(j);
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(this.selectedStartItem);
        parcel.writeValue(this.selectedEndItem);
    }

    public static void access$100(RangeDateSelector rangeDateSelector, TextInputLayout textInputLayout, TextInputLayout textInputLayout2, OnSelectionChangedListener onSelectionChangedListener) {
        boolean z;
        Objects.requireNonNull(rangeDateSelector);
        Long l = rangeDateSelector.proposedTextStart;
        if (l == null || rangeDateSelector.proposedTextEnd == null) {
            if (textInputLayout.getError() != null && rangeDateSelector.invalidRangeStartError.contentEquals(textInputLayout.getError())) {
                textInputLayout.setError(null);
            }
            if (textInputLayout2.getError() != null && " ".contentEquals(textInputLayout2.getError())) {
                textInputLayout2.setError(null);
            }
            onSelectionChangedListener.onIncompleteSelectionChanged();
            return;
        }
        if (l.longValue() <= rangeDateSelector.proposedTextEnd.longValue()) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            Long l2 = rangeDateSelector.proposedTextStart;
            rangeDateSelector.selectedStartItem = l2;
            Long l3 = rangeDateSelector.proposedTextEnd;
            rangeDateSelector.selectedEndItem = l3;
            onSelectionChangedListener.onSelectionChanged(new Pair(l2, l3));
            return;
        }
        textInputLayout.setError(rangeDateSelector.invalidRangeStartError);
        textInputLayout2.setError(" ");
        onSelectionChangedListener.onIncompleteSelectionChanged();
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public final int getDefaultThemeResId(Context context) {
        int i;
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels) > resources.getDimensionPixelSize(2131166451)) {
            i = 2130969429;
        } else {
            i = 2130969418;
        }
        return MaterialAttributes.resolveOrThrow(context, i, MaterialDatePicker.class.getCanonicalName());
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public final String getSelectionDisplayString(Context context) {
        Pair pair;
        Resources resources = context.getResources();
        Long l = this.selectedStartItem;
        if (l == null && this.selectedEndItem == null) {
            return resources.getString(2131952848);
        }
        Long l2 = this.selectedEndItem;
        if (l2 == null) {
            return resources.getString(2131952845, DateStrings.getDateString$1(l.longValue()));
        }
        if (l == null) {
            return resources.getString(2131952844, DateStrings.getDateString$1(l2.longValue()));
        }
        Calendar todayCalendar = UtcDates.getTodayCalendar();
        Calendar utcCalendarOf = UtcDates.getUtcCalendarOf(null);
        utcCalendarOf.setTimeInMillis(l.longValue());
        Calendar utcCalendarOf2 = UtcDates.getUtcCalendarOf(null);
        utcCalendarOf2.setTimeInMillis(l2.longValue());
        if (utcCalendarOf.get(1) != utcCalendarOf2.get(1)) {
            pair = new Pair(DateStrings.getYearMonthDay(l.longValue(), Locale.getDefault()), DateStrings.getYearMonthDay(l2.longValue(), Locale.getDefault()));
        } else if (utcCalendarOf.get(1) == todayCalendar.get(1)) {
            pair = new Pair(DateStrings.getMonthDay(l.longValue(), Locale.getDefault()), DateStrings.getMonthDay(l2.longValue(), Locale.getDefault()));
        } else {
            pair = new Pair(DateStrings.getMonthDay(l.longValue(), Locale.getDefault()), DateStrings.getYearMonthDay(l2.longValue(), Locale.getDefault()));
        }
        return resources.getString(2131952846, pair.first, pair.second);
    }
}
