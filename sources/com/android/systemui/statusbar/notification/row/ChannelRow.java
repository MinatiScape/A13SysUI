package com.android.systemui.statusbar.notification.row;

import android.app.NotificationChannel;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.android.settingslib.Utils;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ChannelEditorListView.kt */
/* loaded from: classes.dex */
public final class ChannelRow extends LinearLayout {
    public NotificationChannel channel;
    public TextView channelDescription;
    public TextView channelName;
    public ChannelEditorDialogController controller;
    public final int highlightColor = Utils.getColorAttrDefaultColor(getContext(), 16843820);

    /* renamed from: switch  reason: not valid java name */
    public Switch f7switch;

    public ChannelRow(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.channelName = (TextView) findViewById(2131427685);
        this.channelDescription = (TextView) findViewById(2131427684);
        Switch r0 = (Switch) findViewById(2131429065);
        this.f7switch = r0;
        r0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.android.systemui.statusbar.notification.row.ChannelRow$onFinishInflate$1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                int i;
                int i2;
                ChannelRow channelRow = ChannelRow.this;
                Objects.requireNonNull(channelRow);
                NotificationChannel notificationChannel = channelRow.channel;
                if (notificationChannel != null) {
                    ChannelRow channelRow2 = ChannelRow.this;
                    Objects.requireNonNull(channelRow2);
                    ChannelEditorDialogController channelEditorDialogController = channelRow2.controller;
                    ChannelEditorDialog channelEditorDialog = null;
                    if (channelEditorDialogController == null) {
                        channelEditorDialogController = null;
                    }
                    boolean z2 = false;
                    if (z) {
                        i = notificationChannel.getImportance();
                    } else {
                        i = 0;
                    }
                    Objects.requireNonNull(channelEditorDialogController);
                    if (notificationChannel.getImportance() == i) {
                        channelEditorDialogController.edits.remove(notificationChannel);
                    } else {
                        channelEditorDialogController.edits.put(notificationChannel, Integer.valueOf(i));
                    }
                    ChannelEditorDialog channelEditorDialog2 = channelEditorDialogController.dialog;
                    if (channelEditorDialog2 != null) {
                        channelEditorDialog = channelEditorDialog2;
                    }
                    if ((!channelEditorDialogController.edits.isEmpty()) || !Intrinsics.areEqual(Boolean.valueOf(channelEditorDialogController.appNotificationsEnabled), channelEditorDialogController.appNotificationsCurrentlyEnabled)) {
                        z2 = true;
                    }
                    Objects.requireNonNull(channelEditorDialog);
                    TextView textView = (TextView) channelEditorDialog.findViewById(2131427866);
                    if (textView != null) {
                        if (z2) {
                            i2 = 2131952463;
                        } else {
                            i2 = 2131952462;
                        }
                        textView.setText(i2);
                    }
                }
            }
        });
        setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.statusbar.notification.row.ChannelRow$onFinishInflate$2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                Switch r02 = ChannelRow.this.f7switch;
                if (r02 == null) {
                    r02 = null;
                }
                r02.toggle();
            }
        });
    }
}
