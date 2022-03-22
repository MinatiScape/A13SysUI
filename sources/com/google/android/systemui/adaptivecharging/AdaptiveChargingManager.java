package com.google.android.systemui.adaptivecharging;

import android.content.Context;
import android.os.IHwBinder;
import android.os.LocaleList;
import android.os.RemoteException;
import android.text.format.DateFormat;
import android.util.Log;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline3;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;
import vendor.google.google_battery.V1_2.IGoogleBattery;
/* loaded from: classes.dex */
public final class AdaptiveChargingManager {
    public static final boolean DEBUG = Log.isLoggable("AdaptiveChargingManager", 3);
    public Context mContext;

    /* renamed from: com.google.android.systemui.adaptivecharging.AdaptiveChargingManager$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 {
        public final /* synthetic */ IHwBinder.DeathRecipient val$deathRecipient;
        public final /* synthetic */ IGoogleBattery val$googleBattery;
        public final /* synthetic */ AdaptiveChargingStatusReceiver val$receiver;

        public AnonymousClass2(AdaptiveChargingStatusReceiver adaptiveChargingStatusReceiver, IGoogleBattery iGoogleBattery, AnonymousClass1 r4) {
            this.val$receiver = adaptiveChargingStatusReceiver;
            this.val$googleBattery = iGoogleBattery;
            this.val$deathRecipient = r4;
        }

        public final void onValues(byte b, String str, int i) {
            if (AdaptiveChargingManager.DEBUG) {
                StringBuilder sb = new StringBuilder();
                sb.append("getChargingStageDeadlineCallback result: ");
                sb.append((int) b);
                sb.append(", stage: \"");
                sb.append(str);
                sb.append("\", seconds: ");
                KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(sb, i, "AdaptiveChargingManager");
            }
            if (b == 0) {
                this.val$receiver.onReceiveStatus(str, i);
            }
            AdaptiveChargingManager adaptiveChargingManager = AdaptiveChargingManager.this;
            IGoogleBattery iGoogleBattery = this.val$googleBattery;
            IHwBinder.DeathRecipient deathRecipient = this.val$deathRecipient;
            Objects.requireNonNull(adaptiveChargingManager);
            AdaptiveChargingManager.destroyHalInterface(iGoogleBattery, deathRecipient);
            this.val$receiver.onDestroyInterface();
        }
    }

    /* loaded from: classes.dex */
    public interface AdaptiveChargingStatusReceiver {
        void onDestroyInterface();

        void onReceiveStatus(String str, int i);
    }

    public static void destroyHalInterface(IGoogleBattery iGoogleBattery, IHwBinder.DeathRecipient deathRecipient) {
        if (DEBUG) {
            Log.d("AdaptiveChargingManager", "destroyHalInterface");
        }
        if (deathRecipient != null) {
            try {
                iGoogleBattery.unlinkToDeath(deathRecipient);
            } catch (RemoteException e) {
                Log.e("AdaptiveChargingManager", "unlinkToDeath failed: ", e);
            }
        }
    }

    public static IGoogleBattery initHalInterface(AnonymousClass1 r2) {
        if (DEBUG) {
            Log.d("AdaptiveChargingManager", "initHalInterface");
        }
        try {
            IGoogleBattery service = IGoogleBattery.getService();
            if (!(service == null || r2 == null)) {
                service.linkToDeath(r2);
            }
            return service;
        } catch (RemoteException | NoSuchElementException e) {
            Log.e("AdaptiveChargingManager", "failed to get Google Battery HAL: ", e);
            return null;
        }
    }

    public final String formatTimeToFull(long j) {
        String str;
        Locale locale;
        if (DateFormat.is24HourFormat(this.mContext)) {
            str = "Hm";
        } else {
            str = "hma";
        }
        LocaleList locales = this.mContext.getResources().getConfiguration().getLocales();
        if (locales == null || locales.isEmpty()) {
            locale = Locale.getDefault();
        } else {
            locale = locales.get(0);
        }
        return DateFormat.format(DateFormat.getBestDateTimePattern(locale, str), j).toString();
    }

    public boolean hasAdaptiveChargingFeature() {
        return this.mContext.getPackageManager().hasSystemFeature("com.google.android.feature.ADAPTIVE_CHARGING");
    }

    public final void queryStatus(final AdaptiveChargingStatusReceiver adaptiveChargingStatusReceiver) {
        IHwBinder.DeathRecipient deathRecipient = new IHwBinder.DeathRecipient() { // from class: com.google.android.systemui.adaptivecharging.AdaptiveChargingManager.1
            public final void serviceDied(long j) {
                if (AdaptiveChargingManager.DEBUG) {
                    Log.d("AdaptiveChargingManager", "serviceDied");
                }
                AdaptiveChargingStatusReceiver.this.onDestroyInterface();
            }
        };
        IGoogleBattery initHalInterface = initHalInterface(deathRecipient);
        if (initHalInterface == null) {
            adaptiveChargingStatusReceiver.onDestroyInterface();
            return;
        }
        try {
            initHalInterface.getChargingStageAndDeadline(new AnonymousClass2(adaptiveChargingStatusReceiver, initHalInterface, deathRecipient));
        } catch (RemoteException e) {
            Log.e("AdaptiveChargingManager", "Failed to get Adaptive Chaging status: ", e);
            destroyHalInterface(initHalInterface, deathRecipient);
            adaptiveChargingStatusReceiver.onDestroyInterface();
        }
    }

    public AdaptiveChargingManager(Context context) {
        this.mContext = context;
    }
}
