package orsac.rosmerta.orsac_vehicle.android;

/**
 * Created by Diwash Choudhary on 1/21/2017.
 */
public class Admin_model {
    String name ;
    int image_url;
    String counts;
    String tot_device;
    String avail_device;
    String install_device;
    String trip_count;
    String com_img;
    String imag_url_web;
    String active ;
    String inactive ;
    String no_mob_track;
    String bg_validity;
   /* public Admin_model(String name, String image_url,String counts,String tot_device,String avail_device,String install_device) {
        this.name = name;
        this.image_url = image_url;
        this.counts=counts;
        this.tot_device=tot_device;
        this.avail_device= avail_device;
        this.install_device= install_device;
    }*/

    public String getBg_validity() {
        return bg_validity;
    }

    public void setBg_validity(String bg_validity) {
        this.bg_validity = bg_validity;
    }

    public String getImag_url_web() {
        return imag_url_web;
    }

    public void setImag_url_web(String imag_url_web) {
        this.imag_url_web = imag_url_web;
    }

    public String getCom_img() {
        return com_img;
    }

    public void setCom_img(String com_img) {
        this.com_img = com_img;
    }

    public String getTrip_count() {
        return trip_count;
    }

    public void setTrip_count(String trip_count) {
        this.trip_count = trip_count;
    }

    public String getAvail_device() {
        return avail_device;
    }

    public void setAvail_device(String avail_device) {
        this.avail_device = avail_device;
    }

    public String getInstall_device() {
        return install_device;
    }

    public void setInstall_device(String install_device) {
        this.install_device = install_device;
    }

    public String getTot_device() {
        return tot_device;
    }

    public void setTot_device(String tot_device) {
        this.tot_device = tot_device;
    }


    public String getCounts() {
        return counts;
    }

    public void setCounts(String counts) {
        this.counts = counts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage_url() {
        return image_url;
    }

    public void setImage_url(int image_url) {
        this.image_url = image_url;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getInactive() {
        return inactive;
    }

    public void setInactive(String inactive) {
        this.inactive = inactive;
    }

    public String getNo_mob_track() {
        return no_mob_track;
    }

    public void setNo_mob_track(String no_mob_track) {
        this.no_mob_track = no_mob_track;
    }
}
