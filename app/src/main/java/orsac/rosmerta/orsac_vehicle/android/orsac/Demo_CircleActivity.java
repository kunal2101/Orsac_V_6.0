package orsac.rosmerta.orsac_vehicle.android.orsac;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.NavigationActivity;
import orsac.rosmerta.orsac_vehicle.android.R;

public class Demo_CircleActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView tool_title;
    private ImageView tool_back_icon;
    private Myadapter mAdapter;
    Button btn_prevoius, btn_next;
    static List<CircelBean> array_admin, workingArr;
    CircelBean etaBean;
    private Toolbar tb;


    int startcounter = 0;
    String[] circlename = {"JODA", "KOIRA", "BALANGIR", "BARIPADA", "BERHAMPUR", "BARGARH", "KALAHANDI", "CUTTACK", "JAJPUR ROAD",
            "JHARSUGUDA", "KEONJHAR", "KORAPUT& RAYAGADA", "ROURKELA", "SAMBALPUR", "TALCHER"};

    GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orsac_circle_recy);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        tool_title = (TextView) findViewById(R.id.tool_title);
        btn_prevoius = (Button) findViewById(R.id.btn_prevoius);
        btn_next = (Button) findViewById(R.id.btn_next);
        tool_title.setText("Circle DashBoard");
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);

        gridview = (GridView) findViewById(R.id.gridview);

        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onbackClick();
            }
        });

        //login_info();
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                final CircelBean adminModel = workingArr.get(position);

                int etpno_cont = Integer.parseInt(adminModel.getNum());
                if (etpno_cont > 0) {
                    Intent intent = new Intent(Demo_CircleActivity.this, Orsac_circle_detail.class);
                    intent.putExtra("circle", adminModel.getTitle());
                    startActivity(intent);
                } else {
                    Toast.makeText(Demo_CircleActivity.this, "No Record available", Toast.LENGTH_LONG).show();
                }
            }
        });


        tool_title = (MyTextView) findViewById(R.id.tool_title);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbackClick();
            }
        });
        tool_title.setText(" Dashboard ");


        btn_prevoius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Toast.makeText(Demo_CircleActivity.this, "dfkwfhjk", Toast.LENGTH_LONG).show();
                //new nextPrevious().execute("prevoius");
                if (startcounter > array_admin.size()) {
                    workingArr = new ArrayList<CircelBean>();

                    int endcounter = array_admin.size();
                    if (array_admin.size() < 4) {
                        startcounter = 0;
                    } else {
                        startcounter = endcounter - 4;
                    }

                    for (int aind = startcounter; aind < endcounter; aind++) {
                        workingArr.add(array_admin.get(aind));
                    }
                   /* recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                    recyclerView.setLayoutManager(layoutManager);
                    ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplication(), R.dimen.item_offset);
                    recyclerView.addItemDecoration(itemDecoration);
                    mAdapter = new Myadapter();
                    recyclerView.setAdapter(mAdapter);*/

                    SpinnerAdaptor spinnerAdaptor = new SpinnerAdaptor(Demo_CircleActivity.this, workingArr, R.layout.circledashboard);
                    gridview.setAdapter(spinnerAdaptor);


                } else if (startcounter > 0) {
                    workingArr = new ArrayList<CircelBean>();

                    int endcounter = (startcounter - 4) < 1 ? 4 : (startcounter - 4);
                    startcounter = endcounter - 4;

                    for (int aind = startcounter; aind < endcounter; aind++) {
                        workingArr.add(array_admin.get(aind));
                    }
                   /* recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                    recyclerView.setLayoutManager(layoutManager);
                    ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplication(), R.dimen.item_offset);
                    recyclerView.addItemDecoration(itemDecoration);
                    mAdapter = new Myadapter();
                    recyclerView.setAdapter(mAdapter);*/

                    SpinnerAdaptor spinnerAdaptor = new SpinnerAdaptor(Demo_CircleActivity.this, workingArr, R.layout.circledashboard);
                    gridview.setAdapter(spinnerAdaptor);
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startcounter < array_admin.size()) {

                    int endcounter = (startcounter + 4) > array_admin.size() ? array_admin.size() : (startcounter + 4);

                    workingArr = new ArrayList<CircelBean>();
                    for (int aind = startcounter; aind < endcounter; aind++) {
                        workingArr.add(array_admin.get(aind));
                    }
                    /*recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                    recyclerView.setLayoutManager(layoutManager);
                    ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplication(), R.dimen.item_offset);
                    recyclerView.addItemDecoration(itemDecoration);
                    mAdapter = new Myadapter();
                    recyclerView.setAdapter(mAdapter);*/
                    SpinnerAdaptor spinnerAdaptor = new SpinnerAdaptor(Demo_CircleActivity.this, workingArr, R.layout.circledashboard);
                    gridview.setAdapter(spinnerAdaptor);

                    startcounter = (startcounter + 4);
                }
                // new nextPrevious().execute("next");
            }
        });

    }


    protected void onbackClick() {
        Intent myIntent = new Intent();
        setResult(RESULT_OK, myIntent);
        super.finish();
    }

    public class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder> {
        @Override
        public Myadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.circledashboard, null);
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(Myadapter.ViewHolder holder, int position) {
            final CircelBean adminModel = workingArr.get(position);

            holder.txtLabel.setText(adminModel.getTitle());
            holder.txtValue.setText(adminModel.getNum());
            holder.card_header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int etpno_cont = Integer.parseInt(adminModel.getNum());
                    if (etpno_cont > 0) {
                        Intent intent = new Intent(Demo_CircleActivity.this, Orsac_circle_detail.class);
                        intent.putExtra("circle", adminModel.getTitle());
                        startActivity(intent);
                    } else {
                        Toast.makeText(Demo_CircleActivity.this, "No Record available", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return workingArr.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            protected TextView txtValue, txtLabel;
            protected CardView card_header;
            protected ImageView img;

            public ViewHolder(final View itemLayputView) {
                super(itemLayputView);
                txtValue = (TextView) itemLayputView.findViewById(R.id.txtValue);
                txtLabel = (TextView) itemLayputView.findViewById(R.id.txtLabel);
                img = (ImageView) itemLayputView.findViewById(R.id.img);
                card_header = (CardView) itemLayputView.findViewById(R.id.card_header);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                //onBackPressed();
                Intent inty = new Intent(Demo_CircleActivity.this, NavigationActivity.class);
                startActivity(inty);
                finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) Demo_CircleActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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

        //ProgressDialog progressDialog = new ProgressDialog(Demo_CircleActivity.this);
        //progressDialog.setMessage("Loading.....");
        // progressDialog.setCancelable(false);

        AndyUtils.showCustomProgressDialog_New(Demo_CircleActivity.this, "Fetching Information Please Wait..", false, null,2);

        aQuery.progress(null);
        aQuery.ajax(url, null, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                String getTitle = null, getValue = null;
                if (isConnection()) try {
                    JSONArray jsonArray = object.getJSONArray("CirclewiseTrip");

                    array_admin = new ArrayList<>();
                    for (int aind = 0; aind < jsonArray.length(); aind++) {
                        JSONArray jsonArray1 = jsonArray.getJSONArray(aind);
                        //String temp = jsonArray1.join("~");
                        getTitle = jsonArray1.getString(0).toString();
                        getValue = jsonArray1.getString(1).toString();
                        //String message = object.getString("Message");
                        CircelBean admin_bean = new CircelBean(getTitle, getValue);
                        admin_bean.setTitle(getTitle);
                        admin_bean.setNum(getValue);
                        array_admin.add(admin_bean);
                    }

                    for (int j = array_admin.size(); j < 15; j++) {
                        etaBean = new CircelBean(circlename[array_admin.size()], "0");
                        array_admin.add(etaBean);
                    }

                    //String message = object.getString("Message");
                      */
/*  Intent inty = new Intent(Demo_CircleActivity.this, NavigationActivity.class);
                        startActivity(inty);*//*

                    // Snackbar.make(view, message, Snackbar.LENGTH_LONG) .show();
                      */
/*  for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObj1 = jsonArray.getJSONObject(i);
                            CircelBean admin_bean = new CircelBean(getTitle,getValue);
                            admin_bean.setTitle(jsonObj1.getString("installedDevices"));
                            array_admin.add(admin_bean);
                        }*//*


                        */
/*recyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                        recyclerView.setLayoutManager(layoutManager);
                        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplication(), R.dimen.item_offset);
                        recyclerView.addItemDecoration(itemDecoration);
                        mAdapter = new Myadapter();
                        recyclerView.setAdapter(mAdapter);*//*

                    workingArr = new ArrayList<CircelBean>();
                    int endcounter = (startcounter + 4) > array_admin.size() ? array_admin.size() : (startcounter + 4);
                    for (int aind = startcounter; aind < endcounter; aind++) {
                        workingArr.add(array_admin.get(aind));
                    }

                    if (array_admin.size() < 5) {
                        btn_prevoius.setVisibility(View.INVISIBLE);
                        btn_next.setVisibility(View.INVISIBLE);
                    }


                    SpinnerAdaptor spinnerAdaptor = new SpinnerAdaptor(Demo_CircleActivity.this, workingArr, R.layout.circledashboard);
                    gridview.setAdapter(spinnerAdaptor);
                    AndyUtils.removeCustomProgressDialog();
                    startcounter = (startcounter + 4);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }
*/
}
