package com.android.wifitrackerlib;

import com.android.wm.shell.splitscreen.SplitScreenController;
import java.net.InetAddress;
import java.util.Objects;
import java.util.function.Function;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WifiEntry$$ExternalSyntheticLambda3 implements Function {
    public static final /* synthetic */ WifiEntry$$ExternalSyntheticLambda3 INSTANCE = new WifiEntry$$ExternalSyntheticLambda3(0);
    public static final /* synthetic */ WifiEntry$$ExternalSyntheticLambda3 INSTANCE$1 = new WifiEntry$$ExternalSyntheticLambda3(1);
    public final /* synthetic */ int $r8$classId;

    public /* synthetic */ WifiEntry$$ExternalSyntheticLambda3(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                return ((InetAddress) obj).getHostAddress();
            default:
                SplitScreenController splitScreenController = (SplitScreenController) obj;
                Objects.requireNonNull(splitScreenController);
                return splitScreenController.mImpl;
        }
    }
}
