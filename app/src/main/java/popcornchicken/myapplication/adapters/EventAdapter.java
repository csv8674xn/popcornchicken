package popcornchicken.myapplication.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import popcornchicken.myapplication.R;
import popcornchicken.myapplication.Utility.DownloadThumbnailTask;
import popcornchicken.myapplication.Utility.Event;

/**
 * Created by Yao-Jung on 2015/11/8.
 */
public class EventAdapter extends BaseAdapter {
    Context context;
    List<Event> clusterList;
    private static LayoutInflater inflater = null;

    public EventAdapter(Context context, ArrayList<popcornchicken.myapplication.Utility.Event> clusterList){
        Log.d("Adapter", "Into constructor");
        this.clusterList = clusterList;
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return clusterList.size();
    }

    @Override
    public Object getItem(int position) {
        return clusterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("Adapter", "Into getView");
        View vi = convertView;
        vi = inflater.inflate(R.layout.event_row_view, null);
        vi.setId(position);
        TextView title = (TextView) vi.findViewById(R.id.tv_title);
        TextView date   = (TextView) vi.findViewById(R.id.tv_date);
        ImageView thumbnail = (ImageView) vi.findViewById(R.id.iv_thumbnail);
        Event currEvent = clusterList.get(position);
        title.setText(currEvent.title);
        if(currEvent.end_time == null || currEvent.end_time.equals("null") || currEvent.end_time.isEmpty()){
            date.setText(currEvent.start_time + " - " );
        } else {
            date.setText(currEvent.start_time + " - "  + currEvent.end_time);
        }

        if(currEvent.image_url != null ){
//            thumbnail.setImageTintList(null);
            new DownloadThumbnailTask(currEvent.image_url, thumbnail).execute();
        }
        return vi;
    }
}
