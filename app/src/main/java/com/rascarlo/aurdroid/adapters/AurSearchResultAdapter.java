package com.rascarlo.aurdroid.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rascarlo.aurdroid.AurDroidUtils;
import com.rascarlo.aurdroid.R;
import com.rascarlo.aurdroid.api.model.aur.search.AurSearchResult;

import java.util.List;

public class AurSearchResultAdapter extends RecyclerView.Adapter<AurSearchResultAdapter.ViewHolder> {

    private Context context;
    private Resources resources;
    private final List<AurSearchResult> aurSearchResultList;
    private final AurSearchResultAdapterCallback aurSearchResultAdapterCallback;
    private AurDroidUtils aurDroidUtils;

    public AurSearchResultAdapter(List<AurSearchResult> aurSearchResultList, AurSearchResultAdapterCallback aurSearchResultAdapterCallback) {
        this.aurSearchResultList = aurSearchResultList;
        this.aurSearchResultAdapterCallback = aurSearchResultAdapterCallback;
    }

    @NonNull
    @Override
    public AurSearchResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        resources = context.getResources();
        aurDroidUtils = new AurDroidUtils(context);
        View itemView = LayoutInflater.from(context).inflate(R.layout.aur_search_result_item_view_holder, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AurSearchResultAdapter.ViewHolder holder, int position) {
        final AurSearchResult aurSearchResult = aurSearchResultList.get(position);
        // name
        holder.packageName.setText(aurSearchResult.name == null | TextUtils.isEmpty(aurSearchResult.name) ?
                resources.getString(R.string.not_available).toLowerCase()
                : aurSearchResult.name);
        // description
        holder.packageDescription.setText(aurSearchResult.description == null | TextUtils.isEmpty(aurSearchResult.description) ?
                resources.getString(R.string.not_available).toLowerCase()
                : aurSearchResult.description);
        // version
        setUpVersion(holder, aurSearchResult);
        // maintainer
        setUpMaintainer(holder, aurSearchResult);
        // votes
        setUpVotes(holder, aurSearchResult);
        // popularity
        setUpPopularity(holder, aurSearchResult);
        // last modified
        setUpLastModified(holder, aurSearchResult);
        // first submitted
        setUpFirstSubmitted(holder, aurSearchResult);
        // flagged out of date
        setUpFlaggedOutOfDate(holder, aurSearchResult);
        // on click
        if (aurSearchResult.name != null
                && !TextUtils.isEmpty(aurSearchResult.name)
                && aurSearchResultAdapterCallback != null) {
            holder.mainContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    aurSearchResultAdapterCallback.onItemClicked(aurSearchResult.name);
                }
            });
        }
    }

    private void setUpVersion(AurSearchResultAdapter.ViewHolder holder, AurSearchResult aurSearchResult) {
        String string = String.format(resources.getString(R.string.formatted_version),
                aurSearchResult.version == null | TextUtils.isEmpty(aurSearchResult.version) ?
                        resources.getString(R.string.not_available).toLowerCase()
                        : aurSearchResult.version);
        holder.packageVersion.setText(aurDroidUtils.getSpannableBoldStyleText(string,
                resources.getString(R.string.formatted_version).length() - 3));
    }

    private void setUpMaintainer(AurSearchResultAdapter.ViewHolder holder, AurSearchResult aurSearchResult) {
        String string = String.format(resources.getString(R.string.formatted_maintainer),
                aurSearchResult.maintainer == null | TextUtils.isEmpty(aurSearchResult.maintainer) ?
                        resources.getString(R.string.orphan)
                        : aurSearchResult.maintainer);
        holder.packageMaintainer.setText(aurDroidUtils.getSpannableBoldStyleText(string,
                resources.getString(R.string.formatted_maintainer).length() - 3));
        if (aurSearchResult.maintainer == null | TextUtils.isEmpty(aurSearchResult.maintainer))
            holder.packageMaintainer.setTextColor(ContextCompat.getColor(context, R.color.text_alert_red));
    }

    private void setUpVotes(AurSearchResultAdapter.ViewHolder holder, AurSearchResult aurSearchResult) {
        String string = String.format(resources.getString(R.string.formatted_votes),
                aurSearchResult.numVotes);
        holder.packageVotes.setText(aurDroidUtils.getSpannableBoldStyleText(string,
                resources.getString(R.string.formatted_votes).length() - 3));
    }

    private void setUpPopularity(AurSearchResultAdapter.ViewHolder holder, AurSearchResult aurSearchResult) {
        String string = String.format(resources.getString(R.string.formatted_popularity),
                aurSearchResult.popularity);
        holder.packagePopularity.setText(aurDroidUtils.getSpannableBoldStyleText(string,
                resources.getString(R.string.formatted_popularity).length() - 3));
    }

    private void setUpLastModified(AurSearchResultAdapter.ViewHolder holder, AurSearchResult aurSearchResult) {
        String string = String.format(resources.getString(R.string.formatted_last_updated),
                aurDroidUtils.getConvertedUnixTimeString(aurSearchResult.lastModified));
        holder.packageLastUpdated.setText(aurDroidUtils.getSpannableBoldStyleText(string,
                resources.getString(R.string.formatted_last_updated).length() - 3));
    }

    private void setUpFirstSubmitted(AurSearchResultAdapter.ViewHolder holder, AurSearchResult aurSearchResult) {
        String string = String.format(resources.getString(R.string.formatted_first_submitted),
                aurDroidUtils.getConvertedUnixTimeString(aurSearchResult.firstSubmitted));
        holder.packageFirstSubmitted.setText(aurDroidUtils.getSpannableBoldStyleText(string,
                resources.getString(R.string.formatted_first_submitted).length() - 3));
    }

    private void setUpFlaggedOutOfDate(AurSearchResultAdapter.ViewHolder holder, AurSearchResult aurSearchResult) {
        if (!TextUtils.isEmpty(aurSearchResult.outOfDate)) {
            holder.packageFlaggedOutOfDate.setVisibility(View.VISIBLE);
            String string = String.format(resources.getString(R.string.formatted_flagged_out_of_date),
                    aurDroidUtils.getConvertedUnixTimeString(Integer.parseInt(aurSearchResult.outOfDate)));
            holder.packageFlaggedOutOfDate.setText(aurDroidUtils.getSpannableBoldStyleText(string,
                    resources.getString(R.string.formatted_flagged_out_of_date).length() - 3));
        } else {
            holder.packageFlaggedOutOfDate.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return aurSearchResultList.size();
    }

    public void addAurSearchResult(AurSearchResult aurSearchResult) {
        aurSearchResultList.add(aurSearchResultList.size(), aurSearchResult);
        notifyItemInserted(aurSearchResultList.size());
    }

    public void clearAll() {
        aurSearchResultList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout mainContainer;
        private final TextView packageName;
        private final TextView packageDescription;
        private final TextView packageVersion;
        private final TextView packageMaintainer;
        private final TextView packageVotes;
        private final TextView packagePopularity;
        private final TextView packageLastUpdated;
        private final TextView packageFirstSubmitted;
        private final TextView packageFlaggedOutOfDate;

        ViewHolder(View itemView) {
            super(itemView);
            mainContainer = itemView.findViewById(R.id.aur_search_result_view_holder_main_container);
            packageName = itemView.findViewById(R.id.aur_search_result_view_holder_package_name_text_view);
            packageDescription = itemView.findViewById(R.id.aur_search_result_view_holder_package_description_text_view);
            packageVersion = itemView.findViewById(R.id.aur_search_result_view_holder_package_version_text_view);
            packageMaintainer = itemView.findViewById(R.id.aur_search_result_view_holder_package_maintainer_text_view);
            packageVotes = itemView.findViewById(R.id.aur_search_result_view_holder_package_votes_text_view);
            packagePopularity = itemView.findViewById(R.id.aur_search_result_view_holder_package_popularity_text_view);
            packageLastUpdated = itemView.findViewById(R.id.aur_search_result_view_holder_package_last_updated_text_view);
            packageFirstSubmitted = itemView.findViewById(R.id.aur_search_result_view_holder_package_first_submitted_text_view);
            packageFlaggedOutOfDate = itemView.findViewById(R.id.aur_search_result_view_holder_package_flagged_out_of_date_text_view);
        }
    }

    public interface AurSearchResultAdapterCallback {
        void onItemClicked(String packageName);
    }
}
