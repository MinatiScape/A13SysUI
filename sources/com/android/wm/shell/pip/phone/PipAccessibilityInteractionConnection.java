package com.android.wm.shell.pip.phone;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.MagnificationSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.IAccessibilityInteractionConnection;
import android.view.accessibility.IAccessibilityInteractionConnectionCallback;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda6;
import com.android.systemui.scrim.ScrimView$$ExternalSyntheticLambda0;
import com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda6;
import com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda7;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipSnapAlgorithm;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.phone.PipAccessibilityInteractionConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class PipAccessibilityInteractionConnection {
    public ArrayList mAccessibilityNodeInfoList;
    public final AccessibilityCallbacks mCallbacks;
    public final Context mContext;
    public final ShellExecutor mMainExcutor;
    public final PipMotionHelper mMotionHelper;
    public final PipBoundsState mPipBoundsState;
    public final PipSnapAlgorithm mSnapAlgorithm;
    public final PipTaskOrganizer mTaskOrganizer;
    public final Runnable mUnstashCallback;
    public final Runnable mUpdateMovementBoundCallback;
    public final Rect mNormalBounds = new Rect();
    public final Rect mExpandedBounds = new Rect();
    public final Rect mNormalMovementBounds = new Rect();
    public final Rect mExpandedMovementBounds = new Rect();
    public Rect mTmpBounds = new Rect();
    public final PipAccessibilityInteractionConnectionImpl mConnectionImpl = new PipAccessibilityInteractionConnectionImpl();

    /* loaded from: classes.dex */
    public interface AccessibilityCallbacks {
    }

    /* loaded from: classes.dex */
    public class PipAccessibilityInteractionConnectionImpl extends IAccessibilityInteractionConnection.Stub {
        public final void clearAccessibilityFocus() throws RemoteException {
        }

        public final void findAccessibilityNodeInfoByAccessibilityId(final long j, final Region region, final int i, final IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, final int i2, final int i3, final long j2, final MagnificationSpec magnificationSpec, final Bundle bundle) throws RemoteException {
            PipAccessibilityInteractionConnection.this.mMainExcutor.execute(new Runnable(j, region, i, iAccessibilityInteractionConnectionCallback, i2, i3, j2, magnificationSpec, bundle) { // from class: com.android.wm.shell.pip.phone.PipAccessibilityInteractionConnection$PipAccessibilityInteractionConnectionImpl$$ExternalSyntheticLambda1
                public final /* synthetic */ long f$1;
                public final /* synthetic */ int f$3;
                public final /* synthetic */ IAccessibilityInteractionConnectionCallback f$4;

                {
                    this.f$3 = i;
                    this.f$4 = iAccessibilityInteractionConnectionCallback;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    List<AccessibilityNodeInfo> list;
                    PipAccessibilityInteractionConnection.PipAccessibilityInteractionConnectionImpl pipAccessibilityInteractionConnectionImpl = PipAccessibilityInteractionConnection.PipAccessibilityInteractionConnectionImpl.this;
                    long j3 = this.f$1;
                    int i4 = this.f$3;
                    IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback2 = this.f$4;
                    Objects.requireNonNull(pipAccessibilityInteractionConnectionImpl);
                    PipAccessibilityInteractionConnection pipAccessibilityInteractionConnection = PipAccessibilityInteractionConnection.this;
                    Objects.requireNonNull(pipAccessibilityInteractionConnection);
                    try {
                        if (j3 == AccessibilityNodeInfo.ROOT_NODE_ID) {
                            list = pipAccessibilityInteractionConnection.getNodeList();
                        } else {
                            list = null;
                        }
                        iAccessibilityInteractionConnectionCallback2.setFindAccessibilityNodeInfosResult(list, i4);
                    } catch (RemoteException unused) {
                    }
                }
            });
        }

        public final void findAccessibilityNodeInfosByText(final long j, final String str, final Region region, final int i, final IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, final int i2, final int i3, final long j2, final MagnificationSpec magnificationSpec) throws RemoteException {
            PipAccessibilityInteractionConnection.this.mMainExcutor.execute(new Runnable(j, str, region, i, iAccessibilityInteractionConnectionCallback, i2, i3, j2, magnificationSpec) { // from class: com.android.wm.shell.pip.phone.PipAccessibilityInteractionConnection$PipAccessibilityInteractionConnectionImpl$$ExternalSyntheticLambda5
                public final /* synthetic */ int f$4;
                public final /* synthetic */ IAccessibilityInteractionConnectionCallback f$5;

                {
                    this.f$4 = i;
                    this.f$5 = iAccessibilityInteractionConnectionCallback;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    PipAccessibilityInteractionConnection.PipAccessibilityInteractionConnectionImpl pipAccessibilityInteractionConnectionImpl = PipAccessibilityInteractionConnection.PipAccessibilityInteractionConnectionImpl.this;
                    int i4 = this.f$4;
                    IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback2 = this.f$5;
                    Objects.requireNonNull(pipAccessibilityInteractionConnectionImpl);
                    Objects.requireNonNull(PipAccessibilityInteractionConnection.this);
                    try {
                        iAccessibilityInteractionConnectionCallback2.setFindAccessibilityNodeInfoResult((AccessibilityNodeInfo) null, i4);
                    } catch (RemoteException unused) {
                    }
                }
            });
        }

        public final void findAccessibilityNodeInfosByViewId(final long j, final String str, final Region region, final int i, final IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, final int i2, final int i3, final long j2, final MagnificationSpec magnificationSpec) throws RemoteException {
            PipAccessibilityInteractionConnection.this.mMainExcutor.execute(new Runnable(j, str, region, i, iAccessibilityInteractionConnectionCallback, i2, i3, j2, magnificationSpec) { // from class: com.android.wm.shell.pip.phone.PipAccessibilityInteractionConnection$PipAccessibilityInteractionConnectionImpl$$ExternalSyntheticLambda4
                public final /* synthetic */ int f$4;
                public final /* synthetic */ IAccessibilityInteractionConnectionCallback f$5;

                {
                    this.f$4 = i;
                    this.f$5 = iAccessibilityInteractionConnectionCallback;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    PipAccessibilityInteractionConnection.PipAccessibilityInteractionConnectionImpl pipAccessibilityInteractionConnectionImpl = PipAccessibilityInteractionConnection.PipAccessibilityInteractionConnectionImpl.this;
                    int i4 = this.f$4;
                    IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback2 = this.f$5;
                    Objects.requireNonNull(pipAccessibilityInteractionConnectionImpl);
                    Objects.requireNonNull(PipAccessibilityInteractionConnection.this);
                    try {
                        iAccessibilityInteractionConnectionCallback2.setFindAccessibilityNodeInfoResult((AccessibilityNodeInfo) null, i4);
                    } catch (RemoteException unused) {
                    }
                }
            });
        }

        public final void findFocus(final long j, final int i, final Region region, final int i2, final IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, final int i3, final int i4, final long j2, final MagnificationSpec magnificationSpec) throws RemoteException {
            PipAccessibilityInteractionConnection.this.mMainExcutor.execute(new Runnable(j, i, region, i2, iAccessibilityInteractionConnectionCallback, i3, i4, j2, magnificationSpec) { // from class: com.android.wm.shell.pip.phone.PipAccessibilityInteractionConnection$PipAccessibilityInteractionConnectionImpl$$ExternalSyntheticLambda2
                public final /* synthetic */ int f$4;
                public final /* synthetic */ IAccessibilityInteractionConnectionCallback f$5;

                {
                    this.f$4 = i2;
                    this.f$5 = iAccessibilityInteractionConnectionCallback;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    PipAccessibilityInteractionConnection.PipAccessibilityInteractionConnectionImpl pipAccessibilityInteractionConnectionImpl = PipAccessibilityInteractionConnection.PipAccessibilityInteractionConnectionImpl.this;
                    int i5 = this.f$4;
                    IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback2 = this.f$5;
                    Objects.requireNonNull(pipAccessibilityInteractionConnectionImpl);
                    Objects.requireNonNull(PipAccessibilityInteractionConnection.this);
                    try {
                        iAccessibilityInteractionConnectionCallback2.setFindAccessibilityNodeInfoResult((AccessibilityNodeInfo) null, i5);
                    } catch (RemoteException unused) {
                    }
                }
            });
        }

        public final void focusSearch(final long j, final int i, final Region region, final int i2, final IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, final int i3, final int i4, final long j2, final MagnificationSpec magnificationSpec) throws RemoteException {
            PipAccessibilityInteractionConnection.this.mMainExcutor.execute(new Runnable(j, i, region, i2, iAccessibilityInteractionConnectionCallback, i3, i4, j2, magnificationSpec) { // from class: com.android.wm.shell.pip.phone.PipAccessibilityInteractionConnection$PipAccessibilityInteractionConnectionImpl$$ExternalSyntheticLambda3
                public final /* synthetic */ int f$4;
                public final /* synthetic */ IAccessibilityInteractionConnectionCallback f$5;

                {
                    this.f$4 = i2;
                    this.f$5 = iAccessibilityInteractionConnectionCallback;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    PipAccessibilityInteractionConnection.PipAccessibilityInteractionConnectionImpl pipAccessibilityInteractionConnectionImpl = PipAccessibilityInteractionConnection.PipAccessibilityInteractionConnectionImpl.this;
                    int i5 = this.f$4;
                    IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback2 = this.f$5;
                    Objects.requireNonNull(pipAccessibilityInteractionConnectionImpl);
                    Objects.requireNonNull(PipAccessibilityInteractionConnection.this);
                    try {
                        iAccessibilityInteractionConnectionCallback2.setFindAccessibilityNodeInfoResult((AccessibilityNodeInfo) null, i5);
                    } catch (RemoteException unused) {
                    }
                }
            });
        }

        public final void notifyOutsideTouch() throws RemoteException {
        }

        public final void performAccessibilityAction(final long j, final int i, final Bundle bundle, final int i2, final IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, final int i3, final int i4, final long j2) throws RemoteException {
            PipAccessibilityInteractionConnection.this.mMainExcutor.execute(new Runnable(j, i, bundle, i2, iAccessibilityInteractionConnectionCallback, i3, i4, j2) { // from class: com.android.wm.shell.pip.phone.PipAccessibilityInteractionConnection$PipAccessibilityInteractionConnectionImpl$$ExternalSyntheticLambda0
                public final /* synthetic */ long f$1;
                public final /* synthetic */ int f$2;
                public final /* synthetic */ Bundle f$3;
                public final /* synthetic */ int f$4;
                public final /* synthetic */ IAccessibilityInteractionConnectionCallback f$5;

                @Override // java.lang.Runnable
                public final void run() {
                    int i5;
                    int i6;
                    PipAccessibilityInteractionConnection.PipAccessibilityInteractionConnectionImpl pipAccessibilityInteractionConnectionImpl = PipAccessibilityInteractionConnection.PipAccessibilityInteractionConnectionImpl.this;
                    long j3 = this.f$1;
                    int i7 = this.f$2;
                    Bundle bundle2 = this.f$3;
                    int i8 = this.f$4;
                    IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback2 = this.f$5;
                    Objects.requireNonNull(pipAccessibilityInteractionConnectionImpl);
                    PipAccessibilityInteractionConnection pipAccessibilityInteractionConnection = PipAccessibilityInteractionConnection.this;
                    Objects.requireNonNull(pipAccessibilityInteractionConnection);
                    boolean z = true;
                    try {
                        if (j3 == AccessibilityNodeInfo.ROOT_NODE_ID) {
                            if (i7 == 2131427442) {
                                if (pipAccessibilityInteractionConnection.mPipBoundsState.getBounds().width() == pipAccessibilityInteractionConnection.mNormalBounds.width() && pipAccessibilityInteractionConnection.mPipBoundsState.getBounds().height() == pipAccessibilityInteractionConnection.mNormalBounds.height()) {
                                    PipSnapAlgorithm pipSnapAlgorithm = pipAccessibilityInteractionConnection.mSnapAlgorithm;
                                    Rect bounds = pipAccessibilityInteractionConnection.mPipBoundsState.getBounds();
                                    Rect rect = pipAccessibilityInteractionConnection.mNormalMovementBounds;
                                    Objects.requireNonNull(pipSnapAlgorithm);
                                    float snapFraction = pipSnapAlgorithm.getSnapFraction(bounds, rect, 0);
                                    PipSnapAlgorithm pipSnapAlgorithm2 = pipAccessibilityInteractionConnection.mSnapAlgorithm;
                                    Rect rect2 = pipAccessibilityInteractionConnection.mExpandedBounds;
                                    Rect rect3 = pipAccessibilityInteractionConnection.mExpandedMovementBounds;
                                    Objects.requireNonNull(pipSnapAlgorithm2);
                                    PipSnapAlgorithm.applySnapFraction(rect2, rect3, snapFraction);
                                    PipTaskOrganizer pipTaskOrganizer = pipAccessibilityInteractionConnection.mTaskOrganizer;
                                    Rect rect4 = pipAccessibilityInteractionConnection.mExpandedBounds;
                                    WMShell$$ExternalSyntheticLambda6 wMShell$$ExternalSyntheticLambda6 = new WMShell$$ExternalSyntheticLambda6(pipAccessibilityInteractionConnection, 3);
                                    Objects.requireNonNull(pipTaskOrganizer);
                                    pipTaskOrganizer.scheduleFinishResizePip(rect4, 0, wMShell$$ExternalSyntheticLambda6);
                                } else {
                                    PipSnapAlgorithm pipSnapAlgorithm3 = pipAccessibilityInteractionConnection.mSnapAlgorithm;
                                    Rect bounds2 = pipAccessibilityInteractionConnection.mPipBoundsState.getBounds();
                                    Rect rect5 = pipAccessibilityInteractionConnection.mExpandedMovementBounds;
                                    Objects.requireNonNull(pipSnapAlgorithm3);
                                    float snapFraction2 = pipSnapAlgorithm3.getSnapFraction(bounds2, rect5, 0);
                                    PipSnapAlgorithm pipSnapAlgorithm4 = pipAccessibilityInteractionConnection.mSnapAlgorithm;
                                    Rect rect6 = pipAccessibilityInteractionConnection.mNormalBounds;
                                    Rect rect7 = pipAccessibilityInteractionConnection.mNormalMovementBounds;
                                    Objects.requireNonNull(pipSnapAlgorithm4);
                                    PipSnapAlgorithm.applySnapFraction(rect6, rect7, snapFraction2);
                                    PipTaskOrganizer pipTaskOrganizer2 = pipAccessibilityInteractionConnection.mTaskOrganizer;
                                    Rect rect8 = pipAccessibilityInteractionConnection.mNormalBounds;
                                    WMShell$$ExternalSyntheticLambda7 wMShell$$ExternalSyntheticLambda7 = new WMShell$$ExternalSyntheticLambda7(pipAccessibilityInteractionConnection, 3);
                                    Objects.requireNonNull(pipTaskOrganizer2);
                                    pipTaskOrganizer2.scheduleFinishResizePip(rect8, 0, wMShell$$ExternalSyntheticLambda7);
                                }
                            } else if (i7 == 2131427443) {
                                PipMotionHelper pipMotionHelper = pipAccessibilityInteractionConnection.mMotionHelper;
                                Objects.requireNonNull(pipMotionHelper);
                                Rect rect9 = new Rect();
                                PipBoundsState pipBoundsState = pipMotionHelper.mPipBoundsState;
                                Objects.requireNonNull(pipBoundsState);
                                DisplayLayout displayLayout = pipBoundsState.mDisplayLayout;
                                Objects.requireNonNull(displayLayout);
                                Rect rect10 = displayLayout.mStableInsets;
                                int i9 = pipMotionHelper.mPipBoundsState.getBounds().left;
                                PipBoundsState pipBoundsState2 = pipMotionHelper.mPipBoundsState;
                                Objects.requireNonNull(pipBoundsState2);
                                if (i9 == pipBoundsState2.mMovementBounds.left) {
                                    i5 = 1;
                                } else {
                                    i5 = 2;
                                }
                                if (i5 == 1) {
                                    PipBoundsState pipBoundsState3 = pipMotionHelper.mPipBoundsState;
                                    Objects.requireNonNull(pipBoundsState3);
                                    i6 = (pipBoundsState3.mStashOffset - pipMotionHelper.mPipBoundsState.getBounds().width()) + rect10.left;
                                } else {
                                    int i10 = pipMotionHelper.mPipBoundsState.getDisplayBounds().right;
                                    PipBoundsState pipBoundsState4 = pipMotionHelper.mPipBoundsState;
                                    Objects.requireNonNull(pipBoundsState4);
                                    i6 = (i10 - pipBoundsState4.mStashOffset) - rect10.right;
                                }
                                float f = i6;
                                rect9.set((int) f, pipMotionHelper.mPipBoundsState.getBounds().top, (int) (f + pipMotionHelper.mPipBoundsState.getBounds().width()), pipMotionHelper.mPipBoundsState.getBounds().bottom);
                                pipMotionHelper.mPipTaskOrganizer.scheduleAnimateResizePip(rect9, 250, 8, null);
                                PipBoundsState pipBoundsState5 = pipMotionHelper.mPipBoundsState;
                                Objects.requireNonNull(pipBoundsState5);
                                PipBoundsState.MotionBoundsState motionBoundsState = pipBoundsState5.mMotionBoundsState;
                                Objects.requireNonNull(motionBoundsState);
                                motionBoundsState.mAnimatingToBounds.set(rect9);
                                pipMotionHelper.mFloatingContentCoordinator.onContentMoved(pipMotionHelper);
                                pipMotionHelper.mPipBoundsState.setStashed(i5);
                            } else if (i7 == 2131427444) {
                                pipAccessibilityInteractionConnection.mUnstashCallback.run();
                                pipAccessibilityInteractionConnection.mPipBoundsState.setStashed(0);
                            } else if (i7 == 16) {
                                PipTouchHandler$$ExternalSyntheticLambda2 pipTouchHandler$$ExternalSyntheticLambda2 = (PipTouchHandler$$ExternalSyntheticLambda2) pipAccessibilityInteractionConnection.mCallbacks;
                                Objects.requireNonNull(pipTouchHandler$$ExternalSyntheticLambda2);
                                PipTouchHandler pipTouchHandler = (PipTouchHandler) pipTouchHandler$$ExternalSyntheticLambda2.f$0;
                                Objects.requireNonNull(pipTouchHandler);
                                pipTouchHandler.mMenuController.showMenu(1, pipTouchHandler.mPipBoundsState.getBounds(), true, pipTouchHandler.willResizeMenu());
                            } else if (i7 == 262144) {
                                PipMotionHelper pipMotionHelper2 = pipAccessibilityInteractionConnection.mMotionHelper;
                                Objects.requireNonNull(pipMotionHelper2);
                                pipMotionHelper2.expandLeavePip(false, false);
                            } else if (i7 == 1048576) {
                                pipAccessibilityInteractionConnection.mMotionHelper.dismissPip();
                            } else if (i7 == 16908354) {
                                int i11 = bundle2.getInt("ACTION_ARGUMENT_MOVE_WINDOW_X");
                                int i12 = bundle2.getInt("ACTION_ARGUMENT_MOVE_WINDOW_Y");
                                new Rect().set(pipAccessibilityInteractionConnection.mPipBoundsState.getBounds());
                                pipAccessibilityInteractionConnection.mTmpBounds.offsetTo(i11, i12);
                                PipMotionHelper pipMotionHelper3 = pipAccessibilityInteractionConnection.mMotionHelper;
                                Rect rect11 = pipAccessibilityInteractionConnection.mTmpBounds;
                                Objects.requireNonNull(pipMotionHelper3);
                                pipMotionHelper3.movePip(rect11, false);
                            }
                            iAccessibilityInteractionConnectionCallback2.setPerformAccessibilityActionResult(z, i8);
                            return;
                        }
                        iAccessibilityInteractionConnectionCallback2.setPerformAccessibilityActionResult(z, i8);
                        return;
                    } catch (RemoteException unused) {
                        return;
                    }
                    z = false;
                }
            });
        }

        public PipAccessibilityInteractionConnectionImpl() {
        }
    }

    public final List<AccessibilityNodeInfo> getNodeList() {
        if (this.mAccessibilityNodeInfoList == null) {
            this.mAccessibilityNodeInfoList = new ArrayList(1);
        }
        Context context = this.mContext;
        AccessibilityNodeInfo obtain = AccessibilityNodeInfo.obtain();
        obtain.setSourceNodeId(AccessibilityNodeInfo.ROOT_NODE_ID, -3);
        obtain.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK);
        obtain.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_DISMISS);
        obtain.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_MOVE_WINDOW);
        obtain.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_EXPAND);
        obtain.addAction(new AccessibilityNodeInfo.AccessibilityAction(2131427442, context.getString(2131951667)));
        obtain.addAction(new AccessibilityNodeInfo.AccessibilityAction(2131427443, context.getString(2131951668)));
        obtain.addAction(new AccessibilityNodeInfo.AccessibilityAction(2131427444, context.getString(2131951669)));
        obtain.setImportantForAccessibility(true);
        obtain.setClickable(true);
        obtain.setVisibleToUser(true);
        this.mAccessibilityNodeInfoList.clear();
        this.mAccessibilityNodeInfoList.add(obtain);
        return this.mAccessibilityNodeInfoList;
    }

    public PipAccessibilityInteractionConnection(Context context, PipBoundsState pipBoundsState, PipMotionHelper pipMotionHelper, PipTaskOrganizer pipTaskOrganizer, PipSnapAlgorithm pipSnapAlgorithm, PipTouchHandler$$ExternalSyntheticLambda2 pipTouchHandler$$ExternalSyntheticLambda2, ScrimView$$ExternalSyntheticLambda0 scrimView$$ExternalSyntheticLambda0, KeyguardUpdateMonitor$$ExternalSyntheticLambda6 keyguardUpdateMonitor$$ExternalSyntheticLambda6, ShellExecutor shellExecutor) {
        this.mContext = context;
        this.mMainExcutor = shellExecutor;
        this.mPipBoundsState = pipBoundsState;
        this.mMotionHelper = pipMotionHelper;
        this.mTaskOrganizer = pipTaskOrganizer;
        this.mSnapAlgorithm = pipSnapAlgorithm;
        this.mUpdateMovementBoundCallback = scrimView$$ExternalSyntheticLambda0;
        this.mUnstashCallback = keyguardUpdateMonitor$$ExternalSyntheticLambda6;
        this.mCallbacks = pipTouchHandler$$ExternalSyntheticLambda2;
    }
}
