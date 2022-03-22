package com.android.systemui.screenshot;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Insets;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Icon;
import android.os.Looper;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Choreographer;
import android.view.DisplayCutout;
import android.view.GestureDetector;
import android.view.InputEvent;
import android.view.InputMonitor;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.leanback.R$color;
import com.android.internal.logging.UiEventLogger;
import com.android.keyguard.KeyguardVisibilityHelper$$ExternalSyntheticLambda0;
import com.android.systemui.ScreenDecorations$$ExternalSyntheticLambda2;
import com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda0;
import com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda6;
import com.android.systemui.screenshot.ScreenshotController;
import com.android.systemui.screenshot.ScreenshotView;
import com.android.systemui.screenshot.SwipeDismissHandler;
import com.android.systemui.shared.system.InputChannelCompat$InputEventListener;
import com.android.systemui.shared.system.InputChannelCompat$InputEventReceiver;
import com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda5;
import com.android.wm.shell.pip.phone.PipMenuView$$ExternalSyntheticLambda5;
import com.google.android.apps.miphone.aiai.matchmaker.overview.ui.SuggestController$$ExternalSyntheticLambda1;
import com.google.android.setupcompat.util.Logger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public class ScreenshotView extends FrameLayout implements ViewTreeObserver.OnComputeInternalInsetsListener {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final AccessibilityManager mAccessibilityManager;
    public HorizontalScrollView mActionsContainer;
    public ImageView mActionsContainerBackground;
    public LinearLayout mActionsView;
    public ImageView mBackgroundProtection;
    public ScreenshotViewCallback mCallbacks;
    public boolean mDirectionLTR;
    public FrameLayout mDismissButton;
    public final DisplayMetrics mDisplayMetrics;
    public OverlayActionChip mEditChip;
    public final Interpolator mFastOutSlowIn;
    public final float mFixedSize;
    public InputChannelCompat$InputEventReceiver mInputEventReceiver;
    public Logger mInputMonitor;
    public int mNavMode;
    public boolean mOrientationPortrait;
    public String mPackageName;
    public PendingInteraction mPendingInteraction;
    public boolean mPendingSharedTransition;
    public OverlayActionChip mQuickShareChip;
    public final Resources mResources;
    public ImageView mScreenshotFlash;
    public ImageView mScreenshotPreview;
    public View mScreenshotPreviewBorder;
    public ScreenshotSelectorView mScreenshotSelectorView;
    public View mScreenshotStatic;
    public OverlayActionChip mScrollChip;
    public ImageView mScrollablePreview;
    public ImageView mScrollingScrim;
    public OverlayActionChip mShareChip;
    public boolean mShowScrollablePreview;
    public final ArrayList<OverlayActionChip> mSmartChips;
    public final GestureDetector mSwipeDetector;
    public SwipeDismissHandler mSwipeDismissHandler;
    public UiEventLogger mUiEventLogger;

    /* renamed from: com.android.systemui.screenshot.ScreenshotView$5  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass5 extends AnimatorListenerAdapter {
        public static final /* synthetic */ int $r8$clinit = 0;
        public final /* synthetic */ Rect val$bounds;
        public final /* synthetic */ float val$cornerScale;
        public final /* synthetic */ PointF val$finalPos;

        public AnonymousClass5(PointF pointF, Rect rect, float f) {
            this.val$finalPos = pointF;
            this.val$bounds = rect;
            this.val$cornerScale = f;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationEnd(Animator animator) {
            float f;
            ScreenshotView.this.mDismissButton.setOnClickListener(new PipMenuView$$ExternalSyntheticLambda5(this, 1));
            ScreenshotView.this.mDismissButton.setAlpha(1.0f);
            float width = ScreenshotView.this.mDismissButton.getWidth() / 2.0f;
            if (ScreenshotView.this.mDirectionLTR) {
                f = ((this.val$bounds.width() * this.val$cornerScale) / 2.0f) + (this.val$finalPos.x - width);
            } else {
                f = (this.val$finalPos.x - width) - ((this.val$bounds.width() * this.val$cornerScale) / 2.0f);
            }
            ScreenshotView.this.mDismissButton.setX(f);
            ScreenshotView.this.mDismissButton.setY((this.val$finalPos.y - width) - ((this.val$bounds.height() * this.val$cornerScale) / 2.0f));
            ScreenshotView.this.mScreenshotPreview.setScaleX(1.0f);
            ScreenshotView.this.mScreenshotPreview.setScaleY(1.0f);
            ImageView imageView = ScreenshotView.this.mScreenshotPreview;
            imageView.setX(this.val$finalPos.x - (imageView.getWidth() / 2.0f));
            ImageView imageView2 = ScreenshotView.this.mScreenshotPreview;
            imageView2.setY(this.val$finalPos.y - (imageView2.getHeight() / 2.0f));
            ScreenshotView.this.requestLayout();
            ScreenshotView.this.createScreenshotActionsShadeAnimation().start();
            ScreenshotView screenshotView = ScreenshotView.this;
            screenshotView.setOnTouchListener(screenshotView.mSwipeDismissHandler);
        }
    }

    /* loaded from: classes.dex */
    public enum PendingInteraction {
        PREVIEW,
        EDIT,
        SHARE,
        QUICK_SHARE
    }

    /* loaded from: classes.dex */
    public interface ScreenshotViewCallback {
    }

    public ScreenshotView(Context context) {
        this(context, null);
    }

    public final void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
        internalInsetsInfo.setTouchableInsets(3);
        internalInsetsInfo.touchableRegion.set(getTouchRegion(true));
    }

    public final void startSharedTransition(ScreenshotController.SavedImageData.ActionTransition actionTransition) {
        try {
            this.mPendingSharedTransition = true;
            actionTransition.action.actionIntent.send();
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda4
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ScreenshotView screenshotView = ScreenshotView.this;
                    int i = ScreenshotView.$r8$clinit;
                    Objects.requireNonNull(screenshotView);
                    float animatedFraction = 1.0f - valueAnimator.getAnimatedFraction();
                    screenshotView.mDismissButton.setAlpha(animatedFraction);
                    screenshotView.mActionsContainerBackground.setAlpha(animatedFraction);
                    screenshotView.mActionsContainer.setAlpha(animatedFraction);
                    screenshotView.mBackgroundProtection.setAlpha(animatedFraction);
                    screenshotView.mScreenshotPreviewBorder.setAlpha(animatedFraction);
                }
            });
            ofFloat.setDuration(600L);
            ofFloat.start();
        } catch (PendingIntent.CanceledException e) {
            this.mPendingSharedTransition = false;
            ScreenDecorations$$ExternalSyntheticLambda2 screenDecorations$$ExternalSyntheticLambda2 = actionTransition.onCancelRunnable;
            if (screenDecorations$$ExternalSyntheticLambda2 != null) {
                screenDecorations$$ExternalSyntheticLambda2.run();
            }
            Log.e("Screenshot", "Intent cancelled", e);
        }
    }

    public ScreenshotView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public final void addQuickShareChip(Notification.Action action) {
        if (this.mPendingInteraction == null) {
            OverlayActionChip overlayActionChip = (OverlayActionChip) LayoutInflater.from(((FrameLayout) this).mContext).inflate(2131624350, (ViewGroup) this.mActionsView, false);
            this.mQuickShareChip = overlayActionChip;
            overlayActionChip.setText(action.title);
            this.mQuickShareChip.setIcon(action.getIcon(), false);
            this.mQuickShareChip.setOnClickListener(new ScreenshotView$$ExternalSyntheticLambda9(this, 0));
            this.mQuickShareChip.setAlpha(1.0f);
            this.mActionsView.addView(this.mQuickShareChip);
            this.mSmartChips.add(this.mQuickShareChip);
        }
    }

    public final Region getSwipeRegion() {
        Region region = new Region();
        Rect rect = new Rect();
        this.mScreenshotPreview.getBoundsOnScreen(rect);
        rect.inset((int) FloatingWindowUtil.dpToPx(this.mDisplayMetrics, -12.0f), (int) FloatingWindowUtil.dpToPx(this.mDisplayMetrics, -12.0f));
        region.op(rect, Region.Op.UNION);
        this.mActionsContainerBackground.getBoundsOnScreen(rect);
        rect.inset((int) FloatingWindowUtil.dpToPx(this.mDisplayMetrics, -12.0f), (int) FloatingWindowUtil.dpToPx(this.mDisplayMetrics, -12.0f));
        region.op(rect, Region.Op.UNION);
        this.mDismissButton.getBoundsOnScreen(rect);
        region.op(rect, Region.Op.UNION);
        return region;
    }

    public final void reset() {
        boolean z;
        SwipeDismissHandler swipeDismissHandler = this.mSwipeDismissHandler;
        Objects.requireNonNull(swipeDismissHandler);
        ValueAnimator valueAnimator = swipeDismissHandler.mDismissAnimation;
        if (valueAnimator == null || !valueAnimator.isRunning()) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            swipeDismissHandler.mDismissAnimation.cancel();
        }
        getViewTreeObserver().removeOnComputeInternalInsetsListener(this);
        this.mScreenshotPreview.setImageDrawable(null);
        this.mScreenshotPreview.setVisibility(4);
        this.mScreenshotPreviewBorder.setAlpha(0.0f);
        this.mPendingSharedTransition = false;
        this.mActionsContainerBackground.setVisibility(8);
        this.mActionsContainer.setVisibility(8);
        this.mBackgroundProtection.setAlpha(0.0f);
        this.mDismissButton.setVisibility(8);
        this.mScrollingScrim.setVisibility(8);
        this.mScrollablePreview.setVisibility(8);
        this.mScreenshotStatic.setTranslationX(0.0f);
        this.mScreenshotPreview.setContentDescription(((FrameLayout) this).mContext.getResources().getString(2131953230));
        this.mScreenshotPreview.setOnClickListener(null);
        this.mShareChip.setOnClickListener(null);
        this.mScrollingScrim.setVisibility(8);
        this.mEditChip.setOnClickListener(null);
        this.mShareChip.setIsPending(false);
        this.mEditChip.setIsPending(false);
        this.mPendingInteraction = null;
        Iterator<OverlayActionChip> it = this.mSmartChips.iterator();
        while (it.hasNext()) {
            this.mActionsView.removeView(it.next());
        }
        this.mSmartChips.clear();
        this.mQuickShareChip = null;
        setAlpha(1.0f);
        ScreenshotSelectorView screenshotSelectorView = this.mScreenshotSelectorView;
        Objects.requireNonNull(screenshotSelectorView);
        if (screenshotSelectorView.mSelectionRect != null) {
            screenshotSelectorView.mStartPoint = null;
            screenshotSelectorView.mSelectionRect = null;
        }
    }

    public final void restoreNonScrollingUi() {
        this.mScrollChip.setVisibility(8);
        this.mScrollablePreview.setVisibility(8);
        this.mScrollingScrim.setVisibility(8);
        if (this.mAccessibilityManager.isEnabled()) {
            this.mDismissButton.setVisibility(0);
        }
        this.mActionsContainer.setVisibility(0);
        this.mBackgroundProtection.setVisibility(0);
        this.mActionsContainerBackground.setVisibility(0);
        this.mScreenshotPreviewBorder.setVisibility(0);
        this.mScreenshotPreview.setVisibility(0);
        ScreenshotController.AnonymousClass3 r2 = (ScreenshotController.AnonymousClass3) this.mCallbacks;
        Objects.requireNonNull(r2);
        ScreenshotController.this.mScreenshotHandler.resetTimeout();
    }

    public final void setChipIntents(final ScreenshotController.SavedImageData savedImageData) {
        this.mShareChip.setOnClickListener(new ScreenshotView$$ExternalSyntheticLambda12(this, savedImageData, 0));
        this.mEditChip.setOnClickListener(new ScreenshotView$$ExternalSyntheticLambda11(this, savedImageData, 0));
        this.mScreenshotPreview.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda14
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ScreenshotView screenshotView = ScreenshotView.this;
                ScreenshotController.SavedImageData savedImageData2 = savedImageData;
                int i = ScreenshotView.$r8$clinit;
                Objects.requireNonNull(screenshotView);
                screenshotView.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_PREVIEW_TAPPED, 0, screenshotView.mPackageName);
                screenshotView.startSharedTransition(savedImageData2.editTransition.get());
            }
        });
        OverlayActionChip overlayActionChip = this.mQuickShareChip;
        if (overlayActionChip != null) {
            overlayActionChip.setOnClickListener(new OverlayActionChip$$ExternalSyntheticLambda0(savedImageData.quickShareAction.actionIntent, new KeyguardVisibilityHelper$$ExternalSyntheticLambda0(this, 3)));
        }
        PendingInteraction pendingInteraction = this.mPendingInteraction;
        if (pendingInteraction != null) {
            int ordinal = pendingInteraction.ordinal();
            if (ordinal == 0) {
                this.mScreenshotPreview.callOnClick();
            } else if (ordinal == 1) {
                this.mEditChip.callOnClick();
            } else if (ordinal == 2) {
                this.mShareChip.callOnClick();
            } else if (ordinal == 3) {
                this.mQuickShareChip.callOnClick();
            }
        } else {
            LayoutInflater from = LayoutInflater.from(((FrameLayout) this).mContext);
            Iterator it = savedImageData.smartActions.iterator();
            while (it.hasNext()) {
                Notification.Action action = (Notification.Action) it.next();
                OverlayActionChip overlayActionChip2 = (OverlayActionChip) from.inflate(2131624350, (ViewGroup) this.mActionsView, false);
                overlayActionChip2.setText(action.title);
                overlayActionChip2.setIcon(action.getIcon(), false);
                overlayActionChip2.setOnClickListener(new OverlayActionChip$$ExternalSyntheticLambda0(action.actionIntent, new SuggestController$$ExternalSyntheticLambda1(this, 3)));
                overlayActionChip2.setAlpha(1.0f);
                this.mActionsView.addView(overlayActionChip2);
                this.mSmartChips.add(overlayActionChip2);
            }
        }
    }

    public final void showScrollChip(String str, BubbleController$$ExternalSyntheticLambda5 bubbleController$$ExternalSyntheticLambda5) {
        this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_IMPRESSION, 0, str);
        this.mScrollChip.setVisibility(0);
        this.mScrollChip.setOnClickListener(new MediaControlPanel$$ExternalSyntheticLambda6(this, str, bubbleController$$ExternalSyntheticLambda5, 1));
    }

    public final void stopInputListening() {
        Logger logger = this.mInputMonitor;
        if (logger != null) {
            ((InputMonitor) logger.prefix).dispose();
            this.mInputMonitor = null;
        }
        InputChannelCompat$InputEventReceiver inputChannelCompat$InputEventReceiver = this.mInputEventReceiver;
        if (inputChannelCompat$InputEventReceiver != null) {
            inputChannelCompat$InputEventReceiver.mReceiver.dispose();
            this.mInputEventReceiver = null;
        }
    }

    public final void updateInsets(WindowInsets windowInsets) {
        boolean z = true;
        if (((FrameLayout) this).mContext.getResources().getConfiguration().orientation != 1) {
            z = false;
        }
        this.mOrientationPortrait = z;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mScreenshotStatic.getLayoutParams();
        DisplayCutout displayCutout = windowInsets.getDisplayCutout();
        Insets insets = windowInsets.getInsets(WindowInsets.Type.navigationBars());
        if (displayCutout == null) {
            layoutParams.setMargins(0, 0, 0, insets.bottom);
        } else {
            Insets waterfallInsets = displayCutout.getWaterfallInsets();
            if (this.mOrientationPortrait) {
                layoutParams.setMargins(waterfallInsets.left, Math.max(displayCutout.getSafeInsetTop(), waterfallInsets.top), waterfallInsets.right, Math.max(displayCutout.getSafeInsetBottom(), Math.max(insets.bottom, waterfallInsets.bottom)));
            } else {
                layoutParams.setMargins(Math.max(displayCutout.getSafeInsetLeft(), waterfallInsets.left), waterfallInsets.top, Math.max(displayCutout.getSafeInsetRight(), waterfallInsets.right), Math.max(insets.bottom, waterfallInsets.bottom));
            }
        }
        this.mScreenshotStatic.setLayoutParams(layoutParams);
        this.mScreenshotStatic.requestLayout();
    }

    public final void updateOrientation(WindowInsets windowInsets) {
        boolean z = true;
        if (((FrameLayout) this).mContext.getResources().getConfiguration().orientation != 1) {
            z = false;
        }
        this.mOrientationPortrait = z;
        updateInsets(windowInsets);
        ViewGroup.LayoutParams layoutParams = this.mScreenshotPreview.getLayoutParams();
        if (this.mOrientationPortrait) {
            layoutParams.width = (int) this.mFixedSize;
            layoutParams.height = -2;
            this.mScreenshotPreview.setScaleType(ImageView.ScaleType.FIT_START);
        } else {
            layoutParams.width = -2;
            layoutParams.height = (int) this.mFixedSize;
            this.mScreenshotPreview.setScaleType(ImageView.ScaleType.FIT_END);
        }
        this.mScreenshotPreview.setLayoutParams(layoutParams);
    }

    public static /* synthetic */ void $r8$lambda$b5uTZBQem2CWFC7azy4AO9g0ukw(ScreenshotView screenshotView) {
        Objects.requireNonNull(screenshotView);
        screenshotView.mShareChip.setIsPending(true);
        screenshotView.mEditChip.setIsPending(false);
        OverlayActionChip overlayActionChip = screenshotView.mQuickShareChip;
        if (overlayActionChip != null) {
            overlayActionChip.setIsPending(false);
        }
        screenshotView.mPendingInteraction = PendingInteraction.SHARE;
    }

    public ScreenshotView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public final ValueAnimator createScreenshotActionsShadeAnimation() {
        try {
            ActivityManager.getService().resumeAppSwitches();
        } catch (RemoteException unused) {
        }
        final ArrayList arrayList = new ArrayList();
        this.mShareChip.setContentDescription(((FrameLayout) this).mContext.getString(2131953235));
        this.mShareChip.setIcon(Icon.createWithResource(((FrameLayout) this).mContext, 2131232256), true);
        this.mShareChip.setOnClickListener(new MediaControlPanel$$ExternalSyntheticLambda0(this, 1));
        arrayList.add(this.mShareChip);
        this.mEditChip.setContentDescription(((FrameLayout) this).mContext.getString(2131953222));
        this.mEditChip.setIcon(Icon.createWithResource(((FrameLayout) this).mContext, 2131232254), true);
        this.mEditChip.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda13
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ScreenshotView screenshotView = ScreenshotView.this;
                int i = ScreenshotView.$r8$clinit;
                Objects.requireNonNull(screenshotView);
                screenshotView.mEditChip.setIsPending(true);
                screenshotView.mShareChip.setIsPending(false);
                OverlayActionChip overlayActionChip = screenshotView.mQuickShareChip;
                if (overlayActionChip != null) {
                    overlayActionChip.setIsPending(false);
                }
                screenshotView.mPendingInteraction = ScreenshotView.PendingInteraction.EDIT;
            }
        });
        arrayList.add(this.mEditChip);
        this.mScreenshotPreview.setOnClickListener(new ScreenshotView$$ExternalSyntheticLambda10(this, 0));
        this.mScrollChip.setText(((FrameLayout) this).mContext.getString(2131953234));
        this.mScrollChip.setIcon(Icon.createWithResource(((FrameLayout) this).mContext, 2131232255), true);
        arrayList.add(this.mScrollChip);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mActionsView.getChildAt(0).getLayoutParams();
        layoutParams.setMarginEnd(0);
        this.mActionsView.getChildAt(0).setLayoutParams(layoutParams);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setDuration(400L);
        this.mActionsContainer.setAlpha(0.0f);
        this.mActionsContainerBackground.setAlpha(0.0f);
        this.mActionsContainer.setVisibility(0);
        this.mActionsContainerBackground.setVisibility(0);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda8
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                float f;
                int i;
                float f2;
                ScreenshotView screenshotView = ScreenshotView.this;
                ArrayList arrayList2 = arrayList;
                int i2 = ScreenshotView.$r8$clinit;
                Objects.requireNonNull(screenshotView);
                float animatedFraction = valueAnimator.getAnimatedFraction();
                screenshotView.mBackgroundProtection.setAlpha(animatedFraction);
                if (animatedFraction < 0.25f) {
                    f = animatedFraction / 0.25f;
                } else {
                    f = 1.0f;
                }
                screenshotView.mActionsContainer.setAlpha(f);
                screenshotView.mActionsContainerBackground.setAlpha(f);
                float f3 = (0.3f * animatedFraction) + 0.7f;
                screenshotView.mActionsContainer.setScaleX(f3);
                screenshotView.mActionsContainerBackground.setScaleX(f3);
                Iterator it = arrayList2.iterator();
                while (it.hasNext()) {
                    OverlayActionChip overlayActionChip = (OverlayActionChip) it.next();
                    overlayActionChip.setAlpha(animatedFraction);
                    overlayActionChip.setScaleX(1.0f / f3);
                }
                HorizontalScrollView horizontalScrollView = screenshotView.mActionsContainer;
                if (screenshotView.mDirectionLTR) {
                    i = 0;
                } else {
                    i = horizontalScrollView.getWidth();
                }
                horizontalScrollView.setScrollX(i);
                HorizontalScrollView horizontalScrollView2 = screenshotView.mActionsContainer;
                float f4 = 0.0f;
                if (screenshotView.mDirectionLTR) {
                    f2 = 0.0f;
                } else {
                    f2 = horizontalScrollView2.getWidth();
                }
                horizontalScrollView2.setPivotX(f2);
                ImageView imageView = screenshotView.mActionsContainerBackground;
                if (!screenshotView.mDirectionLTR) {
                    f4 = imageView.getWidth();
                }
                imageView.setPivotX(f4);
            }
        });
        return ofFloat;
    }

    public final Region getTouchRegion(boolean z) {
        Region swipeRegion = getSwipeRegion();
        if (z && this.mScrollingScrim.getVisibility() == 0) {
            Rect rect = new Rect();
            this.mScrollingScrim.getBoundsOnScreen(rect);
            swipeRegion.op(rect, Region.Op.UNION);
        }
        if (R$color.isGesturalMode(this.mNavMode)) {
            Insets insets = ((WindowManager) ((FrameLayout) this).mContext.getSystemService(WindowManager.class)).getCurrentWindowMetrics().getWindowInsets().getInsets(WindowInsets.Type.systemGestures());
            Rect rect2 = new Rect(0, 0, insets.left, this.mDisplayMetrics.heightPixels);
            swipeRegion.op(rect2, Region.Op.UNION);
            DisplayMetrics displayMetrics = this.mDisplayMetrics;
            int i = displayMetrics.widthPixels;
            rect2.set(i - insets.right, 0, i, displayMetrics.heightPixels);
            swipeRegion.op(rect2, Region.Op.UNION);
        }
        return swipeRegion;
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        boolean z;
        ImageView imageView = (ImageView) findViewById(2131428781);
        Objects.requireNonNull(imageView);
        this.mScrollingScrim = imageView;
        View findViewById = findViewById(2131428784);
        Objects.requireNonNull(findViewById);
        this.mScreenshotStatic = findViewById;
        ImageView imageView2 = (ImageView) findViewById(2131428775);
        Objects.requireNonNull(imageView2);
        this.mScreenshotPreview = imageView2;
        View findViewById2 = findViewById(2131428776);
        Objects.requireNonNull(findViewById2);
        this.mScreenshotPreviewBorder = findViewById2;
        this.mScreenshotPreview.setClipToOutline(true);
        ImageView imageView3 = (ImageView) findViewById(2131428769);
        Objects.requireNonNull(imageView3);
        this.mActionsContainerBackground = imageView3;
        HorizontalScrollView horizontalScrollView = (HorizontalScrollView) findViewById(2131428768);
        Objects.requireNonNull(horizontalScrollView);
        this.mActionsContainer = horizontalScrollView;
        LinearLayout linearLayout = (LinearLayout) findViewById(2131428766);
        Objects.requireNonNull(linearLayout);
        this.mActionsView = linearLayout;
        ImageView imageView4 = (ImageView) findViewById(2131428767);
        Objects.requireNonNull(imageView4);
        this.mBackgroundProtection = imageView4;
        FrameLayout frameLayout = (FrameLayout) findViewById(2131428770);
        Objects.requireNonNull(frameLayout);
        this.mDismissButton = frameLayout;
        ImageView imageView5 = (ImageView) findViewById(2131428780);
        Objects.requireNonNull(imageView5);
        this.mScrollablePreview = imageView5;
        ImageView imageView6 = (ImageView) findViewById(2131428773);
        Objects.requireNonNull(imageView6);
        this.mScreenshotFlash = imageView6;
        ScreenshotSelectorView screenshotSelectorView = (ScreenshotSelectorView) findViewById(2131428782);
        Objects.requireNonNull(screenshotSelectorView);
        this.mScreenshotSelectorView = screenshotSelectorView;
        OverlayActionChip overlayActionChip = (OverlayActionChip) this.mActionsContainer.findViewById(2131428783);
        Objects.requireNonNull(overlayActionChip);
        this.mShareChip = overlayActionChip;
        OverlayActionChip overlayActionChip2 = (OverlayActionChip) this.mActionsContainer.findViewById(2131428772);
        Objects.requireNonNull(overlayActionChip2);
        this.mEditChip = overlayActionChip2;
        OverlayActionChip overlayActionChip3 = (OverlayActionChip) this.mActionsContainer.findViewById(2131428779);
        Objects.requireNonNull(overlayActionChip3);
        this.mScrollChip = overlayActionChip3;
        int dpToPx = (int) FloatingWindowUtil.dpToPx(this.mDisplayMetrics, 12.0f);
        this.mScreenshotPreview.setTouchDelegate(new TouchDelegate(new Rect(dpToPx, dpToPx, dpToPx, dpToPx), this.mScreenshotPreview));
        this.mActionsContainerBackground.setTouchDelegate(new TouchDelegate(new Rect(dpToPx, dpToPx, dpToPx, dpToPx), this.mActionsContainerBackground));
        setFocusable(true);
        this.mScreenshotSelectorView.setFocusable(true);
        this.mScreenshotSelectorView.setFocusableInTouchMode(true);
        boolean z2 = false;
        this.mActionsContainer.setScrollX(0);
        this.mNavMode = getResources().getInteger(17694878);
        if (getResources().getConfiguration().orientation == 1) {
            z = true;
        } else {
            z = false;
        }
        this.mOrientationPortrait = z;
        if (getResources().getConfiguration().getLayoutDirection() == 0) {
            z2 = true;
        }
        this.mDirectionLTR = z2;
        setFocusableInTouchMode(true);
        requestFocus();
        this.mSwipeDismissHandler = new SwipeDismissHandler(((FrameLayout) this).mContext, this.mScreenshotStatic, new SwipeDismissHandler.SwipeDismissCallbacks() { // from class: com.android.systemui.screenshot.ScreenshotView.3
            @Override // com.android.systemui.screenshot.SwipeDismissHandler.SwipeDismissCallbacks
            public final void onDismiss() {
                ScreenshotView screenshotView = ScreenshotView.this;
                screenshotView.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_SWIPE_DISMISSED, 0, screenshotView.mPackageName);
                ScreenshotController.AnonymousClass3 r4 = (ScreenshotController.AnonymousClass3) ScreenshotView.this.mCallbacks;
                Objects.requireNonNull(r4);
                ScreenshotController.this.finishDismiss();
            }

            @Override // com.android.systemui.screenshot.SwipeDismissHandler.SwipeDismissCallbacks
            public final void onInteraction() {
                ScreenshotController.AnonymousClass3 r0 = (ScreenshotController.AnonymousClass3) ScreenshotView.this.mCallbacks;
                Objects.requireNonNull(r0);
                ScreenshotController.this.mScreenshotHandler.resetTimeout();
            }
        });
    }

    @Override // android.view.ViewGroup
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!getSwipeRegion().contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY())) {
            return false;
        }
        if (motionEvent.getActionMasked() == 0) {
            this.mSwipeDismissHandler.onTouch(this, motionEvent);
        }
        return this.mSwipeDetector.onTouchEvent(motionEvent);
    }

    public ScreenshotView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        new AccelerateInterpolator();
        this.mPackageName = "";
        this.mSmartChips = new ArrayList<>();
        Resources resources = ((FrameLayout) this).mContext.getResources();
        this.mResources = resources;
        this.mFixedSize = resources.getDimensionPixelSize(2131166750);
        this.mFastOutSlowIn = AnimationUtils.loadInterpolator(((FrameLayout) this).mContext, 17563661);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.mDisplayMetrics = displayMetrics;
        ((FrameLayout) this).mContext.getDisplay().getRealMetrics(displayMetrics);
        this.mAccessibilityManager = AccessibilityManager.getInstance(((FrameLayout) this).mContext);
        GestureDetector gestureDetector = new GestureDetector(((FrameLayout) this).mContext, new GestureDetector.SimpleOnGestureListener() { // from class: com.android.systemui.screenshot.ScreenshotView.1
            public final Rect mActionsRect = new Rect();

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public final boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                ScreenshotView.this.mActionsContainer.getBoundsOnScreen(this.mActionsRect);
                if (!this.mActionsRect.contains((int) motionEvent2.getRawX(), (int) motionEvent2.getRawY()) || !ScreenshotView.this.mActionsContainer.canScrollHorizontally((int) f)) {
                    return true;
                }
                return false;
            }
        });
        this.mSwipeDetector = gestureDetector;
        gestureDetector.setIsLongpressEnabled(false);
        addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.android.systemui.screenshot.ScreenshotView.2
            @Override // android.view.View.OnAttachStateChangeListener
            public final void onViewAttachedToWindow(View view) {
                final ScreenshotView screenshotView = ScreenshotView.this;
                int i3 = ScreenshotView.$r8$clinit;
                Objects.requireNonNull(screenshotView);
                screenshotView.stopInputListening();
                Logger logger = new Logger("Screenshot", 0);
                screenshotView.mInputMonitor = logger;
                screenshotView.mInputEventReceiver = logger.getInputReceiver(Looper.getMainLooper(), Choreographer.getInstance(), new InputChannelCompat$InputEventListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda15
                    @Override // com.android.systemui.shared.system.InputChannelCompat$InputEventListener
                    public final void onInputEvent(InputEvent inputEvent) {
                        ScreenshotView screenshotView2 = ScreenshotView.this;
                        int i4 = ScreenshotView.$r8$clinit;
                        Objects.requireNonNull(screenshotView2);
                        if (inputEvent instanceof MotionEvent) {
                            MotionEvent motionEvent = (MotionEvent) inputEvent;
                            if (motionEvent.getActionMasked() == 0 && !screenshotView2.getTouchRegion(false).contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY())) {
                                ScreenshotController.AnonymousClass3 r3 = (ScreenshotController.AnonymousClass3) screenshotView2.mCallbacks;
                                Objects.requireNonNull(r3);
                                ScreenshotController.this.setWindowFocusable(false);
                            }
                        }
                    }
                });
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public final void onViewDetachedFromWindow(View view) {
                ScreenshotView.this.stopInputListening();
            }
        });
    }
}
