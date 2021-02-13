package orsac.rosmerta.orsac_vehicle.android.orsac;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import orsac.rosmerta.orsac_vehicle.android.R;

public class Orsac_admin_third_deta extends AppCompatActivity  implements View.OnClickListener{
    TextView mapview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orsac_admin_third_deta);
        mapview= (TextView)findViewById(R.id.mapview);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mapview:
                Intent intent = new Intent(Orsac_admin_third_deta.this, OsmMap.class);
                startActivity(intent);
                break;
        }
    }
}
