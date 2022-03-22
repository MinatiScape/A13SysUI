package com.google.android.systemui.gamedashboard;

import android.content.Context;
import android.os.Handler;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import com.android.systemui.ScreenDecorations$$ExternalSyntheticLambda3;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
/* loaded from: classes.dex */
public final class ScreenRecordController implements LifecycleOwner {
    public final Context mContext;
    public final RecordingController mController;
    public final KeyguardDismissUtil mKeyguardDismissUtil;
    public final LifecycleRegistry mLifecycle = new LifecycleRegistry(this, true);
    public final ToastController mToast;
    public final Handler mUiHandler;

    public ScreenRecordController(RecordingController recordingController, Handler handler, KeyguardDismissUtil keyguardDismissUtil, Context context, ToastController toastController) {
        this.mContext = context;
        this.mController = recordingController;
        this.mUiHandler = handler;
        handler.post(new ScreenDecorations$$ExternalSyntheticLambda3(this, 5));
        this.mKeyguardDismissUtil = keyguardDismissUtil;
        this.mToast = toastController;
    }

    @Override // androidx.lifecycle.LifecycleOwner
    public final Lifecycle getLifecycle() {
        return this.mLifecycle;
    }
}
