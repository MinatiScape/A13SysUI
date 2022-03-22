package com.airbnb.lottie.network;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieResult;
import com.airbnb.lottie.utils.Logger;
import com.airbnb.lottie.utils.Utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.zip.ZipInputStream;
/* loaded from: classes.dex */
public final class NetworkFetcher {
    public final NetworkCache networkCache;
    public final String url;

    public NetworkFetcher(Context context, String str) {
        Context applicationContext = context.getApplicationContext();
        this.url = str;
        this.networkCache = new NetworkCache(applicationContext, str);
    }

    public static String getErrorFromConnection(HttpURLConnection httpURLConnection) throws IOException {
        httpURLConnection.getResponseCode();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        sb.append(readLine);
                        sb.append('\n');
                    } else {
                        try {
                            break;
                        } catch (Exception unused) {
                        }
                    }
                } catch (Exception e) {
                    throw e;
                }
            } catch (Throwable th) {
                try {
                    bufferedReader.close();
                } catch (Exception unused2) {
                }
                throw th;
            }
        }
        bufferedReader.close();
        return sb.toString();
    }

    public final LottieResult fetchFromNetworkInternal() throws IOException {
        Logger.debug();
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(this.url).openConnection();
        httpURLConnection.setRequestMethod("GET");
        try {
            httpURLConnection.connect();
            if (httpURLConnection.getErrorStream() == null && httpURLConnection.getResponseCode() == 200) {
                LottieResult<LottieComposition> resultFromConnection = getResultFromConnection(httpURLConnection);
                LottieComposition lottieComposition = resultFromConnection.value;
                Logger.debug();
                return resultFromConnection;
            }
            String errorFromConnection = getErrorFromConnection(httpURLConnection);
            return new LottieResult(new IllegalArgumentException("Unable to fetch " + this.url + ". Failed with " + httpURLConnection.getResponseCode() + "\n" + errorFromConnection));
        } catch (Exception e) {
            return new LottieResult(e);
        } finally {
            httpURLConnection.disconnect();
        }
    }

    public final LottieResult<LottieComposition> getResultFromConnection(HttpURLConnection httpURLConnection) throws IOException {
        LottieResult<LottieComposition> lottieResult;
        FileExtension fileExtension;
        String contentType = httpURLConnection.getContentType();
        if (contentType == null) {
            contentType = "application/json";
        }
        if (contentType.contains("application/zip")) {
            Logger.debug();
            fileExtension = FileExtension.ZIP;
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(this.networkCache.writeTempCacheFile(httpURLConnection.getInputStream(), fileExtension)));
            try {
                lottieResult = LottieCompositionFactory.fromZipStreamSyncInternal(zipInputStream, this.url);
            } finally {
                Utils.closeQuietly(zipInputStream);
            }
        } else {
            Logger.debug();
            fileExtension = FileExtension.JSON;
            lottieResult = LottieCompositionFactory.fromJsonInputStreamSync(new FileInputStream(new File(this.networkCache.writeTempCacheFile(httpURLConnection.getInputStream(), fileExtension).getAbsolutePath())), this.url);
        }
        if (lottieResult.value != null) {
            NetworkCache networkCache = this.networkCache;
            Objects.requireNonNull(networkCache);
            File file = new File(networkCache.appContext.getCacheDir(), NetworkCache.filenameForUrl(networkCache.url, fileExtension, true));
            File file2 = new File(file.getAbsolutePath().replace(".temp", ""));
            boolean renameTo = file.renameTo(file2);
            file2.toString();
            Logger.debug();
            if (!renameTo) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Unable to rename cache file ");
                m.append(file.getAbsolutePath());
                m.append(" to ");
                m.append(file2.getAbsolutePath());
                m.append(".");
                Logger.warning(m.toString());
            }
        }
        return lottieResult;
    }
}
