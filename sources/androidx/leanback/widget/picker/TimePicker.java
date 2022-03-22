package androidx.leanback.widget.picker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import androidx.leanback.R$styleable;
import androidx.leanback.widget.picker.PickerUtility;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class TimePicker extends Picker {
    public PickerColumn mAmPmColumn;
    public int mColAmPmIndex;
    public int mColHourIndex;
    public int mColMinuteIndex;
    public final PickerUtility.TimeConstant mConstant;
    public int mCurrentAmPmIndex;
    public int mCurrentHour;
    public PickerColumn mHourColumn;
    public boolean mIs24hFormat;
    public PickerColumn mMinuteColumn;
    public String mTimePickerFormat;

    public TimePicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    /* JADX WARN: Finally extract failed */
    @SuppressLint({"CustomViewStyleable"})
    public TimePicker(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, 2130970032);
        boolean z;
        Locale locale = Locale.getDefault();
        context.getResources();
        this.mConstant = new PickerUtility.TimeConstant(locale);
        int[] iArr = R$styleable.lbTimePicker;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr);
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        ViewCompat.Api29Impl.saveAttributeDataForStyleable(this, context, iArr, attributeSet, obtainStyledAttributes, 0, 0);
        try {
            this.mIs24hFormat = obtainStyledAttributes.getBoolean(0, DateFormat.is24HourFormat(context));
            boolean z2 = obtainStyledAttributes.getBoolean(3, true);
            obtainStyledAttributes.recycle();
            String bestHourMinutePattern = getBestHourMinutePattern();
            if (!TextUtils.equals(bestHourMinutePattern, this.mTimePickerFormat)) {
                this.mTimePickerFormat = bestHourMinutePattern;
                String bestHourMinutePattern2 = getBestHourMinutePattern();
                boolean z3 = TextUtils.getLayoutDirectionFromLocale(locale) == 1;
                boolean z4 = bestHourMinutePattern2.indexOf(97) < 0 || bestHourMinutePattern2.indexOf("a") > bestHourMinutePattern2.indexOf("m");
                String str = z3 ? "mh" : "hm";
                if (!this.mIs24hFormat) {
                    StringBuilder sb = new StringBuilder();
                    if (z4) {
                        sb.append(str);
                        sb.append("a");
                    } else {
                        sb.append("a");
                        sb.append(str);
                    }
                    str = sb.toString();
                }
                String bestHourMinutePattern3 = getBestHourMinutePattern();
                ArrayList arrayList = new ArrayList();
                StringBuilder sb2 = new StringBuilder();
                int i2 = 7;
                char[] cArr = {'H', 'h', 'K', 'k', 'm', 'M', 'a'};
                int i3 = 0;
                boolean z5 = false;
                char c = 0;
                while (i3 < bestHourMinutePattern3.length()) {
                    char charAt = bestHourMinutePattern3.charAt(i3);
                    if (charAt != ' ') {
                        if (charAt != '\'') {
                            if (z5) {
                                sb2.append(charAt);
                            } else {
                                int i4 = 0;
                                while (true) {
                                    if (i4 >= i2) {
                                        z = false;
                                        break;
                                    } else if (charAt == cArr[i4]) {
                                        z = true;
                                        break;
                                    } else {
                                        i4++;
                                        i2 = 7;
                                    }
                                }
                                if (!z) {
                                    sb2.append(charAt);
                                } else if (charAt != c) {
                                    arrayList.add(sb2.toString());
                                    sb2.setLength(0);
                                }
                            }
                            c = charAt;
                        } else if (!z5) {
                            sb2.setLength(0);
                            z5 = true;
                        } else {
                            z5 = false;
                        }
                    }
                    i3++;
                    i2 = 7;
                    z5 = z5;
                }
                arrayList.add(sb2.toString());
                if (arrayList.size() == str.length() + 1) {
                    this.mSeparators.clear();
                    this.mSeparators.addAll(arrayList);
                    String upperCase = str.toUpperCase(this.mConstant.locale);
                    this.mAmPmColumn = null;
                    this.mMinuteColumn = null;
                    this.mHourColumn = null;
                    this.mColAmPmIndex = -1;
                    this.mColMinuteIndex = -1;
                    this.mColHourIndex = -1;
                    ArrayList arrayList2 = new ArrayList(3);
                    for (int i5 = 0; i5 < upperCase.length(); i5++) {
                        char charAt2 = upperCase.charAt(i5);
                        if (charAt2 == 'A') {
                            PickerColumn pickerColumn = new PickerColumn();
                            this.mAmPmColumn = pickerColumn;
                            arrayList2.add(pickerColumn);
                            PickerColumn pickerColumn2 = this.mAmPmColumn;
                            String[] strArr = this.mConstant.ampm;
                            Objects.requireNonNull(pickerColumn2);
                            pickerColumn2.mStaticLabels = strArr;
                            this.mColAmPmIndex = i5;
                            PickerColumn pickerColumn3 = this.mAmPmColumn;
                            Objects.requireNonNull(pickerColumn3);
                            if (pickerColumn3.mMinValue != 0) {
                                pickerColumn3.mMinValue = 0;
                            }
                            PickerColumn pickerColumn4 = this.mAmPmColumn;
                            Objects.requireNonNull(pickerColumn4);
                            if (1 != pickerColumn4.mMaxValue) {
                                pickerColumn4.mMaxValue = 1;
                            }
                        } else if (charAt2 == 'H') {
                            PickerColumn pickerColumn5 = new PickerColumn();
                            this.mHourColumn = pickerColumn5;
                            arrayList2.add(pickerColumn5);
                            PickerColumn pickerColumn6 = this.mHourColumn;
                            String[] strArr2 = this.mConstant.hours24;
                            Objects.requireNonNull(pickerColumn6);
                            pickerColumn6.mStaticLabels = strArr2;
                            this.mColHourIndex = i5;
                        } else if (charAt2 == 'M') {
                            PickerColumn pickerColumn7 = new PickerColumn();
                            this.mMinuteColumn = pickerColumn7;
                            arrayList2.add(pickerColumn7);
                            PickerColumn pickerColumn8 = this.mMinuteColumn;
                            String[] strArr3 = this.mConstant.minutes;
                            Objects.requireNonNull(pickerColumn8);
                            pickerColumn8.mStaticLabels = strArr3;
                            this.mColMinuteIndex = i5;
                        } else {
                            throw new IllegalArgumentException("Invalid time picker format.");
                        }
                    }
                    setColumns(arrayList2);
                } else {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Separators size: ");
                    m.append(arrayList.size());
                    m.append(" must equal the size of timeFieldsPattern: ");
                    m.append(str.length());
                    m.append(" + 1");
                    throw new IllegalStateException(m.toString());
                }
            }
            PickerColumn pickerColumn9 = this.mHourColumn;
            int i6 = !this.mIs24hFormat ? 1 : 0;
            Objects.requireNonNull(pickerColumn9);
            if (i6 != pickerColumn9.mMinValue) {
                pickerColumn9.mMinValue = i6;
            }
            PickerColumn pickerColumn10 = this.mHourColumn;
            int i7 = this.mIs24hFormat ? 23 : 12;
            Objects.requireNonNull(pickerColumn10);
            if (i7 != pickerColumn10.mMaxValue) {
                pickerColumn10.mMaxValue = i7;
            }
            PickerColumn pickerColumn11 = this.mMinuteColumn;
            Objects.requireNonNull(pickerColumn11);
            if (pickerColumn11.mMinValue != 0) {
                pickerColumn11.mMinValue = 0;
            }
            PickerColumn pickerColumn12 = this.mMinuteColumn;
            Objects.requireNonNull(pickerColumn12);
            if (59 != pickerColumn12.mMaxValue) {
                pickerColumn12.mMaxValue = 59;
            }
            PickerColumn pickerColumn13 = this.mAmPmColumn;
            if (pickerColumn13 != null) {
                if (pickerColumn13.mMinValue != 0) {
                    pickerColumn13.mMinValue = 0;
                }
                Objects.requireNonNull(pickerColumn13);
                if (1 != pickerColumn13.mMaxValue) {
                    pickerColumn13.mMaxValue = 1;
                }
            }
            if (z2) {
                Calendar calendarForLocale = PickerUtility.getCalendarForLocale(null, this.mConstant.locale);
                int i8 = calendarForLocale.get(11);
                if (i8 < 0 || i8 > 23) {
                    throw new IllegalArgumentException(ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("hour: ", i8, " is not in [0-23] range in"));
                }
                this.mCurrentHour = i8;
                boolean z6 = this.mIs24hFormat;
                if (!z6) {
                    if (i8 >= 12) {
                        this.mCurrentAmPmIndex = 1;
                        if (i8 > 12) {
                            this.mCurrentHour = i8 - 12;
                        }
                    } else {
                        this.mCurrentAmPmIndex = 0;
                        if (i8 == 0) {
                            this.mCurrentHour = 12;
                        }
                    }
                    if (!z6) {
                        setColumnValue(this.mColAmPmIndex, this.mCurrentAmPmIndex, false);
                    }
                }
                setColumnValue(this.mColHourIndex, this.mCurrentHour, false);
                int i9 = calendarForLocale.get(12);
                if (i9 < 0 || i9 > 59) {
                    throw new IllegalArgumentException(ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("minute: ", i9, " is not in [0-59] range."));
                }
                setColumnValue(this.mColMinuteIndex, i9, false);
                if (!this.mIs24hFormat) {
                    setColumnValue(this.mColAmPmIndex, this.mCurrentAmPmIndex, false);
                }
            }
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    public final String getBestHourMinutePattern() {
        String str;
        Locale locale = this.mConstant.locale;
        if (this.mIs24hFormat) {
            str = "Hma";
        } else {
            str = "hma";
        }
        String bestDateTimePattern = DateFormat.getBestDateTimePattern(locale, str);
        if (TextUtils.isEmpty(bestDateTimePattern)) {
            return "h:mma";
        }
        return bestDateTimePattern;
    }

    @Override // androidx.leanback.widget.picker.Picker
    public final void onColumnValueChanged(int i, int i2) {
        if (i == this.mColHourIndex) {
            this.mCurrentHour = i2;
        } else if (i != this.mColMinuteIndex) {
            if (i == this.mColAmPmIndex) {
                this.mCurrentAmPmIndex = i2;
                return;
            }
            throw new IllegalArgumentException("Invalid column index.");
        }
    }
}
