package com.android.wm.shell;

import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import com.android.keyguard.CarrierTextManager;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.ImageWallpaper;
import com.android.systemui.biometrics.AuthContainerView;
import com.android.systemui.communal.CommunalSource;
import com.android.systemui.communal.CommunalSourceMonitor;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.tiles.CastTile;
import com.android.systemui.qs.tiles.CastTile$$ExternalSyntheticLambda1;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.policy.BrightnessMirrorController;
import com.android.wm.shell.common.split.SplitLayout;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class TaskView$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ TaskView$$ExternalSyntheticLambda4(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Intent registerReceiver;
        WeakReference<CommunalSource> weakReference;
        switch (this.$r8$classId) {
            case 0:
                TaskView taskView = (TaskView) this.f$0;
                int i = TaskView.$r8$clinit;
                Objects.requireNonNull(taskView);
                taskView.mListener.onReleased();
                return;
            case 1:
                boolean z = CarrierTextManager.DEBUG;
                ((CarrierTextManager.CarrierTextCallback) this.f$0).updateCarrierInfo(new CarrierTextManager.CarrierTextCallbackInfo("", null, false, null));
                return;
            case 2:
                KeyguardUpdateMonitor keyguardUpdateMonitor = (KeyguardUpdateMonitor) this.f$0;
                boolean z2 = KeyguardUpdateMonitor.DEBUG;
                Objects.requireNonNull(keyguardUpdateMonitor);
                int defaultSubscriptionId = SubscriptionManager.getDefaultSubscriptionId();
                ServiceState serviceStateForSubscriber = ((TelephonyManager) keyguardUpdateMonitor.mContext.getSystemService(TelephonyManager.class)).getServiceStateForSubscriber(defaultSubscriptionId);
                KeyguardUpdateMonitor.AnonymousClass14 r4 = keyguardUpdateMonitor.mHandler;
                r4.sendMessage(r4.obtainMessage(330, defaultSubscriptionId, 0, serviceStateForSubscriber));
                if (keyguardUpdateMonitor.mBatteryStatus == null && (registerReceiver = keyguardUpdateMonitor.mContext.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"))) != null && keyguardUpdateMonitor.mBatteryStatus == null) {
                    keyguardUpdateMonitor.mBroadcastReceiver.onReceive(keyguardUpdateMonitor.mContext, registerReceiver);
                    return;
                }
                return;
            case 3:
                ImageWallpaper.GLEngine gLEngine = (ImageWallpaper.GLEngine) this.f$0;
                int i2 = ImageWallpaper.GLEngine.MIN_SURFACE_WIDTH;
                Objects.requireNonNull(gLEngine);
                gLEngine.computeAndNotifyLocalColors(new ArrayList(ImageWallpaper.this.mColorAreas), ImageWallpaper.this.mMiniBitmap);
                return;
            case 4:
                AuthContainerView authContainerView = (AuthContainerView) this.f$0;
                int i3 = AuthContainerView.$r8$clinit;
                Objects.requireNonNull(authContainerView);
                authContainerView.setVisibility(4);
                authContainerView.removeWindowIfAttached();
                return;
            case 5:
                CommunalSourceMonitor communalSourceMonitor = (CommunalSourceMonitor) this.f$0;
                boolean z3 = CommunalSourceMonitor.DEBUG;
                Objects.requireNonNull(communalSourceMonitor);
                Iterator<WeakReference<CommunalSourceMonitor.Callback>> it = communalSourceMonitor.mCallbacks.iterator();
                while (it.hasNext()) {
                    CommunalSourceMonitor.Callback callback = it.next().get();
                    if (callback == null) {
                        it.remove();
                    } else {
                        if (!communalSourceMonitor.mAllCommunalConditionsMet || communalSourceMonitor.mCurrentSource == null) {
                            weakReference = null;
                        } else {
                            weakReference = new WeakReference<>(communalSourceMonitor.mCurrentSource);
                        }
                        callback.onSourceAvailable(weakReference);
                    }
                }
                return;
            case FalsingManager.VERSION /* 6 */:
                GlobalActionsDialogLite globalActionsDialogLite = (GlobalActionsDialogLite) this.f$0;
                Objects.requireNonNull(globalActionsDialogLite);
                globalActionsDialogLite.createActionItems();
                return;
            case 7:
                CastTile castTile = (CastTile) this.f$0;
                int i4 = CastTile.$r8$clinit;
                Objects.requireNonNull(castTile);
                castTile.mUiHandler.post(new CastTile$$ExternalSyntheticLambda1(castTile, null, 0));
                return;
            case 8:
                VisualStabilityManager visualStabilityManager = (VisualStabilityManager) this.f$0;
                Objects.requireNonNull(visualStabilityManager);
                visualStabilityManager.mIsTemporaryReorderingAllowed = false;
                visualStabilityManager.updateAllowedStates();
                return;
            case 9:
                BrightnessMirrorController brightnessMirrorController = (BrightnessMirrorController) this.f$0;
                Objects.requireNonNull(brightnessMirrorController);
                brightnessMirrorController.mBrightnessMirror.setVisibility(4);
                return;
            default:
                SplitLayout splitLayout = (SplitLayout) this.f$0;
                Objects.requireNonNull(splitLayout);
                splitLayout.mSplitLayoutHandler.onSnappedToDismiss(true);
                return;
        }
    }
}
