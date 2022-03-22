package androidx.fragment.app;

import android.util.Log;
import android.view.SurfaceControl;
import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class DialogFragment$$ExternalSyntheticOutline0 implements PipSurfaceTransactionHelper.SurfaceControlTransactionFactory {
    public static final /* synthetic */ DialogFragment$$ExternalSyntheticOutline0 INSTANCE = new DialogFragment$$ExternalSyntheticOutline0();

    @Override // com.android.wm.shell.pip.PipSurfaceTransactionHelper.SurfaceControlTransactionFactory
    public SurfaceControl.Transaction getTransaction() {
        return new SurfaceControl.Transaction();
    }

    public static void m(String str, String str2, String str3) {
        Log.d(str3, str + str2);
    }
}
