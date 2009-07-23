/*
 * Copyright (C) 2008-2009 Marc Blank
 * Licensed to The Android Open Source Project.
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

package com.android.exchange.adapter;

import com.android.email.provider.EmailContent.Mailbox;
import com.android.exchange.EasSyncService;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Parse the result of a Move command
 *
 * This is currently unused, as "move to folder" is not implemented in the application.
 **/
public class EasMoveParser extends EasParser {
    private static final String TAG = "EasMoveParser";
    private EasSyncService mService;
    private Mailbox mMailbox;
    protected boolean mMoreAvailable = false;

    public EasMoveParser(InputStream in, EasSyncService service) throws IOException {
        super(in);
        mService = service;
        mMailbox = service.mMailbox;
        //setDebug(true);
    }

    public boolean parse() throws IOException {
        int status;
        if (nextTag(START_DOCUMENT) != EasTags.MOVE_MOVE_ITEMS) {
            throw new IOException();
        }
        while (nextTag(START_DOCUMENT) != END_DOCUMENT) {
            if (tag == EasTags.MOVE_RESPONSE) {
                // Ignore
            } else if (tag == EasTags.MOVE_STATUS) {
                status = getValueInt();
                if (status != 3) {
                    Log.e(TAG, "Sync failed (3 is success): " + status);
                }
            } else if (tag == EasTags.SYNC_RESPONSES) {
                // TODO See if any of these cases need to be handled
                skipTag();
            } else {
                skipTag();
            }
        }
        mMailbox.save(mService.mContext);
        return false;
    }
}
