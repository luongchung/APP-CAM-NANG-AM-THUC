package company.luongchung.model;

import java.io.Serializable;

/**
 * Created by LUONG CHUNG on 4/4/2017.
 */
public class LichSuMonAn implements Serializable {
    private int iD;
    private String txtTenMon;
    private String linkImg;
    private String title;
    private String linkURL;
    private String Time;

    public LichSuMonAn(int iD, String txtTenMon, String linkImg, String title, String linkURL, String time) {
        this.iD = iD;
        this.txtTenMon = txtTenMon;
        this.linkImg = linkImg;
        this.title = title;
        this.linkURL = linkURL;
        Time = time;
    }
    public LichSuMonAn() {

    }
    public int getiD() {
        return iD;
    }
    public void setiD(int iD) {
        this.iD = iD;
    }
    public String getTxtTenMon() {
        return txtTenMon;
    }
    public void setTxtTenMon(String txtTenMon) {
        this.txtTenMon = txtTenMon;
    }
    public String getLinkImg() {
        return linkImg;
    }
    public void setLinkImg(String linkImg) {
        this.linkImg = linkImg;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getLinkURL() {
        return linkURL;
    }
    public void setLinkURL(String linkURL) {
        this.linkURL = linkURL;
    }
    public String getTime() {
        return Time;
    }
    public void setTime(String time) {
        Time = time;
    }

    @Override
    public String toString() {
        return "ID: "+iD+"   Tên món: "+txtTenMon+"   LinkURL: "+linkURL;
    }
}
