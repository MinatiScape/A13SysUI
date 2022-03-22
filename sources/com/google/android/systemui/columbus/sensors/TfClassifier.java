package com.google.android.systemui.columbus.sensors;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.util.Log;
import java.io.FileInputStream;
import java.nio.channels.FileChannel;
import org.tensorflow.lite.Interpreter;
/* loaded from: classes.dex */
public final class TfClassifier {
    public Interpreter mInterpreter;

    public TfClassifier(AssetManager assetManager, String str) {
        try {
            AssetFileDescriptor openFd = assetManager.openFd(str);
            this.mInterpreter = new Interpreter(new FileInputStream(openFd.getFileDescriptor()).getChannel().map(FileChannel.MapMode.READ_ONLY, openFd.getStartOffset(), openFd.getDeclaredLength()));
            Log.d("Columbus", "tflite file loaded: " + str);
        } catch (Exception e) {
            Log.d("Columbus", "load tflite file error: " + str);
            Log.e("Columbus", "tflite file:" + e.toString());
        }
    }
}
