package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dimension.netandlist.R;

import java.util.List;

public class CityAdapter extends ArrayAdapter<City>{
    int resourceId;
    public CityAdapter(@NonNull Context context, int resource, @NonNull List<City> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        City city = getItem(position);
        ViewHolder viewHolder;
        if(convertView !=null){
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.Point=(ImageView)view.findViewById(R.id.City_Item_Point);
            viewHolder.CityName = (TextView) view.findViewById(R.id.City_Item_Name);
            view.setTag(viewHolder);
        }
        viewHolder.CityName.setText(city.getCityName());
        viewHolder.Point.setImageResource(R.drawable.ic_citylistitem);
        return view;
    }
    class ViewHolder{
        TextView CityName;
        ImageView Point;
    }
}
