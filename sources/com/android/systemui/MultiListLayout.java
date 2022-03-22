package com.android.systemui;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import androidx.preference.R$id;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
import com.android.systemui.globalactions.GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda5;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class MultiListLayout extends LinearLayout {
    public MultiListAdapter mAdapter;
    public int mRotation;
    public RotationListener mRotationListener;

    /* loaded from: classes.dex */
    public static abstract class MultiListAdapter extends BaseAdapter {
    }

    /* loaded from: classes.dex */
    public interface RotationListener {
    }

    public abstract ViewGroup getListView();

    public abstract ViewGroup getSeparatedView();

    public MultiListLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mRotation = R$id.getRotation(context);
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int rotation = R$id.getRotation(((LinearLayout) this).mContext);
        if (rotation != this.mRotation) {
            RotationListener rotationListener = this.mRotationListener;
            if (rotationListener != null) {
                ((GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda5) rotationListener).onRotate();
            }
            this.mRotation = rotation;
        }
    }

    public void onUpdateList() {
        removeAllItems();
        MultiListAdapter multiListAdapter = this.mAdapter;
        Objects.requireNonNull(multiListAdapter);
        boolean z = true;
        int i = 0;
        if (((GlobalActionsDialogLite.MyAdapter) multiListAdapter).countItems(true) <= 0) {
            z = false;
        }
        ViewGroup separatedView = getSeparatedView();
        if (separatedView != null) {
            if (!z) {
                i = 8;
            }
            separatedView.setVisibility(i);
        }
    }

    public void removeAllItems() {
        removeAllListViews();
        ViewGroup separatedView = getSeparatedView();
        if (separatedView != null) {
            separatedView.removeAllViews();
        }
    }

    public void removeAllListViews() {
        ViewGroup listView = getListView();
        if (listView != null) {
            listView.removeAllViews();
        }
    }
}
