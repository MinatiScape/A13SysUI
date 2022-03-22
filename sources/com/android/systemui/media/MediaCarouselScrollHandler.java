package com.android.systemui.media;

import android.graphics.Outline;
import android.util.MathUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import androidx.core.view.GestureDetectorCompat;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.PageIndicator;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.wm.shell.animation.PhysicsAnimator;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaCarouselScrollHandler.kt */
/* loaded from: classes.dex */
public final class MediaCarouselScrollHandler {
    public static final MediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1 CONTENT_TRANSLATION = new FloatPropertyCompat<MediaCarouselScrollHandler>() { // from class: com.android.systemui.media.MediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1
        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final float getValue(MediaCarouselScrollHandler mediaCarouselScrollHandler) {
            return mediaCarouselScrollHandler.contentTranslation;
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final void setValue(MediaCarouselScrollHandler mediaCarouselScrollHandler, float f) {
            mediaCarouselScrollHandler.setContentTranslation(f);
        }
    };
    public int carouselHeight;
    public int carouselWidth;
    public final Function1<Boolean, Unit> closeGuts;
    public float contentTranslation;
    public int cornerRadius;
    public final Function0<Unit> dismissCallback;
    public final FalsingCollector falsingCollector;
    public final FalsingManager falsingManager;
    public boolean falsingProtectionNeeded;
    public final GestureDetectorCompat gestureDetector;
    public final Function1<Boolean, Unit> logSmartspaceImpression;
    public final DelayableExecutor mainExecutor;
    public ViewGroup mediaContent;
    public final PageIndicator pageIndicator;
    public int playerWidthPlusPadding;
    public boolean qsExpanded;
    public int scrollIntoCurrentMedia;
    public final MediaScrollView scrollView;
    public View settingsButton;
    public boolean showsSettingsButton;
    public Function0<Unit> translationChangedListener;
    public int visibleMediaIndex;
    public boolean visibleToUser;

    public final int getMaxTranslation() {
        if (!this.showsSettingsButton) {
            return this.playerWidthPlusPadding;
        }
        View view = this.settingsButton;
        if (view == null) {
            view = null;
        }
        return view.getWidth();
    }

    public final void resetTranslation(boolean z) {
        boolean z2;
        if (this.scrollView.getContentTranslation() == 0.0f) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (z2) {
            return;
        }
        if (z) {
            Function1<Object, ? extends PhysicsAnimator<?>> function1 = PhysicsAnimator.instanceConstructor;
            PhysicsAnimator instance = PhysicsAnimator.Companion.getInstance(this);
            instance.spring(CONTENT_TRANSLATION, 0.0f, 0.0f, MediaCarouselScrollHandlerKt.translationConfig);
            instance.start();
            MediaScrollView mediaScrollView = this.scrollView;
            Objects.requireNonNull(mediaScrollView);
            mediaScrollView.animationTargetX = 0.0f;
            return;
        }
        Function1<Object, ? extends PhysicsAnimator<?>> function12 = PhysicsAnimator.instanceConstructor;
        PhysicsAnimator.Companion.getInstance(this).cancel();
        setContentTranslation(0.0f);
    }

    public final void scrollToPlayer(int i, int i2) {
        if (i >= 0 && i < this.mediaContent.getChildCount()) {
            MediaScrollView mediaScrollView = this.scrollView;
            int i3 = i * this.playerWidthPlusPadding;
            Objects.requireNonNull(mediaScrollView);
            if (mediaScrollView.isLayoutRtl()) {
                ViewGroup viewGroup = mediaScrollView.contentContainer;
                if (viewGroup == null) {
                    viewGroup = null;
                }
                i3 = (viewGroup.getWidth() - mediaScrollView.getWidth()) - i3;
            }
            mediaScrollView.setScrollX(i3);
        }
        final View childAt = this.mediaContent.getChildAt(Math.min(this.mediaContent.getChildCount() - 1, i2));
        this.mainExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.media.MediaCarouselScrollHandler$scrollToPlayer$1
            @Override // java.lang.Runnable
            public final void run() {
                MediaCarouselScrollHandler.this.scrollView.smoothScrollTo(childAt.getLeft(), MediaCarouselScrollHandler.this.scrollView.getScrollY());
            }
        }, 100L);
    }

    public final void setContentTranslation(float f) {
        boolean z;
        this.contentTranslation = f;
        this.mediaContent.setTranslationX(f);
        updateSettingsPresentation();
        this.translationChangedListener.invoke();
        boolean z2 = true;
        if (this.contentTranslation == 0.0f) {
            z = true;
        } else {
            z = false;
        }
        if (z && this.scrollIntoCurrentMedia == 0) {
            z2 = false;
        }
        this.scrollView.setClipToOutline(z2);
    }

    public final void updatePlayerVisibilities() {
        boolean z;
        boolean z2;
        int i;
        if (this.scrollIntoCurrentMedia != 0) {
            z = true;
        } else {
            z = false;
        }
        int childCount = this.mediaContent.getChildCount();
        int i2 = 0;
        while (i2 < childCount) {
            int i3 = i2 + 1;
            View childAt = this.mediaContent.getChildAt(i2);
            int i4 = this.visibleMediaIndex;
            if (i2 == i4 || (i2 == i4 + 1 && z)) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z2) {
                i = 0;
            } else {
                i = 4;
            }
            childAt.setVisibility(i);
            i2 = i3;
        }
    }

    public final void updateSettingsPresentation() {
        boolean z;
        int i = 4;
        View view = null;
        if (this.showsSettingsButton) {
            View view2 = this.settingsButton;
            if (view2 == null) {
                view2 = null;
            }
            if (view2.getWidth() > 0) {
                float map = MathUtils.map(0.0f, getMaxTranslation(), 0.0f, 1.0f, Math.abs(this.contentTranslation));
                float f = 1.0f - map;
                View view3 = this.settingsButton;
                if (view3 == null) {
                    view3 = null;
                }
                float f2 = (-view3.getWidth()) * f * 0.3f;
                if (this.scrollView.isLayoutRtl()) {
                    if (this.contentTranslation > 0.0f) {
                        float width = this.scrollView.getWidth() - f2;
                        View view4 = this.settingsButton;
                        if (view4 == null) {
                            view4 = null;
                        }
                        f2 = -(width - view4.getWidth());
                    } else {
                        f2 = -f2;
                    }
                } else if (this.contentTranslation <= 0.0f) {
                    float width2 = this.scrollView.getWidth() - f2;
                    View view5 = this.settingsButton;
                    if (view5 == null) {
                        view5 = null;
                    }
                    f2 = width2 - view5.getWidth();
                }
                float f3 = f * 50;
                View view6 = this.settingsButton;
                if (view6 == null) {
                    view6 = null;
                }
                view6.setRotation(f3 * (-Math.signum(this.contentTranslation)));
                float saturate = MathUtils.saturate(MathUtils.map(0.5f, 1.0f, 0.0f, 1.0f, map));
                View view7 = this.settingsButton;
                if (view7 == null) {
                    view7 = null;
                }
                view7.setAlpha(saturate);
                View view8 = this.settingsButton;
                if (view8 == null) {
                    view8 = null;
                }
                if (saturate == 0.0f) {
                    z = true;
                } else {
                    z = false;
                }
                if (!z) {
                    i = 0;
                }
                view8.setVisibility(i);
                View view9 = this.settingsButton;
                if (view9 == null) {
                    view9 = null;
                }
                view9.setTranslationX(f2);
                View view10 = this.settingsButton;
                if (view10 == null) {
                    view10 = null;
                }
                int height = this.scrollView.getHeight();
                View view11 = this.settingsButton;
                if (view11 != null) {
                    view = view11;
                }
                view10.setTranslationY((height - view.getHeight()) / 2.0f);
                return;
            }
        }
        View view12 = this.settingsButton;
        if (view12 != null) {
            view = view12;
        }
        view.setVisibility(4);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public MediaCarouselScrollHandler(MediaScrollView mediaScrollView, PageIndicator pageIndicator, DelayableExecutor delayableExecutor, Function0<Unit> function0, Function0<Unit> function02, Function1<? super Boolean, Unit> function1, FalsingCollector falsingCollector, FalsingManager falsingManager, Function1<? super Boolean, Unit> function12) {
        this.scrollView = mediaScrollView;
        this.pageIndicator = pageIndicator;
        this.mainExecutor = delayableExecutor;
        this.dismissCallback = function0;
        this.translationChangedListener = function02;
        this.closeGuts = function1;
        this.falsingCollector = falsingCollector;
        this.falsingManager = falsingManager;
        this.logSmartspaceImpression = function12;
        GestureDetector.SimpleOnGestureListener mediaCarouselScrollHandler$gestureListener$1 = new GestureDetector.SimpleOnGestureListener() { // from class: com.android.systemui.media.MediaCarouselScrollHandler$gestureListener$1
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public final boolean onDown(MotionEvent motionEvent) {
                MediaCarouselScrollHandler mediaCarouselScrollHandler = MediaCarouselScrollHandler.this;
                Objects.requireNonNull(mediaCarouselScrollHandler);
                if (!mediaCarouselScrollHandler.falsingProtectionNeeded) {
                    return false;
                }
                MediaCarouselScrollHandler.this.falsingCollector.onNotificationStartDismissing();
                return false;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public final boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                boolean z;
                int i;
                boolean z2;
                boolean z3;
                final MediaCarouselScrollHandler mediaCarouselScrollHandler = MediaCarouselScrollHandler.this;
                Objects.requireNonNull(mediaCarouselScrollHandler);
                float f3 = f * f;
                double d = f2;
                boolean z4 = false;
                if (f3 < 0.5d * d * d || f3 < 1000000.0f) {
                    return false;
                }
                float contentTranslation = mediaCarouselScrollHandler.scrollView.getContentTranslation();
                float f4 = 0.0f;
                if (contentTranslation == 0.0f) {
                    z = true;
                } else {
                    z = false;
                }
                if (!z) {
                    if (Math.signum(f) == Math.signum(contentTranslation)) {
                        z3 = true;
                    } else {
                        z3 = false;
                    }
                    if (z3) {
                        if (mediaCarouselScrollHandler.falsingProtectionNeeded && mediaCarouselScrollHandler.falsingManager.isFalseTouch(1)) {
                            z4 = true;
                        }
                        if (!z4) {
                            f4 = mediaCarouselScrollHandler.getMaxTranslation() * Math.signum(contentTranslation);
                            if (!mediaCarouselScrollHandler.showsSettingsButton) {
                                mediaCarouselScrollHandler.mainExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.media.MediaCarouselScrollHandler$onFling$1
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        MediaCarouselScrollHandler.this.dismissCallback.invoke();
                                    }
                                }, 100L);
                            }
                        }
                    }
                    Function1<Object, ? extends PhysicsAnimator<?>> function13 = PhysicsAnimator.instanceConstructor;
                    PhysicsAnimator instance = PhysicsAnimator.Companion.getInstance(mediaCarouselScrollHandler);
                    instance.spring(MediaCarouselScrollHandler.CONTENT_TRANSLATION, f4, f, MediaCarouselScrollHandlerKt.translationConfig);
                    instance.start();
                    MediaScrollView mediaScrollView2 = mediaCarouselScrollHandler.scrollView;
                    Objects.requireNonNull(mediaScrollView2);
                    mediaScrollView2.animationTargetX = f4;
                } else {
                    MediaScrollView mediaScrollView3 = mediaCarouselScrollHandler.scrollView;
                    Objects.requireNonNull(mediaScrollView3);
                    int scrollX = mediaScrollView3.getScrollX();
                    if (mediaScrollView3.isLayoutRtl()) {
                        ViewGroup viewGroup = mediaScrollView3.contentContainer;
                        if (viewGroup == null) {
                            viewGroup = null;
                        }
                        scrollX = (viewGroup.getWidth() - mediaScrollView3.getWidth()) - scrollX;
                    }
                    int i2 = mediaCarouselScrollHandler.playerWidthPlusPadding;
                    if (i2 > 0) {
                        i = scrollX / i2;
                    } else {
                        i = 0;
                    }
                    if (!mediaCarouselScrollHandler.scrollView.isLayoutRtl() ? f >= 0.0f : f <= 0.0f) {
                        z2 = false;
                    } else {
                        z2 = true;
                    }
                    if (z2) {
                        i++;
                    }
                    final View childAt = mediaCarouselScrollHandler.mediaContent.getChildAt(Math.min(mediaCarouselScrollHandler.mediaContent.getChildCount() - 1, Math.max(0, i)));
                    mediaCarouselScrollHandler.mainExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaCarouselScrollHandler$onFling$2
                        @Override // java.lang.Runnable
                        public final void run() {
                            MediaCarouselScrollHandler.this.scrollView.smoothScrollTo(childAt.getLeft(), MediaCarouselScrollHandler.this.scrollView.getScrollY());
                        }
                    });
                }
                return true;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public final boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                boolean z;
                boolean z2;
                boolean z3;
                MediaCarouselScrollHandler mediaCarouselScrollHandler = MediaCarouselScrollHandler.this;
                Intrinsics.checkNotNull(motionEvent);
                Intrinsics.checkNotNull(motionEvent2);
                Objects.requireNonNull(mediaCarouselScrollHandler);
                float x = motionEvent2.getX() - motionEvent.getX();
                float contentTranslation = mediaCarouselScrollHandler.scrollView.getContentTranslation();
                int i = (contentTranslation > 0.0f ? 1 : (contentTranslation == 0.0f ? 0 : -1));
                boolean z4 = false;
                if (i == 0) {
                    z = true;
                } else {
                    z = false;
                }
                if (z && mediaCarouselScrollHandler.scrollView.canScrollHorizontally((int) (-x))) {
                    return false;
                }
                float f3 = contentTranslation - f;
                float abs = Math.abs(f3);
                if (abs > mediaCarouselScrollHandler.getMaxTranslation()) {
                    if (Math.signum(f) == Math.signum(contentTranslation)) {
                        z3 = true;
                    } else {
                        z3 = false;
                    }
                    if (!z3) {
                        if (Math.abs(contentTranslation) > mediaCarouselScrollHandler.getMaxTranslation()) {
                            f3 = contentTranslation - (f * 0.2f);
                        } else {
                            f3 = Math.signum(f3) * (((abs - mediaCarouselScrollHandler.getMaxTranslation()) * 0.2f) + mediaCarouselScrollHandler.getMaxTranslation());
                        }
                    }
                }
                if (Math.signum(f3) == Math.signum(contentTranslation)) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (!z2) {
                    if (i == 0) {
                        z4 = true;
                    }
                    if (!z4 && mediaCarouselScrollHandler.scrollView.canScrollHorizontally(-((int) f3))) {
                        f3 = 0.0f;
                    }
                }
                Function1<Object, ? extends PhysicsAnimator<?>> function13 = PhysicsAnimator.instanceConstructor;
                PhysicsAnimator instance = PhysicsAnimator.Companion.getInstance(mediaCarouselScrollHandler);
                if (instance.isRunning()) {
                    instance.spring(MediaCarouselScrollHandler.CONTENT_TRANSLATION, f3, 0.0f, MediaCarouselScrollHandlerKt.translationConfig);
                    instance.start();
                } else {
                    mediaCarouselScrollHandler.setContentTranslation(f3);
                }
                MediaScrollView mediaScrollView2 = mediaCarouselScrollHandler.scrollView;
                Objects.requireNonNull(mediaScrollView2);
                mediaScrollView2.animationTargetX = f3;
                return true;
            }
        };
        Gefingerpoken mediaCarouselScrollHandler$touchListener$1 = new Gefingerpoken() { // from class: com.android.systemui.media.MediaCarouselScrollHandler$touchListener$1
            @Override // com.android.systemui.Gefingerpoken
            public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                MediaCarouselScrollHandler mediaCarouselScrollHandler = MediaCarouselScrollHandler.this;
                Intrinsics.checkNotNull(motionEvent);
                Objects.requireNonNull(mediaCarouselScrollHandler);
                return mediaCarouselScrollHandler.gestureDetector.onTouchEvent(motionEvent);
            }

            @Override // com.android.systemui.Gefingerpoken
            public final boolean onTouchEvent(MotionEvent motionEvent) {
                boolean z;
                int i;
                boolean z2;
                float f;
                boolean z3;
                final MediaCarouselScrollHandler mediaCarouselScrollHandler = MediaCarouselScrollHandler.this;
                Intrinsics.checkNotNull(motionEvent);
                Objects.requireNonNull(mediaCarouselScrollHandler);
                boolean z4 = true;
                if (motionEvent.getAction() == 1) {
                    z = true;
                } else {
                    z = false;
                }
                if (z && mediaCarouselScrollHandler.falsingProtectionNeeded) {
                    mediaCarouselScrollHandler.falsingCollector.onNotificationStopDismissing();
                }
                if (mediaCarouselScrollHandler.gestureDetector.onTouchEvent(motionEvent)) {
                    if (z) {
                        mediaCarouselScrollHandler.scrollView.cancelCurrentScroll();
                        return true;
                    }
                } else if (z || motionEvent.getAction() == 3) {
                    MediaScrollView mediaScrollView2 = mediaCarouselScrollHandler.scrollView;
                    Objects.requireNonNull(mediaScrollView2);
                    int scrollX = mediaScrollView2.getScrollX();
                    ViewGroup viewGroup = null;
                    if (mediaScrollView2.isLayoutRtl()) {
                        ViewGroup viewGroup2 = mediaScrollView2.contentContainer;
                        if (viewGroup2 == null) {
                            viewGroup2 = null;
                        }
                        scrollX = (viewGroup2.getWidth() - mediaScrollView2.getWidth()) - scrollX;
                    }
                    int i2 = mediaCarouselScrollHandler.playerWidthPlusPadding;
                    int i3 = scrollX % i2;
                    if (i3 > i2 / 2) {
                        i = i2 - i3;
                    } else {
                        i = i3 * (-1);
                    }
                    if (i != 0) {
                        if (mediaCarouselScrollHandler.scrollView.isLayoutRtl()) {
                            i = -i;
                        }
                        MediaScrollView mediaScrollView3 = mediaCarouselScrollHandler.scrollView;
                        Objects.requireNonNull(mediaScrollView3);
                        int scrollX2 = mediaScrollView3.getScrollX();
                        if (mediaScrollView3.isLayoutRtl()) {
                            ViewGroup viewGroup3 = mediaScrollView3.contentContainer;
                            if (viewGroup3 != null) {
                                viewGroup = viewGroup3;
                            }
                            scrollX2 = (viewGroup.getWidth() - mediaScrollView3.getWidth()) - scrollX2;
                        }
                        final int i4 = scrollX2 + i;
                        mediaCarouselScrollHandler.mainExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaCarouselScrollHandler$onTouch$1
                            @Override // java.lang.Runnable
                            public final void run() {
                                MediaScrollView mediaScrollView4 = MediaCarouselScrollHandler.this.scrollView;
                                mediaScrollView4.smoothScrollTo(i4, mediaScrollView4.getScrollY());
                            }
                        });
                    }
                    float contentTranslation = mediaCarouselScrollHandler.scrollView.getContentTranslation();
                    if (contentTranslation == 0.0f) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    if (!z2) {
                        if (Math.abs(contentTranslation) >= mediaCarouselScrollHandler.getMaxTranslation() / 2) {
                            if (!mediaCarouselScrollHandler.falsingProtectionNeeded || !mediaCarouselScrollHandler.falsingManager.isFalseTouch(1)) {
                                z3 = false;
                            } else {
                                z3 = true;
                            }
                            if (!z3) {
                                z4 = false;
                            }
                        }
                        if (z4) {
                            f = 0.0f;
                        } else {
                            f = Math.signum(contentTranslation) * mediaCarouselScrollHandler.getMaxTranslation();
                            if (!mediaCarouselScrollHandler.showsSettingsButton) {
                                mediaCarouselScrollHandler.mainExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.media.MediaCarouselScrollHandler$onTouch$2
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        MediaCarouselScrollHandler.this.dismissCallback.invoke();
                                    }
                                }, 100L);
                            }
                        }
                        Function1<Object, ? extends PhysicsAnimator<?>> function13 = PhysicsAnimator.instanceConstructor;
                        PhysicsAnimator instance = PhysicsAnimator.Companion.getInstance(mediaCarouselScrollHandler);
                        instance.spring(MediaCarouselScrollHandler.CONTENT_TRANSLATION, f, 0.0f, MediaCarouselScrollHandlerKt.translationConfig);
                        instance.start();
                        MediaScrollView mediaScrollView4 = mediaCarouselScrollHandler.scrollView;
                        Objects.requireNonNull(mediaScrollView4);
                        mediaScrollView4.animationTargetX = f;
                    }
                }
                return false;
            }
        };
        View.OnScrollChangeListener mediaCarouselScrollHandler$scrollChangedListener$1 = new View.OnScrollChangeListener() { // from class: com.android.systemui.media.MediaCarouselScrollHandler$scrollChangedListener$1
            @Override // android.view.View.OnScrollChangeListener
            public final void onScrollChange(View view, int i, int i2, int i3, int i4) {
                boolean z;
                boolean z2;
                float f;
                boolean z3;
                MediaCarouselScrollHandler mediaCarouselScrollHandler = MediaCarouselScrollHandler.this;
                Objects.requireNonNull(mediaCarouselScrollHandler);
                if (mediaCarouselScrollHandler.playerWidthPlusPadding != 0) {
                    MediaScrollView mediaScrollView2 = MediaCarouselScrollHandler.this.scrollView;
                    Objects.requireNonNull(mediaScrollView2);
                    int scrollX = mediaScrollView2.getScrollX();
                    if (mediaScrollView2.isLayoutRtl()) {
                        ViewGroup viewGroup = mediaScrollView2.contentContainer;
                        if (viewGroup == null) {
                            viewGroup = null;
                        }
                        scrollX = (viewGroup.getWidth() - mediaScrollView2.getWidth()) - scrollX;
                    }
                    MediaCarouselScrollHandler mediaCarouselScrollHandler2 = MediaCarouselScrollHandler.this;
                    Objects.requireNonNull(mediaCarouselScrollHandler2);
                    int i5 = scrollX / mediaCarouselScrollHandler2.playerWidthPlusPadding;
                    MediaCarouselScrollHandler mediaCarouselScrollHandler3 = MediaCarouselScrollHandler.this;
                    Objects.requireNonNull(mediaCarouselScrollHandler3);
                    int i6 = scrollX % mediaCarouselScrollHandler3.playerWidthPlusPadding;
                    boolean z4 = true;
                    if (mediaCarouselScrollHandler2.scrollIntoCurrentMedia != 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                    mediaCarouselScrollHandler2.scrollIntoCurrentMedia = i6;
                    if (i6 != 0) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    int i7 = mediaCarouselScrollHandler2.visibleMediaIndex;
                    if (!(i5 == i7 && z == z2)) {
                        mediaCarouselScrollHandler2.visibleMediaIndex = i5;
                        if (i7 != i5 && mediaCarouselScrollHandler2.visibleToUser) {
                            mediaCarouselScrollHandler2.logSmartspaceImpression.invoke(Boolean.valueOf(mediaCarouselScrollHandler2.qsExpanded));
                        }
                        mediaCarouselScrollHandler2.closeGuts.invoke(Boolean.FALSE);
                        mediaCarouselScrollHandler2.updatePlayerVisibilities();
                    }
                    float f2 = mediaCarouselScrollHandler2.visibleMediaIndex;
                    int i8 = mediaCarouselScrollHandler2.playerWidthPlusPadding;
                    if (i8 > 0) {
                        f = i6 / i8;
                    } else {
                        f = 0.0f;
                    }
                    float f3 = f2 + f;
                    if (mediaCarouselScrollHandler2.scrollView.isLayoutRtl()) {
                        f3 = (mediaCarouselScrollHandler2.mediaContent.getChildCount() - f3) - 1;
                    }
                    mediaCarouselScrollHandler2.pageIndicator.setLocation(f3);
                    if (mediaCarouselScrollHandler2.contentTranslation == 0.0f) {
                        z3 = true;
                    } else {
                        z3 = false;
                    }
                    if (z3 && mediaCarouselScrollHandler2.scrollIntoCurrentMedia == 0) {
                        z4 = false;
                    }
                    mediaCarouselScrollHandler2.scrollView.setClipToOutline(z4);
                }
            }
        };
        this.gestureDetector = new GestureDetectorCompat(mediaScrollView.getContext(), mediaCarouselScrollHandler$gestureListener$1);
        mediaScrollView.touchListener = mediaCarouselScrollHandler$touchListener$1;
        mediaScrollView.setOverScrollMode(2);
        ViewGroup viewGroup = mediaScrollView.contentContainer;
        this.mediaContent = viewGroup == null ? null : viewGroup;
        mediaScrollView.setOnScrollChangeListener(mediaCarouselScrollHandler$scrollChangedListener$1);
        mediaScrollView.setOutlineProvider(new ViewOutlineProvider() { // from class: com.android.systemui.media.MediaCarouselScrollHandler.1
            @Override // android.view.ViewOutlineProvider
            public final void getOutline(View view, Outline outline) {
                if (outline != null) {
                    MediaCarouselScrollHandler mediaCarouselScrollHandler = MediaCarouselScrollHandler.this;
                    outline.setRoundRect(0, 0, mediaCarouselScrollHandler.carouselWidth, mediaCarouselScrollHandler.carouselHeight, mediaCarouselScrollHandler.cornerRadius);
                }
            }
        });
    }

    public final void onPlayersChanged() {
        int i;
        updatePlayerVisibilities();
        int dimensionPixelSize = this.scrollView.getContext().getResources().getDimensionPixelSize(2131166889);
        int childCount = this.mediaContent.getChildCount();
        int i2 = 0;
        while (i2 < childCount) {
            int i3 = i2 + 1;
            View childAt = this.mediaContent.getChildAt(i2);
            if (i2 == childCount - 1) {
                i = 0;
            } else {
                i = dimensionPixelSize;
            }
            ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
            Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            if (marginLayoutParams.getMarginEnd() != i) {
                marginLayoutParams.setMarginEnd(i);
                childAt.setLayoutParams(marginLayoutParams);
            }
            i2 = i3;
        }
    }
}
