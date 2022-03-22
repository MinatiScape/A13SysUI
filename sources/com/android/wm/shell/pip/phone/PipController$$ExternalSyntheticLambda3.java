package com.android.wm.shell.pip.phone;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PipController$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ PipController$$ExternalSyntheticLambda3(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0062  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0068  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0073  */
    /* JADX WARN: Removed duplicated region for block: B:24:? A[RETURN, SYNTHETIC] */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void run() {
        /*
            r9 = this;
            int r0 = r9.$r8$classId
            r1 = 0
            switch(r0) {
                case 0: goto L_0x0030;
                case 1: goto L_0x0017;
                case 2: goto L_0x0008;
                default: goto L_0x0006;
            }
        L_0x0006:
            goto L_0x0079
        L_0x0008:
            java.lang.Object r0 = r9.f$0
            com.android.systemui.screenshot.ImageTileSet r0 = (com.android.systemui.screenshot.ImageTileSet) r0
            java.lang.Object r9 = r9.f$1
            com.android.systemui.screenshot.ImageTile r9 = (com.android.systemui.screenshot.ImageTile) r9
            java.util.Objects.requireNonNull(r0)
            r0.addTile(r9)
            return
        L_0x0017:
            java.lang.Object r0 = r9.f$0
            com.android.systemui.qs.QSFooterViewController r0 = (com.android.systemui.qs.QSFooterViewController) r0
            java.lang.Object r9 = r9.f$1
            android.view.View r9 = (android.view.View) r9
            java.util.Objects.requireNonNull(r0)
            com.android.systemui.qs.QSPanelController r0 = r0.mQsPanelController
            java.util.Objects.requireNonNull(r0)
            com.android.systemui.qs.QSPanelController$$ExternalSyntheticLambda0 r2 = new com.android.systemui.qs.QSPanelController$$ExternalSyntheticLambda0
            r2.<init>(r0, r9, r1)
            r9.post(r2)
            return
        L_0x0030:
            java.lang.Object r0 = r9.f$0
            com.android.wm.shell.pip.phone.PipController r0 = (com.android.wm.shell.pip.phone.PipController) r0
            java.lang.Object r9 = r9.f$1
            com.android.wm.shell.common.DisplayLayout r9 = (com.android.wm.shell.common.DisplayLayout) r9
            java.util.Objects.requireNonNull(r0)
            boolean r2 = com.android.wm.shell.transition.Transitions.ENABLE_SHELL_TRANSITIONS
            r8 = 1
            if (r2 == 0) goto L_0x0055
            com.android.wm.shell.pip.PipBoundsState r2 = r0.mPipBoundsState
            java.util.Objects.requireNonNull(r2)
            com.android.wm.shell.common.DisplayLayout r2 = r2.mDisplayLayout
            java.util.Objects.requireNonNull(r2)
            int r2 = r2.mRotation
            java.util.Objects.requireNonNull(r9)
            int r3 = r9.mRotation
            if (r2 == r3) goto L_0x0055
            r4 = r8
            goto L_0x0056
        L_0x0055:
            r4 = r1
        L_0x0056:
            com.android.wm.shell.pip.PipBoundsState r2 = r0.mPipBoundsState
            java.util.Objects.requireNonNull(r2)
            com.android.wm.shell.common.DisplayLayout r2 = r2.mDisplayLayout
            r2.set(r9)
            if (r4 == 0) goto L_0x0068
            android.window.WindowContainerTransaction r9 = new android.window.WindowContainerTransaction
            r9.<init>()
            goto L_0x0069
        L_0x0068:
            r9 = 0
        L_0x0069:
            r3 = 0
            r5 = 0
            r6 = 0
            r2 = r0
            r7 = r9
            r2.updateMovementBounds(r3, r4, r5, r6, r7)
            if (r9 == 0) goto L_0x0078
            com.android.wm.shell.pip.PipTaskOrganizer r0 = r0.mPipTaskOrganizer
            r0.applyFinishBoundsResize(r9, r8, r1)
        L_0x0078:
            return
        L_0x0079:
            java.lang.Object r0 = r9.f$0
            com.android.systemui.shared.plugins.PluginActionManager r0 = (com.android.systemui.shared.plugins.PluginActionManager) r0
            java.lang.Object r9 = r9.f$1
            java.lang.String r9 = (java.lang.String) r9
            com.android.systemui.shared.plugins.PluginActionManager.$r8$lambda$nTrqaXNoyZ9Ewe_oMzlt3sqKZco(r0, r9)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.pip.phone.PipController$$ExternalSyntheticLambda3.run():void");
    }
}
