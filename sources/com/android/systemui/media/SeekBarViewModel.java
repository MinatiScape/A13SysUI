package com.android.systemui.media;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.SystemClock;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.SeekBar;
import androidx.core.graphics.Insets$$ExternalSyntheticOutline0;
import androidx.core.view.GestureDetectorCompat;
import androidx.lifecycle.MutableLiveData;
import com.android.systemui.media.SeekBarViewModel;
import com.android.systemui.util.concurrency.RepeatableExecutor;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SeekBarViewModel.kt */
/* loaded from: classes.dex */
public final class SeekBarViewModel {
    public final MutableLiveData<Progress> _progress;
    public final RepeatableExecutor bgExecutor;
    public Runnable cancel;
    public MediaController controller;
    public boolean isFalseSeek;
    public Function0<Unit> logSmartspaceClick;
    public PlaybackState playbackState;
    public boolean scrubbing;
    public Progress _data = new Progress(false, false, null, 0);
    public SeekBarViewModel$callback$1 callback = new MediaController.Callback() { // from class: com.android.systemui.media.SeekBarViewModel$callback$1
        @Override // android.media.session.MediaController.Callback
        public final void onPlaybackStateChanged(PlaybackState playbackState) {
            SeekBarViewModel.this.playbackState = playbackState;
            if (playbackState != null) {
                Integer num = 0;
                if (!num.equals(SeekBarViewModel.this.playbackState)) {
                    SeekBarViewModel.this.checkIfPollingNeeded();
                    return;
                }
            }
            SeekBarViewModel seekBarViewModel = SeekBarViewModel.this;
            Objects.requireNonNull(seekBarViewModel);
            seekBarViewModel.bgExecutor.execute(new SeekBarViewModel$clearController$1(seekBarViewModel));
        }

        @Override // android.media.session.MediaController.Callback
        public final void onSessionDestroyed() {
            SeekBarViewModel seekBarViewModel = SeekBarViewModel.this;
            Objects.requireNonNull(seekBarViewModel);
            seekBarViewModel.bgExecutor.execute(new SeekBarViewModel$clearController$1(seekBarViewModel));
        }
    };
    public boolean listening = true;

