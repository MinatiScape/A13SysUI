package com.android.systemui.qs.tiles;

import android.animation.ValueAnimator;
import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;
import android.view.CompositionSamplingListener;
import android.view.SurfaceControl;
import com.android.settingslib.drawable.CircleFramedDrawable;
import com.android.settingslib.users.EditUserPhotoController;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.shared.navigationbar.RegionSamplingHelper;
import com.android.systemui.util.RingerModeLiveData;
import com.android.systemui.util.concurrency.MessageRouter;
import com.android.systemui.util.concurrency.MessageRouterImpl;
import com.android.systemui.volume.D;
import com.android.systemui.volume.Util;
import com.android.systemui.volume.VolumeDialogControllerImpl;
import com.android.systemui.wallet.controller.QuickAccessWalletController;
import com.android.wm.shell.splitscreen.SplitScreenTransitions;
import java.util.List;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class QuickAccessWalletTile$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ QuickAccessWalletTile$$ExternalSyntheticLambda0(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        boolean z = false;
        switch (this.$r8$classId) {
            case 0:
                QuickAccessWalletTile quickAccessWalletTile = (QuickAccessWalletTile) this.f$0;
                ActivityLaunchAnimator.Controller controller = (ActivityLaunchAnimator.Controller) this.f$1;
                Objects.requireNonNull(quickAccessWalletTile);
                QuickAccessWalletController quickAccessWalletController = quickAccessWalletTile.mController;
                ActivityStarter activityStarter = quickAccessWalletTile.mActivityStarter;
                if (quickAccessWalletTile.mSelectedCard != null) {
                    z = true;
                }
                quickAccessWalletController.startQuickAccessUiIntent(activityStarter, controller, z);
                return;
            case 1:
                EditUserPhotoController editUserPhotoController = (EditUserPhotoController) this.f$0;
                Bitmap bitmap = (Bitmap) this.f$1;
                Objects.requireNonNull(editUserPhotoController);
                if (bitmap != null) {
                    editUserPhotoController.mNewUserPhotoBitmap = bitmap;
                    Context context = editUserPhotoController.mImageView.getContext();
                    Bitmap bitmap2 = editUserPhotoController.mNewUserPhotoBitmap;
                    int i = CircleFramedDrawable.$r8$clinit;
                    CircleFramedDrawable circleFramedDrawable = new CircleFramedDrawable(bitmap2, context.getResources().getDimensionPixelSize(17105621));
                    editUserPhotoController.mNewUserPhotoDrawable = circleFramedDrawable;
                    editUserPhotoController.mImageView.setImageDrawable(circleFramedDrawable);
                    return;
                }
                return;
            case 2:
                RegionSamplingHelper regionSamplingHelper = (RegionSamplingHelper) this.f$0;
                SurfaceControl surfaceControl = (SurfaceControl) this.f$1;
                if (surfaceControl != null) {
                    Objects.requireNonNull(regionSamplingHelper);
                    if (!surfaceControl.isValid()) {
                        return;
                    }
                }
                RegionSamplingHelper.SysuiCompositionSamplingListener sysuiCompositionSamplingListener = regionSamplingHelper.mCompositionSamplingListener;
                RegionSamplingHelper.AnonymousClass3 r3 = regionSamplingHelper.mSamplingListener;
                Rect rect = regionSamplingHelper.mSamplingRequestBounds;
                Objects.requireNonNull(sysuiCompositionSamplingListener);
                CompositionSamplingListener.register(r3, 0, surfaceControl, rect);
                return;
            case 3:
                MessageRouterImpl messageRouterImpl = (MessageRouterImpl) this.f$0;
                Object obj = this.f$1;
                Objects.requireNonNull(messageRouterImpl);
                synchronized (messageRouterImpl.mDataMessageListenerMap) {
                    if (messageRouterImpl.mDataMessageListenerMap.containsKey(obj.getClass())) {
                        for (MessageRouter.DataMessageListener dataMessageListener : (List) messageRouterImpl.mDataMessageListenerMap.get(obj.getClass())) {
                            dataMessageListener.onMessage(obj);
                        }
                    }
                }
                synchronized (messageRouterImpl.mDataMessageCancelers) {
                    if (messageRouterImpl.mDataMessageCancelers.containsKey(obj.getClass()) && !((List) messageRouterImpl.mDataMessageCancelers.get(obj.getClass())).isEmpty()) {
                        ((List) messageRouterImpl.mDataMessageCancelers.get(obj.getClass())).remove(0);
                        if (((List) messageRouterImpl.mDataMessageCancelers.get(obj.getClass())).isEmpty()) {
                            messageRouterImpl.mDataMessageCancelers.remove(obj.getClass());
                        }
                    }
                }
                return;
            case 4:
                VolumeDialogControllerImpl.RingerModeObservers.AnonymousClass2 r0 = (VolumeDialogControllerImpl.RingerModeObservers.AnonymousClass2) this.f$0;
                Objects.requireNonNull(r0);
                int intValue = ((Integer) this.f$1).intValue();
                RingerModeLiveData ringerModeLiveData = VolumeDialogControllerImpl.RingerModeObservers.this.mRingerModeInternal;
                Objects.requireNonNull(ringerModeLiveData);
                if (ringerModeLiveData.initialSticky) {
                    VolumeDialogControllerImpl.this.mState.ringerModeInternal = intValue;
                }
                if (D.BUG) {
                    String str = VolumeDialogControllerImpl.TAG;
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("onChange internal_ringer_mode rm=");
                    m.append(Util.ringerModeToString(intValue));
                    Log.d(str, m.toString());
                }
                VolumeDialogControllerImpl volumeDialogControllerImpl = VolumeDialogControllerImpl.this;
                String str2 = VolumeDialogControllerImpl.TAG;
                if (volumeDialogControllerImpl.updateRingerModeInternalW(intValue)) {
                    VolumeDialogControllerImpl volumeDialogControllerImpl2 = VolumeDialogControllerImpl.this;
                    volumeDialogControllerImpl2.mCallbacks.onStateChanged(volumeDialogControllerImpl2.mState);
                    return;
                }
                return;
            default:
                SplitScreenTransitions splitScreenTransitions = (SplitScreenTransitions) this.f$0;
                Objects.requireNonNull(splitScreenTransitions);
                splitScreenTransitions.mAnimations.remove((ValueAnimator) this.f$1);
                splitScreenTransitions.onFinish(null);
                return;
        }
    }
}
