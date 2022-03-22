package androidx.lifecycle;

import android.os.Handler;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ReportFragment;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ProcessLifecycleOwner implements LifecycleOwner {
    public static final long TIMEOUT_MS = 700;
    public static final ProcessLifecycleOwner sInstance = new ProcessLifecycleOwner();
    public Handler mHandler;
    public int mStartedCounter = 0;
    public int mResumedCounter = 0;
    public boolean mPauseSent = true;
    public boolean mStopSent = true;
    public final LifecycleRegistry mRegistry = new LifecycleRegistry(this, true);
    public AnonymousClass1 mDelayedPauseRunnable = new Runnable() { // from class: androidx.lifecycle.ProcessLifecycleOwner.1
        @Override // java.lang.Runnable
        public final void run() {
            ProcessLifecycleOwner processLifecycleOwner = ProcessLifecycleOwner.this;
            Objects.requireNonNull(processLifecycleOwner);
            if (processLifecycleOwner.mResumedCounter == 0) {
                processLifecycleOwner.mPauseSent = true;
                processLifecycleOwner.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
            }
            ProcessLifecycleOwner processLifecycleOwner2 = ProcessLifecycleOwner.this;
            Objects.requireNonNull(processLifecycleOwner2);
            if (processLifecycleOwner2.mStartedCounter == 0 && processLifecycleOwner2.mPauseSent) {
                processLifecycleOwner2.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
                processLifecycleOwner2.mStopSent = true;
            }
        }
    };
    public AnonymousClass2 mInitializationListener = new AnonymousClass2();

    /* renamed from: androidx.lifecycle.ProcessLifecycleOwner$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements ReportFragment.ActivityInitializationListener {
        public AnonymousClass2() {
        }
    }

    public final void activityResumed() {
        int i = this.mResumedCounter + 1;
        this.mResumedCounter = i;
        if (i != 1) {
            return;
        }
        if (this.mPauseSent) {
            this.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
            this.mPauseSent = false;
            return;
        }
        this.mHandler.removeCallbacks(this.mDelayedPauseRunnable);
    }

    @Override // androidx.lifecycle.LifecycleOwner
    public final Lifecycle getLifecycle() {
        return this.mRegistry;
    }
}
