package com.google.android.setupcompat.internal;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import com.android.systemui.plugins.FalsingManager;
import com.google.android.setupcompat.ISetupCompatService;
import com.google.android.setupcompat.util.Logger;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: classes.dex */
public final class SetupCompatServiceProvider {
    @SuppressLint({"StaticFieldLeak"})
    public static volatile SetupCompatServiceProvider instance;
    public final Context context;
    public static final Logger LOG = new Logger("SetupCompatServiceProvider");
    public static final Intent COMPAT_SERVICE_INTENT = new Intent().setPackage("com.google.android.setupwizard").setAction("com.google.android.setupcompat.SetupCompatService.BIND");
    public static boolean disableLooperCheckForTesting = false;
    public final ServiceConnection serviceConnection = new ServiceConnection() { // from class: com.google.android.setupcompat.internal.SetupCompatServiceProvider.1
        @Override // android.content.ServiceConnection
        public final void onBindingDied(ComponentName componentName) {
            SetupCompatServiceProvider.this.swapServiceContextAndNotify(new ServiceContext(State.REBIND_REQUIRED, null));
        }

        @Override // android.content.ServiceConnection
        public final void onNullBinding(ComponentName componentName) {
            SetupCompatServiceProvider.this.swapServiceContextAndNotify(new ServiceContext(State.SERVICE_NOT_USABLE, null));
        }

        @Override // android.content.ServiceConnection
        public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ISetupCompatService iSetupCompatService;
            State state = State.CONNECTED;
            if (iBinder == null) {
                state = State.DISCONNECTED;
                SetupCompatServiceProvider.LOG.w("Binder is null when onServiceConnected was called!");
            }
            SetupCompatServiceProvider setupCompatServiceProvider = SetupCompatServiceProvider.this;
            int i = ISetupCompatService.Stub.$r8$clinit;
            if (iBinder == null) {
                iSetupCompatService = null;
            } else {
                IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.setupcompat.ISetupCompatService");
                if (queryLocalInterface == null || !(queryLocalInterface instanceof ISetupCompatService)) {
                    iSetupCompatService = new ISetupCompatService.Stub.Proxy(iBinder);
                } else {
                    iSetupCompatService = (ISetupCompatService) queryLocalInterface;
                }
            }
            setupCompatServiceProvider.swapServiceContextAndNotify(new ServiceContext(state, iSetupCompatService));
        }

