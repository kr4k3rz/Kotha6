package com.codelite.kr4k3rz.kotha6.ui.profile;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.codelite.kr4k3rz.kotha6.R;
import com.codelite.kr4k3rz.kotha6.holer.RoomListHolder;
import com.codelite.kr4k3rz.kotha6.model.Post;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

public class ManageListings extends AppCompatActivity {
    FirebaseRecyclerAdapter mFirebaseRecyclerAdapter;

    FirebaseAuth mAuth = getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mUserRef = database.getReference().child("users").child(mAuth.getCurrentUser().getUid());
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_listings);

        recyclerView = findViewById(R.id.recyclerview_lists);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        mFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, RoomListHolder>(Post.class, R.layout.item_room_list, RoomListHolder.class, mUserRef.child("posts")) {
            @Override
            protected void populateViewHolder(RoomListHolder viewHolder, Post model, int position) {
                viewHolder.setRoomRent(model.getRent_amt());
                viewHolder.setAvailableDate(model.getAvailable_date());
                viewHolder.setRoomImg(getApplicationContext(), model.getImg_urls().get(0));
                viewHolder.setAddress(model.getAddress());
                Log.d("TAG", "position : " + position);
            }

            @Override
            public RoomListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                RoomListHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new RoomListHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                    }

                    @Override
                    public void onItemLongClick(View view, final int position) {
                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(ManageListings.this);
                        builder.setTitle("Delete");
                        builder.setMessage("Delete this Post Listing?");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d("TAG", "position inside OnitemLongClick : " + position);
                                mFirebaseRecyclerAdapter.getRef(position).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        final Post post = dataSnapshot.getValue(Post.class);
                                        assert post != null;
                                        FirebaseDatabase.getInstance().getReference().child("rooms").orderByChild("uid").equalTo(post.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    Post post1 = snapshot.getValue(Post.class);
                                                    assert post1 != null;
                                                    if (post1.getPostId().equals(post.getPostId())) {
                                                        snapshot.getRef().removeValue();
                                                        mFirebaseRecyclerAdapter.getRef(position).removeValue();

                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.show();
                    }
                });
                return viewHolder;
            }
        };

        recyclerView.setAdapter(mFirebaseRecyclerAdapter);


    }
}
