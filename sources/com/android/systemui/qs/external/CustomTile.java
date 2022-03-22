package com.android.systemui.qs.external;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.metrics.LogMaker;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.service.quicksettings.Tile;
import android.util.ArrayMap;
import android.util.Log;
import android.view.IWindowManager;
import android.view.View;
import android.view.WindowManagerGlobal;
import android.widget.Switch;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.external.TileLifecycleManager;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.settings.UserTracker;
import dagger.Lazy;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes.dex */
public final class CustomTile extends QSTileImpl<QSTile.State> implements TileLifecycleManager.TileChangeListener {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final ComponentName mComponent;
    public final CustomTileStatePersister mCustomTileStatePersister;
    public Icon mDefaultIcon;
    public CharSequence mDefaultLabel;
    public boolean mIsShowingDialog;
    public boolean mIsTokenGranted;
    public final TileServiceKey mKey;
    public boolean mListening;
    public final TileLifecycleManager mService;
    public final TileServiceManager mServiceManager;
    public final TileServices mTileServices;
    public final int mUser;
    public final Context mUserContext;
    public final Binder mToken = new Binder();
    public final AtomicBoolean mInitialDefaultIconFetched = new AtomicBoolean(false);
    public final IWindowManager mWindowManager = WindowManagerGlobal.getWindowManagerService();
    public final Tile mTile = new Tile();

    /* loaded from: classes.dex */
    public static class Builder {
        public final ActivityStarter mActivityStarter;
        public final Looper mBackgroundLooper;
        public final CustomTileStatePersister mCustomTileStatePersister;
        public final FalsingManager mFalsingManager;
        public final Handler mMainHandler;
        public final MetricsLogger mMetricsLogger;
        public final Lazy<QSHost> mQSHostLazy;
        public final QSLogger mQSLogger;
        public String mSpec = "";
        public final StatusBarStateController mStatusBarStateController;
        public TileServices mTileServices;
        public Context mUserContext;

        @VisibleForTesting
        public CustomTile build() {
            Objects.requireNonNull(this.mUserContext, "UserContext cannot be null");
            String str = this.mSpec;
            int i = CustomTile.$r8$clinit;
            if (str == null || !str.startsWith("custom(") || !str.endsWith(")")) {
                throw new IllegalArgumentException(SupportMenuInflater$$ExternalSyntheticOutline0.m("Bad custom tile spec: ", str));
            }
            String substring = str.substring(7, str.length() - 1);
            if (!substring.isEmpty()) {
                return new CustomTile(this.mQSHostLazy.get(), this.mBackgroundLooper, this.mMainHandler, this.mFalsingManager, this.mMetricsLogger, this.mStatusBarStateController, this.mActivityStarter, this.mQSLogger, substring, this.mUserContext, this.mCustomTileStatePersister, this.mTileServices);
            }
            throw new IllegalArgumentException("Empty custom tile spec action");
        }

        public Builder(Lazy<QSHost> lazy, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, CustomTileStatePersister customTileStatePersister, TileServices tileServices) {
            this.mQSHostLazy = lazy;
            this.mBackgroundLooper = looper;
            this.mMainHandler = handler;
            this.mFalsingManager = falsingManager;
            this.mMetricsLogger = metricsLogger;
            this.mStatusBarStateController = statusBarStateController;
            this.mActivityStarter = activityStarter;
            this.mQSLogger = qSLogger;
            this.mCustomTileStatePersister = customTileStatePersister;
            this.mTileServices = tileServices;
        }
    }

