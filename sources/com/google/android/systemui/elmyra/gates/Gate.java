package com.google.android.systemui.elmyra.gates;

import android.content.Context;
import android.os.Handler;
import com.android.wifitrackerlib.WifiEntry$$ExternalSyntheticLambda0;
/* loaded from: classes.dex */
public abstract class Gate {
    public boolean mActive = false;
    public final Context mContext;
    public Listener mListener;
    public final Handler mNotifyHandler;

    /* loaded from: classes.dex */
    public interface Listener {
        void onGateChanged(Gate gate);
    }

    public abstract boolean isBlocked();

    public abstract void onActivate();

    public abstract void onDeactivate();

    public final void activate() {
        if (!this.mActive) {
            this.mActive = true;
            onActivate();
        }
    }

    public final void deactivate() {
        if (this.mActive) {
            this.mActive = false;
            onDeactivate();
        }
    }

    public final boolean isBlocking() {
        if (!this.mActive || !isBlocked()) {
            return false;
        }
        return true;
    }

    public final void notifyListener() {
        if (this.mActive && this.mListener != null) {
            this.mNotifyHandler.post(new WifiEntry$$ExternalSyntheticLambda0(this, 7));
        }
    }

    public Gate(Context context) {
        this.mContext = context;
        this.mNotifyHandler = new Handler(context.getMainLooper());
    }

    public String toString() {
        return getClass().getSimpleName();
    }
}
