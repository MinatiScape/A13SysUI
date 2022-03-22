package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.controls.IControlsActionCallback;
import android.service.controls.IControlsProvider;
import android.service.controls.IControlsSubscriber;
import android.service.controls.IControlsSubscription;
import android.service.controls.actions.ControlAction;
import android.service.controls.actions.ControlActionWrapper;
import android.util.ArraySet;
import android.util.Log;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline2;
import com.android.internal.annotations.GuardedBy;
import com.android.systemui.controls.controller.ControlsBindingControllerImpl;
import com.android.systemui.controls.controller.ControlsProviderLifecycleManager;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsProviderLifecycleManager.kt */
/* loaded from: classes.dex */
public final class ControlsProviderLifecycleManager implements IBinder.DeathRecipient {
    public final IControlsActionCallback.Stub actionCallbackService;
    public int bindTryCount;
    public final ComponentName componentName;
    public final Context context;
    public final DelayableExecutor executor;
    public final Intent intent;
    public Runnable onLoadCanceller;
    public boolean requiresBound;
    public final Binder token;
    public final UserHandle user;
    public ServiceWrapper wrapper;
    @GuardedBy({"queuedServiceMethods"})
    public final ArraySet queuedServiceMethods = new ArraySet();
    public final String TAG = "ControlsProviderLifecycleManager";
    public final ControlsProviderLifecycleManager$serviceConnection$1 serviceConnection = new ServiceConnection() { // from class: com.android.systemui.controls.controller.ControlsProviderLifecycleManager$serviceConnection$1
        @Override // android.content.ServiceConnection
        public final void onNullBinding(ComponentName componentName) {
            Log.d(ControlsProviderLifecycleManager.this.TAG, Intrinsics.stringPlus("onNullBinding ", componentName));
            ControlsProviderLifecycleManager controlsProviderLifecycleManager = ControlsProviderLifecycleManager.this;
            controlsProviderLifecycleManager.wrapper = null;
            controlsProviderLifecycleManager.context.unbindService(this);
        }

        @Override // android.content.ServiceConnection
        public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ArraySet arraySet;
            Log.d(ControlsProviderLifecycleManager.this.TAG, Intrinsics.stringPlus("onServiceConnected ", componentName));
            ControlsProviderLifecycleManager controlsProviderLifecycleManager = ControlsProviderLifecycleManager.this;
            controlsProviderLifecycleManager.bindTryCount = 0;
            controlsProviderLifecycleManager.wrapper = new ServiceWrapper(IControlsProvider.Stub.asInterface(iBinder));
            try {
                iBinder.linkToDeath(ControlsProviderLifecycleManager.this, 0);
            } catch (RemoteException unused) {
            }
            ControlsProviderLifecycleManager controlsProviderLifecycleManager2 = ControlsProviderLifecycleManager.this;
            Objects.requireNonNull(controlsProviderLifecycleManager2);
            synchronized (controlsProviderLifecycleManager2.queuedServiceMethods) {
                arraySet = new ArraySet((Collection) controlsProviderLifecycleManager2.queuedServiceMethods);
                controlsProviderLifecycleManager2.queuedServiceMethods.clear();
            }
            Iterator it = arraySet.iterator();
            while (it.hasNext()) {
                ControlsProviderLifecycleManager.ServiceMethod serviceMethod = (ControlsProviderLifecycleManager.ServiceMethod) it.next();
                Objects.requireNonNull(serviceMethod);
                if (!serviceMethod.callWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
                    ControlsProviderLifecycleManager controlsProviderLifecycleManager3 = ControlsProviderLifecycleManager.this;
                    Objects.requireNonNull(controlsProviderLifecycleManager3);
                    synchronized (controlsProviderLifecycleManager3.queuedServiceMethods) {
                        controlsProviderLifecycleManager3.queuedServiceMethods.add(serviceMethod);
                    }
                    ControlsProviderLifecycleManager.this.binderDied();
                }
            }
        }

