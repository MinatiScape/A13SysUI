package com.android.wm.shell.common;

import android.os.RemoteException;
import android.util.Slog;
import android.view.IDisplayWindowRotationCallback;
import android.view.IDisplayWindowRotationController;
import android.view.IWindowManager;
import android.window.WindowContainerTransaction;
import com.android.wm.shell.common.DisplayChangeController;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes.dex */
public final class DisplayChangeController {
    public final DisplayWindowRotationControllerImpl mControllerImpl;
    public final ShellExecutor mMainExecutor;
    public final CopyOnWriteArrayList<OnDisplayChangingListener> mRotationListener = new CopyOnWriteArrayList<>();

    /* loaded from: classes.dex */
    public class DisplayWindowRotationControllerImpl extends IDisplayWindowRotationController.Stub {
        public DisplayWindowRotationControllerImpl() {
        }

        public final void onRotateDisplay(final int i, final int i2, final int i3, final IDisplayWindowRotationCallback iDisplayWindowRotationCallback) {
            DisplayChangeController.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.common.DisplayChangeController$DisplayWindowRotationControllerImpl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    DisplayChangeController.DisplayWindowRotationControllerImpl displayWindowRotationControllerImpl = DisplayChangeController.DisplayWindowRotationControllerImpl.this;
                    int i4 = i;
                    int i5 = i2;
                    int i6 = i3;
                    IDisplayWindowRotationCallback iDisplayWindowRotationCallback2 = iDisplayWindowRotationCallback;
                    Objects.requireNonNull(displayWindowRotationControllerImpl);
                    DisplayChangeController displayChangeController = DisplayChangeController.this;
                    Objects.requireNonNull(displayChangeController);
                    WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
                    Iterator<DisplayChangeController.OnDisplayChangingListener> it = displayChangeController.mRotationListener.iterator();
                    while (it.hasNext()) {
                        it.next().onRotateDisplay(i4, i5, i6, windowContainerTransaction);
                    }
                    try {
                        iDisplayWindowRotationCallback2.continueRotateDisplay(i6, windowContainerTransaction);
                    } catch (RemoteException e) {
                        Slog.e("DisplayChangeController", "Failed to continue rotation", e);
                    }
                }
            });
        }
    }

    /* loaded from: classes.dex */
    public interface OnDisplayChangingListener {
        void onRotateDisplay(int i, int i2, int i3, WindowContainerTransaction windowContainerTransaction);
    }

    public DisplayChangeController(IWindowManager iWindowManager, ShellExecutor shellExecutor) {
        this.mMainExecutor = shellExecutor;
        DisplayWindowRotationControllerImpl displayWindowRotationControllerImpl = new DisplayWindowRotationControllerImpl();
        this.mControllerImpl = displayWindowRotationControllerImpl;
        try {
            iWindowManager.setDisplayWindowRotationController(displayWindowRotationControllerImpl);
        } catch (RemoteException unused) {
            throw new RuntimeException("Unable to register rotation controller");
        }
    }
}
