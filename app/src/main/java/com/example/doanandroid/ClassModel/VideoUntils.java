package com.example.doanandroid.ClassModel;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VideoUntils {
    Context context;

    public VideoUntils(Context context) {
        this.context = context;
    }

    String filename = "database";

    public static ArrayList<VideoYoutube> videoFavorite=new ArrayList<>();

    public  static ArrayList<VideoYoutube> videoHistory=new ArrayList<>();
    public static ArrayList<String> listnodes=new ArrayList<>();

    public void addVideoFovorite(VideoYoutube videoYoutube)
    {
        if(videoFavorite.indexOf(videoYoutube)>0)
        {
            videoFavorite.add(0,videoYoutube);
        }
    }
    public void addVideoHistory(VideoYoutube videoYoutube)
    {
        if(videoHistory.indexOf(videoYoutube)>0)
        {
            videoHistory.add(0,videoYoutube);
        }
    }
    public ArrayList<VideoYoutube> getVideoFavorite() {

        return videoFavorite;
    }
    public ArrayList<VideoYoutube> getVideoHistory() {
        return videoHistory;
    }
    public ArrayList<String> getnodes()
    {
        return listnodes;
    }
    public void getJSONYoutube(String URL,VideoYouTubeAdapter adapter,ArrayList<VideoYoutube> arrayList, Context context)
    {

        RequestQueue requestQueue= Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            JSONArray jsonArray=response.getJSONArray("items");
                            String title="";
                            String url="";
                            String ID="";
                            String ChannelTitle="";
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonItem=jsonArray.getJSONObject(i);
                                JSONObject jsonSnip=jsonItem.getJSONObject("snippet");
                                title=jsonSnip.getString("title");
                                ChannelTitle=jsonSnip.getString("videoOwnerChannelTitle");
                                JSONObject jsonThum=jsonSnip.getJSONObject("thumbnails");
                                JSONObject jsonMedi=jsonThum.getJSONObject("medium");
                                url=jsonMedi.getString("url");
                                JSONObject JsonID=jsonSnip.getJSONObject("resourceId");
                                ID=JsonID.getString("videoId");
                                arrayList.add(new VideoYoutube(title,url,ID));
                            }
                            adapter.notifyDataSetChanged();
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        // Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
    public int KiemTraTrung(String id,ArrayList<VideoYoutube> vy)
    {
        for(int i=0;i<vy.size();i++)
            if(id==vy.get(i).getVideoid().trim())
                return 0;
        return 1;
    }
    public ArrayList<Question> getDataQues()
    {
        ArrayList<Question> list=new ArrayList<>();

        List<Answer> answerList1=new ArrayList<>();
        answerList1.add(new Answer("Ho Chi Minh City",false));
        answerList1.add(new Answer("Da Nang City",false));
        answerList1.add(new Answer("Ha Noi",true));
        answerList1.add(new Answer("Hue",false));

        List<Answer> answerList2=new ArrayList<>();
        answerList2.add(new Answer("Son Doong",false));
        answerList2.add(new Answer("PhanXiPhang",true));
        answerList2.add(new Answer("Yen Tu",false));
        answerList2.add(new Answer("Pu Si Lung",false));

        List<Answer> answerList3=new ArrayList<>();
        answerList3.add(new Answer("Blue",false));
        answerList3.add(new Answer("Green",false));
        answerList3.add(new Answer("Oranges",false));
        answerList3.add(new Answer("Pink",true));

        List<Answer> answerList4=new ArrayList<>();
        answerList4.add(new Answer("stopover",false));
        answerList4.add(new Answer("long-haul flight",false));
        answerList4.add(new Answer("one-way ticket",false));
        answerList4.add(new Answer("return ticket",true));

        List<Answer> answerList5=new ArrayList<>();
        answerList5.add(new Answer("laddles",false));
        answerList5.add(new Answer("dishes",false));
        answerList5.add(new Answer("knives",false));
        answerList5.add(new Answer("eating utensils",true));

        List<Answer> answerList6=new ArrayList<>();
        answerList6.add(new Answer("Anh Chị em họ",false));
        answerList6.add(new Answer("Cậu, Chú, Bác",true));
        answerList6.add(new Answer("Cô, Dì, Thím, Mợ",false));
        answerList6.add(new Answer("Cháu Trai",false));

        List<Answer> answerList7=new ArrayList<>();
        answerList7.add(new Answer("Anh Chị em họ",false));
        answerList7.add(new Answer("Cậu, Chú, Bác",false));
        answerList7.add(new Answer("Cô, Dì, Thím, Mợ",true));
        answerList7.add(new Answer("Cháu Trai",false));

        List<Answer> answerList8=new ArrayList<>();
        answerList8.add(new Answer("watch",true));
        answerList8.add(new Answer("listen",false));
        answerList8.add(new Answer("are",false));
        answerList8.add(new Answer("watches",false));

        List<Answer> answerList9=new ArrayList<>();
        answerList9.add(new Answer("Singer",false));
        answerList9.add(new Answer("authors",false));
        answerList9.add(new Answer("musicians",true));
        answerList9.add(new Answer("actors",false));

        List<Answer> answerList10=new ArrayList<>();
        answerList10.add(new Answer("In the reindeer car",false));
        answerList10.add(new Answer("In the sock",true));
        answerList10.add(new Answer("In a carrot-shaped hat",false));
        answerList10.add(new Answer("In the shirt ",false));


        list.add(new Question(1,"What color represents love?",answerList3));
        list.add(new Question(2,"WWhat is the highest mountain in Vietnam?",answerList2));
        list.add(new Question(3,"What is the capital of Vietnam?",answerList1));
        list.add(new Question(4,"It's usually cheaper to buy a _____ than two singles",answerList4));
        list.add(new Question(5,"Chopsticks are used as traditional _____ in China, Japan, Korea, and Vietnam.",answerList5));
        list.add(new Question(6,"Uncle -........",answerList6));
        list.add(new Question(7,"Aunt -........",answerList7));
        list.add(new Question(8,"I and Yen ... TV every evening",answerList8));
        list.add(new Question(9,"Van Cao is one the most-well-know .... in Viet Nam",answerList9));
        list.add(new Question(10,"Where does Santa Claus usually put the gifts?",answerList10));

        return  list;
    }
    public ArrayList<Answer> ListAnswer()
    {
        ArrayList<Answer> list=new ArrayList<>();

        list.add(new Answer("Ho Chi Minh City",false,3));
        list.add(new Answer("Da Nang City",false,3));
        list.add(new Answer("Ha Noi",true,3));
        list.add(new Answer("Hue",false,3));

        list.add(new Answer("Son Doong",false,2));
        list.add(new Answer("PhanXiPhang",true,2));
        list.add(new Answer("Yen Tu",false,2));
        list.add(new Answer("Pu Si Lung",false,2));

        list.add(new Answer("Blue",false,1));
        list.add(new Answer("Green",false,1));
        list.add(new Answer("Oranges",false,1));
        list.add(new Answer("Pink",true,1));

        list.add(new Answer("stopover",false,4));
        list.add(new Answer("long-haul flight",false,4));
        list.add(new Answer("one-way ticket",false,4));
        list.add(new Answer("return ticket",true,4));

        list.add(new Answer("laddles",false,5));
        list.add(new Answer("dishes",false,5));
        list.add(new Answer("knives",false,5));
        list.add(new Answer("eating utensils",true,5));

        list.add(new Answer("Anh Chị em họ",false,6));
        list.add(new Answer("Cậu, Chú, Bác",true,6));
        list.add(new Answer("Cô, Dì, Thím, Mợ",false,6));
        list.add(new Answer("Cháu Trai",false,6));

        list.add(new Answer("Anh Chị em họ",false,7));
        list.add(new Answer("Cậu, Chú, Bác",false,7));
        list.add(new Answer("Cô, Dì, Thím, Mợ",true,7));
        list.add(new Answer("Cháu Trai",false,7));

        list.add(new Answer("watch",true,8));
        list.add(new Answer("listen",false,8));
        list.add(new Answer("are",false,8));
        list.add(new Answer("watches",false,8));

        list.add(new Answer("Singer",false,9));
        list.add(new Answer("authors",false,9));
        list.add(new Answer("musicians",true,9));
        list.add(new Answer("actors",false,9));


        list.add(new Answer("In the reindeer car",false,10));
        list.add(new Answer("In the sock",true,10));
        list.add(new Answer("In a carrot-shaped hat",false,10));
        list.add(new Answer("In the shirt ",false,10));

        return list;
    }
    public ArrayList<Question> listQuestion()
    {
        ArrayList<Question> list=new ArrayList<>();
        list.add(new Question(1,"What color represents love?"));
        list.add(new Question(2,"WWhat is the highest mountain in Vietnam?"));
        list.add(new Question(3,"What is the capital of Vietnam?"));
        list.add(new Question(4,"It's usually cheaper to buy a _____ than two singles"));
        list.add(new Question(5,"Chopsticks are used as traditional _____ in China, Japan, Korea, and Vietnam."));
        list.add(new Question(6,"Uncle -........"));
        list.add(new Question(7,"Aunt -........"));
        list.add(new Question(8,"I and Yen ... TV every evening"));
        list.add(new Question(9,"Van Cao is one the most-well-know .... in Viet Nam"));
        list.add(new Question(10,"Where does Santa Claus usually put the gifts?"));
        return  list;
    }
    public ArrayList<AvatarVideo> getDataChuDe()
    {
        ArrayList<AvatarVideo> tmp=new ArrayList<>();
        tmp.add(new AvatarVideo("FOOD AND DRINK","avtfood.jpg","https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PL3BXo6gVOnutshXwQoDJatT-pDEF2gLoC&key=AIzaSyBR8XzVDlWmv9PtUiX9WMv9zG9PzcF-3vM&maxResults=50"));
        tmp.add(new AvatarVideo("SPORT","avtsports.jpg","https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PL3BXo6gVOnuv-5WzbhNYgh0nEoBS4-2ZN&key=AIzaSyBR8XzVDlWmv9PtUiX9WMv9zG9PzcF-3vM&maxResults=50"));
        tmp.add(new AvatarVideo("TRAVELS","avttravel.jpg","https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PL3BXo6gVOnusR4HdZfSt-qURh9M5z9P2K&key=AIzaSyBR8XzVDlWmv9PtUiX9WMv9zG9PzcF-3vM&maxResults=50"));
        tmp.add(new AvatarVideo("FAMILY","avtfamily.jpg","https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PL3BXo6gVOnuuei2_pEKC9R6wxxj2MpTqB&key=AIzaSyBR8XzVDlWmv9PtUiX9WMv9zG9PzcF-3vM&maxResults=50"));
        tmp.add(new AvatarVideo("COLOR","avtcolor.jpg","https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PL3BXo6gVOnutcQ_KvekmAQqMc-oYcTRfB&key=AIzaSyBR8XzVDlWmv9PtUiX9WMv9zG9PzcF-3vM&maxResults=50"));
        tmp.add(new AvatarVideo("DAILY ACTIVITIES","avtdaily.jpg","https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PL3BXo6gVOnuvzEWVSNm0VD_UZ1w18uwyk&key=AIzaSyBR8XzVDlWmv9PtUiX9WMv9zG9PzcF-3vM&maxResults=50"));
        tmp.add(new AvatarVideo("CLOTHES","avtclothes.jpg","https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PL3BXo6gVOnusFMUYw1WZJnI6QgIFMm-CF&key=AIzaSyBR8XzVDlWmv9PtUiX9WMv9zG9PzcF-3vM&maxResults=50"));
        tmp.add(new AvatarVideo("TRANSPORTATION","avttrans.jpg","https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PL3BXo6gVOnuupsjNCeHWVH3wzqf-seSHm&key=AIzaSyBR8XzVDlWmv9PtUiX9WMv9zG9PzcF-3vM&maxResults=50"));
        tmp.add(new AvatarVideo("Grammar","avttrans.jpg","https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PL3BXo6gVOnusXtDXIcWFNrZ5R2BebHCwK&key=AIzaSyBR8XzVDlWmv9PtUiX9WMv9zG9PzcF-3vM&maxResults=50"));
        return tmp;
    }
}
