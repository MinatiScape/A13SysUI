package com.google.android.setupcompat.internal;

import android.os.Bundle;
import android.os.RemoteException;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter;
import com.google.android.setupcompat.ISetupCompatService;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class SetupCompatServiceInvoker$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;
    public final /* synthetic */ Object f$2;

    public /* synthetic */ SetupCompatServiceInvoker$$ExternalSyntheticLambda0(Object obj, Object obj2, Object obj3, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
        this.f$2 = obj3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                SetupCompatServiceInvoker setupCompatServiceInvoker = (SetupCompatServiceInvoker) this.f$0;
                String str = (String) this.f$1;
                Bundle bundle = (Bundle) this.f$2;
                Objects.requireNonNull(setupCompatServiceInvoker);
                try {
                    ISetupCompatService service = SetupCompatServiceProvider.getInstance(setupCompatServiceInvoker.context).getService(setupCompatServiceInvoker.waitTimeInMillisForServiceConnection, TimeUnit.MILLISECONDS);
                    if (service != null) {
                        service.onFocusStatusChanged(bundle);
                    } else {
                        SetupCompatServiceInvoker.LOG.w("Report focusChange failed since service reference is null. Are the permission valid?");
                    }
                    return;
                } catch (RemoteException | InterruptedException | UnsupportedOperationException | TimeoutException e) {
                    SetupCompatServiceInvoker.LOG.e(String.format("Exception occurred while %s trying report windowFocusChange to SetupWizard.", str), e);
                    return;
                }
            default:
                StatusBarNotificationActivityStarter statusBarNotificationActivityStarter = (StatusBarNotificationActivityStarter) this.f$0;
                Objects.requireNonNull(statusBarNotificationActivityStarter);
                statusBarNotificationActivityStarter.mOnUserInteractionCallback.onDismiss((NotificationEntry) this.f$1, 1, (NotificationEntry) this.f$2);
                return;
        }
    }
}
