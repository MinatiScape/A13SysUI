package com.android.systemui.screenrecord;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaMuxer;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline2;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public final class ScreenRecordingMuxer {
    public String[] mFiles;
    public String mOutFile;
    public ArrayMap<Pair<MediaExtractor, Integer>, Integer> mExtractorIndexToMuxerIndex = new ArrayMap<>();
    public ArrayList<MediaExtractor> mExtractors = new ArrayList<>();
    public int mFormat = 0;

    public final void mux() throws IOException {
        String[] strArr;
        MediaMuxer mediaMuxer = new MediaMuxer(this.mOutFile, this.mFormat);
        for (String str : this.mFiles) {
            MediaExtractor mediaExtractor = new MediaExtractor();
            try {
                mediaExtractor.setDataSource(str);
                StringBuilder m = DebugInfo$$ExternalSyntheticOutline0.m(str, " track count: ");
                m.append(mediaExtractor.getTrackCount());
                Log.d("ScreenRecordingMuxer", m.toString());
                this.mExtractors.add(mediaExtractor);
                for (int i = 0; i < mediaExtractor.getTrackCount(); i++) {
                    int addTrack = mediaMuxer.addTrack(mediaExtractor.getTrackFormat(i));
                    StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("created extractor format");
                    m2.append(mediaExtractor.getTrackFormat(i).toString());
                    Log.d("ScreenRecordingMuxer", m2.toString());
                    this.mExtractorIndexToMuxerIndex.put(Pair.create(mediaExtractor, Integer.valueOf(i)), Integer.valueOf(addTrack));
                }
            } catch (IOException e) {
                Log.e("ScreenRecordingMuxer", "error creating extractor: " + str);
                e.printStackTrace();
            }
        }
        mediaMuxer.start();
        for (Pair<MediaExtractor, Integer> pair : this.mExtractorIndexToMuxerIndex.keySet()) {
            MediaExtractor mediaExtractor2 = (MediaExtractor) pair.first;
            mediaExtractor2.selectTrack(((Integer) pair.second).intValue());
            int intValue = this.mExtractorIndexToMuxerIndex.get(pair).intValue();
            StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("track format: ");
            m3.append(mediaExtractor2.getTrackFormat(((Integer) pair.second).intValue()));
            Log.d("ScreenRecordingMuxer", m3.toString());
            mediaExtractor2.seekTo(0L, 2);
            ByteBuffer allocate = ByteBuffer.allocate(4194304);
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            while (true) {
                int readSampleData = mediaExtractor2.readSampleData(allocate, allocate.arrayOffset());
                bufferInfo.size = readSampleData;
                if (readSampleData < 0) {
                    break;
                }
                bufferInfo.presentationTimeUs = mediaExtractor2.getSampleTime();
                bufferInfo.flags = mediaExtractor2.getSampleFlags();
                mediaMuxer.writeSampleData(intValue, allocate, bufferInfo);
                mediaExtractor2.advance();
            }
        }
        Iterator<MediaExtractor> it = this.mExtractors.iterator();
        while (it.hasNext()) {
            it.next().release();
        }
        mediaMuxer.stop();
        mediaMuxer.release();
    }

    public ScreenRecordingMuxer(String str, String... strArr) {
        this.mFiles = strArr;
        this.mOutFile = str;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("out: ");
        m.append(this.mOutFile);
        m.append(" , in: ");
        ExifInterface$$ExternalSyntheticOutline2.m(m, this.mFiles[0], "ScreenRecordingMuxer");
    }
}
