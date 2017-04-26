package com.suraj.pocketguide.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.suraj.pocketguide.Models.SearchResultModel;

import java.util.List;

/**
 * Created by Suraj on 12/28/2016.
 */

public class SearchRankAdapter  extends ArrayAdapter<SearchResultModel> {

    public SearchRankAdapter(Context context, int resource, List<SearchResultModel> objects) {
        super(context, resource, objects);
    }

}
