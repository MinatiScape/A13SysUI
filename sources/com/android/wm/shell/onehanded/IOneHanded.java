package com.android.wm.shell.onehanded;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.systemui.classifier.BrightLineFalsingManager$2$$ExternalSyntheticLambda0;
import com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda16;
import com.android.wm.shell.common.ExecutorUtils;
import com.android.wm.shell.onehanded.OneHandedController;
/* loaded from: classes.dex */
public interface IOneHanded extends IInterface {

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOneHanded {
        @Override // android.os.IInterface
        public final IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public final boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface("com.android.wm.shell.onehanded.IOneHanded");
            }
            if (i != 1598968902) {
                if (i == 2) {
                    ExecutorUtils.executeRemoteCallWithTaskPermission(((OneHandedController.IOneHandedImpl) this).mController, "startOneHanded", BrightLineFalsingManager$2$$ExternalSyntheticLambda0.INSTANCE$2, false);
                } else if (i != 3) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    ExecutorUtils.executeRemoteCallWithTaskPermission(((OneHandedController.IOneHandedImpl) this).mController, "stopOneHanded", OverviewProxyService$1$$ExternalSyntheticLambda16.INSTANCE$1, false);
                }
                return true;
            }
            parcel2.writeString("com.android.wm.shell.onehanded.IOneHanded");
            return true;
        }

        public Stub() {
            attachInterface(this, "com.android.wm.shell.onehanded.IOneHanded");
        }
    }
}
