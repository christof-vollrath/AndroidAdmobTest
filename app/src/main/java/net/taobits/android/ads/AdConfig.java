package net.taobits.android.ads;

class AdConfig {

    enum MediationType{
        ADMOB_WITH_MEDIATION,
        FACEBOOK_AUDIENCE_NETWORK_ONLY, // Facebook AudienceNetwork will be shown through mediation, use only for testing directly
        AMAZON_MOBILE_ADS_ONLY // Amazon Ads will be shown through mediation, use only for testing
    }

    final static MediationType MEDIATION_TYPE = MediationType.ADMOB_WITH_MEDIATION;

    final static boolean ADMOB_TEST_ENABLED = false; // Don't enable admob testing if you want to use mediation
    final static String[] ADMOB_TEST_DEVICES = {
            // Add your test devices here (admob device id)
    };
    final static boolean FACEBOOK_TEST_ENABLED = true;
    final static String[] FACEBOOK_TEST_DEVICES = {
            // Add your test devices here (facebook device id)
    };
    final static boolean AMAZON_TEST_ENABLED = true;

    final static String ADMOB_ADUNIT_ID = "Your admob ad unit";
    final static String FACEBOOK_PLACEMENT_ID = "Your facebook placement id";
    static final String AMAZON_APP_KEY = "Your amazon app key"; // Application Key
}
