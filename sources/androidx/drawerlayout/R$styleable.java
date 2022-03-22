package androidx.drawerlayout;

import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.google.protobuf.ByteString;
/* loaded from: classes.dex */
public final class R$styleable {
    public static final int[] DrawerLayout = {2130968992};

    public static String escapeBytes(ByteString byteString) {
        StringBuilder sb = new StringBuilder(byteString.size());
        for (int i = 0; i < byteString.size(); i++) {
            byte byteAt = byteString.byteAt(i);
            if (byteAt == 34) {
                sb.append("\\\"");
            } else if (byteAt == 39) {
                sb.append("\\'");
            } else if (byteAt != 92) {
                switch (byteAt) {
                    case 7:
                        sb.append("\\a");
                        continue;
                    case 8:
                        sb.append("\\b");
                        continue;
                    case 9:
                        sb.append("\\t");
                        continue;
                    case 10:
                        sb.append("\\n");
                        continue;
                    case QSTileImpl.H.STALE /* 11 */:
                        sb.append("\\v");
                        continue;
                    case KeyguardSliceProvider.ALARM_VISIBILITY_HOURS /* 12 */:
                        sb.append("\\f");
                        continue;
                    case QS.VERSION /* 13 */:
                        sb.append("\\r");
                        continue;
                    default:
                        if (byteAt < 32 || byteAt > 126) {
                            sb.append('\\');
                            sb.append((char) (((byteAt >>> 6) & 3) + 48));
                            sb.append((char) (((byteAt >>> 3) & 7) + 48));
                            sb.append((char) ((byteAt & 7) + 48));
                            break;
                        } else {
                            sb.append((char) byteAt);
                            continue;
                        }
                        break;
                }
            } else {
                sb.append("\\\\");
            }
        }
        return sb.toString();
    }
}
