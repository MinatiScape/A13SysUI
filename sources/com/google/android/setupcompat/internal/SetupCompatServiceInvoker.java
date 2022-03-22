package com.google.android.setupcompat.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.setupcompat.ISetupCompatService;
import com.google.android.setupcompat.util.Logger;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/* loaded from: classes.dex */
public final class SetupCompatServiceInvoker {
    public static final Logger LOG = new Logger("SetupCompatServiceInvoker");
    public static final long MAX_WAIT_TIME_FOR_CONNECTION_MS = TimeUnit.SECONDS.toMillis(10);
    @SuppressLint({"StaticFieldLeak"})
    public static SetupCompatServiceInvoker instance;
    public final Context context;
    public final ExecutorService loggingExecutor;
    public final long waitTimeInMillisForServiceConnection;

    public static synchronized SetupCompatServiceInvoker get(Context context) {
        SetupCompatServiceInvoker setupCompatServiceInvoker;
        synchronized (SetupCompatServiceInvoker.class) {
            if (instance == null) {
                instance = new SetupCompatServiceInvoker(context.getApplicationContext());
            }
            setupCompatServiceInvoker = instance;
        }
        return setupCompatServiceInvoker;
    }

    @SuppressLint({"DefaultLocale"})
    public final void logMetricEvent(final int i, final Bundle bundle) {
        try {
            this.loggingExecutor.execute(new Runnable() { // from class: com.google.android.setupcompat.internal.SetupCompatServiceInvoker$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    SetupCompatServiceInvoker setupCompatServiceInvoker = SetupCompatServiceInvoker.this;
                    int i2 = i;
                    Bundle bundle2 = bundle;
                    Objects.requireNonNull(setupCompatServiceInvoker);
                    try {
                        ISetupCompatService service = SetupCompatServiceProvider.getInstance(setupCompatServiceInvoker.context).getService(setupCompatServiceInvoker.waitTimeInMillisForServiceConnection, TimeUnit.MILLISECONDS);
                        if (service != null) {
                            Bundle bundle3 = Bundle.EMPTY;
                            service.logMetric(i2, bundle2);
                        } else {
                            SetupCompatServiceInvoker.LOG.w("logMetric failed since service reference is null. Are the permissions valid?");
                        }
                    } catch (RemoteException | IllegalStateException | InterruptedException | TimeoutException e) {
                        SetupCompatServiceInvoker.LOG.e(String.format("Exception occurred while trying to log metric = [%s]", bundle2), e);
                    }
                }
            });
        } catch (RejectedExecutionException e) {
            LOG.e(String.format("Metric of type %d dropped since queue is full.", Integer.valueOf(i)), e);
        }
    }

    public SetupCompatServiceInvoker(Context context) {
        this.context = context;
        ExecutorProvider<ExecutorService> executorProvider = ExecutorProvider.setupCompatServiceInvoker;
        Objects.requireNonNull(executorProvider);
        ExecutorService executorService = executorProvider.injectedExecutor;
        this.loggingExecutor = executorService == null ? executorProvider.executor : executorService;
        this.waitTimeInMillisForServiceConnection = MAX_WAIT_TIME_FOR_CONNECTION_MS;
    }

    public static void setInstanceForTesting(SetupCompatServiceInvoker setupCompatServiceInvoker) {
        instance = setupCompatServiceInvoker;
    }
}
