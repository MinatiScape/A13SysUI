package com.android.systemui.battery;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import com.android.settingslib.graph.ThemedBatteryDrawable;
import com.android.settingslib.graph.ThemedBatteryDrawable$sam$java_lang_Runnable$0;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.ViewController;
import java.util.Objects;
/* loaded from: classes.dex */
public final class BatteryMeterViewController extends ViewController<BatteryMeterView> {
    public final BatteryController mBatteryController;
    public final ConfigurationController mConfigurationController;
    public final ContentResolver mContentResolver;
    public final AnonymousClass4 mCurrentUserTracker;
    public boolean mIgnoreTunerUpdates;
    public boolean mIsSubscribedForTunerUpdates;
    public final SettingObserver mSettingObserver;
    public final TunerService mTunerService;
    public final AnonymousClass1 mConfigurationListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.battery.BatteryMeterViewController.1
        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onDensityOrFontScaleChanged() {
            BatteryMeterView batteryMeterView = (BatteryMeterView) BatteryMeterViewController.this.mView;
            Objects.requireNonNull(batteryMeterView);
            Resources resources = batteryMeterView.getContext().getResources();
            TypedValue typedValue = new TypedValue();
            resources.getValue(2131167066, typedValue, true);
            float f = typedValue.getFloat();
            int dimensionPixelSize = resources.getDimensionPixelSize(2131167052);
            int dimensionPixelSize2 = resources.getDimensionPixelSize(2131167053);
            int dimensionPixelSize3 = resources.getDimensionPixelSize(2131165358);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) (dimensionPixelSize2 * f), (int) (dimensionPixelSize * f));
            layoutParams.setMargins(0, 0, 0, dimensionPixelSize3);
            batteryMeterView.mBatteryIconView.setLayoutParams(layoutParams);
        }
    };
    public final AnonymousClass2 mTunable = new TunerService.Tunable() { // from class: com.android.systemui.battery.BatteryMeterViewController.2
        @Override // com.android.systemui.tuner.TunerService.Tunable
        public final void onTuningChanged(String str, String str2) {
            int i;
            if ("icon_blacklist".equals(str)) {
                ArraySet<String> iconHideList = StatusBarIconController.getIconHideList(BatteryMeterViewController.this.getContext(), str2);
                BatteryMeterViewController batteryMeterViewController = BatteryMeterViewController.this;
                BatteryMeterView batteryMeterView = (BatteryMeterView) batteryMeterViewController.mView;
                if (iconHideList.contains(batteryMeterViewController.mSlotBattery)) {
                    i = 8;
                } else {
                    i = 0;
                }
                batteryMeterView.setVisibility(i);
            }
        }
    };
    public final AnonymousClass3 mBatteryStateChangeCallback = new BatteryController.BatteryStateChangeCallback() { // from class: com.android.systemui.battery.BatteryMeterViewController.3
        @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
        public final void onBatteryLevelChanged(int i, boolean z, boolean z2) {
            boolean z3;
            BatteryMeterView batteryMeterView = (BatteryMeterView) BatteryMeterViewController.this.mView;
            Objects.requireNonNull(batteryMeterView);
            ThemedBatteryDrawable themedBatteryDrawable = batteryMeterView.mDrawable;
            Objects.requireNonNull(themedBatteryDrawable);
            themedBatteryDrawable.charging = z;
            themedBatteryDrawable.unscheduleSelf(new ThemedBatteryDrawable$sam$java_lang_Runnable$0(themedBatteryDrawable.invalidateRunnable));
            themedBatteryDrawable.scheduleSelf(new ThemedBatteryDrawable$sam$java_lang_Runnable$0(themedBatteryDrawable.invalidateRunnable), 0L);
            ThemedBatteryDrawable themedBatteryDrawable2 = batteryMeterView.mDrawable;
            Objects.requireNonNull(themedBatteryDrawable2);
            if (i >= 67) {
                z3 = true;
            } else if (i <= 33) {
                z3 = false;
            } else {
                z3 = themedBatteryDrawable2.invertFillIcon;
            }
            themedBatteryDrawable2.invertFillIcon = z3;
            themedBatteryDrawable2.batteryLevel = i;
            themedBatteryDrawable2.levelColor = themedBatteryDrawable2.batteryColorForLevel(i);
            themedBatteryDrawable2.invalidateSelf();
            batteryMeterView.mCharging = z;
            batteryMeterView.mLevel = i;
            batteryMeterView.updatePercentText();
        }

        @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
        public final void onBatteryUnknownStateChanged(boolean z) {
            ((BatteryMeterView) BatteryMeterViewController.this.mView).onBatteryUnknownStateChanged(z);
        }

        @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
        public final void onPowerSaveChanged(boolean z) {
            BatteryMeterView batteryMeterView = (BatteryMeterView) BatteryMeterViewController.this.mView;
            Objects.requireNonNull(batteryMeterView);
            ThemedBatteryDrawable themedBatteryDrawable = batteryMeterView.mDrawable;
            Objects.requireNonNull(themedBatteryDrawable);
            themedBatteryDrawable.powerSaveEnabled = z;
            themedBatteryDrawable.unscheduleSelf(new ThemedBatteryDrawable$sam$java_lang_Runnable$0(themedBatteryDrawable.invalidateRunnable));
            themedBatteryDrawable.scheduleSelf(new ThemedBatteryDrawable$sam$java_lang_Runnable$0(themedBatteryDrawable.invalidateRunnable), 0L);
        }
    };
    public final String mSlotBattery = getResources().getString(17041535);

    /* loaded from: classes.dex */
    public final class SettingObserver extends ContentObserver {
        public SettingObserver(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public final void onChange(boolean z, Uri uri) {
            super.onChange(z, uri);
            ((BatteryMeterView) BatteryMeterViewController.this.mView).updateShowPercent();
            if (TextUtils.equals(uri.getLastPathSegment(), "battery_estimates_last_update_time")) {
                ((BatteryMeterView) BatteryMeterViewController.this.mView).updatePercentText();
            }
        }
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        if (!this.mIsSubscribedForTunerUpdates && !this.mIgnoreTunerUpdates) {
            this.mTunerService.addTunable(this.mTunable, "icon_blacklist");
            this.mIsSubscribedForTunerUpdates = true;
        }
        this.mBatteryController.addCallback(this.mBatteryStateChangeCallback);
        this.mContentResolver.registerContentObserver(Settings.System.getUriFor("status_bar_show_battery_percent"), false, this.mSettingObserver, ActivityManager.getCurrentUser());
        this.mContentResolver.registerContentObserver(Settings.Global.getUriFor("battery_estimates_last_update_time"), false, this.mSettingObserver);
        startTracking();
        ((BatteryMeterView) this.mView).updateShowPercent();
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        if (this.mIsSubscribedForTunerUpdates) {
            this.mTunerService.removeTunable(this.mTunable);
            this.mIsSubscribedForTunerUpdates = false;
        }
        this.mBatteryController.removeCallback(this.mBatteryStateChangeCallback);
        stopTracking();
        this.mContentResolver.unregisterContentObserver(this.mSettingObserver);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.battery.BatteryMeterViewController$1] */
    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.systemui.battery.BatteryMeterViewController$2] */
    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.systemui.battery.BatteryMeterViewController$3] */
    /* JADX WARN: Type inference failed for: r2v4, types: [com.android.systemui.battery.BatteryMeterViewController$4] */
    public BatteryMeterViewController(BatteryMeterView batteryMeterView, ConfigurationController configurationController, TunerService tunerService, BroadcastDispatcher broadcastDispatcher, Handler handler, final ContentResolver contentResolver, BatteryController batteryController) {
        super(batteryMeterView);
        this.mConfigurationController = configurationController;
        this.mTunerService = tunerService;
        this.mContentResolver = contentResolver;
        this.mBatteryController = batteryController;
        Objects.requireNonNull(batteryController);
        BatteryMeterViewController$$ExternalSyntheticLambda0 batteryMeterViewController$$ExternalSyntheticLambda0 = new BatteryMeterViewController$$ExternalSyntheticLambda0(batteryController);
        Objects.requireNonNull(batteryMeterView);
        batteryMeterView.mBatteryEstimateFetcher = batteryMeterViewController$$ExternalSyntheticLambda0;
        this.mSettingObserver = new SettingObserver(handler);
        this.mCurrentUserTracker = new CurrentUserTracker(broadcastDispatcher) { // from class: com.android.systemui.battery.BatteryMeterViewController.4
            @Override // com.android.systemui.settings.CurrentUserTracker
            public final void onUserSwitched(int i) {
                contentResolver.unregisterContentObserver(BatteryMeterViewController.this.mSettingObserver);
                BatteryMeterViewController batteryMeterViewController = BatteryMeterViewController.this;
                Objects.requireNonNull(batteryMeterViewController);
                batteryMeterViewController.mContentResolver.registerContentObserver(Settings.System.getUriFor("status_bar_show_battery_percent"), false, batteryMeterViewController.mSettingObserver, i);
                ((BatteryMeterView) BatteryMeterViewController.this.mView).updateShowPercent();
            }
        };
    }
}
