package com.google.android.systemui.columbus.gates;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.statusbar.CommandQueue;
import java.util.Set;
/* compiled from: SystemKeyPress.kt */
/* loaded from: classes.dex */
public final class SystemKeyPress extends TransientGate {
    public final Set<Integer> blockingKeys;
    public final CommandQueue commandQueue;
    public final SystemKeyPress$commandQueueCallbacks$1 commandQueueCallbacks = new CommandQueue.Callbacks() { // from class: com.google.android.systemui.columbus.gates.SystemKeyPress$commandQueueCallbacks$1
        @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
        public final void handleSystemKey(int i) {
            if (SystemKeyPress.this.blockingKeys.contains(Integer.valueOf(i))) {
                SystemKeyPress systemKeyPress = SystemKeyPress.this;
                systemKeyPress.blockForMillis(systemKeyPress.gateDuration);
            }
        }
    };
    public final long gateDuration;

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final void onActivate() {
        this.commandQueue.addCallback((CommandQueue.Callbacks) this.commandQueueCallbacks);
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final void onDeactivate() {
        this.commandQueue.removeCallback((CommandQueue.Callbacks) this.commandQueueCallbacks);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.google.android.systemui.columbus.gates.SystemKeyPress$commandQueueCallbacks$1] */
    public SystemKeyPress(Context context, Handler handler, CommandQueue commandQueue, long j, Set<Integer> set) {
        super(context, handler);
        this.commandQueue = commandQueue;
        this.gateDuration = j;
        this.blockingKeys = set;
    }
}
