package com.google.android.systemui.smartspace;

import android.content.Context;
import android.util.ArrayMap;
import android.util.Log;
import com.android.systemui.smartspace.nano.SmartspaceProto$CardWrapper;
import com.google.protobuf.nano.MessageNano;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
/* loaded from: classes.dex */
public final class ProtoStore {
    public final Object mContext;

    public /* synthetic */ ProtoStore(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public final void store(SmartspaceProto$CardWrapper smartspaceProto$CardWrapper, String str) {
        try {
            FileOutputStream openFileOutput = ((Context) this.mContext).openFileOutput(str, 0);
            try {
                if (smartspaceProto$CardWrapper != null) {
                    openFileOutput.write(MessageNano.toByteArray(smartspaceProto$CardWrapper));
                } else {
                    Log.d("ProtoStore", "deleting " + str);
                    ((Context) this.mContext).deleteFile(str);
                }
                if (openFileOutput != null) {
                    openFileOutput.close();
                }
            } catch (Throwable th) {
                if (openFileOutput != null) {
                    try {
                        openFileOutput.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (FileNotFoundException unused) {
            Log.d("ProtoStore", "file does not exist");
        } catch (Exception e) {
            Log.e("ProtoStore", "unable to write file", e);
        }
    }

    public /* synthetic */ ProtoStore(FileInputStream fileInputStream) {
        this.mContext = new ArrayMap();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    int indexOf = readLine.indexOf(58);
                    if (indexOf != -1) {
                        try {
                            ((Map) this.mContext).put(readLine.substring(0, indexOf).trim(), Float.valueOf(Float.parseFloat(readLine.substring(indexOf + 1))));
                        } catch (NumberFormatException unused) {
                        }
                    }
                } else {
                    return;
                }
            } catch (IOException e) {
                Log.e("Elmyra/SensorCalibration", "Error reading calibration file", e);
                return;
            }
        }
    }
}
