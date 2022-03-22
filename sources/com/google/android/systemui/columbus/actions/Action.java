package com.google.android.systemui.columbus.actions;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.google.android.systemui.columbus.actions.Action;
import com.google.android.systemui.columbus.feedback.FeedbackEffect;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import java.util.LinkedHashSet;
import java.util.Set;
/* compiled from: Action.kt */
/* loaded from: classes.dex */
public abstract class Action {
    public final Context context;
    public final Set<FeedbackEffect> feedbackEffects;
    public boolean isAvailable = true;
    public final LinkedHashSet listeners = new LinkedHashSet();
    public final Handler handler = new Handler(Looper.getMainLooper());

    /* compiled from: Action.kt */
    /* loaded from: classes.dex */
    public interface Listener {
        void onActionAvailabilityChanged(Action action);
    }

    public abstract String getTag$vendor__unbundled_google__packages__SystemUIGoogle__android_common__sysuig();

    public abstract void onTrigger(GestureSensor.DetectionProperties detectionProperties);

    public final void setAvailable(boolean z) {
        if (this.isAvailable != z) {
            this.isAvailable = z;
            for (final Listener listener : this.listeners) {
                this.handler.post(new Runnable() { // from class: com.google.android.systemui.columbus.actions.Action$setAvailable$1$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        Action.Listener.this.onActionAvailabilityChanged(this);
                    }
                });
            }
            if (!this.isAvailable) {
                this.handler.post(new Runnable() { // from class: com.google.android.systemui.columbus.actions.Action$setAvailable$2
                    @Override // java.lang.Runnable
                    public final void run() {
                        Action.this.updateFeedbackEffects(0, null);
                    }
                });
            }
        }
    }

    public void updateFeedbackEffects(int i, GestureSensor.DetectionProperties detectionProperties) {
        Set<FeedbackEffect> set = this.feedbackEffects;
        if (set != null) {
            for (FeedbackEffect feedbackEffect : set) {
                feedbackEffect.onGestureDetected(i, detectionProperties);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Action(Context context, Set<? extends FeedbackEffect> set) {
        this.context = context;
        this.feedbackEffects = set;
    }

    public void onGestureDetected(int i, GestureSensor.DetectionProperties detectionProperties) {
        updateFeedbackEffects(i, detectionProperties);
        if (i == 1) {
            Log.i(getTag$vendor__unbundled_google__packages__SystemUIGoogle__android_common__sysuig(), "Triggering");
            onTrigger(detectionProperties);
        }
    }

    public String toString() {
        return getClass().getSimpleName();
    }
}
