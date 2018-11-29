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

public class AurdroidConstants {
    public static final String AUR_API_BASE_URL = "https://aur.archlinux.org/";

    public static final int SEARCH_PARAMETER_NAME_OR_DESCRIPTION = 0;
    public static final int SEARCH_PARAMETER_NAME = 1;
    public static final int SEARCH_PARAMETER_MAINTAINER = 2;
    public static final int SEARCH_PARAMETER_DEPENDS = 3;
    public static final int SEARCH_PARAMETER_MAKE_DEPENDS = 4;
    public static final int SEARCH_PARAMETER_OPT_DEPENDS = 5;
    public static final int SEARCH_PARAMETER_CHECK_DEPENDS = 6;

    public static final int SEARCH_RESULT_SORT_BY_PACKAGE_NAME = 0;
    public static final int SEARCH_RESULT_SORT_BY_VOTES = 1;
    public static final int SEARCH_RESULT_SORT_BY_POPULARITY = 2;
    public static final int SEARCH_RESULT_SORT_BY_LAST_UPDATED = 3;
    public static final int SEARCH_RESULT_SORT_BY_FIRST_SUBMITTED = 4;

    public static final String AUR_PACKAGES_BASE_URL = "https://aur.archlinux.org/packages/";
    public static final String AUR_PACKAGE_PKGBUILD_BASE_URL = "https://aur.archlinux.org/cgit/aur.git/tree/PKGBUILD";
    public static final String AUR_PACKAGE_LOG_BASE_URL = "https://aur.archlinux.org/cgit/aur.git/log/";
}