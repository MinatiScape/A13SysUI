package com.android.systemui.media;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.util.MathUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import com.android.keyguard.KeyguardViewController;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda20;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.Utils;
import java.util.Objects;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: MediaHierarchyManager.kt */
/* loaded from: classes.dex */
public final class MediaHierarchyManager {
    public boolean animationPending;
    public float animationStartAlpha;
    public float animationStartCrossFadeProgress;
    public ValueAnimator animator;
    public final KeyguardBypassController bypassController;
    public boolean collapsingShadeFromQS;
    public final Context context;
    public int distanceForFullShadeTransition;
    public boolean dozeAnimationRunning;
    public boolean dreamOverlayActive;
    public final DreamOverlayStateController dreamOverlayStateController;
    public float fullShadeTransitionProgress;
    public boolean fullyAwake;
    public boolean goingToSleep;
    public boolean inSplitShade;
    public boolean isCrossFadeAnimatorRunning;
    public final KeyguardStateController keyguardStateController;
    public final KeyguardViewController keyguardViewController;
    public final MediaCarouselController mediaCarouselController;
    public final NotificationLockscreenUserManager notifLockscreenUserManager;
    public boolean qsExpanded;
    public float qsExpansion;
    public ViewGroupOverlay rootOverlay;
    public View rootView;
    public final SysuiStatusBarStateController statusBarStateController;
    public int statusbarState;
    public Rect currentBounds = new Rect();
    public Rect animationStartBounds = new Rect();
    public int crossFadeAnimationStartLocation = -1;
    public int crossFadeAnimationEndLocation = -1;
    public Rect targetBounds = new Rect();
    public final MediaHost[] mediaHosts = new MediaHost[4];
    public int previousLocation = -1;
    public int desiredLocation = -1;
    public int currentAttachmentLocation = -1;
    public final MediaHierarchyManager$startAnimation$1 startAnimation = new Runnable() { // from class: com.android.systemui.media.MediaHierarchyManager$startAnimation$1
        @Override // java.lang.Runnable
        public final void run() {
            MediaHierarchyManager.this.animator.start();
        }
    };
    public float animationCrossFadeProgress = 1.0f;
    public float carouselAlpha = 1.0f;

    /* compiled from: MediaHierarchyManager.kt */
    /* renamed from: com.android.systemui.media.MediaHierarchyManager$5 */
    /* loaded from: classes.dex */
    public static final class AnonymousClass5 extends Lambda implements Function0<Unit> {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass5() {
            super(0);
            MediaHierarchyManager.this = r1;
        }

        @Override // kotlin.jvm.functions.Function0
        public final Unit invoke() {
            MediaCarouselController mediaCarouselController = MediaHierarchyManager.this.mediaCarouselController;
            Objects.requireNonNull(mediaCarouselController);
            MediaCarouselScrollHandler mediaCarouselScrollHandler = mediaCarouselController.mediaCarouselScrollHandler;
            boolean isVisibleToUser = MediaHierarchyManager.this.isVisibleToUser();
            Objects.requireNonNull(mediaCarouselScrollHandler);
            mediaCarouselScrollHandler.visibleToUser = isVisibleToUser;
            return Unit.INSTANCE;
        }
    }

    public final Pair<Long, Long> getAnimationParams(int i, int i2) {
        long j;
        long j2 = 0;
        if (i == 2 && i2 == 1) {
            if (this.statusbarState == 0 && this.keyguardStateController.isKeyguardFadingAway()) {
                j2 = this.keyguardStateController.getKeyguardFadingAwayDelay();
            }
            j = 224;
        } else if (i == 1 && i2 == 2) {
            j = 464;
        } else {
            j = 200;
        }
        return new Pair<>(Long.valueOf(j), Long.valueOf(j2));
    }

    public static Rect interpolateBounds(Rect rect, Rect rect2, float f, Rect rect3) {
        int lerp = (int) MathUtils.lerp(rect.left, rect2.left, f);
        int lerp2 = (int) MathUtils.lerp(rect.top, rect2.top, f);
        int lerp3 = (int) MathUtils.lerp(rect.right, rect2.right, f);
        int lerp4 = (int) MathUtils.lerp(rect.bottom, rect2.bottom, f);
        if (rect3 == null) {
            rect3 = new Rect();
        }
        rect3.set(lerp, lerp2, lerp3, lerp4);
        return rect3;
    }

