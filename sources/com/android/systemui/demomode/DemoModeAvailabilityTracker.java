package com.android.systemui.demomode;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import java.util.Objects;
/* compiled from: DemoModeAvailabilityTracker.kt */
/* loaded from: classes.dex */
public abstract class DemoModeAvailabilityTracker {
    public final DemoModeAvailabilityTracker$allowedObserver$1 allowedObserver;
    public final Context context;
    public boolean isDemoModeAvailable;
    public boolean isInDemoMode;
    public final DemoModeAvailabilityTracker$onObserver$1 onObserver;

    public abstract void onDemoModeAvailabilityChanged();

    public abstract void onDemoModeFinished();

    public abstract void onDemoModeStarted();

    public final void startTracking() {
        ContentResolver contentResolver = this.context.getContentResolver();
        contentResolver.registerContentObserver(Settings.Global.getUriFor("sysui_demo_allowed"), false, this.allowedObserver);
        contentResolver.registerContentObserver(Settings.Global.getUriFor("sysui_tuner_demo_on"), false, this.onObserver);
    }

    /* JADX WARN: Type inference failed for: r0v5, types: [com.android.systemui.demomode.DemoModeAvailabilityTracker$allowedObserver$1] */
    /* JADX WARN: Type inference failed for: r0v7, types: [com.android.systemui.demomode.DemoModeAvailabilityTracker$onObserver$1] */
    public DemoModeAvailabilityTracker(Context context) {
        boolean z;
        this.context = context;
        boolean z2 = false;
        if (Settings.Global.getInt(context.getContentResolver(), "sysui_tuner_demo_on", 0) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.isInDemoMode = z;
        this.isDemoModeAvailable = Settings.Global.getInt(context.getContentResolver(), "sysui_demo_allowed", 0) != 0 ? true : z2;
        final Handler handler = new Handler(Looper.getMainLooper());
        this.allowedObserver = new ContentObserver(handler) { // from class: com.android.systemui.demomode.DemoModeAvailabilityTracker$allowedObserver$1
            @Override // android.database.ContentObserver
            public final void onChange(boolean z3) {
                DemoModeAvailabilityTracker demoModeAvailabilityTracker = DemoModeAvailabilityTracker.this;
                Objects.requireNonNull(demoModeAvailabilityTracker);
                boolean z4 = false;
                if (Settings.Global.getInt(demoModeAvailabilityTracker.context.getContentResolver(), "sysui_demo_allowed", 0) != 0) {
                    z4 = true;
                }
                DemoModeAvailabilityTracker demoModeAvailabilityTracker2 = DemoModeAvailabilityTracker.this;
                Objects.requireNonNull(demoModeAvailabilityTracker2);
                if (demoModeAvailabilityTracker2.isDemoModeAvailable != z4) {
                    DemoModeAvailabilityTracker demoModeAvailabilityTracker3 = DemoModeAvailabilityTracker.this;
                    Objects.requireNonNull(demoModeAvailabilityTracker3);
                    demoModeAvailabilityTracker3.isDemoModeAvailable = z4;
                    DemoModeAvailabilityTracker.this.onDemoModeAvailabilityChanged();
                }
            }
        };
        final Handler handler2 = new Handler(Looper.getMainLooper());
        this.onObserver = new ContentObserver(handler2) { // from class: com.android.systemui.demomode.DemoModeAvailabilityTracker$onObserver$1
            @Override // android.database.ContentObserver
            public final void onChange(boolean z3) {
                DemoModeAvailabilityTracker demoModeAvailabilityTracker = DemoModeAvailabilityTracker.this;
                Objects.requireNonNull(demoModeAvailabilityTracker);
                boolean z4 = false;
                if (Settings.Global.getInt(demoModeAvailabilityTracker.context.getContentResolver(), "sysui_tuner_demo_on", 0) != 0) {
                    z4 = true;
                }
                DemoModeAvailabilityTracker demoModeAvailabilityTracker2 = DemoModeAvailabilityTracker.this;
                Objects.requireNonNull(demoModeAvailabilityTracker2);
                if (demoModeAvailabilityTracker2.isInDemoMode != z4) {
                    DemoModeAvailabilityTracker demoModeAvailabilityTracker3 = DemoModeAvailabilityTracker.this;
                    Objects.requireNonNull(demoModeAvailabilityTracker3);
                    demoModeAvailabilityTracker3.isInDemoMode = z4;
                    if (z4) {
                        DemoModeAvailabilityTracker.this.onDemoModeStarted();
                    } else {
                        DemoModeAvailabilityTracker.this.onDemoModeFinished();
                    }
                }
            }
        };
    }
}
