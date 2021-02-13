package orsac.rosmerta.orsac_vehicle.android.model;

/**
 * Created by DKC on 04-01-2018.
 */

public class Model_New_Device {

    private String deviceId;
    private String installedTo;
    private String installationDate;
    private String status;
    private String issuedDate;
    private String managerName;

    public Model_New_Device(String deviceId, String installedTo, String installationDate, String status, String issuedDate, String managerName) {
        this.deviceId = deviceId;
        this.installedTo = installedTo;
        this.installationDate = installationDate;
        this.status = status;
        this.issuedDate = issuedDate;
        this.managerName = managerName;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getInstalledTo() {
        return installedTo;
    }

    public void setInstalledTo(String installedTo) {
        this.installedTo = installedTo;
    }

    public String getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(String installationDate) {
        this.installationDate = installationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(String issuedDate) {
        this.issuedDate = issuedDate;
    }
}
