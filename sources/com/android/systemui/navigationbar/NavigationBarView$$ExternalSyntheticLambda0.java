package com.android.systemui.navigationbar;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NavigationBarView$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ NavigationBarView$$ExternalSyntheticLambda0(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x004a, code lost:
        if (r6 == null) goto L_0x0054;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x004c, code lost:
        r6.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0050, code lost:
        r6 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0051, code lost:
        android.util.Log.w("EditUserPhotoController", "Cannot close image stream", r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x003b, code lost:
        if (r6 != null) goto L_0x004c;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0065 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r6v17 */
    /* JADX WARN: Type inference failed for: r6v18 */
    /* JADX WARN: Type inference failed for: r6v2, types: [android.net.Uri] */
    /* JADX WARN: Type inference failed for: r6v3, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r6v6 */
    /* JADX WARN: Unknown variable types count: 1 */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void run() {
        /*
            r6 = this;
            int r0 = r6.$r8$classId
            switch(r0) {
                case 0: goto L_0x0006;
                default: goto L_0x0005;
            }
        L_0x0005:
            goto L_0x001d
        L_0x0006:
            java.lang.Object r0 = r6.f$0
            com.android.systemui.navigationbar.NavigationBarView r0 = (com.android.systemui.navigationbar.NavigationBarView) r0
            java.lang.Object r6 = r6.f$1
            java.lang.Boolean r6 = (java.lang.Boolean) r6
            int r1 = com.android.systemui.navigationbar.NavigationBarView.$r8$clinit
            java.util.Objects.requireNonNull(r0)
            boolean r6 = r6.booleanValue()
            r0.mDockedStackExists = r6
            r0.updateRecentsIcon()
            return
        L_0x001d:
            java.lang.Object r0 = r6.f$0
            com.android.settingslib.users.EditUserPhotoController r0 = (com.android.settingslib.users.EditUserPhotoController) r0
            java.lang.Object r6 = r6.f$1
            android.net.Uri r6 = (android.net.Uri) r6
            java.util.Objects.requireNonNull(r0)
            java.lang.String r1 = "Cannot close image stream"
            java.lang.String r2 = "EditUserPhotoController"
            r3 = 0
            android.app.Activity r4 = r0.mActivity     // Catch: all -> 0x0040, FileNotFoundException -> 0x0042
            android.content.ContentResolver r4 = r4.getContentResolver()     // Catch: all -> 0x0040, FileNotFoundException -> 0x0042
            java.io.InputStream r6 = r4.openInputStream(r6)     // Catch: all -> 0x0040, FileNotFoundException -> 0x0042
            android.graphics.Bitmap r3 = android.graphics.BitmapFactory.decodeStream(r6)     // Catch: FileNotFoundException -> 0x003e, all -> 0x0060
            if (r6 == 0) goto L_0x0054
            goto L_0x004c
        L_0x003e:
            r4 = move-exception
            goto L_0x0045
        L_0x0040:
            r6 = move-exception
            goto L_0x0063
        L_0x0042:
            r6 = move-exception
            r4 = r6
            r6 = r3
        L_0x0045:
            java.lang.String r5 = "Cannot find image file"
            android.util.Log.w(r2, r5, r4)     // Catch: all -> 0x0060
            if (r6 == 0) goto L_0x0054
        L_0x004c:
            r6.close()     // Catch: IOException -> 0x0050
            goto L_0x0054
        L_0x0050:
            r6 = move-exception
            android.util.Log.w(r2, r1, r6)
        L_0x0054:
            if (r3 == 0) goto L_0x005f
            com.android.keyguard.KeyguardPatternView$$ExternalSyntheticLambda0 r6 = new com.android.keyguard.KeyguardPatternView$$ExternalSyntheticLambda0
            r1 = 1
            r6.<init>(r0, r3, r1)
            androidx.recyclerview.R$dimen.postOnMainThread(r6)
        L_0x005f:
            return
        L_0x0060:
            r0 = move-exception
            r3 = r6
            r6 = r0
        L_0x0063:
            if (r3 == 0) goto L_0x006d
            r3.close()     // Catch: IOException -> 0x0069
            goto L_0x006d
        L_0x0069:
            r0 = move-exception
            android.util.Log.w(r2, r1, r0)
        L_0x006d:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.navigationbar.NavigationBarView$$ExternalSyntheticLambda0.run():void");
    }
}
