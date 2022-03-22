package com.android.systemui.toast;

import android.animation.Animator;
import android.app.ITransientNotificationCallback;
import android.content.Context;
import android.os.IBinder;
import android.os.UserHandle;
import android.view.View;
import android.widget.ToastPresenter;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ToastUI$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ToastUI f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ CharSequence f$2;
    public final /* synthetic */ String f$3;
    public final /* synthetic */ ITransientNotificationCallback f$4;
    public final /* synthetic */ IBinder f$5;
    public final /* synthetic */ IBinder f$6;
    public final /* synthetic */ int f$7;

    public /* synthetic */ ToastUI$$ExternalSyntheticLambda0(ToastUI toastUI, int i, CharSequence charSequence, String str, ITransientNotificationCallback iTransientNotificationCallback, IBinder iBinder, IBinder iBinder2, int i2) {
        this.f$0 = toastUI;
        this.f$1 = i;
        this.f$2 = charSequence;
        this.f$3 = str;
        this.f$4 = iTransientNotificationCallback;
        this.f$5 = iBinder;
        this.f$6 = iBinder2;
        this.f$7 = i2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        boolean z;
        ToastUI toastUI = this.f$0;
        int i = this.f$1;
        CharSequence charSequence = this.f$2;
        String str = this.f$3;
        ITransientNotificationCallback iTransientNotificationCallback = this.f$4;
        IBinder iBinder = this.f$5;
        IBinder iBinder2 = this.f$6;
        int i2 = this.f$7;
        Objects.requireNonNull(toastUI);
        UserHandle userHandleForUid = UserHandle.getUserHandleForUid(i);
        Context createContextAsUser = toastUI.mContext.createContextAsUser(userHandleForUid, 0);
        SystemUIToast createToast = toastUI.mToastFactory.createToast(toastUI.mContext, charSequence, str, userHandleForUid.getIdentifier(), toastUI.mOrientation);
        toastUI.mToast = createToast;
        Animator animator = createToast.mInAnimator;
        if (animator != null) {
            animator.start();
        }
        toastUI.mCallback = iTransientNotificationCallback;
        ToastPresenter toastPresenter = new ToastPresenter(createContextAsUser, toastUI.mIAccessibilityManager, toastUI.mNotificationManager, str);
        toastUI.mPresenter = toastPresenter;
        toastPresenter.getLayoutParams().setTrustedOverlay();
        ToastLogger toastLogger = toastUI.mToastLogger;
        String charSequence2 = charSequence.toString();
        String obj = iBinder.toString();
        Objects.requireNonNull(toastLogger);
        LogLevel logLevel = LogLevel.DEBUG;
        ToastLogger$logOnShowToast$2 toastLogger$logOnShowToast$2 = ToastLogger$logOnShowToast$2.INSTANCE;
        LogBuffer logBuffer = toastLogger.buffer;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("ToastLog", logLevel, toastLogger$logOnShowToast$2);
            obtain.int1 = i;
            obtain.str1 = str;
            obtain.str2 = charSequence2;
            obtain.str3 = obj;
            logBuffer.push(obtain);
        }
        ToastPresenter toastPresenter2 = toastUI.mPresenter;
        SystemUIToast systemUIToast = toastUI.mToast;
        Objects.requireNonNull(systemUIToast);
        View view = systemUIToast.mToastView;
        int intValue = toastUI.mToast.getGravity().intValue();
        int intValue2 = toastUI.mToast.getXOffset().intValue();
        int intValue3 = toastUI.mToast.getYOffset().intValue();
        float intValue4 = toastUI.mToast.getHorizontalMargin().intValue();
        float intValue5 = toastUI.mToast.getVerticalMargin().intValue();
        ITransientNotificationCallback iTransientNotificationCallback2 = toastUI.mCallback;
        SystemUIToast systemUIToast2 = toastUI.mToast;
        Objects.requireNonNull(systemUIToast2);
        if (systemUIToast2.mInAnimator == null && systemUIToast2.mOutAnimator == null) {
            z = false;
        } else {
            z = true;
        }
        toastPresenter2.show(view, iBinder, iBinder2, i2, intValue, intValue2, intValue3, intValue4, intValue5, iTransientNotificationCallback2, z);
    }
}
