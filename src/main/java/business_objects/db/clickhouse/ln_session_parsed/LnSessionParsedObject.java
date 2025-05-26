package business_objects.db.clickhouse.ln_session_parsed;

import java.util.Arrays;
import java.util.Objects;

public class LnSessionParsedObject {

    private String ucid;
    private Integer id;
    private String brand;
    private String sessionId;
    private Integer userId;
    private String email;
    private Integer mobileCode;
    private String mobile;
    private String eventType;
    private Boolean isFromApp;
    private String createTime;
    private Integer policyScore;
    private String riskRating;
    private String reviewStatus;
    private String requestResult;
    private String accountAddress;
    private String accountAddressActivities;
    private String[] accountAddressAttributes;
    private String accountAddressCity;
    private String accountAddressCountry;
    private String accountAddressFirstSeen;
    private String accountAddressResult;
    private Integer accountAddressScore;
    private String accountAddressState;
    private String accountAddressStreet1;
    private Integer accountAddressWorstScore;
    private String accountAddressZip;
    private String accountDateOfBirth;
    private String accountEmail;
    private String accountEmailActivities;
    private String[] accountEmailAttributes;
    private String accountEmailDomain;
    private String accountEmailFirstSeen;
    private String accountEmailResult;
    private Integer accountEmailScore;
    private Integer accountEmailWorstScore;
    private String accountFirstName;
    private String accountGender;
    private String accountLastName;
    private String accountLexIdActivities;
    private String[] accountLexIdAttributes;
    private String accountLexIdFirstSeen;
    private String accountLexIdNumber;
    private String accountLexIdResult;
    private Integer accountLexIdScore;
    private Integer accountLexIdWorstScore;
    private String accountLogin;
    private String accountLoginActivities;
    private String[] accountLoginAttributes;
    private String accountLoginFirstSeen;
    private String accountLoginResult;
    private Integer accountLoginScore;
    private Integer accountLoginWorstScore;
    private String accountName;
    private String accountNameActivities;
    private String[] accountNameAttributes;
    private String accountNameFirstSeen;
    private String accountNameResult;
    private Integer accountNameScore;
    private Integer accountNameWorstScore;
    private String accountTelephone;
    private String accountTelephoneActivities;
    private String[] accountTelephoneAttributes;
    private Integer accountTelephoneCountryCode;
    private String accountTelephoneFirstSeen;
    private String accountTelephoneGeo;
    private String accountTelephoneIsPossible;
    private String accountTelephoneIsValid;
    private String accountTelephoneResult;
    private Integer accountTelephoneScore;
    private String accountTelephoneType;
    private Integer accountTelephoneWorstScore;
    private String agentBssidActivities;
    private String[] agentBssidAttributes;
    private String agentBssidFirstSeen;
    private String agentBssidResult;
    private Integer agentBssidScore;
    private Integer agentBssidWorstScore;
    private String agentSsidClear;
    private Integer appIntegrityScore;
    private String audioContext;
    private Double batteryStatusLevel;
    private String batteryStatus;
    private String bbAnomalyRating;
    private String[] bbAnomalyReasonCode;
    private Integer bbAnomalyScore;
    private Double bbAssessment;
    private String bbAssessmentRating;
    private Double bbAuthConfidenceScore;
    private Double bbAuthHistoricalScoreMean;
    private Double bbAuthHistoricalScoreStd;
    private Double bbAuthScore;
    private String bbBotRating;
    private Double bbBotScore;
    private String bbFraudRating;
    private Double bbFraudScore;
    private String[] behaviosecBotReasons;
    private Double behaviosecConfidence;
    private String[] behaviosecDataIntegrityReasons;
    private Double behaviosecPopulationProfileChallengerRiskRank;
    private Double behaviosecPopulationProfileChallengerScore;
    private Double behaviosecPopulationProfileRiskRank;
    private Double behaviosecPopulationProfileScore;
    private Double behaviosecScore;
    private String behaviosecUserid;
    private String browser;
    private String browserVersion;
    private String browserAddon;
    private String browserAddonHash;
    private String browserAnomaly;
    private String browserLanguage;
    private String browserSpoofRating;
    private String browserStringHash;
    private String browserString;
    private int pluginNumber;
    private String profiledUrl;
    private String canvasHash;
    private Integer cidrNumber;
    private String deviceActivities;
    private String[] deviceAttributes;
    private String deviceFingerprint;
    private String deviceFingerprintActivities;
    private String[] deviceFingerprintAttributes;
    private String deviceFingerprintFirstSeen;
    private String deviceFingerprintResult;
    private Integer deviceFingerprintScore;
    private Integer deviceFingerprintWorstScore;
    private String deviceFirstSeen;
    private String[] deviceHealthReasons;
    private String deviceId;
    private Double deviceIdConfidence;
    private Integer deviceMemory;
    private String deviceModel;
    private String deviceName;
    private String agentLanguage;
    private String deviceResult;
    private Integer deviceScore;
    private Integer deviceWorstScore;
    private String digitalId;
    private String digitalIdActivities;
    private String[] digitalIdAttributes;
    private Integer digitalIdConfidence;
    private String digitalIdConfidenceRating;
    private String digitalIdFirstSeen;
    private String digitalIdReasonCode;
    private String digitalIdResult;
    private Double digitalIdTrustScore;
    private String digitalIdTrustScoreRating;
    private String[] digitalIdTrustScoreReasonCode;
    private String[] digitalIdTrustScoreSummaryReasonCode;
    private String dnsIp;
    private String[] dnsIpAttributes;
    private String dnsIpCity;
    private String dnsIpGeo;
    private String dnsIpIsp;
    private String dnsIpOrganization;
    private String dnsIpPostalCode;
    private String dnsIpRegion;
    private String emailageEmailriskscoreEaadvice;
    private String emailageEmailriskscoreEareason;
    private Integer emailageEmailriskscoreEascore;
    private Integer emailageEmailriskscoreEariskbandid;
    private Integer emailageEmailriskscoreEmailCreationDays;
    private String emailageEmailriskscoreEmailage;
    private String emailageEmailriskscorePhonecarriertype;
    private String emailageEmailriskscorePhoneownermatch;
    private Integer emailageEmailriskscorePhonetofullnameconfidence;
    private Integer emailageEmailriskscorePhonetolastnameconfidence;
    private String emailageEmailriskscoreIpRisklevel;
    private String emailageEmailriskscoreIpRiskreason;
    private String emailageEmailriskscore;
    private String emailageEmailriskscoreDomainrisklevel;
    private Integer emailageEmailriskscoreDomainCreationDays;
    private String emailageEmailriskscoreDomainage;
    private String emailageEmailriskscoreDomainexists;
    private String emailageEmailriskscoreDomaincountry;
    private String emailageEmailriskscoreDomaincompany;
    private String emailageEmailriskscoreDomainname;
    private String emailageEmailriskscoreDomaincategory;
    private Integer emailageEmailriskscoreOveralldigitalidentityscore;
    private String emailageEmailriskscoreDisdescription;
    private String eventDatetime;
    private Integer eventId;
    private String finalReviewStatus;
    private String[] fuzzyDeviceAttributes;
    private String fuzzyDeviceFirstSeen;
    private String fuzzyDeviceId;
    private Double fuzzyDeviceIdConfidence;
    private String fuzzyDeviceMatchResult;
    private String fuzzyDeviceResult;
    private Integer fuzzyDeviceScore;
    private Integer fuzzyDeviceWorstScore;
    private String geofenceCountryGps;
    private String geofenceRegionGps;
    private String gpsSpoof;
    private String gpuName;
    private String gpuVendor;
    private String hwFingerprint;
    private Integer jbRoot;
    private String jsBrowser;
    private String jsBrowserStringHash;
    private String jsFontsHash;
    private Integer jsFontsNumber;
    private String jsOs;
    private String liveness;
    private String macAddress;
    private String mathRoutine;
    private String mimeTypeHash;
    private String multiDisplay;
    private String nationalId;
    private String passwordHash;
    private String passwordHashActivities;
    private String[] passwordHashAttributes;
    private String passwordHashFirstSeen;
    private String passwordHashResult;
    private Integer passwordHashScore;
    private Integer passwordHashWorstScore;
    private String policy;
    private String privateBrowsing;
    private String proxyIp;
    private String proxyIpActivities;
    private String[] proxyIpAttributes;
    private String proxyIpCity;
    private String proxyIpConnectionType;
    private String proxyIpFirstSeen;
    private String proxyIpGeo;
    private String proxyIpHome;
    private String proxyIpIsp;
    private Double proxyIpLatitude;
    private Double proxyIpLongitude;
    private String proxyIpOrganization;
    private String proxyIpOrganizationType;
    private String proxyIpPostalCode;
    private String proxyIpRegion;
    private String proxyIpResult;
    private String proxyIpRoutingType;
    private Integer proxyIpScore;
    private Integer proxyIpWorstScore;
    private String proxyIpv6;
    private String proxyName;
    private Double proxyScore;
    private String proxyType;
    private String relatedRequestId;
    private String remoteAccess;
    private String remoteAccessRating;
    private Integer remoteAccessScore;
    private String remoteDesktop;
    private String screenRes;
    private String screenResAnomaly;
    private Double screenResZoom;
    private String seRating;
    private Double seScore;
    private String smartLearningFraudRating;
    private Double smartLearningPScore;
    private Integer smartLearningPolicyScore;
    private String smartLearningReasonCode;
    private Double smartLearningRiskRank;
    private String smartLearningSummaryReasonCode;
    private Integer timeZone;
    private Integer timeZoneDstOffset;
    private String timezoneName;
    private String timezoneOffsetAnomaly;
    private Integer tmxPolicyScore;
    private String tmxRiskRating;
    private String tmxVariables;
    private String trueIp;
    private String trueIpActivities;
    private String[] trueIpAttributes;
    private String trueIpCity;
    private Integer trueIpCountryConfidence;
    private String trueIpFirstSeen;
    private String trueIpGeo;
    private String trueIpIsp;
    private String trueIpLastEvent;
    private String trueIpOrganization;
    private String trueIpOrganizationType;
    private String trueIpPostalCode;
    private String trueIpRegion;
    private String trueIpResult;
    private String trueIpConnectionType;
    private String trueIpRoutingType;
    private Integer trueIpScore;
    private Integer trueIpWorstScore;
    private String trueIpv6;
    private String uaAgent;
    private String uaMobile;
    private String unknownSession;
    private Integer virtualDevice;
    private String virtualDeviceReason;
    private String vpnReason;
    private Integer vpnScore;
    private String webglHash;
    private String webrtcExternalIp;
    private String customerEventType;
    private String inputIpAddress;
    private String inputIpIsp;
    private String inputIpCity;
    private String inputIpRegion;
    private String inputIpGeo;
    private String inputIpRoutingType;
    private String webSessionId;
    private String conditionAttrib5;
    private String agentBrand;
    private String agentModel;
    private String applicationName;
    private String transactionId;
    private String lineOfBusiness;
    private String accountAddressStreet2;
    private String conditionAttrib1;
    private String conditionAttrib2;
    private Double customCount1;
    private String localAttrib1;
    private String customCount4;
    private String conditionAttrib6;
    private String conditionAttrib3;
    private Double primaryAccountBalanceUsd;
    private Double primaryAccountBalance;
    private String primaryAccountBalanceCurrency;
    private String conditionAttrib10;
    private Double conditionAttrib9;
    private String conditionAttrib4;
    private String conditionAttrib7;
    private String conditionAttrib8;
    private String localAttrib12;
    private String localAttrib13;
    private String localAttrib14;
    private String localAttrib15;
    private String customCount2;
    private String customCount13;
    private String summaryReasonCode;
    private String os;
    private String osVersion;
    private String tmxSummaryReasonCode;
    private String lastUpdated;

    public LnSessionParsedObject() {
    }

