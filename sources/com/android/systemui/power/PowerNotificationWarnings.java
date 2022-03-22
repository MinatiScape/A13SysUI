package com.android.systemui.power;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.Annotation;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.util.Slog;
import android.view.View;
import com.android.settingslib.Utils;
import com.android.settingslib.fuelgauge.BatterySaverUtils;
import com.android.systemui.SystemUIApplication;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.power.PowerNotificationWarnings;
import com.android.systemui.power.PowerUI;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.wmshell.WMShell$7$$ExternalSyntheticLambda0;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Objects;
/* loaded from: classes.dex */
public class PowerNotificationWarnings implements PowerUI.WarningsUI {
    public static final boolean DEBUG = PowerUI.DEBUG;
    public static final String[] SHOWING_STRINGS = {"SHOWING_NOTHING", "SHOWING_WARNING", "SHOWING_SAVER", "SHOWING_INVALID_CHARGER", "SHOWING_AUTO_SAVER_SUGGESTION"};
    public ActivityStarter mActivityStarter;
    public int mBatteryLevel;
    public int mBucket;
    public final Context mContext;
    public BatteryStateSnapshot mCurrentBatterySnapshot;
    public final Handler mHandler;
    public SystemUIDialog mHighTempDialog;
    public boolean mHighTempWarning;
    public boolean mInvalidCharger;
    public final KeyguardManager mKeyguard;
    public final NotificationManager mNoMan;
    public final Intent mOpenBatterySettings = new Intent("android.intent.action.POWER_USAGE_SUMMARY").setFlags(1551892480);
    public boolean mPlaySound;
    public final PowerManager mPowerMan;
    public SystemUIDialog mSaverConfirmation;
    public boolean mShowAutoSaverSuggestion;
    public int mShowing;
    public SystemUIDialog mThermalShutdownDialog;
    public SystemUIDialog mUsbHighTempDialog;
    public boolean mWarning;
    public long mWarningTriggerTimeMs;

    /* renamed from: com.android.systemui.power.PowerNotificationWarnings$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass1 implements DialogInterface.OnClickListener {
        public final /* synthetic */ String val$url;

        public AnonymousClass1(String str) {
            this.val$url = str;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public final void onClick(DialogInterface dialogInterface, int i) {
            PowerNotificationWarnings.this.mActivityStarter.startActivity(new Intent("android.intent.action.VIEW").setData(Uri.parse(this.val$url)).setFlags(268435456), true, new ActivityStarter.Callback() { // from class: com.android.systemui.power.PowerNotificationWarnings$1$$ExternalSyntheticLambda0
                @Override // com.android.systemui.plugins.ActivityStarter.Callback
                public final void onActivityStarted(int i2) {
                    PowerNotificationWarnings.AnonymousClass1 r0 = PowerNotificationWarnings.AnonymousClass1.this;
                    Objects.requireNonNull(r0);
                    PowerNotificationWarnings.this.mHighTempDialog = null;
                }
            });
        }
    }

    /* renamed from: com.android.systemui.power.PowerNotificationWarnings$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass2 implements DialogInterface.OnClickListener {
        public final /* synthetic */ String val$url;

        public AnonymousClass2(String str) {
            this.val$url = str;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public final void onClick(DialogInterface dialogInterface, int i) {
            PowerNotificationWarnings.this.mActivityStarter.startActivity(new Intent("android.intent.action.VIEW").setData(Uri.parse(this.val$url)).setFlags(268435456), true, new ActivityStarter.Callback() { // from class: com.android.systemui.power.PowerNotificationWarnings$2$$ExternalSyntheticLambda0
                @Override // com.android.systemui.plugins.ActivityStarter.Callback
                public final void onActivityStarted(int i2) {
                    PowerNotificationWarnings.AnonymousClass2 r0 = PowerNotificationWarnings.AnonymousClass2.this;
                    Objects.requireNonNull(r0);
                    PowerNotificationWarnings.this.mThermalShutdownDialog = null;
                }
            });
        }
    }

