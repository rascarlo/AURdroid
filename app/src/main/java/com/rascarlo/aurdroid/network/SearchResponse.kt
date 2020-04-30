package com.rascarlo.aurdroid.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchResponse(
    @Json(name = "version") val version: Int? = null,
    @Json(name = "type") val type: String? = null,
    @Json(name = "resultcount") val resultCount: Long? = null,
    @Json(name = "results") val results: List<SearchResult>? = null,
    @Json(name = "error") val error: String? = null
) : Parcelable