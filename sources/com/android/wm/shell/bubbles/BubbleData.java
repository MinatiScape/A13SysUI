package com.android.wm.shell.bubbles;

import android.app.PendingIntent;
import android.content.Context;
import android.content.LocusId;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline1;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda2;
import com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda5;
import com.android.wm.shell.bubbles.BubbleLogger;
import com.android.wm.shell.bubbles.Bubbles;
import com.android.wm.shell.common.ShellExecutor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final class BubbleData {
    public static final Comparator<Bubble> BUBBLES_BY_SORT_KEY_DESCENDING = Comparator.comparing(BubbleData$$ExternalSyntheticLambda4.INSTANCE).reversed();
    public final ArrayList mBubbles;
    public Bubbles.PendingIntentCanceledListener mCancelledListener;
    public int mCurrentUserId;
    public boolean mExpanded;
    public Listener mListener;
    public BubbleLogger mLogger;
    public final Executor mMainExecutor;
    public int mMaxBubbles;
    public int mMaxOverflowBubbles;
    public boolean mNeedsTrimming;
    public final BubbleOverflow mOverflow;
    public final ArrayList mOverflowBubbles;
    public final BubblePositioner mPositioner;
    public BubbleViewProvider mSelectedBubble;
    public boolean mShowingOverflow;
    public Update mStateChange;
    public Bubbles.SuppressionChangedListener mSuppressionListener;
    public final ArrayMap<LocusId, Bubble> mSuppressedBubbles = new ArrayMap<>();
    public final ArraySet<LocusId> mVisibleLocusIds = new ArraySet<>();
    public TimeSource mTimeSource = BubbleData$$ExternalSyntheticLambda0.INSTANCE;
    public HashMap<String, String> mSuppressedGroupKeys = new HashMap<>();
    public final HashMap<String, Bubble> mPendingBubbles = new HashMap<>();

    /* loaded from: classes.dex */
    public interface Listener {
        void applyUpdate(Update update);
    }

    /* loaded from: classes.dex */
    public interface TimeSource {
    }

    /* loaded from: classes.dex */
    public static final class Update {
        public Bubble addedBubble;
        public Bubble addedOverflowBubble;
        public final List<Bubble> bubbles;
        public boolean expanded;
        public boolean expandedChanged;
        public boolean orderChanged;
        public final List<Bubble> overflowBubbles;
        public final ArrayList removedBubbles = new ArrayList();
        public Bubble removedOverflowBubble;
        public BubbleViewProvider selectedBubble;
        public boolean selectionChanged;
        public Bubble suppressedBubble;
        public boolean suppressedSummaryChanged;
        public String suppressedSummaryGroup;
        public Bubble unsuppressedBubble;
        public Bubble updatedBubble;

        public final void bubbleRemoved(Bubble bubble, int i) {
            this.removedBubbles.add(new Pair(bubble, Integer.valueOf(i)));
        }

        public Update(ArrayList arrayList, ArrayList arrayList2) {
            this.bubbles = Collections.unmodifiableList(arrayList);
            this.overflowBubbles = Collections.unmodifiableList(arrayList2);
        }
    }

    public final void doUnsuppress(Bubble bubble) {
        bubble.setSuppressBubble(false);
        this.mStateChange.unsuppressedBubble = bubble;
        this.mBubbles.add(bubble);
        if (this.mBubbles.size() > 1) {
            repackAll();
            this.mStateChange.orderChanged = true;
        }
        if (this.mBubbles.get(0) == bubble) {
            setNewSelectedIndex(0);
        }
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public Bubble getBubbleInStackWithKey(String str) {
        for (int i = 0; i < this.mBubbles.size(); i++) {
            Bubble bubble = (Bubble) this.mBubbles.get(i);
            Objects.requireNonNull(bubble);
            if (bubble.mKey.equals(str)) {
                return bubble;
            }
        }
        return null;
    }

    public final Bubble getBubbleWithView(View view) {
        for (int i = 0; i < this.mBubbles.size(); i++) {
            Bubble bubble = (Bubble) this.mBubbles.get(i);
            Objects.requireNonNull(bubble);
            BadgedImageView badgedImageView = bubble.mIconView;
            if (badgedImageView != null && badgedImageView.equals(view)) {
                return bubble;
            }
        }
        return null;
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public Bubble getOverflowBubbleWithKey(String str) {
        for (int i = 0; i < this.mOverflowBubbles.size(); i++) {
            Bubble bubble = (Bubble) this.mOverflowBubbles.get(i);
            Objects.requireNonNull(bubble);
            if (bubble.mKey.equals(str)) {
                return bubble;
            }
        }
        return null;
    }

    public static void performActionOnBubblesMatching(List list, Predicate predicate, Consumer consumer) {
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Bubble bubble = (Bubble) it.next();
            if (predicate.test(bubble)) {
                arrayList.add(bubble);
            }
        }
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            consumer.accept((Bubble) it2.next());
        }
    }

    public final void dismissAll(int i) {
        if (!this.mBubbles.isEmpty() || !this.mSuppressedBubbles.isEmpty()) {
            setExpandedInternal(false);
            setSelectedBubbleInternal(null);
            while (!this.mBubbles.isEmpty()) {
                Bubble bubble = (Bubble) this.mBubbles.get(0);
                Objects.requireNonNull(bubble);
                doRemove(bubble.mKey, i);
            }
            while (!this.mSuppressedBubbles.isEmpty()) {
                Bubble removeAt = this.mSuppressedBubbles.removeAt(0);
                Objects.requireNonNull(removeAt);
                doRemove(removeAt.mKey, i);
            }
            dispatchPendingChanges();
        }
    }

    public final void dispatchPendingChanges() {
        boolean z;
        if (this.mListener != null) {
            Update update = this.mStateChange;
            Objects.requireNonNull(update);
            if (!update.expandedChanged && !update.selectionChanged && update.addedBubble == null && update.updatedBubble == null && update.removedBubbles.isEmpty() && update.addedOverflowBubble == null && update.removedOverflowBubble == null && !update.orderChanged && update.suppressedBubble == null && update.unsuppressedBubble == null && !update.suppressedSummaryChanged && update.suppressedSummaryGroup == null) {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                this.mListener.applyUpdate(this.mStateChange);
            }
        }
        this.mStateChange = new Update(this.mBubbles, this.mOverflowBubbles);
    }

    public final void doRemove(String str, int i) {
        boolean z;
        PendingIntent pendingIntent;
        Bubble suppressedBubbleWithKey;
        BubbleViewInfoTask bubbleViewInfoTask;
        if (this.mPendingBubbles.containsKey(str)) {
            this.mPendingBubbles.remove(str);
        }
        if (i == 5 || i == 9 || i == 7 || i == 4 || i == 12 || i == 13 || i == 8) {
            z = true;
        } else {
            z = false;
        }
        int i2 = 0;
        while (true) {
            if (i2 >= this.mBubbles.size()) {
                i2 = -1;
                break;
            }
            Bubble bubble = (Bubble) this.mBubbles.get(i2);
            Objects.requireNonNull(bubble);
            if (bubble.mKey.equals(str)) {
                break;
            }
            i2++;
        }
        if (i2 == -1) {
            if (hasOverflowBubbleWithKey(str) && z) {
                Bubble overflowBubbleWithKey = getOverflowBubbleWithKey(str);
                if (!(overflowBubbleWithKey == null || (bubbleViewInfoTask = overflowBubbleWithKey.mInflationTask) == null)) {
                    bubbleViewInfoTask.cancel(true);
                }
                BubbleLogger bubbleLogger = this.mLogger;
                if (i == 5) {
                    bubbleLogger.log(overflowBubbleWithKey, BubbleLogger.Event.BUBBLE_OVERFLOW_REMOVE_CANCEL);
                } else if (i == 9) {
                    bubbleLogger.log(overflowBubbleWithKey, BubbleLogger.Event.BUBBLE_OVERFLOW_REMOVE_GROUP_CANCEL);
                } else if (i == 7) {
                    bubbleLogger.log(overflowBubbleWithKey, BubbleLogger.Event.BUBBLE_OVERFLOW_REMOVE_NO_LONGER_BUBBLE);
                } else if (i == 4) {
                    bubbleLogger.log(overflowBubbleWithKey, BubbleLogger.Event.BUBBLE_OVERFLOW_REMOVE_BLOCKED);
                } else {
                    Objects.requireNonNull(bubbleLogger);
                }
                this.mOverflowBubbles.remove(overflowBubbleWithKey);
                this.mStateChange.bubbleRemoved(overflowBubbleWithKey, i);
                this.mStateChange.removedOverflowBubble = overflowBubbleWithKey;
            }
            if (this.mSuppressedBubbles.values().stream().anyMatch(new BubbleData$$ExternalSyntheticLambda5(str, 0)) && z && (suppressedBubbleWithKey = getSuppressedBubbleWithKey(str)) != null) {
                this.mSuppressedBubbles.remove(suppressedBubbleWithKey.mLocusId);
                BubbleViewInfoTask bubbleViewInfoTask2 = suppressedBubbleWithKey.mInflationTask;
                if (bubbleViewInfoTask2 != null) {
                    bubbleViewInfoTask2.cancel(true);
                }
                this.mStateChange.bubbleRemoved(suppressedBubbleWithKey, i);
                return;
            }
            return;
        }
        Bubble bubble2 = (Bubble) this.mBubbles.get(i2);
        Objects.requireNonNull(bubble2);
        BubbleViewInfoTask bubbleViewInfoTask3 = bubble2.mInflationTask;
        if (bubbleViewInfoTask3 != null) {
            bubbleViewInfoTask3.cancel(true);
        }
        overflowBubble(i, bubble2);
        if (this.mBubbles.size() == 1) {
            setExpandedInternal(false);
            this.mSelectedBubble = null;
        }
        if (i2 < this.mBubbles.size() - 1) {
            this.mStateChange.orderChanged = true;
        }
        this.mBubbles.remove(i2);
        this.mStateChange.bubbleRemoved(bubble2, i);
        if (!this.mExpanded) {
            this.mStateChange.orderChanged |= repackAll();
        }
        if (Objects.equals(this.mSelectedBubble, bubble2)) {
            setNewSelectedIndex(i2);
        }
        if (i == 1 && (pendingIntent = bubble2.mDeleteIntent) != null) {
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException unused) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Failed to send delete intent for bubble with key: ");
                m.append(bubble2.mKey);
                Log.w("Bubbles", m.toString());
            }
        }
    }

    public final void doSuppress(Bubble bubble) {
        this.mStateChange.suppressedBubble = bubble;
        boolean z = true;
        bubble.setSuppressBubble(true);
        int indexOf = this.mBubbles.indexOf(bubble);
        Update update = this.mStateChange;
        if (this.mBubbles.size() - 1 == indexOf) {
            z = false;
        }
        update.orderChanged = z;
        this.mBubbles.remove(indexOf);
        if (!Objects.equals(this.mSelectedBubble, bubble)) {
            return;
        }
        if (this.mBubbles.isEmpty()) {
            this.mSelectedBubble = null;
        } else {
            setNewSelectedIndex(0);
        }
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public List<Bubble> getBubbles() {
        return Collections.unmodifiableList(this.mBubbles);
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x003d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.android.wm.shell.bubbles.Bubble getOrCreateBubble(com.android.wm.shell.bubbles.BubbleEntry r5, com.android.wm.shell.bubbles.Bubble r6) {
        /*
            r4 = this;
            if (r6 == 0) goto L_0x0005
            java.lang.String r0 = r6.mKey
            goto L_0x0009
        L_0x0005:
            java.lang.String r0 = r5.getKey()
        L_0x0009:
            com.android.wm.shell.bubbles.Bubble r1 = r4.getBubbleInStackWithKey(r0)
            if (r1 != 0) goto L_0x003a
            com.android.wm.shell.bubbles.Bubble r1 = r4.getOverflowBubbleWithKey(r0)
            if (r1 == 0) goto L_0x001b
            java.util.ArrayList r6 = r4.mOverflowBubbles
            r6.remove(r1)
            goto L_0x003a
        L_0x001b:
            java.util.HashMap<java.lang.String, com.android.wm.shell.bubbles.Bubble> r1 = r4.mPendingBubbles
            boolean r1 = r1.containsKey(r0)
            if (r1 == 0) goto L_0x002c
            java.util.HashMap<java.lang.String, com.android.wm.shell.bubbles.Bubble> r6 = r4.mPendingBubbles
            java.lang.Object r6 = r6.get(r0)
            com.android.wm.shell.bubbles.Bubble r6 = (com.android.wm.shell.bubbles.Bubble) r6
            goto L_0x003b
        L_0x002c:
            if (r5 == 0) goto L_0x003b
            com.android.wm.shell.bubbles.Bubble r6 = new com.android.wm.shell.bubbles.Bubble
            com.android.wm.shell.bubbles.Bubbles$SuppressionChangedListener r1 = r4.mSuppressionListener
            com.android.wm.shell.bubbles.Bubbles$PendingIntentCanceledListener r2 = r4.mCancelledListener
            java.util.concurrent.Executor r3 = r4.mMainExecutor
            r6.<init>(r5, r1, r2, r3)
            goto L_0x003b
        L_0x003a:
            r6 = r1
        L_0x003b:
            if (r5 == 0) goto L_0x0040
            r6.setEntry(r5)
        L_0x0040:
            java.util.HashMap<java.lang.String, com.android.wm.shell.bubbles.Bubble> r4 = r4.mPendingBubbles
            r4.put(r0, r6)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.bubbles.BubbleData.getOrCreateBubble(com.android.wm.shell.bubbles.BubbleEntry, com.android.wm.shell.bubbles.Bubble):com.android.wm.shell.bubbles.Bubble");
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public List<Bubble> getOverflowBubbles() {
        return Collections.unmodifiableList(this.mOverflowBubbles);
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public Bubble getSuppressedBubbleWithKey(String str) {
        for (Bubble bubble : this.mSuppressedBubbles.values()) {
            Objects.requireNonNull(bubble);
            if (bubble.mKey.equals(str)) {
                return bubble;
            }
        }
        return null;
    }

    public final boolean isShowingOverflow() {
        if (this.mShowingOverflow) {
            if (this.mExpanded) {
                return true;
            }
            Objects.requireNonNull(this.mPositioner);
        }
        return false;
    }

    @VisibleForTesting
    public boolean isSummarySuppressed(String str) {
        return this.mSuppressedGroupKeys.containsKey(str);
    }

    public final boolean repackAll() {
        if (this.mBubbles.isEmpty()) {
            return false;
        }
        ArrayList arrayList = new ArrayList(this.mBubbles.size());
        this.mBubbles.stream().sorted(BUBBLES_BY_SORT_KEY_DESCENDING).forEachOrdered(new WMShell$$ExternalSyntheticLambda2(arrayList, 5));
        if (arrayList.equals(this.mBubbles)) {
            return false;
        }
        this.mBubbles.clear();
        this.mBubbles.addAll(arrayList);
        return true;
    }

    public final void setExpandedInternal(boolean z) {
        if (this.mExpanded != z) {
            if (z) {
                if (!this.mBubbles.isEmpty() || this.mShowingOverflow) {
                    BubbleViewProvider bubbleViewProvider = this.mSelectedBubble;
                    if (bubbleViewProvider == null) {
                        Log.e("Bubbles", "Attempt to expand stack without selected bubble!");
                        return;
                    }
                    String key = bubbleViewProvider.getKey();
                    Objects.requireNonNull(this.mOverflow);
                    if (key.equals("Overflow") && !this.mBubbles.isEmpty()) {
                        setSelectedBubbleInternal((BubbleViewProvider) this.mBubbles.get(0));
                    }
                    BubbleViewProvider bubbleViewProvider2 = this.mSelectedBubble;
                    if (bubbleViewProvider2 instanceof Bubble) {
                        Bubble bubble = (Bubble) bubbleViewProvider2;
                        Objects.requireNonNull((BubbleData$$ExternalSyntheticLambda0) this.mTimeSource);
                        long currentTimeMillis = System.currentTimeMillis();
                        Objects.requireNonNull(bubble);
                        bubble.mLastAccessed = currentTimeMillis;
                        bubble.setSuppressNotification(true);
                        bubble.setShowDot(false);
                    }
                    this.mStateChange.orderChanged |= repackAll();
                } else {
                    Log.e("Bubbles", "Attempt to expand stack when empty!");
                    return;
                }
            } else if (!this.mBubbles.isEmpty()) {
                this.mStateChange.orderChanged |= repackAll();
                if (this.mBubbles.indexOf(this.mSelectedBubble) > 0 && this.mBubbles.indexOf(this.mSelectedBubble) != 0) {
                    this.mBubbles.remove((Bubble) this.mSelectedBubble);
                    this.mBubbles.add(0, (Bubble) this.mSelectedBubble);
                    this.mStateChange.orderChanged = true;
                }
            }
            if (this.mNeedsTrimming) {
                this.mNeedsTrimming = false;
                trim();
            }
            this.mExpanded = z;
            Update update = this.mStateChange;
            update.expanded = z;
            update.expandedChanged = true;
        }
    }

    public final void setNewSelectedIndex(int i) {
        if (this.mBubbles.isEmpty()) {
            GridLayoutManager$$ExternalSyntheticOutline1.m("Bubbles list empty when attempting to select index: ", i, "Bubbles");
            return;
        }
        setSelectedBubbleInternal((BubbleViewProvider) this.mBubbles.get(Math.min(i, this.mBubbles.size() - 1)));
    }

    public final void setSelectedBubbleInternal(BubbleViewProvider bubbleViewProvider) {
        boolean z;
        if (!Objects.equals(bubbleViewProvider, this.mSelectedBubble)) {
            if (bubbleViewProvider == null || !"Overflow".equals(bubbleViewProvider.getKey())) {
                z = false;
            } else {
                z = true;
            }
            if (bubbleViewProvider == null || this.mBubbles.contains(bubbleViewProvider) || this.mOverflowBubbles.contains(bubbleViewProvider) || z) {
                if (this.mExpanded && bubbleViewProvider != null && !z) {
                    Bubble bubble = (Bubble) bubbleViewProvider;
                    Objects.requireNonNull((BubbleData$$ExternalSyntheticLambda0) this.mTimeSource);
                    bubble.mLastAccessed = System.currentTimeMillis();
                    bubble.setSuppressNotification(true);
                    bubble.setShowDot(false);
                }
                this.mSelectedBubble = bubbleViewProvider;
                Update update = this.mStateChange;
                update.selectedBubble = bubbleViewProvider;
                update.selectionChanged = true;
                return;
            }
            Log.e("Bubbles", "Cannot select bubble which doesn't exist! (" + bubbleViewProvider + ") bubbles=" + this.mBubbles);
        }
    }

    public final void trim() {
        if (this.mBubbles.size() > this.mMaxBubbles) {
            final int size = this.mBubbles.size() - this.mMaxBubbles;
            final ArrayList arrayList = new ArrayList();
            this.mBubbles.stream().sorted(Comparator.comparingLong(BubbleData$$ExternalSyntheticLambda9.INSTANCE)).filter(new BubbleData$$ExternalSyntheticLambda6(this, 0)).forEachOrdered(new Consumer() { // from class: com.android.wm.shell.bubbles.BubbleData$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ArrayList arrayList2 = arrayList;
                    Bubble bubble = (Bubble) obj;
                    if (arrayList2.size() < size) {
                        arrayList2.add(bubble);
                    }
                }
            });
            arrayList.forEach(new WMShell$$ExternalSyntheticLambda5(this, 2));
        }
    }

    public BubbleData(Context context, BubbleLogger bubbleLogger, BubblePositioner bubblePositioner, ShellExecutor shellExecutor) {
        this.mLogger = bubbleLogger;
        this.mPositioner = bubblePositioner;
        this.mMainExecutor = shellExecutor;
        this.mOverflow = new BubbleOverflow(context, bubblePositioner);
        ArrayList arrayList = new ArrayList();
        this.mBubbles = arrayList;
        ArrayList arrayList2 = new ArrayList();
        this.mOverflowBubbles = arrayList2;
        this.mStateChange = new Update(arrayList, arrayList2);
        this.mMaxBubbles = bubblePositioner.mMaxBubbles;
        this.mMaxOverflowBubbles = context.getResources().getInteger(2131492873);
    }

    public final void dismissBubbleWithKey(String str, int i) {
        doRemove(str, i);
        dispatchPendingChanges();
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public Bubble getAnyBubbleWithkey(String str) {
        Bubble bubbleInStackWithKey = getBubbleInStackWithKey(str);
        if (bubbleInStackWithKey == null) {
            bubbleInStackWithKey = getOverflowBubbleWithKey(str);
        }
        if (bubbleInStackWithKey == null) {
            return getSuppressedBubbleWithKey(str);
        }
        return bubbleInStackWithKey;
    }

    public final boolean hasAnyBubbleWithKey(String str) {
        if (hasBubbleInStackWithKey(str) || hasOverflowBubbleWithKey(str) || this.mSuppressedBubbles.values().stream().anyMatch(new BubbleData$$ExternalSyntheticLambda5(str, 0))) {
            return true;
        }
        return false;
    }

    public final boolean hasBubbleInStackWithKey(String str) {
        if (getBubbleInStackWithKey(str) != null) {
            return true;
        }
        return false;
    }

    public final boolean hasOverflowBubbleWithKey(String str) {
        if (getOverflowBubbleWithKey(str) != null) {
            return true;
        }
        return false;
    }

    public final void overflowBubble(int i, Bubble bubble) {
        Objects.requireNonNull(bubble);
        if (bubble.mPendingIntentCanceled) {
            return;
        }
        if (i == 2 || i == 1 || i == 15) {
            BubbleLogger bubbleLogger = this.mLogger;
            if (i == 2) {
                bubbleLogger.log(bubble, BubbleLogger.Event.BUBBLE_OVERFLOW_ADD_AGED);
            } else if (i == 1) {
                bubbleLogger.log(bubble, BubbleLogger.Event.BUBBLE_OVERFLOW_ADD_USER_GESTURE);
            } else if (i == 15) {
                bubbleLogger.log(bubble, BubbleLogger.Event.BUBBLE_OVERFLOW_RECOVER);
            } else {
                Objects.requireNonNull(bubbleLogger);
            }
            this.mOverflowBubbles.remove(bubble);
            this.mOverflowBubbles.add(0, bubble);
            this.mStateChange.addedOverflowBubble = bubble;
            BubbleViewInfoTask bubbleViewInfoTask = bubble.mInflationTask;
            if (bubbleViewInfoTask != null) {
                bubbleViewInfoTask.cancel(true);
            }
            if (this.mOverflowBubbles.size() == this.mMaxOverflowBubbles + 1) {
                ArrayList arrayList = this.mOverflowBubbles;
                Bubble bubble2 = (Bubble) arrayList.get(arrayList.size() - 1);
                this.mStateChange.bubbleRemoved(bubble2, 11);
                this.mLogger.log(bubble, BubbleLogger.Event.BUBBLE_OVERFLOW_REMOVE_MAX_REACHED);
                this.mOverflowBubbles.remove(bubble2);
                this.mStateChange.removedOverflowBubble = bubble2;
            }
        }
    }

    public final void setExpanded(boolean z) {
        setExpandedInternal(z);
        dispatchPendingChanges();
    }

    public final void setSelectedBubble(BubbleViewProvider bubbleViewProvider) {
        setSelectedBubbleInternal(bubbleViewProvider);
        dispatchPendingChanges();
    }

    @VisibleForTesting
    public void setMaxOverflowBubbles(int i) {
        this.mMaxOverflowBubbles = i;
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public void setTimeSource(TimeSource timeSource) {
        this.mTimeSource = timeSource;
    }
}
