package com.android.wm.shell.bubbles;

import android.content.Context;
import android.content.pm.ShortcutInfo;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.internal.util.ContrastColorUtil;
import com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda13;
import com.android.wm.shell.bubbles.BadgedImageView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: BubbleOverflowContainerView.java */
/* loaded from: classes.dex */
public final class BubbleOverflowAdapter extends RecyclerView.Adapter<ViewHolder> {
    public List<Bubble> mBubbles;
    public Context mContext;
    public BubblePositioner mPositioner;
    public Consumer<Bubble> mPromoteBubbleFromOverflow;

    /* compiled from: BubbleOverflowContainerView.java */
    /* loaded from: classes.dex */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public BadgedImageView iconView;
        public TextView textView;

        public ViewHolder(LinearLayout linearLayout, BubblePositioner bubblePositioner) {
            super(linearLayout);
            BadgedImageView badgedImageView = (BadgedImageView) linearLayout.findViewById(2131427634);
            this.iconView = badgedImageView;
            badgedImageView.initialize(bubblePositioner);
            this.textView = (TextView) linearLayout.findViewById(2131427635);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final int getItemCount() {
        return this.mBubbles.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final void onBindViewHolder(ViewHolder viewHolder, int i) {
        CharSequence charSequence;
        ViewHolder viewHolder2 = viewHolder;
        final Bubble bubble = this.mBubbles.get(i);
        viewHolder2.iconView.setRenderedBubble(bubble);
        viewHolder2.iconView.removeDotSuppressionFlag(BadgedImageView.SuppressionFlag.FLYOUT_VISIBLE);
        viewHolder2.iconView.setOnClickListener(new View.OnClickListener() { // from class: com.android.wm.shell.bubbles.BubbleOverflowAdapter$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                BubbleOverflowAdapter bubbleOverflowAdapter = BubbleOverflowAdapter.this;
                Bubble bubble2 = bubble;
                Objects.requireNonNull(bubbleOverflowAdapter);
                bubbleOverflowAdapter.mBubbles.remove(bubble2);
                bubbleOverflowAdapter.notifyDataSetChanged();
                bubbleOverflowAdapter.mPromoteBubbleFromOverflow.accept(bubble2);
            }
        });
        String str = bubble.mTitle;
        if (str == null) {
            str = this.mContext.getResources().getString(2131952883);
        }
        viewHolder2.iconView.setContentDescription(this.mContext.getResources().getString(2131952062, str, bubble.mAppName));
        viewHolder2.iconView.setAccessibilityDelegate(new View.AccessibilityDelegate() { // from class: com.android.wm.shell.bubbles.BubbleOverflowAdapter.1
            @Override // android.view.View.AccessibilityDelegate
            public final void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, BubbleOverflowAdapter.this.mContext.getResources().getString(2131952057)));
            }
        });
        ShortcutInfo shortcutInfo = bubble.mShortcutInfo;
        if (shortcutInfo != null) {
            charSequence = shortcutInfo.getLabel();
        } else {
            charSequence = bubble.mAppName;
        }
        viewHolder2.textView.setText(charSequence);
    }

    public BubbleOverflowAdapter(Context context, ArrayList arrayList, NavigationBar$$ExternalSyntheticLambda13 navigationBar$$ExternalSyntheticLambda13, BubblePositioner bubblePositioner) {
        this.mContext = context;
        this.mBubbles = arrayList;
        this.mPromoteBubbleFromOverflow = navigationBar$$ExternalSyntheticLambda13;
        this.mPositioner = bubblePositioner;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final RecyclerView.ViewHolder onCreateViewHolder(RecyclerView recyclerView, int i) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(recyclerView.getContext()).inflate(2131624024, (ViewGroup) recyclerView, false);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(new int[]{16844002, 16842806});
        int ensureTextContrast = ContrastColorUtil.ensureTextContrast(obtainStyledAttributes.getColor(1, -16777216), obtainStyledAttributes.getColor(0, -1), true);
        obtainStyledAttributes.recycle();
        ((TextView) linearLayout.findViewById(2131427635)).setTextColor(ensureTextContrast);
        return new ViewHolder(linearLayout, this.mPositioner);
    }
}