        @Override // android.content.ServiceConnection
        public final void onServiceDisconnected(ComponentName componentName) {
            Log.d(ControlsProviderLifecycleManager.this.TAG, Intrinsics.stringPlus("onServiceDisconnected ", componentName));
            ControlsProviderLifecycleManager controlsProviderLifecycleManager = ControlsProviderLifecycleManager.this;
            controlsProviderLifecycleManager.wrapper = null;
            controlsProviderLifecycleManager.executor.execute(new ControlsProviderLifecycleManager$bindService$1(controlsProviderLifecycleManager, false));
        }
    };

    /* compiled from: ControlsProviderLifecycleManager.kt */
    /* loaded from: classes.dex */
    public final class Action extends ServiceMethod {
        public final ControlAction action;
        public final String id;

        public Action(String str, ControlAction controlAction) {
            super();
            this.id = str;
            this.action = controlAction;
        }

        @Override // com.android.systemui.controls.controller.ControlsProviderLifecycleManager.ServiceMethod
        public final boolean callWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
            String str = ControlsProviderLifecycleManager.this.TAG;
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("onAction ");
            ControlsProviderLifecycleManager controlsProviderLifecycleManager = ControlsProviderLifecycleManager.this;
            Objects.requireNonNull(controlsProviderLifecycleManager);
            m.append(controlsProviderLifecycleManager.componentName);
            m.append(" - ");
            ExifInterface$$ExternalSyntheticOutline2.m(m, this.id, str);
            ControlsProviderLifecycleManager controlsProviderLifecycleManager2 = ControlsProviderLifecycleManager.this;
            ServiceWrapper serviceWrapper = controlsProviderLifecycleManager2.wrapper;
            if (serviceWrapper == null) {
                return false;
            }
            String str2 = this.id;
            ControlAction controlAction = this.action;
            try {
                serviceWrapper.service.action(str2, new ControlActionWrapper(controlAction), controlsProviderLifecycleManager2.actionCallbackService);
                return true;
            } catch (Exception e) {
                Log.e("ServiceWrapper", "Caught exception from ControlsProviderService", e);
                return false;
            }
        }
    }

    /* compiled from: ControlsProviderLifecycleManager.kt */
    /* loaded from: classes.dex */
    public final class Load extends ServiceMethod {
        public final IControlsSubscriber.Stub subscriber;

        public Load(ControlsBindingControllerImpl.LoadSubscriber loadSubscriber) {
            super();
            this.subscriber = loadSubscriber;
        }

        @Override // com.android.systemui.controls.controller.ControlsProviderLifecycleManager.ServiceMethod
        public final boolean callWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
            ControlsProviderLifecycleManager controlsProviderLifecycleManager = ControlsProviderLifecycleManager.this;
            String str = controlsProviderLifecycleManager.TAG;
            Objects.requireNonNull(controlsProviderLifecycleManager);
            Log.d(str, Intrinsics.stringPlus("load ", controlsProviderLifecycleManager.componentName));
            ServiceWrapper serviceWrapper = ControlsProviderLifecycleManager.this.wrapper;
            if (serviceWrapper == null) {
                return false;
            }
            try {
                serviceWrapper.service.load(this.subscriber);
                return true;
            } catch (Exception e) {
                Log.e("ServiceWrapper", "Caught exception from ControlsProviderService", e);
                return false;
            }
        }
    }

    /* compiled from: ControlsProviderLifecycleManager.kt */
    /* loaded from: classes.dex */
    public abstract class ServiceMethod {
        public abstract boolean callWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core();

        public ServiceMethod() {
        }
    }

    /* compiled from: ControlsProviderLifecycleManager.kt */
    /* loaded from: classes.dex */
    public final class Subscribe extends ServiceMethod {
        public final List<String> list;
        public final IControlsSubscriber subscriber;

        public Subscribe(ArrayList arrayList, StatefulControlSubscriber statefulControlSubscriber) {
            super();
            this.list = arrayList;
            this.subscriber = statefulControlSubscriber;
        }

        @Override // com.android.systemui.controls.controller.ControlsProviderLifecycleManager.ServiceMethod
        public final boolean callWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
            String str = ControlsProviderLifecycleManager.this.TAG;
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("subscribe ");
            ControlsProviderLifecycleManager controlsProviderLifecycleManager = ControlsProviderLifecycleManager.this;
            Objects.requireNonNull(controlsProviderLifecycleManager);
            m.append(controlsProviderLifecycleManager.componentName);
            m.append(" - ");
            m.append(this.list);
            Log.d(str, m.toString());
            ServiceWrapper serviceWrapper = ControlsProviderLifecycleManager.this.wrapper;
            if (serviceWrapper == null) {
                return false;
            }
            try {
                serviceWrapper.service.subscribe(this.list, this.subscriber);
                return true;
            } catch (Exception e) {
                Log.e("ServiceWrapper", "Caught exception from ControlsProviderService", e);
                return false;
            }
        }
    }

    /* compiled from: ControlsProviderLifecycleManager.kt */
    /* loaded from: classes.dex */
    public final class Suggest extends ServiceMethod {
        public final IControlsSubscriber.Stub subscriber;

        public Suggest(ControlsBindingControllerImpl.LoadSubscriber loadSubscriber) {
            super();
            this.subscriber = loadSubscriber;
        }

        @Override // com.android.systemui.controls.controller.ControlsProviderLifecycleManager.ServiceMethod
        public final boolean callWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
            ControlsProviderLifecycleManager controlsProviderLifecycleManager = ControlsProviderLifecycleManager.this;
            String str = controlsProviderLifecycleManager.TAG;
            Objects.requireNonNull(controlsProviderLifecycleManager);
            Log.d(str, Intrinsics.stringPlus("suggest ", controlsProviderLifecycleManager.componentName));
            ServiceWrapper serviceWrapper = ControlsProviderLifecycleManager.this.wrapper;
            if (serviceWrapper == null) {
                return false;
            }
            try {
                serviceWrapper.service.loadSuggested(this.subscriber);
                return true;
            } catch (Exception e) {
                Log.e("ServiceWrapper", "Caught exception from ControlsProviderService", e);
                return false;
            }
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public final void binderDied() {
        if (this.wrapper != null) {
            this.wrapper = null;
            if (this.requiresBound) {
                Log.d(this.TAG, "binderDied");
            }
        }
    }

    public final void cancelSubscription(IControlsSubscription iControlsSubscription) {
        Log.d(this.TAG, Intrinsics.stringPlus("cancelSubscription: ", iControlsSubscription));
        if (this.wrapper != null) {
            try {
                iControlsSubscription.cancel();
            } catch (Exception e) {
                Log.e("ServiceWrapper", "Caught exception from ControlsProviderService", e);
            }
        }
    }

    public final void invokeOrQueue(ServiceMethod serviceMethod) {
        Unit unit;
        if (this.wrapper == null) {
            unit = null;
        } else {
            if (!serviceMethod.callWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
                ControlsProviderLifecycleManager controlsProviderLifecycleManager = ControlsProviderLifecycleManager.this;
                Objects.requireNonNull(controlsProviderLifecycleManager);
                synchronized (controlsProviderLifecycleManager.queuedServiceMethods) {
                    controlsProviderLifecycleManager.queuedServiceMethods.add(serviceMethod);
                }
                ControlsProviderLifecycleManager.this.binderDied();
            }
            unit = Unit.INSTANCE;
        }
        if (unit == null) {
            synchronized (this.queuedServiceMethods) {
                this.queuedServiceMethods.add(serviceMethod);
            }
            this.executor.execute(new ControlsProviderLifecycleManager$bindService$1(this, true));
        }
    }

    public final void startSubscription(IControlsSubscription iControlsSubscription, long j) {
        Log.d(this.TAG, Intrinsics.stringPlus("startSubscription: ", iControlsSubscription));
        if (this.wrapper != null) {
            try {
                iControlsSubscription.request(j);
            } catch (Exception e) {
                Log.e("ServiceWrapper", "Caught exception from ControlsProviderService", e);
            }
        }
    }

    public final String toString() {
        return "ControlsProviderLifecycleManager(" + Intrinsics.stringPlus("component=", this.componentName) + Intrinsics.stringPlus(", user=", this.user) + ")";
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [com.android.systemui.controls.controller.ControlsProviderLifecycleManager$serviceConnection$1] */
    public ControlsProviderLifecycleManager(Context context, DelayableExecutor delayableExecutor, ControlsBindingControllerImpl$actionCallbackService$1 controlsBindingControllerImpl$actionCallbackService$1, UserHandle userHandle, ComponentName componentName) {
        this.context = context;
        this.executor = delayableExecutor;
        this.actionCallbackService = controlsBindingControllerImpl$actionCallbackService$1;
        this.user = userHandle;
        this.componentName = componentName;
        Binder binder = new Binder();
        this.token = binder;
        Intent intent = new Intent();
        intent.setComponent(componentName);
        Bundle bundle = new Bundle();
        bundle.putBinder("CALLBACK_TOKEN", binder);
        intent.putExtra("CALLBACK_BUNDLE", bundle);
        this.intent = intent;
    }
}
