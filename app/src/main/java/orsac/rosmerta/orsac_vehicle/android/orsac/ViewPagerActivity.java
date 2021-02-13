package orsac.rosmerta.orsac_vehicle.android.orsac;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;

import orsac.rosmerta.orsac_vehicle.android.R;

public class ViewPagerActivity extends AppCompatActivity {
    private TextView tool_title;
    private ImageView tool_back_icon;
    private LinearLayout dotsLayout;
    ArrayList<HashMap<String, String>> dataList;
    ArrayList<String> dataTitleList;
    String[] circlename = {"JODA", "KOIRA", "BALANGIR", "BARIPADA", "BERHAMPUR", "BARGARH", "KALAHANDI", "CUTTACK", "JAJPUR ROAD",
            "JHARSUGUDA", "KEONJHAR", "KORAPUT& RAYAGADA", "ROURKELA", "SAMBALPUR", "TALCHER"};


    ViewPagerActivity viewpager;
    private SpinnerAdaptor CustomPagerAdapter;
    private TextView[] dots;
    private int[] layouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view);

//        tool_title.setText("Circle DashBoard");
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });
        addBottomDots(0);
        //login_info();
    }

    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }
    private void addBottomDots(int currentPage) {
        dots = new TextView[4];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }
    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) ViewPagerActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            return false;
        }
    }

/*
    private void login_info() {
        String url = UrlContants.CIRCLE_DETAIL;

        //  String url = "http://209.190.15.26/androidorsac/circlewiseTrip";

        AndyUtils.showCustomProgressDialog(ViewPagerActivity.this, "Fetching Information Please Wait..", false, null);

        aQuery.progress(null);
        aQuery.ajax(url, null, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {

                try {
                    JSONArray jsonArray = object.getJSONArray("CirclewiseTrip");

                    dataList = new ArrayList<>();
                    dataTitleList = new ArrayList<>();
                    */
/*for (int aind = 0; aind < jsonArray.length(); aind++) {
                        JSONArray jsonArray1 = jsonArray.getJSONArray(aind);
                        HashMap<String,String> map = new HashMap<String, String>();
                        map.put("title", jsonArray1.getString(0).toString());
                        map.put("value", jsonArray1.getString(1).toString());

                        dataList.add(map);
                    }*//*




                        for (int ind = 0; ind < jsonArray.length(); ind++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(ind);
                            String names = jsonObject.getString("circleName");
                            String counts = jsonObject.getString("tripcount");
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("title", names);
                            map.put("value", counts);
                            dataList.add(map);
                            dataTitleList.add(names);
                        }
                        for (int ind = 0; ind < circlename.length; ind++) {
                            String getTitle = circlename[ind];

                            */
/*boolean isMatch = false;
                            for(int pqr = 0 ; pqr < dataList.size(); pqr ++){
                                HashMap<String,String> map = dataList.get(pqr);

                                if(map.get("title").equalsIgnoreCase(getTitle)){
                                    isMatch = true;
                                }
                            }

                            if (!isMatch) {
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("title", getTitle);
                                map.put("value", "0");
                                dataList.add(map);
                            }*//*


                            if(!dataTitleList.contains(getTitle)){
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("title", getTitle);
                                map.put("value", "0");
                                dataList.add(map);
                            }
                        }
                        CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(ViewPagerActivity.this, dataList);
                        android.support.v4.view.ViewPager viewPager = (android.support.v4.view.ViewPager) findViewById(R.id.viewpager);
                        viewPager.setAdapter(customPagerAdapter);
                        AndyUtils.removeCustomProgressDialog();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
*/
}
