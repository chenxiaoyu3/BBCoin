package com.chenxiaoyu.bbcoin;


import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;


public class BaseActivity extends SherlockActivity {
    protected void onCreate(Bundle savedInstanceState) {

        DataCenter.getInstance();
//    	getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.menu_share, menu);

        // Set file with share history to the provider and set the share intent.
        MenuItem actionItem = menu.findItem(R.id.menu_item_share_action_provider_action_bar);
//        ShareActionProvider sActionProvider = new ShareActionProvider(this);

//        actionItem.setActionProvider(new ShareActionProvider(this));
        ShareActionProvider actionProvider = (ShareActionProvider) actionItem.getActionProvider();
        actionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        actionProvider.setShareIntent(createShareIntent());

        return true;
    }

    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
//        Uri uri = Uri.fromFile(getFileStreamPath("shared.png"));?
//        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        return shareIntent;
    }
}
