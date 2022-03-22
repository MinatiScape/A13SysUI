package androidx.mediarouter.media;

import android.content.IntentFilter;
import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MediaRouteDescriptor {
    public final Bundle mBundle;
    public List<IntentFilter> mControlFilters;
    public List<String> mGroupMemberIds;

    /* loaded from: classes.dex */
    public static final class Builder {
        public final Bundle mBundle;
        public ArrayList<IntentFilter> mControlFilters;
        public ArrayList<String> mGroupMemberIds;

        public final Builder addControlFilters(List list) {
            if (list != null) {
                if (!list.isEmpty()) {
                    Iterator it = list.iterator();
                    while (it.hasNext()) {
                        IntentFilter intentFilter = (IntentFilter) it.next();
                        if (intentFilter != null) {
                            if (this.mControlFilters == null) {
                                this.mControlFilters = new ArrayList<>();
                            }
                            if (!this.mControlFilters.contains(intentFilter)) {
                                this.mControlFilters.add(intentFilter);
                            }
                        }
                    }
                }
                return this;
            }
            throw new IllegalArgumentException("filters must not be null");
        }

        public final MediaRouteDescriptor build() {
            ArrayList<IntentFilter> arrayList = this.mControlFilters;
            if (arrayList != null) {
                this.mBundle.putParcelableArrayList("controlFilters", arrayList);
            }
            ArrayList<String> arrayList2 = this.mGroupMemberIds;
            if (arrayList2 != null) {
                this.mBundle.putStringArrayList("groupMemberIds", arrayList2);
            }
            return new MediaRouteDescriptor(this.mBundle);
        }

        public Builder(String str, String str2) {
            Bundle bundle = new Bundle();
            this.mBundle = bundle;
            Objects.requireNonNull(str, "id must not be null");
            bundle.putString("id", str);
            Objects.requireNonNull(str2, "name must not be null");
            bundle.putString("name", str2);
        }
    }

    public final void ensureControlFilters() {
        if (this.mControlFilters == null) {
            ArrayList parcelableArrayList = this.mBundle.getParcelableArrayList("controlFilters");
            this.mControlFilters = parcelableArrayList;
            if (parcelableArrayList == null) {
                this.mControlFilters = Collections.emptyList();
            }
        }
    }

    public final List<String> getGroupMemberIds() {
        if (this.mGroupMemberIds == null) {
            ArrayList<String> stringArrayList = this.mBundle.getStringArrayList("groupMemberIds");
            this.mGroupMemberIds = stringArrayList;
            if (stringArrayList == null) {
                this.mGroupMemberIds = Collections.emptyList();
            }
        }
        return this.mGroupMemberIds;
    }

    public final Uri getIconUri() {
        String string = this.mBundle.getString("iconUri");
        if (string == null) {
            return null;
        }
        return Uri.parse(string);
    }

    public final String getId() {
        return this.mBundle.getString("id");
    }

    public final String toString() {
        StringBuilder m = DebugInfo$$ExternalSyntheticOutline0.m("MediaRouteDescriptor{ ", "id=");
        m.append(getId());
        m.append(", groupMemberIds=");
        m.append(getGroupMemberIds());
        m.append(", name=");
        m.append(this.mBundle.getString("name"));
        m.append(", description=");
        m.append(this.mBundle.getString("status"));
        m.append(", iconUri=");
        m.append(getIconUri());
        m.append(", isEnabled=");
        m.append(this.mBundle.getBoolean("enabled", true));
        m.append(", connectionState=");
        m.append(this.mBundle.getInt("connectionState", 0));
        m.append(", controlFilters=");
        ensureControlFilters();
        m.append(Arrays.toString(this.mControlFilters.toArray()));
        m.append(", playbackType=");
        m.append(this.mBundle.getInt("playbackType", 1));
        m.append(", playbackStream=");
        m.append(this.mBundle.getInt("playbackStream", -1));
        m.append(", deviceType=");
        m.append(this.mBundle.getInt("deviceType"));
        m.append(", volume=");
        m.append(this.mBundle.getInt("volume"));
        m.append(", volumeMax=");
        m.append(this.mBundle.getInt("volumeMax"));
        m.append(", volumeHandling=");
        m.append(this.mBundle.getInt("volumeHandling", 0));
        m.append(", presentationDisplayId=");
        m.append(this.mBundle.getInt("presentationDisplayId", -1));
        m.append(", extras=");
        m.append(this.mBundle.getBundle("extras"));
        m.append(", isValid=");
        m.append(isValid());
        m.append(", minClientVersion=");
        m.append(this.mBundle.getInt("minClientVersion", 1));
        m.append(", maxClientVersion=");
        m.append(this.mBundle.getInt("maxClientVersion", Integer.MAX_VALUE));
        m.append(" }");
        return m.toString();
    }

    public MediaRouteDescriptor(Bundle bundle) {
        this.mBundle = bundle;
    }

    public final boolean isValid() {
        ensureControlFilters();
        if (TextUtils.isEmpty(getId()) || TextUtils.isEmpty(this.mBundle.getString("name")) || this.mControlFilters.contains(null)) {
            return false;
        }
        return true;
    }
}
