package com.android.systemui.util.sensors;

import com.android.systemui.plugins.SensorManagerPlugin;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationContentInflater;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class AsyncSensorManager$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;
    public final /* synthetic */ Object f$2;

    public /* synthetic */ AsyncSensorManager$$ExternalSyntheticLambda1(Object obj, Object obj2, Object obj3, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
        this.f$2 = obj3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                AsyncSensorManager asyncSensorManager = (AsyncSensorManager) this.f$0;
                SensorManagerPlugin.Sensor sensor = (SensorManagerPlugin.Sensor) this.f$1;
                SensorManagerPlugin.SensorEventListener sensorEventListener = (SensorManagerPlugin.SensorEventListener) this.f$2;
                Objects.requireNonNull(asyncSensorManager);
                for (int i = 0; i < asyncSensorManager.mPlugins.size(); i++) {
                    ((SensorManagerPlugin) asyncSensorManager.mPlugins.get(i)).registerListener(sensor, sensorEventListener);
                }
                return;
            default:
                NotificationContentInflater notificationContentInflater = (NotificationContentInflater) this.f$0;
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) this.f$1;
                Objects.requireNonNull(notificationContentInflater);
                Objects.requireNonNull(expandableNotificationRow);
                expandableNotificationRow.mPrivateLayout.setContractedChild(null);
                notificationContentInflater.mRemoteViewCache.removeCachedView((NotificationEntry) this.f$2, 1);
                return;
        }
    }
}
