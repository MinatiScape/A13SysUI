package com.android.systemui.classifier;

import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.classifier.HistoryTracker;
import com.android.systemui.util.time.SystemClock;
import com.android.wm.shell.dagger.WMShellBaseModule$$ExternalSyntheticLambda4;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
/* loaded from: classes.dex */
public final class HistoryTracker {
    public static final /* synthetic */ int $r8$clinit = 0;
    public static final double HISTORY_DECAY = Math.pow(10.0d, (Math.log10(0.1d) / 10000.0d) * 100.0d);
    public final SystemClock mSystemClock;
    public DelayQueue<CombinedResult> mResults = new DelayQueue<>();
    public final ArrayList mBeliefListeners = new ArrayList();

    /* loaded from: classes.dex */
    public interface BeliefListener {
        void onBeliefChanged(double d);
    }

    /* loaded from: classes.dex */
    public class CombinedResult implements Delayed {
        public final long mExpiryMs;
        public final double mScore;

        public CombinedResult(long j, double d) {
            this.mExpiryMs = j + 10000;
            this.mScore = d;
        }

        @Override // java.lang.Comparable
        public final int compareTo(Delayed delayed) {
            TimeUnit timeUnit = TimeUnit.MILLISECONDS;
            return Long.compare(getDelay(timeUnit), delayed.getDelay(timeUnit));
        }

        @Override // java.util.concurrent.Delayed
        public final long getDelay(TimeUnit timeUnit) {
            return timeUnit.convert(this.mExpiryMs - HistoryTracker.this.mSystemClock.uptimeMillis(), TimeUnit.MILLISECONDS);
        }
    }

    public final double falseBelief() {
        do {
        } while (this.mResults.poll() != null);
        if (this.mResults.isEmpty()) {
            return 0.5d;
        }
        final long uptimeMillis = this.mSystemClock.uptimeMillis();
        return ((Double) this.mResults.stream().map(new Function() { // from class: com.android.systemui.classifier.HistoryTracker$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                long j = uptimeMillis;
                HistoryTracker.CombinedResult combinedResult = (HistoryTracker.CombinedResult) obj;
                Objects.requireNonNull(combinedResult);
                return Double.valueOf((Math.pow(HistoryTracker.HISTORY_DECAY, (10000 - (combinedResult.mExpiryMs - j)) / 100.0d) * (combinedResult.mScore - 0.5d)) + 0.5d);
            }
        }).reduce(Double.valueOf(0.5d), HistoryTracker$$ExternalSyntheticLambda1.INSTANCE)).doubleValue();
    }

    public final double falseConfidence() {
        do {
        } while (this.mResults.poll() != null);
        if (this.mResults.isEmpty()) {
            return 0.0d;
        }
        final double doubleValue = ((Double) this.mResults.stream().map(WMShellBaseModule$$ExternalSyntheticLambda4.INSTANCE$1).reduce(Double.valueOf(0.0d), HistoryTracker$$ExternalSyntheticLambda0.INSTANCE)).doubleValue() / this.mResults.size();
        return 1.0d - Math.sqrt(((Double) this.mResults.stream().map(new Function() { // from class: com.android.systemui.classifier.HistoryTracker$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                double d = doubleValue;
                HistoryTracker.CombinedResult combinedResult = (HistoryTracker.CombinedResult) obj;
                Objects.requireNonNull(combinedResult);
                return Double.valueOf(Math.pow(combinedResult.mScore - d, 2.0d));
            }
        }).reduce(Double.valueOf(0.0d), HistoryTracker$$ExternalSyntheticLambda0.INSTANCE)).doubleValue() / this.mResults.size());
    }

    public HistoryTracker(SystemClock systemClock) {
        this.mSystemClock = systemClock;
    }

    public final void addResults(Collection<FalsingClassifier.Result> collection, long j) {
        double d;
        double d2 = 0.0d;
        for (FalsingClassifier.Result result : collection) {
            Objects.requireNonNull(result);
            if (result.mFalsed) {
                d = 0.5d;
            } else {
                d = -0.5d;
            }
            d2 += (d * result.mConfidence) + 0.5d;
        }
        double size = d2 / collection.size();
        if (size == 1.0d) {
            size = 0.99999d;
        } else if (size == 0.0d) {
            size = 1.0E-5d;
        }
        do {
        } while (this.mResults.poll() != null);
        this.mResults.add((DelayQueue<CombinedResult>) new CombinedResult(j, size));
        this.mBeliefListeners.forEach(new HistoryTracker$$ExternalSyntheticLambda2(this, 0));
    }
}
