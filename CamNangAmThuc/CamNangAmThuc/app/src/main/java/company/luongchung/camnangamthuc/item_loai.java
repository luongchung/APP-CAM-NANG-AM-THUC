package company.luongchung.camnangamthuc;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import company.luongchung.adapter.adapterMonAn;
import company.luongchung.model.MonAn;
import dmax.dialog.SpotsDialog;

public class item_loai extends AppCompatActivity {
    Intent intent=new Intent();
    String link;
    ArrayList<MonAn> temp;
    adapterMonAn adapter;
    ListView lvChiTietLoai;
    AlertDialog progressDialog;
    TextView txtTenMonAn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_loai);
        progressDialog = new SpotsDialog(this,R.style.Custom);
        progressDialog.setCanceledOnTouchOutside(false);
        txtTenMonAn= (TextView) findViewById(R.id.txtTenMon);
        temp=new ArrayList<MonAn>();
        lvChiTietLoai= (ListView) findViewById(R.id.lvChiTietLoai);
        intent=getIntent();
        String a=intent.getStringExtra("TenLoai");
        txtTenMonAn.setText(a);
        link=intent.getStringExtra("LinkURL");
        new LayDanhSachLoai().execute();

    }
    public class LayDanhSachLoai extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter=new adapterMonAn(item_loai.this,R.layout.item_monan,temp);
            lvChiTietLoai.setAdapter(adapter);
            progressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            DocHTML();
            return null;
        }
    }
    private void DocHTML() {
        int size=10;
        for (int page = 0; page <size; page++)
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
                }

            }


        }
    }
}
