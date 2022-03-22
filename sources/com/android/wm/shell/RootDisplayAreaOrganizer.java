package com.android.wm.shell;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.util.SparseArray;
import android.view.SurfaceControl;
import android.window.DisplayAreaAppearedInfo;
import android.window.DisplayAreaInfo;
import android.window.DisplayAreaOrganizer;
import com.android.wm.shell.common.ShellExecutor;
import java.util.List;
/* loaded from: classes.dex */
public class RootDisplayAreaOrganizer extends DisplayAreaOrganizer {
    public final SparseArray<DisplayAreaInfo> mDisplayAreasInfo = new SparseArray<>();
    public final SparseArray<SurfaceControl> mLeashes = new SparseArray<>();

    public final void onDisplayAreaAppeared(DisplayAreaInfo displayAreaInfo, SurfaceControl surfaceControl) {
        if (displayAreaInfo.featureId == 0) {
            int i = displayAreaInfo.displayId;
            if (this.mDisplayAreasInfo.get(i) == null) {
                this.mDisplayAreasInfo.put(i, displayAreaInfo);
                this.mLeashes.put(i, surfaceControl);
                return;
            }
            throw new IllegalArgumentException("Duplicate DA for displayId: " + i + " displayAreaInfo:" + displayAreaInfo + " mDisplayAreasInfo.get():" + this.mDisplayAreasInfo.get(i));
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Unknown feature: ");
        m.append(displayAreaInfo.featureId);
        m.append("displayAreaInfo:");
        m.append(displayAreaInfo);
        throw new IllegalArgumentException(m.toString());
    }

    public final void onDisplayAreaInfoChanged(DisplayAreaInfo displayAreaInfo) {
        int i = displayAreaInfo.displayId;
        if (this.mDisplayAreasInfo.get(i) != null) {
            this.mDisplayAreasInfo.put(i, displayAreaInfo);
            return;
        }
        throw new IllegalArgumentException("onDisplayAreaInfoChanged() Unknown DA displayId: " + i + " displayAreaInfo:" + displayAreaInfo + " mDisplayAreasInfo.get():" + this.mDisplayAreasInfo.get(i));
    }

    public final void onDisplayAreaVanished(DisplayAreaInfo displayAreaInfo) {
        int i = displayAreaInfo.displayId;
        if (this.mDisplayAreasInfo.get(i) != null) {
            this.mDisplayAreasInfo.remove(i);
            return;
        }
        throw new IllegalArgumentException("onDisplayAreaVanished() Unknown DA displayId: " + i + " displayAreaInfo:" + displayAreaInfo + " mDisplayAreasInfo.get():" + this.mDisplayAreasInfo.get(i));
    }

    public final String toString() {
        StringBuilder m = DebugInfo$$ExternalSyntheticOutline0.m("RootDisplayAreaOrganizer", "#");
        m.append(this.mDisplayAreasInfo.size());
        return m.toString();
    }

    public RootDisplayAreaOrganizer(ShellExecutor shellExecutor) {
        super(shellExecutor);
        List registerOrganizer = registerOrganizer(0);
        int size = registerOrganizer.size();
        while (true) {
            size--;
            if (size >= 0) {
                onDisplayAreaAppeared(((DisplayAreaAppearedInfo) registerOrganizer.get(size)).getDisplayAreaInfo(), ((DisplayAreaAppearedInfo) registerOrganizer.get(size)).getLeash());
            } else {
                return;
            }
        }
    }
}
