package com.android.systemui.wmshell;

import android.content.Context;
import android.view.View;
import com.android.systemui.plugins.OverlayPlugin;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.wmshell.BubblesManager;
import com.android.wm.shell.bubbles.BadgedImageView;
import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.bubbles.Bubbles;
import com.android.wm.shell.bubbles.animation.StackAnimationController;
import com.android.wm.shell.pip.tv.TvPipController;
import com.android.wm.shell.pip.tv.TvPipNotificationController;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BubblesManager$5$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ BubblesManager$5$$ExternalSyntheticLambda2(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        float f;
        Object[] objArr = null;
        boolean z = false;
        switch (this.$r8$classId) {
            case 0:
                BubblesManager.AnonymousClass5 r0 = (BubblesManager.AnonymousClass5) this.f$0;
                Objects.requireNonNull(r0);
                ((Consumer) this.f$1).accept(Boolean.valueOf(BubblesManager.this.mNotificationShadeWindowController.getPanelExpanded()));
                return;
            case 1:
                StatusBar.AnonymousClass2 r02 = (StatusBar.AnonymousClass2) this.f$0;
                Objects.requireNonNull(r02);
                r02.mOverlays.remove((OverlayPlugin) this.f$1);
                NotificationShadeWindowController notificationShadeWindowController = StatusBar.this.mNotificationShadeWindowController;
                if (r02.mOverlays.size() != 0) {
                    z = true;
                }
                notificationShadeWindowController.setForcePluginOpen(z, r02);
                return;
            case 2:
                BubbleController.BubblesImpl bubblesImpl = (BubbleController.BubblesImpl) this.f$0;
                Objects.requireNonNull(bubblesImpl);
                BubbleController.this.setExpandListener((Bubbles.BubbleExpandListener) this.f$1);
                return;
            case 3:
                StackAnimationController stackAnimationController = (StackAnimationController) this.f$0;
                List list = (List) this.f$1;
                Objects.requireNonNull(stackAnimationController);
                for (int i = 0; i < list.size(); i++) {
                    View view = (View) list.get(i);
                    if (i < 2) {
                        f = (stackAnimationController.mMaxBubbles * stackAnimationController.mElevation) - i;
                    } else {
                        f = 0.0f;
                    }
                    view.setZ(f);
                    BadgedImageView badgedImageView = (BadgedImageView) view;
                    if (i == 0) {
                        badgedImageView.showDotAndBadge(!stackAnimationController.isStackOnLeftSide());
                    } else {
                        badgedImageView.hideDotAndBadge(!stackAnimationController.isStackOnLeftSide());
                    }
                }
                return;
            default:
                TvPipController.TvPipImpl tvPipImpl = (TvPipController.TvPipImpl) this.f$0;
                Objects.requireNonNull(tvPipImpl);
                TvPipController tvPipController = TvPipController.this;
                Objects.requireNonNull(tvPipController);
                if (tvPipController.mState != 0) {
                    objArr = 1;
                }
                if (objArr != null) {
                    tvPipController.closePip();
                }
                tvPipController.mResizeAnimationDuration = tvPipController.mContext.getResources().getInteger(2131492902);
                TvPipNotificationController tvPipNotificationController = tvPipController.mPipNotificationController;
                Context context = tvPipController.mContext;
                Objects.requireNonNull(tvPipNotificationController);
                tvPipNotificationController.mDefaultTitle = context.getResources().getString(2131952982);
                if (tvPipNotificationController.mNotified) {
                    tvPipNotificationController.update();
                    return;
                }
                return;
        }
    }
}
