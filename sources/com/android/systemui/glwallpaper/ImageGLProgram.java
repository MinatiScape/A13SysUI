package com.android.systemui.glwallpaper;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline1;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/* loaded from: classes.dex */
public final class ImageGLProgram {
    public Context mContext;
    public int mProgramHandle;

    public final String getShaderResource(int i) {
        Resources resources = this.mContext.getResources();
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resources.openRawResource(i)));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                sb.append(readLine);
                sb.append("\n");
            }
            bufferedReader.close();
        } catch (Resources.NotFoundException | IOException e) {
            Log.d("ImageGLProgram", "Can not read the shader source", e);
            sb = null;
        }
        if (sb == null) {
            return "";
        }
        return sb.toString();
    }

    public ImageGLProgram(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static int getShaderHandle(int i, String str) {
        int glCreateShader = GLES20.glCreateShader(i);
        if (glCreateShader == 0) {
            ExifInterface$$ExternalSyntheticOutline1.m("Create shader failed, type=", i, "ImageGLProgram");
            return 0;
        }
        GLES20.glShaderSource(glCreateShader, str);
        GLES20.glCompileShader(glCreateShader);
        return glCreateShader;
    }
}
