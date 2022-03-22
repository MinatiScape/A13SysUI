package com.android.systemui.screenrecord;

import android.media.AudioFormat;
import android.media.AudioPlaybackCaptureConfiguration;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.projection.MediaProjection;
import android.util.Log;
import android.util.MathUtils;
import android.view.Surface;
import androidx.fragment.app.DialogFragment$$ExternalSyntheticOutline0;
import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline0;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ScreenInternalAudioRecorder {
    public AudioRecord mAudioRecord;
    public AudioRecord mAudioRecordMic;
    public MediaCodec mCodec;
    public MediaProjection mMediaProjection;
    public boolean mMic;
    public MediaMuxer mMuxer;
    public long mPresentationTime;
    public boolean mStarted;
    public Thread mThread;
    public long mTotalBytes;
    public Config mConfig = new Config();
    public int mTrackId = -1;

    /* loaded from: classes.dex */
    public static class Config {
        public final String toString() {
            return "channelMask=4\n   encoding=2\n sampleRate=44100\n bufferSize=131072\n privileged=true\n legacy app looback=false";
        }
    }

    public final void writeOutput() {
        while (true) {
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int dequeueOutputBuffer = this.mCodec.dequeueOutputBuffer(bufferInfo, 500L);
            if (dequeueOutputBuffer == -2) {
                this.mTrackId = this.mMuxer.addTrack(this.mCodec.getOutputFormat());
                this.mMuxer.start();
            } else if (dequeueOutputBuffer != -1 && this.mTrackId >= 0) {
                ByteBuffer outputBuffer = this.mCodec.getOutputBuffer(dequeueOutputBuffer);
                if ((bufferInfo.flags & 2) == 0 || bufferInfo.size == 0) {
                    this.mMuxer.writeSampleData(this.mTrackId, outputBuffer, bufferInfo);
                }
                this.mCodec.releaseOutputBuffer(dequeueOutputBuffer, false);
            } else {
                return;
            }
        }
    }

    public ScreenInternalAudioRecorder(String str, MediaProjection mediaProjection, boolean z) throws IOException {
        this.mMic = z;
        this.mMuxer = new MediaMuxer(str, 0);
        this.mMediaProjection = mediaProjection;
        DialogFragment$$ExternalSyntheticOutline0.m("creating audio file ", str, "ScreenAudioRecorder");
        Objects.requireNonNull(this.mConfig);
        Objects.requireNonNull(this.mConfig);
        Objects.requireNonNull(this.mConfig);
        final int minBufferSize = AudioRecord.getMinBufferSize(44100, 16, 2) * 2;
        Log.d("ScreenAudioRecorder", "audio buffer size: " + minBufferSize);
        AudioFormat.Builder builder = new AudioFormat.Builder();
        Objects.requireNonNull(this.mConfig);
        AudioFormat.Builder encoding = builder.setEncoding(2);
        Objects.requireNonNull(this.mConfig);
        AudioFormat.Builder sampleRate = encoding.setSampleRate(44100);
        Objects.requireNonNull(this.mConfig);
        AudioFormat build = sampleRate.setChannelMask(4).build();
        this.mAudioRecord = new AudioRecord.Builder().setAudioFormat(build).setAudioPlaybackCaptureConfig(new AudioPlaybackCaptureConfiguration.Builder(this.mMediaProjection).addMatchingUsage(1).addMatchingUsage(0).addMatchingUsage(14).build()).build();
        if (this.mMic) {
            Objects.requireNonNull(this.mConfig);
            Objects.requireNonNull(this.mConfig);
            this.mAudioRecordMic = new AudioRecord(7, 44100, 16, 2, minBufferSize);
        }
        this.mCodec = MediaCodec.createEncoderByType("audio/mp4a-latm");
        Objects.requireNonNull(this.mConfig);
        MediaFormat createAudioFormat = MediaFormat.createAudioFormat("audio/mp4a-latm", 44100, 1);
        createAudioFormat.setInteger("aac-profile", 2);
        Objects.requireNonNull(this.mConfig);
        createAudioFormat.setInteger("bitrate", 196000);
        Objects.requireNonNull(this.mConfig);
        createAudioFormat.setInteger("pcm-encoding", 2);
        this.mCodec.configure(createAudioFormat, (Surface) null, (MediaCrypto) null, 1);
        this.mThread = new Thread(new Runnable() { // from class: com.android.systemui.screenrecord.ScreenInternalAudioRecorder$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                short[] sArr;
                int i;
                int i2;
                int i3;
                ScreenInternalAudioRecorder screenInternalAudioRecorder = ScreenInternalAudioRecorder.this;
                int i4 = minBufferSize;
                Objects.requireNonNull(screenInternalAudioRecorder);
                byte[] bArr = new byte[i4];
                short[] sArr2 = null;
                if (screenInternalAudioRecorder.mMic) {
                    int i5 = i4 / 2;
                    sArr2 = new short[i5];
                    sArr = new short[i5];
                } else {
                    sArr = null;
                }
                int i6 = 0;
                int i7 = 0;
                short s = 0;
                int i8 = 0;
                int i9 = 0;
                while (true) {
                    if (screenInternalAudioRecorder.mMic) {
                        int read = screenInternalAudioRecorder.mAudioRecord.read(sArr2, i6, sArr2.length - i6);
                        int read2 = screenInternalAudioRecorder.mAudioRecordMic.read(sArr, i7, sArr.length - i7);
                        if (read < 0 && read2 < 0) {
                            break;
                        }
                        if (read < 0) {
                            Arrays.fill(sArr2, s);
                            i6 = i7;
                            read = read2;
                        }
                        if (read2 < 0) {
                            Arrays.fill(sArr, s == 1 ? (short) 1 : (short) 0);
                            i7 = i6;
                            read2 = read;
                        }
                        i8 = read + i6;
                        i9 = read2 + i7;
                        int min = Math.min(i8, i9);
                        i = min * 2;
                        short s2 = s;
                        while (true) {
                            i3 = -32768;
                            if (s2 >= min) {
                                break;
                            }
                            sArr[s2] = (short) MathUtils.constrain((int) (sArr[s2] * 1.4f), -32768, 32767);
                            s2++;
                        }
                        int i10 = 0;
                        while (i10 < min) {
                            short constrain = (short) MathUtils.constrain(sArr2[i10] + sArr[i10], i3, 32767);
                            int i11 = i10 * 2;
                            bArr[i11] = (byte) (constrain & 255);
                            bArr[i11 + 1] = (byte) ((constrain >> 8) & 255);
                            i10++;
                            i3 = -32768;
                        }
                        for (int i12 = 0; i12 < i6 - min; i12++) {
                            sArr2[i12] = sArr2[min + i12];
                        }
                        for (int i13 = 0; i13 < i7 - min; i13++) {
                            sArr[i13] = sArr[min + i13];
                        }
                        i6 = i8 - min;
                        i7 = i9 - min;
                        i2 = 0;
                    } else {
                        i = screenInternalAudioRecorder.mAudioRecord.read(bArr, 0, i4);
                        i2 = 0;
                    }
                    if (i < 0) {
                        StringBuilder m = GridLayoutManager$$ExternalSyntheticOutline0.m("read error ", i, ", shorts internal: ", i8, ", shorts mic: ");
                        m.append(i9);
                        Log.e("ScreenAudioRecorder", m.toString());
                        break;
                    }
                    while (true) {
                        if (i <= 0) {
                            break;
                        }
                        int dequeueInputBuffer = screenInternalAudioRecorder.mCodec.dequeueInputBuffer(500L);
                        if (dequeueInputBuffer < 0) {
                            screenInternalAudioRecorder.writeOutput();
                            break;
                        }
                        ByteBuffer inputBuffer = screenInternalAudioRecorder.mCodec.getInputBuffer(dequeueInputBuffer);
                        inputBuffer.clear();
                        int capacity = inputBuffer.capacity();
                        if (i <= capacity) {
                            capacity = i;
                        }
                        i -= capacity;
                        inputBuffer.put(bArr, i2, capacity);
                        i2 += capacity;
                        screenInternalAudioRecorder.mCodec.queueInputBuffer(dequeueInputBuffer, 0, capacity, screenInternalAudioRecorder.mPresentationTime, 0);
                        long j = screenInternalAudioRecorder.mTotalBytes + capacity + 0;
                        screenInternalAudioRecorder.mTotalBytes = j;
                        Objects.requireNonNull(screenInternalAudioRecorder.mConfig);
                        screenInternalAudioRecorder.mPresentationTime = ((j / 2) * 1000000) / 44100;
                        screenInternalAudioRecorder.writeOutput();
                        sArr = sArr;
                        bArr = bArr;
                    }
                    s = 0;
                    sArr = sArr;
                    bArr = bArr;
                }
                screenInternalAudioRecorder.mCodec.queueInputBuffer(screenInternalAudioRecorder.mCodec.dequeueInputBuffer(500L), 0, 0, screenInternalAudioRecorder.mPresentationTime, 4);
                screenInternalAudioRecorder.writeOutput();
            }
        });
    }
}
