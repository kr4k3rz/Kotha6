package com.codelite.kr4k3rz.kotha6.holer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codelite.kr4k3rz.kotha6.R;


public class RoomListHolder extends RecyclerView.ViewHolder {
    private ImageView roomImg;
    private TextView roomRent;
    private TextView availableDate;
    private TextView address;


    public RoomListHolder(View itemView) {
        super(itemView);
        roomImg = itemView.findViewById(R.id.imageRoom);
        roomRent = itemView.findViewById(R.id.rentAmt);
        availableDate = itemView.findViewById(R.id.availableFrom);
        address = itemView.findViewById(R.id.address);
        //listener set on ENTIRE ROW, you may set on individual components within a row.
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());

            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });


    }


    public void setRoomImg(Context context, String imgUrl) {
        Glide.with(context)
                .load(imgUrl).into(roomImg);
    }

    public void setRoomRent(String rentAmt) {
        roomRent.setText(String.format("रु.%s", rentAmt));
    }

    public void setAvailableDate(String date) {
        availableDate.setText(date);
    }

    public void setAddress(String address1) {
        address.setText(address1);
    }


    private RoomListHolder.ClickListener mClickListener;

    //Interface to send callbacks...
    public interface ClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(RoomListHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }

}
