package com.android.wm.shell.pip.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.RemoteAction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import android.util.Pair;
import android.util.Property;
import android.view.IWindow;
import android.view.IWindowSession;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManagerGlobal;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.android.keyguard.KeyguardStatusView$$ExternalSyntheticLambda0;
import com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda4;
import com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda0;
import com.android.wm.shell.animation.Interpolators;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SystemWindows;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda3;
import com.android.wm.shell.pip.PipUiEventLogger;
import com.android.wm.shell.pip.PipUtils;
import com.android.wm.shell.pip.phone.PhonePipMenuController;
import com.android.wm.shell.splitscreen.SplitScreenController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class PipMenuView extends FrameLayout {
    public static final /* synthetic */ int $r8$clinit = 0;
    public AccessibilityManager mAccessibilityManager;
    public Drawable mBackgroundDrawable;
    public final PhonePipMenuController mController;
    public boolean mDidLastShowMenuResize;
    public View mDismissButton;
    public int mDismissFadeOutDurationMs;
    public View mEnterSplitButton;
    public boolean mFocusedTaskAllowSplitScreen;
    public ShellExecutor mMainExecutor;
    public Handler mMainHandler;
    public View mMenuContainer;
    public AnimatorSet mMenuContainerAnimator;
    public int mMenuState;
    public PipMenuIconsAlgorithm mPipMenuIconsAlgorithm;
    public final PipUiEventLogger mPipUiEventLogger;
    public View mSettingsButton;
    public final Optional<SplitScreenController> mSplitScreenControllerOptional;
    public View mViewRoot;
    public boolean mAllowMenuTimeout = true;
    public boolean mAllowTouches = true;
    public final ArrayList mActions = new ArrayList();
    public AnonymousClass1 mMenuBgUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.wm.shell.pip.phone.PipMenuView.1
        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
            PipMenuView.this.mBackgroundDrawable.setAlpha((int) (((Float) valueAnimator.getAnimatedValue()).floatValue() * 0.3f * 255.0f));
        }
    };
    public final KeyguardStatusView$$ExternalSyntheticLambda0 mHideMenuRunnable = new KeyguardStatusView$$ExternalSyntheticLambda0(this, 9);
    public View mTopEndContainer = findViewById(2131429070);
    public LinearLayout mActionsGroup = (LinearLayout) findViewById(2131427458);
    public int mBetweenActionPaddingLand = getResources().getDimensionPixelSize(2131166778);

    /* JADX WARN: Type inference failed for: r0v3, types: [com.android.wm.shell.pip.phone.PipMenuView$1] */
    public PipMenuView(Context context, PhonePipMenuController phonePipMenuController, ShellExecutor shellExecutor, Handler handler, Optional<SplitScreenController> optional, PipUiEventLogger pipUiEventLogger) {
        super(context, null, 0);
        ((FrameLayout) this).mContext = context;
        this.mController = phonePipMenuController;
        this.mMainExecutor = shellExecutor;
        this.mMainHandler = handler;
        this.mSplitScreenControllerOptional = optional;
        this.mPipUiEventLogger = pipUiEventLogger;
        this.mAccessibilityManager = (AccessibilityManager) context.getSystemService(AccessibilityManager.class);
        View.inflate(context, 2131624379, this);
        Drawable drawable = ((FrameLayout) this).mContext.getDrawable(2131232567);
        this.mBackgroundDrawable = drawable;
        drawable.setAlpha(0);
        View findViewById = findViewById(2131427539);
        this.mViewRoot = findViewById;
        findViewById.setBackground(this.mBackgroundDrawable);
        View findViewById2 = findViewById(2131428365);
        this.mMenuContainer = findViewById2;
        findViewById2.setAlpha(0.0f);
        View findViewById3 = findViewById(2131428837);
        this.mSettingsButton = findViewById3;
        findViewById3.setAlpha(0.0f);
        this.mSettingsButton.setOnClickListener(new PipMenuView$$ExternalSyntheticLambda2(this, 0));
        View findViewById4 = findViewById(2131427850);
        this.mDismissButton = findViewById4;
        findViewById4.setAlpha(0.0f);
        this.mDismissButton.setOnClickListener(new PipMenuView$$ExternalSyntheticLambda3(this, 0));
        findViewById(2131427944).setOnClickListener(new PipMenuView$$ExternalSyntheticLambda4(this, 0));
        View findViewById5 = findViewById(2131427924);
        this.mEnterSplitButton = findViewById5;
        findViewById5.setAlpha(0.0f);
        this.mEnterSplitButton.setOnClickListener(new PipMenuView$$ExternalSyntheticLambda5(this, 0));
        findViewById(2131428694).setAlpha(0.0f);
        PipMenuIconsAlgorithm pipMenuIconsAlgorithm = new PipMenuIconsAlgorithm();
        this.mPipMenuIconsAlgorithm = pipMenuIconsAlgorithm;
        ViewGroup viewGroup = (ViewGroup) this.mViewRoot;
        ViewGroup viewGroup2 = (ViewGroup) this.mTopEndContainer;
        View findViewById6 = findViewById(2131428694);
        View view = this.mEnterSplitButton;
        View view2 = this.mSettingsButton;
        View view3 = this.mDismissButton;
        pipMenuIconsAlgorithm.mDragHandle = findViewById6;
        pipMenuIconsAlgorithm.mEnterSplitButton = view;
        pipMenuIconsAlgorithm.mSettingsButton = view2;
        pipMenuIconsAlgorithm.mDismissButton = view3;
        this.mDismissFadeOutDurationMs = context.getResources().getInteger(2131492901);
        setAccessibilityDelegate(new View.AccessibilityDelegate() { // from class: com.android.wm.shell.pip.phone.PipMenuView.2
            @Override // android.view.View.AccessibilityDelegate
            public final boolean performAccessibilityAction(View view4, int i, Bundle bundle) {
                if (i == 16) {
                    PipMenuView pipMenuView = PipMenuView.this;
                    if (pipMenuView.mMenuState != 1) {
                        PhonePipMenuController phonePipMenuController2 = pipMenuView.mController;
                        Objects.requireNonNull(phonePipMenuController2);
                        phonePipMenuController2.mListeners.forEach(QSTileHost$$ExternalSyntheticLambda4.INSTANCE$3);
                    }
                }
                return super.performAccessibilityAction(view4, i, bundle);
            }

            @Override // android.view.View.AccessibilityDelegate
            public final void onInitializeAccessibilityNodeInfo(View view4, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view4, accessibilityNodeInfo);
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, PipMenuView.this.getResources().getString(2131952978)));
            }
        });
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public final boolean shouldDelayChildPressedState() {
        return true;
    }

    @Override // android.view.View
    public final boolean dispatchGenericMotionEvent(MotionEvent motionEvent) {
        if (this.mAllowMenuTimeout) {
            repostDelayedHide(2000);
        }
        return super.dispatchGenericMotionEvent(motionEvent);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (!this.mAllowTouches) {
            return false;
        }
        if (this.mAllowMenuTimeout) {
            repostDelayedHide(2000);
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public final void hideMenu(final Runnable runnable, final boolean z, boolean z2, int i) {
        long j;
        if (this.mMenuState != 0) {
            this.mMainExecutor.removeCallbacks(this.mHideMenuRunnable);
            if (z) {
                notifyMenuStateChangeStart(0, z2, null);
            }
            this.mMenuContainerAnimator = new AnimatorSet();
            View view = this.mMenuContainer;
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, View.ALPHA, view.getAlpha(), 0.0f);
            ofFloat.addUpdateListener(this.mMenuBgUpdateListener);
            View view2 = this.mSettingsButton;
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view2, View.ALPHA, view2.getAlpha(), 0.0f);
            View view3 = this.mDismissButton;
            ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(view3, View.ALPHA, view3.getAlpha(), 0.0f);
            View view4 = this.mEnterSplitButton;
            this.mMenuContainerAnimator.playTogether(ofFloat, ofFloat2, ofFloat3, ObjectAnimator.ofFloat(view4, View.ALPHA, view4.getAlpha(), 0.0f));
            this.mMenuContainerAnimator.setInterpolator(Interpolators.ALPHA_OUT);
            AnimatorSet animatorSet = this.mMenuContainerAnimator;
            if (i == 0) {
                j = 0;
            } else if (i == 1) {
                j = 125;
            } else if (i == 2) {
                j = this.mDismissFadeOutDurationMs;
            } else {
                throw new IllegalStateException(VendorAtomValue$$ExternalSyntheticOutline0.m("Invalid animation type ", i));
            }
            animatorSet.setDuration(j);
            this.mMenuContainerAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.wm.shell.pip.phone.PipMenuView.4
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    PipMenuView.this.setVisibility(8);
                    if (z) {
                        PipMenuView.m156$$Nest$mnotifyMenuStateChangeFinish(PipMenuView.this, 0);
                    }
                    Runnable runnable2 = runnable;
                    if (runnable2 != null) {
                        runnable2.run();
                    }
                }
            });
            this.mMenuContainerAnimator.start();
        }
    }

    public final void notifyMenuStateChangeStart(final int i, final boolean z, final PipTaskOrganizer$$ExternalSyntheticLambda3 pipTaskOrganizer$$ExternalSyntheticLambda3) {
        PhonePipMenuController phonePipMenuController = this.mController;
        Objects.requireNonNull(phonePipMenuController);
        if (i != phonePipMenuController.mMenuState) {
            phonePipMenuController.mListeners.forEach(new Consumer() { // from class: com.android.wm.shell.pip.phone.PhonePipMenuController$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((PhonePipMenuController.Listener) obj).onPipMenuStateChangeStart(i, z, pipTaskOrganizer$$ExternalSyntheticLambda3);
                }
            });
            boolean z2 = true;
            if (i == 1) {
                PipMediaController pipMediaController = phonePipMenuController.mMediaController;
                PhonePipMenuController.AnonymousClass1 r0 = phonePipMenuController.mMediaActionListener;
                Objects.requireNonNull(pipMediaController);
                if (!pipMediaController.mActionListeners.contains(r0)) {
                    pipMediaController.mActionListeners.add(r0);
                    r0.onMediaActionsChanged(pipMediaController.getMediaActions());
                }
            } else {
                PipMediaController pipMediaController2 = phonePipMenuController.mMediaController;
                PhonePipMenuController.AnonymousClass1 r02 = phonePipMenuController.mMediaActionListener;
                Objects.requireNonNull(pipMediaController2);
                r02.onMediaActionsChanged(Collections.emptyList());
                pipMediaController2.mActionListeners.remove(r02);
            }
            try {
                IWindowSession windowSession = WindowManagerGlobal.getWindowSession();
                IBinder focusGrantToken = phonePipMenuController.mSystemWindows.getFocusGrantToken(phonePipMenuController.mPipMenuView);
                if (i == 0) {
                    z2 = false;
                }
                windowSession.grantEmbeddedWindowFocus((IWindow) null, focusGrantToken, z2);
            } catch (RemoteException e) {
                Log.e("PhonePipMenuController", "Unable to update focus as menu appears/disappears", e);
            }
        }
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public final boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i != 111) {
            return super.onKeyUp(i, keyEvent);
        }
        hideMenu(null, true, this.mDidLastShowMenuResize, 1);
        return true;
    }

    public final void repostDelayedHide(int i) {
        int recommendedTimeoutMillis = this.mAccessibilityManager.getRecommendedTimeoutMillis(i, 5);
        this.mMainExecutor.removeCallbacks(this.mHideMenuRunnable);
        this.mMainExecutor.executeDelayed(this.mHideMenuRunnable, recommendedTimeoutMillis);
    }

    public final void showMenu(final int i, Rect rect, final boolean z, boolean z2, boolean z3) {
        boolean z4;
        this.mAllowMenuTimeout = z;
        this.mDidLastShowMenuResize = z2;
        boolean z5 = ((FrameLayout) this).mContext.getResources().getBoolean(2131034151);
        int i2 = this.mMenuState;
        if (i2 != i) {
            if (!z2 || !(i2 == 1 || i == 1)) {
                z4 = false;
            } else {
                z4 = true;
            }
            this.mAllowTouches = !z4;
            this.mMainExecutor.removeCallbacks(this.mHideMenuRunnable);
            AnimatorSet animatorSet = this.mMenuContainerAnimator;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            this.mMenuContainerAnimator = new AnimatorSet();
            View view = this.mMenuContainer;
            float f = 1.0f;
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, View.ALPHA, view.getAlpha(), 1.0f);
            ofFloat.addUpdateListener(this.mMenuBgUpdateListener);
            View view2 = this.mSettingsButton;
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view2, View.ALPHA, view2.getAlpha(), 1.0f);
            View view3 = this.mDismissButton;
            ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(view3, View.ALPHA, view3.getAlpha(), 1.0f);
            View view4 = this.mEnterSplitButton;
            Property property = View.ALPHA;
            float[] fArr = new float[2];
            fArr[0] = view4.getAlpha();
            if (!z5 || !this.mFocusedTaskAllowSplitScreen) {
                f = 0.0f;
            }
            fArr[1] = f;
            ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(view4, property, fArr);
            if (i == 1) {
                this.mMenuContainerAnimator.playTogether(ofFloat, ofFloat2, ofFloat3, ofFloat4);
            } else {
                this.mMenuContainerAnimator.playTogether(ofFloat4);
            }
            this.mMenuContainerAnimator.setInterpolator(Interpolators.ALPHA_IN);
            this.mMenuContainerAnimator.setDuration(125L);
            this.mMenuContainerAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.wm.shell.pip.phone.PipMenuView.3
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationCancel(Animator animator) {
                    PipMenuView.this.mAllowTouches = true;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    PipMenuView pipMenuView = PipMenuView.this;
                    pipMenuView.mAllowTouches = true;
                    PipMenuView.m156$$Nest$mnotifyMenuStateChangeFinish(pipMenuView, i);
                    if (z) {
                        PipMenuView.this.repostDelayedHide(3500);
                    }
                }
            });
            if (z3) {
                notifyMenuStateChangeStart(i, z2, new PipTaskOrganizer$$ExternalSyntheticLambda3(this, 5));
            } else {
                notifyMenuStateChangeStart(i, z2, null);
                setVisibility(0);
                this.mMenuContainerAnimator.start();
            }
            updateActionViews(i, rect);
        } else if (z) {
            repostDelayedHide(2000);
        }
    }

    public static void $r8$lambda$ZIdnozLD4vi0K38Zv_I0w2iOV1U(PipMenuView pipMenuView, View view) {
        Objects.requireNonNull(pipMenuView);
        if (view.getAlpha() != 0.0f) {
            Pair<ComponentName, Integer> topPipActivity = PipUtils.getTopPipActivity(((FrameLayout) pipMenuView).mContext);
            if (topPipActivity.first != null) {
                Intent intent = new Intent("android.settings.PICTURE_IN_PICTURE_SETTINGS", Uri.fromParts("package", ((ComponentName) topPipActivity.first).getPackageName(), null));
                intent.setFlags(268468224);
                ((FrameLayout) pipMenuView).mContext.startActivityAsUser(intent, UserHandle.of(((Integer) topPipActivity.second).intValue()));
                pipMenuView.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_SHOW_SETTINGS);
            }
        }
    }

    /* renamed from: -$$Nest$mnotifyMenuStateChangeFinish  reason: not valid java name */
    public static void m156$$Nest$mnotifyMenuStateChangeFinish(PipMenuView pipMenuView, int i) {
        Objects.requireNonNull(pipMenuView);
        pipMenuView.mMenuState = i;
        PhonePipMenuController phonePipMenuController = pipMenuView.mController;
        Objects.requireNonNull(phonePipMenuController);
        if (i != phonePipMenuController.mMenuState) {
            phonePipMenuController.mListeners.forEach(new ShellCommandHandlerImpl$$ExternalSyntheticLambda0(i, 1));
        }
        phonePipMenuController.mMenuState = i;
        if (i != 0) {
            SystemWindows systemWindows = phonePipMenuController.mSystemWindows;
            PipMenuView pipMenuView2 = phonePipMenuController.mPipMenuView;
            Objects.requireNonNull(systemWindows);
            SystemWindows.PerDisplay perDisplay = systemWindows.mPerDisplay.get(0);
            if (perDisplay != null) {
                perDisplay.setShellRootAccessibilityWindow(1, pipMenuView2);
                return;
            }
            return;
        }
        SystemWindows systemWindows2 = phonePipMenuController.mSystemWindows;
        Objects.requireNonNull(systemWindows2);
        SystemWindows.PerDisplay perDisplay2 = systemWindows2.mPerDisplay.get(0);
        if (perDisplay2 != null) {
            perDisplay2.setShellRootAccessibilityWindow(1, null);
        }
    }

    public final void updateActionViews(int i, Rect rect) {
        int i2;
        float f;
        int i3;
        int i4;
        ViewGroup viewGroup = (ViewGroup) findViewById(2131427945);
        ViewGroup viewGroup2 = (ViewGroup) findViewById(2131427456);
        viewGroup2.setOnTouchListener(PipMenuView$$ExternalSyntheticLambda6.INSTANCE);
        boolean z = true;
        if (i == 1) {
            i2 = 0;
        } else {
            i2 = 4;
        }
        viewGroup.setVisibility(i2);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) viewGroup.getLayoutParams();
        if (this.mActions.isEmpty() || i == 0) {
            viewGroup2.setVisibility(4);
            layoutParams.topMargin = 0;
            layoutParams.bottomMargin = 0;
        } else {
            viewGroup2.setVisibility(0);
            if (this.mActionsGroup != null) {
                LayoutInflater from = LayoutInflater.from(((FrameLayout) this).mContext);
                while (this.mActionsGroup.getChildCount() < this.mActions.size()) {
                    this.mActionsGroup.addView((PipMenuActionView) from.inflate(2131624380, (ViewGroup) this.mActionsGroup, false));
                }
                for (int i5 = 0; i5 < this.mActionsGroup.getChildCount(); i5++) {
                    View childAt = this.mActionsGroup.getChildAt(i5);
                    if (i5 < this.mActions.size()) {
                        i4 = 0;
                    } else {
                        i4 = 8;
                    }
                    childAt.setVisibility(i4);
                }
                if (rect.width() <= rect.height()) {
                    z = false;
                }
                for (int i6 = 0; i6 < this.mActions.size(); i6++) {
                    RemoteAction remoteAction = (RemoteAction) this.mActions.get(i6);
                    final PipMenuActionView pipMenuActionView = (PipMenuActionView) this.mActionsGroup.getChildAt(i6);
                    remoteAction.getIcon().loadDrawableAsync(((FrameLayout) this).mContext, new Icon.OnDrawableLoadedListener() { // from class: com.android.wm.shell.pip.phone.PipMenuView$$ExternalSyntheticLambda0
                        @Override // android.graphics.drawable.Icon.OnDrawableLoadedListener
                        public final void onDrawableLoaded(Drawable drawable) {
                            PipMenuActionView pipMenuActionView2 = PipMenuActionView.this;
                            if (drawable != null) {
                                drawable.setTint(-1);
                                Objects.requireNonNull(pipMenuActionView2);
                                pipMenuActionView2.mImageView.setImageDrawable(drawable);
                            }
                        }
                    }, this.mMainHandler);
                    pipMenuActionView.setContentDescription(remoteAction.getContentDescription());
                    if (remoteAction.isEnabled()) {
                        pipMenuActionView.setOnClickListener(new PipMenuView$$ExternalSyntheticLambda1(remoteAction, 0));
                    }
                    pipMenuActionView.setEnabled(remoteAction.isEnabled());
                    if (remoteAction.isEnabled()) {
                        f = 1.0f;
                    } else {
                        f = 0.54f;
                    }
                    pipMenuActionView.setAlpha(f);
                    LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) pipMenuActionView.getLayoutParams();
                    if (!z || i6 <= 0) {
                        i3 = 0;
                    } else {
                        i3 = this.mBetweenActionPaddingLand;
                    }
                    layoutParams2.leftMargin = i3;
                }
            }
            layoutParams.topMargin = getResources().getDimensionPixelSize(2131166776);
            layoutParams.bottomMargin = getResources().getDimensionPixelSize(2131166784);
        }
        viewGroup.requestLayout();
    }
}
