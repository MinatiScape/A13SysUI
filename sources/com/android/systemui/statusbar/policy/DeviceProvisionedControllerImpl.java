package com.android.systemui.statusbar.policy;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.util.ArraySet;
import android.util.SparseBooleanArray;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.util.settings.GlobalSettings;
import com.android.systemui.util.settings.SecureSettings;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DeviceProvisionedControllerImpl.kt */
/* loaded from: classes.dex */
public final class DeviceProvisionedControllerImpl implements DeviceProvisionedController, DeviceProvisionedController.DeviceProvisionedListener, Dumpable {
    public final HandlerExecutor backgroundExecutor;
    public final Uri deviceProvisionedUri;
    public final DumpManager dumpManager;
    public final GlobalSettings globalSettings;
    public final Executor mainExecutor;
    public final DeviceProvisionedControllerImpl$observer$1 observer;
    public final SecureSettings secureSettings;
    public final SparseBooleanArray userSetupComplete;
    public final Uri userSetupUri;
    public final UserTracker userTracker;
    public final AtomicBoolean deviceProvisioned = new AtomicBoolean(false);
    public final ArraySet<DeviceProvisionedController.DeviceProvisionedListener> listeners = new ArraySet<>();
    public final Object lock = new Object();
    public final AtomicBoolean initted = new AtomicBoolean(false);
    public final DeviceProvisionedControllerImpl$userChangedCallback$1 userChangedCallback = new UserTracker.Callback() { // from class: com.android.systemui.statusbar.policy.DeviceProvisionedControllerImpl$userChangedCallback$1
        @Override // com.android.systemui.settings.UserTracker.Callback
        public final void onProfilesChanged() {
        }

        @Override // com.android.systemui.settings.UserTracker.Callback
        public final void onUserChanged(int i) {
            DeviceProvisionedControllerImpl.this.updateValues(false, i);
            DeviceProvisionedControllerImpl.this.onUserSwitched();
        }
    };

