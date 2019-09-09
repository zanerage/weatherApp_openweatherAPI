package com.simple.weather;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.simple.weather.data.AppConfig;
import com.simple.weather.data.Constant;
import com.simple.weather.data.GDPR;
import com.simple.weather.data.GlobalVariable;
import com.simple.weather.data.Utils;


public class ActivitySetting extends AppCompatActivity {

    private LinearLayout lyt_unit, lyt_rate, lyt_about, lyt_more_app;
    private GlobalVariable global;
    private TextView tv_unit, tv_refresh;
    String[] string_unit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        global = (GlobalVariable) getApplication();

        string_unit = getResources().getStringArray(R.array.string_unit);

        lyt_unit = (LinearLayout) findViewById(R.id.lyt_unit);
        lyt_about = (LinearLayout) findViewById(R.id.lyt_about);
        lyt_rate = (LinearLayout) findViewById(R.id.lyt_rate);
        lyt_more_app = (LinearLayout) findViewById(R.id.lyt_more_app);
        tv_unit = (TextView) findViewById(R.id.tv_unit);

        tv_unit.setText(string_unit[global.getIntPref(Constant.I_KEY_UNIT, 0)]);
        actionMenu();
        initToolbar();
        initBannerAds();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(R.string.action_settings);
        Utils.systemBarLollipop(this);
    }

    private void actionMenu() {
        //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
        lyt_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUnit();
            }

        });


        lyt_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAbout();
            }
        });


        lyt_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });

        lyt_more_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.directLinkToBrowser(ActivitySetting.this, getString(R.string.more_app_url));
            }
        });

    }

    protected void dialogAbout() {
        final Dialog dialog = new Dialog(ActivitySetting.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_about);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        final Button button_ok = (Button) dialog.findViewById(R.id.button_ok);

        button_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    AlertDialog unitDialog = null;

    public void dialogUnit() {

        // Creating and Building the Dialog 
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Unit Temperature");
        builder.setSingleChoiceItems(string_unit, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        global.setIntPref(Constant.I_KEY_UNIT, 0);
                        break;
                    case 1:
                        global.setIntPref(Constant.I_KEY_UNIT, 1);
                        break;
                }
                unitDialog.dismiss();
                tv_unit.setText(string_unit[global.getIntPref(Constant.I_KEY_UNIT, 0)]);
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                unitDialog.dismiss();
            }
        });
        unitDialog = builder.create();
        unitDialog.show();
    }

    private void initBannerAds() {
        if (!AppConfig.ENABLE_SETTING_BANNER) return;
        final AdView mAdView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, GDPR.getBundleAd(this)).build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mAdView.setVisibility(View.VISIBLE);
                super.onAdLoaded();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(getApplicationContext(), ActivityMain.class);
                startActivity(i);
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), ActivityMain.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }


}
