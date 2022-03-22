package com.android.systemui.util.concurrency;

import com.android.systemui.media.SeekBarViewModel$checkIfPollingNeeded$1;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda17;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public interface RepeatableExecutor extends Executor {
    BubbleStackView$$ExternalSyntheticLambda17 executeRepeatedly(SeekBarViewModel$checkIfPollingNeeded$1 seekBarViewModel$checkIfPollingNeeded$1);

    /* renamed from: executeRepeatedly  reason: collision with other method in class */
    default Runnable m146executeRepeatedly(SeekBarViewModel$checkIfPollingNeeded$1 seekBarViewModel$checkIfPollingNeeded$1) {
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        return executeRepeatedly(seekBarViewModel$checkIfPollingNeeded$1);
    }
}
