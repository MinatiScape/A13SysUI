package com.android.systemui.statusbar.notification.collection.coordinator;

import androidx.preference.R$id;
import java.util.Map;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
/* compiled from: HeadsUpCoordinator.kt */
/* loaded from: classes.dex */
final /* synthetic */ class HeadsUpCoordinator$onBeforeFinalizeFilter$1$4 extends FunctionReferenceImpl implements Function1<String, GroupLocation> {
    public HeadsUpCoordinator$onBeforeFinalizeFilter$1$4(Map map) {
        super(1, map, R$id.class, "getLocation", "getLocation(Ljava/util/Map;Ljava/lang/String;)Lcom/android/systemui/statusbar/notification/collection/coordinator/GroupLocation;", 1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final GroupLocation invoke(String str) {
        return (GroupLocation) ((Map) this.receiver).getOrDefault(str, GroupLocation.Detached);
    }
}