    public static /* synthetic */ void updateDesiredLocation$default(MediaHierarchyManager mediaHierarchyManager, boolean z, int i) {
        if ((i & 1) != 0) {
            z = false;
        }
        mediaHierarchyManager.updateDesiredLocation(z, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:106:0x015d, code lost:
        if (r13 != false) goto L_0x015f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0077, code lost:
        if (r13 == false) goto L_0x00fb;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x00ef, code lost:
        if (r11 != r13) goto L_0x00f1;
     */
    /* JADX WARN: Removed duplicated region for block: B:102:0x0152  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x0171  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x017d  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x017f  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0184  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x01ee  */
    /* JADX WARN: Removed duplicated region for block: B:138:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x00ff  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0111  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x011c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void applyState(android.graphics.Rect r11, float r12, boolean r13) {
        /*
            Method dump skipped, instructions count: 512
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaHierarchyManager.applyState(android.graphics.Rect, float, boolean):void");
    }

    public final void applyTargetStateIfNotAnimating() {
        if (!this.animator.isRunning()) {
            applyState(this.targetBounds, this.carouselAlpha, false);
        }
    }

    public final void cancelAnimationAndApplyDesiredState() {
        this.animator.cancel();
        MediaHost host = getHost(this.desiredLocation);
        if (host != null) {
            applyState(host.getCurrentBounds(), 1.0f, true);
        }
    }

    public final MediaHost getHost(int i) {
        if (i < 0) {
            return null;
        }
        return this.mediaHosts[i];
    }

    public final ViewGroup getMediaFrame() {
        MediaCarouselController mediaCarouselController = this.mediaCarouselController;
        Objects.requireNonNull(mediaCarouselController);
        return mediaCarouselController.mediaFrame;
    }

    public final float getQSTransformationProgress() {
        boolean z;
        boolean z2;
        MediaHost host = getHost(this.desiredLocation);
        MediaHost host2 = getHost(this.previousLocation);
        MediaHost mediaHost = this.mediaHosts[1];
        boolean z3 = false;
        if (mediaHost != null && mediaHost.getVisible()) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            return -1.0f;
        }
        if (host != null && host.location == 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (!z2 || this.inSplitShade) {
            return -1.0f;
        }
        if (host2 != null && host2.location == 1) {
            z3 = true;
        }
        if (!z3) {
            return -1.0f;
        }
        if (host2.getVisible() || this.statusbarState != 1) {
            return this.qsExpansion;
        }
        return -1.0f;
    }

    public final boolean isHomeScreenShadeVisibleToUser() {
        if (this.statusBarStateController.isDozing() || this.statusBarStateController.getState() != 0 || !this.statusBarStateController.isExpanded()) {
            return false;
        }
        return true;
    }

    public final boolean isLockScreenShadeVisibleToUser() {
        if (!this.statusBarStateController.isDozing() && !this.keyguardViewController.isBouncerShowing()) {
            if (this.statusBarStateController.getState() == 2) {
                return true;
            }
            if (this.statusBarStateController.getState() == 1 && this.qsExpanded) {
                return true;
            }
        }
        return false;
    }

    public final boolean isLockScreenVisibleToUser() {
        if (this.statusBarStateController.isDozing() || this.keyguardViewController.isBouncerShowing() || this.statusBarStateController.getState() != 1 || !this.notifLockscreenUserManager.shouldShowLockscreenNotifications() || !this.statusBarStateController.isExpanded() || this.qsExpanded) {
            return false;
        }
        return true;
    }

    public final boolean isTransitioningToFullShade() {
        boolean z;
        if (this.fullShadeTransitionProgress == 0.0f) {
            z = true;
        } else {
            z = false;
        }
        if (z || this.bypassController.getBypassEnabled() || this.statusbarState != 1) {
            return false;
        }
        return true;
    }

    public final void setQsExpanded(boolean z) {
        if (this.qsExpanded != z) {
            this.qsExpanded = z;
            MediaCarouselController mediaCarouselController = this.mediaCarouselController;
            Objects.requireNonNull(mediaCarouselController);
            MediaCarouselScrollHandler mediaCarouselScrollHandler = mediaCarouselController.mediaCarouselScrollHandler;
            Objects.requireNonNull(mediaCarouselScrollHandler);
            mediaCarouselScrollHandler.qsExpanded = z;
        }
        if (z && (isLockScreenShadeVisibleToUser() || isHomeScreenShadeVisibleToUser())) {
            this.mediaCarouselController.logSmartspaceImpression(z);
        }
        MediaCarouselController mediaCarouselController2 = this.mediaCarouselController;
        Objects.requireNonNull(mediaCarouselController2);
        MediaCarouselScrollHandler mediaCarouselScrollHandler2 = mediaCarouselController2.mediaCarouselScrollHandler;
        boolean isVisibleToUser = isVisibleToUser();
        Objects.requireNonNull(mediaCarouselScrollHandler2);
        mediaCarouselScrollHandler2.visibleToUser = isVisibleToUser;
    }

    /* JADX WARN: Code restructure failed: missing block: B:130:0x0145, code lost:
        if (r13.animationPending == false) goto L_0x0148;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0070, code lost:
        if (r6 != false) goto L_0x0079;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0094, code lost:
        if (r13.statusBarStateController.isDozing() == false) goto L_0x00a1;
     */
    /* JADX WARN: Removed duplicated region for block: B:134:0x014d  */
    /* JADX WARN: Removed duplicated region for block: B:139:0x0177  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x0179  */
    /* JADX WARN: Removed duplicated region for block: B:142:0x017c  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x018e A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x00b4 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x00b8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x00bf  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x00dd  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x00df  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x00e4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateDesiredLocation(boolean r14, boolean r15) {
        /*
            Method dump skipped, instructions count: 600
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaHierarchyManager.updateDesiredLocation(boolean, boolean):void");
    }

    /* JADX WARN: Type inference failed for: r3v4, types: [com.android.systemui.media.MediaHierarchyManager$startAnimation$1] */
    public MediaHierarchyManager(Context context, SysuiStatusBarStateController sysuiStatusBarStateController, KeyguardStateController keyguardStateController, KeyguardBypassController keyguardBypassController, MediaCarouselController mediaCarouselController, NotificationLockscreenUserManager notificationLockscreenUserManager, ConfigurationController configurationController, WakefulnessLifecycle wakefulnessLifecycle, KeyguardViewController keyguardViewController, DreamOverlayStateController dreamOverlayStateController) {
        this.context = context;
        this.statusBarStateController = sysuiStatusBarStateController;
        this.keyguardStateController = keyguardStateController;
        this.bypassController = keyguardBypassController;
        this.mediaCarouselController = mediaCarouselController;
        this.notifLockscreenUserManager = notificationLockscreenUserManager;
        this.keyguardViewController = keyguardViewController;
        this.dreamOverlayStateController = dreamOverlayStateController;
        this.statusbarState = sysuiStatusBarStateController.getState();
        final ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.media.MediaHierarchyManager$animator$1$1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                float f;
                float f2;
                MediaHierarchyManager.this.updateTargetState();
                float animatedFraction = ofFloat.getAnimatedFraction();
                MediaHierarchyManager mediaHierarchyManager = MediaHierarchyManager.this;
                if (mediaHierarchyManager.isCrossFadeAnimatorRunning) {
                    mediaHierarchyManager.animationCrossFadeProgress = MathUtils.lerp(mediaHierarchyManager.animationStartCrossFadeProgress, 1.0f, ofFloat.getAnimatedFraction());
                    float f3 = MediaHierarchyManager.this.animationCrossFadeProgress;
                    int i = (f3 > 0.5f ? 1 : (f3 == 0.5f ? 0 : -1));
                    if (i < 0) {
                        f2 = 0.0f;
                    } else {
                        f2 = 1.0f;
                    }
                    if (i <= 0) {
                        f = 1.0f - (f3 / 0.5f);
                    } else {
                        f = (f3 - 0.5f) / 0.5f;
                    }
                    animatedFraction = f2;
                } else {
                    f = MathUtils.lerp(mediaHierarchyManager.animationStartAlpha, 1.0f, ofFloat.getAnimatedFraction());
                }
                MediaHierarchyManager mediaHierarchyManager2 = MediaHierarchyManager.this;
                MediaHierarchyManager.interpolateBounds(mediaHierarchyManager2.animationStartBounds, mediaHierarchyManager2.targetBounds, animatedFraction, mediaHierarchyManager2.currentBounds);
                MediaHierarchyManager mediaHierarchyManager3 = MediaHierarchyManager.this;
                mediaHierarchyManager3.applyState(mediaHierarchyManager3.currentBounds, f, false);
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.media.MediaHierarchyManager$animator$1$2
            public boolean cancelled;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationCancel(Animator animator) {
                this.cancelled = true;
                MediaHierarchyManager mediaHierarchyManager = MediaHierarchyManager.this;
                mediaHierarchyManager.animationPending = false;
                View view = mediaHierarchyManager.rootView;
                if (view != null) {
                    view.removeCallbacks(mediaHierarchyManager.startAnimation);
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationStart(Animator animator) {
                this.cancelled = false;
                MediaHierarchyManager.this.animationPending = false;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                MediaHierarchyManager mediaHierarchyManager = MediaHierarchyManager.this;
                mediaHierarchyManager.isCrossFadeAnimatorRunning = false;
                if (!this.cancelled) {
                    mediaHierarchyManager.applyTargetStateIfNotAnimating();
                }
            }
        });
        this.animator = ofFloat;
        this.distanceForFullShadeTransition = context.getResources().getDimensionPixelSize(2131166123);
        this.inSplitShade = Utils.shouldUseSplitNotificationShade(context.getResources());
        configurationController.addCallback(new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.media.MediaHierarchyManager.1
            {
                MediaHierarchyManager.this = this;
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public final void onConfigChanged(Configuration configuration) {
                MediaHierarchyManager mediaHierarchyManager = MediaHierarchyManager.this;
                Objects.requireNonNull(mediaHierarchyManager);
                mediaHierarchyManager.distanceForFullShadeTransition = mediaHierarchyManager.context.getResources().getDimensionPixelSize(2131166123);
                mediaHierarchyManager.inSplitShade = Utils.shouldUseSplitNotificationShade(mediaHierarchyManager.context.getResources());
                MediaHierarchyManager.this.updateDesiredLocation(true, true);
            }
        });
        sysuiStatusBarStateController.addCallback(new StatusBarStateController.StateListener() { // from class: com.android.systemui.media.MediaHierarchyManager.2
            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public final void onDozingChanged(boolean z) {
                if (!z) {
                    MediaHierarchyManager mediaHierarchyManager = MediaHierarchyManager.this;
                    Objects.requireNonNull(mediaHierarchyManager);
                    if (mediaHierarchyManager.dozeAnimationRunning) {
                        mediaHierarchyManager.dozeAnimationRunning = false;
                        MediaHierarchyManager.updateDesiredLocation$default(mediaHierarchyManager, false, 3);
                    }
                    if (MediaHierarchyManager.this.isLockScreenVisibleToUser()) {
                        MediaHierarchyManager mediaHierarchyManager2 = MediaHierarchyManager.this;
                        mediaHierarchyManager2.mediaCarouselController.logSmartspaceImpression(mediaHierarchyManager2.qsExpanded);
                    }
                } else {
                    MediaHierarchyManager.updateDesiredLocation$default(MediaHierarchyManager.this, false, 3);
                    MediaHierarchyManager.this.setQsExpanded(false);
                    MediaHierarchyManager mediaHierarchyManager3 = MediaHierarchyManager.this;
                    Objects.requireNonNull(mediaHierarchyManager3);
                    Objects.requireNonNull(mediaHierarchyManager3.mediaCarouselController);
                    Objects.requireNonNull(MediaPlayerData.INSTANCE);
                    for (MediaControlPanel mediaControlPanel : MediaPlayerData.players()) {
                        mediaControlPanel.closeGuts(true);
                    }
                }
                MediaCarouselController mediaCarouselController2 = MediaHierarchyManager.this.mediaCarouselController;
                Objects.requireNonNull(mediaCarouselController2);
                MediaCarouselScrollHandler mediaCarouselScrollHandler = mediaCarouselController2.mediaCarouselScrollHandler;
                boolean isVisibleToUser = MediaHierarchyManager.this.isVisibleToUser();
                Objects.requireNonNull(mediaCarouselScrollHandler);
                mediaCarouselScrollHandler.visibleToUser = isVisibleToUser;
            }

            {
                MediaHierarchyManager.this = this;
            }

            /* JADX WARN: Code restructure failed: missing block: B:11:0x0017, code lost:
                if (r3 == false) goto L_0x001b;
             */
            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public final void onDozeAmountChanged(float r3, float r4) {
                /*
                    r2 = this;
                    com.android.systemui.media.MediaHierarchyManager r2 = com.android.systemui.media.MediaHierarchyManager.this
                    r4 = 0
                    int r4 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1))
                    r0 = 0
                    r1 = 1
                    if (r4 != 0) goto L_0x000b
                    r4 = r1
                    goto L_0x000c
                L_0x000b:
                    r4 = r0
                L_0x000c:
                    if (r4 != 0) goto L_0x001a
                    r4 = 1065353216(0x3f800000, float:1.0)
                    int r3 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1))
                    if (r3 != 0) goto L_0x0016
                    r3 = r1
                    goto L_0x0017
                L_0x0016:
                    r3 = r0
                L_0x0017:
                    if (r3 != 0) goto L_0x001a
                    goto L_0x001b
                L_0x001a:
                    r1 = r0
                L_0x001b:
                    java.util.Objects.requireNonNull(r2)
                    boolean r3 = r2.dozeAnimationRunning
                    if (r3 == r1) goto L_0x002a
                    r2.dozeAnimationRunning = r1
                    if (r1 != 0) goto L_0x002a
                    r3 = 3
                    com.android.systemui.media.MediaHierarchyManager.updateDesiredLocation$default(r2, r0, r3)
                L_0x002a:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaHierarchyManager.AnonymousClass2.onDozeAmountChanged(float, float):void");
            }

            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public final void onExpandedChanged(boolean z) {
                if (MediaHierarchyManager.this.isHomeScreenShadeVisibleToUser()) {
                    MediaHierarchyManager mediaHierarchyManager = MediaHierarchyManager.this;
                    MediaCarouselController mediaCarouselController2 = mediaHierarchyManager.mediaCarouselController;
                    Objects.requireNonNull(mediaHierarchyManager);
                    mediaCarouselController2.logSmartspaceImpression(mediaHierarchyManager.qsExpanded);
                }
                MediaCarouselController mediaCarouselController3 = MediaHierarchyManager.this.mediaCarouselController;
                Objects.requireNonNull(mediaCarouselController3);
                MediaCarouselScrollHandler mediaCarouselScrollHandler = mediaCarouselController3.mediaCarouselScrollHandler;
                boolean isVisibleToUser = MediaHierarchyManager.this.isVisibleToUser();
                Objects.requireNonNull(mediaCarouselScrollHandler);
                mediaCarouselScrollHandler.visibleToUser = isVisibleToUser;
            }

            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public final void onStateChanged(int i) {
                MediaHierarchyManager.this.updateTargetState();
                if (i == 2 && MediaHierarchyManager.this.isLockScreenShadeVisibleToUser()) {
                    MediaHierarchyManager mediaHierarchyManager = MediaHierarchyManager.this;
                    MediaCarouselController mediaCarouselController2 = mediaHierarchyManager.mediaCarouselController;
                    Objects.requireNonNull(mediaHierarchyManager);
                    mediaCarouselController2.logSmartspaceImpression(mediaHierarchyManager.qsExpanded);
                }
                MediaCarouselController mediaCarouselController3 = MediaHierarchyManager.this.mediaCarouselController;
                Objects.requireNonNull(mediaCarouselController3);
                MediaCarouselScrollHandler mediaCarouselScrollHandler = mediaCarouselController3.mediaCarouselScrollHandler;
                boolean isVisibleToUser = MediaHierarchyManager.this.isVisibleToUser();
                Objects.requireNonNull(mediaCarouselScrollHandler);
                mediaCarouselScrollHandler.visibleToUser = isVisibleToUser;
            }

            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public final void onStatePreChange(int i, int i2) {
                MediaHierarchyManager mediaHierarchyManager = MediaHierarchyManager.this;
                mediaHierarchyManager.statusbarState = i2;
                MediaHierarchyManager.updateDesiredLocation$default(mediaHierarchyManager, false, 3);
            }
        });
        DreamOverlayStateController.Callback callback = new DreamOverlayStateController.Callback() { // from class: com.android.systemui.media.MediaHierarchyManager.3
            {
                MediaHierarchyManager.this = this;
            }

            @Override // com.android.systemui.dreams.DreamOverlayStateController.Callback
            public final void onStateChanged() {
                boolean z;
                DreamOverlayStateController dreamOverlayStateController2 = MediaHierarchyManager.this.dreamOverlayStateController;
                Objects.requireNonNull(dreamOverlayStateController2);
                if ((dreamOverlayStateController2.mState & 1) != 0) {
                    z = true;
                } else {
                    z = false;
                }
                MediaHierarchyManager mediaHierarchyManager = MediaHierarchyManager.this;
                Objects.requireNonNull(mediaHierarchyManager);
                if (mediaHierarchyManager.dreamOverlayActive != z) {
                    mediaHierarchyManager.dreamOverlayActive = z;
                    MediaHierarchyManager.updateDesiredLocation$default(mediaHierarchyManager, true, 2);
                }
            }
        };
        Objects.requireNonNull(dreamOverlayStateController);
        dreamOverlayStateController.mExecutor.execute(new StatusBar$$ExternalSyntheticLambda20(dreamOverlayStateController, callback, 2));
        wakefulnessLifecycle.mObservers.add(new WakefulnessLifecycle.Observer() { // from class: com.android.systemui.media.MediaHierarchyManager.4
            {
                MediaHierarchyManager.this = this;
            }

            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public final void onFinishedGoingToSleep() {
                MediaHierarchyManager.access$setGoingToSleep(MediaHierarchyManager.this, false);
            }

            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public final void onFinishedWakingUp() {
                MediaHierarchyManager.access$setGoingToSleep(MediaHierarchyManager.this, false);
                MediaHierarchyManager mediaHierarchyManager = MediaHierarchyManager.this;
                Objects.requireNonNull(mediaHierarchyManager);
                if (!mediaHierarchyManager.fullyAwake) {
                    mediaHierarchyManager.fullyAwake = true;
                    MediaHierarchyManager.updateDesiredLocation$default(mediaHierarchyManager, true, 2);
                }
            }

            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public final void onStartedGoingToSleep() {
                MediaHierarchyManager.access$setGoingToSleep(MediaHierarchyManager.this, true);
                MediaHierarchyManager mediaHierarchyManager = MediaHierarchyManager.this;
                Objects.requireNonNull(mediaHierarchyManager);
                if (mediaHierarchyManager.fullyAwake) {
                    mediaHierarchyManager.fullyAwake = false;
                }
            }

            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public final void onStartedWakingUp() {
                MediaHierarchyManager.access$setGoingToSleep(MediaHierarchyManager.this, false);
            }
        });
        AnonymousClass5 r1 = new AnonymousClass5();
        Objects.requireNonNull(mediaCarouselController);
        mediaCarouselController.updateUserVisibility = r1;
    }

    public static final void access$setGoingToSleep(MediaHierarchyManager mediaHierarchyManager, boolean z) {
        Objects.requireNonNull(mediaHierarchyManager);
        if (mediaHierarchyManager.goingToSleep != z) {
            mediaHierarchyManager.goingToSleep = z;
            if (!z) {
                updateDesiredLocation$default(mediaHierarchyManager, false, 3);
            }
        }
    }

    public final int calculateTransformationType() {
        if (isTransitioningToFullShade()) {
            return 1;
        }
        int i = this.previousLocation;
        if ((i == 2 && this.desiredLocation == 0) || (i == 0 && this.desiredLocation == 2)) {
            return 1;
        }
        if (i == 2 && this.desiredLocation == 1) {
            return 1;
        }
        return 0;
    }

    public final float getTransformationProgress() {
        float qSTransformationProgress = getQSTransformationProgress();
        if (this.statusbarState != 1 && qSTransformationProgress >= 0.0f) {
            return qSTransformationProgress;
        }
        if (isTransitioningToFullShade()) {
            return this.fullShadeTransitionProgress;
        }
        return -1.0f;
    }

    public final boolean isCurrentlyFading() {
        if (isTransitioningToFullShade()) {
            return true;
        }
        return this.isCrossFadeAnimatorRunning;
    }

    public final boolean isCurrentlyInGuidedTransformation() {
        if (getTransformationProgress() >= 0.0f) {
            return true;
        }
        return false;
    }

    public final boolean isVisibleToUser() {
        if (isLockScreenVisibleToUser() || isLockScreenShadeVisibleToUser() || isHomeScreenShadeVisibleToUser()) {
            return true;
        }
        return false;
    }

    public final void updateTargetState() {
        Rect rect = null;
        if (!isCurrentlyInGuidedTransformation() || isCurrentlyFading()) {
            MediaHost host = getHost(this.desiredLocation);
            if (host != null) {
                rect = host.getCurrentBounds();
            }
            if (rect != null) {
                this.targetBounds.set(rect);
                return;
            }
            return;
        }
        float transformationProgress = getTransformationProgress();
        MediaHost host2 = getHost(this.desiredLocation);
        Intrinsics.checkNotNull(host2);
        MediaHost host3 = getHost(this.previousLocation);
        Intrinsics.checkNotNull(host3);
        if (!host2.getVisible()) {
            host2 = host3;
        } else if (!host3.getVisible()) {
            host3 = host2;
        }
        this.targetBounds = interpolateBounds(host3.getCurrentBounds(), host2.getCurrentBounds(), transformationProgress, null);
    }
}
