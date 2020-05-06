package com.rascarlo.aurdroid.utils

enum class FieldEnum {
    NAME_OR_DESCRIPTION {
        override fun toString(): String = "name-desc"
    },
    NAME {
        override fun toString(): String = "name"
    },
    MAINTAINER {
        override fun toString(): String = "maintainer"
    },
    DEPENDS {
        override fun toString(): String = "depends"
    },
    MAKE_DEPENDS {
        override fun toString(): String = "makedepends"
    },
    OPT_DEPENDS {
        override fun toString(): String = "optdepends"
    },
    CHECK_DEPENDS {
        override fun toString(): String = "checkdepends"
    }
}