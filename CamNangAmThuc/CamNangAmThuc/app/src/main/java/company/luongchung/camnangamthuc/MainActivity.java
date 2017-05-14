package company.luongchung.camnangamthuc;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.support.design.widget.BottomNavigationView;
import company.luongchung.adapter.ViewPagerAdapter;
import company.luongchung.model.MonAn;
import company.luongchung.utils.BottomNavigationHelper;
import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;
    AlertDialog dialog,dialog1;
    ViewPagerAdapter viewPagerAdapter;
    String link = "http://www.doivi.net/nau-gi-hom-nay";
    String DATABASE_NAME="dbCamNangAmThuc.sqlite";
    String DB_PATH="/databases/";
    SQLiteDatabase sqLiteDatabase=null;
    String TrangThaiKhoiDong ="ChuaCo";
    TextView txtLableMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("");

        dialog = new SpotsDialog(MainActivity.this,R.style.Custom1);
        dialog.setCanceledOnTouchOutside(false);
        dialog1 = new SpotsDialog(MainActivity.this,R.style.Custom2);
        dialog1.setCanceledOnTouchOutside(false);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationHelper.disableShiftMode(navigation);

        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/font3.ttf");
        txtLableMain= (TextView) findViewById(R.id.txtLabelMain);
        txtLableMain.setTypeface(font);

        if(isOnline())
        {
            xuLySaoChepSQLite();
            addControls();
            new GhiSQLite().execute();
        }
        else
        {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
            Toast.makeText(this,"Không có kết nối Internet, yêu cầu bật kết nối.",Toast.LENGTH_LONG).show();
            finish();
        }
    }
    private void addControls() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new LoaiFragment(), "Thể loại");
        viewPagerAdapter.addFragments(new NoiBatFragment(), "Nổi bật");
        viewPagerAdapter.addFragments(new TatCaFragment(), "Tất cả");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.btnYeuThich) {
                    Intent intent = new Intent(MainActivity.this, yeuthich.class);
                    startActivity(intent);
                }

                if(item.getItemId() == R.id.btnThemMon) {
                    Intent intent = new Intent(MainActivity.this,MonCaNhan.class);
                    startActivity(intent);
                }

                if(item.getItemId() == R.id.btnLichSu)
                {
                    Intent intent = new Intent(MainActivity.this, lichsu.class);
                    startActivity(intent);
                }
                if(item.getItemId() == R.id.btnUpdate)
                {
                    xuLyUpdateData();
                }
                return false;
            }
        });
    }

    private void xuLyUpdateData() {
        new AlertDialog.Builder(this).setIcon(R.drawable.ic_update_black).setTitle("CẬP NHẬP ỨNG DỤNG")
                .setMessage("Bạn có muốn thoát cập nhập không ?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        xuLyCapNhap();
                    }
                }).setNegativeButton("Không", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     dialog.cancel();
                    }
                }).show();
    }

    private void xuLyCapNhap() {
        sqLiteDatabase=openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        sqLiteDatabase.execSQL("DELETE FROM TBMONAN");
        sqLiteDatabase.close();
        new UpdateData().execute();

    }

    private void xuLySaoChepSQLite() {
        File dbfile= getDatabasePath(DATABASE_NAME);
        if (!dbfile.exists())
        {
            try
            {
                saoChepDatabaseTuAsset();
                //Toast.makeText(this,"Sao chép Database thành công !",Toast.LENGTH_SHORT).show();
            }
            catch (Exception ex)
            {
                Toast.makeText(this,ex.toString(),Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void saoChepDatabaseTuAsset() {
        try {
            // Load database từ assets
            InputStream myInput = getAssets().open(DATABASE_NAME);
            // Đường dẫn tới file database
            String outFileName = layDuongDanLuuTru();

            File f= new File(getApplicationInfo().dataDir+DB_PATH);
            if(!f.exists())
            {
                f.mkdir();//chưa có đường dẫn thì tạo đường dẫn database
            }
            // Tạo một outputstream theo kiểu file
            OutputStream myOutput = new FileOutputStream(outFileName);
            //chuyển các byte từ input sang output
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer))>0){
                myOutput.write(buffer, 0, length);
            }

            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();

        }
        catch (Exception ex)
        {
            Toast.makeText(MainActivity.this,ex.toString(),Toast.LENGTH_LONG).show();
        }

    }
    private String layDuongDanLuuTru() {
        return getApplicationInfo().dataDir+DB_PATH+DATABASE_NAME;
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
    public class GhiSQLite extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            SharedPreferences sharedPreferences= getSharedPreferences(TrangThaiKhoiDong,MODE_PRIVATE);
            Boolean kt=sharedPreferences.getBoolean("SQLite",true);
            if(kt)
            {
                GhiVaoCSDL();
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean("SQLite",false);
                editor.commit();
            }
            return null;
        }
    }
    public class UpdateData extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog1.show();


        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog1.dismiss();
        }
        @Override
        protected Void doInBackground(Void... params) {
            GhiVaoCSDL();
            return null;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem itemSearch = menu.findItem(R.id.btnSearch);
        itemSearch.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent=new Intent(MainActivity.this,Search_View.class);
                startActivity(intent);
                return true;
            }
        });

        return true;
    }
    private void GhiVaoCSDL() {
        ArrayList<MonAn> temp=new ArrayList<MonAn>();
        for (int page = 0; page < 56; page++)
        {
            String linkPage = link + "?trang=" + page;
            Document doc = null;
            try {
                doc = Jsoup.connect(linkPage).timeout(20*1000).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements elements = doc.select("div.w-news-list li");
            for (int i = 0; i < elements.size(); i++) {
                Element e = elements.get(i);
                Elements a = e.select("a");
                String linkURL = a.attr("href");
                Elements img = a.select("img");
                String tenMon = img.attr("alt");
                String LinkImage = img.attr("src");
                Elements tit = e.select("div.w-news-item-des");
                String title = tit.text();
                String s="http://www.doivi.net" + linkURL;
                if (LinkImage != "" && tenMon != "" && linkURL != "")
                {
                    MonAn monAn=new MonAn(i,tenMon,title,LinkImage,s);
                    temp.add(monAn);
                    Log.d("tagchung",tenMon);
                }

            }
        }
        Log.d("tagchung","Đọc xong mạng");
        sqLiteDatabase=openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        ContentValues row;
        for (int l=0;l<temp.size();l++) {
            row = new ContentValues();
            row.put("TenMonAn", temp.get(l).getTxtTenMon() + "");
            row.put("Title", temp.get(l).getTitle() + "");
            row.put("linkImage", temp.get(l).getLinkImg() + "");
            row.put("linkURL", temp.get(l).getLinkURL() + "");
            sqLiteDatabase.insert("tbMonAn", null, row);
            Log.d("tagchungh", "insert: " + l);
        }
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(R.drawable.ic_exit).setTitle("THOÁT ỨNG DỤNG")
                .setMessage("Bạn có muốn thoát ứng dụng không ?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("Không", null).show();
    }

}
