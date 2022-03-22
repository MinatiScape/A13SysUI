package androidx.transition;

import android.view.View;
import android.view.WindowId;
/* loaded from: classes.dex */
public final class WindowIdApi18 implements WindowIdImpl {
    public final WindowId mWindowId;

    public final boolean equals(Object obj) {
        if (!(obj instanceof WindowIdApi18) || !((WindowIdApi18) obj).mWindowId.equals(this.mWindowId)) {
            return false;
        }
        return true;
    }

    public final int hashCode() {
        return this.mWindowId.hashCode();
    }

    public WindowIdApi18(View view) {
        this.mWindowId = view.getWindowId();
    }
}
