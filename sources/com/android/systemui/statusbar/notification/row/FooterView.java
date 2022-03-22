package com.android.systemui.statusbar.notification.row;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.IndentingPrintWriter;
import android.view.View;
import android.widget.FrameLayout;
import androidx.fragment.R$id;
import com.android.systemui.statusbar.notification.stack.ExpandableViewState;
import com.android.wm.shell.pip.phone.PipController$PipImpl$$ExternalSyntheticLambda0;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: classes.dex */
public class FooterView extends StackScrollerDecorView {
    public static final /* synthetic */ int $r8$clinit = 0;
    public FooterViewButton mClearAllButton;
    public FooterViewButton mManageButton;
    public String mManageNotificationHistoryText;
    public String mManageNotificationText;
    public boolean mShowHistory;

    /* loaded from: classes.dex */
    public class FooterViewState extends ExpandableViewState {
        public boolean hideContent;

        @Override // com.android.systemui.statusbar.notification.stack.ExpandableViewState, com.android.systemui.statusbar.notification.stack.ViewState
        public final void applyToView(View view) {
            super.applyToView(view);
            if (view instanceof FooterView) {
                ((FooterView) view).setContentVisible(!this.hideContent);
            }
        }

        @Override // com.android.systemui.statusbar.notification.stack.ExpandableViewState, com.android.systemui.statusbar.notification.stack.ViewState
        public final void copyFrom(ExpandableViewState expandableViewState) {
            super.copyFrom(expandableViewState);
            if (expandableViewState instanceof FooterViewState) {
                this.hideContent = ((FooterViewState) expandableViewState).hideContent;
            }
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final ExpandableViewState createExpandableViewState() {
        return new FooterViewState();
    }

    public final void updateColors() {
        Resources.Theme theme = ((FrameLayout) this).mContext.getTheme();
        int color = getResources().getColor(2131100468, theme);
        this.mClearAllButton.setBackground(theme.getDrawable(2131232514));
        this.mClearAllButton.setTextColor(color);
        this.mManageButton.setBackground(theme.getDrawable(2131232514));
        this.mManageButton.setTextColor(color);
    }

    public final void updateText() {
        if (this.mShowHistory) {
            this.mManageButton.setText(this.mManageNotificationHistoryText);
            this.mManageButton.setContentDescription(this.mManageNotificationHistoryText);
            return;
        }
        this.mManageButton.setText(this.mManageNotificationText);
        this.mManageButton.setContentDescription(this.mManageNotificationText);
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter asIndenting = R$id.asIndenting(printWriter);
        super.dump(fileDescriptor, asIndenting, strArr);
        R$id.withIncreasedIndent(asIndenting, new PipController$PipImpl$$ExternalSyntheticLambda0(this, asIndenting, 1));
    }

    @Override // com.android.systemui.statusbar.notification.row.StackScrollerDecorView
    public final View findContentView() {
        return findViewById(2131427738);
    }

    @Override // com.android.systemui.statusbar.notification.row.StackScrollerDecorView
    public final View findSecondaryView() {
        return findViewById(2131427853);
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateColors();
        this.mClearAllButton.setText(2131952109);
        this.mClearAllButton.setContentDescription(((FrameLayout) this).mContext.getString(2131951689));
        this.mManageNotificationText = getContext().getString(2131952708);
        this.mManageNotificationHistoryText = getContext().getString(2131952707);
        updateText();
    }

    @Override // com.android.systemui.statusbar.notification.row.StackScrollerDecorView, android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mClearAllButton = (FooterViewButton) findSecondaryView();
        this.mManageButton = (FooterViewButton) findViewById(2131428299);
        this.mManageNotificationText = getContext().getString(2131952708);
        this.mManageNotificationHistoryText = getContext().getString(2131952707);
        updateText();
    }

    public FooterView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