        @Override // android.content.ServiceConnection
        public final void onServiceDisconnected(ComponentName componentName) {
            SetupCompatServiceProvider.this.swapServiceContextAndNotify(new ServiceContext(State.DISCONNECTED, null));
        }
    };
    public volatile ServiceContext serviceContext = new ServiceContext(State.NOT_STARTED, null);
    public final AtomicReference<CountDownLatch> connectedConditionRef = new AtomicReference<>();

    /* loaded from: classes.dex */
    public enum State {
        NOT_STARTED,
        BIND_FAILED,
        BINDING,
        CONNECTED,
        DISCONNECTED,
        SERVICE_NOT_USABLE,
        REBIND_REQUIRED
    }

    public final ISetupCompatService waitForConnection(long j, TimeUnit timeUnit) throws TimeoutException, InterruptedException {
        ServiceContext serviceContext;
        CountDownLatch countDownLatch;
        ServiceContext serviceContext2;
        synchronized (this) {
            serviceContext = this.serviceContext;
        }
        if (serviceContext.state == State.CONNECTED) {
            return serviceContext.compatService;
        }
        do {
            countDownLatch = this.connectedConditionRef.get();
            if (countDownLatch != null) {
                break;
            }
            countDownLatch = createCountDownLatch();
        } while (!this.connectedConditionRef.compareAndSet(null, countDownLatch));
        Logger logger = LOG;
        logger.atInfo("Waiting for service to get connected");
        if (countDownLatch.await(j, timeUnit)) {
            synchronized (this) {
                serviceContext2 = this.serviceContext;
            }
            logger.atInfo(String.format("Finished waiting for service to get connected. Current state = %s", serviceContext2.state));
            return serviceContext2.compatService;
        }
        requestServiceBind();
        throw new TimeoutException(String.format("Failed to acquire connection after [%s %s]", Long.valueOf(j), timeUnit));
    }

    /* loaded from: classes.dex */
    public static final class ServiceContext {
        public final ISetupCompatService compatService;
        public final State state;

        public ServiceContext(State state, ISetupCompatService iSetupCompatService) {
            this.state = state;
            this.compatService = iSetupCompatService;
            if (state == State.CONNECTED) {
                Objects.requireNonNull(iSetupCompatService, "CompatService cannot be null when state is connected");
            }
        }
    }

    public static SetupCompatServiceProvider getInstance(Context context) {
        Objects.requireNonNull(context, "Context object cannot be null.");
        SetupCompatServiceProvider setupCompatServiceProvider = instance;
        if (setupCompatServiceProvider == null) {
            synchronized (SetupCompatServiceProvider.class) {
                setupCompatServiceProvider = instance;
                if (setupCompatServiceProvider == null) {
                    setupCompatServiceProvider = new SetupCompatServiceProvider(context.getApplicationContext());
                    instance = setupCompatServiceProvider;
                    instance.requestServiceBind();
                }
            }
        }
        return setupCompatServiceProvider;
    }

    public CountDownLatch createCountDownLatch() {
        return new CountDownLatch(1);
    }

    public State getCurrentState() {
        return this.serviceContext.state;
    }

    public ISetupCompatService getService(long j, TimeUnit timeUnit) throws TimeoutException, InterruptedException {
        boolean z;
        ServiceContext serviceContext;
        if (disableLooperCheckForTesting || Looper.getMainLooper() != Looper.myLooper()) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            synchronized (this) {
                serviceContext = this.serviceContext;
            }
            switch (serviceContext.state.ordinal()) {
                case 0:
                    throw new IllegalStateException("NOT_STARTED state only possible before instance is created.");
                case 1:
                case 5:
                    return null;
                case 2:
                case 4:
                    return waitForConnection(j, timeUnit);
                case 3:
                    return serviceContext.compatService;
                case FalsingManager.VERSION /* 6 */:
                    requestServiceBind();
                    return waitForConnection(j, timeUnit);
                default:
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Unknown state = ");
                    m.append(serviceContext.state);
                    throw new IllegalStateException(m.toString());
            }
        } else {
            throw new IllegalStateException("getService blocks and should not be called from the main thread.");
        }
    }

    public final synchronized void requestServiceBind() {
        ServiceContext serviceContext;
        boolean z;
        State state = State.CONNECTED;
        synchronized (this) {
            synchronized (this) {
                serviceContext = this.serviceContext;
            }
        }
        State state2 = serviceContext.state;
        if (state2 == state) {
            LOG.atInfo("Refusing to rebind since current state is already connected");
            return;
        }
        if (state2 != State.NOT_STARTED) {
            LOG.atInfo("Unbinding existing service connection.");
            this.context.unbindService(this.serviceConnection);
        }
        try {
            z = this.context.bindService(COMPAT_SERVICE_INTENT, this.serviceConnection, 1);
        } catch (SecurityException e) {
            Logger logger = LOG;
            logger.e("Unable to bind to compat service. " + e);
            z = false;
        }
        if (!z) {
            swapServiceContextAndNotify(new ServiceContext(State.BIND_FAILED, null));
            LOG.e("Context#bindService did not succeed.");
        } else if (getCurrentState() != state) {
            swapServiceContextAndNotify(new ServiceContext(State.BINDING, null));
            LOG.atInfo("Context#bindService went through, now waiting for service connection");
        }
    }

    public final void swapServiceContextAndNotify(ServiceContext serviceContext) {
        LOG.atInfo(String.format("State changed: %s -> %s", this.serviceContext.state, serviceContext.state));
        this.serviceContext = serviceContext;
        CountDownLatch andSet = this.connectedConditionRef.getAndSet(null);
        if (andSet != null) {
            andSet.countDown();
        }
    }

    public SetupCompatServiceProvider(Context context) {
        this.context = context.getApplicationContext();
    }

    public static void setInstanceForTesting(SetupCompatServiceProvider setupCompatServiceProvider) {
        instance = setupCompatServiceProvider;
    }
}
