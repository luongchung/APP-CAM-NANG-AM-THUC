package company.luongchung.camnangamthuc;

import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import company.luongchung.adapter.adapterYeuThich;
import company.luongchung.model.MonAn;

public class yeuthich extends AppCompatActivity {
    ArrayList<MonAn> listYeuThich;
    adapterYeuThich adapterYT;
    ListView lvYeuThich;
    String DATABASE_NAME="dbCamNangAmThuc.sqlite";
    SQLiteDatabase sqLiteDatabase=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yeuthich);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(999);
        lvYeuThich= (ListView) findViewById(R.id.lvYeuThich);
        listYeuThich=new ArrayList<>();
        loadData();

    }

    public void loadData() {
        layDuLieuTuSQLite();
        adapterYT=new adapterYeuThich(yeuthich.this,R.layout.item_yeuthich,listYeuThich);
        lvYeuThich.setAdapter(adapterYT);
    }

    private void layDuLieuTuSQLite() {
        sqLiteDatabase=openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        Cursor cursor=sqLiteDatabase.rawQuery("Select * from tbYeuThich",null);
        listYeuThich.clear();
        while (cursor.moveToNext())
        {
            int _ID=cursor.getInt(0);
            String _TenMonAn=cursor.getString(1);
            String _Title=cursor.getString(2);
            String _LinkImage=cursor.getString(3);
            String _LinkURL=cursor.getString(4);
            MonAn monAn=new MonAn(_ID,_TenMonAn,_Title,_LinkImage,_LinkURL);
            listYeuThich.add(monAn);
        }
        cursor.close();
    }
}
