package com.android.wifitrackerlib;

import android.net.wifi.ScanResult;
import android.service.notification.ConversationChannelWrapper;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.wifitrackerlib.StandardWifiEntry;
import java.util.HashMap;
import java.util.function.Function;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda7 implements Function {
    public final /* synthetic */ int $r8$classId;
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda7 INSTANCE$1 = new WifiPickerTracker$$ExternalSyntheticLambda7(1);
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda7 INSTANCE = new WifiPickerTracker$$ExternalSyntheticLambda7(0);

    public /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda7(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                return new StandardWifiEntry.ScanResultKey((ScanResult) obj);
            default:
                HashMap hashMap = PeopleSpaceWidgetManager.mListeners;
                return ((ConversationChannelWrapper) obj).getShortcutInfo();
        }
    }
}
