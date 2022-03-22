package com.android.systemui.util.condition;

import android.util.Log;
import androidx.core.view.ViewCompat$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.policy.CallbackController;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public abstract class Condition implements CallbackController<Callback> {
    public final String mTag = getClass().getSimpleName();
    public final ArrayList<WeakReference<Callback>> mCallbacks = new ArrayList<>();
    public boolean mIsConditionMet = false;
    public boolean mStarted = false;

    /* loaded from: classes.dex */
    public interface Callback {
        void onConditionChanged();
    }

    public boolean isOverridingCondition() {
        return false;
    }

    public abstract void start();

    public abstract void stop();

    public final void addCallback(Callback callback) {
        if (Log.isLoggable(this.mTag, 3)) {
            Log.d(this.mTag, "adding callback");
        }
        this.mCallbacks.add(new WeakReference<>(callback));
        if (this.mStarted) {
            callback.onConditionChanged();
            return;
        }
        start();
        this.mStarted = true;
    }

    public final void removeCallback(Callback callback) {
        if (Log.isLoggable(this.mTag, 3)) {
            Log.d(this.mTag, "removing callback");
        }
        Iterator<WeakReference<Callback>> it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            Callback callback2 = it.next().get();
            if (callback2 == null || callback2 == callback) {
                it.remove();
            }
        }
        if (this.mCallbacks.isEmpty() && this.mStarted) {
            stop();
            this.mStarted = false;
        }
    }

    public final void updateCondition(boolean z) {
        if (this.mIsConditionMet != z) {
            if (Log.isLoggable(this.mTag, 3)) {
                ViewCompat$$ExternalSyntheticLambda0.m("updating condition to ", z, this.mTag);
            }
            this.mIsConditionMet = z;
            Iterator<WeakReference<Callback>> it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                Callback callback = it.next().get();
                if (callback == null) {
                    it.remove();
                } else {
                    callback.onConditionChanged();
                }
            }
        }
    }
}
