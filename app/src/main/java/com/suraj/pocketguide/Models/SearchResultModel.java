package com.suraj.pocketguide.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Suraj on 12/22/2016.
 */

public class SearchResultModel {

    @SerializedName("items")

    Items webItems[];

    public void setQueries(Queries queries) {
        this.queries = queries;
    }

    public Queries getQueries() {
        return queries;
    }

    @SerializedName("queries")
    Queries queries;

    public Items[] getWebItems() {
        return webItems;
    }

    public void setWebItems(Items[] webItems) {
        this.webItems = webItems;
    }

    public class Items{

        @SerializedName("title") //aactual link
                String webURLTitle;
        @SerializedName("link") //aactual link
                String webURL;
        @SerializedName("displayLink") //aactual link
                String webURLToShow;
        @SerializedName("snippet") //aactual link
                String webURLminiContent;
        @SerializedName("pagemap")
        Pagemap cseThumbnail;

        public void setWebURLTitle(String webURLTitle) {
            this.webURLTitle = webURLTitle;
        }

        public void setWebURL(String webURL) {
            this.webURL = webURL;
        }

        public void setWebURLToShow(String webURLToShow) {
            this.webURLToShow = webURLToShow;
        }

        public void setWebURLminiContent(String webURLminiContent) {
            this.webURLminiContent = webURLminiContent;
        }

        public void setCseThumbnail(Pagemap cseThumbnail) {
            this.cseThumbnail = cseThumbnail;
        }

        public String getWebURLTitle() {
            return webURLTitle;
        }

        public String getWebURL() {
            return webURL;
        }

        public String getWebURLToShow() {
            return webURLToShow;
        }

        public String getWebURLminiContent() {
            return webURLminiContent;
        }

        public Pagemap getCseThumbnail() {
            return cseThumbnail;
        }


        public class Pagemap{

            @SerializedName("cse_thumbnail")
            CseThumbnail imageURLObject[];

            @SerializedName("cse_image")
            CseImage imageActualURLObject[];

            public CseImage[] getImageActualURLObject() {
                return imageActualURLObject;
            }

            public void setImageActualURLObject(CseImage[] imageActualURLObject) {
                this.imageActualURLObject = imageActualURLObject;
            }

            public void setImageURLObject(CseThumbnail[] imageURLObject) {
                this.imageURLObject = imageURLObject;
            }

            public CseThumbnail[] getImageURLObject() {

                return imageURLObject;
            }

            public class CseThumbnail
            {
                @SerializedName("src")
                String imageURL;

                public void setImageURL(String imageURL) {
                    this.imageURL = imageURL;
                }
                public String getImageURL() {

                    return imageURL;
                }
            }
            public class CseImage
            {
                @SerializedName("src")
                String imageURL;

                public void setImageURL(String imageURL) {
                    this.imageURL = imageURL;
                }
                public String getImageURL() {

                    return imageURL;
                }
            }
        }
    }

    public class Queries
    {

        @SerializedName("request")
        Request requests [];

        public Request[] getRequests() {
            return requests;
        }

        public void setRequests(Request[] requests) {
            this.requests = requests;
        }


        public class Request{
            @SerializedName("totalResults")
            public int totalResults ;

            public int getTotalResults() {
                return totalResults;
            }

            public void setTotalResults(int totalResults) {
                this.totalResults = totalResults;
            }


        }
    }
}
