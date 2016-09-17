package net.taobits.androidadmobtest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import net.taobits.android.AdDisplay;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addAdMobAd(this);
    }

    @Override
    public void onResume() {
        adDisplay.resume();
        super.onResume();
    }

    @Override
    public void onPause() {
        adDisplay.pause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        adDisplay.destroy();
        super.onDestroy();
    }

    private void addAdMobAd(Activity activity){
        LinearLayout adsView = (LinearLayout) activity.findViewById(R.id.ads);
        adDisplay = new AdDisplay(activity, adsView, 320, 50);
    }

    private AdDisplay adDisplay;
}
