package androidx.appcompat.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.app.TwilightManager;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.view.SupportActionModeWrapper;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.ViewPropertyAnimatorCompatSet;
import androidx.appcompat.view.WindowCallbackWrapper;
import androidx.appcompat.view.menu.ListMenuPresenter;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.widget.ActionBarContextView;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.appcompat.widget.ContentFrameLayout;
import androidx.appcompat.widget.DecorContentParent;
import androidx.appcompat.widget.ResourceManagerInternal;
import androidx.appcompat.widget.ViewUtils;
import androidx.collection.LongSparseArray;
import androidx.collection.SimpleArrayMap;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.leanback.R$styleable;
import com.airbnb.lottie.L;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public final class AppCompatDelegateImpl extends AppCompatDelegate implements MenuBuilder.Callback, LayoutInflater.Factory2 {
    public WindowDecorActionBar mActionBar;
    public ActionMenuPresenterCallback mActionMenuPresenterCallback;
    public ActionMode mActionMode;
    public PopupWindow mActionModePopup;
    public ActionBarContextView mActionModeView;
    public boolean mActivityHandlesUiMode;
    public boolean mActivityHandlesUiModeChecked;
    public final AppCompatCallback mAppCompatCallback;
    public AppCompatViewInflater mAppCompatViewInflater;
    public AppCompatWindowCallback mAppCompatWindowCallback;
    public AutoBatteryNightModeManager mAutoBatteryNightModeManager;
    public AutoTimeNightModeManager mAutoTimeNightModeManager;
    public boolean mBaseContextAttached;
    public boolean mClosingActionMenu;
    public final Context mContext;
    public boolean mCreated;
    public DecorContentParent mDecorContentParent;
    public boolean mDestroyed;
    public Configuration mEffectiveConfiguration;
    public boolean mEnableDefaultActionBarUp;
    public boolean mFeatureIndeterminateProgress;
    public boolean mFeatureProgress;
    public boolean mHasActionBar;
    public final Object mHost;
    public int mInvalidatePanelMenuFeatures;
    public boolean mInvalidatePanelMenuPosted;
    public boolean mIsFloating;
    public int mLocalNightMode;
    public boolean mLongPressBackDown;
    public SupportMenuInflater mMenuInflater;
    public boolean mOverlayActionBar;
    public boolean mOverlayActionMode;
    public PanelMenuPresenterCallback mPanelMenuPresenterCallback;
    public PanelFeatureState[] mPanels;
    public PanelFeatureState mPreparedPanel;
    public AnonymousClass6 mShowActionModePopup;
    public View mStatusGuard;
    public ViewGroup mSubDecor;
    public boolean mSubDecorInstalled;
    public Rect mTempRect1;
    public Rect mTempRect2;
    public int mThemeResId;
    public CharSequence mTitle;
    public TextView mTitleView;
    public Window mWindow;
    public boolean mWindowNoTitle;
    public static final SimpleArrayMap<String, Integer> sLocalNightModes = new SimpleArrayMap<>();
    public static final int[] sWindowBackgroundStyleable = {16842836};
    public static final boolean sCanReturnDifferentContext = !"robolectric".equals(Build.FINGERPRINT);
    public static final boolean sCanApplyOverrideConfiguration = true;
    public ViewPropertyAnimatorCompat mFadeAnim = null;
    public boolean mHandleNativeActionModes = true;
    public final AnonymousClass2 mInvalidatePanelMenuRunnable = new AnonymousClass2();

    /* renamed from: androidx.appcompat.app.AppCompatDelegateImpl$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements Runnable {
        public AnonymousClass2() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
            if ((appCompatDelegateImpl.mInvalidatePanelMenuFeatures & 1) != 0) {
                appCompatDelegateImpl.doInvalidatePanelMenu(0);
            }
            AppCompatDelegateImpl appCompatDelegateImpl2 = AppCompatDelegateImpl.this;
            if ((appCompatDelegateImpl2.mInvalidatePanelMenuFeatures & 4096) != 0) {
                appCompatDelegateImpl2.doInvalidatePanelMenu(108);
            }
            AppCompatDelegateImpl appCompatDelegateImpl3 = AppCompatDelegateImpl.this;
            appCompatDelegateImpl3.mInvalidatePanelMenuPosted = false;
            appCompatDelegateImpl3.mInvalidatePanelMenuFeatures = 0;
        }
    }

    /* renamed from: androidx.appcompat.app.AppCompatDelegateImpl$5  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass5 implements ContentFrameLayout.OnAttachListener {
        public AnonymousClass5() {
        }
    }

    /* loaded from: classes.dex */
    public final class ActionMenuPresenterCallback implements MenuPresenter.Callback {
        public ActionMenuPresenterCallback() {
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter.Callback
        public final void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
            AppCompatDelegateImpl.this.checkCloseActionMenu(menuBuilder);
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter.Callback
        public final boolean onOpenSubMenu(MenuBuilder menuBuilder) {
            Window.Callback windowCallback = AppCompatDelegateImpl.this.getWindowCallback();
            if (windowCallback == null) {
                return true;
            }
            windowCallback.onMenuOpened(108, menuBuilder);
            return true;
        }
    }

    /* loaded from: classes.dex */
    public class ActionModeCallbackWrapperV9 implements ActionMode.Callback {
        public ActionMode.Callback mWrapped;

        public ActionModeCallbackWrapperV9(SupportActionModeWrapper.CallbackWrapper callbackWrapper) {
            this.mWrapped = callbackWrapper;
        }

        @Override // androidx.appcompat.view.ActionMode.Callback
        public final boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return this.mWrapped.onActionItemClicked(actionMode, menuItem);
        }

        @Override // androidx.appcompat.view.ActionMode.Callback
        public final boolean onCreateActionMode(ActionMode actionMode, MenuBuilder menuBuilder) {
            return this.mWrapped.onCreateActionMode(actionMode, menuBuilder);
        }

        @Override // androidx.appcompat.view.ActionMode.Callback
        public final void onDestroyActionMode(ActionMode actionMode) {
            this.mWrapped.onDestroyActionMode(actionMode);
            AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
            if (appCompatDelegateImpl.mActionModePopup != null) {
                appCompatDelegateImpl.mWindow.getDecorView().removeCallbacks(AppCompatDelegateImpl.this.mShowActionModePopup);
            }
            AppCompatDelegateImpl appCompatDelegateImpl2 = AppCompatDelegateImpl.this;
            if (appCompatDelegateImpl2.mActionModeView != null) {
                ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = appCompatDelegateImpl2.mFadeAnim;
                if (viewPropertyAnimatorCompat != null) {
                    viewPropertyAnimatorCompat.cancel();
                }
                AppCompatDelegateImpl appCompatDelegateImpl3 = AppCompatDelegateImpl.this;
                ViewPropertyAnimatorCompat animate = ViewCompat.animate(appCompatDelegateImpl3.mActionModeView);
                animate.alpha(0.0f);
                appCompatDelegateImpl3.mFadeAnim = animate;
                AppCompatDelegateImpl.this.mFadeAnim.setListener(new L() { // from class: androidx.appcompat.app.AppCompatDelegateImpl.ActionModeCallbackWrapperV9.1
                    @Override // androidx.core.view.ViewPropertyAnimatorListener
                    public final void onAnimationEnd() {
                        AppCompatDelegateImpl.this.mActionModeView.setVisibility(8);
                        AppCompatDelegateImpl appCompatDelegateImpl4 = AppCompatDelegateImpl.this;
                        PopupWindow popupWindow = appCompatDelegateImpl4.mActionModePopup;
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        } else if (appCompatDelegateImpl4.mActionModeView.getParent() instanceof View) {
                            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                            ViewCompat.Api20Impl.requestApplyInsets((View) AppCompatDelegateImpl.this.mActionModeView.getParent());
                        }
                        AppCompatDelegateImpl.this.mActionModeView.killMode();
                        AppCompatDelegateImpl.this.mFadeAnim.setListener(null);
                        AppCompatDelegateImpl appCompatDelegateImpl5 = AppCompatDelegateImpl.this;
                        appCompatDelegateImpl5.mFadeAnim = null;
                        ViewGroup viewGroup = appCompatDelegateImpl5.mSubDecor;
                        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
                        ViewCompat.Api20Impl.requestApplyInsets(viewGroup);
                    }
                });
            }
            AppCompatCallback appCompatCallback = AppCompatDelegateImpl.this.mAppCompatCallback;
            if (appCompatCallback != null) {
                appCompatCallback.onSupportActionModeFinished();
            }
            AppCompatDelegateImpl appCompatDelegateImpl4 = AppCompatDelegateImpl.this;
            appCompatDelegateImpl4.mActionMode = null;
            ViewGroup viewGroup = appCompatDelegateImpl4.mSubDecor;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api20Impl.requestApplyInsets(viewGroup);
        }

        @Override // androidx.appcompat.view.ActionMode.Callback
        public final boolean onPrepareActionMode(ActionMode actionMode, MenuBuilder menuBuilder) {
            ViewGroup viewGroup = AppCompatDelegateImpl.this.mSubDecor;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api20Impl.requestApplyInsets(viewGroup);
            return this.mWrapped.onPrepareActionMode(actionMode, menuBuilder);
        }
    }

    /* loaded from: classes.dex */
    public class AppCompatWindowCallback extends WindowCallbackWrapper {
        @Override // android.view.Window.Callback
        public final void onContentChanged() {
        }

        @Override // android.view.Window.Callback
        public final android.view.ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
            return null;
        }

        public AppCompatWindowCallback(Window.Callback callback) {
            super(callback);
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public final boolean dispatchKeyEvent(KeyEvent keyEvent) {
            if (AppCompatDelegateImpl.this.dispatchKeyEvent(keyEvent) || super.dispatchKeyEvent(keyEvent)) {
                return true;
            }
            return false;
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public final boolean onCreatePanelMenu(int i, Menu menu) {
            if (i != 0 || (menu instanceof MenuBuilder)) {
                return super.onCreatePanelMenu(i, menu);
            }
            return false;
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public final boolean onPreparePanel(int i, View view, Menu menu) {
            MenuBuilder menuBuilder;
            if (menu instanceof MenuBuilder) {
                menuBuilder = (MenuBuilder) menu;
            } else {
                menuBuilder = null;
            }
            if (i == 0 && menuBuilder == null) {
                return false;
            }
            if (menuBuilder != null) {
                menuBuilder.mOverrideVisibleItems = true;
            }
            boolean onPreparePanel = super.onPreparePanel(i, view, menu);
            if (menuBuilder != null) {
                menuBuilder.mOverrideVisibleItems = false;
            }
            return onPreparePanel;
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public final void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i) {
            MenuBuilder menuBuilder = AppCompatDelegateImpl.this.getPanelState(0).menu;
            if (menuBuilder != null) {
                super.onProvideKeyboardShortcuts(list, menuBuilder, i);
            } else {
                super.onProvideKeyboardShortcuts(list, menu, i);
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:73:0x01b6, code lost:
            if (androidx.core.view.ViewCompat.Api19Impl.isLaidOut(r8) != false) goto L_0x01ba;
         */
        /* JADX WARN: Type inference failed for: r0v31, types: [androidx.appcompat.app.AppCompatDelegateImpl$6] */
        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final android.view.ActionMode onWindowStartingActionMode(android.view.ActionMode.Callback r8, int r9) {
            /*
                Method dump skipped, instructions count: 552
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.app.AppCompatDelegateImpl.AppCompatWindowCallback.onWindowStartingActionMode(android.view.ActionMode$Callback, int):android.view.ActionMode");
        }

        /* JADX WARN: Code restructure failed: missing block: B:16:0x003b, code lost:
            if (r0 != false) goto L_0x006a;
         */
        /* JADX WARN: Code restructure failed: missing block: B:27:0x0068, code lost:
            if (r5 != false) goto L_0x006a;
         */
        /* JADX WARN: Removed duplicated region for block: B:33:? A[RETURN, SYNTHETIC] */
        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final boolean dispatchKeyShortcutEvent(android.view.KeyEvent r6) {
            /*
                r5 = this;
                boolean r0 = super.dispatchKeyShortcutEvent(r6)
                r1 = 0
                r2 = 1
                if (r0 != 0) goto L_0x006f
                androidx.appcompat.app.AppCompatDelegateImpl r5 = androidx.appcompat.app.AppCompatDelegateImpl.this
                int r0 = r6.getKeyCode()
                java.util.Objects.requireNonNull(r5)
                r5.initWindowDecorActionBar()
                androidx.appcompat.app.WindowDecorActionBar r3 = r5.mActionBar
                if (r3 == 0) goto L_0x003e
                androidx.appcompat.app.WindowDecorActionBar$ActionModeImpl r3 = r3.mActionMode
                if (r3 != 0) goto L_0x001d
                goto L_0x003a
            L_0x001d:
                androidx.appcompat.view.menu.MenuBuilder r3 = r3.mMenu
                if (r3 == 0) goto L_0x003a
                int r4 = r6.getDeviceId()
                android.view.KeyCharacterMap r4 = android.view.KeyCharacterMap.load(r4)
                int r4 = r4.getKeyboardType()
                if (r4 == r2) goto L_0x0031
                r4 = r2
                goto L_0x0032
            L_0x0031:
                r4 = r1
            L_0x0032:
                r3.setQwertyMode(r4)
                boolean r0 = r3.performShortcut(r0, r6, r1)
                goto L_0x003b
            L_0x003a:
                r0 = r1
            L_0x003b:
                if (r0 == 0) goto L_0x003e
                goto L_0x006a
            L_0x003e:
                androidx.appcompat.app.AppCompatDelegateImpl$PanelFeatureState r0 = r5.mPreparedPanel
                if (r0 == 0) goto L_0x0053
                int r3 = r6.getKeyCode()
                boolean r0 = r5.performPanelShortcut(r0, r3, r6)
                if (r0 == 0) goto L_0x0053
                androidx.appcompat.app.AppCompatDelegateImpl$PanelFeatureState r5 = r5.mPreparedPanel
                if (r5 == 0) goto L_0x006a
                r5.isHandled = r2
                goto L_0x006a
            L_0x0053:
                androidx.appcompat.app.AppCompatDelegateImpl$PanelFeatureState r0 = r5.mPreparedPanel
                if (r0 != 0) goto L_0x006c
                androidx.appcompat.app.AppCompatDelegateImpl$PanelFeatureState r0 = r5.getPanelState(r1)
                r5.preparePanel(r0, r6)
                int r3 = r6.getKeyCode()
                boolean r5 = r5.performPanelShortcut(r0, r3, r6)
                r0.isPrepared = r1
                if (r5 == 0) goto L_0x006c
            L_0x006a:
                r5 = r2
                goto L_0x006d
            L_0x006c:
                r5 = r1
            L_0x006d:
                if (r5 == 0) goto L_0x0070
            L_0x006f:
                r1 = r2
            L_0x0070:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.app.AppCompatDelegateImpl.AppCompatWindowCallback.dispatchKeyShortcutEvent(android.view.KeyEvent):boolean");
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public final View onCreatePanelView(int i) {
            return super.onCreatePanelView(i);
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public final boolean onMenuOpened(int i, Menu menu) {
            super.onMenuOpened(i, menu);
            AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
            if (i == 108) {
                appCompatDelegateImpl.initWindowDecorActionBar();
                WindowDecorActionBar windowDecorActionBar = appCompatDelegateImpl.mActionBar;
                if (windowDecorActionBar != null) {
                    windowDecorActionBar.dispatchMenuVisibilityChanged(true);
                }
            } else {
                Objects.requireNonNull(appCompatDelegateImpl);
            }
            return true;
        }

        @Override // androidx.appcompat.view.WindowCallbackWrapper, android.view.Window.Callback
        public final void onPanelClosed(int i, Menu menu) {
            super.onPanelClosed(i, menu);
            AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
            if (i == 108) {
                appCompatDelegateImpl.initWindowDecorActionBar();
                WindowDecorActionBar windowDecorActionBar = appCompatDelegateImpl.mActionBar;
                if (windowDecorActionBar != null) {
                    windowDecorActionBar.dispatchMenuVisibilityChanged(false);
                }
            } else if (i == 0) {
                PanelFeatureState panelState = appCompatDelegateImpl.getPanelState(i);
                if (panelState.isOpen) {
                    appCompatDelegateImpl.closePanel(panelState, false);
                }
            } else {
                Objects.requireNonNull(appCompatDelegateImpl);
            }
        }
    }

    /* loaded from: classes.dex */
    public class AutoBatteryNightModeManager extends AutoNightModeManager {
        public final PowerManager mPowerManager;

        public AutoBatteryNightModeManager(Context context) {
            super();
            this.mPowerManager = (PowerManager) context.getApplicationContext().getSystemService(GlobalActionsDialogLite.GLOBAL_ACTION_KEY_POWER);
        }

        @Override // androidx.appcompat.app.AppCompatDelegateImpl.AutoNightModeManager
        public final IntentFilter createIntentFilterForBroadcastReceiver() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.os.action.POWER_SAVE_MODE_CHANGED");
            return intentFilter;
        }

        @Override // androidx.appcompat.app.AppCompatDelegateImpl.AutoNightModeManager
        public final int getApplyableNightMode() {
            if (this.mPowerManager.isPowerSaveMode()) {
                return 2;
            }
            return 1;
        }

        @Override // androidx.appcompat.app.AppCompatDelegateImpl.AutoNightModeManager
        public final void onChange() {
            AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
            Objects.requireNonNull(appCompatDelegateImpl);
            appCompatDelegateImpl.applyDayNight(true);
        }
    }

    /* loaded from: classes.dex */
    public abstract class AutoNightModeManager {
        public AnonymousClass1 mReceiver;

        public abstract IntentFilter createIntentFilterForBroadcastReceiver();

        public abstract int getApplyableNightMode();

        public abstract void onChange();

        public AutoNightModeManager() {
        }

        public final void cleanup() {
            AnonymousClass1 r0 = this.mReceiver;
            if (r0 != null) {
                try {
                    AppCompatDelegateImpl.this.mContext.unregisterReceiver(r0);
                } catch (IllegalArgumentException unused) {
                }
                this.mReceiver = null;
            }
        }

        /* JADX WARN: Type inference failed for: r1v4, types: [androidx.appcompat.app.AppCompatDelegateImpl$AutoNightModeManager$1] */
        public final void setup() {
            cleanup();
            IntentFilter createIntentFilterForBroadcastReceiver = createIntentFilterForBroadcastReceiver();
            if (createIntentFilterForBroadcastReceiver != null && createIntentFilterForBroadcastReceiver.countActions() != 0) {
                if (this.mReceiver == null) {
                    this.mReceiver = new BroadcastReceiver() { // from class: androidx.appcompat.app.AppCompatDelegateImpl.AutoNightModeManager.1
                        @Override // android.content.BroadcastReceiver
                        public final void onReceive(Context context, Intent intent) {
                            AutoNightModeManager.this.onChange();
                        }
                    };
                }
                AppCompatDelegateImpl.this.mContext.registerReceiver(this.mReceiver, createIntentFilterForBroadcastReceiver);
            }
        }
    }

    /* loaded from: classes.dex */
    public class AutoTimeNightModeManager extends AutoNightModeManager {
        public final TwilightManager mTwilightManager;

        public AutoTimeNightModeManager(TwilightManager twilightManager) {
            super();
            this.mTwilightManager = twilightManager;
        }

        @Override // androidx.appcompat.app.AppCompatDelegateImpl.AutoNightModeManager
        public final IntentFilter createIntentFilterForBroadcastReceiver() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.TIME_SET");
            intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
            intentFilter.addAction("android.intent.action.TIME_TICK");
            return intentFilter;
        }

        @Override // androidx.appcompat.app.AppCompatDelegateImpl.AutoNightModeManager
        public final int getApplyableNightMode() {
            boolean z;
            boolean z2;
            Location location;
            long j;
            long j2;
            Location location2;
            TwilightManager twilightManager = this.mTwilightManager;
            Objects.requireNonNull(twilightManager);
            TwilightManager.TwilightState twilightState = twilightManager.mTwilightState;
            boolean z3 = false;
            if (twilightState.nextUpdate > System.currentTimeMillis()) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                z2 = twilightState.isNight;
            } else {
                Location location3 = null;
                if (R$styleable.checkSelfPermission(twilightManager.mContext, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
                    try {
                    } catch (Exception e) {
                        Log.d("TwilightManager", "Failed to get last known location", e);
                    }
                    if (twilightManager.mLocationManager.isProviderEnabled("network")) {
                        location2 = twilightManager.mLocationManager.getLastKnownLocation("network");
                        location = location2;
                    }
                    location2 = null;
                    location = location2;
                } else {
                    location = null;
                }
                if (R$styleable.checkSelfPermission(twilightManager.mContext, "android.permission.ACCESS_FINE_LOCATION") == 0) {
                    try {
                        if (twilightManager.mLocationManager.isProviderEnabled("gps")) {
                            location3 = twilightManager.mLocationManager.getLastKnownLocation("gps");
                        }
                    } catch (Exception e2) {
                        Log.d("TwilightManager", "Failed to get last known location", e2);
                    }
                }
                if (location3 == null || location == null ? location3 != null : location3.getTime() > location.getTime()) {
                    location = location3;
                }
                if (location != null) {
                    TwilightManager.TwilightState twilightState2 = twilightManager.mTwilightState;
                    long currentTimeMillis = System.currentTimeMillis();
                    if (TwilightCalculator.sInstance == null) {
                        TwilightCalculator.sInstance = new TwilightCalculator();
                    }
                    TwilightCalculator twilightCalculator = TwilightCalculator.sInstance;
                    twilightCalculator.calculateTwilight(currentTimeMillis - 86400000, location.getLatitude(), location.getLongitude());
                    twilightCalculator.calculateTwilight(currentTimeMillis, location.getLatitude(), location.getLongitude());
                    if (twilightCalculator.state == 1) {
                        z3 = true;
                    }
                    long j3 = twilightCalculator.sunrise;
                    long j4 = twilightCalculator.sunset;
                    twilightCalculator.calculateTwilight(currentTimeMillis + 86400000, location.getLatitude(), location.getLongitude());
                    long j5 = twilightCalculator.sunrise;
                    if (j3 == -1 || j4 == -1) {
                        j = 43200000 + currentTimeMillis;
                    } else {
                        if (currentTimeMillis > j4) {
                            j2 = j5 + 0;
                        } else if (currentTimeMillis > j3) {
                            j2 = j4 + 0;
                        } else {
                            j2 = j3 + 0;
                        }
                        j = j2 + 60000;
                    }
                    twilightState2.isNight = z3;
                    twilightState2.nextUpdate = j;
                    z2 = twilightState.isNight;
                } else {
                    Log.i("TwilightManager", "Could not get last known location. This is probably because the app does not have any location permissions. Falling back to hardcoded sunrise/sunset values.");
                    int i = Calendar.getInstance().get(11);
                    if (i < 6 || i >= 22) {
                        z3 = true;
                    }
                    z2 = z3;
                }
            }
            if (z2) {
                return 2;
            }
            return 1;
        }

        @Override // androidx.appcompat.app.AppCompatDelegateImpl.AutoNightModeManager
        public final void onChange() {
            AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
            Objects.requireNonNull(appCompatDelegateImpl);
            appCompatDelegateImpl.applyDayNight(true);
        }
    }

    /* loaded from: classes.dex */
    public class ListMenuDecorView extends ContentFrameLayout {
        public ListMenuDecorView(ContextThemeWrapper contextThemeWrapper) {
            super(contextThemeWrapper);
        }

        @Override // android.view.ViewGroup, android.view.View
        public final boolean dispatchKeyEvent(KeyEvent keyEvent) {
            if (AppCompatDelegateImpl.this.dispatchKeyEvent(keyEvent) || super.dispatchKeyEvent(keyEvent)) {
                return true;
            }
            return false;
        }

        @Override // android.view.ViewGroup
        public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            boolean z;
            if (motionEvent.getAction() == 0) {
                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                if (x < -5 || y < -5 || x > getWidth() + 5 || y > getHeight() + 5) {
                    z = true;
                } else {
                    z = false;
                }
                if (z) {
                    AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
                    Objects.requireNonNull(appCompatDelegateImpl);
                    appCompatDelegateImpl.closePanel(appCompatDelegateImpl.getPanelState(0), true);
                    return true;
                }
            }
            return super.onInterceptTouchEvent(motionEvent);
        }

        @Override // android.view.View
        public final void setBackgroundResource(int i) {
            setBackgroundDrawable(AppCompatResources.getDrawable(getContext(), i));
        }
    }

    /* loaded from: classes.dex */
    public final class PanelMenuPresenterCallback implements MenuPresenter.Callback {
        public PanelMenuPresenterCallback() {
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter.Callback
        public final void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
            boolean z2;
            int i;
            PanelFeatureState panelFeatureState;
            MenuBuilder rootMenu = menuBuilder.getRootMenu();
            int i2 = 0;
            if (rootMenu != menuBuilder) {
                z2 = true;
            } else {
                z2 = false;
            }
            AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
            if (z2) {
                menuBuilder = rootMenu;
            }
            Objects.requireNonNull(appCompatDelegateImpl);
            PanelFeatureState[] panelFeatureStateArr = appCompatDelegateImpl.mPanels;
            if (panelFeatureStateArr != null) {
                i = panelFeatureStateArr.length;
            } else {
                i = 0;
            }
            while (true) {
                if (i2 < i) {
                    panelFeatureState = panelFeatureStateArr[i2];
                    if (panelFeatureState != null && panelFeatureState.menu == menuBuilder) {
                        break;
                    }
                    i2++;
                } else {
                    panelFeatureState = null;
                    break;
                }
            }
            if (panelFeatureState == null) {
                return;
            }
            if (z2) {
                AppCompatDelegateImpl.this.callOnPanelClosed(panelFeatureState.featureId, panelFeatureState, rootMenu);
                AppCompatDelegateImpl.this.closePanel(panelFeatureState, true);
                return;
            }
            AppCompatDelegateImpl.this.closePanel(panelFeatureState, z);
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter.Callback
        public final boolean onOpenSubMenu(MenuBuilder menuBuilder) {
            Window.Callback windowCallback;
            if (menuBuilder != menuBuilder.getRootMenu()) {
                return true;
            }
            AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
            if (!appCompatDelegateImpl.mHasActionBar || (windowCallback = appCompatDelegateImpl.getWindowCallback()) == null || AppCompatDelegateImpl.this.mDestroyed) {
                return true;
            }
            windowCallback.onMenuOpened(108, menuBuilder);
            return true;
        }
    }

    public static Configuration createOverrideConfigurationForDayNight(Context context, int i, Configuration configuration) {
        int i2;
        if (i == 1) {
            i2 = 16;
        } else if (i != 2) {
            i2 = context.getApplicationContext().getResources().getConfiguration().uiMode & 48;
        } else {
            i2 = 32;
        }
        Configuration configuration2 = new Configuration();
        configuration2.fontScale = 0.0f;
        if (configuration != null) {
            configuration2.setTo(configuration);
        }
        configuration2.uiMode = i2 | (configuration2.uiMode & (-49));
        return configuration2;
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public final Context attachBaseContext2(Context context) {
        boolean z = true;
        this.mBaseContextAttached = true;
        int i = this.mLocalNightMode;
        if (i == -100) {
            i = -100;
        }
        int mapNightMode = mapNightMode(context, i);
        Configuration configuration = null;
        if (sCanApplyOverrideConfiguration && (context instanceof android.view.ContextThemeWrapper)) {
            try {
                ((android.view.ContextThemeWrapper) context).applyOverrideConfiguration(createOverrideConfigurationForDayNight(context, mapNightMode, null));
                return context;
            } catch (IllegalStateException unused) {
            }
        }
        if (context instanceof ContextThemeWrapper) {
            try {
                ((ContextThemeWrapper) context).applyOverrideConfiguration(createOverrideConfigurationForDayNight(context, mapNightMode, null));
                return context;
            } catch (IllegalStateException unused2) {
            }
        }
        if (!sCanReturnDifferentContext) {
            return context;
        }
        Configuration configuration2 = new Configuration();
        configuration2.uiMode = -1;
        configuration2.fontScale = 0.0f;
        Configuration configuration3 = context.createConfigurationContext(configuration2).getResources().getConfiguration();
        Configuration configuration4 = context.getResources().getConfiguration();
        configuration3.uiMode = configuration4.uiMode;
        if (!configuration3.equals(configuration4)) {
            configuration = new Configuration();
            configuration.fontScale = 0.0f;
            if (configuration3.diff(configuration4) != 0) {
                float f = configuration3.fontScale;
                float f2 = configuration4.fontScale;
                if (f != f2) {
                    configuration.fontScale = f2;
                }
                int i2 = configuration3.mcc;
                int i3 = configuration4.mcc;
                if (i2 != i3) {
                    configuration.mcc = i3;
                }
                int i4 = configuration3.mnc;
                int i5 = configuration4.mnc;
                if (i4 != i5) {
                    configuration.mnc = i5;
                }
                LocaleList locales = configuration3.getLocales();
                LocaleList locales2 = configuration4.getLocales();
                if (!locales.equals(locales2)) {
                    configuration.setLocales(locales2);
                    configuration.locale = configuration4.locale;
                }
                int i6 = configuration3.touchscreen;
                int i7 = configuration4.touchscreen;
                if (i6 != i7) {
                    configuration.touchscreen = i7;
                }
                int i8 = configuration3.keyboard;
                int i9 = configuration4.keyboard;
                if (i8 != i9) {
                    configuration.keyboard = i9;
                }
                int i10 = configuration3.keyboardHidden;
                int i11 = configuration4.keyboardHidden;
                if (i10 != i11) {
                    configuration.keyboardHidden = i11;
                }
                int i12 = configuration3.navigation;
                int i13 = configuration4.navigation;
                if (i12 != i13) {
                    configuration.navigation = i13;
                }
                int i14 = configuration3.navigationHidden;
                int i15 = configuration4.navigationHidden;
                if (i14 != i15) {
                    configuration.navigationHidden = i15;
                }
                int i16 = configuration3.orientation;
                int i17 = configuration4.orientation;
                if (i16 != i17) {
                    configuration.orientation = i17;
                }
                int i18 = configuration3.screenLayout & 15;
                int i19 = configuration4.screenLayout & 15;
                if (i18 != i19) {
                    configuration.screenLayout |= i19;
                }
                int i20 = configuration3.screenLayout & 192;
                int i21 = configuration4.screenLayout & 192;
                if (i20 != i21) {
                    configuration.screenLayout |= i21;
                }
                int i22 = configuration3.screenLayout & 48;
                int i23 = configuration4.screenLayout & 48;
                if (i22 != i23) {
                    configuration.screenLayout |= i23;
                }
                int i24 = configuration3.screenLayout & 768;
                int i25 = configuration4.screenLayout & 768;
                if (i24 != i25) {
                    configuration.screenLayout |= i25;
                }
                int i26 = configuration3.colorMode & 3;
                int i27 = configuration4.colorMode & 3;
                if (i26 != i27) {
                    configuration.colorMode |= i27;
                }
                int i28 = configuration3.colorMode & 12;
                int i29 = configuration4.colorMode & 12;
                if (i28 != i29) {
                    configuration.colorMode |= i29;
                }
                int i30 = configuration3.uiMode & 15;
                int i31 = configuration4.uiMode & 15;
                if (i30 != i31) {
                    configuration.uiMode |= i31;
                }
                int i32 = configuration3.uiMode & 48;
                int i33 = configuration4.uiMode & 48;
                if (i32 != i33) {
                    configuration.uiMode |= i33;
                }
                int i34 = configuration3.screenWidthDp;
                int i35 = configuration4.screenWidthDp;
                if (i34 != i35) {
                    configuration.screenWidthDp = i35;
                }
                int i36 = configuration3.screenHeightDp;
                int i37 = configuration4.screenHeightDp;
                if (i36 != i37) {
                    configuration.screenHeightDp = i37;
                }
                int i38 = configuration3.smallestScreenWidthDp;
                int i39 = configuration4.smallestScreenWidthDp;
                if (i38 != i39) {
                    configuration.smallestScreenWidthDp = i39;
                }
                int i40 = configuration3.densityDpi;
                int i41 = configuration4.densityDpi;
                if (i40 != i41) {
                    configuration.densityDpi = i41;
                }
            }
        }
        Configuration createOverrideConfigurationForDayNight = createOverrideConfigurationForDayNight(context, mapNightMode, configuration);
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, 2132018059);
        contextThemeWrapper.applyOverrideConfiguration(createOverrideConfigurationForDayNight);
        boolean z2 = false;
        try {
            if (context.getTheme() == null) {
                z = false;
            }
            z2 = z;
        } catch (NullPointerException unused3) {
        }
        if (z2) {
            contextThemeWrapper.getTheme().rebase();
        }
        return contextThemeWrapper;
    }

    public final AutoNightModeManager getAutoTimeNightModeManager() {
        return getAutoTimeNightModeManager(this.mContext);
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public final void onCreate() {
        this.mBaseContextAttached = true;
        applyDayNight(false);
        ensureWindow();
        Object obj = this.mHost;
        if (obj instanceof Activity) {
            String str = null;
            try {
                Activity activity = (Activity) obj;
                try {
                    str = NavUtils.getParentActivityName(activity, activity.getComponentName());
                } catch (PackageManager.NameNotFoundException e) {
                    throw new IllegalArgumentException(e);
                }
            } catch (IllegalArgumentException unused) {
            }
            if (str != null) {
                WindowDecorActionBar windowDecorActionBar = this.mActionBar;
                if (windowDecorActionBar == null) {
                    this.mEnableDefaultActionBarUp = true;
                } else {
                    windowDecorActionBar.setDefaultDisplayHomeAsUpEnabled(true);
                }
            }
            synchronized (AppCompatDelegate.sActivityDelegatesLock) {
                AppCompatDelegate.removeDelegateFromActives(this);
                AppCompatDelegate.sActivityDelegates.add(new WeakReference<>(this));
            }
        }
        this.mEffectiveConfiguration = new Configuration(this.mContext.getResources().getConfiguration());
        this.mCreated = true;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0117, code lost:
        if (r8.equals("ImageButton") == false) goto L_0x0146;
     */
    @Override // android.view.LayoutInflater.Factory2
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final android.view.View onCreateView(android.view.View r7, java.lang.String r8, android.content.Context r9, android.util.AttributeSet r10) {
        /*
            Method dump skipped, instructions count: 650
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.app.AppCompatDelegateImpl.onCreateView(android.view.View, java.lang.String, android.content.Context, android.util.AttributeSet):android.view.View");
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public final void onStart() {
        applyDayNight(true);
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public final void setContentView(View view) {
        ensureSubDecor();
        ViewGroup viewGroup = (ViewGroup) this.mSubDecor.findViewById(16908290);
        viewGroup.removeAllViews();
        viewGroup.addView(view);
        AppCompatWindowCallback appCompatWindowCallback = this.mAppCompatWindowCallback;
        Objects.requireNonNull(appCompatWindowCallback);
        appCompatWindowCallback.mWrapped.onContentChanged();
    }

    /* loaded from: classes.dex */
    public static final class PanelFeatureState {
        public int background;
        public View createdPanelView;
        public ListMenuDecorView decorView;
        public int featureId;
        public Bundle frozenActionViewState;
        public int gravity;
        public boolean isHandled;
        public boolean isOpen;
        public boolean isPrepared;
        public ListMenuPresenter listMenuPresenter;
        public ContextThemeWrapper listPresenterContext;
        public MenuBuilder menu;
        public boolean refreshDecorView = false;
        public boolean refreshMenuContent;
        public View shownPanelView;
        public int windowAnimations;

        @SuppressLint({"BanParcelableUsage"})
        /* loaded from: classes.dex */
        public static class SavedState implements Parcelable {
            public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() { // from class: androidx.appcompat.app.AppCompatDelegateImpl.PanelFeatureState.SavedState.1
                @Override // android.os.Parcelable.ClassLoaderCreator
                public final SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                    return SavedState.readFromParcel(parcel, classLoader);
                }

                @Override // android.os.Parcelable.Creator
                public final Object createFromParcel(Parcel parcel) {
                    return SavedState.readFromParcel(parcel, null);
                }

                @Override // android.os.Parcelable.Creator
                public final Object[] newArray(int i) {
                    return new SavedState[i];
                }
            };
            public int featureId;
            public boolean isOpen;
            public Bundle menuState;

            @Override // android.os.Parcelable
            public final int describeContents() {
                return 0;
            }

            public static SavedState readFromParcel(Parcel parcel, ClassLoader classLoader) {
                SavedState savedState = new SavedState();
                savedState.featureId = parcel.readInt();
                boolean z = true;
                if (parcel.readInt() != 1) {
                    z = false;
                }
                savedState.isOpen = z;
                if (z) {
                    savedState.menuState = parcel.readBundle(classLoader);
                }
                return savedState;
            }

            @Override // android.os.Parcelable
            public final void writeToParcel(Parcel parcel, int i) {
                parcel.writeInt(this.featureId);
                parcel.writeInt(this.isOpen ? 1 : 0);
                if (this.isOpen) {
                    parcel.writeBundle(this.menuState);
                }
            }
        }

        public PanelFeatureState(int i) {
            this.featureId = i;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x0063  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0108  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0115  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x011f  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0129  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x013c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean applyDayNight(boolean r11) {
        /*
            Method dump skipped, instructions count: 324
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.app.AppCompatDelegateImpl.applyDayNight(boolean):boolean");
    }

    public final void attachToWindow(Window window) {
        int resourceId;
        Drawable drawable;
        if (this.mWindow == null) {
            Window.Callback callback = window.getCallback();
            if (!(callback instanceof AppCompatWindowCallback)) {
                AppCompatWindowCallback appCompatWindowCallback = new AppCompatWindowCallback(callback);
                this.mAppCompatWindowCallback = appCompatWindowCallback;
                window.setCallback(appCompatWindowCallback);
                Context context = this.mContext;
                Drawable drawable2 = null;
                TypedArray obtainStyledAttributes = context.obtainStyledAttributes((AttributeSet) null, sWindowBackgroundStyleable);
                if (obtainStyledAttributes.hasValue(0) && (resourceId = obtainStyledAttributes.getResourceId(0, 0)) != 0) {
                    AppCompatDrawableManager appCompatDrawableManager = AppCompatDrawableManager.get();
                    Objects.requireNonNull(appCompatDrawableManager);
                    synchronized (appCompatDrawableManager) {
                        drawable = appCompatDrawableManager.mResourceManager.getDrawable(context, resourceId, true);
                    }
                    drawable2 = drawable;
                }
                if (drawable2 != null) {
                    window.setBackgroundDrawable(drawable2);
                }
                obtainStyledAttributes.recycle();
                this.mWindow = window;
                return;
            }
            throw new IllegalStateException("AppCompat has already installed itself into the Window");
        }
        throw new IllegalStateException("AppCompat has already installed itself into the Window");
    }

    public final void callOnPanelClosed(int i, PanelFeatureState panelFeatureState, MenuBuilder menuBuilder) {
        if (menuBuilder == null) {
            if (panelFeatureState == null && i >= 0) {
                PanelFeatureState[] panelFeatureStateArr = this.mPanels;
                if (i < panelFeatureStateArr.length) {
                    panelFeatureState = panelFeatureStateArr[i];
                }
            }
            if (panelFeatureState != null) {
                menuBuilder = panelFeatureState.menu;
            }
        }
        if ((panelFeatureState == null || panelFeatureState.isOpen) && !this.mDestroyed) {
            AppCompatWindowCallback appCompatWindowCallback = this.mAppCompatWindowCallback;
            Objects.requireNonNull(appCompatWindowCallback);
            appCompatWindowCallback.mWrapped.onPanelClosed(i, menuBuilder);
        }
    }

    public final void checkCloseActionMenu(MenuBuilder menuBuilder) {
        if (!this.mClosingActionMenu) {
            this.mClosingActionMenu = true;
            this.mDecorContentParent.dismissPopups();
            Window.Callback windowCallback = getWindowCallback();
            if (windowCallback != null && !this.mDestroyed) {
                windowCallback.onPanelClosed(108, menuBuilder);
            }
            this.mClosingActionMenu = false;
        }
    }

    public final void closePanel(PanelFeatureState panelFeatureState, boolean z) {
        ListMenuDecorView listMenuDecorView;
        DecorContentParent decorContentParent;
        if (!z || panelFeatureState.featureId != 0 || (decorContentParent = this.mDecorContentParent) == null || !decorContentParent.isOverflowMenuShowing()) {
            WindowManager windowManager = (WindowManager) this.mContext.getSystemService("window");
            if (!(windowManager == null || !panelFeatureState.isOpen || (listMenuDecorView = panelFeatureState.decorView) == null)) {
                windowManager.removeView(listMenuDecorView);
                if (z) {
                    callOnPanelClosed(panelFeatureState.featureId, panelFeatureState, null);
                }
            }
            panelFeatureState.isPrepared = false;
            panelFeatureState.isHandled = false;
            panelFeatureState.isOpen = false;
            panelFeatureState.shownPanelView = null;
            panelFeatureState.refreshDecorView = true;
            if (this.mPreparedPanel == panelFeatureState) {
                this.mPreparedPanel = null;
                return;
            }
            return;
        }
        checkCloseActionMenu(panelFeatureState.menu);
    }

    /* JADX WARN: Code restructure failed: missing block: B:83:0x0120, code lost:
        if (r6 != false) goto L_0x0122;
     */
    /* JADX WARN: Removed duplicated region for block: B:64:0x00d1  */
    /* JADX WARN: Removed duplicated region for block: B:92:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:98:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean dispatchKeyEvent(android.view.KeyEvent r7) {
        /*
            Method dump skipped, instructions count: 298
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.app.AppCompatDelegateImpl.dispatchKeyEvent(android.view.KeyEvent):boolean");
    }

    public final void ensureSubDecor() {
        ViewGroup viewGroup;
        CharSequence charSequence;
        Context context;
        if (!this.mSubDecorInstalled) {
            TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(androidx.appcompat.R$styleable.AppCompatTheme);
            if (obtainStyledAttributes.hasValue(117)) {
                if (obtainStyledAttributes.getBoolean(126, false)) {
                    requestWindowFeature(1);
                } else if (obtainStyledAttributes.getBoolean(117, false)) {
                    requestWindowFeature(108);
                }
                if (obtainStyledAttributes.getBoolean(118, false)) {
                    requestWindowFeature(109);
                }
                if (obtainStyledAttributes.getBoolean(119, false)) {
                    requestWindowFeature(10);
                }
                this.mIsFloating = obtainStyledAttributes.getBoolean(0, false);
                obtainStyledAttributes.recycle();
                ensureWindow();
                this.mWindow.getDecorView();
                LayoutInflater from = LayoutInflater.from(this.mContext);
                if (this.mWindowNoTitle) {
                    viewGroup = this.mOverlayActionMode ? (ViewGroup) from.inflate(2131623958, (ViewGroup) null) : (ViewGroup) from.inflate(2131623957, (ViewGroup) null);
                } else if (this.mIsFloating) {
                    viewGroup = (ViewGroup) from.inflate(2131623948, (ViewGroup) null);
                    this.mOverlayActionBar = false;
                    this.mHasActionBar = false;
                } else if (this.mHasActionBar) {
                    TypedValue typedValue = new TypedValue();
                    this.mContext.getTheme().resolveAttribute(2130968585, typedValue, true);
                    if (typedValue.resourceId != 0) {
                        context = new ContextThemeWrapper(this.mContext, typedValue.resourceId);
                    } else {
                        context = this.mContext;
                    }
                    viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(2131623959, (ViewGroup) null);
                    DecorContentParent decorContentParent = (DecorContentParent) viewGroup.findViewById(2131427805);
                    this.mDecorContentParent = decorContentParent;
                    decorContentParent.setWindowCallback(getWindowCallback());
                    if (this.mOverlayActionBar) {
                        this.mDecorContentParent.initFeature(109);
                    }
                    if (this.mFeatureProgress) {
                        this.mDecorContentParent.initFeature(2);
                    }
                    if (this.mFeatureIndeterminateProgress) {
                        this.mDecorContentParent.initFeature(5);
                    }
                } else {
                    viewGroup = null;
                }
                if (viewGroup != null) {
                    OnApplyWindowInsetsListener onApplyWindowInsetsListener = new OnApplyWindowInsetsListener() { // from class: androidx.appcompat.app.AppCompatDelegateImpl.3
                        @Override // androidx.core.view.OnApplyWindowInsetsListener
                        public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                            boolean z;
                            int i;
                            int i2;
                            boolean z2;
                            int i3;
                            int systemWindowInsetTop = windowInsetsCompat.getSystemWindowInsetTop();
                            AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
                            Objects.requireNonNull(appCompatDelegateImpl);
                            int systemWindowInsetTop2 = windowInsetsCompat.getSystemWindowInsetTop();
                            ActionBarContextView actionBarContextView = appCompatDelegateImpl.mActionModeView;
                            int i4 = 8;
                            if (actionBarContextView == null || !(actionBarContextView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
                                z = false;
                            } else {
                                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) appCompatDelegateImpl.mActionModeView.getLayoutParams();
                                boolean z3 = true;
                                if (appCompatDelegateImpl.mActionModeView.isShown()) {
                                    if (appCompatDelegateImpl.mTempRect1 == null) {
                                        appCompatDelegateImpl.mTempRect1 = new Rect();
                                        appCompatDelegateImpl.mTempRect2 = new Rect();
                                    }
                                    Rect rect = appCompatDelegateImpl.mTempRect1;
                                    Rect rect2 = appCompatDelegateImpl.mTempRect2;
                                    rect.set(windowInsetsCompat.getSystemWindowInsetLeft(), windowInsetsCompat.getSystemWindowInsetTop(), windowInsetsCompat.getSystemWindowInsetRight(), windowInsetsCompat.getSystemWindowInsetBottom());
                                    ViewGroup viewGroup2 = appCompatDelegateImpl.mSubDecor;
                                    Method method = ViewUtils.sComputeFitSystemWindowsMethod;
                                    if (method != null) {
                                        try {
                                            method.invoke(viewGroup2, rect, rect2);
                                        } catch (Exception e) {
                                            Log.d("ViewUtils", "Could not invoke computeFitSystemWindows", e);
                                        }
                                    }
                                    int i5 = rect.top;
                                    int i6 = rect.left;
                                    int i7 = rect.right;
                                    ViewGroup viewGroup3 = appCompatDelegateImpl.mSubDecor;
                                    WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                                    WindowInsetsCompat rootWindowInsets = ViewCompat.Api23Impl.getRootWindowInsets(viewGroup3);
                                    if (rootWindowInsets == null) {
                                        i = 0;
                                    } else {
                                        i = rootWindowInsets.getSystemWindowInsetLeft();
                                    }
                                    if (rootWindowInsets == null) {
                                        i2 = 0;
                                    } else {
                                        i2 = rootWindowInsets.getSystemWindowInsetRight();
                                    }
                                    if (marginLayoutParams.topMargin == i5 && marginLayoutParams.leftMargin == i6 && marginLayoutParams.rightMargin == i7) {
                                        z2 = false;
                                    } else {
                                        marginLayoutParams.topMargin = i5;
                                        marginLayoutParams.leftMargin = i6;
                                        marginLayoutParams.rightMargin = i7;
                                        z2 = true;
                                    }
                                    if (i5 <= 0 || appCompatDelegateImpl.mStatusGuard != null) {
                                        View view2 = appCompatDelegateImpl.mStatusGuard;
                                        if (view2 != null) {
                                            ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) view2.getLayoutParams();
                                            int i8 = marginLayoutParams2.height;
                                            int i9 = marginLayoutParams.topMargin;
                                            if (!(i8 == i9 && marginLayoutParams2.leftMargin == i && marginLayoutParams2.rightMargin == i2)) {
                                                marginLayoutParams2.height = i9;
                                                marginLayoutParams2.leftMargin = i;
                                                marginLayoutParams2.rightMargin = i2;
                                                appCompatDelegateImpl.mStatusGuard.setLayoutParams(marginLayoutParams2);
                                            }
                                        }
                                    } else {
                                        View view3 = new View(appCompatDelegateImpl.mContext);
                                        appCompatDelegateImpl.mStatusGuard = view3;
                                        view3.setVisibility(8);
                                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, marginLayoutParams.topMargin, 51);
                                        layoutParams.leftMargin = i;
                                        layoutParams.rightMargin = i2;
                                        appCompatDelegateImpl.mSubDecor.addView(appCompatDelegateImpl.mStatusGuard, -1, layoutParams);
                                    }
                                    View view4 = appCompatDelegateImpl.mStatusGuard;
                                    if (view4 != null) {
                                        z = true;
                                    } else {
                                        z = false;
                                    }
                                    if (z && view4.getVisibility() != 0) {
                                        View view5 = appCompatDelegateImpl.mStatusGuard;
                                        if ((ViewCompat.Api16Impl.getWindowSystemUiVisibility(view5) & 8192) == 0) {
                                            z3 = false;
                                        }
                                        if (z3) {
                                            Context context2 = appCompatDelegateImpl.mContext;
                                            Object obj = ContextCompat.sLock;
                                            i3 = context2.getColor(2131099670);
                                        } else {
                                            Context context3 = appCompatDelegateImpl.mContext;
                                            Object obj2 = ContextCompat.sLock;
                                            i3 = context3.getColor(2131099669);
                                        }
                                        view5.setBackgroundColor(i3);
                                    }
                                    if (!appCompatDelegateImpl.mOverlayActionMode && z) {
                                        systemWindowInsetTop2 = 0;
                                    }
                                    z3 = z2;
                                } else if (marginLayoutParams.topMargin != 0) {
                                    marginLayoutParams.topMargin = 0;
                                    z = false;
                                } else {
                                    z3 = false;
                                    z = false;
                                }
                                if (z3) {
                                    appCompatDelegateImpl.mActionModeView.setLayoutParams(marginLayoutParams);
                                }
                            }
                            View view6 = appCompatDelegateImpl.mStatusGuard;
                            if (view6 != null) {
                                if (z) {
                                    i4 = 0;
                                }
                                view6.setVisibility(i4);
                            }
                            if (systemWindowInsetTop != systemWindowInsetTop2) {
                                windowInsetsCompat = windowInsetsCompat.replaceSystemWindowInsets(windowInsetsCompat.getSystemWindowInsetLeft(), systemWindowInsetTop2, windowInsetsCompat.getSystemWindowInsetRight(), windowInsetsCompat.getSystemWindowInsetBottom());
                            }
                            return ViewCompat.onApplyWindowInsets(view, windowInsetsCompat);
                        }
                    };
                    WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                    ViewCompat.Api21Impl.setOnApplyWindowInsetsListener(viewGroup, onApplyWindowInsetsListener);
                    if (this.mDecorContentParent == null) {
                        this.mTitleView = (TextView) viewGroup.findViewById(2131429057);
                    }
                    Method method = ViewUtils.sComputeFitSystemWindowsMethod;
                    try {
                        Method method2 = viewGroup.getClass().getMethod("makeOptionalFitsSystemWindows", new Class[0]);
                        if (!method2.isAccessible()) {
                            method2.setAccessible(true);
                        }
                        method2.invoke(viewGroup, new Object[0]);
                    } catch (IllegalAccessException e) {
                        Log.d("ViewUtils", "Could not invoke makeOptionalFitsSystemWindows", e);
                    } catch (NoSuchMethodException unused) {
                        Log.d("ViewUtils", "Could not find method makeOptionalFitsSystemWindows. Oh well...");
                    } catch (InvocationTargetException e2) {
                        Log.d("ViewUtils", "Could not invoke makeOptionalFitsSystemWindows", e2);
                    }
                    ContentFrameLayout contentFrameLayout = (ContentFrameLayout) viewGroup.findViewById(2131427413);
                    ViewGroup viewGroup2 = (ViewGroup) this.mWindow.findViewById(16908290);
                    if (viewGroup2 != null) {
                        while (viewGroup2.getChildCount() > 0) {
                            View childAt = viewGroup2.getChildAt(0);
                            viewGroup2.removeViewAt(0);
                            contentFrameLayout.addView(childAt);
                        }
                        viewGroup2.setId(-1);
                        contentFrameLayout.setId(16908290);
                        if (viewGroup2 instanceof FrameLayout) {
                            ((FrameLayout) viewGroup2).setForeground(null);
                        }
                    }
                    this.mWindow.setContentView(viewGroup);
                    AnonymousClass5 r2 = new AnonymousClass5();
                    Objects.requireNonNull(contentFrameLayout);
                    contentFrameLayout.mAttachListener = r2;
                    this.mSubDecor = viewGroup;
                    Object obj = this.mHost;
                    if (obj instanceof Activity) {
                        charSequence = ((Activity) obj).getTitle();
                    } else {
                        charSequence = this.mTitle;
                    }
                    if (!TextUtils.isEmpty(charSequence)) {
                        DecorContentParent decorContentParent2 = this.mDecorContentParent;
                        if (decorContentParent2 != null) {
                            decorContentParent2.setWindowTitle(charSequence);
                        } else {
                            WindowDecorActionBar windowDecorActionBar = this.mActionBar;
                            if (windowDecorActionBar != null) {
                                windowDecorActionBar.mDecorToolbar.setWindowTitle(charSequence);
                            } else {
                                TextView textView = this.mTitleView;
                                if (textView != null) {
                                    textView.setText(charSequence);
                                }
                            }
                        }
                    }
                    ContentFrameLayout contentFrameLayout2 = (ContentFrameLayout) this.mSubDecor.findViewById(16908290);
                    View decorView = this.mWindow.getDecorView();
                    int paddingLeft = decorView.getPaddingLeft();
                    int paddingTop = decorView.getPaddingTop();
                    int paddingRight = decorView.getPaddingRight();
                    int paddingBottom = decorView.getPaddingBottom();
                    Objects.requireNonNull(contentFrameLayout2);
                    contentFrameLayout2.mDecorPadding.set(paddingLeft, paddingTop, paddingRight, paddingBottom);
                    WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
                    if (ViewCompat.Api19Impl.isLaidOut(contentFrameLayout2)) {
                        contentFrameLayout2.requestLayout();
                    }
                    TypedArray obtainStyledAttributes2 = this.mContext.obtainStyledAttributes(androidx.appcompat.R$styleable.AppCompatTheme);
                    if (contentFrameLayout2.mMinWidthMajor == null) {
                        contentFrameLayout2.mMinWidthMajor = new TypedValue();
                    }
                    obtainStyledAttributes2.getValue(124, contentFrameLayout2.mMinWidthMajor);
                    if (contentFrameLayout2.mMinWidthMinor == null) {
                        contentFrameLayout2.mMinWidthMinor = new TypedValue();
                    }
                    obtainStyledAttributes2.getValue(125, contentFrameLayout2.mMinWidthMinor);
                    if (obtainStyledAttributes2.hasValue(122)) {
                        if (contentFrameLayout2.mFixedWidthMajor == null) {
                            contentFrameLayout2.mFixedWidthMajor = new TypedValue();
                        }
                        obtainStyledAttributes2.getValue(122, contentFrameLayout2.mFixedWidthMajor);
                    }
                    if (obtainStyledAttributes2.hasValue(123)) {
                        if (contentFrameLayout2.mFixedWidthMinor == null) {
                            contentFrameLayout2.mFixedWidthMinor = new TypedValue();
                        }
                        obtainStyledAttributes2.getValue(123, contentFrameLayout2.mFixedWidthMinor);
                    }
                    if (obtainStyledAttributes2.hasValue(120)) {
                        if (contentFrameLayout2.mFixedHeightMajor == null) {
                            contentFrameLayout2.mFixedHeightMajor = new TypedValue();
                        }
                        obtainStyledAttributes2.getValue(120, contentFrameLayout2.mFixedHeightMajor);
                    }
                    if (obtainStyledAttributes2.hasValue(121)) {
                        if (contentFrameLayout2.mFixedHeightMinor == null) {
                            contentFrameLayout2.mFixedHeightMinor = new TypedValue();
                        }
                        obtainStyledAttributes2.getValue(121, contentFrameLayout2.mFixedHeightMinor);
                    }
                    obtainStyledAttributes2.recycle();
                    contentFrameLayout2.requestLayout();
                    this.mSubDecorInstalled = true;
                    PanelFeatureState panelState = getPanelState(0);
                    if (!this.mDestroyed && panelState.menu == null) {
                        this.mInvalidatePanelMenuFeatures |= 4096;
                        if (!this.mInvalidatePanelMenuPosted) {
                            ViewCompat.Api16Impl.postOnAnimation(this.mWindow.getDecorView(), this.mInvalidatePanelMenuRunnable);
                            this.mInvalidatePanelMenuPosted = true;
                            return;
                        }
                        return;
                    }
                    return;
                }
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("AppCompat does not support the current theme features: { windowActionBar: ");
                m.append(this.mHasActionBar);
                m.append(", windowActionBarOverlay: ");
                m.append(this.mOverlayActionBar);
                m.append(", android:windowIsFloating: ");
                m.append(this.mIsFloating);
                m.append(", windowActionModeOverlay: ");
                m.append(this.mOverlayActionMode);
                m.append(", windowNoTitle: ");
                m.append(this.mWindowNoTitle);
                m.append(" }");
                throw new IllegalArgumentException(m.toString());
            }
            obtainStyledAttributes.recycle();
            throw new IllegalStateException("You need to use a Theme.AppCompat theme (or descendant) with this activity.");
        }
    }

    public final void ensureWindow() {
        if (this.mWindow == null) {
            Object obj = this.mHost;
            if (obj instanceof Activity) {
                attachToWindow(((Activity) obj).getWindow());
            }
        }
        if (this.mWindow == null) {
            throw new IllegalStateException("We have not been given a Window");
        }
    }

    public final AutoNightModeManager getAutoTimeNightModeManager(Context context) {
        if (this.mAutoTimeNightModeManager == null) {
            if (TwilightManager.sInstance == null) {
                Context applicationContext = context.getApplicationContext();
                TwilightManager.sInstance = new TwilightManager(applicationContext, (LocationManager) applicationContext.getSystemService("location"));
            }
            this.mAutoTimeNightModeManager = new AutoTimeNightModeManager(TwilightManager.sInstance);
        }
        return this.mAutoTimeNightModeManager;
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public final MenuInflater getMenuInflater() {
        Context context;
        if (this.mMenuInflater == null) {
            initWindowDecorActionBar();
            WindowDecorActionBar windowDecorActionBar = this.mActionBar;
            if (windowDecorActionBar != null) {
                context = windowDecorActionBar.getThemedContext();
            } else {
                context = this.mContext;
            }
            this.mMenuInflater = new SupportMenuInflater(context);
        }
        return this.mMenuInflater;
    }

    public final PanelFeatureState getPanelState(int i) {
        PanelFeatureState[] panelFeatureStateArr = this.mPanels;
        if (panelFeatureStateArr == null || panelFeatureStateArr.length <= i) {
            PanelFeatureState[] panelFeatureStateArr2 = new PanelFeatureState[i + 1];
            if (panelFeatureStateArr != null) {
                System.arraycopy(panelFeatureStateArr, 0, panelFeatureStateArr2, 0, panelFeatureStateArr.length);
            }
            this.mPanels = panelFeatureStateArr2;
            panelFeatureStateArr = panelFeatureStateArr2;
        }
        PanelFeatureState panelFeatureState = panelFeatureStateArr[i];
        if (panelFeatureState != null) {
            return panelFeatureState;
        }
        PanelFeatureState panelFeatureState2 = new PanelFeatureState(i);
        panelFeatureStateArr[i] = panelFeatureState2;
        return panelFeatureState2;
    }

    public final Window.Callback getWindowCallback() {
        return this.mWindow.getCallback();
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public final void installViewFactory() {
        LayoutInflater from = LayoutInflater.from(this.mContext);
        if (from.getFactory() == null) {
            from.setFactory2(this);
        } else if (!(from.getFactory2() instanceof AppCompatDelegateImpl)) {
            Log.i("AppCompatDelegate", "The Activity's LayoutInflater already has a Factory installed so we can not install AppCompat's");
        }
    }

    public final int mapNightMode(Context context, int i) {
        if (i == -100) {
            return -1;
        }
        if (i != -1) {
            if (i != 0) {
                if (!(i == 1 || i == 2)) {
                    if (i == 3) {
                        if (this.mAutoBatteryNightModeManager == null) {
                            this.mAutoBatteryNightModeManager = new AutoBatteryNightModeManager(context);
                        }
                        return this.mAutoBatteryNightModeManager.getApplyableNightMode();
                    }
                    throw new IllegalStateException("Unknown value set for night mode. Please use one of the MODE_NIGHT values from AppCompatDelegate.");
                }
            } else if (((UiModeManager) context.getApplicationContext().getSystemService("uimode")).getNightMode() == 0) {
                return -1;
            } else {
                return getAutoTimeNightModeManager(context).getApplyableNightMode();
            }
        }
        return i;
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public final void onConfigurationChanged() {
        if (this.mHasActionBar && this.mSubDecorInstalled) {
            initWindowDecorActionBar();
            WindowDecorActionBar windowDecorActionBar = this.mActionBar;
            if (windowDecorActionBar != null) {
                windowDecorActionBar.setHasEmbeddedTabs(windowDecorActionBar.mContext.getResources().getBoolean(2131034112));
            }
        }
        AppCompatDrawableManager appCompatDrawableManager = AppCompatDrawableManager.get();
        Context context = this.mContext;
        Objects.requireNonNull(appCompatDrawableManager);
        synchronized (appCompatDrawableManager) {
            ResourceManagerInternal resourceManagerInternal = appCompatDrawableManager.mResourceManager;
            Objects.requireNonNull(resourceManagerInternal);
            synchronized (resourceManagerInternal) {
                LongSparseArray<WeakReference<Drawable.ConstantState>> longSparseArray = resourceManagerInternal.mDrawableCaches.get(context);
                if (longSparseArray != null) {
                    longSparseArray.clear();
                }
            }
        }
        this.mEffectiveConfiguration = new Configuration(this.mContext.getResources().getConfiguration());
        applyDayNight(false);
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x0060  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0067  */
    /* JADX WARN: Removed duplicated region for block: B:32:? A[RETURN, SYNTHETIC] */
    @Override // androidx.appcompat.app.AppCompatDelegate
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onDestroy() {
        /*
            r3 = this;
            java.lang.Object r0 = r3.mHost
            boolean r0 = r0 instanceof android.app.Activity
            if (r0 == 0) goto L_0x0011
            java.lang.Object r0 = androidx.appcompat.app.AppCompatDelegate.sActivityDelegatesLock
            monitor-enter(r0)
            androidx.appcompat.app.AppCompatDelegate.removeDelegateFromActives(r3)     // Catch: all -> 0x000e
            monitor-exit(r0)     // Catch: all -> 0x000e
            goto L_0x0011
        L_0x000e:
            r3 = move-exception
            monitor-exit(r0)     // Catch: all -> 0x000e
            throw r3
        L_0x0011:
            boolean r0 = r3.mInvalidatePanelMenuPosted
            if (r0 == 0) goto L_0x0020
            android.view.Window r0 = r3.mWindow
            android.view.View r0 = r0.getDecorView()
            androidx.appcompat.app.AppCompatDelegateImpl$2 r1 = r3.mInvalidatePanelMenuRunnable
            r0.removeCallbacks(r1)
        L_0x0020:
            r0 = 1
            r3.mDestroyed = r0
            int r0 = r3.mLocalNightMode
            r1 = -100
            if (r0 == r1) goto L_0x004d
            java.lang.Object r0 = r3.mHost
            boolean r1 = r0 instanceof android.app.Activity
            if (r1 == 0) goto L_0x004d
            android.app.Activity r0 = (android.app.Activity) r0
            boolean r0 = r0.isChangingConfigurations()
            if (r0 == 0) goto L_0x004d
            androidx.collection.SimpleArrayMap<java.lang.String, java.lang.Integer> r0 = androidx.appcompat.app.AppCompatDelegateImpl.sLocalNightModes
            java.lang.Object r1 = r3.mHost
            java.lang.Class r1 = r1.getClass()
            java.lang.String r1 = r1.getName()
            int r2 = r3.mLocalNightMode
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            r0.put(r1, r2)
            goto L_0x005c
        L_0x004d:
            androidx.collection.SimpleArrayMap<java.lang.String, java.lang.Integer> r0 = androidx.appcompat.app.AppCompatDelegateImpl.sLocalNightModes
            java.lang.Object r1 = r3.mHost
            java.lang.Class r1 = r1.getClass()
            java.lang.String r1 = r1.getName()
            r0.remove(r1)
        L_0x005c:
            androidx.appcompat.app.AppCompatDelegateImpl$AutoTimeNightModeManager r0 = r3.mAutoTimeNightModeManager
            if (r0 == 0) goto L_0x0063
            r0.cleanup()
        L_0x0063:
            androidx.appcompat.app.AppCompatDelegateImpl$AutoBatteryNightModeManager r3 = r3.mAutoBatteryNightModeManager
            if (r3 == 0) goto L_0x006a
            r3.cleanup()
        L_0x006a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.app.AppCompatDelegateImpl.onDestroy():void");
    }

    @Override // androidx.appcompat.view.menu.MenuBuilder.Callback
    public final void onMenuModeChange(MenuBuilder menuBuilder) {
        DecorContentParent decorContentParent = this.mDecorContentParent;
        if (decorContentParent == null || !decorContentParent.canShowOverflowMenu() || (ViewConfiguration.get(this.mContext).hasPermanentMenuKey() && !this.mDecorContentParent.isOverflowMenuShowPending())) {
            PanelFeatureState panelState = getPanelState(0);
            panelState.refreshDecorView = true;
            closePanel(panelState, false);
            openPanel(panelState, null);
            return;
        }
        Window.Callback windowCallback = getWindowCallback();
        if (this.mDecorContentParent.isOverflowMenuShowing()) {
            this.mDecorContentParent.hideOverflowMenu();
            if (!this.mDestroyed) {
                windowCallback.onPanelClosed(108, getPanelState(0).menu);
            }
        } else if (windowCallback != null && !this.mDestroyed) {
            if (this.mInvalidatePanelMenuPosted && (1 & this.mInvalidatePanelMenuFeatures) != 0) {
                this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable);
                this.mInvalidatePanelMenuRunnable.run();
            }
            PanelFeatureState panelState2 = getPanelState(0);
            MenuBuilder menuBuilder2 = panelState2.menu;
            if (menuBuilder2 != null && !panelState2.refreshMenuContent && windowCallback.onPreparePanel(0, panelState2.createdPanelView, menuBuilder2)) {
                windowCallback.onMenuOpened(108, panelState2.menu);
                this.mDecorContentParent.showOverflowMenu();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:75:0x015b, code lost:
        if (r13 != null) goto L_0x015d;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x0182, code lost:
        if (r13.mAdapter.getCount() > 0) goto L_0x0184;
     */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0162  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x018a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void openPanel(androidx.appcompat.app.AppCompatDelegateImpl.PanelFeatureState r14, android.view.KeyEvent r15) {
        /*
            Method dump skipped, instructions count: 487
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.app.AppCompatDelegateImpl.openPanel(androidx.appcompat.app.AppCompatDelegateImpl$PanelFeatureState, android.view.KeyEvent):void");
    }

    public final boolean preparePanel(PanelFeatureState panelFeatureState, KeyEvent keyEvent) {
        boolean z;
        int i;
        boolean z2;
        DecorContentParent decorContentParent;
        DecorContentParent decorContentParent2;
        DecorContentParent decorContentParent3;
        Resources.Theme theme;
        DecorContentParent decorContentParent4;
        if (this.mDestroyed) {
            return false;
        }
        if (panelFeatureState.isPrepared) {
            return true;
        }
        PanelFeatureState panelFeatureState2 = this.mPreparedPanel;
        if (!(panelFeatureState2 == null || panelFeatureState2 == panelFeatureState)) {
            closePanel(panelFeatureState2, false);
        }
        Window.Callback windowCallback = getWindowCallback();
        if (windowCallback != null) {
            panelFeatureState.createdPanelView = windowCallback.onCreatePanelView(panelFeatureState.featureId);
        }
        int i2 = panelFeatureState.featureId;
        if (i2 == 0 || i2 == 108) {
            z = true;
        } else {
            z = false;
        }
        if (z && (decorContentParent4 = this.mDecorContentParent) != null) {
            decorContentParent4.setMenuPrepared();
        }
        if (panelFeatureState.createdPanelView == null) {
            MenuBuilder menuBuilder = panelFeatureState.menu;
            if (menuBuilder == null || panelFeatureState.refreshMenuContent) {
                if (menuBuilder == null) {
                    Context context = this.mContext;
                    int i3 = panelFeatureState.featureId;
                    if ((i3 == 0 || i3 == 108) && this.mDecorContentParent != null) {
                        TypedValue typedValue = new TypedValue();
                        Resources.Theme theme2 = context.getTheme();
                        theme2.resolveAttribute(2130968585, typedValue, true);
                        if (typedValue.resourceId != 0) {
                            theme = context.getResources().newTheme();
                            theme.setTo(theme2);
                            theme.applyStyle(typedValue.resourceId, true);
                            theme.resolveAttribute(2130968586, typedValue, true);
                        } else {
                            theme2.resolveAttribute(2130968586, typedValue, true);
                            theme = null;
                        }
                        if (typedValue.resourceId != 0) {
                            if (theme == null) {
                                theme = context.getResources().newTheme();
                                theme.setTo(theme2);
                            }
                            theme.applyStyle(typedValue.resourceId, true);
                        }
                        if (theme != null) {
                            ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, 0);
                            contextThemeWrapper.getTheme().setTo(theme);
                            context = contextThemeWrapper;
                        }
                    }
                    MenuBuilder menuBuilder2 = new MenuBuilder(context);
                    menuBuilder2.mCallback = this;
                    MenuBuilder menuBuilder3 = panelFeatureState.menu;
                    if (menuBuilder2 != menuBuilder3) {
                        if (menuBuilder3 != null) {
                            menuBuilder3.removeMenuPresenter(panelFeatureState.listMenuPresenter);
                        }
                        panelFeatureState.menu = menuBuilder2;
                        ListMenuPresenter listMenuPresenter = panelFeatureState.listMenuPresenter;
                        if (listMenuPresenter != null) {
                            menuBuilder2.addMenuPresenter(listMenuPresenter, menuBuilder2.mContext);
                        }
                    }
                    if (panelFeatureState.menu == null) {
                        return false;
                    }
                }
                if (z && (decorContentParent3 = this.mDecorContentParent) != null) {
                    if (this.mActionMenuPresenterCallback == null) {
                        this.mActionMenuPresenterCallback = new ActionMenuPresenterCallback();
                    }
                    decorContentParent3.setMenu(panelFeatureState.menu, this.mActionMenuPresenterCallback);
                }
                panelFeatureState.menu.stopDispatchingItemsChanged();
                if (!windowCallback.onCreatePanelMenu(panelFeatureState.featureId, panelFeatureState.menu)) {
                    MenuBuilder menuBuilder4 = panelFeatureState.menu;
                    if (menuBuilder4 != null) {
                        if (menuBuilder4 != null) {
                            menuBuilder4.removeMenuPresenter(panelFeatureState.listMenuPresenter);
                        }
                        panelFeatureState.menu = null;
                    }
                    if (z && (decorContentParent2 = this.mDecorContentParent) != null) {
                        decorContentParent2.setMenu(null, this.mActionMenuPresenterCallback);
                    }
                    return false;
                }
                panelFeatureState.refreshMenuContent = false;
            }
            panelFeatureState.menu.stopDispatchingItemsChanged();
            Bundle bundle = panelFeatureState.frozenActionViewState;
            if (bundle != null) {
                panelFeatureState.menu.restoreActionViewStates(bundle);
                panelFeatureState.frozenActionViewState = null;
            }
            if (!windowCallback.onPreparePanel(0, panelFeatureState.createdPanelView, panelFeatureState.menu)) {
                if (z && (decorContentParent = this.mDecorContentParent) != null) {
                    decorContentParent.setMenu(null, this.mActionMenuPresenterCallback);
                }
                panelFeatureState.menu.startDispatchingItemsChanged();
                return false;
            }
            if (keyEvent != null) {
                i = keyEvent.getDeviceId();
            } else {
                i = -1;
            }
            if (KeyCharacterMap.load(i).getKeyboardType() != 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            panelFeatureState.menu.setQwertyMode(z2);
            panelFeatureState.menu.startDispatchingItemsChanged();
        }
        panelFeatureState.isPrepared = true;
        panelFeatureState.isHandled = false;
        this.mPreparedPanel = panelFeatureState;
        return true;
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public final boolean requestWindowFeature(int i) {
        if (i == 8) {
            Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR id when requesting this feature.");
            i = 108;
        } else if (i == 9) {
            Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY id when requesting this feature.");
            i = 109;
        }
        if (this.mWindowNoTitle && i == 108) {
            return false;
        }
        if (this.mHasActionBar && i == 1) {
            this.mHasActionBar = false;
        }
        if (i == 1) {
            throwFeatureRequestIfSubDecorInstalled();
            this.mWindowNoTitle = true;
            return true;
        } else if (i == 2) {
            throwFeatureRequestIfSubDecorInstalled();
            this.mFeatureProgress = true;
            return true;
        } else if (i == 5) {
            throwFeatureRequestIfSubDecorInstalled();
            this.mFeatureIndeterminateProgress = true;
            return true;
        } else if (i == 10) {
            throwFeatureRequestIfSubDecorInstalled();
            this.mOverlayActionMode = true;
            return true;
        } else if (i == 108) {
            throwFeatureRequestIfSubDecorInstalled();
            this.mHasActionBar = true;
            return true;
        } else if (i != 109) {
            return this.mWindow.requestFeature(i);
        } else {
            throwFeatureRequestIfSubDecorInstalled();
            this.mOverlayActionBar = true;
            return true;
        }
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public final void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        DecorContentParent decorContentParent = this.mDecorContentParent;
        if (decorContentParent != null) {
            decorContentParent.setWindowTitle(charSequence);
            return;
        }
        WindowDecorActionBar windowDecorActionBar = this.mActionBar;
        if (windowDecorActionBar != null) {
            windowDecorActionBar.mDecorToolbar.setWindowTitle(charSequence);
            return;
        }
        TextView textView = this.mTitleView;
        if (textView != null) {
            textView.setText(charSequence);
        }
    }

    public final void throwFeatureRequestIfSubDecorInstalled() {
        if (this.mSubDecorInstalled) {
            throw new AndroidRuntimeException("Window feature must be requested before adding content");
        }
    }

    public AppCompatDelegateImpl(Context context, Window window, AppCompatCallback appCompatCallback, Object obj) {
        SimpleArrayMap<String, Integer> simpleArrayMap;
        Integer orDefault;
        AppCompatActivity appCompatActivity;
        this.mLocalNightMode = -100;
        this.mContext = context;
        this.mAppCompatCallback = appCompatCallback;
        this.mHost = obj;
        if (obj instanceof Dialog) {
            while (context != null) {
                if (!(context instanceof AppCompatActivity)) {
                    if (!(context instanceof ContextWrapper)) {
                        break;
                    }
                    context = ((ContextWrapper) context).getBaseContext();
                } else {
                    appCompatActivity = (AppCompatActivity) context;
                    break;
                }
            }
            appCompatActivity = null;
            if (appCompatActivity != null) {
                this.mLocalNightMode = appCompatActivity.getDelegate().getLocalNightMode();
            }
        }
        if (this.mLocalNightMode == -100 && (orDefault = (simpleArrayMap = sLocalNightModes).getOrDefault(this.mHost.getClass().getName(), null)) != null) {
            this.mLocalNightMode = orDefault.intValue();
            simpleArrayMap.remove(this.mHost.getClass().getName());
        }
        if (window != null) {
            attachToWindow(window);
        }
        AppCompatDrawableManager.preload();
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public final void addContentView(View view, ViewGroup.LayoutParams layoutParams) {
        ensureSubDecor();
        ((ViewGroup) this.mSubDecor.findViewById(16908290)).addView(view, layoutParams);
        AppCompatWindowCallback appCompatWindowCallback = this.mAppCompatWindowCallback;
        Objects.requireNonNull(appCompatWindowCallback);
        appCompatWindowCallback.mWrapped.onContentChanged();
    }

    public final void doInvalidatePanelMenu(int i) {
        PanelFeatureState panelState = getPanelState(i);
        if (panelState.menu != null) {
            Bundle bundle = new Bundle();
            panelState.menu.saveActionViewStates(bundle);
            if (bundle.size() > 0) {
                panelState.frozenActionViewState = bundle;
            }
            panelState.menu.stopDispatchingItemsChanged();
            panelState.menu.clear();
        }
        panelState.refreshMenuContent = true;
        panelState.refreshDecorView = true;
        if ((i == 108 || i == 0) && this.mDecorContentParent != null) {
            PanelFeatureState panelState2 = getPanelState(0);
            panelState2.isPrepared = false;
            preparePanel(panelState2, null);
        }
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public final <T extends View> T findViewById(int i) {
        ensureSubDecor();
        return (T) this.mWindow.findViewById(i);
    }

    public final void initWindowDecorActionBar() {
        ensureSubDecor();
        if (this.mHasActionBar && this.mActionBar == null) {
            Object obj = this.mHost;
            if (obj instanceof Activity) {
                this.mActionBar = new WindowDecorActionBar((Activity) this.mHost, this.mOverlayActionBar);
            } else if (obj instanceof Dialog) {
                this.mActionBar = new WindowDecorActionBar((Dialog) this.mHost);
            }
            WindowDecorActionBar windowDecorActionBar = this.mActionBar;
            if (windowDecorActionBar != null) {
                windowDecorActionBar.setDefaultDisplayHomeAsUpEnabled(this.mEnableDefaultActionBarUp);
            }
        }
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public final void invalidateOptionsMenu() {
        initWindowDecorActionBar();
        this.mInvalidatePanelMenuFeatures |= 1;
        if (!this.mInvalidatePanelMenuPosted) {
            View decorView = this.mWindow.getDecorView();
            AnonymousClass2 r2 = this.mInvalidatePanelMenuRunnable;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api16Impl.postOnAnimation(decorView, r2);
            this.mInvalidatePanelMenuPosted = true;
        }
    }

    @Override // androidx.appcompat.view.menu.MenuBuilder.Callback
    public final boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
        int i;
        int i2;
        PanelFeatureState panelFeatureState;
        Window.Callback windowCallback = getWindowCallback();
        if (windowCallback != null && !this.mDestroyed) {
            MenuBuilder rootMenu = menuBuilder.getRootMenu();
            PanelFeatureState[] panelFeatureStateArr = this.mPanels;
            if (panelFeatureStateArr != null) {
                i2 = panelFeatureStateArr.length;
                i = 0;
            } else {
                i2 = 0;
                i = 0;
            }
            while (true) {
                if (i < i2) {
                    panelFeatureState = panelFeatureStateArr[i];
                    if (panelFeatureState != null && panelFeatureState.menu == rootMenu) {
                        break;
                    }
                    i++;
                } else {
                    panelFeatureState = null;
                    break;
                }
            }
            if (panelFeatureState != null) {
                return windowCallback.onMenuItemSelected(panelFeatureState.featureId, menuItem);
            }
        }
        return false;
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public final void onPostResume() {
        initWindowDecorActionBar();
        WindowDecorActionBar windowDecorActionBar = this.mActionBar;
        if (windowDecorActionBar != null) {
            windowDecorActionBar.mShowHideAnimationEnabled = true;
        }
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public final void onStop() {
        initWindowDecorActionBar();
        WindowDecorActionBar windowDecorActionBar = this.mActionBar;
        if (windowDecorActionBar != null) {
            windowDecorActionBar.mShowHideAnimationEnabled = false;
            ViewPropertyAnimatorCompatSet viewPropertyAnimatorCompatSet = windowDecorActionBar.mCurrentShowAnim;
            if (viewPropertyAnimatorCompatSet != null) {
                viewPropertyAnimatorCompatSet.cancel();
            }
        }
    }

    public final boolean performPanelShortcut(PanelFeatureState panelFeatureState, int i, KeyEvent keyEvent) {
        MenuBuilder menuBuilder;
        if (keyEvent.isSystem()) {
            return false;
        }
        if ((panelFeatureState.isPrepared || preparePanel(panelFeatureState, keyEvent)) && (menuBuilder = panelFeatureState.menu) != null) {
            return menuBuilder.performShortcut(i, keyEvent, 1);
        }
        return false;
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public final void setContentView(int i) {
        ensureSubDecor();
        ViewGroup viewGroup = (ViewGroup) this.mSubDecor.findViewById(16908290);
        viewGroup.removeAllViews();
        LayoutInflater.from(this.mContext).inflate(i, viewGroup);
        AppCompatWindowCallback appCompatWindowCallback = this.mAppCompatWindowCallback;
        Objects.requireNonNull(appCompatWindowCallback);
        appCompatWindowCallback.mWrapped.onContentChanged();
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public final void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        ensureSubDecor();
        ViewGroup viewGroup = (ViewGroup) this.mSubDecor.findViewById(16908290);
        viewGroup.removeAllViews();
        viewGroup.addView(view, layoutParams);
        AppCompatWindowCallback appCompatWindowCallback = this.mAppCompatWindowCallback;
        Objects.requireNonNull(appCompatWindowCallback);
        appCompatWindowCallback.mWrapped.onContentChanged();
    }

    @Override // android.view.LayoutInflater.Factory
    public final View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return onCreateView(null, str, context, attributeSet);
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public final void setTheme(int i) {
        this.mThemeResId = i;
    }

    @Override // androidx.appcompat.app.AppCompatDelegate
    public final int getLocalNightMode() {
        return this.mLocalNightMode;
    }
}
