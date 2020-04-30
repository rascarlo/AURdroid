package com.rascarlo.aurdroid.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchResult(
    @Json(name = "ID") val id: Long? = null,
    @Json(name = "Name") val name: String? = null,
    @Json(name = "PackageBaseID") val packageBaseId: Long? = null,
    @Json(name = "PackageBase") val packageBase: String? = null,
    @Json(name = "Version") val version: String? = null,
    @Json(name = "Description") val description: String? = null,
    @Json(name = "URL") val url: String? = null,
    @Json(name = "NumVotes") val numVotes: Long? = null,
    @Json(name = "Popularity") val popularity: Double? = null,
    @Json(name = "OutOfDate") val outOfDate: Long? = null,
    @Json(name = "Maintainer") var maintainer: String? = null,
    @Json(name = "FirstSubmitted") var firstSubmitted: Long? = null,
    @Json(name = "LastModified") var lastModified: Long? = null,
    @Json(name = "URLPath") val urlPath: String? = null
) : Parcelable