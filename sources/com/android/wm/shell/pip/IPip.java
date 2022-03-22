package com.android.wm.shell.pip;

import android.app.PictureInPictureParams;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.SurfaceControl;
import com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda2;
import com.android.wm.shell.common.ExecutorUtils;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipTransitionState;
import com.android.wm.shell.pip.phone.PipController;
import java.util.Objects;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public interface IPip extends IInterface {

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IPip {
        @Override // android.os.IInterface
        public final IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public final boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            IPipAnimationListener iPipAnimationListener;
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface("com.android.wm.shell.pip.IPip");
            }
            if (i != 1598968902) {
                if (i == 2) {
                    final ComponentName componentName = (ComponentName) parcel.readTypedObject(ComponentName.CREATOR);
                    final ActivityInfo activityInfo = (ActivityInfo) parcel.readTypedObject(ActivityInfo.CREATOR);
                    final PictureInPictureParams pictureInPictureParams = (PictureInPictureParams) parcel.readTypedObject(PictureInPictureParams.CREATOR);
                    final int readInt = parcel.readInt();
                    final int readInt2 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    final Rect[] rectArr = new Rect[1];
                    ExecutorUtils.executeRemoteCallWithTaskPermission(((PipController.IPipImpl) this).mController, "startSwipePipToHome", new Consumer() { // from class: com.android.wm.shell.pip.phone.PipController$IPipImpl$$ExternalSyntheticLambda2
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            boolean z;
                            Rect[] rectArr2 = rectArr;
                            ComponentName componentName2 = componentName;
                            ActivityInfo activityInfo2 = activityInfo;
                            PictureInPictureParams pictureInPictureParams2 = pictureInPictureParams;
                            int i3 = readInt;
                            int i4 = readInt2;
                            PipController pipController = (PipController) obj;
                            Objects.requireNonNull(pipController);
                            if (i4 > 0) {
                                z = true;
                            } else {
                                z = false;
                            }
                            if (!z) {
                                i4 = 0;
                            }
                            PipBoundsState pipBoundsState = pipController.mPipBoundsState;
                            Objects.requireNonNull(pipBoundsState);
                            pipBoundsState.setShelfVisibility(z, i4, true);
                            Context context = pipController.mContext;
                            PipBoundsState pipBoundsState2 = pipController.mPipBoundsState;
                            Objects.requireNonNull(pipBoundsState2);
                            pipBoundsState2.mDisplayLayout.rotateTo(context.getResources(), i3);
                            PipTaskOrganizer pipTaskOrganizer = pipController.mPipTaskOrganizer;
                            Objects.requireNonNull(pipTaskOrganizer);
                            PipTransitionState pipTransitionState = pipTaskOrganizer.mPipTransitionState;
                            Objects.requireNonNull(pipTransitionState);
                            pipTransitionState.mInSwipePipToHomeTransition = true;
                            PipTransitionState pipTransitionState2 = pipTaskOrganizer.mPipTransitionState;
                            Objects.requireNonNull(pipTransitionState2);
                            pipTransitionState2.mState = 3;
                            pipTaskOrganizer.mPipTransitionController.sendOnPipTransitionStarted(2);
                            pipTaskOrganizer.mPipBoundsState.setBoundsStateForEntry(componentName2, activityInfo2, pictureInPictureParams2, pipTaskOrganizer.mPipBoundsAlgorithm);
                            Rect entryDestinationBounds = pipTaskOrganizer.mPipBoundsAlgorithm.getEntryDestinationBounds();
                            PipBoundsState pipBoundsState3 = pipController.mPipBoundsState;
                            Objects.requireNonNull(pipBoundsState3);
                            pipBoundsState3.mNormalBounds.set(entryDestinationBounds);
                            rectArr2[0] = entryDestinationBounds;
                        }
                    }, true);
                    Rect rect = rectArr[0];
                    parcel2.writeNoException();
                    parcel2.writeTypedObject(rect, 1);
                } else if (i == 3) {
                    final ComponentName componentName2 = (ComponentName) parcel.readTypedObject(ComponentName.CREATOR);
                    final Rect rect2 = (Rect) parcel.readTypedObject(Rect.CREATOR);
                    final SurfaceControl surfaceControl = (SurfaceControl) parcel.readTypedObject(SurfaceControl.CREATOR);
                    parcel.enforceNoDataAvail();
                    ExecutorUtils.executeRemoteCallWithTaskPermission(((PipController.IPipImpl) this).mController, "stopSwipePipToHome", new Consumer(componentName2, rect2, surfaceControl) { // from class: com.android.wm.shell.pip.phone.PipController$IPipImpl$$ExternalSyntheticLambda3
                        public final /* synthetic */ Rect f$1;
                        public final /* synthetic */ SurfaceControl f$2;

                        {
                            this.f$1 = rect2;
                            this.f$2 = surfaceControl;
                        }

                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            Rect rect3 = this.f$1;
                            SurfaceControl surfaceControl2 = this.f$2;
                            PipController pipController = (PipController) obj;
                            Objects.requireNonNull(pipController);
                            PipTaskOrganizer pipTaskOrganizer = pipController.mPipTaskOrganizer;
                            Objects.requireNonNull(pipTaskOrganizer);
                            PipTransitionState pipTransitionState = pipTaskOrganizer.mPipTransitionState;
                            Objects.requireNonNull(pipTransitionState);
                            if (pipTransitionState.mInSwipePipToHomeTransition) {
                                pipTaskOrganizer.mPipBoundsState.setBounds(rect3);
                                pipTaskOrganizer.mSwipePipToHomeOverlay = surfaceControl2;
                            }
                        }
                    }, false);
                } else if (i == 4) {
                    final IBinder readStrongBinder = parcel.readStrongBinder();
                    if (readStrongBinder == null) {
                        iPipAnimationListener = null;
                    } else {
                        IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.android.wm.shell.pip.IPipAnimationListener");
                        if (queryLocalInterface == null || !(queryLocalInterface instanceof IPipAnimationListener)) {
                            iPipAnimationListener = new IPipAnimationListener(readStrongBinder) { // from class: com.android.wm.shell.pip.IPipAnimationListener$Stub$Proxy
                                public IBinder mRemote;

                                {
                                    this.mRemote = readStrongBinder;
                                }

                                @Override // com.android.wm.shell.pip.IPipAnimationListener
                                public final void onPipAnimationStarted() throws RemoteException {
                                    Parcel obtain = Parcel.obtain();
                                    try {
                                        obtain.writeInterfaceToken("com.android.wm.shell.pip.IPipAnimationListener");
                                        this.mRemote.transact(1, obtain, null, 1);
                                    } finally {
                                        obtain.recycle();
                                    }
                                }

                                @Override // com.android.wm.shell.pip.IPipAnimationListener
                                public final void onPipCornerRadiusChanged(int i3) throws RemoteException {
                                    Parcel obtain = Parcel.obtain();
                                    try {
                                        obtain.writeInterfaceToken("com.android.wm.shell.pip.IPipAnimationListener");
                                        obtain.writeInt(i3);
                                        this.mRemote.transact(2, obtain, null, 1);
                                    } finally {
                                        obtain.recycle();
                                    }
                                }

                                @Override // android.os.IInterface
                                public final IBinder asBinder() {
                                    return this.mRemote;
                                }
                            };
                        } else {
                            iPipAnimationListener = (IPipAnimationListener) queryLocalInterface;
                        }
                    }
                    parcel.enforceNoDataAvail();
                    PipController.IPipImpl iPipImpl = (PipController.IPipImpl) this;
                    ExecutorUtils.executeRemoteCallWithTaskPermission(iPipImpl.mController, "setPinnedStackAnimationListener", new WifiPickerTracker$$ExternalSyntheticLambda2(iPipImpl, iPipAnimationListener, 2), false);
                } else if (i != 5) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    final boolean readBoolean = parcel.readBoolean();
                    final int readInt3 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    ExecutorUtils.executeRemoteCallWithTaskPermission(((PipController.IPipImpl) this).mController, "setShelfHeight", new Consumer() { // from class: com.android.wm.shell.pip.phone.PipController$IPipImpl$$ExternalSyntheticLambda1
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            boolean z = readBoolean;
                            int i3 = readInt3;
                            PipController pipController = (PipController) obj;
                            Objects.requireNonNull(pipController);
                            if (!z) {
                                i3 = 0;
                            }
                            PipBoundsState pipBoundsState = pipController.mPipBoundsState;
                            Objects.requireNonNull(pipBoundsState);
                            pipBoundsState.setShelfVisibility(z, i3, true);
                        }
                    }, false);
                }
                return true;
            }
            parcel2.writeString("com.android.wm.shell.pip.IPip");
            return true;
        }

        public Stub() {
            attachInterface(this, "com.android.wm.shell.pip.IPip");
        }
    }
}
