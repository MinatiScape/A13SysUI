package com.android.wm.shell.pip.phone;

import android.graphics.Rect;
import android.os.RemoteException;
import android.util.Log;
import android.view.Choreographer;
import android.view.accessibility.IWindowMagnificationConnectionCallback;
import com.android.systemui.accessibility.WindowMagnification;
import com.android.systemui.accessibility.WindowMagnificationAnimationController;
import com.android.systemui.accessibility.WindowMagnificationConnectionImpl;
import com.android.systemui.accessibility.WindowMagnificationController;
import com.android.systemui.accessibility.WindowMagnifierCallback;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PipMotionHelper$1$$ExternalSyntheticLambda0 implements Choreographer.FrameCallback {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ PipMotionHelper$1$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.Choreographer.FrameCallback
    public final void doFrame(long j) {
        IWindowMagnificationConnectionCallback iWindowMagnificationConnectionCallback;
        switch (this.$r8$classId) {
            case 0:
                ((Runnable) this.f$0).run();
                return;
            default:
                WindowMagnificationController windowMagnificationController = (WindowMagnificationController) this.f$0;
                boolean z = WindowMagnificationController.DEBUG;
                Objects.requireNonNull(windowMagnificationController);
                if (windowMagnificationController.isWindowVisible() && windowMagnificationController.mMirrorSurface != null) {
                    Rect rect = windowMagnificationController.mMagnificationFrame;
                    float f = windowMagnificationController.mScale;
                    int width = rect.width() / 2;
                    int height = rect.height() / 2;
                    int i = width - ((int) (width / f));
                    int i2 = height - ((int) (height / f));
                    windowMagnificationController.mSourceBounds.set(rect.left + i, rect.top + i2, rect.right - i, rect.bottom - i2);
                    windowMagnificationController.mSourceBounds.offset(-windowMagnificationController.mMagnificationFrameOffsetX, -windowMagnificationController.mMagnificationFrameOffsetY);
                    Rect rect2 = windowMagnificationController.mSourceBounds;
                    if (rect2.left < 0) {
                        rect2.offsetTo(0, rect2.top);
                    } else if (rect2.right > windowMagnificationController.mWindowBounds.width()) {
                        windowMagnificationController.mSourceBounds.offsetTo(windowMagnificationController.mWindowBounds.width() - windowMagnificationController.mSourceBounds.width(), windowMagnificationController.mSourceBounds.top);
                    }
                    Rect rect3 = windowMagnificationController.mSourceBounds;
                    if (rect3.top < 0) {
                        rect3.offsetTo(rect3.left, 0);
                    } else if (rect3.bottom > windowMagnificationController.mWindowBounds.height()) {
                        Rect rect4 = windowMagnificationController.mSourceBounds;
                        rect4.offsetTo(rect4.left, windowMagnificationController.mWindowBounds.height() - windowMagnificationController.mSourceBounds.height());
                    }
                    windowMagnificationController.mTmpRect.set(0, 0, windowMagnificationController.mMagnificationFrame.width(), windowMagnificationController.mMagnificationFrame.height());
                    windowMagnificationController.mTransaction.setGeometry(windowMagnificationController.mMirrorSurface, windowMagnificationController.mSourceBounds, windowMagnificationController.mTmpRect, 0).apply();
                    WindowMagnificationAnimationController windowMagnificationAnimationController = windowMagnificationController.mAnimationController;
                    Objects.requireNonNull(windowMagnificationAnimationController);
                    if (!windowMagnificationAnimationController.mValueAnimator.isRunning()) {
                        WindowMagnifierCallback windowMagnifierCallback = windowMagnificationController.mWindowMagnifierCallback;
                        int i3 = windowMagnificationController.mDisplayId;
                        Rect rect5 = windowMagnificationController.mSourceBounds;
                        WindowMagnification windowMagnification = (WindowMagnification) windowMagnifierCallback;
                        Objects.requireNonNull(windowMagnification);
                        WindowMagnificationConnectionImpl windowMagnificationConnectionImpl = windowMagnification.mWindowMagnificationConnectionImpl;
                        if (windowMagnificationConnectionImpl != null && (iWindowMagnificationConnectionCallback = windowMagnificationConnectionImpl.mConnectionCallback) != null) {
                            try {
                                iWindowMagnificationConnectionCallback.onSourceBoundsChanged(i3, rect5);
                                return;
                            } catch (RemoteException e) {
                                Log.e("WindowMagnificationConnectionImpl", "Failed to inform source bounds changed", e);
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
        }
    }
}
