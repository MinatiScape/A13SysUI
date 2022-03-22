package androidx.fragment.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.loader.app.LoaderManagerImpl;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryController;
import androidx.savedstate.SavedStateRegistryOwner;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public class Fragment implements ComponentCallbacks, View.OnCreateContextMenuListener, LifecycleOwner, ViewModelStoreOwner, SavedStateRegistryOwner {
    public static final Object USE_DEFAULT_TRANSITION = new Object();
    public boolean mAdded;
    public AnimationInfo mAnimationInfo;
    public Bundle mArguments;
    public int mBackStackNesting;
    public boolean mCalled;
    public ViewGroup mContainer;
    public int mContainerId;
    public boolean mDeferStart;
    public boolean mDetached;
    public int mFragmentId;
    public FragmentManager mFragmentManager;
    public boolean mFromLayout;
    public boolean mHidden;
    public boolean mHiddenChanged;
    public FragmentHostCallback<?> mHost;
    public boolean mInLayout;
    public boolean mIsCreated;
    public Fragment mParentFragment;
    public boolean mPerformedCreateView;
    public String mPreviousWho;
    public boolean mRemoving;
    public boolean mRestored;
    public boolean mRetainInstance;
    public Bundle mSavedFragmentState;
    public Bundle mSavedViewRegistryState;
    public SparseArray<Parcelable> mSavedViewState;
    public String mTag;
    public Fragment mTarget;
    public int mTargetRequestCode;
    public View mView;
    public FragmentViewLifecycleOwner mViewLifecycleOwner;
    public int mState = -1;
    public String mWho = UUID.randomUUID().toString();
    public String mTargetWho = null;
    public Boolean mIsPrimaryNavigationFragment = null;
    public FragmentManagerImpl mChildFragmentManager = new FragmentManagerImpl();
    public boolean mMenuVisible = true;
    public boolean mUserVisibleHint = true;
    public Lifecycle.State mMaxState = Lifecycle.State.RESUMED;
    public MutableLiveData<LifecycleOwner> mViewLifecycleOwnerLiveData = new MutableLiveData<>();
    public final ArrayList<OnPreAttachedListener> mOnPreAttachedListeners = new ArrayList<>();
    public LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this, true);
    public SavedStateRegistryController mSavedStateRegistryController = new SavedStateRegistryController(this);

    /* renamed from: androidx.fragment.app.Fragment$4  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass4 extends FragmentContainer {
        public AnonymousClass4() {
        }

        @Override // androidx.fragment.app.FragmentContainer
        public final View onFindViewById(int i) {
            View view = Fragment.this.mView;
            if (view != null) {
                return view.findViewById(i);
            }
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Fragment ");
            m.append(Fragment.this);
            m.append(" does not have a view");
            throw new IllegalStateException(m.toString());
        }

        @Override // androidx.fragment.app.FragmentContainer
        public final boolean onHasView() {
            if (Fragment.this.mView != null) {
                return true;
            }
            return false;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class OnPreAttachedListener {
        public abstract void onPreAttached();
    }

    @SuppressLint({"BanParcelableUsage, ParcelClassLoader"})
    /* loaded from: classes.dex */
    public static class SavedState implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() { // from class: androidx.fragment.app.Fragment.SavedState.1
            @Override // android.os.Parcelable.Creator
            public final Object createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            @Override // android.os.Parcelable.ClassLoaderCreator
            public final SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            @Override // android.os.Parcelable.Creator
            public final Object[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public final Bundle mState;

        @Override // android.os.Parcelable
        public final int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            parcel.writeBundle(this.mState);
        }

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            Bundle readBundle = parcel.readBundle();
            this.mState = readBundle;
            if (classLoader != null && readBundle != null) {
                readBundle.setClassLoader(classLoader);
            }
        }
    }

    @Deprecated
    public final void onActivityResult(int i, int i2, Intent intent) {
        if (FragmentManager.isLoggingEnabled(2)) {
            Log.v("FragmentManager", "Fragment " + this + " received the following in onActivityResult(): requestCode: " + i + " resultCode: " + i2 + " data: " + intent);
        }
    }

    public void onAttach(Context context) {
        Activity activity;
        this.mCalled = true;
        FragmentHostCallback<?> fragmentHostCallback = this.mHost;
        if (fragmentHostCallback == null) {
            activity = null;
        } else {
            Objects.requireNonNull(fragmentHostCallback);
            activity = fragmentHostCallback.mActivity;
        }
        if (activity != null) {
            this.mCalled = true;
        }
    }

    @Override // android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        this.mCalled = true;
    }

    public void onCreate(Bundle bundle) {
        boolean z = true;
        this.mCalled = true;
        restoreChildFragmentState(bundle);
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        Objects.requireNonNull(fragmentManagerImpl);
        if (fragmentManagerImpl.mCurState < 1) {
            z = false;
        }
        if (!z) {
            this.mChildFragmentManager.dispatchCreate();
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return null;
    }

    public void onDestroyView() {
        this.mCalled = true;
    }

    public void onDetach() {
        this.mCalled = true;
    }

    public final void onInflate() {
        Activity activity;
        this.mCalled = true;
        FragmentHostCallback<?> fragmentHostCallback = this.mHost;
        if (fragmentHostCallback == null) {
            activity = null;
        } else {
            activity = fragmentHostCallback.mActivity;
        }
        if (activity != null) {
            this.mCalled = true;
        }
    }

    @Override // android.content.ComponentCallbacks
    public final void onLowMemory() {
        this.mCalled = true;
    }

    public void onSaveInstanceState(Bundle bundle) {
    }

    public void onStart() {
        this.mCalled = true;
    }

    public void onStop() {
        this.mCalled = true;
    }

    public void onViewStateRestored(Bundle bundle) {
        this.mCalled = true;
    }

    /* loaded from: classes.dex */
    public static class AnimationInfo {
        public int mEnterAnim;
        public int mExitAnim;
        public boolean mIsPop;
        public int mNextTransition;
        public int mPopEnterAnim;
        public int mPopExitAnim;
        public Object mReenterTransition;
        public Object mReturnTransition;
        public Object mSharedElementReturnTransition;
        public ArrayList<String> mSharedElementSourceNames;
        public ArrayList<String> mSharedElementTargetNames;
        public float mPostOnViewCreatedAlpha = 1.0f;
        public View mFocusedView = null;

        public AnimationInfo() {
            Object obj = Fragment.USE_DEFAULT_TRANSITION;
            this.mReturnTransition = obj;
            this.mReenterTransition = obj;
            this.mSharedElementReturnTransition = obj;
        }
    }

    public FragmentContainer createFragmentContainer() {
        return new AnonymousClass4();
    }

    public final AnimationInfo ensureAnimationInfo() {
        if (this.mAnimationInfo == null) {
            this.mAnimationInfo = new AnimationInfo();
        }
        return this.mAnimationInfo;
    }

    public final FragmentManager getChildFragmentManager() {
        if (this.mHost != null) {
            return this.mChildFragmentManager;
        }
        throw new IllegalStateException("Fragment " + this + " has not been attached yet.");
    }

    public final Context getContext() {
        FragmentHostCallback<?> fragmentHostCallback = this.mHost;
        if (fragmentHostCallback == null) {
            return null;
        }
        Objects.requireNonNull(fragmentHostCallback);
        return fragmentHostCallback.mContext;
    }

    public final int getMinimumMaxLifecycleState() {
        Lifecycle.State state = this.mMaxState;
        if (state == Lifecycle.State.INITIALIZED || this.mParentFragment == null) {
            return state.ordinal();
        }
        return Math.min(state.ordinal(), this.mParentFragment.getMinimumMaxLifecycleState());
    }

    public final FragmentManager getParentFragmentManager() {
        FragmentManager fragmentManager = this.mFragmentManager;
        if (fragmentManager != null) {
            return fragmentManager;
        }
        throw new IllegalStateException("Fragment " + this + " not associated with a fragment manager.");
    }

    @Override // androidx.savedstate.SavedStateRegistryOwner
    public final SavedStateRegistry getSavedStateRegistry() {
        SavedStateRegistryController savedStateRegistryController = this.mSavedStateRegistryController;
        Objects.requireNonNull(savedStateRegistryController);
        return savedStateRegistryController.mRegistry;
    }

    @Override // androidx.lifecycle.ViewModelStoreOwner
    public final ViewModelStore getViewModelStore() {
        if (this.mFragmentManager == null) {
            throw new IllegalStateException("Can't access ViewModels from detached fragment");
        } else if (getMinimumMaxLifecycleState() != 1) {
            FragmentManager fragmentManager = this.mFragmentManager;
            Objects.requireNonNull(fragmentManager);
            FragmentManagerViewModel fragmentManagerViewModel = fragmentManager.mNonConfig;
            Objects.requireNonNull(fragmentManagerViewModel);
            ViewModelStore viewModelStore = fragmentManagerViewModel.mViewModelStores.get(this.mWho);
            if (viewModelStore != null) {
                return viewModelStore;
            }
            ViewModelStore viewModelStore2 = new ViewModelStore();
            fragmentManagerViewModel.mViewModelStores.put(this.mWho, viewModelStore2);
            return viewModelStore2;
        } else {
            throw new IllegalStateException("Calling getViewModelStore() before a Fragment reaches onCreate() when using setMaxLifecycle(INITIALIZED) is not supported");
        }
    }

    @Override // android.view.View.OnCreateContextMenuListener
    public final void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        FragmentActivity fragmentActivity;
        FragmentHostCallback<?> fragmentHostCallback = this.mHost;
        if (fragmentHostCallback == null) {
            fragmentActivity = null;
        } else {
            fragmentActivity = (FragmentActivity) fragmentHostCallback.mActivity;
        }
        if (fragmentActivity != null) {
            fragmentActivity.onCreateContextMenu(contextMenu, view, contextMenuInfo);
            return;
        }
        throw new IllegalStateException("Fragment " + this + " not attached to an activity.");
    }

    public LayoutInflater onGetLayoutInflater(Bundle bundle) {
        FragmentHostCallback<?> fragmentHostCallback = this.mHost;
        if (fragmentHostCallback != null) {
            LayoutInflater onGetLayoutInflater = fragmentHostCallback.onGetLayoutInflater();
            FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
            Objects.requireNonNull(fragmentManagerImpl);
            onGetLayoutInflater.setFactory2(fragmentManagerImpl.mLayoutInflaterFactory);
            return onGetLayoutInflater;
        }
        throw new IllegalStateException("onGetLayoutInflater() cannot be executed until the Fragment is attached to the FragmentManager.");
    }

    public void performCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mChildFragmentManager.noteStateNotSaved();
        boolean z = true;
        this.mPerformedCreateView = true;
        this.mViewLifecycleOwner = new FragmentViewLifecycleOwner(getViewModelStore());
        View onCreateView = onCreateView(layoutInflater, viewGroup, bundle);
        this.mView = onCreateView;
        if (onCreateView != null) {
            this.mViewLifecycleOwner.initialize();
            this.mView.setTag(2131429194, this.mViewLifecycleOwner);
            this.mView.setTag(2131429196, this.mViewLifecycleOwner);
            this.mView.setTag(2131429195, this.mViewLifecycleOwner);
            this.mViewLifecycleOwnerLiveData.setValue(this.mViewLifecycleOwner);
            return;
        }
        FragmentViewLifecycleOwner fragmentViewLifecycleOwner = this.mViewLifecycleOwner;
        Objects.requireNonNull(fragmentViewLifecycleOwner);
        if (fragmentViewLifecycleOwner.mLifecycleRegistry == null) {
            z = false;
        }
        if (!z) {
            this.mViewLifecycleOwner = null;
            return;
        }
        throw new IllegalStateException("Called getViewLifecycleOwner() but onCreateView() returned null");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void performMultiWindowModeChanged(boolean z) {
        this.mChildFragmentManager.dispatchMultiWindowModeChanged(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void performPictureInPictureModeChanged(boolean z) {
        this.mChildFragmentManager.dispatchPictureInPictureModeChanged(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean performPrepareOptionsMenu() {
        if (!this.mHidden) {
            return false | this.mChildFragmentManager.dispatchPrepareOptionsMenu();
        }
        return false;
    }

    public final View requireView() {
        View view = this.mView;
        if (view != null) {
            return view;
        }
        throw new IllegalStateException("Fragment " + this + " did not return a View from onCreateView() or this was called before onCreateView().");
    }

    public final void restoreChildFragmentState(Bundle bundle) {
        Parcelable parcelable;
        if (bundle != null && (parcelable = bundle.getParcelable("android:support:fragments")) != null) {
            this.mChildFragmentManager.restoreSaveStateInternal(parcelable);
            this.mChildFragmentManager.dispatchCreate();
        }
    }

    public final void setAnimations(int i, int i2, int i3, int i4) {
        if (this.mAnimationInfo != null || i != 0 || i2 != 0 || i3 != 0 || i4 != 0) {
            ensureAnimationInfo().mEnterAnim = i;
            ensureAnimationInfo().mExitAnim = i2;
            ensureAnimationInfo().mPopEnterAnim = i3;
            ensureAnimationInfo().mPopExitAnim = i4;
        }
    }

    public final void setArguments(Bundle bundle) {
        boolean z;
        FragmentManager fragmentManager = this.mFragmentManager;
        if (fragmentManager != null) {
            if (fragmentManager.mStateSaved || fragmentManager.mStopped) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                throw new IllegalStateException("Fragment already added and state has been saved");
            }
        }
        this.mArguments = bundle;
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append(getClass().getSimpleName());
        sb.append("{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append("}");
        sb.append(" (");
        sb.append(this.mWho);
        if (this.mFragmentId != 0) {
            sb.append(" id=0x");
            sb.append(Integer.toHexString(this.mFragmentId));
        }
        if (this.mTag != null) {
            sb.append(" tag=");
            sb.append(this.mTag);
        }
        sb.append(")");
        return sb.toString();
    }

    public Fragment() {
        new AtomicInteger();
    }

    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        boolean z;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        String str2;
        printWriter.print(str);
        printWriter.print("mFragmentId=#");
        printWriter.print(Integer.toHexString(this.mFragmentId));
        printWriter.print(" mContainerId=#");
        printWriter.print(Integer.toHexString(this.mContainerId));
        printWriter.print(" mTag=");
        printWriter.println(this.mTag);
        printWriter.print(str);
        printWriter.print("mState=");
        printWriter.print(this.mState);
        printWriter.print(" mWho=");
        printWriter.print(this.mWho);
        printWriter.print(" mBackStackNesting=");
        printWriter.println(this.mBackStackNesting);
        printWriter.print(str);
        printWriter.print("mAdded=");
        printWriter.print(this.mAdded);
        printWriter.print(" mRemoving=");
        printWriter.print(this.mRemoving);
        printWriter.print(" mFromLayout=");
        printWriter.print(this.mFromLayout);
        printWriter.print(" mInLayout=");
        printWriter.println(this.mInLayout);
        printWriter.print(str);
        printWriter.print("mHidden=");
        printWriter.print(this.mHidden);
        printWriter.print(" mDetached=");
        printWriter.print(this.mDetached);
        printWriter.print(" mMenuVisible=");
        printWriter.print(this.mMenuVisible);
        printWriter.print(" mHasMenu=");
        int i8 = 0;
        printWriter.println(false);
        printWriter.print(str);
        printWriter.print("mRetainInstance=");
        printWriter.print(this.mRetainInstance);
        printWriter.print(" mUserVisibleHint=");
        printWriter.println(this.mUserVisibleHint);
        if (this.mFragmentManager != null) {
            printWriter.print(str);
            printWriter.print("mFragmentManager=");
            printWriter.println(this.mFragmentManager);
        }
        if (this.mHost != null) {
            printWriter.print(str);
            printWriter.print("mHost=");
            printWriter.println(this.mHost);
        }
        if (this.mParentFragment != null) {
            printWriter.print(str);
            printWriter.print("mParentFragment=");
            printWriter.println(this.mParentFragment);
        }
        if (this.mArguments != null) {
            printWriter.print(str);
            printWriter.print("mArguments=");
            printWriter.println(this.mArguments);
        }
        if (this.mSavedFragmentState != null) {
            printWriter.print(str);
            printWriter.print("mSavedFragmentState=");
            printWriter.println(this.mSavedFragmentState);
        }
        if (this.mSavedViewState != null) {
            printWriter.print(str);
            printWriter.print("mSavedViewState=");
            printWriter.println(this.mSavedViewState);
        }
        if (this.mSavedViewRegistryState != null) {
            printWriter.print(str);
            printWriter.print("mSavedViewRegistryState=");
            printWriter.println(this.mSavedViewRegistryState);
        }
        Fragment fragment = this.mTarget;
        if (fragment == null) {
            FragmentManager fragmentManager = this.mFragmentManager;
            if (fragmentManager == null || (str2 = this.mTargetWho) == null) {
                fragment = null;
            } else {
                fragment = fragmentManager.findActiveFragment(str2);
            }
        }
        if (fragment != null) {
            printWriter.print(str);
            printWriter.print("mTarget=");
            printWriter.print(fragment);
            printWriter.print(" mTargetRequestCode=");
            printWriter.println(this.mTargetRequestCode);
        }
        printWriter.print(str);
        printWriter.print("mPopDirection=");
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            z = false;
        } else {
            z = animationInfo.mIsPop;
        }
        printWriter.println(z);
        AnimationInfo animationInfo2 = this.mAnimationInfo;
        if (animationInfo2 == null) {
            i = 0;
        } else {
            i = animationInfo2.mEnterAnim;
        }
        if (i != 0) {
            printWriter.print(str);
            printWriter.print("getEnterAnim=");
            AnimationInfo animationInfo3 = this.mAnimationInfo;
            if (animationInfo3 == null) {
                i7 = 0;
            } else {
                i7 = animationInfo3.mEnterAnim;
            }
            printWriter.println(i7);
        }
        AnimationInfo animationInfo4 = this.mAnimationInfo;
        if (animationInfo4 == null) {
            i2 = 0;
        } else {
            i2 = animationInfo4.mExitAnim;
        }
        if (i2 != 0) {
            printWriter.print(str);
            printWriter.print("getExitAnim=");
            AnimationInfo animationInfo5 = this.mAnimationInfo;
            if (animationInfo5 == null) {
                i6 = 0;
            } else {
                i6 = animationInfo5.mExitAnim;
            }
            printWriter.println(i6);
        }
        AnimationInfo animationInfo6 = this.mAnimationInfo;
        if (animationInfo6 == null) {
            i3 = 0;
        } else {
            i3 = animationInfo6.mPopEnterAnim;
        }
        if (i3 != 0) {
            printWriter.print(str);
            printWriter.print("getPopEnterAnim=");
            AnimationInfo animationInfo7 = this.mAnimationInfo;
            if (animationInfo7 == null) {
                i5 = 0;
            } else {
                i5 = animationInfo7.mPopEnterAnim;
            }
            printWriter.println(i5);
        }
        AnimationInfo animationInfo8 = this.mAnimationInfo;
        if (animationInfo8 == null) {
            i4 = 0;
        } else {
            i4 = animationInfo8.mPopExitAnim;
        }
        if (i4 != 0) {
            printWriter.print(str);
            printWriter.print("getPopExitAnim=");
            AnimationInfo animationInfo9 = this.mAnimationInfo;
            if (animationInfo9 != null) {
                i8 = animationInfo9.mPopExitAnim;
            }
            printWriter.println(i8);
        }
        if (this.mContainer != null) {
            printWriter.print(str);
            printWriter.print("mContainer=");
            printWriter.println(this.mContainer);
        }
        if (this.mView != null) {
            printWriter.print(str);
            printWriter.print("mView=");
            printWriter.println(this.mView);
        }
        if (getContext() != null) {
            new LoaderManagerImpl(this, getViewModelStore()).dump(str, printWriter);
        }
        printWriter.print(str);
        printWriter.println("Child " + this.mChildFragmentManager + ":");
        this.mChildFragmentManager.dump(SupportMenuInflater$$ExternalSyntheticOutline0.m(str, "  "), fileDescriptor, printWriter, strArr);
    }

    public final boolean equals(Object obj) {
        return super.equals(obj);
    }

    public final int hashCode() {
        return super.hashCode();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void performLowMemory() {
        onLowMemory();
        this.mChildFragmentManager.dispatchLowMemory();
    }

    public final Context requireContext() {
        Context context = getContext();
        if (context != null) {
            return context;
        }
        throw new IllegalStateException("Fragment " + this + " not attached to a context.");
    }

    /* loaded from: classes.dex */
    public static class InstantiationException extends RuntimeException {
        public InstantiationException(String str, Exception exc) {
            super(str, exc);
        }
    }

    @Override // androidx.lifecycle.LifecycleOwner
    public final Lifecycle getLifecycle() {
        return this.mLifecycleRegistry;
    }
}
