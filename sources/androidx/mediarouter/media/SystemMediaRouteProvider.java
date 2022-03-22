package androidx.mediarouter.media;

import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.media.MediaRouter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Display;
import androidx.mediarouter.media.MediaRouteDescriptor;
import androidx.mediarouter.media.MediaRouteProvider;
import androidx.mediarouter.media.MediaRouter;
import androidx.mediarouter.media.SystemMediaRouteProvider;
import com.android.systemui.theme.ThemeOverlayApplier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class SystemMediaRouteProvider extends MediaRouteProvider {

    /* loaded from: classes.dex */
    public static class JellybeanImpl extends SystemMediaRouteProvider implements MediaRouterJellybean$Callback, MediaRouterJellybean$VolumeCallback {
        public static final ArrayList<IntentFilter> LIVE_AUDIO_CONTROL_FILTERS;
        public static final ArrayList<IntentFilter> LIVE_VIDEO_CONTROL_FILTERS;
        public boolean mActiveScan;
        public final Object mCallbackObj;
        public boolean mCallbackRegistered;
        public int mRouteTypes;
        public final Object mRouterObj;
        public final SyncCallback mSyncCallback;
        public final MediaRouter.RouteCategory mUserRouteCategoryObj;
        public final MediaRouterJellybean$VolumeCallbackProxy mVolumeCallbackObj;
        public final ArrayList<SystemRouteRecord> mSystemRouteRecords = new ArrayList<>();
        public final ArrayList<UserRouteRecord> mUserRouteRecords = new ArrayList<>();

        /* loaded from: classes.dex */
        public static final class SystemRouteController extends MediaRouteProvider.RouteController {
            public final Object mRouteObj;

            @Override // androidx.mediarouter.media.MediaRouteProvider.RouteController
            public final void onSetVolume(int i) {
                ((MediaRouter.RouteInfo) this.mRouteObj).requestSetVolume(i);
            }

            @Override // androidx.mediarouter.media.MediaRouteProvider.RouteController
            public final void onUpdateVolume(int i) {
                ((MediaRouter.RouteInfo) this.mRouteObj).requestUpdateVolume(i);
            }

            public SystemRouteController(Object obj) {
                this.mRouteObj = obj;
            }
        }

        public MediaRouter.RouteInfo getDefaultRoute() {
            throw null;
        }

        @Override // androidx.mediarouter.media.MediaRouteProvider
        public final void onDiscoveryRequestChanged(MediaRouteDiscoveryRequest mediaRouteDiscoveryRequest) {
            boolean z;
            int i = 0;
            if (mediaRouteDiscoveryRequest != null) {
                mediaRouteDiscoveryRequest.ensureSelector();
                ArrayList controlCategories = mediaRouteDiscoveryRequest.mSelector.getControlCategories();
                int size = controlCategories.size();
                int i2 = 0;
                while (i < size) {
                    String str = (String) controlCategories.get(i);
                    if (str.equals("android.media.intent.category.LIVE_AUDIO")) {
                        i2 |= 1;
                    } else if (str.equals("android.media.intent.category.LIVE_VIDEO")) {
                        i2 |= 2;
                    } else {
                        i2 |= 8388608;
                    }
                    i++;
                }
                z = mediaRouteDiscoveryRequest.isActiveScan();
                i = i2;
            } else {
                z = false;
            }
            if (this.mRouteTypes != i || this.mActiveScan != z) {
                this.mRouteTypes = i;
                this.mActiveScan = z;
                updateSystemRoutes();
            }
        }

        @Override // androidx.mediarouter.media.MediaRouterJellybean$Callback
        public final void onRouteGrouped() {
        }

        @Override // androidx.mediarouter.media.MediaRouterJellybean$Callback
        public final void onRouteUngrouped() {
        }

        @Override // androidx.mediarouter.media.MediaRouterJellybean$Callback
        public final void onRouteUnselected() {
        }

        public void selectRoute(Object obj) {
            throw null;
        }

        public void updateCallback() {
            throw null;
        }

        /* loaded from: classes.dex */
        public static final class SystemRouteRecord {
            public MediaRouteDescriptor mRouteDescriptor;
            public final String mRouteDescriptorId;
            public final Object mRouteObj;

            public SystemRouteRecord(Object obj, String str) {
                this.mRouteObj = obj;
                this.mRouteDescriptorId = str;
            }
        }

        /* loaded from: classes.dex */
        public static final class UserRouteRecord {
            public final MediaRouter.RouteInfo mRoute;
            public final Object mRouteObj;

            public UserRouteRecord(MediaRouter.RouteInfo routeInfo, MediaRouter.UserRouteInfo userRouteInfo) {
                this.mRoute = routeInfo;
                this.mRouteObj = userRouteInfo;
            }
        }

        static {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addCategory("android.media.intent.category.LIVE_AUDIO");
            ArrayList<IntentFilter> arrayList = new ArrayList<>();
            LIVE_AUDIO_CONTROL_FILTERS = arrayList;
            arrayList.add(intentFilter);
            IntentFilter intentFilter2 = new IntentFilter();
            intentFilter2.addCategory("android.media.intent.category.LIVE_VIDEO");
            ArrayList<IntentFilter> arrayList2 = new ArrayList<>();
            LIVE_VIDEO_CONTROL_FILTERS = arrayList2;
            arrayList2.add(intentFilter2);
        }

        public static UserRouteRecord getUserRouteRecord(Object obj) {
            Object tag = ((MediaRouter.RouteInfo) obj).getTag();
            if (tag instanceof UserRouteRecord) {
                return (UserRouteRecord) tag;
            }
            return null;
        }

        public final int findSystemRouteRecord(Object obj) {
            int size = this.mSystemRouteRecords.size();
            for (int i = 0; i < size; i++) {
                if (this.mSystemRouteRecords.get(i).mRouteObj == obj) {
                    return i;
                }
            }
            return -1;
        }

        public final int findSystemRouteRecordByDescriptorId(String str) {
            int size = this.mSystemRouteRecords.size();
            for (int i = 0; i < size; i++) {
                if (this.mSystemRouteRecords.get(i).mRouteDescriptorId.equals(str)) {
                    return i;
                }
            }
            return -1;
        }

        public final int findUserRouteRecord(MediaRouter.RouteInfo routeInfo) {
            int size = this.mUserRouteRecords.size();
            for (int i = 0; i < size; i++) {
                if (this.mUserRouteRecords.get(i).mRoute == routeInfo) {
                    return i;
                }
            }
            return -1;
        }

        public void onBuildSystemRouteDescriptor(SystemRouteRecord systemRouteRecord, MediaRouteDescriptor.Builder builder) {
            int supportedTypes = ((MediaRouter.RouteInfo) systemRouteRecord.mRouteObj).getSupportedTypes();
            if ((supportedTypes & 1) != 0) {
                builder.addControlFilters(LIVE_AUDIO_CONTROL_FILTERS);
            }
            if ((supportedTypes & 2) != 0) {
                builder.addControlFilters(LIVE_VIDEO_CONTROL_FILTERS);
            }
            builder.mBundle.putInt("playbackType", ((MediaRouter.RouteInfo) systemRouteRecord.mRouteObj).getPlaybackType());
            builder.mBundle.putInt("playbackStream", ((MediaRouter.RouteInfo) systemRouteRecord.mRouteObj).getPlaybackStream());
            builder.mBundle.putInt("volume", ((MediaRouter.RouteInfo) systemRouteRecord.mRouteObj).getVolume());
            builder.mBundle.putInt("volumeMax", ((MediaRouter.RouteInfo) systemRouteRecord.mRouteObj).getVolumeMax());
            builder.mBundle.putInt("volumeHandling", ((MediaRouter.RouteInfo) systemRouteRecord.mRouteObj).getVolumeHandling());
        }

        @Override // androidx.mediarouter.media.MediaRouterJellybean$Callback
        public final void onRouteSelected(Object obj) {
            MediaRouter.RouteInfo findRouteByDescriptorId;
            if (obj == ((android.media.MediaRouter) this.mRouterObj).getSelectedRoute(8388611)) {
                UserRouteRecord userRouteRecord = getUserRouteRecord(obj);
                if (userRouteRecord != null) {
                    userRouteRecord.mRoute.select();
                    return;
                }
                int findSystemRouteRecord = findSystemRouteRecord(obj);
                if (findSystemRouteRecord >= 0) {
                    SyncCallback syncCallback = this.mSyncCallback;
                    String str = this.mSystemRouteRecords.get(findSystemRouteRecord).mRouteDescriptorId;
                    MediaRouter.GlobalMediaRouter globalMediaRouter = (MediaRouter.GlobalMediaRouter) syncCallback;
                    Objects.requireNonNull(globalMediaRouter);
                    globalMediaRouter.mCallbackHandler.removeMessages(262);
                    MediaRouter.ProviderInfo findProviderInfo = globalMediaRouter.findProviderInfo(globalMediaRouter.mSystemProvider);
                    if (findProviderInfo != null && (findRouteByDescriptorId = findProviderInfo.findRouteByDescriptorId(str)) != null) {
                        findRouteByDescriptorId.select();
                    }
                }
            }
        }

        public final void publishRoutes() {
            int size = this.mSystemRouteRecords.size();
            ArrayList arrayList = null;
            for (int i = 0; i < size; i++) {
                MediaRouteDescriptor mediaRouteDescriptor = this.mSystemRouteRecords.get(i).mRouteDescriptor;
                if (mediaRouteDescriptor != null) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    } else if (arrayList.contains(mediaRouteDescriptor)) {
                        throw new IllegalArgumentException("route descriptor already added");
                    }
                    arrayList.add(mediaRouteDescriptor);
                } else {
                    throw new IllegalArgumentException("route must not be null");
                }
            }
            setDescriptor(new MediaRouteProviderDescriptor(arrayList, false));
        }

        public void updateUserRouteProperties(UserRouteRecord userRouteRecord) {
            Object obj = userRouteRecord.mRouteObj;
            MediaRouter.RouteInfo routeInfo = userRouteRecord.mRoute;
            Objects.requireNonNull(routeInfo);
            ((MediaRouter.UserRouteInfo) obj).setName(routeInfo.mName);
            Object obj2 = userRouteRecord.mRouteObj;
            MediaRouter.RouteInfo routeInfo2 = userRouteRecord.mRoute;
            Objects.requireNonNull(routeInfo2);
            ((MediaRouter.UserRouteInfo) obj2).setPlaybackType(routeInfo2.mPlaybackType);
            Object obj3 = userRouteRecord.mRouteObj;
            MediaRouter.RouteInfo routeInfo3 = userRouteRecord.mRoute;
            Objects.requireNonNull(routeInfo3);
            ((MediaRouter.UserRouteInfo) obj3).setPlaybackStream(routeInfo3.mPlaybackStream);
            Object obj4 = userRouteRecord.mRouteObj;
            MediaRouter.RouteInfo routeInfo4 = userRouteRecord.mRoute;
            Objects.requireNonNull(routeInfo4);
            ((MediaRouter.UserRouteInfo) obj4).setVolume(routeInfo4.mVolume);
            Object obj5 = userRouteRecord.mRouteObj;
            MediaRouter.RouteInfo routeInfo5 = userRouteRecord.mRoute;
            Objects.requireNonNull(routeInfo5);
            ((MediaRouter.UserRouteInfo) obj5).setVolumeMax(routeInfo5.mVolumeMax);
            ((MediaRouter.UserRouteInfo) userRouteRecord.mRouteObj).setVolumeHandling(userRouteRecord.mRoute.getVolumeHandling());
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [androidx.mediarouter.media.MediaRouterJellybean$VolumeCallbackProxy] */
        public JellybeanImpl(Context context, MediaRouter.GlobalMediaRouter globalMediaRouter) {
            super(context);
            this.mSyncCallback = globalMediaRouter;
            Object systemService = context.getSystemService("media_router");
            this.mRouterObj = systemService;
            final JellybeanMr1Impl jellybeanMr1Impl = (JellybeanMr1Impl) this;
            this.mCallbackObj = new MediaRouterJellybean$CallbackProxy<T>(jellybeanMr1Impl) { // from class: androidx.mediarouter.media.MediaRouterJellybeanMr1$CallbackProxy
                @Override // android.media.MediaRouter.Callback
                public final void onRoutePresentationDisplayChanged(android.media.MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
                    ((MediaRouterJellybeanMr1$Callback) this.mCallback).onRoutePresentationDisplayChanged(routeInfo);
                }
            };
            this.mVolumeCallbackObj = new MediaRouter.VolumeCallback(jellybeanMr1Impl) { // from class: androidx.mediarouter.media.MediaRouterJellybean$VolumeCallbackProxy
                public final T mCallback;

                @Override // android.media.MediaRouter.VolumeCallback
                public final void onVolumeSetRequest(MediaRouter.RouteInfo routeInfo, int i) {
                    Objects.requireNonNull((SystemMediaRouteProvider.JellybeanImpl) this.mCallback);
                    SystemMediaRouteProvider.JellybeanImpl.UserRouteRecord userRouteRecord = SystemMediaRouteProvider.JellybeanImpl.getUserRouteRecord(routeInfo);
                    if (userRouteRecord != null) {
                        userRouteRecord.mRoute.requestSetVolume(i);
                    }
                }

                @Override // android.media.MediaRouter.VolumeCallback
                public final void onVolumeUpdateRequest(MediaRouter.RouteInfo routeInfo, int i) {
                    Objects.requireNonNull((SystemMediaRouteProvider.JellybeanImpl) this.mCallback);
                    SystemMediaRouteProvider.JellybeanImpl.UserRouteRecord userRouteRecord = SystemMediaRouteProvider.JellybeanImpl.getUserRouteRecord(routeInfo);
                    if (userRouteRecord != null) {
                        userRouteRecord.mRoute.requestUpdateVolume(i);
                    }
                }

                {
                    this.mCallback = jellybeanMr1Impl;
                }
            };
            this.mUserRouteCategoryObj = ((android.media.MediaRouter) systemService).createRouteCategory((CharSequence) context.getResources().getString(2131952824), false);
            updateSystemRoutes();
        }

        public final boolean addSystemRouteNoPublish(Object obj) {
            boolean z;
            String str;
            String format;
            String str2;
            if (getUserRouteRecord(obj) != null || findSystemRouteRecord(obj) >= 0) {
                return false;
            }
            if (getDefaultRoute() == obj) {
                z = true;
            } else {
                z = false;
            }
            String str3 = "";
            if (z) {
                str = "DEFAULT_ROUTE";
            } else {
                Locale locale = Locale.US;
                Object[] objArr = new Object[1];
                CharSequence name = ((MediaRouter.RouteInfo) obj).getName(this.mContext);
                if (name != null) {
                    str2 = name.toString();
                } else {
                    str2 = str3;
                }
                objArr[0] = Integer.valueOf(str2.hashCode());
                str = String.format(locale, "ROUTE_%08x", objArr);
            }
            if (findSystemRouteRecordByDescriptorId(str) >= 0) {
                int i = 2;
                while (true) {
                    format = String.format(Locale.US, "%s_%d", str, Integer.valueOf(i));
                    if (findSystemRouteRecordByDescriptorId(format) < 0) {
                        break;
                    }
                    i++;
                }
                str = format;
            }
            SystemRouteRecord systemRouteRecord = new SystemRouteRecord(obj, str);
            CharSequence name2 = ((MediaRouter.RouteInfo) obj).getName(this.mContext);
            if (name2 != null) {
                str3 = name2.toString();
            }
            MediaRouteDescriptor.Builder builder = new MediaRouteDescriptor.Builder(str, str3);
            onBuildSystemRouteDescriptor(systemRouteRecord, builder);
            systemRouteRecord.mRouteDescriptor = builder.build();
            this.mSystemRouteRecords.add(systemRouteRecord);
            return true;
        }

        @Override // androidx.mediarouter.media.MediaRouteProvider
        public final MediaRouteProvider.RouteController onCreateRouteController(String str) {
            int findSystemRouteRecordByDescriptorId = findSystemRouteRecordByDescriptorId(str);
            if (findSystemRouteRecordByDescriptorId >= 0) {
                return new SystemRouteController(this.mSystemRouteRecords.get(findSystemRouteRecordByDescriptorId).mRouteObj);
            }
            return null;
        }

        @Override // androidx.mediarouter.media.MediaRouterJellybean$Callback
        public final void onRouteAdded(Object obj) {
            if (addSystemRouteNoPublish(obj)) {
                publishRoutes();
            }
        }

        @Override // androidx.mediarouter.media.MediaRouterJellybean$Callback
        public final void onRouteChanged(Object obj) {
            int findSystemRouteRecord;
            String str;
            if (getUserRouteRecord(obj) == null && (findSystemRouteRecord = findSystemRouteRecord(obj)) >= 0) {
                SystemRouteRecord systemRouteRecord = this.mSystemRouteRecords.get(findSystemRouteRecord);
                String str2 = systemRouteRecord.mRouteDescriptorId;
                CharSequence name = ((MediaRouter.RouteInfo) systemRouteRecord.mRouteObj).getName(this.mContext);
                if (name != null) {
                    str = name.toString();
                } else {
                    str = "";
                }
                MediaRouteDescriptor.Builder builder = new MediaRouteDescriptor.Builder(str2, str);
                onBuildSystemRouteDescriptor(systemRouteRecord, builder);
                systemRouteRecord.mRouteDescriptor = builder.build();
                publishRoutes();
            }
        }

        @Override // androidx.mediarouter.media.MediaRouterJellybean$Callback
        public final void onRouteRemoved(Object obj) {
            int findSystemRouteRecord;
            if (getUserRouteRecord(obj) == null && (findSystemRouteRecord = findSystemRouteRecord(obj)) >= 0) {
                this.mSystemRouteRecords.remove(findSystemRouteRecord);
                publishRoutes();
            }
        }

        @Override // androidx.mediarouter.media.MediaRouterJellybean$Callback
        public final void onRouteVolumeChanged(Object obj) {
            int findSystemRouteRecord;
            ArrayList<String> arrayList;
            if (getUserRouteRecord(obj) == null && (findSystemRouteRecord = findSystemRouteRecord(obj)) >= 0) {
                SystemRouteRecord systemRouteRecord = this.mSystemRouteRecords.get(findSystemRouteRecord);
                int volume = ((MediaRouter.RouteInfo) obj).getVolume();
                MediaRouteDescriptor mediaRouteDescriptor = systemRouteRecord.mRouteDescriptor;
                Objects.requireNonNull(mediaRouteDescriptor);
                if (volume != mediaRouteDescriptor.mBundle.getInt("volume")) {
                    MediaRouteDescriptor mediaRouteDescriptor2 = systemRouteRecord.mRouteDescriptor;
                    if (mediaRouteDescriptor2 != null) {
                        Bundle bundle = new Bundle(mediaRouteDescriptor2.mBundle);
                        ArrayList<? extends Parcelable> arrayList2 = null;
                        if (!mediaRouteDescriptor2.getGroupMemberIds().isEmpty()) {
                            arrayList = new ArrayList<>(mediaRouteDescriptor2.getGroupMemberIds());
                        } else {
                            arrayList = null;
                        }
                        mediaRouteDescriptor2.ensureControlFilters();
                        if (!mediaRouteDescriptor2.mControlFilters.isEmpty()) {
                            arrayList2 = new ArrayList<>(mediaRouteDescriptor2.mControlFilters);
                        }
                        bundle.putInt("volume", volume);
                        if (arrayList2 != null) {
                            bundle.putParcelableArrayList("controlFilters", arrayList2);
                        }
                        if (arrayList != null) {
                            bundle.putStringArrayList("groupMemberIds", arrayList);
                        }
                        systemRouteRecord.mRouteDescriptor = new MediaRouteDescriptor(bundle);
                        publishRoutes();
                        return;
                    }
                    throw new IllegalArgumentException("descriptor must not be null");
                }
            }
        }

        public final void onSyncRouteAdded(MediaRouter.RouteInfo routeInfo) {
            if (routeInfo.getProviderInstance() != this) {
                MediaRouter.UserRouteInfo createUserRoute = ((android.media.MediaRouter) this.mRouterObj).createUserRoute(this.mUserRouteCategoryObj);
                UserRouteRecord userRouteRecord = new UserRouteRecord(routeInfo, createUserRoute);
                createUserRoute.setTag(userRouteRecord);
                createUserRoute.setVolumeCallback(this.mVolumeCallbackObj);
                updateUserRouteProperties(userRouteRecord);
                this.mUserRouteRecords.add(userRouteRecord);
                ((android.media.MediaRouter) this.mRouterObj).addUserRoute(createUserRoute);
                return;
            }
            int findSystemRouteRecord = findSystemRouteRecord(((android.media.MediaRouter) this.mRouterObj).getSelectedRoute(8388611));
            if (findSystemRouteRecord >= 0 && this.mSystemRouteRecords.get(findSystemRouteRecord).mRouteDescriptorId.equals(routeInfo.mDescriptorId)) {
                routeInfo.select();
            }
        }

        public final void onSyncRouteRemoved(MediaRouter.RouteInfo routeInfo) {
            int findUserRouteRecord;
            if (routeInfo.getProviderInstance() != this && (findUserRouteRecord = findUserRouteRecord(routeInfo)) >= 0) {
                UserRouteRecord remove = this.mUserRouteRecords.remove(findUserRouteRecord);
                ((MediaRouter.RouteInfo) remove.mRouteObj).setTag(null);
                ((MediaRouter.UserRouteInfo) remove.mRouteObj).setVolumeCallback(null);
                ((android.media.MediaRouter) this.mRouterObj).removeUserRoute((MediaRouter.UserRouteInfo) remove.mRouteObj);
            }
        }

        public final void onSyncRouteSelected(MediaRouter.RouteInfo routeInfo) {
            if (routeInfo.isSelected()) {
                if (routeInfo.getProviderInstance() != this) {
                    int findUserRouteRecord = findUserRouteRecord(routeInfo);
                    if (findUserRouteRecord >= 0) {
                        selectRoute(this.mUserRouteRecords.get(findUserRouteRecord).mRouteObj);
                        return;
                    }
                    return;
                }
                int findSystemRouteRecordByDescriptorId = findSystemRouteRecordByDescriptorId(routeInfo.mDescriptorId);
                if (findSystemRouteRecordByDescriptorId >= 0) {
                    selectRoute(this.mSystemRouteRecords.get(findSystemRouteRecordByDescriptorId).mRouteObj);
                }
            }
        }

        public final void updateSystemRoutes() {
            updateCallback();
            android.media.MediaRouter mediaRouter = (android.media.MediaRouter) this.mRouterObj;
            int routeCount = mediaRouter.getRouteCount();
            ArrayList arrayList = new ArrayList(routeCount);
            boolean z = false;
            for (int i = 0; i < routeCount; i++) {
                arrayList.add(mediaRouter.getRouteAt(i));
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                z |= addSystemRouteNoPublish(it.next());
            }
            if (z) {
                publishRoutes();
            }
        }
    }

    /* loaded from: classes.dex */
    public static class JellybeanMr2Impl extends JellybeanMr1Impl {
        @Override // androidx.mediarouter.media.SystemMediaRouteProvider.JellybeanImpl
        public final MediaRouter.RouteInfo getDefaultRoute() {
            return ((android.media.MediaRouter) this.mRouterObj).getDefaultRoute();
        }

        @Override // androidx.mediarouter.media.SystemMediaRouteProvider.JellybeanMr1Impl
        public final boolean isConnecting(JellybeanImpl.SystemRouteRecord systemRouteRecord) {
            return ((MediaRouter.RouteInfo) systemRouteRecord.mRouteObj).isConnecting();
        }

        @Override // androidx.mediarouter.media.SystemMediaRouteProvider.JellybeanImpl
        public final void selectRoute(Object obj) {
            ((android.media.MediaRouter) this.mRouterObj).selectRoute(8388611, (MediaRouter.RouteInfo) obj);
        }

        @Override // androidx.mediarouter.media.SystemMediaRouteProvider.JellybeanImpl
        public final void updateCallback() {
            if (this.mCallbackRegistered) {
                ((android.media.MediaRouter) this.mRouterObj).removeCallback((MediaRouter.Callback) this.mCallbackObj);
            }
            this.mCallbackRegistered = true;
            Object obj = this.mRouterObj;
            android.media.MediaRouter mediaRouter = (android.media.MediaRouter) obj;
            mediaRouter.addCallback(this.mRouteTypes, (MediaRouter.Callback) this.mCallbackObj, (this.mActiveScan ? 1 : 0) | 2);
        }

        @Override // androidx.mediarouter.media.SystemMediaRouteProvider.JellybeanMr1Impl, androidx.mediarouter.media.SystemMediaRouteProvider.JellybeanImpl
        public void onBuildSystemRouteDescriptor(JellybeanImpl.SystemRouteRecord systemRouteRecord, MediaRouteDescriptor.Builder builder) {
            super.onBuildSystemRouteDescriptor(systemRouteRecord, builder);
            CharSequence description = ((MediaRouter.RouteInfo) systemRouteRecord.mRouteObj).getDescription();
            if (description != null) {
                builder.mBundle.putString("status", description.toString());
            }
        }

        @Override // androidx.mediarouter.media.SystemMediaRouteProvider.JellybeanImpl
        public final void updateUserRouteProperties(JellybeanImpl.UserRouteRecord userRouteRecord) {
            super.updateUserRouteProperties(userRouteRecord);
            Object obj = userRouteRecord.mRouteObj;
            MediaRouter.RouteInfo routeInfo = userRouteRecord.mRoute;
            Objects.requireNonNull(routeInfo);
            ((MediaRouter.UserRouteInfo) obj).setDescription(routeInfo.mDescription);
        }

        public JellybeanMr2Impl(Context context, MediaRouter.GlobalMediaRouter globalMediaRouter) {
            super(context, globalMediaRouter);
        }
    }

    /* loaded from: classes.dex */
    public interface SyncCallback {
    }

    /* loaded from: classes.dex */
    public static class Api24Impl extends JellybeanMr2Impl {
        @Override // androidx.mediarouter.media.SystemMediaRouteProvider.JellybeanMr2Impl, androidx.mediarouter.media.SystemMediaRouteProvider.JellybeanMr1Impl, androidx.mediarouter.media.SystemMediaRouteProvider.JellybeanImpl
        public final void onBuildSystemRouteDescriptor(JellybeanImpl.SystemRouteRecord systemRouteRecord, MediaRouteDescriptor.Builder builder) {
            super.onBuildSystemRouteDescriptor(systemRouteRecord, builder);
            builder.mBundle.putInt("deviceType", ((MediaRouter.RouteInfo) systemRouteRecord.mRouteObj).getDeviceType());
        }

        public Api24Impl(Context context, MediaRouter.GlobalMediaRouter globalMediaRouter) {
            super(context, globalMediaRouter);
        }
    }

    /* loaded from: classes.dex */
    public static class JellybeanMr1Impl extends JellybeanImpl implements MediaRouterJellybeanMr1$Callback {
        public boolean isConnecting(JellybeanImpl.SystemRouteRecord systemRouteRecord) {
            throw null;
        }

        @Override // androidx.mediarouter.media.SystemMediaRouteProvider.JellybeanImpl
        public void onBuildSystemRouteDescriptor(JellybeanImpl.SystemRouteRecord systemRouteRecord, MediaRouteDescriptor.Builder builder) {
            Display display;
            super.onBuildSystemRouteDescriptor(systemRouteRecord, builder);
            if (!((MediaRouter.RouteInfo) systemRouteRecord.mRouteObj).isEnabled()) {
                builder.mBundle.putBoolean("enabled", false);
            }
            if (isConnecting(systemRouteRecord)) {
                builder.mBundle.putInt("connectionState", 1);
            }
            try {
                display = ((MediaRouter.RouteInfo) systemRouteRecord.mRouteObj).getPresentationDisplay();
            } catch (NoSuchMethodError e) {
                Log.w("MediaRouterJellybeanMr1", "Cannot get presentation display for the route.", e);
                display = null;
            }
            if (display != null) {
                builder.mBundle.putInt("presentationDisplayId", display.getDisplayId());
            }
        }

        @Override // androidx.mediarouter.media.MediaRouterJellybeanMr1$Callback
        public final void onRoutePresentationDisplayChanged(Object obj) {
            Display display;
            int i;
            ArrayList<String> arrayList;
            int findSystemRouteRecord = findSystemRouteRecord(obj);
            if (findSystemRouteRecord >= 0) {
                JellybeanImpl.SystemRouteRecord systemRouteRecord = this.mSystemRouteRecords.get(findSystemRouteRecord);
                ArrayList<? extends Parcelable> arrayList2 = null;
                try {
                    display = ((MediaRouter.RouteInfo) obj).getPresentationDisplay();
                } catch (NoSuchMethodError e) {
                    Log.w("MediaRouterJellybeanMr1", "Cannot get presentation display for the route.", e);
                    display = null;
                }
                if (display != null) {
                    i = display.getDisplayId();
                } else {
                    i = -1;
                }
                MediaRouteDescriptor mediaRouteDescriptor = systemRouteRecord.mRouteDescriptor;
                Objects.requireNonNull(mediaRouteDescriptor);
                if (i != mediaRouteDescriptor.mBundle.getInt("presentationDisplayId", -1)) {
                    MediaRouteDescriptor mediaRouteDescriptor2 = systemRouteRecord.mRouteDescriptor;
                    if (mediaRouteDescriptor2 != null) {
                        Bundle bundle = new Bundle(mediaRouteDescriptor2.mBundle);
                        if (!mediaRouteDescriptor2.getGroupMemberIds().isEmpty()) {
                            arrayList = new ArrayList<>(mediaRouteDescriptor2.getGroupMemberIds());
                        } else {
                            arrayList = null;
                        }
                        mediaRouteDescriptor2.ensureControlFilters();
                        if (!mediaRouteDescriptor2.mControlFilters.isEmpty()) {
                            arrayList2 = new ArrayList<>(mediaRouteDescriptor2.mControlFilters);
                        }
                        bundle.putInt("presentationDisplayId", i);
                        if (arrayList2 != null) {
                            bundle.putParcelableArrayList("controlFilters", arrayList2);
                        }
                        if (arrayList != null) {
                            bundle.putStringArrayList("groupMemberIds", arrayList);
                        }
                        systemRouteRecord.mRouteDescriptor = new MediaRouteDescriptor(bundle);
                        publishRoutes();
                        return;
                    }
                    throw new IllegalArgumentException("descriptor must not be null");
                }
            }
        }

        public JellybeanMr1Impl(Context context, MediaRouter.GlobalMediaRouter globalMediaRouter) {
            super(context, globalMediaRouter);
        }
    }

    public SystemMediaRouteProvider(Context context) {
        super(context, new MediaRouteProvider.ProviderMetadata(new ComponentName(ThemeOverlayApplier.ANDROID_PACKAGE, SystemMediaRouteProvider.class.getName())));
    }
}
