package com.google.android.systemui.columbus;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.Settings;
import com.android.systemui.settings.UserTracker;
import com.google.android.systemui.columbus.ColumbusContentObserver;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__IteratorsJVMKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.functions.Function1;
/* compiled from: ColumbusSettings.kt */
/* loaded from: classes.dex */
public final class ColumbusSettings {
    public static final Uri COLUMBUS_ACTION_URI;
    public static final Uri COLUMBUS_AP_SENSOR_URI;
    public static final Uri COLUMBUS_ENABLED_URI;
    public static final Uri COLUMBUS_LAUNCH_APP_SHORTCUT_URI;
    public static final Uri COLUMBUS_LAUNCH_APP_URI;
    public static final Uri COLUMBUS_LOW_SENSITIVITY_URI;
    public static final Uri COLUMBUS_SILENCE_ALERTS_URI;
    public static final Set<Uri> MONITORED_URIS;
    public final String backupPackage;
    public final ContentResolver contentResolver;
    public final UserTracker userTracker;
    public final LinkedHashSet listeners = new LinkedHashSet();
    public final Function1<Uri, Unit> callback = new ColumbusSettings$callback$1(this);

    /* compiled from: ColumbusSettings.kt */
    /* loaded from: classes.dex */
    public interface ColumbusSettingsChangeListener {
        void onAlertSilenceEnabledChange(boolean z);

        void onColumbusEnabledChange(boolean z);

        void onLowSensitivityChange(boolean z);

        void onSelectedActionChange(String str);

        void onSelectedAppChange(String str);

        void onSelectedAppShortcutChange(String str);

        void onUseApSensorChange();
    }

    static {
        Uri uriFor = Settings.Secure.getUriFor("columbus_enabled");
        COLUMBUS_ENABLED_URI = uriFor;
        Uri uriFor2 = Settings.Secure.getUriFor("columbus_ap_sensor");
        COLUMBUS_AP_SENSOR_URI = uriFor2;
        Uri uriFor3 = Settings.Secure.getUriFor("columbus_action");
        COLUMBUS_ACTION_URI = uriFor3;
        Uri uriFor4 = Settings.Secure.getUriFor("columbus_launch_app");
        COLUMBUS_LAUNCH_APP_URI = uriFor4;
        Uri uriFor5 = Settings.Secure.getUriFor("columbus_launch_app_shortcut");
        COLUMBUS_LAUNCH_APP_SHORTCUT_URI = uriFor5;
        Uri uriFor6 = Settings.Secure.getUriFor("columbus_low_sensitivity");
        COLUMBUS_LOW_SENSITIVITY_URI = uriFor6;
        Uri uriFor7 = Settings.Secure.getUriFor("columbus_silence_alerts");
        COLUMBUS_SILENCE_ALERTS_URI = uriFor7;
        MONITORED_URIS = SetsKt__SetsKt.setOf(uriFor, uriFor2, uriFor3, uriFor4, uriFor5, uriFor6, uriFor7);
    }

    public final boolean isColumbusEnabled() {
        if (Settings.Secure.getIntForUser(this.contentResolver, "columbus_enabled", 0, this.userTracker.getUserId()) != 0) {
            return true;
        }
        return false;
    }

    public final void registerColumbusSettingsChangeListener(ColumbusSettingsChangeListener columbusSettingsChangeListener) {
        this.listeners.add(columbusSettingsChangeListener);
    }

    public final String selectedAction() {
        String stringForUser = Settings.Secure.getStringForUser(this.contentResolver, "columbus_action", this.userTracker.getUserId());
        if (stringForUser == null) {
            return "";
        }
        return stringForUser;
    }

    public final String selectedApp() {
        String stringForUser = Settings.Secure.getStringForUser(this.contentResolver, "columbus_launch_app", this.userTracker.getUserId());
        if (stringForUser == null) {
            return "";
        }
        return stringForUser;
    }

    public ColumbusSettings(Context context, UserTracker userTracker, ColumbusContentObserver.Factory factory) {
        this.userTracker = userTracker;
        this.backupPackage = context.getBasePackageName();
        this.contentResolver = context.getContentResolver();
        Set<Uri> set = MONITORED_URIS;
        ArrayList arrayList = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(set, 10));
        for (Uri uri : set) {
            Function1<Uri, Unit> function1 = this.callback;
            Objects.requireNonNull(factory);
            arrayList.add(new ColumbusContentObserver(factory.contentResolver, uri, function1, factory.userTracker, factory.executor, factory.handler));
        }
        for (ColumbusContentObserver columbusContentObserver : CollectionsKt___CollectionsKt.toSet(arrayList)) {
            Objects.requireNonNull(columbusContentObserver);
            columbusContentObserver.userTracker.addCallback(columbusContentObserver.userTrackerCallback, columbusContentObserver.executor);
            columbusContentObserver.updateContentObserver();
        }
    }
}
