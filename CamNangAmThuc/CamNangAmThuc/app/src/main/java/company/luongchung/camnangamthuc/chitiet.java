package company.luongchung.camnangamthuc;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import company.luongchung.model.MonAn;
import dmax.dialog.SpotsDialog;

public class chitiet extends AppCompatActivity {
    MonAn monAn;
    TextView txtTenND;
    Intent intent;
    String LinkURL, LinkImage, TenMonAn, Title;
    WebView webView;
    AlertDialog progressDialog;
    Button btnLikeItem, btnShareItem;
    String DATABASE_NAME = "dbCamNangAmThuc.sqlite";
    SQLiteDatabase sqLiteDatabase = null;
    private ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet);
        addControls();
        addEvents();
    }

    private void addControls() {
        intent = getIntent();
        progressDialog = new SpotsDialog(this, R.style.Custom);
        progressDialog.setCanceledOnTouchOutside(false);
        LinkURL = intent.getStringExtra("LinkURL");
        LinkImage = intent.getStringExtra("LinkImage");
        TenMonAn = intent.getStringExtra("TenMonAn");
        Title = intent.getStringExtra("Title");
        monAn = new MonAn(1, TenMonAn, Title, LinkImage, LinkURL);
        btnLikeItem = (Button) findViewById(R.id.btnLikeItem);
        btnShareItem = (Button) findViewById(R.id.btnShareItem);
        txtTenND = (TextView) findViewById(R.id.txtTenND);
        txtTenND.setText(TenMonAn);
        webView = (WebView) findViewById(R.id.wv);
        webView.getSettings().setJavaScriptEnabled(true);
        new GetItem().execute();
        new GhiLichSu().execute();
        btnShareItem.setEnabled(false);
        btnLikeItem.setEnabled(false);
    }

    private void addEvents() {
        webView.getViewTreeObserver().addOnScrollChangedListener(mOnScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (webView.getScrollY() < 20) {
                    btnShareItem.setEnabled(false);
                    btnLikeItem.setEnabled(false);
                } else {
                    btnLikeItem.setEnabled(true);
                    btnShareItem.setEnabled(true);
                }

            }
        });
        btnShareItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyShare(monAn);
            }
        });
        btnLikeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyLike(monAn);
            }
        });
    }

    private void xuLyShare(MonAn monAn) {
        ShareDialog shareDialog = new ShareDialog(chitiet.this);
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(monAn.getLinkURL()))
                .build();
        shareDialog.show(linkContent);
    }

    private void xuLyLike(MonAn monAn) {
        if (!isTrungMonAn(monAn)) {
            sqLiteDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            ContentValues row;
            row = new ContentValues();
            row.put("TenMonAn", monAn.getTxtTenMon());
            row.put("Title", monAn.getTitle());
            row.put("linkImage", monAn.getLinkImg());
            row.put("linkURL", monAn.getLinkURL());
            sqLiteDatabase.insert("tbYeuThich", null, row);
            Toast.makeText(chitiet.this, "Bạn đã thêm " + monAn.getTxtTenMon() + " vào danh sách yêu thích.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(chitiet.this, "Mục bạn chọn đã có trong danh sách yêu thích.", Toast.LENGTH_LONG).show();
        }

    }

    private boolean isTrungMonAn(MonAn monAn) {
        //lấy ra list yêu thích trong sqlite
        ArrayList<MonAn> listMonAnTemp = new ArrayList<>();
        sqLiteDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from tbYeuThich", null);
        while (cursor.moveToNext()) {
            int _ID = cursor.getInt(0);
            String _TenMonAn = cursor.getString(1);
            String _Title = cursor.getString(2);
            String _LinkImage = cursor.getString(3);
            String _LinkURL = cursor.getString(4);
            MonAn monAntemp = new MonAn(_ID, _TenMonAn, _Title, _LinkImage, _LinkURL);
            listMonAnTemp.add(monAntemp);
        }
        cursor.close();
        //kiểm tra mục yêu thích muốn thêm có trùng ko thì insert
        for (int i = 0; i < listMonAnTemp.size(); i++) {
            if (monAn.getTxtTenMon().equals(listMonAnTemp.get(i).getTxtTenMon())
                    && monAn.getLinkURL().equals(listMonAnTemp.get(i).getLinkURL())) {
                return true;
            }
        }
        return false;
    }


    class GetItem extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            webView.loadDataWithBaseURL(null, s, "text/html", "UTF-8", null);
            progressDialog.dismiss();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected String doInBackground(String... params) {
            String html="";
            try
            {
                Document doc = Jsoup.connect(LinkURL).timeout(20*1000).get();
                Elements elements = doc.select("div.w-news-content");
                html=elements.toString();
            } catch (Exception ex)
            {
                Log.d("Loi",ex.toString());
            }
            return html;
    }
    }
    class GhiLichSu extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            Date today=new Date(System.currentTimeMillis());
            SimpleDateFormat timeFormat= new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
            String s=timeFormat.format(today.getTime());
            sqLiteDatabase=openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
            ContentValues row;
            row = new ContentValues();
            row.put("TenMonAn", monAn.getTxtTenMon());
            row.put("Title",monAn.getTitle());
            row.put("linkImage", monAn.getLinkImg());
            row.put("linkURL", monAn.getLinkURL());
            row.put("Time","Thời gian: "+s);
            sqLiteDatabase.insert("tbLichSu", null, row);
            return null;
        }
    }
}