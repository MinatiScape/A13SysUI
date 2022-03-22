package com.android.wm.shell.transition;

import android.content.Context;
import android.content.res.Configuration;
import android.window.WindowContainerTransaction;
import com.android.systemui.fragments.FragmentHostManager;
import com.android.systemui.fragments.FragmentService;
import com.android.systemui.plugins.PluginFragment;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.shared.plugins.PluginActionManager;
import com.android.systemui.shared.plugins.PluginInstance;
import com.android.systemui.statusbar.AlertingNotificationManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.wm.shell.transition.Transitions;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class OneShotRemoteHandler$2$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ OneShotRemoteHandler$2$$ExternalSyntheticLambda0(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                ((Transitions.TransitionFinishCallback) this.f$0).onTransitionFinished((WindowContainerTransaction) this.f$1);
                return;
            case 1:
                FragmentService.FragmentHostState fragmentHostState = (FragmentService.FragmentHostState) this.f$0;
                Configuration configuration = (Configuration) this.f$1;
                Objects.requireNonNull(fragmentHostState);
                FragmentHostManager fragmentHostManager = fragmentHostState.mFragmentHostManager;
                Objects.requireNonNull(fragmentHostManager);
                if (fragmentHostManager.mConfigChanges.applyNewConfig(fragmentHostManager.mContext.getResources())) {
                    fragmentHostManager.reloadFragments();
                    return;
                } else {
                    fragmentHostManager.mFragments.dispatchConfigurationChanged(configuration);
                    return;
                }
            case 2:
                PluginActionManager pluginActionManager = (PluginActionManager) this.f$0;
                PluginInstance pluginInstance = (PluginInstance) this.f$1;
                Objects.requireNonNull(pluginActionManager);
                pluginActionManager.mContext.getSharedPreferences("plugin_prefs", 0).edit().putBoolean("plugins", true).apply();
                Context context = pluginActionManager.mContext;
                PluginListener<T> pluginListener = pluginActionManager.mListener;
                Objects.requireNonNull(pluginInstance);
                T t = pluginInstance.mPlugin;
                if (!(t instanceof PluginFragment)) {
                    t.onCreate(context, pluginInstance.mPluginContext);
                }
                pluginListener.onPluginConnected(pluginInstance.mPlugin, pluginInstance.mPluginContext);
                return;
            default:
                AlertingNotificationManager.AlertEntry alertEntry = (AlertingNotificationManager.AlertEntry) this.f$0;
                NotificationEntry notificationEntry = (NotificationEntry) this.f$1;
                Objects.requireNonNull(alertEntry);
                AlertingNotificationManager alertingNotificationManager = AlertingNotificationManager.this;
                Objects.requireNonNull(notificationEntry);
                alertingNotificationManager.removeAlertEntry(notificationEntry.mKey);
                return;
        }
    }
}
