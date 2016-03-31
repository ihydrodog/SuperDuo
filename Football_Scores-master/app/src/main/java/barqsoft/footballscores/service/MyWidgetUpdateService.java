package barqsoft.footballscores.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.MainScreenFragment;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;
import barqsoft.footballscores.ViewHolder;
import barqsoft.footballscores.scoresAdapter;

import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;

public class MyWidgetUpdateService extends Service implements CursorLoader.OnLoadCompleteListener<Cursor> {

    CursorLoader mCursorLoader;
    public static final int SCORES_LOADER = 2;
    public scoresAdapter mAdapter;


    public MyWidgetUpdateService() {
    }

    @Override public void onCreate() {
        Date fragmentdate = new Date(System.currentTimeMillis()+((0-0)*86400000));
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");

        String[] date = {mformat.format(fragmentdate)};


        Log.d( "onCreate", date[0]);

        mCursorLoader = new CursorLoader(getApplicationContext(), DatabaseContract.scores_table.buildScoreWithDate(),
                null,null,date,null);

        mCursorLoader.registerListener(SCORES_LOADER, this);
        mCursorLoader.startLoading();



    }

    @Override public int onStartCommand( Intent intent, int flags, int startId) {
        if( intent != null) {
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            Log.d( "onStartCommand", String.format( "%d", mAppWidgetId));
        }

        return START_STICKY;
    }

    @Override public void onDestroy() {
        if( mCursorLoader != null) {
            mCursorLoader.unregisterListener( this);
            mCursorLoader.cancelLoad();
            mCursorLoader.stopLoading();
        }
    }
    int mAppWidgetId;
    @Override public void onLoadComplete( Loader<Cursor> loader, Cursor cursor) {

        cursor.moveToFirst();

        Context context = getApplicationContext();

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);



        views.setTextViewText(R.id.home_name, cursor.getString(scoresAdapter.COL_HOME));
        views.setTextViewText(R.id.away_name, cursor.getString(scoresAdapter.COL_AWAY));
        views.setTextViewText(R.id.score_textview, Utilies.getScores(cursor.getInt(scoresAdapter.COL_HOME_GOALS), cursor.getInt(scoresAdapter.COL_AWAY_GOALS)));
        views.setTextViewText(R.id.data_textview, cursor.getString(scoresAdapter.COL_MATCHTIME));
        views.setImageViewResource(R.id.home_crest, Utilies.getTeamCrestByTeamName(
                cursor.getString(scoresAdapter.COL_HOME)));
        views.setImageViewResource(R.id.away_crest, Utilies.getTeamCrestByTeamName(
                cursor.getString(scoresAdapter.COL_AWAY)));

        views.setOnClickPendingIntent(R.id.myWidget, PendingIntent.getActivity(context, 0, new Intent( context, MainActivity.class), 0));

        // Instruct the widget manager to update the widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(mAppWidgetId, views);

/*
        final ListView score_list = (ListView) rootView.findViewById(R.id.scores_list);
        mAdapter = new scoresAdapter(getApplicationContext(),null,0);
        score_list.setAdapter(mAdapter);


        mAdapter.detail_match_id = MainActivity.selected_match_id;
        score_list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                ViewHolder selected = (ViewHolder) view.getTag();
                mAdapter.detail_match_id = selected.match_id;
                MainActivity.selected_match_id = (int) selected.match_id;
                mAdapter.notifyDataSetChanged();
            }
        });

        int i = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            i++;
            cursor.moveToNext();
        }
        //Log.v(FetchScoreTask.LOG_TAG,"Loader query: " + String.valueOf(i));
        mAdapter.swapCursor(cursor);
        */
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
