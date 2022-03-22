package com.android.systemui.classifier;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.net.Uri;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda7;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;
/* loaded from: classes.dex */
public final class BrightLineFalsingManager implements FalsingManager {
    public static final boolean DEBUG = Log.isLoggable("FalsingManager", 3);
    public static final ArrayDeque RECENT_INFO_LOG = new ArrayDeque(41);
    public static final ArrayDeque RECENT_SWIPES = new ArrayDeque(21);
    public AccessibilityManager mAccessibilityManager;
    public final AnonymousClass2 mBeliefListener;
    public final Set mClassifiers;
    public final FalsingDataProvider mDataProvider;
    public boolean mDestroyed;
    public final DoubleTapClassifier mDoubleTapClassifier;
    public final AnonymousClass3 mGestureFinalizedListener;
    public final HistoryTracker mHistoryTracker;
    public final KeyguardStateController mKeyguardStateController;
    public final MetricsLogger mMetricsLogger;
    public Collection<FalsingClassifier.Result> mPriorResults;
    public final AnonymousClass1 mSessionListener;
    public final SingleTapClassifier mSingleTapClassifier;
    public final boolean mTestHarness;
    public final ArrayList mFalsingBeliefListeners = new ArrayList();
    public ArrayList mFalsingTapListeners = new ArrayList();
    public int mPriorInteractionType = 7;

    /* loaded from: classes.dex */
    public static class XYDt {
        public final int mDT;
        public final int mX;
        public final int mY;

        public final String toString() {
            return this.mX + "," + this.mY + "," + this.mDT;
        }

