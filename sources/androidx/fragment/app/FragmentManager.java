package androidx.fragment.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.Cancellable;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.ActivityResultRegistry$3$$ExternalSyntheticOutline0;
import androidx.activity.result.ActivityResultRegistryOwner;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import androidx.concurrent.futures.AbstractResolvableFuture$$ExternalSyntheticOutline0;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline1;
import androidx.constraintlayout.motion.widget.MotionLayout$$ExternalSyntheticOutline0;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline0;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.strictmode.FragmentStrictMode;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryOwner;
import com.android.systemui.plugins.FalsingManager;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public abstract class FragmentManager {
    public ArrayList<BackStackRecord> mBackStack;
    public FragmentContainer mContainer;
    public ArrayList<Fragment> mCreatedMenus;
    public boolean mDestroyed;
    public boolean mExecutingActions;
    public boolean mHavePendingDeferredStart;
    public FragmentHostCallback<?> mHost;
    public boolean mNeedMenuInvalidate;
    public FragmentManagerViewModel mNonConfig;
    public OnBackPressedDispatcher mOnBackPressedDispatcher;
    public Fragment mParent;
    public Fragment mPrimaryNav;
    public ActivityResultRegistry.AnonymousClass3 mRequestPermissions;
    public ActivityResultRegistry.AnonymousClass3 mStartActivityForResult;
    public ActivityResultRegistry.AnonymousClass3 mStartIntentSenderForResult;
    public boolean mStateSaved;
    public boolean mStopped;
    public ArrayList<Fragment> mTmpAddedFragments;
    public ArrayList<Boolean> mTmpIsPop;
    public ArrayList<BackStackRecord> mTmpRecords;
    public final ArrayList<OpGenerator> mPendingActions = new ArrayList<>();
    public final FragmentStore mFragmentStore = new FragmentStore();
    public final FragmentLayoutInflaterFactory mLayoutInflaterFactory = new FragmentLayoutInflaterFactory(this);
    public final AnonymousClass1 mOnBackPressedCallback = new OnBackPressedCallback() { // from class: androidx.fragment.app.FragmentManager.1
        @Override // androidx.activity.OnBackPressedCallback
        public final void handleOnBackPressed() {
            FragmentManager fragmentManager = FragmentManager.this;
            Objects.requireNonNull(fragmentManager);
            fragmentManager.execPendingActions(true);
            AnonymousClass1 r0 = fragmentManager.mOnBackPressedCallback;
            Objects.requireNonNull(r0);
            if (r0.mEnabled) {
                fragmentManager.popBackStackImmediate$1();
            } else {
                fragmentManager.mOnBackPressedDispatcher.onBackPressed();
            }
        }
    };
    public final AtomicInteger mBackStackIndex = new AtomicInteger();
    public final Map<String, BackStackState> mBackStackStates = Collections.synchronizedMap(new HashMap());
    public final Map<String, Bundle> mResults = Collections.synchronizedMap(new HashMap());
    public final Map<String, Object> mResultListeners = Collections.synchronizedMap(new HashMap());
    public final FragmentLifecycleCallbacksDispatcher mLifecycleCallbacksDispatcher = new FragmentLifecycleCallbacksDispatcher(this);
    public final CopyOnWriteArrayList<FragmentOnAttachListener> mOnAttachListeners = new CopyOnWriteArrayList<>();
    public int mCurState = -1;
    public AnonymousClass2 mHostFragmentFactory = new FragmentFactory() { // from class: androidx.fragment.app.FragmentManager.2
        @Override // androidx.fragment.app.FragmentFactory
        public final Fragment instantiate(String str) {
            FragmentManager fragmentManager = FragmentManager.this;
            Objects.requireNonNull(fragmentManager);
            FragmentHostCallback<?> fragmentHostCallback = fragmentManager.mHost;
            FragmentManager fragmentManager2 = FragmentManager.this;
            Objects.requireNonNull(fragmentManager2);
            FragmentHostCallback<?> fragmentHostCallback2 = fragmentManager2.mHost;
            Objects.requireNonNull(fragmentHostCallback2);
            Context context = fragmentHostCallback2.mContext;
            Objects.requireNonNull(fragmentHostCallback);
            Object obj = Fragment.USE_DEFAULT_TRANSITION;
            try {
                return (Fragment) FragmentFactory.loadFragmentClass(context.getClassLoader(), str).getConstructor(new Class[0]).newInstance(new Object[0]);
            } catch (IllegalAccessException e) {
                throw new Fragment.InstantiationException(AbstractResolvableFuture$$ExternalSyntheticOutline0.m("Unable to instantiate fragment ", str, ": make sure class name exists, is public, and has an empty constructor that is public"), e);
            } catch (InstantiationException e2) {
                throw new Fragment.InstantiationException(AbstractResolvableFuture$$ExternalSyntheticOutline0.m("Unable to instantiate fragment ", str, ": make sure class name exists, is public, and has an empty constructor that is public"), e2);
            } catch (NoSuchMethodException e3) {
                throw new Fragment.InstantiationException(AbstractResolvableFuture$$ExternalSyntheticOutline0.m("Unable to instantiate fragment ", str, ": could not find Fragment constructor"), e3);
            } catch (InvocationTargetException e4) {
                throw new Fragment.InstantiationException(AbstractResolvableFuture$$ExternalSyntheticOutline0.m("Unable to instantiate fragment ", str, ": calling Fragment constructor caused an exception"), e4);
            }
        }
    };
    public AnonymousClass3 mDefaultSpecialEffectsControllerFactory = new SpecialEffectsControllerFactory() { // from class: androidx.fragment.app.FragmentManager.3
    };
    public ArrayDeque<LaunchedFragmentInfo> mLaunchedFragments = new ArrayDeque<>();
    public AnonymousClass4 mExecCommit = new Runnable() { // from class: androidx.fragment.app.FragmentManager.4
        @Override // java.lang.Runnable
        public final void run() {
            FragmentManager.this.execPendingActions(true);
        }
    };

    /* renamed from: androidx.fragment.app.FragmentManager$5  reason: invalid class name */
    /* loaded from: classes.dex */
    class AnonymousClass5 implements LifecycleEventObserver {
        @Override // androidx.lifecycle.LifecycleEventObserver
        public final void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
            if (event == Lifecycle.Event.ON_START) {
                throw null;
            } else if (event == Lifecycle.Event.ON_DESTROY) {
                throw null;
            }
        }
    }

    /* loaded from: classes.dex */
    public static class FragmentIntentSenderContract extends ActivityResultContract<IntentSenderRequest, ActivityResult> {
        @Override // androidx.activity.result.contract.ActivityResultContract
        public final ActivityResult parseResult(int i, Intent intent) {
            return new ActivityResult(i, intent);
        }
    }

    @SuppressLint({"BanParcelableUsage"})
    /* loaded from: classes.dex */
    public static class LaunchedFragmentInfo implements Parcelable {
        public static final Parcelable.Creator<LaunchedFragmentInfo> CREATOR = new Parcelable.Creator<LaunchedFragmentInfo>() { // from class: androidx.fragment.app.FragmentManager.LaunchedFragmentInfo.1
            @Override // android.os.Parcelable.Creator
            public final LaunchedFragmentInfo createFromParcel(Parcel parcel) {
                return new LaunchedFragmentInfo(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public final LaunchedFragmentInfo[] newArray(int i) {
                return new LaunchedFragmentInfo[i];
            }
        };
        public int mRequestCode;
        public String mWho;

        @Override // android.os.Parcelable
        public final int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.mWho);
            parcel.writeInt(this.mRequestCode);
        }

        public LaunchedFragmentInfo(Parcel parcel) {
            this.mWho = parcel.readString();
            this.mRequestCode = parcel.readInt();
        }
    }

    /* loaded from: classes.dex */
    public interface OpGenerator {
        boolean generateOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2);
    }

    /* loaded from: classes.dex */
    public class PopBackStackState implements OpGenerator {
        public final int mFlags = 1;
        public final int mId;

        public PopBackStackState(int i) {
            this.mId = i;
        }

        @Override // androidx.fragment.app.FragmentManager.OpGenerator
        public final boolean generateOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
            Fragment fragment = FragmentManager.this.mPrimaryNav;
            if (fragment == null || this.mId >= 0 || !fragment.getChildFragmentManager().popBackStackImmediate$1()) {
                return FragmentManager.this.popBackStackState(arrayList, arrayList2, this.mId, this.mFlags);
            }
            return false;
        }
    }

    public static boolean isParentMenuVisible(Fragment fragment) {
        if (fragment == null) {
            return true;
        }
        return fragment.mMenuVisible && (fragment.mFragmentManager == null || isParentMenuVisible(fragment.mParentFragment));
    }

    public static boolean isPrimaryNavigation(Fragment fragment) {
        if (fragment == null) {
            return true;
        }
        FragmentManager fragmentManager = fragment.mFragmentManager;
        Objects.requireNonNull(fragmentManager);
        return fragment.equals(fragmentManager.mPrimaryNav) && isPrimaryNavigation(fragmentManager.mParent);
    }

    public static void showFragment(Fragment fragment) {
        if (isLoggingEnabled(2)) {
            Log.v("FragmentManager", "show: " + fragment);
        }
        if (fragment.mHidden) {
            fragment.mHidden = false;
            fragment.mHiddenChanged = !fragment.mHiddenChanged;
        }
    }

    public final void attachFragment(Fragment fragment) {
        if (isLoggingEnabled(2)) {
            Log.v("FragmentManager", "attach: " + fragment);
        }
        if (fragment.mDetached) {
            fragment.mDetached = false;
            if (!fragment.mAdded) {
                this.mFragmentStore.addFragment(fragment);
                if (isLoggingEnabled(2)) {
                    Log.v("FragmentManager", "add from attach: " + fragment);
                }
                if (isMenuAvailable(fragment)) {
                    this.mNeedMenuInvalidate = true;
                }
            }
        }
    }

    public final void cleanupExec() {
        this.mExecutingActions = false;
        this.mTmpIsPop.clear();
        this.mTmpRecords.clear();
    }

    public final void detachFragment(Fragment fragment) {
        if (isLoggingEnabled(2)) {
            Log.v("FragmentManager", "detach: " + fragment);
        }
        if (!fragment.mDetached) {
            fragment.mDetached = true;
            if (fragment.mAdded) {
                if (isLoggingEnabled(2)) {
                    Log.v("FragmentManager", "remove from detach: " + fragment);
                }
                FragmentStore fragmentStore = this.mFragmentStore;
                Objects.requireNonNull(fragmentStore);
                synchronized (fragmentStore.mAdded) {
                    fragmentStore.mAdded.remove(fragment);
                }
                fragment.mAdded = false;
                if (isMenuAvailable(fragment)) {
                    this.mNeedMenuInvalidate = true;
                }
                setVisibleRemovingFragment(fragment);
            }
        }
    }

    public final void dispatchCreate() {
        this.mStateSaved = false;
        this.mStopped = false;
        FragmentManagerViewModel fragmentManagerViewModel = this.mNonConfig;
        Objects.requireNonNull(fragmentManagerViewModel);
        fragmentManagerViewModel.mIsStateSaved = false;
        dispatchStateChange(1);
    }

    public final void dispatchDestroy() {
        Integer num;
        Integer num2;
        Integer num3;
        boolean z = true;
        this.mDestroyed = true;
        execPendingActions(true);
        Iterator it = collectAllSpecialEffectsController().iterator();
        while (it.hasNext()) {
            ((SpecialEffectsController) it.next()).forceCompleteAllOperations();
        }
        FragmentHostCallback<?> fragmentHostCallback = this.mHost;
        if (fragmentHostCallback instanceof ViewModelStoreOwner) {
            FragmentStore fragmentStore = this.mFragmentStore;
            Objects.requireNonNull(fragmentStore);
            FragmentManagerViewModel fragmentManagerViewModel = fragmentStore.mNonConfig;
            Objects.requireNonNull(fragmentManagerViewModel);
            z = fragmentManagerViewModel.mHasBeenCleared;
        } else {
            Objects.requireNonNull(fragmentHostCallback);
            if (fragmentHostCallback.mContext instanceof Activity) {
                FragmentHostCallback<?> fragmentHostCallback2 = this.mHost;
                Objects.requireNonNull(fragmentHostCallback2);
                z = true ^ ((Activity) fragmentHostCallback2.mContext).isChangingConfigurations();
            }
        }
        if (z) {
            for (BackStackState backStackState : this.mBackStackStates.values()) {
                for (String str : backStackState.mFragments) {
                    FragmentStore fragmentStore2 = this.mFragmentStore;
                    Objects.requireNonNull(fragmentStore2);
                    FragmentManagerViewModel fragmentManagerViewModel2 = fragmentStore2.mNonConfig;
                    Objects.requireNonNull(fragmentManagerViewModel2);
                    if (isLoggingEnabled(3)) {
                        DialogFragment$$ExternalSyntheticOutline0.m("Clearing non-config state for saved state of Fragment ", str, "FragmentManager");
                    }
                    fragmentManagerViewModel2.clearNonConfigStateInternal(str);
                }
            }
        }
        dispatchStateChange(-1);
        this.mHost = null;
        this.mContainer = null;
        this.mParent = null;
        if (this.mOnBackPressedDispatcher != null) {
            AnonymousClass1 r1 = this.mOnBackPressedCallback;
            Objects.requireNonNull(r1);
            Iterator<Cancellable> it2 = r1.mCancellables.iterator();
            while (it2.hasNext()) {
                it2.next().cancel();
            }
            this.mOnBackPressedDispatcher = null;
        }
        ActivityResultRegistry.AnonymousClass3 r12 = this.mStartActivityForResult;
        if (r12 != null) {
            ActivityResultRegistry activityResultRegistry = ActivityResultRegistry.this;
            String str2 = r12.val$key;
            Objects.requireNonNull(activityResultRegistry);
            if (!activityResultRegistry.mLaunchedKeys.contains(str2) && (num3 = (Integer) activityResultRegistry.mKeyToRc.remove(str2)) != null) {
                activityResultRegistry.mRcToKey.remove(num3);
            }
            activityResultRegistry.mKeyToCallback.remove(str2);
            if (activityResultRegistry.mParsedPendingResults.containsKey(str2)) {
                StringBuilder m = ActivityResultRegistry$3$$ExternalSyntheticOutline0.m("Dropping pending result for request ", str2, ": ");
                m.append(activityResultRegistry.mParsedPendingResults.get(str2));
                Log.w("ActivityResultRegistry", m.toString());
                activityResultRegistry.mParsedPendingResults.remove(str2);
            }
            if (activityResultRegistry.mPendingResults.containsKey(str2)) {
                StringBuilder m2 = ActivityResultRegistry$3$$ExternalSyntheticOutline0.m("Dropping pending result for request ", str2, ": ");
                m2.append(activityResultRegistry.mPendingResults.getParcelable(str2));
                Log.w("ActivityResultRegistry", m2.toString());
                activityResultRegistry.mPendingResults.remove(str2);
            }
            if (((ActivityResultRegistry.LifecycleContainer) activityResultRegistry.mKeyToLifecycleContainers.get(str2)) == null) {
                ActivityResultRegistry.AnonymousClass3 r13 = this.mStartIntentSenderForResult;
                Objects.requireNonNull(r13);
                ActivityResultRegistry activityResultRegistry2 = ActivityResultRegistry.this;
                String str3 = r13.val$key;
                Objects.requireNonNull(activityResultRegistry2);
                if (!activityResultRegistry2.mLaunchedKeys.contains(str3) && (num2 = (Integer) activityResultRegistry2.mKeyToRc.remove(str3)) != null) {
                    activityResultRegistry2.mRcToKey.remove(num2);
                }
                activityResultRegistry2.mKeyToCallback.remove(str3);
                if (activityResultRegistry2.mParsedPendingResults.containsKey(str3)) {
                    StringBuilder m3 = ActivityResultRegistry$3$$ExternalSyntheticOutline0.m("Dropping pending result for request ", str3, ": ");
                    m3.append(activityResultRegistry2.mParsedPendingResults.get(str3));
                    Log.w("ActivityResultRegistry", m3.toString());
                    activityResultRegistry2.mParsedPendingResults.remove(str3);
                }
                if (activityResultRegistry2.mPendingResults.containsKey(str3)) {
                    StringBuilder m4 = ActivityResultRegistry$3$$ExternalSyntheticOutline0.m("Dropping pending result for request ", str3, ": ");
                    m4.append(activityResultRegistry2.mPendingResults.getParcelable(str3));
                    Log.w("ActivityResultRegistry", m4.toString());
                    activityResultRegistry2.mPendingResults.remove(str3);
                }
                if (((ActivityResultRegistry.LifecycleContainer) activityResultRegistry2.mKeyToLifecycleContainers.get(str3)) == null) {
                    ActivityResultRegistry.AnonymousClass3 r8 = this.mRequestPermissions;
                    Objects.requireNonNull(r8);
                    ActivityResultRegistry activityResultRegistry3 = ActivityResultRegistry.this;
                    String str4 = r8.val$key;
                    Objects.requireNonNull(activityResultRegistry3);
                    if (!activityResultRegistry3.mLaunchedKeys.contains(str4) && (num = (Integer) activityResultRegistry3.mKeyToRc.remove(str4)) != null) {
                        activityResultRegistry3.mRcToKey.remove(num);
                    }
                    activityResultRegistry3.mKeyToCallback.remove(str4);
                    if (activityResultRegistry3.mParsedPendingResults.containsKey(str4)) {
                        StringBuilder m5 = ActivityResultRegistry$3$$ExternalSyntheticOutline0.m("Dropping pending result for request ", str4, ": ");
                        m5.append(activityResultRegistry3.mParsedPendingResults.get(str4));
                        Log.w("ActivityResultRegistry", m5.toString());
                        activityResultRegistry3.mParsedPendingResults.remove(str4);
                    }
                    if (activityResultRegistry3.mPendingResults.containsKey(str4)) {
                        StringBuilder m6 = ActivityResultRegistry$3$$ExternalSyntheticOutline0.m("Dropping pending result for request ", str4, ": ");
                        m6.append(activityResultRegistry3.mPendingResults.getParcelable(str4));
                        Log.w("ActivityResultRegistry", m6.toString());
                        activityResultRegistry3.mPendingResults.remove(str4);
                    }
                    if (((ActivityResultRegistry.LifecycleContainer) activityResultRegistry3.mKeyToLifecycleContainers.get(str4)) != null) {
                        throw null;
                    }
                    return;
                }
                throw null;
            }
            throw null;
        }
    }

    /* JADX WARN: Finally extract failed */
    public final void dispatchStateChange(int i) {
        try {
            this.mExecutingActions = true;
            FragmentStore fragmentStore = this.mFragmentStore;
            Objects.requireNonNull(fragmentStore);
            for (FragmentStateManager fragmentStateManager : fragmentStore.mActive.values()) {
                if (fragmentStateManager != null) {
                    fragmentStateManager.mFragmentManagerState = i;
                }
            }
            moveToState(i, false);
            Iterator it = collectAllSpecialEffectsController().iterator();
            while (it.hasNext()) {
                ((SpecialEffectsController) it.next()).forceCompleteAllOperations();
            }
            this.mExecutingActions = false;
            execPendingActions(true);
        } catch (Throwable th) {
            this.mExecutingActions = false;
            throw th;
        }
    }

    public final void hideFragment(Fragment fragment) {
        if (isLoggingEnabled(2)) {
            Log.v("FragmentManager", "hide: " + fragment);
        }
        if (!fragment.mHidden) {
            fragment.mHidden = true;
            fragment.mHiddenChanged = true ^ fragment.mHiddenChanged;
            setVisibleRemovingFragment(fragment);
        }
    }

    public final boolean popBackStackImmediate$1() {
        execPendingActions(false);
        ensureExecReady(true);
        Fragment fragment = this.mPrimaryNav;
        if (fragment != null && fragment.getChildFragmentManager().popBackStackImmediate$1()) {
            return true;
        }
        boolean popBackStackState = popBackStackState(this.mTmpRecords, this.mTmpIsPop, -1, 0);
        if (popBackStackState) {
            this.mExecutingActions = true;
            try {
                removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
            } finally {
                cleanupExec();
            }
        }
        updateOnBackPressedCallbackEnabled();
        doPendingDeferredStart();
        FragmentStore fragmentStore = this.mFragmentStore;
        Objects.requireNonNull(fragmentStore);
        fragmentStore.mActive.values().removeAll(Collections.singleton(null));
        return popBackStackState;
    }

    public final boolean popBackStackState(ArrayList arrayList, ArrayList arrayList2, int i, int i2) {
        boolean z;
        if ((i2 & 1) != 0) {
            z = true;
        } else {
            z = false;
        }
        ArrayList<BackStackRecord> arrayList3 = this.mBackStack;
        int i3 = -1;
        if (arrayList3 != null && !arrayList3.isEmpty()) {
            if (i < 0) {
                i3 = z ? 0 : (-1) + this.mBackStack.size();
            } else {
                int size = this.mBackStack.size() - 1;
                while (size >= 0) {
                    BackStackRecord backStackRecord = this.mBackStack.get(size);
                    if (i >= 0 && i == backStackRecord.mIndex) {
                        break;
                    }
                    size--;
                }
                if (size >= 0) {
                    if (z) {
                        while (size > 0) {
                            int i4 = size - 1;
                            BackStackRecord backStackRecord2 = this.mBackStack.get(i4);
                            if (i < 0 || i != backStackRecord2.mIndex) {
                                break;
                            }
                            size = i4;
                        }
                    } else if (size != this.mBackStack.size() - 1) {
                        size++;
                    }
                }
                i3 = size;
            }
        }
        if (i3 < 0) {
            return false;
        }
        for (int size2 = this.mBackStack.size() - 1; size2 >= i3; size2--) {
            arrayList.add(this.mBackStack.remove(size2));
            arrayList2.add(Boolean.TRUE);
        }
        return true;
    }

    public final void removeFragment(Fragment fragment) {
        boolean z;
        if (isLoggingEnabled(2)) {
            Log.v("FragmentManager", "remove: " + fragment + " nesting=" + fragment.mBackStackNesting);
        }
        if (fragment.mBackStackNesting > 0) {
            z = true;
        } else {
            z = false;
        }
        boolean z2 = !z;
        if (!fragment.mDetached || z2) {
            FragmentStore fragmentStore = this.mFragmentStore;
            Objects.requireNonNull(fragmentStore);
            synchronized (fragmentStore.mAdded) {
                fragmentStore.mAdded.remove(fragment);
            }
            fragment.mAdded = false;
            if (isMenuAvailable(fragment)) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.mRemoving = true;
            setVisibleRemovingFragment(fragment);
        }
    }

    public static boolean isLoggingEnabled(int i) {
        if (Log.isLoggable("FragmentManager", i)) {
            return true;
        }
        return false;
    }

    public static boolean isMenuAvailable(Fragment fragment) {
        FragmentManagerImpl fragmentManagerImpl = fragment.mChildFragmentManager;
        Objects.requireNonNull(fragmentManagerImpl);
        FragmentStore fragmentStore = fragmentManagerImpl.mFragmentStore;
        Objects.requireNonNull(fragmentStore);
        ArrayList arrayList = new ArrayList();
        for (FragmentStateManager fragmentStateManager : fragmentStore.mActive.values()) {
            if (fragmentStateManager != null) {
                arrayList.add(fragmentStateManager.mFragment);
            } else {
                arrayList.add(null);
            }
        }
        Iterator it = arrayList.iterator();
        boolean z = false;
        while (it.hasNext()) {
            Fragment fragment2 = (Fragment) it.next();
            if (fragment2 != null) {
                z = isMenuAvailable(fragment2);
                continue;
            }
            if (z) {
                return true;
            }
        }
        return false;
    }

    public final FragmentStateManager addFragment(Fragment fragment) {
        String str = fragment.mPreviousWho;
        if (str != null) {
            FragmentStrictMode.onFragmentReuse(fragment, str);
        }
        if (isLoggingEnabled(2)) {
            Log.v("FragmentManager", "add: " + fragment);
        }
        FragmentStateManager createOrGetFragmentStateManager = createOrGetFragmentStateManager(fragment);
        fragment.mFragmentManager = this;
        this.mFragmentStore.makeActive(createOrGetFragmentStateManager);
        if (!fragment.mDetached) {
            this.mFragmentStore.addFragment(fragment);
            fragment.mRemoving = false;
            if (fragment.mView == null) {
                fragment.mHiddenChanged = false;
            }
            if (isMenuAvailable(fragment)) {
                this.mNeedMenuInvalidate = true;
            }
        }
        return createOrGetFragmentStateManager;
    }

    @SuppressLint({"SyntheticAccessor"})
    public final void attachController(FragmentHostCallback<?> fragmentHostCallback, FragmentContainer fragmentContainer, final Fragment fragment) {
        String str;
        if (this.mHost == null) {
            this.mHost = fragmentHostCallback;
            this.mContainer = fragmentContainer;
            this.mParent = fragment;
            if (fragment != null) {
                this.mOnAttachListeners.add(new FragmentOnAttachListener() { // from class: androidx.fragment.app.FragmentManager.6
                    @Override // androidx.fragment.app.FragmentOnAttachListener
                    public final void onAttachFragment$1() {
                        Objects.requireNonNull(Fragment.this);
                    }
                });
            } else if (fragmentHostCallback instanceof FragmentOnAttachListener) {
                this.mOnAttachListeners.add((FragmentOnAttachListener) fragmentHostCallback);
            }
            if (this.mParent != null) {
                updateOnBackPressedCallbackEnabled();
            }
            if (fragmentHostCallback instanceof OnBackPressedDispatcherOwner) {
                OnBackPressedDispatcherOwner onBackPressedDispatcherOwner = (OnBackPressedDispatcherOwner) fragmentHostCallback;
                OnBackPressedDispatcher onBackPressedDispatcher = onBackPressedDispatcherOwner.getOnBackPressedDispatcher();
                this.mOnBackPressedDispatcher = onBackPressedDispatcher;
                LifecycleOwner lifecycleOwner = onBackPressedDispatcherOwner;
                if (fragment != null) {
                    lifecycleOwner = fragment;
                }
                onBackPressedDispatcher.addCallback(lifecycleOwner, this.mOnBackPressedCallback);
            }
            boolean z = false;
            if (fragment != null) {
                FragmentManager fragmentManager = fragment.mFragmentManager;
                Objects.requireNonNull(fragmentManager);
                FragmentManagerViewModel fragmentManagerViewModel = fragmentManager.mNonConfig;
                Objects.requireNonNull(fragmentManagerViewModel);
                FragmentManagerViewModel fragmentManagerViewModel2 = fragmentManagerViewModel.mChildNonConfigs.get(fragment.mWho);
                if (fragmentManagerViewModel2 == null) {
                    fragmentManagerViewModel2 = new FragmentManagerViewModel(fragmentManagerViewModel.mStateAutomaticallySaved);
                    fragmentManagerViewModel.mChildNonConfigs.put(fragment.mWho, fragmentManagerViewModel2);
                }
                this.mNonConfig = fragmentManagerViewModel2;
            } else if (fragmentHostCallback instanceof ViewModelStoreOwner) {
                this.mNonConfig = (FragmentManagerViewModel) new ViewModelProvider(((ViewModelStoreOwner) fragmentHostCallback).getViewModelStore(), FragmentManagerViewModel.FACTORY).get(FragmentManagerViewModel.class);
            } else {
                this.mNonConfig = new FragmentManagerViewModel(false);
            }
            FragmentManagerViewModel fragmentManagerViewModel3 = this.mNonConfig;
            if (this.mStateSaved || this.mStopped) {
                z = true;
            }
            Objects.requireNonNull(fragmentManagerViewModel3);
            fragmentManagerViewModel3.mIsStateSaved = z;
            FragmentStore fragmentStore = this.mFragmentStore;
            FragmentManagerViewModel fragmentManagerViewModel4 = this.mNonConfig;
            Objects.requireNonNull(fragmentStore);
            fragmentStore.mNonConfig = fragmentManagerViewModel4;
            FragmentHostCallback<?> fragmentHostCallback2 = this.mHost;
            if ((fragmentHostCallback2 instanceof SavedStateRegistryOwner) && fragment == null) {
                SavedStateRegistry savedStateRegistry = ((SavedStateRegistryOwner) fragmentHostCallback2).getSavedStateRegistry();
                final FragmentManagerImpl fragmentManagerImpl = (FragmentManagerImpl) this;
                savedStateRegistry.registerSavedStateProvider("android:support:fragments", new SavedStateRegistry.SavedStateProvider() { // from class: androidx.fragment.app.FragmentManager$$ExternalSyntheticLambda0
                    @Override // androidx.savedstate.SavedStateRegistry.SavedStateProvider
                    public final Bundle saveState() {
                        FragmentManager fragmentManager2 = fragmentManagerImpl;
                        Objects.requireNonNull(fragmentManager2);
                        Bundle bundle = new Bundle();
                        Parcelable saveAllStateInternal = fragmentManager2.saveAllStateInternal();
                        if (saveAllStateInternal != null) {
                            bundle.putParcelable("android:support:fragments", saveAllStateInternal);
                        }
                        return bundle;
                    }
                });
                Bundle consumeRestoredStateForKey = savedStateRegistry.consumeRestoredStateForKey("android:support:fragments");
                if (consumeRestoredStateForKey != null) {
                    restoreSaveStateInternal(consumeRestoredStateForKey.getParcelable("android:support:fragments"));
                }
            }
            FragmentHostCallback<?> fragmentHostCallback3 = this.mHost;
            if (fragmentHostCallback3 instanceof ActivityResultRegistryOwner) {
                ActivityResultRegistry activityResultRegistry = ((ActivityResultRegistryOwner) fragmentHostCallback3).getActivityResultRegistry();
                if (fragment != null) {
                    str = MotionController$$ExternalSyntheticOutline1.m(new StringBuilder(), fragment.mWho, ":");
                } else {
                    str = "";
                }
                String m = SupportMenuInflater$$ExternalSyntheticOutline0.m("FragmentManager:", str);
                final FragmentManagerImpl fragmentManagerImpl2 = (FragmentManagerImpl) this;
                this.mStartActivityForResult = activityResultRegistry.register(SupportMenuInflater$$ExternalSyntheticOutline0.m(m, "StartActivityForResult"), new ActivityResultContract<Intent, ActivityResult>() { // from class: androidx.activity.result.contract.ActivityResultContracts$StartActivityForResult
                    @Override // androidx.activity.result.contract.ActivityResultContract
                    public final ActivityResult parseResult(int i, Intent intent) {
                        return new ActivityResult(i, intent);
                    }
                }, new ActivityResultCallback<ActivityResult>() { // from class: androidx.fragment.app.FragmentManager.7
                    @Override // androidx.activity.result.ActivityResultCallback
                    public final void onActivityResult(ActivityResult activityResult) {
                        ActivityResult activityResult2 = activityResult;
                        LaunchedFragmentInfo pollFirst = fragmentManagerImpl2.mLaunchedFragments.pollFirst();
                        if (pollFirst == null) {
                            Log.w("FragmentManager", "No Activities were started for result for " + this);
                            return;
                        }
                        String str2 = pollFirst.mWho;
                        int i = pollFirst.mRequestCode;
                        Fragment findFragmentByWho = fragmentManagerImpl2.mFragmentStore.findFragmentByWho(str2);
                        if (findFragmentByWho == null) {
                            MotionLayout$$ExternalSyntheticOutline0.m("Activity result delivered for unknown Fragment ", str2, "FragmentManager");
                            return;
                        }
                        Objects.requireNonNull(activityResult2);
                        findFragmentByWho.onActivityResult(i, activityResult2.mResultCode, activityResult2.mData);
                    }
                });
                this.mStartIntentSenderForResult = activityResultRegistry.register(SupportMenuInflater$$ExternalSyntheticOutline0.m(m, "StartIntentSenderForResult"), new FragmentIntentSenderContract(), new ActivityResultCallback<ActivityResult>() { // from class: androidx.fragment.app.FragmentManager.8
                    @Override // androidx.activity.result.ActivityResultCallback
                    public final void onActivityResult(ActivityResult activityResult) {
                        ActivityResult activityResult2 = activityResult;
                        LaunchedFragmentInfo pollFirst = fragmentManagerImpl2.mLaunchedFragments.pollFirst();
                        if (pollFirst == null) {
                            Log.w("FragmentManager", "No IntentSenders were started for " + this);
                            return;
                        }
                        String str2 = pollFirst.mWho;
                        int i = pollFirst.mRequestCode;
                        Fragment findFragmentByWho = fragmentManagerImpl2.mFragmentStore.findFragmentByWho(str2);
                        if (findFragmentByWho == null) {
                            MotionLayout$$ExternalSyntheticOutline0.m("Intent Sender result delivered for unknown Fragment ", str2, "FragmentManager");
                            return;
                        }
                        Objects.requireNonNull(activityResult2);
                        findFragmentByWho.onActivityResult(i, activityResult2.mResultCode, activityResult2.mData);
                    }
                });
                this.mRequestPermissions = activityResultRegistry.register(SupportMenuInflater$$ExternalSyntheticOutline0.m(m, "RequestPermissions"), new ActivityResultContract<String[], Map<String, Boolean>>() { // from class: androidx.activity.result.contract.ActivityResultContracts$RequestMultiplePermissions
                    @Override // androidx.activity.result.contract.ActivityResultContract
                    public final Map<String, Boolean> parseResult(int i, Intent intent) {
                        boolean z2;
                        if (i != -1) {
                            return Collections.emptyMap();
                        }
                        if (intent == null) {
                            return Collections.emptyMap();
                        }
                        String[] stringArrayExtra = intent.getStringArrayExtra("androidx.activity.result.contract.extra.PERMISSIONS");
                        int[] intArrayExtra = intent.getIntArrayExtra("androidx.activity.result.contract.extra.PERMISSION_GRANT_RESULTS");
                        if (intArrayExtra == null || stringArrayExtra == null) {
                            return Collections.emptyMap();
                        }
                        HashMap hashMap = new HashMap();
                        int length = stringArrayExtra.length;
                        for (int i2 = 0; i2 < length; i2++) {
                            String str2 = stringArrayExtra[i2];
                            if (intArrayExtra[i2] == 0) {
                                z2 = true;
                            } else {
                                z2 = false;
                            }
                            hashMap.put(str2, Boolean.valueOf(z2));
                        }
                        return hashMap;
                    }
                }, new ActivityResultCallback<Map<String, Boolean>>() { // from class: androidx.fragment.app.FragmentManager.9
                    @Override // androidx.activity.result.ActivityResultCallback
                    @SuppressLint({"SyntheticAccessor"})
                    public final void onActivityResult(Map<String, Boolean> map) {
                        int i;
                        Map<String, Boolean> map2 = map;
                        String[] strArr = (String[]) map2.keySet().toArray(new String[0]);
                        ArrayList arrayList = new ArrayList(map2.values());
                        int[] iArr = new int[arrayList.size()];
                        for (int i2 = 0; i2 < arrayList.size(); i2++) {
                            if (((Boolean) arrayList.get(i2)).booleanValue()) {
                                i = 0;
                            } else {
                                i = -1;
                            }
                            iArr[i2] = i;
                        }
                        LaunchedFragmentInfo pollFirst = fragmentManagerImpl2.mLaunchedFragments.pollFirst();
                        if (pollFirst == null) {
                            Log.w("FragmentManager", "No permissions were requested for " + this);
                            return;
                        }
                        String str2 = pollFirst.mWho;
                        if (fragmentManagerImpl2.mFragmentStore.findFragmentByWho(str2) == null) {
                            MotionLayout$$ExternalSyntheticOutline0.m("Permission request result delivered for unknown Fragment ", str2, "FragmentManager");
                        }
                    }
                });
                return;
            }
            return;
        }
        throw new IllegalStateException("Already attached");
    }

    public final HashSet collectAllSpecialEffectsController() {
        HashSet hashSet = new HashSet();
        Iterator it = this.mFragmentStore.getActiveFragmentStateManagers().iterator();
        while (it.hasNext()) {
            FragmentStateManager fragmentStateManager = (FragmentStateManager) it.next();
            Objects.requireNonNull(fragmentStateManager);
            ViewGroup viewGroup = fragmentStateManager.mFragment.mContainer;
            if (viewGroup != null) {
                hashSet.add(SpecialEffectsController.getOrCreateController(viewGroup, getSpecialEffectsControllerFactory()));
            }
        }
        return hashSet;
    }

    public final FragmentStateManager createOrGetFragmentStateManager(Fragment fragment) {
        FragmentStore fragmentStore = this.mFragmentStore;
        String str = fragment.mWho;
        Objects.requireNonNull(fragmentStore);
        FragmentStateManager fragmentStateManager = fragmentStore.mActive.get(str);
        if (fragmentStateManager != null) {
            return fragmentStateManager;
        }
        FragmentStateManager fragmentStateManager2 = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, this.mFragmentStore, fragment);
        FragmentHostCallback<?> fragmentHostCallback = this.mHost;
        Objects.requireNonNull(fragmentHostCallback);
        fragmentStateManager2.restoreState(fragmentHostCallback.mContext.getClassLoader());
        fragmentStateManager2.mFragmentManagerState = this.mCurState;
        return fragmentStateManager2;
    }

    public final void dispatchConfigurationChanged(Configuration configuration) {
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            if (fragment != null) {
                fragment.onConfigurationChanged(configuration);
                fragment.mChildFragmentManager.dispatchConfigurationChanged(configuration);
            }
        }
    }

    public final boolean dispatchContextItemSelected() {
        boolean z;
        if (this.mCurState < 1) {
            return false;
        }
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            if (fragment != null) {
                if (!fragment.mHidden) {
                    z = fragment.mChildFragmentManager.dispatchContextItemSelected();
                } else {
                    z = false;
                }
                if (z) {
                    return true;
                }
            }
        }
        return false;
    }

    public final boolean dispatchCreateOptionsMenu() {
        boolean z;
        if (this.mCurState < 1) {
            return false;
        }
        ArrayList<Fragment> arrayList = null;
        boolean z2 = false;
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            if (fragment != null && isParentMenuVisible(fragment)) {
                if (!fragment.mHidden) {
                    z = fragment.mChildFragmentManager.dispatchCreateOptionsMenu() | false;
                } else {
                    z = false;
                }
                if (z) {
                    if (arrayList == null) {
                        arrayList = new ArrayList<>();
                    }
                    arrayList.add(fragment);
                    z2 = true;
                }
            }
        }
        if (this.mCreatedMenus != null) {
            for (int i = 0; i < this.mCreatedMenus.size(); i++) {
                Fragment fragment2 = this.mCreatedMenus.get(i);
                if (arrayList == null || !arrayList.contains(fragment2)) {
                    Objects.requireNonNull(fragment2);
                }
            }
        }
        this.mCreatedMenus = arrayList;
        return z2;
    }

    public final void dispatchLowMemory() {
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            if (fragment != null) {
                fragment.performLowMemory();
            }
        }
    }

    public final void dispatchMultiWindowModeChanged(boolean z) {
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            if (fragment != null) {
                fragment.performMultiWindowModeChanged(z);
            }
        }
    }

    public final boolean dispatchOptionsItemSelected() {
        boolean z;
        if (this.mCurState < 1) {
            return false;
        }
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            if (fragment != null) {
                if (!fragment.mHidden) {
                    z = fragment.mChildFragmentManager.dispatchOptionsItemSelected();
                } else {
                    z = false;
                }
                if (z) {
                    return true;
                }
            }
        }
        return false;
    }

    public final void dispatchOptionsMenuClosed() {
        if (this.mCurState >= 1) {
            for (Fragment fragment : this.mFragmentStore.getFragments()) {
                if (fragment != null && !fragment.mHidden) {
                    fragment.mChildFragmentManager.dispatchOptionsMenuClosed();
                }
            }
        }
    }

    public final void dispatchParentPrimaryNavigationFragmentChanged(Fragment fragment) {
        if (fragment != null && fragment.equals(findActiveFragment(fragment.mWho))) {
            Objects.requireNonNull(fragment.mFragmentManager);
            boolean isPrimaryNavigation = isPrimaryNavigation(fragment);
            Boolean bool = fragment.mIsPrimaryNavigationFragment;
            if (bool == null || bool.booleanValue() != isPrimaryNavigation) {
                fragment.mIsPrimaryNavigationFragment = Boolean.valueOf(isPrimaryNavigation);
                FragmentManagerImpl fragmentManagerImpl = fragment.mChildFragmentManager;
                Objects.requireNonNull(fragmentManagerImpl);
                fragmentManagerImpl.updateOnBackPressedCallbackEnabled();
                fragmentManagerImpl.dispatchParentPrimaryNavigationFragmentChanged(fragmentManagerImpl.mPrimaryNav);
            }
        }
    }

    public final void dispatchPictureInPictureModeChanged(boolean z) {
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            if (fragment != null) {
                fragment.performPictureInPictureModeChanged(z);
            }
        }
    }

    public final boolean dispatchPrepareOptionsMenu() {
        boolean z = false;
        if (this.mCurState < 1) {
            return false;
        }
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            if (fragment != null && isParentMenuVisible(fragment) && fragment.performPrepareOptionsMenu()) {
                z = true;
            }
        }
        return z;
    }

    public final void doPendingDeferredStart() {
        if (this.mHavePendingDeferredStart) {
            this.mHavePendingDeferredStart = false;
            startPendingDeferredFragments();
        }
    }

    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        int size;
        int size2;
        String m = SupportMenuInflater$$ExternalSyntheticOutline0.m(str, "    ");
        FragmentStore fragmentStore = this.mFragmentStore;
        Objects.requireNonNull(fragmentStore);
        String str2 = str + "    ";
        if (!fragmentStore.mActive.isEmpty()) {
            printWriter.print(str);
            printWriter.println("Active Fragments:");
            for (FragmentStateManager fragmentStateManager : fragmentStore.mActive.values()) {
                printWriter.print(str);
                if (fragmentStateManager != null) {
                    Fragment fragment = fragmentStateManager.mFragment;
                    printWriter.println(fragment);
                    fragment.dump(str2, fileDescriptor, printWriter, strArr);
                } else {
                    printWriter.println("null");
                }
            }
        }
        int size3 = fragmentStore.mAdded.size();
        if (size3 > 0) {
            printWriter.print(str);
            printWriter.println("Added Fragments:");
            for (int i = 0; i < size3; i++) {
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i);
                printWriter.print(": ");
                printWriter.println(fragmentStore.mAdded.get(i).toString());
            }
        }
        ArrayList<Fragment> arrayList = this.mCreatedMenus;
        if (arrayList != null && (size2 = arrayList.size()) > 0) {
            printWriter.print(str);
            printWriter.println("Fragments Created Menus:");
            for (int i2 = 0; i2 < size2; i2++) {
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i2);
                printWriter.print(": ");
                printWriter.println(this.mCreatedMenus.get(i2).toString());
            }
        }
        ArrayList<BackStackRecord> arrayList2 = this.mBackStack;
        if (arrayList2 != null && (size = arrayList2.size()) > 0) {
            printWriter.print(str);
            printWriter.println("Back Stack:");
            for (int i3 = 0; i3 < size; i3++) {
                BackStackRecord backStackRecord = this.mBackStack.get(i3);
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i3);
                printWriter.print(": ");
                printWriter.println(backStackRecord.toString());
                backStackRecord.dump(m, printWriter, true);
            }
        }
        printWriter.print(str);
        printWriter.println("Back Stack Index: " + this.mBackStackIndex.get());
        synchronized (this.mPendingActions) {
            int size4 = this.mPendingActions.size();
            if (size4 > 0) {
                printWriter.print(str);
                printWriter.println("Pending Actions:");
                for (int i4 = 0; i4 < size4; i4++) {
                    printWriter.print(str);
                    printWriter.print("  #");
                    printWriter.print(i4);
                    printWriter.print(": ");
                    printWriter.println((OpGenerator) this.mPendingActions.get(i4));
                }
            }
        }
        printWriter.print(str);
        printWriter.println("FragmentManager misc state:");
        printWriter.print(str);
        printWriter.print("  mHost=");
        printWriter.println(this.mHost);
        printWriter.print(str);
        printWriter.print("  mContainer=");
        printWriter.println(this.mContainer);
        if (this.mParent != null) {
            printWriter.print(str);
            printWriter.print("  mParent=");
            printWriter.println(this.mParent);
        }
        printWriter.print(str);
        printWriter.print("  mCurState=");
        printWriter.print(this.mCurState);
        printWriter.print(" mStateSaved=");
        printWriter.print(this.mStateSaved);
        printWriter.print(" mStopped=");
        printWriter.print(this.mStopped);
        printWriter.print(" mDestroyed=");
        printWriter.println(this.mDestroyed);
        if (this.mNeedMenuInvalidate) {
            printWriter.print(str);
            printWriter.print("  mNeedMenuInvalidate=");
            printWriter.println(this.mNeedMenuInvalidate);
        }
    }

    public final void enqueueAction(OpGenerator opGenerator, boolean z) {
        boolean z2;
        if (!z) {
            if (this.mHost != null) {
                if (this.mStateSaved || this.mStopped) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (z2) {
                    throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
                }
            } else if (this.mDestroyed) {
                throw new IllegalStateException("FragmentManager has been destroyed");
            } else {
                throw new IllegalStateException("FragmentManager has not been attached to a host.");
            }
        }
        synchronized (this.mPendingActions) {
            if (this.mHost != null) {
                this.mPendingActions.add(opGenerator);
                scheduleCommit();
            } else if (!z) {
                throw new IllegalStateException("Activity has been destroyed");
            }
        }
    }

    public final void ensureExecReady(boolean z) {
        boolean z2;
        if (this.mExecutingActions) {
            throw new IllegalStateException("FragmentManager is already executing transactions");
        } else if (this.mHost != null) {
            Looper myLooper = Looper.myLooper();
            FragmentHostCallback<?> fragmentHostCallback = this.mHost;
            Objects.requireNonNull(fragmentHostCallback);
            if (myLooper == fragmentHostCallback.mHandler.getLooper()) {
                if (!z) {
                    if (this.mStateSaved || this.mStopped) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    if (z2) {
                        throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
                    }
                }
                if (this.mTmpRecords == null) {
                    this.mTmpRecords = new ArrayList<>();
                    this.mTmpIsPop = new ArrayList<>();
                    return;
                }
                return;
            }
            throw new IllegalStateException("Must be called from main thread of fragment host");
        } else if (this.mDestroyed) {
            throw new IllegalStateException("FragmentManager has been destroyed");
        } else {
            throw new IllegalStateException("FragmentManager has not been attached to a host.");
        }
    }

    /* JADX WARN: Finally extract failed */
    public final void execSingleAction(BackStackRecord backStackRecord, boolean z) {
        if (!z || (this.mHost != null && !this.mDestroyed)) {
            ensureExecReady(z);
            backStackRecord.generateOps(this.mTmpRecords, this.mTmpIsPop);
            this.mExecutingActions = true;
            try {
                removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
                cleanupExec();
                updateOnBackPressedCallbackEnabled();
                doPendingDeferredStart();
                FragmentStore fragmentStore = this.mFragmentStore;
                Objects.requireNonNull(fragmentStore);
                fragmentStore.mActive.values().removeAll(Collections.singleton(null));
            } catch (Throwable th) {
                cleanupExec();
                throw th;
            }
        }
    }

    public final void executeOpsTogether(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int i, int i2) {
        ArrayList<BackStackRecord> arrayList3;
        int i3;
        ViewGroup viewGroup;
        boolean z;
        Fragment fragment;
        int i4;
        int i5;
        int i6;
        ArrayList<BackStackRecord> arrayList4 = arrayList;
        ArrayList<Boolean> arrayList5 = arrayList2;
        int i7 = i2;
        boolean z2 = arrayList4.get(i).mReorderingAllowed;
        ArrayList<Fragment> arrayList6 = this.mTmpAddedFragments;
        if (arrayList6 == null) {
            this.mTmpAddedFragments = new ArrayList<>();
        } else {
            arrayList6.clear();
        }
        this.mTmpAddedFragments.addAll(this.mFragmentStore.getFragments());
        Fragment fragment2 = this.mPrimaryNav;
        boolean z3 = false;
        int i8 = i;
        while (true) {
            int i9 = 1;
            if (i8 < i7) {
                BackStackRecord backStackRecord = arrayList4.get(i8);
                int i10 = 3;
                if (!arrayList5.get(i8).booleanValue()) {
                    ArrayList<Fragment> arrayList7 = this.mTmpAddedFragments;
                    Objects.requireNonNull(backStackRecord);
                    int i11 = 0;
                    while (i11 < backStackRecord.mOps.size()) {
                        FragmentTransaction.Op op = backStackRecord.mOps.get(i11);
                        int i12 = op.mCmd;
                        if (i12 != i9) {
                            if (i12 == 2) {
                                Fragment fragment3 = op.mFragment;
                                int i13 = fragment3.mContainerId;
                                int size = arrayList7.size() - 1;
                                boolean z4 = false;
                                while (size >= 0) {
                                    Fragment fragment4 = arrayList7.get(size);
                                    if (fragment4.mContainerId == i13) {
                                        if (fragment4 == fragment3) {
                                            z4 = true;
                                        } else {
                                            if (fragment4 == fragment2) {
                                                i5 = i13;
                                                i6 = 0;
                                                backStackRecord.mOps.add(i11, new FragmentTransaction.Op(9, fragment4, 0));
                                                i11++;
                                                fragment2 = null;
                                            } else {
                                                i5 = i13;
                                                i6 = 0;
                                            }
                                            FragmentTransaction.Op op2 = new FragmentTransaction.Op(3, fragment4, i6);
                                            op2.mEnterAnim = op.mEnterAnim;
                                            op2.mPopEnterAnim = op.mPopEnterAnim;
                                            op2.mExitAnim = op.mExitAnim;
                                            op2.mPopExitAnim = op.mPopExitAnim;
                                            backStackRecord.mOps.add(i11, op2);
                                            arrayList7.remove(fragment4);
                                            i11++;
                                            size--;
                                            i13 = i5;
                                        }
                                    }
                                    i5 = i13;
                                    size--;
                                    i13 = i5;
                                }
                                if (z4) {
                                    backStackRecord.mOps.remove(i11);
                                    i11--;
                                } else {
                                    i4 = 1;
                                    op.mCmd = 1;
                                    op.mFromExpandedOp = true;
                                    arrayList7.add(fragment3);
                                    i9 = i4;
                                    i11 += i9;
                                    i10 = 3;
                                }
                            } else if (i12 == i10 || i12 == 6) {
                                arrayList7.remove(op.mFragment);
                                Fragment fragment5 = op.mFragment;
                                if (fragment5 == fragment2) {
                                    backStackRecord.mOps.add(i11, new FragmentTransaction.Op(9, fragment5));
                                    i11++;
                                    fragment2 = null;
                                    i9 = 1;
                                    i11 += i9;
                                    i10 = 3;
                                }
                            } else if (i12 == 7) {
                                i9 = 1;
                            } else if (i12 == 8) {
                                backStackRecord.mOps.add(i11, new FragmentTransaction.Op(9, fragment2, 0));
                                op.mFromExpandedOp = true;
                                i11++;
                                fragment2 = op.mFragment;
                            }
                            i4 = 1;
                            i9 = i4;
                            i11 += i9;
                            i10 = 3;
                        }
                        arrayList7.add(op.mFragment);
                        i11 += i9;
                        i10 = 3;
                    }
                } else {
                    ArrayList<Fragment> arrayList8 = this.mTmpAddedFragments;
                    Objects.requireNonNull(backStackRecord);
                    int size2 = backStackRecord.mOps.size() - 1;
                    while (size2 >= 0) {
                        FragmentTransaction.Op op3 = backStackRecord.mOps.get(size2);
                        int i14 = op3.mCmd;
                        if (i14 != i9) {
                            if (i14 != 3) {
                                switch (i14) {
                                    case 8:
                                        fragment = null;
                                        fragment2 = fragment;
                                        break;
                                    case 9:
                                        fragment = op3.mFragment;
                                        fragment2 = fragment;
                                        break;
                                    case 10:
                                        op3.mCurrentMaxState = op3.mOldMaxState;
                                        break;
                                }
                                size2--;
                                i9 = 1;
                            }
                            arrayList8.add(op3.mFragment);
                            size2--;
                            i9 = 1;
                        }
                        arrayList8.remove(op3.mFragment);
                        size2--;
                        i9 = 1;
                    }
                }
                if (z3 || backStackRecord.mAddToBackStack) {
                    z = true;
                } else {
                    z = false;
                }
                z3 = z;
                i8++;
                arrayList4 = arrayList;
                arrayList5 = arrayList2;
                i7 = i2;
            } else {
                this.mTmpAddedFragments.clear();
                if (z2 || this.mCurState < 1) {
                    arrayList3 = arrayList;
                    i3 = i2;
                } else {
                    int i15 = i;
                    i3 = i2;
                    while (true) {
                        arrayList3 = arrayList;
                        if (i15 < i3) {
                            Iterator<FragmentTransaction.Op> it = arrayList3.get(i15).mOps.iterator();
                            while (it.hasNext()) {
                                Fragment fragment6 = it.next().mFragment;
                                if (!(fragment6 == null || fragment6.mFragmentManager == null)) {
                                    this.mFragmentStore.makeActive(createOrGetFragmentStateManager(fragment6));
                                }
                            }
                            i15++;
                        }
                    }
                }
                for (int i16 = i; i16 < i3; i16++) {
                    BackStackRecord backStackRecord2 = arrayList3.get(i16);
                    if (arrayList2.get(i16).booleanValue()) {
                        backStackRecord2.bumpBackStackNesting(-1);
                        for (int size3 = backStackRecord2.mOps.size() - 1; size3 >= 0; size3--) {
                            FragmentTransaction.Op op4 = backStackRecord2.mOps.get(size3);
                            Fragment fragment7 = op4.mFragment;
                            if (fragment7 != null) {
                                if (fragment7.mAnimationInfo != null) {
                                    fragment7.ensureAnimationInfo().mIsPop = true;
                                }
                                int i17 = backStackRecord2.mTransition;
                                int i18 = 4100;
                                if (i17 == 4097) {
                                    i18 = 8194;
                                } else if (i17 == 8194) {
                                    i18 = 4097;
                                } else if (i17 != 8197) {
                                    if (i17 == 4099) {
                                        i18 = 4099;
                                    } else if (i17 != 4100) {
                                        i18 = 0;
                                    } else {
                                        i18 = 8197;
                                    }
                                }
                                if (!(fragment7.mAnimationInfo == null && i18 == 0)) {
                                    fragment7.ensureAnimationInfo();
                                    fragment7.mAnimationInfo.mNextTransition = i18;
                                }
                                ArrayList<String> arrayList9 = backStackRecord2.mSharedElementTargetNames;
                                ArrayList<String> arrayList10 = backStackRecord2.mSharedElementSourceNames;
                                fragment7.ensureAnimationInfo();
                                Fragment.AnimationInfo animationInfo = fragment7.mAnimationInfo;
                                animationInfo.mSharedElementSourceNames = arrayList9;
                                animationInfo.mSharedElementTargetNames = arrayList10;
                            }
                            switch (op4.mCmd) {
                                case 1:
                                    fragment7.setAnimations(op4.mEnterAnim, op4.mExitAnim, op4.mPopEnterAnim, op4.mPopExitAnim);
                                    backStackRecord2.mManager.setExitAnimationOrder(fragment7, true);
                                    backStackRecord2.mManager.removeFragment(fragment7);
                                    break;
                                case 2:
                                default:
                                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Unknown cmd: ");
                                    m.append(op4.mCmd);
                                    throw new IllegalArgumentException(m.toString());
                                case 3:
                                    fragment7.setAnimations(op4.mEnterAnim, op4.mExitAnim, op4.mPopEnterAnim, op4.mPopExitAnim);
                                    backStackRecord2.mManager.addFragment(fragment7);
                                    break;
                                case 4:
                                    fragment7.setAnimations(op4.mEnterAnim, op4.mExitAnim, op4.mPopEnterAnim, op4.mPopExitAnim);
                                    Objects.requireNonNull(backStackRecord2.mManager);
                                    showFragment(fragment7);
                                    break;
                                case 5:
                                    fragment7.setAnimations(op4.mEnterAnim, op4.mExitAnim, op4.mPopEnterAnim, op4.mPopExitAnim);
                                    backStackRecord2.mManager.setExitAnimationOrder(fragment7, true);
                                    backStackRecord2.mManager.hideFragment(fragment7);
                                    break;
                                case FalsingManager.VERSION /* 6 */:
                                    fragment7.setAnimations(op4.mEnterAnim, op4.mExitAnim, op4.mPopEnterAnim, op4.mPopExitAnim);
                                    backStackRecord2.mManager.attachFragment(fragment7);
                                    break;
                                case 7:
                                    fragment7.setAnimations(op4.mEnterAnim, op4.mExitAnim, op4.mPopEnterAnim, op4.mPopExitAnim);
                                    backStackRecord2.mManager.setExitAnimationOrder(fragment7, true);
                                    backStackRecord2.mManager.detachFragment(fragment7);
                                    break;
                                case 8:
                                    backStackRecord2.mManager.setPrimaryNavigationFragment(null);
                                    break;
                                case 9:
                                    backStackRecord2.mManager.setPrimaryNavigationFragment(fragment7);
                                    break;
                                case 10:
                                    backStackRecord2.mManager.setMaxLifecycle(fragment7, op4.mOldMaxState);
                                    break;
                            }
                        }
                        continue;
                    } else {
                        backStackRecord2.bumpBackStackNesting(1);
                        int size4 = backStackRecord2.mOps.size();
                        for (int i19 = 0; i19 < size4; i19++) {
                            FragmentTransaction.Op op5 = backStackRecord2.mOps.get(i19);
                            Fragment fragment8 = op5.mFragment;
                            if (fragment8 != null) {
                                if (fragment8.mAnimationInfo != null) {
                                    fragment8.ensureAnimationInfo().mIsPop = false;
                                }
                                int i20 = backStackRecord2.mTransition;
                                if (!(fragment8.mAnimationInfo == null && i20 == 0)) {
                                    fragment8.ensureAnimationInfo();
                                    fragment8.mAnimationInfo.mNextTransition = i20;
                                }
                                ArrayList<String> arrayList11 = backStackRecord2.mSharedElementSourceNames;
                                ArrayList<String> arrayList12 = backStackRecord2.mSharedElementTargetNames;
                                fragment8.ensureAnimationInfo();
                                Fragment.AnimationInfo animationInfo2 = fragment8.mAnimationInfo;
                                animationInfo2.mSharedElementSourceNames = arrayList11;
                                animationInfo2.mSharedElementTargetNames = arrayList12;
                            }
                            switch (op5.mCmd) {
                                case 1:
                                    fragment8.setAnimations(op5.mEnterAnim, op5.mExitAnim, op5.mPopEnterAnim, op5.mPopExitAnim);
                                    backStackRecord2.mManager.setExitAnimationOrder(fragment8, false);
                                    backStackRecord2.mManager.addFragment(fragment8);
                                    break;
                                case 2:
                                default:
                                    StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Unknown cmd: ");
                                    m2.append(op5.mCmd);
                                    throw new IllegalArgumentException(m2.toString());
                                case 3:
                                    fragment8.setAnimations(op5.mEnterAnim, op5.mExitAnim, op5.mPopEnterAnim, op5.mPopExitAnim);
                                    backStackRecord2.mManager.removeFragment(fragment8);
                                    break;
                                case 4:
                                    fragment8.setAnimations(op5.mEnterAnim, op5.mExitAnim, op5.mPopEnterAnim, op5.mPopExitAnim);
                                    backStackRecord2.mManager.hideFragment(fragment8);
                                    break;
                                case 5:
                                    fragment8.setAnimations(op5.mEnterAnim, op5.mExitAnim, op5.mPopEnterAnim, op5.mPopExitAnim);
                                    backStackRecord2.mManager.setExitAnimationOrder(fragment8, false);
                                    Objects.requireNonNull(backStackRecord2.mManager);
                                    showFragment(fragment8);
                                    break;
                                case FalsingManager.VERSION /* 6 */:
                                    fragment8.setAnimations(op5.mEnterAnim, op5.mExitAnim, op5.mPopEnterAnim, op5.mPopExitAnim);
                                    backStackRecord2.mManager.detachFragment(fragment8);
                                    break;
                                case 7:
                                    fragment8.setAnimations(op5.mEnterAnim, op5.mExitAnim, op5.mPopEnterAnim, op5.mPopExitAnim);
                                    backStackRecord2.mManager.setExitAnimationOrder(fragment8, false);
                                    backStackRecord2.mManager.attachFragment(fragment8);
                                    break;
                                case 8:
                                    backStackRecord2.mManager.setPrimaryNavigationFragment(fragment8);
                                    break;
                                case 9:
                                    backStackRecord2.mManager.setPrimaryNavigationFragment(null);
                                    break;
                                case 10:
                                    backStackRecord2.mManager.setMaxLifecycle(fragment8, op5.mCurrentMaxState);
                                    break;
                            }
                        }
                        continue;
                    }
                }
                boolean booleanValue = arrayList2.get(i3 - 1).booleanValue();
                for (int i21 = i; i21 < i3; i21++) {
                    BackStackRecord backStackRecord3 = arrayList3.get(i21);
                    if (booleanValue) {
                        for (int size5 = backStackRecord3.mOps.size() - 1; size5 >= 0; size5--) {
                            Fragment fragment9 = backStackRecord3.mOps.get(size5).mFragment;
                            if (fragment9 != null) {
                                createOrGetFragmentStateManager(fragment9).moveToExpectedState();
                            }
                        }
                    } else {
                        Iterator<FragmentTransaction.Op> it2 = backStackRecord3.mOps.iterator();
                        while (it2.hasNext()) {
                            Fragment fragment10 = it2.next().mFragment;
                            if (fragment10 != null) {
                                createOrGetFragmentStateManager(fragment10).moveToExpectedState();
                            }
                        }
                    }
                }
                moveToState(this.mCurState, true);
                HashSet hashSet = new HashSet();
                for (int i22 = i; i22 < i3; i22++) {
                    Iterator<FragmentTransaction.Op> it3 = arrayList3.get(i22).mOps.iterator();
                    while (it3.hasNext()) {
                        Fragment fragment11 = it3.next().mFragment;
                        if (!(fragment11 == null || (viewGroup = fragment11.mContainer) == null)) {
                            hashSet.add(SpecialEffectsController.getOrCreateController(viewGroup, getSpecialEffectsControllerFactory()));
                        }
                    }
                }
                Iterator it4 = hashSet.iterator();
                while (it4.hasNext()) {
                    SpecialEffectsController specialEffectsController = (SpecialEffectsController) it4.next();
                    Objects.requireNonNull(specialEffectsController);
                    specialEffectsController.mOperationDirectionIsPop = booleanValue;
                    specialEffectsController.markPostponedState();
                    specialEffectsController.executePendingOperations();
                }
                for (int i23 = i; i23 < i3; i23++) {
                    BackStackRecord backStackRecord4 = arrayList3.get(i23);
                    if (arrayList2.get(i23).booleanValue() && backStackRecord4.mIndex >= 0) {
                        backStackRecord4.mIndex = -1;
                    }
                    Objects.requireNonNull(backStackRecord4);
                }
                return;
            }
        }
    }

    public final Fragment findActiveFragment(String str) {
        return this.mFragmentStore.findActiveFragment(str);
    }

    public final Fragment findFragmentById(int i) {
        FragmentStore fragmentStore = this.mFragmentStore;
        Objects.requireNonNull(fragmentStore);
        int size = fragmentStore.mAdded.size();
        while (true) {
            size--;
            if (size >= 0) {
                Fragment fragment = fragmentStore.mAdded.get(size);
                if (fragment != null && fragment.mFragmentId == i) {
                    return fragment;
                }
            } else {
                for (FragmentStateManager fragmentStateManager : fragmentStore.mActive.values()) {
                    if (fragmentStateManager != null) {
                        Fragment fragment2 = fragmentStateManager.mFragment;
                        if (fragment2.mFragmentId == i) {
                            return fragment2;
                        }
                    }
                }
                return null;
            }
        }
    }

    public final Fragment findFragmentByTag(String str) {
        FragmentStore fragmentStore = this.mFragmentStore;
        int size = fragmentStore.mAdded.size();
        while (true) {
            size--;
            if (size >= 0) {
                Fragment fragment = fragmentStore.mAdded.get(size);
                if (fragment != null && str.equals(fragment.mTag)) {
                    return fragment;
                }
            } else {
                for (FragmentStateManager fragmentStateManager : fragmentStore.mActive.values()) {
                    if (fragmentStateManager != null) {
                        Fragment fragment2 = fragmentStateManager.mFragment;
                        if (str.equals(fragment2.mTag)) {
                            return fragment2;
                        }
                    }
                }
                return null;
            }
        }
    }

    public final ViewGroup getFragmentContainer(Fragment fragment) {
        ViewGroup viewGroup = fragment.mContainer;
        if (viewGroup != null) {
            return viewGroup;
        }
        if (fragment.mContainerId > 0 && this.mContainer.onHasView()) {
            View onFindViewById = this.mContainer.onFindViewById(fragment.mContainerId);
            if (onFindViewById instanceof ViewGroup) {
                return (ViewGroup) onFindViewById;
            }
        }
        return null;
    }

    public final FragmentFactory getFragmentFactory() {
        Fragment fragment = this.mParent;
        if (fragment != null) {
            return fragment.mFragmentManager.getFragmentFactory();
        }
        return this.mHostFragmentFactory;
    }

    public final SpecialEffectsControllerFactory getSpecialEffectsControllerFactory() {
        Fragment fragment = this.mParent;
        if (fragment != null) {
            return fragment.mFragmentManager.getSpecialEffectsControllerFactory();
        }
        return this.mDefaultSpecialEffectsControllerFactory;
    }

    public final void moveToState(int i, boolean z) {
        FragmentHostCallback<?> fragmentHostCallback;
        boolean z2;
        if (this.mHost == null && i != -1) {
            throw new IllegalStateException("No activity");
        } else if (z || i != this.mCurState) {
            this.mCurState = i;
            FragmentStore fragmentStore = this.mFragmentStore;
            Objects.requireNonNull(fragmentStore);
            Iterator<Fragment> it = fragmentStore.mAdded.iterator();
            while (it.hasNext()) {
                FragmentStateManager fragmentStateManager = fragmentStore.mActive.get(it.next().mWho);
                if (fragmentStateManager != null) {
                    fragmentStateManager.moveToExpectedState();
                }
            }
            Iterator<FragmentStateManager> it2 = fragmentStore.mActive.values().iterator();
            while (true) {
                boolean z3 = false;
                if (!it2.hasNext()) {
                    break;
                }
                FragmentStateManager next = it2.next();
                if (next != null) {
                    next.moveToExpectedState();
                    Fragment fragment = next.mFragment;
                    if (fragment.mRemoving) {
                        if (fragment.mBackStackNesting > 0) {
                            z2 = true;
                        } else {
                            z2 = false;
                        }
                        if (!z2) {
                            z3 = true;
                        }
                    }
                    if (z3) {
                        fragmentStore.makeInactive(next);
                    }
                }
            }
            startPendingDeferredFragments();
            if (this.mNeedMenuInvalidate && (fragmentHostCallback = this.mHost) != null && this.mCurState == 7) {
                fragmentHostCallback.onSupportInvalidateOptionsMenu();
                this.mNeedMenuInvalidate = false;
            }
        }
    }

    public final void noteStateNotSaved() {
        if (this.mHost != null) {
            this.mStateSaved = false;
            this.mStopped = false;
            FragmentManagerViewModel fragmentManagerViewModel = this.mNonConfig;
            Objects.requireNonNull(fragmentManagerViewModel);
            fragmentManagerViewModel.mIsStateSaved = false;
            for (Fragment fragment : this.mFragmentStore.getFragments()) {
                if (fragment != null) {
                    fragment.mChildFragmentManager.noteStateNotSaved();
                }
            }
        }
    }

    public final void restoreSaveStateInternal(Parcelable parcelable) {
        FragmentManagerState fragmentManagerState;
        ArrayList<FragmentState> arrayList;
        int i;
        boolean z;
        FragmentStateManager fragmentStateManager;
        if (!(parcelable == null || (arrayList = (fragmentManagerState = (FragmentManagerState) parcelable).mSavedState) == null)) {
            FragmentStore fragmentStore = this.mFragmentStore;
            Objects.requireNonNull(fragmentStore);
            fragmentStore.mSavedState.clear();
            Iterator<FragmentState> it = arrayList.iterator();
            while (it.hasNext()) {
                FragmentState next = it.next();
                fragmentStore.mSavedState.put(next.mWho, next);
            }
            FragmentStore fragmentStore2 = this.mFragmentStore;
            Objects.requireNonNull(fragmentStore2);
            fragmentStore2.mActive.clear();
            Iterator<String> it2 = fragmentManagerState.mActive.iterator();
            while (it2.hasNext()) {
                FragmentState savedState = this.mFragmentStore.setSavedState(it2.next(), null);
                if (savedState != null) {
                    FragmentManagerViewModel fragmentManagerViewModel = this.mNonConfig;
                    String str = savedState.mWho;
                    Objects.requireNonNull(fragmentManagerViewModel);
                    Fragment fragment = fragmentManagerViewModel.mRetainedFragments.get(str);
                    if (fragment != null) {
                        if (isLoggingEnabled(2)) {
                            Log.v("FragmentManager", "restoreSaveState: re-attaching retained " + fragment);
                        }
                        fragmentStateManager = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, this.mFragmentStore, fragment, savedState);
                    } else {
                        FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher = this.mLifecycleCallbacksDispatcher;
                        FragmentStore fragmentStore3 = this.mFragmentStore;
                        FragmentHostCallback<?> fragmentHostCallback = this.mHost;
                        Objects.requireNonNull(fragmentHostCallback);
                        fragmentStateManager = new FragmentStateManager(fragmentLifecycleCallbacksDispatcher, fragmentStore3, fragmentHostCallback.mContext.getClassLoader(), getFragmentFactory(), savedState);
                    }
                    Fragment fragment2 = fragmentStateManager.mFragment;
                    fragment2.mFragmentManager = this;
                    if (isLoggingEnabled(2)) {
                        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("restoreSaveState: active (");
                        m.append(fragment2.mWho);
                        m.append("): ");
                        m.append(fragment2);
                        Log.v("FragmentManager", m.toString());
                    }
                    FragmentHostCallback<?> fragmentHostCallback2 = this.mHost;
                    Objects.requireNonNull(fragmentHostCallback2);
                    fragmentStateManager.restoreState(fragmentHostCallback2.mContext.getClassLoader());
                    this.mFragmentStore.makeActive(fragmentStateManager);
                    fragmentStateManager.mFragmentManagerState = this.mCurState;
                }
            }
            FragmentManagerViewModel fragmentManagerViewModel2 = this.mNonConfig;
            Objects.requireNonNull(fragmentManagerViewModel2);
            Iterator it3 = new ArrayList(fragmentManagerViewModel2.mRetainedFragments.values()).iterator();
            while (true) {
                i = 0;
                if (!it3.hasNext()) {
                    break;
                }
                Fragment fragment3 = (Fragment) it3.next();
                FragmentStore fragmentStore4 = this.mFragmentStore;
                String str2 = fragment3.mWho;
                Objects.requireNonNull(fragmentStore4);
                if (fragmentStore4.mActive.get(str2) != null) {
                    i = 1;
                }
                if (i == 0) {
                    if (isLoggingEnabled(2)) {
                        Log.v("FragmentManager", "Discarding retained Fragment " + fragment3 + " that was not found in the set of active Fragments " + fragmentManagerState.mActive);
                    }
                    this.mNonConfig.removeRetainedFragment(fragment3);
                    fragment3.mFragmentManager = this;
                    FragmentStateManager fragmentStateManager2 = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, this.mFragmentStore, fragment3);
                    fragmentStateManager2.mFragmentManagerState = 1;
                    fragmentStateManager2.moveToExpectedState();
                    fragment3.mRemoving = true;
                    fragmentStateManager2.moveToExpectedState();
                }
            }
            FragmentStore fragmentStore5 = this.mFragmentStore;
            ArrayList<String> arrayList2 = fragmentManagerState.mAdded;
            Objects.requireNonNull(fragmentStore5);
            fragmentStore5.mAdded.clear();
            if (arrayList2 != null) {
                for (String str3 : arrayList2) {
                    Fragment findActiveFragment = fragmentStore5.findActiveFragment(str3);
                    if (findActiveFragment != null) {
                        if (isLoggingEnabled(2)) {
                            Log.v("FragmentManager", "restoreSaveState: added (" + str3 + "): " + findActiveFragment);
                        }
                        fragmentStore5.addFragment(findActiveFragment);
                    } else {
                        throw new IllegalStateException(AbstractResolvableFuture$$ExternalSyntheticOutline0.m("No instantiated fragment for (", str3, ")"));
                    }
                }
            }
            if (fragmentManagerState.mBackStack != null) {
                this.mBackStack = new ArrayList<>(fragmentManagerState.mBackStack.length);
                int i2 = 0;
                while (true) {
                    BackStackRecordState[] backStackRecordStateArr = fragmentManagerState.mBackStack;
                    if (i2 >= backStackRecordStateArr.length) {
                        break;
                    }
                    BackStackRecordState backStackRecordState = backStackRecordStateArr[i2];
                    Objects.requireNonNull(backStackRecordState);
                    BackStackRecord backStackRecord = new BackStackRecord(this);
                    int i3 = 0;
                    int i4 = 0;
                    while (true) {
                        int[] iArr = backStackRecordState.mOps;
                        if (i3 >= iArr.length) {
                            break;
                        }
                        FragmentTransaction.Op op = new FragmentTransaction.Op();
                        int i5 = i3 + 1;
                        op.mCmd = iArr[i3];
                        if (isLoggingEnabled(2)) {
                            Log.v("FragmentManager", "Instantiate " + backStackRecord + " op #" + i4 + " base fragment #" + backStackRecordState.mOps[i5]);
                        }
                        op.mOldMaxState = Lifecycle.State.values()[backStackRecordState.mOldMaxLifecycleStates[i4]];
                        op.mCurrentMaxState = Lifecycle.State.values()[backStackRecordState.mCurrentMaxLifecycleStates[i4]];
                        int[] iArr2 = backStackRecordState.mOps;
                        int i6 = i5 + 1;
                        if (iArr2[i5] != 0) {
                            z = true;
                        } else {
                            z = false;
                        }
                        op.mFromExpandedOp = z;
                        int i7 = i6 + 1;
                        int i8 = iArr2[i6];
                        op.mEnterAnim = i8;
                        int i9 = i7 + 1;
                        int i10 = iArr2[i7];
                        op.mExitAnim = i10;
                        int i11 = i9 + 1;
                        int i12 = iArr2[i9];
                        op.mPopEnterAnim = i12;
                        int i13 = iArr2[i11];
                        op.mPopExitAnim = i13;
                        backStackRecord.mEnterAnim = i8;
                        backStackRecord.mExitAnim = i10;
                        backStackRecord.mPopEnterAnim = i12;
                        backStackRecord.mPopExitAnim = i13;
                        backStackRecord.addOp(op);
                        i4++;
                        i3 = i11 + 1;
                    }
                    backStackRecord.mTransition = backStackRecordState.mTransition;
                    backStackRecord.mName = backStackRecordState.mName;
                    backStackRecord.mAddToBackStack = true;
                    backStackRecord.mBreadCrumbTitleRes = backStackRecordState.mBreadCrumbTitleRes;
                    backStackRecord.mBreadCrumbTitleText = backStackRecordState.mBreadCrumbTitleText;
                    backStackRecord.mBreadCrumbShortTitleRes = backStackRecordState.mBreadCrumbShortTitleRes;
                    backStackRecord.mBreadCrumbShortTitleText = backStackRecordState.mBreadCrumbShortTitleText;
                    backStackRecord.mSharedElementSourceNames = backStackRecordState.mSharedElementSourceNames;
                    backStackRecord.mSharedElementTargetNames = backStackRecordState.mSharedElementTargetNames;
                    backStackRecord.mReorderingAllowed = backStackRecordState.mReorderingAllowed;
                    backStackRecord.mIndex = backStackRecordState.mIndex;
                    for (int i14 = 0; i14 < backStackRecordState.mFragmentWhos.size(); i14++) {
                        String str4 = backStackRecordState.mFragmentWhos.get(i14);
                        if (str4 != null) {
                            backStackRecord.mOps.get(i14).mFragment = findActiveFragment(str4);
                        }
                    }
                    backStackRecord.bumpBackStackNesting(1);
                    if (isLoggingEnabled(2)) {
                        StringBuilder m2 = ExifInterface$$ExternalSyntheticOutline0.m("restoreAllState: back stack #", i2, " (index ");
                        m2.append(backStackRecord.mIndex);
                        m2.append("): ");
                        m2.append(backStackRecord);
                        Log.v("FragmentManager", m2.toString());
                        PrintWriter printWriter = new PrintWriter(new LogWriter());
                        backStackRecord.dump("  ", printWriter, false);
                        printWriter.close();
                    }
                    this.mBackStack.add(backStackRecord);
                    i2++;
                }
            } else {
                this.mBackStack = null;
            }
            this.mBackStackIndex.set(fragmentManagerState.mBackStackIndex);
            String str5 = fragmentManagerState.mPrimaryNavActiveWho;
            if (str5 != null) {
                Fragment findActiveFragment2 = findActiveFragment(str5);
                this.mPrimaryNav = findActiveFragment2;
                dispatchParentPrimaryNavigationFragmentChanged(findActiveFragment2);
            }
            ArrayList<String> arrayList3 = fragmentManagerState.mBackStackStateKeys;
            if (arrayList3 != null) {
                for (int i15 = 0; i15 < arrayList3.size(); i15++) {
                    this.mBackStackStates.put(arrayList3.get(i15), fragmentManagerState.mBackStackStates.get(i15));
                }
            }
            ArrayList<String> arrayList4 = fragmentManagerState.mResultKeys;
            if (arrayList4 != null) {
                while (i < arrayList4.size()) {
                    Bundle bundle = fragmentManagerState.mResults.get(i);
                    FragmentHostCallback<?> fragmentHostCallback3 = this.mHost;
                    Objects.requireNonNull(fragmentHostCallback3);
                    bundle.setClassLoader(fragmentHostCallback3.mContext.getClassLoader());
                    this.mResults.put(arrayList4.get(i), bundle);
                    i++;
                }
            }
            this.mLaunchedFragments = new ArrayDeque<>(fragmentManagerState.mLaunchedFragments);
        }
    }

    public final void scheduleCommit() {
        synchronized (this.mPendingActions) {
            boolean z = true;
            if (this.mPendingActions.size() != 1) {
                z = false;
            }
            if (z) {
                FragmentHostCallback<?> fragmentHostCallback = this.mHost;
                Objects.requireNonNull(fragmentHostCallback);
                fragmentHostCallback.mHandler.removeCallbacks(this.mExecCommit);
                FragmentHostCallback<?> fragmentHostCallback2 = this.mHost;
                Objects.requireNonNull(fragmentHostCallback2);
                fragmentHostCallback2.mHandler.post(this.mExecCommit);
                updateOnBackPressedCallbackEnabled();
            }
        }
    }

    public final void setMaxLifecycle(Fragment fragment, Lifecycle.State state) {
        if (!fragment.equals(findActiveFragment(fragment.mWho)) || !(fragment.mHost == null || fragment.mFragmentManager == this)) {
            throw new IllegalArgumentException("Fragment " + fragment + " is not an active fragment of FragmentManager " + this);
        }
        fragment.mMaxState = state;
    }

    public final void setPrimaryNavigationFragment(Fragment fragment) {
        if (fragment == null || (fragment.equals(findActiveFragment(fragment.mWho)) && (fragment.mHost == null || fragment.mFragmentManager == this))) {
            Fragment fragment2 = this.mPrimaryNav;
            this.mPrimaryNav = fragment;
            dispatchParentPrimaryNavigationFragmentChanged(fragment2);
            dispatchParentPrimaryNavigationFragmentChanged(this.mPrimaryNav);
            return;
        }
        throw new IllegalArgumentException("Fragment " + fragment + " is not an active fragment of FragmentManager " + this);
    }

    public final void startPendingDeferredFragments() {
        Iterator it = this.mFragmentStore.getActiveFragmentStateManagers().iterator();
        while (it.hasNext()) {
            FragmentStateManager fragmentStateManager = (FragmentStateManager) it.next();
            Objects.requireNonNull(fragmentStateManager);
            Fragment fragment = fragmentStateManager.mFragment;
            if (fragment.mDeferStart) {
                if (this.mExecutingActions) {
                    this.mHavePendingDeferredStart = true;
                } else {
                    fragment.mDeferStart = false;
                    fragmentStateManager.moveToExpectedState();
                }
            }
        }
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("FragmentManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        Fragment fragment = this.mParent;
        if (fragment != null) {
            sb.append(fragment.getClass().getSimpleName());
            sb.append("{");
            sb.append(Integer.toHexString(System.identityHashCode(this.mParent)));
            sb.append("}");
        } else {
            FragmentHostCallback<?> fragmentHostCallback = this.mHost;
            if (fragmentHostCallback != null) {
                sb.append(fragmentHostCallback.getClass().getSimpleName());
                sb.append("{");
                sb.append(Integer.toHexString(System.identityHashCode(this.mHost)));
                sb.append("}");
            } else {
                sb.append("null");
            }
        }
        sb.append("}}");
        return sb.toString();
    }

    public final void updateOnBackPressedCallbackEnabled() {
        int i;
        synchronized (this.mPendingActions) {
            boolean z = true;
            if (!this.mPendingActions.isEmpty()) {
                AnonymousClass1 r4 = this.mOnBackPressedCallback;
                Objects.requireNonNull(r4);
                r4.mEnabled = true;
                return;
            }
            AnonymousClass1 r0 = this.mOnBackPressedCallback;
            ArrayList<BackStackRecord> arrayList = this.mBackStack;
            if (arrayList != null) {
                i = arrayList.size();
            } else {
                i = 0;
            }
            if (i <= 0 || !isPrimaryNavigation(this.mParent)) {
                z = false;
            }
            Objects.requireNonNull(r0);
            r0.mEnabled = z;
        }
    }

    /* JADX WARN: Finally extract failed */
    public final boolean execPendingActions(boolean z) {
        boolean z2;
        ensureExecReady(z);
        boolean z3 = false;
        while (true) {
            ArrayList<BackStackRecord> arrayList = this.mTmpRecords;
            ArrayList<Boolean> arrayList2 = this.mTmpIsPop;
            synchronized (this.mPendingActions) {
                if (this.mPendingActions.isEmpty()) {
                    z2 = false;
                } else {
                    int size = this.mPendingActions.size();
                    z2 = false;
                    for (int i = 0; i < size; i++) {
                        z2 |= this.mPendingActions.get(i).generateOps(arrayList, arrayList2);
                    }
                    this.mPendingActions.clear();
                    FragmentHostCallback<?> fragmentHostCallback = this.mHost;
                    Objects.requireNonNull(fragmentHostCallback);
                    fragmentHostCallback.mHandler.removeCallbacks(this.mExecCommit);
                }
            }
            if (z2) {
                this.mExecutingActions = true;
                try {
                    removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
                    cleanupExec();
                    z3 = true;
                } catch (Throwable th) {
                    cleanupExec();
                    throw th;
                }
            } else {
                updateOnBackPressedCallbackEnabled();
                doPendingDeferredStart();
                FragmentStore fragmentStore = this.mFragmentStore;
                Objects.requireNonNull(fragmentStore);
                fragmentStore.mActive.values().removeAll(Collections.singleton(null));
                return z3;
            }
        }
    }

    public final void forcePostponedTransactions() {
        Iterator it = collectAllSpecialEffectsController().iterator();
        while (it.hasNext()) {
            SpecialEffectsController specialEffectsController = (SpecialEffectsController) it.next();
            Objects.requireNonNull(specialEffectsController);
            if (specialEffectsController.mIsContainerPostponed) {
                specialEffectsController.mIsContainerPostponed = false;
                specialEffectsController.executePendingOperations();
            }
        }
    }

    public final void removeRedundantOperationsAndExecute(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
        if (!arrayList.isEmpty()) {
            if (arrayList.size() == arrayList2.size()) {
                int size = arrayList.size();
                int i = 0;
                int i2 = 0;
                while (i < size) {
                    if (!arrayList.get(i).mReorderingAllowed) {
                        if (i2 != i) {
                            executeOpsTogether(arrayList, arrayList2, i2, i);
                        }
                        i2 = i + 1;
                        if (arrayList2.get(i).booleanValue()) {
                            while (i2 < size && arrayList2.get(i2).booleanValue() && !arrayList.get(i2).mReorderingAllowed) {
                                i2++;
                            }
                        }
                        executeOpsTogether(arrayList, arrayList2, i, i2);
                        i = i2 - 1;
                    }
                    i++;
                }
                if (i2 != size) {
                    executeOpsTogether(arrayList, arrayList2, i2, size);
                    return;
                }
                return;
            }
            throw new IllegalStateException("Internal error with the back stack records");
        }
    }

    public final Parcelable saveAllStateInternal() {
        ArrayList<String> arrayList;
        int size;
        forcePostponedTransactions();
        Iterator it = collectAllSpecialEffectsController().iterator();
        while (it.hasNext()) {
            ((SpecialEffectsController) it.next()).forceCompleteAllOperations();
        }
        execPendingActions(true);
        this.mStateSaved = true;
        FragmentManagerViewModel fragmentManagerViewModel = this.mNonConfig;
        Objects.requireNonNull(fragmentManagerViewModel);
        fragmentManagerViewModel.mIsStateSaved = true;
        FragmentStore fragmentStore = this.mFragmentStore;
        Objects.requireNonNull(fragmentStore);
        ArrayList<String> arrayList2 = new ArrayList<>(fragmentStore.mActive.size());
        for (FragmentStateManager fragmentStateManager : fragmentStore.mActive.values()) {
            if (fragmentStateManager != null) {
                Fragment fragment = fragmentStateManager.mFragment;
                fragmentStateManager.saveState();
                arrayList2.add(fragment.mWho);
                if (isLoggingEnabled(2)) {
                    Log.v("FragmentManager", "Saved state of " + fragment + ": " + fragment.mSavedFragmentState);
                }
            }
        }
        FragmentStore fragmentStore2 = this.mFragmentStore;
        Objects.requireNonNull(fragmentStore2);
        ArrayList<FragmentState> arrayList3 = new ArrayList<>(fragmentStore2.mSavedState.values());
        BackStackRecordState[] backStackRecordStateArr = null;
        if (arrayList3.isEmpty()) {
            if (isLoggingEnabled(2)) {
                Log.v("FragmentManager", "saveAllState: no fragments!");
            }
            return null;
        }
        FragmentStore fragmentStore3 = this.mFragmentStore;
        Objects.requireNonNull(fragmentStore3);
        synchronized (fragmentStore3.mAdded) {
            if (fragmentStore3.mAdded.isEmpty()) {
                arrayList = null;
            } else {
                arrayList = new ArrayList<>(fragmentStore3.mAdded.size());
                Iterator<Fragment> it2 = fragmentStore3.mAdded.iterator();
                while (it2.hasNext()) {
                    Fragment next = it2.next();
                    arrayList.add(next.mWho);
                    if (isLoggingEnabled(2)) {
                        Log.v("FragmentManager", "saveAllState: adding fragment (" + next.mWho + "): " + next);
                    }
                }
            }
        }
        ArrayList<BackStackRecord> arrayList4 = this.mBackStack;
        if (arrayList4 != null && (size = arrayList4.size()) > 0) {
            backStackRecordStateArr = new BackStackRecordState[size];
            for (int i = 0; i < size; i++) {
                backStackRecordStateArr[i] = new BackStackRecordState(this.mBackStack.get(i));
                if (isLoggingEnabled(2)) {
                    StringBuilder m = ExifInterface$$ExternalSyntheticOutline0.m("saveAllState: adding back stack #", i, ": ");
                    m.append(this.mBackStack.get(i));
                    Log.v("FragmentManager", m.toString());
                }
            }
        }
        FragmentManagerState fragmentManagerState = new FragmentManagerState();
        fragmentManagerState.mSavedState = arrayList3;
        fragmentManagerState.mActive = arrayList2;
        fragmentManagerState.mAdded = arrayList;
        fragmentManagerState.mBackStack = backStackRecordStateArr;
        fragmentManagerState.mBackStackIndex = this.mBackStackIndex.get();
        Fragment fragment2 = this.mPrimaryNav;
        if (fragment2 != null) {
            fragmentManagerState.mPrimaryNavActiveWho = fragment2.mWho;
        }
        fragmentManagerState.mBackStackStateKeys.addAll(this.mBackStackStates.keySet());
        fragmentManagerState.mBackStackStates.addAll(this.mBackStackStates.values());
        fragmentManagerState.mResultKeys.addAll(this.mResults.keySet());
        fragmentManagerState.mResults.addAll(this.mResults.values());
        fragmentManagerState.mLaunchedFragments = new ArrayList<>(this.mLaunchedFragments);
        return fragmentManagerState;
    }

    public final void setExitAnimationOrder(Fragment fragment, boolean z) {
        ViewGroup fragmentContainer = getFragmentContainer(fragment);
        if (fragmentContainer != null && (fragmentContainer instanceof FragmentContainerView)) {
            ((FragmentContainerView) fragmentContainer).mDrawDisappearingViewsFirst = !z;
        }
    }

    public final void setVisibleRemovingFragment(Fragment fragment) {
        int i;
        int i2;
        int i3;
        int i4;
        ViewGroup fragmentContainer = getFragmentContainer(fragment);
        if (fragmentContainer != null) {
            Fragment.AnimationInfo animationInfo = fragment.mAnimationInfo;
            boolean z = false;
            if (animationInfo == null) {
                i = 0;
            } else {
                i = animationInfo.mEnterAnim;
            }
            if (animationInfo == null) {
                i2 = 0;
            } else {
                i2 = animationInfo.mExitAnim;
            }
            int i5 = i2 + i;
            if (animationInfo == null) {
                i3 = 0;
            } else {
                i3 = animationInfo.mPopEnterAnim;
            }
            int i6 = i3 + i5;
            if (animationInfo == null) {
                i4 = 0;
            } else {
                i4 = animationInfo.mPopExitAnim;
            }
            if (i4 + i6 > 0) {
                if (fragmentContainer.getTag(2131429202) == null) {
                    fragmentContainer.setTag(2131429202, fragment);
                }
                Fragment fragment2 = (Fragment) fragmentContainer.getTag(2131429202);
                Fragment.AnimationInfo animationInfo2 = fragment.mAnimationInfo;
                if (animationInfo2 != null) {
                    z = animationInfo2.mIsPop;
                }
                Objects.requireNonNull(fragment2);
                if (fragment2.mAnimationInfo != null) {
                    fragment2.ensureAnimationInfo().mIsPop = z;
                }
            }
        }
    }
}
