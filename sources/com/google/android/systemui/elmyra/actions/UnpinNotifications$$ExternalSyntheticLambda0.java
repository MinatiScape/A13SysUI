package com.google.android.systemui.elmyra.actions;

import android.net.Uri;
import com.android.systemui.statusbar.notification.collection.coordinator.BubbleCoordinator;
import com.android.systemui.wmshell.BubblesManager;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsListener;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class UnpinNotifications$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ UnpinNotifications$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                UnpinNotifications unpinNotifications = (UnpinNotifications) this.f$0;
                Uri uri = (Uri) obj;
                Objects.requireNonNull(unpinNotifications);
                unpinNotifications.updateHeadsUpListener();
                return;
            case 1:
                BubbleCoordinator bubbleCoordinator = (BubbleCoordinator) this.f$0;
                BubblesManager bubblesManager = (BubblesManager) obj;
                Objects.requireNonNull(bubbleCoordinator);
                BubbleCoordinator.AnonymousClass3 r1 = bubbleCoordinator.mNotifCallback;
                Objects.requireNonNull(bubblesManager);
                bubblesManager.mCallbacks.add(r1);
                return;
            default:
                EdgeLightsView edgeLightsView = (EdgeLightsView) this.f$0;
                int i = EdgeLightsView.$r8$clinit;
                Objects.requireNonNull(edgeLightsView);
                ((EdgeLightsListener) obj).onAssistLightsUpdated(edgeLightsView.mMode, edgeLightsView.mAssistLights);
                return;
        }
    }
}
