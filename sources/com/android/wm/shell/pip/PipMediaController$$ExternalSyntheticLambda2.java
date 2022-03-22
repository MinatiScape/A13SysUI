package com.android.wm.shell.pip;

import android.graphics.Region;
import android.media.MediaMetadata;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.splitscreen.StageCoordinator;
import com.google.android.systemui.gamedashboard.EntryPointController;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PipMediaController$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ PipMediaController$$ExternalSyntheticLambda2(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                ((PipMediaController.MetadataListener) obj).onMediaMetadataChanged((MediaMetadata) this.f$0);
                return;
            case 1:
                SplitScreenController.ISplitScreenImpl iSplitScreenImpl = (SplitScreenController.ISplitScreenImpl) this.f$0;
                SplitScreenController splitScreenController = (SplitScreenController) obj;
                int i = SplitScreenController.ISplitScreenImpl.$r8$clinit;
                Objects.requireNonNull(iSplitScreenImpl);
                SplitScreenController.ISplitScreenImpl.AnonymousClass1 r3 = iSplitScreenImpl.mSplitScreenListener;
                Objects.requireNonNull(splitScreenController);
                StageCoordinator stageCoordinator = splitScreenController.mStageCoordinator;
                Objects.requireNonNull(stageCoordinator);
                if (!stageCoordinator.mListeners.contains(r3)) {
                    stageCoordinator.mListeners.add(r3);
                    r3.onStagePositionChanged(0, stageCoordinator.getMainStagePosition());
                    r3.onStagePositionChanged(1, stageCoordinator.mSideStagePosition);
                    if (stageCoordinator.mSideStageListener.mVisible) {
                        boolean z = stageCoordinator.mMainStageListener.mVisible;
                    }
                    stageCoordinator.mSideStage.onSplitScreenListenerRegistered(r3, 1);
                    stageCoordinator.mMainStage.onSplitScreenListenerRegistered(r3, 0);
                    return;
                }
                return;
            case 2:
                ((Region) this.f$0).op((Region) obj, Region.Op.DIFFERENCE);
                return;
            default:
                EntryPointController entryPointController = (EntryPointController) this.f$0;
                Objects.requireNonNull(entryPointController);
                ((LegacySplitScreen) obj).registerInSplitScreenListener(entryPointController.mInSplitScreenCallback);
                return;
        }
    }
}
