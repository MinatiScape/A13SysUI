package com.android.systemui.statusbar;

import android.view.ViewGroup;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import com.android.systemui.statusbar.phone.StatusBar$2$Callback$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.phone.StatusBarTouchableRegionManager$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.phone.StatusBarWindowCallback;
/* loaded from: classes.dex */
public interface NotificationShadeWindowController extends RemoteInputController.Callback {

    /* loaded from: classes.dex */
    public interface ForcePluginOpenListener {
    }

    /* loaded from: classes.dex */
    public interface OtherwisedCollapsedListener {
    }

    default void attach$1() {
    }

    default boolean getForcePluginOpen() {
        return false;
    }

    default ViewGroup getNotificationShadeView() {
        return null;
    }

    default boolean getPanelExpanded() {
        return false;
    }

    default boolean isLaunchingActivity() {
        return false;
    }

    default boolean isShowingWallpaper() {
        return false;
    }

    default void notifyStateChangedCallbacks() {
    }

    @Override // com.android.systemui.statusbar.RemoteInputController.Callback
    default void onRemoteInputActive(boolean z) {
    }

    default void registerCallback(StatusBarWindowCallback statusBarWindowCallback) {
    }

    default void setBackdropShowing(boolean z) {
    }

    default void setBackgroundBlurRadius(int i) {
    }

    default void setBouncerShowing(boolean z) {
    }

    default void setDozeScreenBrightness(int i) {
    }

    default void setForceDozeBrightness(boolean z) {
    }

    default void setForcePluginOpen(boolean z, Object obj) {
    }

    default void setForcePluginOpenListener(StatusBarTouchableRegionManager$$ExternalSyntheticLambda0 statusBarTouchableRegionManager$$ExternalSyntheticLambda0) {
    }

    default void setForceWindowCollapsed(boolean z) {
    }

    default void setHeadsUpShowing(boolean z) {
    }

    default void setKeyguardFadingAway(boolean z) {
    }

    default void setKeyguardGoingAway(boolean z) {
    }

    default void setKeyguardNeedsInput(boolean z) {
    }

    default void setKeyguardOccluded(boolean z) {
    }

    default void setKeyguardShowing(boolean z) {
    }

    default void setLaunchingActivity(boolean z) {
    }

    default void setLightRevealScrimOpaque(boolean z) {
    }

    default void setNotTouchable() {
    }

    default void setNotificationShadeFocusable(boolean z) {
    }

    default void setNotificationShadeView(NotificationShadeWindowView notificationShadeWindowView) {
    }

    default void setPanelExpanded(boolean z) {
    }

    default void setPanelVisible(boolean z) {
    }

    default void setQsExpanded(boolean z) {
    }

    default void setRequestTopUi(boolean z, String str) {
    }

    default void setScrimsVisibility(int i) {
    }

    default void setScrimsVisibilityListener(NotificationShadeDepthController.AnonymousClass1 r1) {
    }

    default void setStateListener(StatusBar$2$Callback$$ExternalSyntheticLambda0 statusBar$2$Callback$$ExternalSyntheticLambda0) {
    }

    default void setWallpaperSupportsAmbientMode() {
    }

    default void unregisterCallback(StatusBarWindowCallback statusBarWindowCallback) {
    }

    default void batchApplyWindowLayoutParams(Runnable runnable) {
        runnable.run();
    }
}
