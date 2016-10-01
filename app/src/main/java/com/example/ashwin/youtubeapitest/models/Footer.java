package com.example.ashwin.youtubeapitest.models;

/**
 * Created by ashwin on 27/9/16.
 */

public class Footer {

    public Footer() {
    }

    String footerUrl = "", footerName = "", footerImage = "";

    public String getFooterurl() {
        return footerUrl;
    }

    public void setFooterurl(String footerUrl) {
        this.footerUrl = footerUrl;
    }

    public String getFootername() {
        return footerName;
    }

    public void setFootername(String footerName) {
        this.footerName = footerName;
    }

    public String getFooterimage() {
        return footerImage;
    }

    public void setFooterimage(String footerImage) {
        this.footerImage = footerImage;
    }

}
