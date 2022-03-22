package com.google.android.systemui.elmyra.gates;

import android.content.Context;
import com.android.systemui.Dependency;
import com.android.systemui.statusbar.CommandQueue;
/* loaded from: classes.dex */
public final class SystemKeyPress extends TransientGate {
    public final int[] mBlockingKeys;
    public final AnonymousClass1 mCommandQueueCallbacks = new CommandQueue.Callbacks() { // from class: com.google.android.systemui.elmyra.gates.SystemKeyPress.1
        @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
        public final void handleSystemKey(int i) {
            int i2 = 0;
            while (true) {
                SystemKeyPress systemKeyPress = SystemKeyPress.this;
                int[] iArr = systemKeyPress.mBlockingKeys;
                if (i2 >= iArr.length) {
                    return;
                }
                if (iArr[i2] == i) {
                    systemKeyPress.block();
                    return;
                }
                i2++;
            }
        }
    };
    public final CommandQueue mCommandQueue = (CommandQueue) Dependency.get(CommandQueue.class);

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final void onActivate() {
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this.mCommandQueueCallbacks);
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final void onDeactivate() {
        this.mCommandQueue.removeCallback((CommandQueue.Callbacks) this.mCommandQueueCallbacks);
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [com.google.android.systemui.elmyra.gates.SystemKeyPress$1] */
    public SystemKeyPress(Context context) {
        super(context, context.getResources().getInteger(2131492930));
        this.mBlockingKeys = context.getResources().getIntArray(2130903109);
    }
}
