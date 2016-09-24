package net.taobits.android.ads;

import com.amazon.device.ads.AdLayout;
import com.amazon.device.ads.AdRegistration;
import com.amazon.device.ads.AdTargetingOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import android.app.Activity;
import android.util.Log;
import android.widget.LinearLayout;

public class AdDisplay {
	public AdDisplay(Activity activity, LinearLayout linearLayout, int adHeightPixel, int adWithPixel) {
		this.activity = activity;
		adHeight = adHeightPixel;
		adWidth = adWithPixel;
		adLayout = linearLayout;

        initAdmobAds();
        initAmazonAds();

        switch(AdConfig.MEDIATION_TYPE) {
            case ADMOB_WITH_MEDIATION:
                addAdMobAd(adHeight, adWidth);
                break;
            case FACEBOOK_AUDIENCE_NETWORK_ONLY:
        		addFacebookAd(adHeight, adWidth);
                break;
            case AMAZON_MOBILE_ADS_ONLY:
        		addAmazonMobileAd(adHeight, adWidth);
                break;
        }
	}

    private void initAdmobAds() {
        try {
            MobileAds.initialize(activity.getApplicationContext(), AdConfig.ADMOB_ADUNIT_ID);
        } catch (Exception e) {
            Log.e("AdDisplay", "Admob MobileAds.initialize throws: " + e);
        }
    }

    private void initAmazonAds() {
        try {
            AdRegistration.setAppKey(AdConfig.AMAZON_APP_KEY);
        } catch (Exception e) {
            Log.e("AdDisplay", "Amazon AdRegistration.setAppKey throws: " + e);
        }
    }

    private void addAdMobAd(int adHeight, int adWidth){
		if (adLayout == null) return;
		AdSize adSize = getAdmobAdSize(adHeight, adWidth);
		admobAdView = new AdView(activity);
		admobAdView.setAdUnitId(AdConfig.ADMOB_ADUNIT_ID);
		admobAdView.setAdSize(adSize);
		adLayout.addView(admobAdView);
    	Builder adRequestBuilder = new Builder();
        if (AdConfig.ADMOB_TEST_ENABLED) {
            for (String device: AdConfig.ADMOB_TEST_DEVICES) {
                adRequestBuilder.addTestDevice(device);
            }
        }
    	AdRequest adRequest = adRequestBuilder.build();
    	admobAdView.loadAd(adRequest);
    }
    
    private void addFacebookAd(int adHeight, int adWidth) {
//		if (adLayout == null) return; No facebook because no money
//        if (AdConfig.FACEBOOK_TEST_ENABLED) {
//            for (String device: AdConfig.FACEBOOK_TEST_DEVICES) {
//                com.facebook.ads.AdSettings.addTestDevice(device);
//            }
//        }
//    	com.facebook.ads.AdSize adSize = getFacebookAdSize(adHeight, adWidth);
//    	facebookAdView = new com.facebook.ads.AdView(activity, AdConfig.FACEBOOK_PLACEMENT_ID, adSize);
//		adLayout.addView(facebookAdView);
//    	facebookAdView.loadAd();
    }

    private void addAmazonMobileAd(int adHeight, int adWidth){
		if (adLayout == null) return;
        if (AdConfig.AMAZON_TEST_ENABLED) {
            AdRegistration.enableLogging(true); // For debugging purposes enable logging, but disable for production builds.
            AdRegistration.enableTesting(true);// For debugging purposes flag all ad requests as tests, but set to false for production builds.
        }

    	com.amazon.device.ads.AdSize adSize = getAmazonAdSize(adHeight, adWidth);
        try {
            AdRegistration.setAppKey(AdConfig.AMAZON_APP_KEY);
        } catch (Exception e) {
            Log.e("AdDisplay", "AdRegistration.setAppKey throws: " + e);
            return;
        }
		amazonAdView = new AdLayout(activity, adSize);
        adLayout.addView(amazonAdView);
    	com.amazon.device.ads.AdTargetingOptions amazonAdOptions = new AdTargetingOptions();
    	amazonAdView.loadAd(amazonAdOptions);
    }
    
    /**
     *     320x50 	Standard banner       Phones and Tablets  BANNER
     *     468x60 	IAB Full-Size Banner  Tablets             FULL_BANNER
     *     728x90 	IAB Leaderboard       Tablets             LEADERBOARD
     */
    private AdSize getAdmobAdSize(int adHeight, int adWidth) {
    	AdSize result = AdSize.BANNER;
    	if (adWidth >= 728 && adHeight >= 90) result = AdSize.LEADERBOARD;
    	else if (adWidth >= 468 && adHeight >= 60) result = AdSize.FULL_BANNER;
    	return result;
    }
    /**
     *     Height 50 Phone banner	BANNER_HEIGHT_50
     *     Height 50 Tablet banner	BANNER_HEIGHT_90
     */
//    private com.facebook.ads.AdSize getFacebookAdSize(int adHeight, int adWidth) { No facebook because no money
//    	com.facebook.ads.AdSize result = com.facebook.ads.AdSize.BANNER_HEIGHT_50;
//    	if (adWidth >= 728 && adHeight >= 90) result = com.facebook.ads.AdSize.BANNER_HEIGHT_90;
//    	else if (adWidth >= 468 && adHeight >= 60) result = com.facebook.ads.AdSize.BANNER_HEIGHT_50; // No full banner for facebook
//    	return result;
//    }

    /**
     * Amazon Ad sizes
     */
    private com.amazon.device.ads.AdSize getAmazonAdSize(int adHeight, int adWidth) {
    	com.amazon.device.ads.AdSize result = com.amazon.device.ads.AdSize.SIZE_320x50;
    	if (adWidth >= 728 && adHeight >= 90) result = com.amazon.device.ads.AdSize.SIZE_728x90;
    	else if (adWidth >= 600 && adHeight >= 90) result = com.amazon.device.ads.AdSize.SIZE_600x90;
    	return result;
    }
	
	public void destroy() {
		//adView.removeAllViews(); // When it is not removed, the error "WebView.destroy() called while still attached!" will be logged by AdMob
		if (admobAdView != null) { admobAdView.destroy(); admobAdView = null; }
//		if (facebookAdView != null) { facebookAdView.destroy(); facebookAdView = null; } No facebook because no money
		if (amazonAdView != null) { amazonAdView.destroy(); amazonAdView = null; }
			
	}

	public void pause() {
		if (admobAdView != null) admobAdView.pause();
	}

	public void resume() {
		if (admobAdView != null) admobAdView.resume();
	}
	
	private Activity activity;
	private AdView admobAdView;
//	private com.facebook.ads.AdView facebookAdView; No facebook because no money
	private com.amazon.device.ads.AdLayout amazonAdView;
	
	
	private int adHeight;
	private int adWidth;
	private LinearLayout adLayout;

}
