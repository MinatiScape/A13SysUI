package androidx.appcompat.app;

import android.content.Context;
import android.location.LocationManager;
/* loaded from: classes.dex */
public final class TwilightManager {
    public static TwilightManager sInstance;
    public final Context mContext;
    public final LocationManager mLocationManager;
    public final TwilightState mTwilightState = new TwilightState();

    /* loaded from: classes.dex */
    public static class TwilightState {
        public boolean isNight;
        public long nextUpdate;
    }

    public TwilightManager(Context context, LocationManager locationManager) {
        this.mContext = context;
        this.mLocationManager = locationManager;
    }

    public static void setInstance(TwilightManager twilightManager) {
        sInstance = twilightManager;
    }
}
