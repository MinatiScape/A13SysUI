package com.android.systemui.log;

import android.content.ContentResolver;
import android.provider.Settings;
import java.util.LinkedHashMap;
/* compiled from: LogcatEchoTrackerDebug.kt */
/* loaded from: classes.dex */
public final class LogcatEchoTrackerDebug implements LogcatEchoTracker {
    public final LinkedHashMap cachedBufferLevels = new LinkedHashMap();
    public final LinkedHashMap cachedTagLevels = new LinkedHashMap();
    public final ContentResolver contentResolver;

    @Override // com.android.systemui.log.LogcatEchoTracker
    public final synchronized boolean isBufferLoggable(String str, LogLevel logLevel) {
        boolean z;
        if (logLevel.ordinal() >= getLogLevel(str, "systemui/buffer", this.cachedBufferLevels).ordinal()) {
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    @Override // com.android.systemui.log.LogcatEchoTracker
    public final synchronized boolean isTagLoggable(String str, LogLevel logLevel) {
        boolean z;
        if (logLevel.compareTo(getLogLevel(str, "systemui/tag", this.cachedTagLevels)) >= 0) {
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0034, code lost:
        if (r7.equals("error") == false) goto L_0x0095;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x003e, code lost:
        if (r7.equals("debug") == false) goto L_0x0095;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x004b, code lost:
        if (r7.equals("info") == false) goto L_0x0095;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0055, code lost:
        if (r7.equals("wtf") == false) goto L_0x0095;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x006f, code lost:
        if (r7.equals("i") == false) goto L_0x0095;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x007a, code lost:
        if (r7.equals("e") == false) goto L_0x0095;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0085, code lost:
        if (r7.equals("d") == false) goto L_0x0095;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0090, code lost:
        if (r7.equals("assert") == false) goto L_0x0095;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:?, code lost:
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:?, code lost:
        return r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:?, code lost:
        return r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:?, code lost:
        return r4;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.android.systemui.log.LogLevel parseProp(java.lang.String r7) {
        /*
            com.android.systemui.log.LogLevel r0 = com.android.systemui.log.LogLevel.VERBOSE
            com.android.systemui.log.LogLevel r1 = com.android.systemui.log.LogLevel.INFO
            com.android.systemui.log.LogLevel r2 = com.android.systemui.log.LogLevel.ERROR
            com.android.systemui.log.LogLevel r3 = com.android.systemui.log.LogLevel.DEBUG
            com.android.systemui.log.LogLevel r4 = com.android.systemui.log.LogLevel.WTF
            com.android.systemui.log.LogLevel r5 = com.android.systemui.log.LogLevel.WARNING
            if (r7 != 0) goto L_0x0010
            r7 = 0
            goto L_0x0014
        L_0x0010:
            java.lang.String r7 = r7.toLowerCase()
        L_0x0014:
            if (r7 == 0) goto L_0x0095
            int r6 = r7.hashCode()
            switch(r6) {
                case -1408208058: goto L_0x008a;
                case 100: goto L_0x007f;
                case 101: goto L_0x0074;
                case 105: goto L_0x0069;
                case 118: goto L_0x005f;
                case 119: goto L_0x0058;
                case 118057: goto L_0x004e;
                case 3237038: goto L_0x0045;
                case 3641990: goto L_0x0041;
                case 95458899: goto L_0x0038;
                case 96784904: goto L_0x002e;
                case 351107458: goto L_0x0023;
                case 1124446108: goto L_0x001f;
                default: goto L_0x001d;
            }
        L_0x001d:
            goto L_0x0095
        L_0x001f:
            java.lang.String r0 = "warning"
            goto L_0x005b
        L_0x0023:
            java.lang.String r1 = "verbose"
            boolean r7 = r7.equals(r1)
            if (r7 != 0) goto L_0x0096
            goto L_0x0095
        L_0x002e:
            java.lang.String r0 = "error"
            boolean r7 = r7.equals(r0)
            if (r7 != 0) goto L_0x007d
            goto L_0x0095
        L_0x0038:
            java.lang.String r0 = "debug"
            boolean r7 = r7.equals(r0)
            if (r7 != 0) goto L_0x0088
            goto L_0x0095
        L_0x0041:
            java.lang.String r0 = "warn"
            goto L_0x005b
        L_0x0045:
            java.lang.String r0 = "info"
            boolean r7 = r7.equals(r0)
            if (r7 != 0) goto L_0x0072
            goto L_0x0095
        L_0x004e:
            java.lang.String r0 = "wtf"
            boolean r7 = r7.equals(r0)
            if (r7 != 0) goto L_0x0093
            goto L_0x0095
        L_0x0058:
            java.lang.String r0 = "w"
        L_0x005b:
            r7.equals(r0)
            goto L_0x0095
        L_0x005f:
            java.lang.String r1 = "v"
            boolean r7 = r7.equals(r1)
            if (r7 != 0) goto L_0x0096
            goto L_0x0095
        L_0x0069:
            java.lang.String r0 = "i"
            boolean r7 = r7.equals(r0)
            if (r7 != 0) goto L_0x0072
            goto L_0x0095
        L_0x0072:
            r0 = r1
            goto L_0x0096
        L_0x0074:
            java.lang.String r0 = "e"
            boolean r7 = r7.equals(r0)
            if (r7 != 0) goto L_0x007d
            goto L_0x0095
        L_0x007d:
            r0 = r2
            goto L_0x0096
        L_0x007f:
            java.lang.String r0 = "d"
            boolean r7 = r7.equals(r0)
            if (r7 != 0) goto L_0x0088
            goto L_0x0095
        L_0x0088:
            r0 = r3
            goto L_0x0096
        L_0x008a:
            java.lang.String r0 = "assert"
            boolean r7 = r7.equals(r0)
            if (r7 != 0) goto L_0x0093
            goto L_0x0095
        L_0x0093:
            r0 = r4
            goto L_0x0096
        L_0x0095:
            r0 = r5
        L_0x0096:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.log.LogcatEchoTrackerDebug.parseProp(java.lang.String):com.android.systemui.log.LogLevel");
    }

    public LogcatEchoTrackerDebug(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public final LogLevel getLogLevel(String str, String str2, LinkedHashMap linkedHashMap) {
        LogLevel logLevel;
        LogLevel logLevel2 = (LogLevel) linkedHashMap.get(str);
        if (logLevel2 != null) {
            return logLevel2;
        }
        try {
            logLevel = parseProp(Settings.Global.getString(this.contentResolver, str2 + '/' + str));
        } catch (Settings.SettingNotFoundException unused) {
            logLevel = LogLevel.WARNING;
        }
        linkedHashMap.put(str, logLevel);
        return logLevel;
    }
}
