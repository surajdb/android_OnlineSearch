package com.suraj.pocketguide.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suraj.pocketguide.Models.SearchResultModel;
import com.suraj.pocketguide.Models.SpeedDialModel;
import com.suraj.pocketguide.R;

import java.util.List;

/**
 * Created by Suraj on 12/26/2016.
 */

public class SpeedDialAdapter  extends ArrayAdapter<SpeedDialModel> {
    private List<SpeedDialModel> speedDialList;

    public SpeedDialAdapter(Context context, int resource, List<SpeedDialModel> objects) {
        super(context, resource, objects);
        this.speedDialList = objects;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.speed_dial, parent, false);
        }

        SpeedDialModel speedDial = speedDialList.get(position);

        ImageView imageViewSpeedDial = (ImageView) convertView.findViewById(R.id.imageViewSpeedDial);
        TextView txtSpeedDialText = (TextView) convertView.findViewById(R.id.txtSpeedDialText);

        //imageViewSpeedDial.setImageDrawable();
        String modifiedURL = speedDial.getBookmarkLink().replace("http://","").replace("https://","").replace("www.","").replace(".com","").replace(".org","").replace(".ca","").toUpperCase();

        if(speedDial.getBookmarkLink().equals("Add New")) {
            imageViewSpeedDial.setImageDrawable(convertView.getResources().getDrawable(R.drawable.main_add));
            // convert pixel to dp
            float scale = convertView.getResources().getDisplayMetrics().density;
            int sizeInDp = 10;
            int dpAsPixels = (int) (sizeInDp*scale + 0.5f);
            imageViewSpeedDial.setPadding(dpAsPixels,dpAsPixels,dpAsPixels,dpAsPixels);
        }
        else {
            imageViewSpeedDial.setImageDrawable(convertView.getResources().getDrawable(android.R.drawable.btn_star_big_on));// small image
        }
        if (modifiedURL.length()>=10)
        {
            modifiedURL = modifiedURL.toString().substring(0,8) + "...";
        }
        txtSpeedDialText.setText(modifiedURL);

        return convertView;
    }
}
