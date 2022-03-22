package com.android.systemui;

import android.graphics.Bitmap;
import android.os.Bundle;
import com.android.systemui.ImageWallpaper;
import com.android.wm.shell.apppairs.AppPairsController;
import com.android.wm.shell.apppairs.AppPairsPool;
import com.google.android.systemui.dreamliner.DockObserver;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ImageWallpaper$GLEngine$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ ImageWallpaper$GLEngine$$ExternalSyntheticLambda3(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                ((ImageWallpaper.GLEngine) this.f$0).updateMiniBitmapAndNotify((Bitmap) obj);
                return;
            case 1:
                PrintWriter printWriter = (PrintWriter) this.f$0;
                AppPairsController appPairsController = (AppPairsController) obj;
                Objects.requireNonNull(appPairsController);
                printWriter.println("" + appPairsController);
                int size = appPairsController.mActiveAppPairs.size();
                while (true) {
                    size--;
                    if (size >= 0) {
                        appPairsController.mActiveAppPairs.valueAt(size).dump(printWriter, "    ");
                    } else {
                        AppPairsPool appPairsPool = appPairsController.mPairsPool;
                        if (appPairsPool != null) {
                            printWriter.println("" + appPairsPool);
                            int size2 = appPairsPool.mPool.size();
                            while (true) {
                                size2--;
                                if (size2 >= 0) {
                                    appPairsPool.mPool.get(size2).dump(printWriter, "    ");
                                } else {
                                    return;
                                }
                            }
                        } else {
                            return;
                        }
                    }
                }
            default:
                String str = DockObserver.ACTION_START_DREAMLINER_CONTROL_SERVICE;
                Bundle bundle = new Bundle();
                bundle.putByteArray("wpc_digest", (byte[]) obj);
                ((ArrayList) this.f$0).add(bundle);
                return;
        }
    }
}
