package com.android.systemui.accessibility;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.IRemoteMagnificationAnimationCallback;
import android.view.accessibility.IWindowMagnificationConnection;
import android.view.accessibility.IWindowMagnificationConnectionCallback;
import com.android.systemui.accessibility.WindowMagnificationAnimationController;
import com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda2;
import java.util.Objects;
/* loaded from: classes.dex */
public final class WindowMagnificationConnectionImpl extends IWindowMagnificationConnection.Stub {
    public static final /* synthetic */ int $r8$clinit = 0;
    public IWindowMagnificationConnectionCallback mConnectionCallback;
    public final Handler mHandler;
    public final ModeSwitchesController mModeSwitchesController;
    public final WindowMagnification mWindowMagnification;

    public final void enableWindowMagnification(final int i, final float f, final float f2, final float f3, final float f4, final float f5, final IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.accessibility.WindowMagnificationConnectionImpl$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                float f6;
                float f7;
                float f8;
                WindowMagnificationConnectionImpl windowMagnificationConnectionImpl = WindowMagnificationConnectionImpl.this;
                int i2 = i;
                float f9 = f;
                float f10 = f2;
                float f11 = f3;
                float f12 = f4;
                float f13 = f5;
                IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback2 = iRemoteMagnificationAnimationCallback;
                Objects.requireNonNull(windowMagnificationConnectionImpl);
                WindowMagnification windowMagnification = windowMagnificationConnectionImpl.mWindowMagnification;
                Objects.requireNonNull(windowMagnification);
                WindowMagnificationController windowMagnificationController = windowMagnification.mMagnificationControllerSupplier.get(i2);
                if (windowMagnificationController != null) {
                    WindowMagnificationAnimationController windowMagnificationAnimationController = windowMagnificationController.mAnimationController;
                    Objects.requireNonNull(windowMagnificationAnimationController);
                    if (windowMagnificationAnimationController.mController != null) {
                        windowMagnificationAnimationController.sendAnimationCallback(false);
                        windowMagnificationAnimationController.mMagnificationFrameOffsetRatioX = f12;
                        windowMagnificationAnimationController.mMagnificationFrameOffsetRatioY = f13;
                        if (iRemoteMagnificationAnimationCallback2 == null) {
                            int i3 = windowMagnificationAnimationController.mState;
                            if (i3 == 3 || i3 == 2) {
                                windowMagnificationAnimationController.mValueAnimator.cancel();
                            }
                            windowMagnificationAnimationController.mController.enableWindowMagnificationInternal(f9, f10, f11, windowMagnificationAnimationController.mMagnificationFrameOffsetRatioX, windowMagnificationAnimationController.mMagnificationFrameOffsetRatioY);
                            windowMagnificationAnimationController.setState(1);
                            return;
                        }
                        windowMagnificationAnimationController.mAnimationCallback = iRemoteMagnificationAnimationCallback2;
                        WindowMagnificationController windowMagnificationController2 = windowMagnificationAnimationController.mController;
                        if (windowMagnificationController2 != null) {
                            float f14 = Float.NaN;
                            if (windowMagnificationController2.isWindowVisible()) {
                                f6 = windowMagnificationController2.mScale;
                            } else {
                                f6 = Float.NaN;
                            }
                            WindowMagnificationController windowMagnificationController3 = windowMagnificationAnimationController.mController;
                            Objects.requireNonNull(windowMagnificationController3);
                            if (windowMagnificationController3.isWindowVisible()) {
                                f7 = windowMagnificationController3.mMagnificationFrame.exactCenterX();
                            } else {
                                f7 = Float.NaN;
                            }
                            WindowMagnificationController windowMagnificationController4 = windowMagnificationAnimationController.mController;
                            Objects.requireNonNull(windowMagnificationController4);
                            if (windowMagnificationController4.isWindowVisible()) {
                                f14 = windowMagnificationController4.mMagnificationFrame.exactCenterY();
                            }
                            if (windowMagnificationAnimationController.mState == 0) {
                                windowMagnificationAnimationController.mStartSpec.set(1.0f, f10, f11);
                                WindowMagnificationAnimationController.AnimationSpec animationSpec = windowMagnificationAnimationController.mEndSpec;
                                if (Float.isNaN(f9)) {
                                    f8 = windowMagnificationAnimationController.mContext.getResources().getInteger(2131492993);
                                } else {
                                    f8 = f9;
                                }
                                animationSpec.set(f8, f10, f11);
                            } else {
                                windowMagnificationAnimationController.mStartSpec.set(f6, f7, f14);
                                WindowMagnificationAnimationController.AnimationSpec animationSpec2 = windowMagnificationAnimationController.mEndSpec;
                                if (!Float.isNaN(f9)) {
                                    f6 = f9;
                                }
                                if (!Float.isNaN(f10)) {
                                    f7 = f10;
                                }
                                if (!Float.isNaN(f11)) {
                                    f14 = f11;
                                }
                                animationSpec2.set(f6, f7, f14);
                            }
                            if (WindowMagnificationAnimationController.DEBUG) {
                                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("SetupEnableAnimationSpecs : mStartSpec = ");
                                m.append(windowMagnificationAnimationController.mStartSpec);
                                m.append(", endSpec = ");
                                m.append(windowMagnificationAnimationController.mEndSpec);
                                Log.d("WindowMagnificationAnimationController", m.toString());
                            }
                        }
                        if (windowMagnificationAnimationController.mEndSpec.equals(windowMagnificationAnimationController.mStartSpec)) {
                            int i4 = windowMagnificationAnimationController.mState;
                            if (i4 == 0) {
                                windowMagnificationAnimationController.mController.enableWindowMagnificationInternal(f9, f10, f11, windowMagnificationAnimationController.mMagnificationFrameOffsetRatioX, windowMagnificationAnimationController.mMagnificationFrameOffsetRatioY);
                            } else if (i4 == 3 || i4 == 2) {
                                windowMagnificationAnimationController.mValueAnimator.cancel();
                            }
                            windowMagnificationAnimationController.sendAnimationCallback(true);
                            windowMagnificationAnimationController.setState(1);
                            return;
                        }
                        int i5 = windowMagnificationAnimationController.mState;
                        if (i5 == 2) {
                            windowMagnificationAnimationController.mValueAnimator.reverse();
                        } else {
                            if (i5 == 3) {
                                windowMagnificationAnimationController.mValueAnimator.cancel();
                            }
                            windowMagnificationAnimationController.mValueAnimator.start();
                        }
                        windowMagnificationAnimationController.setState(3);
                    }
                }
            }
        });
    }

    public final void disableWindowMagnification(final int i, final IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.accessibility.WindowMagnificationConnectionImpl$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                float f;
                WindowMagnificationConnectionImpl windowMagnificationConnectionImpl = WindowMagnificationConnectionImpl.this;
                int i2 = i;
                IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback2 = iRemoteMagnificationAnimationCallback;
                Objects.requireNonNull(windowMagnificationConnectionImpl);
                WindowMagnification windowMagnification = windowMagnificationConnectionImpl.mWindowMagnification;
                Objects.requireNonNull(windowMagnification);
                WindowMagnificationController windowMagnificationController = windowMagnification.mMagnificationControllerSupplier.get(i2);
                if (windowMagnificationController != null) {
                    WindowMagnificationAnimationController windowMagnificationAnimationController = windowMagnificationController.mAnimationController;
                    Objects.requireNonNull(windowMagnificationAnimationController);
                    if (windowMagnificationAnimationController.mController != null) {
                        windowMagnificationAnimationController.sendAnimationCallback(false);
                        if (iRemoteMagnificationAnimationCallback2 == null) {
                            int i3 = windowMagnificationAnimationController.mState;
                            if (i3 == 3 || i3 == 2) {
                                windowMagnificationAnimationController.mValueAnimator.cancel();
                            }
                            windowMagnificationAnimationController.mController.deleteWindowMagnification$1();
                            windowMagnificationAnimationController.setState(0);
                            return;
                        }
                        windowMagnificationAnimationController.mAnimationCallback = iRemoteMagnificationAnimationCallback2;
                        int i4 = windowMagnificationAnimationController.mState;
                        if (i4 != 0 && i4 != 2) {
                            windowMagnificationAnimationController.mStartSpec.set(1.0f, Float.NaN, Float.NaN);
                            WindowMagnificationAnimationController.AnimationSpec animationSpec = windowMagnificationAnimationController.mEndSpec;
                            WindowMagnificationController windowMagnificationController2 = windowMagnificationAnimationController.mController;
                            Objects.requireNonNull(windowMagnificationController2);
                            if (windowMagnificationController2.isWindowVisible()) {
                                f = windowMagnificationController2.mScale;
                            } else {
                                f = Float.NaN;
                            }
                            animationSpec.set(f, Float.NaN, Float.NaN);
                            windowMagnificationAnimationController.mValueAnimator.reverse();
                            windowMagnificationAnimationController.setState(2);
                        } else if (i4 == 0) {
                            windowMagnificationAnimationController.sendAnimationCallback(true);
                        }
                    }
                }
            }
        });
    }

    public final void moveWindowMagnifier(final int i, final float f, final float f2) {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.accessibility.WindowMagnificationConnectionImpl$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                WindowMagnificationConnectionImpl windowMagnificationConnectionImpl = WindowMagnificationConnectionImpl.this;
                int i2 = i;
                float f3 = f;
                float f4 = f2;
                Objects.requireNonNull(windowMagnificationConnectionImpl);
                WindowMagnification windowMagnification = windowMagnificationConnectionImpl.mWindowMagnification;
                Objects.requireNonNull(windowMagnification);
                WindowMagnificationController windowMagnificationController = windowMagnification.mMagnificationControllerSupplier.get(i2);
                if (windowMagnificationController != null) {
                    windowMagnificationController.moveWindowMagnifier(f3, f4);
                }
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void removeMagnificationButton(int i) {
        this.mHandler.post(new OverviewProxyService$1$$ExternalSyntheticLambda2(this, i, 1));
    }

    public final void setScale(final int i, final float f) {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.accessibility.WindowMagnificationConnectionImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                WindowMagnificationConnectionImpl windowMagnificationConnectionImpl = WindowMagnificationConnectionImpl.this;
                int i2 = i;
                float f2 = f;
                Objects.requireNonNull(windowMagnificationConnectionImpl);
                WindowMagnification windowMagnification = windowMagnificationConnectionImpl.mWindowMagnification;
                Objects.requireNonNull(windowMagnification);
                WindowMagnificationController windowMagnificationController = windowMagnification.mMagnificationControllerSupplier.get(i2);
                if (windowMagnificationController != null) {
                    WindowMagnificationAnimationController windowMagnificationAnimationController = windowMagnificationController.mAnimationController;
                    Objects.requireNonNull(windowMagnificationAnimationController);
                    if (!windowMagnificationAnimationController.mValueAnimator.isRunning() && windowMagnificationController.isWindowVisible() && windowMagnificationController.mScale != f2) {
                        windowMagnificationController.enableWindowMagnificationInternal(f2, Float.NaN, Float.NaN, Float.NaN, Float.NaN);
                        windowMagnificationController.mHandler.removeCallbacks(windowMagnificationController.mUpdateStateDescriptionRunnable);
                        windowMagnificationController.mHandler.postDelayed(windowMagnificationController.mUpdateStateDescriptionRunnable, 100L);
                    }
                }
            }
        });
    }

    public final void showMagnificationButton(final int i, final int i2) {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.accessibility.WindowMagnificationConnectionImpl$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                WindowMagnificationConnectionImpl windowMagnificationConnectionImpl = WindowMagnificationConnectionImpl.this;
                int i3 = i;
                int i4 = i2;
                Objects.requireNonNull(windowMagnificationConnectionImpl);
                ModeSwitchesController modeSwitchesController = windowMagnificationConnectionImpl.mModeSwitchesController;
                Objects.requireNonNull(modeSwitchesController);
                MagnificationModeSwitch magnificationModeSwitch = modeSwitchesController.mSwitchSupplier.get(i3);
                if (magnificationModeSwitch != null) {
                    magnificationModeSwitch.showButton(i4, true);
                }
            }
        });
    }

    public WindowMagnificationConnectionImpl(WindowMagnification windowMagnification, Handler handler, ModeSwitchesController modeSwitchesController) {
        this.mWindowMagnification = windowMagnification;
        this.mHandler = handler;
        this.mModeSwitchesController = modeSwitchesController;
    }

    public final void setConnectionCallback(IWindowMagnificationConnectionCallback iWindowMagnificationConnectionCallback) {
        this.mConnectionCallback = iWindowMagnificationConnectionCallback;
    }
}
