package com.android.systemui.globalactions;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.settingslib.Utils;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
import com.android.systemui.plugins.GlobalActions;
import com.android.systemui.scrim.ScrimDrawable;
import com.android.systemui.statusbar.BlurUtils;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.telephony.TelephonyCallback;
import com.android.systemui.telephony.TelephonyListenerManager;
import dagger.Lazy;
import java.util.Objects;
/* loaded from: classes.dex */
public final class GlobalActionsImpl implements GlobalActions, CommandQueue.Callbacks {
    public final BlurUtils mBlurUtils;
    public final CommandQueue mCommandQueue;
    public final Context mContext;
    public final DeviceProvisionedController mDeviceProvisionedController;
    public boolean mDisabled;
    public GlobalActionsDialogLite mGlobalActionsDialog;
    public final Lazy<GlobalActionsDialogLite> mGlobalActionsDialogLazy;
    public final KeyguardStateController mKeyguardStateController;

    @Override // com.android.systemui.plugins.GlobalActions
    public final void destroy() {
        this.mCommandQueue.removeCallback((CommandQueue.Callbacks) this);
        GlobalActionsDialogLite globalActionsDialogLite = this.mGlobalActionsDialog;
        if (globalActionsDialogLite != null) {
            globalActionsDialogLite.mBroadcastDispatcher.unregisterReceiver(globalActionsDialogLite.mBroadcastReceiver);
            TelephonyListenerManager telephonyListenerManager = globalActionsDialogLite.mTelephonyListenerManager;
            GlobalActionsDialogLite.AnonymousClass6 r2 = globalActionsDialogLite.mPhoneStateListener;
            Objects.requireNonNull(telephonyListenerManager);
            TelephonyCallback telephonyCallback = telephonyListenerManager.mTelephonyCallback;
            Objects.requireNonNull(telephonyCallback);
            telephonyCallback.mServiceStateListeners.remove(r2);
            telephonyListenerManager.updateListening();
            globalActionsDialogLite.mGlobalSettings.unregisterContentObserver(globalActionsDialogLite.mAirplaneModeObserver);
            globalActionsDialogLite.mConfigurationController.removeCallback(globalActionsDialogLite);
            this.mGlobalActionsDialog = null;
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void disable(int i, int i2, int i3, boolean z) {
        boolean z2;
        GlobalActionsDialogLite globalActionsDialogLite;
        if ((i3 & 8) != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (i == this.mContext.getDisplayId() && z2 != this.mDisabled) {
            this.mDisabled = z2;
            if (z2 && (globalActionsDialogLite = this.mGlobalActionsDialog) != null) {
                globalActionsDialogLite.mHandler.removeMessages(0);
                globalActionsDialogLite.mHandler.sendEmptyMessage(0);
            }
        }
    }

    @Override // com.android.systemui.plugins.GlobalActions
    public final void showGlobalActions(GlobalActions.GlobalActionsManager globalActionsManager) {
        if (!this.mDisabled) {
            GlobalActionsDialogLite globalActionsDialogLite = this.mGlobalActionsDialogLazy.get();
            this.mGlobalActionsDialog = globalActionsDialogLite;
            globalActionsDialogLite.showOrHideDialog(this.mKeyguardStateController.isShowing(), this.mDeviceProvisionedController.isDeviceProvisioned(), null);
        }
    }

    @Override // com.android.systemui.plugins.GlobalActions
    public final void showShutdownUi(boolean z, String str) {
        int i;
        int i2;
        String str2;
        final ScrimDrawable scrimDrawable = new ScrimDrawable();
        final Dialog dialog = new Dialog(this.mContext, 2132018186);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.android.systemui.globalactions.GlobalActionsImpl$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnShowListener
            public final void onShow(DialogInterface dialogInterface) {
                GlobalActionsImpl globalActionsImpl = GlobalActionsImpl.this;
                ScrimDrawable scrimDrawable2 = scrimDrawable;
                Dialog dialog2 = dialog;
                Objects.requireNonNull(globalActionsImpl);
                if (globalActionsImpl.mBlurUtils.supportsBlursOnWindows()) {
                    scrimDrawable2.setAlpha(255);
                    globalActionsImpl.mBlurUtils.applyBlur(dialog2.getWindow().getDecorView().getViewRootImpl(), (int) globalActionsImpl.mBlurUtils.blurRadiusOfRatio(1.0f), true);
                    return;
                }
                scrimDrawable2.setAlpha((int) (globalActionsImpl.mContext.getResources().getFloat(2131167014) * 255.0f));
            }
        });
        Window window = dialog.getWindow();
        window.requestFeature(1);
        window.getAttributes().systemUiVisibility |= 1792;
        window.getDecorView();
        window.getAttributes().width = -1;
        window.getAttributes().height = -1;
        window.getAttributes().layoutInDisplayCutoutMode = 3;
        window.setType(2020);
        window.getAttributes().setFitInsetsTypes(0);
        window.clearFlags(2);
        window.addFlags(17629472);
        window.setBackgroundDrawable(scrimDrawable);
        window.setWindowAnimations(2132017166);
        dialog.setContentView(17367316);
        dialog.setCancelable(false);
        if (this.mBlurUtils.supportsBlursOnWindows()) {
            i = Utils.getColorAttrDefaultColor(this.mContext, 2130970103);
        } else {
            i = this.mContext.getResources().getColor(2131099879);
        }
        ((ProgressBar) dialog.findViewById(16908301)).getIndeterminateDrawable().setTint(i);
        TextView textView = (TextView) dialog.findViewById(16908308);
        TextView textView2 = (TextView) dialog.findViewById(16908309);
        textView.setTextColor(i);
        textView2.setTextColor(i);
        if (str != null && str.startsWith("recovery-update")) {
            i2 = 17041347;
        } else if ((str == null || !str.equals("recovery")) && !z) {
            i2 = 17041492;
        } else {
            i2 = 17041343;
        }
        textView2.setText(i2);
        if (str != null && str.startsWith("recovery-update")) {
            str2 = this.mContext.getString(17041348);
        } else if (str == null || !str.equals("recovery")) {
            str2 = null;
        } else {
            str2 = this.mContext.getString(17041344);
        }
        if (str2 != null) {
            textView.setVisibility(0);
            textView.setText(str2);
        }
        dialog.show();
    }

    public GlobalActionsImpl(Context context, CommandQueue commandQueue, Lazy<GlobalActionsDialogLite> lazy, BlurUtils blurUtils, KeyguardStateController keyguardStateController, DeviceProvisionedController deviceProvisionedController) {
        this.mContext = context;
        this.mGlobalActionsDialogLazy = lazy;
        this.mKeyguardStateController = keyguardStateController;
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mCommandQueue = commandQueue;
        this.mBlurUtils = blurUtils;
        commandQueue.addCallback((CommandQueue.Callbacks) this);
    }
}
