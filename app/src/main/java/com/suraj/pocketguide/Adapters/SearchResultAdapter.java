package com.suraj.pocketguide.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.suraj.pocketguide.R;
import com.suraj.pocketguide.Models.SearchResultModel;

import java.io.File;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Suraj on 12/22/2016.
 */

public class SearchResultAdapter extends ArrayAdapter<SearchResultModel.Items>{

    private List <SearchResultModel.Items> searchResult;
    private TextView txtSearchTitle;
    private ImageView imgSearchImage;
    private TextView txtSearchWebsite;

    public SearchResultAdapter(Context context, int resource, List<SearchResultModel.Items> objects) {
        super(context, resource, objects);
        this.searchResult = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).
                inflate(R.layout.search_result, parent, false);
        //}

        SearchResultModel.Items search = searchResult.get(position);

        String Title = search.getWebURLTitle();
        String displayWebURL = search.getWebURLToShow();

        txtSearchTitle = (TextView) convertView.findViewById(R.id.txtSearchTitle);
        txtSearchWebsite = (TextView) convertView.findViewById(R.id.txtSearchWebsite);
        imgSearchImage = (ImageView) convertView.findViewById(R.id.imgSearchImage);

        //getting imageview

        if(search.getCseThumbnail()!= null){
            if (search.getCseThumbnail().getImageURLObject() != null) {
                Picasso.with(getContext()).load(search.getCseThumbnail().getImageURLObject()[0].getImageURL()).into(imgSearchImage);
            }
            else if (search.getCseThumbnail().getImageActualURLObject() != null) {
                Picasso.with(getContext()).load(search.getCseThumbnail().getImageActualURLObject()[0].getImageURL()).into(imgSearchImage);
            }
            else
                Picasso.with(getContext()).load(R.drawable.main_image_not_found).into(imgSearchImage);
        }
        if(search.getWebURLTitle().equals("Personalised Result")||search.getWebURLTitle().equals("General Results"))
        {
            LinearLayout searchLayout = (LinearLayout) convertView.findViewById(R.id.searchLayout);
            searchLayout.setBackgroundColor(Color.BLUE);
            txtSearchTitle.setTextColor(Color.WHITE);
            txtSearchTitle.setText(Title);
            txtSearchWebsite.setVisibility(View.GONE);
        }
        else
        {
            txtSearchTitle.setText(Title);
            txtSearchWebsite.setText(displayWebURL);
        }
        return convertView;
    }
}
