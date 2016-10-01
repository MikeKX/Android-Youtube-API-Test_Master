package com.example.ashwin.youtubeapitest;

import java.io.IOException;

/**
 * Created by vihaan on 16/4/16.
 */
public class GetVideosAsync extends CoreAsync<Request, Void, Response> {


    public interface VideoLoader
    {
        void onVideosPreLoad();

        void loadVideos();

        void onVideosLoaded(Response response);

        void onVideoLoadFailed(Response response);

        void couldNotLoadVideos();

        void onVideoLoadsResponse(Response response);
    }

    private VideoLoader mVideoLoader;
    public GetVideosAsync(VideoLoader videoLoader)
    {
        mVideoLoader = videoLoader;
    }

    @Override
    protected Response doInBackground(Request... params) {
        Request url = params[0];

        Response response = null;
        try {
            response = mApiClient.execute(url);
        } catch (IOException e) {

            e.printStackTrace();
        }
        return response;
    }


    @Override
    protected void onPostExecute(Response response) {
        super.onPostExecute(response);
        if(mVideoLoader != null)
        {
            mVideoLoader.onVideoLoadsResponse(response);
        }
    }
}
