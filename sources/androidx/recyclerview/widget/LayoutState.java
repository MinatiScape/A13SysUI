package androidx.recyclerview.widget;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.core.graphics.Insets$$ExternalSyntheticOutline0;
/* loaded from: classes.dex */
public final class LayoutState {
    public int mAvailable;
    public int mCurrentPosition;
    public boolean mInfinite;
    public int mItemDirection;
    public int mLayoutDirection;
    public boolean mStopInFocusable;
    public boolean mRecycle = true;
    public int mStartLine = 0;
    public int mEndLine = 0;

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("LayoutState{mAvailable=");
        m.append(this.mAvailable);
        m.append(", mCurrentPosition=");
        m.append(this.mCurrentPosition);
        m.append(", mItemDirection=");
        m.append(this.mItemDirection);
        m.append(", mLayoutDirection=");
        m.append(this.mLayoutDirection);
        m.append(", mStartLine=");
        m.append(this.mStartLine);
        m.append(", mEndLine=");
        return Insets$$ExternalSyntheticOutline0.m(m, this.mEndLine, '}');
    }
}