        public XYDt(int i, int i2, int i3) {
            this.mX = i;
            this.mY = i2;
            this.mDT = i3;
        }
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final void cleanupInternal() {
        this.mDestroyed = true;
        FalsingDataProvider falsingDataProvider = this.mDataProvider;
        AnonymousClass1 r1 = this.mSessionListener;
        Objects.requireNonNull(falsingDataProvider);
        falsingDataProvider.mSessionListeners.remove(r1);
        FalsingDataProvider falsingDataProvider2 = this.mDataProvider;
        AnonymousClass3 r12 = this.mGestureFinalizedListener;
        Objects.requireNonNull(falsingDataProvider2);
        falsingDataProvider2.mGestureFinalizedListeners.remove(r12);
        this.mClassifiers.forEach(BrightLineFalsingManager$$ExternalSyntheticLambda1.INSTANCE);
        this.mFalsingBeliefListeners.clear();
        HistoryTracker historyTracker = this.mHistoryTracker;
        AnonymousClass2 r2 = this.mBeliefListener;
        Objects.requireNonNull(historyTracker);
        historyTracker.mBeliefListeners.remove(r2);
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final boolean isClassifierEnabled() {
        return true;
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final boolean isReportingEnabled() {
        return false;
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final boolean isUnlockingDisabled() {
        return false;
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final void onSuccessfulUnlock() {
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final Uri reportRejectedTouch() {
        return null;
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final boolean shouldEnforceBouncer() {
        return false;
    }

    public final boolean skipFalsing(int i) {
        boolean z;
        if (i != 16 && this.mKeyguardStateController.isShowing() && !this.mTestHarness) {
            FalsingDataProvider falsingDataProvider = this.mDataProvider;
            Objects.requireNonNull(falsingDataProvider);
            if (!falsingDataProvider.mJustUnlockedWithFace) {
                FalsingDataProvider falsingDataProvider2 = this.mDataProvider;
                Objects.requireNonNull(falsingDataProvider2);
                if (falsingDataProvider2.mBatteryController.isWirelessCharging() || falsingDataProvider2.mDockManager.isDocked()) {
                    z = true;
                } else {
                    z = false;
                }
                if (!z && !this.mAccessibilityManager.isTouchExplorationEnabled()) {
                    return false;
                }
            }
        }
        return true;
    }

    /* loaded from: classes.dex */
    public static class DebugSwipeRecord {
        public final int mInteractionType;
        public final boolean mIsFalse;
        public final List<XYDt> mRecentMotionEvents;

        public DebugSwipeRecord(boolean z, int i, List<XYDt> list) {
            this.mIsFalse = z;
            this.mInteractionType = i;
            this.mRecentMotionEvents = list;
        }
    }

    public static void logDebug(String str) {
        if (DEBUG) {
            Log.d("FalsingManager", str, null);
        }
    }

    public static void logInfo(String str) {
        Log.i("FalsingManager", str);
        RECENT_INFO_LOG.add(str);
        while (true) {
            ArrayDeque arrayDeque = RECENT_INFO_LOG;
            if (arrayDeque.size() > 40) {
                arrayDeque.remove();
            } else {
                return;
            }
        }
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final void addFalsingBeliefListener(FalsingManager.FalsingBeliefListener falsingBeliefListener) {
        this.mFalsingBeliefListeners.add(falsingBeliefListener);
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final void addTapListener(FalsingManager.FalsingTapListener falsingTapListener) {
        this.mFalsingTapListeners.add(falsingTapListener);
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        int i;
        String str;
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("BRIGHTLINE FALSING MANAGER");
        indentingPrintWriter.print("classifierEnabled=");
        indentingPrintWriter.println(1);
        indentingPrintWriter.print("mJustUnlockedWithFace=");
        FalsingDataProvider falsingDataProvider = this.mDataProvider;
        Objects.requireNonNull(falsingDataProvider);
        indentingPrintWriter.println(falsingDataProvider.mJustUnlockedWithFace ? 1 : 0);
        indentingPrintWriter.print("isDocked=");
        FalsingDataProvider falsingDataProvider2 = this.mDataProvider;
        Objects.requireNonNull(falsingDataProvider2);
        if (falsingDataProvider2.mBatteryController.isWirelessCharging() || falsingDataProvider2.mDockManager.isDocked()) {
            i = 1;
        } else {
            i = 0;
        }
        indentingPrintWriter.println(i);
        indentingPrintWriter.print("width=");
        FalsingDataProvider falsingDataProvider3 = this.mDataProvider;
        Objects.requireNonNull(falsingDataProvider3);
        indentingPrintWriter.println(falsingDataProvider3.mWidthPixels);
        indentingPrintWriter.print("height=");
        FalsingDataProvider falsingDataProvider4 = this.mDataProvider;
        Objects.requireNonNull(falsingDataProvider4);
        indentingPrintWriter.println(falsingDataProvider4.mHeightPixels);
        indentingPrintWriter.println();
        ArrayDeque arrayDeque = RECENT_SWIPES;
        if (arrayDeque.size() != 0) {
            indentingPrintWriter.println("Recent swipes:");
            indentingPrintWriter.increaseIndent();
            Iterator it = arrayDeque.iterator();
            while (it.hasNext()) {
                DebugSwipeRecord debugSwipeRecord = (DebugSwipeRecord) it.next();
                Objects.requireNonNull(debugSwipeRecord);
                StringJoiner stringJoiner = new StringJoiner(",");
                StringJoiner add = stringJoiner.add(Integer.toString(1));
                if (debugSwipeRecord.mIsFalse) {
                    str = "1";
                } else {
                    str = "0";
                }
                add.add(str).add(Integer.toString(debugSwipeRecord.mInteractionType));
                for (XYDt xYDt : debugSwipeRecord.mRecentMotionEvents) {
                    stringJoiner.add(xYDt.toString());
                }
                indentingPrintWriter.println(stringJoiner.toString());
                indentingPrintWriter.println();
            }
            indentingPrintWriter.decreaseIndent();
        } else {
            indentingPrintWriter.println("No recent swipes");
        }
        indentingPrintWriter.println();
        indentingPrintWriter.println("Recent falsing info:");
        indentingPrintWriter.increaseIndent();
        Iterator it2 = RECENT_INFO_LOG.iterator();
        while (it2.hasNext()) {
            indentingPrintWriter.println((String) it2.next());
        }
        indentingPrintWriter.println();
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final boolean isFalseDoubleTap() {
        if (this.mDestroyed) {
            Log.wtf("FalsingManager", "Tried to use FalsingManager after being destroyed!");
        }
        if (skipFalsing(7)) {
            this.mPriorResults = Collections.singleton(FalsingClassifier.Result.passed(1.0d));
            logDebug("Skipped falsing");
            return false;
        }
        DoubleTapClassifier doubleTapClassifier = this.mDoubleTapClassifier;
        this.mHistoryTracker.falseBelief();
        this.mHistoryTracker.falseConfidence();
        Objects.requireNonNull(doubleTapClassifier);
        FalsingClassifier.Result calculateFalsingResult = doubleTapClassifier.calculateFalsingResult(7);
        this.mPriorResults = Collections.singleton(calculateFalsingResult);
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("False Double Tap: ");
        m.append(calculateFalsingResult.mFalsed);
        logDebug(m.toString());
        return calculateFalsingResult.mFalsed;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0056  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x005e  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0073  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00d5  */
    @Override // com.android.systemui.plugins.FalsingManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean isFalseTap(int r13) {
        /*
            Method dump skipped, instructions count: 239
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.classifier.BrightLineFalsingManager.isFalseTap(int):boolean");
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final boolean isFalseTouch(final int i) {
        if (this.mDestroyed) {
            Log.wtf("FalsingManager", "Tried to use FalsingManager after being destroyed!");
        }
        this.mPriorInteractionType = i;
        if (skipFalsing(i)) {
            this.mPriorResults = Collections.singleton(FalsingClassifier.Result.passed(1.0d));
            logDebug("Skipped falsing");
            return false;
        }
        final boolean[] zArr = {false};
        this.mPriorResults = (Collection) this.mClassifiers.stream().map(new Function() { // from class: com.android.systemui.classifier.BrightLineFalsingManager$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                BrightLineFalsingManager brightLineFalsingManager = BrightLineFalsingManager.this;
                int i2 = i;
                boolean[] zArr2 = zArr;
                FalsingClassifier falsingClassifier = (FalsingClassifier) obj;
                Objects.requireNonNull(brightLineFalsingManager);
                brightLineFalsingManager.mHistoryTracker.falseBelief();
                brightLineFalsingManager.mHistoryTracker.falseConfidence();
                Objects.requireNonNull(falsingClassifier);
                FalsingClassifier.Result calculateFalsingResult = falsingClassifier.calculateFalsingResult(i2);
                boolean z = zArr2[0];
                Objects.requireNonNull(calculateFalsingResult);
                zArr2[0] = z | calculateFalsingResult.mFalsed;
                return calculateFalsingResult;
            }
        }).collect(Collectors.toList());
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("False Gesture: ");
        m.append(zArr[0]);
        logDebug(m.toString());
        return zArr[0];
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final boolean isSimpleTap() {
        if (this.mDestroyed) {
            Log.wtf("FalsingManager", "Tried to use FalsingManager after being destroyed!");
        }
        SingleTapClassifier singleTapClassifier = this.mSingleTapClassifier;
        FalsingDataProvider falsingDataProvider = this.mDataProvider;
        Objects.requireNonNull(falsingDataProvider);
        FalsingClassifier.Result isTap = singleTapClassifier.isTap(falsingDataProvider.mRecentMotionEvents, 0.0d);
        this.mPriorResults = Collections.singleton(isTap);
        return !isTap.mFalsed;
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final void onProximityEvent(FalsingManager.ProximityEvent proximityEvent) {
        this.mClassifiers.forEach(new BubbleController$$ExternalSyntheticLambda7(proximityEvent, 1));
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final void removeFalsingBeliefListener(FalsingManager.FalsingBeliefListener falsingBeliefListener) {
        this.mFalsingBeliefListeners.remove(falsingBeliefListener);
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final void removeTapListener(FalsingManager.FalsingTapListener falsingTapListener) {
        this.mFalsingTapListeners.remove(falsingTapListener);
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.systemui.classifier.BrightLineFalsingManager$1, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r1v0, types: [com.android.systemui.classifier.BrightLineFalsingManager$2, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r2v0, types: [com.android.systemui.classifier.BrightLineFalsingManager$3, java.lang.Object] */
    /* JADX WARN: Unknown variable types count: 3 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public BrightLineFalsingManager(com.android.systemui.classifier.FalsingDataProvider r5, com.android.internal.logging.MetricsLogger r6, java.util.Set<com.android.systemui.classifier.FalsingClassifier> r7, com.android.systemui.classifier.SingleTapClassifier r8, com.android.systemui.classifier.DoubleTapClassifier r9, com.android.systemui.classifier.HistoryTracker r10, com.android.systemui.statusbar.policy.KeyguardStateController r11, android.view.accessibility.AccessibilityManager r12, boolean r13) {
        /*
            r4 = this;
            r4.<init>()
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r4.mFalsingBeliefListeners = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r4.mFalsingTapListeners = r0
            com.android.systemui.classifier.BrightLineFalsingManager$1 r0 = new com.android.systemui.classifier.BrightLineFalsingManager$1
            r0.<init>()
            r4.mSessionListener = r0
            com.android.systemui.classifier.BrightLineFalsingManager$2 r1 = new com.android.systemui.classifier.BrightLineFalsingManager$2
            r1.<init>()
            r4.mBeliefListener = r1
            com.android.systemui.classifier.BrightLineFalsingManager$3 r2 = new com.android.systemui.classifier.BrightLineFalsingManager$3
            r2.<init>()
            r4.mGestureFinalizedListener = r2
            r3 = 7
            r4.mPriorInteractionType = r3
            r4.mDataProvider = r5
            r4.mMetricsLogger = r6
            r4.mClassifiers = r7
            r4.mSingleTapClassifier = r8
            r4.mDoubleTapClassifier = r9
            r4.mHistoryTracker = r10
            r4.mKeyguardStateController = r11
            r4.mAccessibilityManager = r12
            r4.mTestHarness = r13
            java.util.Objects.requireNonNull(r5)
            java.util.ArrayList r4 = r5.mSessionListeners
            r4.add(r0)
            java.util.ArrayList r4 = r5.mGestureFinalizedListeners
            r4.add(r2)
            java.util.Objects.requireNonNull(r10)
            java.util.ArrayList r4 = r10.mBeliefListeners
            r4.add(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.classifier.BrightLineFalsingManager.<init>(com.android.systemui.classifier.FalsingDataProvider, com.android.internal.logging.MetricsLogger, java.util.Set, com.android.systemui.classifier.SingleTapClassifier, com.android.systemui.classifier.DoubleTapClassifier, com.android.systemui.classifier.HistoryTracker, com.android.systemui.statusbar.policy.KeyguardStateController, android.view.accessibility.AccessibilityManager, boolean):void");
    }
}