    /* loaded from: classes.dex */
    public final class Receiver extends BroadcastReceiver {
        public Receiver() {
        }

        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            CharSequence charSequence;
            Annotation[] annotationArr;
            String action = intent.getAction();
            Slog.i("PowerUI.Notification", "Received " + action);
            if (action.equals("PNW.batterySettings")) {
                PowerNotificationWarnings.this.dismissLowBatteryNotification();
                PowerNotificationWarnings powerNotificationWarnings = PowerNotificationWarnings.this;
                powerNotificationWarnings.mContext.startActivityAsUser(powerNotificationWarnings.mOpenBatterySettings, UserHandle.CURRENT);
            } else if (action.equals("PNW.startSaver")) {
                PowerNotificationWarnings powerNotificationWarnings2 = PowerNotificationWarnings.this;
                Objects.requireNonNull(powerNotificationWarnings2);
                BatterySaverUtils.setPowerSaveMode(powerNotificationWarnings2.mContext, true, true);
                PowerNotificationWarnings.this.dismissLowBatteryNotification();
            } else if (action.equals("PNW.startSaverConfirmation")) {
                PowerNotificationWarnings.this.dismissLowBatteryNotification();
                final PowerNotificationWarnings powerNotificationWarnings3 = PowerNotificationWarnings.this;
                Bundle extras = intent.getExtras();
                Objects.requireNonNull(powerNotificationWarnings3);
                if (powerNotificationWarnings3.mSaverConfirmation == null) {
                    SystemUIDialog systemUIDialog = new SystemUIDialog(powerNotificationWarnings3.mContext);
                    boolean z = extras.getBoolean("extra_confirm_only");
                    final int i = extras.getInt("extra_power_save_mode_trigger", 0);
                    final int i2 = extras.getInt("extra_power_save_mode_trigger_level", 0);
                    String charSequence2 = powerNotificationWarnings3.mContext.getText(2131952436).toString();
                    if (TextUtils.isEmpty(charSequence2)) {
                        charSequence = powerNotificationWarnings3.mContext.getText(17039783);
                    } else {
                        SpannableString spannableString = new SpannableString(powerNotificationWarnings3.mContext.getText(17039784));
                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(spannableString);
                        for (Annotation annotation : (Annotation[]) spannableString.getSpans(0, spannableString.length(), Annotation.class)) {
                            if ("url".equals(annotation.getValue())) {
                                int spanStart = spannableString.getSpanStart(annotation);
                                int spanEnd = spannableString.getSpanEnd(annotation);
                                URLSpan uRLSpan = new URLSpan(charSequence2) { // from class: com.android.systemui.power.PowerNotificationWarnings.3
                                    @Override // android.text.style.URLSpan, android.text.style.ClickableSpan
                                    public final void onClick(View view) {
                                        SystemUIDialog systemUIDialog2 = PowerNotificationWarnings.this.mSaverConfirmation;
                                        if (systemUIDialog2 != null) {
                                            systemUIDialog2.dismiss();
                                        }
                                        PowerNotificationWarnings.this.mContext.sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS").setFlags(268435456));
                                        Uri parse = Uri.parse(getURL());
                                        Context context2 = view.getContext();
                                        Intent flags = new Intent("android.intent.action.VIEW", parse).setFlags(268435456);
                                        try {
                                            context2.startActivity(flags);
                                        } catch (ActivityNotFoundException unused) {
                                            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Activity was not found for intent, ");
                                            m.append(flags.toString());
                                            Log.w("PowerUI.Notification", m.toString());
                                        }
                                    }

                                    @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
                                    public final void updateDrawState(TextPaint textPaint) {
                                        super.updateDrawState(textPaint);
                                        textPaint.setUnderlineText(false);
                                    }
                                };
                                spannableStringBuilder.setSpan(uRLSpan, spanStart, spanEnd, spannableString.getSpanFlags(uRLSpan));
                            }
                        }
                        charSequence = spannableStringBuilder;
                    }
                    systemUIDialog.setMessage(charSequence);
                    if (Objects.equals(Locale.getDefault().getLanguage(), Locale.ENGLISH.getLanguage())) {
                        systemUIDialog.setMessageHyphenationFrequency(0);
                    }
                    systemUIDialog.setMessageMovementMethod(LinkMovementMethod.getInstance());
                    if (z) {
                        systemUIDialog.setTitle(2131951920);
                        systemUIDialog.setPositiveButton(17040061, new DialogInterface.OnClickListener() { // from class: com.android.systemui.power.PowerNotificationWarnings$$ExternalSyntheticLambda3
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i3) {
                                PowerNotificationWarnings powerNotificationWarnings4 = PowerNotificationWarnings.this;
                                int i4 = i;
                                int i5 = i2;
                                Objects.requireNonNull(powerNotificationWarnings4);
                                ContentResolver contentResolver = powerNotificationWarnings4.mContext.getContentResolver();
                                Settings.Global.putInt(contentResolver, "automatic_power_save_mode", i4);
                                Settings.Global.putInt(contentResolver, "low_power_trigger_level", i5);
                                Settings.Secure.putIntForUser(contentResolver, "low_power_warning_acknowledged", 1, -2);
                            }
                        });
                    } else {
                        systemUIDialog.setTitle(2131951919);
                        systemUIDialog.setPositiveButton(2131951918, new DialogInterface.OnClickListener() { // from class: com.android.systemui.power.PowerNotificationWarnings$$ExternalSyntheticLambda1
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i3) {
                                PowerNotificationWarnings powerNotificationWarnings4 = PowerNotificationWarnings.this;
                                Objects.requireNonNull(powerNotificationWarnings4);
                                BatterySaverUtils.setPowerSaveMode(powerNotificationWarnings4.mContext, true, false);
                            }
                        });
                        systemUIDialog.setNegativeButton(17039360, null);
                    }
                    SystemUIDialog.setShowForAllUsers(systemUIDialog);
                    systemUIDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.android.systemui.power.PowerNotificationWarnings$$ExternalSyntheticLambda6
                        @Override // android.content.DialogInterface.OnDismissListener
                        public final void onDismiss(DialogInterface dialogInterface) {
                            PowerNotificationWarnings powerNotificationWarnings4 = PowerNotificationWarnings.this;
                            Objects.requireNonNull(powerNotificationWarnings4);
                            powerNotificationWarnings4.mSaverConfirmation = null;
                        }
                    });
                    systemUIDialog.show();
                    powerNotificationWarnings3.mSaverConfirmation = systemUIDialog;
                }
            } else if (action.equals("PNW.dismissedWarning")) {
                PowerNotificationWarnings.this.dismissLowBatteryWarning();
            } else if ("PNW.clickedTempWarning".equals(action)) {
                PowerNotificationWarnings.this.dismissHighTemperatureWarningInternal();
                final PowerNotificationWarnings powerNotificationWarnings4 = PowerNotificationWarnings.this;
                Objects.requireNonNull(powerNotificationWarnings4);
                if (powerNotificationWarnings4.mHighTempDialog == null) {
                    SystemUIDialog systemUIDialog2 = new SystemUIDialog(powerNotificationWarnings4.mContext);
                    systemUIDialog2.setIconAttribute(16843605);
                    systemUIDialog2.setTitle(2131952446);
                    systemUIDialog2.setMessage(2131952444);
                    systemUIDialog2.setPositiveButton(17039370, null);
                    SystemUIDialog.setShowForAllUsers(systemUIDialog2);
                    systemUIDialog2.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.android.systemui.power.PowerNotificationWarnings$$ExternalSyntheticLambda4
                        @Override // android.content.DialogInterface.OnDismissListener
                        public final void onDismiss(DialogInterface dialogInterface) {
                            PowerNotificationWarnings powerNotificationWarnings5 = PowerNotificationWarnings.this;
                            Objects.requireNonNull(powerNotificationWarnings5);
                            powerNotificationWarnings5.mHighTempDialog = null;
                        }
                    });
                    String string = powerNotificationWarnings4.mContext.getString(2131952443);
                    if (!string.isEmpty()) {
                        systemUIDialog2.setButton(-3, 2131952442, new AnonymousClass1(string), true);
                    }
                    systemUIDialog2.show();
                    powerNotificationWarnings4.mHighTempDialog = systemUIDialog2;
                }
            } else if ("PNW.dismissedTempWarning".equals(action)) {
                PowerNotificationWarnings.this.dismissHighTemperatureWarningInternal();
            } else if ("PNW.clickedThermalShutdownWarning".equals(action)) {
                PowerNotificationWarnings.this.dismissThermalShutdownWarning();
                final PowerNotificationWarnings powerNotificationWarnings5 = PowerNotificationWarnings.this;
                Objects.requireNonNull(powerNotificationWarnings5);
                if (powerNotificationWarnings5.mThermalShutdownDialog == null) {
                    SystemUIDialog systemUIDialog3 = new SystemUIDialog(powerNotificationWarnings5.mContext);
                    systemUIDialog3.setIconAttribute(16843605);
                    systemUIDialog3.setTitle(2131953366);
                    systemUIDialog3.setMessage(2131953364);
                    systemUIDialog3.setPositiveButton(17039370, null);
                    SystemUIDialog.setShowForAllUsers(systemUIDialog3);
                    systemUIDialog3.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.android.systemui.power.PowerNotificationWarnings$$ExternalSyntheticLambda5
                        @Override // android.content.DialogInterface.OnDismissListener
                        public final void onDismiss(DialogInterface dialogInterface) {
                            PowerNotificationWarnings powerNotificationWarnings6 = PowerNotificationWarnings.this;
                            Objects.requireNonNull(powerNotificationWarnings6);
                            powerNotificationWarnings6.mThermalShutdownDialog = null;
                        }
                    });
                    String string2 = powerNotificationWarnings5.mContext.getString(2131953363);
                    if (!string2.isEmpty()) {
                        systemUIDialog3.setButton(-3, 2131953362, new AnonymousClass2(string2), true);
                    }
                    systemUIDialog3.show();
                    powerNotificationWarnings5.mThermalShutdownDialog = systemUIDialog3;
                }
            } else if ("PNW.dismissedThermalShutdownWarning".equals(action)) {
                PowerNotificationWarnings.this.dismissThermalShutdownWarning();
            } else if ("PNW.autoSaverSuggestion".equals(action)) {
                PowerNotificationWarnings powerNotificationWarnings6 = PowerNotificationWarnings.this;
                Objects.requireNonNull(powerNotificationWarnings6);
                powerNotificationWarnings6.mShowAutoSaverSuggestion = true;
                powerNotificationWarnings6.updateNotification();
            } else if ("PNW.dismissAutoSaverSuggestion".equals(action)) {
                PowerNotificationWarnings powerNotificationWarnings7 = PowerNotificationWarnings.this;
                Objects.requireNonNull(powerNotificationWarnings7);
                powerNotificationWarnings7.mShowAutoSaverSuggestion = false;
                powerNotificationWarnings7.updateNotification();
            } else if ("PNW.enableAutoSaver".equals(action)) {
                PowerNotificationWarnings powerNotificationWarnings8 = PowerNotificationWarnings.this;
                Objects.requireNonNull(powerNotificationWarnings8);
                powerNotificationWarnings8.mShowAutoSaverSuggestion = false;
                powerNotificationWarnings8.updateNotification();
                PowerNotificationWarnings powerNotificationWarnings9 = PowerNotificationWarnings.this;
                Objects.requireNonNull(powerNotificationWarnings9);
                Intent intent2 = new Intent("com.android.settings.BATTERY_SAVER_SCHEDULE_SETTINGS");
                intent2.setFlags(268468224);
                powerNotificationWarnings9.mActivityStarter.startActivity(intent2, true);
            } else if ("PNW.autoSaverNoThanks".equals(action)) {
                PowerNotificationWarnings powerNotificationWarnings10 = PowerNotificationWarnings.this;
                Objects.requireNonNull(powerNotificationWarnings10);
                powerNotificationWarnings10.mShowAutoSaverSuggestion = false;
                powerNotificationWarnings10.updateNotification();
                Settings.Secure.putInt(context.getContentResolver(), "suppress_auto_battery_saver_suggestion", 1);
            }
        }
    }

    @Override // com.android.systemui.power.PowerUI.WarningsUI
    public final void showInvalidChargerWarning() {
        this.mInvalidCharger = true;
        updateNotification();
    }

    static {
        new AudioAttributes.Builder().setContentType(4).setUsage(13).build();
    }

    @Override // com.android.systemui.power.PowerUI.WarningsUI
    public final void dismissHighTemperatureWarning() {
        if (this.mHighTempWarning) {
            dismissHighTemperatureWarningInternal();
        }
    }

    public final void dismissHighTemperatureWarningInternal() {
        this.mNoMan.cancelAsUser("high_temp", 4, UserHandle.ALL);
        this.mHighTempWarning = false;
    }

    @Override // com.android.systemui.power.PowerUI.WarningsUI
    public final void dismissInvalidChargerWarning() {
        if (this.mInvalidCharger) {
            Slog.i("PowerUI.Notification", "dismissing invalid charger notification");
        }
        this.mInvalidCharger = false;
        updateNotification();
    }

    public final void dismissLowBatteryNotification() {
        if (this.mWarning) {
            Slog.i("PowerUI.Notification", "dismissing low battery notification");
        }
        this.mWarning = false;
        updateNotification();
    }

    @Override // com.android.systemui.power.PowerUI.WarningsUI
    public final void dismissLowBatteryWarning() {
        if (DEBUG) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("dismissing low battery warning: level=");
            m.append(this.mBatteryLevel);
            Slog.d("PowerUI.Notification", m.toString());
        }
        dismissLowBatteryNotification();
    }

    public void dismissThermalShutdownWarning() {
        this.mNoMan.cancelAsUser("high_temp", 39, UserHandle.ALL);
    }

    @Override // com.android.systemui.power.PowerUI.WarningsUI
    public final void dump(PrintWriter printWriter) {
        String str;
        String str2;
        String str3;
        printWriter.print("mWarning=");
        printWriter.println(this.mWarning);
        printWriter.print("mPlaySound=");
        printWriter.println(this.mPlaySound);
        printWriter.print("mInvalidCharger=");
        printWriter.println(this.mInvalidCharger);
        printWriter.print("mShowing=");
        printWriter.println(SHOWING_STRINGS[this.mShowing]);
        printWriter.print("mSaverConfirmation=");
        String str4 = "not null";
        if (this.mSaverConfirmation != null) {
            str = str4;
        } else {
            str = null;
        }
        printWriter.println(str);
        printWriter.print("mSaverEnabledConfirmation=");
        printWriter.print("mHighTempWarning=");
        printWriter.println(this.mHighTempWarning);
        printWriter.print("mHighTempDialog=");
        if (this.mHighTempDialog != null) {
            str2 = str4;
        } else {
            str2 = null;
        }
        printWriter.println(str2);
        printWriter.print("mThermalShutdownDialog=");
        if (this.mThermalShutdownDialog != null) {
            str3 = str4;
        } else {
            str3 = null;
        }
        printWriter.println(str3);
        printWriter.print("mUsbHighTempDialog=");
        if (this.mUsbHighTempDialog == null) {
            str4 = null;
        }
        printWriter.println(str4);
    }

    public final PendingIntent pendingBroadcast(String str) {
        return PendingIntent.getBroadcastAsUser(this.mContext, 0, new Intent(str).setPackage(this.mContext.getPackageName()).setFlags(268435456), 67108864, UserHandle.CURRENT);
    }

    @Override // com.android.systemui.power.PowerUI.WarningsUI
    public final void showHighTemperatureWarning() {
        if (!this.mHighTempWarning) {
            this.mHighTempWarning = true;
            String string = this.mContext.getString(2131952445);
            Notification.Builder color = new Notification.Builder(this.mContext, "ALR").setSmallIcon(2131231908).setWhen(0L).setShowWhen(false).setContentTitle(this.mContext.getString(2131952446)).setContentText(string).setStyle(new Notification.BigTextStyle().bigText(string)).setVisibility(1).setContentIntent(pendingBroadcast("PNW.clickedTempWarning")).setDeleteIntent(pendingBroadcast("PNW.dismissedTempWarning")).setColor(Utils.getColorAttrDefaultColor(this.mContext, 16844099));
            SystemUIApplication.overrideNotificationAppName(this.mContext, color, false);
            this.mNoMan.notifyAsUser("high_temp", 4, color.build(), UserHandle.ALL);
        }
    }

    @Override // com.android.systemui.power.PowerUI.WarningsUI
    public final void showThermalShutdownWarning() {
        String string = this.mContext.getString(2131953365);
        Notification.Builder color = new Notification.Builder(this.mContext, "ALR").setSmallIcon(2131231908).setWhen(0L).setShowWhen(false).setContentTitle(this.mContext.getString(2131953366)).setContentText(string).setStyle(new Notification.BigTextStyle().bigText(string)).setVisibility(1).setContentIntent(pendingBroadcast("PNW.clickedThermalShutdownWarning")).setDeleteIntent(pendingBroadcast("PNW.dismissedThermalShutdownWarning")).setColor(Utils.getColorAttrDefaultColor(this.mContext, 16844099));
        SystemUIApplication.overrideNotificationAppName(this.mContext, color, false);
        this.mNoMan.notifyAsUser("high_temp", 39, color.build(), UserHandle.ALL);
    }

    @Override // com.android.systemui.power.PowerUI.WarningsUI
    public final void showUsbHighTemperatureAlarm() {
        this.mHandler.post(new WMShell$7$$ExternalSyntheticLambda0(this, 2));
    }

    @Override // com.android.systemui.power.PowerUI.WarningsUI
    public final void update(int i, int i2) {
        this.mBatteryLevel = i;
        if (i2 >= 0) {
            this.mWarningTriggerTimeMs = 0L;
        } else if (i2 < this.mBucket) {
            this.mWarningTriggerTimeMs = System.currentTimeMillis();
        }
        this.mBucket = i2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:64:0x0277, code lost:
        if (r5 < r2.severeThresholdMillis) goto L_0x0279;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateNotification() {
        /*
            Method dump skipped, instructions count: 863
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.power.PowerNotificationWarnings.updateNotification():void");
    }

    public PowerNotificationWarnings(Context context, ActivityStarter activityStarter) {
        Handler handler = new Handler(Looper.getMainLooper());
        this.mHandler = handler;
        Receiver receiver = new Receiver();
        this.mContext = context;
        this.mNoMan = (NotificationManager) context.getSystemService(NotificationManager.class);
        this.mPowerMan = (PowerManager) context.getSystemService(GlobalActionsDialogLite.GLOBAL_ACTION_KEY_POWER);
        this.mKeyguard = (KeyguardManager) context.getSystemService(KeyguardManager.class);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("PNW.batterySettings");
        intentFilter.addAction("PNW.startSaver");
        intentFilter.addAction("PNW.dismissedWarning");
        intentFilter.addAction("PNW.clickedTempWarning");
        intentFilter.addAction("PNW.dismissedTempWarning");
        intentFilter.addAction("PNW.clickedThermalShutdownWarning");
        intentFilter.addAction("PNW.dismissedThermalShutdownWarning");
        intentFilter.addAction("PNW.startSaverConfirmation");
        intentFilter.addAction("PNW.autoSaverSuggestion");
        intentFilter.addAction("PNW.enableAutoSaver");
        intentFilter.addAction("PNW.autoSaverNoThanks");
        intentFilter.addAction("PNW.dismissAutoSaverSuggestion");
        context.registerReceiverAsUser(receiver, UserHandle.ALL, intentFilter, "android.permission.DEVICE_POWER", handler, 2);
        this.mActivityStarter = activityStarter;
    }

    @Override // com.android.systemui.power.PowerUI.WarningsUI
    public final void showLowBatteryWarning(boolean z) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("show low battery warning: level=");
        m.append(this.mBatteryLevel);
        m.append(" [");
        m.append(this.mBucket);
        m.append("] playSound=");
        m.append(z);
        Slog.i("PowerUI.Notification", m.toString());
        this.mPlaySound = z;
        this.mWarning = true;
        updateNotification();
    }

    @Override // com.android.systemui.power.PowerUI.WarningsUI
    public final void updateSnapshot(BatteryStateSnapshot batteryStateSnapshot) {
        this.mCurrentBatterySnapshot = batteryStateSnapshot;
    }

    @Override // com.android.systemui.power.PowerUI.WarningsUI
    public final boolean isInvalidChargerWarningShowing() {
        return this.mInvalidCharger;
    }

    @Override // com.android.systemui.power.PowerUI.WarningsUI
    public final void updateLowBatteryWarning() {
        updateNotification();
    }

    @Override // com.android.systemui.power.PowerUI.WarningsUI
    public final void userSwitched() {
        updateNotification();
    }
}
