package com.google.android.systemui.columbus.gates;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.google.android.systemui.columbus.gates.Gate;
import java.util.LinkedHashSet;
/* compiled from: Gate.kt */
/* loaded from: classes.dex */
public abstract class Gate {
    public boolean active;
    public final Context context;
    public boolean isBlocked;
    public final LinkedHashSet listeners;
    public final Handler notifyHandler;

    /* compiled from: Gate.kt */
    /* loaded from: classes.dex */
    public interface Listener {
        void onGateChanged(Gate gate);
    }

    public Gate(Context context, Handler handler) {
        this.context = context;
        this.notifyHandler = handler;
        this.listeners = new LinkedHashSet();
    }

    public abstract void onActivate();

    public abstract void onDeactivate();

    public final boolean isBlocking() {
        if (!this.active || !this.isBlocked) {
            return false;
        }
        return true;
    }

    public final void registerListener(Listener listener) {
        this.listeners.add(listener);
        if (!this.active && (!this.listeners.isEmpty())) {
            this.active = true;
            onActivate();
        }
    }

    public final void setBlocking(boolean z) {
        if (this.isBlocked != z) {
            this.isBlocked = z;
            if (this.active) {
                for (final Listener listener : this.listeners) {
                    this.notifyHandler.post(new Runnable() { // from class: com.google.android.systemui.columbus.gates.Gate$notifyListeners$1$1
                        @Override // java.lang.Runnable
                        public final void run() {
                            Gate.Listener.this.onGateChanged(this);
                        }
                    });
                }
            }
        }
    }

    public final void unregisterListener(Listener listener) {
        this.listeners.remove(listener);
        if (this.active && this.listeners.isEmpty()) {
            this.active = false;
            onDeactivate();
        }
    }

    public String toString() {
        return getClass().getSimpleName();
    }

    public /* synthetic */ Gate(Context context) {
        this(context, new Handler(Looper.getMainLooper()));
    }
}
