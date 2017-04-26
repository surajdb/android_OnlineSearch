package com.suraj.pocketguide.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suraj.pocketguide.Models.ScreenshotModel;
import com.suraj.pocketguide.Models.SpeedDialModel;
import com.suraj.pocketguide.R;

import java.io.File;
import java.util.List;

/**
 * Created by Suraj on 1/6/2017.
 */

public class ScreenshotAdapter  extends ArrayAdapter<ScreenshotModel> {
    private List<ScreenshotModel> screenshotList;

    public ScreenshotAdapter(Context context, int resource, List<ScreenshotModel> objects) {
        super(context, resource, objects);
        this.screenshotList = objects;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.speed_dial, parent, false);
        }

        ScreenshotModel screenshot = screenshotList.get(position);
        LinearLayout ll= (LinearLayout) convertView.findViewById(R.id.gridViewLL);


        ImageView imageViewSpeedDial = (ImageView) convertView.findViewById(R.id.imageViewSpeedDial);
        TextView txtSpeedDialText = (TextView) convertView.findViewById(R.id.txtSpeedDialText);

        //imageViewSpeedDial.setImageDrawable();
        //String modifiedURL = speedDial.getBookmarkLink().replace("http://","").replace("https://","").replace("www.","").replace(".com","").replace(".org","").replace(".ca","").toUpperCase();
//        if(speedDial.getBookmarkLink().equals("Add New")) {
//            imageViewSpeedDial.setImageDrawable(convertView.getResources().getDrawable(R.drawable.main_add));
//            // convert pixel to dp
//            float scale = convertView.getResources().getDisplayMetrics().density;
//            int sizeInDp = 10;
//            int dpAsPixels = (int) (sizeInDp*scale + 0.5f);
//            imageViewSpeedDial.setPadding(dpAsPixels,dpAsPixels,dpAsPixels,dpAsPixels);
//        }
//        else {

        File imgFile = new  File(screenshot.getSreenshotLink());

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageViewSpeedDial.setImageBitmap(myBitmap);
        }
//        }
        txtSpeedDialText.setText(screenshot.getSreenshotDate());
        return convertView;
    }
}