package com.android.systemui.people;

import android.app.Activity;
import android.app.people.PeopleSpaceTile;
import android.content.Context;
import android.content.Intent;
import android.graphics.Outline;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.people.widget.PeopleTileKey;
import com.android.systemui.theme.ThemeOverlayApplier$$ExternalSyntheticLambda7;
import com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda10;
import com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda25;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
/* loaded from: classes.dex */
public class PeopleSpaceActivity extends Activity {
    public static final /* synthetic */ int $r8$clinit = 0;
    public int mAppWidgetId;
    public Context mContext;
    public PeopleSpaceWidgetManager mPeopleSpaceWidgetManager;
    public AnonymousClass1 mViewOutlineProvider = new ViewOutlineProvider() { // from class: com.android.systemui.people.PeopleSpaceActivity.1
        @Override // android.view.ViewOutlineProvider
        public final void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), PeopleSpaceActivity.this.mContext.getResources().getDimension(2131166764));
        }
    };

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.people.PeopleSpaceActivity$1] */
    public PeopleSpaceActivity(PeopleSpaceWidgetManager peopleSpaceWidgetManager) {
        this.mPeopleSpaceWidgetManager = peopleSpaceWidgetManager;
    }

    @Override // android.app.Activity
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mContext = getApplicationContext();
        this.mAppWidgetId = getIntent().getIntExtra("appWidgetId", 0);
        setResult(0);
    }

    @Override // android.app.Activity
    public final void onResume() {
        super.onResume();
        List<PeopleSpaceTile> arrayList = new ArrayList<>();
        List<PeopleSpaceTile> arrayList2 = new ArrayList<>();
        try {
            PeopleSpaceWidgetManager peopleSpaceWidgetManager = this.mPeopleSpaceWidgetManager;
            Objects.requireNonNull(peopleSpaceWidgetManager);
            arrayList = PeopleSpaceUtils.getSortedTiles(peopleSpaceWidgetManager.mIPeopleManager, peopleSpaceWidgetManager.mLauncherApps, peopleSpaceWidgetManager.mUserManager, peopleSpaceWidgetManager.mINotificationManager.getConversations(true).getList().stream().filter(WifiPickerTracker$$ExternalSyntheticLambda25.INSTANCE$1).map(WifiPickerTracker$$ExternalSyntheticLambda10.INSTANCE$2));
            arrayList2 = this.mPeopleSpaceWidgetManager.getRecentTiles();
        } catch (Exception e) {
            Log.e("PeopleSpaceActivity", "Couldn't retrieve conversations", e);
        }
        if (!arrayList2.isEmpty() || !arrayList.isEmpty()) {
            setContentView(2131624353);
            setTileViews(2131428611, 2131428616, arrayList);
            setTileViews(2131428668, 2131428671, arrayList2);
            return;
        }
        setContentView(2131624355);
        ((GradientDrawable) ((LinearLayout) findViewById(16908288)).getBackground()).setColor(this.mContext.getTheme().obtainStyledAttributes(new int[]{17956909}).getColor(0, -1));
    }

    public final void setTileViews(int i, int i2, List<PeopleSpaceTile> list) {
        boolean z;
        boolean z2;
        if (list.isEmpty()) {
            ((LinearLayout) findViewById(i)).setVisibility(8);
            return;
        }
        ViewGroup viewGroup = (ViewGroup) findViewById(i2);
        viewGroup.setClipToOutline(true);
        viewGroup.setOutlineProvider(this.mViewOutlineProvider);
        for (int i3 = 0; i3 < list.size(); i3++) {
            final PeopleSpaceTile peopleSpaceTile = list.get(i3);
            Context context = this.mContext;
            String id = peopleSpaceTile.getId();
            if (i3 == list.size() - 1) {
                z = true;
            } else {
                z = false;
            }
            PeopleSpaceTileView peopleSpaceTileView = new PeopleSpaceTileView(context, viewGroup, id, z);
            try {
                if (peopleSpaceTile.getUserName() != null) {
                    peopleSpaceTileView.mNameView.setText(peopleSpaceTile.getUserName().toString());
                }
                Context context2 = this.mContext;
                float f = context2.getResources().getDisplayMetrics().density;
                Pattern pattern = PeopleTileViewHelper.DOUBLE_EXCLAMATION_PATTERN;
                int dimension = (int) (context2.getResources().getDimension(2131165353) / f);
                if (peopleSpaceTile.getStatuses() == null || !peopleSpaceTile.getStatuses().stream().anyMatch(ThemeOverlayApplier$$ExternalSyntheticLambda7.INSTANCE$1)) {
                    z2 = false;
                } else {
                    z2 = true;
                }
                peopleSpaceTileView.mPersonIconView.setImageBitmap(PeopleTileViewHelper.getPersonIconBitmap(context2, peopleSpaceTile, dimension, z2));
                final PeopleTileKey peopleTileKey = new PeopleTileKey(peopleSpaceTile);
                peopleSpaceTileView.setOnClickListener(new View.OnClickListener(peopleSpaceTile, peopleTileKey) { // from class: com.android.systemui.people.PeopleSpaceActivity$$ExternalSyntheticLambda0
                    public final /* synthetic */ PeopleTileKey f$2;

                    {
                        this.f$2 = peopleTileKey;
                    }

                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        PeopleSpaceActivity peopleSpaceActivity = PeopleSpaceActivity.this;
                        PeopleTileKey peopleTileKey2 = this.f$2;
                        int i4 = PeopleSpaceActivity.$r8$clinit;
                        Objects.requireNonNull(peopleSpaceActivity);
                        peopleSpaceActivity.mPeopleSpaceWidgetManager.addNewWidget(peopleSpaceActivity.mAppWidgetId, peopleTileKey2);
                        Intent intent = new Intent();
                        intent.putExtra("appWidgetId", peopleSpaceActivity.mAppWidgetId);
                        peopleSpaceActivity.setResult(-1, intent);
                        peopleSpaceActivity.finish();
                    }
                });
            } catch (Exception e) {
                Log.e("PeopleSpaceActivity", "Couldn't retrieve shortcut information", e);
            }
        }
    }

    public void dismissActivity(View view) {
        finish();
    }
}
