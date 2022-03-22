package androidx.mediarouter.media;

import android.media.MediaRoute2Info;
import android.net.Uri;
import android.os.Bundle;
import androidx.mediarouter.media.MediaRouteDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public final class MediaRouter2Utils {
    public static MediaRouteDescriptor toMediaRouteDescriptor(MediaRoute2Info mediaRoute2Info) {
        if (mediaRoute2Info == null) {
            return null;
        }
        MediaRouteDescriptor.Builder builder = new MediaRouteDescriptor.Builder(mediaRoute2Info.getId(), mediaRoute2Info.getName().toString());
        builder.mBundle.putInt("connectionState", mediaRoute2Info.getConnectionState());
        builder.mBundle.putInt("volumeHandling", mediaRoute2Info.getVolumeHandling());
        builder.mBundle.putInt("volumeMax", mediaRoute2Info.getVolumeMax());
        builder.mBundle.putInt("volume", mediaRoute2Info.getVolume());
        Bundle extras = mediaRoute2Info.getExtras();
        if (extras == null) {
            builder.mBundle.putBundle("extras", null);
        } else {
            builder.mBundle.putBundle("extras", new Bundle(extras));
        }
        builder.mBundle.putBoolean("enabled", true);
        builder.mBundle.putBoolean("canDisconnect", false);
        CharSequence description = mediaRoute2Info.getDescription();
        if (description != null) {
            builder.mBundle.putString("status", description.toString());
        }
        Uri iconUri = mediaRoute2Info.getIconUri();
        if (iconUri != null) {
            builder.mBundle.putString("iconUri", iconUri.toString());
        }
        Bundle extras2 = mediaRoute2Info.getExtras();
        if (extras2 == null || !extras2.containsKey("androidx.mediarouter.media.KEY_EXTRAS") || !extras2.containsKey("androidx.mediarouter.media.KEY_DEVICE_TYPE") || !extras2.containsKey("androidx.mediarouter.media.KEY_CONTROL_FILTERS")) {
            return null;
        }
        Bundle bundle = extras2.getBundle("androidx.mediarouter.media.KEY_EXTRAS");
        if (bundle == null) {
            builder.mBundle.putBundle("extras", null);
        } else {
            builder.mBundle.putBundle("extras", new Bundle(bundle));
        }
        builder.mBundle.putInt("deviceType", extras2.getInt("androidx.mediarouter.media.KEY_DEVICE_TYPE", 0));
        builder.mBundle.putInt("playbackType", extras2.getInt("androidx.mediarouter.media.KEY_PLAYBACK_TYPE", 1));
        ArrayList parcelableArrayList = extras2.getParcelableArrayList("androidx.mediarouter.media.KEY_CONTROL_FILTERS");
        if (parcelableArrayList != null) {
            builder.addControlFilters(parcelableArrayList);
        }
        return builder.build();
    }

    public static ArrayList getRouteIds(List list) {
        if (list == null) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            MediaRoute2Info mediaRoute2Info = (MediaRoute2Info) it.next();
            if (mediaRoute2Info != null) {
                arrayList.add(mediaRoute2Info.getId());
            }
        }
        return arrayList;
    }
}
