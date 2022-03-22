package androidx.leanback;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.Process;
import java.util.Objects;
import kotlin.jvm.functions.Function1;
/* loaded from: classes.dex */
public class R$styleable {
    public static final int[] LeanbackGuidedStepTheme = {2130969120, 2130969121, 2130969122, 2130969123, 2130969124, 2130969125, 2130969126, 2130969127, 2130969128, 2130969129, 2130969130, 2130969131, 2130969132, 2130969133, 2130969134, 2130969135, 2130969136, 2130969137, 2130969138, 2130969139, 2130969140, 2130969141, 2130969142, 2130969143, 2130969144, 2130969145, 2130969146, 2130969147, 2130969148, 2130969149, 2130969150, 2130969151, 2130969152, 2130969153, 2130969154, 2130969155, 2130969156, 2130969157, 2130969158, 2130969159, 2130969160, 2130969161, 2130969162, 2130969163, 2130969164, 2130969165, 2130969166, 2130969167, 2130969168, 2130969169, 2130969170};
    public static final int[] PagingIndicator = {2130968636, 2130968637, 2130968639, 2130968963, 2130968965, 2130968966, 2130969359};
    public static final int[] lbBaseCardView = {2130968613, 2130968736, 2130968740, 2130968744, 2130969042, 2130969239, 2130969704, 2130969705};
    public static final int[] lbBaseCardView_Layout = {2130969358};
    public static final int[] lbBaseGridView = {16842927, 16843028, 16843029, 2130969088, 2130969089, 2130969090, 2130969091, 2130969202, 2130970095};
    public static final int[] lbDatePicker = {16843583, 16843584, 2130968915, 2130969577, 2130969578};
    public static final int[] lbHorizontalGridView = {2130969523, 2130969670};
    public static final int[] lbImageCardView = {2130969238, 2130969360};
    public static final int[] lbPicker = {2130969577, 2130969578};
    public static final int[] lbPinPicker = {2130968853, 2130969577, 2130969578};
    public static final int[] lbResizingTextView = {2130969405, 2130969655, 2130969656, 2130969657, 2130969658};
    public static final int[] lbSearchOrbView = {2130969690, 2130969691, 2130969692, 2130969693};
    public static final int[] lbSlide = {16843073, 16843160, 16843746, 2130969361};
    public static final int[] lbTimePicker = {2130969244, 2130969577, 2130969578, 2130970090};
    public static final int[] lbVerticalGridView = {2130968854, 2130969522};

    public static final void appendElement(StringBuilder sb, Object obj, Function1 function1) {
        boolean z;
        if (function1 != null) {
            sb.append((CharSequence) function1.invoke(obj));
            return;
        }
        if (obj == null) {
            z = true;
        } else {
            z = obj instanceof CharSequence;
        }
        if (z) {
            sb.append((CharSequence) obj);
        } else if (obj instanceof Character) {
            sb.append(((Character) obj).charValue());
        } else {
            sb.append((CharSequence) String.valueOf(obj));
        }
    }

    public static int checkSelfPermission(Context context, String str) {
        boolean z;
        int i;
        int myPid = Process.myPid();
        int myUid = Process.myUid();
        String packageName = context.getPackageName();
        if (context.checkPermission(str, myPid, myUid) == -1) {
            return -1;
        }
        String permissionToOp = AppOpsManager.permissionToOp(str);
        if (permissionToOp != null) {
            if (packageName == null) {
                String[] packagesForUid = context.getPackageManager().getPackagesForUid(myUid);
                if (packagesForUid == null || packagesForUid.length <= 0) {
                    return -1;
                }
                packageName = packagesForUid[0];
            }
            int myUid2 = Process.myUid();
            String packageName2 = context.getPackageName();
            int i2 = 1;
            if (myUid2 != myUid || !Objects.equals(packageName2, packageName)) {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(AppOpsManager.class);
                int callingUid = Binder.getCallingUid();
                if (appOpsManager == null) {
                    i = 1;
                } else {
                    i = appOpsManager.checkOpNoThrow(permissionToOp, callingUid, packageName);
                }
                if (i == 0) {
                    String opPackageName = context.getOpPackageName();
                    if (appOpsManager != null) {
                        i2 = appOpsManager.checkOpNoThrow(permissionToOp, myUid, opPackageName);
                    }
                    i = i2;
                }
            } else {
                i = ((AppOpsManager) context.getSystemService(AppOpsManager.class)).noteProxyOpNoThrow(permissionToOp, packageName);
            }
            if (i != 0) {
                return -2;
            }
        }
        return 0;
    }
}
