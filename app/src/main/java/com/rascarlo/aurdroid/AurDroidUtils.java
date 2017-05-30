package com.rascarlo.aurdroid;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AurDroidUtils {

    private final Context context;

    public AurDroidUtils(Context context) {
        this.context = context;
    }

    /**
     * - convert UNIX time into java time
     * - parse date
     * - format date into human readable format
     *
     * @param unixTime: the returned time in UNIX format
     * @return UNIX time converted to java time
     */
    public String getConvertedUnixTimeString(Integer unixTime) {
        // convert unix time
        long timeFromUnixTime = unixTime * 1000L;
        // parse date
        try {
            Date date = (new Date(timeFromUnixTime));
            // format date
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aaa", Locale.getDefault());
            return simpleDateFormat.format(date);
        } catch (Exception ex) {
            return context.getResources().getString(R.string.not_available);
        }
    }

    /**
     * apply bold text to given length of string
     *
     * @param spannableString: string to be spanned
     * @param lengthToSpan:    length to span
     * @return a spannable string
     */
    public Spannable getSpannableBoldStyleText(String spannableString, int lengthToSpan) {
        Spannable spannable = new SpannableString(spannableString);
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        spannable.setSpan(styleSpan, 0, lengthToSpan, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spannable;
    }

    boolean isNetworkInfoConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
