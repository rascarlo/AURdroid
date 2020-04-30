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
 * [Constants.SORT_BY_PACKAGE_NAME]
 * [Constants.SORT_BY_VOTES]
 * [Constants.SORT_BY_POPULARITY]
 * [Constants.SORT_BY_LAST_UPDATED]
 * [Constants.SORT_BY_FIRST_SUBMITTED]
 */
fun sortSearchResultList(resultList: List<SearchResult>, sort: Int): List<SearchResult>? {
    return when (sort) {
        Constants.SORT_BY_PACKAGE_NAME -> resultList.sortedBy { resultSearch -> resultSearch.name }
        Constants.SORT_BY_VOTES -> resultList.sortedByDescending { resultSearch -> resultSearch.numVotes }
        Constants.SORT_BY_POPULARITY -> resultList.sortedByDescending { resultSearch -> resultSearch.popularity }
        Constants.SORT_BY_LAST_UPDATED -> resultList.sortedByDescending { resultSearch -> resultSearch.lastModified }
        Constants.SORT_BY_FIRST_SUBMITTED -> resultList.sortedByDescending { resultSearch -> resultSearch.firstSubmitted }
        else -> resultList.sortedBy { resultSearch -> resultSearch.name }
    }
}