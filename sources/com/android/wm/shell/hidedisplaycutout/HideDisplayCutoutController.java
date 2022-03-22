package com.android.wm.shell.hidedisplaycutout;

import android.content.Context;
import android.content.res.Configuration;
import android.view.SurfaceControl;
import android.window.DisplayAreaAppearedInfo;
import android.window.WindowContainerTransaction;
import com.android.systemui.dreams.DreamOverlayStateController$$ExternalSyntheticLambda0;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutoutOrganizer;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class HideDisplayCutoutController {
    public final Context mContext;
    public boolean mEnabled;
    public final HideDisplayCutoutImpl mImpl = new HideDisplayCutoutImpl();
    public final ShellExecutor mMainExecutor;
    public final HideDisplayCutoutOrganizer mOrganizer;

    /* loaded from: classes.dex */
    public class HideDisplayCutoutImpl implements HideDisplayCutout {
        public HideDisplayCutoutImpl() {
        }

        @Override // com.android.wm.shell.hidedisplaycutout.HideDisplayCutout
        public final void onConfigurationChanged(Configuration configuration) {
            HideDisplayCutoutController.this.mMainExecutor.execute(new DreamOverlayStateController$$ExternalSyntheticLambda0(this, configuration, 1));
        }
    }

    public void updateStatus() {
        boolean z = this.mContext.getResources().getBoolean(17891672);
        if (z != this.mEnabled) {
            this.mEnabled = z;
            if (z) {
                HideDisplayCutoutOrganizer hideDisplayCutoutOrganizer = this.mOrganizer;
                Objects.requireNonNull(hideDisplayCutoutOrganizer);
                hideDisplayCutoutOrganizer.mDisplayController.addDisplayWindowListener(hideDisplayCutoutOrganizer.mListener);
                DisplayLayout displayLayout = hideDisplayCutoutOrganizer.mDisplayController.getDisplayLayout(0);
                if (displayLayout != null) {
                    hideDisplayCutoutOrganizer.mRotation = displayLayout.mRotation;
                }
                List registerOrganizer = hideDisplayCutoutOrganizer.registerOrganizer(6);
                for (int i = 0; i < registerOrganizer.size(); i++) {
                    hideDisplayCutoutOrganizer.addDisplayAreaInfoAndLeashToMap(((DisplayAreaAppearedInfo) registerOrganizer.get(i)).getDisplayAreaInfo(), ((DisplayAreaAppearedInfo) registerOrganizer.get(i)).getLeash());
                }
                hideDisplayCutoutOrganizer.updateBoundsAndOffsets(true);
                WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
                SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
                synchronized (hideDisplayCutoutOrganizer) {
                    hideDisplayCutoutOrganizer.mDisplayAreaMap.forEach(new HideDisplayCutoutOrganizer$$ExternalSyntheticLambda0(hideDisplayCutoutOrganizer, windowContainerTransaction, transaction));
                }
                hideDisplayCutoutOrganizer.applyTransaction(windowContainerTransaction, transaction);
                return;
            }
            HideDisplayCutoutOrganizer hideDisplayCutoutOrganizer2 = this.mOrganizer;
            Objects.requireNonNull(hideDisplayCutoutOrganizer2);
            hideDisplayCutoutOrganizer2.updateBoundsAndOffsets(false);
            DisplayController displayController = hideDisplayCutoutOrganizer2.mDisplayController;
            HideDisplayCutoutOrganizer.AnonymousClass1 r1 = hideDisplayCutoutOrganizer2.mListener;
            Objects.requireNonNull(displayController);
            synchronized (displayController.mDisplays) {
                displayController.mDisplayChangedListeners.remove(r1);
            }
            hideDisplayCutoutOrganizer2.unregisterOrganizer();
        }
    }

    public HideDisplayCutoutController(Context context, HideDisplayCutoutOrganizer hideDisplayCutoutOrganizer, ShellExecutor shellExecutor) {
        this.mContext = context;
        this.mOrganizer = hideDisplayCutoutOrganizer;
        this.mMainExecutor = shellExecutor;
        updateStatus();
    }
}
