package com.codelite.kr4k3rz.kotha6.holer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codelite.kr4k3rz.kotha6.R;

import java.util.ArrayList;

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder> {
    //  private LocationFilter locationFilter;
    private Context context;
    private ArrayList<String> arrayList;

    public LocationListAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    /*  @Override
      public int getCount() {
          return arrayList.size();
      }

      @Override
      public Object getItem(int i) {
          return arrayList.get(i);
      }
  */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.locText.setText(arrayList.get(position));
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

 /*   @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_location, viewGroup, false);
            holder = new ViewHolder();
            holder.locText = view.findViewById(R.id.loc_list);
            view.setTag(holder);
        } else {
            // get view holder back
            holder = (ViewHolder) view.getTag();
        }
        holder.locText.setText(arrayList.get(i));
        return view;
    }*/

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView locText;

        ViewHolder(View itemView) {
            super(itemView);
            locText = itemView.findViewById(R.id.tv_location);
        }
    }


}
