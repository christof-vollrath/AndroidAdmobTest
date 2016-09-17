package net.taobits.android;

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

//		addAdMobAd(adHeight, adWidth);
//		addFacebookAd(adHeight, adWidth); // Facebook AudienceNetwork will be shown through mediation, uncomment only for testing directly
		addAmazonMobileAd(adHeight, adWidth); // Amazon Ads will be shown through mediation, uncomment only for testing
	}

    private void initAdmobAds() {
        MobileAds.initialize(activity.getApplicationContext(), ADMOB_ADUNIT_ID);
    }

    private void addAdMobAd(int adHeight, int adWidth){
		if (adLayout == null) return;
		AdSize adSize = getAdmobAdSize(adHeight, adWidth);
		admobAdView = new AdView(activity);
		admobAdView.setAdUnitId(ADMOB_ADUNIT_ID);
		admobAdView.setAdSize(adSize);
		adLayout.addView(admobAdView);
    	Builder adRequestBuilder = new Builder();
    	adRequestBuilder.addTestDevice("B0BEAA292BC8F9DA9328AC194C8FFBED");
    	AdRequest adRequest = adRequestBuilder.build();
    	admobAdView.loadAd(adRequest);
    }
    
    private void addFacebookAd(int adHeight, int adWidth){
		if (adLayout == null) return;
    	com.facebook.ads.AdSettings.addTestDevice("b5ce670bf5247cc3c501fecc610f5a83");
    	com.facebook.ads.AdSize adSize = getFacebookAdSize(adHeight, adWidth);
    	facebookAdView = new com.facebook.ads.AdView(activity, FACEBOOK_PLACEMENT_ID, adSize);
		adLayout.addView(facebookAdView);
    	facebookAdView.loadAd();
    }

    private void addAmazonMobileAd(int adHeight, int adWidth){
		if (adLayout == null) return;
        // For debugging purposes enable logging, but disable for production builds.
        AdRegistration.enableLogging(true);
        // For debugging purposes flag all ad requests as tests, but set to false for production builds.
        //AdRegistration.enableTesting(true);

    	com.amazon.device.ads.AdSize adSize = getAmazonAdSize(adHeight, adWidth);
        try {
            AdRegistration.setAppKey(AMAZON_APP_KEY);
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
    private com.facebook.ads.AdSize getFacebookAdSize(int adHeight, int adWidth) {
    	com.facebook.ads.AdSize result = com.facebook.ads.AdSize.BANNER_HEIGHT_50;
    	if (adWidth >= 728 && adHeight >= 90) result = com.facebook.ads.AdSize.BANNER_HEIGHT_90;
    	else if (adWidth >= 468 && adHeight >= 60) result = com.facebook.ads.AdSize.BANNER_HEIGHT_50; // No full banner for facebook
    	return result;
    }

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
		if (facebookAdView != null) { facebookAdView.destroy(); facebookAdView = null; }
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
	private com.facebook.ads.AdView facebookAdView;
	private com.amazon.device.ads.AdLayout amazonAdView;
	
	
	private int adHeight;
	private int adWidth;
	private LinearLayout adLayout;

	public final static String ADMOB_ADUNIT_ID = "ca-app-pub-8195484043291241/5976474214"; 
	public final static String FACEBOOK_PLACEMENT_ID = "575329099236386_595075407261755"; 
	public static final String AMAZON_APP_KEY = "41325255515048484f56374355323357"; // Application Key
}
