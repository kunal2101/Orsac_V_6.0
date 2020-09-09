package orsac.rosmerta.orsac_vehicle.android;

/**
 * Created by Diwash Choudhary on 2/23/2017.
 */
public class UrlContants  {

    public static final String BASE_URL1="http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/";
    public static final String BASE_URL = "http://vts.orissaminerals.gov.in/getweb/rest/webservice/";
    public  static  final  String LOGIN_ORSCA="http://vts.orissaminerals.gov.in/andorsac/rest/CallService/login1?username=";
    public  static  final  String LOGIN=BASE_URL+ "Login";

    public static final String     VERIFY_OTP = "otpvarify";
    public static final String     DASHBORAD =BASE_URL+"getcompanydata?userid=10001";
    public static final String     TRIP_CONT_ETP ="http://vts.orissaminerals.gov.in/orsacweb/rest/CallService/companywiseetp?companyname=";
    public static final String     CIRCLE_DETAIL ="http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/circlewiseTrip";
    public static final String BASE_URL_NRDA = "http://61.0.175.99/nrdamobileservice/mobileservice.asmx/getAllShelterDetails";

    public static final String STATIC_DYNAMIC = "http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/getvehiclecategorization?dataRange=0";
    public static final String STATIC_DYNAMIC_detail = "http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/getvehiclecategorizationbycompany?companyname=";

    // http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/omvtslogin?u=admin1&p=admin1@0011

    public  static  final  String LOGIN1    =   BASE_URL1+ "omvtslogin";
/////////////////////////////////////////////////////////////////////


   // public static final String BASE_URL_CLOUD="";
 //http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/

  //  getUrls
public static final String BASE_URL_OMVTS="https://vtscloud.co.in/rtlorsacandroidcloud/rest/CallService/";
  //  https://vtscloud.co.in/rtlorsacandroidcloud/rest/CallService/changeUrl
    public static final String     GET_MOB_DASH_DATA = "getMobDashDataNew";
    public static final String     OMVTS_LOGIN = "omvtslogin";
    public static final String     GET_MOB_DASH_DATA_NEW = "getMobDashDataNew";
    public static final String     GET_MOB_DASH_DATA_NAVI = "getMobDashData";
    public static final String     GET_MOB_DASH_DATA_SPECIFIC = "getMobDashDataSpecific";
    public static final String     ETP_MOB_DASH_DATA_SPECIFIC = "etptriplivesKunal";
    public static final String     GET_BG_DETAIL =  "getBgData";
    public static final String     GET_BG_STATUS_UPDATE =  "bgstatusEnableFromMobile";
    public static final String     GET_BG_AMOUNT_UPDATE =  "updateBgData";
    public static final String     GET_CHANGEURLE =  "changeUrl";

    //http://vts.orissaminerals.gov.in/rtlorsacandroid/rest/CallService/updateBgData?loginid=10002&bgamount=3750000&vehicleallowed=15000





}
