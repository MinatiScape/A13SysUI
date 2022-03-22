package com.android.systemui.qs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Rect;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import android.util.Slog;
import android.view.View;
import android.view.WindowManagerGlobal;
import android.view.animation.PathInterpolator;
import com.android.internal.graphics.ColorUtils;
import com.android.internal.util.function.TriConsumer;
import com.android.systemui.accessibility.MagnificationModeSwitch;
import com.android.systemui.clipboardoverlay.ClipboardOverlayController;
import com.android.systemui.doze.DozeScreenState$$ExternalSyntheticLambda0;
import com.android.systemui.navigationbar.gestural.NavigationBarEdgePanel;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.tileimpl.QSTileImpl$$ExternalSyntheticLambda0;
import com.android.systemui.screenshot.ImageLoader$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.connectivity.AccessPointControllerImpl;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarHeadsUpChangeListener;
import com.android.systemui.util.sensors.ProximitySensorImpl;
import com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda11;
import com.android.wifitrackerlib.BaseWifiTracker;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.FloatingContentCoordinator;
import com.android.wm.shell.common.TaskStackListenerCallback;
import com.android.wm.shell.onehanded.OneHandedController;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.PipUtils;
import com.android.wm.shell.pip.phone.PipController;
import com.android.wm.shell.pip.phone.PipController$$ExternalSyntheticLambda1;
import com.android.wm.shell.pip.phone.PipController$$ExternalSyntheticLambda4;
import com.android.wm.shell.pip.phone.PipInputConsumer;
import com.android.wm.shell.pip.phone.PipMotionHelper;
import com.android.wm.shell.pip.phone.PipTouchHandler;
import com.google.android.apps.miphone.aiai.matchmaker.overview.ui.SuggestController$$ExternalSyntheticLambda1;
import java.util.Collections;
import java.util.Objects;
import java.util.function.IntConsumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class QSFgsManagerFooter$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ QSFgsManagerFooter$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        final float f = 0.0f;
        switch (this.$r8$classId) {
            case 0:
                QSFgsManagerFooter qSFgsManagerFooter = (QSFgsManagerFooter) this.f$0;
                Objects.requireNonNull(qSFgsManagerFooter);
                qSFgsManagerFooter.mMainExecutor.execute(new DozeScreenState$$ExternalSyntheticLambda0(qSFgsManagerFooter, 3));
                return;
            case 1:
                MagnificationModeSwitch magnificationModeSwitch = (MagnificationModeSwitch) this.f$0;
                Objects.requireNonNull(magnificationModeSwitch);
                magnificationModeSwitch.mImageView.setSystemGestureExclusionRects(Collections.singletonList(new Rect(0, 0, magnificationModeSwitch.mImageView.getWidth(), magnificationModeSwitch.mImageView.getHeight())));
                return;
            case 2:
                final ClipboardOverlayController clipboardOverlayController = (ClipboardOverlayController) this.f$0;
                Objects.requireNonNull(clipboardOverlayController);
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                clipboardOverlayController.mContainer.setAlpha(0.0f);
                clipboardOverlayController.mDismissButton.setVisibility(8);
                final View findViewById = clipboardOverlayController.mView.findViewById(2131428608);
                Objects.requireNonNull(findViewById);
                final View findViewById2 = clipboardOverlayController.mView.findViewById(2131427457);
                Objects.requireNonNull(findViewById2);
                clipboardOverlayController.mImagePreview.setVisibility(0);
                clipboardOverlayController.mActionContainerBackground.setVisibility(0);
                if (clipboardOverlayController.mAccessibilityManager.isEnabled()) {
                    clipboardOverlayController.mDismissButton.setVisibility(0);
                }
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.clipboardoverlay.ClipboardOverlayController$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        ClipboardOverlayController clipboardOverlayController2 = ClipboardOverlayController.this;
                        View view = findViewById;
                        View view2 = findViewById2;
                        Objects.requireNonNull(clipboardOverlayController2);
                        clipboardOverlayController2.mContainer.setAlpha(valueAnimator.getAnimatedFraction());
                        float animatedFraction = (valueAnimator.getAnimatedFraction() * 0.4f) + 0.6f;
                        DraggableConstraintLayout draggableConstraintLayout = clipboardOverlayController2.mView;
                        draggableConstraintLayout.setPivotY(draggableConstraintLayout.getHeight() - (view.getHeight() / 2.0f));
                        clipboardOverlayController2.mView.setPivotX(view2.getWidth() / 2.0f);
                        clipboardOverlayController2.mView.setScaleX(animatedFraction);
                        clipboardOverlayController2.mView.setScaleY(animatedFraction);
                    }
                });
                ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.clipboardoverlay.ClipboardOverlayController.4
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        super.onAnimationEnd(animator);
                        clipboardOverlayController.mContainer.setAlpha(1.0f);
                        clipboardOverlayController.mTimeoutHandler.resetTimeout();
                    }
                });
                ofFloat.start();
                return;
            case 3:
                NavigationBarEdgePanel navigationBarEdgePanel = (NavigationBarEdgePanel) this.f$0;
                PathInterpolator pathInterpolator = NavigationBarEdgePanel.RUBBER_BAND_INTERPOLATOR;
                Objects.requireNonNull(navigationBarEdgePanel);
                navigationBarEdgePanel.setVisibility(8);
                return;
            case 4:
                final StatusBar statusBar = (StatusBar) this.f$0;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                Objects.requireNonNull(statusBar);
                if (statusBar.mWallpaperManager.lockScreenWallpaperExists()) {
                    f = statusBar.mWallpaperManager.getWallpaperDimAmount();
                }
                statusBar.mMainExecutor.execute(new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda25
                    @Override // java.lang.Runnable
                    public final void run() {
                        ScrimState[] values;
                        StatusBar statusBar2 = StatusBar.this;
                        float f2 = f;
                        long[] jArr2 = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                        Objects.requireNonNull(statusBar2);
                        ScrimController scrimController = statusBar2.mScrimController;
                        Objects.requireNonNull(scrimController);
                        scrimController.mAdditionalScrimBehindAlphaKeyguard = f2;
                        ScrimController scrimController2 = statusBar2.mScrimController;
                        Objects.requireNonNull(scrimController2);
                        float compositeAlpha = ColorUtils.compositeAlpha((int) (scrimController2.mAdditionalScrimBehindAlphaKeyguard * 255.0f), 51) / 255.0f;
                        scrimController2.mScrimBehindAlphaKeyguard = compositeAlpha;
                        for (ScrimState scrimState : ScrimState.values()) {
                            Objects.requireNonNull(scrimState);
                            scrimState.mScrimBehindAlphaKeyguard = compositeAlpha;
                        }
                        scrimController2.scheduleUpdate();
                    }
                });
                return;
            case 5:
                StatusBarHeadsUpChangeListener statusBarHeadsUpChangeListener = (StatusBarHeadsUpChangeListener) this.f$0;
                Objects.requireNonNull(statusBarHeadsUpChangeListener);
                HeadsUpManagerPhone headsUpManagerPhone = statusBarHeadsUpChangeListener.mHeadsUpManager;
                Objects.requireNonNull(headsUpManagerPhone);
                if (!headsUpManagerPhone.mHasPinnedNotification) {
                    statusBarHeadsUpChangeListener.mNotificationShadeWindowController.setHeadsUpShowing(false);
                    statusBarHeadsUpChangeListener.mHeadsUpManager.setHeadsUpGoingAway(false);
                }
                NotificationRemoteInputManager notificationRemoteInputManager = statusBarHeadsUpChangeListener.mNotificationRemoteInputManager;
                Objects.requireNonNull(notificationRemoteInputManager);
                NotificationRemoteInputManager.RemoteInputListener remoteInputListener = notificationRemoteInputManager.mRemoteInputListener;
                if (remoteInputListener != null) {
                    remoteInputListener.onPanelCollapsed();
                    return;
                }
                return;
            case FalsingManager.VERSION /* 6 */:
                ProximitySensorImpl.AnonymousClass1 r8 = (ProximitySensorImpl.AnonymousClass1) this.f$0;
                Objects.requireNonNull(r8);
                ProximitySensorImpl.this.mPrimaryThresholdSensor.pause();
                ProximitySensorImpl.this.mSecondaryThresholdSensor.resume();
                return;
            case 7:
                AccessPointControllerImpl accessPointControllerImpl = (AccessPointControllerImpl) ((BaseWifiTracker.BaseWifiTrackerCallback) this.f$0);
                Objects.requireNonNull(accessPointControllerImpl);
                accessPointControllerImpl.scanForAccessPoints();
                return;
            case 8:
                ((OneHandedController) this.f$0).onShortcutEnabledChanged();
                return;
            case 9:
                final PipController pipController = (PipController) this.f$0;
                Objects.requireNonNull(pipController);
                pipController.mPipInputConsumer = new PipInputConsumer(WindowManagerGlobal.getWindowManagerService(), pipController.mMainExecutor);
                PipTransitionController pipTransitionController = pipController.mPipTransitionController;
                Objects.requireNonNull(pipTransitionController);
                pipTransitionController.mPipTransitionCallbacks.add(pipController);
                PipTaskOrganizer pipTaskOrganizer = pipController.mPipTaskOrganizer;
                IntConsumer pipController$$ExternalSyntheticLambda5 = new IntConsumer() { // from class: com.android.wm.shell.pip.phone.PipController$$ExternalSyntheticLambda5
                    @Override // java.util.function.IntConsumer
                    public final void accept(int i) {
                        PipController pipController2 = PipController.this;
                        Objects.requireNonNull(pipController2);
                        PipBoundsState pipBoundsState = pipController2.mPipBoundsState;
                        Objects.requireNonNull(pipBoundsState);
                        pipBoundsState.mDisplayId = i;
                        pipController2.onDisplayChanged(pipController2.mDisplayController.getDisplayLayout(i), false);
                    }
                };
                Objects.requireNonNull(pipTaskOrganizer);
                pipTaskOrganizer.mOnDisplayIdChangeCallback = pipController$$ExternalSyntheticLambda5;
                PipBoundsState pipBoundsState = pipController.mPipBoundsState;
                QSTileImpl$$ExternalSyntheticLambda0 qSTileImpl$$ExternalSyntheticLambda0 = new QSTileImpl$$ExternalSyntheticLambda0(pipController, 9);
                Objects.requireNonNull(pipBoundsState);
                pipBoundsState.mOnMinimalSizeChangeCallback = qSTileImpl$$ExternalSyntheticLambda0;
                PipBoundsState pipBoundsState2 = pipController.mPipBoundsState;
                TriConsumer<Boolean, Integer, Boolean> pipController$$ExternalSyntheticLambda0 = new TriConsumer() { // from class: com.android.wm.shell.pip.phone.PipController$$ExternalSyntheticLambda0
                    public final void accept(Object obj, Object obj2, Object obj3) {
                        PipController pipController2 = PipController.this;
                        Objects.requireNonNull(pipController2);
                        PipTouchHandler pipTouchHandler = pipController2.mTouchHandler;
                        boolean booleanValue = ((Boolean) obj).booleanValue();
                        int intValue = ((Integer) obj2).intValue();
                        Objects.requireNonNull(pipTouchHandler);
                        pipTouchHandler.mIsShelfShowing = booleanValue;
                        pipTouchHandler.mShelfHeight = intValue;
                        if (((Boolean) obj3).booleanValue()) {
                            pipController2.updateMovementBounds(pipController2.mPipBoundsState.getBounds(), false, false, true, null);
                        }
                    }
                };
                Objects.requireNonNull(pipBoundsState2);
                pipBoundsState2.mOnShelfVisibilityChangeCallback = pipController$$ExternalSyntheticLambda0;
                PipTouchHandler pipTouchHandler = pipController.mTouchHandler;
                if (pipTouchHandler != null) {
                    PipInputConsumer pipInputConsumer = pipController.mPipInputConsumer;
                    PipController$$ExternalSyntheticLambda1 pipController$$ExternalSyntheticLambda1 = new PipController$$ExternalSyntheticLambda1(pipTouchHandler);
                    Objects.requireNonNull(pipInputConsumer);
                    pipInputConsumer.mListener = pipController$$ExternalSyntheticLambda1;
                    PipInputConsumer pipInputConsumer2 = pipController.mPipInputConsumer;
                    PipTouchHandler pipTouchHandler2 = pipController.mTouchHandler;
                    Objects.requireNonNull(pipTouchHandler2);
                    ImageLoader$$ExternalSyntheticLambda0 imageLoader$$ExternalSyntheticLambda0 = new ImageLoader$$ExternalSyntheticLambda0(pipTouchHandler2);
                    Objects.requireNonNull(pipInputConsumer2);
                    pipInputConsumer2.mRegistrationListener = imageLoader$$ExternalSyntheticLambda0;
                    pipInputConsumer2.mMainExecutor.execute(new SuggestController$$ExternalSyntheticLambda1(pipInputConsumer2, 10));
                }
                pipController.mDisplayController.addDisplayChangingController(pipController.mRotationController);
                pipController.mDisplayController.addDisplayWindowListener(pipController.mDisplaysChangedListener);
                PipBoundsState pipBoundsState3 = pipController.mPipBoundsState;
                int displayId = pipController.mContext.getDisplayId();
                Objects.requireNonNull(pipBoundsState3);
                pipBoundsState3.mDisplayId = displayId;
                PipBoundsState pipBoundsState4 = pipController.mPipBoundsState;
                Context context = pipController.mContext;
                DisplayLayout displayLayout = new DisplayLayout(context, context.getDisplay());
                Objects.requireNonNull(pipBoundsState4);
                pipBoundsState4.mDisplayLayout.set(displayLayout);
                try {
                    pipController.mWindowManagerShellWrapper.addPinnedStackListener(pipController.mPinnedTaskListener);
                } catch (RemoteException e) {
                    Slog.e("PipController", "Failed to register pinned stack listener", e);
                }
                try {
                    if (ActivityTaskManager.getService().getRootTaskInfo(2, 0) != null) {
                        pipController.mPipInputConsumer.registerInputConsumer();
                    }
                } catch (RemoteException | UnsupportedOperationException e2) {
                    Log.e("PipController", "Failed to register pinned stack listener", e2);
                    e2.printStackTrace();
                }
                pipController.mTaskStackListener.addListener(new TaskStackListenerCallback() { // from class: com.android.wm.shell.pip.phone.PipController.2
                    @Override // com.android.wm.shell.common.TaskStackListenerCallback
                    public final void onActivityPinned(String str) {
                        PipTouchHandler pipTouchHandler3 = pipController.mTouchHandler;
                        Objects.requireNonNull(pipTouchHandler3);
                        pipTouchHandler3.mPipDismissTargetHandler.createOrUpdateDismissTarget();
                        PipResizeGestureHandler pipResizeGestureHandler = pipTouchHandler3.mPipResizeGestureHandler;
                        Objects.requireNonNull(pipResizeGestureHandler);
                        pipResizeGestureHandler.mIsAttached = true;
                        pipResizeGestureHandler.updateIsEnabled();
                        FloatingContentCoordinator floatingContentCoordinator = pipTouchHandler3.mFloatingContentCoordinator;
                        PipMotionHelper pipMotionHelper = pipTouchHandler3.mMotionHelper;
                        Objects.requireNonNull(floatingContentCoordinator);
                        floatingContentCoordinator.updateContentBounds();
                        floatingContentCoordinator.allContentBounds.put(pipMotionHelper, pipMotionHelper.getFloatingBoundsOnScreen());
                        floatingContentCoordinator.maybeMoveConflictingContent(pipMotionHelper);
                        PipMediaController pipMediaController = pipController.mMediaController;
                        Objects.requireNonNull(pipMediaController);
                        pipMediaController.resolveActiveMediaController(pipMediaController.mMediaSessionManager.getActiveSessionsForUser(null, UserHandle.CURRENT));
                        PipAppOpsListener pipAppOpsListener = pipController.mAppOpsListener;
                        Objects.requireNonNull(pipAppOpsListener);
                        pipAppOpsListener.mAppOpsManager.startWatchingMode(67, str, pipAppOpsListener.mAppOpsChangedListener);
                        pipController.mPipInputConsumer.registerInputConsumer();
                    }

                    @Override // com.android.wm.shell.common.TaskStackListenerCallback
                    public final void onActivityUnpinned() {
                        ComponentName componentName = (ComponentName) PipUtils.getTopPipActivity(pipController.mContext).first;
                        PipTouchHandler pipTouchHandler3 = pipController.mTouchHandler;
                        Objects.requireNonNull(pipTouchHandler3);
                        if (componentName == null) {
                            PipDismissTargetHandler pipDismissTargetHandler = pipTouchHandler3.mPipDismissTargetHandler;
                            Objects.requireNonNull(pipDismissTargetHandler);
                            if (pipDismissTargetHandler.mTargetViewContainer.isAttachedToWindow()) {
                                pipDismissTargetHandler.mWindowManager.removeViewImmediate(pipDismissTargetHandler.mTargetViewContainer);
                            }
                            FloatingContentCoordinator floatingContentCoordinator = pipTouchHandler3.mFloatingContentCoordinator;
                            PipMotionHelper pipMotionHelper = pipTouchHandler3.mMotionHelper;
                            Objects.requireNonNull(floatingContentCoordinator);
                            floatingContentCoordinator.allContentBounds.remove(pipMotionHelper);
                        }
                        PipResizeGestureHandler pipResizeGestureHandler = pipTouchHandler3.mPipResizeGestureHandler;
                        Objects.requireNonNull(pipResizeGestureHandler);
                        pipResizeGestureHandler.mIsAttached = false;
                        pipResizeGestureHandler.mUserResizeBounds.setEmpty();
                        pipResizeGestureHandler.updateIsEnabled();
                        PipAppOpsListener pipAppOpsListener = pipController.mAppOpsListener;
                        Objects.requireNonNull(pipAppOpsListener);
                        pipAppOpsListener.mAppOpsManager.stopWatchingMode(pipAppOpsListener.mAppOpsChangedListener);
                        PipInputConsumer pipInputConsumer3 = pipController.mPipInputConsumer;
                        Objects.requireNonNull(pipInputConsumer3);
                        if (pipInputConsumer3.mInputEventReceiver != null) {
                            try {
                                pipInputConsumer3.mWindowManager.destroyInputConsumer(pipInputConsumer3.mName, 0);
                            } catch (RemoteException e3) {
                                Log.e("PipInputConsumer", "Failed to destroy input consumer", e3);
                            }
                            pipInputConsumer3.mInputEventReceiver.dispose();
                            pipInputConsumer3.mInputEventReceiver = null;
                            pipInputConsumer3.mMainExecutor.execute(new VolumeDialogImpl$$ExternalSyntheticLambda11(pipInputConsumer3, 4));
                        }
                    }

                    @Override // com.android.wm.shell.common.TaskStackListenerCallback
                    public final void onActivityRestartAttempt(ActivityManager.RunningTaskInfo runningTaskInfo, boolean z, boolean z2) {
                        if (runningTaskInfo.getWindowingMode() == 2) {
                            PipTouchHandler pipTouchHandler3 = pipController.mTouchHandler;
                            Objects.requireNonNull(pipTouchHandler3);
                            PipMotionHelper pipMotionHelper = pipTouchHandler3.mMotionHelper;
                            Objects.requireNonNull(pipMotionHelper);
                            pipMotionHelper.expandLeavePip(z, false);
                        }
                    }
                });
                pipController.mOneHandedController.ifPresent(new PipController$$ExternalSyntheticLambda4(pipController, 0));
                return;
            default:
                PipMotionHelper.$r8$lambda$QFpQr4PSFRGfS8YBsx6HKEKo4u4((PipMotionHelper) this.f$0);
                return;
        }
    }
}
