package com.android.systemui.dreams;

import android.graphics.Rect;
import android.graphics.Region;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardDisplayManager$$ExternalSyntheticLambda1;
import com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda0;
import com.android.systemui.R$anim;
import com.android.systemui.dreams.complication.ComplicationHostViewController;
import com.android.systemui.dreams.complication.dagger.ComplicationHostViewComponent;
import com.android.systemui.util.ViewController;
import com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda10;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DreamOverlayContainerViewController extends ViewController<DreamOverlayContainerView> {
    public final long mBurnInProtectionUpdateInterval;
    public final ComplicationHostViewController mComplicationHostViewController;
    public final ViewGroup mDreamOverlayContentView;
    public final int mDreamOverlayNotificationsDragAreaHeight;
    public final Handler mHandler;
    public final int mMaxBurnInOffset;
    public final AnonymousClass1 mOnComputeInternalInsetsListener = new ViewTreeObserver.OnComputeInternalInsetsListener() { // from class: com.android.systemui.dreams.DreamOverlayContainerViewController.1
        public final void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
            internalInsetsInfo.setTouchableInsets(3);
            Region region = new Region();
            Rect rect = new Rect();
            int childCount = DreamOverlayContainerViewController.this.mDreamOverlayContentView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = DreamOverlayContainerViewController.this.mDreamOverlayContentView.getChildAt(i);
                ComplicationHostViewController complicationHostViewController = DreamOverlayContainerViewController.this.mComplicationHostViewController;
                Objects.requireNonNull(complicationHostViewController);
                if (complicationHostViewController.mView == childAt) {
                    ComplicationHostViewController complicationHostViewController2 = DreamOverlayContainerViewController.this.mComplicationHostViewController;
                    Objects.requireNonNull(complicationHostViewController2);
                    Region region2 = new Region();
                    Rect rect2 = new Rect();
                    int childCount2 = ((ConstraintLayout) complicationHostViewController2.mView).getChildCount();
                    for (int i2 = 0; i2 < childCount2; i2++) {
                        if (((ConstraintLayout) complicationHostViewController2.mView).getChildAt(i2).getGlobalVisibleRect(rect2)) {
                            region2.op(rect2, Region.Op.UNION);
                        }
                    }
                    region.op(region2, Region.Op.UNION);
                } else if (childAt.getGlobalVisibleRect(rect)) {
                    region.op(rect, Region.Op.UNION);
                }
            }
            if (DreamOverlayContainerViewController.this.mDreamOverlayContentView.getGlobalVisibleRect(rect)) {
                rect.bottom = rect.top + DreamOverlayContainerViewController.this.mDreamOverlayNotificationsDragAreaHeight;
                region.op(rect, Region.Op.UNION);
            }
            internalInsetsInfo.touchableRegion.set(region);
        }

        {
            DreamOverlayContainerViewController.this = this;
        }
    };
    public final DreamOverlayStatusBarViewController mStatusBarViewController;

    @Override // com.android.systemui.util.ViewController
    public final void onInit() {
        this.mStatusBarViewController.init();
        this.mComplicationHostViewController.init();
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        ((DreamOverlayContainerView) this.mView).getViewTreeObserver().addOnComputeInternalInsetsListener(this.mOnComputeInternalInsetsListener);
        this.mHandler.postDelayed(new AccessPoint$$ExternalSyntheticLambda0(this, 3), this.mBurnInProtectionUpdateInterval);
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        this.mHandler.removeCallbacks(new VolumeDialogImpl$$ExternalSyntheticLambda10(this, 3));
        ((DreamOverlayContainerView) this.mView).getViewTreeObserver().removeOnComputeInternalInsetsListener(this.mOnComputeInternalInsetsListener);
    }

    public static void $r8$lambda$Oxvj_GJUc06UJC_m7GrRwIKFrUA(DreamOverlayContainerViewController dreamOverlayContainerViewController) {
        Objects.requireNonNull(dreamOverlayContainerViewController);
        ((DreamOverlayContainerView) dreamOverlayContainerViewController.mView).setTranslationX(R$anim.getBurnInOffset(dreamOverlayContainerViewController.mMaxBurnInOffset * 2, true) - dreamOverlayContainerViewController.mMaxBurnInOffset);
        ((DreamOverlayContainerView) dreamOverlayContainerViewController.mView).setTranslationY(R$anim.getBurnInOffset(dreamOverlayContainerViewController.mMaxBurnInOffset * 2, false) - dreamOverlayContainerViewController.mMaxBurnInOffset);
        dreamOverlayContainerViewController.mHandler.postDelayed(new KeyguardDisplayManager$$ExternalSyntheticLambda1(dreamOverlayContainerViewController, 2), dreamOverlayContainerViewController.mBurnInProtectionUpdateInterval);
    }

    public DreamOverlayContainerViewController(DreamOverlayContainerView dreamOverlayContainerView, ComplicationHostViewComponent.Factory factory, ViewGroup viewGroup, DreamOverlayStatusBarViewController dreamOverlayStatusBarViewController, Handler handler, int i, long j) {
        super(dreamOverlayContainerView);
        this.mDreamOverlayContentView = viewGroup;
        this.mStatusBarViewController = dreamOverlayStatusBarViewController;
        this.mDreamOverlayNotificationsDragAreaHeight = dreamOverlayContainerView.getResources().getDimensionPixelSize(2131165695);
        ComplicationHostViewController controller = factory.create().getController();
        this.mComplicationHostViewController = controller;
        Objects.requireNonNull(controller);
        viewGroup.addView(controller.mView, new ViewGroup.LayoutParams(-1, -1));
        this.mHandler = handler;
        this.mMaxBurnInOffset = i;
        this.mBurnInProtectionUpdateInterval = j;
    }

    @VisibleForTesting
    public int getDreamOverlayNotificationsDragAreaHeight() {
        return this.mDreamOverlayNotificationsDragAreaHeight;
    }
}
