package com.rascarlo.aurdroid.utils

enum class ReturnTypeEnum {
    ERROR {
        override fun toString(): String = "error"
    },
    MULTIINFO {
        override fun toString(): String = "multiinfo"
    },
    SEARCH {
        override fun toString(): String = "search"
    }
}