    public final void updateValues(boolean z, int i) {
        boolean z2;
        boolean z3;
        boolean z4 = true;
        if (z) {
            AtomicBoolean atomicBoolean = this.deviceProvisioned;
            if (this.globalSettings.getInt("device_provisioned", 0) != 0) {
                z3 = true;
            } else {
                z3 = false;
            }
            atomicBoolean.set(z3);
        }
        synchronized (this.lock) {
            try {
                if (i == -1) {
                    int size = this.userSetupComplete.size();
                    int i2 = 0;
                    while (i2 < size) {
                        int i3 = i2 + 1;
                        int keyAt = this.userSetupComplete.keyAt(i2);
                        if (this.secureSettings.getIntForUser("user_setup_complete", 0, keyAt) != 0) {
                            z2 = true;
                        } else {
                            z2 = false;
                        }
                        this.userSetupComplete.put(keyAt, z2);
                        i2 = i3;
                    }
                } else if (i != -2) {
                    if (this.secureSettings.getIntForUser("user_setup_complete", 0, i) == 0) {
                        z4 = false;
                    }
                    this.userSetupComplete.put(i, z4);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(DeviceProvisionedController.DeviceProvisionedListener deviceProvisionedListener) {
        DeviceProvisionedController.DeviceProvisionedListener deviceProvisionedListener2 = deviceProvisionedListener;
        synchronized (this.lock) {
            this.listeners.add(deviceProvisionedListener2);
        }
    }

    public final void dispatchChange(final Function1<? super DeviceProvisionedController.DeviceProvisionedListener, Unit> function1) {
        final ArrayList arrayList;
        synchronized (this.lock) {
            arrayList = new ArrayList(this.listeners);
        }
        this.mainExecutor.execute(new Runnable() { // from class: com.android.systemui.statusbar.policy.DeviceProvisionedControllerImpl$dispatchChange$1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.lang.Runnable
            public final void run() {
                ArrayList<DeviceProvisionedController.DeviceProvisionedListener> arrayList2 = arrayList;
                Function1<DeviceProvisionedController.DeviceProvisionedListener, Unit> function12 = function1;
                for (Object obj : arrayList2) {
                    function12.invoke(obj);
                }
            }
        });
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println(Intrinsics.stringPlus("Device provisioned: ", Boolean.valueOf(this.deviceProvisioned.get())));
        synchronized (this.lock) {
            printWriter.println(Intrinsics.stringPlus("User setup complete: ", this.userSetupComplete));
            printWriter.println(Intrinsics.stringPlus("Listeners: ", this.listeners));
        }
    }

    public final void init() {
        if (this.initted.compareAndSet(false, true)) {
            this.dumpManager.registerDumpable(this);
            updateValues(true, -1);
            this.userTracker.addCallback(this.userChangedCallback, this.backgroundExecutor);
            this.globalSettings.registerContentObserver(this.deviceProvisionedUri, this.observer);
            this.secureSettings.registerContentObserverForUser(this.userSetupUri, this.observer, -1);
        }
    }

    @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController
    public final boolean isCurrentUserSetup() {
        return isUserSetup(this.userTracker.getUserId());
    }

    @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController
    public final boolean isDeviceProvisioned() {
        return this.deviceProvisioned.get();
    }

    @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController
    public final boolean isUserSetup(int i) {
        int indexOfKey;
        synchronized (this.lock) {
            indexOfKey = this.userSetupComplete.indexOfKey(i);
        }
        boolean z = false;
        if (indexOfKey < 0) {
            if (this.secureSettings.getIntForUser("user_setup_complete", 0, i) != 0) {
                z = true;
            }
            synchronized (this.lock) {
                this.userSetupComplete.put(i, z);
            }
        } else {
            synchronized (this.lock) {
                z = this.userSetupComplete.get(i, false);
            }
        }
        return z;
    }

    @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController.DeviceProvisionedListener
    public final void onDeviceProvisionedChanged() {
        dispatchChange(DeviceProvisionedControllerImpl$onDeviceProvisionedChanged$1.INSTANCE);
    }

    @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController.DeviceProvisionedListener
    public final void onUserSetupChanged() {
        dispatchChange(DeviceProvisionedControllerImpl$onUserSetupChanged$1.INSTANCE);
    }

    @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController.DeviceProvisionedListener
    public final void onUserSwitched() {
        dispatchChange(DeviceProvisionedControllerImpl$onUserSwitched$1.INSTANCE);
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(DeviceProvisionedController.DeviceProvisionedListener deviceProvisionedListener) {
        DeviceProvisionedController.DeviceProvisionedListener deviceProvisionedListener2 = deviceProvisionedListener;
        synchronized (this.lock) {
            this.listeners.remove(deviceProvisionedListener2);
        }
    }

    /* JADX WARN: Type inference failed for: r4v6, types: [com.android.systemui.statusbar.policy.DeviceProvisionedControllerImpl$observer$1] */
    /* JADX WARN: Type inference failed for: r4v7, types: [com.android.systemui.statusbar.policy.DeviceProvisionedControllerImpl$userChangedCallback$1] */
    public DeviceProvisionedControllerImpl(SecureSettings secureSettings, GlobalSettings globalSettings, UserTracker userTracker, DumpManager dumpManager, final Handler handler, Executor executor) {
        this.secureSettings = secureSettings;
        this.globalSettings = globalSettings;
        this.userTracker = userTracker;
        this.dumpManager = dumpManager;
        this.mainExecutor = executor;
        this.deviceProvisionedUri = globalSettings.getUriFor("device_provisioned");
        this.userSetupUri = secureSettings.getUriFor("user_setup_complete");
        SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
        this.userSetupComplete = sparseBooleanArray;
        this.backgroundExecutor = new HandlerExecutor(handler);
        this.observer = new ContentObserver(handler) { // from class: com.android.systemui.statusbar.policy.DeviceProvisionedControllerImpl$observer$1
            public final void onChange(boolean z, Collection<Uri> collection, int i, int i2) {
                boolean contains = collection.contains(DeviceProvisionedControllerImpl.this.deviceProvisionedUri);
                if (!collection.contains(DeviceProvisionedControllerImpl.this.userSetupUri)) {
                    i2 = -2;
                }
                DeviceProvisionedControllerImpl.this.updateValues(contains, i2);
                if (contains) {
                    DeviceProvisionedControllerImpl.this.onDeviceProvisionedChanged();
                }
                if (i2 != -2) {
                    DeviceProvisionedControllerImpl.this.onUserSetupChanged();
                }
            }
        };
        sparseBooleanArray.put(userTracker.getUserId(), false);
    }
}
