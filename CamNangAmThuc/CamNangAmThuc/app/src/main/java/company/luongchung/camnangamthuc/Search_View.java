package company.luongchung.camnangamthuc;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import company.luongchung.adapter.adapterMonAn;
import company.luongchung.model.MonAn;
import dmax.dialog.SpotsDialog;

public class Search_View extends ActionBarActivity {
    adapterMonAn adapter;
    ArrayList<MonAn> monAnArrayList;
    ListView lvSearch;
    AlertDialog dialog;
    SearchView svSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__view);

        addControls();
        addEvents();

    }

    private void addEvents() {
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                CharSequence name = svSearch.getQuery();
                String txtQr = String.format(name + "");
                new getDataSearch().execute(txtQr);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
    }

    private void addControls() {
        svSearch= (SearchView) findViewById(R.id.sv_search);
        svSearch.setIconified(false);
        dialog = new SpotsDialog(Search_View.this,R.style.Custom);
        dialog.setCanceledOnTouchOutside(false);
        monAnArrayList=new ArrayList<MonAn>();
        lvSearch= (ListView) findViewById(R.id.lvSearch);
        adapter=new adapterMonAn(Search_View.this,R.layout.item_monan,monAnArrayList);
        lvSearch.setAdapter(adapter);
    }
    public class getDataSearch extends AsyncTask<String,Void,ArrayList<MonAn>>  {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();

        }

        @Override
        protected void onPostExecute(ArrayList<MonAn> monen) {
            super.onPostExecute(monen);
            adapter=new adapterMonAn(Search_View.this,R.layout.item_monan,monAnArrayList);
            lvSearch.setAdapter(adapter);
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            dialog.dismiss();
        }

        @Override
        protected ArrayList<MonAn> doInBackground(String... params) {
            monAnArrayList.clear();
            ArrayList<MonAn> temp = new ArrayList<MonAn>();
            for (int page =1; page <= 5; page++)
            {
                String linkPage = "http://www.doivi.net/tim-kiem?tukhoa=" + xuLyChuoi(params[0])+"&trang="+page;
                Log.d("TimKiemChung", linkPage);
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
                    String s = "http://www.doivi.net" + linkURL;
                    if (LinkImage != "" && tenMon != "" && linkURL != "") {
                        MonAn monAn = new MonAn(i, tenMon, title, LinkImage, s);
                        monAnArrayList.add(monAn);
                        Log.d("TimKiemChung", monAn.toString());
                    }

                }
            }
            return temp;
        }
        private String xuLyChuoi(String a){
            char[] temp=new char[a.length()];
            for (int i=0;i<a.length();i++)
            {
                if(a.charAt(i)==' ')
                {
                    temp[i]='+';
                }
                else
                {
                    temp[i]=a.charAt(i);
                }
            }
            String g=String.copyValueOf(temp);
            return g;
        }
    }

}
