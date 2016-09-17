package net.taobits.androidadmobtest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAds();
        addAdMobAd(this);
    }

    private void initAds() {
        MobileAds.initialize(getApplicationContext(), ADMOB_ADUNIT_ID);
    }

    private void addAdMobAd(Activity activity){
        AdSize adSize = AdSize.BANNER;
        LinearLayout adsView = (LinearLayout) activity.findViewById(R.id.ads);
        if (adsView == null) return;
        AdView admobAdView = new AdView(activity);
        admobAdView.setAdUnitId(ADMOB_ADUNIT_ID);
        admobAdView.setAdSize(adSize);
        adsView.addView(admobAdView);
        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
        adRequestBuilder.addTestDevice("B0BEAA292BC8F9DA9328AC194C8FFBED");
        AdRequest adRequest = adRequestBuilder.build();
        admobAdView.loadAd(adRequest);
    }

    public final static String ADMOB_ADUNIT_ID = "ca-app-pub-3940256099942544~3347511713"; // Replace with your Admob-Id
}