    public CustomTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, String str, Context context, CustomTileStatePersister customTileStatePersister, TileServices tileServices) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mTileServices = tileServices;
        ComponentName unflattenFromString = ComponentName.unflattenFromString(str);
        this.mComponent = unflattenFromString;
        this.mUserContext = context;
        int userId = context.getUserId();
        this.mUser = userId;
        this.mKey = new TileServiceKey(unflattenFromString, userId);
        Objects.requireNonNull(tileServices);
        BroadcastDispatcher broadcastDispatcher = tileServices.mBroadcastDispatcher;
        Handler handler2 = tileServices.mHandler;
        UserTracker userTracker = tileServices.mUserTracker;
        Context context2 = tileServices.mContext;
        TileServiceManager tileServiceManager = new TileServiceManager(tileServices, handler2, userTracker, new TileLifecycleManager(handler2, context2, tileServices, new PackageManagerAdapter(context2), broadcastDispatcher, new Intent().setComponent(unflattenFromString), userTracker.getUserHandle()));
        synchronized (tileServices.mServices) {
            tileServices.mServices.put(this, tileServiceManager);
            tileServices.mTiles.put(unflattenFromString, this);
            ArrayMap<IBinder, CustomTile> arrayMap = tileServices.mTokenMap;
            TileLifecycleManager tileLifecycleManager = tileServiceManager.mStateManager;
            Objects.requireNonNull(tileLifecycleManager);
            arrayMap.put(tileLifecycleManager.mToken, this);
        }
        tileServiceManager.mStarted = true;
        TileLifecycleManager tileLifecycleManager2 = tileServiceManager.mStateManager;
        Objects.requireNonNull(tileLifecycleManager2);
        ComponentName component = tileLifecycleManager2.mIntent.getComponent();
        TileServices tileServices2 = tileServiceManager.mServices;
        Objects.requireNonNull(tileServices2);
        Context context3 = tileServices2.mContext;
        if (!context3.getSharedPreferences("tiles_prefs", 0).getBoolean(component.flattenToString(), false)) {
            context3.getSharedPreferences("tiles_prefs", 0).edit().putBoolean(component.flattenToString(), true).commit();
            tileServiceManager.mStateManager.onTileAdded();
            TileLifecycleManager tileLifecycleManager3 = tileServiceManager.mStateManager;
            Objects.requireNonNull(tileLifecycleManager3);
            tileLifecycleManager3.mUnbindImmediate = true;
            tileLifecycleManager3.setBindService(true);
        }
        this.mServiceManager = tileServiceManager;
        this.mService = tileServiceManager.mStateManager;
        this.mCustomTileStatePersister = customTileStatePersister;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final int getMetricsCategory() {
        return 268;
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x0070  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0071  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0076 A[Catch: NameNotFoundException -> 0x00b0, TryCatch #0 {NameNotFoundException -> 0x00b0, blocks: (B:3:0x0001, B:6:0x001d, B:9:0x0028, B:10:0x002c, B:12:0x0035, B:18:0x0046, B:20:0x004d, B:23:0x0054, B:26:0x005f, B:34:0x0076, B:36:0x0082, B:38:0x0086, B:39:0x008b, B:41:0x0093, B:44:0x00a2, B:46:0x00aa), top: B:49:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0081  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0086 A[Catch: NameNotFoundException -> 0x00b0, TryCatch #0 {NameNotFoundException -> 0x00b0, blocks: (B:3:0x0001, B:6:0x001d, B:9:0x0028, B:10:0x002c, B:12:0x0035, B:18:0x0046, B:20:0x004d, B:23:0x0054, B:26:0x005f, B:34:0x0076, B:36:0x0082, B:38:0x0086, B:39:0x008b, B:41:0x0093, B:44:0x00a2, B:46:0x00aa), top: B:49:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00aa A[Catch: NameNotFoundException -> 0x00b0, TRY_LEAVE, TryCatch #0 {NameNotFoundException -> 0x00b0, blocks: (B:3:0x0001, B:6:0x001d, B:9:0x0028, B:10:0x002c, B:12:0x0035, B:18:0x0046, B:20:0x004d, B:23:0x0054, B:26:0x005f, B:34:0x0076, B:36:0x0082, B:38:0x0086, B:39:0x008b, B:41:0x0093, B:44:0x00a2, B:46:0x00aa), top: B:49:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:51:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateDefaultTileAndIcon() {
        /*
            r10 = this;
            r0 = 0
            android.content.Context r1 = r10.mUserContext     // Catch: NameNotFoundException -> 0x00b0
            android.content.pm.PackageManager r1 = r1.getPackageManager()     // Catch: NameNotFoundException -> 0x00b0
            r2 = 786432(0xc0000, float:1.102026E-39)
            android.content.ComponentName r3 = r10.mComponent     // Catch: NameNotFoundException -> 0x00b0
            java.lang.String r3 = r3.getPackageName()     // Catch: NameNotFoundException -> 0x00b0
            r4 = 0
            android.content.pm.ApplicationInfo r3 = r1.getApplicationInfo(r3, r4)     // Catch: NameNotFoundException -> 0x00b0
            boolean r3 = r3.isSystemApp()     // Catch: NameNotFoundException -> 0x00b0
            if (r3 == 0) goto L_0x001d
            r2 = 786944(0xc0200, float:1.102743E-39)
        L_0x001d:
            android.content.ComponentName r3 = r10.mComponent     // Catch: NameNotFoundException -> 0x00b0
            android.content.pm.ServiceInfo r2 = r1.getServiceInfo(r3, r2)     // Catch: NameNotFoundException -> 0x00b0
            int r3 = r2.icon     // Catch: NameNotFoundException -> 0x00b0
            if (r3 == 0) goto L_0x0028
            goto L_0x002c
        L_0x0028:
            android.content.pm.ApplicationInfo r3 = r2.applicationInfo     // Catch: NameNotFoundException -> 0x00b0
            int r3 = r3.icon     // Catch: NameNotFoundException -> 0x00b0
        L_0x002c:
            android.service.quicksettings.Tile r5 = r10.mTile     // Catch: NameNotFoundException -> 0x00b0
            android.graphics.drawable.Icon r5 = r5.getIcon()     // Catch: NameNotFoundException -> 0x00b0
            r6 = 1
            if (r5 == 0) goto L_0x0073
            android.service.quicksettings.Tile r5 = r10.mTile     // Catch: NameNotFoundException -> 0x00b0
            android.graphics.drawable.Icon r5 = r5.getIcon()     // Catch: NameNotFoundException -> 0x00b0
            android.graphics.drawable.Icon r7 = r10.mDefaultIcon     // Catch: NameNotFoundException -> 0x00b0
            if (r5 != r7) goto L_0x0041
        L_0x003f:
            r5 = r6
            goto L_0x006e
        L_0x0041:
            if (r5 == 0) goto L_0x006d
            if (r7 != 0) goto L_0x0046
            goto L_0x006d
        L_0x0046:
            int r8 = r5.getType()     // Catch: NameNotFoundException -> 0x00b0
            r9 = 2
            if (r8 != r9) goto L_0x006d
            int r8 = r7.getType()     // Catch: NameNotFoundException -> 0x00b0
            if (r8 == r9) goto L_0x0054
            goto L_0x006d
        L_0x0054:
            int r8 = r5.getResId()     // Catch: NameNotFoundException -> 0x00b0
            int r9 = r7.getResId()     // Catch: NameNotFoundException -> 0x00b0
            if (r8 == r9) goto L_0x005f
            goto L_0x006d
        L_0x005f:
            java.lang.String r5 = r5.getResPackage()     // Catch: NameNotFoundException -> 0x00b0
            java.lang.String r7 = r7.getResPackage()     // Catch: NameNotFoundException -> 0x00b0
            boolean r5 = java.util.Objects.equals(r5, r7)     // Catch: NameNotFoundException -> 0x00b0
            if (r5 != 0) goto L_0x003f
        L_0x006d:
            r5 = r4
        L_0x006e:
            if (r5 == 0) goto L_0x0071
            goto L_0x0073
        L_0x0071:
            r5 = r4
            goto L_0x0074
        L_0x0073:
            r5 = r6
        L_0x0074:
            if (r3 == 0) goto L_0x0081
            android.content.ComponentName r7 = r10.mComponent     // Catch: NameNotFoundException -> 0x00b0
            java.lang.String r7 = r7.getPackageName()     // Catch: NameNotFoundException -> 0x00b0
            android.graphics.drawable.Icon r3 = android.graphics.drawable.Icon.createWithResource(r7, r3)     // Catch: NameNotFoundException -> 0x00b0
            goto L_0x0082
        L_0x0081:
            r3 = r0
        L_0x0082:
            r10.mDefaultIcon = r3     // Catch: NameNotFoundException -> 0x00b0
            if (r5 == 0) goto L_0x008b
            android.service.quicksettings.Tile r5 = r10.mTile     // Catch: NameNotFoundException -> 0x00b0
            r5.setIcon(r3)     // Catch: NameNotFoundException -> 0x00b0
        L_0x008b:
            android.service.quicksettings.Tile r3 = r10.mTile     // Catch: NameNotFoundException -> 0x00b0
            java.lang.CharSequence r3 = r3.getLabel()     // Catch: NameNotFoundException -> 0x00b0
            if (r3 == 0) goto L_0x00a1
            android.service.quicksettings.Tile r3 = r10.mTile     // Catch: NameNotFoundException -> 0x00b0
            java.lang.CharSequence r3 = r3.getLabel()     // Catch: NameNotFoundException -> 0x00b0
            java.lang.CharSequence r5 = r10.mDefaultLabel     // Catch: NameNotFoundException -> 0x00b0
            boolean r3 = android.text.TextUtils.equals(r3, r5)     // Catch: NameNotFoundException -> 0x00b0
            if (r3 == 0) goto L_0x00a2
        L_0x00a1:
            r4 = r6
        L_0x00a2:
            java.lang.CharSequence r1 = r2.loadLabel(r1)     // Catch: NameNotFoundException -> 0x00b0
            r10.mDefaultLabel = r1     // Catch: NameNotFoundException -> 0x00b0
            if (r4 == 0) goto L_0x00b4
            android.service.quicksettings.Tile r2 = r10.mTile     // Catch: NameNotFoundException -> 0x00b0
            r2.setLabel(r1)     // Catch: NameNotFoundException -> 0x00b0
            goto L_0x00b4
        L_0x00b0:
            r10.mDefaultIcon = r0
            r10.mDefaultLabel = r0
        L_0x00b4:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.external.CustomTile.updateDefaultTileAndIcon():void");
    }

    public static String toSpec(ComponentName componentName) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("custom(");
        m.append(componentName.flattenToShortString());
        m.append(")");
        return m.toString();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final Intent getLongClickIntent() {
        Intent intent;
        Intent intent2 = new Intent("android.service.quicksettings.action.QS_TILE_PREFERENCES");
        intent2.setPackage(this.mComponent.getPackageName());
        ResolveInfo resolveActivityAsUser = this.mContext.getPackageManager().resolveActivityAsUser(intent2, 0, this.mUser);
        if (resolveActivityAsUser != null) {
            Intent intent3 = new Intent("android.service.quicksettings.action.QS_TILE_PREFERENCES");
            ActivityInfo activityInfo = resolveActivityAsUser.activityInfo;
            intent = intent3.setClassName(activityInfo.packageName, activityInfo.name);
        } else {
            intent = null;
        }
        if (intent == null) {
            return new Intent("android.settings.APPLICATION_DETAILS_SETTINGS").setData(Uri.fromParts("package", this.mComponent.getPackageName(), null));
        }
        intent.putExtra("android.intent.extra.COMPONENT_NAME", this.mComponent);
        intent.putExtra("state", this.mTile.getState());
        return intent;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final String getMetricsSpec() {
        return this.mComponent.getPackageName();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final long getStaleTimeout() {
        return (this.mHost.indexOf(this.mTileSpec) * 60000) + 3600000;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final CharSequence getTileLabel() {
        return this.mState.label;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleClick(View view) {
        if (this.mTile.getState() != 0) {
            try {
                this.mWindowManager.addWindowToken(this.mToken, 2035, 0, (Bundle) null);
                this.mIsTokenGranted = true;
            } catch (RemoteException unused) {
            }
            try {
                if (this.mServiceManager.isActiveTile()) {
                    this.mServiceManager.setBindRequested(true);
                    this.mService.onStartListening();
                }
                this.mService.onClick(this.mToken);
            } catch (RemoteException unused2) {
            }
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUpdateState(QSTile.State state, Object obj) {
        Drawable drawable;
        int state2 = this.mTile.getState();
        TileServiceManager tileServiceManager = this.mServiceManager;
        Objects.requireNonNull(tileServiceManager);
        boolean z = false;
        if (tileServiceManager.mPendingBind) {
            state2 = 0;
        }
        state.state = state2;
        try {
            drawable = this.mTile.getIcon().loadDrawable(this.mUserContext);
        } catch (Exception unused) {
            Log.w(this.TAG, "Invalid icon, forcing into unavailable state");
            state.state = 0;
            drawable = this.mDefaultIcon.loadDrawable(this.mUserContext);
        }
        state.iconSupplier = new CustomTile$$ExternalSyntheticLambda1(drawable, 0);
        state.label = this.mTile.getLabel();
        CharSequence subtitle = this.mTile.getSubtitle();
        if (subtitle == null || subtitle.length() <= 0) {
            state.secondaryLabel = null;
        } else {
            state.secondaryLabel = subtitle;
        }
        if (this.mTile.getContentDescription() != null) {
            state.contentDescription = this.mTile.getContentDescription();
        } else {
            state.contentDescription = state.label;
        }
        if (this.mTile.getStateDescription() != null) {
            state.stateDescription = this.mTile.getStateDescription();
        } else {
            state.stateDescription = null;
        }
        if (state instanceof QSTile.BooleanState) {
            state.expandedAccessibilityClassName = Switch.class.getName();
            QSTile.BooleanState booleanState = (QSTile.BooleanState) state;
            if (state.state == 2) {
                z = true;
            }
            booleanState.value = z;
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final boolean isAvailable() {
        if (!this.mInitialDefaultIconFetched.get() || this.mDefaultIcon != null) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final QSTile.State newTileState() {
        TileServiceManager tileServiceManager = this.mServiceManager;
        if (tileServiceManager == null || !tileServiceManager.isToggleableTile()) {
            return new QSTile.State();
        }
        return new QSTile.BooleanState();
    }

    public static ComponentName getComponentFromSpec(String str) {
        String substring = str.substring(7, str.length() - 1);
        if (!substring.isEmpty()) {
            return ComponentName.unflattenFromString(substring);
        }
        throw new IllegalArgumentException("Empty custom tile spec action");
    }

    public final void applyTileState(Tile tile, boolean z) {
        if (tile.getIcon() != null || z) {
            this.mTile.setIcon(tile.getIcon());
        }
        if (tile.getLabel() != null || z) {
            this.mTile.setLabel(tile.getLabel());
        }
        if (tile.getSubtitle() != null || z) {
            this.mTile.setSubtitle(tile.getSubtitle());
        }
        if (tile.getContentDescription() != null || z) {
            this.mTile.setContentDescription(tile.getContentDescription());
        }
        if (tile.getStateDescription() != null || z) {
            this.mTile.setStateDescription(tile.getStateDescription());
        }
        this.mTile.setState(tile.getState());
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleDestroy() {
        super.handleDestroy();
        if (this.mIsTokenGranted) {
            try {
                this.mWindowManager.removeWindowToken(this.mToken, 0);
            } catch (RemoteException unused) {
            }
        }
        TileServices tileServices = this.mTileServices;
        TileServiceManager tileServiceManager = this.mServiceManager;
        Objects.requireNonNull(tileServices);
        synchronized (tileServices.mServices) {
            tileServiceManager.setBindAllowed(false);
            tileServiceManager.setBindAllowed(false);
            TileServices tileServices2 = tileServiceManager.mServices;
            Objects.requireNonNull(tileServices2);
            tileServices2.mContext.unregisterReceiver(tileServiceManager.mUninstallReceiver);
            TileLifecycleManager tileLifecycleManager = tileServiceManager.mStateManager;
            Objects.requireNonNull(tileLifecycleManager);
            if (tileLifecycleManager.mPackageReceiverRegistered.get() || tileLifecycleManager.mUserReceiverRegistered.get()) {
                tileLifecycleManager.stopPackageListening();
            }
            tileLifecycleManager.mChangeListener = null;
            tileServices.mServices.remove(this);
            ArrayMap<IBinder, CustomTile> arrayMap = tileServices.mTokenMap;
            TileLifecycleManager tileLifecycleManager2 = tileServiceManager.mStateManager;
            Objects.requireNonNull(tileLifecycleManager2);
            arrayMap.remove(tileLifecycleManager2.mToken);
            tileServices.mTiles.remove(this.mComponent);
            tileServices.mMainHandler.post(new TileServices$$ExternalSyntheticLambda0(tileServices, this.mComponent.getClassName(), 0));
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0088  */
    /* JADX WARN: Removed duplicated region for block: B:24:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r0v17, types: [TState extends com.android.systemui.plugins.qs.QSTile$State, com.android.systemui.plugins.qs.QSTile$State] */
    /* JADX WARN: Type inference failed for: r0v18, types: [TState extends com.android.systemui.plugins.qs.QSTile$State, com.android.systemui.plugins.qs.QSTile$State] */
    /* JADX WARN: Unknown variable types count: 1 */
    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void handleInitialize() {
        /*
            r5 = this;
            r5.updateDefaultTileAndIcon()
            java.util.concurrent.atomic.AtomicBoolean r0 = r5.mInitialDefaultIconFetched
            r1 = 0
            r2 = 1
            boolean r0 = r0.compareAndSet(r1, r2)
            if (r0 == 0) goto L_0x0031
            android.graphics.drawable.Icon r0 = r5.mDefaultIcon
            if (r0 != 0) goto L_0x0031
            java.lang.String r0 = r5.TAG
            java.lang.String r2 = "No default icon for "
            java.lang.StringBuilder r2 = android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1.m(r2)
            java.lang.String r3 = r5.mTileSpec
            r2.append(r3)
            java.lang.String r3 = ", destroying tile"
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            android.util.Log.w(r0, r2)
            com.android.systemui.qs.QSHost r0 = r5.mHost
            java.lang.String r2 = r5.mTileSpec
            r0.removeTile(r2)
        L_0x0031:
            com.android.systemui.qs.external.TileServiceManager r0 = r5.mServiceManager
            boolean r0 = r0.isToggleableTile()
            if (r0 == 0) goto L_0x004d
            com.android.systemui.plugins.qs.QSTile$State r0 = r5.newTileState()
            r5.mState = r0
            com.android.systemui.plugins.qs.QSTile$State r0 = r5.newTileState()
            r5.mTmpState = r0
            TState extends com.android.systemui.plugins.qs.QSTile$State r2 = r5.mState
            java.lang.String r3 = r5.mTileSpec
            r2.spec = r3
            r0.spec = r3
        L_0x004d:
            com.android.systemui.qs.external.TileServiceManager r0 = r5.mServiceManager
            java.util.Objects.requireNonNull(r0)
            com.android.systemui.qs.external.TileLifecycleManager r0 = r0.mStateManager
            java.util.Objects.requireNonNull(r0)
            r0.mChangeListener = r5
            com.android.systemui.qs.external.TileServiceManager r0 = r5.mServiceManager
            boolean r0 = r0.isActiveTile()
            if (r0 == 0) goto L_0x0095
            com.android.systemui.qs.external.CustomTileStatePersister r0 = r5.mCustomTileStatePersister
            com.android.systemui.qs.external.TileServiceKey r2 = r5.mKey
            java.util.Objects.requireNonNull(r0)
            android.content.SharedPreferences r0 = r0.sharedPreferences
            java.lang.String r2 = r2.string
            r3 = 0
            java.lang.String r0 = r0.getString(r2, r3)
            if (r0 != 0) goto L_0x0074
            goto L_0x0085
        L_0x0074:
            android.service.quicksettings.Tile r0 = com.android.systemui.qs.external.CustomTileStatePersisterKt.readTileFromString(r0)     // Catch: JSONException -> 0x0079
            goto L_0x0086
        L_0x0079:
            r2 = move-exception
            java.lang.String r4 = "Bad saved state: "
            java.lang.String r0 = kotlin.jvm.internal.Intrinsics.stringPlus(r4, r0)
            java.lang.String r4 = "TileServicePersistence"
            android.util.Log.e(r4, r0, r2)
        L_0x0085:
            r0 = r3
        L_0x0086:
            if (r0 == 0) goto L_0x0095
            r5.applyTileState(r0, r1)
            com.android.systemui.qs.external.TileServiceManager r0 = r5.mServiceManager
            java.util.Objects.requireNonNull(r0)
            r0.mPendingBind = r1
            r5.refreshState(r3)
        L_0x0095:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.external.CustomTile.handleInitialize():void");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleSetListening(boolean z) {
        super.handleSetListening(z);
        if (this.mListening != z) {
            this.mListening = z;
            try {
                if (z) {
                    updateDefaultTileAndIcon();
                    refreshState(null);
                    if (!this.mServiceManager.isActiveTile()) {
                        this.mServiceManager.setBindRequested(true);
                        this.mService.onStartListening();
                        return;
                    }
                    return;
                }
                this.mService.onStopListening();
                if (this.mIsTokenGranted && !this.mIsShowingDialog) {
                    try {
                        this.mWindowManager.removeWindowToken(this.mToken, 0);
                    } catch (RemoteException unused) {
                    }
                    this.mIsTokenGranted = false;
                }
                this.mIsShowingDialog = false;
                this.mServiceManager.setBindRequested(false);
            } catch (RemoteException unused2) {
            }
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final LogMaker populate(LogMaker logMaker) {
        return super.populate(logMaker).setComponentName(this.mComponent);
    }
}
