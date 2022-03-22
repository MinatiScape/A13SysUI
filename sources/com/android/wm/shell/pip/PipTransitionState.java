package com.android.wm.shell.pip;
/* loaded from: classes.dex */
public final class PipTransitionState {
    public boolean mInSwipePipToHomeTransition;
    public int mState = 0;

    public final boolean isInPip() {
        int i = this.mState;
        if (i < 1 || i == 5) {
            return false;
        }
        return true;
    }
}
