package androidx.lifecycle;

import android.annotation.SuppressLint;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Looper;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.arch.core.internal.FastSafeIterableMap;
import androidx.arch.core.internal.SafeIterableMap;
import androidx.concurrent.futures.AbstractResolvableFuture$$ExternalSyntheticOutline0;
import androidx.lifecycle.Lifecycle;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class LifecycleRegistry extends Lifecycle {
    public final boolean mEnforceMainThread;
    public final WeakReference<LifecycleOwner> mLifecycleOwner;
    public FastSafeIterableMap<LifecycleObserver, ObserverWithState> mObserverMap = new FastSafeIterableMap<>();
    public int mAddingObserverCounter = 0;
    public boolean mHandlingEvent = false;
    public boolean mNewEventOccurred = false;
    public ArrayList<Lifecycle.State> mParentStates = new ArrayList<>();
    public Lifecycle.State mState = Lifecycle.State.INITIALIZED;

    /* loaded from: classes.dex */
    public static class ObserverWithState {
        public LifecycleEventObserver mLifecycleObserver;
        public Lifecycle.State mState;

        public ObserverWithState(LifecycleObserver lifecycleObserver, Lifecycle.State state) {
            LifecycleEventObserver lifecycleEventObserver;
            HashMap hashMap = Lifecycling.sCallbackCache;
            boolean z = lifecycleObserver instanceof LifecycleEventObserver;
            boolean z2 = lifecycleObserver instanceof FullLifecycleObserver;
            if (z && z2) {
                lifecycleEventObserver = new FullLifecycleObserverAdapter((FullLifecycleObserver) lifecycleObserver, (LifecycleEventObserver) lifecycleObserver);
            } else if (z2) {
                lifecycleEventObserver = new FullLifecycleObserverAdapter((FullLifecycleObserver) lifecycleObserver, null);
            } else if (z) {
                lifecycleEventObserver = (LifecycleEventObserver) lifecycleObserver;
            } else {
                Class<?> cls = lifecycleObserver.getClass();
                if (Lifecycling.getObserverConstructorType(cls) == 2) {
                    List list = (List) Lifecycling.sClassToAdapters.get(cls);
                    if (list.size() == 1) {
                        lifecycleEventObserver = new SingleGeneratedAdapterObserver(Lifecycling.createGeneratedAdapter((Constructor) list.get(0), lifecycleObserver));
                    } else {
                        GeneratedAdapter[] generatedAdapterArr = new GeneratedAdapter[list.size()];
                        for (int i = 0; i < list.size(); i++) {
                            generatedAdapterArr[i] = Lifecycling.createGeneratedAdapter((Constructor) list.get(i), lifecycleObserver);
                        }
                        lifecycleEventObserver = new CompositeGeneratedAdaptersObserver(generatedAdapterArr);
                    }
                } else {
                    lifecycleEventObserver = new ReflectiveGenericLifecycleObserver(lifecycleObserver);
                }
            }
            this.mLifecycleObserver = lifecycleEventObserver;
            this.mState = state;
        }

        public final void dispatchEvent(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
            Lifecycle.State targetState = event.getTargetState();
            Lifecycle.State state = this.mState;
            if (targetState.compareTo(state) < 0) {
                state = targetState;
            }
            this.mState = state;
            this.mLifecycleObserver.onStateChanged(lifecycleOwner, event);
            this.mState = targetState;
        }
    }

    public static LifecycleRegistry createUnsafe(LifecycleOwner lifecycleOwner) {
        return new LifecycleRegistry(lifecycleOwner, false);
    }

    @Override // androidx.lifecycle.Lifecycle
    public void addObserver(LifecycleObserver lifecycleObserver) {
        LifecycleOwner lifecycleOwner;
        boolean z;
        Lifecycle.Event event;
        ArrayList<Lifecycle.State> arrayList;
        enforceMainThreadIfNeeded("addObserver");
        Lifecycle.State state = this.mState;
        Lifecycle.State state2 = Lifecycle.State.DESTROYED;
        if (state != state2) {
            state2 = Lifecycle.State.INITIALIZED;
        }
        ObserverWithState observerWithState = new ObserverWithState(lifecycleObserver, state2);
        if (this.mObserverMap.putIfAbsent(lifecycleObserver, observerWithState) == null && (lifecycleOwner = this.mLifecycleOwner.get()) != null) {
            if (this.mAddingObserverCounter != 0 || this.mHandlingEvent) {
                z = true;
            } else {
                z = false;
            }
            Lifecycle.State calculateTargetState = calculateTargetState(lifecycleObserver);
            this.mAddingObserverCounter++;
            while (observerWithState.mState.compareTo(calculateTargetState) < 0) {
                FastSafeIterableMap<LifecycleObserver, ObserverWithState> fastSafeIterableMap = this.mObserverMap;
                Objects.requireNonNull(fastSafeIterableMap);
                if (!fastSafeIterableMap.mHashMap.containsKey(lifecycleObserver)) {
                    break;
                }
                this.mParentStates.add(observerWithState.mState);
                int ordinal = observerWithState.mState.ordinal();
                if (ordinal == 1) {
                    event = Lifecycle.Event.ON_CREATE;
                } else if (ordinal == 2) {
                    event = Lifecycle.Event.ON_START;
                } else if (ordinal != 3) {
                    event = null;
                } else {
                    event = Lifecycle.Event.ON_RESUME;
                }
                if (event != null) {
                    observerWithState.dispatchEvent(lifecycleOwner, event);
                    this.mParentStates.remove(arrayList.size() - 1);
                    calculateTargetState = calculateTargetState(lifecycleObserver);
                } else {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("no event up from ");
                    m.append(observerWithState.mState);
                    throw new IllegalStateException(m.toString());
                }
            }
            if (!z) {
                sync();
            }
            this.mAddingObserverCounter--;
        }
    }

    public final Lifecycle.State calculateTargetState(LifecycleObserver lifecycleObserver) {
        SafeIterableMap.Entry<LifecycleObserver, ObserverWithState> entry;
        Lifecycle.State state;
        ArrayList<Lifecycle.State> arrayList;
        FastSafeIterableMap<LifecycleObserver, ObserverWithState> fastSafeIterableMap = this.mObserverMap;
        Objects.requireNonNull(fastSafeIterableMap);
        Lifecycle.State state2 = null;
        if (fastSafeIterableMap.mHashMap.containsKey(lifecycleObserver)) {
            entry = fastSafeIterableMap.mHashMap.get(lifecycleObserver).mPrevious;
        } else {
            entry = null;
        }
        if (entry != null) {
            state = entry.mValue.mState;
        } else {
            state = null;
        }
        if (!this.mParentStates.isEmpty()) {
            state2 = this.mParentStates.get(arrayList.size() - 1);
        }
        Lifecycle.State state3 = this.mState;
        if (state == null || state.compareTo(state3) >= 0) {
            state = state3;
        }
        if (state2 == null || state2.compareTo(state) >= 0) {
            return state;
        }
        return state2;
    }

    @SuppressLint({"RestrictedApi"})
    public final void enforceMainThreadIfNeeded(String str) {
        boolean z;
        if (this.mEnforceMainThread) {
            ArchTaskExecutor instance = ArchTaskExecutor.getInstance();
            Objects.requireNonNull(instance);
            Objects.requireNonNull(instance.mDelegate);
            if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                throw new IllegalStateException(AbstractResolvableFuture$$ExternalSyntheticOutline0.m("Method ", str, " must be called on the main thread"));
            }
        }
    }

    public final void handleLifecycleEvent(Lifecycle.Event event) {
        enforceMainThreadIfNeeded("handleLifecycleEvent");
        moveToState(event.getTargetState());
    }

    public final void moveToState(Lifecycle.State state) {
        if (this.mState != state) {
            this.mState = state;
            if (this.mHandlingEvent || this.mAddingObserverCounter != 0) {
                this.mNewEventOccurred = true;
                return;
            }
            this.mHandlingEvent = true;
            sync();
            this.mHandlingEvent = false;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:90:0x0073, code lost:
        continue;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x011c, code lost:
        continue;
     */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0041  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0196 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void sync() {
        /*
            Method dump skipped, instructions count: 417
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.lifecycle.LifecycleRegistry.sync():void");
    }

    public LifecycleRegistry(LifecycleOwner lifecycleOwner, boolean z) {
        this.mLifecycleOwner = new WeakReference<>(lifecycleOwner);
        this.mEnforceMainThread = z;
    }

    @Override // androidx.lifecycle.Lifecycle
    public void removeObserver(LifecycleObserver lifecycleObserver) {
        enforceMainThreadIfNeeded("removeObserver");
        this.mObserverMap.remove(lifecycleObserver);
    }

    public final void setCurrentState(Lifecycle.State state) {
        enforceMainThreadIfNeeded("setCurrentState");
        moveToState(state);
    }

    @Override // androidx.lifecycle.Lifecycle
    public final Lifecycle.State getCurrentState() {
        return this.mState;
    }
}
