package androidx.fragment.app;

import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class FragmentStore {
    public FragmentManagerViewModel mNonConfig;
    public final ArrayList<Fragment> mAdded = new ArrayList<>();
    public final HashMap<String, FragmentStateManager> mActive = new HashMap<>();
    public final HashMap<String, FragmentState> mSavedState = new HashMap<>();

    public final void addFragment(Fragment fragment) {
        if (!this.mAdded.contains(fragment)) {
            synchronized (this.mAdded) {
                this.mAdded.add(fragment);
            }
            fragment.mAdded = true;
            return;
        }
        throw new IllegalStateException("Fragment already added: " + fragment);
    }

    public final Fragment findActiveFragment(String str) {
        FragmentStateManager fragmentStateManager = this.mActive.get(str);
        if (fragmentStateManager != null) {
            return fragmentStateManager.mFragment;
        }
        return null;
    }

    public final Fragment findFragmentByWho(String str) {
        for (FragmentStateManager fragmentStateManager : this.mActive.values()) {
            if (fragmentStateManager != null) {
                Fragment fragment = fragmentStateManager.mFragment;
                Objects.requireNonNull(fragment);
                if (!str.equals(fragment.mWho)) {
                    FragmentManagerImpl fragmentManagerImpl = fragment.mChildFragmentManager;
                    Objects.requireNonNull(fragmentManagerImpl);
                    fragment = fragmentManagerImpl.mFragmentStore.findFragmentByWho(str);
                }
                if (fragment != null) {
                    return fragment;
                }
            }
        }
        return null;
    }

    public final ArrayList getActiveFragmentStateManagers() {
        ArrayList arrayList = new ArrayList();
        for (FragmentStateManager fragmentStateManager : this.mActive.values()) {
            if (fragmentStateManager != null) {
                arrayList.add(fragmentStateManager);
            }
        }
        return arrayList;
    }

    public final List<Fragment> getFragments() {
        ArrayList arrayList;
        if (this.mAdded.isEmpty()) {
            return Collections.emptyList();
        }
        synchronized (this.mAdded) {
            arrayList = new ArrayList(this.mAdded);
        }
        return arrayList;
    }

    public final void makeActive(FragmentStateManager fragmentStateManager) {
        boolean z;
        Fragment fragment = fragmentStateManager.mFragment;
        if (this.mActive.get(fragment.mWho) != null) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            this.mActive.put(fragment.mWho, fragmentStateManager);
            if (FragmentManager.isLoggingEnabled(2)) {
                Log.v("FragmentManager", "Added fragment to active set " + fragment);
            }
        }
    }

    public final FragmentState setSavedState(String str, FragmentState fragmentState) {
        if (fragmentState != null) {
            return this.mSavedState.put(str, fragmentState);
        }
        return this.mSavedState.remove(str);
    }

    public final void makeInactive(FragmentStateManager fragmentStateManager) {
        Objects.requireNonNull(fragmentStateManager);
        Fragment fragment = fragmentStateManager.mFragment;
        if (fragment.mRetainInstance) {
            this.mNonConfig.removeRetainedFragment(fragment);
        }
        if (this.mActive.put(fragment.mWho, null) != null && FragmentManager.isLoggingEnabled(2)) {
            Log.v("FragmentManager", "Removed fragment from active set " + fragment);
        }
    }
}
