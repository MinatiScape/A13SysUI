package com.android.settingslib.notification;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.net.Uri;
import android.service.notification.Condition;
import android.service.notification.ZenModeConfig;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Slog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.systemui.qs.QSDndEvent;
import com.android.systemui.qs.tiles.dialog.QSZenModeDialogMetricsLogger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;
/* loaded from: classes.dex */
public final class EnableZenModeDialog {
    @VisibleForTesting
    public static final int COUNTDOWN_ALARM_CONDITION_INDEX = 2;
    @VisibleForTesting
    public static final int COUNTDOWN_CONDITION_INDEX = 1;
    public static final boolean DEBUG = Log.isLoggable("EnableZenModeDialog", 3);
    public static final int DEFAULT_BUCKET_INDEX;
    @VisibleForTesting
    public static final int FOREVER_CONDITION_INDEX = 0;
    public static final int MAX_BUCKET_MINUTES;
    public static final int[] MINUTE_BUCKETS;
    public static final int MIN_BUCKET_MINUTES;
    public AlarmManager mAlarmManager;
    public int mBucketIndex = -1;
    @VisibleForTesting
    public Context mContext;
    @VisibleForTesting
    public Uri mForeverId;
    @VisibleForTesting
    public LayoutInflater mLayoutInflater;
    public final ZenModeDialogMetricsLogger mMetricsLogger;
    @VisibleForTesting
    public NotificationManager mNotificationManager;
    public int mUserId;
    @VisibleForTesting
    public TextView mZenAlarmWarning;
    public RadioGroup mZenRadioGroup;
    @VisibleForTesting
    public LinearLayout mZenRadioGroupContent;

    @VisibleForTesting
    /* loaded from: classes.dex */
    public static class ConditionTag {
        public Condition condition;
        public TextView line1;
        public TextView line2;
        public View lines;
        public RadioButton rb;
    }

    /* renamed from: -$$Nest$monClickTimeButton  reason: not valid java name */
    public static void m22$$Nest$monClickTimeButton(EnableZenModeDialog enableZenModeDialog, View view, ConditionTag conditionTag, boolean z, int i) {
        QSDndEvent qSDndEvent;
        Condition condition;
        Uri uri;
        int i2;
        Objects.requireNonNull(enableZenModeDialog);
        QSZenModeDialogMetricsLogger qSZenModeDialogMetricsLogger = (QSZenModeDialogMetricsLogger) enableZenModeDialog.mMetricsLogger;
        Objects.requireNonNull(qSZenModeDialogMetricsLogger);
        MetricsLogger.action(qSZenModeDialogMetricsLogger.mContext, 163, z);
        UiEventLoggerImpl uiEventLoggerImpl = qSZenModeDialogMetricsLogger.mUiEventLogger;
        if (z) {
            qSDndEvent = QSDndEvent.QS_DND_TIME_UP;
        } else {
            qSDndEvent = QSDndEvent.QS_DND_TIME_DOWN;
        }
        uiEventLoggerImpl.log(qSDndEvent);
        int[] iArr = MINUTE_BUCKETS;
        int length = iArr.length;
        int i3 = enableZenModeDialog.mBucketIndex;
        int i4 = -1;
        int i5 = 0;
        if (i3 == -1) {
            Condition condition2 = conditionTag.condition;
            condition = null;
            if (condition2 != null) {
                uri = condition2.id;
            } else {
                uri = null;
            }
            long tryParseCountdownConditionId = ZenModeConfig.tryParseCountdownConditionId(uri);
            long currentTimeMillis = System.currentTimeMillis();
            for (int i6 = 0; i6 < length; i6++) {
                if (z) {
                    i2 = i6;
                } else {
                    i2 = (length - 1) - i6;
                }
                int i7 = MINUTE_BUCKETS[i2];
                long j = currentTimeMillis + (60000 * i7);
                if ((z && j > tryParseCountdownConditionId) || (!z && j < tryParseCountdownConditionId)) {
                    enableZenModeDialog.mBucketIndex = i2;
                    condition = ZenModeConfig.toTimeCondition(enableZenModeDialog.mContext, j, i7, ActivityManager.getCurrentUser(), false);
                    break;
                }
            }
            if (condition == null) {
                int i8 = DEFAULT_BUCKET_INDEX;
                enableZenModeDialog.mBucketIndex = i8;
                condition = ZenModeConfig.toTimeCondition(enableZenModeDialog.mContext, MINUTE_BUCKETS[i8], ActivityManager.getCurrentUser());
            }
        } else {
            int i9 = length - 1;
            if (z) {
                i4 = 1;
            }
            int max = Math.max(0, Math.min(i9, i3 + i4));
            enableZenModeDialog.mBucketIndex = max;
            condition = ZenModeConfig.toTimeCondition(enableZenModeDialog.mContext, iArr[max], ActivityManager.getCurrentUser());
        }
        enableZenModeDialog.bind(condition, view, i);
        String computeAlarmWarningText = enableZenModeDialog.computeAlarmWarningText(conditionTag.condition);
        enableZenModeDialog.mZenAlarmWarning.setText(computeAlarmWarningText);
        TextView textView = enableZenModeDialog.mZenAlarmWarning;
        if (computeAlarmWarningText == null) {
            i5 = 8;
        }
        textView.setVisibility(i5);
        conditionTag.rb.setChecked(true);
    }

