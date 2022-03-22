package com.android.systemui.dagger;

import com.android.systemui.qs.tileimpl.QSTileImpl$$ExternalSyntheticLambda1;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.unfold.FoldStateLogger;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyClient;
import com.google.android.systemui.statusbar.phone.StatusBarGoogle;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class SysUIComponent$$ExternalSyntheticLambda3 implements Consumer {
    public static final /* synthetic */ SysUIComponent$$ExternalSyntheticLambda3 INSTANCE = new SysUIComponent$$ExternalSyntheticLambda3(0);
    public static final /* synthetic */ SysUIComponent$$ExternalSyntheticLambda3 INSTANCE$1 = new SysUIComponent$$ExternalSyntheticLambda3(1);
    public static final /* synthetic */ SysUIComponent$$ExternalSyntheticLambda3 INSTANCE$2 = new SysUIComponent$$ExternalSyntheticLambda3(2);
    public final /* synthetic */ int $r8$classId;

    public /* synthetic */ SysUIComponent$$ExternalSyntheticLambda3(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                FoldStateLogger foldStateLogger = (FoldStateLogger) obj;
                Objects.requireNonNull(foldStateLogger);
                foldStateLogger.foldStateLoggingProvider.addCallback(foldStateLogger);
                return;
            case 1:
                StatusBar statusBar = (StatusBar) obj;
                Objects.requireNonNull(statusBar);
                statusBar.mUiBgExecutor.execute(new QSTileImpl$$ExternalSyntheticLambda1(statusBar, 3));
                return;
            default:
                boolean z = StatusBarGoogle.DEBUG;
                ((NotificationVoiceReplyClient) obj).startClient();
                return;
        }
    }
}
