package com.google.android.systemui.columbus.gates;

import android.content.Context;
import android.os.Handler;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
/* compiled from: TransientGate.kt */
/* loaded from: classes.dex */
public abstract class TransientGate extends Gate {
    public final Function0<Unit> resetGate = new TransientGate$resetGate$1(this);
    public final Handler resetGateHandler;

    public final void blockForMillis(long j) {
        Handler handler = this.resetGateHandler;
        final Function0<Unit> function0 = this.resetGate;
        handler.removeCallbacks(new Runnable() { // from class: com.google.android.systemui.columbus.gates.TransientGate$sam$java_lang_Runnable$0
            @Override // java.lang.Runnable
            public final /* synthetic */ void run() {
                Function0.this.invoke();
            }
        });
        setBlocking(true);
        Handler handler2 = this.resetGateHandler;
        final Function0<Unit> function02 = this.resetGate;
        handler2.postDelayed(new Runnable() { // from class: com.google.android.systemui.columbus.gates.TransientGate$sam$java_lang_Runnable$0
            @Override // java.lang.Runnable
            public final /* synthetic */ void run() {
                Function0.this.invoke();
            }
        }, j);
    }

    public TransientGate(Context context, Handler handler) {
        super(context);
        this.resetGateHandler = handler;
    }
}
