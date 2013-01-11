/*
 * Copyright (C) 2012 The MoKee Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mokee.notepad;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.mokee.notepad.R;
import com.mokee.notepad.NotePad.NoteColumns;

/**
 * An activity that will edit the title of a note. Displays a floating window
 * with a text field.
 */
public class TitleEditor extends Activity implements View.OnClickListener {

    /**
     * This is a special intent action that means "edit the title of a note".
     */
    public static final String EDIT_TITLE_ACTION = "com.android.notepad.action.EDIT_TITLE";

    /**
     * An array of the columns we are interested in.
     */
    private static final String[] PROJECTION = new String[] {
            NoteColumns._ID, // 0
            NoteColumns.TITLE, // 1
            NoteColumns.NOTE, // 2
    };
    /** Index of the title column */
    private static final int COLUMN_INDEX_TITLE = 1;
    /** The index of the note column */
    private static final int COLUMN_INDEX_NOTE = 2;

    /**
     * Cursor which will provide access to the note whose title we are editing.
     */
    private Cursor mCursor;

    /**
     * The EditText field from our UI. Keep track of this so we can extract the
     * text when we are finished.
     */
    private EditText mText;

    /**
     * The content URI to the note that's being edited.
     */
    private Uri mUri;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.title_editor);

        // Get the uri of the note whose title we want to edit
        mUri = getIntent().getData();

        // Get a cursor to access the note
        mCursor = managedQuery(mUri, PROJECTION, null, null, null);

        // Set up click handlers for the text field and button
        mText = (EditText) this.findViewById(R.id.title);
        mText.setOnClickListener(this);

        Button b = (Button) findViewById(R.id.ok);
        b.setOnClickListener(bClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Initialize the text with the title column from the cursor
        if (mCursor != null) {
            mCursor.moveToFirst();
            mText.setText(mCursor.getString(COLUMN_INDEX_TITLE));
            // add by gaoerjing 2012.04.05 for QELYSW-1200(begin)
            mText.setSelection(mText.getText().length());
            // add by gaoerjing 2012.04.05 for QELYSW-1200(end)
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mCursor != null) {
            // Write the title back to the note
            ContentValues values = new ContentValues();
            if ((mText.getText().toString().length() == 0)
                    && (mCursor.getString(COLUMN_INDEX_NOTE).toString().length() == 0)) {
                getContentResolver().delete(mUri, null, null);
            }
            else {
                values.put(NoteColumns.TITLE, mText.getText().toString());
                getContentResolver().update(mUri, values, null, null);
            }
        }
    }

    public void onClick(View v) {
        // When the user clicks, just finish this activity.
        // onPause will be called, and we save our data there.
        // finish();
    }

    private OnClickListener bClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // When the user clicks, just finish this activity.
            // onPause will be called, and we save our data there.
            finish();
        }
    };
}