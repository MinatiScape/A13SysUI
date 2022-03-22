package com.google.android.systemui.smartspace;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Handler;
import android.text.format.DateFormat;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.statusbar.policy.NextAlarmController;
import com.android.systemui.statusbar.policy.ZenModeController;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.reflect.KFunction;
/* compiled from: KeyguardZenAlarmViewController.kt */
/* loaded from: classes.dex */
public final class KeyguardZenAlarmViewController {
    public final Drawable alarmImage;
    public final AlarmManager alarmManager;
    public final Context context;
    public final Drawable dndImage;
    public final Handler handler;
    public final NextAlarmController nextAlarmController;
    public final BcSmartspaceDataPlugin plugin;
    public final ZenModeController zenModeController;
    public LinkedHashSet smartspaceViews = new LinkedHashSet();
    public final KFunction<Unit> showNextAlarm = new KeyguardZenAlarmViewController$showNextAlarm$1(this);
    public final KeyguardZenAlarmViewController$zenModeCallback$1 zenModeCallback = new ZenModeController.Callback() { // from class: com.google.android.systemui.smartspace.KeyguardZenAlarmViewController$zenModeCallback$1
        @Override // com.android.systemui.statusbar.policy.ZenModeController.Callback
        public final void onZenChanged(int i) {
            KeyguardZenAlarmViewController.this.updateDnd();
        }
    };
    public final KeyguardZenAlarmViewController$nextAlarmCallback$1 nextAlarmCallback = new NextAlarmController.NextAlarmChangeCallback() { // from class: com.google.android.systemui.smartspace.KeyguardZenAlarmViewController$nextAlarmCallback$1
        @Override // com.android.systemui.statusbar.policy.NextAlarmController.NextAlarmChangeCallback
        public final void onNextAlarmChanged(AlarmManager.AlarmClockInfo alarmClockInfo) {
            KeyguardZenAlarmViewController.this.updateNextAlarm();
        }
    };

    @VisibleForTesting
    public static /* synthetic */ void getSmartspaceViews$annotations() {
    }

    @VisibleForTesting
    public final void showAlarm() {
        boolean z;
        String str;
        long nextAlarm = this.zenModeController.getNextAlarm();
        if (nextAlarm > 0) {
            if (nextAlarm <= TimeUnit.HOURS.toMillis(12L) + System.currentTimeMillis()) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                if (DateFormat.is24HourFormat(this.context, ActivityManager.getCurrentUser())) {
                    str = "HH:mm";
                } else {
                    str = "h:mm";
                }
                String obj = DateFormat.format(str, nextAlarm).toString();
                for (BcSmartspaceDataPlugin.SmartspaceView smartspaceView : this.smartspaceViews) {
                    smartspaceView.setNextAlarm(this.alarmImage, obj);
                }
                return;
            }
        }
        for (BcSmartspaceDataPlugin.SmartspaceView smartspaceView2 : this.smartspaceViews) {
            smartspaceView2.setNextAlarm(null, null);
        }
    }

    @VisibleForTesting
    public final void updateDnd() {
        boolean z;
        if (this.zenModeController.getZen() != 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            String string = this.context.getResources().getString(2131951788);
            for (BcSmartspaceDataPlugin.SmartspaceView smartspaceView : this.smartspaceViews) {
                smartspaceView.setDnd(this.dndImage, string);
            }
            return;
        }
        for (BcSmartspaceDataPlugin.SmartspaceView smartspaceView2 : this.smartspaceViews) {
            smartspaceView2.setDnd(null, null);
        }
    }

    public final void updateNextAlarm() {
        AlarmManager alarmManager = this.alarmManager;
        final Function0 function0 = (Function0) this.showNextAlarm;
        alarmManager.cancel(new AlarmManager.OnAlarmListener() { // from class: com.google.android.systemui.smartspace.KeyguardZenAlarmViewController$sam$android_app_AlarmManager_OnAlarmListener$0
            @Override // android.app.AlarmManager.OnAlarmListener
            public final /* synthetic */ void onAlarm() {
                Function0.this.invoke();
            }
        });
        long nextAlarm = this.zenModeController.getNextAlarm();
        if (nextAlarm > 0) {
            long millis = nextAlarm - TimeUnit.HOURS.toMillis(12L);
            if (millis > 0) {
                AlarmManager alarmManager2 = this.alarmManager;
                final Function0 function02 = (Function0) this.showNextAlarm;
                alarmManager2.setExact(1, millis, "lock_screen_next_alarm", new AlarmManager.OnAlarmListener() { // from class: com.google.android.systemui.smartspace.KeyguardZenAlarmViewController$sam$android_app_AlarmManager_OnAlarmListener$0
                    @Override // android.app.AlarmManager.OnAlarmListener
                    public final /* synthetic */ void onAlarm() {
                        Function0.this.invoke();
                    }
                }, this.handler);
            }
        }
        showAlarm();
    }

    /* JADX WARN: Type inference failed for: r2v3, types: [com.google.android.systemui.smartspace.KeyguardZenAlarmViewController$zenModeCallback$1] */
    /* JADX WARN: Type inference failed for: r2v4, types: [com.google.android.systemui.smartspace.KeyguardZenAlarmViewController$nextAlarmCallback$1] */
    public KeyguardZenAlarmViewController(Context context, BcSmartspaceDataPlugin bcSmartspaceDataPlugin, ZenModeController zenModeController, AlarmManager alarmManager, NextAlarmController nextAlarmController, Handler handler) {
        this.context = context;
        this.plugin = bcSmartspaceDataPlugin;
        this.zenModeController = zenModeController;
        this.alarmManager = alarmManager;
        this.nextAlarmController = nextAlarmController;
        this.handler = handler;
        Drawable drawable = context.getResources().getDrawable(2131232678, null);
        Objects.requireNonNull(drawable, "null cannot be cast to non-null type android.graphics.drawable.InsetDrawable");
        this.dndImage = ((InsetDrawable) drawable).getDrawable();
        this.alarmImage = context.getResources().getDrawable(2131231739, null);
    }
}
