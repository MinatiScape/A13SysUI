package com.android.systemui.statusbar.tv.notifications;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.SparseArray;
import android.view.View;
import androidx.leanback.widget.VerticalGridView;
import com.android.systemui.statusbar.tv.notifications.TvNotificationHandler;
import com.android.wm.shell.ShellTaskOrganizer$$ExternalSyntheticLambda0;
import java.util.Objects;
/* loaded from: classes.dex */
public class TvNotificationPanelActivity extends Activity implements TvNotificationHandler.Listener {
    public static final /* synthetic */ int $r8$clinit = 0;
    public VerticalGridView mNotificationListView;
    public View mNotificationPlaceholder;
    public TvNotificationAdapter mTvNotificationAdapter;
    public final TvNotificationHandler mTvNotificationHandler;
    public boolean mPanelAlreadyOpen = false;
    public final ShellTaskOrganizer$$ExternalSyntheticLambda0 mBlurConsumer = new ShellTaskOrganizer$$ExternalSyntheticLambda0(this, 4);

    public final void notificationsUpdated(SparseArray<StatusBarNotification> sparseArray) {
        boolean z;
        int i;
        TvNotificationAdapter tvNotificationAdapter = this.mTvNotificationAdapter;
        Objects.requireNonNull(tvNotificationAdapter);
        tvNotificationAdapter.mNotifications = sparseArray;
        tvNotificationAdapter.notifyDataSetChanged();
        int i2 = 0;
        if (sparseArray.size() == 0) {
            z = true;
        } else {
            z = false;
        }
        VerticalGridView verticalGridView = this.mNotificationListView;
        if (z) {
            i = 8;
        } else {
            i = 0;
        }
        verticalGridView.setVisibility(i);
        View view = this.mNotificationPlaceholder;
        if (!z) {
            i2 = 8;
        }
        view.setVisibility(i2);
    }

    public TvNotificationPanelActivity(TvNotificationHandler tvNotificationHandler) {
        this.mTvNotificationHandler = tvNotificationHandler;
    }

    public final boolean maybeClosePanel(Intent intent) {
        if (!"android.app.action.CLOSE_NOTIFICATION_HANDLER_PANEL".equals(intent.getAction()) && (!this.mPanelAlreadyOpen || !"android.app.action.TOGGLE_NOTIFICATION_HANDLER_PANEL".equals(intent.getAction()))) {
            return false;
        }
        finish();
        return true;
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        getWindow().setGravity(8388613);
        getWindowManager().addCrossWindowBlurEnabledListener(this.mBlurConsumer);
    }

    @Override // android.app.Activity
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!maybeClosePanel(getIntent())) {
            this.mPanelAlreadyOpen = true;
            setContentView(2131624631);
            this.mNotificationPlaceholder = findViewById(2131428501);
            this.mTvNotificationAdapter = new TvNotificationAdapter();
            VerticalGridView verticalGridView = (VerticalGridView) findViewById(2131428524);
            this.mNotificationListView = verticalGridView;
            verticalGridView.setAdapter(this.mTvNotificationAdapter);
            VerticalGridView verticalGridView2 = this.mNotificationListView;
            Objects.requireNonNull(verticalGridView2);
            verticalGridView2.mLayoutManager.setRowHeight(2131167247);
            verticalGridView2.requestLayout();
            TvNotificationHandler tvNotificationHandler = this.mTvNotificationHandler;
            Objects.requireNonNull(tvNotificationHandler);
            tvNotificationHandler.mUpdateListener = this;
            TvNotificationHandler tvNotificationHandler2 = this.mTvNotificationHandler;
            Objects.requireNonNull(tvNotificationHandler2);
            notificationsUpdated(tvNotificationHandler2.mNotifications);
        }
    }

    @Override // android.app.Activity
    public final void onDestroy() {
        super.onDestroy();
        TvNotificationHandler tvNotificationHandler = this.mTvNotificationHandler;
        Objects.requireNonNull(tvNotificationHandler);
        tvNotificationHandler.mUpdateListener = null;
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getWindowManager().removeCrossWindowBlurEnabledListener(this.mBlurConsumer);
    }

    @Override // android.app.Activity
    public final void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        maybeClosePanel(intent);
    }
}
