/*
 *     Copyright (C) rascarlo  rascarlo@gmail.com
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.rascarlo.aurdroid.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AurdroidSharedPreferences {

    public static void setSharedPreferenceString(Context context, String sharedPreferenceString, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(sharedPreferenceString, value);
        editor.apply();
    }

    public static String getSharedPreferenceString(Context context, String sharedPreferenceString, String defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(sharedPreferenceString, defaultValue);
    }

    public static void setSharedPreferenceBoolean(Context context, String sharedPreferenceString, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(sharedPreferenceString, value);
        editor.apply();
    }

    public static boolean getSharedPreferenceBoolean(Context context, String sharedPreference, boolean defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(sharedPreference, defaultValue);
    }

}
