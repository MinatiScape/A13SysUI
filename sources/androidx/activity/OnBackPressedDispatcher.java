package androidx.activity;

import android.annotation.SuppressLint;
import androidx.activity.ComponentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class OnBackPressedDispatcher {
    public final Runnable mFallbackOnBackPressed;
    public final ArrayDeque<OnBackPressedCallback> mOnBackPressedCallbacks = new ArrayDeque<>();

    /* loaded from: classes.dex */
    public class LifecycleOnBackPressedCancellable implements LifecycleEventObserver, Cancellable {
        public OnBackPressedCancellable mCurrentCancellable;
        public final Lifecycle mLifecycle;
        public final OnBackPressedCallback mOnBackPressedCallback;

        public LifecycleOnBackPressedCancellable(Lifecycle lifecycle, FragmentManager.AnonymousClass1 r3) {
            this.mLifecycle = lifecycle;
            this.mOnBackPressedCallback = r3;
            lifecycle.addObserver(this);
        }

        @Override // androidx.activity.Cancellable
        public final void cancel() {
            this.mLifecycle.removeObserver(this);
            OnBackPressedCallback onBackPressedCallback = this.mOnBackPressedCallback;
            Objects.requireNonNull(onBackPressedCallback);
            onBackPressedCallback.mCancellables.remove(this);
            OnBackPressedCancellable onBackPressedCancellable = this.mCurrentCancellable;
            if (onBackPressedCancellable != null) {
                onBackPressedCancellable.cancel();
                this.mCurrentCancellable = null;
            }
        }

        @Override // androidx.lifecycle.LifecycleEventObserver
        public final void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
            if (event == Lifecycle.Event.ON_START) {
                OnBackPressedDispatcher onBackPressedDispatcher = OnBackPressedDispatcher.this;
                OnBackPressedCallback onBackPressedCallback = this.mOnBackPressedCallback;
                Objects.requireNonNull(onBackPressedDispatcher);
                onBackPressedDispatcher.mOnBackPressedCallbacks.add(onBackPressedCallback);
                OnBackPressedCancellable onBackPressedCancellable = new OnBackPressedCancellable(onBackPressedCallback);
                Objects.requireNonNull(onBackPressedCallback);
                onBackPressedCallback.mCancellables.add(onBackPressedCancellable);
                this.mCurrentCancellable = onBackPressedCancellable;
            } else if (event == Lifecycle.Event.ON_STOP) {
                OnBackPressedCancellable onBackPressedCancellable2 = this.mCurrentCancellable;
                if (onBackPressedCancellable2 != null) {
                    onBackPressedCancellable2.cancel();
                }
            } else if (event == Lifecycle.Event.ON_DESTROY) {
                cancel();
            }
        }
    }

    /* loaded from: classes.dex */
    public class OnBackPressedCancellable implements Cancellable {
        public final OnBackPressedCallback mOnBackPressedCallback;

        public OnBackPressedCancellable(OnBackPressedCallback onBackPressedCallback) {
            this.mOnBackPressedCallback = onBackPressedCallback;
        }

        @Override // androidx.activity.Cancellable
        public final void cancel() {
            OnBackPressedDispatcher.this.mOnBackPressedCallbacks.remove(this.mOnBackPressedCallback);
            OnBackPressedCallback onBackPressedCallback = this.mOnBackPressedCallback;
            Objects.requireNonNull(onBackPressedCallback);
            onBackPressedCallback.mCancellables.remove(this);
        }
    }

    public final void onBackPressed() {
        Iterator<OnBackPressedCallback> descendingIterator = this.mOnBackPressedCallbacks.descendingIterator();
        while (descendingIterator.hasNext()) {
            OnBackPressedCallback next = descendingIterator.next();
            Objects.requireNonNull(next);
            if (next.mEnabled) {
                next.handleOnBackPressed();
                return;
            }
        }
        Runnable runnable = this.mFallbackOnBackPressed;
        if (runnable != null) {
            runnable.run();
        }
    }

    public OnBackPressedDispatcher(ComponentActivity.AnonymousClass1 r2) {
        this.mFallbackOnBackPressed = r2;
    }

    @SuppressLint({"LambdaLast"})
    public final void addCallback(LifecycleOwner lifecycleOwner, FragmentManager.AnonymousClass1 r4) {
        Lifecycle lifecycle = lifecycleOwner.getLifecycle();
        if (lifecycle.getCurrentState() != Lifecycle.State.DESTROYED) {
            LifecycleOnBackPressedCancellable lifecycleOnBackPressedCancellable = new LifecycleOnBackPressedCancellable(lifecycle, r4);
            Objects.requireNonNull(r4);
            r4.mCancellables.add(lifecycleOnBackPressedCancellable);
        }
    }
}
