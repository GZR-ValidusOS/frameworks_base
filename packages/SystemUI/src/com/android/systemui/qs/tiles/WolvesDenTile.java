/*
 * Copyright (C) 2016 Ground Zero Roms
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

package com.android.systemui.qs.tiles;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;

import com.android.systemui.R;
import com.android.systemui.qs.QSTile;
import com.android.internal.logging.MetricsProto.MetricsEvent;

/** Quick settings tile: WolvesDen **/
public class WolvesDenTile extends QSTile<QSTile.BooleanState> {

    private static final Intent WOLVESDEN = new Intent().setComponent(new ComponentName(
            "com.android.settings", "com.android.settings.Settings$WolvesDenActivity"));

    private boolean mListening;

    public WolvesDenTile(Host host) {
        super(host);
    }

    @Override
    public BooleanState newTileState() {
        return new BooleanState();
    }

    public void setListening(boolean listening) {
        if (mListening == listening) return;
        mListening = listening;
    }

    @Override
    public void handleClick() {
        mHost.startActivityDismissingKeyguard(WOLVESDEN);
    }

    @Override
    public CharSequence getTileLabel() {
        return mContext.getString(R.string.quick_settings_wolvesden);
    }

    @Override
    public Intent getLongClickIntent() {
        return(WOLVESDEN);
    }

    @Override
    protected void handleUpdateState(BooleanState state, Object arg) {
        state.label = mContext.getString(R.string.quick_settings_wolvesden);
        state.icon = ResourceIcon.get(R.drawable.ic_qs_fling);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsEvent.WOLVESDEN;

    }
} 
