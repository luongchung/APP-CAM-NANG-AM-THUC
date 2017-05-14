package company.luongchung.camnangamthuc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import company.luongchung.adapter.adapterLichSu;
import company.luongchung.adapter.adapterYeuThich;
import company.luongchung.model.LichSuMonAn;
import company.luongchung.model.MonAn;

public class lichsu extends Activity {
    ArrayList<LichSuMonAn> listLichSu;
    adapterLichSu adapterLS;
    ListView lvLichSu;
    String DATABASE_NAME="dbCamNangAmThuc.sqlite";
    SQLiteDatabase sqLiteDatabase=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lichsu);
        lvLichSu= (ListView) findViewById(R.id.lvLichSu);
        listLichSu=new ArrayList<>();
        loadData();
    }
    public void loadData(){
        layDuLieuTuSQLite();
        Collections.reverse(listLichSu);
        adapterLS=new adapterLichSu(lichsu.this,R.layout.item_lichsu,listLichSu);
        lvLichSu.setAdapter(adapterLS);
    }
    private void layDuLieuTuSQLite() {
        sqLiteDatabase=openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        Cursor cursor=sqLiteDatabase.rawQuery("Select * from tbLichSu",null);
        listLichSu.clear();
        while (cursor.moveToNext())
        {
            int _ID=cursor.getInt(0);
            String _TenMonAn=cursor.getString(1);
            String _Title=cursor.getString(2);
            String _LinkImage=cursor.getString(3);
            String _LinkURL=cursor.getString(4);
            String _Time=cursor.getString(5);
            LichSuMonAn lichSuMonAn=new LichSuMonAn(_ID,_TenMonAn,_LinkImage,_Title,_LinkURL,_Time);
            listLichSu.add(lichSuMonAn);
        }
        cursor.close();
    }
    public void xuLyXoaHetLichSu(View view) {
        new AlertDialog.Builder(this).setIcon(R.drawable.ic_xoaall).setTitle("XÓA TẤT CẢ LỊCH SỦ")
                .setMessage("Bạn có muốn xóa hết lịch sử không ?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sqLiteDatabase=openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
                        sqLiteDatabase.execSQL("DELETE FROM TBLICHSU");
                        sqLiteDatabase.close();
                        listLichSu.clear();
                        adapterLS.notifyDataSetChanged();
                    }
                }).setNegativeButton("Không", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();

    }
}
