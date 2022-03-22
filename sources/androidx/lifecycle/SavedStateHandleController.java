package androidx.lifecycle;

import androidx.lifecycle.Lifecycle;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryOwner;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
final class SavedStateHandleController implements LifecycleEventObserver {
    public boolean mIsAttached;

    /* renamed from: androidx.lifecycle.SavedStateHandleController$1  reason: invalid class name */
    /* loaded from: classes.dex */
    class AnonymousClass1 implements LifecycleEventObserver {
        public final /* synthetic */ Lifecycle val$lifecycle;
        public final /* synthetic */ SavedStateRegistry val$registry;

        @Override // androidx.lifecycle.LifecycleEventObserver
        public final void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
            if (event == Lifecycle.Event.ON_START) {
                this.val$lifecycle.removeObserver(this);
                this.val$registry.runOnNextRecreation();
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class OnRecreation implements SavedStateRegistry.AutoRecreated {
        @Override // androidx.savedstate.SavedStateRegistry.AutoRecreated
        public final void onRecreated(SavedStateRegistryOwner savedStateRegistryOwner) {
            Object obj;
            boolean z;
            if (savedStateRegistryOwner instanceof ViewModelStoreOwner) {
                ViewModelStore viewModelStore = ((ViewModelStoreOwner) savedStateRegistryOwner).getViewModelStore();
                SavedStateRegistry savedStateRegistry = savedStateRegistryOwner.getSavedStateRegistry();
                Objects.requireNonNull(viewModelStore);
                Iterator it = new HashSet(viewModelStore.mMap.keySet()).iterator();
                while (it.hasNext()) {
                    ViewModel viewModel = viewModelStore.mMap.get((String) it.next());
                    Lifecycle lifecycle = savedStateRegistryOwner.getLifecycle();
                    Objects.requireNonNull(viewModel);
                    HashMap hashMap = viewModel.mBagOfTags;
                    if (hashMap == null) {
                        obj = null;
                    } else {
                        synchronized (hashMap) {
                            obj = viewModel.mBagOfTags.get("androidx.lifecycle.savedstate.vm.tag");
                        }
                    }
                    SavedStateHandleController savedStateHandleController = (SavedStateHandleController) obj;
                    if (savedStateHandleController != null && !(z = savedStateHandleController.mIsAttached)) {
                        if (z) {
                            throw new IllegalStateException("Already attached to lifecycleOwner");
                        }
                        savedStateHandleController.mIsAttached = true;
                        lifecycle.addObserver(savedStateHandleController);
                        throw null;
                    }
                }
                if (!new HashSet(viewModelStore.mMap.keySet()).isEmpty()) {
                    savedStateRegistry.runOnNextRecreation();
                    return;
                }
                return;
            }
            throw new IllegalStateException("Internal error: OnRecreation should be registered only on componentsthat implement ViewModelStoreOwner");
        }
    }

    @Override // androidx.lifecycle.LifecycleEventObserver
    public final void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            this.mIsAttached = false;
            lifecycleOwner.getLifecycle().removeObserver(this);
        }
    }
}
