package com.chenxiaoyu.bbcoin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String time2str(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
    }

    public static String timeFormat(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        return sdf.format(date);

    }

    public static String timePassed(Context context, Calendar calendar) {

        Calendar c2 = Calendar.getInstance();
        long l = c2.getTimeInMillis() - calendar.getTimeInMillis();

        Calendar ret = Calendar.getInstance();
        ret.set(0, 0, 1, 0, 0, 0);
        ret.add(Calendar.MILLISECOND, (int) l);

        int day = ret.get(Calendar.DAY_OF_YEAR);
        int hour = ret.get(Calendar.HOUR_OF_DAY);
        int min = ret.get(Calendar.MINUTE);
        int sec = ret.get(Calendar.SECOND);
        StringBuilder builder = new StringBuilder();
        if (day >= 2) {
            builder.append(day - 1);
            builder.append(context.getString(R.string.day));

        }
        if (hour != 0) {
            builder.append(hour);
            builder.append(context.getString(R.string.hour));
        }
        if (min != 0) {
            builder.append(min);
            builder.append(context.getString(R.string.minite));
        }
        builder.append(sec);
        builder.append(context.getString(R.string.second));
        return builder.toString();
    }

    public static String timePassed(Context context, Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return timePassed(context, calendar);
    }

    public static int timeCompareTo(Date d1, Date d2) {
        if (d1 == null || d2 == null) {
            return 0;
        }
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);

        return c1.compareTo(c2);
    }

    public static int numberRound(double d) {
        return new BigDecimal(d).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }

    public static int getScale(double v) {
        if (v != v || v == Double.POSITIVE_INFINITY || v == Double.NEGATIVE_INFINITY)
            return 0;//throw exception or return any other stuff

        BigDecimal d = new BigDecimal(v);
        return Math.max(0, d.stripTrailingZeros().scale());
    }

    public static Bitmap getBitmapFromView(View view, String text) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.BLACK);
        view.draw(canvas);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextSize(42);
        p.setColor(Color.WHITE);
        p.setShadowLayer(5.0f, 5.0f, 5.0f, Color.BLACK);
        Rect bounds = new Rect();

        p.getTextBounds(text, 0, text.length(), bounds);

        int x = (returnedBitmap.getWidth() - bounds.width() - 10);
        int y = (returnedBitmap.getHeight() - bounds.height() - 8);

        canvas.drawText(text, x, y, p);
        return returnedBitmap;
    }


    public static File readSDFile(String file) {
        File sdchard = Environment.getExternalStorageDirectory();
        return new File(sdchard, file);
    }

    public static void writeSDFile(byte[] bytes, String name) {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {

            inputStream = new ByteArrayInputStream(bytes);
            File sdchard = Environment.getExternalStorageDirectory();
            File file = new File(sdchard, name);
            outputStream = new FileOutputStream(file, false);
            byte[] buffer = new byte[1024];
            int length = 0;
            try {
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            } catch (IOException ioe) {
                /* ignore */
            }
        } catch (FileNotFoundException fnfe) {
	        /* ignore */
        } finally {
            try {
                inputStream.close();
            } catch (IOException ioe) {
	           /* ignore */
            }
            try {
                outputStream.close();
            } catch (IOException ioe) {
	           /* ignore */
            }
        }
    }

    public static Point getScreenSize(Context ctx) {
        Point ret = new Point();
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        ret.x = display.getWidth();
        ret.y = display.getHeight();
        return ret;
    }


}
