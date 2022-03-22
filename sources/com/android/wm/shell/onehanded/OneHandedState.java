package com.android.wm.shell.onehanded;

import com.android.wm.shell.onehanded.OneHandedState;
import java.util.ArrayList;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class OneHandedState {
    public static int sCurrentState;
    public ArrayList mStateChangeListeners = new ArrayList();

    /* loaded from: classes.dex */
    public interface OnStateChangedListener {
        default void onStateChanged(int i) {
        }
    }

    public final void setState(final int i) {
        sCurrentState = i;
        if (!this.mStateChangeListeners.isEmpty()) {
            this.mStateChangeListeners.forEach(new Consumer() { // from class: com.android.wm.shell.onehanded.OneHandedState$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((OneHandedState.OnStateChangedListener) obj).onStateChanged(i);
                }
            });
        }
    }

    public OneHandedState() {
        sCurrentState = 0;
    }
}
