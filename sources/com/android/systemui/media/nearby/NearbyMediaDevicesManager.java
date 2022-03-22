package com.android.systemui.media.nearby;

import android.media.INearbyMediaDevicesProvider;
import android.media.INearbyMediaDevicesUpdateCallback;
import android.os.IBinder;
import com.android.systemui.statusbar.CommandQueue;
import java.util.ArrayList;
import java.util.Iterator;
/* compiled from: NearbyMediaDevicesManager.kt */
/* loaded from: classes.dex */
public final class NearbyMediaDevicesManager {
    public ArrayList providers = new ArrayList();
    public ArrayList activeCallbacks = new ArrayList();
    public final NearbyMediaDevicesManager$deathRecipient$1 deathRecipient = new IBinder.DeathRecipient() { // from class: com.android.systemui.media.nearby.NearbyMediaDevicesManager$deathRecipient$1
        @Override // android.os.IBinder.DeathRecipient
        public final void binderDied() {
        }

        /* JADX WARN: Code restructure failed: missing block: B:8:0x0026, code lost:
            r4.providers.remove(r1);
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void binderDied(android.os.IBinder r5) {
            /*
                r4 = this;
                com.android.systemui.media.nearby.NearbyMediaDevicesManager r4 = com.android.systemui.media.nearby.NearbyMediaDevicesManager.this
                java.util.Objects.requireNonNull(r4)
                java.util.ArrayList r0 = r4.providers
                monitor-enter(r0)
                java.util.ArrayList r1 = r4.providers     // Catch: all -> 0x0033
                int r1 = r1.size()     // Catch: all -> 0x0033
                int r1 = r1 + (-1)
                if (r1 < 0) goto L_0x0031
            L_0x0012:
                int r2 = r1 + (-1)
                java.util.ArrayList r3 = r4.providers     // Catch: all -> 0x0033
                java.lang.Object r3 = r3.get(r1)     // Catch: all -> 0x0033
                android.media.INearbyMediaDevicesProvider r3 = (android.media.INearbyMediaDevicesProvider) r3     // Catch: all -> 0x0033
                android.os.IBinder r3 = r3.asBinder()     // Catch: all -> 0x0033
                boolean r3 = kotlin.jvm.internal.Intrinsics.areEqual(r3, r5)     // Catch: all -> 0x0033
                if (r3 == 0) goto L_0x002c
                java.util.ArrayList r4 = r4.providers     // Catch: all -> 0x0033
                r4.remove(r1)     // Catch: all -> 0x0033
                goto L_0x0031
            L_0x002c:
                if (r2 >= 0) goto L_0x002f
                goto L_0x0031
            L_0x002f:
                r1 = r2
                goto L_0x0012
            L_0x0031:
                monitor-exit(r0)
                return
            L_0x0033:
                r4 = move-exception
                monitor-exit(r0)
                throw r4
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.nearby.NearbyMediaDevicesManager$deathRecipient$1.binderDied(android.os.IBinder):void");
        }
    };

    /* JADX WARN: Type inference failed for: r1v0, types: [com.android.systemui.media.nearby.NearbyMediaDevicesManager$deathRecipient$1] */
    public NearbyMediaDevicesManager(CommandQueue commandQueue) {
        CommandQueue.Callbacks nearbyMediaDevicesManager$commandQueueCallbacks$1 = new CommandQueue.Callbacks() { // from class: com.android.systemui.media.nearby.NearbyMediaDevicesManager$commandQueueCallbacks$1
            @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
            public final void registerNearbyMediaDevicesProvider(INearbyMediaDevicesProvider iNearbyMediaDevicesProvider) {
                if (!NearbyMediaDevicesManager.this.providers.contains(iNearbyMediaDevicesProvider)) {
                    Iterator it = NearbyMediaDevicesManager.this.activeCallbacks.iterator();
                    while (it.hasNext()) {
                        iNearbyMediaDevicesProvider.registerNearbyDevicesCallback((INearbyMediaDevicesUpdateCallback) it.next());
                    }
                    NearbyMediaDevicesManager.this.providers.add(iNearbyMediaDevicesProvider);
                    iNearbyMediaDevicesProvider.asBinder().linkToDeath(NearbyMediaDevicesManager.this.deathRecipient, 0);
                }
            }

            @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
            public final void unregisterNearbyMediaDevicesProvider(INearbyMediaDevicesProvider iNearbyMediaDevicesProvider) {
                NearbyMediaDevicesManager.this.providers.remove(iNearbyMediaDevicesProvider);
            }
        };
        commandQueue.addCallback(nearbyMediaDevicesManager$commandQueueCallbacks$1);
    }
}
