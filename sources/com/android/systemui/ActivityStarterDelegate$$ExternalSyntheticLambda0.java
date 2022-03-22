package com.android.systemui;

import android.graphics.Rect;
import android.graphics.Region;
import com.android.keyguard.KeyguardUnfoldTransition;
import com.android.systemui.shared.animation.UnfoldConstantTranslateAnimator;
import com.android.systemui.statusbar.phone.NotificationPanelView;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.google.android.systemui.assist.uihints.NgaUiController;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ActivityStarterDelegate$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ ActivityStarterDelegate$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                ((StatusBar) obj).postQSRunnableDismissingKeyguard((Runnable) this.f$0);
                return;
            case 1:
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
            case 2:
                PrintWriter printWriter = (PrintWriter) this.f$0;
                LegacySplitScreenController legacySplitScreenController = (LegacySplitScreenController) obj;
                Objects.requireNonNull(legacySplitScreenController);
                printWriter.print("  mVisible=");
                printWriter.println(legacySplitScreenController.mVisible);
                printWriter.print("  mMinimized=");
                printWriter.println(legacySplitScreenController.mMinimized);
                printWriter.print("  mAdjustedForIme=");
                printWriter.println(legacySplitScreenController.mAdjustedForIme);
                return;
            default:
                boolean z = NgaUiController.VERBOSE;
                ((Region) this.f$0).op((Region) obj, Region.Op.UNION);
                return;
        }
    }
}
