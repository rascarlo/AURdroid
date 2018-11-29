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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UtilStringFormatter {

    public static String getConvertedUnixTimeString(long unixTime) {
        // convert unix time
        long timeFromUnixTime = unixTime * 1000L;
        // parse date
        try {
            Date date = (new Date(timeFromUnixTime));
            // format date
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm.sss aaa", Locale.getDefault());
            return simpleDateFormat.format(date);
        } catch (Exception ex) {
            return null;
        }
    }

}
