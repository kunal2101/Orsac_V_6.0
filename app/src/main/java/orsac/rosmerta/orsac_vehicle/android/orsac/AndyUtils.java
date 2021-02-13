package orsac.rosmerta.orsac_vehicle.android.orsac;

/**
 * Created by Diwash Choudhary on 09-08-2016.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import orsac.rosmerta.orsac_vehicle.android.R;


@SuppressLint("NewApi")
public class AndyUtils {

    private static ProgressDialog mProgressDialog;
    private static Dialog mDialog;
    private static OnProgressCancelListener progressCancelListener;
private  static  int value;
    private static Context contexts;
    public static void showSimpleProgressDialog(Context context, String title,
                                                String msg, boolean isCancelable) {
        isCancelable = true;
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context, title, msg);
                mProgressDialog.setCancelable(isCancelable);
            }

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSimpleProgressDialog(Context context) {
        showSimpleProgressDialog(context, null, "Processing your request  Please Wait...", false);
    }



    public static void removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();

        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivity = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public static boolean isNetworkAvaliable(Context ctx) {
            ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if ((connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                    || (connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    .getState() == NetworkInfo.State.CONNECTED)) {
                return true;
            } else {
                return false;
            }
        }
    public static boolean eMailValidation(String emailstring) {
//        if (null == emailstring || emailstring.length() == 0) {
//            return false;
//        }
        Pattern emailPattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher emailMatcher = emailPattern.matcher(emailstring);
        return emailMatcher.matches();
    }

    //-------------------------Changes on 31-8-2015----------------------------------
    public static boolean mobNumberValidation(String phonestring) {
        if (null == phonestring || phonestring.length() == 0) {
            return false;
        }
        Pattern phonePattern = Pattern.compile("[0-9]{10}");
        Matcher phoneMatcher = phonePattern.matcher(phonestring);
        return phoneMatcher.matches();
    }




    public static void showToast(String msg, Context ctx) {
       // Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
        //Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
        Toast toast = Toast.makeText(
                //getActivity(),"Custom Toast From Fragment",Toast.LENGTH_LONG
                ctx, msg, Toast.LENGTH_LONG
        );
        // Set the Toast display position layout center
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
                0, 0);
        // Finally, show the toast

        toast.show();
    }

    public static String UppercaseFirstLetters(String str) {
        boolean prevWasWhiteSp = true;
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (Character.isLetter(chars[i])) {
                if (prevWasWhiteSp) {
                    chars[i] = Character.toUpperCase(chars[i]);
                }
                prevWasWhiteSp = false;
            } else {
                prevWasWhiteSp = Character.isWhitespace(chars[i]);
            }
        }
        return new String(chars);
    }



    public static final SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    }

    public static void showCustomProgressDialog(final Context context, String title, boolean iscancelable, OnProgressCancelListener progressCancelListener) {
       // iscancelable = true;
        if (mDialog != null && mDialog.isShowing())
            return;
        AndyUtils.progressCancelListener = progressCancelListener;

        mDialog = new Dialog(context, R.style.MyDialog);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setContentView(R.layout.progress_bar_cancel);

        ImageView imageView = (ImageView) mDialog.findViewById(R.id.ivProgressBar);
        if (!TextUtils.isEmpty(title)) {
            TextView tvTitle = (TextView) mDialog.findViewById(R.id.tvTitle);
            tvTitle.setText(title);
        }

        Button btnCancel = (Button) mDialog.findViewById(R.id.btnCancel);
        if (AndyUtils.progressCancelListener == null) {
            btnCancel.setVisibility(View.GONE);
        } else {
            btnCancel.setVisibility(View.VISIBLE);
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (AndyUtils.progressCancelListener != null)
                {
                    AndyUtils.progressCancelListener.onProgressCancel();

                    mDialog.dismiss();

                }

            }
        });
        imageView.setBackgroundResource(R.drawable.loading);
        final AnimationDrawable frameAnimation = (AnimationDrawable) imageView.getBackground();
        mDialog.setCancelable(iscancelable);
        imageView.post(new Runnable() {

            @Override
            public void run() {
                frameAnimation.start();
                frameAnimation.setOneShot(false);
            }
        });
        try {
            mDialog.show();

        }catch (Exception ev){
            ev.getMessage();
        }
    }

    public static void showCustomProgressDialog_New(final Context context, String title, boolean iscancelable, OnProgressCancelListener progressCancelListener,int value) {
        // iscancelable = true;
        final Activity mactivity = (Activity)context;
        if (mDialog != null && mDialog.isShowing())
            return;
        AndyUtils.progressCancelListener = progressCancelListener;

        mDialog = new Dialog(context, R.style.MyDialog);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setContentView(R.layout.progress_bar_cancel);

        ImageView imageView = (ImageView) mDialog.findViewById(R.id.ivProgressBar);
        if (!TextUtils.isEmpty(title)) {
            TextView tvTitle = (TextView) mDialog.findViewById(R.id.tvTitle);
            tvTitle.setText(title);
        }

        Button btnCancel = (Button) mDialog.findViewById(R.id.btnCancel);
        if (value == 0) {
            btnCancel.setVisibility(View.GONE);
        } else {
            btnCancel.setVisibility(View.VISIBLE);

        }
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
               /* if (AndyUtils.progressCancelListener != null)
                {*/
                  //  AndyUtils.progressCancelListener.onProgressCancel();
                 mDialog.dismiss();

                    mactivity.finish();

                //}

            }
        });
        imageView.setBackgroundResource(R.drawable.loading);
        final AnimationDrawable frameAnimation = (AnimationDrawable) imageView.getBackground();
        mDialog.setCancelable(iscancelable);
        imageView.post(new Runnable() {

            @Override
            public void run() {
                frameAnimation.start();
                frameAnimation.setOneShot(false);
            }
        });
        mDialog.show();
    }
/*
    public static void showCustomProgressDialog(Context context, String title, String details, boolean iscancelable) {
        iscancelable = true;
        removeCustomProgressDialog();
        mDialog = new Dialog(context, R.style.MyDialog);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        mDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setContentView(R.layout.dialog_progress);
        ImageView imageView = (ImageView) mDialog
                .findViewById(R.id.iv_dialog_progress);
        ((TextView) mDialog.findViewById(R.id.tv_dialog_title)).setText(title);
        ((TextView) mDialog.findViewById(R.id.tv_dialog_detail))
                .setText(details);
        imageView.setBackgroundResource(R.drawable.loading);
        final AnimationDrawable frameAnimation = (AnimationDrawable) imageView
                .getBackground();
        mDialog.setCancelable(iscancelable);
        imageView.post(new Runnable() {

            @Override
            public void run() {
                frameAnimation.start();
                frameAnimation.setOneShot(false);
            }
        });

        mDialog.show();
    }
*/

    public static void removeCustomProgressDialog() {
        try {
            AndyUtils.progressCancelListener = null;
            if (mDialog != null) {
                // if (mProgressDialog.isShowing()) {
                mDialog.dismiss();
                mDialog = null;
                // }
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();

        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
