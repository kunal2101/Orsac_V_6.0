package orsac.rosmerta.orsac_vehicle.android.Orsac.Model;

public class Pojo_Device_Management {
    String com_name;
    String install_device;
    String bg_device;
    String bg_amont;
    String loginid;
    String bgStatus;

    public Pojo_Device_Management(String com_name, String install_device, String bg_device) {
        this.com_name = com_name;
        this.install_device = install_device;
        this.bg_device = bg_device;
    }

    public Pojo_Device_Management() {
    }

    public Pojo_Device_Management(String com_name, String install_device, String bg_device, String bg_amont) {
        this.com_name = com_name;
        this.install_device = install_device;
        this.bg_device = bg_device;
        this.bg_amont = bg_amont;
    }

    public String getBgStatus() {
        return bgStatus;
    }

    public void setBgStatus(String bgStatus) {
        this.bgStatus = bgStatus;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public String getBg_amont() {
        return bg_amont;
    }

    public void setBg_amont(String bg_amont) {
        this.bg_amont = bg_amont;
    }

    public String getCom_name() {
        return com_name;
    }

    public void setCom_name(String com_name) {
        this.com_name = com_name;
    }

    public String getInstall_device() {
        return install_device;
    }

    public void setInstall_device(String install_device) {
        this.install_device = install_device;
    }

    public String getBg_device() {
        return bg_device;
    }

    public void setBg_device(String bg_device) {
        this.bg_device = bg_device;
    }
}
