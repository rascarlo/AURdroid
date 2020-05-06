package com.rascarlo.aurdroid.utils

import android.content.res.Resources
import com.rascarlo.aurdroid.R
import com.rascarlo.aurdroid.network.SearchResult
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

/**
 * convert long unit time
 * @param unixTime: long for date
 * @param resources: to get string resource
 * @return simple date format string
 */
fun convertUnixTime(unixTime: Long?, resources: Resources): String? = if (null != unixTime) {
    try {
        val date = Date(unixTime.times(1000L))
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm.sss aaa", Locale.getDefault())
        simpleDateFormat.format(date)
    } catch (e: Exception) {
        Timber.e(e)
        resources.getString(R.string.not_available)
    }
} else {
    resources.getString(R.string.not_available)
}

/**
 * sort search result list
 * @param resultList: list to sort
 * @param sort: sort by
 * [SortEnum.PACKAGE_NAME]
 * [SortEnum.VOTES]
 * [SortEnum.POPULARITY]
 * [SortEnum.LAST_UPDATED]
 * [SortEnum.FIRST_SUBMITTED]
 */
fun sortSearchResultList(resultList: List<SearchResult>, sort: String): List<SearchResult>? {
    return when (sort) {
        SortEnum.PACKAGE_NAME.toString() -> resultList.sortedBy { resultSearch -> resultSearch.name }
        SortEnum.VOTES.toString() -> resultList.sortedByDescending { resultSearch -> resultSearch.numVotes }
        SortEnum.POPULARITY.toString() -> resultList.sortedByDescending { resultSearch -> resultSearch.popularity }
        SortEnum.LAST_UPDATED.toString() -> resultList.sortedByDescending { resultSearch -> resultSearch.lastModified }
        SortEnum.FIRST_SUBMITTED.toString() -> resultList.sortedByDescending { resultSearch -> resultSearch.firstSubmitted }
        else -> resultList.sortedBy { resultSearch -> resultSearch.name }
    }
}