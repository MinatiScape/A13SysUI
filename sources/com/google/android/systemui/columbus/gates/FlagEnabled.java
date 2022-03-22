package com.google.android.systemui.columbus.gates;

import android.content.Context;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0;
import com.google.android.systemui.columbus.ColumbusSettings;
import java.util.Objects;
/* compiled from: FlagEnabled.kt */
/* loaded from: classes.dex */
public final class FlagEnabled extends Gate {
    public boolean columbusEnabled;
    public final ColumbusSettings columbusSettings;
    public final FlagEnabled$settingsChangeListener$1 settingsChangeListener = new FlagEnabled$settingsChangeListener$1(this);

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final void onActivate() {
        this.settingsChangeListener.onColumbusEnabledChange(this.columbusSettings.isColumbusEnabled());
        this.columbusSettings.registerColumbusSettingsChangeListener(this.settingsChangeListener);
        boolean isColumbusEnabled = this.columbusSettings.isColumbusEnabled();
        this.columbusEnabled = isColumbusEnabled;
        setBlocking(!isColumbusEnabled);
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final void onDeactivate() {
        ColumbusSettings columbusSettings = this.columbusSettings;
        FlagEnabled$settingsChangeListener$1 flagEnabled$settingsChangeListener$1 = this.settingsChangeListener;
        Objects.requireNonNull(columbusSettings);
        columbusSettings.listeners.remove(flagEnabled$settingsChangeListener$1);
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" [columbusEnabled -> ");
        return LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0.m(sb, this.columbusEnabled, ']');
    }

    public FlagEnabled(Context context, ColumbusSettings columbusSettings, Handler handler) {
        super(context, handler);
        this.columbusSettings = columbusSettings;
    }
}
