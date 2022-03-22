package com.android.systemui.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.UserManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.helper.widget.Flow;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.util.LifecycleActivity;
import java.util.ArrayList;
import java.util.Objects;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: UserSwitcherActivity.kt */
/* loaded from: classes.dex */
public final class UserSwitcherActivity extends LifecycleActivity {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final UserSwitcherActivity$adapter$1 adapter;
    public View addButton;
    public final BroadcastDispatcher broadcastDispatcher;
    public UserSwitcherActivity$initBroadcastReceiver$1 broadcastReceiver;
    public final FalsingManager falsingManager;
    public final LayoutInflater layoutInflater;
    public ViewGroup parent;
    public final ShadeController shadeController;
    public final UserManager userManager;
    public final UserSwitcherController userSwitcherController;
    public ArrayList addUserRecords = new ArrayList();
    public final UserSwitcherController.UserRecord manageUserRecord = new UserSwitcherController.UserRecord(null, null, false, false, false, false, false, false);

    /* compiled from: UserSwitcherActivity.kt */
    /* loaded from: classes.dex */
    public static final class ItemAdapter extends ArrayAdapter<UserSwitcherController.UserRecord> {
        public final Function1<UserSwitcherController.UserRecord, Drawable> iconGetter;
        public final LayoutInflater layoutInflater;
        public final int resource = 2131624647;
        public final Function1<UserSwitcherController.UserRecord, String> textGetter;

        public ItemAdapter(UserSwitcherActivity userSwitcherActivity, LayoutInflater layoutInflater, Function1 function1, Function1 function12) {
            super(userSwitcherActivity, 2131624647);
            this.layoutInflater = layoutInflater;
            this.textGetter = function1;
            this.iconGetter = function12;
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public final View getView(int i, View view, ViewGroup viewGroup) {
            UserSwitcherController.UserRecord item = getItem(i);
            if (view == null) {
                view = this.layoutInflater.inflate(this.resource, viewGroup, false);
            }
            ((ImageView) view.requireViewById(2131428102)).setImageDrawable(this.iconGetter.invoke(item));
            ((TextView) view.requireViewById(2131429024)).setText(this.textGetter.invoke(item));
            return view;
        }
    }

