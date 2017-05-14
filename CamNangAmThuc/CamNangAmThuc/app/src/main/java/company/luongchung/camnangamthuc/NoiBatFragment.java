package company.luongchung.camnangamthuc;


import android.app.AlertDialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.baoyz.widget.PullRefreshLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import company.luongchung.adapter.adapter;
import company.luongchung.adapter.adapterMonAn;
import company.luongchung.model.MonAn;
import dmax.dialog.SpotsDialog;

public class NoiBatFragment extends Fragment {
    AlertDialog dialog;
    String link = "http://www.doivi.net/nau-gi-hom-nay";
    ListView lvNoiBat;
    ArrayList<MonAn> listNoiBat;
    adapterMonAn adapterNoiBat;
    PullRefreshLayout layoutRefresh;

    public NoiBatFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_noi_bat, container, false);
        layoutRefresh = (PullRefreshLayout)view.findViewById(R.id.swipeRefreshLayoutNoiBat);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvNoiBat= (ListView) view.findViewById(R.id.lvNoiBat);
        dialog = new SpotsDialog(getActivity(),R.style.Custom);
        dialog.setCanceledOnTouchOutside(false);

        layoutRefresh.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listNoiBat.clear();
                layoutRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new GetDataNoiBat().execute();
                    }
                },3000);
            }
        });
        listNoiBat=new ArrayList<>();
        new GetDataNoiBat().execute();


    }

    public class GetDataNoiBat extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapterNoiBat = new adapterMonAn(getActivity(), R.layout.item_monan,listNoiBat);
            lvNoiBat.setAdapter(adapterNoiBat);
            layoutRefresh.setRefreshing(false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            DocPageNoiBat();
            return null;
        }
    }
    public void DocPageNoiBat() {
        Document doc = null;
        try {
            doc = Jsoup.connect(link).timeout(20*1000).get();
            Log.d("KIEMTRA", "connect link thành công");
        } catch (IOException e) {
            e.getStackTrace();
            Log.d("KIEMTRA","connect link lỗi");
        }
        Elements elements = doc.select("div.w-news-option li");
        for (int i = 0; i < elements.size(); i++) {
            Element e = elements.get(i);
            Elements a = e.select("a");
            String linkURL = a.attr("href");
            Elements img = a.select("img");
            String tenMon = img.attr("alt");
            String LinkImage = img.attr("src");
            Elements tit = e.select("div.w-news-option-item-name");
            String title = tit.text();
            MonAn monAn = new MonAn();
            monAn.setTxtTenMon(tenMon);
            monAn.setTitle(title);
            monAn.setLinkImg(LinkImage);
            monAn.setLinkURL("http://www.doivi.net" + linkURL);
            if (LinkImage != "" && tenMon != "" && linkURL != "") {
                listNoiBat.add(monAn);
                Log.d("KIEMTRA","insert nổi bật: "+monAn.toString());
            }
        }
    }

}
