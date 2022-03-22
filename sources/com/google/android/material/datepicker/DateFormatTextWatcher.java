package com.google.android.material.datepicker;

import android.content.Context;
import com.google.android.material.internal.TextWatcherAdapter;
import com.google.android.material.textfield.TextInputLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
/* loaded from: classes.dex */
public abstract class DateFormatTextWatcher extends TextWatcherAdapter {
    public final CalendarConstraints constraints;
    public final DateFormat dateFormat;
    public final String outOfRange;
    public final AnonymousClass1 setErrorCallback;
    public AnonymousClass2 setRangeErrorCallback;
    public final TextInputLayout textInputLayout;

    public abstract void onInvalidDate();

    public abstract void onValidDate(Long l);

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0060 A[Catch: ParseException -> 0x0079, TryCatch #0 {ParseException -> 0x0079, blocks: (B:6:0x0020, B:8:0x0040, B:10:0x0050, B:15:0x0060, B:17:0x006c), top: B:20:0x0020 }] */
    /* JADX WARN: Type inference failed for: r7v4, types: [com.google.android.material.datepicker.DateFormatTextWatcher$2, java.lang.Runnable] */
    /* JADX WARN: Unknown variable types count: 1 */
    @Override // com.google.android.material.internal.TextWatcherAdapter, android.text.TextWatcher
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onTextChanged(java.lang.CharSequence r7, int r8, int r9, int r10) {
        /*
            r6 = this;
            com.google.android.material.textfield.TextInputLayout r8 = r6.textInputLayout
            com.google.android.material.datepicker.DateFormatTextWatcher$1 r9 = r6.setErrorCallback
            r8.removeCallbacks(r9)
            com.google.android.material.textfield.TextInputLayout r8 = r6.textInputLayout
            com.google.android.material.datepicker.DateFormatTextWatcher$2 r9 = r6.setRangeErrorCallback
            r8.removeCallbacks(r9)
            com.google.android.material.textfield.TextInputLayout r8 = r6.textInputLayout
            r9 = 0
            r8.setError(r9)
            r6.onValidDate(r9)
            boolean r8 = android.text.TextUtils.isEmpty(r7)
            if (r8 == 0) goto L_0x001e
            return
        L_0x001e:
            r0 = 1000(0x3e8, double:4.94E-321)
            java.text.DateFormat r8 = r6.dateFormat     // Catch: ParseException -> 0x0079
            java.lang.String r7 = r7.toString()     // Catch: ParseException -> 0x0079
            java.util.Date r7 = r8.parse(r7)     // Catch: ParseException -> 0x0079
            com.google.android.material.textfield.TextInputLayout r8 = r6.textInputLayout     // Catch: ParseException -> 0x0079
            r8.setError(r9)     // Catch: ParseException -> 0x0079
            long r8 = r7.getTime()     // Catch: ParseException -> 0x0079
            com.google.android.material.datepicker.CalendarConstraints r10 = r6.constraints     // Catch: ParseException -> 0x0079
            java.util.Objects.requireNonNull(r10)     // Catch: ParseException -> 0x0079
            com.google.android.material.datepicker.CalendarConstraints$DateValidator r10 = r10.validator     // Catch: ParseException -> 0x0079
            boolean r10 = r10.isValid(r8)     // Catch: ParseException -> 0x0079
            if (r10 == 0) goto L_0x006c
            com.google.android.material.datepicker.CalendarConstraints r10 = r6.constraints     // Catch: ParseException -> 0x0079
            java.util.Objects.requireNonNull(r10)     // Catch: ParseException -> 0x0079
            com.google.android.material.datepicker.Month r2 = r10.start     // Catch: ParseException -> 0x0079
            r3 = 1
            long r4 = r2.getDay(r3)     // Catch: ParseException -> 0x0079
            int r2 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
            if (r2 > 0) goto L_0x005d
            com.google.android.material.datepicker.Month r10 = r10.end     // Catch: ParseException -> 0x0079
            int r2 = r10.daysInMonth     // Catch: ParseException -> 0x0079
            long r4 = r10.getDay(r2)     // Catch: ParseException -> 0x0079
            int r10 = (r8 > r4 ? 1 : (r8 == r4 ? 0 : -1))
            if (r10 > 0) goto L_0x005d
            goto L_0x005e
        L_0x005d:
            r3 = 0
        L_0x005e:
            if (r3 == 0) goto L_0x006c
            long r7 = r7.getTime()     // Catch: ParseException -> 0x0079
            java.lang.Long r7 = java.lang.Long.valueOf(r7)     // Catch: ParseException -> 0x0079
            r6.onValidDate(r7)     // Catch: ParseException -> 0x0079
            return
        L_0x006c:
            com.google.android.material.datepicker.DateFormatTextWatcher$2 r7 = new com.google.android.material.datepicker.DateFormatTextWatcher$2     // Catch: ParseException -> 0x0079
            r7.<init>()     // Catch: ParseException -> 0x0079
            r6.setRangeErrorCallback = r7     // Catch: ParseException -> 0x0079
            com.google.android.material.textfield.TextInputLayout r8 = r6.textInputLayout     // Catch: ParseException -> 0x0079
            r8.postDelayed(r7, r0)     // Catch: ParseException -> 0x0079
            goto L_0x0080
        L_0x0079:
            com.google.android.material.textfield.TextInputLayout r7 = r6.textInputLayout
            com.google.android.material.datepicker.DateFormatTextWatcher$1 r6 = r6.setErrorCallback
            r7.postDelayed(r6, r0)
        L_0x0080:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.datepicker.DateFormatTextWatcher.onTextChanged(java.lang.CharSequence, int, int, int):void");
    }

    /* JADX WARN: Type inference failed for: r2v3, types: [com.google.android.material.datepicker.DateFormatTextWatcher$1] */
    public DateFormatTextWatcher(final String str, SimpleDateFormat simpleDateFormat, TextInputLayout textInputLayout, CalendarConstraints calendarConstraints) {
        this.dateFormat = simpleDateFormat;
        this.textInputLayout = textInputLayout;
        this.constraints = calendarConstraints;
        this.outOfRange = textInputLayout.getContext().getString(2131952843);
        this.setErrorCallback = new Runnable() { // from class: com.google.android.material.datepicker.DateFormatTextWatcher.1
            @Override // java.lang.Runnable
            public final void run() {
                DateFormatTextWatcher dateFormatTextWatcher = DateFormatTextWatcher.this;
                TextInputLayout textInputLayout2 = dateFormatTextWatcher.textInputLayout;
                DateFormat dateFormat = dateFormatTextWatcher.dateFormat;
                Context context = textInputLayout2.getContext();
                String string = context.getString(2131952838);
                String format = String.format(context.getString(2131952840), str);
                String format2 = String.format(context.getString(2131952839), dateFormat.format(new Date(UtcDates.getTodayCalendar().getTimeInMillis())));
                textInputLayout2.setError(string + "\n" + format + "\n" + format2);
                DateFormatTextWatcher.this.onInvalidDate();
            }
        };
    }
}
