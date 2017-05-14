package company.luongchung.model;

import java.io.Serializable;

/**
 * Created by LUONG CHUNG on 3/13/2017.
 */

public class MonAn implements Serializable {
    int iD;
    String txtTenMon;
    String linkImg;
    String title;
    String linkURL;
    public MonAn() { }
    public String getLinkURL() {
        return linkURL;
    }
    public void setLinkURL(String linkURL) {
        this.linkURL = linkURL;
    }
    public MonAn(int iD, String txtTenMon, String title, String linkImg, String linkURL) {
        this.iD = iD;
        this.txtTenMon = txtTenMon;
        this.linkImg = linkImg;
        this.title = title;
        this.linkURL=linkURL;

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
    public void setLinkImg(String link) {
        this.linkImg = link;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String toString() {
        return getTxtTenMon()+"  "+getLinkImg();
    }
}
