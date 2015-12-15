package com.bantrs.twaltze.bantrs;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.bantrs.twaltze.bantrs.models.Auth;
import com.bantrs.twaltze.bantrs.models.Room;

/**
 * A login screen that offers login via username/password.
 */
public class CreateRoomActivity extends Activity {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private CreateRoomTask mCreateRoomTask = null;

    // UI references.
    private EditText mTitleView;
    private EditText mURLView;
    private View mLoginFormView;

    private Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        // Set up the login form.
        mTitleView = (EditText) findViewById(R.id.room_title);
        mURLView = (EditText) findViewById(R.id.room_url);

        Button mCreateRoomButton = (Button) findViewById(R.id.create_room_button);
        mCreateRoomButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                createRoom();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
    }

    public void createRoom() {
        if (mCreateRoomTask != null) {
            return;
        }

        // Reset errors.
        mTitleView.setError(null);
        mURLView.setError(null);

        // Store values at the time of the login attempt.
        String title = mTitleView.getText().toString();
        String url = mURLView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid title.
        if (TextUtils.isEmpty(title)) {
            mTitleView.setError(getString(R.string.error_field_required));
            focusView = mTitleView;
            cancel = true;
        }

        if (TextUtils.isEmpty(url)) {
            mURLView.setError(getString(R.string.error_field_required));
            focusView = mURLView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            mCreateRoomTask = new CreateRoomTask(title, url);
            mCreateRoomTask.execute((Void) null);
        }
    }

    public class CreateRoomTask extends AsyncTask<Void, Void, Boolean> {

        private final String mTitle;
        private final String mURL;

        CreateRoomTask(String title, String url) {
            mTitle = title;
            mURL = url;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                room = new Room(mTitle, mURL).create();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return room != null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mCreateRoomTask = null;

            if (success) {
                finish();

                Intent myIntent = new Intent(CreateRoomActivity.this, FeedActivity.class);
                CreateRoomActivity.this.startActivity(myIntent);
            } else {
                mTitleView.setError(getString(R.string.error_unknown));
                mTitleView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mCreateRoomTask = null;
        }
    }
}

