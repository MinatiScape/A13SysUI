package com.android.systemui.statusbar.policy;

import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.android.systemui.statusbar.policy.SmartReplyView;
import java.util.Objects;
import kotlin.jvm.functions.Function0;
/* compiled from: SmartReplyStateInflater.kt */
/* loaded from: classes.dex */
public final class SmartReplyInflaterImpl implements SmartReplyInflater {
    public final SmartReplyConstants constants;
    public final Context context;
    public final KeyguardDismissUtil keyguardDismissUtil;
    public final NotificationRemoteInputManager remoteInputManager;
    public final SmartReplyController smartReplyController;

    @Override // com.android.systemui.statusbar.policy.SmartReplyInflater
    public final Button inflateReplyButton(final SmartReplyView smartReplyView, final NotificationEntry notificationEntry, final SmartReplyView.SmartReplies smartReplies, final int i, final CharSequence charSequence, boolean z) {
        View inflate = LayoutInflater.from(smartReplyView.getContext()).inflate(2131624490, (ViewGroup) smartReplyView, false);
        Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.widget.Button");
        final Button button = (Button) inflate;
        button.setText(charSequence);
        View.OnClickListener smartReplyInflaterImpl$inflateReplyButton$1$onClickListener$1 = new View.OnClickListener() { // from class: com.android.systemui.statusbar.policy.SmartReplyInflaterImpl$inflateReplyButton$1$onClickListener$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SmartReplyInflaterImpl smartReplyInflaterImpl = SmartReplyInflaterImpl.this;
                NotificationEntry notificationEntry2 = notificationEntry;
                SmartReplyView.SmartReplies smartReplies2 = smartReplies;
                int i2 = i;
                SmartReplyView smartReplyView2 = smartReplyView;
                Button button2 = button;
                CharSequence charSequence2 = charSequence;
                Objects.requireNonNull(smartReplyInflaterImpl);
                final SmartReplyInflaterImpl$onSmartReplyClick$1 smartReplyInflaterImpl$onSmartReplyClick$1 = new SmartReplyInflaterImpl$onSmartReplyClick$1(smartReplyInflaterImpl, smartReplies2, button2, charSequence2, i2, notificationEntry2, smartReplyView2);
                boolean z2 = SmartReplyStateInflaterKt.DEBUG;
                smartReplyInflaterImpl.keyguardDismissUtil.executeWhenUnlocked(new ActivityStarter.OnDismissAction() { // from class: com.android.systemui.statusbar.policy.SmartReplyStateInflaterKt$sam$com_android_systemui_plugins_ActivityStarter_OnDismissAction$0
                    @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
                    public final /* synthetic */ boolean onDismiss() {
                        return ((Boolean) Function0.this.invoke()).booleanValue();
                    }
                }, !notificationEntry2.isRowPinned(), false);
            }
        };
        if (z) {
            SmartReplyConstants smartReplyConstants = this.constants;
            Objects.requireNonNull(smartReplyConstants);
            smartReplyInflaterImpl$inflateReplyButton$1$onClickListener$1 = new DelayedOnClickListener(smartReplyInflaterImpl$inflateReplyButton$1$onClickListener$1, smartReplyConstants.mOnClickInitDelay);
        }
        button.setOnClickListener(smartReplyInflaterImpl$inflateReplyButton$1$onClickListener$1);
        button.setAccessibilityDelegate(new View.AccessibilityDelegate() { // from class: com.android.systemui.statusbar.policy.SmartReplyInflaterImpl$inflateReplyButton$1$1
            @Override // android.view.View.AccessibilityDelegate
            public final void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, SmartReplyView.this.getResources().getString(2131951808)));
            }
        });
        ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
        Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type com.android.systemui.statusbar.policy.SmartReplyView.LayoutParams");
        ((SmartReplyView.LayoutParams) layoutParams).mButtonType = SmartReplyView.SmartButtonType.REPLY;
        return button;
    }

    public SmartReplyInflaterImpl(SmartReplyConstants smartReplyConstants, KeyguardDismissUtil keyguardDismissUtil, NotificationRemoteInputManager notificationRemoteInputManager, SmartReplyController smartReplyController, Context context) {
        this.constants = smartReplyConstants;
        this.keyguardDismissUtil = keyguardDismissUtil;
        this.remoteInputManager = notificationRemoteInputManager;
        this.smartReplyController = smartReplyController;
        this.context = context;
    }

    public static final Intent access$createRemoteInputIntent(SmartReplyInflaterImpl smartReplyInflaterImpl, SmartReplyView.SmartReplies smartReplies, CharSequence charSequence) {
        Objects.requireNonNull(smartReplyInflaterImpl);
        Bundle bundle = new Bundle();
        bundle.putString(smartReplies.remoteInput.getResultKey(), charSequence.toString());
        Intent addFlags = new Intent().addFlags(268435456);
        RemoteInput.addResultsToIntent(new RemoteInput[]{smartReplies.remoteInput}, addFlags, bundle);
        RemoteInput.setResultsSource(addFlags, 1);
        return addFlags;
    }
}
