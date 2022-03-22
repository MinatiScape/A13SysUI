package androidx.mediarouter.media;

import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public final class MediaRouteSelector {
    public static final MediaRouteSelector EMPTY = new MediaRouteSelector(new Bundle(), null);
    public final Bundle mBundle;
    public List<String> mControlCategories;

    /* loaded from: classes.dex */
    public static final class Builder {
        public ArrayList<String> mControlCategories;

        public Builder() {
        }

        public Builder(MediaRouteSelector mediaRouteSelector) {
            if (mediaRouteSelector != null) {
                mediaRouteSelector.ensureControlCategories();
                if (!mediaRouteSelector.mControlCategories.isEmpty()) {
                    this.mControlCategories = new ArrayList<>(mediaRouteSelector.mControlCategories);
                    return;
                }
                return;
            }
            throw new IllegalArgumentException("selector must not be null");
        }

        public final MediaRouteSelector build() {
            if (this.mControlCategories == null) {
                return MediaRouteSelector.EMPTY;
            }
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("controlCategories", this.mControlCategories);
            return new MediaRouteSelector(bundle, this.mControlCategories);
        }

        public final Builder addControlCategories(ArrayList arrayList) {
            if (!arrayList.isEmpty()) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    String str = (String) it.next();
                    if (str != null) {
                        if (this.mControlCategories == null) {
                            this.mControlCategories = new ArrayList<>();
                        }
                        if (!this.mControlCategories.contains(str)) {
                            this.mControlCategories.add(str);
                        }
                    } else {
                        throw new IllegalArgumentException("category must not be null");
                    }
                }
            }
            return this;
        }
    }

    public final void ensureControlCategories() {
        if (this.mControlCategories == null) {
            ArrayList<String> stringArrayList = this.mBundle.getStringArrayList("controlCategories");
            this.mControlCategories = stringArrayList;
            if (stringArrayList == null || stringArrayList.isEmpty()) {
                this.mControlCategories = Collections.emptyList();
            }
        }
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof MediaRouteSelector)) {
            return false;
        }
        MediaRouteSelector mediaRouteSelector = (MediaRouteSelector) obj;
        ensureControlCategories();
        mediaRouteSelector.ensureControlCategories();
        return this.mControlCategories.equals(mediaRouteSelector.mControlCategories);
    }

    public final String toString() {
        StringBuilder m = DebugInfo$$ExternalSyntheticOutline0.m("MediaRouteSelector{ ", "controlCategories=");
        m.append(Arrays.toString(getControlCategories().toArray()));
        m.append(" }");
        return m.toString();
    }

    public MediaRouteSelector(Bundle bundle, ArrayList arrayList) {
        this.mBundle = bundle;
        this.mControlCategories = arrayList;
    }

    public final ArrayList getControlCategories() {
        ensureControlCategories();
        return new ArrayList(this.mControlCategories);
    }

    public final int hashCode() {
        ensureControlCategories();
        return this.mControlCategories.hashCode();
    }

    public final boolean isEmpty() {
        ensureControlCategories();
        return this.mControlCategories.isEmpty();
    }
}
