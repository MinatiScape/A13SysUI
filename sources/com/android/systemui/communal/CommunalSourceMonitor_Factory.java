package com.android.systemui.communal;

import android.content.Context;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.systemui.ActivityStarterDelegate;
import com.android.systemui.dagger.ContextComponentHelper;
import com.android.systemui.media.LocalMediaManagerFactory;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.PluginDependencyProvider;
import com.android.systemui.recents.RecentsImplementation;
import com.android.systemui.util.condition.Monitor;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class CommunalSourceMonitor_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider communalConditionsMonitorProvider;
    public final Provider executorProvider;

    public /* synthetic */ CommunalSourceMonitor_Factory(Provider provider, Provider provider2, int i) {
        this.$r8$classId = i;
        this.executorProvider = provider;
        this.communalConditionsMonitorProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new CommunalSourceMonitor((Executor) this.executorProvider.mo144get(), (Monitor) this.communalConditionsMonitorProvider.mo144get());
            case 1:
                ActivityStarterDelegate activityStarterDelegate = (ActivityStarterDelegate) this.executorProvider.mo144get();
                ((PluginDependencyProvider) this.communalConditionsMonitorProvider.mo144get()).allowPluginDependency(ActivityStarter.class, activityStarterDelegate);
                Objects.requireNonNull(activityStarterDelegate, "Cannot return null from a non-@Nullable @Provides method");
                return activityStarterDelegate;
            case 2:
                return new LocalMediaManagerFactory((Context) this.executorProvider.mo144get(), (LocalBluetoothManager) this.communalConditionsMonitorProvider.mo144get());
            default:
                Context context = (Context) this.executorProvider.mo144get();
                ContextComponentHelper contextComponentHelper = (ContextComponentHelper) this.communalConditionsMonitorProvider.mo144get();
                String string = context.getString(2131952144);
                if (string == null || string.length() == 0) {
                    throw new RuntimeException("No recents component configured", null);
                }
                RecentsImplementation resolveRecents = contextComponentHelper.resolveRecents(string);
                if (resolveRecents == null) {
                    try {
                        try {
                            resolveRecents = (RecentsImplementation) context.getClassLoader().loadClass(string).newInstance();
                        } catch (Throwable th) {
                            throw new RuntimeException(SupportMenuInflater$$ExternalSyntheticOutline0.m("Error creating recents component: ", string), th);
                        }
                    } catch (Throwable th2) {
                        throw new RuntimeException(SupportMenuInflater$$ExternalSyntheticOutline0.m("Error loading recents component: ", string), th2);
                    }
                }
                Objects.requireNonNull(resolveRecents, "Cannot return null from a non-@Nullable @Provides method");
                return resolveRecents;
        }
    }
}
