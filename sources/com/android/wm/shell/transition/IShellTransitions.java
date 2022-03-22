package com.android.wm.shell.transition;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Pair;
import android.window.RemoteTransition;
import android.window.TransitionFilter;
import com.android.wm.shell.common.ExecutorUtils;
import com.android.wm.shell.transition.Transitions;
import java.util.Objects;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public interface IShellTransitions extends IInterface {

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IShellTransitions {
        @Override // android.os.IInterface
        public final IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public final boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface("com.android.wm.shell.transition.IShellTransitions");
            }
            if (i != 1598968902) {
                if (i == 2) {
                    final TransitionFilter transitionFilter = (TransitionFilter) parcel.readTypedObject(TransitionFilter.CREATOR);
                    final RemoteTransition remoteTransition = (RemoteTransition) parcel.readTypedObject(RemoteTransition.CREATOR);
                    parcel.enforceNoDataAvail();
                    ExecutorUtils.executeRemoteCallWithTaskPermission(((Transitions.IShellTransitionsImpl) this).mTransitions, "registerRemote", new Consumer() { // from class: com.android.wm.shell.transition.Transitions$IShellTransitionsImpl$$ExternalSyntheticLambda1
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            TransitionFilter transitionFilter2 = transitionFilter;
                            RemoteTransition remoteTransition2 = remoteTransition;
                            RemoteTransitionHandler remoteTransitionHandler = ((Transitions) obj).mRemoteTransitionHandler;
                            Objects.requireNonNull(remoteTransitionHandler);
                            remoteTransitionHandler.handleDeath(remoteTransition2.asBinder(), null);
                            remoteTransitionHandler.mFilters.add(new Pair<>(transitionFilter2, remoteTransition2));
                        }
                    }, false);
                } else if (i != 3) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    parcel.enforceNoDataAvail();
                    ExecutorUtils.executeRemoteCallWithTaskPermission(((Transitions.IShellTransitionsImpl) this).mTransitions, "unregisterRemote", new Transitions$IShellTransitionsImpl$$ExternalSyntheticLambda0((RemoteTransition) parcel.readTypedObject(RemoteTransition.CREATOR), 0), false);
                }
                return true;
            }
            parcel2.writeString("com.android.wm.shell.transition.IShellTransitions");
            return true;
        }

        public Stub() {
            attachInterface(this, "com.android.wm.shell.transition.IShellTransitions");
        }
    }
}
