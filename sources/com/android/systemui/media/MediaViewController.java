package com.android.systemui.media;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintSet;
import com.android.systemui.media.MediaHost;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.animation.MeasurementInput;
import com.android.systemui.util.animation.MeasurementOutput;
import com.android.systemui.util.animation.TransitionLayout;
import com.android.systemui.util.animation.TransitionLayoutController;
import com.android.systemui.util.animation.TransitionViewState;
import com.android.systemui.util.animation.WidgetState;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: MediaViewController.kt */
/* loaded from: classes.dex */
public final class MediaViewController {
    public boolean animateNextStateChange;
    public long animationDelay;
    public long animationDuration;
    public final ConfigurationController configurationController;
    public final MediaViewController$configurationListener$1 configurationListener;
    public final Context context;
    public int currentHeight;
    public int currentWidth;
    public boolean isGutsVisible;
    public final TransitionLayoutController layoutController;
    public final MediaHostStatesManager mediaHostStatesManager;
    public boolean shouldHideGutsSettings;
    public Function0<Unit> sizeChangedListener;
    public TransitionLayout transitionLayout;
    public boolean firstRefresh = true;
    public final MeasurementOutput measurement = new MeasurementOutput();
    public TYPE type = TYPE.PLAYER;
    public final LinkedHashMap viewStates = new LinkedHashMap();
    public int currentEndLocation = -1;
    public int currentStartLocation = -1;
    public float currentTransitionProgress = 1.0f;
    public final TransitionViewState tmpState = new TransitionViewState();
    public final TransitionViewState tmpState2 = new TransitionViewState();
    public final TransitionViewState tmpState3 = new TransitionViewState();
    public final CacheKey tmpKey = new CacheKey(0);
    public final MediaViewController$stateCallback$1 stateCallback = new MediaViewController$stateCallback$1(this);
    public final ConstraintSet collapsedLayout = new ConstraintSet();
    public final ConstraintSet expandedLayout = new ConstraintSet();

    /* compiled from: MediaViewController.kt */
    /* renamed from: com.android.systemui.media.MediaViewController$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass1 extends Lambda implements Function2<Integer, Integer, Unit> {
        public AnonymousClass1() {
            super(2);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Unit invoke(Integer num, Integer num2) {
            int intValue = num.intValue();
            int intValue2 = num2.intValue();
            MediaViewController mediaViewController = MediaViewController.this;
            Objects.requireNonNull(mediaViewController);
            mediaViewController.currentWidth = intValue;
            MediaViewController mediaViewController2 = MediaViewController.this;
            Objects.requireNonNull(mediaViewController2);
            mediaViewController2.currentHeight = intValue2;
            MediaViewController mediaViewController3 = MediaViewController.this;
            Objects.requireNonNull(mediaViewController3);
            Function0<Unit> function0 = mediaViewController3.sizeChangedListener;
            if (function0 == null) {
                function0 = null;
            }
            function0.invoke();
            return Unit.INSTANCE;
        }
    }

    /* compiled from: MediaViewController.kt */
    /* loaded from: classes.dex */
    public enum TYPE {
        PLAYER,
        PLAYER_SESSION,
        RECOMMENDATION
    }

