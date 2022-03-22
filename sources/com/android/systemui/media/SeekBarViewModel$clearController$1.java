package com.android.systemui.media;

import com.android.systemui.media.SeekBarViewModel;
/* compiled from: SeekBarViewModel.kt */
/* loaded from: classes.dex */
public final class SeekBarViewModel$clearController$1 implements Runnable {
    public final /* synthetic */ SeekBarViewModel this$0;

    public SeekBarViewModel$clearController$1(SeekBarViewModel seekBarViewModel) {
        this.this$0 = seekBarViewModel;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.this$0.setController(null);
        SeekBarViewModel seekBarViewModel = this.this$0;
        seekBarViewModel.playbackState = null;
        Runnable runnable = seekBarViewModel.cancel;
        if (runnable != null) {
            runnable.run();
        }
        SeekBarViewModel seekBarViewModel2 = this.this$0;
        seekBarViewModel2.cancel = null;
        SeekBarViewModel.Progress copy$default = SeekBarViewModel.Progress.copy$default(seekBarViewModel2._data, null, 14);
        seekBarViewModel2._data = copy$default;
        seekBarViewModel2._progress.postValue(copy$default);
    }
}
