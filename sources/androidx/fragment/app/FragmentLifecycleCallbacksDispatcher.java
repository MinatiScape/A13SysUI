package androidx.fragment.app;

import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes.dex */
public final class FragmentLifecycleCallbacksDispatcher {
    public final FragmentManager mFragmentManager;
    public final CopyOnWriteArrayList<FragmentLifecycleCallbacksHolder> mLifecycleCallbacks = new CopyOnWriteArrayList<>();

    /* loaded from: classes.dex */
    public static final class FragmentLifecycleCallbacksHolder {
    }

    public final void dispatchOnFragmentActivityCreated(boolean z) {
        FragmentManager fragmentManager = this.mFragmentManager;
        Objects.requireNonNull(fragmentManager);
        Fragment fragment = fragmentManager.mParent;
        if (fragment != null) {
            fragment.getParentFragmentManager().mLifecycleCallbacksDispatcher.dispatchOnFragmentActivityCreated(true);
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (z) {
                Objects.requireNonNull(next);
            } else {
                Objects.requireNonNull(next);
                throw null;
            }
        }
    }

    public final void dispatchOnFragmentAttached(boolean z) {
        FragmentManager fragmentManager = this.mFragmentManager;
        Objects.requireNonNull(fragmentManager);
        Objects.requireNonNull(fragmentManager.mHost);
        FragmentManager fragmentManager2 = this.mFragmentManager;
        Objects.requireNonNull(fragmentManager2);
        Fragment fragment = fragmentManager2.mParent;
        if (fragment != null) {
            fragment.getParentFragmentManager().mLifecycleCallbacksDispatcher.dispatchOnFragmentAttached(true);
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (z) {
                Objects.requireNonNull(next);
            } else {
                Objects.requireNonNull(next);
                throw null;
            }
        }
    }

    public final void dispatchOnFragmentCreated(boolean z) {
        FragmentManager fragmentManager = this.mFragmentManager;
        Objects.requireNonNull(fragmentManager);
        Fragment fragment = fragmentManager.mParent;
        if (fragment != null) {
            fragment.getParentFragmentManager().mLifecycleCallbacksDispatcher.dispatchOnFragmentCreated(true);
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (z) {
                Objects.requireNonNull(next);
            } else {
                Objects.requireNonNull(next);
                throw null;
            }
        }
    }

    public final void dispatchOnFragmentDestroyed(boolean z) {
        FragmentManager fragmentManager = this.mFragmentManager;
        Objects.requireNonNull(fragmentManager);
        Fragment fragment = fragmentManager.mParent;
        if (fragment != null) {
            fragment.getParentFragmentManager().mLifecycleCallbacksDispatcher.dispatchOnFragmentDestroyed(true);
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (z) {
                Objects.requireNonNull(next);
            } else {
                Objects.requireNonNull(next);
                throw null;
            }
        }
    }

    public final void dispatchOnFragmentDetached(boolean z) {
        FragmentManager fragmentManager = this.mFragmentManager;
        Objects.requireNonNull(fragmentManager);
        Fragment fragment = fragmentManager.mParent;
        if (fragment != null) {
            fragment.getParentFragmentManager().mLifecycleCallbacksDispatcher.dispatchOnFragmentDetached(true);
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (z) {
                Objects.requireNonNull(next);
            } else {
                Objects.requireNonNull(next);
                throw null;
            }
        }
    }

    public final void dispatchOnFragmentPaused(boolean z) {
        FragmentManager fragmentManager = this.mFragmentManager;
        Objects.requireNonNull(fragmentManager);
        Fragment fragment = fragmentManager.mParent;
        if (fragment != null) {
            fragment.getParentFragmentManager().mLifecycleCallbacksDispatcher.dispatchOnFragmentPaused(true);
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (z) {
                Objects.requireNonNull(next);
            } else {
                Objects.requireNonNull(next);
                throw null;
            }
        }
    }

