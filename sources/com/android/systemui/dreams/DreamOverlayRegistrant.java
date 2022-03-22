package com.android.systemui.dreams;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.service.dreams.IDreamManager;
import android.util.Log;
import com.android.systemui.CoreStartable;
/* loaded from: classes.dex */
public class DreamOverlayRegistrant extends CoreStartable {
    public static final boolean DEBUG = Log.isLoggable("DreamOverlayRegistrant", 3);
    public final Resources mResources;
    public boolean mCurrentRegisteredState = false;
    public final AnonymousClass1 mReceiver = new BroadcastReceiver() { // from class: com.android.systemui.dreams.DreamOverlayRegistrant.1
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            if (DreamOverlayRegistrant.DEBUG) {
                Log.d("DreamOverlayRegistrant", "package changed receiver - onReceive");
            }
            DreamOverlayRegistrant.this.registerOverlayService();
        }
    };
    public final IDreamManager mDreamManager = IDreamManager.Stub.asInterface(ServiceManager.getService("dreams"));
    public final ComponentName mOverlayServiceComponent = new ComponentName(this.mContext, DreamOverlayService.class);

    public final void registerOverlayService() {
        ComponentName componentName;
        String str;
        int i;
        PackageManager packageManager = this.mContext.getPackageManager();
        int componentEnabledSetting = packageManager.getComponentEnabledSetting(this.mOverlayServiceComponent);
        boolean z = false;
        if (componentEnabledSetting != 3) {
            if (this.mResources.getBoolean(2131034125)) {
                i = 1;
            } else {
                i = 2;
            }
            if (i != componentEnabledSetting) {
                packageManager.setComponentEnabledSetting(this.mOverlayServiceComponent, i, 0);
            }
        }
        if (packageManager.getComponentEnabledSetting(this.mOverlayServiceComponent) == 1) {
            z = true;
        }
        if (this.mCurrentRegisteredState != z) {
            this.mCurrentRegisteredState = z;
            try {
                if (DEBUG) {
                    if (z) {
                        str = "registering dream overlay service:" + this.mOverlayServiceComponent;
                    } else {
                        str = "clearing dream overlay service";
                    }
                    Log.d("DreamOverlayRegistrant", str);
                }
                IDreamManager iDreamManager = this.mDreamManager;
                if (this.mCurrentRegisteredState) {
                    componentName = this.mOverlayServiceComponent;
                } else {
                    componentName = null;
                }
                iDreamManager.registerDreamOverlayService(componentName);
            } catch (RemoteException e) {
                Log.e("DreamOverlayRegistrant", "could not register dream overlay service:" + e);
            }
        }
    }

    @Override // com.android.systemui.CoreStartable
    public final void start() {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addDataScheme("package");
        intentFilter.addDataSchemeSpecificPart(this.mOverlayServiceComponent.getPackageName(), 0);
        this.mContext.registerReceiver(this.mReceiver, intentFilter);
        registerOverlayService();
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [com.android.systemui.dreams.DreamOverlayRegistrant$1] */
    public DreamOverlayRegistrant(Context context, Resources resources) {
        super(context);
        this.mResources = resources;
    }
}
