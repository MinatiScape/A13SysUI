package com.android.systemui.shared.plugins;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Build;
import android.os.SystemProperties;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.widget.Toast;
import com.android.systemui.ScreenDecorations$$ExternalSyntheticLambda3;
import com.android.systemui.plugins.Plugin;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.shared.plugins.PluginActionManager;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda22;
import com.android.wm.shell.back.BackAnimationController$IBackAnimationImpl$$ExternalSyntheticLambda0;
import com.android.wm.shell.pip.phone.PipController$$ExternalSyntheticLambda3;
import java.lang.Thread;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class PluginManagerImpl extends BroadcastReceiver implements PluginManager {
    public final PluginActionManager.Factory mActionManagerFactory;
    public final Context mContext;
    public final boolean mIsDebuggable;
    public boolean mListening;
    public final PluginEnabler mPluginEnabler;
    public final PluginPrefs mPluginPrefs;
    public final ArraySet<String> mPrivilegedPlugins;
    public final ArrayMap<PluginListener<?>, PluginActionManager<?>> mPluginMap = new ArrayMap<>();
    public final ArrayMap mClassLoaders = new ArrayMap();

    /* loaded from: classes.dex */
    public static class ClassLoaderFilter extends ClassLoader {
        public final ClassLoader mBase;
        public final String mPackage = "com.android.systemui.plugin";

        @Override // java.lang.ClassLoader
        public final Class<?> loadClass(String str, boolean z) throws ClassNotFoundException {
            if (!str.startsWith(this.mPackage)) {
                super.loadClass(str, z);
            }
            return this.mBase.loadClass(str);
        }

        public ClassLoaderFilter(ClassLoader classLoader) {
            super(ClassLoader.getSystemClassLoader());
            this.mBase = classLoader;
        }
    }

    /* loaded from: classes.dex */
    public class PluginExceptionHandler implements Thread.UncaughtExceptionHandler {
        public final Optional<Thread.UncaughtExceptionHandler> mExceptionHandlerOptional;

        public final boolean checkStack(Throwable th) {
            StackTraceElement[] stackTrace;
            boolean z;
            if (th == null) {
                return false;
            }
            synchronized (this) {
                z = false;
                for (StackTraceElement stackTraceElement : th.getStackTrace()) {
                    for (PluginActionManager<?> pluginActionManager : PluginManagerImpl.this.mPluginMap.values()) {
                        z |= pluginActionManager.checkAndDisable(stackTraceElement.getClassName());
                    }
                }
            }
            return checkStack(th.getCause()) | z;
        }

        public PluginExceptionHandler(Optional optional) {
            this.mExceptionHandlerOptional = optional;
        }

        @Override // java.lang.Thread.UncaughtExceptionHandler
        public final void uncaughtException(final Thread thread, final Throwable th) {
            if (SystemProperties.getBoolean("plugin.debugging", false)) {
                this.mExceptionHandlerOptional.ifPresent(new BackAnimationController$IBackAnimationImpl$$ExternalSyntheticLambda0(thread, th, 1));
                return;
            }
            boolean checkStack = checkStack(th);
            if (!checkStack) {
                synchronized (this) {
                    for (PluginActionManager<?> pluginActionManager : PluginManagerImpl.this.mPluginMap.values()) {
                        checkStack |= pluginActionManager.disableAll();
                    }
                }
            }
            if (checkStack) {
                th = new CrashWhilePluginActiveException(th);
            }
            this.mExceptionHandlerOptional.ifPresent(new Consumer() { // from class: com.android.systemui.shared.plugins.PluginManagerImpl$PluginExceptionHandler$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((Thread.UncaughtExceptionHandler) obj).uncaughtException(thread, th);
                }
            });
        }
    }

    @Override // com.android.systemui.shared.plugins.PluginManager
    public final <T extends Plugin> void addPluginListener(PluginListener<T> pluginListener, Class<T> cls) {
        addPluginListener((PluginListener) pluginListener, (Class) cls, false);
    }

    @Override // com.android.systemui.shared.plugins.PluginManager
    public final <T> boolean dependsOn(Plugin plugin, Class<T> cls) {
        synchronized (this) {
            for (int i = 0; i < this.mPluginMap.size(); i++) {
                if (this.mPluginMap.valueAt(i).dependsOn(plugin, cls)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override // com.android.systemui.shared.plugins.PluginManager
    public final void removePluginListener(PluginListener<?> pluginListener) {
        synchronized (this) {
            if (this.mPluginMap.containsKey(pluginListener)) {
                this.mPluginMap.remove(pluginListener).destroy();
                if (this.mPluginMap.size() == 0 && this.mListening) {
                    this.mListening = false;
                    this.mContext.unregisterReceiver(this);
                }
            }
        }
    }

    @Override // com.android.systemui.shared.plugins.PluginManager
    public final <T extends Plugin> void addPluginListener(PluginListener<T> pluginListener, Class<T> cls, boolean z) {
        addPluginListener(PluginManager.Helper.getAction(cls), pluginListener, cls, z);
    }

    @Override // com.android.systemui.shared.plugins.PluginManager
    public final String[] getPrivilegedPlugins() {
        return (String[]) this.mPrivilegedPlugins.toArray(new String[0]);
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        int disableReason;
        boolean z = true;
        if ("android.intent.action.USER_UNLOCKED".equals(intent.getAction())) {
            synchronized (this) {
                for (PluginActionManager<?> pluginActionManager : this.mPluginMap.values()) {
                    Objects.requireNonNull(pluginActionManager);
                    pluginActionManager.mBgExecutor.execute(new ScreenDecorations$$ExternalSyntheticLambda3(pluginActionManager, 1));
                }
            }
            return;
        }
        boolean z2 = false;
        if ("com.android.systemui.action.DISABLE_PLUGIN".equals(intent.getAction())) {
            ComponentName unflattenFromString = ComponentName.unflattenFromString(intent.getData().toString().substring(10));
            Iterator<String> it = this.mPrivilegedPlugins.iterator();
            while (true) {
                if (!it.hasNext()) {
                    z = false;
                    break;
                }
                String next = it.next();
                ComponentName unflattenFromString2 = ComponentName.unflattenFromString(next);
                if (unflattenFromString2 != null) {
                    if (unflattenFromString2.equals(unflattenFromString)) {
                        break;
                    }
                } else if (next.equals(unflattenFromString.getPackageName())) {
                    break;
                }
            }
            if (!z) {
                this.mPluginEnabler.setDisabled(unflattenFromString, 2);
                ((NotificationManager) this.mContext.getSystemService(NotificationManager.class)).cancel(unflattenFromString.getClassName(), 6);
                return;
            }
            return;
        }
        String encodedSchemeSpecificPart = intent.getData().getEncodedSchemeSpecificPart();
        ComponentName unflattenFromString3 = ComponentName.unflattenFromString(encodedSchemeSpecificPart);
        if (this.mClassLoaders.remove(encodedSchemeSpecificPart) != null) {
            z2 = true;
        }
        if (z2) {
            if (Build.IS_ENG) {
                Toast.makeText(this.mContext, "Reloading " + encodedSchemeSpecificPart, 1).show();
            } else {
                Log.v("PluginManagerImpl", "Reloading " + encodedSchemeSpecificPart);
            }
        }
        if ("android.intent.action.PACKAGE_REPLACED".equals(intent.getAction()) && unflattenFromString3 != null && ((disableReason = this.mPluginEnabler.getDisableReason(unflattenFromString3)) == 3 || disableReason == 4 || disableReason == 2)) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Re-enabling previously disabled plugin that has been updated: ");
            m.append(unflattenFromString3.flattenToShortString());
            Log.i("PluginManagerImpl", m.toString());
            this.mPluginEnabler.setEnabled(unflattenFromString3);
        }
        synchronized (this) {
            if (!"android.intent.action.PACKAGE_ADDED".equals(intent.getAction()) && !"android.intent.action.PACKAGE_CHANGED".equals(intent.getAction()) && !"android.intent.action.PACKAGE_REPLACED".equals(intent.getAction())) {
                for (PluginActionManager<?> pluginActionManager2 : this.mPluginMap.values()) {
                    Objects.requireNonNull(pluginActionManager2);
                    pluginActionManager2.mBgExecutor.execute(new StatusBar$$ExternalSyntheticLambda22(pluginActionManager2, encodedSchemeSpecificPart, 2));
                }
            }
            for (PluginActionManager<?> pluginActionManager3 : this.mPluginMap.values()) {
                Objects.requireNonNull(pluginActionManager3);
                pluginActionManager3.mBgExecutor.execute(new PipController$$ExternalSyntheticLambda3(pluginActionManager3, encodedSchemeSpecificPart, 3));
            }
        }
    }

    public PluginManagerImpl(Context context, PluginActionManager.Factory factory, boolean z, Optional<Thread.UncaughtExceptionHandler> optional, PluginEnabler pluginEnabler, PluginPrefs pluginPrefs, List<String> list) {
        ArraySet<String> arraySet = new ArraySet<>();
        this.mPrivilegedPlugins = arraySet;
        this.mContext = context;
        this.mActionManagerFactory = factory;
        this.mIsDebuggable = z;
        arraySet.addAll(list);
        this.mPluginPrefs = pluginPrefs;
        this.mPluginEnabler = pluginEnabler;
        Thread.setUncaughtExceptionPreHandler(new PluginExceptionHandler(optional));
    }

    @Override // com.android.systemui.shared.plugins.PluginManager
    public final <T extends Plugin> void addPluginListener(String str, PluginListener<T> pluginListener, Class<T> cls) {
        addPluginListener(str, pluginListener, cls, false);
    }

    public final <T extends Plugin> void addPluginListener(String str, PluginListener<T> pluginListener, Class<T> cls, boolean z) {
        PluginPrefs pluginPrefs = this.mPluginPrefs;
        Objects.requireNonNull(pluginPrefs);
        synchronized (pluginPrefs) {
            if (pluginPrefs.mPluginActions.add(str)) {
                pluginPrefs.mSharedPrefs.edit().putStringSet("actions", pluginPrefs.mPluginActions).apply();
            }
        }
        PluginActionManager.Factory factory = this.mActionManagerFactory;
        boolean z2 = this.mIsDebuggable;
        Objects.requireNonNull(factory);
        PluginActionManager<?> pluginActionManager = new PluginActionManager<>(factory.mContext, factory.mPackageManager, str, pluginListener, cls, z, factory.mMainExecutor, factory.mBgExecutor, z2, factory.mNotificationManager, factory.mPluginEnabler, factory.mPrivilegedPlugins, factory.mPluginInstanceFactory);
        pluginActionManager.mBgExecutor.execute(new ScreenDecorations$$ExternalSyntheticLambda3(pluginActionManager, 1));
        synchronized (this) {
            this.mPluginMap.put(pluginListener, pluginActionManager);
        }
        if (!this.mListening) {
            this.mListening = true;
            IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
            intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
            intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
            intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
            intentFilter.addDataScheme("package");
            this.mContext.registerReceiver(this, intentFilter);
            intentFilter.addAction("com.android.systemui.action.PLUGIN_CHANGED");
            intentFilter.addAction("com.android.systemui.action.DISABLE_PLUGIN");
            intentFilter.addDataScheme("package");
            this.mContext.registerReceiver(this, intentFilter, "com.android.systemui.permission.PLUGIN", null, 2);
            this.mContext.registerReceiver(this, new IntentFilter("android.intent.action.USER_UNLOCKED"));
        }
    }

    /* loaded from: classes.dex */
    public static class CrashWhilePluginActiveException extends RuntimeException {
        public CrashWhilePluginActiveException(Throwable th) {
            super(th);
        }
    }
}
