package com.google.android.systemui.lowlightclock;

import android.content.res.Resources;
import android.os.SystemClock;
import android.util.Log;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.google.android.systemui.lowlightclock.AmbientLightModeMonitor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: LightSensorEventsDebounceAlgorithm.kt */
/* loaded from: classes.dex */
public final class LightSensorEventsDebounceAlgorithm implements AmbientLightModeMonitor.DebounceAlgorithm {
    public static final boolean DEBUG = Log.isLoggable("LightDebounceAlgorithm", 3);
    public AmbientLightModeMonitor.Callback callback;
    public final int darkModeThreshold;
    public final int darkSamplingFrequencyMillis;
    public final int darkSamplingSpanMillis;
    public final DelayableExecutor executor;
    public boolean isDarkMode;
    public boolean isLightMode;
    public final int lightModeThreshold;
    public final int lightSamplingFrequencyMillis;
    public final int lightSamplingSpanMillis;
    public final ArrayList<ArrayList<Float>> bundlesQueueLightMode = new ArrayList<>();
    public final ArrayList<ArrayList<Float>> bundlesQueueDarkMode = new ArrayList<>();
    public int mode = 2;
    public ArrayList<Float> bundleLightMode = new ArrayList<>();
    public ArrayList<Float> bundleDarkMode = new ArrayList<>();
    public final LightSensorEventsDebounceAlgorithm$enqueueLightModeBundle$1 enqueueLightModeBundle = new Runnable() { // from class: com.google.android.systemui.lowlightclock.LightSensorEventsDebounceAlgorithm$enqueueLightModeBundle$1
        @Override // java.lang.Runnable
        public final void run() {
            if (LightSensorEventsDebounceAlgorithm.DEBUG) {
                Log.d("LightDebounceAlgorithm", "enqueueing a light mode bundle");
            }
            LightSensorEventsDebounceAlgorithm lightSensorEventsDebounceAlgorithm = LightSensorEventsDebounceAlgorithm.this;
            Objects.requireNonNull(lightSensorEventsDebounceAlgorithm);
            lightSensorEventsDebounceAlgorithm.bundlesQueueLightMode.add(new ArrayList<>());
            LightSensorEventsDebounceAlgorithm lightSensorEventsDebounceAlgorithm2 = LightSensorEventsDebounceAlgorithm.this;
            lightSensorEventsDebounceAlgorithm2.executor.executeDelayed(lightSensorEventsDebounceAlgorithm2.dequeueLightModeBundle, lightSensorEventsDebounceAlgorithm2.lightSamplingSpanMillis);
            LightSensorEventsDebounceAlgorithm lightSensorEventsDebounceAlgorithm3 = LightSensorEventsDebounceAlgorithm.this;
            lightSensorEventsDebounceAlgorithm3.executor.executeDelayed(this, lightSensorEventsDebounceAlgorithm3.lightSamplingFrequencyMillis);
        }
    };
    public final LightSensorEventsDebounceAlgorithm$enqueueDarkModeBundle$1 enqueueDarkModeBundle = new Runnable() { // from class: com.google.android.systemui.lowlightclock.LightSensorEventsDebounceAlgorithm$enqueueDarkModeBundle$1
        @Override // java.lang.Runnable
        public final void run() {
            if (LightSensorEventsDebounceAlgorithm.DEBUG) {
                Log.d("LightDebounceAlgorithm", "enqueueing a dark mode bundle");
            }
            LightSensorEventsDebounceAlgorithm lightSensorEventsDebounceAlgorithm = LightSensorEventsDebounceAlgorithm.this;
            Objects.requireNonNull(lightSensorEventsDebounceAlgorithm);
            lightSensorEventsDebounceAlgorithm.bundlesQueueDarkMode.add(new ArrayList<>());
            LightSensorEventsDebounceAlgorithm lightSensorEventsDebounceAlgorithm2 = LightSensorEventsDebounceAlgorithm.this;
            lightSensorEventsDebounceAlgorithm2.executor.executeDelayed(lightSensorEventsDebounceAlgorithm2.dequeueDarkModeBundle, lightSensorEventsDebounceAlgorithm2.darkSamplingSpanMillis);
            LightSensorEventsDebounceAlgorithm lightSensorEventsDebounceAlgorithm3 = LightSensorEventsDebounceAlgorithm.this;
            lightSensorEventsDebounceAlgorithm3.executor.executeDelayed(this, lightSensorEventsDebounceAlgorithm3.darkSamplingFrequencyMillis);
        }
    };
    public final LightSensorEventsDebounceAlgorithm$dequeueLightModeBundle$1 dequeueLightModeBundle = new Runnable() { // from class: com.google.android.systemui.lowlightclock.LightSensorEventsDebounceAlgorithm$dequeueLightModeBundle$1
        @Override // java.lang.Runnable
        public final void run() {
            boolean z;
            LightSensorEventsDebounceAlgorithm lightSensorEventsDebounceAlgorithm = LightSensorEventsDebounceAlgorithm.this;
            Objects.requireNonNull(lightSensorEventsDebounceAlgorithm);
            if (!lightSensorEventsDebounceAlgorithm.bundlesQueueLightMode.isEmpty()) {
                LightSensorEventsDebounceAlgorithm lightSensorEventsDebounceAlgorithm2 = LightSensorEventsDebounceAlgorithm.this;
                Objects.requireNonNull(lightSensorEventsDebounceAlgorithm2);
                int i = 0;
                ArrayList<Float> remove = lightSensorEventsDebounceAlgorithm2.bundlesQueueLightMode.remove(0);
                lightSensorEventsDebounceAlgorithm2.bundleLightMode = remove;
                double averageOfFloat = CollectionsKt___CollectionsKt.averageOfFloat(remove);
                if (!Double.isNaN(averageOfFloat)) {
                    boolean z2 = LightSensorEventsDebounceAlgorithm.DEBUG;
                    if (z2) {
                        Log.d("LightDebounceAlgorithm", Intrinsics.stringPlus("light mode average: ", Double.valueOf(averageOfFloat)));
                    }
                    if (averageOfFloat > lightSensorEventsDebounceAlgorithm2.lightModeThreshold) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (lightSensorEventsDebounceAlgorithm2.isLightMode != z) {
                        lightSensorEventsDebounceAlgorithm2.isLightMode = z;
                        if (z2) {
                            Log.d("LightDebounceAlgorithm", Intrinsics.stringPlus("isLightMode: ", Boolean.valueOf(z)));
                        }
                        if (lightSensorEventsDebounceAlgorithm2.isDarkMode) {
                            i = 1;
                        } else if (!z) {
                            i = 2;
                        }
                        lightSensorEventsDebounceAlgorithm2.setMode(i);
                    }
                }
                if (LightSensorEventsDebounceAlgorithm.DEBUG) {
                    LightSensorEventsDebounceAlgorithm lightSensorEventsDebounceAlgorithm3 = LightSensorEventsDebounceAlgorithm.this;
                    Objects.requireNonNull(lightSensorEventsDebounceAlgorithm3);
                    Log.d("LightDebounceAlgorithm", Intrinsics.stringPlus("dequeued a light mode bundle of size ", Integer.valueOf(lightSensorEventsDebounceAlgorithm3.bundleLightMode.size())));
                }
            }
        }
    };
    public final LightSensorEventsDebounceAlgorithm$dequeueDarkModeBundle$1 dequeueDarkModeBundle = new Runnable() { // from class: com.google.android.systemui.lowlightclock.LightSensorEventsDebounceAlgorithm$dequeueDarkModeBundle$1
        @Override // java.lang.Runnable
        public final void run() {
            boolean z;
            LightSensorEventsDebounceAlgorithm lightSensorEventsDebounceAlgorithm = LightSensorEventsDebounceAlgorithm.this;
            Objects.requireNonNull(lightSensorEventsDebounceAlgorithm);
            if (!lightSensorEventsDebounceAlgorithm.bundlesQueueDarkMode.isEmpty()) {
                LightSensorEventsDebounceAlgorithm lightSensorEventsDebounceAlgorithm2 = LightSensorEventsDebounceAlgorithm.this;
                Objects.requireNonNull(lightSensorEventsDebounceAlgorithm2);
                int i = 0;
                ArrayList<Float> remove = lightSensorEventsDebounceAlgorithm2.bundlesQueueDarkMode.remove(0);
                lightSensorEventsDebounceAlgorithm2.bundleDarkMode = remove;
                double averageOfFloat = CollectionsKt___CollectionsKt.averageOfFloat(remove);
                if (!Double.isNaN(averageOfFloat)) {
                    boolean z2 = LightSensorEventsDebounceAlgorithm.DEBUG;
                    if (z2) {
                        Log.d("LightDebounceAlgorithm", Intrinsics.stringPlus("dark mode average: ", Double.valueOf(averageOfFloat)));
                    }
                    if (averageOfFloat < lightSensorEventsDebounceAlgorithm2.darkModeThreshold) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (lightSensorEventsDebounceAlgorithm2.isDarkMode != z) {
                        lightSensorEventsDebounceAlgorithm2.isDarkMode = z;
                        if (z2) {
                            Log.d("LightDebounceAlgorithm", Intrinsics.stringPlus("isDarkMode: ", Boolean.valueOf(z)));
                        }
                        if (z) {
                            i = 1;
                        } else if (!lightSensorEventsDebounceAlgorithm2.isLightMode) {
                            i = 2;
                        }
                        lightSensorEventsDebounceAlgorithm2.setMode(i);
                    }
                }
                if (LightSensorEventsDebounceAlgorithm.DEBUG) {
                    LightSensorEventsDebounceAlgorithm lightSensorEventsDebounceAlgorithm3 = LightSensorEventsDebounceAlgorithm.this;
                    Objects.requireNonNull(lightSensorEventsDebounceAlgorithm3);
                    Log.d("LightDebounceAlgorithm", Intrinsics.stringPlus("dequeued a dark mode bundle of size ", Integer.valueOf(lightSensorEventsDebounceAlgorithm3.bundleDarkMode.size())));
                }
            }
        }
    };

