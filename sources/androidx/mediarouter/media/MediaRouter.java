package androidx.mediarouter.media;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.media.MediaRoute2Info;
import android.media.MediaRouter2;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import androidx.collection.ArrayMap;
import androidx.concurrent.futures.AbstractResolvableFuture$$ExternalSyntheticOutline0;
import androidx.constraintlayout.motion.widget.MotionLayout$$ExternalSyntheticOutline0;
import androidx.core.util.Pair;
import androidx.mediarouter.media.MediaRoute2Provider;
import androidx.mediarouter.media.MediaRouteProvider;
import androidx.mediarouter.media.MediaRouteSelector;
import androidx.mediarouter.media.RegisteredMediaRouteProviderWatcher;
import androidx.mediarouter.media.SystemMediaRouteProvider;
import com.android.settingslib.wifi.WifiTracker;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda15;
import com.google.common.util.concurrent.ListenableFuture;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MediaRouter {
    public static final boolean DEBUG = Log.isLoggable("MediaRouter", 3);
    public static GlobalMediaRouter sGlobal;
    public final ArrayList<CallbackRecord> mCallbackRecords = new ArrayList<>();
    public final Context mContext;

    /* loaded from: classes.dex */
    public static abstract class Callback {
        public void onProviderAdded() {
        }

        public void onProviderChanged() {
        }

        public void onProviderRemoved() {
        }

        public void onRouteAdded() {
        }

        public abstract void onRouteChanged(RouteInfo routeInfo);

        public void onRouteRemoved() {
        }

        @Deprecated
        public void onRouteSelected(RouteInfo routeInfo) {
        }

        @Deprecated
        public void onRouteUnselected() {
        }

        public void onRouteVolumeChanged(RouteInfo routeInfo) {
        }

        public void onRouterParamsChanged(MediaRouterParams mediaRouterParams) {
        }
    }

    /* loaded from: classes.dex */
    public static abstract class ControlRequestCallback {
        public void onError(String str, Bundle bundle) {
        }

        public void onResult(Bundle bundle) {
        }
    }

    /* loaded from: classes.dex */
    public static final class GlobalMediaRouter implements SystemMediaRouteProvider.SyncCallback, RegisteredMediaRouteProviderWatcher.Callback {
        public MediaRouterActiveScanThrottlingHelper mActiveScanThrottlingHelper;
        public final Context mApplicationContext;
        public RouteInfo mBluetoothRoute;
        public int mCallbackCount;
        public RouteInfo mDefaultRoute;
        public MediaRouteDiscoveryRequest mDiscoveryRequest;
        public MediaRouteDiscoveryRequest mDiscoveryRequestForMr2Provider;
        public boolean mIsInitialized;
        public final boolean mLowRam;
        public MediaRoute2Provider mMr2Provider;
        public RegisteredMediaRouteProviderWatcher mRegisteredProviderWatcher;
        public RouteInfo mRequestedRoute;
        public MediaRouteProvider.DynamicGroupRouteController mRequestedRouteController;
        public RouteInfo mSelectedRoute;
        public MediaRouteProvider.RouteController mSelectedRouteController;
        public SystemMediaRouteProvider.Api24Impl mSystemProvider;
        public PrepareTransferNotifier mTransferNotifier;
        public boolean mTransferReceiverDeclared;
        public final ArrayList<WeakReference<MediaRouter>> mRouters = new ArrayList<>();
        public final ArrayList<RouteInfo> mRoutes = new ArrayList<>();
        public final HashMap mUniqueIdMap = new HashMap();
        public final ArrayList<ProviderInfo> mProviders = new ArrayList<>();
        public final ArrayList<RemoteControlClientRecord> mRemoteControlClients = new ArrayList<>();
        public final RemoteControlClientCompat$PlaybackInfo mPlaybackInfo = new Object() { // from class: androidx.mediarouter.media.RemoteControlClientCompat$PlaybackInfo
        };
        public final ProviderCallback mProviderCallback = new ProviderCallback();
        public final CallbackHandler mCallbackHandler = new CallbackHandler();
        public final HashMap mRouteControllerMap = new HashMap();
        public AnonymousClass3 mDynamicRoutesListener = new AnonymousClass3();

        /* renamed from: androidx.mediarouter.media.MediaRouter$GlobalMediaRouter$3  reason: invalid class name */
        /* loaded from: classes.dex */
        public class AnonymousClass3 implements MediaRouteProvider.DynamicGroupRouteController.OnDynamicRoutesChangedListener {
            public AnonymousClass3() {
            }

            public final void onRoutesChanged(MediaRouteProvider.DynamicGroupRouteController dynamicGroupRouteController, MediaRouteDescriptor mediaRouteDescriptor, Collection<MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor> collection) {
                GlobalMediaRouter globalMediaRouter = GlobalMediaRouter.this;
                if (dynamicGroupRouteController == globalMediaRouter.mRequestedRouteController && mediaRouteDescriptor != null) {
                    RouteInfo routeInfo = globalMediaRouter.mRequestedRoute;
                    Objects.requireNonNull(routeInfo);
                    ProviderInfo providerInfo = routeInfo.mProvider;
                    String id = mediaRouteDescriptor.getId();
                    RouteInfo routeInfo2 = new RouteInfo(providerInfo, id, GlobalMediaRouter.this.assignRouteUniqueId(providerInfo, id));
                    routeInfo2.maybeUpdateDescriptor(mediaRouteDescriptor);
                    GlobalMediaRouter globalMediaRouter2 = GlobalMediaRouter.this;
                    if (globalMediaRouter2.mSelectedRoute != routeInfo2) {
                        globalMediaRouter2.notifyTransfer(globalMediaRouter2, routeInfo2, globalMediaRouter2.mRequestedRouteController, 3, globalMediaRouter2.mRequestedRoute, collection);
                        GlobalMediaRouter globalMediaRouter3 = GlobalMediaRouter.this;
                        globalMediaRouter3.mRequestedRoute = null;
                        globalMediaRouter3.mRequestedRouteController = null;
                    }
                } else if (dynamicGroupRouteController == globalMediaRouter.mSelectedRouteController) {
                    if (mediaRouteDescriptor != null) {
                        globalMediaRouter.updateRouteDescriptorAndNotify(globalMediaRouter.mSelectedRoute, mediaRouteDescriptor);
                    }
                    GlobalMediaRouter.this.mSelectedRoute.updateDynamicDescriptors(collection);
                }
            }
        }

        /* loaded from: classes.dex */
        public final class CallbackHandler extends Handler {
            public final ArrayList<CallbackRecord> mTempCallbackRecords = new ArrayList<>();
            public final ArrayList mDynamicGroupRoutes = new ArrayList();

            public CallbackHandler() {
            }

            public static void invokeCallback(CallbackRecord callbackRecord, int i, Object obj) {
                RouteInfo routeInfo;
                boolean z;
                MediaRouter mediaRouter = callbackRecord.mRouter;
                Callback callback = callbackRecord.mCallback;
                int i2 = 65280 & i;
                if (i2 == 256) {
                    if (i == 264 || i == 262) {
                        routeInfo = (RouteInfo) ((Pair) obj).second;
                    } else {
                        routeInfo = (RouteInfo) obj;
                    }
                    if (i == 264 || i == 262) {
                        RouteInfo routeInfo2 = (RouteInfo) ((Pair) obj).first;
                    }
                    if (routeInfo != null) {
                        if ((callbackRecord.mFlags & 2) != 0 || routeInfo.matchesSelector(callbackRecord.mSelector)) {
                            z = true;
                        } else {
                            MediaRouter.getGlobalRouter();
                            z = false;
                        }
                        if (z) {
                            switch (i) {
                                case 257:
                                    callback.onRouteAdded();
                                    return;
                                case 258:
                                    callback.onRouteRemoved();
                                    return;
                                case 259:
                                    callback.onRouteChanged(routeInfo);
                                    return;
                                case 260:
                                    callback.onRouteVolumeChanged(routeInfo);
                                    return;
                                case 261:
                                    Objects.requireNonNull(callback);
                                    return;
                                case 262:
                                    Objects.requireNonNull(callback);
                                    callback.onRouteSelected(routeInfo);
                                    return;
                                case 263:
                                    Objects.requireNonNull(callback);
                                    callback.onRouteUnselected();
                                    return;
                                case 264:
                                    Objects.requireNonNull(callback);
                                    callback.onRouteSelected(routeInfo);
                                    return;
                                default:
                                    return;
                            }
                        }
                    }
                } else if (i2 == 512) {
                    ProviderInfo providerInfo = (ProviderInfo) obj;
                    switch (i) {
                        case 513:
                            callback.onProviderAdded();
                            return;
                        case 514:
                            callback.onProviderRemoved();
                            return;
                        case 515:
                            callback.onProviderChanged();
                            return;
                        default:
                            return;
                    }
                } else if (i2 == 768 && i == 769) {
                    callback.onRouterParamsChanged((MediaRouterParams) obj);
                }
            }

            @Override // android.os.Handler
            public final void handleMessage(Message message) {
                int findUserRouteRecord;
                int i = message.what;
                Object obj = message.obj;
                if (i == 259) {
                    RouteInfo selectedRoute = GlobalMediaRouter.this.getSelectedRoute();
                    Objects.requireNonNull(selectedRoute);
                    String str = selectedRoute.mUniqueId;
                    RouteInfo routeInfo = (RouteInfo) obj;
                    Objects.requireNonNull(routeInfo);
                    if (str.equals(routeInfo.mUniqueId)) {
                        GlobalMediaRouter.this.updateSelectedRouteIfNeeded(true);
                    }
                }
                if (i == 262) {
                    RouteInfo routeInfo2 = (RouteInfo) ((Pair) obj).second;
                    GlobalMediaRouter.this.mSystemProvider.onSyncRouteSelected(routeInfo2);
                    if (GlobalMediaRouter.this.mDefaultRoute != null && routeInfo2.isDefaultOrBluetooth()) {
                        Iterator it = this.mDynamicGroupRoutes.iterator();
                        while (it.hasNext()) {
                            GlobalMediaRouter.this.mSystemProvider.onSyncRouteRemoved((RouteInfo) it.next());
                        }
                        this.mDynamicGroupRoutes.clear();
                    }
                } else if (i != 264) {
                    switch (i) {
                        case 257:
                            GlobalMediaRouter.this.mSystemProvider.onSyncRouteAdded((RouteInfo) obj);
                            break;
                        case 258:
                            GlobalMediaRouter.this.mSystemProvider.onSyncRouteRemoved((RouteInfo) obj);
                            break;
                        case 259:
                            SystemMediaRouteProvider.Api24Impl api24Impl = GlobalMediaRouter.this.mSystemProvider;
                            RouteInfo routeInfo3 = (RouteInfo) obj;
                            Objects.requireNonNull(api24Impl);
                            if (routeInfo3.getProviderInstance() != api24Impl && (findUserRouteRecord = api24Impl.findUserRouteRecord(routeInfo3)) >= 0) {
                                api24Impl.updateUserRouteProperties(api24Impl.mUserRouteRecords.get(findUserRouteRecord));
                                break;
                            }
                            break;
                    }
                } else {
                    RouteInfo routeInfo4 = (RouteInfo) ((Pair) obj).second;
                    this.mDynamicGroupRoutes.add(routeInfo4);
                    GlobalMediaRouter.this.mSystemProvider.onSyncRouteAdded(routeInfo4);
                    GlobalMediaRouter.this.mSystemProvider.onSyncRouteSelected(routeInfo4);
                }
                try {
                    int size = GlobalMediaRouter.this.mRouters.size();
                    while (true) {
                        size--;
                        if (size >= 0) {
                            MediaRouter mediaRouter = GlobalMediaRouter.this.mRouters.get(size).get();
                            if (mediaRouter == null) {
                                GlobalMediaRouter.this.mRouters.remove(size);
                            } else {
                                this.mTempCallbackRecords.addAll(mediaRouter.mCallbackRecords);
                            }
                        } else {
                            int size2 = this.mTempCallbackRecords.size();
                            for (int i2 = 0; i2 < size2; i2++) {
                                invokeCallback(this.mTempCallbackRecords.get(i2), i, obj);
                            }
                            return;
                        }
                    }
                } finally {
                    this.mTempCallbackRecords.clear();
                }
            }

            public final void post(int i, Object obj) {
                obtainMessage(i, obj).sendToTarget();
            }
        }

        /* loaded from: classes.dex */
        public final class Mr2ProviderCallback extends MediaRoute2Provider.Callback {
            public Mr2ProviderCallback() {
            }
        }

        /* loaded from: classes.dex */
        public final class ProviderCallback extends MediaRouteProvider.Callback {
            public ProviderCallback() {
            }
        }

        /* loaded from: classes.dex */
        public final class RemoteControlClientRecord {
        }

        public final RouteInfo chooseFallbackRoute() {
            boolean z;
            Iterator<RouteInfo> it = this.mRoutes.iterator();
            while (it.hasNext()) {
                RouteInfo next = it.next();
                if (next != this.mDefaultRoute) {
                    if (next.getProviderInstance() != this.mSystemProvider || !next.supportsControlCategory("android.media.intent.category.LIVE_AUDIO") || next.supportsControlCategory("android.media.intent.category.LIVE_VIDEO")) {
                        z = false;
                    } else {
                        z = true;
                    }
                    if (z && next.isSelectable()) {
                        return next;
                    }
                }
            }
            return this.mDefaultRoute;
        }

        /* JADX WARN: Type inference failed for: r3v5, types: [androidx.mediarouter.media.MediaRouter$GlobalMediaRouter$2] */
        @SuppressLint({"NewApi", "SyntheticAccessor"})
        public final void ensureInitialized() {
            if (!this.mIsInitialized) {
                this.mIsInitialized = true;
                Context context = this.mApplicationContext;
                int i = MediaTransferReceiver.$r8$clinit;
                Intent intent = new Intent(context, MediaTransferReceiver.class);
                intent.setPackage(context.getPackageName());
                boolean z = false;
                if (context.getPackageManager().queryBroadcastReceivers(intent, 0).size() > 0) {
                    z = true;
                }
                this.mTransferReceiverDeclared = z;
                if (z) {
                    this.mMr2Provider = new MediaRoute2Provider(this.mApplicationContext, new Mr2ProviderCallback());
                } else {
                    this.mMr2Provider = null;
                }
                this.mSystemProvider = new SystemMediaRouteProvider.Api24Impl(this.mApplicationContext, this);
                this.mActiveScanThrottlingHelper = new MediaRouterActiveScanThrottlingHelper(new Runnable() { // from class: androidx.mediarouter.media.MediaRouter.GlobalMediaRouter.2
                    @Override // java.lang.Runnable
                    public final void run() {
                        GlobalMediaRouter.this.updateDiscoveryRequest();
                    }
                });
                addProvider(this.mSystemProvider);
                MediaRoute2Provider mediaRoute2Provider = this.mMr2Provider;
                if (mediaRoute2Provider != null) {
                    addProvider(mediaRoute2Provider);
                }
                RegisteredMediaRouteProviderWatcher registeredMediaRouteProviderWatcher = new RegisteredMediaRouteProviderWatcher(this.mApplicationContext, this);
                this.mRegisteredProviderWatcher = registeredMediaRouteProviderWatcher;
                if (!registeredMediaRouteProviderWatcher.mRunning) {
                    registeredMediaRouteProviderWatcher.mRunning = true;
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
                    intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
                    intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
                    intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
                    intentFilter.addAction("android.intent.action.PACKAGE_RESTARTED");
                    intentFilter.addDataScheme("package");
                    registeredMediaRouteProviderWatcher.mContext.registerReceiver(registeredMediaRouteProviderWatcher.mScanPackagesReceiver, intentFilter, null, registeredMediaRouteProviderWatcher.mHandler);
                    registeredMediaRouteProviderWatcher.mHandler.post(registeredMediaRouteProviderWatcher.mScanPackagesRunnable);
                }
            }
        }

        public final ProviderInfo findProviderInfo(MediaRouteProvider mediaRouteProvider) {
            int size = this.mProviders.size();
            for (int i = 0; i < size; i++) {
                if (this.mProviders.get(i).mProviderInstance == mediaRouteProvider) {
                    return this.mProviders.get(i);
                }
            }
            return null;
        }

        public final int findRouteByUniqueId(String str) {
            int size = this.mRoutes.size();
            for (int i = 0; i < size; i++) {
                if (this.mRoutes.get(i).mUniqueId.equals(str)) {
                    return i;
                }
            }
            return -1;
        }

        public final RouteInfo getSelectedRoute() {
            RouteInfo routeInfo = this.mSelectedRoute;
            if (routeInfo != null) {
                return routeInfo;
            }
            throw new IllegalStateException("There is no currently selected route.  The media router has not yet been fully initialized.");
        }

        public final void maybeUpdateMemberRouteControllers() {
            if (this.mSelectedRoute.isGroup()) {
                List<RouteInfo> memberRoutes = this.mSelectedRoute.getMemberRoutes();
                HashSet hashSet = new HashSet();
                for (RouteInfo routeInfo : memberRoutes) {
                    hashSet.add(routeInfo.mUniqueId);
                }
                Iterator it = this.mRouteControllerMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    if (!hashSet.contains(entry.getKey())) {
                        MediaRouteProvider.RouteController routeController = (MediaRouteProvider.RouteController) entry.getValue();
                        routeController.onUnselect(0);
                        routeController.onRelease();
                        it.remove();
                    }
                }
                for (RouteInfo routeInfo2 : memberRoutes) {
                    if (!this.mRouteControllerMap.containsKey(routeInfo2.mUniqueId)) {
                        MediaRouteProvider.RouteController onCreateRouteController = routeInfo2.getProviderInstance().onCreateRouteController(routeInfo2.mDescriptorId, this.mSelectedRoute.mDescriptorId);
                        onCreateRouteController.onSelect();
                        this.mRouteControllerMap.put(routeInfo2.mUniqueId, onCreateRouteController);
                    }
                }
            }
        }

        public final void notifyTransfer(GlobalMediaRouter globalMediaRouter, RouteInfo routeInfo, MediaRouteProvider.RouteController routeController, int i, RouteInfo routeInfo2, Collection<MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor> collection) {
            PrepareTransferNotifier prepareTransferNotifier = this.mTransferNotifier;
            if (prepareTransferNotifier != null) {
                if (!prepareTransferNotifier.mFinished && !prepareTransferNotifier.mCanceled) {
                    prepareTransferNotifier.mCanceled = true;
                    MediaRouteProvider.RouteController routeController2 = prepareTransferNotifier.mToRouteController;
                    if (routeController2 != null) {
                        routeController2.onUnselect(0);
                        prepareTransferNotifier.mToRouteController.onRelease();
                    }
                }
                this.mTransferNotifier = null;
            }
            PrepareTransferNotifier prepareTransferNotifier2 = new PrepareTransferNotifier(globalMediaRouter, routeInfo, routeController, i, routeInfo2, collection);
            this.mTransferNotifier = prepareTransferNotifier2;
            prepareTransferNotifier2.finishTransfer();
        }

        public final void selectRoute(RouteInfo routeInfo, int i) {
            if (!this.mRoutes.contains(routeInfo)) {
                Log.w("MediaRouter", "Ignoring attempt to select removed route: " + routeInfo);
            } else if (!routeInfo.mEnabled) {
                Log.w("MediaRouter", "Ignoring attempt to select disabled route: " + routeInfo);
            } else {
                MediaRouteProvider providerInstance = routeInfo.getProviderInstance();
                MediaRoute2Provider mediaRoute2Provider = this.mMr2Provider;
                if (providerInstance != mediaRoute2Provider || this.mSelectedRoute == routeInfo) {
                    selectRouteInternal(routeInfo, i);
                    return;
                }
                String str = routeInfo.mDescriptorId;
                Objects.requireNonNull(mediaRoute2Provider);
                MediaRoute2Info routeById = mediaRoute2Provider.getRouteById(str);
                if (routeById == null) {
                    MotionLayout$$ExternalSyntheticOutline0.m("transferTo: Specified route not found. routeId=", str, "MR2Provider");
                } else {
                    mediaRoute2Provider.mMediaRouter2.transferTo(routeById);
                }
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:11:0x0021, code lost:
            if (r0 != false) goto L_0x002c;
         */
        /* JADX WARN: Removed duplicated region for block: B:24:0x00b8 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:25:0x00b9  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void selectRouteInternal(androidx.mediarouter.media.MediaRouter.RouteInfo r13, int r14) {
            /*
                Method dump skipped, instructions count: 422
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.mediarouter.media.MediaRouter.GlobalMediaRouter.selectRouteInternal(androidx.mediarouter.media.MediaRouter$RouteInfo, int):void");
        }

        /* JADX WARN: Code restructure failed: missing block: B:53:0x0108, code lost:
            if (r20.mDiscoveryRequestForMr2Provider.isActiveScan() == r2) goto L_0x013f;
         */
        /* JADX WARN: Removed duplicated region for block: B:101:0x00a7 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:23:0x0096  */
        /* JADX WARN: Removed duplicated region for block: B:31:0x00a6  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void updateDiscoveryRequest() {
            /*
                Method dump skipped, instructions count: 434
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.mediarouter.media.MediaRouter.GlobalMediaRouter.updateDiscoveryRequest():void");
        }

        @SuppressLint({"NewApi"})
        public final void updatePlaybackInfoFromSelectedRoute() {
            MediaRouter2.RoutingController routingController;
            if (this.mSelectedRoute != null) {
                Objects.requireNonNull(this.mPlaybackInfo);
                RemoteControlClientCompat$PlaybackInfo remoteControlClientCompat$PlaybackInfo = this.mPlaybackInfo;
                Objects.requireNonNull(this.mSelectedRoute);
                Objects.requireNonNull(remoteControlClientCompat$PlaybackInfo);
                RemoteControlClientCompat$PlaybackInfo remoteControlClientCompat$PlaybackInfo2 = this.mPlaybackInfo;
                this.mSelectedRoute.getVolumeHandling();
                Objects.requireNonNull(remoteControlClientCompat$PlaybackInfo2);
                RemoteControlClientCompat$PlaybackInfo remoteControlClientCompat$PlaybackInfo3 = this.mPlaybackInfo;
                Objects.requireNonNull(this.mSelectedRoute);
                Objects.requireNonNull(remoteControlClientCompat$PlaybackInfo3);
                RemoteControlClientCompat$PlaybackInfo remoteControlClientCompat$PlaybackInfo4 = this.mPlaybackInfo;
                Objects.requireNonNull(this.mSelectedRoute);
                Objects.requireNonNull(remoteControlClientCompat$PlaybackInfo4);
                if (!this.mTransferReceiverDeclared || this.mSelectedRoute.getProviderInstance() != this.mMr2Provider) {
                    Objects.requireNonNull(this.mPlaybackInfo);
                } else {
                    RemoteControlClientCompat$PlaybackInfo remoteControlClientCompat$PlaybackInfo5 = this.mPlaybackInfo;
                    MediaRouteProvider.RouteController routeController = this.mSelectedRouteController;
                    int i = MediaRoute2Provider.$r8$clinit;
                    if ((routeController instanceof MediaRoute2Provider.GroupRouteController) && (routingController = ((MediaRoute2Provider.GroupRouteController) routeController).mRoutingController) != null) {
                        routingController.getId();
                    }
                    Objects.requireNonNull(remoteControlClientCompat$PlaybackInfo5);
                }
                if (this.mRemoteControlClients.size() > 0) {
                    Objects.requireNonNull(this.mRemoteControlClients.get(0));
                    throw null;
                }
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:13:0x0024, code lost:
            if (r18 == r6.mDescriptor) goto L_0x0026;
         */
        /* JADX WARN: Removed duplicated region for block: B:65:0x0196 A[LOOP:4: B:64:0x0194->B:65:0x0196, LOOP_END] */
        /* JADX WARN: Removed duplicated region for block: B:68:0x01b6  */
        /* JADX WARN: Removed duplicated region for block: B:74:0x01e4  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void updateProviderContents(androidx.mediarouter.media.MediaRouter.ProviderInfo r17, androidx.mediarouter.media.MediaRouteProviderDescriptor r18) {
            /*
                Method dump skipped, instructions count: 512
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.mediarouter.media.MediaRouter.GlobalMediaRouter.updateProviderContents(androidx.mediarouter.media.MediaRouter$ProviderInfo, androidx.mediarouter.media.MediaRouteProviderDescriptor):void");
        }

        public final void updateSelectedRouteIfNeeded(boolean z) {
            boolean z2;
            boolean z3;
            RouteInfo routeInfo = this.mDefaultRoute;
            if (routeInfo != null && !routeInfo.isSelectable()) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Clearing the default route because it is no longer selectable: ");
                m.append(this.mDefaultRoute);
                Log.i("MediaRouter", m.toString());
                this.mDefaultRoute = null;
            }
            if (this.mDefaultRoute == null && !this.mRoutes.isEmpty()) {
                Iterator<RouteInfo> it = this.mRoutes.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    RouteInfo next = it.next();
                    if (next.getProviderInstance() != this.mSystemProvider || !next.mDescriptorId.equals("DEFAULT_ROUTE")) {
                        z3 = false;
                    } else {
                        z3 = true;
                    }
                    if (z3 && next.isSelectable()) {
                        this.mDefaultRoute = next;
                        StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Found default route: ");
                        m2.append(this.mDefaultRoute);
                        Log.i("MediaRouter", m2.toString());
                        break;
                    }
                }
            }
            RouteInfo routeInfo2 = this.mBluetoothRoute;
            if (routeInfo2 != null && !routeInfo2.isSelectable()) {
                StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("Clearing the bluetooth route because it is no longer selectable: ");
                m3.append(this.mBluetoothRoute);
                Log.i("MediaRouter", m3.toString());
                this.mBluetoothRoute = null;
            }
            if (this.mBluetoothRoute == null && !this.mRoutes.isEmpty()) {
                Iterator<RouteInfo> it2 = this.mRoutes.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    RouteInfo next2 = it2.next();
                    if (next2.getProviderInstance() != this.mSystemProvider || !next2.supportsControlCategory("android.media.intent.category.LIVE_AUDIO") || next2.supportsControlCategory("android.media.intent.category.LIVE_VIDEO")) {
                        z2 = false;
                    } else {
                        z2 = true;
                    }
                    if (z2 && next2.isSelectable()) {
                        this.mBluetoothRoute = next2;
                        StringBuilder m4 = VendorAtomValue$$ExternalSyntheticOutline1.m("Found bluetooth route: ");
                        m4.append(this.mBluetoothRoute);
                        Log.i("MediaRouter", m4.toString());
                        break;
                    }
                }
            }
            RouteInfo routeInfo3 = this.mSelectedRoute;
            if (routeInfo3 == null || !routeInfo3.mEnabled) {
                StringBuilder m5 = VendorAtomValue$$ExternalSyntheticOutline1.m("Unselecting the current route because it is no longer selectable: ");
                m5.append(this.mSelectedRoute);
                Log.i("MediaRouter", m5.toString());
                selectRouteInternal(chooseFallbackRoute(), 0);
            } else if (z) {
                maybeUpdateMemberRouteControllers();
                updatePlaybackInfoFromSelectedRoute();
            }
        }

        /* JADX WARN: Type inference failed for: r0v5, types: [androidx.mediarouter.media.RemoteControlClientCompat$PlaybackInfo] */
        public GlobalMediaRouter(Context context) {
            this.mApplicationContext = context;
            this.mLowRam = ((ActivityManager) context.getSystemService("activity")).isLowRamDevice();
        }

        public final void addProvider(MediaRouteProvider mediaRouteProvider) {
            if (findProviderInfo(mediaRouteProvider) == null) {
                ProviderInfo providerInfo = new ProviderInfo(mediaRouteProvider);
                this.mProviders.add(providerInfo);
                if (MediaRouter.DEBUG) {
                    Log.d("MediaRouter", "Provider added: " + providerInfo);
                }
                this.mCallbackHandler.post(513, providerInfo);
                updateProviderContents(providerInfo, mediaRouteProvider.mDescriptor);
                ProviderCallback providerCallback = this.mProviderCallback;
                MediaRouter.checkCallingThread();
                mediaRouteProvider.mCallback = providerCallback;
                mediaRouteProvider.setDiscoveryRequest(this.mDiscoveryRequest);
            }
        }

        public final String assignRouteUniqueId(ProviderInfo providerInfo, String str) {
            Objects.requireNonNull(providerInfo);
            MediaRouteProvider.ProviderMetadata providerMetadata = providerInfo.mMetadata;
            Objects.requireNonNull(providerMetadata);
            String flattenToShortString = providerMetadata.mComponentName.flattenToShortString();
            String m = AbstractResolvableFuture$$ExternalSyntheticOutline0.m(flattenToShortString, ":", str);
            if (findRouteByUniqueId(m) < 0) {
                this.mUniqueIdMap.put(new Pair(flattenToShortString, str), m);
                return m;
            }
            Log.w("MediaRouter", "Either " + str + " isn't unique in " + flattenToShortString + " or we're trying to assign a unique ID for an already added route");
            int i = 2;
            while (true) {
                String format = String.format(Locale.US, "%s_%d", m, Integer.valueOf(i));
                if (findRouteByUniqueId(format) < 0) {
                    this.mUniqueIdMap.put(new Pair(flattenToShortString, str), format);
                    return format;
                }
                i++;
            }
        }

        public final int updateRouteDescriptorAndNotify(RouteInfo routeInfo, MediaRouteDescriptor mediaRouteDescriptor) {
            int maybeUpdateDescriptor = routeInfo.maybeUpdateDescriptor(mediaRouteDescriptor);
            if (maybeUpdateDescriptor != 0) {
                if ((maybeUpdateDescriptor & 1) != 0) {
                    if (MediaRouter.DEBUG) {
                        Log.d("MediaRouter", "Route changed: " + routeInfo);
                    }
                    this.mCallbackHandler.post(259, routeInfo);
                }
                if ((maybeUpdateDescriptor & 2) != 0) {
                    if (MediaRouter.DEBUG) {
                        Log.d("MediaRouter", "Route volume changed: " + routeInfo);
                    }
                    this.mCallbackHandler.post(260, routeInfo);
                }
                if ((maybeUpdateDescriptor & 4) != 0) {
                    if (MediaRouter.DEBUG) {
                        Log.d("MediaRouter", "Route presentation display changed: " + routeInfo);
                    }
                    this.mCallbackHandler.post(261, routeInfo);
                }
            }
            return maybeUpdateDescriptor;
        }
    }

    /* loaded from: classes.dex */
    public static final class ProviderInfo {
        public MediaRouteProviderDescriptor mDescriptor;
        public final MediaRouteProvider.ProviderMetadata mMetadata;
        public final MediaRouteProvider mProviderInstance;
        public final ArrayList mRoutes = new ArrayList();

        public final RouteInfo findRouteByDescriptorId(String str) {
            int size = this.mRoutes.size();
            for (int i = 0; i < size; i++) {
                if (((RouteInfo) this.mRoutes.get(i)).mDescriptorId.equals(str)) {
                    return (RouteInfo) this.mRoutes.get(i);
                }
            }
            return null;
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("MediaRouter.RouteProviderInfo{ packageName=");
            MediaRouteProvider.ProviderMetadata providerMetadata = this.mMetadata;
            Objects.requireNonNull(providerMetadata);
            m.append(providerMetadata.mComponentName.getPackageName());
            m.append(" }");
            return m.toString();
        }

        public ProviderInfo(MediaRouteProvider mediaRouteProvider) {
            this.mProviderInstance = mediaRouteProvider;
            Objects.requireNonNull(mediaRouteProvider);
            this.mMetadata = mediaRouteProvider.mMetadata;
        }
    }

    /* loaded from: classes.dex */
    public static class RouteInfo {
        public boolean mCanDisconnect;
        public int mConnectionState;
        public String mDescription;
        public MediaRouteDescriptor mDescriptor;
        public final String mDescriptorId;
        public int mDeviceType;
        public ArrayMap mDynamicGroupDescriptors;
        public boolean mEnabled;
        public Bundle mExtras;
        public Uri mIconUri;
        public String mName;
        public int mPlaybackStream;
        public int mPlaybackType;
        public final ProviderInfo mProvider;
        public IntentSender mSettingsIntent;
        public final String mUniqueId;
        public int mVolume;
        public int mVolumeHandling;
        public int mVolumeMax;
        public final ArrayList<IntentFilter> mControlFilters = new ArrayList<>();
        public int mPresentationDisplayId = -1;
        public ArrayList mMemberRoutes = new ArrayList();

        /* loaded from: classes.dex */
        public static final class DynamicGroupState {
            public final MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor mDynamicDescriptor;

            public final boolean isGroupable() {
                MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor dynamicRouteDescriptor = this.mDynamicDescriptor;
                if (dynamicRouteDescriptor != null) {
                    Objects.requireNonNull(dynamicRouteDescriptor);
                    if (dynamicRouteDescriptor.mIsGroupable) {
                        return true;
                    }
                }
                return false;
            }

            public DynamicGroupState(MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor dynamicRouteDescriptor) {
                this.mDynamicDescriptor = dynamicRouteDescriptor;
            }
        }

        public final List<RouteInfo> getMemberRoutes() {
            return Collections.unmodifiableList(this.mMemberRoutes);
        }

        public final MediaRouteProvider getProviderInstance() {
            ProviderInfo providerInfo = this.mProvider;
            Objects.requireNonNull(providerInfo);
            MediaRouter.checkCallingThread();
            return providerInfo.mProviderInstance;
        }

        public final boolean isSelectable() {
            if (this.mDescriptor == null || !this.mEnabled) {
                return false;
            }
            return true;
        }

        public final boolean matchesSelector(MediaRouteSelector mediaRouteSelector) {
            if (mediaRouteSelector != null) {
                MediaRouter.checkCallingThread();
                ArrayList<IntentFilter> arrayList = this.mControlFilters;
                if (arrayList == null) {
                    return false;
                }
                mediaRouteSelector.ensureControlCategories();
                if (mediaRouteSelector.mControlCategories.isEmpty()) {
                    return false;
                }
                Iterator<IntentFilter> it = arrayList.iterator();
                while (it.hasNext()) {
                    IntentFilter next = it.next();
                    if (next != null) {
                        for (String str : mediaRouteSelector.mControlCategories) {
                            if (next.hasCategory(str)) {
                                return true;
                            }
                        }
                        continue;
                    }
                }
                return false;
            }
            throw new IllegalArgumentException("selector must not be null");
        }

        /* JADX WARN: Code restructure failed: missing block: B:55:0x0109, code lost:
            if (r4.hasNext() == false) goto L_0x010b;
         */
        /* JADX WARN: Removed duplicated region for block: B:111:0x028d  */
        /* JADX WARN: Removed duplicated region for block: B:112:0x0292  */
        /* JADX WARN: Removed duplicated region for block: B:116:0x010d A[EDGE_INSN: B:116:0x010d->B:57:0x010d ?: BREAK  , SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:59:0x0110  */
        /* JADX WARN: Removed duplicated region for block: B:62:0x012d  */
        /* JADX WARN: Removed duplicated region for block: B:65:0x0144  */
        /* JADX WARN: Removed duplicated region for block: B:68:0x015a  */
        /* JADX WARN: Removed duplicated region for block: B:71:0x0171  */
        /* JADX WARN: Removed duplicated region for block: B:74:0x0188  */
        /* JADX WARN: Removed duplicated region for block: B:77:0x019f  */
        /* JADX WARN: Removed duplicated region for block: B:80:0x01b5  */
        /* JADX WARN: Removed duplicated region for block: B:83:0x01cf  */
        /* JADX WARN: Removed duplicated region for block: B:86:0x01ec  */
        /* JADX WARN: Removed duplicated region for block: B:89:0x0204  */
        /* JADX WARN: Removed duplicated region for block: B:92:0x0223  */
        /* JADX WARN: Removed duplicated region for block: B:95:0x022a  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final int maybeUpdateDescriptor(androidx.mediarouter.media.MediaRouteDescriptor r12) {
            /*
                Method dump skipped, instructions count: 660
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.mediarouter.media.MediaRouter.RouteInfo.maybeUpdateDescriptor(androidx.mediarouter.media.MediaRouteDescriptor):int");
        }

        public final String toString() {
            StringBuilder sb = new StringBuilder();
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("MediaRouter.RouteInfo{ uniqueId=");
            m.append(this.mUniqueId);
            m.append(", name=");
            m.append(this.mName);
            m.append(", description=");
            m.append(this.mDescription);
            m.append(", iconUri=");
            m.append(this.mIconUri);
            m.append(", enabled=");
            m.append(this.mEnabled);
            m.append(", connectionState=");
            m.append(this.mConnectionState);
            m.append(", canDisconnect=");
            m.append(this.mCanDisconnect);
            m.append(", playbackType=");
            m.append(this.mPlaybackType);
            m.append(", playbackStream=");
            m.append(this.mPlaybackStream);
            m.append(", deviceType=");
            m.append(this.mDeviceType);
            m.append(", volumeHandling=");
            m.append(this.mVolumeHandling);
            m.append(", volume=");
            m.append(this.mVolume);
            m.append(", volumeMax=");
            m.append(this.mVolumeMax);
            m.append(", presentationDisplayId=");
            m.append(this.mPresentationDisplayId);
            m.append(", extras=");
            m.append(this.mExtras);
            m.append(", settingsIntent=");
            m.append(this.mSettingsIntent);
            m.append(", providerPackageName=");
            ProviderInfo providerInfo = this.mProvider;
            Objects.requireNonNull(providerInfo);
            MediaRouteProvider.ProviderMetadata providerMetadata = providerInfo.mMetadata;
            Objects.requireNonNull(providerMetadata);
            m.append(providerMetadata.mComponentName.getPackageName());
            sb.append(m.toString());
            if (isGroup()) {
                sb.append(", members=[");
                int size = this.mMemberRoutes.size();
                for (int i = 0; i < size; i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    if (this.mMemberRoutes.get(i) != this) {
                        RouteInfo routeInfo = (RouteInfo) this.mMemberRoutes.get(i);
                        Objects.requireNonNull(routeInfo);
                        sb.append(routeInfo.mUniqueId);
                    }
                }
                sb.append(']');
            }
            sb.append(" }");
            return sb.toString();
        }

        public final void updateDynamicDescriptors(Collection<MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor> collection) {
            this.mMemberRoutes.clear();
            if (this.mDynamicGroupDescriptors == null) {
                this.mDynamicGroupDescriptors = new ArrayMap();
            }
            this.mDynamicGroupDescriptors.clear();
            for (MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor dynamicRouteDescriptor : collection) {
                Objects.requireNonNull(dynamicRouteDescriptor);
                RouteInfo findRouteByDescriptorId = this.mProvider.findRouteByDescriptorId(dynamicRouteDescriptor.mMediaRouteDescriptor.getId());
                if (findRouteByDescriptorId != null) {
                    this.mDynamicGroupDescriptors.put(findRouteByDescriptorId.mUniqueId, dynamicRouteDescriptor);
                    int i = dynamicRouteDescriptor.mSelectionState;
                    if (i == 2 || i == 3) {
                        this.mMemberRoutes.add(findRouteByDescriptorId);
                    }
                }
            }
            MediaRouter.getGlobalRouter().mCallbackHandler.post(259, this);
        }

        public RouteInfo(ProviderInfo providerInfo, String str, String str2) {
            this.mProvider = providerInfo;
            this.mDescriptorId = str;
            this.mUniqueId = str2;
        }

        public static MediaRouteProvider.DynamicGroupRouteController getDynamicGroupController() {
            MediaRouter.checkCallingThread();
            MediaRouteProvider.RouteController routeController = MediaRouter.getGlobalRouter().mSelectedRouteController;
            if (routeController instanceof MediaRouteProvider.DynamicGroupRouteController) {
                return (MediaRouteProvider.DynamicGroupRouteController) routeController;
            }
            return null;
        }

        public final DynamicGroupState getDynamicGroupState(RouteInfo routeInfo) {
            Objects.requireNonNull(routeInfo, "route must not be null");
            ArrayMap arrayMap = this.mDynamicGroupDescriptors;
            if (arrayMap == null || !arrayMap.containsKey(routeInfo.mUniqueId)) {
                return null;
            }
            ArrayMap arrayMap2 = this.mDynamicGroupDescriptors;
            String str = routeInfo.mUniqueId;
            Objects.requireNonNull(arrayMap2);
            return new DynamicGroupState((MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor) arrayMap2.getOrDefault(str, null));
        }

        public final int getVolumeHandling() {
            boolean z;
            if (isGroup()) {
                if (MediaRouter.sGlobal == null) {
                    z = false;
                } else {
                    Objects.requireNonNull(MediaRouter.getGlobalRouter());
                    z = true;
                }
                if (!z) {
                    return 0;
                }
            }
            return this.mVolumeHandling;
        }

        public final boolean isDefaultOrBluetooth() {
            boolean z;
            MediaRouter.checkCallingThread();
            GlobalMediaRouter globalRouter = MediaRouter.getGlobalRouter();
            Objects.requireNonNull(globalRouter);
            RouteInfo routeInfo = globalRouter.mDefaultRoute;
            if (routeInfo != null) {
                if (routeInfo == this) {
                    z = true;
                } else {
                    z = false;
                }
                if (z || this.mDeviceType == 3) {
                    return true;
                }
                MediaRouteProvider providerInstance = getProviderInstance();
                Objects.requireNonNull(providerInstance);
                MediaRouteProvider.ProviderMetadata providerMetadata = providerInstance.mMetadata;
                Objects.requireNonNull(providerMetadata);
                if (!TextUtils.equals(providerMetadata.mComponentName.getPackageName(), ThemeOverlayApplier.ANDROID_PACKAGE) || !supportsControlCategory("android.media.intent.category.LIVE_AUDIO") || supportsControlCategory("android.media.intent.category.LIVE_VIDEO")) {
                    return false;
                }
                return true;
            }
            throw new IllegalStateException("There is no default route.  The media router has not yet been fully initialized.");
        }

        public final boolean isGroup() {
            if (getMemberRoutes().size() >= 1) {
                return true;
            }
            return false;
        }

        public final boolean isSelected() {
            MediaRouter.checkCallingThread();
            if (MediaRouter.getGlobalRouter().getSelectedRoute() == this) {
                return true;
            }
            return false;
        }

        public final void requestSetVolume(int i) {
            MediaRouteProvider.RouteController routeController;
            MediaRouteProvider.RouteController routeController2;
            MediaRouter.checkCallingThread();
            GlobalMediaRouter globalRouter = MediaRouter.getGlobalRouter();
            int min = Math.min(this.mVolumeMax, Math.max(0, i));
            Objects.requireNonNull(globalRouter);
            if (this == globalRouter.mSelectedRoute && (routeController2 = globalRouter.mSelectedRouteController) != null) {
                routeController2.onSetVolume(min);
            } else if (!globalRouter.mRouteControllerMap.isEmpty() && (routeController = (MediaRouteProvider.RouteController) globalRouter.mRouteControllerMap.get(this.mUniqueId)) != null) {
                routeController.onSetVolume(min);
            }
        }

        public final void requestUpdateVolume(int i) {
            MediaRouteProvider.RouteController routeController;
            MediaRouteProvider.RouteController routeController2;
            MediaRouter.checkCallingThread();
            if (i != 0) {
                GlobalMediaRouter globalRouter = MediaRouter.getGlobalRouter();
                Objects.requireNonNull(globalRouter);
                if (this == globalRouter.mSelectedRoute && (routeController2 = globalRouter.mSelectedRouteController) != null) {
                    routeController2.onUpdateVolume(i);
                } else if (!globalRouter.mRouteControllerMap.isEmpty() && (routeController = (MediaRouteProvider.RouteController) globalRouter.mRouteControllerMap.get(this.mUniqueId)) != null) {
                    routeController.onUpdateVolume(i);
                }
            }
        }

        public final void select() {
            MediaRouter.checkCallingThread();
            MediaRouter.getGlobalRouter().selectRoute(this, 3);
        }

        public final boolean supportsControlCategory(String str) {
            MediaRouter.checkCallingThread();
            int size = this.mControlFilters.size();
            for (int i = 0; i < size; i++) {
                if (this.mControlFilters.get(i).hasCategory(str)) {
                    return true;
                }
            }
            return false;
        }
    }

    /* loaded from: classes.dex */
    public static final class CallbackRecord {
        public final Callback mCallback;
        public int mFlags;
        public final MediaRouter mRouter;
        public MediaRouteSelector mSelector = MediaRouteSelector.EMPTY;
        public long mTimestamp;

        public CallbackRecord(MediaRouter mediaRouter, Callback callback) {
            this.mRouter = mediaRouter;
            this.mCallback = callback;
        }
    }

    /* loaded from: classes.dex */
    public static final class PrepareTransferNotifier {
        public final RouteInfo mFromRoute;
        public final ArrayList mMemberRoutes;
        public final int mReason;
        public final RouteInfo mRequestedRoute;
        public final WeakReference<GlobalMediaRouter> mRouter;
        public final RouteInfo mToRoute;
        public final MediaRouteProvider.RouteController mToRouteController;
        public ListenableFuture<Void> mFuture = null;
        public boolean mFinished = false;
        public boolean mCanceled = false;

        public PrepareTransferNotifier(GlobalMediaRouter globalMediaRouter, RouteInfo routeInfo, MediaRouteProvider.RouteController routeController, int i, RouteInfo routeInfo2, Collection<MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor> collection) {
            ArrayList arrayList = null;
            this.mRouter = new WeakReference<>(globalMediaRouter);
            this.mToRoute = routeInfo;
            this.mToRouteController = routeController;
            this.mReason = i;
            this.mFromRoute = globalMediaRouter.mSelectedRoute;
            this.mRequestedRoute = routeInfo2;
            this.mMemberRoutes = collection != null ? new ArrayList(collection) : arrayList;
            globalMediaRouter.mCallbackHandler.postDelayed(new BubbleStackView$$ExternalSyntheticLambda15(this, 1), WifiTracker.MAX_SCAN_RESULT_AGE_MILLIS);
        }

        public final void finishTransfer() {
            ListenableFuture<Void> listenableFuture;
            MediaRouter.checkCallingThread();
            if (!this.mFinished && !this.mCanceled) {
                GlobalMediaRouter globalMediaRouter = this.mRouter.get();
                if (globalMediaRouter != null && globalMediaRouter.mTransferNotifier == this && ((listenableFuture = this.mFuture) == null || !listenableFuture.isCancelled())) {
                    this.mFinished = true;
                    globalMediaRouter.mTransferNotifier = null;
                    GlobalMediaRouter globalMediaRouter2 = this.mRouter.get();
                    if (globalMediaRouter2 != null) {
                        RouteInfo routeInfo = globalMediaRouter2.mSelectedRoute;
                        RouteInfo routeInfo2 = this.mFromRoute;
                        if (routeInfo == routeInfo2) {
                            GlobalMediaRouter.CallbackHandler callbackHandler = globalMediaRouter2.mCallbackHandler;
                            int i = this.mReason;
                            Objects.requireNonNull(callbackHandler);
                            Message obtainMessage = callbackHandler.obtainMessage(263, routeInfo2);
                            obtainMessage.arg1 = i;
                            obtainMessage.sendToTarget();
                            MediaRouteProvider.RouteController routeController = globalMediaRouter2.mSelectedRouteController;
                            if (routeController != null) {
                                routeController.onUnselect(this.mReason);
                                globalMediaRouter2.mSelectedRouteController.onRelease();
                            }
                            if (!globalMediaRouter2.mRouteControllerMap.isEmpty()) {
                                for (MediaRouteProvider.RouteController routeController2 : globalMediaRouter2.mRouteControllerMap.values()) {
                                    routeController2.onUnselect(this.mReason);
                                    routeController2.onRelease();
                                }
                                globalMediaRouter2.mRouteControllerMap.clear();
                            }
                            globalMediaRouter2.mSelectedRouteController = null;
                        }
                    }
                    GlobalMediaRouter globalMediaRouter3 = this.mRouter.get();
                    if (globalMediaRouter3 != null) {
                        RouteInfo routeInfo3 = this.mToRoute;
                        globalMediaRouter3.mSelectedRoute = routeInfo3;
                        globalMediaRouter3.mSelectedRouteController = this.mToRouteController;
                        RouteInfo routeInfo4 = this.mRequestedRoute;
                        if (routeInfo4 == null) {
                            GlobalMediaRouter.CallbackHandler callbackHandler2 = globalMediaRouter3.mCallbackHandler;
                            Pair pair = new Pair(this.mFromRoute, routeInfo3);
                            int i2 = this.mReason;
                            Objects.requireNonNull(callbackHandler2);
                            Message obtainMessage2 = callbackHandler2.obtainMessage(262, pair);
                            obtainMessage2.arg1 = i2;
                            obtainMessage2.sendToTarget();
                        } else {
                            GlobalMediaRouter.CallbackHandler callbackHandler3 = globalMediaRouter3.mCallbackHandler;
                            Pair pair2 = new Pair(routeInfo4, routeInfo3);
                            int i3 = this.mReason;
                            Objects.requireNonNull(callbackHandler3);
                            Message obtainMessage3 = callbackHandler3.obtainMessage(264, pair2);
                            obtainMessage3.arg1 = i3;
                            obtainMessage3.sendToTarget();
                        }
                        globalMediaRouter3.mRouteControllerMap.clear();
                        globalMediaRouter3.maybeUpdateMemberRouteControllers();
                        globalMediaRouter3.updatePlaybackInfoFromSelectedRoute();
                        ArrayList arrayList = this.mMemberRoutes;
                        if (arrayList != null) {
                            globalMediaRouter3.mSelectedRoute.updateDynamicDescriptors(arrayList);
                        }
                    }
                } else if (!this.mFinished && !this.mCanceled) {
                    this.mCanceled = true;
                    MediaRouteProvider.RouteController routeController3 = this.mToRouteController;
                    if (routeController3 != null) {
                        routeController3.onUnselect(0);
                        this.mToRouteController.onRelease();
                    }
                }
            }
        }
    }

    public static GlobalMediaRouter getGlobalRouter() {
        GlobalMediaRouter globalMediaRouter = sGlobal;
        if (globalMediaRouter == null) {
            return null;
        }
        globalMediaRouter.ensureInitialized();
        return sGlobal;
    }

    public static MediaRouter getInstance(Context context) {
        if (context != null) {
            checkCallingThread();
            if (sGlobal == null) {
                sGlobal = new GlobalMediaRouter(context.getApplicationContext());
            }
            GlobalMediaRouter globalMediaRouter = sGlobal;
            Objects.requireNonNull(globalMediaRouter);
            int size = globalMediaRouter.mRouters.size();
            while (true) {
                size--;
                if (size >= 0) {
                    MediaRouter mediaRouter = globalMediaRouter.mRouters.get(size).get();
                    if (mediaRouter == null) {
                        globalMediaRouter.mRouters.remove(size);
                    } else if (mediaRouter.mContext == context) {
                        return mediaRouter;
                    }
                } else {
                    MediaRouter mediaRouter2 = new MediaRouter(context);
                    globalMediaRouter.mRouters.add(new WeakReference<>(mediaRouter2));
                    return mediaRouter2;
                }
            }
        } else {
            throw new IllegalArgumentException("context must not be null");
        }
    }

    public static boolean isRouteAvailable(MediaRouteSelector mediaRouteSelector) {
        if (mediaRouteSelector != null) {
            checkCallingThread();
            GlobalMediaRouter globalRouter = getGlobalRouter();
            Objects.requireNonNull(globalRouter);
            if (mediaRouteSelector.isEmpty()) {
                return false;
            }
            if (!globalRouter.mLowRam) {
                int size = globalRouter.mRoutes.size();
                for (int i = 0; i < size; i++) {
                    RouteInfo routeInfo = globalRouter.mRoutes.get(i);
                    if (routeInfo.isDefaultOrBluetooth() || !routeInfo.matchesSelector(mediaRouteSelector)) {
                    }
                }
                return false;
            }
            return true;
        }
        throw new IllegalArgumentException("selector must not be null");
    }

    public static void unselect(int i) {
        if (i < 0 || i > 3) {
            throw new IllegalArgumentException("Unsupported reason to unselect route");
        }
        checkCallingThread();
        GlobalMediaRouter globalRouter = getGlobalRouter();
        RouteInfo chooseFallbackRoute = globalRouter.chooseFallbackRoute();
        if (globalRouter.getSelectedRoute() != chooseFallbackRoute) {
            globalRouter.selectRoute(chooseFallbackRoute, i);
        }
    }

    public final void addCallback(MediaRouteSelector mediaRouteSelector, Callback callback, int i) {
        CallbackRecord callbackRecord;
        if (mediaRouteSelector == null) {
            throw new IllegalArgumentException("selector must not be null");
        } else if (callback != null) {
            checkCallingThread();
            if (DEBUG) {
                Log.d("MediaRouter", "addCallback: selector=" + mediaRouteSelector + ", callback=" + callback + ", flags=" + Integer.toHexString(i));
            }
            int size = this.mCallbackRecords.size();
            boolean z = false;
            int i2 = 0;
            while (true) {
                if (i2 >= size) {
                    i2 = -1;
                    break;
                } else if (this.mCallbackRecords.get(i2).mCallback == callback) {
                    break;
                } else {
                    i2++;
                }
            }
            if (i2 < 0) {
                callbackRecord = new CallbackRecord(this, callback);
                this.mCallbackRecords.add(callbackRecord);
            } else {
                callbackRecord = this.mCallbackRecords.get(i2);
            }
            boolean z2 = true;
            if (i != callbackRecord.mFlags) {
                callbackRecord.mFlags = i;
                z = true;
            }
            long elapsedRealtime = SystemClock.elapsedRealtime();
            if ((i & 1) != 0) {
                z = true;
            }
            callbackRecord.mTimestamp = elapsedRealtime;
            MediaRouteSelector mediaRouteSelector2 = callbackRecord.mSelector;
            mediaRouteSelector2.ensureControlCategories();
            mediaRouteSelector.ensureControlCategories();
            if (!mediaRouteSelector2.mControlCategories.containsAll(mediaRouteSelector.mControlCategories)) {
                MediaRouteSelector.Builder builder = new MediaRouteSelector.Builder(callbackRecord.mSelector);
                builder.addControlCategories(mediaRouteSelector.getControlCategories());
                callbackRecord.mSelector = builder.build();
            } else {
                z2 = z;
            }
            if (z2) {
                getGlobalRouter().updateDiscoveryRequest();
            }
        } else {
            throw new IllegalArgumentException("callback must not be null");
        }
    }

    public final void removeCallback(Callback callback) {
        if (callback != null) {
            checkCallingThread();
            if (DEBUG) {
                Log.d("MediaRouter", "removeCallback: callback=" + callback);
            }
            int size = this.mCallbackRecords.size();
            int i = 0;
            while (true) {
                if (i >= size) {
                    i = -1;
                    break;
                } else if (this.mCallbackRecords.get(i).mCallback == callback) {
                    break;
                } else {
                    i++;
                }
            }
            if (i >= 0) {
                this.mCallbackRecords.remove(i);
                getGlobalRouter().updateDiscoveryRequest();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("callback must not be null");
    }

    public MediaRouter(Context context) {
        this.mContext = context;
    }

    public static void checkCallingThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("The media router service must only be accessed on the application's main thread.");
        }
    }

    public static RouteInfo getSelectedRoute() {
        checkCallingThread();
        return getGlobalRouter().getSelectedRoute();
    }
}
