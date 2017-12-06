package com.codelite.kr4k3rz.kotha6.holer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codelite.kr4k3rz.kotha6.R;


public class RoomListHolder extends RecyclerView.ViewHolder {
    private ImageView roomImg;
    private TextView roomRent;
    private TextView availableDate;


    public RoomListHolder(View itemView) {
        super(itemView);
        roomImg = itemView.findViewById(R.id.imageRoom);
        roomRent = itemView.findViewById(R.id.rentAmt);
        availableDate = itemView.findViewById(R.id.availableFrom);

    }

    public void setRoomImg(String img) {
        //
    }

    public void setRoomRent(String rentAmt) {
        roomRent.setText(rentAmt);
    }

    public void setAvailableDate(String date) {
        availableDate.setText(date);
    }


}
