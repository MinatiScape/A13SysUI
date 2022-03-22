package androidx.mediarouter.media;

import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
public final class MediaRouteProviderDescriptor {
    public final List<MediaRouteDescriptor> mRoutes;
    public final boolean mSupportsDynamicGroupRoute;

    public static MediaRouteProviderDescriptor fromBundle(Bundle bundle) {
        MediaRouteDescriptor mediaRouteDescriptor;
        ArrayList arrayList = null;
        if (bundle == null) {
            return null;
        }
        ArrayList parcelableArrayList = bundle.getParcelableArrayList("routes");
        if (parcelableArrayList != null && !parcelableArrayList.isEmpty()) {
            int size = parcelableArrayList.size();
            ArrayList arrayList2 = new ArrayList(size);
            for (int i = 0; i < size; i++) {
                Bundle bundle2 = (Bundle) parcelableArrayList.get(i);
                if (bundle2 != null) {
                    mediaRouteDescriptor = new MediaRouteDescriptor(bundle2);
                } else {
                    mediaRouteDescriptor = null;
                }
                arrayList2.add(mediaRouteDescriptor);
            }
            arrayList = arrayList2;
        }
        return new MediaRouteProviderDescriptor(arrayList, bundle.getBoolean("supportsDynamicGroupRoute", false));
    }

    public final boolean isValid() {
        int size = this.mRoutes.size();
        for (int i = 0; i < size; i++) {
            MediaRouteDescriptor mediaRouteDescriptor = this.mRoutes.get(i);
            if (mediaRouteDescriptor == null || !mediaRouteDescriptor.isValid()) {
                return false;
            }
        }
        return true;
    }

    public final String toString() {
        StringBuilder m = DebugInfo$$ExternalSyntheticOutline0.m("MediaRouteProviderDescriptor{ ", "routes=");
        m.append(Arrays.toString(this.mRoutes.toArray()));
        m.append(", isValid=");
        m.append(isValid());
        m.append(" }");
        return m.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v2, types: [java.util.List] */
    public MediaRouteProviderDescriptor(ArrayList arrayList, boolean z) {
        this.mRoutes = arrayList == null ? Collections.emptyList() : arrayList;
        this.mSupportsDynamicGroupRoute = z;
    }
}
