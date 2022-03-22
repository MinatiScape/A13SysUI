package com.android.systemui;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.Log;
import androidx.core.app.AppComponentFactory;
import com.android.systemui.dagger.ContextComponentHelper;
import com.android.systemui.dagger.SysUIComponent;
import com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda9;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
/* loaded from: classes.dex */
public class SystemUIAppComponentFactory extends AppComponentFactory {
    public static final /* synthetic */ int $r8$clinit = 0;
    public ContextComponentHelper mComponentHelper;

    /* loaded from: classes.dex */
    public interface ContextAvailableCallback {
        void onContextAvailable(Context context);
    }

    /* loaded from: classes.dex */
    public interface ContextInitializer {
        void setContextAvailableCallback(ContextAvailableCallback contextAvailableCallback);
    }

    @Override // androidx.core.app.AppComponentFactory
    public final Activity instantiateActivityCompat(ClassLoader classLoader, String str, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        if (this.mComponentHelper == null) {
            SystemUIFactory systemUIFactory = SystemUIFactory.mFactory;
            Objects.requireNonNull(systemUIFactory);
            systemUIFactory.mSysUIComponent.inject(this);
        }
        Activity resolveActivity = this.mComponentHelper.resolveActivity(str);
        if (resolveActivity != null) {
            return resolveActivity;
        }
        return super.instantiateActivityCompat(classLoader, str, intent);
    }

    @Override // androidx.core.app.AppComponentFactory
    public final BroadcastReceiver instantiateReceiverCompat(ClassLoader classLoader, String str, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        if (this.mComponentHelper == null) {
            SystemUIFactory systemUIFactory = SystemUIFactory.mFactory;
            Objects.requireNonNull(systemUIFactory);
            systemUIFactory.mSysUIComponent.inject(this);
        }
        BroadcastReceiver resolveBroadcastReceiver = this.mComponentHelper.resolveBroadcastReceiver(str);
        if (resolveBroadcastReceiver != null) {
            return resolveBroadcastReceiver;
        }
        return super.instantiateReceiverCompat(classLoader, str, intent);
    }

    @Override // androidx.core.app.AppComponentFactory
    public final Service instantiateServiceCompat(ClassLoader classLoader, String str, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        if (this.mComponentHelper == null) {
            SystemUIFactory systemUIFactory = SystemUIFactory.mFactory;
            Objects.requireNonNull(systemUIFactory);
            systemUIFactory.mSysUIComponent.inject(this);
        }
        Service resolveService = this.mComponentHelper.resolveService(str);
        if (resolveService != null) {
            return resolveService;
        }
        return super.instantiateServiceCompat(classLoader, str, intent);
    }

    @Override // androidx.core.app.AppComponentFactory
    public final Application instantiateApplicationCompat(ClassLoader classLoader, String str) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Application instantiateApplicationCompat = super.instantiateApplicationCompat(classLoader, str);
        if (instantiateApplicationCompat instanceof ContextInitializer) {
            ((ContextInitializer) instantiateApplicationCompat).setContextAvailableCallback(new VolumeDialogImpl$$ExternalSyntheticLambda9(this));
        }
        return instantiateApplicationCompat;
    }

    @Override // androidx.core.app.AppComponentFactory
    public final ContentProvider instantiateProviderCompat(ClassLoader classLoader, String str) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        final ContentProvider instantiateProviderCompat = super.instantiateProviderCompat(classLoader, str);
        if (instantiateProviderCompat instanceof ContextInitializer) {
            ((ContextInitializer) instantiateProviderCompat).setContextAvailableCallback(new ContextAvailableCallback() { // from class: com.android.systemui.SystemUIAppComponentFactory$$ExternalSyntheticLambda0
                @Override // com.android.systemui.SystemUIAppComponentFactory.ContextAvailableCallback
                public final void onContextAvailable(Context context) {
                    ContentProvider contentProvider = instantiateProviderCompat;
                    int i = SystemUIAppComponentFactory.$r8$clinit;
                    SystemUIFactory.createFromConfig(context, false);
                    SystemUIFactory systemUIFactory = SystemUIFactory.mFactory;
                    Objects.requireNonNull(systemUIFactory);
                    SysUIComponent sysUIComponent = systemUIFactory.mSysUIComponent;
                    try {
                        sysUIComponent.getClass().getMethod("inject", contentProvider.getClass()).invoke(sysUIComponent, contentProvider);
                    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("No injector for class: ");
                        m.append(contentProvider.getClass());
                        Log.w("AppComponentFactory", m.toString(), e);
                    }
                }
            });
        }
        return instantiateProviderCompat;
    }
}
