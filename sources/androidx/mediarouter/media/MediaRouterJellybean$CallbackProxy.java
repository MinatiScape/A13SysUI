package androidx.mediarouter.media;

import android.media.MediaRouter;
import androidx.mediarouter.media.MediaRouterJellybean$Callback;
/* loaded from: classes.dex */
public class MediaRouterJellybean$CallbackProxy<T extends MediaRouterJellybean$Callback> extends MediaRouter.Callback {
    public final T mCallback;

    @Override // android.media.MediaRouter.Callback
    public final void onRouteAdded(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
        this.mCallback.onRouteAdded(routeInfo);
    }

    @Override // android.media.MediaRouter.Callback
    public final void onRouteChanged(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
        this.mCallback.onRouteChanged(routeInfo);
    }

    @Override // android.media.MediaRouter.Callback
    public final void onRouteGrouped(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo, MediaRouter.RouteGroup routeGroup, int i) {
        this.mCallback.onRouteGrouped();
    }

    @Override // android.media.MediaRouter.Callback
    public final void onRouteRemoved(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
        this.mCallback.onRouteRemoved(routeInfo);
    }

    @Override // android.media.MediaRouter.Callback
    public final void onRouteSelected(MediaRouter mediaRouter, int i, MediaRouter.RouteInfo routeInfo) {
        this.mCallback.onRouteSelected(routeInfo);
    }

    @Override // android.media.MediaRouter.Callback
    public final void onRouteUngrouped(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo, MediaRouter.RouteGroup routeGroup) {
        this.mCallback.onRouteUngrouped();
    }

    @Override // android.media.MediaRouter.Callback
    public final void onRouteUnselected(MediaRouter mediaRouter, int i, MediaRouter.RouteInfo routeInfo) {
        this.mCallback.onRouteUnselected();
    }

    @Override // android.media.MediaRouter.Callback
    public final void onRouteVolumeChanged(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
        this.mCallback.onRouteVolumeChanged(routeInfo);
    }

    public MediaRouterJellybean$CallbackProxy(T t) {
        this.mCallback = t;
    }
}
