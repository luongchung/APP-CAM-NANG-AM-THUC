package company.luongchung.model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by LUONGCHUNG on 4/12/2017.
 */
public class MonAnCaNhan implements Serializable{
    private int iD;

    public int getiD() {
        return iD;
    }

    public void setiD(int iD) {
        this.iD = iD;
    }

    private String TenMonAn;
    private String NguyenLieu;
    private String CheBien;
    private Bitmap Anh;

    public MonAnCaNhan() {
    }
    public MonAnCaNhan(int ID,String tenMonAn, String nguyenLieu, String cheBien, Bitmap anh) {
        iD=ID;
        TenMonAn = tenMonAn;
        NguyenLieu = nguyenLieu;
        CheBien = cheBien;
        Anh = anh;
    }

    public String getTenMonAn() {
        return TenMonAn;
    }

    public void setTenMonAn(String tenMonAn) {
        TenMonAn = tenMonAn;
    }

    public String getNguyenLieu() {
        return NguyenLieu;
    }

    public void setNguyenLieu(String nguyenLieu) {
        NguyenLieu = nguyenLieu;
    }

    public String getCheBien() {
        return CheBien;
    }

    public void setCheBien(String cheBien) {
        CheBien = cheBien;
    }

    public Bitmap getAnh() {
        return Anh;
    }

    public void setAnh(Bitmap anh) {
        Anh = anh;
    }
}
