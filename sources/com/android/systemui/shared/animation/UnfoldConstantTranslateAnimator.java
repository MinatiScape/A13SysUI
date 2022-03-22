package com.android.systemui.shared.animation;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import kotlin.collections.EmptyList;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: UnfoldConstantTranslateAnimator.kt */
/* loaded from: classes.dex */
public final class UnfoldConstantTranslateAnimator implements UnfoldTransitionProgressProvider.TransitionProgressListener {
    public final UnfoldTransitionProgressProvider progressProvider;
    public ViewGroup rootView;
    public float translationMax;
    public final Set<ViewIdToTranslate> viewsIdToTranslate;
    public List<ViewToTranslate> viewsToTranslate = EmptyList.INSTANCE;

    /* compiled from: UnfoldConstantTranslateAnimator.kt */
    /* loaded from: classes.dex */
    public static final class ViewIdToTranslate {
        public final Direction direction;
        public final Function0<Boolean> shouldBeAnimated;
        public final int viewId;

        public ViewIdToTranslate(int i, Direction direction, Function0<Boolean> function0) {
            this.viewId = i;
            this.direction = direction;
            this.shouldBeAnimated = function0;
        }

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ViewIdToTranslate)) {
                return false;
            }
            ViewIdToTranslate viewIdToTranslate = (ViewIdToTranslate) obj;
            return this.viewId == viewIdToTranslate.viewId && this.direction == viewIdToTranslate.direction && Intrinsics.areEqual(this.shouldBeAnimated, viewIdToTranslate.shouldBeAnimated);
        }

        public final int hashCode() {
            int hashCode = this.direction.hashCode();
            return this.shouldBeAnimated.hashCode() + ((hashCode + (Integer.hashCode(this.viewId) * 31)) * 31);
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("ViewIdToTranslate(viewId=");
            m.append(this.viewId);
            m.append(", direction=");
            m.append(this.direction);
            m.append(", shouldBeAnimated=");
            m.append(this.shouldBeAnimated);
            m.append(')');
            return m.toString();
        }

        public /* synthetic */ ViewIdToTranslate(int i, Direction direction) {
            this(i, direction, AnonymousClass1.INSTANCE);
        }

        /* compiled from: UnfoldConstantTranslateAnimator.kt */
        /* renamed from: com.android.systemui.shared.animation.UnfoldConstantTranslateAnimator$ViewIdToTranslate$1  reason: invalid class name */
        /* loaded from: classes.dex */
        final class AnonymousClass1 extends Lambda implements Function0<Boolean> {
            public static final AnonymousClass1 INSTANCE = new AnonymousClass1();

            public AnonymousClass1() {
                super(0);
            }

            @Override // kotlin.jvm.functions.Function0
            public final /* bridge */ /* synthetic */ Boolean invoke() {
                return Boolean.TRUE;
            }
        }
    }

    /* compiled from: UnfoldConstantTranslateAnimator.kt */
    /* loaded from: classes.dex */
    public static final class ViewToTranslate {
        public final Direction direction;
        public final Function0<Boolean> shouldBeAnimated;
        public final WeakReference<View> view;

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ViewToTranslate)) {
                return false;
            }
            ViewToTranslate viewToTranslate = (ViewToTranslate) obj;
            return Intrinsics.areEqual(this.view, viewToTranslate.view) && this.direction == viewToTranslate.direction && Intrinsics.areEqual(this.shouldBeAnimated, viewToTranslate.shouldBeAnimated);
        }

        public final int hashCode() {
            int hashCode = this.direction.hashCode();
            return this.shouldBeAnimated.hashCode() + ((hashCode + (this.view.hashCode() * 31)) * 31);
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("ViewToTranslate(view=");
            m.append(this.view);
            m.append(", direction=");
            m.append(this.direction);
            m.append(", shouldBeAnimated=");
            m.append(this.shouldBeAnimated);
            m.append(')');
            return m.toString();
        }

        public ViewToTranslate(WeakReference<View> weakReference, Direction direction, Function0<Boolean> function0) {
            this.view = weakReference;
            this.direction = direction;
            this.shouldBeAnimated = function0;
        }
    }

    /* compiled from: UnfoldConstantTranslateAnimator.kt */
    /* loaded from: classes.dex */
    public enum Direction {
        LEFT(-1.0f),
        RIGHT(1.0f);
        
        private final float multiplier;

        Direction(float f) {
            this.multiplier = f;
        }

        public final float getMultiplier() {
            return this.multiplier;
        }
    }

    @Override // com.android.systemui.unfold.UnfoldTransitionProgressProvider.TransitionProgressListener
    public final void onTransitionFinished() {
        translateViews(1.0f);
    }

    @Override // com.android.systemui.unfold.UnfoldTransitionProgressProvider.TransitionProgressListener
    public final void onTransitionStarted() {
        ViewToTranslate viewToTranslate;
        ViewGroup viewGroup = this.rootView;
        if (viewGroup == null) {
            viewGroup = null;
        }
        Set<ViewIdToTranslate> set = this.viewsIdToTranslate;
        ArrayList arrayList = new ArrayList();
        for (ViewIdToTranslate viewIdToTranslate : set) {
            Objects.requireNonNull(viewIdToTranslate);
            int i = viewIdToTranslate.viewId;
            Direction direction = viewIdToTranslate.direction;
            Function0<Boolean> function0 = viewIdToTranslate.shouldBeAnimated;
            View findViewById = viewGroup.findViewById(i);
            if (findViewById == null) {
                viewToTranslate = null;
            } else {
                viewToTranslate = new ViewToTranslate(new WeakReference(findViewById), direction, function0);
            }
            if (viewToTranslate != null) {
                arrayList.add(viewToTranslate);
            }
        }
        this.viewsToTranslate = arrayList;
    }

    public final void translateViews(float f) {
        View view;
        float f2 = (f - 1.0f) * this.translationMax;
        for (ViewToTranslate viewToTranslate : this.viewsToTranslate) {
            Objects.requireNonNull(viewToTranslate);
            WeakReference<View> weakReference = viewToTranslate.view;
            Direction direction = viewToTranslate.direction;
            if (viewToTranslate.shouldBeAnimated.invoke().booleanValue() && (view = weakReference.get()) != null) {
                view.setTranslationX(direction.getMultiplier() * f2);
            }
        }
    }

    public UnfoldConstantTranslateAnimator(Set set, NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider) {
        this.viewsIdToTranslate = set;
        this.progressProvider = naturalRotationUnfoldProgressProvider;
    }

    @Override // com.android.systemui.unfold.UnfoldTransitionProgressProvider.TransitionProgressListener
    public final void onTransitionProgress(float f) {
        translateViews(f);
    }
}
