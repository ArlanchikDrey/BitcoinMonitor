package bitcoin.crypto.booster.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Collections;
import java.util.List;

import bitcoin.crypto.booster.R;
import bitcoin.crypto.booster.Ti;
import bitcoin.crypto.booster.fragments.FragmentError;
import bitcoin.crypto.booster.fragments.FragmentInfo;
import bitcoin.crypto.booster.fragments.FragmentTicker;
import bitcoin.crypto.booster.mo.Ticker;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    Drawer result;
    int itemId = -1;
    List<Ticker> data;
    private boolean showAd = false;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        result.saveInstanceState(outState);
        outState.putInt("itemId", itemId);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.a_name));
        data = Ti.data(this);
        if(data==null)
            data = Collections.singletonList(new Ticker());
        init(savedInstanceState);

        StartAppAd.disableSplash();
        StartAppSDK.init(this, getString(R.string.a_start), false);


        if (!getSharedPreferences("PREFS", 0).getBoolean("first", false)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage(getString(R.string.alert_short))
                    .setTitle(getString(R.string.alert_title))
                    .setNegativeButton(getString(R.string.alert_no), null)
                    .setPositiveButton(getString(R.string.alert_yes), (dialog, which) -> {
                        getSharedPreferences("PREFS", 0).edit().putBoolean("first", true).apply();
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            Button btn = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            btn.setOnClickListener(view -> {
                TextView tv_message = (TextView) alertDialog.findViewById(android.R.id.message);
                if (tv_message != null) {
                    tv_message.setText(getString(R.string.alert_full));
                }
                btn.setVisibility(View.GONE);

            });
        }
    }

    private void init(Bundle savedInstanceState) {

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName(getString(R.string.item1)).withTag(getString(R.string.item1)).withIcon(R.drawable.ic_btc).withIdentifier(1);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName(getString(R.string.item2)).withTag(getString(R.string.item2)).withIcon(R.drawable.ic_xrp).withIdentifier(2);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withName(getString(R.string.item3)).withTag(getString(R.string.item3)).withIcon(R.drawable.ic_eth).withIdentifier(3);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withName(getString(R.string.item4)).withTag(getString(R.string.item4)).withIcon(R.drawable.ic_stellar).withIdentifier(4);
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withName(getString(R.string.item5)).withTag(getString(R.string.item5)).withIcon(R.drawable.ic_btc_cash).withIdentifier(5);

        result = new DrawerBuilder()
                .withSavedInstance(savedInstanceState)
                .withActivity(this)
                .withRootView(R.id.drawer_container)
                .withToolbar(toolbar)
                .withDisplayBelowStatusBar(false)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(item1,item2,item3,item4,item5,
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(getString(R.string.item6)).withTag(getString(R.string.item6)).withName(getString(R.string.item6)).withIcon(FontAwesome.Icon.faw_question).withIdentifier(90),
                        new PrimaryDrawerItem().withName(getString(R.string.item7)).withName(getString(R.string.item7)).withIcon(FontAwesome.Icon.faw_sign_out_alt).withIdentifier(99)
                )
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    if (drawerItem != null) {
                        if(drawerItem.getIdentifier()==99) {
                            StartAppAd.showAd(MainActivity.this);
                            MainActivity.this.finish();

                        }
                        else {
                            setTitle(drawerItem.getTag().toString());
                            changeFragment(drawerItem.getIdentifier());
                        }

                    }
                    return false;
                })
                .build();

        if (itemId == -1)
            result.setSelection(1, true);
        else
            result.setSelection(itemId, false);
    }

    private void changeFragment(long identifier) {
        Fragment fragment = new FragmentError();
        if(data.get(0)!=null) fragment = FragmentTicker.newInstance(data.get(0));
        if(identifier==2 && data.get(2)!=null) fragment = FragmentTicker.newInstance(data.get(2));
        if(identifier==3 && data.get(1)!=null) fragment = FragmentTicker.newInstance(data.get(1));
        if(identifier==4 && data.get(9)!=null) fragment = FragmentTicker.newInstance(data.get(9));
        if(identifier==5 && data.get(3)!=null) fragment = FragmentTicker.newInstance(data.get(3));

        if(identifier==90) fragment = new FragmentInfo();
        FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment).commit();

    }

    @Override
    public void onBackPressed() {
        StartAppAd.onBackPressed(this);
        super.onBackPressed();
    }
}
