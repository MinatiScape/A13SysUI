package com.android.systemui.media;
/* compiled from: SeekBarViewModel.kt */
/* loaded from: classes.dex */
public final class SeekBarViewModel$listening$1 implements Runnable {
    public final /* synthetic */ boolean $value;
    public final /* synthetic */ SeekBarViewModel this$0;

    public SeekBarViewModel$listening$1(SeekBarViewModel seekBarViewModel, boolean z) {
        this.this$0 = seekBarViewModel;
        this.$value = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        SeekBarViewModel seekBarViewModel = this.this$0;
        boolean z = seekBarViewModel.listening;
        boolean z2 = this.$value;
        if (z != z2) {
            seekBarViewModel.listening = z2;
            seekBarViewModel.checkIfPollingNeeded();
        }
    }
}
