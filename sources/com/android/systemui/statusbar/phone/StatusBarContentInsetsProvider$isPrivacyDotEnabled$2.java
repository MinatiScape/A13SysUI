package com.android.systemui.statusbar.phone;

import java.util.Objects;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: StatusBarContentInsetsProvider.kt */
/* loaded from: classes.dex */
public final class StatusBarContentInsetsProvider$isPrivacyDotEnabled$2 extends Lambda implements Function0<Boolean> {
    public final /* synthetic */ StatusBarContentInsetsProvider this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public StatusBarContentInsetsProvider$isPrivacyDotEnabled$2(StatusBarContentInsetsProvider statusBarContentInsetsProvider) {
        super(0);
        this.this$0 = statusBarContentInsetsProvider;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Boolean invoke() {
        StatusBarContentInsetsProvider statusBarContentInsetsProvider = this.this$0;
        Objects.requireNonNull(statusBarContentInsetsProvider);
        return Boolean.valueOf(statusBarContentInsetsProvider.context.getResources().getBoolean(2131034133));
    }
}
