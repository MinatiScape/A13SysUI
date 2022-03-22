package com.airbnb.lottie.network;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes.dex */
public final class NetworkCache {
    public final Context appContext;
    public final String url;

    public static String filenameForUrl(String str, FileExtension fileExtension, boolean z) {
        String str2;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("lottie_cache_");
        m.append(str.replaceAll("\\W+", ""));
        if (z) {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m(".temp");
            m2.append(fileExtension.extension);
            str2 = m2.toString();
        } else {
            str2 = fileExtension.extension;
        }
        m.append(str2);
        return m.toString();
    }

    public final File writeTempCacheFile(InputStream inputStream, FileExtension fileExtension) throws IOException {
        File file = new File(this.appContext.getCacheDir(), filenameForUrl(this.url, fileExtension, true));
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = inputStream.read(bArr);
                if (read != -1) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    return file;
                }
            }
        } finally {
            inputStream.close();
        }
    }

    public NetworkCache(Context context, String str) {
        this.appContext = context.getApplicationContext();
        this.url = str;
    }
}
