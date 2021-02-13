package orsac.rosmerta.orsac_vehicle.android.orsac;

/**
 * Created by Diwash Choudhary on 3/23/2017.
 */
public class Admin_sec_bean {
    String eta_no;
    String circle;
    String vehregno;
    String polling_status;

    public Admin_sec_bean(String eta_no) {
        this.eta_no = eta_no;
    }

    public Admin_sec_bean() {
    }

    public Admin_sec_bean(String eta_no, String circle) {
        this.eta_no = eta_no;
        this.circle = circle;
    }
    public Admin_sec_bean(String eta_no, String vehregno,String polling_status) {
        this.eta_no = eta_no;
        this.vehregno = vehregno;
        this.polling_status = polling_status;
    }

    public String getPolling_status() {
        return polling_status;
    }

    public void setPolling_status(String polling_status) {
        this.polling_status = polling_status;
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

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }
}
