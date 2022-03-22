package androidx.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleDispatcher;
import androidx.startup.Initializer;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ProcessLifecycleInitializer implements Initializer<LifecycleOwner> {
    @Override // androidx.startup.Initializer
    public final LifecycleOwner create(Context context) {
        if (!LifecycleDispatcher.sInitialized.getAndSet(true)) {
            ((Application) context.getApplicationContext()).registerActivityLifecycleCallbacks(new LifecycleDispatcher.DispatcherActivityCallback());
        }
        final ProcessLifecycleOwner processLifecycleOwner = ProcessLifecycleOwner.sInstance;
        Objects.requireNonNull(processLifecycleOwner);
        processLifecycleOwner.mHandler = new Handler();
        processLifecycleOwner.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        ((Application) context.getApplicationContext()).registerActivityLifecycleCallbacks(new EmptyActivityLifecycleCallbacks() { // from class: androidx.lifecycle.ProcessLifecycleOwner.3
            @Override // androidx.lifecycle.EmptyActivityLifecycleCallbacks, android.app.Application.ActivityLifecycleCallbacks
            public void onActivityCreated(Activity activity, Bundle bundle) {
            }

            @Override // androidx.lifecycle.EmptyActivityLifecycleCallbacks, android.app.Application.ActivityLifecycleCallbacks
            public void onActivityPaused(Activity activity) {
                ProcessLifecycleOwner processLifecycleOwner2 = processLifecycleOwner;
                Objects.requireNonNull(processLifecycleOwner2);
                int i = processLifecycleOwner2.mResumedCounter - 1;
                processLifecycleOwner2.mResumedCounter = i;
                if (i == 0) {
                    processLifecycleOwner2.mHandler.postDelayed(processLifecycleOwner2.mDelayedPauseRunnable, 700L);
                }
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityPreCreated(Activity activity, Bundle bundle) {
                activity.registerActivityLifecycleCallbacks(new EmptyActivityLifecycleCallbacks() { // from class: androidx.lifecycle.ProcessLifecycleOwner.3.1
                    {
                        AnonymousClass3.this = this;
                    }

                    @Override // android.app.Application.ActivityLifecycleCallbacks
                    public void onActivityPostResumed(Activity activity2) {
                        processLifecycleOwner.activityResumed();
                    }

                    @Override // android.app.Application.ActivityLifecycleCallbacks
                    public void onActivityPostStarted(Activity activity2) {
                        ProcessLifecycleOwner processLifecycleOwner2 = processLifecycleOwner;
                        Objects.requireNonNull(processLifecycleOwner2);
                        int i = processLifecycleOwner2.mStartedCounter + 1;
                        processLifecycleOwner2.mStartedCounter = i;
                        if (i == 1 && processLifecycleOwner2.mStopSent) {
                            processLifecycleOwner2.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
                            processLifecycleOwner2.mStopSent = false;
                        }
                    }
                });
            }

            @Override // androidx.lifecycle.EmptyActivityLifecycleCallbacks, android.app.Application.ActivityLifecycleCallbacks
            public void onActivityStopped(Activity activity) {
                ProcessLifecycleOwner processLifecycleOwner2 = processLifecycleOwner;
                Objects.requireNonNull(processLifecycleOwner2);
                int i = processLifecycleOwner2.mStartedCounter - 1;
                processLifecycleOwner2.mStartedCounter = i;
                if (i == 0 && processLifecycleOwner2.mPauseSent) {
                    processLifecycleOwner2.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
                    processLifecycleOwner2.mStopSent = true;
                }
            }
        });
        return processLifecycleOwner;
    }

    @Override // androidx.startup.Initializer
    public final List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.emptyList();
    }
}
