package com.example.ashwin.youtubeapitest.models;

/**
 * Created by ashwin on 12/9/16.
 */

public class App {

    private String title, subTitle, callToActionText, appImage, appLogo, marketUrl, url;

    public App() {
    }

    public App(String title, String subTitle, String callToAction, String appImageUrl, String appLogoUrl, String marketUrl, String url) {
        this.title = title;
        this.subTitle = subTitle;
        this.appImage = appImageUrl;
        this.appLogo = appLogoUrl;
        this.marketUrl = marketUrl;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subTitle;
    }

    public void setSubtitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getAppimage() {
        return appImage;
    }

    public void setAppimage(String appImageUrl) {
        this.appImage = appImageUrl;
    }

    public String getCalltoactiontext() {
        return callToActionText;
    }

    public void setCalltoactiontext(String callToActionText) {
        this.callToActionText = callToActionText;
    }

    public String getApplogo() {
        return appLogo;
    }

    public void setApplogo(String appLogoUrl) {
        this.appLogo = appLogoUrl;
    }

    public String getMarketurl() {
        return marketUrl;
    }

    public void setMarketurl(String marketUrl) {
        this.marketUrl = marketUrl;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

}
