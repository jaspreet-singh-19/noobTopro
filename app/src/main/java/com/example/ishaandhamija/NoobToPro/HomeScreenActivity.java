package com.example.ishaandhamija.NoobToPro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ishaandhamija.NoobToPro.Models.News;
import com.example.ishaandhamija.NoobToPro.Models.articles;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

public class HomeScreenActivity extends AppCompatActivity {

    ImageView toi,hindu;
    RequestQueue requestQueue;
    ArrayList<articles> newsarr;
    News news;
    RecyclerView newsrv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        toi=(ImageView) findViewById(R.id.toi);

        final Gson gson = new Gson();
        requestQueue = Volley.newRequestQueue(this);
        newsarr=new ArrayList<>();

        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(
                "https://newsapi.org/v1/articles?source=the-times-of-india&sortBy=top&apiKey=043ebbb54491468cb62b76c702bad05b",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        news =gson.fromJson(response.toString(),News.class);
                        Toast.makeText(HomeScreenActivity.this, "Fetching news", Toast.LENGTH_SHORT).show();
                        createArray();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HomeScreenActivity.this, "onError", Toast.LENGTH_SHORT).show();
                    }
                }
        );




        //-----------------------------------------------write here


        toi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeScreenActivity.this, "pressed", Toast.LENGTH_SHORT).show();

                newsrv=(RecyclerView) findViewById(R.id.recyclerView);
                requestQueue.add(jsonObjectRequest);
                if(!newsarr.isEmpty()) {
                    RvNews adapter = new RvNews();

                    newsrv.setLayoutManager(new LinearLayoutManager(HomeScreenActivity.this));

                    newsrv.setAdapter(adapter);
                }

            }
        });
    }





//-------------------------------------------------------------------------------view holder

    public class NewsHolder extends RecyclerView.ViewHolder{

        TextView title,description;
        ImageView image;

        public NewsHolder(View itemView) {
            super(itemView);
            this.title=(TextView) itemView.findViewById(R.id.titleTV);
            this.description=(TextView) itemView.findViewById(R.id.descriptionTV);
            this.image=(ImageView) itemView.findViewById(R.id.imageIV);
        }
    }

    public void createArray(){
        newsarr=news.getArticles();
    }

    // ----------------------------------------------------------- RecyclerView  stuff
    public class RvNews extends RecyclerView.Adapter<NewsHolder>{


        @Override
        public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li=getLayoutInflater();
            View itemView=li.inflate(R.layout.newselement,parent,false);
            return new NewsHolder(itemView);
        }

        @Override
        public void onBindViewHolder(NewsHolder holder, int position) {

            holder.title.setText(newsarr.get(position).getTitle());
            holder.description.setText(newsarr.get(position).getDescription());

            Picasso.with(HomeScreenActivity.this)
                    .load(newsarr.get(position).getUrlToImage()) // the image path
                    .fit()
                    .placeholder(R.mipmap.default_img)
                    .into(holder.image);

        }

        @Override
        public int getItemCount() {
            return newsarr.size();
        }
    }
}
