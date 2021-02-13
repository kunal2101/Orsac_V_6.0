package orsac.rosmerta.orsac_vehicle.android.orsac;

/**
 * Created by Diwash Choudhary on 3/23/2017.
 */
public class StaticDynamicModel {
    String eta_no;
    String vehregno;


    public StaticDynamicModel(String eta_no) {
        this.eta_no = eta_no;
    }

    public StaticDynamicModel() {
    }


    public StaticDynamicModel(String eta_no, String vehregno) {
        this.eta_no = eta_no;
        this.vehregno = vehregno;

    }

    public String getVehregno() {
        return vehregno;
    }

    public void setVehregno(String vehregno) {
        this.vehregno = vehregno;
    }

    public String getEta_no() {
        return eta_no;
    }

    public void setEta_no(String eta_no) {
        this.eta_no = eta_no;
    }


}
