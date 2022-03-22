package androidx.savedstate;

import android.app.smartspace.SmartspaceTarget;
import java.util.Objects;
/* loaded from: classes.dex */
public final class R$id {
    public static Integer create(SmartspaceTarget smartspaceTarget) {
        String smartspaceTargetId = smartspaceTarget.getSmartspaceTargetId();
        if (smartspaceTargetId == null || smartspaceTargetId.isEmpty()) {
            return Integer.valueOf(Math.abs(Math.floorMod(Objects.hashCode(String.valueOf(smartspaceTarget.getCreationTimeMillis())), 8192)));
        }
        return Integer.valueOf(Math.abs(Math.floorMod(smartspaceTargetId.hashCode(), 8192)));
    }
}
