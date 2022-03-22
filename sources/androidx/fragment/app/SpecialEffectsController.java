package androidx.fragment.app;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.os.CancellationSignal;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public abstract class SpecialEffectsController {
    public final ViewGroup mContainer;
    public final ArrayList<Operation> mPendingOperations = new ArrayList<>();
    public final ArrayList<Operation> mRunningOperations = new ArrayList<>();
    public boolean mOperationDirectionIsPop = false;
    public boolean mIsContainerPostponed = false;

    /* loaded from: classes.dex */
    public static class FragmentStateManagerOperation extends Operation {
        public final FragmentStateManager mFragmentStateManager;

        @Override // androidx.fragment.app.SpecialEffectsController.Operation
        public final void onStart() {
            float f;
            if (this.mLifecycleImpact == Operation.LifecycleImpact.ADDING) {
                FragmentStateManager fragmentStateManager = this.mFragmentStateManager;
                Objects.requireNonNull(fragmentStateManager);
                Fragment fragment = fragmentStateManager.mFragment;
                View findFocus = fragment.mView.findFocus();
                if (findFocus != null) {
                    fragment.ensureAnimationInfo().mFocusedView = findFocus;
                    if (FragmentManager.isLoggingEnabled(2)) {
                        Log.v("FragmentManager", "requestFocus: Saved focused view " + findFocus + " for Fragment " + fragment);
                    }
                }
                View requireView = this.mFragment.requireView();
                if (requireView.getParent() == null) {
                    this.mFragmentStateManager.addViewToContainer();
                    requireView.setAlpha(0.0f);
                }
                if (requireView.getAlpha() == 0.0f && requireView.getVisibility() == 0) {
                    requireView.setVisibility(4);
                }
                Fragment.AnimationInfo animationInfo = fragment.mAnimationInfo;
                if (animationInfo == null) {
                    f = 1.0f;
                } else {
                    f = animationInfo.mPostOnViewCreatedAlpha;
                }
                requireView.setAlpha(f);
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public FragmentStateManagerOperation(Operation.State state, Operation.LifecycleImpact lifecycleImpact, FragmentStateManager fragmentStateManager, CancellationSignal cancellationSignal) {
            super(state, lifecycleImpact, fragmentStateManager.mFragment, cancellationSignal);
            Objects.requireNonNull(fragmentStateManager);
            this.mFragmentStateManager = fragmentStateManager;
        }

        @Override // androidx.fragment.app.SpecialEffectsController.Operation
        public final void complete() {
            super.complete();
            this.mFragmentStateManager.moveToExpectedState();
        }
    }

    /* loaded from: classes.dex */
    public static class Operation {
        public State mFinalState;
        public final Fragment mFragment;
        public LifecycleImpact mLifecycleImpact;
        public final ArrayList mCompletionListeners = new ArrayList();
        public final HashSet<CancellationSignal> mSpecialEffectsSignals = new HashSet<>();
        public boolean mIsCanceled = false;
        public boolean mIsComplete = false;

        /* loaded from: classes.dex */
        public enum LifecycleImpact {
            NONE,
            ADDING,
            REMOVING
        }

        public void onStart() {
        }

        /* loaded from: classes.dex */
        public enum State {
            REMOVED,
            VISIBLE,
            GONE,
            INVISIBLE;

            public static State from(View view) {
                if (view.getAlpha() == 0.0f && view.getVisibility() == 0) {
                    return INVISIBLE;
                }
                return from(view.getVisibility());
            }

            public final void applyState(View view) {
                int ordinal = ordinal();
                if (ordinal == 0) {
                    ViewGroup viewGroup = (ViewGroup) view.getParent();
                    if (viewGroup != null) {
                        if (FragmentManager.isLoggingEnabled(2)) {
                            Log.v("FragmentManager", "SpecialEffectsController: Removing view " + view + " from container " + viewGroup);
                        }
                        viewGroup.removeView(view);
                    }
                } else if (ordinal == 1) {
                    if (FragmentManager.isLoggingEnabled(2)) {
                        Log.v("FragmentManager", "SpecialEffectsController: Setting view " + view + " to VISIBLE");
                    }
                    view.setVisibility(0);
                } else if (ordinal == 2) {
                    if (FragmentManager.isLoggingEnabled(2)) {
                        Log.v("FragmentManager", "SpecialEffectsController: Setting view " + view + " to GONE");
                    }
                    view.setVisibility(8);
                } else if (ordinal == 3) {
                    if (FragmentManager.isLoggingEnabled(2)) {
                        Log.v("FragmentManager", "SpecialEffectsController: Setting view " + view + " to INVISIBLE");
                    }
                    view.setVisibility(4);
                }
            }

            public static State from(int i) {
                if (i == 0) {
                    return VISIBLE;
                }
                if (i == 4) {
                    return INVISIBLE;
                }
                if (i == 8) {
                    return GONE;
                }
                throw new IllegalArgumentException(VendorAtomValue$$ExternalSyntheticOutline0.m("Unknown visibility ", i));
            }
        }

        public final void cancel() {
            if (!this.mIsCanceled) {
                this.mIsCanceled = true;
                if (this.mSpecialEffectsSignals.isEmpty()) {
                    complete();
                    return;
                }
                Iterator it = new ArrayList(this.mSpecialEffectsSignals).iterator();
                while (it.hasNext()) {
                    ((CancellationSignal) it.next()).cancel();
                }
            }
        }

        public void complete() {
            if (!this.mIsComplete) {
                if (FragmentManager.isLoggingEnabled(2)) {
                    Log.v("FragmentManager", "SpecialEffectsController: " + this + " has called complete.");
                }
                this.mIsComplete = true;
                Iterator it = this.mCompletionListeners.iterator();
                while (it.hasNext()) {
                    ((Runnable) it.next()).run();
                }
            }
        }

        public final void mergeWith(State state, LifecycleImpact lifecycleImpact) {
            State state2 = State.REMOVED;
            int ordinal = lifecycleImpact.ordinal();
            if (ordinal != 0) {
                if (ordinal != 1) {
                    if (ordinal == 2) {
                        if (FragmentManager.isLoggingEnabled(2)) {
                            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("SpecialEffectsController: For fragment ");
                            m.append(this.mFragment);
                            m.append(" mFinalState = ");
                            m.append(this.mFinalState);
                            m.append(" -> REMOVED. mLifecycleImpact  = ");
                            m.append(this.mLifecycleImpact);
                            m.append(" to REMOVING.");
                            Log.v("FragmentManager", m.toString());
                        }
                        this.mFinalState = state2;
                        this.mLifecycleImpact = LifecycleImpact.REMOVING;
                    }
                } else if (this.mFinalState == state2) {
                    if (FragmentManager.isLoggingEnabled(2)) {
                        StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("SpecialEffectsController: For fragment ");
                        m2.append(this.mFragment);
                        m2.append(" mFinalState = REMOVED -> VISIBLE. mLifecycleImpact = ");
                        m2.append(this.mLifecycleImpact);
                        m2.append(" to ADDING.");
                        Log.v("FragmentManager", m2.toString());
                    }
                    this.mFinalState = State.VISIBLE;
                    this.mLifecycleImpact = LifecycleImpact.ADDING;
                }
            } else if (this.mFinalState != state2) {
                if (FragmentManager.isLoggingEnabled(2)) {
                    StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("SpecialEffectsController: For fragment ");
                    m3.append(this.mFragment);
                    m3.append(" mFinalState = ");
                    m3.append(this.mFinalState);
                    m3.append(" -> ");
                    m3.append(state);
                    m3.append(". ");
                    Log.v("FragmentManager", m3.toString());
                }
                this.mFinalState = state;
            }
        }

        public final String toString() {
            StringBuilder m = DebugInfo$$ExternalSyntheticOutline0.m("Operation ", "{");
            m.append(Integer.toHexString(System.identityHashCode(this)));
            m.append("} ");
            m.append("{");
            m.append("mFinalState = ");
            m.append(this.mFinalState);
            m.append("} ");
            m.append("{");
            m.append("mLifecycleImpact = ");
            m.append(this.mLifecycleImpact);
            m.append("} ");
            m.append("{");
            m.append("mFragment = ");
            m.append(this.mFragment);
            m.append("}");
            return m.toString();
        }

        public Operation(State state, LifecycleImpact lifecycleImpact, Fragment fragment, CancellationSignal cancellationSignal) {
            this.mFinalState = state;
            this.mLifecycleImpact = lifecycleImpact;
            this.mFragment = fragment;
            cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() { // from class: androidx.fragment.app.SpecialEffectsController.Operation.1
                @Override // androidx.core.os.CancellationSignal.OnCancelListener
                public final void onCancel() {
                    Operation.this.cancel();
                }
            });
        }
    }

    public final void enqueueHide(FragmentStateManager fragmentStateManager) {
        if (FragmentManager.isLoggingEnabled(2)) {
            StringBuilder sb = new StringBuilder();
            sb.append("SpecialEffectsController: Enqueuing hide operation for fragment ");
            Objects.requireNonNull(fragmentStateManager);
            sb.append(fragmentStateManager.mFragment);
            Log.v("FragmentManager", sb.toString());
        }
        enqueue(Operation.State.GONE, Operation.LifecycleImpact.NONE, fragmentStateManager);
    }

    public final void enqueueRemove(FragmentStateManager fragmentStateManager) {
        if (FragmentManager.isLoggingEnabled(2)) {
            StringBuilder sb = new StringBuilder();
            sb.append("SpecialEffectsController: Enqueuing remove operation for fragment ");
            Objects.requireNonNull(fragmentStateManager);
            sb.append(fragmentStateManager.mFragment);
            Log.v("FragmentManager", sb.toString());
        }
        enqueue(Operation.State.REMOVED, Operation.LifecycleImpact.REMOVING, fragmentStateManager);
    }

    public final void enqueueShow(FragmentStateManager fragmentStateManager) {
        if (FragmentManager.isLoggingEnabled(2)) {
            StringBuilder sb = new StringBuilder();
            sb.append("SpecialEffectsController: Enqueuing show operation for fragment ");
            Objects.requireNonNull(fragmentStateManager);
            sb.append(fragmentStateManager.mFragment);
            Log.v("FragmentManager", sb.toString());
        }
        enqueue(Operation.State.VISIBLE, Operation.LifecycleImpact.NONE, fragmentStateManager);
    }

    public abstract void executeOperations(ArrayList arrayList, boolean z);

    public final void enqueue(Operation.State state, Operation.LifecycleImpact lifecycleImpact, FragmentStateManager fragmentStateManager) {
        synchronized (this.mPendingOperations) {
            CancellationSignal cancellationSignal = new CancellationSignal();
            Objects.requireNonNull(fragmentStateManager);
            Operation findPendingOperation = findPendingOperation(fragmentStateManager.mFragment);
            if (findPendingOperation != null) {
                findPendingOperation.mergeWith(state, lifecycleImpact);
                return;
            }
            final FragmentStateManagerOperation fragmentStateManagerOperation = new FragmentStateManagerOperation(state, lifecycleImpact, fragmentStateManager, cancellationSignal);
            this.mPendingOperations.add(fragmentStateManagerOperation);
            fragmentStateManagerOperation.mCompletionListeners.add(new Runnable() { // from class: androidx.fragment.app.SpecialEffectsController.1
                @Override // java.lang.Runnable
                public final void run() {
                    if (SpecialEffectsController.this.mPendingOperations.contains(fragmentStateManagerOperation)) {
                        FragmentStateManagerOperation fragmentStateManagerOperation2 = fragmentStateManagerOperation;
                        Objects.requireNonNull(fragmentStateManagerOperation2);
                        Operation.State state2 = fragmentStateManagerOperation2.mFinalState;
                        FragmentStateManagerOperation fragmentStateManagerOperation3 = fragmentStateManagerOperation;
                        Objects.requireNonNull(fragmentStateManagerOperation3);
                        state2.applyState(fragmentStateManagerOperation3.mFragment.mView);
                    }
                }
            });
            fragmentStateManagerOperation.mCompletionListeners.add(new Runnable() { // from class: androidx.fragment.app.SpecialEffectsController.2
                @Override // java.lang.Runnable
                public final void run() {
                    SpecialEffectsController.this.mPendingOperations.remove(fragmentStateManagerOperation);
                    SpecialEffectsController.this.mRunningOperations.remove(fragmentStateManagerOperation);
                }
            });
        }
    }

    public final void executePendingOperations() {
        if (!this.mIsContainerPostponed) {
            ViewGroup viewGroup = this.mContainer;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            if (!ViewCompat.Api19Impl.isAttachedToWindow(viewGroup)) {
                forceCompleteAllOperations();
                this.mOperationDirectionIsPop = false;
                return;
            }
            synchronized (this.mPendingOperations) {
                if (!this.mPendingOperations.isEmpty()) {
                    ArrayList arrayList = new ArrayList(this.mRunningOperations);
                    this.mRunningOperations.clear();
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        Operation operation = (Operation) it.next();
                        if (FragmentManager.isLoggingEnabled(2)) {
                            Log.v("FragmentManager", "SpecialEffectsController: Cancelling operation " + operation);
                        }
                        operation.cancel();
                        if (!operation.mIsComplete) {
                            this.mRunningOperations.add(operation);
                        }
                    }
                    updateFinalState();
                    ArrayList arrayList2 = new ArrayList(this.mPendingOperations);
                    this.mPendingOperations.clear();
                    this.mRunningOperations.addAll(arrayList2);
                    Iterator it2 = arrayList2.iterator();
                    while (it2.hasNext()) {
                        ((Operation) it2.next()).onStart();
                    }
                    executeOperations(arrayList2, this.mOperationDirectionIsPop);
                    this.mOperationDirectionIsPop = false;
                }
            }
        }
    }

    public final Operation findPendingOperation(Fragment fragment) {
        Iterator<Operation> it = this.mPendingOperations.iterator();
        while (it.hasNext()) {
            Operation next = it.next();
            Objects.requireNonNull(next);
            if (next.mFragment.equals(fragment) && !next.mIsCanceled) {
                return next;
            }
        }
        return null;
    }

    public final void forceCompleteAllOperations() {
        String str;
        String str2;
        ViewGroup viewGroup = this.mContainer;
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        boolean isAttachedToWindow = ViewCompat.Api19Impl.isAttachedToWindow(viewGroup);
        synchronized (this.mPendingOperations) {
            updateFinalState();
            Iterator<Operation> it = this.mPendingOperations.iterator();
            while (it.hasNext()) {
                it.next().onStart();
            }
            Iterator it2 = new ArrayList(this.mRunningOperations).iterator();
            while (it2.hasNext()) {
                Operation operation = (Operation) it2.next();
                if (FragmentManager.isLoggingEnabled(2)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("SpecialEffectsController: ");
                    if (isAttachedToWindow) {
                        str2 = "";
                    } else {
                        str2 = "Container " + this.mContainer + " is not attached to window. ";
                    }
                    sb.append(str2);
                    sb.append("Cancelling running operation ");
                    sb.append(operation);
                    Log.v("FragmentManager", sb.toString());
                }
                operation.cancel();
            }
            Iterator it3 = new ArrayList(this.mPendingOperations).iterator();
            while (it3.hasNext()) {
                Operation operation2 = (Operation) it3.next();
                if (FragmentManager.isLoggingEnabled(2)) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("SpecialEffectsController: ");
                    if (isAttachedToWindow) {
                        str = "";
                    } else {
                        str = "Container " + this.mContainer + " is not attached to window. ";
                    }
                    sb2.append(str);
                    sb2.append("Cancelling pending operation ");
                    sb2.append(operation2);
                    Log.v("FragmentManager", sb2.toString());
                }
                operation2.cancel();
            }
        }
    }

    public final void markPostponedState() {
        synchronized (this.mPendingOperations) {
            updateFinalState();
            this.mIsContainerPostponed = false;
            int size = this.mPendingOperations.size();
            while (true) {
                size--;
                if (size < 0) {
                    break;
                }
                Operation operation = this.mPendingOperations.get(size);
                Objects.requireNonNull(operation);
                Operation.State from = Operation.State.from(operation.mFragment.mView);
                Operation.State state = operation.mFinalState;
                Operation.State state2 = Operation.State.VISIBLE;
                if (state == state2 && from != state2) {
                    Fragment fragment = operation.mFragment;
                    Objects.requireNonNull(fragment);
                    Fragment.AnimationInfo animationInfo = fragment.mAnimationInfo;
                    this.mIsContainerPostponed = false;
                    break;
                }
            }
        }
    }

    public final void updateFinalState() {
        Iterator<Operation> it = this.mPendingOperations.iterator();
        while (it.hasNext()) {
            Operation next = it.next();
            Objects.requireNonNull(next);
            if (next.mLifecycleImpact == Operation.LifecycleImpact.ADDING) {
                next.mergeWith(Operation.State.from(next.mFragment.requireView().getVisibility()), Operation.LifecycleImpact.NONE);
            }
        }
    }

    public SpecialEffectsController(ViewGroup viewGroup) {
        this.mContainer = viewGroup;
    }

    public static SpecialEffectsController getOrCreateController(ViewGroup viewGroup, SpecialEffectsControllerFactory specialEffectsControllerFactory) {
        Object tag = viewGroup.getTag(2131428894);
        if (tag instanceof SpecialEffectsController) {
            return (SpecialEffectsController) tag;
        }
        Objects.requireNonNull((FragmentManager.AnonymousClass3) specialEffectsControllerFactory);
        DefaultSpecialEffectsController defaultSpecialEffectsController = new DefaultSpecialEffectsController(viewGroup);
        viewGroup.setTag(2131428894, defaultSpecialEffectsController);
        return defaultSpecialEffectsController;
    }
}
