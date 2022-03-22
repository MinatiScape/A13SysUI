package androidx.fragment.app;

import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.collection.SparseArrayCompat;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline1;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline0;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.SpecialEffectsController;
import androidx.fragment.app.strictmode.FragmentStrictMode;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManagerImpl;
import com.android.systemui.plugins.FalsingManager;
import java.util.Iterator;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public final class FragmentStateManager {
    public final FragmentLifecycleCallbacksDispatcher mDispatcher;
    public final Fragment mFragment;
    public final FragmentStore mFragmentStore;
    public boolean mMovingToState = false;
    public int mFragmentManagerState = -1;

    public FragmentStateManager(FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher, FragmentStore fragmentStore, Fragment fragment) {
        this.mDispatcher = fragmentLifecycleCallbacksDispatcher;
        this.mFragmentStore = fragmentStore;
        this.mFragment = fragment;
    }

    public final void activityCreated() {
        if (FragmentManager.isLoggingEnabled(3)) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("moveto ACTIVITY_CREATED: ");
            m.append(this.mFragment);
            Log.d("FragmentManager", m.toString());
        }
        Fragment fragment = this.mFragment;
        Bundle bundle = fragment.mSavedFragmentState;
        fragment.mChildFragmentManager.noteStateNotSaved();
        fragment.mState = 3;
        fragment.mCalled = true;
        if (FragmentManager.isLoggingEnabled(3)) {
            Log.d("FragmentManager", "moveto RESTORE_VIEW_STATE: " + fragment);
        }
        View view = fragment.mView;
        if (view != null) {
            Bundle bundle2 = fragment.mSavedFragmentState;
            SparseArray<Parcelable> sparseArray = fragment.mSavedViewState;
            if (sparseArray != null) {
                view.restoreHierarchyState(sparseArray);
                fragment.mSavedViewState = null;
            }
            if (fragment.mView != null) {
                FragmentViewLifecycleOwner fragmentViewLifecycleOwner = fragment.mViewLifecycleOwner;
                Bundle bundle3 = fragment.mSavedViewRegistryState;
                Objects.requireNonNull(fragmentViewLifecycleOwner);
                fragmentViewLifecycleOwner.mSavedStateRegistryController.performRestore(bundle3);
                fragment.mSavedViewRegistryState = null;
            }
            fragment.mCalled = false;
            fragment.onViewStateRestored(bundle2);
            if (!fragment.mCalled) {
                throw new SuperNotCalledException("Fragment " + fragment + " did not call through to super.onViewStateRestored()");
            } else if (fragment.mView != null) {
                fragment.mViewLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
            }
        }
        fragment.mSavedFragmentState = null;
        FragmentManagerImpl fragmentManagerImpl = fragment.mChildFragmentManager;
        Objects.requireNonNull(fragmentManagerImpl);
        fragmentManagerImpl.mStateSaved = false;
        fragmentManagerImpl.mStopped = false;
        FragmentManagerViewModel fragmentManagerViewModel = fragmentManagerImpl.mNonConfig;
        Objects.requireNonNull(fragmentManagerViewModel);
        fragmentManagerViewModel.mIsStateSaved = false;
        fragmentManagerImpl.dispatchStateChange(4);
        FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher = this.mDispatcher;
        Bundle bundle4 = this.mFragment.mSavedFragmentState;
        fragmentLifecycleCallbacksDispatcher.dispatchOnFragmentActivityCreated(false);
    }

    public final void attach() {
        if (FragmentManager.isLoggingEnabled(3)) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("moveto ATTACHED: ");
            m.append(this.mFragment);
            Log.d("FragmentManager", m.toString());
        }
        Fragment fragment = this.mFragment;
        Fragment fragment2 = fragment.mTarget;
        FragmentStateManager fragmentStateManager = null;
        if (fragment2 != null) {
            FragmentStore fragmentStore = this.mFragmentStore;
            String str = fragment2.mWho;
            Objects.requireNonNull(fragmentStore);
            FragmentStateManager fragmentStateManager2 = fragmentStore.mActive.get(str);
            if (fragmentStateManager2 != null) {
                Fragment fragment3 = this.mFragment;
                fragment3.mTargetWho = fragment3.mTarget.mWho;
                fragment3.mTarget = null;
                fragmentStateManager = fragmentStateManager2;
            } else {
                StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Fragment ");
                m2.append(this.mFragment);
                m2.append(" declared target fragment ");
                m2.append(this.mFragment.mTarget);
                m2.append(" that does not belong to this FragmentManager!");
                throw new IllegalStateException(m2.toString());
            }
        } else {
            String str2 = fragment.mTargetWho;
            if (str2 != null) {
                FragmentStore fragmentStore2 = this.mFragmentStore;
                Objects.requireNonNull(fragmentStore2);
                fragmentStateManager = fragmentStore2.mActive.get(str2);
                if (fragmentStateManager == null) {
                    StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("Fragment ");
                    m3.append(this.mFragment);
                    m3.append(" declared target fragment ");
                    throw new IllegalStateException(MotionController$$ExternalSyntheticOutline1.m(m3, this.mFragment.mTargetWho, " that does not belong to this FragmentManager!"));
                }
            }
        }
        if (fragmentStateManager != null) {
            fragmentStateManager.moveToExpectedState();
        }
        Fragment fragment4 = this.mFragment;
        FragmentManager fragmentManager = fragment4.mFragmentManager;
        Objects.requireNonNull(fragmentManager);
        fragment4.mHost = fragmentManager.mHost;
        Fragment fragment5 = this.mFragment;
        FragmentManager fragmentManager2 = fragment5.mFragmentManager;
        Objects.requireNonNull(fragmentManager2);
        fragment5.mParentFragment = fragmentManager2.mParent;
        this.mDispatcher.dispatchOnFragmentPreAttached(false);
        Fragment fragment6 = this.mFragment;
        Objects.requireNonNull(fragment6);
        Iterator<Fragment.OnPreAttachedListener> it = fragment6.mOnPreAttachedListeners.iterator();
        while (it.hasNext()) {
            it.next().onPreAttached();
        }
        fragment6.mOnPreAttachedListeners.clear();
        fragment6.mChildFragmentManager.attachController(fragment6.mHost, fragment6.createFragmentContainer(), fragment6);
        fragment6.mState = 0;
        fragment6.mCalled = false;
        FragmentHostCallback<?> fragmentHostCallback = fragment6.mHost;
        Objects.requireNonNull(fragmentHostCallback);
        fragment6.onAttach(fragmentHostCallback.mContext);
        if (fragment6.mCalled) {
            FragmentManager fragmentManager3 = fragment6.mFragmentManager;
            Objects.requireNonNull(fragmentManager3);
            Iterator<FragmentOnAttachListener> it2 = fragmentManager3.mOnAttachListeners.iterator();
            while (it2.hasNext()) {
                it2.next().onAttachFragment$1();
            }
            FragmentManagerImpl fragmentManagerImpl = fragment6.mChildFragmentManager;
            Objects.requireNonNull(fragmentManagerImpl);
            fragmentManagerImpl.mStateSaved = false;
            fragmentManagerImpl.mStopped = false;
            FragmentManagerViewModel fragmentManagerViewModel = fragmentManagerImpl.mNonConfig;
            Objects.requireNonNull(fragmentManagerViewModel);
            fragmentManagerViewModel.mIsStateSaved = false;
            fragmentManagerImpl.dispatchStateChange(0);
            this.mDispatcher.dispatchOnFragmentAttached(false);
            return;
        }
        throw new SuperNotCalledException("Fragment " + fragment6 + " did not call through to super.onAttach()");
    }

    public final void create() {
        if (FragmentManager.isLoggingEnabled(3)) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("moveto CREATED: ");
            m.append(this.mFragment);
            Log.d("FragmentManager", m.toString());
        }
        Fragment fragment = this.mFragment;
        if (!fragment.mIsCreated) {
            this.mDispatcher.dispatchOnFragmentPreCreated(false);
            final Fragment fragment2 = this.mFragment;
            Bundle bundle = fragment2.mSavedFragmentState;
            fragment2.mChildFragmentManager.noteStateNotSaved();
            fragment2.mState = 1;
            fragment2.mCalled = false;
            fragment2.mLifecycleRegistry.addObserver(new LifecycleEventObserver() { // from class: androidx.fragment.app.Fragment.5
                @Override // androidx.lifecycle.LifecycleEventObserver
                public final void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
                    View view;
                    if (event == Lifecycle.Event.ON_STOP && (view = fragment2.mView) != null) {
                        view.cancelPendingInputEvents();
                    }
                }
            });
            fragment2.mSavedStateRegistryController.performRestore(bundle);
            fragment2.onCreate(bundle);
            fragment2.mIsCreated = true;
            if (fragment2.mCalled) {
                fragment2.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
                FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher = this.mDispatcher;
                Bundle bundle2 = this.mFragment.mSavedFragmentState;
                fragmentLifecycleCallbacksDispatcher.dispatchOnFragmentCreated(false);
                return;
            }
            throw new SuperNotCalledException("Fragment " + fragment2 + " did not call through to super.onCreate()");
        }
        fragment.restoreChildFragmentState(fragment.mSavedFragmentState);
        this.mFragment.mState = 1;
    }

    /* JADX WARN: Code restructure failed: missing block: B:37:0x0096, code lost:
        if (r1 != false) goto L_0x0098;
     */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0032  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0067  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0120  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void destroy() {
        /*
            Method dump skipped, instructions count: 315
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentStateManager.destroy():void");
    }

    public final void destroyFragmentView() {
        View view;
        if (FragmentManager.isLoggingEnabled(3)) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("movefrom CREATE_VIEW: ");
            m.append(this.mFragment);
            Log.d("FragmentManager", m.toString());
        }
        Fragment fragment = this.mFragment;
        ViewGroup viewGroup = fragment.mContainer;
        if (!(viewGroup == null || (view = fragment.mView) == null)) {
            viewGroup.removeView(view);
        }
        Fragment fragment2 = this.mFragment;
        Objects.requireNonNull(fragment2);
        FragmentManagerImpl fragmentManagerImpl = fragment2.mChildFragmentManager;
        Objects.requireNonNull(fragmentManagerImpl);
        fragmentManagerImpl.dispatchStateChange(1);
        if (fragment2.mView != null) {
            FragmentViewLifecycleOwner fragmentViewLifecycleOwner = fragment2.mViewLifecycleOwner;
            Objects.requireNonNull(fragmentViewLifecycleOwner);
            fragmentViewLifecycleOwner.initialize();
            LifecycleRegistry lifecycleRegistry = fragmentViewLifecycleOwner.mLifecycleRegistry;
            Objects.requireNonNull(lifecycleRegistry);
            if (lifecycleRegistry.mState.isAtLeast(Lifecycle.State.CREATED)) {
                fragment2.mViewLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
            }
        }
        fragment2.mState = 1;
        fragment2.mCalled = false;
        fragment2.onDestroyView();
        if (fragment2.mCalled) {
            LoaderManagerImpl.LoaderViewModel loaderViewModel = (LoaderManagerImpl.LoaderViewModel) new ViewModelProvider(fragment2.getViewModelStore(), LoaderManagerImpl.LoaderViewModel.FACTORY).get(LoaderManagerImpl.LoaderViewModel.class);
            Objects.requireNonNull(loaderViewModel);
            SparseArrayCompat<LoaderManagerImpl.LoaderInfo> sparseArrayCompat = loaderViewModel.mLoaders;
            Objects.requireNonNull(sparseArrayCompat);
            int i = sparseArrayCompat.mSize;
            for (int i2 = 0; i2 < i; i2++) {
                SparseArrayCompat<LoaderManagerImpl.LoaderInfo> sparseArrayCompat2 = loaderViewModel.mLoaders;
                Objects.requireNonNull(sparseArrayCompat2);
                Objects.requireNonNull((LoaderManagerImpl.LoaderInfo) sparseArrayCompat2.mValues[i2]);
            }
            fragment2.mPerformedCreateView = false;
            this.mDispatcher.dispatchOnFragmentViewDestroyed(false);
            Fragment fragment3 = this.mFragment;
            fragment3.mContainer = null;
            fragment3.mView = null;
            fragment3.mViewLifecycleOwner = null;
            fragment3.mViewLifecycleOwnerLiveData.setValue(null);
            this.mFragment.mInLayout = false;
            return;
        }
        throw new SuperNotCalledException("Fragment " + fragment2 + " did not call through to super.onDestroyView()");
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0065  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x008c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void detach() {
        /*
            Method dump skipped, instructions count: 253
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentStateManager.detach():void");
    }

    public final void pause() {
        if (FragmentManager.isLoggingEnabled(3)) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("movefrom RESUMED: ");
            m.append(this.mFragment);
            Log.d("FragmentManager", m.toString());
        }
        Fragment fragment = this.mFragment;
        Objects.requireNonNull(fragment);
        FragmentManagerImpl fragmentManagerImpl = fragment.mChildFragmentManager;
        Objects.requireNonNull(fragmentManagerImpl);
        fragmentManagerImpl.dispatchStateChange(5);
        if (fragment.mView != null) {
            fragment.mViewLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
        }
        fragment.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
        fragment.mState = 6;
        fragment.mCalled = true;
        this.mDispatcher.dispatchOnFragmentPaused(false);
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x004a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void resume() {
        /*
            r8 = this;
            r0 = 3
            boolean r0 = androidx.fragment.app.FragmentManager.isLoggingEnabled(r0)
            java.lang.String r1 = "FragmentManager"
            if (r0 == 0) goto L_0x001b
            java.lang.String r0 = "moveto RESUMED: "
            java.lang.StringBuilder r0 = android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1.m(r0)
            androidx.fragment.app.Fragment r2 = r8.mFragment
            r0.append(r2)
            java.lang.String r0 = r0.toString()
            android.util.Log.d(r1, r0)
        L_0x001b:
            androidx.fragment.app.Fragment r0 = r8.mFragment
            java.util.Objects.requireNonNull(r0)
            androidx.fragment.app.Fragment$AnimationInfo r0 = r0.mAnimationInfo
            r2 = 0
            if (r0 != 0) goto L_0x0027
            r0 = r2
            goto L_0x0029
        L_0x0027:
            android.view.View r0 = r0.mFocusedView
        L_0x0029:
            r3 = 1
            r4 = 0
            if (r0 == 0) goto L_0x0094
            androidx.fragment.app.Fragment r5 = r8.mFragment
            android.view.View r5 = r5.mView
            if (r0 != r5) goto L_0x0034
            goto L_0x0040
        L_0x0034:
            android.view.ViewParent r5 = r0.getParent()
        L_0x0038:
            if (r5 == 0) goto L_0x0047
            androidx.fragment.app.Fragment r6 = r8.mFragment
            android.view.View r6 = r6.mView
            if (r5 != r6) goto L_0x0042
        L_0x0040:
            r5 = r3
            goto L_0x0048
        L_0x0042:
            android.view.ViewParent r5 = r5.getParent()
            goto L_0x0038
        L_0x0047:
            r5 = r4
        L_0x0048:
            if (r5 == 0) goto L_0x0094
            boolean r5 = r0.requestFocus()
            r6 = 2
            boolean r6 = androidx.fragment.app.FragmentManager.isLoggingEnabled(r6)
            if (r6 == 0) goto L_0x0094
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "requestFocus: Restoring focused view "
            r6.append(r7)
            r6.append(r0)
            java.lang.String r0 = " "
            r6.append(r0)
            if (r5 == 0) goto L_0x006e
            java.lang.String r0 = "succeeded"
            goto L_0x0070
        L_0x006e:
            java.lang.String r0 = "failed"
        L_0x0070:
            r6.append(r0)
            java.lang.String r0 = " on Fragment "
            r6.append(r0)
            androidx.fragment.app.Fragment r0 = r8.mFragment
            r6.append(r0)
            java.lang.String r0 = " resulting in focused view "
            r6.append(r0)
            androidx.fragment.app.Fragment r0 = r8.mFragment
            android.view.View r0 = r0.mView
            android.view.View r0 = r0.findFocus()
            r6.append(r0)
            java.lang.String r0 = r6.toString()
            android.util.Log.v(r1, r0)
        L_0x0094:
            androidx.fragment.app.Fragment r0 = r8.mFragment
            java.util.Objects.requireNonNull(r0)
            androidx.fragment.app.Fragment$AnimationInfo r0 = r0.ensureAnimationInfo()
            r0.mFocusedView = r2
            androidx.fragment.app.Fragment r0 = r8.mFragment
            java.util.Objects.requireNonNull(r0)
            androidx.fragment.app.FragmentManagerImpl r1 = r0.mChildFragmentManager
            r1.noteStateNotSaved()
            androidx.fragment.app.FragmentManagerImpl r1 = r0.mChildFragmentManager
            r1.execPendingActions(r3)
            r1 = 7
            r0.mState = r1
            r0.mCalled = r3
            androidx.lifecycle.LifecycleRegistry r3 = r0.mLifecycleRegistry
            androidx.lifecycle.Lifecycle$Event r5 = androidx.lifecycle.Lifecycle.Event.ON_RESUME
            r3.handleLifecycleEvent(r5)
            android.view.View r3 = r0.mView
            if (r3 == 0) goto L_0x00c3
            androidx.fragment.app.FragmentViewLifecycleOwner r3 = r0.mViewLifecycleOwner
            r3.handleLifecycleEvent(r5)
        L_0x00c3:
            androidx.fragment.app.FragmentManagerImpl r0 = r0.mChildFragmentManager
            java.util.Objects.requireNonNull(r0)
            r0.mStateSaved = r4
            r0.mStopped = r4
            androidx.fragment.app.FragmentManagerViewModel r3 = r0.mNonConfig
            java.util.Objects.requireNonNull(r3)
            r3.mIsStateSaved = r4
            r0.dispatchStateChange(r1)
            androidx.fragment.app.FragmentLifecycleCallbacksDispatcher r0 = r8.mDispatcher
            r0.dispatchOnFragmentResumed(r4)
            androidx.fragment.app.Fragment r8 = r8.mFragment
            r8.mSavedFragmentState = r2
            r8.mSavedViewState = r2
            r8.mSavedViewRegistryState = r2
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentStateManager.resume():void");
    }

    public final void start() {
        if (FragmentManager.isLoggingEnabled(3)) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("moveto STARTED: ");
            m.append(this.mFragment);
            Log.d("FragmentManager", m.toString());
        }
        Fragment fragment = this.mFragment;
        Objects.requireNonNull(fragment);
        fragment.mChildFragmentManager.noteStateNotSaved();
        fragment.mChildFragmentManager.execPendingActions(true);
        fragment.mState = 5;
        fragment.mCalled = false;
        fragment.onStart();
        if (fragment.mCalled) {
            LifecycleRegistry lifecycleRegistry = fragment.mLifecycleRegistry;
            Lifecycle.Event event = Lifecycle.Event.ON_START;
            lifecycleRegistry.handleLifecycleEvent(event);
            if (fragment.mView != null) {
                fragment.mViewLifecycleOwner.handleLifecycleEvent(event);
            }
            FragmentManagerImpl fragmentManagerImpl = fragment.mChildFragmentManager;
            Objects.requireNonNull(fragmentManagerImpl);
            fragmentManagerImpl.mStateSaved = false;
            fragmentManagerImpl.mStopped = false;
            FragmentManagerViewModel fragmentManagerViewModel = fragmentManagerImpl.mNonConfig;
            Objects.requireNonNull(fragmentManagerViewModel);
            fragmentManagerViewModel.mIsStateSaved = false;
            fragmentManagerImpl.dispatchStateChange(5);
            this.mDispatcher.dispatchOnFragmentStarted(false);
            return;
        }
        throw new SuperNotCalledException("Fragment " + fragment + " did not call through to super.onStart()");
    }

    public final void stop() {
        if (FragmentManager.isLoggingEnabled(3)) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("movefrom STARTED: ");
            m.append(this.mFragment);
            Log.d("FragmentManager", m.toString());
        }
        Fragment fragment = this.mFragment;
        Objects.requireNonNull(fragment);
        FragmentManagerImpl fragmentManagerImpl = fragment.mChildFragmentManager;
        Objects.requireNonNull(fragmentManagerImpl);
        fragmentManagerImpl.mStopped = true;
        FragmentManagerViewModel fragmentManagerViewModel = fragmentManagerImpl.mNonConfig;
        Objects.requireNonNull(fragmentManagerViewModel);
        fragmentManagerViewModel.mIsStateSaved = true;
        fragmentManagerImpl.dispatchStateChange(4);
        if (fragment.mView != null) {
            fragment.mViewLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        }
        fragment.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        fragment.mState = 4;
        fragment.mCalled = false;
        fragment.onStop();
        if (fragment.mCalled) {
            this.mDispatcher.dispatchOnFragmentStopped(false);
            return;
        }
        throw new SuperNotCalledException("Fragment " + fragment + " did not call through to super.onStop()");
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0031, code lost:
        r1 = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0039, code lost:
        if (r1 >= r0.mAdded.size()) goto L_0x004f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x003b, code lost:
        r4 = r0.mAdded.get(r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0045, code lost:
        if (r4.mContainer != r2) goto L_0x0031;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0047, code lost:
        r4 = r4.mView;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0049, code lost:
        if (r4 == null) goto L_0x0031;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x004b, code lost:
        r3 = r2.indexOfChild(r4);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void addViewToContainer() {
        /*
            r7 = this;
            androidx.fragment.app.FragmentStore r0 = r7.mFragmentStore
            androidx.fragment.app.Fragment r1 = r7.mFragment
            java.util.Objects.requireNonNull(r0)
            android.view.ViewGroup r2 = r1.mContainer
            r3 = -1
            if (r2 != 0) goto L_0x000d
            goto L_0x004f
        L_0x000d:
            java.util.ArrayList<androidx.fragment.app.Fragment> r4 = r0.mAdded
            int r1 = r4.indexOf(r1)
            int r4 = r1 + (-1)
        L_0x0015:
            if (r4 < 0) goto L_0x0031
            java.util.ArrayList<androidx.fragment.app.Fragment> r5 = r0.mAdded
            java.lang.Object r5 = r5.get(r4)
            androidx.fragment.app.Fragment r5 = (androidx.fragment.app.Fragment) r5
            android.view.ViewGroup r6 = r5.mContainer
            if (r6 != r2) goto L_0x002e
            android.view.View r5 = r5.mView
            if (r5 == 0) goto L_0x002e
            int r0 = r2.indexOfChild(r5)
            int r3 = r0 + 1
            goto L_0x004f
        L_0x002e:
            int r4 = r4 + (-1)
            goto L_0x0015
        L_0x0031:
            int r1 = r1 + 1
            java.util.ArrayList<androidx.fragment.app.Fragment> r4 = r0.mAdded
            int r4 = r4.size()
            if (r1 >= r4) goto L_0x004f
            java.util.ArrayList<androidx.fragment.app.Fragment> r4 = r0.mAdded
            java.lang.Object r4 = r4.get(r1)
            androidx.fragment.app.Fragment r4 = (androidx.fragment.app.Fragment) r4
            android.view.ViewGroup r5 = r4.mContainer
            if (r5 != r2) goto L_0x0031
            android.view.View r4 = r4.mView
            if (r4 == 0) goto L_0x0031
            int r3 = r2.indexOfChild(r4)
        L_0x004f:
            androidx.fragment.app.Fragment r7 = r7.mFragment
            android.view.ViewGroup r0 = r7.mContainer
            android.view.View r7 = r7.mView
            r0.addView(r7, r3)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentStateManager.addViewToContainer():void");
    }

    public final int computeExpectedState() {
        SpecialEffectsController.Operation.LifecycleImpact lifecycleImpact;
        Fragment fragment = this.mFragment;
        if (fragment.mFragmentManager == null) {
            return fragment.mState;
        }
        int i = this.mFragmentManagerState;
        int ordinal = fragment.mMaxState.ordinal();
        boolean z = false;
        if (ordinal == 1) {
            i = Math.min(i, 0);
        } else if (ordinal == 2) {
            i = Math.min(i, 1);
        } else if (ordinal == 3) {
            i = Math.min(i, 5);
        } else if (ordinal != 4) {
            i = Math.min(i, -1);
        }
        Fragment fragment2 = this.mFragment;
        if (fragment2.mFromLayout) {
            if (fragment2.mInLayout) {
                i = Math.max(this.mFragmentManagerState, 2);
                View view = this.mFragment.mView;
                if (view != null && view.getParent() == null) {
                    i = Math.min(i, 2);
                }
            } else {
                i = this.mFragmentManagerState < 4 ? Math.min(i, fragment2.mState) : Math.min(i, 1);
            }
        }
        if (!this.mFragment.mAdded) {
            i = Math.min(i, 1);
        }
        Fragment fragment3 = this.mFragment;
        ViewGroup viewGroup = fragment3.mContainer;
        SpecialEffectsController.Operation.LifecycleImpact lifecycleImpact2 = null;
        SpecialEffectsController.Operation operation = null;
        if (viewGroup != null) {
            SpecialEffectsController orCreateController = SpecialEffectsController.getOrCreateController(viewGroup, fragment3.getParentFragmentManager().getSpecialEffectsControllerFactory());
            Objects.requireNonNull(orCreateController);
            SpecialEffectsController.Operation findPendingOperation = orCreateController.findPendingOperation(this.mFragment);
            if (findPendingOperation != null) {
                lifecycleImpact = findPendingOperation.mLifecycleImpact;
            } else {
                lifecycleImpact = null;
            }
            Fragment fragment4 = this.mFragment;
            Iterator<SpecialEffectsController.Operation> it = orCreateController.mRunningOperations.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SpecialEffectsController.Operation next = it.next();
                Objects.requireNonNull(next);
                if (next.mFragment.equals(fragment4) && !next.mIsCanceled) {
                    operation = next;
                    break;
                }
            }
            if (operation == null || !(lifecycleImpact == null || lifecycleImpact == SpecialEffectsController.Operation.LifecycleImpact.NONE)) {
                lifecycleImpact2 = lifecycleImpact;
            } else {
                lifecycleImpact2 = operation.mLifecycleImpact;
            }
        }
        if (lifecycleImpact2 == SpecialEffectsController.Operation.LifecycleImpact.ADDING) {
            i = Math.min(i, 6);
        } else if (lifecycleImpact2 == SpecialEffectsController.Operation.LifecycleImpact.REMOVING) {
            i = Math.max(i, 3);
        } else {
            Fragment fragment5 = this.mFragment;
            if (fragment5.mRemoving) {
                if (fragment5.mBackStackNesting > 0) {
                    z = true;
                }
                if (z) {
                    i = Math.min(i, 1);
                } else {
                    i = Math.min(i, -1);
                }
            }
        }
        Fragment fragment6 = this.mFragment;
        if (fragment6.mDeferStart && fragment6.mState < 5) {
            i = Math.min(i, 4);
        }
        if (FragmentManager.isLoggingEnabled(2)) {
            StringBuilder m = ExifInterface$$ExternalSyntheticOutline0.m("computeExpectedState() of ", i, " for ");
            m.append(this.mFragment);
            Log.v("FragmentManager", m.toString());
        }
        return i;
    }

    public final void createView() {
        String str;
        if (!this.mFragment.mFromLayout) {
            if (FragmentManager.isLoggingEnabled(3)) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("moveto CREATE_VIEW: ");
                m.append(this.mFragment);
                Log.d("FragmentManager", m.toString());
            }
            Fragment fragment = this.mFragment;
            LayoutInflater onGetLayoutInflater = fragment.onGetLayoutInflater(fragment.mSavedFragmentState);
            ViewGroup viewGroup = null;
            Fragment fragment2 = this.mFragment;
            ViewGroup viewGroup2 = fragment2.mContainer;
            if (viewGroup2 != null) {
                viewGroup = viewGroup2;
            } else {
                int i = fragment2.mContainerId;
                if (i != 0) {
                    if (i != -1) {
                        FragmentManager fragmentManager = fragment2.mFragmentManager;
                        Objects.requireNonNull(fragmentManager);
                        viewGroup = (ViewGroup) fragmentManager.mContainer.onFindViewById(this.mFragment.mContainerId);
                        if (viewGroup == null) {
                            Fragment fragment3 = this.mFragment;
                            if (!fragment3.mRestored) {
                                try {
                                    str = fragment3.requireContext().getResources().getResourceName(this.mFragment.mContainerId);
                                } catch (Resources.NotFoundException unused) {
                                    str = "unknown";
                                }
                                StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("No view found for id 0x");
                                m2.append(Integer.toHexString(this.mFragment.mContainerId));
                                m2.append(" (");
                                m2.append(str);
                                m2.append(") for fragment ");
                                m2.append(this.mFragment);
                                throw new IllegalArgumentException(m2.toString());
                            }
                        } else if (!(viewGroup instanceof FragmentContainerView)) {
                            FragmentStrictMode.onWrongFragmentContainer(this.mFragment, viewGroup);
                        }
                    } else {
                        StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("Cannot create fragment ");
                        m3.append(this.mFragment);
                        m3.append(" for a container view with no id");
                        throw new IllegalArgumentException(m3.toString());
                    }
                }
            }
            Fragment fragment4 = this.mFragment;
            fragment4.mContainer = viewGroup;
            fragment4.performCreateView(onGetLayoutInflater, viewGroup, fragment4.mSavedFragmentState);
            View view = this.mFragment.mView;
            if (view != null) {
                view.setSaveFromParentEnabled(false);
                Fragment fragment5 = this.mFragment;
                fragment5.mView.setTag(2131427992, fragment5);
                if (viewGroup != null) {
                    addViewToContainer();
                }
                Fragment fragment6 = this.mFragment;
                if (fragment6.mHidden) {
                    fragment6.mView.setVisibility(8);
                }
                View view2 = this.mFragment.mView;
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                if (ViewCompat.Api19Impl.isAttachedToWindow(view2)) {
                    ViewCompat.Api20Impl.requestApplyInsets(this.mFragment.mView);
                } else {
                    final View view3 = this.mFragment.mView;
                    view3.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: androidx.fragment.app.FragmentStateManager.1
                        @Override // android.view.View.OnAttachStateChangeListener
                        public final void onViewDetachedFromWindow(View view4) {
                        }

                        @Override // android.view.View.OnAttachStateChangeListener
                        public final void onViewAttachedToWindow(View view4) {
                            view3.removeOnAttachStateChangeListener(this);
                            View view5 = view3;
                            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
                            ViewCompat.Api20Impl.requestApplyInsets(view5);
                        }
                    });
                }
                Fragment fragment7 = this.mFragment;
                Objects.requireNonNull(fragment7);
                FragmentManagerImpl fragmentManagerImpl = fragment7.mChildFragmentManager;
                Objects.requireNonNull(fragmentManagerImpl);
                fragmentManagerImpl.dispatchStateChange(2);
                FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher = this.mDispatcher;
                View view4 = this.mFragment.mView;
                fragmentLifecycleCallbacksDispatcher.dispatchOnFragmentViewCreated(false);
                int visibility = this.mFragment.mView.getVisibility();
                float alpha = this.mFragment.mView.getAlpha();
                Fragment fragment8 = this.mFragment;
                Objects.requireNonNull(fragment8);
                fragment8.ensureAnimationInfo().mPostOnViewCreatedAlpha = alpha;
                Fragment fragment9 = this.mFragment;
                if (fragment9.mContainer != null && visibility == 0) {
                    View findFocus = fragment9.mView.findFocus();
                    if (findFocus != null) {
                        Fragment fragment10 = this.mFragment;
                        Objects.requireNonNull(fragment10);
                        fragment10.ensureAnimationInfo().mFocusedView = findFocus;
                        if (FragmentManager.isLoggingEnabled(2)) {
                            Log.v("FragmentManager", "requestFocus: Saved focused view " + findFocus + " for Fragment " + this.mFragment);
                        }
                    }
                    this.mFragment.mView.setAlpha(0.0f);
                }
            }
            this.mFragment.mState = 2;
        }
    }

    public final void ensureInflatedView() {
        Fragment fragment = this.mFragment;
        if (fragment.mFromLayout && fragment.mInLayout && !fragment.mPerformedCreateView) {
            if (FragmentManager.isLoggingEnabled(3)) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("moveto CREATE_VIEW: ");
                m.append(this.mFragment);
                Log.d("FragmentManager", m.toString());
            }
            Fragment fragment2 = this.mFragment;
            fragment2.performCreateView(fragment2.onGetLayoutInflater(fragment2.mSavedFragmentState), null, this.mFragment.mSavedFragmentState);
            View view = this.mFragment.mView;
            if (view != null) {
                view.setSaveFromParentEnabled(false);
                Fragment fragment3 = this.mFragment;
                fragment3.mView.setTag(2131427992, fragment3);
                Fragment fragment4 = this.mFragment;
                if (fragment4.mHidden) {
                    fragment4.mView.setVisibility(8);
                }
                Fragment fragment5 = this.mFragment;
                Objects.requireNonNull(fragment5);
                FragmentManagerImpl fragmentManagerImpl = fragment5.mChildFragmentManager;
                Objects.requireNonNull(fragmentManagerImpl);
                fragmentManagerImpl.dispatchStateChange(2);
                FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher = this.mDispatcher;
                View view2 = this.mFragment.mView;
                fragmentLifecycleCallbacksDispatcher.dispatchOnFragmentViewCreated(false);
                this.mFragment.mState = 2;
            }
        }
    }

    public final void moveToExpectedState() {
        ViewGroup viewGroup;
        ViewGroup viewGroup2;
        ViewGroup viewGroup3;
        if (!this.mMovingToState) {
            try {
                this.mMovingToState = true;
                while (true) {
                    int computeExpectedState = computeExpectedState();
                    Fragment fragment = this.mFragment;
                    int i = fragment.mState;
                    if (computeExpectedState == i) {
                        if (fragment.mHiddenChanged) {
                            if (!(fragment.mView == null || (viewGroup = fragment.mContainer) == null)) {
                                SpecialEffectsController orCreateController = SpecialEffectsController.getOrCreateController(viewGroup, fragment.getParentFragmentManager().getSpecialEffectsControllerFactory());
                                if (this.mFragment.mHidden) {
                                    orCreateController.enqueueHide(this);
                                } else {
                                    orCreateController.enqueueShow(this);
                                }
                            }
                            Fragment fragment2 = this.mFragment;
                            FragmentManager fragmentManager = fragment2.mFragmentManager;
                            if (fragmentManager != null && fragment2.mAdded && FragmentManager.isMenuAvailable(fragment2)) {
                                fragmentManager.mNeedMenuInvalidate = true;
                            }
                            this.mFragment.mHiddenChanged = false;
                        }
                        return;
                    } else if (computeExpectedState > i) {
                        switch (i + 1) {
                            case 0:
                                attach();
                                continue;
                            case 1:
                                create();
                                continue;
                            case 2:
                                ensureInflatedView();
                                createView();
                                continue;
                            case 3:
                                activityCreated();
                                continue;
                            case 4:
                                if (!(fragment.mView == null || (viewGroup2 = fragment.mContainer) == null)) {
                                    SpecialEffectsController orCreateController2 = SpecialEffectsController.getOrCreateController(viewGroup2, fragment.getParentFragmentManager().getSpecialEffectsControllerFactory());
                                    SpecialEffectsController.Operation.State from = SpecialEffectsController.Operation.State.from(this.mFragment.mView.getVisibility());
                                    Objects.requireNonNull(orCreateController2);
                                    if (FragmentManager.isLoggingEnabled(2)) {
                                        Log.v("FragmentManager", "SpecialEffectsController: Enqueuing add operation for fragment " + this.mFragment);
                                    }
                                    orCreateController2.enqueue(from, SpecialEffectsController.Operation.LifecycleImpact.ADDING, this);
                                }
                                this.mFragment.mState = 4;
                                continue;
                            case 5:
                                start();
                                continue;
                            case FalsingManager.VERSION /* 6 */:
                                fragment.mState = 6;
                                continue;
                            case 7:
                                resume();
                                continue;
                            default:
                                continue;
                        }
                    } else {
                        switch (i - 1) {
                            case -1:
                                detach();
                                continue;
                            case 0:
                                destroy();
                                continue;
                            case 1:
                                destroyFragmentView();
                                this.mFragment.mState = 1;
                                continue;
                            case 2:
                                fragment.mInLayout = false;
                                fragment.mState = 2;
                                continue;
                            case 3:
                                if (FragmentManager.isLoggingEnabled(3)) {
                                    Log.d("FragmentManager", "movefrom ACTIVITY_CREATED: " + this.mFragment);
                                }
                                Objects.requireNonNull(this.mFragment);
                                Fragment fragment3 = this.mFragment;
                                if (fragment3.mView != null && fragment3.mSavedViewState == null) {
                                    saveViewState();
                                }
                                Fragment fragment4 = this.mFragment;
                                if (!(fragment4.mView == null || (viewGroup3 = fragment4.mContainer) == null)) {
                                    SpecialEffectsController.getOrCreateController(viewGroup3, fragment4.getParentFragmentManager().getSpecialEffectsControllerFactory()).enqueueRemove(this);
                                }
                                this.mFragment.mState = 3;
                                continue;
                            case 4:
                                stop();
                                continue;
                            case 5:
                                fragment.mState = 5;
                                continue;
                            case FalsingManager.VERSION /* 6 */:
                                pause();
                                continue;
                            default:
                                continue;
                        }
                    }
                }
            } finally {
                this.mMovingToState = false;
            }
        } else if (FragmentManager.isLoggingEnabled(2)) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Ignoring re-entrant call to moveToExpectedState() for ");
            m.append(this.mFragment);
            Log.v("FragmentManager", m.toString());
        }
    }

    public final void restoreState(ClassLoader classLoader) {
        Bundle bundle = this.mFragment.mSavedFragmentState;
        if (bundle != null) {
            bundle.setClassLoader(classLoader);
            Fragment fragment = this.mFragment;
            fragment.mSavedViewState = fragment.mSavedFragmentState.getSparseParcelableArray("android:view_state");
            Fragment fragment2 = this.mFragment;
            fragment2.mSavedViewRegistryState = fragment2.mSavedFragmentState.getBundle("android:view_registry_state");
            Fragment fragment3 = this.mFragment;
            fragment3.mTargetWho = fragment3.mSavedFragmentState.getString("android:target_state");
            Fragment fragment4 = this.mFragment;
            if (fragment4.mTargetWho != null) {
                fragment4.mTargetRequestCode = fragment4.mSavedFragmentState.getInt("android:target_req_state", 0);
            }
            Fragment fragment5 = this.mFragment;
            Objects.requireNonNull(fragment5);
            fragment5.mUserVisibleHint = fragment5.mSavedFragmentState.getBoolean("android:user_visible_hint", true);
            Fragment fragment6 = this.mFragment;
            if (!fragment6.mUserVisibleHint) {
                fragment6.mDeferStart = true;
            }
        }
    }

    public final void saveState() {
        FragmentState fragmentState = new FragmentState(this.mFragment);
        Fragment fragment = this.mFragment;
        if (fragment.mState <= -1 || fragmentState.mSavedFragmentState != null) {
            fragmentState.mSavedFragmentState = fragment.mSavedFragmentState;
        } else {
            Bundle bundle = new Bundle();
            Fragment fragment2 = this.mFragment;
            Objects.requireNonNull(fragment2);
            fragment2.onSaveInstanceState(bundle);
            fragment2.mSavedStateRegistryController.performSave(bundle);
            Parcelable saveAllStateInternal = fragment2.mChildFragmentManager.saveAllStateInternal();
            if (saveAllStateInternal != null) {
                bundle.putParcelable("android:support:fragments", saveAllStateInternal);
            }
            this.mDispatcher.dispatchOnFragmentSaveInstanceState(false);
            if (bundle.isEmpty()) {
                bundle = null;
            }
            if (this.mFragment.mView != null) {
                saveViewState();
            }
            if (this.mFragment.mSavedViewState != null) {
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putSparseParcelableArray("android:view_state", this.mFragment.mSavedViewState);
            }
            if (this.mFragment.mSavedViewRegistryState != null) {
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putBundle("android:view_registry_state", this.mFragment.mSavedViewRegistryState);
            }
            if (!this.mFragment.mUserVisibleHint) {
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putBoolean("android:user_visible_hint", this.mFragment.mUserVisibleHint);
            }
            fragmentState.mSavedFragmentState = bundle;
            if (this.mFragment.mTargetWho != null) {
                if (bundle == null) {
                    fragmentState.mSavedFragmentState = new Bundle();
                }
                fragmentState.mSavedFragmentState.putString("android:target_state", this.mFragment.mTargetWho);
                int i = this.mFragment.mTargetRequestCode;
                if (i != 0) {
                    fragmentState.mSavedFragmentState.putInt("android:target_req_state", i);
                }
            }
        }
        this.mFragmentStore.setSavedState(this.mFragment.mWho, fragmentState);
    }

    public final void saveViewState() {
        if (this.mFragment.mView != null) {
            SparseArray<Parcelable> sparseArray = new SparseArray<>();
            this.mFragment.mView.saveHierarchyState(sparseArray);
            if (sparseArray.size() > 0) {
                this.mFragment.mSavedViewState = sparseArray;
            }
            Bundle bundle = new Bundle();
            FragmentViewLifecycleOwner fragmentViewLifecycleOwner = this.mFragment.mViewLifecycleOwner;
            Objects.requireNonNull(fragmentViewLifecycleOwner);
            fragmentViewLifecycleOwner.mSavedStateRegistryController.performSave(bundle);
            if (!bundle.isEmpty()) {
                this.mFragment.mSavedViewRegistryState = bundle;
            }
        }
    }

    public FragmentStateManager(FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher, FragmentStore fragmentStore, ClassLoader classLoader, FragmentFactory fragmentFactory, FragmentState fragmentState) {
        this.mDispatcher = fragmentLifecycleCallbacksDispatcher;
        this.mFragmentStore = fragmentStore;
        Fragment instantiate = fragmentFactory.instantiate(fragmentState.mClassName);
        Bundle bundle = fragmentState.mArguments;
        if (bundle != null) {
            bundle.setClassLoader(classLoader);
        }
        instantiate.setArguments(fragmentState.mArguments);
        instantiate.mWho = fragmentState.mWho;
        instantiate.mFromLayout = fragmentState.mFromLayout;
        instantiate.mRestored = true;
        instantiate.mFragmentId = fragmentState.mFragmentId;
        instantiate.mContainerId = fragmentState.mContainerId;
        instantiate.mTag = fragmentState.mTag;
        instantiate.mRetainInstance = fragmentState.mRetainInstance;
        instantiate.mRemoving = fragmentState.mRemoving;
        instantiate.mDetached = fragmentState.mDetached;
        instantiate.mHidden = fragmentState.mHidden;
        instantiate.mMaxState = Lifecycle.State.values()[fragmentState.mMaxLifecycleState];
        Bundle bundle2 = fragmentState.mSavedFragmentState;
        if (bundle2 != null) {
            instantiate.mSavedFragmentState = bundle2;
        } else {
            instantiate.mSavedFragmentState = new Bundle();
        }
        this.mFragment = instantiate;
        if (FragmentManager.isLoggingEnabled(2)) {
            Log.v("FragmentManager", "Instantiated fragment " + instantiate);
        }
    }

    public FragmentStateManager(FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher, FragmentStore fragmentStore, Fragment fragment, FragmentState fragmentState) {
        this.mDispatcher = fragmentLifecycleCallbacksDispatcher;
        this.mFragmentStore = fragmentStore;
        this.mFragment = fragment;
        fragment.mSavedViewState = null;
        fragment.mSavedViewRegistryState = null;
        fragment.mBackStackNesting = 0;
        fragment.mInLayout = false;
        fragment.mAdded = false;
        Fragment fragment2 = fragment.mTarget;
        fragment.mTargetWho = fragment2 != null ? fragment2.mWho : null;
        fragment.mTarget = null;
        Bundle bundle = fragmentState.mSavedFragmentState;
        if (bundle != null) {
            fragment.mSavedFragmentState = bundle;
        } else {
            fragment.mSavedFragmentState = new Bundle();
        }
    }
}
