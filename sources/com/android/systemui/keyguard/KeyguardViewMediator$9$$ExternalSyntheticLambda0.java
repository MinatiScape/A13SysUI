package com.android.systemui.keyguard;

import android.view.IRemoteAnimationFinishedCallback;
import android.view.RemoteAnimationTarget;
import android.view.SurfaceControl;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.recents.OverviewProxyRecentsImpl;
import com.android.wm.shell.bubbles.Bubble;
import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.bubbles.BubbleData;
import com.android.wm.shell.bubbles.BubbleEntry;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class KeyguardViewMediator$9$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ KeyguardViewMediator$9$$ExternalSyntheticLambda0(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                KeyguardViewMediator.AnonymousClass9 r0 = (KeyguardViewMediator.AnonymousClass9) this.f$0;
                KeyguardViewMediator.StartKeyguardExitAnimParams startKeyguardExitAnimParams = (KeyguardViewMediator.StartKeyguardExitAnimParams) this.f$1;
                Objects.requireNonNull(r0);
                KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
                long j = startKeyguardExitAnimParams.startTime;
                long j2 = startKeyguardExitAnimParams.fadeoutDuration;
                RemoteAnimationTarget[] remoteAnimationTargetArr = startKeyguardExitAnimParams.mApps;
                RemoteAnimationTarget[] remoteAnimationTargetArr2 = startKeyguardExitAnimParams.mWallpapers;
                RemoteAnimationTarget[] remoteAnimationTargetArr3 = startKeyguardExitAnimParams.mNonApps;
                IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback = startKeyguardExitAnimParams.mFinishedCallback;
                boolean z = KeyguardViewMediator.DEBUG;
                keyguardViewMediator.handleStartKeyguardExitAnimation(j, j2, remoteAnimationTargetArr, remoteAnimationTargetArr2, remoteAnimationTargetArr3, iRemoteAnimationFinishedCallback);
                KeyguardViewMediator.this.mFalsingCollector.onSuccessfulUnlock();
                return;
            case 1:
                OverviewProxyRecentsImpl overviewProxyRecentsImpl = (OverviewProxyRecentsImpl) this.f$0;
                Objects.requireNonNull(overviewProxyRecentsImpl);
                overviewProxyRecentsImpl.mHandler.post((Runnable) this.f$1);
                return;
            case 2:
                BubbleController.BubblesImpl bubblesImpl = (BubbleController.BubblesImpl) this.f$0;
                BubbleEntry bubbleEntry = (BubbleEntry) this.f$1;
                Objects.requireNonNull(bubblesImpl);
                BubbleController bubbleController = BubbleController.this;
                Objects.requireNonNull(bubbleController);
                if (bubbleController.isSummaryOfBubbles(bubbleEntry)) {
                    String groupKey = bubbleEntry.mSbn.getGroupKey();
                    BubbleData bubbleData = bubbleController.mBubbleData;
                    Objects.requireNonNull(bubbleData);
                    bubbleData.mSuppressedGroupKeys.remove(groupKey);
                    BubbleData.Update update = bubbleData.mStateChange;
                    update.suppressedSummaryChanged = true;
                    update.suppressedSummaryGroup = groupKey;
                    bubbleData.dispatchPendingChanges();
                    ArrayList<Bubble> bubblesInGroup = bubbleController.getBubblesInGroup(groupKey);
                    for (int i = 0; i < bubblesInGroup.size(); i++) {
                        Bubble bubble = bubblesInGroup.get(i);
                        Objects.requireNonNull(bubble);
                        bubbleController.removeBubble(bubble.mKey, 9);
                    }
                    return;
                }
                bubbleController.removeBubble(bubbleEntry.getKey(), 5);
                return;
            default:
                ((Consumer) this.f$0).accept((SurfaceControl.ScreenshotHardwareBuffer) this.f$1);
                return;
        }
    }
}
