package orsac.rosmerta.orsac_vehicle.android.orsac;

/**
 * Created by Diwash Choudhary on 3/22/2017.
 */
public class CircelBean {

    String title;
    String num;

    public CircelBean() {
    }

    public CircelBean(String title, String num) {
        this.title = title;
        this.num = num;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
