package com.android.systemui.shared.plugins;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.util.ArraySet;
import android.util.Log;
import android.view.LayoutInflater;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.plugins.Plugin;
import com.android.systemui.plugins.PluginFragment;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.qs.tiles.ScreenRecordTile$$ExternalSyntheticLambda1;
import com.android.systemui.screenshot.ScreenshotController$$ExternalSyntheticLambda6;
import com.android.systemui.shared.plugins.PluginInstance;
import com.android.systemui.shared.plugins.VersionInfo;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda21;
import com.android.wm.shell.transition.OneShotRemoteHandler$2$$ExternalSyntheticLambda0;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class PluginActionManager<T extends Plugin> {
    public final String mAction;
    public final boolean mAllowMultiple;
    public final Executor mBgExecutor;
    public final Context mContext;
    public final boolean mIsDebuggable;
    public final PluginListener<T> mListener;
    public final Executor mMainExecutor;
    public final NotificationManager mNotificationManager;
    public final Class<T> mPluginClass;
    public final PluginEnabler mPluginEnabler;
    public final PluginInstance.Factory mPluginInstanceFactory;
    @VisibleForTesting
    private final ArrayList<PluginInstance<T>> mPluginInstances;
    public final PackageManager mPm;
    public final ArraySet<String> mPrivilegedPlugins;

    /* loaded from: classes.dex */
    public static class PluginContextWrapper extends ContextWrapper {
        public final ClassLoader mClassLoader;
        public LayoutInflater mInflater;

        @Override // android.content.ContextWrapper, android.content.Context
        public final Object getSystemService(String str) {
            if (!"layout_inflater".equals(str)) {
                return getBaseContext().getSystemService(str);
            }
            if (this.mInflater == null) {
                this.mInflater = LayoutInflater.from(getBaseContext()).cloneInContext(this);
            }
            return this.mInflater;
        }

        public PluginContextWrapper(Context context, ClassLoader classLoader) {
            super(context);
            this.mClassLoader = classLoader;
        }

        @Override // android.content.ContextWrapper, android.content.Context
        public final ClassLoader getClassLoader() {
            return this.mClassLoader;
        }
    }

    public PluginActionManager() {
        throw null;
    }

    public PluginActionManager(Context context, PackageManager packageManager, String str, PluginListener pluginListener, Class cls, boolean z, Executor executor, Executor executor2, boolean z2, NotificationManager notificationManager, PluginEnabler pluginEnabler, List list, PluginInstance.Factory factory) {
        ArraySet<String> arraySet = new ArraySet<>();
        this.mPrivilegedPlugins = arraySet;
        this.mPluginInstances = new ArrayList<>();
        this.mPluginClass = cls;
        this.mMainExecutor = executor;
        this.mBgExecutor = executor2;
        this.mContext = context;
        this.mPm = packageManager;
        this.mAction = str;
        this.mListener = pluginListener;
        this.mAllowMultiple = z;
        this.mNotificationManager = notificationManager;
        this.mPluginEnabler = pluginEnabler;
        this.mPluginInstanceFactory = factory;
        arraySet.addAll(list);
        this.mIsDebuggable = z2;
    }

    public final String toString() {
        return String.format("%s@%s (action=%s)", "PluginActionManager", Integer.valueOf(hashCode()), this.mAction);
    }

    /* loaded from: classes.dex */
    public static class Factory {
        public final Executor mBgExecutor;
        public final Context mContext;
        public final Executor mMainExecutor;
        public final NotificationManager mNotificationManager;
        public final PackageManager mPackageManager;
        public final PluginEnabler mPluginEnabler;
        public final PluginInstance.Factory mPluginInstanceFactory;
        public final List<String> mPrivilegedPlugins;

        public Factory(Context context, PackageManager packageManager, Executor executor, Executor executor2, NotificationManager notificationManager, PluginEnabler pluginEnabler, List<String> list, PluginInstance.Factory factory) {
            this.mContext = context;
            this.mPackageManager = packageManager;
            this.mMainExecutor = executor;
            this.mBgExecutor = executor2;
            this.mNotificationManager = notificationManager;
            this.mPluginEnabler = pluginEnabler;
            this.mPrivilegedPlugins = list;
            this.mPluginInstanceFactory = factory;
        }
    }

    public final boolean checkAndDisable(String str) {
        Iterator it = new ArrayList(this.mPluginInstances).iterator();
        boolean z = false;
        while (it.hasNext()) {
            PluginInstance<T> pluginInstance = (PluginInstance) it.next();
            Objects.requireNonNull(pluginInstance);
            if (str.startsWith(pluginInstance.mComponentName.getPackageName())) {
                z |= disable(pluginInstance, 3);
            }
        }
        return z;
    }

    public final <C> boolean dependsOn(Plugin plugin, Class<C> cls) {
        Iterator it = new ArrayList(this.mPluginInstances).iterator();
        while (it.hasNext()) {
            PluginInstance pluginInstance = (PluginInstance) it.next();
            Class<?> cls2 = plugin.getClass();
            Objects.requireNonNull(pluginInstance);
            if (pluginInstance.mPlugin.getClass().getName().equals(cls2.getName())) {
                VersionInfo versionInfo = pluginInstance.mVersionInfo;
                if (versionInfo == null) {
                    return false;
                }
                Objects.requireNonNull(versionInfo);
                if (versionInfo.mVersions.containsKey(cls)) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public final void destroy() {
        Iterator it = new ArrayList(this.mPluginInstances).iterator();
        while (it.hasNext()) {
            this.mMainExecutor.execute(new BubbleStackView$$ExternalSyntheticLambda21(this, (PluginInstance) it.next(), 1));
        }
    }

    public final boolean disableAll() {
        ArrayList arrayList = new ArrayList(this.mPluginInstances);
        boolean z = false;
        for (int i = 0; i < arrayList.size(); i++) {
            z |= disable((PluginInstance) arrayList.get(i), 4);
        }
        return z;
    }

    public final void handleQueryPlugins(String str) {
        Intent intent = new Intent(this.mAction);
        if (str != null) {
            intent.setPackage(str);
        }
        List<ResolveInfo> queryIntentServices = this.mPm.queryIntentServices(intent, 0);
        if (queryIntentServices.size() <= 1 || this.mAllowMultiple) {
            for (ResolveInfo resolveInfo : queryIntentServices) {
                ServiceInfo serviceInfo = resolveInfo.serviceInfo;
                ComponentName componentName = new ComponentName(serviceInfo.packageName, serviceInfo.name);
                PluginInstance<T> pluginInstance = null;
                if (!this.mIsDebuggable && !isPluginPrivileged(componentName)) {
                    Log.w("PluginInstanceManager", "Plugin cannot be loaded on production build: " + componentName);
                } else if (this.mPluginEnabler.isEnabled(componentName)) {
                    String packageName = componentName.getPackageName();
                    try {
                        if (this.mPm.checkPermission("com.android.systemui.permission.PLUGIN", packageName) != 0) {
                            Log.d("PluginInstanceManager", "Plugin doesn't have permission: " + packageName);
                        } else {
                            try {
                                pluginInstance = this.mPluginInstanceFactory.create(this.mContext, this.mPm.getApplicationInfo(packageName, 0), componentName, this.mPluginClass);
                            } catch (VersionInfo.InvalidVersionException e) {
                                reportInvalidVersion(componentName, componentName.getClassName(), e);
                            }
                        }
                    } catch (Throwable th) {
                        Log.w("PluginInstanceManager", "Couldn't load plugin: " + packageName, th);
                    }
                }
                if (pluginInstance != null) {
                    this.mPluginInstances.add(pluginInstance);
                    this.mMainExecutor.execute(new OneShotRemoteHandler$2$$ExternalSyntheticLambda0(this, pluginInstance, 2));
                }
            }
            return;
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Multiple plugins found for ");
        m.append(this.mAction);
        Log.w("PluginInstanceManager", m.toString());
    }

    public final boolean isPluginPrivileged(ComponentName componentName) {
        Iterator<String> it = this.mPrivilegedPlugins.iterator();
        while (it.hasNext()) {
            String next = it.next();
            ComponentName unflattenFromString = ComponentName.unflattenFromString(next);
            if (unflattenFromString == null) {
                if (next.equals(componentName.getPackageName())) {
                    return true;
                }
            } else if (unflattenFromString.equals(componentName)) {
                return true;
            }
        }
        return false;
    }

    public final void onPluginDisconnected(PluginInstance<T> pluginInstance) {
        PluginListener<T> pluginListener = this.mListener;
        Objects.requireNonNull(pluginInstance);
        pluginListener.onPluginDisconnected(pluginInstance.mPlugin);
        T t = pluginInstance.mPlugin;
        if (!(t instanceof PluginFragment)) {
            t.onDestroy();
        }
    }

    public final void removePkg(String str) {
        int size = this.mPluginInstances.size();
        while (true) {
            size--;
            if (size >= 0) {
                PluginInstance<T> pluginInstance = this.mPluginInstances.get(size);
                Objects.requireNonNull(pluginInstance);
                if (pluginInstance.mComponentName.getPackageName().equals(str)) {
                    this.mMainExecutor.execute(new ScreenRecordTile$$ExternalSyntheticLambda1(this, pluginInstance, 3));
                    this.mPluginInstances.remove(size);
                }
            } else {
                return;
            }
        }
    }

    public static void $r8$lambda$kBZKG6dtfbpWOlthMBUiG2NGTM4(PluginActionManager pluginActionManager) {
        Objects.requireNonNull(pluginActionManager);
        for (int size = pluginActionManager.mPluginInstances.size() - 1; size >= 0; size--) {
            pluginActionManager.mMainExecutor.execute(new ScreenshotController$$ExternalSyntheticLambda6(pluginActionManager, pluginActionManager.mPluginInstances.get(size), 1));
        }
        pluginActionManager.mPluginInstances.clear();
        pluginActionManager.handleQueryPlugins(null);
    }

    public static void $r8$lambda$nTrqaXNoyZ9Ewe_oMzlt3sqKZco(PluginActionManager pluginActionManager, String str) {
        Objects.requireNonNull(pluginActionManager);
        pluginActionManager.removePkg(str);
        if (pluginActionManager.mAllowMultiple || pluginActionManager.mPluginInstances.size() == 0) {
            pluginActionManager.handleQueryPlugins(str);
        }
    }

    public final boolean disable(PluginInstance<T> pluginInstance, int i) {
        Objects.requireNonNull(pluginInstance);
        ComponentName componentName = pluginInstance.mComponentName;
        if (isPluginPrivileged(componentName)) {
            return false;
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Disabling plugin ");
        m.append(componentName.flattenToShortString());
        Log.w("PluginInstanceManager", m.toString());
        this.mPluginEnabler.setDisabled(componentName, i);
        return true;
    }

    public final void reportInvalidVersion(ComponentName componentName, String str, VersionInfo.InvalidVersionException invalidVersionException) {
        Notification.Builder color = new Notification.Builder(this.mContext, "ALR").setStyle(new Notification.BigTextStyle()).setSmallIcon(Resources.getSystem().getIdentifier("stat_sys_warning", "drawable", ThemeOverlayApplier.ANDROID_PACKAGE)).setWhen(0L).setShowWhen(false).setVisibility(1).setColor(this.mContext.getColor(Resources.getSystem().getIdentifier("system_notification_accent_color", "color", ThemeOverlayApplier.ANDROID_PACKAGE)));
        try {
            str = this.mPm.getServiceInfo(componentName, 0).loadLabel(this.mPm).toString();
        } catch (PackageManager.NameNotFoundException unused) {
        }
        if (!invalidVersionException.isTooNew()) {
            Notification.Builder contentTitle = color.setContentTitle("Plugin \"" + str + "\" is too old");
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Contact plugin developer to get an updated version.\n");
            m.append(invalidVersionException.getMessage());
            contentTitle.setContentText(m.toString());
        } else {
            Notification.Builder contentTitle2 = color.setContentTitle("Plugin \"" + str + "\" is too new");
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Check to see if an OTA is available.\n");
            m2.append(invalidVersionException.getMessage());
            contentTitle2.setContentText(m2.toString());
        }
        Intent intent = new Intent("com.android.systemui.action.DISABLE_PLUGIN");
        StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("package://");
        m3.append(componentName.flattenToString());
        color.addAction(new Notification.Action.Builder((Icon) null, "Disable plugin", PendingIntent.getBroadcast(this.mContext, 0, intent.setData(Uri.parse(m3.toString())), 67108864)).build());
        this.mNotificationManager.notify(6, color.build());
        Log.w("PluginInstanceManager", "Plugin has invalid interface version " + invalidVersionException.getActualVersion() + ", expected " + invalidVersionException.getExpectedVersion());
    }
}
