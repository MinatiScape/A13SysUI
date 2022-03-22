package com.android.systemui.screenshot;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.app.ExitTransitionCoordinator;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Bitmap;
import android.graphics.Insets;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.hardware.display.DisplayManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.util.MathUtils;
import android.util.Pair;
import android.view.Display;
import android.view.DisplayAddress;
import android.view.IRemoteAnimationFinishedCallback;
import android.view.IRemoteAnimationRunner;
import android.view.IScrollCaptureResponseListener;
import android.view.RemoteAnimationTarget;
import android.view.ScrollCaptureResponse;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.window.WindowContext;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.policy.PhoneWindow;
import com.android.settingslib.applications.InterestingConfigChanges;
import com.android.systemui.ScreenDecorations$$ExternalSyntheticLambda2;
import com.android.systemui.screenshot.ScreenshotController;
import com.android.systemui.screenshot.ScreenshotView;
import com.android.systemui.screenshot.TakeScreenshotService;
import com.android.systemui.tuner.NavBarTuner$$ExternalSyntheticLambda6;
import com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda0;
import com.android.wifitrackerlib.BaseWifiTracker$$ExternalSyntheticLambda1;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda17;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public final class ScreenshotController {
    public static final AnonymousClass1 SCREENSHOT_REMOTE_RUNNER = new IRemoteAnimationRunner.Stub() { // from class: com.android.systemui.screenshot.ScreenshotController.1
        public final void onAnimationCancelled() {
        }

        public final void onAnimationStart(int i, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
            try {
                iRemoteAnimationFinishedCallback.onAnimationFinished();
            } catch (RemoteException e) {
                AnonymousClass1 r1 = ScreenshotController.SCREENSHOT_REMOTE_RUNNER;
                Log.e("Screenshot", "Error finishing screenshot remote animation", e);
            }
        }
    };
    public final AccessibilityManager mAccessibilityManager;
    public boolean mBlockAttach;
    public final MediaPlayer mCameraSound;
    public final InterestingConfigChanges mConfigChanges;
    public final WindowContext mContext;
    public AnonymousClass2 mCopyBroadcastReceiver;
    public TakeScreenshotService.RequestCallback mCurrentRequestCallback;
    public final DisplayManager mDisplayManager;
    public final ImageExporter mImageExporter;
    public final boolean mIsLowRamDevice;
    public CallbackToFutureAdapter.SafeFuture mLastScrollCaptureRequest;
    public ScrollCaptureResponse mLastScrollCaptureResponse;
    public CallbackToFutureAdapter.SafeFuture mLongScreenshotFuture;
    public final LongScreenshotData mLongScreenshotHolder;
    public final Executor mMainExecutor;
    public final ScreenshotNotificationsController mNotificationsController;
    public SaveImageInBackgroundTask mSaveInBgTask;
    public Bitmap mScreenBitmap;
    public AnimatorSet mScreenshotAnimation;
    public final TimeoutHandler mScreenshotHandler;
    public final ScreenshotSmartActions mScreenshotSmartActions;
    public boolean mScreenshotTakenInPortrait;
    public ScreenshotView mScreenshotView;
    public final ScrollCaptureClient mScrollCaptureClient;
    public final ScrollCaptureController mScrollCaptureController;
    public final UiEventLogger mUiEventLogger;
    public final PhoneWindow mWindow;
    public final WindowManager.LayoutParams mWindowLayoutParams;
    public final WindowManager mWindowManager;
    public String mPackageName = "";
    public final ExecutorService mBgExecutor = Executors.newSingleThreadExecutor();

    /* renamed from: com.android.systemui.screenshot.ScreenshotController$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass3 implements ScreenshotView.ScreenshotViewCallback {
        public AnonymousClass3() {
        }
    }

    /* loaded from: classes.dex */
    public interface ActionsReadyListener {
        void onActionsReady(SavedImageData savedImageData);
    }

    /* loaded from: classes.dex */
    public interface QuickShareActionReadyListener {
    }

    /* loaded from: classes.dex */
    public static class QuickShareData {
        public Notification.Action quickShareAction;
    }

    /* loaded from: classes.dex */
    public static class SaveImageInBackgroundData {
        public Consumer<Uri> finisher;
        public Bitmap image;
        public ActionsReadyListener mActionsReadyListener;
        public QuickShareActionReadyListener mQuickShareActionsReadyListener;
    }

    /* loaded from: classes.dex */
    public static class SavedImageData {
        public Supplier<ActionTransition> editTransition;
        public Notification.Action quickShareAction;
        public Supplier<ActionTransition> shareTransition;
        public ArrayList smartActions;
        public Uri uri;

        /* loaded from: classes.dex */
        public static class ActionTransition {
            public Notification.Action action;
            public Bundle bundle;
            public ScreenDecorations$$ExternalSyntheticLambda2 onCancelRunnable;
        }
    }

    /* loaded from: classes.dex */
    public class ScreenshotExitTransitionCallbacksSupplier implements Supplier<ExitTransitionCoordinator.ExitTransitionCallbacks> {

        /* renamed from: com.android.systemui.screenshot.ScreenshotController$ScreenshotExitTransitionCallbacksSupplier$1  reason: invalid class name */
        /* loaded from: classes.dex */
        public final class AnonymousClass1 implements ExitTransitionCoordinator.ExitTransitionCallbacks {
            public final boolean isReturnTransitionAllowed() {
                return false;
            }

            public final void onFinish() {
            }

            public AnonymousClass1() {
            }

            public final void hideSharedElements() {
                Objects.requireNonNull(ScreenshotExitTransitionCallbacksSupplier.this);
                ScreenshotController screenshotController = ScreenshotController.this;
                AnonymousClass1 r0 = ScreenshotController.SCREENSHOT_REMOTE_RUNNER;
                screenshotController.finishDismiss();
            }
        }

        public ScreenshotExitTransitionCallbacksSupplier() {
        }

        @Override // java.util.function.Supplier
        public final ExitTransitionCoordinator.ExitTransitionCallbacks get() {
            return new AnonymousClass1();
        }
    }

    /* loaded from: classes.dex */
    public interface TransitionDestination {
        void setTransitionDestination(Rect rect, LongScreenshotActivity$1$$ExternalSyntheticLambda0 longScreenshotActivity$1$$ExternalSyntheticLambda0);
    }

    public final void attachWindow() {
        View decorView = this.mWindow.getDecorView();
        if (!decorView.isAttachedToWindow() && !this.mBlockAttach) {
            this.mBlockAttach = true;
            this.mWindowManager.addView(decorView, this.mWindowLayoutParams);
            decorView.requestApplyInsets();
        }
    }

    public final void dismissScreenshot() {
        boolean z;
        ScreenshotView screenshotView = this.mScreenshotView;
        Objects.requireNonNull(screenshotView);
        SwipeDismissHandler swipeDismissHandler = screenshotView.mSwipeDismissHandler;
        Objects.requireNonNull(swipeDismissHandler);
        ValueAnimator valueAnimator = swipeDismissHandler.mDismissAnimation;
        if (valueAnimator == null || !valueAnimator.isRunning()) {
            z = false;
        } else {
            z = true;
        }
        if (!z) {
            TimeoutHandler timeoutHandler = this.mScreenshotHandler;
            Objects.requireNonNull(timeoutHandler);
            timeoutHandler.removeMessages(2);
            ScreenshotView screenshotView2 = this.mScreenshotView;
            Objects.requireNonNull(screenshotView2);
            SwipeDismissHandler swipeDismissHandler2 = screenshotView2.mSwipeDismissHandler;
            Objects.requireNonNull(swipeDismissHandler2);
            swipeDismissHandler2.dismiss(1.0f);
        }
    }

    public final void finishDismiss() {
        CallbackToFutureAdapter.SafeFuture safeFuture = this.mLastScrollCaptureRequest;
        if (safeFuture != null) {
            safeFuture.cancel(true);
            this.mLastScrollCaptureRequest = null;
        }
        ScrollCaptureResponse scrollCaptureResponse = this.mLastScrollCaptureResponse;
        if (scrollCaptureResponse != null) {
            scrollCaptureResponse.close();
            this.mLastScrollCaptureResponse = null;
        }
        CallbackToFutureAdapter.SafeFuture safeFuture2 = this.mLongScreenshotFuture;
        if (safeFuture2 != null) {
            safeFuture2.cancel(true);
        }
        TakeScreenshotService.RequestCallback requestCallback = this.mCurrentRequestCallback;
        if (requestCallback != null) {
            Messenger messenger = ((TakeScreenshotService.RequestCallbackImpl) requestCallback).mReplyTo;
            int i = TakeScreenshotService.$r8$clinit;
            try {
                messenger.send(Message.obtain((Handler) null, 2));
            } catch (RemoteException e) {
                Log.d("Screenshot", "ignored remote exception", e);
            }
            this.mCurrentRequestCallback = null;
        }
        this.mScreenshotView.reset();
        removeWindow();
        TimeoutHandler timeoutHandler = this.mScreenshotHandler;
        Objects.requireNonNull(timeoutHandler);
        timeoutHandler.removeMessages(2);
    }

    public final void logSuccessOnActionsReady(SavedImageData savedImageData) {
        if (savedImageData.uri == null) {
            this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_NOT_SAVED, 0, this.mPackageName);
            this.mNotificationsController.notifyScreenshotError(2131953226);
            return;
        }
        this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_SAVED, 0, this.mPackageName);
    }

    public final void removeWindow() {
        View peekDecorView = this.mWindow.peekDecorView();
        if (peekDecorView != null && peekDecorView.isAttachedToWindow()) {
            this.mWindowManager.removeViewImmediate(peekDecorView);
        }
        ScreenshotView screenshotView = this.mScreenshotView;
        if (screenshotView != null) {
            screenshotView.stopInputListening();
        }
    }

    public final void requestScrollCapture() {
        if (!(!this.mIsLowRamDevice)) {
            Log.d("Screenshot", "Long screenshots not supported on this device");
            return;
        }
        ScrollCaptureClient scrollCaptureClient = this.mScrollCaptureClient;
        IBinder windowToken = this.mWindow.getDecorView().getWindowToken();
        Objects.requireNonNull(scrollCaptureClient);
        scrollCaptureClient.mHostWindowToken = windowToken;
        CallbackToFutureAdapter.SafeFuture safeFuture = this.mLastScrollCaptureRequest;
        if (safeFuture != null) {
            safeFuture.cancel(true);
        }
        final ScrollCaptureClient scrollCaptureClient2 = this.mScrollCaptureClient;
        Objects.requireNonNull(scrollCaptureClient2);
        CallbackToFutureAdapter.SafeFuture future = CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver() { // from class: com.android.systemui.screenshot.ScrollCaptureClient$$ExternalSyntheticLambda0
            public final /* synthetic */ int f$1 = 0;
            public final /* synthetic */ int f$2 = -1;

            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(final CallbackToFutureAdapter.Completer completer) {
                ScrollCaptureClient scrollCaptureClient3 = ScrollCaptureClient.this;
                int i = this.f$1;
                int i2 = this.f$2;
                Objects.requireNonNull(scrollCaptureClient3);
                try {
                    scrollCaptureClient3.mWindowManagerService.requestScrollCapture(i, scrollCaptureClient3.mHostWindowToken, i2, new IScrollCaptureResponseListener.Stub() { // from class: com.android.systemui.screenshot.ScrollCaptureClient.1
                        public final void onScrollCaptureResponse(ScrollCaptureResponse scrollCaptureResponse) {
                            completer.set(scrollCaptureResponse);
                        }
                    });
                } catch (RemoteException e) {
                    completer.setException(e);
                }
                return "ScrollCaptureClient#request(displayId=" + i + ", taskId=" + i2 + ")";
            }
        });
        this.mLastScrollCaptureRequest = future;
        future.delegate.addListener(new BubbleStackView$$ExternalSyntheticLambda17(this, 5), this.mMainExecutor);
    }

    public final void saveScreenshot(Bitmap bitmap, Consumer<Uri> consumer, final Rect rect, Insets insets, ComponentName componentName, final boolean z) {
        String str;
        boolean z2;
        boolean z3;
        if (this.mAccessibilityManager.isEnabled()) {
            AccessibilityEvent accessibilityEvent = new AccessibilityEvent(32);
            accessibilityEvent.setContentDescription(this.mContext.getResources().getString(2131953233));
            this.mAccessibilityManager.sendAccessibilityEvent(accessibilityEvent);
        }
        if (this.mScreenshotView.isAttachedToWindow()) {
            ScreenshotView screenshotView = this.mScreenshotView;
            Objects.requireNonNull(screenshotView);
            SwipeDismissHandler swipeDismissHandler = screenshotView.mSwipeDismissHandler;
            Objects.requireNonNull(swipeDismissHandler);
            ValueAnimator valueAnimator = swipeDismissHandler.mDismissAnimation;
            if (valueAnimator == null || !valueAnimator.isRunning()) {
                z3 = false;
            } else {
                z3 = true;
            }
            if (!z3) {
                this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_REENTERED, 0, this.mPackageName);
            }
            this.mScreenshotView.reset();
        }
        if (componentName == null) {
            str = "";
        } else {
            str = componentName.getPackageName();
        }
        this.mPackageName = str;
        ScreenshotView screenshotView2 = this.mScreenshotView;
        Objects.requireNonNull(screenshotView2);
        screenshotView2.mPackageName = str;
        this.mScreenshotView.updateOrientation(this.mWindowManager.getCurrentWindowMetrics().getWindowInsets());
        this.mScreenBitmap = bitmap;
        if (Settings.Secure.getInt(this.mContext.getContentResolver(), "user_setup_complete", 0) == 1) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (!z2) {
            Log.w("Screenshot", "User setup not complete, displaying toast only");
            MediaPlayer mediaPlayer = this.mCameraSound;
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
            saveScreenshotInWorkerThread(consumer, new ImageExporter$$ExternalSyntheticLambda1(this, consumer), null);
            return;
        }
        this.mScreenBitmap.setHasAlpha(false);
        this.mScreenBitmap.prepareToDraw();
        saveScreenshotInWorkerThread(consumer, new ActionsReadyListener() { // from class: com.android.systemui.screenshot.ScreenshotController$$ExternalSyntheticLambda2
            @Override // com.android.systemui.screenshot.ScreenshotController.ActionsReadyListener
            public final void onActionsReady(ScreenshotController.SavedImageData savedImageData) {
                ScreenshotController screenshotController = ScreenshotController.this;
                Objects.requireNonNull(screenshotController);
                screenshotController.logSuccessOnActionsReady(savedImageData);
                screenshotController.mScreenshotHandler.resetTimeout();
                if (savedImageData.uri != null) {
                    screenshotController.mScreenshotHandler.post(new NavBarTuner$$ExternalSyntheticLambda6(screenshotController, savedImageData, 2));
                }
            }
        }, new ScreenshotController$$ExternalSyntheticLambda4(this));
        setWindowFocusable(true);
        final BaseWifiTracker$$ExternalSyntheticLambda1 baseWifiTracker$$ExternalSyntheticLambda1 = new BaseWifiTracker$$ExternalSyntheticLambda1(this, 4);
        final View decorView = this.mWindow.getDecorView();
        if (decorView.isAttachedToWindow()) {
            baseWifiTracker$$ExternalSyntheticLambda1.run();
        } else {
            decorView.getViewTreeObserver().addOnWindowAttachListener(new ViewTreeObserver.OnWindowAttachListener() { // from class: com.android.systemui.screenshot.ScreenshotController.6
                @Override // android.view.ViewTreeObserver.OnWindowAttachListener
                public final void onWindowDetached() {
                }

                @Override // android.view.ViewTreeObserver.OnWindowAttachListener
                public final void onWindowAttached() {
                    ScreenshotController.this.mBlockAttach = false;
                    decorView.getViewTreeObserver().removeOnWindowAttachListener(this);
                    baseWifiTracker$$ExternalSyntheticLambda1.run();
                }
            });
        }
        attachWindow();
        this.mScreenshotView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: com.android.systemui.screenshot.ScreenshotController.5
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public final boolean onPreDraw() {
                int i;
                ScreenshotController.this.mScreenshotView.getViewTreeObserver().removeOnPreDrawListener(this);
                ScreenshotController screenshotController = ScreenshotController.this;
                Rect rect2 = rect;
                boolean z4 = z;
                Objects.requireNonNull(screenshotController);
                AnimatorSet animatorSet = screenshotController.mScreenshotAnimation;
                if (animatorSet != null && animatorSet.isRunning()) {
                    screenshotController.mScreenshotAnimation.cancel();
                }
                final ScreenshotView screenshotView3 = screenshotController.mScreenshotView;
                Objects.requireNonNull(screenshotView3);
                Rect rect3 = new Rect();
                screenshotView3.mScreenshotPreview.getHitRect(rect3);
                float f = screenshotView3.mFixedSize;
                if (screenshotView3.mOrientationPortrait) {
                    i = rect2.width();
                } else {
                    i = rect2.height();
                }
                float f2 = f / i;
                final float f3 = 1.0f / f2;
                AnimatorSet animatorSet2 = new AnimatorSet();
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                ofFloat.setDuration(133L);
                ofFloat.setInterpolator(screenshotView3.mFastOutSlowIn);
                ofFloat.addUpdateListener(new ScreenshotView$$ExternalSyntheticLambda2(screenshotView3, 0));
                ValueAnimator ofFloat2 = ValueAnimator.ofFloat(1.0f, 0.0f);
                ofFloat2.setDuration(217L);
                ofFloat2.setInterpolator(screenshotView3.mFastOutSlowIn);
                ofFloat2.addUpdateListener(new VolumeDialogImpl$$ExternalSyntheticLambda0(screenshotView3, 1));
                final PointF pointF = new PointF(rect2.centerX(), rect2.centerY());
                final PointF pointF2 = new PointF(rect3.exactCenterX(), rect3.exactCenterY());
                int[] locationOnScreen = screenshotView3.mScreenshotPreview.getLocationOnScreen();
                pointF.offset(rect3.left - locationOnScreen[0], rect3.top - locationOnScreen[1]);
                ValueAnimator ofFloat3 = ValueAnimator.ofFloat(0.0f, 1.0f);
                ofFloat3.setDuration(500L);
                ofFloat3.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.screenshot.ScreenshotView.4
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationStart(Animator animator) {
                        ScreenshotView.this.mScreenshotPreview.setScaleX(f3);
                        ScreenshotView.this.mScreenshotPreview.setScaleY(f3);
                        ScreenshotView.this.mScreenshotPreview.setVisibility(0);
                        if (ScreenshotView.this.mAccessibilityManager.isEnabled()) {
                            ScreenshotView.this.mDismissButton.setAlpha(0.0f);
                            ScreenshotView.this.mDismissButton.setVisibility(0);
                        }
                    }
                });
                ofFloat3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda7
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        ScreenshotView screenshotView4 = ScreenshotView.this;
                        float f4 = f3;
                        PointF pointF3 = pointF;
                        PointF pointF4 = pointF2;
                        int i2 = ScreenshotView.$r8$clinit;
                        Objects.requireNonNull(screenshotView4);
                        float animatedFraction = valueAnimator2.getAnimatedFraction();
                        int i3 = (animatedFraction > 0.468f ? 1 : (animatedFraction == 0.468f ? 0 : -1));
                        if (i3 < 0) {
                            float lerp = MathUtils.lerp(f4, 1.0f, screenshotView4.mFastOutSlowIn.getInterpolation(animatedFraction / 0.468f));
                            screenshotView4.mScreenshotPreview.setScaleX(lerp);
                            screenshotView4.mScreenshotPreview.setScaleY(lerp);
                        } else {
                            screenshotView4.mScreenshotPreview.setScaleX(1.0f);
                            screenshotView4.mScreenshotPreview.setScaleY(1.0f);
                        }
                        if (i3 < 0) {
                            float lerp2 = MathUtils.lerp(pointF3.x, pointF4.x, screenshotView4.mFastOutSlowIn.getInterpolation(animatedFraction / 0.468f));
                            ImageView imageView = screenshotView4.mScreenshotPreview;
                            imageView.setX(lerp2 - (imageView.getWidth() / 2.0f));
                        } else {
                            ImageView imageView2 = screenshotView4.mScreenshotPreview;
                            imageView2.setX(pointF4.x - (imageView2.getWidth() / 2.0f));
                        }
                        float lerp3 = MathUtils.lerp(pointF3.y, pointF4.y, screenshotView4.mFastOutSlowIn.getInterpolation(animatedFraction));
                        ImageView imageView3 = screenshotView4.mScreenshotPreview;
                        imageView3.setY(lerp3 - (imageView3.getHeight() / 2.0f));
                        if (animatedFraction >= 0.4f) {
                            screenshotView4.mDismissButton.setAlpha((animatedFraction - 0.4f) / 0.6f);
                            float x = screenshotView4.mScreenshotPreview.getX();
                            float y = screenshotView4.mScreenshotPreview.getY();
                            FrameLayout frameLayout = screenshotView4.mDismissButton;
                            frameLayout.setY(y - (frameLayout.getHeight() / 2.0f));
                            if (screenshotView4.mDirectionLTR) {
                                screenshotView4.mDismissButton.setX((x + screenshotView4.mScreenshotPreview.getWidth()) - (screenshotView4.mDismissButton.getWidth() / 2.0f));
                                return;
                            }
                            FrameLayout frameLayout2 = screenshotView4.mDismissButton;
                            frameLayout2.setX(x - (frameLayout2.getWidth() / 2.0f));
                        }
                    }
                });
                screenshotView3.mScreenshotFlash.setAlpha(0.0f);
                screenshotView3.mScreenshotFlash.setVisibility(0);
                ValueAnimator ofFloat4 = ValueAnimator.ofFloat(0.0f, 1.0f);
                ofFloat4.setDuration(100L);
                ofFloat4.addUpdateListener(new ScreenshotView$$ExternalSyntheticLambda0(screenshotView3, 0));
                if (z4) {
                    animatorSet2.play(ofFloat2).after(ofFloat);
                    animatorSet2.play(ofFloat2).with(ofFloat3);
                } else {
                    animatorSet2.play(ofFloat3);
                }
                animatorSet2.play(ofFloat4).after(ofFloat3);
                animatorSet2.addListener(new ScreenshotView.AnonymousClass5(pointF2, rect2, f2));
                screenshotController.mScreenshotAnimation = animatorSet2;
                MediaPlayer mediaPlayer2 = screenshotController.mCameraSound;
                if (mediaPlayer2 != null) {
                    mediaPlayer2.start();
                }
                screenshotController.mScreenshotAnimation.start();
                return true;
            }
        });
        ScreenshotView screenshotView3 = this.mScreenshotView;
        Bitmap bitmap2 = this.mScreenBitmap;
        Objects.requireNonNull(screenshotView3);
        ImageView imageView = screenshotView3.mScreenshotPreview;
        Resources resources = screenshotView3.mResources;
        int width = (bitmap2.getWidth() - insets.left) - insets.right;
        int height = (bitmap2.getHeight() - insets.top) - insets.bottom;
        Drawable bitmapDrawable = new BitmapDrawable(resources, bitmap2);
        if (height == 0 || width == 0 || bitmap2.getWidth() == 0 || bitmap2.getHeight() == 0) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Can't create inset drawable, using 0 insets bitmap and insets create degenerate region: ");
            m.append(bitmap2.getWidth());
            m.append("x");
            m.append(bitmap2.getHeight());
            m.append(" ");
            m.append(bitmapDrawable);
            Log.e("Screenshot", m.toString());
        } else {
            float f = width;
            float f2 = height;
            InsetDrawable insetDrawable = new InsetDrawable(bitmapDrawable, (insets.left * (-1.0f)) / f, (insets.top * (-1.0f)) / f2, (insets.right * (-1.0f)) / f, (insets.bottom * (-1.0f)) / f2);
            if (insets.left < 0 || insets.top < 0 || insets.right < 0 || insets.bottom < 0) {
                bitmapDrawable = new LayerDrawable(new Drawable[]{new ColorDrawable(-16777216), insetDrawable});
            } else {
                bitmapDrawable = insetDrawable;
            }
        }
        imageView.setImageDrawable(bitmapDrawable);
        this.mWindow.setContentView(this.mScreenshotView);
        this.mWindow.getDecorView().setOnApplyWindowInsetsListener(ScreenshotController$$ExternalSyntheticLambda0.INSTANCE);
        TimeoutHandler timeoutHandler = this.mScreenshotHandler;
        Objects.requireNonNull(timeoutHandler);
        timeoutHandler.removeMessages(2);
    }

    /* JADX WARN: Type inference failed for: r5v0, types: [com.android.systemui.screenshot.ScreenshotController$$ExternalSyntheticLambda8] */
    public final void saveScreenshotInWorkerThread(Consumer consumer, ActionsReadyListener actionsReadyListener, ScreenshotController$$ExternalSyntheticLambda4 screenshotController$$ExternalSyntheticLambda4) {
        SaveImageInBackgroundData saveImageInBackgroundData = new SaveImageInBackgroundData();
        saveImageInBackgroundData.image = this.mScreenBitmap;
        saveImageInBackgroundData.finisher = consumer;
        saveImageInBackgroundData.mActionsReadyListener = actionsReadyListener;
        saveImageInBackgroundData.mQuickShareActionsReadyListener = screenshotController$$ExternalSyntheticLambda4;
        SaveImageInBackgroundTask saveImageInBackgroundTask = this.mSaveInBgTask;
        if (saveImageInBackgroundTask != null) {
            saveImageInBackgroundTask.mParams.mActionsReadyListener = new ScreenshotController$$ExternalSyntheticLambda3(this);
        }
        SaveImageInBackgroundTask saveImageInBackgroundTask2 = new SaveImageInBackgroundTask(this.mContext, this.mImageExporter, this.mScreenshotSmartActions, saveImageInBackgroundData, new Supplier() { // from class: com.android.systemui.screenshot.ScreenshotController$$ExternalSyntheticLambda8
            @Override // java.util.function.Supplier
            public final Object get() {
                ScreenshotController screenshotController = ScreenshotController.this;
                Objects.requireNonNull(screenshotController);
                PhoneWindow phoneWindow = screenshotController.mWindow;
                ScreenshotController.ScreenshotExitTransitionCallbacksSupplier.AnonymousClass1 r2 = new ScreenshotController.ScreenshotExitTransitionCallbacksSupplier.AnonymousClass1();
                ScreenshotView screenshotView = screenshotController.mScreenshotView;
                Objects.requireNonNull(screenshotView);
                Pair startSharedElementAnimation = ActivityOptions.startSharedElementAnimation(phoneWindow, r2, null, new Pair[]{Pair.create(screenshotView.mScreenshotPreview, "screenshot_preview_image")});
                ((ExitTransitionCoordinator) startSharedElementAnimation.second).startExit();
                ScreenshotController.SavedImageData.ActionTransition actionTransition = new ScreenshotController.SavedImageData.ActionTransition();
                actionTransition.bundle = ((ActivityOptions) startSharedElementAnimation.first).toBundle();
                actionTransition.onCancelRunnable = new ScreenDecorations$$ExternalSyntheticLambda2(screenshotController, 4);
                return actionTransition;
            }
        });
        this.mSaveInBgTask = saveImageInBackgroundTask2;
        saveImageInBackgroundTask2.execute(new Void[0]);
    }

    public final void setWindowFocusable(boolean z) {
        View peekDecorView;
        WindowManager.LayoutParams layoutParams = this.mWindowLayoutParams;
        int i = layoutParams.flags;
        if (z) {
            layoutParams.flags = i & (-9);
        } else {
            layoutParams.flags = i | 8;
        }
        if (layoutParams.flags != i && (peekDecorView = this.mWindow.peekDecorView()) != null && peekDecorView.isAttachedToWindow()) {
            this.mWindowManager.updateViewLayout(peekDecorView, this.mWindowLayoutParams);
        }
    }

    public final void takeScreenshotInternal(ComponentName componentName, Consumer<Uri> consumer, Rect rect) {
        boolean z;
        if (this.mContext.getResources().getConfiguration().orientation == 1) {
            z = true;
        } else {
            z = false;
        }
        this.mScreenshotTakenInPortrait = z;
        Rect rect2 = new Rect(rect);
        Bitmap captureScreenshot = captureScreenshot(rect);
        if (captureScreenshot == null) {
            Log.e("Screenshot", "takeScreenshotInternal: Screenshot bitmap was null");
            this.mNotificationsController.notifyScreenshotError(2131953225);
            TakeScreenshotService.RequestCallback requestCallback = this.mCurrentRequestCallback;
            if (requestCallback != null) {
                TakeScreenshotService.RequestCallbackImpl requestCallbackImpl = (TakeScreenshotService.RequestCallbackImpl) requestCallback;
                Messenger messenger = requestCallbackImpl.mReplyTo;
                int i = TakeScreenshotService.$r8$clinit;
                try {
                    messenger.send(Message.obtain(null, 1, null));
                } catch (RemoteException e) {
                    Log.d("Screenshot", "ignored remote exception", e);
                }
                try {
                    requestCallbackImpl.mReplyTo.send(Message.obtain((Handler) null, 2));
                } catch (RemoteException e2) {
                    Log.d("Screenshot", "ignored remote exception", e2);
                }
            }
        } else {
            saveScreenshot(captureScreenshot, consumer, rect2, Insets.NONE, componentName, true);
            this.mContext.sendBroadcast(new Intent("com.android.systemui.SCREENSHOT"), "com.android.systemui.permission.SELF");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r8v4, types: [com.android.systemui.screenshot.ScreenshotController$2, android.content.BroadcastReceiver] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public ScreenshotController(android.content.Context r3, com.android.systemui.screenshot.ScreenshotSmartActions r4, com.android.systemui.screenshot.ScreenshotNotificationsController r5, com.android.systemui.screenshot.ScrollCaptureClient r6, com.android.internal.logging.UiEventLogger r7, com.android.systemui.screenshot.ImageExporter r8, java.util.concurrent.Executor r9, com.android.systemui.screenshot.ScrollCaptureController r10, com.android.systemui.screenshot.LongScreenshotData r11, android.app.ActivityManager r12, com.android.systemui.screenshot.TimeoutHandler r13) {
        /*
            Method dump skipped, instructions count: 273
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.screenshot.ScreenshotController.<init>(android.content.Context, com.android.systemui.screenshot.ScreenshotSmartActions, com.android.systemui.screenshot.ScreenshotNotificationsController, com.android.systemui.screenshot.ScrollCaptureClient, com.android.internal.logging.UiEventLogger, com.android.systemui.screenshot.ImageExporter, java.util.concurrent.Executor, com.android.systemui.screenshot.ScrollCaptureController, com.android.systemui.screenshot.LongScreenshotData, android.app.ActivityManager, com.android.systemui.screenshot.TimeoutHandler):void");
    }

    public final Bitmap captureScreenshot(Rect rect) {
        int width = rect.width();
        int height = rect.height();
        Display display = this.mDisplayManager.getDisplay(0);
        DisplayAddress.Physical address = display.getAddress();
        if (!(address instanceof DisplayAddress.Physical)) {
            Log.e("Screenshot", "Skipping Screenshot - Default display does not have a physical address: " + display);
            return null;
        }
        SurfaceControl.ScreenshotHardwareBuffer captureDisplay = SurfaceControl.captureDisplay(new SurfaceControl.DisplayCaptureArgs.Builder(SurfaceControl.getPhysicalDisplayToken(address.getPhysicalDisplayId())).setSourceCrop(rect).setSize(width, height).build());
        if (captureDisplay == null) {
            return null;
        }
        return captureDisplay.asBitmap();
    }
}
