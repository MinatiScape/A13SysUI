package androidx.transition;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import androidx.collection.ArrayMap;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public final class TransitionManager {
    public static AutoTransition sDefaultTransition = new AutoTransition();
    public static ThreadLocal<WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>>> sRunningTransitions = new ThreadLocal<>();
    public static ArrayList<ViewGroup> sPendingTransitions = new ArrayList<>();

    /* loaded from: classes.dex */
    public static class MultiListener implements ViewTreeObserver.OnPreDrawListener, View.OnAttachStateChangeListener {
        public ViewGroup mSceneRoot;
        public Transition mTransition;

        @Override // android.view.View.OnAttachStateChangeListener
        public final void onViewAttachedToWindow(View view) {
        }

        /* JADX WARN: Removed duplicated region for block: B:101:0x0215  */
        /* JADX WARN: Removed duplicated region for block: B:107:0x0241  */
        /* JADX WARN: Removed duplicated region for block: B:143:0x01ef A[EDGE_INSN: B:143:0x01ef->B:91:0x01ef ?: BREAK  , SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:14:0x005c  */
        /* JADX WARN: Removed duplicated region for block: B:21:0x009f  */
        /* JADX WARN: Removed duplicated region for block: B:94:0x01f4  */
        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final boolean onPreDraw() {
            /*
                Method dump skipped, instructions count: 712
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.transition.TransitionManager.MultiListener.onPreDraw():boolean");
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public final void onViewDetachedFromWindow(View view) {
            this.mSceneRoot.getViewTreeObserver().removeOnPreDrawListener(this);
            this.mSceneRoot.removeOnAttachStateChangeListener(this);
            TransitionManager.sPendingTransitions.remove(this.mSceneRoot);
            ArrayList<Transition> orDefault = TransitionManager.getRunningTransitions().getOrDefault(this.mSceneRoot, null);
            if (orDefault != null && orDefault.size() > 0) {
                Iterator<Transition> it = orDefault.iterator();
                while (it.hasNext()) {
                    it.next().resume(this.mSceneRoot);
                }
            }
            this.mTransition.clearValues(true);
        }

        public MultiListener(Transition transition, ViewGroup viewGroup) {
            this.mTransition = transition;
            this.mSceneRoot = viewGroup;
        }
    }

    public static void beginDelayedTransition(ViewGroup viewGroup, Transition transition) {
        if (!sPendingTransitions.contains(viewGroup)) {
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            if (ViewCompat.Api19Impl.isLaidOut(viewGroup)) {
                sPendingTransitions.add(viewGroup);
                if (transition == null) {
                    transition = sDefaultTransition;
                }
                Transition clone = transition.clone();
                ArrayList<Transition> orDefault = getRunningTransitions().getOrDefault(viewGroup, null);
                if (orDefault != null && orDefault.size() > 0) {
                    Iterator<Transition> it = orDefault.iterator();
                    while (it.hasNext()) {
                        it.next().pause(viewGroup);
                    }
                }
                if (clone != null) {
                    clone.captureValues(viewGroup, true);
                }
                if (((Scene) viewGroup.getTag(2131429095)) == null) {
                    viewGroup.setTag(2131429095, null);
                    if (clone != null) {
                        MultiListener multiListener = new MultiListener(clone, viewGroup);
                        viewGroup.addOnAttachStateChangeListener(multiListener);
                        viewGroup.getViewTreeObserver().addOnPreDrawListener(multiListener);
                        return;
                    }
                    return;
                }
                throw null;
            }
        }
    }

    public static ArrayMap<ViewGroup, ArrayList<Transition>> getRunningTransitions() {
        ArrayMap<ViewGroup, ArrayList<Transition>> arrayMap;
        WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>> weakReference = sRunningTransitions.get();
        if (weakReference != null && (arrayMap = weakReference.get()) != null) {
            return arrayMap;
        }
        ArrayMap<ViewGroup, ArrayList<Transition>> arrayMap2 = new ArrayMap<>();
        sRunningTransitions.set(new WeakReference<>(arrayMap2));
        return arrayMap2;
    }
}