    public final TransitionViewState obtainViewState(MediaHostState mediaHostState) {
        int i;
        int i2;
        boolean z;
        ConstraintSet constraintSet;
        Set<Integer> set;
        Set<Integer> set2;
        WidgetState widgetState;
        float f;
        float f2;
        boolean z2;
        if (mediaHostState == null || mediaHostState.getMeasurementInput() == null) {
            return null;
        }
        boolean z3 = this.isGutsVisible;
        CacheKey cacheKey = this.tmpKey;
        MeasurementInput measurementInput = mediaHostState.getMeasurementInput();
        boolean z4 = false;
        if (measurementInput == null) {
            i = 0;
        } else {
            i = measurementInput.heightMeasureSpec;
        }
        Objects.requireNonNull(cacheKey);
        cacheKey.heightMeasureSpec = i;
        MeasurementInput measurementInput2 = mediaHostState.getMeasurementInput();
        if (measurementInput2 == null) {
            i2 = 0;
        } else {
            i2 = measurementInput2.widthMeasureSpec;
        }
        cacheKey.widthMeasureSpec = i2;
        cacheKey.expansion = mediaHostState.getExpansion();
        cacheKey.gutsVisible = z3;
        TransitionViewState transitionViewState = (TransitionViewState) this.viewStates.get(cacheKey);
        if (transitionViewState != null) {
            return transitionViewState;
        }
        CacheKey cacheKey2 = new CacheKey(cacheKey.widthMeasureSpec, cacheKey.heightMeasureSpec, cacheKey.expansion, cacheKey.gutsVisible);
        if (this.transitionLayout == null) {
            return null;
        }
        if (mediaHostState.getExpansion() == 0.0f) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            if (mediaHostState.getExpansion() == 1.0f) {
                z4 = true;
            }
            if (!z4) {
                MediaHost.MediaHostStateHolder copy = mediaHostState.copy();
                copy.setExpansion(0.0f);
                TransitionViewState obtainViewState = obtainViewState(copy);
                Objects.requireNonNull(obtainViewState, "null cannot be cast to non-null type com.android.systemui.util.animation.TransitionViewState");
                MediaHost.MediaHostStateHolder copy2 = mediaHostState.copy();
                copy2.setExpansion(1.0f);
                TransitionViewState obtainViewState2 = obtainViewState(copy2);
                Objects.requireNonNull(obtainViewState2, "null cannot be cast to non-null type com.android.systemui.util.animation.TransitionViewState");
                return this.layoutController.getInterpolatedState(obtainViewState, obtainViewState2, mediaHostState.getExpansion(), null);
            }
        }
        TransitionLayout transitionLayout = this.transitionLayout;
        Intrinsics.checkNotNull(transitionLayout);
        MeasurementInput measurementInput3 = mediaHostState.getMeasurementInput();
        Intrinsics.checkNotNull(measurementInput3);
        if (mediaHostState.getExpansion() > 0.0f) {
            constraintSet = this.expandedLayout;
        } else {
            constraintSet = this.collapsedLayout;
        }
        TransitionViewState transitionViewState2 = new TransitionViewState();
        transitionLayout.calculateViewState(measurementInput3, constraintSet, transitionViewState2);
        int ordinal = this.type.ordinal();
        if (ordinal == 0) {
            set = PlayerViewHolder.controlsIds;
        } else if (ordinal == 1) {
            set = PlayerSessionViewHolder.controlsIds;
        } else if (ordinal == 2) {
            set = RecommendationViewHolder.controlsIds;
        } else {
            throw new NoWhenBranchMatchedException();
        }
        int ordinal2 = this.type.ordinal();
        if (ordinal2 == 0) {
            set2 = PlayerViewHolder.gutsIds;
        } else if (ordinal2 == 1) {
            set2 = PlayerSessionViewHolder.gutsIds;
        } else if (ordinal2 == 2) {
            set2 = RecommendationViewHolder.gutsIds;
        } else {
            throw new NoWhenBranchMatchedException();
        }
        for (Number number : set) {
            WidgetState widgetState2 = (WidgetState) transitionViewState2.widgetStates.get(Integer.valueOf(number.intValue()));
            if (widgetState2 != null) {
                boolean z5 = this.isGutsVisible;
                if (z5) {
                    f2 = 0.0f;
                } else {
                    f2 = widgetState2.alpha;
                }
                widgetState2.alpha = f2;
                if (z5) {
                    z2 = true;
                } else {
                    z2 = widgetState2.gone;
                }
                widgetState2.gone = z2;
            }
        }
        for (Number number2 : set2) {
            int intValue = number2.intValue();
            WidgetState widgetState3 = (WidgetState) transitionViewState2.widgetStates.get(Integer.valueOf(intValue));
            if (widgetState3 != null) {
                if (this.isGutsVisible) {
                    f = 1.0f;
                } else {
                    f = 0.0f;
                }
                widgetState3.alpha = f;
            }
            WidgetState widgetState4 = (WidgetState) transitionViewState2.widgetStates.get(Integer.valueOf(intValue));
            if (widgetState4 != null) {
                widgetState4.gone = !this.isGutsVisible;
            }
        }
        if (this.shouldHideGutsSettings && (widgetState = (WidgetState) transitionViewState2.widgetStates.get(2131428837)) != null) {
            widgetState.gone = true;
        }
        this.viewStates.put(cacheKey2, transitionViewState2);
        return transitionViewState2;
    }

    public final TransitionViewState updateViewStateToCarouselSize(TransitionViewState transitionViewState, int i, TransitionViewState transitionViewState2) {
        TransitionViewState transitionViewState3;
        if (transitionViewState == null) {
            transitionViewState3 = null;
        } else {
            transitionViewState3 = transitionViewState.copy(transitionViewState2);
        }
        if (transitionViewState3 == null) {
            return null;
        }
        MediaHostStatesManager mediaHostStatesManager = this.mediaHostStatesManager;
        Objects.requireNonNull(mediaHostStatesManager);
        MeasurementOutput measurementOutput = (MeasurementOutput) mediaHostStatesManager.carouselSizes.get(Integer.valueOf(i));
        if (measurementOutput != null) {
            transitionViewState3.height = Math.max(measurementOutput.measuredHeight, transitionViewState3.height);
            transitionViewState3.width = Math.max(measurementOutput.measuredWidth, transitionViewState3.width);
        }
        return transitionViewState3;
    }

    public final void attach(TransitionLayout transitionLayout, TYPE type) {
        this.type = type;
        int ordinal = type.ordinal();
        if (ordinal == 0) {
            this.collapsedLayout.load(this.context, 2132213763);
            this.expandedLayout.load(this.context, 2132213764);
        } else if (ordinal == 1) {
            this.collapsedLayout.load(this.context, 2132213767);
            this.expandedLayout.load(this.context, 2132213768);
        } else if (ordinal == 2) {
            this.collapsedLayout.load(this.context, 2132213765);
            this.expandedLayout.load(this.context, 2132213766);
        }
        refreshState();
        this.transitionLayout = transitionLayout;
        TransitionLayoutController transitionLayoutController = this.layoutController;
        Objects.requireNonNull(transitionLayoutController);
        transitionLayoutController.transitionLayout = transitionLayout;
        int i = this.currentEndLocation;
        if (i != -1) {
            setCurrentState(this.currentStartLocation, i, this.currentTransitionProgress, true);
        }
    }

    public final void refreshState() {
        this.viewStates.clear();
        if (this.firstRefresh) {
            MediaHostStatesManager mediaHostStatesManager = this.mediaHostStatesManager;
            Objects.requireNonNull(mediaHostStatesManager);
            for (Map.Entry entry : mediaHostStatesManager.mediaHostStates.entrySet()) {
                obtainViewState((MediaHostState) entry.getValue());
            }
            this.firstRefresh = false;
        }
        setCurrentState(this.currentStartLocation, this.currentEndLocation, this.currentTransitionProgress, true);
    }

    /* JADX WARN: Code restructure failed: missing block: B:44:0x00c7, code lost:
        if (r7.width != 0) goto L_0x00ce;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void setCurrentState(int r8, int r9, float r10, boolean r11) {
        /*
            Method dump skipped, instructions count: 287
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaViewController.setCurrentState(int, int, float, boolean):void");
    }

    /* JADX WARN: Type inference failed for: r0v9, types: [java.lang.Object, com.android.systemui.media.MediaViewController$configurationListener$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public MediaViewController(android.content.Context r3, com.android.systemui.statusbar.policy.ConfigurationController r4, com.android.systemui.media.MediaHostStatesManager r5) {
        /*
            r2 = this;
            r2.<init>()
            r2.context = r3
            r2.configurationController = r4
            r2.mediaHostStatesManager = r5
            r3 = 1
            r2.firstRefresh = r3
            com.android.systemui.util.animation.TransitionLayoutController r3 = new com.android.systemui.util.animation.TransitionLayoutController
            r3.<init>()
            r2.layoutController = r3
            com.android.systemui.util.animation.MeasurementOutput r0 = new com.android.systemui.util.animation.MeasurementOutput
            r0.<init>()
            r2.measurement = r0
            com.android.systemui.media.MediaViewController$TYPE r0 = com.android.systemui.media.MediaViewController.TYPE.PLAYER
            r2.type = r0
            java.util.LinkedHashMap r0 = new java.util.LinkedHashMap
            r0.<init>()
            r2.viewStates = r0
            r0 = -1
            r2.currentEndLocation = r0
            r2.currentStartLocation = r0
            r0 = 1065353216(0x3f800000, float:1.0)
            r2.currentTransitionProgress = r0
            com.android.systemui.util.animation.TransitionViewState r0 = new com.android.systemui.util.animation.TransitionViewState
            r0.<init>()
            r2.tmpState = r0
            com.android.systemui.util.animation.TransitionViewState r0 = new com.android.systemui.util.animation.TransitionViewState
            r0.<init>()
            r2.tmpState2 = r0
            com.android.systemui.util.animation.TransitionViewState r0 = new com.android.systemui.util.animation.TransitionViewState
            r0.<init>()
            r2.tmpState3 = r0
            com.android.systemui.media.CacheKey r0 = new com.android.systemui.media.CacheKey
            r1 = 0
            r0.<init>(r1)
            r2.tmpKey = r0
            com.android.systemui.media.MediaViewController$configurationListener$1 r0 = new com.android.systemui.media.MediaViewController$configurationListener$1
            r0.<init>()
            r2.configurationListener = r0
            com.android.systemui.media.MediaViewController$stateCallback$1 r1 = new com.android.systemui.media.MediaViewController$stateCallback$1
            r1.<init>(r2)
            r2.stateCallback = r1
            androidx.constraintlayout.widget.ConstraintSet r1 = new androidx.constraintlayout.widget.ConstraintSet
            r1.<init>()
            r2.collapsedLayout = r1
            androidx.constraintlayout.widget.ConstraintSet r1 = new androidx.constraintlayout.widget.ConstraintSet
            r1.<init>()
            r2.expandedLayout = r1
            java.util.Objects.requireNonNull(r5)
            java.util.LinkedHashSet r5 = r5.controllers
            r5.add(r2)
            com.android.systemui.media.MediaViewController$1 r5 = new com.android.systemui.media.MediaViewController$1
            r5.<init>()
            r3.sizeChangedListener = r5
            r4.addCallback(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaViewController.<init>(android.content.Context, com.android.systemui.statusbar.policy.ConfigurationController, com.android.systemui.media.MediaHostStatesManager):void");
    }
}
