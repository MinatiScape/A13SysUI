package com.google.android.systemui.elmyra.actions;

import android.content.Context;
import android.os.Handler;
import com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda3;
import com.google.android.systemui.elmyra.feedback.FeedbackEffect;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import java.util.ArrayList;
/* loaded from: classes.dex */
public abstract class Action {
    public final Context mContext;
    public final ArrayList mFeedbackEffects;
    public final Handler mHandler;
    public Listener mListener;

    /* loaded from: classes.dex */
    public interface Listener {
    }

    public abstract boolean isAvailable();

    public void onProgress(float f, int i) {
    }

    public abstract void onTrigger(GestureSensor.DetectionProperties detectionProperties);

    public void updateFeedbackEffects(float f, int i) {
        int i2 = 0;
        if (f == 0.0f || i == 0) {
            while (i2 < this.mFeedbackEffects.size()) {
                ((FeedbackEffect) this.mFeedbackEffects.get(i2)).onRelease();
                i2++;
            }
        } else if (isAvailable()) {
            while (i2 < this.mFeedbackEffects.size()) {
                ((FeedbackEffect) this.mFeedbackEffects.get(i2)).onProgress(f, i);
                i2++;
            }
        }
    }

    public final void notifyListener() {
        if (this.mListener != null) {
            this.mHandler.post(new Action$$ExternalSyntheticLambda0(this, 0));
        }
        if (!isAvailable()) {
            this.mHandler.post(new PipTaskOrganizer$$ExternalSyntheticLambda3(this, 6));
        }
    }

    public Action(Context context, ArrayList arrayList) {
        ArrayList arrayList2 = new ArrayList();
        this.mFeedbackEffects = arrayList2;
        this.mContext = context;
        this.mHandler = new Handler(context.getMainLooper());
        if (arrayList != null) {
            arrayList2.addAll(arrayList);
        }
    }

    public String toString() {
        return getClass().getSimpleName();
    }

    public void triggerFeedbackEffects(GestureSensor.DetectionProperties detectionProperties) {
        if (isAvailable()) {
            for (int i = 0; i < this.mFeedbackEffects.size(); i++) {
                ((FeedbackEffect) this.mFeedbackEffects.get(i)).onResolve(detectionProperties);
            }
        }
    }
}
