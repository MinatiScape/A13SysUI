package androidx.mediarouter.media;

import android.os.Bundle;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MediaRouteDiscoveryRequest {
    public final Bundle mBundle;
    public MediaRouteSelector mSelector;

    public final void ensureSelector() {
        if (this.mSelector == null) {
            Bundle bundle = this.mBundle.getBundle("selector");
            MediaRouteSelector mediaRouteSelector = MediaRouteSelector.EMPTY;
            MediaRouteSelector mediaRouteSelector2 = null;
            if (bundle != null) {
                mediaRouteSelector2 = new MediaRouteSelector(bundle, null);
            }
            this.mSelector = mediaRouteSelector2;
            if (mediaRouteSelector2 == null) {
                this.mSelector = MediaRouteSelector.EMPTY;
            }
        }
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof MediaRouteDiscoveryRequest)) {
            return false;
        }
        MediaRouteDiscoveryRequest mediaRouteDiscoveryRequest = (MediaRouteDiscoveryRequest) obj;
        ensureSelector();
        MediaRouteSelector mediaRouteSelector = this.mSelector;
        Objects.requireNonNull(mediaRouteDiscoveryRequest);
        mediaRouteDiscoveryRequest.ensureSelector();
        if (!mediaRouteSelector.equals(mediaRouteDiscoveryRequest.mSelector) || isActiveScan() != mediaRouteDiscoveryRequest.isActiveScan()) {
            return false;
        }
        return true;
    }

    public final boolean isActiveScan() {
        return this.mBundle.getBoolean("activeScan");
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DiscoveryRequest{ selector=");
        ensureSelector();
        sb.append(this.mSelector);
        sb.append(", activeScan=");
        sb.append(isActiveScan());
        sb.append(", isValid=");
        ensureSelector();
        MediaRouteSelector mediaRouteSelector = this.mSelector;
        Objects.requireNonNull(mediaRouteSelector);
        mediaRouteSelector.ensureControlCategories();
        sb.append(!mediaRouteSelector.mControlCategories.contains(null));
        sb.append(" }");
        return sb.toString();
    }

    public MediaRouteDiscoveryRequest(MediaRouteSelector mediaRouteSelector, boolean z) {
        if (mediaRouteSelector != null) {
            Bundle bundle = new Bundle();
            this.mBundle = bundle;
            this.mSelector = mediaRouteSelector;
            bundle.putBundle("selector", mediaRouteSelector.mBundle);
            bundle.putBoolean("activeScan", z);
            return;
        }
        throw new IllegalArgumentException("selector must not be null");
    }

    public final int hashCode() {
        ensureSelector();
        return isActiveScan() ^ this.mSelector.hashCode();
    }
}
