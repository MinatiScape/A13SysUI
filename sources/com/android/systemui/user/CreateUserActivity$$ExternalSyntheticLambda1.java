package com.android.systemui.user;

import android.graphics.Outline;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.view.ViewOutlineProvider;
import androidx.core.widget.ContentLoadingProgressBar;
import com.android.settingslib.Utils;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.phone.PhoneStatusBarPolicy;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.TapAgainView;
import com.android.systemui.statusbar.phone.TapAgainViewController;
import com.android.systemui.volume.VolumeDialogImpl;
import com.android.wm.shell.bubbles.Bubble;
import com.android.wm.shell.bubbles.BubbleController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class CreateUserActivity$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ CreateUserActivity$$ExternalSyntheticLambda1(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        int i;
        int i2;
        switch (this.$r8$classId) {
            case 0:
                ((CreateUserActivity) this.f$0).finish();
                return;
            case 1:
                ContentLoadingProgressBar contentLoadingProgressBar = (ContentLoadingProgressBar) this.f$0;
                int i3 = ContentLoadingProgressBar.$r8$clinit;
                Objects.requireNonNull(contentLoadingProgressBar);
                System.currentTimeMillis();
                contentLoadingProgressBar.setVisibility(0);
                return;
            case 2:
                ((ExpandableView) this.f$0).removeFromTransientContainer();
                return;
            case 3:
                PhoneStatusBarPolicy phoneStatusBarPolicy = (PhoneStatusBarPolicy) this.f$0;
                boolean z = PhoneStatusBarPolicy.DEBUG;
                Objects.requireNonNull(phoneStatusBarPolicy);
                phoneStatusBarPolicy.mIconController.setIconVisibility(phoneStatusBarPolicy.mSlotScreenRecord, true);
                return;
            case 4:
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                ((StatusBar) this.f$0).onLaunchTransitionFadingEnded();
                return;
            case 5:
                TapAgainViewController tapAgainViewController = (TapAgainViewController) this.f$0;
                Objects.requireNonNull(tapAgainViewController);
                tapAgainViewController.mHideCanceler = null;
                ((TapAgainView) tapAgainViewController.mView).animateOut();
                return;
            case FalsingManager.VERSION /* 6 */:
                final VolumeDialogImpl volumeDialogImpl = (VolumeDialogImpl) this.f$0;
                String str = VolumeDialogImpl.TAG;
                Objects.requireNonNull(volumeDialogImpl);
                LayerDrawable layerDrawable = (LayerDrawable) volumeDialogImpl.mRingerAndDrawerContainer.getBackground();
                if (layerDrawable != null && layerDrawable.getNumberOfLayers() > 0) {
                    volumeDialogImpl.mRingerAndDrawerContainerBackground = layerDrawable.getDrawable(0);
                    volumeDialogImpl.updateBackgroundForDrawerClosedAmount();
                    if (volumeDialogImpl.mTopContainer != null) {
                        LayerDrawable layerDrawable2 = new LayerDrawable(new Drawable[]{new ColorDrawable(Utils.getColorAttrDefaultColor(volumeDialogImpl.mContext, 17956909))});
                        int i4 = volumeDialogImpl.mDialogWidth;
                        if (!volumeDialogImpl.isLandscape()) {
                            i = volumeDialogImpl.mDialogRowsView.getHeight();
                        } else {
                            i = volumeDialogImpl.mDialogRowsView.getHeight() + volumeDialogImpl.mDialogCornerRadius;
                        }
                        layerDrawable2.setLayerSize(0, i4, i);
                        if (!volumeDialogImpl.isLandscape()) {
                            i2 = volumeDialogImpl.mDialogRowsViewContainer.getTop();
                        } else {
                            i2 = volumeDialogImpl.mDialogRowsViewContainer.getTop() - volumeDialogImpl.mDialogCornerRadius;
                        }
                        layerDrawable2.setLayerInsetTop(0, i2);
                        layerDrawable2.setLayerGravity(0, 53);
                        if (volumeDialogImpl.isLandscape()) {
                            volumeDialogImpl.mRingerAndDrawerContainer.setOutlineProvider(new ViewOutlineProvider() { // from class: com.android.systemui.volume.VolumeDialogImpl.5
                                @Override // android.view.ViewOutlineProvider
                                public final void getOutline(View view, Outline outline) {
                                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), VolumeDialogImpl.this.mDialogCornerRadius);
                                }
                            });
                            volumeDialogImpl.mRingerAndDrawerContainer.setClipToOutline(true);
                        }
                        volumeDialogImpl.mTopContainer.setBackground(layerDrawable2);
                        return;
                    }
                    return;
                }
                return;
            default:
                BubbleController.BubblesImpl bubblesImpl = (BubbleController.BubblesImpl) this.f$0;
                Objects.requireNonNull(bubblesImpl);
                BubbleController bubbleController = BubbleController.this;
                Objects.requireNonNull(bubbleController);
                for (Bubble bubble : bubbleController.mBubbleData.getBubbles()) {
                    bubble.setShowDot(bubble.showInShade());
                }
                return;
        }
    }
}
