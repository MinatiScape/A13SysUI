package com.android.systemui.accessibility;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.view.Display;
import android.view.SurfaceControl;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AccelerateInterpolator;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.graphics.SfVsyncFrameCallbackProvider;
import com.android.systemui.CoreStartable;
import com.android.systemui.model.SysUiState;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda2;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Objects;
/* loaded from: classes.dex */
public class WindowMagnification extends CoreStartable implements WindowMagnifierCallback, CommandQueue.Callbacks {
    public final AccessibilityManager mAccessibilityManager = (AccessibilityManager) this.mContext.getSystemService(AccessibilityManager.class);
    public final CommandQueue mCommandQueue;
    public final Handler mHandler;
    @VisibleForTesting
    public DisplayIdIndexSupplier<WindowMagnificationController> mMagnificationControllerSupplier;
    public final ModeSwitchesController mModeSwitchesController;
    public final OverviewProxyService mOverviewProxyService;
    public SysUiState mSysUiState;
    public WindowMagnificationConnectionImpl mWindowMagnificationConnectionImpl;

    /* loaded from: classes.dex */
    public static class ControllerSupplier extends DisplayIdIndexSupplier<WindowMagnificationController> {
        public final Context mContext;
        public final Handler mHandler;
        public final SysUiState mSysUiState;
        public final WindowMagnifierCallback mWindowMagnifierCallback;

        @Override // com.android.systemui.accessibility.DisplayIdIndexSupplier
        public final WindowMagnificationController createInstance(Display display) {
            Context createWindowContext = this.mContext.createWindowContext(display, 2039, null);
            Handler handler = this.mHandler;
            Resources resources = createWindowContext.getResources();
            ValueAnimator valueAnimator = new ValueAnimator();
            valueAnimator.setDuration(resources.getInteger(17694722));
            valueAnimator.setInterpolator(new AccelerateInterpolator(2.5f));
            valueAnimator.setFloatValues(0.0f, 1.0f);
            return new WindowMagnificationController(createWindowContext, handler, new WindowMagnificationAnimationController(createWindowContext, valueAnimator), new SfVsyncFrameCallbackProvider(), new SurfaceControl.Transaction(), this.mWindowMagnifierCallback, this.mSysUiState);
        }

        public ControllerSupplier(Context context, Handler handler, WindowMagnifierCallback windowMagnifierCallback, DisplayManager displayManager, SysUiState sysUiState) {
            super(displayManager);
            this.mContext = context;
            this.mHandler = handler;
            this.mWindowMagnifierCallback = windowMagnifierCallback;
            this.mSysUiState = sysUiState;
        }
    }

    @Override // com.android.systemui.CoreStartable, com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("WindowMagnification");
        DisplayIdIndexSupplier<WindowMagnificationController> displayIdIndexSupplier = this.mMagnificationControllerSupplier;
        WMShell$$ExternalSyntheticLambda2 wMShell$$ExternalSyntheticLambda2 = new WMShell$$ExternalSyntheticLambda2(printWriter, 1);
        Objects.requireNonNull(displayIdIndexSupplier);
        for (int i = 0; i < displayIdIndexSupplier.mSparseArray.size(); i++) {
            wMShell$$ExternalSyntheticLambda2.accept(displayIdIndexSupplier.mSparseArray.valueAt(i));
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void requestWindowMagnificationConnection(boolean z) {
        if (z) {
            if (this.mWindowMagnificationConnectionImpl == null) {
                this.mWindowMagnificationConnectionImpl = new WindowMagnificationConnectionImpl(this, this.mHandler, this.mModeSwitchesController);
            }
            ModeSwitchesController modeSwitchesController = this.mModeSwitchesController;
            WindowMagnificationConnectionImpl windowMagnificationConnectionImpl = this.mWindowMagnificationConnectionImpl;
            Objects.requireNonNull(windowMagnificationConnectionImpl);
            WindowMagnification$$ExternalSyntheticLambda0 windowMagnification$$ExternalSyntheticLambda0 = new WindowMagnification$$ExternalSyntheticLambda0(windowMagnificationConnectionImpl);
            Objects.requireNonNull(modeSwitchesController);
            modeSwitchesController.mSwitchListenerDelegate = windowMagnification$$ExternalSyntheticLambda0;
            this.mAccessibilityManager.setWindowMagnificationConnection(this.mWindowMagnificationConnectionImpl);
            return;
        }
        this.mAccessibilityManager.setWindowMagnificationConnection(null);
        ModeSwitchesController modeSwitchesController2 = this.mModeSwitchesController;
        Objects.requireNonNull(modeSwitchesController2);
        modeSwitchesController2.mSwitchListenerDelegate = null;
    }

    @Override // com.android.systemui.CoreStartable
    public final void start() {
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
        this.mOverviewProxyService.addCallback(new OverviewProxyService.OverviewProxyListener() { // from class: com.android.systemui.accessibility.WindowMagnification.1
            @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
            public final void onConnectionChanged(boolean z) {
                if (z) {
                    WindowMagnification windowMagnification = WindowMagnification.this;
                    Objects.requireNonNull(windowMagnification);
                    DisplayIdIndexSupplier<WindowMagnificationController> displayIdIndexSupplier = windowMagnification.mMagnificationControllerSupplier;
                    Objects.requireNonNull(displayIdIndexSupplier);
                    WindowMagnificationController windowMagnificationController = displayIdIndexSupplier.mSparseArray.get(0);
                    if (windowMagnificationController != null) {
                        windowMagnificationController.updateSysUIState(true);
                        return;
                    }
                    SysUiState sysUiState = windowMagnification.mSysUiState;
                    sysUiState.setFlag(524288, false);
                    sysUiState.commitUpdate(0);
                }
            }
        });
    }

    public WindowMagnification(Context context, Handler handler, CommandQueue commandQueue, ModeSwitchesController modeSwitchesController, SysUiState sysUiState, OverviewProxyService overviewProxyService) {
        super(context);
        this.mHandler = handler;
        this.mCommandQueue = commandQueue;
        this.mModeSwitchesController = modeSwitchesController;
        this.mSysUiState = sysUiState;
        this.mOverviewProxyService = overviewProxyService;
        this.mMagnificationControllerSupplier = new ControllerSupplier(context, handler, this, (DisplayManager) context.getSystemService(DisplayManager.class), sysUiState);
    }
}
