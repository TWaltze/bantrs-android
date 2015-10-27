package com.bantrs.twaltze.bantrs;

import android.app.Activity;
import android.os.AsyncTask;

import android.os.Bundle;
import android.widget.TextView;

import com.bantrs.twaltze.bantrs.models.User;

/**
 * A login screen that offers login via email/password.
 */
public class FeedActivity extends Activity {
    private UserRoomTask mRoomTask = null;

    private User user = null;
    private String rooms;

    // UI references.
    private TextView mRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        mRooms = (TextView) findViewById(R.id.rooms);

        mRoomTask = new UserRoomTask();
        mRoomTask.execute((Void) null);
    }
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserRoomTask extends AsyncTask<Void, Void, Boolean> {
        UserRoomTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                user = new User("tyler");
                rooms = user.getRooms();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mRoomTask = null;

            mRooms.setText(rooms);
        }

        @Override
        protected void onCancelled() {
            mRoomTask = null;
        }
    }
}

