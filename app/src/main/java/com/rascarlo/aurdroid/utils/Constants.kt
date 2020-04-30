package com.rascarlo.aurdroid.utils

class Constants {

    companion object {
        // base url
        const val BASE_URL = "https://aur.archlinux.org/"

        // return types
        const val RETURN_TYPE_ERROR: String = "error"
        const val RETURN_TYPE_MULTIINFO: String = "multiinfo"
        const val RETURN_TYPE_SEARCH: String = "search"

        // search fields
        const val SEARCH_FIELD_NAME_OR_DESCRIPTION: String = "name-desc"
        const val SEARCH_FIELD_NAME: String = "name"
        const val SEARCH_FIELD_MAINTAINER: String = "maintainer"
        const val SEARCH_FIELD_DEPENDS: String = "depends"
        const val SEARCH_FIELD_MAKE_DEPENDS: String = "makedepends"
        const val SEARCH_FIELD_OPT_DEPENDS: String = "optdepends"
        const val SEARCH_FIELD_CHECK_DEPENDS: String = "checkdepends"

        // sort by
        const val SORT_BY_PACKAGE_NAME = 0
        const val SORT_BY_VOTES = 1
        const val SORT_BY_POPULARITY = 2
        const val SORT_BY_LAST_UPDATED = 3
        const val SORT_BY_FIRST_SUBMITTED = 4

        const val AUR_PACKAGES_BASE_URL = "https://aur.archlinux.org/packages/"
        const val AUR_PACKAGE_PKGBUILD_BASE_URL =
            "https://aur.archlinux.org/cgit/aur.git/tree/PKGBUILD"
        const val AUR_PACKAGE_SNAPSHOT_BASE_URL =
            "https://aur.archlinux.org"
        const val AUR_PACKAGE_LOG_BASE_URL = "https://aur.archlinux.org/cgit/aur.git/log/"
        const val AUR_PACKAGES_GIT_CLONE_BASE_URL = "https://aur.archlinux.org/"
    }
}