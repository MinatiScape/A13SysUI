package com.android.systemui.navigationbar;

import android.animation.ValueAnimator;
import android.app.StatusBarManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.Trace;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline0;
import androidx.leanback.R$color;
import androidx.mediarouter.media.MediaRoute2Provider$$ExternalSyntheticLambda0;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.statusbar.RegisterStatusBarResult;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline0;
import com.android.keyguard.LockIconView$$ExternalSyntheticOutline0;
import com.android.settingslib.applications.InterestingConfigChanges;
import com.android.systemui.Dumpable;
import com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda2;
import com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda3;
import com.android.systemui.navigationbar.NavigationBar;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.navigationbar.TaskbarDelegate;
import com.android.systemui.navigationbar.buttons.ContextualButton;
import com.android.systemui.navigationbar.buttons.ContextualButtonGroup;
import com.android.systemui.navigationbar.buttons.RotationContextButton;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.shared.recents.utilities.Utilities;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.BarTransitions;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.phone.LightBarTransitionsController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.wm.shell.back.BackAnimation;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public final class NavigationBarController implements CommandQueue.Callbacks, ConfigurationController.ConfigurationListener, NavigationModeController.ModeChangedListener, Dumpable {
    public final InterestingConfigChanges mConfigChanges;
    public final Context mContext;
    public final DisplayManager mDisplayManager;
    public final Handler mHandler;
    @VisibleForTesting
    public boolean mIsTablet;
    public int mNavMode;
    public final NavigationBar.Factory mNavigationBarFactory;
    @VisibleForTesting
    public SparseArray<NavigationBar> mNavigationBars = new SparseArray<>();
    public final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    public final TaskbarDelegate mTaskbarDelegate;

    /* JADX WARN: Type inference failed for: r2v4, types: [com.android.systemui.navigationbar.TaskbarDelegate$4] */
    /* JADX WARN: Type inference failed for: r7v9, types: [com.android.systemui.navigationbar.TaskbarDelegate$3] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public NavigationBarController(android.content.Context r10, com.android.systemui.recents.OverviewProxyService r11, com.android.systemui.navigationbar.NavigationModeController r12, com.android.systemui.model.SysUiState r13, com.android.systemui.statusbar.CommandQueue r14, android.os.Handler r15, com.android.systemui.statusbar.policy.ConfigurationController r16, com.android.systemui.navigationbar.NavBarHelper r17, final com.android.systemui.navigationbar.TaskbarDelegate r18, com.android.systemui.navigationbar.NavigationBar.Factory r19, com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager r20, com.android.systemui.dump.DumpManager r21, com.android.systemui.statusbar.phone.AutoHideController r22, com.android.systemui.statusbar.phone.LightBarController r23, java.util.Optional<com.android.wm.shell.pip.Pip> r24, java.util.Optional<com.android.wm.shell.back.BackAnimation> r25) {
        /*
            r9 = this;
            r0 = r9
            r1 = r10
            r2 = r12
            r3 = r14
            r4 = r18
            r5 = r21
            r9.<init>()
            android.util.SparseArray r6 = new android.util.SparseArray
            r6.<init>()
            r0.mNavigationBars = r6
            com.android.settingslib.applications.InterestingConfigChanges r6 = new com.android.settingslib.applications.InterestingConfigChanges
            r7 = 1073742592(0x40000300, float:2.000183)
            r6.<init>(r7)
            r0.mConfigChanges = r6
            r0.mContext = r1
            r7 = r15
            r0.mHandler = r7
            r7 = r19
            r0.mNavigationBarFactory = r7
            java.lang.Class<android.hardware.display.DisplayManager> r7 = android.hardware.display.DisplayManager.class
            java.lang.Object r7 = r10.getSystemService(r7)
            android.hardware.display.DisplayManager r7 = (android.hardware.display.DisplayManager) r7
            r0.mDisplayManager = r7
            r14.addCallback(r9)
            r7 = r16
            r7.addCallback(r9)
            android.content.res.Resources r7 = r10.getResources()
            r6.applyNewConfig(r7)
            int r6 = r12.addListener(r9)
            r0.mNavMode = r6
            r0.mTaskbarDelegate = r4
            r6 = r20
            r0.mStatusBarKeyguardViewManager = r6
            r6 = 0
            r7 = r25
            java.lang.Object r6 = r7.orElse(r6)
            com.android.wm.shell.back.BackAnimation r6 = (com.android.wm.shell.back.BackAnimation) r6
            java.util.Objects.requireNonNull(r18)
            r4.mCommandQueue = r3
            r3 = r11
            r4.mOverviewProxyService = r3
            r3 = r17
            r4.mNavBarHelper = r3
            r4.mNavigationModeController = r2
            r2 = r13
            r4.mSysUiState = r2
            r5.registerDumpable(r4)
            r2 = r22
            r4.mAutoHideController = r2
            r2 = r23
            r4.mLightBarController = r2
            com.android.systemui.navigationbar.TaskbarDelegate$4 r2 = new com.android.systemui.navigationbar.TaskbarDelegate$4
            android.content.Context r3 = r4.mContext
            com.android.systemui.navigationbar.TaskbarDelegate$3 r7 = new com.android.systemui.navigationbar.TaskbarDelegate$3
            r7.<init>()
            com.android.systemui.statusbar.CommandQueue r8 = r4.mCommandQueue
            r2.<init>(r3, r7, r8)
            r4.mLightBarTransitionsController = r2
            r2 = r24
            r4.mPipOptional = r2
            r4.mBackAnimation = r6
            boolean r1 = com.android.systemui.shared.recents.utilities.Utilities.isTablet(r10)
            r0.mIsTablet = r1
            r5.registerDumpable(r9)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.navigationbar.NavigationBarController.<init>(android.content.Context, com.android.systemui.recents.OverviewProxyService, com.android.systemui.navigationbar.NavigationModeController, com.android.systemui.model.SysUiState, com.android.systemui.statusbar.CommandQueue, android.os.Handler, com.android.systemui.statusbar.policy.ConfigurationController, com.android.systemui.navigationbar.NavBarHelper, com.android.systemui.navigationbar.TaskbarDelegate, com.android.systemui.navigationbar.NavigationBar$Factory, com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager, com.android.systemui.dump.DumpManager, com.android.systemui.statusbar.phone.AutoHideController, com.android.systemui.statusbar.phone.LightBarController, java.util.Optional, java.util.Optional):void");
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        boolean z;
        String str;
        String str2;
        String str3;
        ContextualButton contextualButton;
        boolean z2;
        boolean z3;
        for (int i = 0; i < this.mNavigationBars.size(); i++) {
            if (i > 0) {
                printWriter.println();
            }
            NavigationBar valueAt = this.mNavigationBars.valueAt(i);
            Objects.requireNonNull(valueAt);
            printWriter.println("NavigationBar (displayId=" + valueAt.mDisplayId + "):");
            StringBuilder sb = new StringBuilder();
            sb.append("  mStartingQuickSwitchRotation=");
            sb.append(valueAt.mStartingQuickSwitchRotation);
            printWriter.println(sb.toString());
            printWriter.println("  mCurrentRotation=" + valueAt.mCurrentRotation);
            printWriter.println("  mHomeButtonLongPressDurationMs=" + valueAt.mHomeButtonLongPressDurationMs);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("  mLongPressHomeEnabled=");
            StringBuilder m = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(sb2, valueAt.mLongPressHomeEnabled, printWriter, "  mNavigationBarWindowState=");
            m.append(StatusBarManager.windowStateToString(valueAt.mNavigationBarWindowState));
            printWriter.println(m.toString());
            printWriter.println("  mNavigationBarMode=" + BarTransitions.modeToString(valueAt.mNavigationBarMode));
            StringBuilder sb3 = new StringBuilder();
            sb3.append("  mTransientShown=");
            StringBuilder m2 = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(sb3, valueAt.mTransientShown, printWriter, "  mTransientShownFromGestureOnSystemBar=");
            m2.append(valueAt.mTransientShownFromGestureOnSystemBar);
            printWriter.println(m2.toString());
            NavigationBarView navigationBarView = valueAt.mNavigationBarView;
            Objects.requireNonNull(navigationBarView);
            StatusBar.dumpBarTransitions(printWriter, "mNavigationBarView", navigationBarView.mBarTransitions);
            NavigationBarView navigationBarView2 = valueAt.mNavigationBarView;
            Objects.requireNonNull(navigationBarView2);
            Rect rect = new Rect();
            Point point = new Point();
            navigationBarView2.getContext().getDisplay().getRealSize(point);
            printWriter.println("NavigationBarView:");
            printWriter.println(String.format("      this: " + StatusBar.viewInfo(navigationBarView2) + " " + NavigationBarView.visibilityToString(navigationBarView2.getVisibility()), new Object[0]));
            navigationBarView2.getWindowVisibleDisplayFrame(rect);
            if (rect.right > point.x || rect.bottom > point.y) {
                z = true;
            } else {
                z = false;
            }
            StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("      window: ");
            m3.append(rect.toShortString());
            m3.append(" ");
            m3.append(NavigationBarView.visibilityToString(navigationBarView2.getWindowVisibility()));
            if (z) {
                str = " OFFSCREEN!";
            } else {
                str = "";
            }
            m3.append(str);
            printWriter.println(m3.toString());
            Object[] objArr = new Object[5];
            int id = navigationBarView2.mCurrentView.getId();
            if (id != 0) {
                try {
                    str2 = navigationBarView2.getContext().getResources().getResourceName(id);
                } catch (Resources.NotFoundException unused) {
                    str2 = "(unknown)";
                }
            } else {
                str2 = "(null)";
            }
            objArr[0] = str2;
            objArr[1] = Integer.valueOf(navigationBarView2.mCurrentView.getWidth());
            objArr[2] = Integer.valueOf(navigationBarView2.mCurrentView.getHeight());
            objArr[3] = NavigationBarView.visibilityToString(navigationBarView2.mCurrentView.getVisibility());
            objArr[4] = Float.valueOf(navigationBarView2.mCurrentView.getAlpha());
            printWriter.println(String.format("      mCurrentView: id=%s (%dx%d) %s %f", objArr));
            Object[] objArr2 = new Object[3];
            objArr2[0] = Integer.valueOf(navigationBarView2.mDisabledFlags);
            if (navigationBarView2.mIsVertical) {
                str3 = "true";
            } else {
                str3 = "false";
            }
            objArr2[1] = str3;
            LightBarTransitionsController lightTransitionsController = navigationBarView2.getLightTransitionsController();
            Objects.requireNonNull(lightTransitionsController);
            objArr2[2] = Float.valueOf(lightTransitionsController.mDarkIntensity);
            printWriter.println(String.format("      disabled=0x%08x vertical=%s darkIntensity=%.2f", objArr2));
            printWriter.println("      mOrientedHandleSamplingRegion: " + navigationBarView2.mOrientedHandleSamplingRegion);
            printWriter.println("    mScreenOn: " + navigationBarView2.mScreenOn);
            NavigationBarView.dumpButton(printWriter, "back", navigationBarView2.getBackButton());
            NavigationBarView.dumpButton(printWriter, "home", navigationBarView2.getHomeButton());
            NavigationBarView.dumpButton(printWriter, "handle", navigationBarView2.mButtonDispatchers.get(2131428097));
            NavigationBarView.dumpButton(printWriter, "rcnt", navigationBarView2.getRecentsButton());
            NavigationBarView.dumpButton(printWriter, "rota", (RotationContextButton) navigationBarView2.mButtonDispatchers.get(2131428715));
            NavigationBarView.dumpButton(printWriter, "a11y", navigationBarView2.mButtonDispatchers.get(2131427368));
            NavigationBarView.dumpButton(printWriter, "ime", navigationBarView2.mButtonDispatchers.get(2131428119));
            NavigationBarInflaterView navigationBarInflaterView = navigationBarView2.mNavigationInflaterView;
            if (navigationBarInflaterView != null) {
                StringBuilder m4 = LockIconView$$ExternalSyntheticOutline0.m(printWriter, "NavigationBarInflaterView", "  mCurrentLayout: ");
                m4.append(navigationBarInflaterView.mCurrentLayout);
                printWriter.println(m4.toString());
            }
            NavigationBarTransitions navigationBarTransitions = navigationBarView2.mBarTransitions;
            Objects.requireNonNull(navigationBarTransitions);
            printWriter.println("NavigationBarTransitions:");
            printWriter.println("  mMode: " + navigationBarTransitions.mMode);
            printWriter.println("  mAlwaysOpaque: false");
            StringBuilder sb4 = new StringBuilder();
            sb4.append("  mAllowAutoDimWallpaperNotVisible: ");
            StringBuilder m5 = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(sb4, navigationBarTransitions.mAllowAutoDimWallpaperNotVisible, printWriter, "  mWallpaperVisible: "), navigationBarTransitions.mWallpaperVisible, printWriter, "  mLightsOut: "), navigationBarTransitions.mLightsOut, printWriter, "  mAutoDim: "), navigationBarTransitions.mAutoDim, printWriter, "  bg overrideAlpha: ");
            BarTransitions.BarBackgroundDrawable barBackgroundDrawable = navigationBarTransitions.mBarBackground;
            Objects.requireNonNull(barBackgroundDrawable);
            m5.append(barBackgroundDrawable.mOverrideAlpha);
            printWriter.println(m5.toString());
            StringBuilder sb5 = new StringBuilder();
            sb5.append("  bg color: ");
            BarTransitions.BarBackgroundDrawable barBackgroundDrawable2 = navigationBarTransitions.mBarBackground;
            Objects.requireNonNull(barBackgroundDrawable2);
            sb5.append(barBackgroundDrawable2.mColor);
            printWriter.println(sb5.toString());
            StringBuilder sb6 = new StringBuilder();
            sb6.append("  bg frame: ");
            BarTransitions.BarBackgroundDrawable barBackgroundDrawable3 = navigationBarTransitions.mBarBackground;
            Objects.requireNonNull(barBackgroundDrawable3);
            sb6.append(barBackgroundDrawable3.mFrame);
            printWriter.println(sb6.toString());
            ContextualButtonGroup contextualButtonGroup = navigationBarView2.mContextualButtonGroup;
            Objects.requireNonNull(contextualButtonGroup);
            View view = contextualButtonGroup.mCurrentView;
            StringBuilder m6 = LockIconView$$ExternalSyntheticOutline0.m(printWriter, "ContextualButtonGroup", "  getVisibleContextButton(): ");
            int size = contextualButtonGroup.mButtonData.size();
            while (true) {
                size--;
                if (size >= 0) {
                    if (((ContextualButtonGroup.ButtonData) contextualButtonGroup.mButtonData.get(size)).markedVisible) {
                        contextualButton = ((ContextualButtonGroup.ButtonData) contextualButtonGroup.mButtonData.get(size)).button;
                        break;
                    }
                } else {
                    contextualButton = null;
                    break;
                }
            }
            m6.append(contextualButton);
            printWriter.println(m6.toString());
            printWriter.println("  isVisible(): " + contextualButtonGroup.isVisible());
            StringBuilder sb7 = new StringBuilder();
            sb7.append("  attached(): ");
            if (view == null || !view.isAttachedToWindow()) {
                z2 = false;
            } else {
                z2 = true;
            }
            sb7.append(z2);
            printWriter.println(sb7.toString());
            printWriter.println("  mButtonData [ ");
            for (int size2 = contextualButtonGroup.mButtonData.size() - 1; size2 >= 0; size2--) {
                ContextualButtonGroup.ButtonData buttonData = (ContextualButtonGroup.ButtonData) contextualButtonGroup.mButtonData.get(size2);
                ContextualButton contextualButton2 = buttonData.button;
                Objects.requireNonNull(contextualButton2);
                View view2 = contextualButton2.mCurrentView;
                StringBuilder m7 = ExifInterface$$ExternalSyntheticOutline0.m("    ", size2, ": markedVisible=");
                m7.append(buttonData.markedVisible);
                m7.append(" visible=");
                m7.append(buttonData.button.getVisibility());
                m7.append(" attached=");
                if (view2 == null || !view2.isAttachedToWindow()) {
                    z3 = false;
                } else {
                    z3 = true;
                }
                m7.append(z3);
                m7.append(" alpha=");
                m7.append(buttonData.button.getAlpha());
                printWriter.println(m7.toString());
            }
            printWriter.println("  ]");
            navigationBarView2.mRegionSamplingHelper.dump(printWriter);
            navigationBarView2.mEdgeBackGestureHandler.dump(printWriter);
        }
    }

    @VisibleForTesting
    public void createNavigationBar(final Display display, Bundle bundle, final RegisterStatusBarResult registerStatusBarResult) {
        boolean z;
        Context context;
        int i;
        boolean z2;
        boolean z3;
        if (display != null) {
            int displayId = display.getDisplayId();
            if (displayId == 0) {
                z = true;
            } else {
                z = false;
            }
            if (!this.mIsTablet || !z) {
                try {
                    if (WindowManagerGlobal.getWindowManagerService().hasNavigationBar(displayId)) {
                        if (z) {
                            context = this.mContext;
                        } else {
                            context = this.mContext.createDisplayContext(display);
                        }
                        NavigationBar.Factory factory = this.mNavigationBarFactory;
                        Objects.requireNonNull(factory);
                        final NavigationBar navigationBar = new NavigationBar(context, (WindowManager) context.getSystemService(WindowManager.class), factory.mAssistManagerLazy, factory.mAccessibilityManager, factory.mDeviceProvisionedController, factory.mMetricsLogger, factory.mOverviewProxyService, factory.mNavigationModeController, factory.mStatusBarStateController, factory.mSysUiFlagsContainer, factory.mBroadcastDispatcher, factory.mCommandQueue, factory.mPipOptional, factory.mSplitScreenOptional, factory.mRecentsOptional, factory.mStatusBarOptionalLazy, factory.mShadeController, factory.mNotificationRemoteInputManager, factory.mNotificationShadeDepthController, factory.mMainHandler, factory.mNavbarOverlayController, factory.mUiEventLogger, factory.mNavBarHelper, factory.mMainLightBarController, factory.mLightBarControllerFactory, factory.mMainAutoHideController, factory.mAutoHideControllerFactory, factory.mTelecomManagerOptional, factory.mInputMethodManager, factory.mBackAnimation);
                        this.mNavigationBars.put(displayId, navigationBar);
                        boolean isNavBarVisible = this.mStatusBarKeyguardViewManager.isNavBarVisible();
                        NavigationBarFrame navigationBarFrame = (NavigationBarFrame) LayoutInflater.from(navigationBar.mContext).inflate(2131624317, (ViewGroup) null);
                        navigationBar.mFrame = navigationBarFrame;
                        View inflate = LayoutInflater.from(navigationBarFrame.getContext()).inflate(2131624316, navigationBar.mFrame);
                        inflate.addOnAttachStateChangeListener(navigationBar);
                        NavigationBarView navigationBarView = (NavigationBarView) inflate.findViewById(2131428486);
                        navigationBar.mNavigationBarView = navigationBarView;
                        if (isNavBarVisible) {
                            i = 0;
                        } else {
                            i = 4;
                        }
                        navigationBarView.setVisibility(i);
                        WindowManager windowManager = navigationBar.mWindowManager;
                        NavigationBarFrame navigationBarFrame2 = navigationBar.mFrame;
                        WindowManager.LayoutParams barLayoutParamsForRotation = navigationBar.getBarLayoutParamsForRotation(navigationBar.mContext.getResources().getConfiguration().windowConfiguration.getRotation());
                        barLayoutParamsForRotation.paramsForRotation = new WindowManager.LayoutParams[4];
                        for (int i2 = 0; i2 <= 3; i2++) {
                            barLayoutParamsForRotation.paramsForRotation[i2] = navigationBar.getBarLayoutParamsForRotation(i2);
                        }
                        windowManager.addView(navigationBarFrame2, barLayoutParamsForRotation);
                        int displayId2 = navigationBar.mContext.getDisplayId();
                        navigationBar.mDisplayId = displayId2;
                        if (displayId2 == 0) {
                            z2 = true;
                        } else {
                            z2 = false;
                        }
                        navigationBar.mIsOnDefaultDisplay = z2;
                        navigationBar.mCommandQueue.addCallback((CommandQueue.Callbacks) navigationBar);
                        NavBarHelper navBarHelper = navigationBar.mNavBarHelper;
                        Objects.requireNonNull(navBarHelper);
                        navigationBar.mLongPressHomeEnabled = navBarHelper.mLongPressHomeEnabled;
                        navigationBar.mContext.getContentResolver();
                        navigationBar.mNavBarHelper.init();
                        navigationBar.mAllowForceNavBarHandleOpaque = navigationBar.mContext.getResources().getBoolean(2131034114);
                        navigationBar.mForceNavBarHandleOpaque = DeviceConfig.getBoolean("systemui", "nav_bar_handle_force_opaque", true);
                        navigationBar.mHomeButtonLongPressDurationMs = Optional.of(Long.valueOf(DeviceConfig.getLong("systemui", "home_button_long_press_duration_ms", 0L))).filter(NavigationBar$$ExternalSyntheticLambda18.INSTANCE);
                        Handler handler = navigationBar.mHandler;
                        Objects.requireNonNull(handler);
                        DeviceConfig.addOnPropertiesChangedListener("systemui", new MediaRoute2Provider$$ExternalSyntheticLambda0(handler), navigationBar.mOnPropertiesChangedListener);
                        if (bundle != null) {
                            z3 = false;
                            navigationBar.mDisabledFlags1 = bundle.getInt("disabled_state", 0);
                            navigationBar.mDisabledFlags2 = bundle.getInt("disabled2_state", 0);
                            navigationBar.mAppearance = bundle.getInt("appearance", 0);
                            navigationBar.mBehavior = bundle.getInt("behavior", 0);
                            navigationBar.mTransientShown = bundle.getBoolean("transient_state", false);
                        } else {
                            z3 = false;
                        }
                        navigationBar.mSavedState = bundle;
                        navigationBar.mCommandQueue.recomputeDisableFlags(navigationBar.mDisplayId, z3);
                        navigationBar.mIsCurrentUserSetup = navigationBar.mDeviceProvisionedController.isCurrentUserSetup();
                        navigationBar.mDeviceProvisionedController.addCallback(navigationBar.mUserSetupListener);
                        NotificationShadeDepthController notificationShadeDepthController = navigationBar.mNotificationShadeDepthController;
                        NavigationBar.AnonymousClass7 r2 = navigationBar.mDepthListener;
                        Objects.requireNonNull(notificationShadeDepthController);
                        notificationShadeDepthController.listeners.add(r2);
                        inflate.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.android.systemui.navigationbar.NavigationBarController.1
                            @Override // android.view.View.OnAttachStateChangeListener
                            public final void onViewAttachedToWindow(View view) {
                                if (registerStatusBarResult != null) {
                                    NavigationBar navigationBar2 = navigationBar;
                                    int displayId3 = display.getDisplayId();
                                    RegisterStatusBarResult registerStatusBarResult2 = registerStatusBarResult;
                                    navigationBar2.setImeWindowStatus(displayId3, registerStatusBarResult2.mImeToken, registerStatusBarResult2.mImeWindowVis, registerStatusBarResult2.mImeBackDisposition, registerStatusBarResult2.mShowImeSwitcher);
                                }
                            }

                            @Override // android.view.View.OnAttachStateChangeListener
                            public final void onViewDetachedFromWindow(View view) {
                                view.removeOnAttachStateChangeListener(this);
                            }
                        });
                    }
                } catch (RemoteException unused) {
                    Log.w("NavigationBarController", "Cannot get WindowManager.");
                }
            }
        }
    }

    public final NavigationBarView getNavigationBarView(int i) {
        NavigationBar navigationBar = this.mNavigationBars.get(i);
        if (navigationBar == null) {
            return null;
        }
        return navigationBar.mNavigationBarView;
    }

    public final boolean initializeTaskbarIfNecessary() {
        if (this.mIsTablet) {
            Trace.beginSection("NavigationBarController#initializeTaskbarIfNecessary");
            removeNavigationBar(this.mContext.getDisplayId());
            TaskbarDelegate taskbarDelegate = this.mTaskbarDelegate;
            int displayId = this.mContext.getDisplayId();
            Objects.requireNonNull(taskbarDelegate);
            if (!taskbarDelegate.mInitialized) {
                taskbarDelegate.mDisplayId = displayId;
                taskbarDelegate.mCommandQueue.addCallback((CommandQueue.Callbacks) taskbarDelegate);
                taskbarDelegate.mOverviewProxyService.addCallback((OverviewProxyService.OverviewProxyListener) taskbarDelegate);
                EdgeBackGestureHandler edgeBackGestureHandler = taskbarDelegate.mEdgeBackGestureHandler;
                int addListener = taskbarDelegate.mNavigationModeController.addListener(taskbarDelegate);
                Objects.requireNonNull(edgeBackGestureHandler);
                edgeBackGestureHandler.mIsGesturalModeEnabled = R$color.isGesturalMode(addListener);
                edgeBackGestureHandler.updateIsEnabled();
                edgeBackGestureHandler.updateCurrentUserResources();
                NavBarHelper navBarHelper = taskbarDelegate.mNavBarHelper;
                TaskbarDelegate.AnonymousClass1 r5 = taskbarDelegate.mNavbarTaskbarStateUpdater;
                Objects.requireNonNull(navBarHelper);
                navBarHelper.mA11yEventListeners.add(r5);
                r5.updateAccessibilityServicesState();
                r5.updateAssistantAvailable(navBarHelper.mAssistantAvailable);
                taskbarDelegate.mNavBarHelper.init();
                if (taskbarDelegate.mNavBarOverlayController.isNavigationBarOverlayEnabled()) {
                    taskbarDelegate.mNavBarOverlayController.registerListeners();
                }
                taskbarDelegate.mEdgeBackGestureHandler.onNavBarAttached();
                Context createWindowContext = taskbarDelegate.mContext.createWindowContext(taskbarDelegate.mDisplayManager.getDisplay(displayId), 2, null);
                taskbarDelegate.mWindowContext = createWindowContext;
                createWindowContext.registerComponentCallbacks(taskbarDelegate);
                taskbarDelegate.mScreenPinningNotify = new ScreenPinningNotify(taskbarDelegate.mWindowContext);
                taskbarDelegate.updateSysuiFlags();
                AutoHideController autoHideController = taskbarDelegate.mAutoHideController;
                TaskbarDelegate.AnonymousClass2 r2 = taskbarDelegate.mAutoHideUiElement;
                Objects.requireNonNull(autoHideController);
                autoHideController.mNavigationBar = r2;
                LightBarController lightBarController = taskbarDelegate.mLightBarController;
                TaskbarDelegate.AnonymousClass4 r22 = taskbarDelegate.mLightBarTransitionsController;
                Objects.requireNonNull(lightBarController);
                lightBarController.mNavigationBarController = r22;
                if (r22 != null) {
                    r22.setIconsDark(lightBarController.mNavigationLight, lightBarController.animateChange());
                }
                taskbarDelegate.mPipOptional.ifPresent(new DozeTriggers$$ExternalSyntheticLambda3(taskbarDelegate, 3));
                EdgeBackGestureHandler edgeBackGestureHandler2 = taskbarDelegate.mEdgeBackGestureHandler;
                BackAnimation backAnimation = taskbarDelegate.mBackAnimation;
                Objects.requireNonNull(edgeBackGestureHandler2);
                edgeBackGestureHandler2.mBackAnimation = backAnimation;
                taskbarDelegate.mInitialized = true;
            }
            Trace.endSection();
        } else {
            TaskbarDelegate taskbarDelegate2 = this.mTaskbarDelegate;
            Objects.requireNonNull(taskbarDelegate2);
            if (taskbarDelegate2.mInitialized) {
                taskbarDelegate2.mCommandQueue.removeCallback((CommandQueue.Callbacks) taskbarDelegate2);
                taskbarDelegate2.mOverviewProxyService.removeCallback((OverviewProxyService.OverviewProxyListener) taskbarDelegate2);
                NavigationModeController navigationModeController = taskbarDelegate2.mNavigationModeController;
                Objects.requireNonNull(navigationModeController);
                navigationModeController.mListeners.remove(taskbarDelegate2);
                NavBarHelper navBarHelper2 = taskbarDelegate2.mNavBarHelper;
                TaskbarDelegate.AnonymousClass1 r4 = taskbarDelegate2.mNavbarTaskbarStateUpdater;
                Objects.requireNonNull(navBarHelper2);
                navBarHelper2.mA11yEventListeners.remove(r4);
                NavBarHelper navBarHelper3 = taskbarDelegate2.mNavBarHelper;
                Objects.requireNonNull(navBarHelper3);
                navBarHelper3.mContentResolver.unregisterContentObserver(navBarHelper3.mAssistContentObserver);
                if (taskbarDelegate2.mNavBarOverlayController.isNavigationBarOverlayEnabled()) {
                    taskbarDelegate2.mNavBarOverlayController.unregisterListeners();
                }
                taskbarDelegate2.mEdgeBackGestureHandler.onNavBarDetached();
                taskbarDelegate2.mScreenPinningNotify = null;
                Context context = taskbarDelegate2.mWindowContext;
                if (context != null) {
                    context.unregisterComponentCallbacks(taskbarDelegate2);
                    taskbarDelegate2.mWindowContext = null;
                }
                AutoHideController autoHideController2 = taskbarDelegate2.mAutoHideController;
                Objects.requireNonNull(autoHideController2);
                autoHideController2.mNavigationBar = null;
                TaskbarDelegate.AnonymousClass4 r3 = taskbarDelegate2.mLightBarTransitionsController;
                Objects.requireNonNull(r3);
                r3.mCommandQueue.removeCallback((CommandQueue.Callbacks) r3);
                r3.mStatusBarStateController.removeCallback(r3);
                LightBarController lightBarController2 = taskbarDelegate2.mLightBarController;
                Objects.requireNonNull(lightBarController2);
                lightBarController2.mNavigationBarController = null;
                taskbarDelegate2.mPipOptional.ifPresent(new DozeTriggers$$ExternalSyntheticLambda2(taskbarDelegate2, 2));
                taskbarDelegate2.mInitialized = false;
            }
        }
        return this.mIsTablet;
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public final void onConfigChanged(Configuration configuration) {
        boolean z;
        boolean z2;
        float f;
        boolean z3 = this.mIsTablet;
        boolean isTablet = Utilities.isTablet(this.mContext);
        this.mIsTablet = isTablet;
        if (isTablet != z3) {
            z = true;
        } else {
            z = false;
        }
        if (z && updateNavbarForTaskbar()) {
            return;
        }
        if (this.mConfigChanges.applyNewConfig(this.mContext.getResources())) {
            for (int i = 0; i < this.mNavigationBars.size(); i++) {
                int keyAt = this.mNavigationBars.keyAt(i);
                Bundle bundle = new Bundle();
                NavigationBar navigationBar = this.mNavigationBars.get(keyAt);
                if (navigationBar != null) {
                    bundle.putInt("disabled_state", navigationBar.mDisabledFlags1);
                    bundle.putInt("disabled2_state", navigationBar.mDisabledFlags2);
                    bundle.putInt("appearance", navigationBar.mAppearance);
                    bundle.putInt("behavior", navigationBar.mBehavior);
                    bundle.putBoolean("transient_state", navigationBar.mTransientShown);
                    LightBarTransitionsController lightTransitionsController = navigationBar.mNavigationBarView.getLightTransitionsController();
                    Objects.requireNonNull(lightTransitionsController);
                    ValueAnimator valueAnimator = lightTransitionsController.mTintAnimator;
                    if (valueAnimator == null || !valueAnimator.isRunning()) {
                        f = lightTransitionsController.mDarkIntensity;
                    } else {
                        f = lightTransitionsController.mNextDarkIntensity;
                    }
                    bundle.putFloat("dark_intensity", f);
                }
                removeNavigationBar(keyAt);
                createNavigationBar(this.mDisplayManager.getDisplay(keyAt), bundle, null);
            }
            return;
        }
        for (int i2 = 0; i2 < this.mNavigationBars.size(); i2++) {
            NavigationBar valueAt = this.mNavigationBars.valueAt(i2);
            Objects.requireNonNull(valueAt);
            int rotation = configuration.windowConfiguration.getRotation();
            Locale locale = valueAt.mContext.getResources().getConfiguration().locale;
            int layoutDirectionFromLocale = TextUtils.getLayoutDirectionFromLocale(locale);
            if (!locale.equals(valueAt.mLocale) || layoutDirectionFromLocale != valueAt.mLayoutDirection) {
                valueAt.mLocale = locale;
                valueAt.mLayoutDirection = layoutDirectionFromLocale;
                valueAt.mNavigationBarView.setLayoutDirection(layoutDirectionFromLocale);
            }
            NavigationBarView navigationBarView = valueAt.mNavigationBarView;
            if (navigationBarView != null && navigationBarView.isAttachedToWindow()) {
                valueAt.prepareNavigationBarView();
                WindowManager windowManager = valueAt.mWindowManager;
                NavigationBarFrame navigationBarFrame = valueAt.mFrame;
                WindowManager.LayoutParams barLayoutParamsForRotation = valueAt.getBarLayoutParamsForRotation(rotation);
                barLayoutParamsForRotation.paramsForRotation = new WindowManager.LayoutParams[4];
                for (int i3 = 0; i3 <= 3; i3++) {
                    barLayoutParamsForRotation.paramsForRotation[i3] = valueAt.getBarLayoutParamsForRotation(i3);
                }
                windowManager.updateViewLayout(navigationBarFrame, barLayoutParamsForRotation);
            }
            if (valueAt.mNavBarMode != 2 || valueAt.mOrientationHandle == null) {
                z2 = false;
            } else {
                z2 = true;
            }
            if (z2 && rotation != valueAt.mCurrentRotation) {
                valueAt.mCurrentRotation = rotation;
                valueAt.orientSecondaryHomeHandle();
            }
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void onDisplayReady(int i) {
        Display display = this.mDisplayManager.getDisplay(i);
        this.mIsTablet = Utilities.isTablet(this.mContext);
        createNavigationBar(display, null, null);
    }

    @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
    public final void onNavigationModeChanged(int i) {
        final int i2 = this.mNavMode;
        if (i2 != i) {
            this.mNavMode = i;
            updateAccessibilityButtonModeIfNeeded();
            this.mHandler.post(new Runnable() { // from class: com.android.systemui.navigationbar.NavigationBarController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    NavigationBarController navigationBarController = NavigationBarController.this;
                    int i3 = i2;
                    Objects.requireNonNull(navigationBarController);
                    if (i3 != navigationBarController.mNavMode) {
                        navigationBarController.updateNavbarForTaskbar();
                    }
                    for (int i4 = 0; i4 < navigationBarController.mNavigationBars.size(); i4++) {
                        NavigationBar valueAt = navigationBarController.mNavigationBars.valueAt(i4);
                        if (valueAt != null) {
                            valueAt.mNavigationBarView.updateStates();
                        }
                    }
                }
            });
        }
    }

    public final void removeNavigationBar(int i) {
        NavigationBar navigationBar = this.mNavigationBars.get(i);
        if (navigationBar != null) {
            navigationBar.mAutoHideController = null;
            NavigationBarView navigationBarView = navigationBar.mNavigationBarView;
            Objects.requireNonNull(navigationBarView);
            navigationBarView.mAutoHideController = null;
            navigationBar.mCommandQueue.removeCallback((CommandQueue.Callbacks) navigationBar);
            navigationBar.mWindowManager.removeViewImmediate(navigationBar.mNavigationBarView.getRootView());
            NavigationModeController navigationModeController = navigationBar.mNavigationModeController;
            Objects.requireNonNull(navigationModeController);
            navigationModeController.mListeners.remove(navigationBar);
            NavBarHelper navBarHelper = navigationBar.mNavBarHelper;
            NavigationBar.AnonymousClass2 r2 = navigationBar.mNavbarTaskbarStateUpdater;
            Objects.requireNonNull(navBarHelper);
            navBarHelper.mA11yEventListeners.remove(r2);
            NavBarHelper navBarHelper2 = navigationBar.mNavBarHelper;
            Objects.requireNonNull(navBarHelper2);
            navBarHelper2.mContentResolver.unregisterContentObserver(navBarHelper2.mAssistContentObserver);
            navigationBar.mDeviceProvisionedController.removeCallback(navigationBar.mUserSetupListener);
            NotificationShadeDepthController notificationShadeDepthController = navigationBar.mNotificationShadeDepthController;
            NavigationBar.AnonymousClass7 r22 = navigationBar.mDepthListener;
            Objects.requireNonNull(notificationShadeDepthController);
            notificationShadeDepthController.listeners.remove(r22);
            DeviceConfig.removeOnPropertiesChangedListener(navigationBar.mOnPropertiesChangedListener);
            this.mNavigationBars.remove(i);
        }
    }

    public final void touchAutoDim(int i) {
        NavigationBar navigationBar = this.mNavigationBars.get(i);
        if (navigationBar != null) {
            navigationBar.getBarTransitions().setAutoDim(false);
            navigationBar.mHandler.removeCallbacks(navigationBar.mAutoDim);
            int state = navigationBar.mStatusBarStateController.getState();
            if (state != 1 && state != 2) {
                navigationBar.mHandler.postDelayed(navigationBar.mAutoDim, 2250L);
            }
        }
    }

    public final void updateAccessibilityButtonModeIfNeeded() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        int intForUser = Settings.Secure.getIntForUser(contentResolver, "accessibility_button_mode", 0, -2);
        if (intForUser != 1) {
            if (R$color.isGesturalMode(this.mNavMode) && intForUser == 0) {
                Settings.Secure.putIntForUser(contentResolver, "accessibility_button_mode", 2, -2);
            } else if (!R$color.isGesturalMode(this.mNavMode) && intForUser == 2) {
                Settings.Secure.putIntForUser(contentResolver, "accessibility_button_mode", 0, -2);
            }
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void setNavigationBarLumaSamplingEnabled(int i, boolean z) {
        NavigationBarView navigationBarView = getNavigationBarView(i);
        if (navigationBarView == null) {
            return;
        }
        if (z) {
            navigationBarView.mRegionSamplingHelper.start(navigationBarView.mSamplingBounds);
        } else {
            navigationBarView.mRegionSamplingHelper.stop();
        }
    }

    public final boolean updateNavbarForTaskbar() {
        boolean initializeTaskbarIfNecessary = initializeTaskbarIfNecessary();
        if (!initializeTaskbarIfNecessary && this.mNavigationBars.get(this.mContext.getDisplayId()) == null) {
            createNavigationBar(this.mContext.getDisplay(), null, null);
        }
        return initializeTaskbarIfNecessary;
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void onDisplayRemoved(int i) {
        removeNavigationBar(i);
    }
}
