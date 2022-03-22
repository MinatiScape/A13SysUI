package androidx.fragment.app;

import android.transition.Transition;
import android.view.View;
import android.view.ViewGroup;
import androidx.collection.ArrayMap;
import androidx.core.os.CancellationSignal;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.fragment.app.FragmentAnim;
import androidx.fragment.app.SpecialEffectsController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public final class DefaultSpecialEffectsController extends SpecialEffectsController {

    /* renamed from: androidx.fragment.app.DefaultSpecialEffectsController$9  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass9 implements Runnable {
        public final /* synthetic */ TransitionInfo val$transitionInfo;

        public AnonymousClass9(TransitionInfo transitionInfo) {
            this.val$transitionInfo = transitionInfo;
        }

        @Override // java.lang.Runnable
        public final void run() {
            this.val$transitionInfo.completeSpecialEffect();
        }
    }

    /* loaded from: classes.dex */
    public static class AnimationInfo extends SpecialEffectsInfo {
        public FragmentAnim.AnimationOrAnimator mAnimation;
        public boolean mIsPop;
        public boolean mLoadedAnim = false;

        /* JADX WARN: Removed duplicated region for block: B:28:0x0051  */
        /* JADX WARN: Removed duplicated region for block: B:33:0x0063  */
        /* JADX WARN: Removed duplicated region for block: B:36:0x006d A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:39:0x0073  */
        /* JADX WARN: Removed duplicated region for block: B:60:0x00bd  */
        /* JADX WARN: Removed duplicated region for block: B:65:0x00c9  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final androidx.fragment.app.FragmentAnim.AnimationOrAnimator getAnimation(android.content.Context r10) {
            /*
                Method dump skipped, instructions count: 270
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.DefaultSpecialEffectsController.AnimationInfo.getAnimation(android.content.Context):androidx.fragment.app.FragmentAnim$AnimationOrAnimator");
        }

        public AnimationInfo(SpecialEffectsController.Operation operation, CancellationSignal cancellationSignal, boolean z) {
            super(operation, cancellationSignal);
            this.mIsPop = z;
        }
    }

    /* loaded from: classes.dex */
    public static class SpecialEffectsInfo {
        public final SpecialEffectsController.Operation mOperation;
        public final CancellationSignal mSignal;

        public final void completeSpecialEffect() {
            SpecialEffectsController.Operation operation = this.mOperation;
            CancellationSignal cancellationSignal = this.mSignal;
            Objects.requireNonNull(operation);
            if (operation.mSpecialEffectsSignals.remove(cancellationSignal) && operation.mSpecialEffectsSignals.isEmpty()) {
                operation.complete();
            }
        }

        public final boolean isVisibilityUnchanged() {
            SpecialEffectsController.Operation.State state;
            SpecialEffectsController.Operation operation = this.mOperation;
            Objects.requireNonNull(operation);
            SpecialEffectsController.Operation.State from = SpecialEffectsController.Operation.State.from(operation.mFragment.mView);
            SpecialEffectsController.Operation operation2 = this.mOperation;
            Objects.requireNonNull(operation2);
            SpecialEffectsController.Operation.State state2 = operation2.mFinalState;
            if (from == state2 || (from != (state = SpecialEffectsController.Operation.State.VISIBLE) && state2 != state)) {
                return true;
            }
            return false;
        }

        public SpecialEffectsInfo(SpecialEffectsController.Operation operation, CancellationSignal cancellationSignal) {
            this.mOperation = operation;
            this.mSignal = cancellationSignal;
        }
    }

    /* loaded from: classes.dex */
    public static class TransitionInfo extends SpecialEffectsInfo {
        public final boolean mOverlapAllowed;
        public final Object mSharedElementTransition;
        public final Object mTransition;

        public final FragmentTransitionImpl getHandlingImpl(Object obj) {
            if (obj == null) {
                return null;
            }
            FragmentTransitionCompat21 fragmentTransitionCompat21 = FragmentTransition.PLATFORM_IMPL;
            if (obj instanceof Transition) {
                return fragmentTransitionCompat21;
            }
            FragmentTransitionImpl fragmentTransitionImpl = FragmentTransition.SUPPORT_IMPL;
            if (fragmentTransitionImpl != null && fragmentTransitionImpl.canHandle(obj)) {
                return fragmentTransitionImpl;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Transition ");
            sb.append(obj);
            sb.append(" for fragment ");
            SpecialEffectsController.Operation operation = this.mOperation;
            Objects.requireNonNull(operation);
            sb.append(operation.mFragment);
            sb.append(" is not a valid framework Transition or AndroidX Transition");
            throw new IllegalArgumentException(sb.toString());
        }

        /* JADX WARN: Code restructure failed: missing block: B:20:0x0046, code lost:
            if (r5 == androidx.fragment.app.Fragment.USE_DEFAULT_TRANSITION) goto L_0x004e;
         */
        /* JADX WARN: Code restructure failed: missing block: B:8:0x001b, code lost:
            if (r5 == androidx.fragment.app.Fragment.USE_DEFAULT_TRANSITION) goto L_0x0023;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public TransitionInfo(androidx.fragment.app.SpecialEffectsController.Operation r4, androidx.core.os.CancellationSignal r5, boolean r6, boolean r7) {
            /*
                r3 = this;
                r3.<init>(r4, r5)
                androidx.fragment.app.SpecialEffectsController$Operation$State r5 = r4.mFinalState
                androidx.fragment.app.SpecialEffectsController$Operation$State r0 = androidx.fragment.app.SpecialEffectsController.Operation.State.VISIBLE
                r1 = 1
                r2 = 0
                if (r5 != r0) goto L_0x0036
                if (r6 == 0) goto L_0x001e
                androidx.fragment.app.Fragment r5 = r4.mFragment
                java.util.Objects.requireNonNull(r5)
                androidx.fragment.app.Fragment$AnimationInfo r5 = r5.mAnimationInfo
                if (r5 != 0) goto L_0x0017
                goto L_0x0023
            L_0x0017:
                java.lang.Object r5 = r5.mReenterTransition
                java.lang.Object r0 = androidx.fragment.app.Fragment.USE_DEFAULT_TRANSITION
                if (r5 != r0) goto L_0x0024
                goto L_0x0023
            L_0x001e:
                androidx.fragment.app.Fragment r5 = r4.mFragment
                java.util.Objects.requireNonNull(r5)
            L_0x0023:
                r5 = r2
            L_0x0024:
                r3.mTransition = r5
                if (r6 == 0) goto L_0x002e
                androidx.fragment.app.Fragment r5 = r4.mFragment
                java.util.Objects.requireNonNull(r5)
                goto L_0x0033
            L_0x002e:
                androidx.fragment.app.Fragment r5 = r4.mFragment
                java.util.Objects.requireNonNull(r5)
            L_0x0033:
                r3.mOverlapAllowed = r1
                goto L_0x0053
            L_0x0036:
                if (r6 == 0) goto L_0x0049
                androidx.fragment.app.Fragment r5 = r4.mFragment
                java.util.Objects.requireNonNull(r5)
                androidx.fragment.app.Fragment$AnimationInfo r5 = r5.mAnimationInfo
                if (r5 != 0) goto L_0x0042
                goto L_0x004e
            L_0x0042:
                java.lang.Object r5 = r5.mReturnTransition
                java.lang.Object r0 = androidx.fragment.app.Fragment.USE_DEFAULT_TRANSITION
                if (r5 != r0) goto L_0x004f
                goto L_0x004e
            L_0x0049:
                androidx.fragment.app.Fragment r5 = r4.mFragment
                java.util.Objects.requireNonNull(r5)
            L_0x004e:
                r5 = r2
            L_0x004f:
                r3.mTransition = r5
                r3.mOverlapAllowed = r1
            L_0x0053:
                if (r7 == 0) goto L_0x0074
                if (r6 == 0) goto L_0x006c
                androidx.fragment.app.Fragment r4 = r4.mFragment
                java.util.Objects.requireNonNull(r4)
                androidx.fragment.app.Fragment$AnimationInfo r4 = r4.mAnimationInfo
                if (r4 != 0) goto L_0x0061
                goto L_0x0069
            L_0x0061:
                java.lang.Object r4 = r4.mSharedElementReturnTransition
                java.lang.Object r5 = androidx.fragment.app.Fragment.USE_DEFAULT_TRANSITION
                if (r4 != r5) goto L_0x0068
                goto L_0x0069
            L_0x0068:
                r2 = r4
            L_0x0069:
                r3.mSharedElementTransition = r2
                goto L_0x0076
            L_0x006c:
                androidx.fragment.app.Fragment r4 = r4.mFragment
                java.util.Objects.requireNonNull(r4)
                r3.mSharedElementTransition = r2
                goto L_0x0076
            L_0x0074:
                r3.mSharedElementTransition = r2
            L_0x0076:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.DefaultSpecialEffectsController.TransitionInfo.<init>(androidx.fragment.app.SpecialEffectsController$Operation, androidx.core.os.CancellationSignal, boolean, boolean):void");
        }
    }

    public static void captureTransitioningViews(ArrayList arrayList, View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            if (!viewGroup.isTransitionGroup()) {
                int childCount = viewGroup.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View childAt = viewGroup.getChildAt(i);
                    if (childAt.getVisibility() == 0) {
                        captureTransitioningViews(arrayList, childAt);
                    }
                }
            } else if (!arrayList.contains(view)) {
                arrayList.add(viewGroup);
            }
        } else if (!arrayList.contains(view)) {
            arrayList.add(view);
        }
    }

    public static void findNamedViews(ArrayMap arrayMap, View view) {
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        String transitionName = ViewCompat.Api21Impl.getTransitionName(view);
        if (transitionName != null) {
            arrayMap.put(transitionName, view);
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = viewGroup.getChildAt(i);
                if (childAt.getVisibility() == 0) {
                    findNamedViews(arrayMap, childAt);
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:208:0x0623  */
    /* JADX WARN: Removed duplicated region for block: B:237:0x06db  */
    /* JADX WARN: Removed duplicated region for block: B:256:0x0779 A[LOOP:15: B:254:0x0773->B:256:0x0779, LOOP_END] */
    @Override // androidx.fragment.app.SpecialEffectsController
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void executeOperations(java.util.ArrayList r35, final boolean r36) {
        /*
            Method dump skipped, instructions count: 1936
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.DefaultSpecialEffectsController.executeOperations(java.util.ArrayList, boolean):void");
    }

    public static void retainMatchingViews(ArrayMap arrayMap, Collection collection) {
        Iterator it = ((ArrayMap.EntrySet) arrayMap.entrySet()).iterator();
        while (true) {
            ArrayMap.MapIterator mapIterator = (ArrayMap.MapIterator) it;
            if (mapIterator.hasNext()) {
                mapIterator.next();
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                if (!collection.contains(ViewCompat.Api21Impl.getTransitionName((View) mapIterator.getValue()))) {
                    mapIterator.remove();
                }
            } else {
                return;
            }
        }
    }

    public DefaultSpecialEffectsController(ViewGroup viewGroup) {
        super(viewGroup);
    }
}
