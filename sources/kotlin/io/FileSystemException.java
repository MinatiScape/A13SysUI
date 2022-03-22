package kotlin.io;

import java.io.File;
import java.io.IOException;
/* compiled from: Exceptions.kt */
/* loaded from: classes.dex */
public class FileSystemException extends IOException {
    private final File file;
    private final File other;
    private final String reason;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public FileSystemException(java.io.File r3, java.io.File r4, java.lang.String r5) {
        /*
            r2 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = r3.toString()
            r0.<init>(r1)
            if (r4 == 0) goto L_0x0014
            java.lang.String r1 = " -> "
            java.lang.String r1 = kotlin.jvm.internal.Intrinsics.stringPlus(r1, r4)
            r0.append(r1)
        L_0x0014:
            if (r5 == 0) goto L_0x001f
            java.lang.String r1 = ": "
            java.lang.String r1 = kotlin.jvm.internal.Intrinsics.stringPlus(r1, r5)
            r0.append(r1)
        L_0x001f:
            java.lang.String r0 = r0.toString()
            r2.<init>(r0)
            r2.file = r3
            r2.other = r4
            r2.reason = r5
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.io.FileSystemException.<init>(java.io.File, java.io.File, java.lang.String):void");
    }
}
