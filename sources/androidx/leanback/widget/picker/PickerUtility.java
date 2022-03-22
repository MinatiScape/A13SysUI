package androidx.leanback.widget.picker;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;
/* loaded from: classes.dex */
public final class PickerUtility {

    /* loaded from: classes.dex */
    public static class DateConstant {
        public final Locale locale;
        public final String[] months;

        public DateConstant(Locale locale) {
            this.locale = locale;
            this.months = DateFormatSymbols.getInstance(locale).getShortMonths();
            Calendar instance = Calendar.getInstance(locale);
            PickerUtility.createStringIntArrays(instance.getMinimum(5), instance.getMaximum(5));
        }
    }

    /* loaded from: classes.dex */
    public static class TimeConstant {
        public final String[] ampm;
        public final Locale locale;
        public final String[] hours24 = PickerUtility.createStringIntArrays(0, 23);
        public final String[] minutes = PickerUtility.createStringIntArrays(0, 59);

        public TimeConstant(Locale locale) {
            this.locale = locale;
            DateFormatSymbols instance = DateFormatSymbols.getInstance(locale);
            PickerUtility.createStringIntArrays(1, 12);
            this.ampm = instance.getAmPmStrings();
        }
    }

    public static String[] createStringIntArrays(int i, int i2) {
        String[] strArr = new String[(i2 - i) + 1];
        for (int i3 = i; i3 <= i2; i3++) {
            strArr[i3 - i] = String.format("%02d", Integer.valueOf(i3));
        }
        return strArr;
    }

    public static Calendar getCalendarForLocale(Calendar calendar, Locale locale) {
        if (calendar == null) {
            return Calendar.getInstance(locale);
        }
        long timeInMillis = calendar.getTimeInMillis();
        Calendar instance = Calendar.getInstance(locale);
        instance.setTimeInMillis(timeInMillis);
        return instance;
    }
}
