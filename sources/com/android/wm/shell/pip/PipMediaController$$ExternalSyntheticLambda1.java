package com.android.wm.shell.pip;

import android.app.PendingIntent;
import android.graphics.Rect;
import android.graphics.Region;
import com.android.keyguard.KeyguardUnfoldTransition;
import com.android.systemui.shared.animation.UnfoldConstantTranslateAnimator;
import com.android.systemui.statusbar.phone.NotificationPanelView;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.splitscreen.StageCoordinator;
import com.google.android.systemui.assist.uihints.NgaUiController;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PipMediaController$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ PipMediaController$$ExternalSyntheticLambda1(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                ((PipMediaController.ActionListener) obj).onMediaActionsChanged((List) this.f$0);
                return;
            case 1:
                ((StatusBar) obj).startPendingIntentDismissingKeyguard((PendingIntent) this.f$0);
                return;
            case 2:
                NotificationPanelViewController notificationPanelViewController = (NotificationPanelViewController) this.f$0;
                KeyguardUnfoldTransition keyguardUnfoldTransition = (KeyguardUnfoldTransition) obj;
                Rect rect = NotificationPanelViewController.M_DUMMY_DIRTY_RECT;
                Objects.requireNonNull(notificationPanelViewController);
                NotificationPanelView notificationPanelView = notificationPanelViewController.mView;
                Objects.requireNonNull(keyguardUnfoldTransition);
                UnfoldConstantTranslateAnimator unfoldConstantTranslateAnimator = (UnfoldConstantTranslateAnimator) keyguardUnfoldTransition.translateAnimator$delegate.getValue();
                Objects.requireNonNull(unfoldConstantTranslateAnimator);
                unfoldConstantTranslateAnimator.rootView = notificationPanelView;
                unfoldConstantTranslateAnimator.translationMax = keyguardUnfoldTransition.context.getResources().getDimensionPixelSize(2131165865);
                unfoldConstantTranslateAnimator.progressProvider.addCallback(unfoldConstantTranslateAnimator);
                return;
            case 3:
                SplitScreenController.ISplitScreenImpl iSplitScreenImpl = (SplitScreenController.ISplitScreenImpl) this.f$0;
                SplitScreenController splitScreenController = (SplitScreenController) obj;
                int i = SplitScreenController.ISplitScreenImpl.$r8$clinit;
                Objects.requireNonNull(iSplitScreenImpl);
                SplitScreenController.ISplitScreenImpl.AnonymousClass1 r2 = iSplitScreenImpl.mSplitScreenListener;
                Objects.requireNonNull(splitScreenController);
                StageCoordinator stageCoordinator = splitScreenController.mStageCoordinator;
                Objects.requireNonNull(stageCoordinator);
                stageCoordinator.mListeners.remove(r2);
                return;
            default:
                boolean z = NgaUiController.VERBOSE;
                ((Region) this.f$0).op((Region) obj, Region.Op.UNION);
                return;
        }
    }
}
