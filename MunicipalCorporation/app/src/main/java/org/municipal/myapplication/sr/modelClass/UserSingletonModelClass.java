package org.municipal.myapplication.sr.modelClass;

public class UserSingletonModelClass {

    String news_content, news_image, news_heading;

    public static void setInstance(UserSingletonModelClass instance) {
        UserSingletonModelClass.instance = instance;
    }

    private static UserSingletonModelClass instance=null;
    protected UserSingletonModelClass(){
        // Exists only to defeat instantiation.
    }
    public static UserSingletonModelClass getInstance(){
        if(instance == null) {
            instance = new UserSingletonModelClass();
        }
        return instance;

    }

    //=================Getter method starts=============

    public String getNews_content() {
        return news_content;
    }

    public String getNews_image() {
        return news_image;
    }

    public String getNews_heading() {
        return news_heading;
    }
    //=================Getter method ends=============

    //==================Setter method starts==================

    public void setNews_content(String news_content) {
        this.news_content = news_content;
    }

    public void setNews_image(String news_image) {
        this.news_image = news_image;
    }

    public void setNews_heading(String news_heading) {
        this.news_heading = news_heading;
    }

//==================Setter method ends===================
}
