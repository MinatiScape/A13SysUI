package androidx.leanback.widget.picker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.leanback.R$styleable;
import androidx.leanback.widget.picker.PickerUtility;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class DatePicker extends Picker {
    public static final int[] DATE_FIELDS = {5, 2, 1};
    public int mColDayIndex;
    public int mColMonthIndex;
    public int mColYearIndex;
    public PickerUtility.DateConstant mConstant;
    public Calendar mCurrentDate;
    public final SimpleDateFormat mDateFormat;
    public String mDatePickerFormat;
    public PickerColumn mDayColumn;
    public Calendar mMaxDate;
    public Calendar mMinDate;
    public PickerColumn mMonthColumn;
    public Calendar mTempDate;
    public PickerColumn mYearColumn;

    /* renamed from: androidx.leanback.widget.picker.DatePicker$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass1 implements Runnable {
        public final /* synthetic */ boolean val$animation = false;

        public AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            PickerColumn pickerColumn;
            boolean z;
            boolean z2;
            boolean z3;
            boolean z4;
            DatePicker datePicker = DatePicker.this;
            boolean z5 = this.val$animation;
            Objects.requireNonNull(datePicker);
            int[] iArr = {datePicker.mColDayIndex, datePicker.mColMonthIndex, datePicker.mColYearIndex};
            boolean z6 = true;
            boolean z7 = true;
            for (int i = 2; i >= 0; i--) {
                if (iArr[i] >= 0) {
                    int i2 = DatePicker.DATE_FIELDS[i];
                    int i3 = iArr[i];
                    ArrayList<PickerColumn> arrayList = datePicker.mColumns;
                    if (arrayList == null) {
                        pickerColumn = null;
                    } else {
                        pickerColumn = arrayList.get(i3);
                    }
                    if (z6) {
                        int i4 = datePicker.mMinDate.get(i2);
                        Objects.requireNonNull(pickerColumn);
                        if (i4 != pickerColumn.mMinValue) {
                            pickerColumn.mMinValue = i4;
                            z = true;
                        }
                        z = false;
                    } else {
                        int actualMinimum = datePicker.mCurrentDate.getActualMinimum(i2);
                        Objects.requireNonNull(pickerColumn);
                        if (actualMinimum != pickerColumn.mMinValue) {
                            pickerColumn.mMinValue = actualMinimum;
                            z = true;
                        }
                        z = false;
                    }
                    boolean z8 = z | false;
                    if (z7) {
                        int i5 = datePicker.mMaxDate.get(i2);
                        Objects.requireNonNull(pickerColumn);
                        if (i5 != pickerColumn.mMaxValue) {
                            pickerColumn.mMaxValue = i5;
                            z2 = true;
                        }
                        z2 = false;
                    } else {
                        int actualMaximum = datePicker.mCurrentDate.getActualMaximum(i2);
                        Objects.requireNonNull(pickerColumn);
                        if (actualMaximum != pickerColumn.mMaxValue) {
                            pickerColumn.mMaxValue = actualMaximum;
                            z2 = true;
                        }
                        z2 = false;
                    }
                    boolean z9 = z8 | z2;
                    if (datePicker.mCurrentDate.get(i2) == datePicker.mMinDate.get(i2)) {
                        z3 = true;
                    } else {
                        z3 = false;
                    }
                    z6 &= z3;
                    if (datePicker.mCurrentDate.get(i2) == datePicker.mMaxDate.get(i2)) {
                        z4 = true;
                    } else {
                        z4 = false;
                    }
                    z7 &= z4;
                    if (z9) {
                        datePicker.setColumnAt(iArr[i], pickerColumn);
                    }
                    datePicker.setColumnValue(iArr[i], datePicker.mCurrentDate.get(i2), z5);
                }
            }
        }
    }

    public DatePicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 2130968916);
    }

    /* JADX WARN: Finally extract failed */
    @SuppressLint({"CustomViewStyleable"})
    public DatePicker(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        Locale locale = Locale.getDefault();
        getContext().getResources();
        this.mConstant = new PickerUtility.DateConstant(locale);
        this.mTempDate = PickerUtility.getCalendarForLocale(this.mTempDate, locale);
        this.mMinDate = PickerUtility.getCalendarForLocale(this.mMinDate, this.mConstant.locale);
        this.mMaxDate = PickerUtility.getCalendarForLocale(this.mMaxDate, this.mConstant.locale);
        this.mCurrentDate = PickerUtility.getCalendarForLocale(this.mCurrentDate, this.mConstant.locale);
        PickerColumn pickerColumn = this.mMonthColumn;
        if (pickerColumn != null) {
            pickerColumn.mStaticLabels = this.mConstant.months;
            setColumnAt(this.mColMonthIndex, pickerColumn);
        }
        int[] iArr = R$styleable.lbDatePicker;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr);
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        ViewCompat.Api29Impl.saveAttributeDataForStyleable(this, context, iArr, attributeSet, obtainStyledAttributes, 0, 0);
        try {
            String string = obtainStyledAttributes.getString(0);
            String string2 = obtainStyledAttributes.getString(1);
            String string3 = obtainStyledAttributes.getString(2);
            obtainStyledAttributes.recycle();
            this.mTempDate.clear();
            if (TextUtils.isEmpty(string)) {
                this.mTempDate.set(1900, 0, 1);
            } else if (!parseDate(string, this.mTempDate)) {
                this.mTempDate.set(1900, 0, 1);
            }
            this.mMinDate.setTimeInMillis(this.mTempDate.getTimeInMillis());
            this.mTempDate.clear();
            if (TextUtils.isEmpty(string2)) {
                this.mTempDate.set(2100, 0, 1);
            } else if (!parseDate(string2, this.mTempDate)) {
                this.mTempDate.set(2100, 0, 1);
            }
            this.mMaxDate.setTimeInMillis(this.mTempDate.getTimeInMillis());
            string3 = TextUtils.isEmpty(string3) ? new String(DateFormat.getDateFormatOrder(context)) : string3;
            string3 = TextUtils.isEmpty(string3) ? new String(DateFormat.getDateFormatOrder(getContext())) : string3;
            if (!TextUtils.equals(this.mDatePickerFormat, string3)) {
                this.mDatePickerFormat = string3;
                List<CharSequence> extractSeparators = extractSeparators();
                if (extractSeparators.size() == string3.length() + 1) {
                    this.mSeparators.clear();
                    this.mSeparators.addAll(extractSeparators);
                    this.mDayColumn = null;
                    this.mMonthColumn = null;
                    this.mYearColumn = null;
                    this.mColMonthIndex = -1;
                    this.mColDayIndex = -1;
                    this.mColYearIndex = -1;
                    String upperCase = string3.toUpperCase(this.mConstant.locale);
                    ArrayList arrayList = new ArrayList(3);
                    for (int i2 = 0; i2 < upperCase.length(); i2++) {
                        char charAt = upperCase.charAt(i2);
                        if (charAt != 'D') {
                            if (charAt != 'M') {
                                if (charAt != 'Y') {
                                    throw new IllegalArgumentException("datePicker format error");
                                } else if (this.mYearColumn == null) {
                                    PickerColumn pickerColumn2 = new PickerColumn();
                                    this.mYearColumn = pickerColumn2;
                                    arrayList.add(pickerColumn2);
                                    this.mColYearIndex = i2;
                                    PickerColumn pickerColumn3 = this.mYearColumn;
                                    Objects.requireNonNull(pickerColumn3);
                                    pickerColumn3.mLabelFormat = "%d";
                                } else {
                                    throw new IllegalArgumentException("datePicker format error");
                                }
                            } else if (this.mMonthColumn == null) {
                                PickerColumn pickerColumn4 = new PickerColumn();
                                this.mMonthColumn = pickerColumn4;
                                arrayList.add(pickerColumn4);
                                PickerColumn pickerColumn5 = this.mMonthColumn;
                                String[] strArr = this.mConstant.months;
                                Objects.requireNonNull(pickerColumn5);
                                pickerColumn5.mStaticLabels = strArr;
                                this.mColMonthIndex = i2;
                            } else {
                                throw new IllegalArgumentException("datePicker format error");
                            }
                        } else if (this.mDayColumn == null) {
                            PickerColumn pickerColumn6 = new PickerColumn();
                            this.mDayColumn = pickerColumn6;
                            arrayList.add(pickerColumn6);
                            PickerColumn pickerColumn7 = this.mDayColumn;
                            Objects.requireNonNull(pickerColumn7);
                            pickerColumn7.mLabelFormat = "%02d";
                            this.mColDayIndex = i2;
                        } else {
                            throw new IllegalArgumentException("datePicker format error");
                        }
                    }
                    setColumns(arrayList);
                    post(new AnonymousClass1());
                    return;
                }
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Separators size: ");
                m.append(extractSeparators.size());
                m.append(" must equal the size of datePickerFormat: ");
                m.append(string3.length());
                m.append(" + 1");
                throw new IllegalStateException(m.toString());
            }
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    public List<CharSequence> extractSeparators() {
        String bestYearMonthDayPattern = getBestYearMonthDayPattern(this.mDatePickerFormat);
        ArrayList arrayList = new ArrayList();
        StringBuilder sb = new StringBuilder();
        char[] cArr = {'Y', 'y', 'M', 'm', 'D', 'd'};
        boolean z = false;
        char c = 0;
        for (int i = 0; i < bestYearMonthDayPattern.length(); i++) {
            char charAt = bestYearMonthDayPattern.charAt(i);
            boolean z2 = true;
            if (charAt != ' ') {
                if (charAt != '\'') {
                    if (z) {
                        sb.append(charAt);
                    } else {
                        int i2 = 0;
                        while (true) {
                            if (i2 >= 6) {
                                z2 = false;
                                break;
                            } else if (charAt == cArr[i2]) {
                                break;
                            } else {
                                i2++;
                            }
                        }
                        if (!z2) {
                            sb.append(charAt);
                        } else if (charAt != c) {
                            arrayList.add(sb.toString());
                            sb.setLength(0);
                        }
                    }
                    c = charAt;
                } else if (!z) {
                    sb.setLength(0);
                    z = true;
                } else {
                    z = false;
                }
            }
        }
        arrayList.add(sb.toString());
        return arrayList;
    }

    public String getBestYearMonthDayPattern(String str) {
        String bestDateTimePattern = DateFormat.getBestDateTimePattern(this.mConstant.locale, str);
        if (TextUtils.isEmpty(bestDateTimePattern)) {
            return "MM/dd/yyyy";
        }
        return bestDateTimePattern;
    }

    @Override // androidx.leanback.widget.picker.Picker
    public final void onColumnValueChanged(int i, int i2) {
        PickerColumn pickerColumn;
        this.mTempDate.setTimeInMillis(this.mCurrentDate.getTimeInMillis());
        ArrayList<PickerColumn> arrayList = this.mColumns;
        if (arrayList == null) {
            pickerColumn = null;
        } else {
            pickerColumn = arrayList.get(i);
        }
        Objects.requireNonNull(pickerColumn);
        int i3 = pickerColumn.mCurrentValue;
        boolean z = true;
        if (i == this.mColDayIndex) {
            this.mTempDate.add(5, i2 - i3);
        } else if (i == this.mColMonthIndex) {
            this.mTempDate.add(2, i2 - i3);
        } else if (i == this.mColYearIndex) {
            this.mTempDate.add(1, i2 - i3);
        } else {
            throw new IllegalArgumentException();
        }
        int i4 = this.mTempDate.get(1);
        int i5 = this.mTempDate.get(2);
        int i6 = this.mTempDate.get(5);
        if (this.mCurrentDate.get(1) == i4 && this.mCurrentDate.get(2) == i6 && this.mCurrentDate.get(5) == i5) {
            z = false;
        }
        if (z) {
            this.mCurrentDate.set(i4, i5, i6);
            if (this.mCurrentDate.before(this.mMinDate)) {
                this.mCurrentDate.setTimeInMillis(this.mMinDate.getTimeInMillis());
            } else if (this.mCurrentDate.after(this.mMaxDate)) {
                this.mCurrentDate.setTimeInMillis(this.mMaxDate.getTimeInMillis());
            }
            post(new AnonymousClass1());
        }
    }

    public final boolean parseDate(String str, Calendar calendar) {
        try {
            calendar.setTime(this.mDateFormat.parse(str));
            return true;
        } catch (ParseException unused) {
            Log.w("DatePicker", "Date: " + str + " not in format: MM/dd/yyyy");
            return false;
        }
    }
}
