package com.android.systemui.wmshell;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.MathUtils;
import android.view.InputMonitor;
import android.view.RemoteAnimationAdapter;
import android.view.View;
import android.view.WindowManagerGlobal;
import android.window.WindowContext;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.settingslib.Utils;
import com.android.systemui.clipboardoverlay.ClipboardOverlayController;
import com.android.systemui.model.SysUiState;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.tiles.LocationTile;
import com.android.systemui.qs.tiles.dialog.InternetDialog;
import com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda10;
import com.android.systemui.qs.tiles.dialog.InternetDialogController;
import com.android.systemui.screenshot.LongScreenshotActivity;
import com.android.systemui.screenshot.LongScreenshotData;
import com.android.systemui.screenshot.ScreenshotController;
import com.android.systemui.screenshot.ScrollCaptureController;
import com.android.systemui.screenshot.TimeoutHandler;
import com.android.systemui.statusbar.KeyguardIndicationController;
import com.android.systemui.statusbar.VibratorHelper$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.StatusBarNotificationPresenter;
import com.android.systemui.wmshell.WMShell;
import com.google.android.systemui.smartspace.InterceptingViewPager;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WMShell$7$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ WMShell$7$$ExternalSyntheticLambda2(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                WMShell.AnonymousClass7 r10 = (WMShell.AnonymousClass7) this.f$0;
                Objects.requireNonNull(r10);
                SysUiState sysUiState = WMShell.this.mSysUiState;
                sysUiState.setFlag(65536, true);
                sysUiState.commitUpdate(0);
                return;
            case 1:
                ClipboardOverlayController clipboardOverlayController = (ClipboardOverlayController) this.f$0;
                Objects.requireNonNull(clipboardOverlayController);
                TimeoutHandler timeoutHandler = clipboardOverlayController.mTimeoutHandler;
                Objects.requireNonNull(timeoutHandler);
                timeoutHandler.removeMessages(2);
                View peekDecorView = clipboardOverlayController.mWindow.peekDecorView();
                if (peekDecorView != null && peekDecorView.isAttachedToWindow()) {
                    clipboardOverlayController.mWindowManager.removeViewImmediate(peekDecorView);
                }
                ClipboardOverlayController.AnonymousClass1 r0 = clipboardOverlayController.mCloseDialogsReceiver;
                if (r0 != null) {
                    clipboardOverlayController.mContext.unregisterReceiver(r0);
                    clipboardOverlayController.mCloseDialogsReceiver = null;
                }
                ClipboardOverlayController.AnonymousClass2 r02 = clipboardOverlayController.mScreenshotReceiver;
                if (r02 != null) {
                    clipboardOverlayController.mContext.unregisterReceiver(r02);
                    clipboardOverlayController.mScreenshotReceiver = null;
                }
                ClipboardOverlayController.AnonymousClass3 r03 = clipboardOverlayController.mInputEventReceiver;
                if (r03 != null) {
                    r03.dispose();
                    clipboardOverlayController.mInputEventReceiver = null;
                }
                InputMonitor inputMonitor = clipboardOverlayController.mInputMonitor;
                if (inputMonitor != null) {
                    inputMonitor.dispose();
                    clipboardOverlayController.mInputMonitor = null;
                }
                Runnable runnable = clipboardOverlayController.mOnSessionCompleteListener;
                if (runnable != null) {
                    runnable.run();
                    return;
                }
                return;
            case 2:
                LocationTile locationTile = (LocationTile) this.f$0;
                int i = LocationTile.$r8$clinit;
                Objects.requireNonNull(locationTile);
                boolean z = ((QSTile.BooleanState) locationTile.mState).value;
                locationTile.mHost.openPanels();
                locationTile.mController.setLocationEnabled(!z);
                return;
            case 3:
                InternetDialog internetDialog = (InternetDialog) this.f$0;
                boolean z2 = InternetDialog.DEBUG;
                Objects.requireNonNull(internetDialog);
                InternetDialogController internetDialogController = internetDialog.mInternetDialogController;
                Objects.requireNonNull(internetDialogController);
                Drawable drawable = internetDialogController.mContext.getDrawable(2131232269);
                try {
                    if (internetDialogController.mTelephonyManager != null) {
                        boolean isCarrierNetworkActive = internetDialogController.isCarrierNetworkActive();
                        if (internetDialogController.isDataStateInService() || internetDialogController.isVoiceStateInService() || isCarrierNetworkActive) {
                            AtomicReference atomicReference = new AtomicReference();
                            atomicReference.set(internetDialogController.getSignalStrengthDrawableWithLevel(isCarrierNetworkActive));
                            drawable = (Drawable) atomicReference.get();
                        }
                        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(internetDialogController.mContext, 16843282);
                        if (internetDialogController.activeNetworkIsCellular() || isCarrierNetworkActive) {
                            colorAttrDefaultColor = internetDialogController.mContext.getColor(2131099775);
                        }
                        drawable.setTint(colorAttrDefaultColor);
                    } else if (InternetDialogController.DEBUG) {
                        Log.d("InternetDialogController", "TelephonyManager is null");
                    }
                } catch (Throwable th) {
                    th.printStackTrace();
                }
                internetDialog.mHandler.post(new InternetDialog$$ExternalSyntheticLambda10(internetDialog, drawable, 0));
                return;
            case 4:
                final ScreenshotController screenshotController = (ScreenshotController) this.f$0;
                ScreenshotController.AnonymousClass1 r04 = ScreenshotController.SCREENSHOT_REMOTE_RUNNER;
                Objects.requireNonNull(screenshotController);
                try {
                    final ScrollCaptureController.LongScreenshot longScreenshot = (ScrollCaptureController.LongScreenshot) screenshotController.mLongScreenshotFuture.get();
                    Objects.requireNonNull(longScreenshot);
                    if (longScreenshot.mImageTileSet.getHeight() == 0) {
                        screenshotController.mScreenshotView.restoreNonScrollingUi();
                        return;
                    }
                    LongScreenshotData longScreenshotData = screenshotController.mLongScreenshotHolder;
                    Objects.requireNonNull(longScreenshotData);
                    longScreenshotData.mLongScreenshot.set(longScreenshot);
                    LongScreenshotData longScreenshotData2 = screenshotController.mLongScreenshotHolder;
                    ScreenshotController.TransitionDestination screenshotController$$ExternalSyntheticLambda5 = new ScreenshotController.TransitionDestination() { // from class: com.android.systemui.screenshot.ScreenshotController$$ExternalSyntheticLambda5
                        @Override // com.android.systemui.screenshot.ScreenshotController.TransitionDestination
                        public final void setTransitionDestination(final Rect rect, final LongScreenshotActivity$1$$ExternalSyntheticLambda0 longScreenshotActivity$1$$ExternalSyntheticLambda0) {
                            ScreenshotController screenshotController2 = ScreenshotController.this;
                            ScrollCaptureController.LongScreenshot longScreenshot2 = longScreenshot;
                            Objects.requireNonNull(screenshotController2);
                            final ScreenshotView screenshotView = screenshotController2.mScreenshotView;
                            Objects.requireNonNull(screenshotView);
                            AnimatorSet animatorSet = new AnimatorSet();
                            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda3
                                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    ScreenshotView screenshotView2 = ScreenshotView.this;
                                    int i2 = ScreenshotView.$r8$clinit;
                                    Objects.requireNonNull(screenshotView2);
                                    screenshotView2.mScrollingScrim.setAlpha(1.0f - valueAnimator.getAnimatedFraction());
                                }
                            });
                            if (screenshotView.mShowScrollablePreview) {
                                screenshotView.mScrollablePreview.setImageBitmap(longScreenshot2.toBitmap());
                                final float x = screenshotView.mScrollablePreview.getX();
                                final float y = screenshotView.mScrollablePreview.getY();
                                int[] locationOnScreen = screenshotView.mScrollablePreview.getLocationOnScreen();
                                rect.offset(((int) x) - locationOnScreen[0], ((int) y) - locationOnScreen[1]);
                                screenshotView.mScrollablePreview.setPivotX(0.0f);
                                screenshotView.mScrollablePreview.setPivotY(0.0f);
                                screenshotView.mScrollablePreview.setAlpha(1.0f);
                                float width = screenshotView.mScrollablePreview.getWidth() / longScreenshot2.mImageTileSet.getWidth();
                                Matrix matrix = new Matrix();
                                matrix.setScale(width, width);
                                ImageTileSet imageTileSet = longScreenshot2.mImageTileSet;
                                Objects.requireNonNull(imageTileSet);
                                ImageTileSet imageTileSet2 = longScreenshot2.mImageTileSet;
                                Objects.requireNonNull(imageTileSet2);
                                matrix.postTranslate(imageTileSet.mRegion.getBounds().left * width, imageTileSet2.mRegion.getBounds().top * width);
                                screenshotView.mScrollablePreview.setImageMatrix(matrix);
                                final float width2 = rect.width() / screenshotView.mScrollablePreview.getWidth();
                                ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
                                ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda6
                                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                                        ScreenshotView screenshotView2 = ScreenshotView.this;
                                        float f = width2;
                                        float f2 = x;
                                        Rect rect2 = rect;
                                        float f3 = y;
                                        int i2 = ScreenshotView.$r8$clinit;
                                        Objects.requireNonNull(screenshotView2);
                                        float animatedFraction = valueAnimator.getAnimatedFraction();
                                        float lerp = MathUtils.lerp(1.0f, f, animatedFraction);
                                        screenshotView2.mScrollablePreview.setScaleX(lerp);
                                        screenshotView2.mScrollablePreview.setScaleY(lerp);
                                        screenshotView2.mScrollablePreview.setX(MathUtils.lerp(f2, rect2.left, animatedFraction));
                                        screenshotView2.mScrollablePreview.setY(MathUtils.lerp(f3, rect2.top, animatedFraction));
                                    }
                                });
                                ValueAnimator ofFloat3 = ValueAnimator.ofFloat(1.0f, 0.0f);
                                ofFloat3.addUpdateListener(new ScreenshotView$$ExternalSyntheticLambda1(screenshotView, 0));
                                animatorSet.play(ofFloat2).with(ofFloat).before(ofFloat3);
                                ofFloat2.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.screenshot.ScreenshotView.6
                                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                    public final void onAnimationEnd(Animator animator) {
                                        super.onAnimationEnd(animator);
                                        longScreenshotActivity$1$$ExternalSyntheticLambda0.run();
                                    }
                                });
                            } else {
                                animatorSet.play(ofFloat);
                                animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.screenshot.ScreenshotView.7
                                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                    public final void onAnimationEnd(Animator animator) {
                                        super.onAnimationEnd(animator);
                                        longScreenshotActivity$1$$ExternalSyntheticLambda0.run();
                                    }
                                });
                            }
                            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.screenshot.ScreenshotView.8
                                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                public final void onAnimationEnd(Animator animator) {
                                    super.onAnimationEnd(animator);
                                    ScreenshotController.AnonymousClass3 r05 = (ScreenshotController.AnonymousClass3) screenshotView.mCallbacks;
                                    Objects.requireNonNull(r05);
                                    ScreenshotController.this.finishDismiss();
                                }
                            });
                            animatorSet.start();
                        }
                    };
                    Objects.requireNonNull(longScreenshotData2);
                    longScreenshotData2.mTransitionDestinationCallback.set(screenshotController$$ExternalSyntheticLambda5);
                    Intent intent = new Intent((Context) screenshotController.mContext, (Class<?>) LongScreenshotActivity.class);
                    intent.setFlags(335544320);
                    WindowContext windowContext = screenshotController.mContext;
                    windowContext.startActivity(intent, ActivityOptions.makeCustomAnimation(windowContext, 0, 0).toBundle());
                    try {
                        WindowManagerGlobal.getWindowManagerService().overridePendingAppTransitionRemote(new RemoteAnimationAdapter(ScreenshotController.SCREENSHOT_REMOTE_RUNNER, 0L, 0L), 0);
                        return;
                    } catch (Exception e) {
                        Log.e("Screenshot", "Error overriding screenshot app transition", e);
                        return;
                    }
                } catch (InterruptedException | ExecutionException e2) {
                    Log.e("Screenshot", "Exception", e2);
                    screenshotController.mScreenshotView.restoreNonScrollingUi();
                    return;
                } catch (CancellationException unused) {
                    Log.e("Screenshot", "Long screenshot cancelled");
                    return;
                }
            case 5:
                KeyguardIndicationController keyguardIndicationController = (KeyguardIndicationController) this.f$0;
                Objects.requireNonNull(keyguardIndicationController);
                String deviceOwnerInfo = keyguardIndicationController.mLockPatternUtils.getDeviceOwnerInfo();
                if (deviceOwnerInfo == null && keyguardIndicationController.mLockPatternUtils.isOwnerInfoEnabled(KeyguardUpdateMonitor.getCurrentUser())) {
                    deviceOwnerInfo = keyguardIndicationController.mLockPatternUtils.getOwnerInfo(KeyguardUpdateMonitor.getCurrentUser());
                }
                keyguardIndicationController.mExecutor.execute(new VibratorHelper$$ExternalSyntheticLambda0(keyguardIndicationController, deviceOwnerInfo, 2));
                return;
            case FalsingManager.VERSION /* 6 */:
                NotificationStackScrollLayout notificationStackScrollLayout = (NotificationStackScrollLayout) this.f$0;
                boolean z3 = NotificationStackScrollLayout.SPEW;
                Objects.requireNonNull(notificationStackScrollLayout);
                notificationStackScrollLayout.animateScroll();
                return;
            case 7:
                StatusBarNotificationPresenter statusBarNotificationPresenter = (StatusBarNotificationPresenter) this.f$0;
                Objects.requireNonNull(statusBarNotificationPresenter);
                NotificationPanelViewController notificationPanelViewController = statusBarNotificationPresenter.mNotificationPanel;
                Objects.requireNonNull(notificationPanelViewController);
                if (!notificationPanelViewController.mTracking) {
                    NotificationPanelViewController notificationPanelViewController2 = statusBarNotificationPresenter.mNotificationPanel;
                    Objects.requireNonNull(notificationPanelViewController2);
                    if (!notificationPanelViewController2.mQsExpanded && statusBarNotificationPresenter.mStatusBarStateController.getState() == 2 && !statusBarNotificationPresenter.isCollapsing()) {
                        statusBarNotificationPresenter.mStatusBarStateController.setState(1);
                        return;
                    }
                    return;
                }
                return;
            default:
                InterceptingViewPager interceptingViewPager = (InterceptingViewPager) this.f$0;
                int i2 = InterceptingViewPager.$r8$clinit;
                Objects.requireNonNull(interceptingViewPager);
                interceptingViewPager.mHasPerformedLongPress = true;
                if (interceptingViewPager.performLongClick()) {
                    interceptingViewPager.getParent().requestDisallowInterceptTouchEvent(true);
                    return;
                }
                return;
        }
    }
}
