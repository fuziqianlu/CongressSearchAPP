package com.ziqianfu.congresssearch;

/**
 * Created by lenovo on 2016/11/17.
 */

public class ItemBean {
    public String bioId;
    public String name;
    public String state;
    public String chamber;
    public String district;
    public String party;
    public String imageViewUrl;
    public ItemBean(String bioId, String name, String state, String imageViewUrl){
        super();
        this.bioId=bioId;
        this.state=state;
        this.name=name;
//        this.imageViewUrl=imageViewUrl;
    }
    public String getBioId(){
        return bioId;
    }
    public String getName(){
        return name;
    }

//    public String getImageViewUrl() {
//        return imageViewUrl;
//    }

    public void setName(String name) {
        this.name = name;
    }

//    public void setImageViewUrl(String imageViewUrl) {
//        this.imageViewUrl = imageViewUrl;
//    }

    public void setBioId(String id) {
        this.bioId = id;
    }
    public ItemBean(){
        super();
    }
}
