package com.android.wm.shell.bubbles;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Rect;
import android.util.IndentingPrintWriter;
import android.view.View;
import com.android.keyguard.LockIconView$$ExternalSyntheticOutline0;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.ExpandableViewState;
import com.android.wm.shell.TaskView;
import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.bubbles.animation.ExpandedAnimationController;
import com.android.wm.shell.bubbles.animation.StackAnimationController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ int $r8$classId = 1;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ FileDescriptor f$1;
    public final /* synthetic */ PrintWriter f$2;
    public final /* synthetic */ String[] f$3;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda2(ExpandableView expandableView, IndentingPrintWriter indentingPrintWriter, FileDescriptor fileDescriptor, String[] strArr) {
        this.f$0 = expandableView;
        this.f$2 = indentingPrintWriter;
        this.f$1 = fileDescriptor;
        this.f$3 = strArr;
    }

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda2(BubbleController.BubblesImpl bubblesImpl, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        this.f$0 = bubblesImpl;
        this.f$1 = fileDescriptor;
        this.f$2 = printWriter;
        this.f$3 = strArr;
    }

    @Override // java.lang.Runnable
    public final void run() {
        String str;
        Object[] objArr;
        String str2;
        String str3;
        switch (this.$r8$classId) {
            case 0:
                BubbleController.BubblesImpl bubblesImpl = (BubbleController.BubblesImpl) this.f$0;
                PrintWriter printWriter = this.f$2;
                Objects.requireNonNull(bubblesImpl);
                BubbleController bubbleController = BubbleController.this;
                Objects.requireNonNull(bubbleController);
                printWriter.println("BubbleController state:");
                BubbleData bubbleData = bubbleController.mBubbleData;
                Objects.requireNonNull(bubbleData);
                printWriter.print("selected: ");
                BubbleViewProvider bubbleViewProvider = bubbleData.mSelectedBubble;
                if (bubbleViewProvider != null) {
                    str = bubbleViewProvider.getKey();
                } else {
                    str = "null";
                }
                printWriter.println(str);
                printWriter.print("expanded: ");
                printWriter.println(bubbleData.mExpanded);
                printWriter.print("stack bubble count:    ");
                printWriter.println(bubbleData.mBubbles.size());
                Iterator it = bubbleData.mBubbles.iterator();
                while (it.hasNext()) {
                    ((Bubble) it.next()).dump(printWriter);
                }
                printWriter.print("overflow bubble count:    ");
                printWriter.println(bubbleData.mOverflowBubbles.size());
                Iterator it2 = bubbleData.mOverflowBubbles.iterator();
                while (it2.hasNext()) {
                    ((Bubble) it2.next()).dump(printWriter);
                }
                printWriter.print("summaryKeys: ");
                printWriter.println(bubbleData.mSuppressedGroupKeys.size());
                Iterator<String> it3 = bubbleData.mSuppressedGroupKeys.keySet().iterator();
                while (it3.hasNext()) {
                    printWriter.println("   suppressing: " + it3.next());
                }
                printWriter.println();
                BubbleStackView bubbleStackView = bubbleController.mStackView;
                if (bubbleStackView != null) {
                    printWriter.println("Stack view state:");
                    ArrayList arrayList = new ArrayList();
                    for (int i = 0; i < bubbleStackView.getBubbleCount(); i++) {
                        View childAt = bubbleStackView.mBubbleContainer.getChildAt(i);
                        if (childAt instanceof BadgedImageView) {
                            BadgedImageView badgedImageView = (BadgedImageView) childAt;
                            Objects.requireNonNull(badgedImageView);
                            BubbleViewProvider bubbleViewProvider2 = badgedImageView.mBubble;
                            if (bubbleViewProvider2 != null) {
                                str3 = bubbleViewProvider2.getKey();
                            } else {
                                str3 = null;
                            }
                            arrayList.add(bubbleStackView.mBubbleData.getBubbleInStackWithKey(str3));
                        }
                    }
                    BubbleViewProvider expandedBubble = bubbleStackView.getExpandedBubble();
                    StringBuilder sb = new StringBuilder();
                    Iterator it4 = arrayList.iterator();
                    while (it4.hasNext()) {
                        Bubble bubble = (Bubble) it4.next();
                        if (bubble == null) {
                            sb.append("   <null> !!!!!\n");
                        } else {
                            if (expandedBubble == null || expandedBubble.getKey() == "Overflow" || bubble != expandedBubble) {
                                objArr = null;
                            } else {
                                objArr = 1;
                            }
                            if (objArr != null) {
                                str2 = "=>";
                            } else {
                                str2 = "  ";
                            }
                            sb.append(String.format("%s Bubble{act=%12d, showInShade=%d, key=%s}\n", str2, Long.valueOf(Math.max(bubble.mLastUpdated, bubble.mLastAccessed)), Integer.valueOf(bubble.showInShade() ? 1 : 0), bubble.mKey));
                        }
                    }
                    String sb2 = sb.toString();
                    printWriter.print("  bubbles on screen:       ");
                    printWriter.println(sb2);
                    printWriter.print("  gestureInProgress:       ");
                    printWriter.println(bubbleStackView.mIsGestureInProgress);
                    printWriter.print("  showingDismiss:          ");
                    DismissView dismissView = bubbleStackView.mDismissView;
                    Objects.requireNonNull(dismissView);
                    printWriter.println(dismissView.isShowing);
                    printWriter.print("  isExpansionAnimating:    ");
                    printWriter.println(bubbleStackView.mIsExpansionAnimating);
                    printWriter.print("  expandedContainerVis:    ");
                    printWriter.println(bubbleStackView.mExpandedViewContainer.getVisibility());
                    printWriter.print("  expandedContainerAlpha:  ");
                    printWriter.println(bubbleStackView.mExpandedViewContainer.getAlpha());
                    printWriter.print("  expandedContainerMatrix: ");
                    printWriter.println(bubbleStackView.mExpandedViewContainer.getAnimationMatrix());
                    StackAnimationController stackAnimationController = bubbleStackView.mStackAnimationController;
                    Objects.requireNonNull(stackAnimationController);
                    printWriter.println("StackAnimationController state:");
                    printWriter.print("  isActive:             ");
                    printWriter.println(stackAnimationController.isActiveController());
                    printWriter.print("  restingStackPos:      ");
                    printWriter.println(stackAnimationController.mPositioner.getRestingPosition().toString());
                    printWriter.print("  currentStackPos:      ");
                    printWriter.println(stackAnimationController.mStackPosition.toString());
                    printWriter.print("  isMovingFromFlinging: ");
                    printWriter.println(stackAnimationController.mIsMovingFromFlinging);
                    printWriter.print("  withinDismiss:        ");
                    printWriter.println(stackAnimationController.isStackStuckToTarget());
                    printWriter.print("  firstBubbleSpringing: ");
                    printWriter.println(stackAnimationController.mFirstBubbleSpringingToTouch);
                    ExpandedAnimationController expandedAnimationController = bubbleStackView.mExpandedAnimationController;
                    Objects.requireNonNull(expandedAnimationController);
                    printWriter.println("ExpandedAnimationController state:");
                    printWriter.print("  isActive:          ");
                    printWriter.println(expandedAnimationController.isActiveController());
                    printWriter.print("  animatingExpand:   ");
                    printWriter.println(expandedAnimationController.mAnimatingExpand);
                    printWriter.print("  animatingCollapse: ");
                    printWriter.println(expandedAnimationController.mAnimatingCollapse);
                    printWriter.print("  springingBubble:   ");
                    printWriter.println(expandedAnimationController.mSpringingBubbleToTouch);
                    if (bubbleStackView.mExpandedBubble != null) {
                        StringBuilder m = LockIconView$$ExternalSyntheticOutline0.m(printWriter, "Expanded bubble state:", "  expandedBubbleKey: ");
                        m.append(bubbleStackView.mExpandedBubble.getKey());
                        printWriter.println(m.toString());
                        BubbleExpandedView expandedView = bubbleStackView.mExpandedBubble.getExpandedView();
                        if (expandedView != null) {
                            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("  expandedViewVis:    ");
                            m2.append(expandedView.getVisibility());
                            printWriter.println(m2.toString());
                            printWriter.println("  expandedViewAlpha:  " + expandedView.getAlpha());
                            printWriter.println("  expandedViewTaskId: " + expandedView.mTaskId);
                            TaskView taskView = expandedView.mTaskView;
                            if (taskView != null) {
                                StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("  activityViewVis:    ");
                                m3.append(taskView.getVisibility());
                                printWriter.println(m3.toString());
                                printWriter.println("  activityViewAlpha:  " + taskView.getAlpha());
                            } else {
                                printWriter.println("  activityView is null");
                            }
                        } else {
                            printWriter.println("Expanded bubble view state: expanded bubble view is null");
                        }
                    } else {
                        printWriter.println("Expanded bubble state: expanded bubble is null");
                    }
                }
                printWriter.println();
                BubbleController.BubblesImpl.CachedState cachedState = bubblesImpl.mCachedState;
                Objects.requireNonNull(cachedState);
                synchronized (cachedState) {
                    printWriter.println("BubbleImpl.CachedState state:");
                    printWriter.println("mIsStackExpanded: " + cachedState.mIsStackExpanded);
                    printWriter.println("mSelectedBubbleKey: " + cachedState.mSelectedBubbleKey);
                    printWriter.print("mSuppressedBubbleKeys: ");
                    printWriter.println(cachedState.mSuppressedBubbleKeys.size());
                    Iterator<String> it5 = cachedState.mSuppressedBubbleKeys.iterator();
                    while (it5.hasNext()) {
                        printWriter.println("   suppressing: " + it5.next());
                    }
                    printWriter.print("mSuppressedGroupToNotifKeys: ");
                    printWriter.println(cachedState.mSuppressedGroupToNotifKeys.size());
                    Iterator<String> it6 = cachedState.mSuppressedGroupToNotifKeys.keySet().iterator();
                    while (it6.hasNext()) {
                        printWriter.println("   suppressing: " + it6.next());
                    }
                }
                return;
            default:
                ExpandableView expandableView = (ExpandableView) this.f$0;
                PrintWriter printWriter2 = (IndentingPrintWriter) this.f$2;
                FileDescriptor fileDescriptor = this.f$1;
                String[] strArr = this.f$3;
                Rect rect = ExpandableView.mClipRect;
                Objects.requireNonNull(expandableView);
                ExpandableViewState expandableViewState = expandableView.mViewState;
                if (expandableViewState == null) {
                    printWriter2.println("no viewState!!!");
                    return;
                }
                expandableViewState.dump(fileDescriptor, printWriter2, strArr);
                printWriter2.println();
                return;
        }
    }
}
