package com.bantrs.twaltze.bantrs;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bantrs.twaltze.bantrs.models.Auth;
import com.bantrs.twaltze.bantrs.models.Comment;
import com.bantrs.twaltze.bantrs.models.Room;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A login screen that offers login via username/password.
 */
public class RoomActivity extends Activity {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
//    private CreateCommentTask mCreateCommentTask = null;
//    private LoadRoomTask mRoomTask = null;
    private LoadCommentsTask mCommentsTask = null;

    // UI references.
    private EditText mCommentView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private Room room;
    private String rid;
    private Comment comment;
    private List<Comment> comments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Bundle extras = getIntent().getExtras();
        rid = extras.getString("rid");
        room = new Room(rid);
        System.out.println(rid);

//        mRoomTask = new LoadRoomTask(rid);
//        mRoomTask.execute((Void) null);

//        mCommentView = (EditText) findViewById(R.id.comment_text);
        mRecyclerView = (RecyclerView)findViewById(R.id.comments_list);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CommentAdapter();
        mRecyclerView.setAdapter(mAdapter);

//        Button mCreateCommentButton = (Button) findViewById(R.id.create_comment_button);
//        mCreateCommentButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                createComment();
//            }
//        });
    }

//    public void createComment() {
//        if (mCreateCommentTask != null) {
//            return;
//        }
//
//        String comment = mCommentView.getText().toString();
//
//        boolean cancel = false;
//
//        // Check for a valid comment.
//        if (TextUtils.isEmpty(comment)) {
//            cancel = true;
//        }
//
//        if (!cancel) {
//            mCreateCommentTask = new CreateCommentTask(comment);
//            mCreateCommentTask.execute((Void) null);
//        }
//    }

    public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
        public CommentAdapter() {
            super();

            mCommentsTask = new LoadCommentsTask();
            mCommentsTask.execute((Void) null);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.recycler_view_comments, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            Comment comment = comments.get(i);

            viewHolder.commentText.setText(comment.comment);
        }

        @Override
        public int getItemCount() {
            if (comments == null) {
                return 0;
            } else {
                return comments.size();
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            public TextView commentText;

            public ViewHolder(View itemView) {
                super(itemView);

                commentText = (TextView)itemView.findViewById(R.id.recycler_view_comment);
            }
        }
    }

    public class LoadCommentsTask extends AsyncTask<Void, Void, Boolean> {
        LoadCommentsTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                comments = room.getComments();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return comments != null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mCommentsTask = null;
            mAdapter.notifyItemInserted(comments.size());
        }

        @Override
        protected void onCancelled() {
            mCommentsTask = null;
        }
    }

//    public class CreateCommentTask extends AsyncTask<Void, Void, Boolean> {
//
//        private final String mComment;
//
//        CreateCommentTask(String comment) {
//            mComment = comment;
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            try {
//                comment = new Comment(mComment, rid).create();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return room != null;
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success) {
//            mCreateCommentTask = null;
//
//            if (success) {
//                finish();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            mCreateCommentTask = null;
//        }
//    }

//    public class LoadRoomTask extends AsyncTask<Void, Void, Boolean> {
//        private Room loaded;
//
//        LoadRoomTask(String rid) {
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            try {
//                loaded = Room.getRoom(rid);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success) {
//            System.out.println("Room loaded");
//            mRoomTask = null;
//
//            if (success) {
//                System.out.println("Success");
//                room = loaded;
//                finish();
//
////                mCommentsTask = new LoadCommentsTask();
////                mCommentsTask.execute((Void) null);
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            mRoomTask = null;
//        }
//    }
}