    @Override // com.google.android.systemui.lowlightclock.AmbientLightModeMonitor.DebounceAlgorithm
    public final void onUpdateLightSensorEvent(float f) {
        if (this.callback != null) {
            Iterator<ArrayList<Float>> it = this.bundlesQueueLightMode.iterator();
            while (it.hasNext()) {
                it.next().add(Float.valueOf(f));
            }
            Iterator<ArrayList<Float>> it2 = this.bundlesQueueDarkMode.iterator();
            while (it2.hasNext()) {
                it2.next().add(Float.valueOf(f));
            }
        } else if (DEBUG) {
            Log.w("LightDebounceAlgorithm", "ignore light sensor event because algorithm not started");
        }
    }

    public final void setMode(int i) {
        if (this.mode != i) {
            this.mode = i;
            if (DEBUG) {
                Log.d("LightDebounceAlgorithm", Intrinsics.stringPlus("ambient light mode changed to ", Integer.valueOf(i)));
            }
            AmbientLightModeMonitor.Callback callback = this.callback;
            if (callback != null) {
                LowLightDockManager lowLightDockManager = (LowLightDockManager) ((LowLightDockManager$$ExternalSyntheticLambda0) callback).f$0;
                Objects.requireNonNull(lowLightDockManager);
                boolean z = true;
                if (i != 1) {
                    z = false;
                }
                lowLightDockManager.mIsLowLight = z;
                if (z) {
                    if (LowLightDockManager.DEBUG) {
                        Log.d("LowLightDockManager", "enter low light, start dozing");
                    }
                    lowLightDockManager.mPowerManager.goToSleep(SystemClock.uptimeMillis(), 0, 0);
                    return;
                }
                if (LowLightDockManager.DEBUG) {
                    Log.d("LowLightDockManager", "exit low light, stop dozing");
                }
                lowLightDockManager.mPowerManager.wakeUp(SystemClock.uptimeMillis(), 2, "Exit low light condition");
            }
        }
    }

