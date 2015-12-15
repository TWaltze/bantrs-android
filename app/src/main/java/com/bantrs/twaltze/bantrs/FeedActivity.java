package com.bantrs.twaltze.bantrs;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bantrs.twaltze.bantrs.models.Auth;
import com.bantrs.twaltze.bantrs.models.Room;
import com.bantrs.twaltze.bantrs.models.User;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A login screen that offers login via email/password.
 */
public class FeedActivity extends Activity {

    private User user = null;
    private List<Room> rooms;

    // UI references.
    private TextView mRooms;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RoomAdapter();
        mRecyclerView.setAdapter(mAdapter);

        Button mCreateRoomButton = (Button) findViewById(R.id.create_room_button);
        mCreateRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(FeedActivity.this, CreateRoomActivity.class);
                FeedActivity.this.startActivity(myIntent);
            }
        });
    }

    public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {
        public List<Room> mItems;
        private UserRoomTask mRoomTask = null;

        public RoomAdapter() {
            super();
            mItems = new ArrayList<Room>();

            mRoomTask = new UserRoomTask();
            mRoomTask.execute((Void) null);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.recycler_view_card_item, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            Room room = mItems.get(i);
            viewHolder.title.setText(room.title);

            DateTime date = new DateTime(room.createdAt);
            DateTimeFormatter formatter = DateTimeFormat.shortDateTime().withLocale(Locale.US);
            String output_US = formatter.print(date);
            viewHolder.meta.setText("By " + room.author.username + " | " + output_US);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            public View view;
            public TextView title;
            public TextView meta;

            public ViewHolder(View itemView) {
                super(itemView);

                view = itemView;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Room clickedRoom = mItems.get(getAdapterPosition());

                        Intent myIntent = new Intent(FeedActivity.this, RoomActivity.class);
                        myIntent.putExtra("rid", clickedRoom.getID());
                        FeedActivity.this.startActivity(myIntent);
                    }
                });
                title = (TextView)itemView.findViewById(R.id.feed_title);
                meta = (TextView)itemView.findViewById(R.id.feed_meta);
            }
        }

        public class UserRoomTask extends AsyncTask<Void, Void, Boolean> {
            UserRoomTask() {
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    user = Auth.getInstance().getUser();
                    rooms = user.getRooms();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
            }

            @Override
            protected void onPostExecute(final Boolean success) {
                mRoomTask = null;
                mItems.addAll(rooms);
                mAdapter.notifyItemInserted(mItems.size() - (rooms.size() * 3));
            }

            @Override
            protected void onCancelled() {
                mRoomTask = null;
            }
        }
    }
}

