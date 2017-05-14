package company.luongchung.camnangamthuc;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import company.luongchung.adapter.adapterCaNhan;
import company.luongchung.model.LichSuMonAn;
import company.luongchung.model.MonAnCaNhan;

public class MonCaNhan extends AppCompatActivity {

    private int CODE_ADD=888;
    ArrayList<MonAnCaNhan> monAnCaNhanArrayList;
    adapterCaNhan adapterCaNhan;
    ListView listView;
    FloatingActionButton fl_ThemMon;
    String DATABASE_NAME="dbCamNangAmThuc.sqlite";
    SQLiteDatabase sqLiteDatabase=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_ca_nhan);
        addControls();
        addEvents();


    }

    private void addEvents() {
        fl_ThemMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MonCaNhan.this,ThemMonAn.class);
                startActivityForResult(intent,CODE_ADD);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CODE_ADD||requestCode==1508)
        {
            GetData();
            adapterCaNhan.notifyDataSetChanged();
        }
    }

    private void addControls() {
        fl_ThemMon= (FloatingActionButton) findViewById(R.id.fl_ThemMon);
        listView= (ListView) findViewById(R.id.lvMonCaNhan);
        monAnCaNhanArrayList=new ArrayList<>();
        GetData();
        adapterCaNhan=new adapterCaNhan(MonCaNhan.this,R.layout.item_canhan,monAnCaNhanArrayList);
        listView.setAdapter(adapterCaNhan);
    }
    private void GetData()
    {
        sqLiteDatabase=openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        Cursor cursor=sqLiteDatabase.rawQuery("Select * from tbMonCaNhan",null);
        monAnCaNhanArrayList.clear();
        while (cursor.moveToNext())
        {
            int _ID=cursor.getInt(0);
            String _TenMonAn=cursor.getString(1);
            String _NguyenLieu=cursor.getString(2);
            String _CheBien=cursor.getString(3);
            byte[] _Anh=cursor.getBlob(4);
            MonAnCaNhan monAnCaNhan =new MonAnCaNhan(_ID,_TenMonAn,_NguyenLieu,_CheBien,getBitMap(_Anh));
            monAnCaNhanArrayList.add(monAnCaNhan);
        }
        cursor.close();
        Log.d("KiemTTTTT","Kết thúc lấy dữ liệu");
    }
    public Bitmap getBitMap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