    @Override // com.google.android.systemui.lowlightclock.AmbientLightModeMonitor.DebounceAlgorithm
    public final void start(LowLightDockManager$$ExternalSyntheticLambda0 lowLightDockManager$$ExternalSyntheticLambda0) {
        boolean z = DEBUG;
        if (z) {
            Log.d("LightDebounceAlgorithm", "start algorithm");
        }
        if (this.callback == null) {
            this.callback = lowLightDockManager$$ExternalSyntheticLambda0;
            this.executor.execute(this.enqueueLightModeBundle);
            this.executor.execute(this.enqueueDarkModeBundle);
        } else if (z) {
            Log.w("LightDebounceAlgorithm", "already started");
        }
    }

    @Override // com.google.android.systemui.lowlightclock.AmbientLightModeMonitor.DebounceAlgorithm
    public final void stop() {
        boolean z = DEBUG;
        if (z) {
            Log.d("LightDebounceAlgorithm", "stop algorithm");
        }
        if (this.callback != null) {
            this.callback = null;
            this.bundlesQueueLightMode.clear();
            this.bundlesQueueDarkMode.clear();
            setMode(2);
        } else if (z) {
            Log.w("LightDebounceAlgorithm", "haven't started");
        }
    }

    /* JADX WARN: Type inference failed for: r1v18, types: [com.google.android.systemui.lowlightclock.LightSensorEventsDebounceAlgorithm$enqueueLightModeBundle$1] */
    /* JADX WARN: Type inference failed for: r1v19, types: [com.google.android.systemui.lowlightclock.LightSensorEventsDebounceAlgorithm$enqueueDarkModeBundle$1] */
    /* JADX WARN: Type inference failed for: r1v20, types: [com.google.android.systemui.lowlightclock.LightSensorEventsDebounceAlgorithm$dequeueLightModeBundle$1] */
    /* JADX WARN: Type inference failed for: r1v21, types: [com.google.android.systemui.lowlightclock.LightSensorEventsDebounceAlgorithm$dequeueDarkModeBundle$1] */
    public LightSensorEventsDebounceAlgorithm(DelayableExecutor delayableExecutor, Resources resources) {
        this.executor = delayableExecutor;
        this.lightModeThreshold = resources.getInteger(2131492884);
        this.darkModeThreshold = resources.getInteger(2131492881);
        this.lightSamplingSpanMillis = resources.getInteger(2131492883);
        this.darkSamplingSpanMillis = resources.getInteger(2131492880);
        this.lightSamplingFrequencyMillis = resources.getInteger(2131492882);
        this.darkSamplingFrequencyMillis = resources.getInteger(2131492879);
    }
}
