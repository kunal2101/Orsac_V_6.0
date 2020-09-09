package orsac.rosmerta.orsac_vehicle.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

@SuppressLint("CommitPrefEdits")
public class PreferenceHelper {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME       = "rtl_vts_application";

    // All Shared Preferences Keys
    private static final String IS_CREATED      = "isCreateIn";

    public static final String USER_ID          = "user_id";
    public static final String USER_NAME        = "user_name";
    public static final String USER_PASS        = "user_pass";
    public static final String OTP              = "otp";
    public static final String USER_EMAIL       = "user_email";
    public static final String USER_MOBILE      = "user_mobile_number";
    public static final String USER_ALTERNATE   = "user_alternate_number";
    public static final String USER_ADDRESS     = "user_address";

    public static final String USER_TYPE        = "user_type";
    public static final String ORG_ID           = "org_id";
    public static final String CLIENT_ID        = "client_id";
    /*public static final String OWNER_ID         = "ownersid";
    public static final String CONTACT_PERSON   = "contactperson";*/

    public static final String CHANGEURL        = "changeurl";

    public static final String APP_INSTALL      = "app_install";

    // Constructor
    public PreferenceHelper(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    //2.
    //store user_id
    public void putUser_id(String user_id){
        editor.putBoolean(IS_CREATED, true);
        editor.putString(USER_ID, user_id);
        editor.commit();
    }

    //get user_id
    public String getUser_id(){
        return pref.getString(USER_ID, "7827026380");
    }

    public void putOtp(String otp){
        editor.putString(OTP, otp);
        editor.commit();
    }
    public String getOtp(){
        return pref.getString(OTP, "null");
    }


    public void putUsername(String user_name){
        editor.putBoolean(IS_CREATED, true);
        editor.putString(USER_NAME, user_name);
        editor.commit();
    }
    public String getUsername(){
        return pref.getString(USER_NAME, null);

    }

    public String getUserPass(){
        return pref.getString(USER_PASS, null);
    }

    public void putUserpass(String pass){
        editor.putBoolean(IS_CREATED, true);
        editor.putString(USER_PASS, pass);
        editor.commit();
    }

    public void putUseremail(String user_email){
        editor.putBoolean(IS_CREATED, true);
        editor.putString(USER_EMAIL, user_email);
        editor.commit();
    }
    public String getUseremail(){
        return pref.getString(USER_EMAIL, null);
    }

    public void putUsermobile(String user_mobile_number){
        editor.putBoolean(IS_CREATED, true);
        editor.putString(USER_MOBILE, user_mobile_number);
        editor.commit();
    }
    public String getUsermobile(){
        return pref.getString(USER_MOBILE, null);
    }

    public void putUseralternatemobile(String user_alternate_number){
        editor.putBoolean(IS_CREATED, true);
        editor.putString(USER_ALTERNATE, user_alternate_number);
        editor.commit();
    }
    public String getUseralternatemobile(){
        return pref.getString(USER_ALTERNATE, null);
    }

    public void putUseraddress(String user_address){
        editor.putBoolean(IS_CREATED, true);
        editor.putString(USER_ADDRESS, user_address);
        editor.commit();
    }
    public String getUseraddress(){
        return pref.getString(USER_ADDRESS, null);
    }
    public void putUrls(String url){
        editor.putString(CHANGEURL, url);
        editor.commit();
    }
    public String getUrls(){
        return pref.getString(CHANGEURL, "null");
    }


    public void putUsertype(String user_type){
        editor.putBoolean(IS_CREATED, true);
        editor.putString(USER_TYPE, user_type);
        editor.commit();
    }

    public String getUsertype(){
        return pref.getString(USER_TYPE, null);
    }

    public void putOrgID(String orgid){
        editor.putBoolean(IS_CREATED, true);
        editor.putString(ORG_ID, orgid);
        editor.commit();
    }
    public String getOrgID(){
        return pref.getString(ORG_ID, null);
    }

    public void putClientID(String clientid){
        editor.putBoolean(IS_CREATED, true);
        editor.putString(CLIENT_ID, clientid);
        editor.commit();
    }

    public String getClientID(){
        return pref.getString(CLIENT_ID, null);
    }
    public void putAppInstall(int count){
        editor.putBoolean(IS_CREATED, true);
        editor.putInt(APP_INSTALL, count);
        editor.commit();
    }

    //get user_id
    public int getAppInstall(){
        return pref.getInt(APP_INSTALL, 0);
    }


    /*public void putUserownersid(String ownersid){
        editor.putBoolean(IS_CREATED, true);
        editor.putString(OWNER_ID, ownersid);
        editor.commit();
    }
    public String getUserownersid(){
        return pref.getString(OWNER_ID, null);
    }

    public void putContactperson(String contactperson){
        editor.putBoolean(IS_CREATED, true);
        editor.putString(CONTACT_PERSON, contactperson);
        editor.commit();
    }
    public String getContactperson(){
        return pref.getString(CONTACT_PERSON, null);
    }*/

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public boolean isCreated(){
        if(this.iscreated()){
            return true;
        }
		return false;
    }
    /** */

    /** Clear session details*/
    public void distorePreferenceHelper(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }
     
    /**Quick check for login* **/
    // Get session State
    public boolean iscreated(){
        return pref.getBoolean(IS_CREATED, false);
    }


}