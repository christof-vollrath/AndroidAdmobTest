package net.taobits.android;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;

import com.amazon.device.ads.Ad;
import com.amazon.device.ads.AdError;
import com.amazon.device.ads.AdLayout;
import com.amazon.device.ads.AdListener;
import com.amazon.device.ads.AdProperties;
import com.amazon.device.ads.AdRegistration;
import com.amazon.device.ads.AdTargetingOptions;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.MediationBannerAdapter;
import com.google.android.gms.ads.mediation.MediationBannerListener;

// Inspired by https://github.com/sheng168/AndroidAds
// and the discussion https://groups.google.com/forum/#!topic/google-admob-ads-sdk/1-VB7GR0Z8g
// and the FacebookAdapter from Google
public class AmazonMediationBannerAdapter implements MediationBannerAdapter {

    @Override
    public void onDestroy() {
        // Clean up custom event variables.
        if (amazonAdView != null) {
            amazonAdView.destroy();
            amazonAdView = null;
        }
    }

    @Override
    public void onPause() {}

    @Override
    public void onResume() {}

    @Override
    public void requestBannerAd(Context context, MediationBannerListener mediationBannerListener, Bundle bundle, AdSize adSize,
                                MediationAdRequest mediationAdRequest, Bundle bundle1) {
        this.mediationBannerListener = mediationBannerListener;
        try {
            AdRegistration.setAppKey(AdDisplay.AMAZON_APP_KEY);
        } catch (Exception e) {
            Log.e("AdDisplay", "AdRegistration.setAppKey throws: " + e);
            mediationBannerListener.onAdFailedToLoad(this, 1);
            return;
        }
        AdRegistration.enableTesting(true);
        AdRegistration.enableLogging(true);

        amazonAdView = getAmazonAdView(context, adSize);
        if (amazonAdView == null)  {
            mediationBannerListener.onAdFailedToLoad(this, 2);
            return;
        }
        amazonAdView.setListener(new AmazonBannerListener());

        AdTargetingOptions adOptions = new AdTargetingOptions();
        amazonAdView.loadAd(adOptions);

    }

    @Override
    public View getBannerView() {
        return amazonAdView;
    }

    private com.amazon.device.ads.AdSize getAmazonAdSize(Context context, AdSize adSize) {
        return getAmazonAdSize(adSize.getHeight(), adSize.getWidth());
    }

    private com.amazon.device.ads.AdSize getAmazonAdSize(int adHeight, int adWidth) {
        com.amazon.device.ads.AdSize result = com.amazon.device.ads.AdSize.SIZE_320x50;
        if (adWidth >= 728 && adHeight >= 90) result = com.amazon.device.ads.AdSize.SIZE_728x90;
        else if (adWidth >= 600 && adHeight >= 90) result = com.amazon.device.ads.AdSize.SIZE_600x90;
        return result;
    }

    private AdLayout amazonAdView;

    private AdLayout getAmazonAdView(Context context, AdSize adSize) {
        if (amazonAdView == null) {
        	com.amazon.device.ads.AdSize amazonAdSize = getAmazonAdSize(context, adSize);
            amazonAdView = new AdLayout(context, amazonAdSize);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                    							LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            amazonAdView.setLayoutParams(layoutParams);
        }

        return amazonAdView;
    }

    private MediationBannerListener mediationBannerListener;

    private class AmazonBannerListener implements AdListener {
        @Override
        public void onAdLoaded(Ad ad, AdProperties adProperties) {
            mediationBannerListener.onAdLoaded(AmazonMediationBannerAdapter.this);
        }

        @Override
        public void onAdFailedToLoad(Ad ad, AdError adError) {
            Log.e(AmazonMediationBannerAdapter.this.getClass().getSimpleName(), adError.getMessage());
            mediationBannerListener.onAdFailedToLoad(AmazonMediationBannerAdapter.this, adError.getCode().ordinal());
        }

        @Override
        public void onAdExpanded(Ad ad) {
            mediationBannerListener.onAdClicked(AmazonMediationBannerAdapter.this);
            mediationBannerListener.onAdOpened(AmazonMediationBannerAdapter.this);
            mediationBannerListener.onAdLeftApplication(AmazonMediationBannerAdapter.this);
        }

        @Override
        public void onAdCollapsed(Ad ad) {
            mediationBannerListener.onAdClosed(AmazonMediationBannerAdapter.this);
        }

        @Override
        public void onAdDismissed(Ad ad) {
            mediationBannerListener.onAdClosed(AmazonMediationBannerAdapter.this);
        }
    }
}
