package com.android.systemui.wmshell;

import com.android.systemui.dock.DockManager;
import com.android.systemui.shared.navigationbar.RegionSamplingHelper$$ExternalSyntheticOutline0;
import com.android.systemui.wmshell.BubblesManager;
import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.bubbles.BubbleEntry;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.phone.PhonePipMenuController;
import com.android.wm.shell.pip.phone.PipController;
import com.android.wm.shell.pip.phone.PipInputConsumer;
import com.android.wm.shell.pip.phone.PipResizeGestureHandler;
import com.android.wm.shell.pip.phone.PipTouchHandler;
import com.android.wm.shell.pip.phone.PipTouchState;
import com.google.android.systemui.dreamliner.DockObserver;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BubblesManager$5$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ BubblesManager$5$$ExternalSyntheticLambda3(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        boolean z;
        switch (this.$r8$classId) {
            case 0:
                BubblesManager.AnonymousClass5 r0 = (BubblesManager.AnonymousClass5) this.f$0;
                String str = (String) this.f$1;
                Objects.requireNonNull(r0);
                Iterator it = BubblesManager.this.mCallbacks.iterator();
                while (it.hasNext()) {
                    ((BubblesManager.NotifCallback) it.next()).invalidateNotifications(str);
                }
                return;
            case 1:
                BubbleController.BubblesImpl bubblesImpl = (BubbleController.BubblesImpl) this.f$0;
                Objects.requireNonNull(bubblesImpl);
                BubbleController.this.expandStackAndSelectBubble((BubbleEntry) this.f$1);
                return;
            case 2:
                PipController.PipImpl pipImpl = (PipController.PipImpl) this.f$0;
                PrintWriter printWriter = (PrintWriter) this.f$1;
                Objects.requireNonNull(pipImpl);
                PipController pipController = PipController.this;
                Objects.requireNonNull(pipController);
                printWriter.println("PipController");
                PhonePipMenuController phonePipMenuController = pipController.mMenuController;
                Objects.requireNonNull(phonePipMenuController);
                printWriter.println("  PhonePipMenuController");
                printWriter.println("    mMenuState=" + phonePipMenuController.mMenuState);
                printWriter.println("    mPipMenuView=" + phonePipMenuController.mPipMenuView);
                printWriter.println("    mListeners=" + phonePipMenuController.mListeners.size());
                PipTouchHandler pipTouchHandler = pipController.mTouchHandler;
                Objects.requireNonNull(pipTouchHandler);
                printWriter.println("  PipTouchHandler");
                printWriter.println("    mMenuState=" + pipTouchHandler.mMenuState);
                StringBuilder sb = new StringBuilder();
                sb.append("    ");
                sb.append("mIsImeShowing=");
                StringBuilder m = RegionSamplingHelper$$ExternalSyntheticOutline0.m(sb, pipTouchHandler.mIsImeShowing, printWriter, "    ", "mImeHeight=");
                m.append(pipTouchHandler.mImeHeight);
                printWriter.println(m.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("    ");
                sb2.append("mIsShelfShowing=");
                StringBuilder m2 = RegionSamplingHelper$$ExternalSyntheticOutline0.m(sb2, pipTouchHandler.mIsShelfShowing, printWriter, "    ", "mShelfHeight=");
                m2.append(pipTouchHandler.mShelfHeight);
                printWriter.println(m2.toString());
                printWriter.println("    mSavedSnapFraction=" + pipTouchHandler.mSavedSnapFraction);
                printWriter.println("    mMovementBoundsExtraOffsets=" + pipTouchHandler.mMovementBoundsExtraOffsets);
                pipTouchHandler.mPipBoundsAlgorithm.dump(printWriter, "    ");
                PipTouchState pipTouchState = pipTouchHandler.mTouchState;
                Objects.requireNonNull(pipTouchState);
                printWriter.println("    PipTouchState");
                StringBuilder sb3 = new StringBuilder();
                sb3.append("      ");
                sb3.append("mAllowTouches=");
                StringBuilder m3 = RegionSamplingHelper$$ExternalSyntheticOutline0.m(sb3, pipTouchState.mAllowTouches, printWriter, "      ", "mActivePointerId=");
                m3.append(pipTouchState.mActivePointerId);
                printWriter.println(m3.toString());
                printWriter.println("      mLastTouchDisplayId=" + pipTouchState.mLastTouchDisplayId);
                printWriter.println("      mDownTouch=" + pipTouchState.mDownTouch);
                printWriter.println("      mDownDelta=" + pipTouchState.mDownDelta);
                printWriter.println("      mLastTouch=" + pipTouchState.mLastTouch);
                printWriter.println("      mLastDelta=" + pipTouchState.mLastDelta);
                printWriter.println("      mVelocity=" + pipTouchState.mVelocity);
                StringBuilder sb4 = new StringBuilder();
                sb4.append("      ");
                sb4.append("mIsUserInteracting=");
                StringBuilder m4 = RegionSamplingHelper$$ExternalSyntheticOutline0.m(RegionSamplingHelper$$ExternalSyntheticOutline0.m(RegionSamplingHelper$$ExternalSyntheticOutline0.m(sb4, pipTouchState.mIsUserInteracting, printWriter, "      ", "mIsDragging="), pipTouchState.mIsDragging, printWriter, "      ", "mStartedDragging="), pipTouchState.mStartedDragging, printWriter, "      ", "mAllowDraggingOffscreen=");
                m4.append(pipTouchState.mAllowDraggingOffscreen);
                printWriter.println(m4.toString());
                PipResizeGestureHandler pipResizeGestureHandler = pipTouchHandler.mPipResizeGestureHandler;
                if (pipResizeGestureHandler != null) {
                    printWriter.println("    PipResizeGestureHandler");
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("      ");
                    sb5.append("mAllowGesture=");
                    StringBuilder m5 = RegionSamplingHelper$$ExternalSyntheticOutline0.m(RegionSamplingHelper$$ExternalSyntheticOutline0.m(RegionSamplingHelper$$ExternalSyntheticOutline0.m(RegionSamplingHelper$$ExternalSyntheticOutline0.m(RegionSamplingHelper$$ExternalSyntheticOutline0.m(sb5, pipResizeGestureHandler.mAllowGesture, printWriter, "      ", "mIsAttached="), pipResizeGestureHandler.mIsAttached, printWriter, "      ", "mIsEnabled="), pipResizeGestureHandler.mIsEnabled, printWriter, "      ", "mEnablePinchResize="), pipResizeGestureHandler.mEnablePinchResize, printWriter, "      ", "mThresholdCrossed="), pipResizeGestureHandler.mThresholdCrossed, printWriter, "      ", "mOhmOffset=");
                    m5.append(pipResizeGestureHandler.mOhmOffset);
                    printWriter.println(m5.toString());
                }
                pipController.mPipBoundsAlgorithm.dump(printWriter, "  ");
                pipController.mPipTaskOrganizer.dump(printWriter, "  ");
                PipBoundsState pipBoundsState = pipController.mPipBoundsState;
                Objects.requireNonNull(pipBoundsState);
                printWriter.println("  PipBoundsState");
                printWriter.println("    mBounds=" + pipBoundsState.mBounds);
                printWriter.println("    mNormalBounds=" + pipBoundsState.mNormalBounds);
                printWriter.println("    mExpandedBounds=" + pipBoundsState.mExpandedBounds);
                printWriter.println("    mMovementBounds=" + pipBoundsState.mMovementBounds);
                printWriter.println("    mNormalMovementBounds=" + pipBoundsState.mNormalMovementBounds);
                printWriter.println("    mExpandedMovementBounds=" + pipBoundsState.mExpandedMovementBounds);
                printWriter.println("    mLastPipComponentName=" + pipBoundsState.mLastPipComponentName);
                printWriter.println("    mAspectRatio=" + pipBoundsState.mAspectRatio);
                printWriter.println("    mDisplayId=" + pipBoundsState.mDisplayId);
                printWriter.println("    mDisplayLayout=" + pipBoundsState.mDisplayLayout);
                printWriter.println("    mStashedState=" + pipBoundsState.mStashedState);
                printWriter.println("    mStashOffset=" + pipBoundsState.mStashOffset);
                printWriter.println("    mMinEdgeSize=" + pipBoundsState.mMinEdgeSize);
                printWriter.println("    mOverrideMinSize=" + pipBoundsState.mOverrideMinSize);
                StringBuilder sb6 = new StringBuilder();
                sb6.append("    ");
                sb6.append("mIsImeShowing=");
                StringBuilder m6 = RegionSamplingHelper$$ExternalSyntheticOutline0.m(sb6, pipBoundsState.mIsImeShowing, printWriter, "    ", "mImeHeight=");
                m6.append(pipBoundsState.mImeHeight);
                printWriter.println(m6.toString());
                StringBuilder sb7 = new StringBuilder();
                sb7.append("    ");
                sb7.append("mIsShelfShowing=");
                StringBuilder m7 = RegionSamplingHelper$$ExternalSyntheticOutline0.m(sb7, pipBoundsState.mIsShelfShowing, printWriter, "    ", "mShelfHeight=");
                m7.append(pipBoundsState.mShelfHeight);
                printWriter.println(m7.toString());
                PipBoundsState.PipReentryState pipReentryState = pipBoundsState.mPipReentryState;
                if (pipReentryState == null) {
                    printWriter.println("    mPipReentryState=null");
                } else {
                    printWriter.println("    PipReentryState");
                    printWriter.println("      mSize=" + pipReentryState.mSize);
                    printWriter.println("      mSnapFraction=" + pipReentryState.mSnapFraction);
                }
                PipBoundsState.MotionBoundsState motionBoundsState = pipBoundsState.mMotionBoundsState;
                Objects.requireNonNull(motionBoundsState);
                printWriter.println("    " + PipBoundsState.MotionBoundsState.class.getSimpleName());
                printWriter.println("      mBoundsInMotion=" + motionBoundsState.mBoundsInMotion);
                printWriter.println("      mAnimatingToBounds=" + motionBoundsState.mAnimatingToBounds);
                PipInputConsumer pipInputConsumer = pipController.mPipInputConsumer;
                Objects.requireNonNull(pipInputConsumer);
                printWriter.println("  PipInputConsumer");
                StringBuilder sb8 = new StringBuilder();
                sb8.append("    ");
                sb8.append("registered=");
                if (pipInputConsumer.mInputEventReceiver != null) {
                    z = true;
                } else {
                    z = false;
                }
                sb8.append(z);
                printWriter.println(sb8.toString());
                return;
            default:
                DockObserver dockObserver = (DockObserver) this.f$0;
                String str2 = DockObserver.ACTION_START_DREAMLINER_CONTROL_SERVICE;
                Objects.requireNonNull(dockObserver);
                dockObserver.dispatchDockEvent((DockManager.DockEventListener) this.f$1);
                return;
        }
    }
}