    public LnSessionParsedObject(String ucid, Integer id, String brand, String sessionId, Integer userId, String email,
            Integer mobileCode, String mobile, String eventType, Boolean isFromApp, String createTime,
            Integer policyScore, String riskRating, String deviceId, String digitalId, String eventDatetime,
            Integer eventId, String proxyIp, String proxyIpActivities, String[] proxyIpAttributes, String proxyIpCity,
            String proxyIpConnectionType, String proxyIpFirstSeen, String proxyIpGeo, String proxyIpHome,
            String proxyIpIsp, Double proxyIpLatitude, Double proxyIpLongitude, String proxyIpOrganization,
            String proxyIpOrganizationType, String proxyIpPostalCode, String proxyIpRegion, String proxyIpResult,
            String proxyIpRoutingType, Integer proxyIpScore, Integer proxyIpWorstScore, String proxyIpv6,
            String proxyName, Double proxyScore, String proxyType, String trueIp, String trueIpActivities,
            String[] trueIpAttributes, String trueIpCity, Integer trueIpCountryConfidence, String trueIpFirstSeen,
            String trueIpGeo, String trueIpIsp, String trueIpLastEvent, String trueIpOrganization,
            String trueIpOrganizationType, String trueIpPostalCode, String trueIpRegion, String trueIpResult,
            String trueIpRoutingType, Integer trueIpScore, Integer trueIpWorstScore, String trueIpv6,
            Integer vpnScore, String summaryReasonCode, String os, String tmxSummaryReasonCode) {
        this.ucid = ucid;
        this.id = id;
        this.brand = brand;
        this.sessionId = sessionId;
        this.userId = userId;
        this.email = email;
        this.mobileCode = mobileCode;
        this.mobile = mobile;
        this.eventType = eventType;
        this.isFromApp = isFromApp;
        this.createTime = createTime;
        this.policyScore = policyScore;
        this.riskRating = riskRating;
        this.deviceId = deviceId;
        this.digitalId = digitalId;
        this.eventDatetime = eventDatetime;
        this.eventId = eventId;
        this.proxyIp = proxyIp;
        this.proxyIpActivities = proxyIpActivities;
        this.proxyIpAttributes = proxyIpAttributes;
        this.proxyIpCity = proxyIpCity;
        this.proxyIpConnectionType = proxyIpConnectionType;
        this.proxyIpFirstSeen = proxyIpFirstSeen;
        this.proxyIpGeo = proxyIpGeo;
        this.proxyIpHome = proxyIpHome;
        this.proxyIpIsp = proxyIpIsp;
        this.proxyIpLatitude = proxyIpLatitude;
        this.proxyIpLongitude = proxyIpLongitude;
        this.proxyIpOrganization = proxyIpOrganization;
        this.proxyIpOrganizationType = proxyIpOrganizationType;
        this.proxyIpPostalCode = proxyIpPostalCode;
        this.proxyIpRegion = proxyIpRegion;
        this.proxyIpResult = proxyIpResult;
        this.proxyIpRoutingType = proxyIpRoutingType;
        this.proxyIpScore = proxyIpScore;
        this.proxyIpWorstScore = proxyIpWorstScore;
        this.proxyIpv6 = proxyIpv6;
        this.proxyName = proxyName;
        this.proxyScore = proxyScore;
        this.proxyType = proxyType;
        this.trueIp = trueIp;
        this.trueIpActivities = trueIpActivities;
        this.trueIpAttributes = trueIpAttributes;
        this.trueIpCity = trueIpCity;
        this.trueIpCountryConfidence = trueIpCountryConfidence;
        this.trueIpFirstSeen = trueIpFirstSeen;
        this.trueIpGeo = trueIpGeo;
        this.trueIpIsp = trueIpIsp;
        this.trueIpLastEvent = trueIpLastEvent;
        this.trueIpOrganization = trueIpOrganization;
        this.trueIpOrganizationType = trueIpOrganizationType;
        this.trueIpPostalCode = trueIpPostalCode;
        this.trueIpRegion = trueIpRegion;
        this.trueIpResult = trueIpResult;
        this.trueIpRoutingType = trueIpRoutingType;
        this.trueIpScore = trueIpScore;
        this.trueIpWorstScore = trueIpWorstScore;
        this.trueIpv6 = trueIpv6;
        this.vpnScore = vpnScore;
        this.summaryReasonCode = summaryReasonCode;
        this.os = os;
        this.tmxSummaryReasonCode = tmxSummaryReasonCode;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LnSessionParsedObject that = (LnSessionParsedObject) o;
        return pluginNumber == that.pluginNumber && Objects.equals(ucid, that.ucid) && Objects.equals(
                id, that.id) && Objects.equals(brand, that.brand) && Objects.equals(sessionId, that.sessionId) && Objects.equals(
                        userId, that.userId) && Objects.equals(email, that.email) && Objects.equals(mobileCode, that.mobileCode) && Objects.equals(
                                mobile, that.mobile) && Objects.equals(eventType, that.eventType) && Objects.equals(
                                        isFromApp, that.isFromApp) && Objects.equals(createTime, that.createTime) && Objects.equals(
                                                policyScore, that.policyScore) && Objects.equals(riskRating, that.riskRating) && Objects.equals(
                                                        reviewStatus, that.reviewStatus) && Objects.equals(requestResult, that.requestResult) && Objects.equals(
                                                                accountAddress, that.accountAddress) && Objects.equals(accountAddressActivities, that.accountAddressActivities) && Objects.deepEquals(
                                                                        accountAddressAttributes, that.accountAddressAttributes) && Objects.equals(accountAddressCity, that.accountAddressCity) && Objects.equals(
                                                                                accountAddressCountry, that.accountAddressCountry) && Objects.equals(accountAddressFirstSeen, that.accountAddressFirstSeen) && Objects.equals(
                                                                                        accountAddressResult, that.accountAddressResult) && Objects.equals(accountAddressScore, that.accountAddressScore) && Objects.equals(
                                                                                                accountAddressState, that.accountAddressState) && Objects.equals(accountAddressStreet1, that.accountAddressStreet1) && Objects.equals(
                                                                                                        accountAddressWorstScore, that.accountAddressWorstScore) && Objects.equals(accountAddressZip, that.accountAddressZip) && Objects.equals(
                                                                                                                accountDateOfBirth, that.accountDateOfBirth) && Objects.equals(accountEmail, that.accountEmail) && Objects.equals(
                                                                                                                        accountEmailActivities, that.accountEmailActivities) && Objects.deepEquals(accountEmailAttributes, that.accountEmailAttributes) && Objects.equals(
                                                                                                                                accountEmailDomain, that.accountEmailDomain) && Objects.equals(accountEmailFirstSeen, that.accountEmailFirstSeen) && Objects.equals(
                                                                                                                                        accountEmailResult, that.accountEmailResult) && Objects.equals(accountEmailScore, that.accountEmailScore) && Objects.equals(
                                                                                                                                                accountEmailWorstScore, that.accountEmailWorstScore) && Objects.equals(accountFirstName, that.accountFirstName) && Objects.equals(
                                                                                                                                                        accountGender, that.accountGender) && Objects.equals(accountLastName, that.accountLastName) && Objects.equals(
                                                                                                                                                                accountLexIdActivities, that.accountLexIdActivities) && Objects.deepEquals(accountLexIdAttributes, that.accountLexIdAttributes) && Objects.equals(
                                                                                                                                                                        accountLexIdFirstSeen, that.accountLexIdFirstSeen) && Objects.equals(accountLexIdNumber, that.accountLexIdNumber) && Objects.equals(
                                                                                                                                                                                accountLexIdResult, that.accountLexIdResult) && Objects.equals(accountLexIdScore, that.accountLexIdScore) && Objects.equals(
                                                                                                                                                                                        accountLexIdWorstScore, that.accountLexIdWorstScore) && Objects.equals(accountLogin, that.accountLogin) && Objects.equals(
                                                                                                                                                                                                accountLoginActivities, that.accountLoginActivities) && Objects.deepEquals(accountLoginAttributes, that.accountLoginAttributes) && Objects.equals(
                                                                                                                                                                                                        accountLoginFirstSeen, that.accountLoginFirstSeen) && Objects.equals(accountLoginResult, that.accountLoginResult) && Objects.equals(
                                                                                                                                                                                                                accountLoginScore, that.accountLoginScore) && Objects.equals(accountLoginWorstScore, that.accountLoginWorstScore) && Objects.equals(
                                                                                                                                                                                                                        accountName, that.accountName) && Objects.equals(accountNameActivities, that.accountNameActivities) && Objects.deepEquals(
                                                                                                                                                                                                                                accountNameAttributes, that.accountNameAttributes) && Objects.equals(accountNameFirstSeen, that.accountNameFirstSeen) && Objects.equals(
                                                                                                                                                                                                                                        accountNameResult, that.accountNameResult) && Objects.equals(accountNameScore, that.accountNameScore) && Objects.equals(
                                                                                                                                                                                                                                                accountNameWorstScore, that.accountNameWorstScore) && Objects.equals(accountTelephone, that.accountTelephone) && Objects.equals(
                                                                                                                                                                                                                                                        accountTelephoneActivities, that.accountTelephoneActivities) && Objects.deepEquals(
                                                                                                                                                                                                                                                                accountTelephoneAttributes, that.accountTelephoneAttributes) && Objects.equals(
                                                                                                                                                                                                                                                                        accountTelephoneCountryCode, that.accountTelephoneCountryCode) && Objects.equals(
                                                                                                                                                                                                                                                                                accountTelephoneFirstSeen, that.accountTelephoneFirstSeen) && Objects.equals(accountTelephoneGeo, that.accountTelephoneGeo) && Objects.equals(
                                                                                                                                                                                                                                                                                        accountTelephoneIsPossible, that.accountTelephoneIsPossible) && Objects.equals(
                                                                                                                                                                                                                                                                                                accountTelephoneIsValid, that.accountTelephoneIsValid) && Objects.equals(accountTelephoneResult, that.accountTelephoneResult) && Objects.equals(
                                                                                                                                                                                                                                                                                                        accountTelephoneScore, that.accountTelephoneScore) && Objects.equals(accountTelephoneType, that.accountTelephoneType) && Objects.equals(
                                                                                                                                                                                                                                                                                                                accountTelephoneWorstScore, that.accountTelephoneWorstScore) && Objects.equals(
                                                                                                                                                                                                                                                                                                                        agentBssidActivities, that.agentBssidActivities) && Objects.deepEquals(agentBssidAttributes, that.agentBssidAttributes) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                agentBssidFirstSeen, that.agentBssidFirstSeen) && Objects.equals(agentBssidResult, that.agentBssidResult) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                        agentBssidScore, that.agentBssidScore) && Objects.equals(agentBssidWorstScore, that.agentBssidWorstScore) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                agentSsidClear, that.agentSsidClear) && Objects.equals(appIntegrityScore, that.appIntegrityScore) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                        audioContext, that.audioContext) && Objects.equals(batteryStatusLevel, that.batteryStatusLevel) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                batteryStatus, that.batteryStatus) && Objects.equals(bbAnomalyRating, that.bbAnomalyRating) && Objects.deepEquals(
                                                                                                                                                                                                                                                                                                                                                                        bbAnomalyReasonCode, that.bbAnomalyReasonCode) && Objects.equals(bbAnomalyScore, that.bbAnomalyScore) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                bbAssessment, that.bbAssessment) && Objects.equals(bbAssessmentRating, that.bbAssessmentRating) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                        bbAuthConfidenceScore, that.bbAuthConfidenceScore) && Objects.equals(bbAuthHistoricalScoreMean, that.bbAuthHistoricalScoreMean) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                bbAuthHistoricalScoreStd, that.bbAuthHistoricalScoreStd) && Objects.equals(bbAuthScore, that.bbAuthScore) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                        bbBotRating, that.bbBotRating) && Objects.equals(bbBotScore, that.bbBotScore) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                bbFraudRating, that.bbFraudRating) && Objects.equals(bbFraudScore, that.bbFraudScore) && Objects.deepEquals(
                                                                                                                                                                                                                                                                                                                                                                                                                        behaviosecBotReasons, that.behaviosecBotReasons) && Objects.equals(behaviosecConfidence, that.behaviosecConfidence) && Objects.deepEquals(
                                                                                                                                                                                                                                                                                                                                                                                                                                behaviosecDataIntegrityReasons, that.behaviosecDataIntegrityReasons) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                        behaviosecPopulationProfileChallengerRiskRank, that.behaviosecPopulationProfileChallengerRiskRank) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                behaviosecPopulationProfileChallengerScore, that.behaviosecPopulationProfileChallengerScore) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                        behaviosecPopulationProfileRiskRank, that.behaviosecPopulationProfileRiskRank) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                behaviosecPopulationProfileScore, that.behaviosecPopulationProfileScore) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        behaviosecScore, that.behaviosecScore) && Objects.equals(behaviosecUserid, that.behaviosecUserid) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                browser, that.browser) && Objects.equals(browserVersion, that.browserVersion) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        browserAddon, that.browserAddon) && Objects.equals(browserAddonHash, that.browserAddonHash) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                browserAnomaly, that.browserAnomaly) && Objects.equals(browserLanguage, that.browserLanguage) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        browserSpoofRating, that.browserSpoofRating) && Objects.equals(browserStringHash, that.browserStringHash) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                browserString, that.browserString) && Objects.equals(profiledUrl, that.profiledUrl) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        canvasHash, that.canvasHash) && Objects.equals(cidrNumber, that.cidrNumber) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                deviceActivities, that.deviceActivities) && Objects.deepEquals(deviceAttributes, that.deviceAttributes) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        deviceFingerprint, that.deviceFingerprint) && Objects.equals(deviceFingerprintActivities, that.deviceFingerprintActivities) && Objects.deepEquals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                deviceFingerprintAttributes, that.deviceFingerprintAttributes) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        deviceFingerprintFirstSeen, that.deviceFingerprintFirstSeen) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                deviceFingerprintResult, that.deviceFingerprintResult) && Objects.equals(deviceFingerprintScore, that.deviceFingerprintScore) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        deviceFingerprintWorstScore, that.deviceFingerprintWorstScore) && Objects.equals(deviceFirstSeen, that.deviceFirstSeen) && Objects.deepEquals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                deviceHealthReasons, that.deviceHealthReasons) && Objects.equals(deviceId, that.deviceId) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        deviceIdConfidence, that.deviceIdConfidence) && Objects.equals(deviceMemory, that.deviceMemory) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                deviceModel, that.deviceModel) && Objects.equals(deviceName, that.deviceName) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        agentLanguage, that.agentLanguage) && Objects.equals(deviceResult, that.deviceResult) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                deviceScore, that.deviceScore) && Objects.equals(deviceWorstScore, that.deviceWorstScore) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        digitalId, that.digitalId) && Objects.equals(digitalIdActivities, that.digitalIdActivities) && Objects.deepEquals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                digitalIdAttributes, that.digitalIdAttributes) && Objects.equals(digitalIdConfidence, that.digitalIdConfidence) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        digitalIdConfidenceRating, that.digitalIdConfidenceRating) && Objects.equals(digitalIdFirstSeen, that.digitalIdFirstSeen) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                digitalIdReasonCode, that.digitalIdReasonCode) && Objects.equals(digitalIdResult, that.digitalIdResult) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        digitalIdTrustScore, that.digitalIdTrustScore) && Objects.equals(digitalIdTrustScoreRating, that.digitalIdTrustScoreRating) && Objects.deepEquals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                digitalIdTrustScoreReasonCode, that.digitalIdTrustScoreReasonCode) && Objects.deepEquals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        digitalIdTrustScoreSummaryReasonCode, that.digitalIdTrustScoreSummaryReasonCode) && Objects.equals(dnsIp, that.dnsIp) && Objects.deepEquals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                dnsIpAttributes, that.dnsIpAttributes) && Objects.equals(dnsIpCity, that.dnsIpCity) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        dnsIpGeo, that.dnsIpGeo) && Objects.equals(dnsIpIsp, that.dnsIpIsp) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                dnsIpOrganization, that.dnsIpOrganization) && Objects.equals(dnsIpPostalCode, that.dnsIpPostalCode) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        dnsIpRegion, that.dnsIpRegion) && Objects.equals(emailageEmailriskscoreEaadvice, that.emailageEmailriskscoreEaadvice) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                emailageEmailriskscoreEareason, that.emailageEmailriskscoreEareason) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        emailageEmailriskscoreEascore, that.emailageEmailriskscoreEascore) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                emailageEmailriskscoreEariskbandid, that.emailageEmailriskscoreEariskbandid) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        emailageEmailriskscoreEmailCreationDays, that.emailageEmailriskscoreEmailCreationDays) && Objects.equals(emailageEmailriskscoreEmailage, that.emailageEmailriskscoreEmailage) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                emailageEmailriskscorePhonecarriertype, that.emailageEmailriskscorePhonecarriertype) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        emailageEmailriskscorePhoneownermatch, that.emailageEmailriskscorePhoneownermatch) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                emailageEmailriskscorePhonetofullnameconfidence, that.emailageEmailriskscorePhonetofullnameconfidence) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        emailageEmailriskscorePhonetolastnameconfidence, that.emailageEmailriskscorePhonetolastnameconfidence) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                emailageEmailriskscoreIpRisklevel, that.emailageEmailriskscoreIpRisklevel) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        emailageEmailriskscoreIpRiskreason, that.emailageEmailriskscoreIpRiskreason) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                emailageEmailriskscore, that.emailageEmailriskscore) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        emailageEmailriskscoreDomainrisklevel, that.emailageEmailriskscoreDomainrisklevel) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                emailageEmailriskscoreDomainCreationDays, that.emailageEmailriskscoreDomainCreationDays) && Objects.equals(emailageEmailriskscoreDomainage, that.emailageEmailriskscoreDomainage) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        emailageEmailriskscoreDomainexists, that.emailageEmailriskscoreDomainexists) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                emailageEmailriskscoreDomaincountry, that.emailageEmailriskscoreDomaincountry) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        emailageEmailriskscoreDomaincompany, that.emailageEmailriskscoreDomaincompany) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                emailageEmailriskscoreDomainname, that.emailageEmailriskscoreDomainname) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        emailageEmailriskscoreDomaincategory, that.emailageEmailriskscoreDomaincategory) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                emailageEmailriskscoreOveralldigitalidentityscore, that.emailageEmailriskscoreOveralldigitalidentityscore) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        emailageEmailriskscoreDisdescription, that.emailageEmailriskscoreDisdescription) && Objects.equals(eventDatetime, that.eventDatetime) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                eventId, that.eventId) && Objects.equals(finalReviewStatus, that.finalReviewStatus) && Objects.deepEquals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        fuzzyDeviceAttributes, that.fuzzyDeviceAttributes) && Objects.equals(fuzzyDeviceFirstSeen, that.fuzzyDeviceFirstSeen) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                fuzzyDeviceId, that.fuzzyDeviceId) && Objects.equals(fuzzyDeviceIdConfidence, that.fuzzyDeviceIdConfidence) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        fuzzyDeviceMatchResult, that.fuzzyDeviceMatchResult) && Objects.equals(fuzzyDeviceResult, that.fuzzyDeviceResult) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                fuzzyDeviceScore, that.fuzzyDeviceScore) && Objects.equals(fuzzyDeviceWorstScore, that.fuzzyDeviceWorstScore) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        geofenceCountryGps, that.geofenceCountryGps) && Objects.equals(geofenceRegionGps, that.geofenceRegionGps) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                gpsSpoof, that.gpsSpoof) && Objects.equals(gpuName, that.gpuName) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        gpuVendor, that.gpuVendor) && Objects.equals(hwFingerprint, that.hwFingerprint) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                jbRoot, that.jbRoot) && Objects.equals(jsBrowser, that.jsBrowser) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        jsBrowserStringHash, that.jsBrowserStringHash) && Objects.equals(jsFontsHash, that.jsFontsHash) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                jsFontsNumber, that.jsFontsNumber) && Objects.equals(jsOs, that.jsOs) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        liveness, that.liveness) && Objects.equals(macAddress, that.macAddress) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                mathRoutine, that.mathRoutine) && Objects.equals(mimeTypeHash, that.mimeTypeHash) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        multiDisplay, that.multiDisplay) && Objects.equals(nationalId, that.nationalId) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                passwordHash, that.passwordHash) && Objects.equals(passwordHashActivities, that.passwordHashActivities) && Objects.deepEquals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        passwordHashAttributes, that.passwordHashAttributes) && Objects.equals(passwordHashFirstSeen, that.passwordHashFirstSeen) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                passwordHashResult, that.passwordHashResult) && Objects.equals(passwordHashScore, that.passwordHashScore) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        passwordHashWorstScore, that.passwordHashWorstScore) && Objects.equals(policy, that.policy) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                privateBrowsing, that.privateBrowsing) && Objects.equals(proxyIp, that.proxyIp) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        proxyIpActivities, that.proxyIpActivities) && Objects.deepEquals(proxyIpAttributes, that.proxyIpAttributes) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                proxyIpCity, that.proxyIpCity) && Objects.equals(proxyIpConnectionType, that.proxyIpConnectionType) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        proxyIpFirstSeen, that.proxyIpFirstSeen) && Objects.equals(proxyIpGeo, that.proxyIpGeo) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                proxyIpHome, that.proxyIpHome) && Objects.equals(proxyIpIsp, that.proxyIpIsp) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        proxyIpLatitude, that.proxyIpLatitude) && Objects.equals(proxyIpLongitude, that.proxyIpLongitude) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                proxyIpOrganization, that.proxyIpOrganization) && Objects.equals(proxyIpOrganizationType, that.proxyIpOrganizationType) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        proxyIpPostalCode, that.proxyIpPostalCode) && Objects.equals(proxyIpRegion, that.proxyIpRegion) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                proxyIpResult, that.proxyIpResult) && Objects.equals(proxyIpRoutingType, that.proxyIpRoutingType) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        proxyIpScore, that.proxyIpScore) && Objects.equals(proxyIpWorstScore, that.proxyIpWorstScore) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                proxyIpv6, that.proxyIpv6) && Objects.equals(proxyName, that.proxyName) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        proxyScore, that.proxyScore) && Objects.equals(proxyType, that.proxyType) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                relatedRequestId, that.relatedRequestId) && Objects.equals(remoteAccess, that.remoteAccess) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        remoteAccessRating, that.remoteAccessRating) && Objects.equals(remoteAccessScore, that.remoteAccessScore) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                remoteDesktop, that.remoteDesktop) && Objects.equals(screenRes, that.screenRes) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        screenResAnomaly, that.screenResAnomaly) && Objects.equals(screenResZoom, that.screenResZoom) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                seRating, that.seRating) && Objects.equals(seScore, that.seScore) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        smartLearningFraudRating, that.smartLearningFraudRating) && Objects.equals(smartLearningPScore, that.smartLearningPScore) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                smartLearningPolicyScore, that.smartLearningPolicyScore) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        smartLearningReasonCode, that.smartLearningReasonCode) && Objects.equals(smartLearningRiskRank, that.smartLearningRiskRank) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                smartLearningSummaryReasonCode, that.smartLearningSummaryReasonCode) && Objects.equals(timeZone, that.timeZone) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        timeZoneDstOffset, that.timeZoneDstOffset) && Objects.equals(timezoneName, that.timezoneName) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                timezoneOffsetAnomaly, that.timezoneOffsetAnomaly) && Objects.equals(tmxPolicyScore, that.tmxPolicyScore) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        tmxRiskRating, that.tmxRiskRating) && Objects.equals(tmxVariables, that.tmxVariables) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                trueIp, that.trueIp) && Objects.equals(trueIpActivities, that.trueIpActivities) && Objects.deepEquals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        trueIpAttributes, that.trueIpAttributes) && Objects.equals(trueIpCity, that.trueIpCity) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                trueIpCountryConfidence, that.trueIpCountryConfidence) && Objects.equals(trueIpFirstSeen, that.trueIpFirstSeen) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        trueIpGeo, that.trueIpGeo) && Objects.equals(trueIpIsp, that.trueIpIsp) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                trueIpLastEvent, that.trueIpLastEvent) && Objects.equals(trueIpOrganization, that.trueIpOrganization) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        trueIpOrganizationType, that.trueIpOrganizationType) && Objects.equals(trueIpPostalCode, that.trueIpPostalCode) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                trueIpRegion, that.trueIpRegion) && Objects.equals(trueIpResult, that.trueIpResult) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        trueIpConnectionType, that.trueIpConnectionType) && Objects.equals(trueIpRoutingType, that.trueIpRoutingType) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                trueIpScore, that.trueIpScore) && Objects.equals(trueIpWorstScore, that.trueIpWorstScore) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        trueIpv6, that.trueIpv6) && Objects.equals(uaAgent, that.uaAgent) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                uaMobile, that.uaMobile) && Objects.equals(unknownSession, that.unknownSession) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        virtualDevice, that.virtualDevice) && Objects.equals(virtualDeviceReason, that.virtualDeviceReason) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                vpnReason, that.vpnReason) && Objects.equals(vpnScore, that.vpnScore) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        webglHash, that.webglHash) && Objects.equals(webrtcExternalIp, that.webrtcExternalIp) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                customerEventType, that.customerEventType) && Objects.equals(inputIpAddress, that.inputIpAddress) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        inputIpIsp, that.inputIpIsp) && Objects.equals(inputIpCity, that.inputIpCity) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                inputIpRegion, that.inputIpRegion) && Objects.equals(inputIpGeo, that.inputIpGeo) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        inputIpRoutingType, that.inputIpRoutingType) && Objects.equals(webSessionId, that.webSessionId) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                conditionAttrib5, that.conditionAttrib5) && Objects.equals(agentBrand, that.agentBrand) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        agentModel, that.agentModel) && Objects.equals(applicationName, that.applicationName) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                transactionId, that.transactionId) && Objects.equals(lineOfBusiness, that.lineOfBusiness) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        accountAddressStreet2, that.accountAddressStreet2) && Objects.equals(conditionAttrib1, that.conditionAttrib1) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                conditionAttrib2, that.conditionAttrib2) && Objects.equals(customCount1, that.customCount1) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        localAttrib1, that.localAttrib1) && Objects.equals(customCount4, that.customCount4) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                conditionAttrib6, that.conditionAttrib6) && Objects.equals(conditionAttrib3, that.conditionAttrib3) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        primaryAccountBalanceUsd, that.primaryAccountBalanceUsd) && Objects.equals(primaryAccountBalance, that.primaryAccountBalance) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                primaryAccountBalanceCurrency, that.primaryAccountBalanceCurrency) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        conditionAttrib10, that.conditionAttrib10) && Objects.equals(conditionAttrib9, that.conditionAttrib9) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                conditionAttrib4, that.conditionAttrib4) && Objects.equals(conditionAttrib7, that.conditionAttrib7) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        conditionAttrib8, that.conditionAttrib8) && Objects.equals(localAttrib12, that.localAttrib12) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                localAttrib13, that.localAttrib13) && Objects.equals(localAttrib14, that.localAttrib14) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        localAttrib15, that.localAttrib15) && Objects.equals(customCount2, that.customCount2) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                customCount13, that.customCount13) && Objects.equals(summaryReasonCode, that.summaryReasonCode) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        os, that.os) && Objects.equals(osVersion, that.osVersion) && Objects.equals(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                tmxSummaryReasonCode, that.tmxSummaryReasonCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, id, brand, sessionId, userId, email, mobileCode, mobile, eventType, isFromApp, createTime, policyScore, riskRating, reviewStatus, requestResult, accountAddress, accountAddressActivities, Arrays.hashCode(accountAddressAttributes), accountAddressCity, accountAddressCountry, accountAddressFirstSeen, accountAddressResult, accountAddressScore, accountAddressState, accountAddressStreet1, accountAddressWorstScore, accountAddressZip, accountDateOfBirth, accountEmail, accountEmailActivities, Arrays.hashCode(accountEmailAttributes), accountEmailDomain, accountEmailFirstSeen, accountEmailResult, accountEmailScore, accountEmailWorstScore, accountFirstName, accountGender, accountLastName, accountLexIdActivities, Arrays.hashCode(accountLexIdAttributes), accountLexIdFirstSeen, accountLexIdNumber, accountLexIdResult, accountLexIdScore, accountLexIdWorstScore, accountLogin, accountLoginActivities, Arrays.hashCode(accountLoginAttributes), accountLoginFirstSeen, accountLoginResult, accountLoginScore, accountLoginWorstScore, accountName, accountNameActivities, Arrays.hashCode(accountNameAttributes), accountNameFirstSeen, accountNameResult, accountNameScore, accountNameWorstScore, accountTelephone, accountTelephoneActivities, Arrays.hashCode(accountTelephoneAttributes), accountTelephoneCountryCode, accountTelephoneFirstSeen, accountTelephoneGeo, accountTelephoneIsPossible, accountTelephoneIsValid, accountTelephoneResult, accountTelephoneScore, accountTelephoneType, accountTelephoneWorstScore, agentBssidActivities, Arrays.hashCode(agentBssidAttributes), agentBssidFirstSeen, agentBssidResult, agentBssidScore, agentBssidWorstScore, agentSsidClear, appIntegrityScore, audioContext, batteryStatusLevel, batteryStatus, bbAnomalyRating, Arrays.hashCode(bbAnomalyReasonCode), bbAnomalyScore, bbAssessment, bbAssessmentRating, bbAuthConfidenceScore, bbAuthHistoricalScoreMean, bbAuthHistoricalScoreStd, bbAuthScore, bbBotRating, bbBotScore, bbFraudRating, bbFraudScore, Arrays.hashCode(behaviosecBotReasons), behaviosecConfidence, Arrays.hashCode(behaviosecDataIntegrityReasons), behaviosecPopulationProfileChallengerRiskRank, behaviosecPopulationProfileChallengerScore, behaviosecPopulationProfileRiskRank, behaviosecPopulationProfileScore, behaviosecScore, behaviosecUserid, browser, browserVersion, browserAddon, browserAddonHash, browserAnomaly, browserLanguage, browserSpoofRating, browserStringHash, browserString, pluginNumber, profiledUrl, canvasHash, cidrNumber, deviceActivities, Arrays.hashCode(deviceAttributes), deviceFingerprint, deviceFingerprintActivities, Arrays.hashCode(deviceFingerprintAttributes), deviceFingerprintFirstSeen, deviceFingerprintResult, deviceFingerprintScore, deviceFingerprintWorstScore, deviceFirstSeen, Arrays.hashCode(deviceHealthReasons), deviceId, deviceIdConfidence, deviceMemory, deviceModel, deviceName, agentLanguage, deviceResult, deviceScore, deviceWorstScore, digitalId, digitalIdActivities, Arrays.hashCode(digitalIdAttributes), digitalIdConfidence, digitalIdConfidenceRating, digitalIdFirstSeen, digitalIdReasonCode, digitalIdResult, digitalIdTrustScore, digitalIdTrustScoreRating, Arrays.hashCode(digitalIdTrustScoreReasonCode), Arrays.hashCode(digitalIdTrustScoreSummaryReasonCode), dnsIp, Arrays.hashCode(dnsIpAttributes), dnsIpCity, dnsIpGeo, dnsIpIsp, dnsIpOrganization, dnsIpPostalCode, dnsIpRegion, emailageEmailriskscoreEaadvice, emailageEmailriskscoreEareason, emailageEmailriskscoreEascore, emailageEmailriskscoreEariskbandid, emailageEmailriskscoreEmailCreationDays, emailageEmailriskscoreEmailage, emailageEmailriskscorePhonecarriertype, emailageEmailriskscorePhoneownermatch, emailageEmailriskscorePhonetofullnameconfidence, emailageEmailriskscorePhonetolastnameconfidence, emailageEmailriskscoreIpRisklevel, emailageEmailriskscoreIpRiskreason, emailageEmailriskscore, emailageEmailriskscoreDomainrisklevel, emailageEmailriskscoreDomainCreationDays, emailageEmailriskscoreDomainage, emailageEmailriskscoreDomainexists, emailageEmailriskscoreDomaincountry, emailageEmailriskscoreDomaincompany, emailageEmailriskscoreDomainname, emailageEmailriskscoreDomaincategory, emailageEmailriskscoreOveralldigitalidentityscore, emailageEmailriskscoreDisdescription, eventDatetime, eventId, finalReviewStatus, Arrays.hashCode(fuzzyDeviceAttributes), fuzzyDeviceFirstSeen, fuzzyDeviceId, fuzzyDeviceIdConfidence, fuzzyDeviceMatchResult, fuzzyDeviceResult, fuzzyDeviceScore, fuzzyDeviceWorstScore, geofenceCountryGps, geofenceRegionGps, gpsSpoof, gpuName, gpuVendor, hwFingerprint, jbRoot, jsBrowser, jsBrowserStringHash, jsFontsHash, jsFontsNumber, jsOs, liveness, macAddress, mathRoutine, mimeTypeHash, multiDisplay, nationalId, passwordHash, passwordHashActivities, Arrays.hashCode(passwordHashAttributes), passwordHashFirstSeen, passwordHashResult, passwordHashScore, passwordHashWorstScore, policy, privateBrowsing, proxyIp, proxyIpActivities, Arrays.hashCode(proxyIpAttributes), proxyIpCity, proxyIpConnectionType, proxyIpFirstSeen, proxyIpGeo, proxyIpHome, proxyIpIsp, proxyIpLatitude, proxyIpLongitude, proxyIpOrganization, proxyIpOrganizationType, proxyIpPostalCode, proxyIpRegion, proxyIpResult, proxyIpRoutingType, proxyIpScore, proxyIpWorstScore, proxyIpv6, proxyName, proxyScore, proxyType, relatedRequestId, remoteAccess, remoteAccessRating, remoteAccessScore, remoteDesktop, screenRes, screenResAnomaly, screenResZoom, seRating, seScore, smartLearningFraudRating, smartLearningPScore, smartLearningPolicyScore, smartLearningReasonCode, smartLearningRiskRank, smartLearningSummaryReasonCode, timeZone, timeZoneDstOffset, timezoneName, timezoneOffsetAnomaly, tmxPolicyScore, tmxRiskRating, tmxVariables, trueIp, trueIpActivities, Arrays.hashCode(trueIpAttributes), trueIpCity, trueIpCountryConfidence, trueIpFirstSeen, trueIpGeo, trueIpIsp, trueIpLastEvent, trueIpOrganization, trueIpOrganizationType, trueIpPostalCode, trueIpRegion, trueIpResult, trueIpConnectionType, trueIpRoutingType, trueIpScore, trueIpWorstScore, trueIpv6, uaAgent, uaMobile, unknownSession, virtualDevice, virtualDeviceReason, vpnReason, vpnScore, webglHash, webrtcExternalIp, customerEventType, inputIpAddress, inputIpIsp, inputIpCity, inputIpRegion, inputIpGeo, inputIpRoutingType, webSessionId, conditionAttrib5, agentBrand, agentModel, applicationName, transactionId, lineOfBusiness, accountAddressStreet2, conditionAttrib1, conditionAttrib2, customCount1, localAttrib1, customCount4, conditionAttrib6, conditionAttrib3, primaryAccountBalanceUsd, primaryAccountBalance, primaryAccountBalanceCurrency, conditionAttrib10, conditionAttrib9, conditionAttrib4, conditionAttrib7, conditionAttrib8, localAttrib12, localAttrib13, localAttrib14, localAttrib15, customCount2, customCount13, summaryReasonCode, os, osVersion, tmxSummaryReasonCode);
    }

    @Override
    public String toString() {
        return "LnSessionParsedObject{" + "ucid='" + ucid + '\'' + ", id=" + id + ", brand='" + brand + '\'' + ", sessionId='" + sessionId + '\'' + ", userId=" + userId + ", email='" + email + '\'' + ", mobileCode=" + mobileCode + ", mobile='" + mobile + '\'' + ", eventType='" + eventType + '\'' + ", isFromApp=" + isFromApp + ", createTime='" + createTime + '\'' + ", policyScore=" + policyScore + ", riskRating='" + riskRating + '\'' + ", reviewStatus='" + reviewStatus + '\'' + ", requestResult='" + requestResult + '\'' + ", accountAddress='" + accountAddress + '\'' + ", accountAddressActivities='" + accountAddressActivities + '\'' + ", accountAddressAttributes=" + Arrays.toString(accountAddressAttributes) + ", accountAddressCity='" + accountAddressCity + '\'' + ", accountAddressCountry='" + accountAddressCountry + '\'' + ", accountAddressFirstSeen='" + accountAddressFirstSeen + '\'' + ", accountAddressResult='" + accountAddressResult + '\'' + ", accountAddressScore=" + accountAddressScore + ", accountAddressState='" + accountAddressState + '\'' + ", accountAddressStreet1='" + accountAddressStreet1 + '\'' + ", accountAddressWorstScore=" + accountAddressWorstScore + ", accountAddressZip='" + accountAddressZip + '\'' + ", accountDateOfBirth='" + accountDateOfBirth + '\'' + ", accountEmail='" + accountEmail + '\'' + ", accountEmailActivities='" + accountEmailActivities + '\'' + ", accountEmailAttributes=" + Arrays.toString(accountEmailAttributes) + ", accountEmailDomain='" + accountEmailDomain + '\'' + ", accountEmailFirstSeen='" + accountEmailFirstSeen + '\'' + ", accountEmailResult='" + accountEmailResult + '\'' + ", accountEmailScore=" + accountEmailScore + ", accountEmailWorstScore=" + accountEmailWorstScore + ", accountFirstName='" + accountFirstName + '\'' + ", accountGender='" + accountGender + '\'' + ", accountLastName='" + accountLastName + '\'' + ", accountLexIdActivities='" + accountLexIdActivities + '\'' + ", accountLexIdAttributes=" + Arrays.toString(accountLexIdAttributes) + ", accountLexIdFirstSeen='" + accountLexIdFirstSeen + '\'' + ", accountLexIdNumber='" + accountLexIdNumber + '\'' + ", accountLexIdResult='" + accountLexIdResult + '\'' + ", accountLexIdScore=" + accountLexIdScore + ", accountLexIdWorstScore=" + accountLexIdWorstScore + ", accountLogin='" + accountLogin + '\'' + ", accountLoginActivities='" + accountLoginActivities + '\'' + ", accountLoginAttributes=" + Arrays.toString(accountLoginAttributes) + ", accountLoginFirstSeen='" + accountLoginFirstSeen + '\'' + ", accountLoginResult='" + accountLoginResult + '\'' + ", accountLoginScore=" + accountLoginScore + ", accountLoginWorstScore=" + accountLoginWorstScore + ", accountName='" + accountName + '\'' + ", accountNameActivities='" + accountNameActivities + '\'' + ", accountNameAttributes=" + Arrays.toString(accountNameAttributes) + ", accountNameFirstSeen='" + accountNameFirstSeen + '\'' + ", accountNameResult='" + accountNameResult + '\'' + ", accountNameScore=" + accountNameScore + ", accountNameWorstScore=" + accountNameWorstScore + ", accountTelephone='" + accountTelephone + '\'' + ", accountTelephoneActivities='" + accountTelephoneActivities + '\'' + ", accountTelephoneAttributes=" + Arrays.toString(accountTelephoneAttributes) + ", accountTelephoneCountryCode=" + accountTelephoneCountryCode + ", accountTelephoneFirstSeen='" + accountTelephoneFirstSeen + '\'' + ", accountTelephoneGeo='" + accountTelephoneGeo + '\'' + ", accountTelephoneIsPossible='" + accountTelephoneIsPossible + '\'' + ", accountTelephoneIsValid='" + accountTelephoneIsValid + '\'' + ", accountTelephoneResult='" + accountTelephoneResult + '\'' + ", accountTelephoneScore=" + accountTelephoneScore + ", accountTelephoneType='" + accountTelephoneType + '\'' + ", accountTelephoneWorstScore=" + accountTelephoneWorstScore + ", agentBssidActivities='" + agentBssidActivities + '\'' + ", agentBssidAttributes=" + Arrays.toString(agentBssidAttributes) + ", agentBssidFirstSeen='" + agentBssidFirstSeen + '\'' + ", agentBssidResult='" + agentBssidResult + '\'' + ", agentBssidScore=" + agentBssidScore + ", agentBssidWorstScore=" + agentBssidWorstScore + ", agentSsidClear='" + agentSsidClear + '\'' + ", appIntegrityScore=" + appIntegrityScore + ", audioContext='" + audioContext + '\'' + ", batteryStatusLevel=" + batteryStatusLevel + ", batteryStatus='" + batteryStatus + '\'' + ", bbAnomalyRating='" + bbAnomalyRating + '\'' + ", bbAnomalyReasonCode=" + Arrays.toString(bbAnomalyReasonCode) + ", bbAnomalyScore=" + bbAnomalyScore + ", bbAssessment=" + bbAssessment + ", bbAssessmentRating='" + bbAssessmentRating + '\'' + ", bbAuthConfidenceScore=" + bbAuthConfidenceScore + ", bbAuthHistoricalScoreMean=" + bbAuthHistoricalScoreMean + ", bbAuthHistoricalScoreStd=" + bbAuthHistoricalScoreStd + ", bbAuthScore=" + bbAuthScore + ", bbBotRating='" + bbBotRating + '\'' + ", bbBotScore=" + bbBotScore + ", bbFraudRating='" + bbFraudRating + '\'' + ", bbFraudScore=" + bbFraudScore + ", behaviosecBotReasons=" + Arrays.toString(behaviosecBotReasons) + ", behaviosecConfidence=" + behaviosecConfidence + ", behaviosecDataIntegrityReasons=" + Arrays.toString(behaviosecDataIntegrityReasons) + ", behaviosecPopulationProfileChallengerRiskRank=" + behaviosecPopulationProfileChallengerRiskRank + ", behaviosecPopulationProfileChallengerScore=" + behaviosecPopulationProfileChallengerScore + ", behaviosecPopulationProfileRiskRank=" + behaviosecPopulationProfileRiskRank + ", behaviosecPopulationProfileScore=" + behaviosecPopulationProfileScore + ", behaviosecScore=" + behaviosecScore + ", behaviosecUserid='" + behaviosecUserid + '\'' + ", browser='" + browser + '\'' + ", browserVersion='" + browserVersion + '\'' + ", browserAddon='" + browserAddon + '\'' + ", browserAddonHash='" + browserAddonHash + '\'' + ", browserAnomaly='" + browserAnomaly + '\'' + ", browserLanguage='" + browserLanguage + '\'' + ", browserSpoofRating='" + browserSpoofRating + '\'' + ", browserStringHash='" + browserStringHash + '\'' + ", browserString='" + browserString + '\'' + ", pluginNumber=" + pluginNumber + ", profiledUrl='" + profiledUrl + '\'' + ", canvasHash='" + canvasHash + '\'' + ", cidrNumber=" + cidrNumber + ", deviceActivities='" + deviceActivities + '\'' + ", deviceAttributes=" + Arrays.toString(deviceAttributes) + ", deviceFingerprint='" + deviceFingerprint + '\'' + ", deviceFingerprintActivities='" + deviceFingerprintActivities + '\'' + ", deviceFingerprintAttributes=" + Arrays.toString(deviceFingerprintAttributes) + ", deviceFingerprintFirstSeen='" + deviceFingerprintFirstSeen + '\'' + ", deviceFingerprintResult='" + deviceFingerprintResult + '\'' + ", deviceFingerprintScore=" + deviceFingerprintScore + ", deviceFingerprintWorstScore=" + deviceFingerprintWorstScore + ", deviceFirstSeen='" + deviceFirstSeen + '\'' + ", deviceHealthReasons=" + Arrays.toString(deviceHealthReasons) + ", deviceId='" + deviceId + '\'' + ", deviceIdConfidence=" + deviceIdConfidence + ", deviceMemory=" + deviceMemory + ", deviceModel='" + deviceModel + '\'' + ", deviceName='" + deviceName + '\'' + ", agentLanguage='" + agentLanguage + '\'' + ", deviceResult='" + deviceResult + '\'' + ", deviceScore=" + deviceScore + ", deviceWorstScore=" + deviceWorstScore + ", digitalId='" + digitalId + '\'' + ", digitalIdActivities='" + digitalIdActivities + '\'' + ", digitalIdAttributes=" + Arrays.toString(digitalIdAttributes) + ", digitalIdConfidence=" + digitalIdConfidence + ", digitalIdConfidenceRating='" + digitalIdConfidenceRating + '\'' + ", digitalIdFirstSeen='" + digitalIdFirstSeen + '\'' + ", digitalIdReasonCode='" + digitalIdReasonCode + '\'' + ", digitalIdResult='" + digitalIdResult + '\'' + ", digitalIdTrustScore=" + digitalIdTrustScore + ", digitalIdTrustScoreRating='" + digitalIdTrustScoreRating + '\'' + ", digitalIdTrustScoreReasonCode=" + Arrays.toString(digitalIdTrustScoreReasonCode) + ", digitalIdTrustScoreSummaryReasonCode=" + Arrays.toString(
                digitalIdTrustScoreSummaryReasonCode) + ", dnsIp='" + dnsIp + '\'' + ", dnsIpAttributes=" + Arrays.toString(dnsIpAttributes) + ", dnsIpCity='" + dnsIpCity + '\'' + ", dnsIpGeo='" + dnsIpGeo + '\'' + ", dnsIpIsp='" + dnsIpIsp + '\'' + ", dnsIpOrganization='" + dnsIpOrganization + '\'' + ", dnsIpPostalCode='" + dnsIpPostalCode + '\'' + ", dnsIpRegion='" + dnsIpRegion + '\'' + ", emailageEmailriskscoreEaadvice='" + emailageEmailriskscoreEaadvice + '\'' + ", emailageEmailriskscoreEareason='" + emailageEmailriskscoreEareason + '\'' + ", emailageEmailriskscoreEascore=" + emailageEmailriskscoreEascore + ", emailageEmailriskscoreEariskbandid=" + emailageEmailriskscoreEariskbandid + ", emailageEmailriskscoreEmailCreationDays=" + emailageEmailriskscoreEmailCreationDays + ", emailageEmailriskscoreEmailage='" + emailageEmailriskscoreEmailage + '\'' + ", emailageEmailriskscorePhonecarriertype='" + emailageEmailriskscorePhonecarriertype + '\'' + ", emailageEmailriskscorePhoneownermatch='" + emailageEmailriskscorePhoneownermatch + '\'' + ", emailageEmailriskscorePhonetofullnameconfidence=" + emailageEmailriskscorePhonetofullnameconfidence + ", emailageEmailriskscorePhonetolastnameconfidence=" + emailageEmailriskscorePhonetolastnameconfidence + ", emailageEmailriskscoreIpRisklevel='" + emailageEmailriskscoreIpRisklevel + '\'' + ", emailageEmailriskscoreIpRiskreason='" + emailageEmailriskscoreIpRiskreason + '\'' + ", emailageEmailriskscore='" + emailageEmailriskscore + '\'' + ", emailageEmailriskscoreDomainrisklevel='" + emailageEmailriskscoreDomainrisklevel + '\'' + ", emailageEmailriskscoreDomainCreationDays=" + emailageEmailriskscoreDomainCreationDays + ", emailageEmailriskscoreDomainage='" + emailageEmailriskscoreDomainage + '\'' + ", emailageEmailriskscoreDomainexists='" + emailageEmailriskscoreDomainexists + '\'' + ", emailageEmailriskscoreDomaincountry='" + emailageEmailriskscoreDomaincountry + '\'' + ", emailageEmailriskscoreDomaincompany='" + emailageEmailriskscoreDomaincompany + '\'' + ", emailageEmailriskscoreDomainname='" + emailageEmailriskscoreDomainname + '\'' + ", emailageEmailriskscoreDomaincategory='" + emailageEmailriskscoreDomaincategory + '\'' + ", emailageEmailriskscoreOveralldigitalidentityscore=" + emailageEmailriskscoreOveralldigitalidentityscore + ", emailageEmailriskscoreDisdescription='" + emailageEmailriskscoreDisdescription + '\'' + ", eventDatetime='" + eventDatetime + '\'' + ", eventId=" + eventId + ", finalReviewStatus='" + finalReviewStatus + '\'' + ", fuzzyDeviceAttributes=" + Arrays.toString(fuzzyDeviceAttributes) + ", fuzzyDeviceFirstSeen='" + fuzzyDeviceFirstSeen + '\'' + ", fuzzyDeviceId='" + fuzzyDeviceId + '\'' + ", fuzzyDeviceIdConfidence=" + fuzzyDeviceIdConfidence + ", fuzzyDeviceMatchResult='" + fuzzyDeviceMatchResult + '\'' + ", fuzzyDeviceResult='" + fuzzyDeviceResult + '\'' + ", fuzzyDeviceScore=" + fuzzyDeviceScore + ", fuzzyDeviceWorstScore=" + fuzzyDeviceWorstScore + ", geofenceCountryGps='" + geofenceCountryGps + '\'' + ", geofenceRegionGps='" + geofenceRegionGps + '\'' + ", gpsSpoof='" + gpsSpoof + '\'' + ", gpuName='" + gpuName + '\'' + ", gpuVendor='" + gpuVendor + '\'' + ", hwFingerprint='" + hwFingerprint + '\'' + ", jbRoot=" + jbRoot + ", jsBrowser='" + jsBrowser + '\'' + ", jsBrowserStringHash='" + jsBrowserStringHash + '\'' + ", jsFontsHash='" + jsFontsHash + '\'' + ", jsFontsNumber=" + jsFontsNumber + ", jsOs='" + jsOs + '\'' + ", liveness='" + liveness + '\'' + ", macAddress='" + macAddress + '\'' + ", mathRoutine='" + mathRoutine + '\'' + ", mimeTypeHash='" + mimeTypeHash + '\'' + ", multiDisplay='" + multiDisplay + '\'' + ", nationalId='" + nationalId + '\'' + ", passwordHash='" + passwordHash + '\'' + ", passwordHashActivities='" + passwordHashActivities + '\'' + ", passwordHashAttributes=" + Arrays.toString(passwordHashAttributes) + ", passwordHashFirstSeen='" + passwordHashFirstSeen + '\'' + ", passwordHashResult='" + passwordHashResult + '\'' + ", passwordHashScore=" + passwordHashScore + ", passwordHashWorstScore=" + passwordHashWorstScore + ", policy='" + policy + '\'' + ", privateBrowsing='" + privateBrowsing + '\'' + ", proxyIp='" + proxyIp + '\'' + ", proxyIpActivities='" + proxyIpActivities + '\'' + ", proxyIpAttributes=" + Arrays.toString(proxyIpAttributes) + ", proxyIpCity='" + proxyIpCity + '\'' + ", proxyIpConnectionType='" + proxyIpConnectionType + '\'' + ", proxyIpFirstSeen='" + proxyIpFirstSeen + '\'' + ", proxyIpGeo='" + proxyIpGeo + '\'' + ", proxyIpHome='" + proxyIpHome + '\'' + ", proxyIpIsp='" + proxyIpIsp + '\'' + ", proxyIpLatitude=" + proxyIpLatitude + ", proxyIpLongitude=" + proxyIpLongitude + ", proxyIpOrganization='" + proxyIpOrganization + '\'' + ", proxyIpOrganizationType='" + proxyIpOrganizationType + '\'' + ", proxyIpPostalCode='" + proxyIpPostalCode + '\'' + ", proxyIpRegion='" + proxyIpRegion + '\'' + ", proxyIpResult='" + proxyIpResult + '\'' + ", proxyIpRoutingType='" + proxyIpRoutingType + '\'' + ", proxyIpScore=" + proxyIpScore + ", proxyIpWorstScore=" + proxyIpWorstScore + ", proxyIpv6='" + proxyIpv6 + '\'' + ", proxyName='" + proxyName + '\'' + ", proxyScore=" + proxyScore + ", proxyType='" + proxyType + '\'' + ", relatedRequestId='" + relatedRequestId + '\'' + ", remoteAccess='" + remoteAccess + '\'' + ", remoteAccessRating='" + remoteAccessRating + '\'' + ", remoteAccessScore=" + remoteAccessScore + ", remoteDesktop='" + remoteDesktop + '\'' + ", screenRes='" + screenRes + '\'' + ", screenResAnomaly='" + screenResAnomaly + '\'' + ", screenResZoom=" + screenResZoom + ", seRating='" + seRating + '\'' + ", seScore=" + seScore + ", smartLearningFraudRating='" + smartLearningFraudRating + '\'' + ", smartLearningPScore=" + smartLearningPScore + ", smartLearningPolicyScore=" + smartLearningPolicyScore + ", smartLearningReasonCode='" + smartLearningReasonCode + '\'' + ", smartLearningRiskRank=" + smartLearningRiskRank + ", smartLearningSummaryReasonCode='" + smartLearningSummaryReasonCode + '\'' + ", timeZone=" + timeZone + ", timeZoneDstOffset=" + timeZoneDstOffset + ", timezoneName='" + timezoneName + '\'' + ", timezoneOffsetAnomaly='" + timezoneOffsetAnomaly + '\'' + ", tmxPolicyScore=" + tmxPolicyScore + ", tmxRiskRating='" + tmxRiskRating + '\'' + ", tmxVariables='" + tmxVariables + '\'' + ", trueIp='" + trueIp + '\'' + ", trueIpActivities='" + trueIpActivities + '\'' + ", trueIpAttributes=" + Arrays.toString(trueIpAttributes) + ", trueIpCity='" + trueIpCity + '\'' + ", trueIpCountryConfidence=" + trueIpCountryConfidence + ", trueIpFirstSeen='" + trueIpFirstSeen + '\'' + ", trueIpGeo='" + trueIpGeo + '\'' + ", trueIpIsp='" + trueIpIsp + '\'' + ", trueIpLastEvent='" + trueIpLastEvent + '\'' + ", trueIpOrganization='" + trueIpOrganization + '\'' + ", trueIpOrganizationType='" + trueIpOrganizationType + '\'' + ", trueIpPostalCode='" + trueIpPostalCode + '\'' + ", trueIpRegion='" + trueIpRegion + '\'' + ", trueIpResult='" + trueIpResult + '\'' + ", trueIpConnectionType='" + trueIpConnectionType + '\'' + ", trueIpRoutingType='" + trueIpRoutingType + '\'' + ", trueIpScore=" + trueIpScore + ", trueIpWorstScore=" + trueIpWorstScore + ", trueIpv6='" + trueIpv6 + '\'' + ", uaAgent='" + uaAgent + '\'' + ", uaMobile='" + uaMobile + '\'' + ", unknownSession='" + unknownSession + '\'' + ", virtualDevice=" + virtualDevice + ", virtualDeviceReason='" + virtualDeviceReason + '\'' + ", vpnReason='" + vpnReason + '\'' + ", vpnScore=" + vpnScore + ", webglHash='" + webglHash + '\'' + ", webrtcExternalIp='" + webrtcExternalIp + '\'' + ", customerEventType='" + customerEventType + '\'' + ", inputIpAddress='" + inputIpAddress + '\'' + ", inputIpIsp='" + inputIpIsp + '\'' + ", inputIpCity='" + inputIpCity + '\'' + ", inputIpRegion='" + inputIpRegion + '\'' + ", inputIpGeo='" + inputIpGeo + '\'' + ", inputIpRoutingType='" + inputIpRoutingType + '\'' + ", webSessionId='" + webSessionId + '\'' + ", conditionAttrib5='" + conditionAttrib5 + '\'' + ", agentBrand='" + agentBrand + '\'' + ", agentModel='" + agentModel + '\'' + ", applicationName='" + applicationName + '\'' + ", transactionId='" + transactionId + '\'' + ", lineOfBusiness='" + lineOfBusiness + '\'' + ", accountAddressStreet2='" + accountAddressStreet2 + '\'' + ", conditionAttrib1='" + conditionAttrib1 + '\'' + ", conditionAttrib2='" + conditionAttrib2 + '\'' + ", customCount1=" + customCount1 + ", localAttrib1='" + localAttrib1 + '\'' + ", customCount4='" + customCount4 + '\'' + ", conditionAttrib6='" + conditionAttrib6 + '\'' + ", conditionAttrib3='" + conditionAttrib3 + '\'' + ", primaryAccountBalanceUsd=" + primaryAccountBalanceUsd + ", primaryAccountBalance=" + primaryAccountBalance + ", primaryAccountBalanceCurrency='" + primaryAccountBalanceCurrency + '\'' + ", conditionAttrib10='" + conditionAttrib10 + '\'' + ", conditionAttrib9=" + conditionAttrib9 + ", conditionAttrib4='" + conditionAttrib4 + '\'' + ", conditionAttrib7='" + conditionAttrib7 + '\'' + ", conditionAttrib8='" + conditionAttrib8 + '\'' + ", localAttrib12='" + localAttrib12 + '\'' + ", localAttrib13='" + localAttrib13 + '\'' + ", localAttrib14='" + localAttrib14 + '\'' + ", localAttrib15='" + localAttrib15 + '\'' + ", customCount2='" + customCount2 + '\'' + ", customCount13='" + customCount13 + '\'' + ", summaryReasonCode='" + summaryReasonCode + '\'' + ", os='" + os + '\'' + ", osVersion='" + osVersion + '\'' + ", tmxSummaryReasonCode='" + tmxSummaryReasonCode + '\'' + '}';
    }

    public String toStringRawResponse() {
        return "{" + "ucid='" + ucid + '\"' + ", id=" + id + ", brand='" + brand + '\"' + ", sessionId='" + sessionId + '\"' + ", userId=" + userId + ", email='" + email + '\"' + ", mobileCode=" + mobileCode + ", mobile='" + mobile + '\"' + ", eventType='" + eventType + '\"' + ", isFromApp=" + isFromApp + ", createTime='" + createTime + '\"' + ", policyScore=" + policyScore + ", riskRating='" + riskRating + '\"' + ", reviewStatus='" + reviewStatus + '\"' + ", requestResult='" + requestResult + '\"' + ", accountAddress='" + accountAddress + '\"' + ", accountAddressActivities='" + accountAddressActivities + '\"' + ", accountAddressAttributes=" + Arrays.toString(accountAddressAttributes) + ", accountAddressCity='" + accountAddressCity + '\"' + ", accountAddressCountry='" + accountAddressCountry + '\"' + ", accountAddressFirstSeen='" + accountAddressFirstSeen + '\"' + ", accountAddressResult='" + accountAddressResult + '\"' + ", accountAddressScore=" + accountAddressScore + ", accountAddressState='" + accountAddressState + '\"' + ", accountAddressStreet1='" + accountAddressStreet1 + '\"' + ", accountAddressWorstScore=" + accountAddressWorstScore + ", accountAddressZip='" + accountAddressZip + '\"' + ", accountDateOfBirth='" + accountDateOfBirth + '\"' + ", accountEmail='" + accountEmail + '\"' + ", accountEmailActivities='" + accountEmailActivities + '\"' + ", accountEmailAttributes=" + Arrays.toString(accountEmailAttributes) + ", accountEmailDomain='" + accountEmailDomain + '\"' + ", accountEmailFirstSeen='" + accountEmailFirstSeen + '\"' + ", accountEmailResult='" + accountEmailResult + '\"' + ", accountEmailScore=" + accountEmailScore + ", accountEmailWorstScore=" + accountEmailWorstScore + ", accountFirstName='" + accountFirstName + '\"' + ", accountGender='" + accountGender + '\"' + ", accountLastName='" + accountLastName + '\"' + ", accountLexIdActivities='" + accountLexIdActivities + '\"' + ", accountLexIdAttributes=" + Arrays.toString(accountLexIdAttributes) + ", accountLexIdFirstSeen='" + accountLexIdFirstSeen + '\"' + ", accountLexIdNumber='" + accountLexIdNumber + '\"' + ", accountLexIdResult='" + accountLexIdResult + '\"' + ", accountLexIdScore=" + accountLexIdScore + ", accountLexIdWorstScore=" + accountLexIdWorstScore + ", accountLogin='" + accountLogin + '\"' + ", accountLoginActivities='" + accountLoginActivities + '\"' + ", accountLoginAttributes=" + Arrays.toString(accountLoginAttributes) + ", accountLoginFirstSeen='" + accountLoginFirstSeen + '\"' + ", accountLoginResult='" + accountLoginResult + '\"' + ", accountLoginScore=" + accountLoginScore + ", accountLoginWorstScore=" + accountLoginWorstScore + ", accountName='" + accountName + '\"' + ", accountNameActivities='" + accountNameActivities + '\"' + ", accountNameAttributes=" + Arrays.toString(accountNameAttributes) + ", accountNameFirstSeen='" + accountNameFirstSeen + '\"' + ", accountNameResult='" + accountNameResult + '\"' + ", accountNameScore=" + accountNameScore + ", accountNameWorstScore=" + accountNameWorstScore + ", accountTelephone='" + accountTelephone + '\"' + ", accountTelephoneActivities='" + accountTelephoneActivities + '\"' + ", accountTelephoneAttributes=" + Arrays.toString(accountTelephoneAttributes) + ", accountTelephoneCountryCode=" + accountTelephoneCountryCode + ", accountTelephoneFirstSeen='" + accountTelephoneFirstSeen + '\"' + ", accountTelephoneGeo='" + accountTelephoneGeo + '\"' + ", accountTelephoneIsPossible='" + accountTelephoneIsPossible + '\"' + ", accountTelephoneIsValid='" + accountTelephoneIsValid + '\"' + ", accountTelephoneResult='" + accountTelephoneResult + '\"' + ", accountTelephoneScore=" + accountTelephoneScore + ", accountTelephoneType='" + accountTelephoneType + '\"' + ", accountTelephoneWorstScore=" + accountTelephoneWorstScore + ", agentBssidActivities='" + agentBssidActivities + '\"' + ", agentBssidAttributes=" + Arrays.toString(agentBssidAttributes) + ", agentBssidFirstSeen='" + agentBssidFirstSeen + '\"' + ", agentBssidResult='" + agentBssidResult + '\"' + ", agentBssidScore=" + agentBssidScore + ", agentBssidWorstScore=" + agentBssidWorstScore + ", agentSsidClear='" + agentSsidClear + '\"' + ", appIntegrityScore=" + appIntegrityScore + ", audioContext='" + audioContext + '\"' + ", batteryStatusLevel=" + batteryStatusLevel + ", batteryStatus='" + batteryStatus + '\"' + ", bbAnomalyRating='" + bbAnomalyRating + '\"' + ", bbAnomalyReasonCode=" + Arrays.toString(bbAnomalyReasonCode) + ", bbAnomalyScore=" + bbAnomalyScore + ", bbAssessment=" + bbAssessment + ", bbAssessmentRating='" + bbAssessmentRating + '\"' + ", bbAuthConfidenceScore=" + bbAuthConfidenceScore + ", bbAuthHistoricalScoreMean=" + bbAuthHistoricalScoreMean + ", bbAuthHistoricalScoreStd=" + bbAuthHistoricalScoreStd + ", bbAuthScore=" + bbAuthScore + ", bbBotRating='" + bbBotRating + '\"' + ", bbBotScore=" + bbBotScore + ", bbFraudRating='" + bbFraudRating + '\"' + ", bbFraudScore=" + bbFraudScore + ", behaviosecBotReasons=" + Arrays.toString(behaviosecBotReasons) + ", behaviosecConfidence=" + behaviosecConfidence + ", behaviosecDataIntegrityReasons=" + Arrays.toString(behaviosecDataIntegrityReasons) + ", behaviosecPopulationProfileChallengerRiskRank=" + behaviosecPopulationProfileChallengerRiskRank + ", behaviosecPopulationProfileChallengerScore=" + behaviosecPopulationProfileChallengerScore + ", behaviosecPopulationProfileRiskRank=" + behaviosecPopulationProfileRiskRank + ", behaviosecPopulationProfileScore=" + behaviosecPopulationProfileScore + ", behaviosecScore=" + behaviosecScore + ", behaviosecUserid='" + behaviosecUserid + '\"' + ", browser='" + browser + '\"' + ", browserVersion='" + browserVersion + '\"' + ", browserAddon='" + browserAddon + '\"' + ", browserAddonHash='" + browserAddonHash + '\"' + ", browserAnomaly='" + browserAnomaly + '\"' + ", browserLanguage='" + browserLanguage + '\"' + ", browserSpoofRating='" + browserSpoofRating + '\"' + ", browserStringHash='" + browserStringHash + '\"' + ", browserString='" + browserString + '\"' + ", pluginNumber=" + pluginNumber + ", profiledUrl='" + profiledUrl + '\"' + ", canvasHash='" + canvasHash + '\"' + ", cidrNumber=" + cidrNumber + ", deviceActivities='" + deviceActivities + '\"' + ", deviceAttributes=" + Arrays.toString(deviceAttributes) + ", deviceFingerprint='" + deviceFingerprint + '\"' + ", deviceFingerprintActivities='" + deviceFingerprintActivities + '\"' + ", deviceFingerprintAttributes=" + Arrays.toString(deviceFingerprintAttributes) + ", deviceFingerprintFirstSeen='" + deviceFingerprintFirstSeen + '\"' + ", deviceFingerprintResult='" + deviceFingerprintResult + '\"' + ", deviceFingerprintScore=" + deviceFingerprintScore + ", deviceFingerprintWorstScore=" + deviceFingerprintWorstScore + ", deviceFirstSeen='" + deviceFirstSeen + '\"' + ", deviceHealthReasons=" + Arrays.toString(deviceHealthReasons) + ", deviceId='" + deviceId + '\"' + ", deviceIdConfidence=" + deviceIdConfidence + ", deviceMemory=" + deviceMemory + ", deviceModel='" + deviceModel + '\"' + ", deviceName='" + deviceName + '\"' + ", agentLanguage='" + agentLanguage + '\"' + ", deviceResult='" + deviceResult + '\"' + ", deviceScore=" + deviceScore + ", deviceWorstScore=" + deviceWorstScore + ", digitalId='" + digitalId + '\"' + ", digitalIdActivities='" + digitalIdActivities + '\"' + ", digitalIdAttributes=" + Arrays.toString(digitalIdAttributes) + ", digitalIdConfidence=" + digitalIdConfidence + ", digitalIdConfidenceRating='" + digitalIdConfidenceRating + '\"' + ", digitalIdFirstSeen='" + digitalIdFirstSeen + '\"' + ", digitalIdReasonCode='" + digitalIdReasonCode + '\"' + ", digitalIdResult='" + digitalIdResult + '\"' + ", digitalIdTrustScore=" + digitalIdTrustScore + ", digitalIdTrustScoreRating='" + digitalIdTrustScoreRating + '\"' + ", digitalIdTrustScoreReasonCode=" + Arrays.toString(digitalIdTrustScoreReasonCode) + ", digitalIdTrustScoreSummaryReasonCode=" + Arrays.toString(
                digitalIdTrustScoreSummaryReasonCode) + ", dnsIp='" + dnsIp + '\"' + ", dnsIpAttributes=" + Arrays.toString(dnsIpAttributes) + ", dnsIpCity='" + dnsIpCity + '\"' + ", dnsIpGeo='" + dnsIpGeo + '\"' + ", dnsIpIsp='" + dnsIpIsp + '\"' + ", dnsIpOrganization='" + dnsIpOrganization + '\"' + ", dnsIpPostalCode='" + dnsIpPostalCode + '\"' + ", dnsIpRegion='" + dnsIpRegion + '\"' + ", emailageEmailriskscoreEaadvice='" + emailageEmailriskscoreEaadvice + '\"' + ", emailageEmailriskscoreEareason='" + emailageEmailriskscoreEareason + '\"' + ", emailageEmailriskscoreEascore=" + emailageEmailriskscoreEascore + ", emailageEmailriskscoreEariskbandid=" + emailageEmailriskscoreEariskbandid + ", emailageEmailriskscoreEmailCreationDays=" + emailageEmailriskscoreEmailCreationDays + ", emailageEmailriskscoreEmailage='" + emailageEmailriskscoreEmailage + '\"' + ", emailageEmailriskscorePhonecarriertype='" + emailageEmailriskscorePhonecarriertype + '\"' + ", emailageEmailriskscorePhoneownermatch='" + emailageEmailriskscorePhoneownermatch + '\"' + ", emailageEmailriskscorePhonetofullnameconfidence=" + emailageEmailriskscorePhonetofullnameconfidence + ", emailageEmailriskscorePhonetolastnameconfidence=" + emailageEmailriskscorePhonetolastnameconfidence + ", emailageEmailriskscoreIpRisklevel='" + emailageEmailriskscoreIpRisklevel + '\"' + ", emailageEmailriskscoreIpRiskreason='" + emailageEmailriskscoreIpRiskreason + '\"' + ", emailageEmailriskscore='" + emailageEmailriskscore + '\"' + ", emailageEmailriskscoreDomainrisklevel='" + emailageEmailriskscoreDomainrisklevel + '\"' + ", emailageEmailriskscoreDomainCreationDays=" + emailageEmailriskscoreDomainCreationDays + ", emailageEmailriskscoreDomainage='" + emailageEmailriskscoreDomainage + '\"' + ", emailageEmailriskscoreDomainexists='" + emailageEmailriskscoreDomainexists + '\"' + ", emailageEmailriskscoreDomaincountry='" + emailageEmailriskscoreDomaincountry + '\"' + ", emailageEmailriskscoreDomaincompany='" + emailageEmailriskscoreDomaincompany + '\"' + ", emailageEmailriskscoreDomainname='" + emailageEmailriskscoreDomainname + '\"' + ", emailageEmailriskscoreDomaincategory='" + emailageEmailriskscoreDomaincategory + '\"' + ", emailageEmailriskscoreOveralldigitalidentityscore=" + emailageEmailriskscoreOveralldigitalidentityscore + ", emailageEmailriskscoreDisdescription='" + emailageEmailriskscoreDisdescription + '\"' + ", eventDatetime='" + eventDatetime + '\"' + ", eventId=" + eventId + ", finalReviewStatus='" + finalReviewStatus + '\"' + ", fuzzyDeviceAttributes=" + Arrays.toString(fuzzyDeviceAttributes) + ", fuzzyDeviceFirstSeen='" + fuzzyDeviceFirstSeen + '\"' + ", fuzzyDeviceId='" + fuzzyDeviceId + '\"' + ", fuzzyDeviceIdConfidence=" + fuzzyDeviceIdConfidence + ", fuzzyDeviceMatchResult='" + fuzzyDeviceMatchResult + '\"' + ", fuzzyDeviceResult='" + fuzzyDeviceResult + '\"' + ", fuzzyDeviceScore=" + fuzzyDeviceScore + ", fuzzyDeviceWorstScore=" + fuzzyDeviceWorstScore + ", geofenceCountryGps='" + geofenceCountryGps + '\"' + ", geofenceRegionGps='" + geofenceRegionGps + '\"' + ", gpsSpoof='" + gpsSpoof + '\"' + ", gpuName='" + gpuName + '\"' + ", gpuVendor='" + gpuVendor + '\"' + ", hwFingerprint='" + hwFingerprint + '\"' + ", jbRoot=" + jbRoot + ", jsBrowser='" + jsBrowser + '\"' + ", jsBrowserStringHash='" + jsBrowserStringHash + '\"' + ", jsFontsHash='" + jsFontsHash + '\"' + ", jsFontsNumber=" + jsFontsNumber + ", jsOs='" + jsOs + '\"' + ", liveness='" + liveness + '\"' + ", macAddress='" + macAddress + '\"' + ", mathRoutine='" + mathRoutine + '\"' + ", mimeTypeHash='" + mimeTypeHash + '\"' + ", multiDisplay='" + multiDisplay + '\"' + ", nationalId='" + nationalId + '\"' + ", passwordHash='" + passwordHash + '\"' + ", passwordHashActivities='" + passwordHashActivities + '\"' + ", passwordHashAttributes=" + Arrays.toString(passwordHashAttributes) + ", passwordHashFirstSeen='" + passwordHashFirstSeen + '\"' + ", passwordHashResult='" + passwordHashResult + '\"' + ", passwordHashScore=" + passwordHashScore + ", passwordHashWorstScore=" + passwordHashWorstScore + ", policy='" + policy + '\"' + ", privateBrowsing='" + privateBrowsing + '\"' + ", proxyIp='" + proxyIp + '\"' + ", proxyIpActivities='" + proxyIpActivities + '\"' + ", proxyIpAttributes=" + Arrays.toString(proxyIpAttributes) + ", proxyIpCity='" + proxyIpCity + '\"' + ", proxyIpConnectionType='" + proxyIpConnectionType + '\"' + ", proxyIpFirstSeen='" + proxyIpFirstSeen + '\"' + ", proxyIpGeo='" + proxyIpGeo + '\"' + ", proxyIpHome='" + proxyIpHome + '\"' + ", proxyIpIsp='" + proxyIpIsp + '\"' + ", proxyIpLatitude=" + proxyIpLatitude + ", proxyIpLongitude=" + proxyIpLongitude + ", proxyIpOrganization='" + proxyIpOrganization + '\"' + ", proxyIpOrganizationType='" + proxyIpOrganizationType + '\"' + ", proxyIpPostalCode='" + proxyIpPostalCode + '\"' + ", proxyIpRegion='" + proxyIpRegion + '\"' + ", proxyIpResult='" + proxyIpResult + '\"' + ", proxyIpRoutingType='" + proxyIpRoutingType + '\"' + ", proxyIpScore=" + proxyIpScore + ", proxyIpWorstScore=" + proxyIpWorstScore + ", proxyIpv6='" + proxyIpv6 + '\"' + ", proxyName='" + proxyName + '\"' + ", proxyScore=" + proxyScore + ", proxyType='" + proxyType + '\"' + ", relatedRequestId='" + relatedRequestId + '\"' + ", remoteAccess='" + remoteAccess + '\"' + ", remoteAccessRating='" + remoteAccessRating + '\"' + ", remoteAccessScore=" + remoteAccessScore + ", remoteDesktop='" + remoteDesktop + '\"' + ", screenRes='" + screenRes + '\"' + ", screenResAnomaly='" + screenResAnomaly + '\"' + ", screenResZoom=" + screenResZoom + ", seRating='" + seRating + '\"' + ", seScore=" + seScore + ", smartLearningFraudRating='" + smartLearningFraudRating + '\"' + ", smartLearningPScore=" + smartLearningPScore + ", smartLearningPolicyScore=" + smartLearningPolicyScore + ", smartLearningReasonCode='" + smartLearningReasonCode + '\"' + ", smartLearningRiskRank=" + smartLearningRiskRank + ", smartLearningSummaryReasonCode='" + smartLearningSummaryReasonCode + '\"' + ", timeZone=" + timeZone + ", timeZoneDstOffset=" + timeZoneDstOffset + ", timezoneName='" + timezoneName + '\"' + ", timezoneOffsetAnomaly='" + timezoneOffsetAnomaly + '\"' + ", tmxPolicyScore=" + tmxPolicyScore + ", tmxRiskRating='" + tmxRiskRating + '\"' + ", tmxVariables='" + tmxVariables + '\"' + ", trueIp='" + trueIp + '\"' + ", trueIpActivities='" + trueIpActivities + '\"' + ", trueIpAttributes=" + Arrays.toString(trueIpAttributes) + ", trueIpCity='" + trueIpCity + '\"' + ", trueIpCountryConfidence=" + trueIpCountryConfidence + ", trueIpFirstSeen='" + trueIpFirstSeen + '\"' + ", trueIpGeo='" + trueIpGeo + '\"' + ", trueIpIsp='" + trueIpIsp + '\"' + ", trueIpLastEvent='" + trueIpLastEvent + '\"' + ", trueIpOrganization='" + trueIpOrganization + '\"' + ", trueIpOrganizationType='" + trueIpOrganizationType + '\"' + ", trueIpPostalCode='" + trueIpPostalCode + '\"' + ", trueIpRegion='" + trueIpRegion + '\"' + ", trueIpResult='" + trueIpResult + '\"' + ", trueIpConnectionType='" + trueIpConnectionType + '\"' + ", trueIpRoutingType='" + trueIpRoutingType + '\"' + ", trueIpScore=" + trueIpScore + ", trueIpWorstScore=" + trueIpWorstScore + ", trueIpv6='" + trueIpv6 + '\"' + ", uaAgent='" + uaAgent + '\"' + ", uaMobile='" + uaMobile + '\"' + ", unknownSession='" + unknownSession + '\"' + ", virtualDevice=" + virtualDevice + ", virtualDeviceReason='" + virtualDeviceReason + '\"' + ", vpnReason='" + vpnReason + '\"' + ", vpnScore=" + vpnScore + ", webglHash='" + webglHash + '\"' + ", webrtcExternalIp='" + webrtcExternalIp + '\"' + ", customerEventType='" + customerEventType + '\"' + ", inputIpAddress='" + inputIpAddress + '\"' + ", inputIpIsp='" + inputIpIsp + '\"' + ", inputIpCity='" + inputIpCity + '\"' + ", inputIpRegion='" + inputIpRegion + '\"' + ", inputIpGeo='" + inputIpGeo + '\"' + ", inputIpRoutingType='" + inputIpRoutingType + '\"' + ", webSessionId='" + webSessionId + '\"' + ", conditionAttrib5='" + conditionAttrib5 + '\"' + ", agentBrand='" + agentBrand + '\"' + ", agentModel='" + agentModel + '\"' + ", applicationName='" + applicationName + '\"' + ", transactionId='" + transactionId + '\"' + ", lineOfBusiness='" + lineOfBusiness + '\"' + ", accountAddressStreet2='" + accountAddressStreet2 + '\"' + ", conditionAttrib1='" + conditionAttrib1 + '\"' + ", conditionAttrib2='" + conditionAttrib2 + '\"' + ", customCount1=" + customCount1 + ", localAttrib1='" + localAttrib1 + '\"' + ", customCount4='" + customCount4 + '\"' + ", conditionAttrib6='" + conditionAttrib6 + '\"' + ", conditionAttrib3='" + conditionAttrib3 + '\"' + ", primaryAccountBalanceUsd=" + primaryAccountBalanceUsd + ", primaryAccountBalance=" + primaryAccountBalance + ", primaryAccountBalanceCurrency='" + primaryAccountBalanceCurrency + '\"' + ", conditionAttrib10='" + conditionAttrib10 + '\"' + ", conditionAttrib9=" + conditionAttrib9 + ", conditionAttrib4='" + conditionAttrib4 + '\"' + ", conditionAttrib7='" + conditionAttrib7 + '\"' + ", conditionAttrib8='" + conditionAttrib8 + '\"' + ", localAttrib12='" + localAttrib12 + '\"' + ", localAttrib13='" + localAttrib13 + '\"' + ", localAttrib14='" + localAttrib14 + '\"' + ", localAttrib15='" + localAttrib15 + '\"' + ", customCount2='" + customCount2 + '\"' + ", customCount13='" + customCount13 + '\"' + ", summaryReasonCode='" + summaryReasonCode + '\"' + ", os='" + os + '\"' + ", osVersion='" + osVersion + '\"' + ", tmxSummaryReasonCode='" + tmxSummaryReasonCode + '\"' + '}';
    }

    public void setRiskRating(String riskRating) {
        this.riskRating = riskRating;
    }

    public String getUcid() {
        return ucid;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getMobileCode() {
        return mobileCode;
    }

    public void setMobileCode(Integer mobileCode) {
        this.mobileCode = mobileCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Boolean getFromApp() {
        return isFromApp;
    }

    public void setFromApp(Boolean fromApp) {
        isFromApp = fromApp;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getPolicyScore() {
        return policyScore;
    }

    public void setPolicyScore(Integer policyScore) {
        this.policyScore = policyScore;
    }

    public String getRiskRating() {
        return riskRating;
    }

    public String getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public String getRequestResult() {
        return requestResult;
    }

    public void setRequestResult(String requestResult) {
        this.requestResult = requestResult;
    }

    public String getAccountAddress() {
        return accountAddress;
    }

    public void setAccountAddress(String accountAddress) {
        this.accountAddress = accountAddress;
    }

    public String getAccountAddressActivities() {
        return accountAddressActivities;
    }

    public void setAccountAddressActivities(String accountAddressActivities) {
        this.accountAddressActivities = accountAddressActivities;
    }

    public String[] getAccountAddressAttributes() {
        return accountAddressAttributes;
    }

    public void setAccountAddressAttributes(String[] accountAddressAttributes) {
        this.accountAddressAttributes = accountAddressAttributes;
    }

    public String getAccountAddressCity() {
        return accountAddressCity;
    }

    public void setAccountAddressCity(String accountAddressCity) {
        this.accountAddressCity = accountAddressCity;
    }

    public String getAccountAddressCountry() {
        return accountAddressCountry;
    }

    public void setAccountAddressCountry(String accountAddressCountry) {
        this.accountAddressCountry = accountAddressCountry;
    }

    public String getAccountAddressFirstSeen() {
        return accountAddressFirstSeen;
    }

    public void setAccountAddressFirstSeen(String accountAddressFirstSeen) {
        this.accountAddressFirstSeen = accountAddressFirstSeen;
    }

    public String getAccountAddressResult() {
        return accountAddressResult;
    }

    public void setAccountAddressResult(String accountAddressResult) {
        this.accountAddressResult = accountAddressResult;
    }

    public Integer getAccountAddressScore() {
        return accountAddressScore;
    }

    public void setAccountAddressScore(Integer accountAddressScore) {
        this.accountAddressScore = accountAddressScore;
    }

    public String getAccountAddressState() {
        return accountAddressState;
    }

    public void setAccountAddressState(String accountAddressState) {
        this.accountAddressState = accountAddressState;
    }

    public String getAccountAddressStreet1() {
        return accountAddressStreet1;
    }

    public void setAccountAddressStreet1(String accountAddressStreet1) {
        this.accountAddressStreet1 = accountAddressStreet1;
    }

    public Integer getAccountAddressWorstScore() {
        return accountAddressWorstScore;
    }

    public void setAccountAddressWorstScore(Integer accountAddressWorstScore) {
        this.accountAddressWorstScore = accountAddressWorstScore;
    }

    public String getAccountAddressZip() {
        return accountAddressZip;
    }

    public void setAccountAddressZip(String accountAddressZip) {
        this.accountAddressZip = accountAddressZip;
    }

    public String getAccountDateOfBirth() {
        return accountDateOfBirth;
    }

    public void setAccountDateOfBirth(String accountDateOfBirth) {
        this.accountDateOfBirth = accountDateOfBirth;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public String getAccountEmailActivities() {
        return accountEmailActivities;
    }

    public void setAccountEmailActivities(String accountEmailActivities) {
        this.accountEmailActivities = accountEmailActivities;
    }

    public String[] getAccountEmailAttributes() {
        return accountEmailAttributes;
    }

    public void setAccountEmailAttributes(String[] accountEmailAttributes) {
        this.accountEmailAttributes = accountEmailAttributes;
    }

    public String getAccountEmailDomain() {
        return accountEmailDomain;
    }

    public void setAccountEmailDomain(String accountEmailDomain) {
        this.accountEmailDomain = accountEmailDomain;
    }

    public String getAccountEmailFirstSeen() {
        return accountEmailFirstSeen;
    }

    public void setAccountEmailFirstSeen(String accountEmailFirstSeen) {
        this.accountEmailFirstSeen = accountEmailFirstSeen;
    }

    public String getAccountEmailResult() {
        return accountEmailResult;
    }

    public void setAccountEmailResult(String accountEmailResult) {
        this.accountEmailResult = accountEmailResult;
    }

    public Integer getAccountEmailScore() {
        return accountEmailScore;
    }

    public void setAccountEmailScore(Integer accountEmailScore) {
        this.accountEmailScore = accountEmailScore;
    }

    public Integer getAccountEmailWorstScore() {
        return accountEmailWorstScore;
    }

    public void setAccountEmailWorstScore(Integer accountEmailWorstScore) {
        this.accountEmailWorstScore = accountEmailWorstScore;
    }

    public String getAccountFirstName() {
        return accountFirstName;
    }

    public void setAccountFirstName(String accountFirstName) {
        this.accountFirstName = accountFirstName;
    }

    public String getAccountGender() {
        return accountGender;
    }

    public void setAccountGender(String accountGender) {
        this.accountGender = accountGender;
    }

    public String getAccountLastName() {
        return accountLastName;
    }

    public void setAccountLastName(String accountLastName) {
        this.accountLastName = accountLastName;
    }

    public String getAccountLexIdActivities() {
        return accountLexIdActivities;
    }

    public void setAccountLexIdActivities(String accountLexIdActivities) {
        this.accountLexIdActivities = accountLexIdActivities;
    }

    public String[] getAccountLexIdAttributes() {
        return accountLexIdAttributes;
    }

    public void setAccountLexIdAttributes(String[] accountLexIdAttributes) {
        this.accountLexIdAttributes = accountLexIdAttributes;
    }

    public String getAccountLexIdFirstSeen() {
        return accountLexIdFirstSeen;
    }

    public void setAccountLexIdFirstSeen(String accountLexIdFirstSeen) {
        this.accountLexIdFirstSeen = accountLexIdFirstSeen;
    }

    public String getAccountLexIdNumber() {
        return accountLexIdNumber;
    }

    public void setAccountLexIdNumber(String accountLexIdNumber) {
        this.accountLexIdNumber = accountLexIdNumber;
    }

    public String getAccountLexIdResult() {
        return accountLexIdResult;
    }

    public void setAccountLexIdResult(String accountLexIdResult) {
        this.accountLexIdResult = accountLexIdResult;
    }

    public Integer getAccountLexIdScore() {
        return accountLexIdScore;
    }

    public void setAccountLexIdScore(Integer accountLexIdScore) {
        this.accountLexIdScore = accountLexIdScore;
    }

    public Integer getAccountLexIdWorstScore() {
        return accountLexIdWorstScore;
    }

    public void setAccountLexIdWorstScore(Integer accountLexIdWorstScore) {
        this.accountLexIdWorstScore = accountLexIdWorstScore;
    }

    public String getAccountLogin() {
        return accountLogin;
    }

    public void setAccountLogin(String accountLogin) {
        this.accountLogin = accountLogin;
    }

    public String getAccountLoginActivities() {
        return accountLoginActivities;
    }

    public void setAccountLoginActivities(String accountLoginActivities) {
        this.accountLoginActivities = accountLoginActivities;
    }

    public String[] getAccountLoginAttributes() {
        return accountLoginAttributes;
    }

    public void setAccountLoginAttributes(String[] accountLoginAttributes) {
        this.accountLoginAttributes = accountLoginAttributes;
    }

    public String getAccountLoginFirstSeen() {
        return accountLoginFirstSeen;
    }

    public void setAccountLoginFirstSeen(String accountLoginFirstSeen) {
        this.accountLoginFirstSeen = accountLoginFirstSeen;
    }

    public String getAccountLoginResult() {
        return accountLoginResult;
    }

    public void setAccountLoginResult(String accountLoginResult) {
        this.accountLoginResult = accountLoginResult;
    }

    public Integer getAccountLoginScore() {
        return accountLoginScore;
    }

    public void setAccountLoginScore(Integer accountLoginScore) {
        this.accountLoginScore = accountLoginScore;
    }

    public Integer getAccountLoginWorstScore() {
        return accountLoginWorstScore;
    }

    public void setAccountLoginWorstScore(Integer accountLoginWorstScore) {
        this.accountLoginWorstScore = accountLoginWorstScore;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNameActivities() {
        return accountNameActivities;
    }

    public void setAccountNameActivities(String accountNameActivities) {
        this.accountNameActivities = accountNameActivities;
    }

    public String[] getAccountNameAttributes() {
        return accountNameAttributes;
    }

    public void setAccountNameAttributes(String[] accountNameAttributes) {
        this.accountNameAttributes = accountNameAttributes;
    }

    public String getAccountNameFirstSeen() {
        return accountNameFirstSeen;
    }

    public void setAccountNameFirstSeen(String accountNameFirstSeen) {
        this.accountNameFirstSeen = accountNameFirstSeen;
    }

    public String getAccountNameResult() {
        return accountNameResult;
    }

    public void setAccountNameResult(String accountNameResult) {
        this.accountNameResult = accountNameResult;
    }

    public Integer getAccountNameScore() {
        return accountNameScore;
    }

    public void setAccountNameScore(Integer accountNameScore) {
        this.accountNameScore = accountNameScore;
    }

    public Integer getAccountNameWorstScore() {
        return accountNameWorstScore;
    }

    public void setAccountNameWorstScore(Integer accountNameWorstScore) {
        this.accountNameWorstScore = accountNameWorstScore;
    }

    public String getAccountTelephone() {
        return accountTelephone;
    }

    public void setAccountTelephone(String accountTelephone) {
        this.accountTelephone = accountTelephone;
    }

    public String getAccountTelephoneActivities() {
        return accountTelephoneActivities;
    }

    public void setAccountTelephoneActivities(String accountTelephoneActivities) {
        this.accountTelephoneActivities = accountTelephoneActivities;
    }

    public String[] getAccountTelephoneAttributes() {
        return accountTelephoneAttributes;
    }

    public void setAccountTelephoneAttributes(String[] accountTelephoneAttributes) {
        this.accountTelephoneAttributes = accountTelephoneAttributes;
    }

    public Integer getAccountTelephoneCountryCode() {
        return accountTelephoneCountryCode;
    }

    public void setAccountTelephoneCountryCode(Integer accountTelephoneCountryCode) {
        this.accountTelephoneCountryCode = accountTelephoneCountryCode;
    }

    public String getAccountTelephoneFirstSeen() {
        return accountTelephoneFirstSeen;
    }

    public void setAccountTelephoneFirstSeen(String accountTelephoneFirstSeen) {
        this.accountTelephoneFirstSeen = accountTelephoneFirstSeen;
    }

    public String getAccountTelephoneGeo() {
        return accountTelephoneGeo;
    }

    public void setAccountTelephoneGeo(String accountTelephoneGeo) {
        this.accountTelephoneGeo = accountTelephoneGeo;
    }

    public String getAccountTelephoneIsPossible() {
        return accountTelephoneIsPossible;
    }

    public void setAccountTelephoneIsPossible(String accountTelephoneIsPossible) {
        this.accountTelephoneIsPossible = accountTelephoneIsPossible;
    }

    public String getAccountTelephoneIsValid() {
        return accountTelephoneIsValid;
    }

    public void setAccountTelephoneIsValid(String accountTelephoneIsValid) {
        this.accountTelephoneIsValid = accountTelephoneIsValid;
    }

    public String getAccountTelephoneResult() {
        return accountTelephoneResult;
    }

    public void setAccountTelephoneResult(String accountTelephoneResult) {
        this.accountTelephoneResult = accountTelephoneResult;
    }

    public Integer getAccountTelephoneScore() {
        return accountTelephoneScore;
    }

    public void setAccountTelephoneScore(Integer accountTelephoneScore) {
        this.accountTelephoneScore = accountTelephoneScore;
    }

    public String getAccountTelephoneType() {
        return accountTelephoneType;
    }

    public void setAccountTelephoneType(String accountTelephoneType) {
        this.accountTelephoneType = accountTelephoneType;
    }

    public Integer getAccountTelephoneWorstScore() {
        return accountTelephoneWorstScore;
    }

    public void setAccountTelephoneWorstScore(Integer accountTelephoneWorstScore) {
        this.accountTelephoneWorstScore = accountTelephoneWorstScore;
    }

    public String getAgentBssidActivities() {
        return agentBssidActivities;
    }

    public void setAgentBssidActivities(String agentBssidActivities) {
        this.agentBssidActivities = agentBssidActivities;
    }

    public String[] getAgentBssidAttributes() {
        return agentBssidAttributes;
    }

    public void setAgentBssidAttributes(String[] agentBssidAttributes) {
        this.agentBssidAttributes = agentBssidAttributes;
    }

    public String getAgentBssidFirstSeen() {
        return agentBssidFirstSeen;
    }

    public void setAgentBssidFirstSeen(String agentBssidFirstSeen) {
        this.agentBssidFirstSeen = agentBssidFirstSeen;
    }

    public String getAgentBssidResult() {
        return agentBssidResult;
    }

    public void setAgentBssidResult(String agentBssidResult) {
        this.agentBssidResult = agentBssidResult;
    }

    public Integer getAgentBssidScore() {
        return agentBssidScore;
    }

    public void setAgentBssidScore(Integer agentBssidScore) {
        this.agentBssidScore = agentBssidScore;
    }

    public Integer getAgentBssidWorstScore() {
        return agentBssidWorstScore;
    }

    public void setAgentBssidWorstScore(Integer agentBssidWorstScore) {
        this.agentBssidWorstScore = agentBssidWorstScore;
    }

    public String getAgentSsidClear() {
        return agentSsidClear;
    }

    public void setAgentSsidClear(String agentSsidClear) {
        this.agentSsidClear = agentSsidClear;
    }

    public Integer getAppIntegrityScore() {
        return appIntegrityScore;
    }

    public void setAppIntegrityScore(Integer appIntegrityScore) {
        this.appIntegrityScore = appIntegrityScore;
    }

    public String getAudioContext() {
        return audioContext;
    }

    public void setAudioContext(String audioContext) {
        this.audioContext = audioContext;
    }

    public Double getBatteryStatusLevel() {
        return batteryStatusLevel;
    }

    public void setBatteryStatusLevel(Double batteryStatusLevel) {
        this.batteryStatusLevel = batteryStatusLevel;
    }

    public String getBatteryStatus() {
        return batteryStatus;
    }

    public void setBatteryStatus(String batteryStatus) {
        this.batteryStatus = batteryStatus;
    }

    public String getBbAnomalyRating() {
        return bbAnomalyRating;
    }

    public void setBbAnomalyRating(String bbAnomalyRating) {
        this.bbAnomalyRating = bbAnomalyRating;
    }

    public String[] getBbAnomalyReasonCode() {
        return bbAnomalyReasonCode;
    }

    public void setBbAnomalyReasonCode(String[] bbAnomalyReasonCode) {
        this.bbAnomalyReasonCode = bbAnomalyReasonCode;
    }

    public Integer getBbAnomalyScore() {
        return bbAnomalyScore;
    }

    public void setBbAnomalyScore(Integer bbAnomalyScore) {
        this.bbAnomalyScore = bbAnomalyScore;
    }

    public Double getBbAssessment() {
        return bbAssessment;
    }

    public void setBbAssessment(Double bbAssessment) {
        this.bbAssessment = bbAssessment;
    }

    public String getBbAssessmentRating() {
        return bbAssessmentRating;
    }

    public void setBbAssessmentRating(String bbAssessmentRating) {
        this.bbAssessmentRating = bbAssessmentRating;
    }

    public Double getBbAuthConfidenceScore() {
        return bbAuthConfidenceScore;
    }

    public void setBbAuthConfidenceScore(Double bbAuthConfidenceScore) {
        this.bbAuthConfidenceScore = bbAuthConfidenceScore;
    }

    public Double getBbAuthHistoricalScoreMean() {
        return bbAuthHistoricalScoreMean;
    }

    public void setBbAuthHistoricalScoreMean(Double bbAuthHistoricalScoreMean) {
        this.bbAuthHistoricalScoreMean = bbAuthHistoricalScoreMean;
    }

    public Double getBbAuthHistoricalScoreStd() {
        return bbAuthHistoricalScoreStd;
    }

    public void setBbAuthHistoricalScoreStd(Double bbAuthHistoricalScoreStd) {
        this.bbAuthHistoricalScoreStd = bbAuthHistoricalScoreStd;
    }

    public Double getBbAuthScore() {
        return bbAuthScore;
    }

    public void setBbAuthScore(Double bbAuthScore) {
        this.bbAuthScore = bbAuthScore;
    }

    public String getBbBotRating() {
        return bbBotRating;
    }

    public void setBbBotRating(String bbBotRating) {
        this.bbBotRating = bbBotRating;
    }

    public Double getBbBotScore() {
        return bbBotScore;
    }

    public void setBbBotScore(Double bbBotScore) {
        this.bbBotScore = bbBotScore;
    }

    public String getBbFraudRating() {
        return bbFraudRating;
    }

    public void setBbFraudRating(String bbFraudRating) {
        this.bbFraudRating = bbFraudRating;
    }

    public Double getBbFraudScore() {
        return bbFraudScore;
    }

    public void setBbFraudScore(Double bbFraudScore) {
        this.bbFraudScore = bbFraudScore;
    }

    public String[] getBehaviosecBotReasons() {
        return behaviosecBotReasons;
    }

    public void setBehaviosecBotReasons(String[] behaviosecBotReasons) {
        this.behaviosecBotReasons = behaviosecBotReasons;
    }

    public Double getBehaviosecConfidence() {
        return behaviosecConfidence;
    }

    public void setBehaviosecConfidence(Double behaviosecConfidence) {
        this.behaviosecConfidence = behaviosecConfidence;
    }

    public String[] getBehaviosecDataIntegrityReasons() {
        return behaviosecDataIntegrityReasons;
    }

    public void setBehaviosecDataIntegrityReasons(String[] behaviosecDataIntegrityReasons) {
        this.behaviosecDataIntegrityReasons = behaviosecDataIntegrityReasons;
    }

    public Double getBehaviosecPopulationProfileChallengerRiskRank() {
        return behaviosecPopulationProfileChallengerRiskRank;
    }

    public void setBehaviosecPopulationProfileChallengerRiskRank(Double behaviosecPopulationProfileChallengerRiskRank) {
        this.behaviosecPopulationProfileChallengerRiskRank = behaviosecPopulationProfileChallengerRiskRank;
    }

    public Double getBehaviosecPopulationProfileChallengerScore() {
        return behaviosecPopulationProfileChallengerScore;
    }

    public void setBehaviosecPopulationProfileChallengerScore(Double behaviosecPopulationProfileChallengerScore) {
        this.behaviosecPopulationProfileChallengerScore = behaviosecPopulationProfileChallengerScore;
    }

    public Double getBehaviosecPopulationProfileRiskRank() {
        return behaviosecPopulationProfileRiskRank;
    }

    public void setBehaviosecPopulationProfileRiskRank(Double behaviosecPopulationProfileRiskRank) {
        this.behaviosecPopulationProfileRiskRank = behaviosecPopulationProfileRiskRank;
    }

    public Double getBehaviosecPopulationProfileScore() {
        return behaviosecPopulationProfileScore;
    }

    public void setBehaviosecPopulationProfileScore(Double behaviosecPopulationProfileScore) {
        this.behaviosecPopulationProfileScore = behaviosecPopulationProfileScore;
    }

    public Double getBehaviosecScore() {
        return behaviosecScore;
    }

    public void setBehaviosecScore(Double behaviosecScore) {
        this.behaviosecScore = behaviosecScore;
    }

    public String getBehaviosecUserid() {
        return behaviosecUserid;
    }

    public void setBehaviosecUserid(String behaviosecUserid) {
        this.behaviosecUserid = behaviosecUserid;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    public String getBrowserAddon() {
        return browserAddon;
    }

    public void setBrowserAddon(String browserAddon) {
        this.browserAddon = browserAddon;
    }

    public String getBrowserAddonHash() {
        return browserAddonHash;
    }

    public void setBrowserAddonHash(String browserAddonHash) {
        this.browserAddonHash = browserAddonHash;
    }

    public String getBrowserAnomaly() {
        return browserAnomaly;
    }

    public void setBrowserAnomaly(String browserAnomaly) {
        this.browserAnomaly = browserAnomaly;
    }

    public String getBrowserLanguage() {
        return browserLanguage;
    }

    public void setBrowserLanguage(String browserLanguage) {
        this.browserLanguage = browserLanguage;
    }

    public String getBrowserSpoofRating() {
        return browserSpoofRating;
    }

    public void setBrowserSpoofRating(String browserSpoofRating) {
        this.browserSpoofRating = browserSpoofRating;
    }

    public String getBrowserStringHash() {
        return browserStringHash;
    }

    public void setBrowserStringHash(String browserStringHash) {
        this.browserStringHash = browserStringHash;
    }

    public String getBrowserString() {
        return browserString;
    }

    public void setBrowserString(String browserString) {
        this.browserString = browserString;
    }

    public int getPluginNumber() {
        return pluginNumber;
    }

    public void setPluginNumber(int pluginNumber) {
        this.pluginNumber = pluginNumber;
    }

    public String getProfiledUrl() {
        return profiledUrl;
    }

    public void setProfiledUrl(String profiledUrl) {
        this.profiledUrl = profiledUrl;
    }

    public String getCanvasHash() {
        return canvasHash;
    }

    public void setCanvasHash(String canvasHash) {
        this.canvasHash = canvasHash;
    }

    public Integer getCidrNumber() {
        return cidrNumber;
    }

    public void setCidrNumber(Integer cidrNumber) {
        this.cidrNumber = cidrNumber;
    }

    public String getDeviceActivities() {
        return deviceActivities;
    }

    public void setDeviceActivities(String deviceActivities) {
        this.deviceActivities = deviceActivities;
    }

    public String[] getDeviceAttributes() {
        return deviceAttributes;
    }

    public void setDeviceAttributes(String[] deviceAttributes) {
        this.deviceAttributes = deviceAttributes;
    }

    public String getDeviceFingerprint() {
        return deviceFingerprint;
    }

    public void setDeviceFingerprint(String deviceFingerprint) {
        this.deviceFingerprint = deviceFingerprint;
    }

    public String getDeviceFingerprintActivities() {
        return deviceFingerprintActivities;
    }

    public void setDeviceFingerprintActivities(String deviceFingerprintActivities) {
        this.deviceFingerprintActivities = deviceFingerprintActivities;
    }

    public String[] getDeviceFingerprintAttributes() {
        return deviceFingerprintAttributes;
    }

    public void setDeviceFingerprintAttributes(String[] deviceFingerprintAttributes) {
        this.deviceFingerprintAttributes = deviceFingerprintAttributes;
    }

    public String getDeviceFingerprintFirstSeen() {
        return deviceFingerprintFirstSeen;
    }

    public void setDeviceFingerprintFirstSeen(String deviceFingerprintFirstSeen) {
        this.deviceFingerprintFirstSeen = deviceFingerprintFirstSeen;
    }

    public String getDeviceFingerprintResult() {
        return deviceFingerprintResult;
    }

    public void setDeviceFingerprintResult(String deviceFingerprintResult) {
        this.deviceFingerprintResult = deviceFingerprintResult;
    }

    public Integer getDeviceFingerprintScore() {
        return deviceFingerprintScore;
    }

    public void setDeviceFingerprintScore(Integer deviceFingerprintScore) {
        this.deviceFingerprintScore = deviceFingerprintScore;
    }

    public Integer getDeviceFingerprintWorstScore() {
        return deviceFingerprintWorstScore;
    }

    public void setDeviceFingerprintWorstScore(Integer deviceFingerprintWorstScore) {
        this.deviceFingerprintWorstScore = deviceFingerprintWorstScore;
    }

    public String getDeviceFirstSeen() {
        return deviceFirstSeen;
    }

    public void setDeviceFirstSeen(String deviceFirstSeen) {
        this.deviceFirstSeen = deviceFirstSeen;
    }

    public String[] getDeviceHealthReasons() {
        return deviceHealthReasons;
    }

    public void setDeviceHealthReasons(String[] deviceHealthReasons) {
        this.deviceHealthReasons = deviceHealthReasons;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Double getDeviceIdConfidence() {
        return deviceIdConfidence;
    }

    public void setDeviceIdConfidence(Double deviceIdConfidence) {
        this.deviceIdConfidence = deviceIdConfidence;
    }

    public Integer getDeviceMemory() {
        return deviceMemory;
    }

    public void setDeviceMemory(Integer deviceMemory) {
        this.deviceMemory = deviceMemory;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getAgentLanguage() {
        return agentLanguage;
    }

    public void setAgentLanguage(String agentLanguage) {
        this.agentLanguage = agentLanguage;
    }

    public String getDeviceResult() {
        return deviceResult;
    }

    public void setDeviceResult(String deviceResult) {
        this.deviceResult = deviceResult;
    }

    public Integer getDeviceScore() {
        return deviceScore;
    }

    public void setDeviceScore(Integer deviceScore) {
        this.deviceScore = deviceScore;
    }

    public Integer getDeviceWorstScore() {
        return deviceWorstScore;
    }

    public void setDeviceWorstScore(Integer deviceWorstScore) {
        this.deviceWorstScore = deviceWorstScore;
    }

    public String getDigitalId() {
        return digitalId;
    }

    public void setDigitalId(String digitalId) {
        this.digitalId = digitalId;
    }

    public String getDigitalIdActivities() {
        return digitalIdActivities;
    }

    public void setDigitalIdActivities(String digitalIdActivities) {
        this.digitalIdActivities = digitalIdActivities;
    }

    public String[] getDigitalIdAttributes() {
        return digitalIdAttributes;
    }

    public void setDigitalIdAttributes(String[] digitalIdAttributes) {
        this.digitalIdAttributes = digitalIdAttributes;
    }

    public Integer getDigitalIdConfidence() {
        return digitalIdConfidence;
    }

    public void setDigitalIdConfidence(Integer digitalIdConfidence) {
        this.digitalIdConfidence = digitalIdConfidence;
    }

    public String getDigitalIdConfidenceRating() {
        return digitalIdConfidenceRating;
    }

    public void setDigitalIdConfidenceRating(String digitalIdConfidenceRating) {
        this.digitalIdConfidenceRating = digitalIdConfidenceRating;
    }

    public String getDigitalIdFirstSeen() {
        return digitalIdFirstSeen;
    }

    public void setDigitalIdFirstSeen(String digitalIdFirstSeen) {
        this.digitalIdFirstSeen = digitalIdFirstSeen;
    }

    public String getDigitalIdReasonCode() {
        return digitalIdReasonCode;
    }

    public void setDigitalIdReasonCode(String digitalIdReasonCode) {
        this.digitalIdReasonCode = digitalIdReasonCode;
    }

    public String getDigitalIdResult() {
        return digitalIdResult;
    }

    public void setDigitalIdResult(String digitalIdResult) {
        this.digitalIdResult = digitalIdResult;
    }

    public Double getDigitalIdTrustScore() {
        return digitalIdTrustScore;
    }

    public void setDigitalIdTrustScore(Double digitalIdTrustScore) {
        this.digitalIdTrustScore = digitalIdTrustScore;
    }

    public String getDigitalIdTrustScoreRating() {
        return digitalIdTrustScoreRating;
    }

    public void setDigitalIdTrustScoreRating(String digitalIdTrustScoreRating) {
        this.digitalIdTrustScoreRating = digitalIdTrustScoreRating;
    }

    public String[] getDigitalIdTrustScoreReasonCode() {
        return digitalIdTrustScoreReasonCode;
    }

    public void setDigitalIdTrustScoreReasonCode(String[] digitalIdTrustScoreReasonCode) {
        this.digitalIdTrustScoreReasonCode = digitalIdTrustScoreReasonCode;
    }

    public String[] getDigitalIdTrustScoreSummaryReasonCode() {
        return digitalIdTrustScoreSummaryReasonCode;
    }

    public void setDigitalIdTrustScoreSummaryReasonCode(String[] digitalIdTrustScoreSummaryReasonCode) {
        this.digitalIdTrustScoreSummaryReasonCode = digitalIdTrustScoreSummaryReasonCode;
    }

    public String getDnsIp() {
        return dnsIp;
    }

    public void setDnsIp(String dnsIp) {
        this.dnsIp = dnsIp;
    }

    public String[] getDnsIpAttributes() {
        return dnsIpAttributes;
    }

    public void setDnsIpAttributes(String[] dnsIpAttributes) {
        this.dnsIpAttributes = dnsIpAttributes;
    }

    public String getDnsIpCity() {
        return dnsIpCity;
    }

    public void setDnsIpCity(String dnsIpCity) {
        this.dnsIpCity = dnsIpCity;
    }

    public String getDnsIpGeo() {
        return dnsIpGeo;
    }

    public void setDnsIpGeo(String dnsIpGeo) {
        this.dnsIpGeo = dnsIpGeo;
    }

    public String getDnsIpIsp() {
        return dnsIpIsp;
    }

    public void setDnsIpIsp(String dnsIpIsp) {
        this.dnsIpIsp = dnsIpIsp;
    }

    public String getDnsIpOrganization() {
        return dnsIpOrganization;
    }

    public void setDnsIpOrganization(String dnsIpOrganization) {
        this.dnsIpOrganization = dnsIpOrganization;
    }

    public String getDnsIpPostalCode() {
        return dnsIpPostalCode;
    }

    public void setDnsIpPostalCode(String dnsIpPostalCode) {
        this.dnsIpPostalCode = dnsIpPostalCode;
    }

    public String getDnsIpRegion() {
        return dnsIpRegion;
    }

    public void setDnsIpRegion(String dnsIpRegion) {
        this.dnsIpRegion = dnsIpRegion;
    }

    public String getEmailageEmailriskscoreEaadvice() {
        return emailageEmailriskscoreEaadvice;
    }

    public void setEmailageEmailriskscoreEaadvice(String emailageEmailriskscoreEaadvice) {
        this.emailageEmailriskscoreEaadvice = emailageEmailriskscoreEaadvice;
    }

    public String getEmailageEmailriskscoreEareason() {
        return emailageEmailriskscoreEareason;
    }

    public void setEmailageEmailriskscoreEareason(String emailageEmailriskscoreEareason) {
        this.emailageEmailriskscoreEareason = emailageEmailriskscoreEareason;
    }

    public Integer getEmailageEmailriskscoreEascore() {
        return emailageEmailriskscoreEascore;
    }

    public void setEmailageEmailriskscoreEascore(Integer emailageEmailriskscoreEascore) {
        this.emailageEmailriskscoreEascore = emailageEmailriskscoreEascore;
    }

    public Integer getEmailageEmailriskscoreEariskbandid() {
        return emailageEmailriskscoreEariskbandid;
    }

    public void setEmailageEmailriskscoreEariskbandid(Integer emailageEmailriskscoreEariskbandid) {
        this.emailageEmailriskscoreEariskbandid = emailageEmailriskscoreEariskbandid;
    }

    public Integer getEmailageEmailriskscoreEmailCreationDays() {
        return emailageEmailriskscoreEmailCreationDays;
    }

    public void setEmailageEmailriskscoreEmailCreationDays(Integer emailageEmailriskscoreEmailCreationDays) {
        this.emailageEmailriskscoreEmailCreationDays = emailageEmailriskscoreEmailCreationDays;
    }

    public String getEmailageEmailriskscoreEmailage() {
        return emailageEmailriskscoreEmailage;
    }

    public void setEmailageEmailriskscoreEmailage(String emailageEmailriskscoreEmailage) {
        this.emailageEmailriskscoreEmailage = emailageEmailriskscoreEmailage;
    }

    public String getEmailageEmailriskscorePhonecarriertype() {
        return emailageEmailriskscorePhonecarriertype;
    }

    public void setEmailageEmailriskscorePhonecarriertype(String emailageEmailriskscorePhonecarriertype) {
        this.emailageEmailriskscorePhonecarriertype = emailageEmailriskscorePhonecarriertype;
    }

    public String getEmailageEmailriskscorePhoneownermatch() {
        return emailageEmailriskscorePhoneownermatch;
    }

    public void setEmailageEmailriskscorePhoneownermatch(String emailageEmailriskscorePhoneownermatch) {
        this.emailageEmailriskscorePhoneownermatch = emailageEmailriskscorePhoneownermatch;
    }

    public Integer getEmailageEmailriskscorePhonetofullnameconfidence() {
        return emailageEmailriskscorePhonetofullnameconfidence;
    }

    public void setEmailageEmailriskscorePhonetofullnameconfidence(
            Integer emailageEmailriskscorePhonetofullnameconfidence) {
        this.emailageEmailriskscorePhonetofullnameconfidence = emailageEmailriskscorePhonetofullnameconfidence;
    }

    public Integer getEmailageEmailriskscorePhonetolastnameconfidence() {
        return emailageEmailriskscorePhonetolastnameconfidence;
    }

    public void setEmailageEmailriskscorePhonetolastnameconfidence(
            Integer emailageEmailriskscorePhonetolastnameconfidence) {
        this.emailageEmailriskscorePhonetolastnameconfidence = emailageEmailriskscorePhonetolastnameconfidence;
    }

    public String getEmailageEmailriskscoreIpRisklevel() {
        return emailageEmailriskscoreIpRisklevel;
    }

    public void setEmailageEmailriskscoreIpRisklevel(String emailageEmailriskscoreIpRisklevel) {
        this.emailageEmailriskscoreIpRisklevel = emailageEmailriskscoreIpRisklevel;
    }

    public String getEmailageEmailriskscoreIpRiskreason() {
        return emailageEmailriskscoreIpRiskreason;
    }

    public void setEmailageEmailriskscoreIpRiskreason(String emailageEmailriskscoreIpRiskreason) {
        this.emailageEmailriskscoreIpRiskreason = emailageEmailriskscoreIpRiskreason;
    }

    public String getEmailageEmailriskscore() {
        return emailageEmailriskscore;
    }

    public void setEmailageEmailriskscore(String emailageEmailriskscore) {
        this.emailageEmailriskscore = emailageEmailriskscore;
    }

    public String getEmailageEmailriskscoreDomainrisklevel() {
        return emailageEmailriskscoreDomainrisklevel;
    }

    public void setEmailageEmailriskscoreDomainrisklevel(String emailageEmailriskscoreDomainrisklevel) {
        this.emailageEmailriskscoreDomainrisklevel = emailageEmailriskscoreDomainrisklevel;
    }

    public Integer getEmailageEmailriskscoreDomainCreationDays() {
        return emailageEmailriskscoreDomainCreationDays;
    }

    public void setEmailageEmailriskscoreDomainCreationDays(Integer emailageEmailriskscoreDomainCreationDays) {
        this.emailageEmailriskscoreDomainCreationDays = emailageEmailriskscoreDomainCreationDays;
    }

    public String getEmailageEmailriskscoreDomainage() {
        return emailageEmailriskscoreDomainage;
    }

    public void setEmailageEmailriskscoreDomainage(String emailageEmailriskscoreDomainage) {
        this.emailageEmailriskscoreDomainage = emailageEmailriskscoreDomainage;
    }

    public String getEmailageEmailriskscoreDomainexists() {
        return emailageEmailriskscoreDomainexists;
    }

    public void setEmailageEmailriskscoreDomainexists(String emailageEmailriskscoreDomainexists) {
        this.emailageEmailriskscoreDomainexists = emailageEmailriskscoreDomainexists;
    }

    public String getEmailageEmailriskscoreDomaincountry() {
        return emailageEmailriskscoreDomaincountry;
    }

    public void setEmailageEmailriskscoreDomaincountry(String emailageEmailriskscoreDomaincountry) {
        this.emailageEmailriskscoreDomaincountry = emailageEmailriskscoreDomaincountry;
    }

    public String getEmailageEmailriskscoreDomaincompany() {
        return emailageEmailriskscoreDomaincompany;
    }

    public void setEmailageEmailriskscoreDomaincompany(String emailageEmailriskscoreDomaincompany) {
        this.emailageEmailriskscoreDomaincompany = emailageEmailriskscoreDomaincompany;
    }

    public String getEmailageEmailriskscoreDomainname() {
        return emailageEmailriskscoreDomainname;
    }

    public void setEmailageEmailriskscoreDomainname(String emailageEmailriskscoreDomainname) {
        this.emailageEmailriskscoreDomainname = emailageEmailriskscoreDomainname;
    }

    public String getEmailageEmailriskscoreDomaincategory() {
        return emailageEmailriskscoreDomaincategory;
    }

    public void setEmailageEmailriskscoreDomaincategory(String emailageEmailriskscoreDomaincategory) {
        this.emailageEmailriskscoreDomaincategory = emailageEmailriskscoreDomaincategory;
    }

    public Integer getEmailageEmailriskscoreOveralldigitalidentityscore() {
        return emailageEmailriskscoreOveralldigitalidentityscore;
    }

    public void setEmailageEmailriskscoreOveralldigitalidentityscore(
            Integer emailageEmailriskscoreOveralldigitalidentityscore) {
        this.emailageEmailriskscoreOveralldigitalidentityscore = emailageEmailriskscoreOveralldigitalidentityscore;
    }

    public String getEmailageEmailriskscoreDisdescription() {
        return emailageEmailriskscoreDisdescription;
    }

    public void setEmailageEmailriskscoreDisdescription(String emailageEmailriskscoreDisdescription) {
        this.emailageEmailriskscoreDisdescription = emailageEmailriskscoreDisdescription;
    }

    public String getEventDatetime() {
        return eventDatetime;
    }

    public void setEventDatetime(String eventDatetime) {
        this.eventDatetime = eventDatetime;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getFinalReviewStatus() {
        return finalReviewStatus;
    }

    public void setFinalReviewStatus(String finalReviewStatus) {
        this.finalReviewStatus = finalReviewStatus;
    }

    public String[] getFuzzyDeviceAttributes() {
        return fuzzyDeviceAttributes;
    }

    public void setFuzzyDeviceAttributes(String[] fuzzyDeviceAttributes) {
        this.fuzzyDeviceAttributes = fuzzyDeviceAttributes;
    }

    public String getFuzzyDeviceFirstSeen() {
        return fuzzyDeviceFirstSeen;
    }

    public void setFuzzyDeviceFirstSeen(String fuzzyDeviceFirstSeen) {
        this.fuzzyDeviceFirstSeen = fuzzyDeviceFirstSeen;
    }

    public String getFuzzyDeviceId() {
        return fuzzyDeviceId;
    }

    public void setFuzzyDeviceId(String fuzzyDeviceId) {
        this.fuzzyDeviceId = fuzzyDeviceId;
    }

    public Double getFuzzyDeviceIdConfidence() {
        return fuzzyDeviceIdConfidence;
    }

    public void setFuzzyDeviceIdConfidence(Double fuzzyDeviceIdConfidence) {
        this.fuzzyDeviceIdConfidence = fuzzyDeviceIdConfidence;
    }

    public String getFuzzyDeviceMatchResult() {
        return fuzzyDeviceMatchResult;
    }

    public void setFuzzyDeviceMatchResult(String fuzzyDeviceMatchResult) {
        this.fuzzyDeviceMatchResult = fuzzyDeviceMatchResult;
    }

    public String getFuzzyDeviceResult() {
        return fuzzyDeviceResult;
    }

    public void setFuzzyDeviceResult(String fuzzyDeviceResult) {
        this.fuzzyDeviceResult = fuzzyDeviceResult;
    }

    public Integer getFuzzyDeviceScore() {
        return fuzzyDeviceScore;
    }

    public void setFuzzyDeviceScore(Integer fuzzyDeviceScore) {
        this.fuzzyDeviceScore = fuzzyDeviceScore;
    }

    public Integer getFuzzyDeviceWorstScore() {
        return fuzzyDeviceWorstScore;
    }

    public void setFuzzyDeviceWorstScore(Integer fuzzyDeviceWorstScore) {
        this.fuzzyDeviceWorstScore = fuzzyDeviceWorstScore;
    }

    public String getGeofenceCountryGps() {
        return geofenceCountryGps;
    }

    public void setGeofenceCountryGps(String geofenceCountryGps) {
        this.geofenceCountryGps = geofenceCountryGps;
    }

    public String getGeofenceRegionGps() {
        return geofenceRegionGps;
    }

    public void setGeofenceRegionGps(String geofenceRegionGps) {
        this.geofenceRegionGps = geofenceRegionGps;
    }

    public String getGpsSpoof() {
        return gpsSpoof;
    }

    public void setGpsSpoof(String gpsSpoof) {
        this.gpsSpoof = gpsSpoof;
    }

    public String getGpuName() {
        return gpuName;
    }

    public void setGpuName(String gpuName) {
        this.gpuName = gpuName;
    }

    public String getGpuVendor() {
        return gpuVendor;
    }

    public void setGpuVendor(String gpuVendor) {
        this.gpuVendor = gpuVendor;
    }

    public String getHwFingerprint() {
        return hwFingerprint;
    }

    public void setHwFingerprint(String hwFingerprint) {
        this.hwFingerprint = hwFingerprint;
    }

    public Integer getJbRoot() {
        return jbRoot;
    }

    public void setJbRoot(Integer jbRoot) {
        this.jbRoot = jbRoot;
    }

    public String getJsBrowser() {
        return jsBrowser;
    }

    public void setJsBrowser(String jsBrowser) {
        this.jsBrowser = jsBrowser;
    }

    public String getJsBrowserStringHash() {
        return jsBrowserStringHash;
    }

    public void setJsBrowserStringHash(String jsBrowserStringHash) {
        this.jsBrowserStringHash = jsBrowserStringHash;
    }

    public String getJsFontsHash() {
        return jsFontsHash;
    }

    public void setJsFontsHash(String jsFontsHash) {
        this.jsFontsHash = jsFontsHash;
    }

    public Integer getJsFontsNumber() {
        return jsFontsNumber;
    }

    public void setJsFontsNumber(Integer jsFontsNumber) {
        this.jsFontsNumber = jsFontsNumber;
    }

    public String getJsOs() {
        return jsOs;
    }

    public void setJsOs(String jsOs) {
        this.jsOs = jsOs;
    }

    public String getLiveness() {
        return liveness;
    }

    public void setLiveness(String liveness) {
        this.liveness = liveness;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getMathRoutine() {
        return mathRoutine;
    }

    public void setMathRoutine(String mathRoutine) {
        this.mathRoutine = mathRoutine;
    }

    public String getMimeTypeHash() {
        return mimeTypeHash;
    }

    public void setMimeTypeHash(String mimeTypeHash) {
        this.mimeTypeHash = mimeTypeHash;
    }

    public String getMultiDisplay() {
        return multiDisplay;
    }

    public void setMultiDisplay(String multiDisplay) {
        this.multiDisplay = multiDisplay;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordHashActivities() {
        return passwordHashActivities;
    }

    public void setPasswordHashActivities(String passwordHashActivities) {
        this.passwordHashActivities = passwordHashActivities;
    }

    public String[] getPasswordHashAttributes() {
        return passwordHashAttributes;
    }

    public void setPasswordHashAttributes(String[] passwordHashAttributes) {
        this.passwordHashAttributes = passwordHashAttributes;
    }

    public String getPasswordHashFirstSeen() {
        return passwordHashFirstSeen;
    }

    public void setPasswordHashFirstSeen(String passwordHashFirstSeen) {
        this.passwordHashFirstSeen = passwordHashFirstSeen;
    }

    public String getPasswordHashResult() {
        return passwordHashResult;
    }

    public void setPasswordHashResult(String passwordHashResult) {
        this.passwordHashResult = passwordHashResult;
    }

    public Integer getPasswordHashScore() {
        return passwordHashScore;
    }

    public void setPasswordHashScore(Integer passwordHashScore) {
        this.passwordHashScore = passwordHashScore;
    }

    public Integer getPasswordHashWorstScore() {
        return passwordHashWorstScore;
    }

    public void setPasswordHashWorstScore(Integer passwordHashWorstScore) {
        this.passwordHashWorstScore = passwordHashWorstScore;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getPrivateBrowsing() {
        return privateBrowsing;
    }

    public void setPrivateBrowsing(String privateBrowsing) {
        this.privateBrowsing = privateBrowsing;
    }

    public String getProxyIp() {
        return proxyIp;
    }

    public void setProxyIp(String proxyIp) {
        this.proxyIp = proxyIp;
    }

    public String getProxyIpActivities() {
        return proxyIpActivities;
    }

    public void setProxyIpActivities(String proxyIpActivities) {
        this.proxyIpActivities = proxyIpActivities;
    }

    public String[] getProxyIpAttributes() {
        return proxyIpAttributes;
    }

    public void setProxyIpAttributes(String[] proxyIpAttributes) {
        this.proxyIpAttributes = proxyIpAttributes;
    }

    public String getProxyIpCity() {
        return proxyIpCity;
    }

    public void setProxyIpCity(String proxyIpCity) {
        this.proxyIpCity = proxyIpCity;
    }

    public String getProxyIpConnectionType() {
        return proxyIpConnectionType;
    }

    public void setProxyIpConnectionType(String proxyIpConnectionType) {
        this.proxyIpConnectionType = proxyIpConnectionType;
    }

    public String getProxyIpFirstSeen() {
        return proxyIpFirstSeen;
    }

    public void setProxyIpFirstSeen(String proxyIpFirstSeen) {
        this.proxyIpFirstSeen = proxyIpFirstSeen;
    }

    public String getProxyIpGeo() {
        return proxyIpGeo;
    }

    public void setProxyIpGeo(String proxyIpGeo) {
        this.proxyIpGeo = proxyIpGeo;
    }

    public String getProxyIpHome() {
        return proxyIpHome;
    }

    public void setProxyIpHome(String proxyIpHome) {
        this.proxyIpHome = proxyIpHome;
    }

    public String getProxyIpIsp() {
        return proxyIpIsp;
    }

    public void setProxyIpIsp(String proxyIpIsp) {
        this.proxyIpIsp = proxyIpIsp;
    }

    public Double getProxyIpLatitude() {
        return proxyIpLatitude;
    }

    public void setProxyIpLatitude(Double proxyIpLatitude) {
        this.proxyIpLatitude = proxyIpLatitude;
    }

    public Double getProxyIpLongitude() {
        return proxyIpLongitude;
    }

    public void setProxyIpLongitude(Double proxyIpLongitude) {
        this.proxyIpLongitude = proxyIpLongitude;
    }

    public String getProxyIpOrganization() {
        return proxyIpOrganization;
    }

    public void setProxyIpOrganization(String proxyIpOrganization) {
        this.proxyIpOrganization = proxyIpOrganization;
    }

    public String getProxyIpOrganizationType() {
        return proxyIpOrganizationType;
    }

    public void setProxyIpOrganizationType(String proxyIpOrganizationType) {
        this.proxyIpOrganizationType = proxyIpOrganizationType;
    }

    public String getProxyIpPostalCode() {
        return proxyIpPostalCode;
    }

    public void setProxyIpPostalCode(String proxyIpPostalCode) {
        this.proxyIpPostalCode = proxyIpPostalCode;
    }

    public String getProxyIpRegion() {
        return proxyIpRegion;
    }

    public void setProxyIpRegion(String proxyIpRegion) {
        this.proxyIpRegion = proxyIpRegion;
    }

    public String getProxyIpResult() {
        return proxyIpResult;
    }

    public void setProxyIpResult(String proxyIpResult) {
        this.proxyIpResult = proxyIpResult;
    }

    public String getProxyIpRoutingType() {
        return proxyIpRoutingType;
    }

    public void setProxyIpRoutingType(String proxyIpRoutingType) {
        this.proxyIpRoutingType = proxyIpRoutingType;
    }

    public Integer getProxyIpScore() {
        return proxyIpScore;
    }

    public void setProxyIpScore(Integer proxyIpScore) {
        this.proxyIpScore = proxyIpScore;
    }

    public Integer getProxyIpWorstScore() {
        return proxyIpWorstScore;
    }

    public void setProxyIpWorstScore(Integer proxyIpWorstScore) {
        this.proxyIpWorstScore = proxyIpWorstScore;
    }

    public String getProxyIpv6() {
        return proxyIpv6;
    }

    public void setProxyIpv6(String proxyIpv6) {
        this.proxyIpv6 = proxyIpv6;
    }

    public String getProxyName() {
        return proxyName;
    }

    public void setProxyName(String proxyName) {
        this.proxyName = proxyName;
    }

    public Double getProxyScore() {
        return proxyScore;
    }

    public void setProxyScore(Double proxyScore) {
        this.proxyScore = proxyScore;
    }

    public String getProxyType() {
        return proxyType;
    }

    public void setProxyType(String proxyType) {
        this.proxyType = proxyType;
    }

    public String getRelatedRequestId() {
        return relatedRequestId;
    }

    public void setRelatedRequestId(String relatedRequestId) {
        this.relatedRequestId = relatedRequestId;
    }

    public String getRemoteAccess() {
        return remoteAccess;
    }

    public void setRemoteAccess(String remoteAccess) {
        this.remoteAccess = remoteAccess;
    }

    public String getRemoteAccessRating() {
        return remoteAccessRating;
    }

    public void setRemoteAccessRating(String remoteAccessRating) {
        this.remoteAccessRating = remoteAccessRating;
    }

    public Integer getRemoteAccessScore() {
        return remoteAccessScore;
    }

    public void setRemoteAccessScore(Integer remoteAccessScore) {
        this.remoteAccessScore = remoteAccessScore;
    }

    public String getRemoteDesktop() {
        return remoteDesktop;
    }

    public void setRemoteDesktop(String remoteDesktop) {
        this.remoteDesktop = remoteDesktop;
    }

    public String getScreenRes() {
        return screenRes;
    }

    public void setScreenRes(String screenRes) {
        this.screenRes = screenRes;
    }

    public String getScreenResAnomaly() {
        return screenResAnomaly;
    }

    public void setScreenResAnomaly(String screenResAnomaly) {
        this.screenResAnomaly = screenResAnomaly;
    }

    public Double getScreenResZoom() {
        return screenResZoom;
    }

    public void setScreenResZoom(Double screenResZoom) {
        this.screenResZoom = screenResZoom;
    }

    public String getSeRating() {
        return seRating;
    }

    public void setSeRating(String seRating) {
        this.seRating = seRating;
    }

    public Double getSeScore() {
        return seScore;
    }

    public void setSeScore(Double seScore) {
        this.seScore = seScore;
    }

    public String getSmartLearningFraudRating() {
        return smartLearningFraudRating;
    }

    public void setSmartLearningFraudRating(String smartLearningFraudRating) {
        this.smartLearningFraudRating = smartLearningFraudRating;
    }

    public Double getSmartLearningPScore() {
        return smartLearningPScore;
    }

    public void setSmartLearningPScore(Double smartLearningPScore) {
        this.smartLearningPScore = smartLearningPScore;
    }

    public Integer getSmartLearningPolicyScore() {
        return smartLearningPolicyScore;
    }

    public void setSmartLearningPolicyScore(Integer smartLearningPolicyScore) {
        this.smartLearningPolicyScore = smartLearningPolicyScore;
    }

    public String getSmartLearningReasonCode() {
        return smartLearningReasonCode;
    }

    public void setSmartLearningReasonCode(String smartLearningReasonCode) {
        this.smartLearningReasonCode = smartLearningReasonCode;
    }

    public Double getSmartLearningRiskRank() {
        return smartLearningRiskRank;
    }

    public void setSmartLearningRiskRank(Double smartLearningRiskRank) {
        this.smartLearningRiskRank = smartLearningRiskRank;
    }

    public String getSmartLearningSummaryReasonCode() {
        return smartLearningSummaryReasonCode;
    }

    public void setSmartLearningSummaryReasonCode(String smartLearningSummaryReasonCode) {
        this.smartLearningSummaryReasonCode = smartLearningSummaryReasonCode;
    }

    public Integer getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(Integer timeZone) {
        this.timeZone = timeZone;
    }

    public Integer getTimeZoneDstOffset() {
        return timeZoneDstOffset;
    }

    public void setTimeZoneDstOffset(Integer timeZoneDstOffset) {
        this.timeZoneDstOffset = timeZoneDstOffset;
    }

    public String getTimezoneName() {
        return timezoneName;
    }

    public void setTimezoneName(String timezoneName) {
        this.timezoneName = timezoneName;
    }

    public String getTimezoneOffsetAnomaly() {
        return timezoneOffsetAnomaly;
    }

    public void setTimezoneOffsetAnomaly(String timezoneOffsetAnomaly) {
        this.timezoneOffsetAnomaly = timezoneOffsetAnomaly;
    }

    public Integer getTmxPolicyScore() {
        return tmxPolicyScore;
    }

    public void setTmxPolicyScore(Integer tmxPolicyScore) {
        this.tmxPolicyScore = tmxPolicyScore;
    }

    public String getTmxRiskRating() {
        return tmxRiskRating;
    }

    public void setTmxRiskRating(String tmxRiskRating) {
        this.tmxRiskRating = tmxRiskRating;
    }

    public String getTmxVariables() {
        return tmxVariables;
    }

    public void setTmxVariables(String tmxVariables) {
        this.tmxVariables = tmxVariables;
    }

    public String getTrueIp() {
        return trueIp;
    }

    public void setTrueIp(String trueIp) {
        this.trueIp = trueIp;
    }

    public String getTrueIpActivities() {
        return trueIpActivities;
    }

    public void setTrueIpActivities(String trueIpActivities) {
        this.trueIpActivities = trueIpActivities;
    }

    public String[] getTrueIpAttributes() {
        return trueIpAttributes;
    }

    public void setTrueIpAttributes(String[] trueIpAttributes) {
        this.trueIpAttributes = trueIpAttributes;
    }

    public String getTrueIpCity() {
        return trueIpCity;
    }

    public void setTrueIpCity(String trueIpCity) {
        this.trueIpCity = trueIpCity;
    }

    public Integer getTrueIpCountryConfidence() {
        return trueIpCountryConfidence;
    }

    public void setTrueIpCountryConfidence(Integer trueIpCountryConfidence) {
        this.trueIpCountryConfidence = trueIpCountryConfidence;
    }

    public String getTrueIpFirstSeen() {
        return trueIpFirstSeen;
    }

    public void setTrueIpFirstSeen(String trueIpFirstSeen) {
        this.trueIpFirstSeen = trueIpFirstSeen;
    }

    public String getTrueIpGeo() {
        return trueIpGeo;
    }

    public void setTrueIpGeo(String trueIpGeo) {
        this.trueIpGeo = trueIpGeo;
    }

    public String getTrueIpIsp() {
        return trueIpIsp;
    }

    public void setTrueIpIsp(String trueIpIsp) {
        this.trueIpIsp = trueIpIsp;
    }

    public String getTrueIpLastEvent() {
        return trueIpLastEvent;
    }

    public void setTrueIpLastEvent(String trueIpLastEvent) {
        this.trueIpLastEvent = trueIpLastEvent;
    }

    public String getTrueIpOrganization() {
        return trueIpOrganization;
    }

    public void setTrueIpOrganization(String trueIpOrganization) {
        this.trueIpOrganization = trueIpOrganization;
    }

    public String getTrueIpOrganizationType() {
        return trueIpOrganizationType;
    }

    public void setTrueIpOrganizationType(String trueIpOrganizationType) {
        this.trueIpOrganizationType = trueIpOrganizationType;
    }

    public String getTrueIpPostalCode() {
        return trueIpPostalCode;
    }

    public void setTrueIpPostalCode(String trueIpPostalCode) {
        this.trueIpPostalCode = trueIpPostalCode;
    }

    public String getTrueIpRegion() {
        return trueIpRegion;
    }

    public void setTrueIpRegion(String trueIpRegion) {
        this.trueIpRegion = trueIpRegion;
    }

    public String getTrueIpResult() {
        return trueIpResult;
    }

    public void setTrueIpResult(String trueIpResult) {
        this.trueIpResult = trueIpResult;
    }

    public String getTrueIpConnectionType() {
        return trueIpConnectionType;
    }

    public void setTrueIpConnectionType(String trueIpConnectionType) {
        this.trueIpConnectionType = trueIpConnectionType;
    }

    public String getTrueIpRoutingType() {
        return trueIpRoutingType;
    }

    public void setTrueIpRoutingType(String trueIpRoutingType) {
        this.trueIpRoutingType = trueIpRoutingType;
    }

    public Integer getTrueIpScore() {
        return trueIpScore;
    }

    public void setTrueIpScore(Integer trueIpScore) {
        this.trueIpScore = trueIpScore;
    }

    public Integer getTrueIpWorstScore() {
        return trueIpWorstScore;
    }

    public void setTrueIpWorstScore(Integer trueIpWorstScore) {
        this.trueIpWorstScore = trueIpWorstScore;
    }

    public String getTrueIpv6() {
        return trueIpv6;
    }

    public void setTrueIpv6(String trueIpv6) {
        this.trueIpv6 = trueIpv6;
    }

    public String getUaAgent() {
        return uaAgent;
    }

    public void setUaAgent(String uaAgent) {
        this.uaAgent = uaAgent;
    }

    public String getUaMobile() {
        return uaMobile;
    }

    public void setUaMobile(String uaMobile) {
        this.uaMobile = uaMobile;
    }

    public String getUnknownSession() {
        return unknownSession;
    }

    public void setUnknownSession(String unknownSession) {
        this.unknownSession = unknownSession;
    }

    public Integer getVirtualDevice() {
        return virtualDevice;
    }

    public void setVirtualDevice(Integer virtualDevice) {
        this.virtualDevice = virtualDevice;
    }

    public String getVirtualDeviceReason() {
        return virtualDeviceReason;
    }

    public void setVirtualDeviceReason(String virtualDeviceReason) {
        this.virtualDeviceReason = virtualDeviceReason;
    }

    public String getVpnReason() {
        return vpnReason;
    }

    public void setVpnReason(String vpnReason) {
        this.vpnReason = vpnReason;
    }

    public Integer getVpnScore() {
        return vpnScore;
    }

    public void setVpnScore(Integer vpnScore) {
        this.vpnScore = vpnScore;
    }

    public String getWebglHash() {
        return webglHash;
    }

    public void setWebglHash(String webglHash) {
        this.webglHash = webglHash;
    }

    public String getWebrtcExternalIp() {
        return webrtcExternalIp;
    }

    public void setWebrtcExternalIp(String webrtcExternalIp) {
        this.webrtcExternalIp = webrtcExternalIp;
    }

    public String getCustomerEventType() {
        return customerEventType;
    }

    public void setCustomerEventType(String customerEventType) {
        this.customerEventType = customerEventType;
    }

    public String getInputIpAddress() {
        return inputIpAddress;
    }

    public void setInputIpAddress(String inputIpAddress) {
        this.inputIpAddress = inputIpAddress;
    }

    public String getInputIpIsp() {
        return inputIpIsp;
    }

    public void setInputIpIsp(String inputIpIsp) {
        this.inputIpIsp = inputIpIsp;
    }

    public String getInputIpCity() {
        return inputIpCity;
    }

    public void setInputIpCity(String inputIpCity) {
        this.inputIpCity = inputIpCity;
    }

    public String getInputIpRegion() {
        return inputIpRegion;
    }

    public void setInputIpRegion(String inputIpRegion) {
        this.inputIpRegion = inputIpRegion;
    }

    public String getInputIpGeo() {
        return inputIpGeo;
    }

    public void setInputIpGeo(String inputIpGeo) {
        this.inputIpGeo = inputIpGeo;
    }

    public String getInputIpRoutingType() {
        return inputIpRoutingType;
    }

    public void setInputIpRoutingType(String inputIpRoutingType) {
        this.inputIpRoutingType = inputIpRoutingType;
    }

    public String getWebSessionId() {
        return webSessionId;
    }

    public void setWebSessionId(String webSessionId) {
        this.webSessionId = webSessionId;
    }

    public String getConditionAttrib5() {
        return conditionAttrib5;
    }

    public void setConditionAttrib5(String conditionAttrib5) {
        this.conditionAttrib5 = conditionAttrib5;
    }

    public String getAgentBrand() {
        return agentBrand;
    }

    public void setAgentBrand(String agentBrand) {
        this.agentBrand = agentBrand;
    }

    public String getAgentModel() {
        return agentModel;
    }

    public void setAgentModel(String agentModel) {
        this.agentModel = agentModel;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getLineOfBusiness() {
        return lineOfBusiness;
    }

    public void setLineOfBusiness(String lineOfBusiness) {
        this.lineOfBusiness = lineOfBusiness;
    }

    public String getAccountAddressStreet2() {
        return accountAddressStreet2;
    }

    public void setAccountAddressStreet2(String accountAddressStreet2) {
        this.accountAddressStreet2 = accountAddressStreet2;
    }

    public String getConditionAttrib1() {
        return conditionAttrib1;
    }

    public void setConditionAttrib1(String conditionAttrib1) {
        this.conditionAttrib1 = conditionAttrib1;
    }

    public String getConditionAttrib2() {
        return conditionAttrib2;
    }

    public void setConditionAttrib2(String conditionAttrib2) {
        this.conditionAttrib2 = conditionAttrib2;
    }

    public Double getCustomCount1() {
        return customCount1;
    }

    public void setCustomCount1(Double customCount1) {
        this.customCount1 = customCount1;
    }

    public String getLocalAttrib1() {
        return localAttrib1;
    }

    public void setLocalAttrib1(String localAttrib1) {
        this.localAttrib1 = localAttrib1;
    }

    public String getCustomCount4() {
        return customCount4;
    }

    public void setCustomCount4(String customCount4) {
        this.customCount4 = customCount4;
    }

    public String getConditionAttrib6() {
        return conditionAttrib6;
    }

    public void setConditionAttrib6(String conditionAttrib6) {
        this.conditionAttrib6 = conditionAttrib6;
    }

    public String getConditionAttrib3() {
        return conditionAttrib3;
    }

    public void setConditionAttrib3(String conditionAttrib3) {
        this.conditionAttrib3 = conditionAttrib3;
    }

    public Double getPrimaryAccountBalanceUsd() {
        return primaryAccountBalanceUsd;
    }

    public void setPrimaryAccountBalanceUsd(Double primaryAccountBalanceUsd) {
        this.primaryAccountBalanceUsd = primaryAccountBalanceUsd;
    }

    public Double getPrimaryAccountBalance() {
        return primaryAccountBalance;
    }

    public void setPrimaryAccountBalance(Double primaryAccountBalance) {
        this.primaryAccountBalance = primaryAccountBalance;
    }

    public String getPrimaryAccountBalanceCurrency() {
        return primaryAccountBalanceCurrency;
    }

    public void setPrimaryAccountBalanceCurrency(String primaryAccountBalanceCurrency) {
        this.primaryAccountBalanceCurrency = primaryAccountBalanceCurrency;
    }

    public String getConditionAttrib10() {
        return conditionAttrib10;
    }

    public void setConditionAttrib10(String conditionAttrib10) {
        this.conditionAttrib10 = conditionAttrib10;
    }

    public Double getConditionAttrib9() {
        return conditionAttrib9;
    }

    public void setConditionAttrib9(Double conditionAttrib9) {
        this.conditionAttrib9 = conditionAttrib9;
    }

    public String getConditionAttrib4() {
        return conditionAttrib4;
    }

    public void setConditionAttrib4(String conditionAttrib4) {
        this.conditionAttrib4 = conditionAttrib4;
    }

    public String getConditionAttrib7() {
        return conditionAttrib7;
    }

    public void setConditionAttrib7(String conditionAttrib7) {
        this.conditionAttrib7 = conditionAttrib7;
    }

    public String getConditionAttrib8() {
        return conditionAttrib8;
    }

    public void setConditionAttrib8(String conditionAttrib8) {
        this.conditionAttrib8 = conditionAttrib8;
    }

    public String getLocalAttrib12() {
        return localAttrib12;
    }

    public void setLocalAttrib12(String localAttrib12) {
        this.localAttrib12 = localAttrib12;
    }

    public String getLocalAttrib13() {
        return localAttrib13;
    }

    public void setLocalAttrib13(String localAttrib13) {
        this.localAttrib13 = localAttrib13;
    }

    public String getLocalAttrib14() {
        return localAttrib14;
    }

    public void setLocalAttrib14(String localAttrib14) {
        this.localAttrib14 = localAttrib14;
    }

    public String getLocalAttrib15() {
        return localAttrib15;
    }

    public void setLocalAttrib15(String localAttrib15) {
        this.localAttrib15 = localAttrib15;
    }

    public String getCustomCount2() {
        return customCount2;
    }

    public void setCustomCount2(String customCount2) {
        this.customCount2 = customCount2;
    }

    public String getCustomCount13() {
        return customCount13;
    }

    public void setCustomCount13(String customCount13) {
        this.customCount13 = customCount13;
    }

    public String getSummaryReasonCode() {
        return summaryReasonCode;
    }

    public void setSummaryReasonCode(String summaryReasonCode) {
        this.summaryReasonCode = summaryReasonCode;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getTmxSummaryReasonCode() {
        return tmxSummaryReasonCode;
    }

    public void setTmxSummaryReasonCode(String tmxSummaryReasonCode) {
        this.tmxSummaryReasonCode = tmxSummaryReasonCode;
    }
}