    public final void buildUserViews() {
        ViewGroup viewGroup = this.parent;
        View view = null;
        if (viewGroup == null) {
            viewGroup = null;
        }
        int childCount = viewGroup.getChildCount();
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (i < childCount) {
            int i4 = i + 1;
            ViewGroup viewGroup2 = this.parent;
            if (viewGroup2 == null) {
                viewGroup2 = null;
            }
            if (Intrinsics.areEqual(viewGroup2.getChildAt(i).getTag(), "user_view")) {
                if (i2 != 0) {
                    i = i3;
                }
                i2++;
                i3 = i;
            }
            i = i4;
        }
        ViewGroup viewGroup3 = this.parent;
        if (viewGroup3 == null) {
            viewGroup3 = null;
        }
        viewGroup3.removeViews(i3, i2);
        this.addUserRecords.clear();
        Flow flow = (Flow) requireViewById(2131427981);
        int count = this.adapter.getCount();
        int i5 = 0;
        while (i5 < count) {
            int i6 = i5 + 1;
            final UserSwitcherController.UserRecord item = this.adapter.getItem(i5);
            if (item.isAddUser || item.isAddSupervisedUser || (item.isGuest && item.info == null)) {
                this.addUserRecords.add(item);
            } else {
                UserSwitcherActivity$adapter$1 userSwitcherActivity$adapter$1 = this.adapter;
                ViewGroup viewGroup4 = this.parent;
                if (viewGroup4 == null) {
                    viewGroup4 = null;
                }
                View view2 = userSwitcherActivity$adapter$1.getView(i5, null, viewGroup4);
                view2.setId(View.generateViewId());
                ViewGroup viewGroup5 = this.parent;
                if (viewGroup5 == null) {
                    viewGroup5 = null;
                }
                viewGroup5.addView(view2);
                flow.addView(view2);
                view2.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.user.UserSwitcherActivity$buildUserViews$1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view3) {
                        if (!UserSwitcherActivity.this.falsingManager.isFalseTap(1) && view3.isEnabled()) {
                            UserSwitcherController.UserRecord userRecord = item;
                            if (!userRecord.isCurrent || userRecord.isGuest) {
                                UserSwitcherActivity$adapter$1 userSwitcherActivity$adapter$12 = UserSwitcherActivity.this.adapter;
                                Objects.requireNonNull(userSwitcherActivity$adapter$12);
                                userSwitcherActivity$adapter$12.onUserListItemClicked(userRecord, null);
                            }
                        }
                    }
                });
            }
            i5 = i6;
        }
        if (!this.addUserRecords.isEmpty()) {
            this.addUserRecords.add(this.manageUserRecord);
            View view3 = this.addButton;
            if (view3 != null) {
                view = view3;
            }
            view.setVisibility(0);
            return;
        }
        View view4 = this.addButton;
        if (view4 != null) {
            view = view4;
        }
        view.setVisibility(8);
    }

    public UserSwitcherActivity(UserSwitcherController userSwitcherController, BroadcastDispatcher broadcastDispatcher, LayoutInflater layoutInflater, FalsingManager falsingManager, UserManager userManager, ShadeController shadeController) {
        this.userSwitcherController = userSwitcherController;
        this.broadcastDispatcher = broadcastDispatcher;
        this.layoutInflater = layoutInflater;
        this.falsingManager = falsingManager;
        this.userManager = userManager;
        this.shadeController = shadeController;
        this.adapter = new UserSwitcherActivity$adapter$1(this, userSwitcherController);
    }

    /* JADX WARN: Type inference failed for: r9v12, types: [com.android.systemui.user.UserSwitcherActivity$initBroadcastReceiver$1] */
    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onCreate(Bundle bundle) {
        UserSwitcherActivity$initBroadcastReceiver$1 userSwitcherActivity$initBroadcastReceiver$1;
        super.onCreate(bundle);
        setContentView(2131624645);
        getWindow().getDecorView().setSystemUiVisibility(770);
        this.parent = (ViewGroup) requireViewById(2131429176);
        requireViewById(2131427659).setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.user.UserSwitcherActivity$onCreate$1$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                UserSwitcherActivity.this.finish();
            }
        });
        View requireViewById = requireViewById(2131427461);
        requireViewById.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.user.UserSwitcherActivity$onCreate$2$1
            /* JADX WARN: Type inference failed for: r1v1, types: [android.widget.ArrayAdapter, T, com.android.systemui.user.UserSwitcherActivity$ItemAdapter] */
            /* JADX WARN: Unknown variable types count: 1 */
            @Override // android.view.View.OnClickListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public final void onClick(android.view.View r6) {
                /*
                    r5 = this;
                    com.android.systemui.user.UserSwitcherActivity r5 = com.android.systemui.user.UserSwitcherActivity.this
                    int r6 = com.android.systemui.user.UserSwitcherActivity.$r8$clinit
                    java.util.Objects.requireNonNull(r5)
                    java.util.ArrayList r6 = new java.util.ArrayList
                    r6.<init>()
                    java.util.ArrayList r0 = r5.addUserRecords
                    java.util.Iterator r0 = r0.iterator()
                L_0x0012:
                    boolean r1 = r0.hasNext()
                    if (r1 == 0) goto L_0x0022
                    java.lang.Object r1 = r0.next()
                    com.android.systemui.statusbar.policy.UserSwitcherController$UserRecord r1 = (com.android.systemui.statusbar.policy.UserSwitcherController.UserRecord) r1
                    r6.add(r1)
                    goto L_0x0012
                L_0x0022:
                    kotlin.jvm.internal.Ref$ObjectRef r0 = new kotlin.jvm.internal.Ref$ObjectRef
                    r0.<init>()
                    com.android.systemui.user.UserSwitcherActivity$ItemAdapter r1 = new com.android.systemui.user.UserSwitcherActivity$ItemAdapter
                    android.view.LayoutInflater r2 = r5.layoutInflater
                    com.android.systemui.user.UserSwitcherActivity$showPopupMenu$popupMenuAdapter$1 r3 = new com.android.systemui.user.UserSwitcherActivity$showPopupMenu$popupMenuAdapter$1
                    r3.<init>(r5)
                    com.android.systemui.user.UserSwitcherActivity$showPopupMenu$popupMenuAdapter$2 r4 = new com.android.systemui.user.UserSwitcherActivity$showPopupMenu$popupMenuAdapter$2
                    r4.<init>(r5)
                    r1.<init>(r5, r2, r3, r4)
                    r0.element = r1
                    r1.addAll(r6)
                    com.android.systemui.user.UserSwitcherPopupMenu r6 = new com.android.systemui.user.UserSwitcherPopupMenu
                    com.android.systemui.plugins.FalsingManager r1 = r5.falsingManager
                    r6.<init>(r5, r1)
                    android.view.View r1 = r5.addButton
                    if (r1 != 0) goto L_0x0049
                    r1 = 0
                L_0x0049:
                    r6.setAnchorView(r1)
                    T r1 = r0.element
                    android.widget.ListAdapter r1 = (android.widget.ListAdapter) r1
                    r6.setAdapter(r1)
                    com.android.systemui.user.UserSwitcherActivity$showPopupMenu$2$1 r1 = new com.android.systemui.user.UserSwitcherActivity$showPopupMenu$2$1
                    r1.<init>()
                    r6.setOnItemClickListener(r1)
                    r6.show()
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.user.UserSwitcherActivity$onCreate$2$1.onClick(android.view.View):void");
            }
        });
        this.addButton = requireViewById;
        UserSwitcherController userSwitcherController = this.userSwitcherController;
        ViewGroup viewGroup = this.parent;
        if (viewGroup == null) {
            viewGroup = null;
        }
        Objects.requireNonNull(userSwitcherController);
        userSwitcherController.mView = viewGroup;
        this.broadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.user.UserSwitcherActivity$initBroadcastReceiver$1
            @Override // android.content.BroadcastReceiver
            public final void onReceive(Context context, Intent intent) {
                if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
                    UserSwitcherActivity.this.finish();
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        BroadcastDispatcher broadcastDispatcher = this.broadcastDispatcher;
        UserSwitcherActivity$initBroadcastReceiver$1 userSwitcherActivity$initBroadcastReceiver$12 = this.broadcastReceiver;
        if (userSwitcherActivity$initBroadcastReceiver$12 == null) {
            userSwitcherActivity$initBroadcastReceiver$1 = null;
        } else {
            userSwitcherActivity$initBroadcastReceiver$1 = userSwitcherActivity$initBroadcastReceiver$12;
        }
        BroadcastDispatcher.registerReceiver$default(broadcastDispatcher, userSwitcherActivity$initBroadcastReceiver$1, intentFilter, null, null, 28);
        buildUserViews();
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public final void onDestroy() {
        super.onDestroy();
        BroadcastDispatcher broadcastDispatcher = this.broadcastDispatcher;
        UserSwitcherActivity$initBroadcastReceiver$1 userSwitcherActivity$initBroadcastReceiver$1 = this.broadcastReceiver;
        if (userSwitcherActivity$initBroadcastReceiver$1 == null) {
            userSwitcherActivity$initBroadcastReceiver$1 = null;
        }
        broadcastDispatcher.unregisterReceiver(userSwitcherActivity$initBroadcastReceiver$1);
    }

    @Override // android.app.Activity
    public final void onBackPressed() {
        finish();
    }
}
