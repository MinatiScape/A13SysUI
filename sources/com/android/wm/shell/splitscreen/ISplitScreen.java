package com.android.wm.shell.splitscreen;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.UserHandle;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationTarget;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.window.RemoteTransition;
import android.window.WindowContainerTransaction;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.policy.LocationControllerImpl$H$$ExternalSyntheticLambda0;
import com.android.wm.shell.RootTaskDisplayAreaOrganizer;
import com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda0;
import com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda2;
import com.android.wm.shell.common.ExecutorUtils;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.transition.Transitions;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public interface ISplitScreen extends IInterface {

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ISplitScreen {
        @Override // android.os.IInterface
        public final IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public final boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            IInterface queryLocalInterface;
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface("com.android.wm.shell.splitscreen.ISplitScreen");
            }
            if (i != 1598968902) {
                final ISplitScreenListener iSplitScreenListener = null;
                switch (i) {
                    case 2:
                        final IBinder readStrongBinder = parcel.readStrongBinder();
                        if (readStrongBinder != null) {
                            IInterface queryLocalInterface2 = readStrongBinder.queryLocalInterface("com.android.wm.shell.splitscreen.ISplitScreenListener");
                            if (queryLocalInterface2 == null || !(queryLocalInterface2 instanceof ISplitScreenListener)) {
                                iSplitScreenListener = new ISplitScreenListener(readStrongBinder) { // from class: com.android.wm.shell.splitscreen.ISplitScreenListener$Stub$Proxy
                                    public IBinder mRemote;

                                    {
                                        this.mRemote = readStrongBinder;
                                    }

                                    @Override // com.android.wm.shell.splitscreen.ISplitScreenListener
                                    public final void onStagePositionChanged(int i3, int i4) throws RemoteException {
                                        Parcel obtain = Parcel.obtain();
                                        try {
                                            obtain.writeInterfaceToken("com.android.wm.shell.splitscreen.ISplitScreenListener");
                                            obtain.writeInt(i3);
                                            obtain.writeInt(i4);
                                            this.mRemote.transact(1, obtain, null, 1);
                                        } finally {
                                            obtain.recycle();
                                        }
                                    }

                                    @Override // com.android.wm.shell.splitscreen.ISplitScreenListener
                                    public final void onTaskStageChanged(int i3, int i4, boolean z) throws RemoteException {
                                        Parcel obtain = Parcel.obtain();
                                        try {
                                            obtain.writeInterfaceToken("com.android.wm.shell.splitscreen.ISplitScreenListener");
                                            obtain.writeInt(i3);
                                            obtain.writeInt(i4);
                                            obtain.writeBoolean(z);
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
                                iSplitScreenListener = (ISplitScreenListener) queryLocalInterface2;
                            }
                        }
                        parcel.enforceNoDataAvail();
                        final SplitScreenController.ISplitScreenImpl iSplitScreenImpl = (SplitScreenController.ISplitScreenImpl) this;
                        ExecutorUtils.executeRemoteCallWithTaskPermission(iSplitScreenImpl.mController, "registerSplitScreenListener", new Consumer() { // from class: com.android.wm.shell.splitscreen.SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda6
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                SplitScreenController.ISplitScreenImpl iSplitScreenImpl2 = SplitScreenController.ISplitScreenImpl.this;
                                ISplitScreenListener iSplitScreenListener2 = iSplitScreenListener;
                                SplitScreenController splitScreenController = (SplitScreenController) obj;
                                Objects.requireNonNull(iSplitScreenImpl2);
                                iSplitScreenImpl2.mListener.register(iSplitScreenListener2);
                            }
                        }, false);
                        break;
                    case 3:
                        IBinder readStrongBinder2 = parcel.readStrongBinder();
                        if (!(readStrongBinder2 == null || (queryLocalInterface = readStrongBinder2.queryLocalInterface("com.android.wm.shell.splitscreen.ISplitScreenListener")) == null || !(queryLocalInterface instanceof ISplitScreenListener))) {
                            ISplitScreenListener iSplitScreenListener2 = (ISplitScreenListener) queryLocalInterface;
                        }
                        parcel.enforceNoDataAvail();
                        SplitScreenController.ISplitScreenImpl iSplitScreenImpl2 = (SplitScreenController.ISplitScreenImpl) this;
                        ExecutorUtils.executeRemoteCallWithTaskPermission(iSplitScreenImpl2.mController, "unregisterSplitScreenListener", new ShellCommandHandlerImpl$$ExternalSyntheticLambda2(iSplitScreenImpl2, 4), false);
                        break;
                    case 4:
                        final boolean readBoolean = parcel.readBoolean();
                        parcel.enforceNoDataAvail();
                        ExecutorUtils.executeRemoteCallWithTaskPermission(((SplitScreenController.ISplitScreenImpl) this).mController, "setSideStageVisibility", new Consumer() { // from class: com.android.wm.shell.splitscreen.SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda8
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                ((SplitScreenController) obj).setSideStageVisibility(readBoolean);
                            }
                        }, false);
                        break;
                    case 5:
                        int readInt = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        ExecutorUtils.executeRemoteCallWithTaskPermission(((SplitScreenController.ISplitScreenImpl) this).mController, "removeFromSideStage", new ShellCommandHandlerImpl$$ExternalSyntheticLambda0(readInt, 2), false);
                        break;
                    case FalsingManager.VERSION /* 6 */:
                        final int readInt2 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        ExecutorUtils.executeRemoteCallWithTaskPermission(((SplitScreenController.ISplitScreenImpl) this).mController, "exitSplitScreen", new Consumer() { // from class: com.android.wm.shell.splitscreen.SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda0
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                int i3 = readInt2;
                                SplitScreenController splitScreenController = (SplitScreenController) obj;
                                Objects.requireNonNull(splitScreenController);
                                StageCoordinator stageCoordinator = splitScreenController.mStageCoordinator;
                                Objects.requireNonNull(stageCoordinator);
                                MainStage mainStage = stageCoordinator.mMainStage;
                                Objects.requireNonNull(mainStage);
                                if (mainStage.mIsActive) {
                                    StageTaskListener stageTaskListener = null;
                                    if (stageCoordinator.mMainStage.containsTask(i3)) {
                                        stageTaskListener = stageCoordinator.mMainStage;
                                    } else if (stageCoordinator.mSideStage.containsTask(i3)) {
                                        stageTaskListener = stageCoordinator.mSideStage;
                                    }
                                    WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
                                    if (stageTaskListener != null && stageTaskListener.containsTask(i3)) {
                                        windowContainerTransaction.reorder(stageTaskListener.mChildrenTaskInfo.get(i3).token, true);
                                    }
                                    stageCoordinator.applyExitSplitScreen(stageTaskListener, windowContainerTransaction, 0);
                                }
                            }
                        }, false);
                        break;
                    case 7:
                        boolean readBoolean2 = parcel.readBoolean();
                        parcel.enforceNoDataAvail();
                        ExecutorUtils.executeRemoteCallWithTaskPermission(((SplitScreenController.ISplitScreenImpl) this).mController, "exitSplitScreenOnHide", new LocationControllerImpl$H$$ExternalSyntheticLambda0(readBoolean2, 1), false);
                        break;
                    case 8:
                        final int readInt3 = parcel.readInt();
                        final int readInt4 = parcel.readInt();
                        final Bundle bundle = (Bundle) parcel.readTypedObject(Bundle.CREATOR);
                        parcel.enforceNoDataAvail();
                        ExecutorUtils.executeRemoteCallWithTaskPermission(((SplitScreenController.ISplitScreenImpl) this).mController, "startTask", new Consumer() { // from class: com.android.wm.shell.splitscreen.SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda1
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                ((SplitScreenController) obj).startTask(readInt3, readInt4, bundle);
                            }
                        }, false);
                        break;
                    case 9:
                        final String readString = parcel.readString();
                        final String readString2 = parcel.readString();
                        final int readInt5 = parcel.readInt();
                        final Bundle bundle2 = (Bundle) parcel.readTypedObject(Bundle.CREATOR);
                        final UserHandle userHandle = (UserHandle) parcel.readTypedObject(UserHandle.CREATOR);
                        parcel.enforceNoDataAvail();
                        ExecutorUtils.executeRemoteCallWithTaskPermission(((SplitScreenController.ISplitScreenImpl) this).mController, "startShortcut", new Consumer() { // from class: com.android.wm.shell.splitscreen.SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda7
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                ((SplitScreenController) obj).startShortcut(readString, readString2, readInt5, bundle2, userHandle);
                            }
                        }, false);
                        break;
                    case 10:
                        final PendingIntent pendingIntent = (PendingIntent) parcel.readTypedObject(PendingIntent.CREATOR);
                        final Intent intent = (Intent) parcel.readTypedObject(Intent.CREATOR);
                        final int readInt6 = parcel.readInt();
                        final Bundle bundle3 = (Bundle) parcel.readTypedObject(Bundle.CREATOR);
                        parcel.enforceNoDataAvail();
                        ExecutorUtils.executeRemoteCallWithTaskPermission(((SplitScreenController.ISplitScreenImpl) this).mController, "startIntent", new Consumer() { // from class: com.android.wm.shell.splitscreen.SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda4
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                ((SplitScreenController) obj).startIntent(pendingIntent, intent, readInt6, bundle3);
                            }
                        }, false);
                        break;
                    case QSTileImpl.H.STALE /* 11 */:
                        final int readInt7 = parcel.readInt();
                        Parcelable.Creator creator = Bundle.CREATOR;
                        final Bundle bundle4 = (Bundle) parcel.readTypedObject(creator);
                        final int readInt8 = parcel.readInt();
                        final Bundle bundle5 = (Bundle) parcel.readTypedObject(creator);
                        final int readInt9 = parcel.readInt();
                        final float readFloat = parcel.readFloat();
                        final RemoteTransition remoteTransition = (RemoteTransition) parcel.readTypedObject(RemoteTransition.CREATOR);
                        parcel.enforceNoDataAvail();
                        ExecutorUtils.executeRemoteCallWithTaskPermission(((SplitScreenController.ISplitScreenImpl) this).mController, "startTasks", new Consumer() { // from class: com.android.wm.shell.splitscreen.SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda3
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                int i3 = readInt7;
                                Bundle bundle6 = bundle4;
                                int i4 = readInt8;
                                Bundle bundle7 = bundle5;
                                int i5 = readInt9;
                                float f = readFloat;
                                RemoteTransition remoteTransition2 = remoteTransition;
                                StageCoordinator stageCoordinator = ((SplitScreenController) obj).mStageCoordinator;
                                Objects.requireNonNull(stageCoordinator);
                                WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
                                if (bundle6 == null) {
                                    bundle6 = new Bundle();
                                }
                                if (bundle7 == null) {
                                    bundle7 = new Bundle();
                                }
                                stageCoordinator.setSideStagePosition(i5, windowContainerTransaction);
                                stageCoordinator.mSplitLayout.setDivideRatio(f);
                                stageCoordinator.mMainStage.activate(stageCoordinator.getMainStageBounds(), windowContainerTransaction, false);
                                SideStage sideStage = stageCoordinator.mSideStage;
                                Rect sideStageBounds = stageCoordinator.getSideStageBounds();
                                Objects.requireNonNull(sideStage);
                                windowContainerTransaction.setBounds(sideStage.mRootTaskInfo.token, sideStageBounds);
                                bundle6.putParcelable("android.activity.launchRootTaskToken", stageCoordinator.mMainStage.mRootTaskInfo.token);
                                bundle7.putParcelable("android.activity.launchRootTaskToken", stageCoordinator.mSideStage.mRootTaskInfo.token);
                                windowContainerTransaction.startTask(i3, bundle6);
                                windowContainerTransaction.startTask(i4, bundle7);
                                stageCoordinator.mSplitTransitions.startEnterTransition(16, windowContainerTransaction, remoteTransition2, stageCoordinator);
                            }
                        }, false);
                        break;
                    case KeyguardSliceProvider.ALARM_VISIBILITY_HOURS /* 12 */:
                        final int readInt10 = parcel.readInt();
                        Parcelable.Creator creator2 = Bundle.CREATOR;
                        final Bundle bundle6 = (Bundle) parcel.readTypedObject(creator2);
                        final int readInt11 = parcel.readInt();
                        final Bundle bundle7 = (Bundle) parcel.readTypedObject(creator2);
                        final int readInt12 = parcel.readInt();
                        final float readFloat2 = parcel.readFloat();
                        final RemoteAnimationAdapter remoteAnimationAdapter = (RemoteAnimationAdapter) parcel.readTypedObject(RemoteAnimationAdapter.CREATOR);
                        parcel.enforceNoDataAvail();
                        ExecutorUtils.executeRemoteCallWithTaskPermission(((SplitScreenController.ISplitScreenImpl) this).mController, "startTasks", new Consumer() { // from class: com.android.wm.shell.splitscreen.SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda2
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                int i3 = readInt10;
                                Bundle bundle8 = bundle6;
                                int i4 = readInt11;
                                Bundle bundle9 = bundle7;
                                int i5 = readInt12;
                                float f = readFloat2;
                                RemoteAnimationAdapter remoteAnimationAdapter2 = remoteAnimationAdapter;
                                StageCoordinator stageCoordinator = ((SplitScreenController) obj).mStageCoordinator;
                                Objects.requireNonNull(stageCoordinator);
                                stageCoordinator.startWithLegacyTransition(i3, i4, null, null, bundle8, bundle9, i5, f, remoteAnimationAdapter2);
                            }
                        }, false);
                        break;
                    case QS.VERSION /* 13 */:
                        final PendingIntent pendingIntent2 = (PendingIntent) parcel.readTypedObject(PendingIntent.CREATOR);
                        final Intent intent2 = (Intent) parcel.readTypedObject(Intent.CREATOR);
                        final int readInt13 = parcel.readInt();
                        Parcelable.Creator creator3 = Bundle.CREATOR;
                        final Bundle bundle8 = (Bundle) parcel.readTypedObject(creator3);
                        final Bundle bundle9 = (Bundle) parcel.readTypedObject(creator3);
                        final int readInt14 = parcel.readInt();
                        final float readFloat3 = parcel.readFloat();
                        final RemoteAnimationAdapter remoteAnimationAdapter2 = (RemoteAnimationAdapter) parcel.readTypedObject(RemoteAnimationAdapter.CREATOR);
                        parcel.enforceNoDataAvail();
                        ExecutorUtils.executeRemoteCallWithTaskPermission(((SplitScreenController.ISplitScreenImpl) this).mController, "startIntentAndTaskWithLegacyTransition", new Consumer() { // from class: com.android.wm.shell.splitscreen.SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda5
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                PendingIntent pendingIntent3 = pendingIntent2;
                                Intent intent3 = intent2;
                                int i3 = readInt13;
                                Bundle bundle10 = bundle8;
                                Bundle bundle11 = bundle9;
                                int i4 = readInt14;
                                float f = readFloat3;
                                RemoteAnimationAdapter remoteAnimationAdapter3 = remoteAnimationAdapter2;
                                StageCoordinator stageCoordinator = ((SplitScreenController) obj).mStageCoordinator;
                                Objects.requireNonNull(stageCoordinator);
                                stageCoordinator.startWithLegacyTransition(i3, -1, pendingIntent3, intent3, bundle10, bundle11, i4, f, remoteAnimationAdapter3);
                            }
                        }, false);
                        break;
                    case 14:
                        final boolean readBoolean3 = parcel.readBoolean();
                        final RemoteAnimationTarget[] remoteAnimationTargetArr = (RemoteAnimationTarget[]) parcel.createTypedArray(RemoteAnimationTarget.CREATOR);
                        parcel.enforceNoDataAvail();
                        final RemoteAnimationTarget[][] remoteAnimationTargetArr2 = {null};
                        ExecutorUtils.executeRemoteCallWithTaskPermission(((SplitScreenController.ISplitScreenImpl) this).mController, "onGoingToRecentsLegacy", new Consumer(remoteAnimationTargetArr2, readBoolean3, remoteAnimationTargetArr) { // from class: com.android.wm.shell.splitscreen.SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda9
                            public final /* synthetic */ RemoteAnimationTarget[][] f$0;
                            public final /* synthetic */ RemoteAnimationTarget[] f$2;

                            {
                                this.f$2 = remoteAnimationTargetArr;
                            }

                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                RemoteAnimationTarget[] remoteAnimationTargetArr3;
                                RemoteAnimationTarget[][] remoteAnimationTargetArr4 = this.f$0;
                                RemoteAnimationTarget[] remoteAnimationTargetArr5 = this.f$2;
                                SplitScreenController splitScreenController = (SplitScreenController) obj;
                                Objects.requireNonNull(splitScreenController);
                                if (Transitions.ENABLE_SHELL_TRANSITIONS || remoteAnimationTargetArr5.length < 2) {
                                    remoteAnimationTargetArr3 = null;
                                } else {
                                    SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
                                    SurfaceControl surfaceControl = splitScreenController.mSplitTasksContainerLayer;
                                    if (surfaceControl != null) {
                                        transaction.remove(surfaceControl);
                                    }
                                    SurfaceControl.Builder callsite = new SurfaceControl.Builder(new SurfaceSession()).setContainerLayer().setName("RecentsAnimationSplitTasks").setHidden(false).setCallsite("SplitScreenController#onGoingtoRecentsLegacy");
                                    RootTaskDisplayAreaOrganizer rootTaskDisplayAreaOrganizer = splitScreenController.mRootTDAOrganizer;
                                    Objects.requireNonNull(rootTaskDisplayAreaOrganizer);
                                    callsite.setParent(rootTaskDisplayAreaOrganizer.mLeashes.get(0));
                                    splitScreenController.mSplitTasksContainerLayer = callsite.build();
                                    Arrays.sort(remoteAnimationTargetArr5, SplitScreenController$$ExternalSyntheticLambda0.INSTANCE);
                                    int length = remoteAnimationTargetArr5.length;
                                    int i3 = 0;
                                    int i4 = 1;
                                    while (i3 < length) {
                                        RemoteAnimationTarget remoteAnimationTarget = remoteAnimationTargetArr5[i3];
                                        transaction.reparent(remoteAnimationTarget.leash, splitScreenController.mSplitTasksContainerLayer);
                                        SurfaceControl surfaceControl2 = remoteAnimationTarget.leash;
                                        Rect rect = remoteAnimationTarget.screenSpaceBounds;
                                        transaction.setPosition(surfaceControl2, rect.left, rect.top);
                                        transaction.setLayer(remoteAnimationTarget.leash, i4);
                                        i3++;
                                        i4++;
                                    }
                                    transaction.apply();
                                    transaction.close();
                                    remoteAnimationTargetArr3 = new RemoteAnimationTarget[]{splitScreenController.mStageCoordinator.getDividerBarLegacyTarget()};
                                }
                                remoteAnimationTargetArr4[0] = remoteAnimationTargetArr3;
                            }
                        }, true);
                        RemoteAnimationTarget[] remoteAnimationTargetArr3 = remoteAnimationTargetArr2[0];
                        parcel2.writeNoException();
                        parcel2.writeTypedArray(remoteAnimationTargetArr3, 1);
                        break;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
                return true;
            }
            parcel2.writeString("com.android.wm.shell.splitscreen.ISplitScreen");
            return true;
        }

        public Stub() {
            attachInterface(this, "com.android.wm.shell.splitscreen.ISplitScreen");
        }
    }
}
