package barqsoft.footballscores;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.format.Time;
import android.view.View;
import android.widget.ListView;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.Date;

import barqsoft.footballscores.service.MyWidgetUpdateService;
import barqsoft.footballscores.service.myFetchService;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    public static final int SCORES_LOADER = 0;
    private String[] fragmentdate = new String[1];

    private Context m_context;


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            Intent service_start = new Intent(context, MyWidgetUpdateService.class);
            service_start.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

            context.startService(service_start);
        }

//        // There may be multiple widgets active, so update all of them
//        final int N = appWidgetIds.length;
//        for (int i = 0; i < N; i++) {
//
//            // Construct the RemoteViews object
//            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
//
//            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
//        }
        super.onUpdate( context, appWidgetManager, appWidgetIds);

    }



    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        Time now = new Time();
        now.setToNow();
        CharSequence widgetText = now.toString();
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
//        views.setTextViewText(R.id.appwidget_text, widgetText);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}

