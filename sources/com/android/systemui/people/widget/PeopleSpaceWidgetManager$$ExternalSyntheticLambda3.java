package com.android.systemui.people.widget;

import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PeopleSpaceWidgetManager$$ExternalSyntheticLambda3 implements Function {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                return (String) ((Map) this.f$0).get((String) obj);
            default:
                return (NotificationGroupManagerLegacy.NotificationGroup) ((HashMap) this.f$0).get((String) obj);
        }
    }

    public /* synthetic */ PeopleSpaceWidgetManager$$ExternalSyntheticLambda3(HashMap hashMap, int i) {
        this.$r8$classId = i;
        this.f$0 = hashMap;
    }
}
