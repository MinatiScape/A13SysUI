package com.android.systemui.statusbar.notification.collection.listbuilder;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline0;
import com.android.systemui.plugins.FalsingManager;
/* loaded from: classes.dex */
public final class PipelineState {
    public int mState = 0;

    public final String getStateName() {
        int i = this.mState;
        switch (i) {
            case 0:
                return "STATE_IDLE";
            case 1:
                return "STATE_BUILD_STARTED";
            case 2:
                return "STATE_RESETTING";
            case 3:
                return "STATE_PRE_GROUP_FILTERING";
            case 4:
                return "STATE_GROUPING";
            case 5:
                return "STATE_TRANSFORMING";
            case FalsingManager.VERSION /* 6 */:
                return "STATE_GROUP_STABILIZING";
            case 7:
                return "STATE_SORTING";
            case 8:
                return "STATE_FINALIZE_FILTERING";
            case 9:
                return "STATE_FINALIZING";
            default:
                return VendorAtomValue$$ExternalSyntheticOutline0.m("STATE:", i);
        }
    }

    public final void incrementTo(int i) {
        if (this.mState == i - 1) {
            this.mState = i;
            return;
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Cannot increment from state ");
        m.append(this.mState);
        m.append(" to state ");
        m.append(i);
        throw new IllegalStateException(m.toString());
    }

    public final void requireIsBefore(int i) {
        if (this.mState >= i) {
            StringBuilder m = ExifInterface$$ExternalSyntheticOutline0.m("Required state is <", i, " but actual state is ");
            m.append(this.mState);
            throw new IllegalStateException(m.toString());
        }
    }

    public final void requireState() {
        if (this.mState != 0) {
            StringBuilder m = ExifInterface$$ExternalSyntheticOutline0.m("Required state is <", 0, " but actual state is ");
            m.append(this.mState);
            throw new IllegalStateException(m.toString());
        }
    }
}
