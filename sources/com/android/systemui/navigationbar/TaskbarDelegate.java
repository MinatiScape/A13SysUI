package com.android.systemui.navigationbar;

import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.hardware.display.DisplayManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.InsetsState;
import android.view.InsetsVisibilities;
import android.widget.Toast;
import androidx.leanback.R$color;
import com.android.internal.view.AppearanceRegion;
import com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda1;
import com.android.systemui.Dependency;
import com.android.systemui.Dumpable;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavBarHelper;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.shared.recents.IOverviewProxy;
import com.android.systemui.shared.recents.utilities.Utilities;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.statusbar.AutoHideUiElement;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda30;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda31;
import com.android.systemui.util.condition.Monitor$$ExternalSyntheticLambda2;
import com.android.wifitrackerlib.WifiEntry$$ExternalSyntheticLambda2;
import com.android.wm.shell.back.BackAnimation;
import com.android.wm.shell.pip.Pip;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public final class TaskbarDelegate implements CommandQueue.Callbacks, OverviewProxyService.OverviewProxyListener, NavigationModeController.ModeChangedListener, ComponentCallbacks, Dumpable {
    public static final /* synthetic */ int $r8$clinit = 0;
    public AutoHideController mAutoHideController;
    public BackAnimation mBackAnimation;
    public int mBehavior;
    public CommandQueue mCommandQueue;
    public final Context mContext;
    public int mDisabledFlags;
    public int mDisplayId;
    public final DisplayManager mDisplayManager;
    public final EdgeBackGestureHandler mEdgeBackGestureHandler;
    public boolean mInitialized;
    public LightBarController mLightBarController;
    public AnonymousClass4 mLightBarTransitionsController;
    public NavBarHelper mNavBarHelper;
    public final NavigationBarOverlayController mNavBarOverlayController;
    public final StatusBar$$ExternalSyntheticLambda30 mNavbarOverlayVisibilityChangeCallback;
    public int mNavigationIconHints;
    public int mNavigationMode;
    public NavigationModeController mNavigationModeController;
    public OverviewProxyService mOverviewProxyService;
    public final Monitor$$ExternalSyntheticLambda2 mPipListener;
    public Optional<Pip> mPipOptional;
    public ScreenPinningNotify mScreenPinningNotify;
    public SysUiState mSysUiState;
    public boolean mTaskbarTransientShowing;
    public Context mWindowContext;
    public final AnonymousClass1 mNavbarTaskbarStateUpdater = new AnonymousClass1();
    public int mTaskBarWindowState = 0;
    public final AnonymousClass2 mAutoHideUiElement = new AutoHideUiElement() { // from class: com.android.systemui.navigationbar.TaskbarDelegate.2
        @Override // com.android.systemui.statusbar.AutoHideUiElement
        public final void synchronizeState() {
        }

        @Override // com.android.systemui.statusbar.AutoHideUiElement
        public final void hide() {
            TaskbarDelegate taskbarDelegate = TaskbarDelegate.this;
            Objects.requireNonNull(taskbarDelegate);
            if (taskbarDelegate.mTaskbarTransientShowing) {
                taskbarDelegate.mTaskbarTransientShowing = false;
                taskbarDelegate.onTransientStateChanged$1();
            }
        }

        @Override // com.android.systemui.statusbar.AutoHideUiElement
        public final boolean isVisible() {
            return TaskbarDelegate.this.mTaskbarTransientShowing;
        }
    };

    /* renamed from: com.android.systemui.navigationbar.TaskbarDelegate$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements NavBarHelper.NavbarTaskbarStateUpdater {
        public AnonymousClass1() {
        }

        @Override // com.android.systemui.navigationbar.NavBarHelper.NavbarTaskbarStateUpdater
        public final void updateAccessibilityServicesState() {
            TaskbarDelegate.this.updateSysuiFlags();
        }

        @Override // com.android.systemui.navigationbar.NavBarHelper.NavbarTaskbarStateUpdater
        public final void updateAssistantAvailable(boolean z) {
            TaskbarDelegate taskbarDelegate = TaskbarDelegate.this;
            Objects.requireNonNull(taskbarDelegate);
            OverviewProxyService overviewProxyService = taskbarDelegate.mOverviewProxyService;
            Objects.requireNonNull(overviewProxyService);
            if (overviewProxyService.mOverviewProxy != null) {
                try {
                    OverviewProxyService overviewProxyService2 = taskbarDelegate.mOverviewProxyService;
                    Objects.requireNonNull(overviewProxyService2);
                    overviewProxyService2.mOverviewProxy.onAssistantAvailable(z);
                } catch (RemoteException e) {
                    Log.e("TaskbarDelegate", "onAssistantAvailable() failed, available: " + z, e);
                }
            }
        }
    }

    @Override // android.content.ComponentCallbacks
    public final void onLowMemory() {
    }

    @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
    public final void onTaskbarAutohideSuspend(boolean z) {
        Runnable runnable = null;
        if (z) {
            AutoHideController autoHideController = this.mAutoHideController;
            Objects.requireNonNull(autoHideController);
            autoHideController.mHandler.removeCallbacks(autoHideController.mAutoHide);
            if (autoHideController.mStatusBar != null) {
                runnable = new WifiEntry$$ExternalSyntheticLambda2(autoHideController, 6);
            } else if (autoHideController.mNavigationBar != null) {
                runnable = new AccessPoint$$ExternalSyntheticLambda1(autoHideController, 5);
            }
            if (runnable != null) {
                autoHideController.mHandler.removeCallbacks(runnable);
            }
            autoHideController.mAutoHideSuspended = autoHideController.isAnyTransientBarShown();
            return;
        }
        AutoHideController autoHideController2 = this.mAutoHideController;
        Objects.requireNonNull(autoHideController2);
        if (autoHideController2.mAutoHideSuspended) {
            autoHideController2.mAutoHideSuspended = false;
            autoHideController2.mHandler.removeCallbacks(autoHideController2.mAutoHide);
            autoHideController2.mHandler.postDelayed(autoHideController2.mAutoHide, 2250L);
            if (autoHideController2.mStatusBar != null) {
                runnable = new WifiEntry$$ExternalSyntheticLambda2(autoHideController2, 6);
            } else if (autoHideController2.mNavigationBar != null) {
                runnable = new AccessPoint$$ExternalSyntheticLambda1(autoHideController2, 5);
            }
            if (runnable != null) {
                autoHideController2.mHandler.postDelayed(runnable, 500L);
            }
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void abortTransient(int i, int[] iArr) {
        if (i == this.mDisplayId && InsetsState.containsType(iArr, 21) && this.mTaskbarTransientShowing) {
            this.mTaskbarTransientShowing = false;
            onTransientStateChanged$1();
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void disable(int i, int i2, int i3, boolean z) {
        this.mDisabledFlags = i2;
        updateSysuiFlags();
        OverviewProxyService overviewProxyService = this.mOverviewProxyService;
        Objects.requireNonNull(overviewProxyService);
        try {
            IOverviewProxy iOverviewProxy = overviewProxyService.mOverviewProxy;
            if (iOverviewProxy != null) {
                iOverviewProxy.disable(i, i2, i3, z);
            } else {
                Log.e("OverviewProxyService", "Failed to get overview proxy for disable flags.");
            }
        } catch (RemoteException e) {
            Log.e("OverviewProxyService", "Failed to call disable()", e);
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("TaskbarDelegate (displayId=");
        m.append(this.mDisplayId);
        m.append("):");
        printWriter.println(m.toString());
        printWriter.println("  mNavigationIconHints=" + this.mNavigationIconHints);
        printWriter.println("  mNavigationMode=" + this.mNavigationMode);
        printWriter.println("  mDisabledFlags=" + this.mDisabledFlags);
        printWriter.println("  mTaskBarWindowState=" + this.mTaskBarWindowState);
        printWriter.println("  mBehavior=" + this.mBehavior);
        printWriter.println("  mTaskbarTransientShowing=" + this.mTaskbarTransientShowing);
        this.mEdgeBackGestureHandler.dump(printWriter);
    }

    @Override // android.content.ComponentCallbacks
    public final void onConfigurationChanged(Configuration configuration) {
        this.mEdgeBackGestureHandler.onConfigurationChanged(configuration);
    }

    @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
    public final void onNavigationModeChanged(int i) {
        this.mNavigationMode = i;
        EdgeBackGestureHandler edgeBackGestureHandler = this.mEdgeBackGestureHandler;
        Objects.requireNonNull(edgeBackGestureHandler);
        edgeBackGestureHandler.mIsGesturalModeEnabled = R$color.isGesturalMode(i);
        edgeBackGestureHandler.updateIsEnabled();
        edgeBackGestureHandler.updateCurrentUserResources();
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void onRecentsAnimationStateChanged(boolean z) {
        if (z) {
            this.mNavBarOverlayController.setButtonState(false, true);
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void onRotationProposal(int i, boolean z) {
        OverviewProxyService overviewProxyService = this.mOverviewProxyService;
        Objects.requireNonNull(overviewProxyService);
        try {
            IOverviewProxy iOverviewProxy = overviewProxyService.mOverviewProxy;
            if (iOverviewProxy != null) {
                iOverviewProxy.onRotationProposal(i, z);
            } else {
                Log.e("OverviewProxyService", "Failed to get overview proxy for proposing rotation.");
            }
        } catch (RemoteException e) {
            Log.e("OverviewProxyService", "Failed to call onRotationProposal()", e);
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void onSystemBarAttributesChanged(int i, int i2, AppearanceRegion[] appearanceRegionArr, boolean z, int i3, InsetsVisibilities insetsVisibilities, String str) {
        OverviewProxyService overviewProxyService = this.mOverviewProxyService;
        Objects.requireNonNull(overviewProxyService);
        try {
            IOverviewProxy iOverviewProxy = overviewProxyService.mOverviewProxy;
            if (iOverviewProxy != null) {
                iOverviewProxy.onSystemBarAttributesChanged(i, i3);
            } else {
                Log.e("OverviewProxyService", "Failed to get overview proxy for system bar attr change.");
            }
        } catch (RemoteException e) {
            Log.e("OverviewProxyService", "Failed to call onSystemBarAttributesChanged()", e);
        }
        LightBarController lightBarController = this.mLightBarController;
        if (lightBarController != null && i == this.mDisplayId) {
            lightBarController.onNavigationBarAppearanceChanged(i2, false, 0, z);
        }
        if (this.mBehavior != i3) {
            this.mBehavior = i3;
            updateSysuiFlags();
        }
    }

    public final void onTransientStateChanged$1() {
        EdgeBackGestureHandler edgeBackGestureHandler = this.mEdgeBackGestureHandler;
        boolean z = this.mTaskbarTransientShowing;
        Objects.requireNonNull(edgeBackGestureHandler);
        edgeBackGestureHandler.mIsNavBarShownTransiently = z;
        if (this.mNavBarOverlayController.isNavigationBarOverlayEnabled()) {
            this.mNavBarOverlayController.setButtonState(this.mTaskbarTransientShowing, false);
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void setImeWindowStatus(int i, IBinder iBinder, int i2, int i3, boolean z) {
        boolean isImeShown = this.mNavBarHelper.isImeShown(i2);
        boolean z2 = true;
        if (!isImeShown) {
            if ((i2 & 8) != 0) {
                isImeShown = true;
            } else {
                isImeShown = false;
            }
        }
        if (!isImeShown || !z) {
            z2 = false;
        }
        int calculateBackDispositionHints = Utilities.calculateBackDispositionHints(this.mNavigationIconHints, i3, isImeShown, z2);
        if (calculateBackDispositionHints != this.mNavigationIconHints) {
            this.mNavigationIconHints = calculateBackDispositionHints;
            updateSysuiFlags();
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void setWindowState(int i, int i2, int i3) {
        if (i == this.mDisplayId && i2 == 2 && this.mTaskBarWindowState != i3) {
            this.mTaskBarWindowState = i3;
            updateSysuiFlags();
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void showTransient(int i, int[] iArr, boolean z) {
        if (i == this.mDisplayId && InsetsState.containsType(iArr, 21) && !this.mTaskbarTransientShowing) {
            this.mTaskbarTransientShowing = true;
            onTransientStateChanged$1();
        }
    }

    public final void updateSysuiFlags() {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean z7;
        boolean z8;
        boolean z9;
        NavBarHelper navBarHelper = this.mNavBarHelper;
        Objects.requireNonNull(navBarHelper);
        int i = navBarHelper.mA11yButtonState;
        boolean z10 = false;
        if ((i & 16) != 0) {
            z = true;
        } else {
            z = false;
        }
        if ((i & 32) != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        SysUiState sysUiState = this.mSysUiState;
        sysUiState.setFlag(16, z);
        sysUiState.setFlag(32, z2);
        if ((this.mNavigationIconHints & 1) != 0) {
            z3 = true;
        } else {
            z3 = false;
        }
        sysUiState.setFlag(262144, z3);
        if ((this.mNavigationIconHints & 4) != 0) {
            z4 = true;
        } else {
            z4 = false;
        }
        sysUiState.setFlag(1048576, z4);
        if ((this.mDisabledFlags & 16777216) != 0) {
            z5 = true;
        } else {
            z5 = false;
        }
        sysUiState.setFlag(128, z5);
        if ((this.mDisabledFlags & 2097152) != 0) {
            z6 = true;
        } else {
            z6 = false;
        }
        sysUiState.setFlag(256, z6);
        if ((this.mDisabledFlags & 4194304) != 0) {
            z7 = true;
        } else {
            z7 = false;
        }
        sysUiState.setFlag(4194304, z7);
        if (this.mTaskBarWindowState == 0) {
            z8 = true;
        } else {
            z8 = false;
        }
        sysUiState.setFlag(2, !z8);
        if (this.mBehavior != 2) {
            z9 = true;
        } else {
            z9 = false;
        }
        sysUiState.setFlag(131072, z9);
        Objects.requireNonNull(ActivityManagerWrapper.sInstance);
        sysUiState.setFlag(1, ActivityManagerWrapper.isScreenPinningActive());
        if (this.mBehavior == 2) {
            z10 = true;
        }
        sysUiState.setFlag(16777216, z10);
        sysUiState.commitUpdate(this.mDisplayId);
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.systemui.navigationbar.TaskbarDelegate$2] */
    public TaskbarDelegate(Context context) {
        StatusBar$$ExternalSyntheticLambda30 statusBar$$ExternalSyntheticLambda30 = new StatusBar$$ExternalSyntheticLambda30(this, 1);
        this.mNavbarOverlayVisibilityChangeCallback = statusBar$$ExternalSyntheticLambda30;
        EdgeBackGestureHandler create = ((EdgeBackGestureHandler.Factory) Dependency.get(EdgeBackGestureHandler.Factory.class)).create(context);
        this.mEdgeBackGestureHandler = create;
        NavigationBarOverlayController navigationBarOverlayController = (NavigationBarOverlayController) Dependency.get(NavigationBarOverlayController.class);
        this.mNavBarOverlayController = navigationBarOverlayController;
        if (navigationBarOverlayController.isNavigationBarOverlayEnabled()) {
            navigationBarOverlayController.init(statusBar$$ExternalSyntheticLambda30, new StatusBar$$ExternalSyntheticLambda31(create, 1));
        }
        this.mContext = context;
        this.mDisplayManager = (DisplayManager) context.getSystemService(DisplayManager.class);
        this.mPipListener = new Monitor$$ExternalSyntheticLambda2(create, 2);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void showPinningEnterExitToast(boolean z) {
        updateSysuiFlags();
        ScreenPinningNotify screenPinningNotify = this.mScreenPinningNotify;
        if (screenPinningNotify != null) {
            if (z) {
                Context context = screenPinningNotify.mContext;
                Toast.makeText(context, context.getString(2131953189), 1).show();
                return;
            }
            Context context2 = screenPinningNotify.mContext;
            Toast.makeText(context2, context2.getString(2131953185), 1).show();
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void showPinningEscapeToast() {
        updateSysuiFlags();
        ScreenPinningNotify screenPinningNotify = this.mScreenPinningNotify;
        if (screenPinningNotify != null) {
            screenPinningNotify.showEscapeToast(R$color.isGesturalMode(this.mNavigationMode), !R$color.isGesturalMode(this.mNavigationMode));
        }
    }
}
