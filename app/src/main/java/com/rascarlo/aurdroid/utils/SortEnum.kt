package com.rascarlo.aurdroid.utils

enum class SortEnum {
    PACKAGE_NAME {
        override fun toString(): String = "package_name"
    },
    VOTES {
        override fun toString(): String = "votes"
    },
    POPULARITY {
        override fun toString(): String = "popularity"
    },
    LAST_UPDATED {
        override fun toString(): String = "last_updated"
    },
    FIRST_SUBMITTED {
        override fun toString(): String = "first_submitted"
    }
}