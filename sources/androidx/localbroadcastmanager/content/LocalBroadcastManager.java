package androidx.localbroadcastmanager.content;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import java.util.ArrayList;
import java.util.HashMap;
@Deprecated
/* loaded from: classes.dex */
public final class LocalBroadcastManager {
    public static LocalBroadcastManager mInstance;
    public static final Object mLock = new Object();
    public final HashMap<String, ArrayList<ReceiverRecord>> mActions;
    public final HashMap<BroadcastReceiver, ArrayList<ReceiverRecord>> mReceivers;

    /* loaded from: classes.dex */
    public static final class ReceiverRecord {
        public boolean dead;
        public final IntentFilter filter;
        public final BroadcastReceiver receiver = null;

        public final String toString() {
            StringBuilder sb = new StringBuilder(128);
            sb.append("Receiver{");
            sb.append(this.receiver);
            sb.append(" filter=");
            sb.append(this.filter);
            if (this.dead) {
                sb.append(" DEAD");
            }
            sb.append("}");
            return sb.toString();
        }

        public ReceiverRecord(IntentFilter intentFilter) {
            this.filter = intentFilter;
        }
    }
}