    /* compiled from: SeekBarViewModel.kt */
    /* loaded from: classes.dex */
    public static final class Progress {
        public final int duration;
        public final Integer elapsedTime;
        public final boolean enabled;
        public final boolean seekAvailable;

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Progress)) {
                return false;
            }
            Progress progress = (Progress) obj;
            return this.enabled == progress.enabled && this.seekAvailable == progress.seekAvailable && Intrinsics.areEqual(this.elapsedTime, progress.elapsedTime) && this.duration == progress.duration;
        }

        public final int hashCode() {
            boolean z = this.enabled;
            int i = 1;
            if (z) {
                z = true;
            }
            int i2 = z ? 1 : 0;
            int i3 = z ? 1 : 0;
            int i4 = i2 * 31;
            boolean z2 = this.seekAvailable;
            if (!z2) {
                i = z2 ? 1 : 0;
            }
            int i5 = (i4 + i) * 31;
            Integer num = this.elapsedTime;
            return Integer.hashCode(this.duration) + ((i5 + (num == null ? 0 : num.hashCode())) * 31);
        }

        public static Progress copy$default(Progress progress, Integer num, int i) {
            boolean z;
            boolean z2;
            int i2 = 0;
            if ((i & 1) != 0) {
                z = progress.enabled;
            } else {
                z = false;
            }
            if ((i & 2) != 0) {
                z2 = progress.seekAvailable;
            } else {
                z2 = false;
            }
            if ((i & 4) != 0) {
                num = progress.elapsedTime;
            }
            if ((i & 8) != 0) {
                i2 = progress.duration;
            }
            Objects.requireNonNull(progress);
            return new Progress(z, z2, num, i2);
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Progress(enabled=");
            m.append(this.enabled);
            m.append(", seekAvailable=");
            m.append(this.seekAvailable);
            m.append(", elapsedTime=");
            m.append(this.elapsedTime);
            m.append(", duration=");
            return Insets$$ExternalSyntheticOutline0.m(m, this.duration, ')');
        }

        public Progress(boolean z, boolean z2, Integer num, int i) {
            this.enabled = z;
            this.seekAvailable = z2;
            this.elapsedTime = num;
            this.duration = i;
        }
    }

    /* compiled from: SeekBarViewModel.kt */
    /* loaded from: classes.dex */
    public static final class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        public final SeekBarViewModel viewModel;

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public final void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            if (z) {
                final SeekBarViewModel seekBarViewModel = this.viewModel;
                final long j = i;
                Objects.requireNonNull(seekBarViewModel);
                seekBarViewModel.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.media.SeekBarViewModel$onSeekProgress$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        SeekBarViewModel seekBarViewModel2 = SeekBarViewModel.this;
                        if (seekBarViewModel2.scrubbing) {
                            SeekBarViewModel.Progress copy$default = SeekBarViewModel.Progress.copy$default(seekBarViewModel2._data, Integer.valueOf((int) j), 11);
                            seekBarViewModel2._data = copy$default;
                            seekBarViewModel2._progress.postValue(copy$default);
                        }
                    }
                });
            }
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public final void onStartTrackingTouch(SeekBar seekBar) {
            final SeekBarViewModel seekBarViewModel = this.viewModel;
            Objects.requireNonNull(seekBarViewModel);
            seekBarViewModel.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.media.SeekBarViewModel$onSeekStarting$1
                @Override // java.lang.Runnable
                public final void run() {
                    SeekBarViewModel seekBarViewModel2 = SeekBarViewModel.this;
                    Objects.requireNonNull(seekBarViewModel2);
                    if (!seekBarViewModel2.scrubbing) {
                        seekBarViewModel2.scrubbing = true;
                        seekBarViewModel2.checkIfPollingNeeded();
                    }
                    SeekBarViewModel.this.isFalseSeek = false;
                }
            });
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public final void onStopTrackingTouch(SeekBar seekBar) {
            final SeekBarViewModel seekBarViewModel = this.viewModel;
            final long progress = seekBar.getProgress();
            Objects.requireNonNull(seekBarViewModel);
            seekBarViewModel.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.media.SeekBarViewModel$onSeek$1
                @Override // java.lang.Runnable
                public final void run() {
                    MediaController.TransportControls transportControls;
                    SeekBarViewModel seekBarViewModel2 = SeekBarViewModel.this;
                    if (seekBarViewModel2.isFalseSeek) {
                        if (seekBarViewModel2.scrubbing) {
                            seekBarViewModel2.scrubbing = false;
                            seekBarViewModel2.checkIfPollingNeeded();
                        }
                        SeekBarViewModel.access$checkPlaybackPosition(SeekBarViewModel.this);
                        return;
                    }
                    Function0<Unit> function0 = seekBarViewModel2.logSmartspaceClick;
                    if (function0 == null) {
                        function0 = null;
                    }
                    function0.invoke();
                    MediaController mediaController = SeekBarViewModel.this.controller;
                    if (!(mediaController == null || (transportControls = mediaController.getTransportControls()) == null)) {
                        transportControls.seekTo(progress);
                    }
                    SeekBarViewModel seekBarViewModel3 = SeekBarViewModel.this;
                    seekBarViewModel3.playbackState = null;
                    if (seekBarViewModel3.scrubbing) {
                        seekBarViewModel3.scrubbing = false;
                        seekBarViewModel3.checkIfPollingNeeded();
                    }
                }
            });
        }

        public SeekBarChangeListener(SeekBarViewModel seekBarViewModel) {
            this.viewModel = seekBarViewModel;
        }
    }

    /* compiled from: SeekBarViewModel.kt */
    /* loaded from: classes.dex */
    public static final class SeekBarTouchListener implements View.OnTouchListener, GestureDetector.OnGestureListener {
        public final SeekBar bar;
        public final GestureDetectorCompat detector;
        public final int flingVelocity;
        public boolean shouldGoToSeekBar;
        public final SeekBarViewModel viewModel;

        @Override // android.view.GestureDetector.OnGestureListener
        public final void onLongPress(MotionEvent motionEvent) {
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public final void onShowPress(MotionEvent motionEvent) {
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public final boolean onSingleTapUp(MotionEvent motionEvent) {
            this.shouldGoToSeekBar = true;
            return true;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public final boolean onDown(MotionEvent motionEvent) {
            double d;
            double d2;
            boolean z;
            ViewParent parent;
            int paddingLeft = this.bar.getPaddingLeft();
            int paddingRight = this.bar.getPaddingRight();
            int progress = this.bar.getProgress();
            int max = this.bar.getMax() - this.bar.getMin();
            if (max > 0) {
                d = (progress - this.bar.getMin()) / max;
            } else {
                d = 0.0d;
            }
            int width = (this.bar.getWidth() - paddingLeft) - paddingRight;
            if (this.bar.isLayoutRtl()) {
                d2 = ((1 - d) * width) + paddingLeft;
            } else {
                d2 = (width * d) + paddingLeft;
            }
            long height = this.bar.getHeight() / 2;
            int round = (int) (Math.round(d2) - height);
            int round2 = (int) (Math.round(d2) + height);
            int round3 = Math.round(motionEvent.getX());
            if (round3 < round || round3 > round2) {
                z = false;
            } else {
                z = true;
            }
            this.shouldGoToSeekBar = z;
            if (z && (parent = this.bar.getParent()) != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
            return this.shouldGoToSeekBar;
        }

        @Override // android.view.View.OnTouchListener
        public final boolean onTouch(View view, MotionEvent motionEvent) {
            if (!Intrinsics.areEqual(view, this.bar)) {
                return false;
            }
            this.detector.onTouchEvent(motionEvent);
            return !this.shouldGoToSeekBar;
        }

        public SeekBarTouchListener(SeekBarViewModel seekBarViewModel, SeekBar seekBar) {
            this.viewModel = seekBarViewModel;
            this.bar = seekBar;
            this.detector = new GestureDetectorCompat(seekBar.getContext(), this);
            this.flingVelocity = ViewConfiguration.get(seekBar.getContext()).getScaledMinimumFlingVelocity() * 10;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public final boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (Math.abs(f) > this.flingVelocity || Math.abs(f2) > this.flingVelocity) {
                final SeekBarViewModel seekBarViewModel = this.viewModel;
                Objects.requireNonNull(seekBarViewModel);
                seekBarViewModel.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.media.SeekBarViewModel$onSeekFalse$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        SeekBarViewModel seekBarViewModel2 = SeekBarViewModel.this;
                        if (seekBarViewModel2.scrubbing) {
                            seekBarViewModel2.isFalseSeek = true;
                        }
                    }
                });
            }
            return this.shouldGoToSeekBar;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public final boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return this.shouldGoToSeekBar;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x0026, code lost:
        if (r0 != false) goto L_0x002a;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.media.SeekBarViewModel$checkIfPollingNeeded$1] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void checkIfPollingNeeded() {
        /*
            r5 = this;
            boolean r0 = r5.listening
            r1 = 1
            r2 = 0
            if (r0 == 0) goto L_0x0029
            boolean r0 = r5.scrubbing
            if (r0 != 0) goto L_0x0029
            android.media.session.PlaybackState r0 = r5.playbackState
            if (r0 != 0) goto L_0x0010
        L_0x000e:
            r0 = r2
            goto L_0x0026
        L_0x0010:
            int r3 = r0.getState()
            r4 = 3
            if (r3 == r4) goto L_0x0025
            int r3 = r0.getState()
            r4 = 4
            if (r3 == r4) goto L_0x0025
            int r0 = r0.getState()
            r3 = 5
            if (r0 != r3) goto L_0x000e
        L_0x0025:
            r0 = r1
        L_0x0026:
            if (r0 == 0) goto L_0x0029
            goto L_0x002a
        L_0x0029:
            r1 = r2
        L_0x002a:
            if (r1 == 0) goto L_0x003e
            java.lang.Runnable r0 = r5.cancel
            if (r0 != 0) goto L_0x0049
            com.android.systemui.util.concurrency.RepeatableExecutor r0 = r5.bgExecutor
            com.android.systemui.media.SeekBarViewModel$checkIfPollingNeeded$1 r1 = new com.android.systemui.media.SeekBarViewModel$checkIfPollingNeeded$1
            r1.<init>()
            java.lang.Runnable r0 = r0.m146executeRepeatedly(r1)
            r5.cancel = r0
            goto L_0x0049
        L_0x003e:
            java.lang.Runnable r0 = r5.cancel
            if (r0 != 0) goto L_0x0043
            goto L_0x0046
        L_0x0043:
            r0.run()
        L_0x0046:
            r0 = 0
            r5.cancel = r0
        L_0x0049:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.SeekBarViewModel.checkIfPollingNeeded():void");
    }

    public final void setController(MediaController mediaController) {
        MediaSession.Token token;
        MediaController mediaController2 = this.controller;
        MediaSession.Token token2 = null;
        if (mediaController2 == null) {
            token = null;
        } else {
            token = mediaController2.getSessionToken();
        }
        if (mediaController != null) {
            token2 = mediaController.getSessionToken();
        }
        if (!Intrinsics.areEqual(token, token2)) {
            MediaController mediaController3 = this.controller;
            if (mediaController3 != null) {
                mediaController3.unregisterCallback(this.callback);
            }
            if (mediaController != null) {
                mediaController.registerCallback(this.callback);
            }
            this.controller = mediaController;
        }
    }

    /* JADX WARN: Type inference failed for: r3v3, types: [com.android.systemui.media.SeekBarViewModel$callback$1] */
    public SeekBarViewModel(RepeatableExecutor repeatableExecutor) {
        this.bgExecutor = repeatableExecutor;
        MutableLiveData<Progress> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.postValue(this._data);
        this._progress = mutableLiveData;
    }

    public static final void access$checkPlaybackPosition(SeekBarViewModel seekBarViewModel) {
        Integer num;
        boolean z;
        Objects.requireNonNull(seekBarViewModel);
        Progress progress = seekBarViewModel._data;
        Objects.requireNonNull(progress);
        int i = progress.duration;
        PlaybackState playbackState = seekBarViewModel.playbackState;
        if (playbackState == null) {
            num = null;
        } else {
            long j = i;
            long position = playbackState.getPosition();
            if (playbackState.getState() == 3 || playbackState.getState() == 4 || playbackState.getState() == 5) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                long lastPositionUpdateTime = playbackState.getLastPositionUpdateTime();
                long elapsedRealtime = SystemClock.elapsedRealtime();
                if (lastPositionUpdateTime > 0) {
                    long position2 = playbackState.getPosition() + (playbackState.getPlaybackSpeed() * ((float) (elapsedRealtime - lastPositionUpdateTime)));
                    if (j < 0 || position2 <= j) {
                        if (position2 < 0) {
                            j = 0;
                        } else {
                            j = position2;
                        }
                    }
                    position = j;
                }
            }
            num = Integer.valueOf((int) position);
        }
        if (num != null) {
            Progress progress2 = seekBarViewModel._data;
            Objects.requireNonNull(progress2);
            if (!Intrinsics.areEqual(progress2.elapsedTime, num)) {
                Progress copy$default = Progress.copy$default(seekBarViewModel._data, num, 11);
                seekBarViewModel._data = copy$default;
                seekBarViewModel._progress.postValue(copy$default);
            }
        }
    }
}
