package androidx.mediarouter.media;

import android.content.Context;
import android.media.MediaRoute2Info;
import android.media.MediaRouter2;
import android.media.RouteDiscoveryPreference;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.SparseArray;
import androidx.constraintlayout.motion.widget.MotionLayout$$ExternalSyntheticOutline0;
import androidx.mediarouter.media.MediaRouteDescriptor;
import androidx.mediarouter.media.MediaRouteProvider;
import androidx.mediarouter.media.MediaRouteSelector;
import androidx.mediarouter.media.MediaRouter;
import com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda2;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public final class MediaRoute2Provider extends MediaRouteProvider {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final Callback mCallback;
    public final MediaRouter2 mMediaRouter2;
    public final ArrayMap mControllerMap = new ArrayMap();
    public final RouteCallback mRouteCallback = new RouteCallback();
    public final TransferCallback mTransferCallback = new TransferCallback();
    public final ControllerCallback mControllerCallback = new ControllerCallback();
    public ArrayList mRoutes = new ArrayList();
    public ArrayMap mRouteIdToOriginalRouteIdMap = new ArrayMap();
    public final MediaRoute2Provider$$ExternalSyntheticLambda0 mHandlerExecutor = new MediaRoute2Provider$$ExternalSyntheticLambda0(new Handler(Looper.getMainLooper()));

    /* loaded from: classes.dex */
    public static abstract class Callback {
    }

    /* loaded from: classes.dex */
    public class ControllerCallback extends MediaRouter2.ControllerCallback {
        public ControllerCallback() {
        }

        @Override // android.media.MediaRouter2.ControllerCallback
        public final void onControllerUpdated(MediaRouter2.RoutingController routingController) {
            MediaRoute2Provider.this.setDynamicRouteDescriptors(routingController);
        }
    }

    /* loaded from: classes.dex */
    public class GroupRouteController extends MediaRouteProvider.DynamicGroupRouteController {
        public final Handler mControllerHandler;
        public MediaRouteDescriptor mGroupRouteDescriptor;
        public final String mInitialMemberRouteId;
        public final Messenger mReceiveMessenger;
        public final MediaRouter2.RoutingController mRoutingController;
        public final Messenger mServiceMessenger;
        public final SparseArray<MediaRouter.ControlRequestCallback> mPendingCallbacks = new SparseArray<>();
        public AtomicInteger mNextRequestId = new AtomicInteger(1);
        public final CreateUserActivity$$ExternalSyntheticLambda2 mClearOptimisticVolumeRunnable = new CreateUserActivity$$ExternalSyntheticLambda2(this, 1);
        public int mOptimisticVolume = -1;

        /* loaded from: classes.dex */
        public class ReceiveHandler extends Handler {
            public ReceiveHandler() {
                super(Looper.getMainLooper());
            }

            @Override // android.os.Handler
            public final void handleMessage(Message message) {
                String str;
                int i = message.what;
                int i2 = message.arg1;
                Object obj = message.obj;
                Bundle peekData = message.peekData();
                MediaRouter.ControlRequestCallback controlRequestCallback = GroupRouteController.this.mPendingCallbacks.get(i2);
                if (controlRequestCallback == null) {
                    Log.w("MR2Provider", "Pending callback not found for control request.");
                    return;
                }
                GroupRouteController.this.mPendingCallbacks.remove(i2);
                if (i == 3) {
                    controlRequestCallback.onResult((Bundle) obj);
                } else if (i == 4) {
                    if (peekData == null) {
                        str = null;
                    } else {
                        str = peekData.getString("error");
                    }
                    controlRequestCallback.onError(str, (Bundle) obj);
                }
            }
        }

        public GroupRouteController(MediaRouter2.RoutingController routingController, String str) {
            Messenger messenger;
            Bundle controlHints;
            this.mRoutingController = routingController;
            this.mInitialMemberRouteId = str;
            int i = MediaRoute2Provider.$r8$clinit;
            Messenger messenger2 = null;
            if (routingController == null || (controlHints = routingController.getControlHints()) == null) {
                messenger = null;
            } else {
                messenger = (Messenger) controlHints.getParcelable("androidx.mediarouter.media.KEY_MESSENGER");
            }
            this.mServiceMessenger = messenger;
            this.mReceiveMessenger = messenger != null ? new Messenger(new ReceiveHandler()) : messenger2;
            this.mControllerHandler = new Handler(Looper.getMainLooper());
        }

        @Override // androidx.mediarouter.media.MediaRouteProvider.DynamicGroupRouteController
        public final void onAddMemberRoute(String str) {
            if (str == null || str.isEmpty()) {
                Log.w("MR2Provider", "onAddMemberRoute: Ignoring null or empty routeId.");
                return;
            }
            MediaRoute2Info routeById = MediaRoute2Provider.this.getRouteById(str);
            if (routeById == null) {
                MotionLayout$$ExternalSyntheticOutline0.m("onAddMemberRoute: Specified route not found. routeId=", str, "MR2Provider");
            } else {
                this.mRoutingController.selectRoute(routeById);
            }
        }

        @Override // androidx.mediarouter.media.MediaRouteProvider.RouteController
        public final void onRelease() {
            this.mRoutingController.release();
        }

        @Override // androidx.mediarouter.media.MediaRouteProvider.DynamicGroupRouteController
        public final void onRemoveMemberRoute(String str) {
            if (str == null || str.isEmpty()) {
                Log.w("MR2Provider", "onRemoveMemberRoute: Ignoring null or empty routeId.");
                return;
            }
            MediaRoute2Info routeById = MediaRoute2Provider.this.getRouteById(str);
            if (routeById == null) {
                MotionLayout$$ExternalSyntheticOutline0.m("onRemoveMemberRoute: Specified route not found. routeId=", str, "MR2Provider");
            } else {
                this.mRoutingController.deselectRoute(routeById);
            }
        }

        @Override // androidx.mediarouter.media.MediaRouteProvider.RouteController
        public final void onSetVolume(int i) {
            MediaRouter2.RoutingController routingController = this.mRoutingController;
            if (routingController != null) {
                routingController.setVolume(i);
                this.mOptimisticVolume = i;
                this.mControllerHandler.removeCallbacks(this.mClearOptimisticVolumeRunnable);
                this.mControllerHandler.postDelayed(this.mClearOptimisticVolumeRunnable, 1000L);
            }
        }

        @Override // androidx.mediarouter.media.MediaRouteProvider.DynamicGroupRouteController
        public final void onUpdateMemberRoutes(List<String> list) {
            if (list == null || list.isEmpty()) {
                Log.w("MR2Provider", "onUpdateMemberRoutes: Ignoring null or empty routeIds.");
                return;
            }
            String str = list.get(0);
            MediaRoute2Info routeById = MediaRoute2Provider.this.getRouteById(str);
            if (routeById == null) {
                MotionLayout$$ExternalSyntheticOutline0.m("onUpdateMemberRoutes: Specified route not found. routeId=", str, "MR2Provider");
            } else {
                MediaRoute2Provider.this.mMediaRouter2.transferTo(routeById);
            }
        }

        @Override // androidx.mediarouter.media.MediaRouteProvider.RouteController
        public final void onUpdateVolume(int i) {
            MediaRouter2.RoutingController routingController = this.mRoutingController;
            if (routingController != null) {
                int i2 = this.mOptimisticVolume;
                if (i2 < 0) {
                    i2 = routingController.getVolume();
                }
                int max = Math.max(0, Math.min(i2 + i, this.mRoutingController.getVolumeMax()));
                this.mOptimisticVolume = max;
                this.mRoutingController.setVolume(max);
                this.mControllerHandler.removeCallbacks(this.mClearOptimisticVolumeRunnable);
                this.mControllerHandler.postDelayed(this.mClearOptimisticVolumeRunnable, 1000L);
            }
        }
    }

    /* loaded from: classes.dex */
    public class MemberRouteController extends MediaRouteProvider.RouteController {
        public final GroupRouteController mGroupRouteController;
        public final String mOriginalRouteId;

        @Override // androidx.mediarouter.media.MediaRouteProvider.RouteController
        public final void onSetVolume(int i) {
            GroupRouteController groupRouteController;
            String str = this.mOriginalRouteId;
            if (str != null && (groupRouteController = this.mGroupRouteController) != null) {
                Objects.requireNonNull(groupRouteController);
                MediaRouter2.RoutingController routingController = groupRouteController.mRoutingController;
                if (routingController != null && !routingController.isReleased() && groupRouteController.mServiceMessenger != null) {
                    int andIncrement = groupRouteController.mNextRequestId.getAndIncrement();
                    Message obtain = Message.obtain();
                    obtain.what = 7;
                    obtain.arg1 = andIncrement;
                    Bundle bundle = new Bundle();
                    bundle.putInt("volume", i);
                    bundle.putString("routeId", str);
                    obtain.setData(bundle);
                    obtain.replyTo = groupRouteController.mReceiveMessenger;
                    try {
                        groupRouteController.mServiceMessenger.send(obtain);
                    } catch (DeadObjectException unused) {
                    } catch (RemoteException e) {
                        Log.e("MR2Provider", "Could not send control request to service.", e);
                    }
                }
            }
        }

        @Override // androidx.mediarouter.media.MediaRouteProvider.RouteController
        public final void onUpdateVolume(int i) {
            GroupRouteController groupRouteController;
            String str = this.mOriginalRouteId;
            if (str != null && (groupRouteController = this.mGroupRouteController) != null) {
                Objects.requireNonNull(groupRouteController);
                MediaRouter2.RoutingController routingController = groupRouteController.mRoutingController;
                if (routingController != null && !routingController.isReleased() && groupRouteController.mServiceMessenger != null) {
                    int andIncrement = groupRouteController.mNextRequestId.getAndIncrement();
                    Message obtain = Message.obtain();
                    obtain.what = 8;
                    obtain.arg1 = andIncrement;
                    Bundle bundle = new Bundle();
                    bundle.putInt("volume", i);
                    bundle.putString("routeId", str);
                    obtain.setData(bundle);
                    obtain.replyTo = groupRouteController.mReceiveMessenger;
                    try {
                        groupRouteController.mServiceMessenger.send(obtain);
                    } catch (DeadObjectException unused) {
                    } catch (RemoteException e) {
                        Log.e("MR2Provider", "Could not send control request to service.", e);
                    }
                }
            }
        }

        public MemberRouteController(String str, GroupRouteController groupRouteController) {
            this.mOriginalRouteId = str;
            this.mGroupRouteController = groupRouteController;
        }
    }

    /* loaded from: classes.dex */
    public class RouteCallback extends MediaRouter2.RouteCallback {
        public RouteCallback() {
        }

        @Override // android.media.MediaRouter2.RouteCallback
        public final void onRoutesAdded(List<MediaRoute2Info> list) {
            MediaRoute2Provider.this.refreshRoutes();
        }

        @Override // android.media.MediaRouter2.RouteCallback
        public final void onRoutesChanged(List<MediaRoute2Info> list) {
            MediaRoute2Provider.this.refreshRoutes();
        }

        @Override // android.media.MediaRouter2.RouteCallback
        public final void onRoutesRemoved(List<MediaRoute2Info> list) {
            MediaRoute2Provider.this.refreshRoutes();
        }
    }

    /* loaded from: classes.dex */
    public class TransferCallback extends MediaRouter2.TransferCallback {
        public TransferCallback() {
        }

        @Override // android.media.MediaRouter2.TransferCallback
        public final void onStop(MediaRouter2.RoutingController routingController) {
            MediaRouteProvider.RouteController routeController = (MediaRouteProvider.RouteController) MediaRoute2Provider.this.mControllerMap.remove(routingController);
            if (routeController != null) {
                MediaRouter.GlobalMediaRouter.Mr2ProviderCallback mr2ProviderCallback = (MediaRouter.GlobalMediaRouter.Mr2ProviderCallback) MediaRoute2Provider.this.mCallback;
                Objects.requireNonNull(mr2ProviderCallback);
                MediaRouter.GlobalMediaRouter globalMediaRouter = MediaRouter.GlobalMediaRouter.this;
                if (routeController == globalMediaRouter.mSelectedRouteController) {
                    MediaRouter.RouteInfo chooseFallbackRoute = globalMediaRouter.chooseFallbackRoute();
                    if (MediaRouter.GlobalMediaRouter.this.getSelectedRoute() != chooseFallbackRoute) {
                        MediaRouter.GlobalMediaRouter.this.selectRouteInternal(chooseFallbackRoute, 2);
                    }
                } else if (MediaRouter.DEBUG) {
                    Log.d("MediaRouter", "A RouteController unrelated to the selected route is released. controller=" + routeController);
                }
            } else {
                Log.w("MR2Provider", "onStop: No matching routeController found. routingController=" + routingController);
            }
        }

        @Override // android.media.MediaRouter2.TransferCallback
        public final void onTransfer(MediaRouter2.RoutingController routingController, MediaRouter2.RoutingController routingController2) {
            MediaRouter.RouteInfo routeInfo;
            MediaRoute2Provider.this.mControllerMap.remove(routingController);
            if (routingController2 == MediaRoute2Provider.this.mMediaRouter2.getSystemController()) {
                MediaRouter.GlobalMediaRouter.Mr2ProviderCallback mr2ProviderCallback = (MediaRouter.GlobalMediaRouter.Mr2ProviderCallback) MediaRoute2Provider.this.mCallback;
                Objects.requireNonNull(mr2ProviderCallback);
                MediaRouter.RouteInfo chooseFallbackRoute = MediaRouter.GlobalMediaRouter.this.chooseFallbackRoute();
                if (MediaRouter.GlobalMediaRouter.this.getSelectedRoute() != chooseFallbackRoute) {
                    MediaRouter.GlobalMediaRouter.this.selectRouteInternal(chooseFallbackRoute, 3);
                    return;
                }
                return;
            }
            List<MediaRoute2Info> selectedRoutes = routingController2.getSelectedRoutes();
            if (selectedRoutes.isEmpty()) {
                Log.w("MR2Provider", "Selected routes are empty. This shouldn't happen.");
                return;
            }
            String id = selectedRoutes.get(0).getId();
            MediaRoute2Provider.this.mControllerMap.put(routingController2, new GroupRouteController(routingController2, id));
            MediaRouter.GlobalMediaRouter.Mr2ProviderCallback mr2ProviderCallback2 = (MediaRouter.GlobalMediaRouter.Mr2ProviderCallback) MediaRoute2Provider.this.mCallback;
            Objects.requireNonNull(mr2ProviderCallback2);
            MediaRouter.GlobalMediaRouter globalMediaRouter = MediaRouter.GlobalMediaRouter.this;
            Objects.requireNonNull(globalMediaRouter);
            Iterator<MediaRouter.RouteInfo> it = globalMediaRouter.mRoutes.iterator();
            while (true) {
                if (!it.hasNext()) {
                    routeInfo = null;
                    break;
                }
                routeInfo = it.next();
                if (routeInfo.getProviderInstance() == MediaRouter.GlobalMediaRouter.this.mMr2Provider && TextUtils.equals(id, routeInfo.mDescriptorId)) {
                    break;
                }
            }
            if (routeInfo == null) {
                MotionLayout$$ExternalSyntheticOutline0.m("onSelectRoute: The target RouteInfo is not found for descriptorId=", id, "MediaRouter");
            } else {
                MediaRouter.GlobalMediaRouter.this.selectRouteInternal(routeInfo, 3);
            }
            MediaRoute2Provider.this.setDynamicRouteDescriptors(routingController2);
        }

        @Override // android.media.MediaRouter2.TransferCallback
        public final void onTransferFailure(MediaRoute2Info mediaRoute2Info) {
            Log.w("MR2Provider", "Transfer failed. requestedRoute=" + mediaRoute2Info);
        }
    }

    public MediaRoute2Provider(Context context, MediaRouter.GlobalMediaRouter.Mr2ProviderCallback mr2ProviderCallback) {
        super(context, null);
        this.mMediaRouter2 = MediaRouter2.getInstance(context);
        this.mCallback = mr2ProviderCallback;
    }

    public final MediaRoute2Info getRouteById(String str) {
        if (str == null) {
            return null;
        }
        Iterator it = this.mRoutes.iterator();
        while (it.hasNext()) {
            MediaRoute2Info mediaRoute2Info = (MediaRoute2Info) it.next();
            if (TextUtils.equals(mediaRoute2Info.getId(), str)) {
                return mediaRoute2Info;
            }
        }
        return null;
    }

    @Override // androidx.mediarouter.media.MediaRouteProvider
    public final MediaRouteProvider.RouteController onCreateRouteController(String str) {
        return new MemberRouteController((String) this.mRouteIdToOriginalRouteIdMap.get(str), null);
    }

    static {
        Log.isLoggable("MR2Provider", 3);
    }

    @Override // androidx.mediarouter.media.MediaRouteProvider
    public final MediaRouteProvider.DynamicGroupRouteController onCreateDynamicGroupRouteController(String str) {
        for (Map.Entry entry : this.mControllerMap.entrySet()) {
            GroupRouteController groupRouteController = (GroupRouteController) entry.getValue();
            if (TextUtils.equals(str, groupRouteController.mInitialMemberRouteId)) {
                return groupRouteController;
            }
        }
        return null;
    }

    @Override // androidx.mediarouter.media.MediaRouteProvider
    public final void onDiscoveryRequestChanged(MediaRouteDiscoveryRequest mediaRouteDiscoveryRequest) {
        int i;
        RouteDiscoveryPreference routeDiscoveryPreference;
        if (MediaRouter.sGlobal == null) {
            i = 0;
        } else {
            MediaRouter.GlobalMediaRouter globalRouter = MediaRouter.getGlobalRouter();
            Objects.requireNonNull(globalRouter);
            i = globalRouter.mCallbackCount;
        }
        if (i > 0) {
            MediaRouter.getGlobalRouter();
            if (mediaRouteDiscoveryRequest == null) {
                mediaRouteDiscoveryRequest = new MediaRouteDiscoveryRequest(MediaRouteSelector.EMPTY, false);
            }
            mediaRouteDiscoveryRequest.ensureSelector();
            ArrayList controlCategories = mediaRouteDiscoveryRequest.mSelector.getControlCategories();
            controlCategories.remove("android.media.intent.category.LIVE_AUDIO");
            MediaRouteSelector.Builder builder = new MediaRouteSelector.Builder();
            builder.addControlCategories(controlCategories);
            MediaRouteSelector build = builder.build();
            boolean isActiveScan = mediaRouteDiscoveryRequest.isActiveScan();
            if (build != null) {
                Bundle bundle = new Bundle();
                bundle.putBundle("selector", build.mBundle);
                bundle.putBoolean("activeScan", isActiveScan);
                MediaRouter2 mediaRouter2 = this.mMediaRouter2;
                MediaRoute2Provider$$ExternalSyntheticLambda0 mediaRoute2Provider$$ExternalSyntheticLambda0 = this.mHandlerExecutor;
                RouteCallback routeCallback = this.mRouteCallback;
                build.ensureControlCategories();
                if (!(!build.mControlCategories.contains(null))) {
                    routeDiscoveryPreference = new RouteDiscoveryPreference.Builder(new ArrayList(), false).build();
                } else {
                    boolean z = bundle.getBoolean("activeScan");
                    ArrayList arrayList = new ArrayList();
                    Iterator it = build.getControlCategories().iterator();
                    while (it.hasNext()) {
                        String str = (String) it.next();
                        Objects.requireNonNull(str);
                        char c = 65535;
                        switch (str.hashCode()) {
                            case -2065577523:
                                if (str.equals("android.media.intent.category.REMOTE_PLAYBACK")) {
                                    c = 0;
                                    break;
                                }
                                break;
                            case 956939050:
                                if (str.equals("android.media.intent.category.LIVE_AUDIO")) {
                                    c = 1;
                                    break;
                                }
                                break;
                            case 975975375:
                                if (str.equals("android.media.intent.category.LIVE_VIDEO")) {
                                    c = 2;
                                    break;
                                }
                                break;
                        }
                        switch (c) {
                            case 0:
                                str = "android.media.route.feature.REMOTE_PLAYBACK";
                                break;
                            case 1:
                                str = "android.media.route.feature.LIVE_AUDIO";
                                break;
                            case 2:
                                str = "android.media.route.feature.LIVE_VIDEO";
                                break;
                        }
                        arrayList.add(str);
                    }
                    routeDiscoveryPreference = new RouteDiscoveryPreference.Builder(arrayList, z).build();
                }
                mediaRouter2.registerRouteCallback(mediaRoute2Provider$$ExternalSyntheticLambda0, routeCallback, routeDiscoveryPreference);
                this.mMediaRouter2.registerTransferCallback(this.mHandlerExecutor, this.mTransferCallback);
                this.mMediaRouter2.registerControllerCallback(this.mHandlerExecutor, this.mControllerCallback);
                return;
            }
            throw new IllegalArgumentException("selector must not be null");
        }
        this.mMediaRouter2.unregisterRouteCallback(this.mRouteCallback);
        this.mMediaRouter2.unregisterTransferCallback(this.mTransferCallback);
        this.mMediaRouter2.unregisterControllerCallback(this.mControllerCallback);
    }

    public final void refreshRoutes() {
        ArrayList arrayList = new ArrayList();
        ArraySet arraySet = new ArraySet();
        for (MediaRoute2Info mediaRoute2Info : this.mMediaRouter2.getRoutes()) {
            if (mediaRoute2Info != null && !arraySet.contains(mediaRoute2Info) && !mediaRoute2Info.isSystemRoute()) {
                arraySet.add(mediaRoute2Info);
                arrayList.add(mediaRoute2Info);
            }
        }
        if (!arrayList.equals(this.mRoutes)) {
            this.mRoutes = arrayList;
            this.mRouteIdToOriginalRouteIdMap.clear();
            Iterator it = this.mRoutes.iterator();
            while (it.hasNext()) {
                MediaRoute2Info mediaRoute2Info2 = (MediaRoute2Info) it.next();
                Bundle extras = mediaRoute2Info2.getExtras();
                if (extras == null || extras.getString("androidx.mediarouter.media.KEY_ORIGINAL_ROUTE_ID") == null) {
                    Log.w("MR2Provider", "Cannot find the original route Id. route=" + mediaRoute2Info2);
                } else {
                    this.mRouteIdToOriginalRouteIdMap.put(mediaRoute2Info2.getId(), extras.getString("androidx.mediarouter.media.KEY_ORIGINAL_ROUTE_ID"));
                }
            }
            ArrayList arrayList2 = new ArrayList();
            Iterator it2 = this.mRoutes.iterator();
            while (it2.hasNext()) {
                MediaRoute2Info mediaRoute2Info3 = (MediaRoute2Info) it2.next();
                MediaRouteDescriptor mediaRouteDescriptor = MediaRouter2Utils.toMediaRouteDescriptor(mediaRoute2Info3);
                if (mediaRoute2Info3 != null) {
                    arrayList2.add(mediaRouteDescriptor);
                }
            }
            ArrayList arrayList3 = null;
            if (!arrayList2.isEmpty()) {
                Iterator it3 = arrayList2.iterator();
                while (it3.hasNext()) {
                    MediaRouteDescriptor mediaRouteDescriptor2 = (MediaRouteDescriptor) it3.next();
                    if (mediaRouteDescriptor2 != null) {
                        if (arrayList3 == null) {
                            arrayList3 = new ArrayList();
                        } else if (arrayList3.contains(mediaRouteDescriptor2)) {
                            throw new IllegalArgumentException("route descriptor already added");
                        }
                        arrayList3.add(mediaRouteDescriptor2);
                    } else {
                        throw new IllegalArgumentException("route must not be null");
                    }
                }
            }
            setDescriptor(new MediaRouteProviderDescriptor(arrayList3, true));
        }
    }

    public final void setDynamicRouteDescriptors(MediaRouter2.RoutingController routingController) {
        int i;
        GroupRouteController groupRouteController = (GroupRouteController) this.mControllerMap.get(routingController);
        if (groupRouteController == null) {
            Log.w("MR2Provider", "setDynamicRouteDescriptors: No matching routeController found. routingController=" + routingController);
            return;
        }
        List<MediaRoute2Info> selectedRoutes = routingController.getSelectedRoutes();
        if (selectedRoutes.isEmpty()) {
            Log.w("MR2Provider", "setDynamicRouteDescriptors: No selected routes. This may happen when the selected routes become invalid.routingController=" + routingController);
            return;
        }
        ArrayList routeIds = MediaRouter2Utils.getRouteIds(selectedRoutes);
        MediaRouteDescriptor mediaRouteDescriptor = MediaRouter2Utils.toMediaRouteDescriptor(selectedRoutes.get(0));
        MediaRouteDescriptor mediaRouteDescriptor2 = null;
        Bundle controlHints = routingController.getControlHints();
        String string = this.mContext.getString(2131952820);
        if (controlHints != null) {
            try {
                String string2 = controlHints.getString("androidx.mediarouter.media.KEY_SESSION_NAME");
                if (!TextUtils.isEmpty(string2)) {
                    string = string2;
                }
                Bundle bundle = controlHints.getBundle("androidx.mediarouter.media.KEY_GROUP_ROUTE");
                if (bundle != null) {
                    mediaRouteDescriptor2 = new MediaRouteDescriptor(bundle);
                }
            } catch (Exception e) {
                Log.w("MR2Provider", "Exception while unparceling control hints.", e);
            }
        }
        if (mediaRouteDescriptor2 == null) {
            MediaRouteDescriptor.Builder builder = new MediaRouteDescriptor.Builder(routingController.getId(), string);
            builder.mBundle.putInt("connectionState", 2);
            builder.mBundle.putInt("playbackType", 1);
            builder.mBundle.putInt("volume", routingController.getVolume());
            builder.mBundle.putInt("volumeMax", routingController.getVolumeMax());
            builder.mBundle.putInt("volumeHandling", routingController.getVolumeHandling());
            Objects.requireNonNull(mediaRouteDescriptor);
            mediaRouteDescriptor.ensureControlFilters();
            builder.addControlFilters(mediaRouteDescriptor.mControlFilters);
            if (!routeIds.isEmpty()) {
                Iterator it = routeIds.iterator();
                while (it.hasNext()) {
                    String str = (String) it.next();
                    if (!TextUtils.isEmpty(str)) {
                        if (builder.mGroupMemberIds == null) {
                            builder.mGroupMemberIds = new ArrayList<>();
                        }
                        if (!builder.mGroupMemberIds.contains(str)) {
                            builder.mGroupMemberIds.add(str);
                        }
                    } else {
                        throw new IllegalArgumentException("groupMemberId must not be empty");
                    }
                }
            }
            mediaRouteDescriptor2 = builder.build();
        }
        ArrayList routeIds2 = MediaRouter2Utils.getRouteIds(routingController.getSelectableRoutes());
        ArrayList routeIds3 = MediaRouter2Utils.getRouteIds(routingController.getDeselectableRoutes());
        MediaRouteProviderDescriptor mediaRouteProviderDescriptor = this.mDescriptor;
        if (mediaRouteProviderDescriptor == null) {
            Log.w("MR2Provider", "setDynamicRouteDescriptors: providerDescriptor is not set.");
            return;
        }
        ArrayList arrayList = new ArrayList();
        List<MediaRouteDescriptor> list = mediaRouteProviderDescriptor.mRoutes;
        if (!list.isEmpty()) {
            for (MediaRouteDescriptor mediaRouteDescriptor3 : list) {
                String id = mediaRouteDescriptor3.getId();
                if (routeIds.contains(id)) {
                    i = 3;
                } else {
                    i = 1;
                }
                arrayList.add(new MediaRouteProvider.DynamicGroupRouteController.DynamicRouteDescriptor(mediaRouteDescriptor3, i, routeIds3.contains(id), routeIds2.contains(id), true));
            }
        }
        groupRouteController.mGroupRouteDescriptor = mediaRouteDescriptor2;
        groupRouteController.notifyDynamicRoutesChanged(mediaRouteDescriptor2, arrayList);
    }

    @Override // androidx.mediarouter.media.MediaRouteProvider
    public final MediaRouteProvider.RouteController onCreateRouteController(String str, String str2) {
        String str3 = (String) this.mRouteIdToOriginalRouteIdMap.get(str);
        for (GroupRouteController groupRouteController : this.mControllerMap.values()) {
            Objects.requireNonNull(groupRouteController);
            MediaRouteDescriptor mediaRouteDescriptor = groupRouteController.mGroupRouteDescriptor;
            if (TextUtils.equals(str2, mediaRouteDescriptor != null ? mediaRouteDescriptor.getId() : groupRouteController.mRoutingController.getId())) {
                return new MemberRouteController(str3, groupRouteController);
            }
        }
        Log.w("MR2Provider", "Could not find the matching GroupRouteController. routeId=" + str + ", routeGroupId=" + str2);
        return new MemberRouteController(str3, null);
    }
}
