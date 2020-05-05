package com.rascarlo.aurdroid.utils

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rascarlo.aurdroid.R
import com.rascarlo.aurdroid.infoResult.DependencyAdapter
import com.rascarlo.aurdroid.network.AurDroidApiStatus
import java.util.*

@BindingAdapter("bindName")
fun TextView.bindName(string: String?) = string.let {
    text = it ?: resources.getString(R.string.not_available)
        .toLowerCase(Locale.getDefault())
}

@BindingAdapter("bindDescription")
fun TextView.bindDescription(string: String?) = string.let {
    text = it ?: resources.getString(R.string.not_available)
        .toLowerCase(Locale.getDefault())
}

@BindingAdapter("bindVersion")
fun TextView.bindVersion(string: String?) = string.let {
    text = resources.getString(
        R.string.formatted_version,
        it ?: resources.getString(R.string.not_available)
            .toLowerCase(Locale.getDefault())
    )
}

@BindingAdapter("bindPackageBase")
fun TextView.bindPackageBase(string: String?) = string.let {
    text = resources.getString(
        R.string.formatted_packagebase,
        it ?: resources.getString(R.string.not_available)
            .toLowerCase(Locale.getDefault())
    )
}

@BindingAdapter("bindUpstreamUrl")
fun TextView.bindUpstreamUrl(string: String?) {
    text = when {
        null != string && string.isNotEmpty() -> resources.getString(
            R.string.formatted_upstream_url,
            string
        )
        else -> resources.getString(R.string.not_available)
            .toLowerCase(Locale.getDefault())
    }
}

@BindingAdapter("bindGitCloneUrl")
fun TextView.bindGitCloneUrl(string: String?) {
    text = when {
        null != string && string.isNotEmpty() -> resources.getString(
            R.string.formatted_git_clone_url,
            string
        )
        else -> resources.getString(R.string.not_available)
            .toLowerCase(Locale.getDefault())
    }
}

@BindingAdapter("bindLicense")
fun TextView.bindLicense(list: List<String>?) = list.let {
    text = resources.getString(
        R.string.formatted_license,
        list?.joinToString(separator = resources.getString(R.string.unicode_comma_whitespace)) { it }
    )
}

@BindingAdapter("bindLastUpdate")
fun TextView.bindLastUpdate(long: Long?) = long.let {
    text = resources.getString(
        R.string.formatted_last_updated,
        convertUnixTime(
            it, resources
        ) ?: resources.getString(R.string.not_available).toLowerCase(Locale.getDefault())
    )
}

@BindingAdapter("bindFirstSubmitted")
fun TextView.bindFirstSubmitted(long: Long?) = long.let {
    text = resources.getString(
        R.string.formatted_first_submitted,
        convertUnixTime(
            it, resources
        ) ?: resources.getString(R.string.not_available).toLowerCase(Locale.getDefault())
    )
}

@BindingAdapter("bindOutOfDate")
fun TextView.bindOutOfDate(long: Long?) = when (long) {
    null -> visibility = View.GONE
    else -> {
        long.let {
            text = resources.getString(
                R.string.formatted_out_of_date,
                convertUnixTime(
                    it, resources
                )
            )
            visibility = View.VISIBLE
        }
    }
}

@BindingAdapter("bindVotes")
fun TextView.bindVotes(long: Long?) = long.let {
    text = resources.getString(
        R.string.formatted_votes,
        it ?: 0
    )
}

@BindingAdapter("bindPopularity")
fun TextView.bindPopularity(double: Double?) = double.let {
    text = resources.getString(R.string.formatted_popularity, it ?: 0.0)
}

@BindingAdapter("bindMaintainer")
fun TextView.bindMaintainer(string: String?) = when (string) {
    null -> {
        setTextColor(ContextCompat.getColor(context, R.color.red_alert))
        text = resources.getString(
            R.string.formatted_maintainer,
            (resources.getString(R.string.orphan).toLowerCase(
                Locale.getDefault()
            ))
        )
    }
    else -> {
        setTextColor(ContextCompat.getColor(context, R.color.primaryTextColor))
        string.let {
            text =
                resources.getString(
                    R.string.formatted_maintainer, it
                )
        }
    }
}

@BindingAdapter("bindRecyclerView")
fun bindRecyclerView(recyclerView: RecyclerView, list: List<String>?) {
    val adapter = recyclerView.adapter as? DependencyAdapter
    adapter?.submitList(list?.sortedBy { it })
}

@BindingAdapter("bindAurDroidApiStatusImageView")
fun bindAurDroidApiStatusImageView(imageView: ImageView, status: AurDroidApiStatus?) {
    when (status) {
        AurDroidApiStatus.ERROR -> {
            imageView.visibility = View.VISIBLE
            imageView.setImageResource(R.drawable.ic_error_outline_red_alert_24dp)
        }
        AurDroidApiStatus.LOADING -> {
            imageView.visibility = View.GONE
        }
        AurDroidApiStatus.DONE -> {
            imageView.visibility = View.GONE
        }
        AurDroidApiStatus.NO_PACKAGE_FOUND -> {
            imageView.visibility = View.VISIBLE
            imageView.setImageResource(R.drawable.ic_sentiment_very_dissatisfied_red_alert_24dp)
        }
        AurDroidApiStatus.RETURN_TYPE_ERROR -> {
            imageView.visibility = View.VISIBLE
            imageView.setImageResource(R.drawable.ic_sentiment_very_dissatisfied_red_alert_24dp)
        }
    }
}

@BindingAdapter("bindAurDroidApiStatusTextView")
fun bindAurDroidApiStatusTextView(textView: TextView, status: AurDroidApiStatus?) {
    when (status) {
        AurDroidApiStatus.ERROR -> {
            textView.visibility = View.VISIBLE
            textView.setText(R.string.something_went_wrong)
        }
        AurDroidApiStatus.LOADING -> {
            textView.visibility = View.VISIBLE
        }
        AurDroidApiStatus.DONE -> {
            textView.visibility = View.GONE
        }
        AurDroidApiStatus.NO_PACKAGE_FOUND -> {
            textView.visibility = View.VISIBLE
            textView.setText(R.string.no_package_found)
        }
        AurDroidApiStatus.RETURN_TYPE_ERROR -> {
            textView.visibility = View.GONE
        }
    }
}

@BindingAdapter("bindAurDroidApiErrorTextView")
fun bindAurDroidApiErrorTextView(textView: TextView, string: String?) {
    textView.visibility = View.VISIBLE
    textView.text = string
}

@BindingAdapter("bindAurDroidApiStatusProgressBar")
fun bindAurDroidApiStatusProgressBar(progressBar: ProgressBar, status: AurDroidApiStatus?) {
    when (status) {
        AurDroidApiStatus.ERROR -> {
            progressBar.visibility = View.GONE
        }
        AurDroidApiStatus.LOADING -> {
            progressBar.visibility = View.VISIBLE
        }
        AurDroidApiStatus.DONE -> {
            progressBar.visibility = View.GONE
        }
        AurDroidApiStatus.NO_PACKAGE_FOUND -> {
            progressBar.visibility = View.GONE
        }
        AurDroidApiStatus.RETURN_TYPE_ERROR -> {
            progressBar.visibility = View.GONE
        }
    }
}