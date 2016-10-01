package com.example.ashwin.youtubeapitest;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ashwin.youtubeapitest.GetVideosAsync.VideoLoader;
import com.example.ashwin.youtubeapitest.models.Item;
import com.example.ashwin.youtubeapitest.models.Videos;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.gson.Gson;

import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements VideoLoader, VideosAdapter.VideoListener {

    public static final String API_VIDEOS = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=latest+trailers&chart=mostRecent&regionCode=IN&key=your youtube api key";

    private RecyclerView mVideoRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private GetVideosAsync mGetVideosAsync;
    private Videos mVideosData;
    private List<Item> mVideos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVideoRecyclerView = (RecyclerView) findViewById(R.id.videoRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mVideoRecyclerView.setLayoutManager(mLinearLayoutManager);

        loadVideos();
    }

    @Override
    public void onVideosPreLoad() {
        //Toast.makeText(this, "Failed to load videos, Please try again later :/", Toast.LENGTH_LONG).show();
    }

    private VideosAdapter mVideosAdapter;
    private void setAdapter()
    {
        mVideosAdapter = new VideosAdapter(this, mVideos);
        mVideoRecyclerView.setAdapter(mVideosAdapter);
    }

    @Override
    public void loadVideos() {
        mGetVideosAsync = new GetVideosAsync(this);
        String url = API_VIDEOS;
        Request request = new Request.RequestBuilder(url, Request.RequestTypeEnum.GET)
                .build();
        mGetVideosAsync.executeTask(request);
    }

    @Override
    public void onVideosLoaded(Response response) {
        Log.v("Response code:", "Response Code: " + response.getResponseCode() + response.getBody());
        try {
                Gson gson = new Gson();

                if(mVideosData == null)
                {
                    mVideosData = gson.fromJson(response.getBody(), Videos.class);
                    mVideos = mVideosData.getItems();
                    setAdapter();
                }
                else
                {
                    Videos videos = gson.fromJson(response.getBody(), Videos.class);
                    mVideosData.getItems().addAll(videos.getItems());
                    mVideosAdapter.notifyItemRangeChanged(mVideosAdapter.getItemCount(), videos.getItems().size());
                    mVideosData.setNextPageToken(videos.getNextPageToken());
                }
        } catch (Exception e) {

                e.printStackTrace();
        }
    }

    @Override
    public void onVideoLoadFailed(Response response) {

        Toast.makeText(this, "Failed to load videos, Please try again later :/", Toast.LENGTH_LONG).show();
    }

    @Override
    public void couldNotLoadVideos() {

    }

    @Override
    public void onVideoLoadsResponse(Response response) {
        if(response.getResponseCode() == HttpsURLConnection.HTTP_OK)
            {
                onVideosLoaded(response);
            }
            else if(response.getResponseCode() == 0)
            {
                Toast.makeText(this, "No internet connection, Please again with connection", Toast.LENGTH_LONG).show();
            }
            else
            {
                onVideoLoadFailed(response);
            }
    }

    @Override
    public void loadMoreVideos() {
        if(mGetVideosAsync != null && mGetVideosAsync.getStatus() != AsyncTask.Status.RUNNING)
        {
                String nextPageToken = mVideosData.getNextPageToken();

                if(nextPageToken == null)
                {
                    mVideosAdapter.onNoMoreImages();
                }
                else {

                    Uri.Builder builder = Uri.parse(API_VIDEOS).buildUpon();
                    builder.appendQueryParameter("pageToken", nextPageToken);

                    String url = builder.build().toString();

                    Request request = new Request.RequestBuilder(url, Request.RequestTypeEnum.GET).build();

                    mGetVideosAsync = new GetVideosAsync(this);
                    mGetVideosAsync.executeTask(request);
                }
        }
    }

    @Override
    public void onVideoImageClicked(View view, int position) {
        try {
                Intent intent = YouTubeIntents.createPlayVideoIntentWithOptions(this, mVideos.get(position).getId().getVideoId(), true, false);
                startActivity(intent);
        }
        catch(Exception e)
        {
                Toast.makeText(this, "Youtube not installed", Toast.LENGTH_LONG).show();
        }
    }

}
