package androidx.mediarouter.media;

import android.content.ComponentName;
import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Handler;
import android.os.Message;
import androidx.mediarouter.media.MediaRouter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public abstract class MediaRouteProvider {
    public Callback mCallback;
    public final Context mContext;
    public MediaRouteProviderDescriptor mDescriptor;
    public MediaRouteDiscoveryRequest mDiscoveryRequest;
    public final ProviderHandler mHandler = new ProviderHandler();
    public final ProviderMetadata mMetadata;
    public boolean mPendingDescriptorChange;
    public boolean mPendingDiscoveryRequestChange;

    /* loaded from: classes.dex */
    public static abstract class Callback {
    }

    /* loaded from: classes.dex */
    public static abstract class DynamicGroupRouteController extends RouteController {
        public Executor mExecutor;
        public OnDynamicRoutesChangedListener mListener;
        public final Object mLock = new Object();
        public MediaRouteDescriptor mPendingGroupRoute;
        public ArrayList mPendingRoutes;

        /* loaded from: classes.dex */
        public interface OnDynamicRoutesChangedListener {
        }

        public String getGroupableSelectionTitle() {
            return null;
        }

        public String getTransferableSectionTitle() {
            return null;
        }

        public abstract void onAddMemberRoute(String str);

        public abstract void onRemoveMemberRoute(String str);

        public abstract void onUpdateMemberRoutes(List<String> list);

        /* loaded from: classes.dex */
        public static final class DynamicRouteDescriptor {
            public final boolean mIsGroupable;
            public final boolean mIsTransferable;
            public final boolean mIsUnselectable;
            public final MediaRouteDescriptor mMediaRouteDescriptor;
            public final int mSelectionState;

            public DynamicRouteDescriptor(MediaRouteDescriptor mediaRouteDescriptor, int i, boolean z, boolean z2, boolean z3) {
                this.mMediaRouteDescriptor = mediaRouteDescriptor;
                this.mSelectionState = i;
                this.mIsUnselectable = z;
                this.mIsGroupable = z2;
                this.mIsTransferable = z3;
            }
        }

        public final void notifyDynamicRoutesChanged(final MediaRouteDescriptor mediaRouteDescriptor, final ArrayList arrayList) {
            Objects.requireNonNull(mediaRouteDescriptor, "groupRoute must not be null");
            synchronized (this.mLock) {
                Executor executor = this.mExecutor;
                if (executor != null) {
                    final OnDynamicRoutesChangedListener onDynamicRoutesChangedListener = this.mListener;
                    executor.execute(new Runnable() { // from class: androidx.mediarouter.media.MediaRouteProvider.DynamicGroupRouteController.3
                        @Override // java.lang.Runnable
                        public final void run() {
                            ((MediaRouter.GlobalMediaRouter.AnonymousClass3) onDynamicRoutesChangedListener).onRoutesChanged(DynamicGroupRouteController.this, mediaRouteDescriptor, arrayList);
                        }
                    });
                } else {
                    this.mPendingGroupRoute = mediaRouteDescriptor;
                    this.mPendingRoutes = new ArrayList(arrayList);
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public final class ProviderHandler extends Handler {
        public ProviderHandler() {
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                MediaRouteProvider mediaRouteProvider = MediaRouteProvider.this;
                Objects.requireNonNull(mediaRouteProvider);
                mediaRouteProvider.mPendingDescriptorChange = false;
                Callback callback = mediaRouteProvider.mCallback;
                if (callback != null) {
                    MediaRouteProviderDescriptor mediaRouteProviderDescriptor = mediaRouteProvider.mDescriptor;
                    MediaRouter.GlobalMediaRouter globalMediaRouter = MediaRouter.GlobalMediaRouter.this;
                    Objects.requireNonNull(globalMediaRouter);
                    MediaRouter.ProviderInfo findProviderInfo = globalMediaRouter.findProviderInfo(mediaRouteProvider);
                    if (findProviderInfo != null) {
                        globalMediaRouter.updateProviderContents(findProviderInfo, mediaRouteProviderDescriptor);
                    }
                }
            } else if (i == 2) {
                MediaRouteProvider mediaRouteProvider2 = MediaRouteProvider.this;
                Objects.requireNonNull(mediaRouteProvider2);
                mediaRouteProvider2.mPendingDiscoveryRequestChange = false;
                mediaRouteProvider2.onDiscoveryRequestChanged(mediaRouteProvider2.mDiscoveryRequest);
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class ProviderMetadata {
        public final ComponentName mComponentName;

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("ProviderMetadata{ componentName=");
            m.append(this.mComponentName.flattenToShortString());
            m.append(" }");
            return m.toString();
        }

        public ProviderMetadata(ComponentName componentName) {
            this.mComponentName = componentName;
        }
    }

    public RouteController onCreateRouteController(String str) {
        if (str != null) {
            return null;
        }
        throw new IllegalArgumentException("routeId cannot be null");
    }

    public void onDiscoveryRequestChanged(MediaRouteDiscoveryRequest mediaRouteDiscoveryRequest) {
    }

    public DynamicGroupRouteController onCreateDynamicGroupRouteController(String str) {
        if (str != null) {
            return null;
        }
        throw new IllegalArgumentException("initialMemberRouteId cannot be null.");
    }

    public RouteController onCreateRouteController(String str, String str2) {
        if (str == null) {
            throw new IllegalArgumentException("routeId cannot be null");
        } else if (str2 != null) {
            return onCreateRouteController(str);
        } else {
            throw new IllegalArgumentException("routeGroupId cannot be null");
        }
    }

    public MediaRouteProvider(Context context, ProviderMetadata providerMetadata) {
        if (context != null) {
            this.mContext = context;
            if (providerMetadata == null) {
                this.mMetadata = new ProviderMetadata(new ComponentName(context, getClass()));
            } else {
                this.mMetadata = providerMetadata;
            }
        } else {
            throw new IllegalArgumentException("context must not be null");
        }
    }

    public final void setDescriptor(MediaRouteProviderDescriptor mediaRouteProviderDescriptor) {
        MediaRouter.checkCallingThread();
        if (this.mDescriptor != mediaRouteProviderDescriptor) {
            this.mDescriptor = mediaRouteProviderDescriptor;
            if (!this.mPendingDescriptorChange) {
                this.mPendingDescriptorChange = true;
                this.mHandler.sendEmptyMessage(1);
            }
        }
    }

    public final void setDiscoveryRequest(MediaRouteDiscoveryRequest mediaRouteDiscoveryRequest) {
        MediaRouter.checkCallingThread();
        if (!Objects.equals(this.mDiscoveryRequest, mediaRouteDiscoveryRequest)) {
            this.mDiscoveryRequest = mediaRouteDiscoveryRequest;
            if (!this.mPendingDiscoveryRequestChange) {
                this.mPendingDiscoveryRequestChange = true;
                this.mHandler.sendEmptyMessage(2);
            }
        }
    }

    /* loaded from: classes.dex */
    public static abstract class RouteController {
        public void onRelease() {
        }

        public void onSelect() {
        }

        public void onSetVolume(int i) {
        }

        @Deprecated
        public void onUnselect() {
        }

        public void onUpdateVolume(int i) {
        }

        public void onUnselect(int i) {
            onUnselect();
        }
    }
}
