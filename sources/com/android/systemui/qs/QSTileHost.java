package com.android.systemui.qs;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import androidx.fragment.app.DialogFragment$$ExternalSyntheticOutline0;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.InstanceIdSequence;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.plugins.qs.QSFactory;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.external.CustomTile;
import com.android.systemui.qs.external.CustomTileStatePersister;
import com.android.systemui.qs.external.TileLifecycleManager;
import com.android.systemui.qs.external.TileRequestDialogEventLogger;
import com.android.systemui.qs.external.TileServiceRequestController;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.settings.SecureSettings;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSTileHost implements QSHost, TunerService.Tunable, PluginListener<QSFactory>, Dumpable {
    public static final boolean DEBUG = Log.isLoggable("QSTileHost", 3);
    public AutoTileManager mAutoTiles;
    public final Context mContext;
    public int mCurrentUser;
    public final CustomTileStatePersister mCustomTileStatePersister;
    public final StatusBarIconController mIconController;
    public final PluginManager mPluginManager;
    public final QSLogger mQSLogger;
    public final ArrayList<QSFactory> mQsFactories;
    public SecureSettings mSecureSettings;
    public final Optional<StatusBar> mStatusBarOptional;
    public TileLifecycleManager.Factory mTileLifeCycleManagerFactory;
    public final TileServiceRequestController mTileServiceRequestController;
    public final TunerService mTunerService;
    public final UiEventLogger mUiEventLogger;
    public Context mUserContext;
    public UserTracker mUserTracker;
    public final LinkedHashMap<String, QSTile> mTiles = new LinkedHashMap<>();
    public final ArrayList<String> mTileSpecs = new ArrayList<>();
    public final ArrayList mCallbacks = new ArrayList();
    public final InstanceIdSequence mInstanceIdSequence = new InstanceIdSequence(1048576);

    public QSTileHost(Context context, StatusBarIconController statusBarIconController, QSFactory qSFactory, Handler handler, PluginManager pluginManager, final TunerService tunerService, final Provider provider, DumpManager dumpManager, Optional optional, QSLogger qSLogger, UiEventLogger uiEventLogger, UserTracker userTracker, SecureSettings secureSettings, CustomTileStatePersister customTileStatePersister, TileServiceRequestController.Builder builder, TileLifecycleManager.Factory factory) {
        ArrayList<QSFactory> arrayList = new ArrayList<>();
        this.mQsFactories = arrayList;
        this.mIconController = statusBarIconController;
        this.mContext = context;
        this.mUserContext = context;
        this.mTunerService = tunerService;
        this.mPluginManager = pluginManager;
        this.mQSLogger = qSLogger;
        this.mUiEventLogger = uiEventLogger;
        Objects.requireNonNull(builder);
        this.mTileServiceRequestController = new TileServiceRequestController(this, builder.commandQueue, builder.commandRegistry, new TileRequestDialogEventLogger(new UiEventLoggerImpl(), new InstanceIdSequence(1048576)));
        this.mTileLifeCycleManagerFactory = factory;
        this.mStatusBarOptional = optional;
        arrayList.add(qSFactory);
        pluginManager.addPluginListener((PluginListener) this, QSFactory.class, true);
        dumpManager.registerDumpable("QSTileHost", this);
        this.mUserTracker = userTracker;
        this.mSecureSettings = secureSettings;
        this.mCustomTileStatePersister = customTileStatePersister;
        handler.post(new Runnable() { // from class: com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                QSTileHost qSTileHost = QSTileHost.this;
                TunerService tunerService2 = tunerService;
                Provider provider2 = provider;
                Objects.requireNonNull(qSTileHost);
                tunerService2.addTunable(qSTileHost, "sysui_qs_tiles");
                qSTileHost.mAutoTiles = (AutoTileManager) provider2.mo144get();
                qSTileHost.mTileServiceRequestController.init();
            }
        });
    }

    public final void addTile(final String str, final int i) {
        if (str.equals("work")) {
            Log.wtfStack("QSTileHost", "Adding work tile");
        }
        changeTileSpecs(new Predicate() { // from class: com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda7
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                String str2 = str;
                int i2 = i;
                List list = (List) obj;
                if (list.contains(str2)) {
                    return false;
                }
                int size = list.size();
                if (i2 == -1 || i2 >= size) {
                    list.add(str2);
                } else {
                    list.add(i2, str2);
                }
                return true;
            }
        });
    }

    public final QSTile createTile(String str) {
        for (int i = 0; i < this.mQsFactories.size(); i++) {
            QSTile createTile = this.mQsFactories.get(i).createTile(str);
            if (createTile != null) {
                return createTile;
            }
        }
        return null;
    }

    @Override // com.android.systemui.qs.QSHost
    public final void warn() {
    }

    public static ArrayList getDefaultSpecs(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(Arrays.asList(context.getResources().getString(2131953139).split(",")));
        if (Build.IS_DEBUGGABLE) {
            arrayList.add("dbg:mem");
        }
        return arrayList;
    }

    public final void changeTileSpecs(Predicate<List<String>> predicate) {
        ArrayList loadTileSpecs = loadTileSpecs(this.mContext, this.mSecureSettings.getStringForUser("sysui_qs_tiles", this.mCurrentUser));
        if (predicate.test(loadTileSpecs)) {
            saveTilesToSettings(loadTileSpecs);
        }
    }

    public final void changeTiles(List list, ArrayList arrayList) {
        ArrayList arrayList2 = new ArrayList(list);
        int size = arrayList2.size();
        for (int i = 0; i < size; i++) {
            String str = (String) arrayList2.get(i);
            if (str.startsWith("custom(") && !arrayList.contains(str)) {
                ComponentName componentFromSpec = CustomTile.getComponentFromSpec(str);
                TileLifecycleManager create = this.mTileLifeCycleManagerFactory.create(new Intent().setComponent(componentFromSpec), new UserHandle(this.mCurrentUser));
                create.onStopListening();
                create.onTileRemoved();
                CustomTileStatePersister customTileStatePersister = this.mCustomTileStatePersister;
                int i2 = this.mCurrentUser;
                StringBuilder sb = new StringBuilder();
                sb.append((Object) componentFromSpec.flattenToString());
                sb.append(':');
                sb.append(i2);
                String sb2 = sb.toString();
                Objects.requireNonNull(customTileStatePersister);
                customTileStatePersister.sharedPreferences.edit().remove(sb2).apply();
                this.mContext.getSharedPreferences("tiles_prefs", 0).edit().putBoolean(componentFromSpec.flattenToString(), false).commit();
                create.mUnbindImmediate = true;
                create.setBindService(true);
            }
        }
        if (DEBUG) {
            Log.d("QSTileHost", "saveCurrentTiles " + arrayList);
        }
        saveTilesToSettings(arrayList);
    }

    @Override // com.android.systemui.qs.QSHost
    public final void collapsePanels() {
        this.mStatusBarOptional.ifPresent(QSTileHost$$ExternalSyntheticLambda5.INSTANCE);
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] strArr) {
        printWriter.println("QSTileHost:");
        this.mTiles.values().stream().filter(QSTileHost$$ExternalSyntheticLambda10.INSTANCE).forEach(new Consumer() { // from class: com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((Dumpable) ((QSTile) obj)).dump(fileDescriptor, printWriter, strArr);
            }
        });
    }

    @Override // com.android.systemui.qs.QSHost
    public final InstanceId getNewInstanceId() {
        return this.mInstanceIdSequence.newInstanceId();
    }

    @Override // com.android.systemui.qs.QSHost
    public final int indexOf(String str) {
        return this.mTileSpecs.indexOf(str);
    }

    @Override // com.android.systemui.plugins.PluginListener
    public final void onPluginConnected(QSFactory qSFactory, Context context) {
        this.mQsFactories.add(0, qSFactory);
        String value = this.mTunerService.getValue("sysui_qs_tiles");
        onTuningChanged("sysui_qs_tiles", "");
        onTuningChanged("sysui_qs_tiles", value);
    }

    @Override // com.android.systemui.plugins.PluginListener
    public final void onPluginDisconnected(QSFactory qSFactory) {
        this.mQsFactories.remove(qSFactory);
        String value = this.mTunerService.getValue("sysui_qs_tiles");
        onTuningChanged("sysui_qs_tiles", "");
        onTuningChanged("sysui_qs_tiles", value);
    }

    @Override // com.android.systemui.qs.QSHost
    public final void openPanels() {
        this.mStatusBarOptional.ifPresent(QSTileHost$$ExternalSyntheticLambda4.INSTANCE);
    }

    @Override // com.android.systemui.qs.QSHost
    public final void removeTile(final String str) {
        changeTileSpecs(new Predicate() { // from class: com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((List) obj).remove(str);
            }
        });
    }

    @Override // com.android.systemui.qs.QSHost
    public final void removeTiles(final ArrayList arrayList) {
        changeTileSpecs(new Predicate() { // from class: com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((List) obj).removeAll(arrayList);
            }
        });
    }

    @Override // com.android.systemui.qs.QSHost
    public final void unmarkTileAsAutoAdded(String str) {
        String str2;
        AutoTileManager autoTileManager = this.mAutoTiles;
        if (autoTileManager != null) {
            AutoAddTracker autoAddTracker = autoTileManager.mAutoTracker;
            Objects.requireNonNull(autoAddTracker);
            synchronized (autoAddTracker.autoAdded) {
                if (autoAddTracker.autoAdded.remove(str)) {
                    str2 = TextUtils.join(",", autoAddTracker.autoAdded);
                } else {
                    str2 = null;
                }
            }
            if (str2 != null) {
                autoAddTracker.secureSettings.putStringForUser$1("qs_auto_tiles", str2, autoAddTracker.userId);
            }
        }
    }

    public static ArrayList loadTileSpecs(Context context, String str) {
        Resources resources = context.getResources();
        if (TextUtils.isEmpty(str)) {
            str = resources.getString(2131953138);
            if (DEBUG) {
                DialogFragment$$ExternalSyntheticOutline0.m("Loaded tile specs from config: ", str, "QSTileHost");
            }
        } else if (DEBUG) {
            DialogFragment$$ExternalSyntheticOutline0.m("Loaded tile specs from setting: ", str, "QSTileHost");
        }
        ArrayList arrayList = new ArrayList();
        ArraySet arraySet = new ArraySet();
        boolean z = false;
        for (String str2 : str.split(",")) {
            String trim = str2.trim();
            if (!trim.isEmpty()) {
                if (trim.equals("default")) {
                    if (!z) {
                        Iterator it = getDefaultSpecs(context).iterator();
                        while (it.hasNext()) {
                            String str3 = (String) it.next();
                            if (!arraySet.contains(str3)) {
                                arrayList.add(str3);
                                arraySet.add(str3);
                            }
                        }
                        z = true;
                    }
                } else if (!arraySet.contains(trim)) {
                    arrayList.add(trim);
                    arraySet.add(trim);
                }
            }
        }
        if (arrayList.contains("internet")) {
            arrayList.remove("wifi");
            arrayList.remove("cell");
        } else if (arrayList.contains("wifi")) {
            arrayList.set(arrayList.indexOf("wifi"), "internet");
            arrayList.remove("cell");
        } else if (arrayList.contains("cell")) {
            arrayList.set(arrayList.indexOf("cell"), "internet");
        }
        return arrayList;
    }

    public final void addTile(ComponentName componentName, boolean z) {
        String spec = CustomTile.toSpec(componentName);
        if (!this.mTileSpecs.contains(spec)) {
            ArrayList arrayList = new ArrayList(this.mTileSpecs);
            if (z) {
                arrayList.add(spec);
            } else {
                arrayList.add(0, spec);
            }
            changeTiles(this.mTileSpecs, arrayList);
        }
    }

    @Override // com.android.systemui.tuner.TunerService.Tunable
    public final void onTuningChanged(String str, String str2) {
        boolean z;
        if ("sysui_qs_tiles".equals(str)) {
            Log.d("QSTileHost", "Recreating tiles");
            if (str2 == null && UserManager.isDeviceInDemoMode(this.mContext)) {
                str2 = this.mContext.getResources().getString(2131953140);
            }
            final ArrayList loadTileSpecs = loadTileSpecs(this.mContext, str2);
            int userId = this.mUserTracker.getUserId();
            if (userId != this.mCurrentUser) {
                this.mUserContext = this.mUserTracker.getUserContext();
                AutoTileManager autoTileManager = this.mAutoTiles;
                if (autoTileManager != null) {
                    autoTileManager.changeUser(UserHandle.of(userId));
                }
            }
            if (!loadTileSpecs.equals(this.mTileSpecs) || userId != this.mCurrentUser) {
                this.mTiles.entrySet().stream().filter(new Predicate() { // from class: com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda9
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return !loadTileSpecs.contains(((Map.Entry) obj).getKey());
                    }
                }).forEach(new QSTileHost$$ExternalSyntheticLambda1(this, 0));
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                Iterator it = loadTileSpecs.iterator();
                while (it.hasNext()) {
                    String str3 = (String) it.next();
                    QSTile qSTile = this.mTiles.get(str3);
                    if (qSTile == null || (((z = qSTile instanceof CustomTile)) && ((CustomTile) qSTile).mUser != userId)) {
                        if (qSTile != null) {
                            qSTile.destroy();
                            Log.d("QSTileHost", "Destroying tile for wrong user: " + str3);
                            this.mQSLogger.logTileDestroyed(str3, "Tile for wrong user");
                        }
                        DialogFragment$$ExternalSyntheticOutline0.m("Creating tile: ", str3, "QSTileHost");
                        try {
                            QSTile createTile = createTile(str3);
                            if (createTile != null) {
                                createTile.setTileSpec(str3);
                                if (createTile.isAvailable()) {
                                    linkedHashMap.put(str3, createTile);
                                    this.mQSLogger.logTileAdded(str3);
                                } else {
                                    createTile.destroy();
                                    Log.d("QSTileHost", "Destroying not available tile: " + str3);
                                    this.mQSLogger.logTileDestroyed(str3, "Tile not available");
                                }
                            }
                        } catch (Throwable th) {
                            Log.w("QSTileHost", "Error creating tile for spec: " + str3, th);
                        }
                    } else if (qSTile.isAvailable()) {
                        if (DEBUG) {
                            Log.d("QSTileHost", "Adding " + qSTile);
                        }
                        qSTile.removeCallbacks();
                        if (!z && this.mCurrentUser != userId) {
                            qSTile.userSwitch(userId);
                        }
                        linkedHashMap.put(str3, qSTile);
                        this.mQSLogger.logTileAdded(str3);
                    } else {
                        qSTile.destroy();
                        Log.d("QSTileHost", "Destroying not available tile: " + str3);
                        this.mQSLogger.logTileDestroyed(str3, "Tile not available");
                    }
                }
                this.mCurrentUser = userId;
                ArrayList arrayList = new ArrayList(this.mTileSpecs);
                this.mTileSpecs.clear();
                this.mTileSpecs.addAll(loadTileSpecs);
                this.mTiles.clear();
                this.mTiles.putAll(linkedHashMap);
                if (!linkedHashMap.isEmpty() || loadTileSpecs.isEmpty()) {
                    for (int i = 0; i < this.mCallbacks.size(); i++) {
                        ((QSHost.Callback) this.mCallbacks.get(i)).onTilesChanged();
                    }
                    return;
                }
                Log.d("QSTileHost", "No valid tiles on tuning changed. Setting to default.");
                changeTiles(arrayList, loadTileSpecs(this.mContext, ""));
            }
        }
    }

    public final void saveTilesToSettings(ArrayList arrayList) {
        if (arrayList.contains("work")) {
            Log.wtfStack("QSTileHost", "Saving work tile");
        }
        this.mSecureSettings.putStringForUser$1("sysui_qs_tiles", TextUtils.join(",", arrayList), this.mCurrentUser);
    }

    @Override // com.android.systemui.qs.QSHost
    public final Context getContext() {
        return this.mContext;
    }

    @Override // com.android.systemui.qs.QSHost
    public final UiEventLogger getUiEventLogger() {
        return this.mUiEventLogger;
    }

    @Override // com.android.systemui.qs.QSHost
    public final Context getUserContext() {
        return this.mUserContext;
    }

    @Override // com.android.systemui.qs.QSHost
    public final int getUserId() {
        return this.mCurrentUser;
    }
}
