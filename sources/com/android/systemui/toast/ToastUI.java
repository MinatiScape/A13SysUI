package com.android.systemui.toast;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.INotificationManager;
import android.app.ITransientNotificationCallback;
import android.content.Context;
import android.content.res.Configuration;
import android.os.IBinder;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.IAccessibilityManager;
import android.widget.ToastPresenter;
import androidx.constraintlayout.motion.widget.MotionLayout$$ExternalSyntheticOutline0;
import com.android.systemui.CoreStartable;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.CommandQueue;
import java.util.Objects;
/* loaded from: classes.dex */
public class ToastUI extends CoreStartable implements CommandQueue.Callbacks {
    public ITransientNotificationCallback mCallback;
    public final CommandQueue mCommandQueue;
    public final IAccessibilityManager mIAccessibilityManager;
    public final INotificationManager mNotificationManager;
    public int mOrientation = 1;
    public ToastPresenter mPresenter;
    public SystemUIToast mToast;
    public final ToastFactory mToastFactory;
    public final ToastLogger mToastLogger;
    public ToastOutAnimatorListener mToastOutAnimatorListener;

    /* loaded from: classes.dex */
    public class ToastOutAnimatorListener extends AnimatorListenerAdapter {
        public final ITransientNotificationCallback mPrevCallback;
        public final ToastPresenter mPrevPresenter;
        public Runnable mShowNextToastRunnable;

        public ToastOutAnimatorListener(ToastPresenter toastPresenter, ITransientNotificationCallback iTransientNotificationCallback, ToastUI$$ExternalSyntheticLambda0 toastUI$$ExternalSyntheticLambda0) {
            this.mPrevPresenter = toastPresenter;
            this.mPrevCallback = iTransientNotificationCallback;
            this.mShowNextToastRunnable = toastUI$$ExternalSyntheticLambda0;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationEnd(Animator animator) {
            this.mPrevPresenter.hide(this.mPrevCallback);
            Runnable runnable = this.mShowNextToastRunnable;
            if (runnable != null) {
                runnable.run();
            }
            ToastUI.this.mToastOutAnimatorListener = null;
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void showToast(int i, String str, IBinder iBinder, CharSequence charSequence, IBinder iBinder2, int i2, ITransientNotificationCallback iTransientNotificationCallback) {
        ToastUI$$ExternalSyntheticLambda0 toastUI$$ExternalSyntheticLambda0 = new ToastUI$$ExternalSyntheticLambda0(this, i, charSequence, str, iTransientNotificationCallback, iBinder, iBinder2, i2);
        ToastOutAnimatorListener toastOutAnimatorListener = this.mToastOutAnimatorListener;
        if (toastOutAnimatorListener != null) {
            toastOutAnimatorListener.mShowNextToastRunnable = toastUI$$ExternalSyntheticLambda0;
        } else if (this.mPresenter != null) {
            hideCurrentToast(toastUI$$ExternalSyntheticLambda0);
        } else {
            toastUI$$ExternalSyntheticLambda0.run();
        }
    }

    public final void hideCurrentToast(ToastUI$$ExternalSyntheticLambda0 toastUI$$ExternalSyntheticLambda0) {
        SystemUIToast systemUIToast = this.mToast;
        Objects.requireNonNull(systemUIToast);
        if (systemUIToast.mOutAnimator != null) {
            SystemUIToast systemUIToast2 = this.mToast;
            Objects.requireNonNull(systemUIToast2);
            Animator animator = systemUIToast2.mOutAnimator;
            ToastOutAnimatorListener toastOutAnimatorListener = new ToastOutAnimatorListener(this.mPresenter, this.mCallback, toastUI$$ExternalSyntheticLambda0);
            this.mToastOutAnimatorListener = toastOutAnimatorListener;
            animator.addListener(toastOutAnimatorListener);
            animator.start();
        } else {
            this.mPresenter.hide(this.mCallback);
            if (toastUI$$ExternalSyntheticLambda0 != null) {
                toastUI$$ExternalSyntheticLambda0.run();
            }
        }
        this.mToast = null;
        this.mPresenter = null;
        this.mCallback = null;
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void hideToast(String str, IBinder iBinder) {
        ToastPresenter toastPresenter = this.mPresenter;
        if (toastPresenter == null || !Objects.equals(toastPresenter.getPackageName(), str) || !Objects.equals(this.mPresenter.getToken(), iBinder)) {
            MotionLayout$$ExternalSyntheticOutline0.m("Attempt to hide non-current toast from package ", str, "ToastUI");
            return;
        }
        ToastLogger toastLogger = this.mToastLogger;
        String obj = iBinder.toString();
        Objects.requireNonNull(toastLogger);
        LogLevel logLevel = LogLevel.DEBUG;
        ToastLogger$logOnHideToast$2 toastLogger$logOnHideToast$2 = ToastLogger$logOnHideToast$2.INSTANCE;
        LogBuffer logBuffer = toastLogger.buffer;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("ToastLog", logLevel, toastLogger$logOnHideToast$2);
            obtain.str1 = str;
            obtain.str2 = obj;
            logBuffer.push(obtain);
        }
        hideCurrentToast(null);
    }

    @Override // com.android.systemui.CoreStartable
    public final void onConfigurationChanged(Configuration configuration) {
        int i = configuration.orientation;
        if (i != this.mOrientation) {
            this.mOrientation = i;
            SystemUIToast systemUIToast = this.mToast;
            if (systemUIToast != null) {
                ToastLogger toastLogger = this.mToastLogger;
                String charSequence = systemUIToast.mText.toString();
                boolean z = true;
                if (this.mOrientation != 1) {
                    z = false;
                }
                Objects.requireNonNull(toastLogger);
                LogLevel logLevel = LogLevel.DEBUG;
                ToastLogger$logOrientationChange$2 toastLogger$logOrientationChange$2 = ToastLogger$logOrientationChange$2.INSTANCE;
                LogBuffer logBuffer = toastLogger.buffer;
                Objects.requireNonNull(logBuffer);
                if (!logBuffer.frozen) {
                    LogMessageImpl obtain = logBuffer.obtain("ToastLog", logLevel, toastLogger$logOrientationChange$2);
                    obtain.str1 = charSequence;
                    obtain.bool1 = z;
                    logBuffer.push(obtain);
                }
                this.mToast.onOrientationChange(this.mOrientation);
                this.mPresenter.updateLayoutParams(this.mToast.getXOffset().intValue(), this.mToast.getYOffset().intValue(), this.mToast.getHorizontalMargin().intValue(), this.mToast.getVerticalMargin().intValue(), this.mToast.getGravity().intValue());
            }
        }
    }

    @Override // com.android.systemui.CoreStartable
    public final void start() {
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
    }

    public ToastUI(Context context, CommandQueue commandQueue, INotificationManager iNotificationManager, IAccessibilityManager iAccessibilityManager, ToastFactory toastFactory, ToastLogger toastLogger) {
        super(context);
        this.mCommandQueue = commandQueue;
        this.mNotificationManager = iNotificationManager;
        this.mIAccessibilityManager = iAccessibilityManager;
        this.mToastFactory = toastFactory;
        AccessibilityManager accessibilityManager = (AccessibilityManager) this.mContext.getSystemService(AccessibilityManager.class);
        this.mToastLogger = toastLogger;
    }
}
