package com.suraj.pocketguide.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Suraj on 12/22/2016.
 */

public class SearchModel {

    //@SerializedName("items")
    Items webItems[];

    public class Items{

        @SerializedName("link")
        String webURL;

        Pagemap imageULR[];

        public String getWebURL() {
            return webURL;
        }

        public void setWebURL(String webURL) {
            this.webURL = webURL;
        }


        public class Pagemap{

            @SerializedName("cse_thumbnail")
            String imageURL;
        }
    }
}