    static {
        int[] iArr = ZenModeConfig.MINUTE_BUCKETS;
        MINUTE_BUCKETS = iArr;
        MIN_BUCKET_MINUTES = iArr[0];
        MAX_BUCKET_MINUTES = iArr[iArr.length - 1];
        DEFAULT_BUCKET_INDEX = Arrays.binarySearch(iArr, 60);
    }

    @VisibleForTesting
    public void bind(Condition condition, final View view, final int i) {
        boolean z;
        final ConditionTag conditionTag;
        boolean z2;
        String str;
        float f;
        float f2;
        boolean z3;
        boolean z4;
        if (condition != null) {
            boolean z5 = true;
            if (condition.state == 1) {
                z = true;
            } else {
                z = false;
            }
            if (view.getTag() != null) {
                conditionTag = (ConditionTag) view.getTag();
            } else {
                conditionTag = new ConditionTag();
            }
            view.setTag(conditionTag);
            RadioButton radioButton = conditionTag.rb;
            if (radioButton == null) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (radioButton == null) {
                conditionTag.rb = (RadioButton) this.mZenRadioGroup.getChildAt(i);
            }
            conditionTag.condition = condition;
            final Uri uri = condition.id;
            if (DEBUG) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("bind i=");
                m.append(this.mZenRadioGroupContent.indexOfChild(view));
                m.append(" first=");
                m.append(z2);
                m.append(" condition=");
                m.append(uri);
                Log.d("EnableZenModeDialog", m.toString());
            }
            conditionTag.rb.setEnabled(z);
            conditionTag.rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.android.settingslib.notification.EnableZenModeDialog.2
                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public final void onCheckedChanged(CompoundButton compoundButton, boolean z6) {
                    int i2;
                    if (z6) {
                        conditionTag.rb.setChecked(true);
                        if (EnableZenModeDialog.DEBUG) {
                            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("onCheckedChanged ");
                            m2.append(uri);
                            Log.d("EnableZenModeDialog", m2.toString());
                        }
                        QSZenModeDialogMetricsLogger qSZenModeDialogMetricsLogger = (QSZenModeDialogMetricsLogger) EnableZenModeDialog.this.mMetricsLogger;
                        Objects.requireNonNull(qSZenModeDialogMetricsLogger);
                        MetricsLogger.action(qSZenModeDialogMetricsLogger.mContext, 164);
                        qSZenModeDialogMetricsLogger.mUiEventLogger.log(QSDndEvent.QS_DND_CONDITION_SELECT);
                        EnableZenModeDialog enableZenModeDialog = EnableZenModeDialog.this;
                        Condition condition2 = conditionTag.condition;
                        Objects.requireNonNull(enableZenModeDialog);
                        String computeAlarmWarningText = enableZenModeDialog.computeAlarmWarningText(condition2);
                        enableZenModeDialog.mZenAlarmWarning.setText(computeAlarmWarningText);
                        TextView textView = enableZenModeDialog.mZenAlarmWarning;
                        if (computeAlarmWarningText == null) {
                            i2 = 8;
                        } else {
                            i2 = 0;
                        }
                        textView.setVisibility(i2);
                    }
                }
            });
            if (conditionTag.lines == null) {
                conditionTag.lines = view.findViewById(16908290);
            }
            if (conditionTag.line1 == null) {
                conditionTag.line1 = (TextView) view.findViewById(16908308);
            }
            if (conditionTag.line2 == null) {
                conditionTag.line2 = (TextView) view.findViewById(16908309);
            }
            if (!TextUtils.isEmpty(condition.line1)) {
                str = condition.line1;
            } else {
                str = condition.summary;
            }
            String str2 = condition.line2;
            conditionTag.line1.setText(str);
            if (TextUtils.isEmpty(str2)) {
                conditionTag.line2.setVisibility(8);
            } else {
                conditionTag.line2.setVisibility(0);
                conditionTag.line2.setText(str2);
            }
            conditionTag.lines.setEnabled(z);
            View view2 = conditionTag.lines;
            float f3 = 1.0f;
            if (z) {
                f = 1.0f;
            } else {
                f = 0.4f;
            }
            view2.setAlpha(f);
            conditionTag.lines.setOnClickListener(new View.OnClickListener() { // from class: com.android.settingslib.notification.EnableZenModeDialog.3
                @Override // android.view.View.OnClickListener
                public final void onClick(View view3) {
                    ConditionTag.this.rb.setChecked(true);
                }
            });
            long tryParseCountdownConditionId = ZenModeConfig.tryParseCountdownConditionId(uri);
            ImageView imageView = (ImageView) view.findViewById(16908313);
            ImageView imageView2 = (ImageView) view.findViewById(16908314);
            if (i != 1 || tryParseCountdownConditionId <= 0) {
                if (imageView != null) {
                    ((ViewGroup) view).removeView(imageView);
                }
                if (imageView2 != null) {
                    ((ViewGroup) view).removeView(imageView2);
                }
            } else {
                imageView.setOnClickListener(new View.OnClickListener() { // from class: com.android.settingslib.notification.EnableZenModeDialog.4
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view3) {
                        EnableZenModeDialog.m22$$Nest$monClickTimeButton(EnableZenModeDialog.this, view, conditionTag, false, i);
                        conditionTag.lines.setAccessibilityLiveRegion(1);
                    }
                });
                imageView2.setOnClickListener(new View.OnClickListener() { // from class: com.android.settingslib.notification.EnableZenModeDialog.5
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view3) {
                        EnableZenModeDialog.m22$$Nest$monClickTimeButton(EnableZenModeDialog.this, view, conditionTag, true, i);
                        conditionTag.lines.setAccessibilityLiveRegion(1);
                    }
                });
                int i2 = this.mBucketIndex;
                if (i2 > -1) {
                    if (i2 > 0) {
                        z4 = true;
                    } else {
                        z4 = false;
                    }
                    imageView.setEnabled(z4);
                    if (this.mBucketIndex >= MINUTE_BUCKETS.length - 1) {
                        z5 = false;
                    }
                    imageView2.setEnabled(z5);
                } else {
                    if (tryParseCountdownConditionId - System.currentTimeMillis() > MIN_BUCKET_MINUTES * 60000) {
                        z3 = true;
                    } else {
                        z3 = false;
                    }
                    imageView.setEnabled(z3);
                    imageView2.setEnabled(!Objects.equals(condition.summary, ZenModeConfig.toTimeCondition(this.mContext, MAX_BUCKET_MINUTES, ActivityManager.getCurrentUser()).summary));
                }
                if (imageView.isEnabled()) {
                    f2 = 1.0f;
                } else {
                    f2 = 0.5f;
                }
                imageView.setAlpha(f2);
                if (!imageView2.isEnabled()) {
                    f3 = 0.5f;
                }
                imageView2.setAlpha(f3);
            }
            view.setVisibility(0);
            return;
        }
        throw new IllegalArgumentException("condition must not be null");
    }

    @VisibleForTesting
    public void bindGenericCountdown() {
        int i = DEFAULT_BUCKET_INDEX;
        this.mBucketIndex = i;
        bind(ZenModeConfig.toTimeCondition(this.mContext, MINUTE_BUCKETS[i], ActivityManager.getCurrentUser()), this.mZenRadioGroupContent.getChildAt(1), 1);
    }

    @VisibleForTesting
    public void bindNextAlarm(Condition condition) {
        boolean z;
        int i;
        View childAt = this.mZenRadioGroupContent.getChildAt(2);
        ConditionTag conditionTag = (ConditionTag) childAt.getTag();
        if (condition != null) {
            bind(condition, childAt, 2);
        }
        ConditionTag conditionTag2 = (ConditionTag) childAt.getTag();
        int i2 = 0;
        if (conditionTag2 == null || conditionTag2.condition == null) {
            z = false;
        } else {
            z = true;
        }
        View childAt2 = this.mZenRadioGroup.getChildAt(2);
        if (z) {
            i = 0;
        } else {
            i = 8;
        }
        childAt2.setVisibility(i);
        if (!z) {
            i2 = 8;
        }
        childAt.setVisibility(i2);
    }

    @VisibleForTesting
    public String computeAlarmWarningText(Condition condition) {
        boolean z;
        long j;
        int i;
        if ((this.mNotificationManager.getNotificationPolicy().priorityCategories & 32) != 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            return null;
        }
        long currentTimeMillis = System.currentTimeMillis();
        AlarmManager.AlarmClockInfo nextAlarmClock = this.mAlarmManager.getNextAlarmClock(this.mUserId);
        if (nextAlarmClock != null) {
            j = nextAlarmClock.getTriggerTime();
        } else {
            j = 0;
        }
        if (j < currentTimeMillis) {
            return null;
        }
        if (condition == null || isForever(condition)) {
            i = 2131953674;
        } else {
            long tryParseCountdownConditionId = ZenModeConfig.tryParseCountdownConditionId(condition.id);
            if (tryParseCountdownConditionId <= currentTimeMillis || j >= tryParseCountdownConditionId) {
                i = 0;
            } else {
                i = 2131953673;
            }
        }
        if (i == 0) {
            return null;
        }
        return this.mContext.getResources().getString(i, getTime(j, currentTimeMillis));
    }

    public final Condition forever() {
        return new Condition(Condition.newId(this.mContext).appendPath("forever").build(), this.mContext.getString(17041779), "", "", 0, 1, 0);
    }

    @VisibleForTesting
    public ConditionTag getConditionTagAt(int i) {
        return (ConditionTag) this.mZenRadioGroupContent.getChildAt(i).getTag();
    }

    @VisibleForTesting
    public String getTime(long j, long j2) {
        boolean z;
        String str;
        int i;
        if (j - j2 < 86400000) {
            z = true;
        } else {
            z = false;
        }
        boolean is24HourFormat = DateFormat.is24HourFormat(this.mContext, ActivityManager.getCurrentUser());
        if (z) {
            if (is24HourFormat) {
                str = "Hm";
            } else {
                str = "hma";
            }
        } else if (is24HourFormat) {
            str = "EEEHm";
        } else {
            str = "EEEhma";
        }
        CharSequence format = DateFormat.format(DateFormat.getBestDateTimePattern(Locale.getDefault(), str), j);
        if (z) {
            i = 2131951874;
        } else {
            i = 2131951875;
        }
        return this.mContext.getResources().getString(i, format);
    }

    @VisibleForTesting
    public Condition getTimeUntilNextAlarmCondition() {
        long j;
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.set(11, 0);
        gregorianCalendar.set(12, 0);
        gregorianCalendar.set(13, 0);
        gregorianCalendar.set(14, 0);
        gregorianCalendar.add(5, 6);
        AlarmManager.AlarmClockInfo nextAlarmClock = this.mAlarmManager.getNextAlarmClock(this.mUserId);
        if (nextAlarmClock != null) {
            j = nextAlarmClock.getTriggerTime();
        } else {
            j = 0;
        }
        if (j <= 0) {
            return null;
        }
        GregorianCalendar gregorianCalendar2 = new GregorianCalendar();
        gregorianCalendar2.setTimeInMillis(j);
        gregorianCalendar2.set(11, 0);
        gregorianCalendar2.set(12, 0);
        gregorianCalendar2.set(13, 0);
        gregorianCalendar2.set(14, 0);
        if (gregorianCalendar.compareTo((Calendar) gregorianCalendar2) >= 0) {
            return ZenModeConfig.toNextAlarmCondition(this.mContext, j, ActivityManager.getCurrentUser());
        }
        return null;
    }

    @VisibleForTesting
    public boolean isAlarm(Condition condition) {
        if (condition == null || !ZenModeConfig.isValidCountdownToAlarmConditionId(condition.id)) {
            return false;
        }
        return true;
    }

    @VisibleForTesting
    public boolean isCountdown(Condition condition) {
        if (condition == null || !ZenModeConfig.isValidCountdownConditionId(condition.id)) {
            return false;
        }
        return true;
    }

    public final boolean isForever(Condition condition) {
        if (condition == null || !this.mForeverId.equals(condition.id)) {
            return false;
        }
        return true;
    }

    public EnableZenModeDialog(Context context, QSZenModeDialogMetricsLogger qSZenModeDialogMetricsLogger) {
        this.mContext = context;
        this.mMetricsLogger = qSZenModeDialogMetricsLogger;
    }

    @VisibleForTesting
    public void bindConditions(Condition condition) {
        bind(forever(), this.mZenRadioGroupContent.getChildAt(0), 0);
        if (condition == null) {
            bindGenericCountdown();
            bindNextAlarm(getTimeUntilNextAlarmCondition());
        } else if (isForever(condition)) {
            getConditionTagAt(0).rb.setChecked(true);
            bindGenericCountdown();
            bindNextAlarm(getTimeUntilNextAlarmCondition());
        } else if (isAlarm(condition)) {
            bindGenericCountdown();
            bindNextAlarm(condition);
            getConditionTagAt(2).rb.setChecked(true);
        } else if (isCountdown(condition)) {
            bindNextAlarm(getTimeUntilNextAlarmCondition());
            bind(condition, this.mZenRadioGroupContent.getChildAt(1), 1);
            getConditionTagAt(1).rb.setChecked(true);
        } else {
            Slog.d("EnableZenModeDialog", "Invalid manual condition: " + condition);
        }
    }
}
