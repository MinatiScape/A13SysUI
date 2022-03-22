package androidx.mediarouter.media;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Handler;
import android.util.Log;
import androidx.mediarouter.media.MediaRouter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class RegisteredMediaRouteProviderWatcher {
    public final Callback mCallback;
    public final Context mContext;
    public final PackageManager mPackageManager;
    public boolean mRunning;
    public final ArrayList<RegisteredMediaRouteProvider> mProviders = new ArrayList<>();
    public final AnonymousClass1 mScanPackagesReceiver = new BroadcastReceiver() { // from class: androidx.mediarouter.media.RegisteredMediaRouteProviderWatcher.1
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            RegisteredMediaRouteProviderWatcher.this.scanPackages();
        }
    };
    public final AnonymousClass2 mScanPackagesRunnable = new Runnable() { // from class: androidx.mediarouter.media.RegisteredMediaRouteProviderWatcher.2
        @Override // java.lang.Runnable
        public final void run() {
            RegisteredMediaRouteProviderWatcher.this.scanPackages();
        }
    };
    public final Handler mHandler = new Handler();

    /* loaded from: classes.dex */
    public interface Callback {
    }

    public final void scanPackages() {
        boolean z;
        int i;
        boolean z2;
        boolean z3;
        if (this.mRunning) {
            new ArrayList();
            Intent intent = new Intent("android.media.MediaRoute2ProviderService");
            ArrayList arrayList = new ArrayList();
            for (ResolveInfo resolveInfo : this.mPackageManager.queryIntentServices(intent, 0)) {
                arrayList.add(resolveInfo.serviceInfo);
            }
            Iterator<ResolveInfo> it = this.mPackageManager.queryIntentServices(new Intent("android.media.MediaRouteProviderService"), 0).iterator();
            int i2 = 0;
            while (true) {
                boolean z4 = true;
                if (!it.hasNext()) {
                    break;
                }
                ServiceInfo serviceInfo = it.next().serviceInfo;
                if (serviceInfo != null) {
                    if (MediaRouter.sGlobal == null) {
                        z = false;
                    } else {
                        MediaRouter.GlobalMediaRouter globalRouter = MediaRouter.getGlobalRouter();
                        Objects.requireNonNull(globalRouter);
                        z = globalRouter.mTransferReceiverDeclared;
                    }
                    if (z) {
                        if (!arrayList.isEmpty()) {
                            Iterator it2 = arrayList.iterator();
                            while (it2.hasNext()) {
                                ServiceInfo serviceInfo2 = (ServiceInfo) it2.next();
                                if (serviceInfo.packageName.equals(serviceInfo2.packageName) && serviceInfo.name.equals(serviceInfo2.name)) {
                                    z3 = true;
                                    break;
                                }
                            }
                        }
                        z3 = false;
                        if (z3) {
                        }
                    }
                    String str = serviceInfo.packageName;
                    String str2 = serviceInfo.name;
                    int size = this.mProviders.size();
                    int i3 = 0;
                    while (true) {
                        if (i3 >= size) {
                            i3 = -1;
                            break;
                        }
                        RegisteredMediaRouteProvider registeredMediaRouteProvider = this.mProviders.get(i3);
                        Objects.requireNonNull(registeredMediaRouteProvider);
                        if (!registeredMediaRouteProvider.mComponentName.getPackageName().equals(str) || !registeredMediaRouteProvider.mComponentName.getClassName().equals(str2)) {
                            z2 = false;
                        } else {
                            z2 = true;
                        }
                        if (z2) {
                            break;
                        }
                        i3++;
                    }
                    if (i3 < 0) {
                        RegisteredMediaRouteProvider registeredMediaRouteProvider2 = new RegisteredMediaRouteProvider(this.mContext, new ComponentName(serviceInfo.packageName, serviceInfo.name));
                        registeredMediaRouteProvider2.mControllerCallback = new RegisteredMediaRouteProviderWatcher$$ExternalSyntheticLambda0(this, registeredMediaRouteProvider2);
                        registeredMediaRouteProvider2.start();
                        i = i2 + 1;
                        this.mProviders.add(i2, registeredMediaRouteProvider2);
                        ((MediaRouter.GlobalMediaRouter) this.mCallback).addProvider(registeredMediaRouteProvider2);
                    } else if (i3 >= i2) {
                        RegisteredMediaRouteProvider registeredMediaRouteProvider3 = this.mProviders.get(i3);
                        registeredMediaRouteProvider3.start();
                        if (registeredMediaRouteProvider3.mActiveConnection == null) {
                            if (!registeredMediaRouteProvider3.mStarted || (registeredMediaRouteProvider3.mDiscoveryRequest == null && registeredMediaRouteProvider3.mControllerConnections.isEmpty())) {
                                z4 = false;
                            }
                            if (z4) {
                                registeredMediaRouteProvider3.unbind();
                                registeredMediaRouteProvider3.bind();
                            }
                        }
                        i = i2 + 1;
                        Collections.swap(this.mProviders, i3, i2);
                    }
                    i2 = i;
                }
            }
            if (i2 < this.mProviders.size()) {
                for (int size2 = this.mProviders.size() - 1; size2 >= i2; size2--) {
                    RegisteredMediaRouteProvider registeredMediaRouteProvider4 = this.mProviders.get(size2);
                    MediaRouter.GlobalMediaRouter globalMediaRouter = (MediaRouter.GlobalMediaRouter) this.mCallback;
                    Objects.requireNonNull(globalMediaRouter);
                    MediaRouter.ProviderInfo findProviderInfo = globalMediaRouter.findProviderInfo(registeredMediaRouteProvider4);
                    if (findProviderInfo != null) {
                        Objects.requireNonNull(registeredMediaRouteProvider4);
                        MediaRouter.checkCallingThread();
                        registeredMediaRouteProvider4.mCallback = null;
                        registeredMediaRouteProvider4.setDiscoveryRequest(null);
                        globalMediaRouter.updateProviderContents(findProviderInfo, null);
                        if (MediaRouter.DEBUG) {
                            Log.d("MediaRouter", "Provider removed: " + findProviderInfo);
                        }
                        globalMediaRouter.mCallbackHandler.post(514, findProviderInfo);
                        globalMediaRouter.mProviders.remove(findProviderInfo);
                    }
                    this.mProviders.remove(registeredMediaRouteProvider4);
                    Objects.requireNonNull(registeredMediaRouteProvider4);
                    registeredMediaRouteProvider4.mControllerCallback = null;
                    if (registeredMediaRouteProvider4.mStarted) {
                        if (RegisteredMediaRouteProvider.DEBUG) {
                            Log.d("MediaRouteProviderProxy", registeredMediaRouteProvider4 + ": Stopping");
                        }
                        registeredMediaRouteProvider4.mStarted = false;
                        registeredMediaRouteProvider4.updateBinding();
                    }
                }
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [androidx.mediarouter.media.RegisteredMediaRouteProviderWatcher$1] */
    /* JADX WARN: Type inference failed for: r0v2, types: [androidx.mediarouter.media.RegisteredMediaRouteProviderWatcher$2] */
    public RegisteredMediaRouteProviderWatcher(Context context, MediaRouter.GlobalMediaRouter globalMediaRouter) {
        this.mContext = context;
        this.mCallback = globalMediaRouter;
        this.mPackageManager = context.getPackageManager();
    }
}