    public final void dispatchOnFragmentPreAttached(boolean z) {
        FragmentManager fragmentManager = this.mFragmentManager;
        Objects.requireNonNull(fragmentManager);
        Objects.requireNonNull(fragmentManager.mHost);
        FragmentManager fragmentManager2 = this.mFragmentManager;
        Objects.requireNonNull(fragmentManager2);
        Fragment fragment = fragmentManager2.mParent;
        if (fragment != null) {
            fragment.getParentFragmentManager().mLifecycleCallbacksDispatcher.dispatchOnFragmentPreAttached(true);
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (z) {
                Objects.requireNonNull(next);
            } else {
                Objects.requireNonNull(next);
                throw null;
            }
        }
    }

    public final void dispatchOnFragmentPreCreated(boolean z) {
        FragmentManager fragmentManager = this.mFragmentManager;
        Objects.requireNonNull(fragmentManager);
        Fragment fragment = fragmentManager.mParent;
        if (fragment != null) {
            fragment.getParentFragmentManager().mLifecycleCallbacksDispatcher.dispatchOnFragmentPreCreated(true);
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (z) {
                Objects.requireNonNull(next);
            } else {
                Objects.requireNonNull(next);
                throw null;
            }
        }
    }

    public final void dispatchOnFragmentResumed(boolean z) {
        FragmentManager fragmentManager = this.mFragmentManager;
        Objects.requireNonNull(fragmentManager);
        Fragment fragment = fragmentManager.mParent;
        if (fragment != null) {
            fragment.getParentFragmentManager().mLifecycleCallbacksDispatcher.dispatchOnFragmentResumed(true);
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (z) {
                Objects.requireNonNull(next);
            } else {
                Objects.requireNonNull(next);
                throw null;
            }
        }
    }

    public final void dispatchOnFragmentSaveInstanceState(boolean z) {
        FragmentManager fragmentManager = this.mFragmentManager;
        Objects.requireNonNull(fragmentManager);
        Fragment fragment = fragmentManager.mParent;
        if (fragment != null) {
            fragment.getParentFragmentManager().mLifecycleCallbacksDispatcher.dispatchOnFragmentSaveInstanceState(true);
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (z) {
                Objects.requireNonNull(next);
            } else {
                Objects.requireNonNull(next);
                throw null;
            }
        }
    }

    public final void dispatchOnFragmentStarted(boolean z) {
        FragmentManager fragmentManager = this.mFragmentManager;
        Objects.requireNonNull(fragmentManager);
        Fragment fragment = fragmentManager.mParent;
        if (fragment != null) {
            fragment.getParentFragmentManager().mLifecycleCallbacksDispatcher.dispatchOnFragmentStarted(true);
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (z) {
                Objects.requireNonNull(next);
            } else {
                Objects.requireNonNull(next);
                throw null;
            }
        }
    }

    public final void dispatchOnFragmentStopped(boolean z) {
        FragmentManager fragmentManager = this.mFragmentManager;
        Objects.requireNonNull(fragmentManager);
        Fragment fragment = fragmentManager.mParent;
        if (fragment != null) {
            fragment.getParentFragmentManager().mLifecycleCallbacksDispatcher.dispatchOnFragmentStopped(true);
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (z) {
                Objects.requireNonNull(next);
            } else {
                Objects.requireNonNull(next);
                throw null;
            }
        }
    }

    public final void dispatchOnFragmentViewCreated(boolean z) {
        FragmentManager fragmentManager = this.mFragmentManager;
        Objects.requireNonNull(fragmentManager);
        Fragment fragment = fragmentManager.mParent;
        if (fragment != null) {
            fragment.getParentFragmentManager().mLifecycleCallbacksDispatcher.dispatchOnFragmentViewCreated(true);
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (z) {
                Objects.requireNonNull(next);
            } else {
                Objects.requireNonNull(next);
                throw null;
            }
        }
    }

    public final void dispatchOnFragmentViewDestroyed(boolean z) {
        FragmentManager fragmentManager = this.mFragmentManager;
        Objects.requireNonNull(fragmentManager);
        Fragment fragment = fragmentManager.mParent;
        if (fragment != null) {
            fragment.getParentFragmentManager().mLifecycleCallbacksDispatcher.dispatchOnFragmentViewDestroyed(true);
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (z) {
                Objects.requireNonNull(next);
            } else {
                Objects.requireNonNull(next);
                throw null;
            }
        }
    }

    public FragmentLifecycleCallbacksDispatcher(FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }
}
