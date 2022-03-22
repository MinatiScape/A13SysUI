package com.google.android.systemui.columbus.sensors.config;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
/* compiled from: Adjustment.kt */
/* loaded from: classes.dex */
public abstract class Adjustment {
    public Function1<? super Adjustment, Unit> callback;

    public abstract float adjustSensitivity(float f);
}
