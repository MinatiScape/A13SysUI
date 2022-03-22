package androidx.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Trace;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import androidx.activity.ComponentActivity;
import androidx.activity.contextaware.ContextAwareHelper;
import androidx.activity.contextaware.OnContextAvailableListener;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.ActivityResultRegistryOwner;
import androidx.core.view.MenuHostHelper;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ReportFragment;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryController;
import androidx.savedstate.SavedStateRegistryOwner;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public class ComponentActivity extends androidx.core.app.ComponentActivity implements ViewModelStoreOwner, SavedStateRegistryOwner, OnBackPressedDispatcherOwner, ActivityResultRegistryOwner {
    public final LifecycleRegistry mLifecycleRegistry;
    public ViewModelStore mViewModelStore;
    public final ContextAwareHelper mContextAwareHelper = new ContextAwareHelper();
    public final MenuHostHelper mMenuHostHelper = new MenuHostHelper();
    public final SavedStateRegistryController mSavedStateRegistryController = new SavedStateRegistryController(this);
    public final OnBackPressedDispatcher mOnBackPressedDispatcher = new OnBackPressedDispatcher(new Runnable() { // from class: androidx.activity.ComponentActivity.1
        @Override // java.lang.Runnable
        public final void run() {
            try {
                ComponentActivity.super.onBackPressed();
            } catch (IllegalStateException e) {
                if (!TextUtils.equals(e.getMessage(), "Can not perform this action after onSaveInstanceState")) {
                    throw e;
                }
            }
        }
    });
    public final AnonymousClass2 mActivityResultRegistry = new ActivityResultRegistry() { // from class: androidx.activity.ComponentActivity.2
    };

    /* loaded from: classes.dex */
    public static final class NonConfigurationInstances {
        public ViewModelStore viewModelStore;
    }

    @Override // android.app.Activity
    public void setContentView(int i) {
        initViewTreeOwners();
        super.setContentView(i);
    }

    @Override // android.app.Activity
    @Deprecated
    public final void startActivityForResult(@SuppressLint({"UnknownNullness"}) Intent intent, int i) {
        super.startActivityForResult(intent, i);
    }

    @Override // android.app.Activity
    @Deprecated
    public final void startIntentSenderForResult(@SuppressLint({"UnknownNullness"}) IntentSender intentSender, int i, Intent intent, int i2, int i3, int i4) throws IntentSender.SendIntentException {
        super.startIntentSenderForResult(intentSender, i, intent, i2, i3, i4);
    }

    public final void addOnContextAvailableListener(OnContextAvailableListener onContextAvailableListener) {
        ContextAwareHelper contextAwareHelper = this.mContextAwareHelper;
        Objects.requireNonNull(contextAwareHelper);
        if (contextAwareHelper.mContext != null) {
            onContextAvailableListener.onContextAvailable();
        }
        contextAwareHelper.mListeners.add(onContextAvailableListener);
    }

    @Override // androidx.savedstate.SavedStateRegistryOwner
    public final SavedStateRegistry getSavedStateRegistry() {
        SavedStateRegistryController savedStateRegistryController = this.mSavedStateRegistryController;
        Objects.requireNonNull(savedStateRegistryController);
        return savedStateRegistryController.mRegistry;
    }

    @Override // android.app.Activity
    @Deprecated
    public void onActivityResult(int i, int i2, Intent intent) {
        if (!dispatchResult(i, i2, intent)) {
            super.onActivityResult(i, i2, intent);
        }
    }

    @Override // android.app.Activity
    public final void onBackPressed() {
        this.mOnBackPressedDispatcher.onBackPressed();
    }

    @Override // androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        this.mSavedStateRegistryController.performRestore(bundle);
        ContextAwareHelper contextAwareHelper = this.mContextAwareHelper;
        Objects.requireNonNull(contextAwareHelper);
        contextAwareHelper.mContext = this;
        Iterator it = contextAwareHelper.mListeners.iterator();
        while (it.hasNext()) {
            ((OnContextAvailableListener) it.next()).onContextAvailable();
        }
        super.onCreate(bundle);
        ReportFragment.injectIfNeededIn(this);
    }

    @Override // android.app.Activity
    @Deprecated
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (!dispatchResult(i, -1, new Intent().putExtra("androidx.activity.result.contract.extra.PERMISSIONS", strArr).putExtra("androidx.activity.result.contract.extra.PERMISSION_GRANT_RESULTS", iArr))) {
            super.onRequestPermissionsResult(i, strArr, iArr);
        }
    }

    @Override // android.app.Activity
    public final Object onRetainNonConfigurationInstance() {
        NonConfigurationInstances nonConfigurationInstances;
        ViewModelStore viewModelStore = this.mViewModelStore;
        if (viewModelStore == null && (nonConfigurationInstances = (NonConfigurationInstances) getLastNonConfigurationInstance()) != null) {
            viewModelStore = nonConfigurationInstances.viewModelStore;
        }
        if (viewModelStore == null) {
            return null;
        }
        NonConfigurationInstances nonConfigurationInstances2 = new NonConfigurationInstances();
        nonConfigurationInstances2.viewModelStore = viewModelStore;
        return nonConfigurationInstances2;
    }

    @Override // androidx.core.app.ComponentActivity, android.app.Activity
    public final void onSaveInstanceState(Bundle bundle) {
        LifecycleRegistry lifecycleRegistry = this.mLifecycleRegistry;
        if (lifecycleRegistry instanceof LifecycleRegistry) {
            lifecycleRegistry.setCurrentState(Lifecycle.State.CREATED);
        }
        super.onSaveInstanceState(bundle);
        this.mSavedStateRegistryController.performSave(bundle);
    }

    @Override // android.app.Activity
    @Deprecated
    public final void startActivityForResult(@SuppressLint({"UnknownNullness"}) Intent intent, int i, Bundle bundle) {
        super.startActivityForResult(intent, i, bundle);
    }

    @Override // android.app.Activity
    @Deprecated
    public final void startIntentSenderForResult(@SuppressLint({"UnknownNullness"}) IntentSender intentSender, int i, Intent intent, int i2, int i3, int i4, Bundle bundle) throws IntentSender.SendIntentException {
        super.startIntentSenderForResult(intentSender, i, intent, i2, i3, i4, bundle);
    }

    /* JADX WARN: Type inference failed for: r1v4, types: [androidx.activity.ComponentActivity$2] */
    /* JADX WARN: Type inference failed for: r2v0, types: [androidx.activity.ComponentActivity$1] */
    public ComponentActivity() {
        LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this, true);
        this.mLifecycleRegistry = lifecycleRegistry;
        new AtomicInteger();
        lifecycleRegistry.addObserver(new LifecycleEventObserver() { // from class: androidx.activity.ComponentActivity.3
            @Override // androidx.lifecycle.LifecycleEventObserver
            public final void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
                View view;
                if (event == Lifecycle.Event.ON_STOP) {
                    Window window = ComponentActivity.this.getWindow();
                    if (window != null) {
                        view = window.peekDecorView();
                    } else {
                        view = null;
                    }
                    if (view != null) {
                        view.cancelPendingInputEvents();
                    }
                }
            }
        });
        lifecycleRegistry.addObserver(new LifecycleEventObserver() { // from class: androidx.activity.ComponentActivity.4
            @Override // androidx.lifecycle.LifecycleEventObserver
            public final void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    ContextAwareHelper contextAwareHelper = ComponentActivity.this.mContextAwareHelper;
                    Objects.requireNonNull(contextAwareHelper);
                    contextAwareHelper.mContext = null;
                    if (!ComponentActivity.this.isChangingConfigurations()) {
                        ComponentActivity.this.getViewModelStore().clear();
                    }
                }
            }
        });
        lifecycleRegistry.addObserver(new LifecycleEventObserver() { // from class: androidx.activity.ComponentActivity.5
            @Override // androidx.lifecycle.LifecycleEventObserver
            public final void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
                ComponentActivity componentActivity = ComponentActivity.this;
                Objects.requireNonNull(componentActivity);
                if (componentActivity.mViewModelStore == null) {
                    NonConfigurationInstances nonConfigurationInstances = (NonConfigurationInstances) componentActivity.getLastNonConfigurationInstance();
                    if (nonConfigurationInstances != null) {
                        componentActivity.mViewModelStore = nonConfigurationInstances.viewModelStore;
                    }
                    if (componentActivity.mViewModelStore == null) {
                        componentActivity.mViewModelStore = new ViewModelStore();
                    }
                }
                ComponentActivity componentActivity2 = ComponentActivity.this;
                Objects.requireNonNull(componentActivity2);
                componentActivity2.mLifecycleRegistry.removeObserver(this);
            }
        });
        getSavedStateRegistry().registerSavedStateProvider("android:support:activity-result", new SavedStateRegistry.SavedStateProvider() { // from class: androidx.activity.ComponentActivity$$ExternalSyntheticLambda1
            @Override // androidx.savedstate.SavedStateRegistry.SavedStateProvider
            public final Bundle saveState() {
                ComponentActivity componentActivity = ComponentActivity.this;
                Objects.requireNonNull(componentActivity);
                Bundle bundle = new Bundle();
                ComponentActivity.AnonymousClass2 r3 = componentActivity.mActivityResultRegistry;
                Objects.requireNonNull(r3);
                bundle.putIntegerArrayList("KEY_COMPONENT_ACTIVITY_REGISTERED_RCS", new ArrayList<>(r3.mKeyToRc.values()));
                bundle.putStringArrayList("KEY_COMPONENT_ACTIVITY_REGISTERED_KEYS", new ArrayList<>(r3.mKeyToRc.keySet()));
                bundle.putStringArrayList("KEY_COMPONENT_ACTIVITY_LAUNCHED_KEYS", new ArrayList<>(r3.mLaunchedKeys));
                bundle.putBundle("KEY_COMPONENT_ACTIVITY_PENDING_RESULT", (Bundle) r3.mPendingResults.clone());
                bundle.putSerializable("KEY_COMPONENT_ACTIVITY_RANDOM_OBJECT", r3.mRandom);
                return bundle;
            }
        });
        addOnContextAvailableListener(new OnContextAvailableListener() { // from class: androidx.activity.ComponentActivity$$ExternalSyntheticLambda0
            @Override // androidx.activity.contextaware.OnContextAvailableListener
            public final void onContextAvailable() {
                ComponentActivity componentActivity = ComponentActivity.this;
                Objects.requireNonNull(componentActivity);
                Bundle consumeRestoredStateForKey = componentActivity.getSavedStateRegistry().consumeRestoredStateForKey("android:support:activity-result");
                if (consumeRestoredStateForKey != null) {
                    ComponentActivity.AnonymousClass2 r7 = componentActivity.mActivityResultRegistry;
                    Objects.requireNonNull(r7);
                    ArrayList<Integer> integerArrayList = consumeRestoredStateForKey.getIntegerArrayList("KEY_COMPONENT_ACTIVITY_REGISTERED_RCS");
                    ArrayList<String> stringArrayList = consumeRestoredStateForKey.getStringArrayList("KEY_COMPONENT_ACTIVITY_REGISTERED_KEYS");
                    if (!(stringArrayList == null || integerArrayList == null)) {
                        r7.mLaunchedKeys = consumeRestoredStateForKey.getStringArrayList("KEY_COMPONENT_ACTIVITY_LAUNCHED_KEYS");
                        r7.mRandom = (Random) consumeRestoredStateForKey.getSerializable("KEY_COMPONENT_ACTIVITY_RANDOM_OBJECT");
                        r7.mPendingResults.putAll(consumeRestoredStateForKey.getBundle("KEY_COMPONENT_ACTIVITY_PENDING_RESULT"));
                        for (int i = 0; i < stringArrayList.size(); i++) {
                            String str = stringArrayList.get(i);
                            if (r7.mKeyToRc.containsKey(str)) {
                                Integer num = (Integer) r7.mKeyToRc.remove(str);
                                if (!r7.mPendingResults.containsKey(str)) {
                                    r7.mRcToKey.remove(num);
                                }
                            }
                            int intValue = integerArrayList.get(i).intValue();
                            String str2 = stringArrayList.get(i);
                            r7.mRcToKey.put(Integer.valueOf(intValue), str2);
                            r7.mKeyToRc.put(str2, Integer.valueOf(intValue));
                        }
                    }
                }
            }
        });
    }

    @Override // android.app.Activity
    public void addContentView(@SuppressLint({"UnknownNullness", "MissingNullability"}) View view, @SuppressLint({"UnknownNullness", "MissingNullability"}) ViewGroup.LayoutParams layoutParams) {
        initViewTreeOwners();
        super.addContentView(view, layoutParams);
    }

    @Override // androidx.lifecycle.ViewModelStoreOwner
    public final ViewModelStore getViewModelStore() {
        if (getApplication() != null) {
            if (this.mViewModelStore == null) {
                NonConfigurationInstances nonConfigurationInstances = (NonConfigurationInstances) getLastNonConfigurationInstance();
                if (nonConfigurationInstances != null) {
                    this.mViewModelStore = nonConfigurationInstances.viewModelStore;
                }
                if (this.mViewModelStore == null) {
                    this.mViewModelStore = new ViewModelStore();
                }
            }
            return this.mViewModelStore;
        }
        throw new IllegalStateException("Your activity is not yet attached to the Application instance. You can't request ViewModel before onCreate call.");
    }

    public final void initViewTreeOwners() {
        getWindow().getDecorView().setTag(2131429194, this);
        getWindow().getDecorView().setTag(2131429196, this);
        getWindow().getDecorView().setTag(2131429195, this);
    }

    @Override // android.app.Activity
    public final boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuHostHelper menuHostHelper = this.mMenuHostHelper;
        getMenuInflater();
        Objects.requireNonNull(menuHostHelper);
        Iterator<MenuProvider> it = menuHostHelper.mMenuProviders.iterator();
        while (it.hasNext()) {
            it.next().onCreateMenu();
        }
        return true;
    }

    @Override // android.app.Activity
    public final boolean onOptionsItemSelected(MenuItem menuItem) {
        if (super.onOptionsItemSelected(menuItem)) {
            return true;
        }
        MenuHostHelper menuHostHelper = this.mMenuHostHelper;
        Objects.requireNonNull(menuHostHelper);
        Iterator<MenuProvider> it = menuHostHelper.mMenuProviders.iterator();
        while (it.hasNext()) {
            if (it.next().onMenuItemSelected()) {
                return true;
            }
        }
        return false;
    }

    @Override // android.app.Activity
    public final void reportFullyDrawn() {
        try {
            if (Trace.isEnabled()) {
                Trace.beginSection("reportFullyDrawn() for ComponentActivity");
            }
            super.reportFullyDrawn();
            Trace.endSection();
        } catch (Throwable th) {
            Trace.endSection();
            throw th;
        }
    }

    @Override // android.app.Activity
    public void setContentView(@SuppressLint({"UnknownNullness", "MissingNullability"}) View view) {
        initViewTreeOwners();
        super.setContentView(view);
    }

    @Override // android.app.Activity
    public void setContentView(@SuppressLint({"UnknownNullness", "MissingNullability"}) View view, @SuppressLint({"UnknownNullness", "MissingNullability"}) ViewGroup.LayoutParams layoutParams) {
        initViewTreeOwners();
        super.setContentView(view, layoutParams);
    }

    @Override // androidx.activity.result.ActivityResultRegistryOwner
    public final ActivityResultRegistry getActivityResultRegistry() {
        return this.mActivityResultRegistry;
    }

    @Override // androidx.core.app.ComponentActivity, androidx.lifecycle.LifecycleOwner
    public final Lifecycle getLifecycle() {
        return this.mLifecycleRegistry;
    }

    @Override // androidx.activity.OnBackPressedDispatcherOwner
    public final OnBackPressedDispatcher getOnBackPressedDispatcher() {
        return this.mOnBackPressedDispatcher;
    }
}
