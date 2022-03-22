package com.android.systemui.util.sensors;

import android.content.Context;
import android.hardware.HardwareBuffer;
import android.hardware.Sensor;
import android.hardware.SensorAdditionalInfo;
import android.hardware.SensorDirectChannel;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEventListener;
import android.os.Handler;
import android.os.MemoryFile;
import android.util.Log;
import com.android.internal.util.Preconditions;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.plugins.SensorManagerPlugin;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda20;
import com.android.systemui.util.concurrency.ThreadFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class AsyncSensorManager extends SensorManager implements PluginListener<SensorManagerPlugin> {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final Executor mExecutor;
    public final SensorManager mInner;
    public final ArrayList mPlugins = new ArrayList();
    public final List<Sensor> mSensorCache;

    public final int configureDirectChannelImpl(SensorDirectChannel sensorDirectChannel, Sensor sensor, int i) {
        throw new UnsupportedOperationException("not implemented");
    }

    public final SensorDirectChannel createDirectChannelImpl(MemoryFile memoryFile, HardwareBuffer hardwareBuffer) {
        throw new UnsupportedOperationException("not implemented");
    }

    public final void destroyDirectChannelImpl(SensorDirectChannel sensorDirectChannel) {
        throw new UnsupportedOperationException("not implemented");
    }

    public final boolean flushImpl(SensorEventListener sensorEventListener) {
        return this.mInner.flush(sensorEventListener);
    }

    public final List<Sensor> getFullDynamicSensorList() {
        return this.mInner.getSensorList(-1);
    }

    public final boolean initDataInjectionImpl(boolean z) {
        throw new UnsupportedOperationException("not implemented");
    }

    public final boolean injectSensorDataImpl(Sensor sensor, float[] fArr, int i, long j) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override // com.android.systemui.plugins.PluginListener
    public final void onPluginConnected(SensorManagerPlugin sensorManagerPlugin, Context context) {
        this.mPlugins.add(sensorManagerPlugin);
    }

    @Override // com.android.systemui.plugins.PluginListener
    public final void onPluginDisconnected(SensorManagerPlugin sensorManagerPlugin) {
        this.mPlugins.remove(sensorManagerPlugin);
    }

    public final void registerDynamicSensorCallbackImpl(final SensorManager.DynamicSensorCallback dynamicSensorCallback, final Handler handler) {
        this.mExecutor.execute(new Runnable() { // from class: com.android.systemui.util.sensors.AsyncSensorManager$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                AsyncSensorManager asyncSensorManager = AsyncSensorManager.this;
                SensorManager.DynamicSensorCallback dynamicSensorCallback2 = dynamicSensorCallback;
                Handler handler2 = handler;
                Objects.requireNonNull(asyncSensorManager);
                asyncSensorManager.mInner.registerDynamicSensorCallback(dynamicSensorCallback2, handler2);
            }
        });
    }

    public final boolean registerListenerImpl(final SensorEventListener sensorEventListener, final Sensor sensor, final int i, final Handler handler, final int i2, int i3) {
        this.mExecutor.execute(new Runnable() { // from class: com.android.systemui.util.sensors.AsyncSensorManager$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                AsyncSensorManager asyncSensorManager = AsyncSensorManager.this;
                SensorEventListener sensorEventListener2 = sensorEventListener;
                Sensor sensor2 = sensor;
                int i4 = i;
                int i5 = i2;
                Handler handler2 = handler;
                Objects.requireNonNull(asyncSensorManager);
                if (!asyncSensorManager.mInner.registerListener(sensorEventListener2, sensor2, i4, i5, handler2)) {
                    Log.e("AsyncSensorManager", "Registering " + sensorEventListener2 + " for " + sensor2 + " failed.");
                }
            }
        });
        return true;
    }

    public final boolean requestTriggerSensorImpl(final TriggerEventListener triggerEventListener, final Sensor sensor) {
        if (triggerEventListener == null) {
            throw new IllegalArgumentException("listener cannot be null");
        } else if (sensor != null) {
            this.mExecutor.execute(new Runnable() { // from class: com.android.systemui.util.sensors.AsyncSensorManager$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    AsyncSensorManager asyncSensorManager = AsyncSensorManager.this;
                    TriggerEventListener triggerEventListener2 = triggerEventListener;
                    Sensor sensor2 = sensor;
                    Objects.requireNonNull(asyncSensorManager);
                    if (!asyncSensorManager.mInner.requestTriggerSensor(triggerEventListener2, sensor2)) {
                        Log.e("AsyncSensorManager", "Requesting " + triggerEventListener2 + " for " + sensor2 + " failed.");
                    }
                }
            });
            return true;
        } else {
            throw new IllegalArgumentException("sensor cannot be null");
        }
    }

    public final boolean setOperationParameterImpl(SensorAdditionalInfo sensorAdditionalInfo) {
        this.mExecutor.execute(new StatusBar$$ExternalSyntheticLambda20(this, sensorAdditionalInfo, 3));
        return true;
    }

    public final void unregisterDynamicSensorCallbackImpl(SensorManager.DynamicSensorCallback dynamicSensorCallback) {
        this.mExecutor.execute(new AsyncSensorManager$$ExternalSyntheticLambda0(this, dynamicSensorCallback, 0));
    }

    public final void unregisterListenerImpl(final SensorEventListener sensorEventListener, final Sensor sensor) {
        this.mExecutor.execute(new Runnable() { // from class: com.android.systemui.util.sensors.AsyncSensorManager$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                AsyncSensorManager asyncSensorManager = AsyncSensorManager.this;
                Sensor sensor2 = sensor;
                SensorEventListener sensorEventListener2 = sensorEventListener;
                Objects.requireNonNull(asyncSensorManager);
                if (sensor2 == null) {
                    asyncSensorManager.mInner.unregisterListener(sensorEventListener2);
                } else {
                    asyncSensorManager.mInner.unregisterListener(sensorEventListener2, sensor2);
                }
            }
        });
    }

    public AsyncSensorManager(SensorManager sensorManager, ThreadFactory threadFactory, PluginManager pluginManager) {
        this.mInner = sensorManager;
        this.mExecutor = threadFactory.buildExecutorOnNewThread("async_sensor");
        this.mSensorCache = sensorManager.getSensorList(-1);
        if (pluginManager != null) {
            pluginManager.addPluginListener((PluginListener) this, SensorManagerPlugin.class, true);
        }
    }

    public final boolean cancelTriggerSensorImpl(final TriggerEventListener triggerEventListener, final Sensor sensor, boolean z) {
        Preconditions.checkArgument(z);
        this.mExecutor.execute(new Runnable() { // from class: com.android.systemui.util.sensors.AsyncSensorManager$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                AsyncSensorManager asyncSensorManager = AsyncSensorManager.this;
                TriggerEventListener triggerEventListener2 = triggerEventListener;
                Sensor sensor2 = sensor;
                Objects.requireNonNull(asyncSensorManager);
                if (!asyncSensorManager.mInner.cancelTriggerSensor(triggerEventListener2, sensor2)) {
                    Log.e("AsyncSensorManager", "Canceling " + triggerEventListener2 + " for " + sensor2 + " failed.");
                }
            }
        });
        return true;
    }

    public final List<Sensor> getFullSensorList() {
        return this.mSensorCache;
    }
}
