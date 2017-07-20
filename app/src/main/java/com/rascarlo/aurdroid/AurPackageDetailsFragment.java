package com.rascarlo.aurdroid;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rascarlo.aurdroid.api.AurRpcApi;
import com.rascarlo.aurdroid.api.AurRpcService;
import com.rascarlo.aurdroid.api.model.aur.info.AurInfoObject;
import com.rascarlo.aurdroid.api.model.aur.info.AurInfoResult;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AurPackageDetailsFragment extends Fragment implements Callback<AurInfoObject> {
    private static final String LOG_TAG = AurPackageDetailsFragment.class.getSimpleName();
    private static final String BUNDLE_AUR_PACKAGE_NAME = "bundle_aur_package_name";
    private static final String AUR_WEB_PACKAGES_BASE_URL = "https://aur.archlinux.org/packages/";
    private static final String AUR_PACKAGE_PKGBUILD_BASE_URL = "https://aur.archlinux.org/cgit/aur.git/tree/PKGBUILD";
    private static final String AUR_PACKAGE_LOG_BASE_URL = "https://aur.archlinux.org/cgit/aur.git/log/";
    private String bundleAurPackageName;

    private AurPackageDetailsFragmentCallback aurPackageDetailsFragmentCallback;
    private AurDroidUtils aurDroidUtils;
    private Context context;
    private Resources resources;
    // views
    private View rootView;
    private ProgressBar progressBar;
    private View rulerView;
    // retrofit
    private Call<AurInfoObject> call;
    private AurInfoResult responseAurInfoResult;

    public AurPackageDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * instantiates a new instance of the fragment
     * with a given package name
     *
     * @param bundlePackageName: parameter to query
     * @return a new @{@link AurPackageDetailsFragment}
     */
    public static AurPackageDetailsFragment newInstance(@NonNull String bundlePackageName) {
        AurPackageDetailsFragment fragment = new AurPackageDetailsFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_AUR_PACKAGE_NAME, bundlePackageName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bundleAurPackageName = getArguments().getString(BUNDLE_AUR_PACKAGE_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        setRetainInstance(true);
        context = getActivity();
        resources = getActivity().getResources();
        // retrofit
        AurRpcApi aurRpcApi = AurRpcService.getAurRpcApi();
        // utils
        aurDroidUtils = new AurDroidUtils(context);
        call = aurRpcApi.infoByPackageName(bundleAurPackageName);
        // views
        rootView = inflater.inflate(R.layout.fragment_aur_package_details, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.fragment_aur_package_details_progress_bar);
        rulerView = rootView.findViewById(R.id.fragment_aur_package_details_ruler);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // show progress bar
        progressBar.setVisibility(View.VISIBLE);
        // scale ruler width
        rulerView.setScaleX(0.0F);
        if (savedInstanceState == null) {
            if (call != null) {
                call.enqueue(this);
            }
        } else {
            if (responseAurInfoResult != null) {
                processResponseAurInfoResult();
            }
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AurPackageDetailsFragmentCallback) {
            aurPackageDetailsFragmentCallback = (AurPackageDetailsFragmentCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AurPackageDetailsFragmentCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        aurPackageDetailsFragmentCallback = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_aur_package_details_, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // view PKGBUILD
        MenuItem viewPkgbuild = menu.findItem(R.id.menu_aur_package_details_view_pkgbuild);
        viewPkgbuild.setEnabled(getResponseAurInfoResultPkgbuildUri() != null);
        // view changes
        MenuItem viewChanges = menu.findItem(R.id.menu_aur_package_details_view_changes);
        viewChanges.setEnabled(getResponseAurInfoResultLogUri() != null);
        // browse maintainer
        MenuItem browseMaintainer = menu.findItem(R.id.menu_aur_package_details_browse_maintainer);
        browseMaintainer.setEnabled(responseAurInfoResult!=null && responseAurInfoResult.maintainer != null && !TextUtils.isEmpty(responseAurInfoResult.maintainer));
        // open in browser
        MenuItem openInBrowser = menu.findItem(R.id.menu_aur_package_details_open_in_browser);
        openInBrowser.setEnabled(getResponseAurInfoResultUri() != null);
        // share
        MenuItem shareItem = menu.findItem(R.id.menu_aur_package_details_share);
        shareItem.setEnabled(getResponseAurInfoResultUri() != null);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        // view PKGBUILD
        if (i == R.id.menu_aur_package_details_view_pkgbuild) {
            if (getResponseAurInfoResultPkgbuildUri() != null) {
                startIntentViewUri(getResponseAurInfoResultPkgbuildUri());
                return true;
            }
            // view changes
        } else if (i == R.id.menu_aur_package_details_view_changes) {
            if (getResponseAurInfoResultLogUri() != null) {
                startIntentViewUri(getResponseAurInfoResultLogUri());
                return true;
            }
        }
        // browse maintainer
        // check for valid string
        if (responseAurInfoResult!=null && responseAurInfoResult.maintainer != null && !TextUtils.isEmpty(responseAurInfoResult.maintainer)) {
            if (i == R.id.menu_aur_package_details_browse_maintainer) {
                if (aurPackageDetailsFragmentCallback != null) {
                    aurPackageDetailsFragmentCallback.onAurPackageDetailsFragmentBrowseMaintainer(responseAurInfoResult.maintainer);
                }
                return true;
            }
        }
        // open in browser and check
        // they depend on responseAurInfoResultUri
        if (getResponseAurInfoResultUri()!=null) {
            // open in browser
            if (i == R.id.menu_aur_package_details_open_in_browser) {
                startIntentViewUri(getResponseAurInfoResultUri());
                return true;
                // share
            } else if (i == R.id.menu_aur_package_details_share) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, getResponseAurInfoResultUri().toString());
                intent.setType("text/plain");
                try {
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, resources.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        } else {
            Toast.makeText(context, resources.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void startIntentViewUri(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, resources.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
        }
    }

    private void processResponseAurInfoResult() {
        // package name
        TextView packageNameTextView = (TextView) rootView.findViewById(R.id.aur_package_details_package_name_text_view);
        packageNameTextView.setText(responseAurInfoResult.name);
        // package version
        TextView packageVersionTextView = (TextView) rootView.findViewById(R.id.aur_package_details_package_version_text_view);
        packageVersionTextView.setText(responseAurInfoResult.version);
        // package description
        TextView packageDescriptionTextView = (TextView) rootView.findViewById(R.id.aur_package_details_package_description_text_view);
        packageDescriptionTextView.setText(responseAurInfoResult.description);
        // community
        setUpCommunity();
        // timestamps
        setUpTimestamps();
        // responseAurInfoResultMaintainer
        setUpMaintainer();
        // depends
        setUpDepends();
        // make depends
        setUpMakeDepends();
        // optional depends
        setUpOptionalDepends();
        // provides
        setUpProvides();
        // conflicts
        setUpConflicts();
        // hide progress bar
        progressBar.setVisibility(View.GONE);
        // animate ruler
        ViewCompat.animate(rulerView)
                .scaleX(1.0F)
                .setDuration(1000)
                .setInterpolator(new OvershootInterpolator())
                .start();
    }

    private void setUpCommunity() {
        CardView cardView = (CardView) rootView.findViewById(R.id.aur_package_details_community_card_view);
        cardView.setVisibility(View.VISIBLE);
        TextView votesTextView = (TextView) rootView.findViewById(R.id.aur_package_details_votes_text_view);
        TextView popularityTextView = (TextView) rootView.findViewById(R.id.aur_package_details_popularity_text_view);
        // votes
        if (responseAurInfoResult.numVotes != null) {
            votesTextView.setText(String.format(Locale.getDefault(), "%d", responseAurInfoResult.numVotes));
        }
        // popularity
        if (responseAurInfoResult.popularity != null) {
            popularityTextView.setText(String.format(Locale.getDefault(), "%f", responseAurInfoResult.popularity));
        }
    }

    private void setUpTimestamps() {
        CardView cardView = (CardView) rootView.findViewById(R.id.aur_package_details_timestamps_card_view);
        cardView.setVisibility(View.VISIBLE);
        TextView lastUpdatedTextView = (TextView) rootView.findViewById(R.id.aur_package_details_last_updated_text_view);
        TextView firstSubmittedTextView = (TextView) rootView.findViewById(R.id.aur_package_details_first_submitted_text_view);
        TextView flaggedOutOfDateTextView = (TextView) rootView.findViewById(R.id.aur_package_details_flagged_out_of_date_text_view);
        TextView flaggedOutOfDateLabelTextView = (TextView) rootView.findViewById(R.id.aur_package_details_flagged_out_of_date_label_text_view);
        // last updated
        lastUpdatedTextView.setText(String.format("%s", responseAurInfoResult.lastModified == null ?
                resources.getString(R.string.not_available)
                : aurDroidUtils.getConvertedUnixTimeString(responseAurInfoResult.lastModified)));
        // first submitted
        firstSubmittedTextView.setText(String.format("%s", responseAurInfoResult.firstSubmitted == null ?
                resources.getString(R.string.not_available)
                : aurDroidUtils.getConvertedUnixTimeString(responseAurInfoResult.firstSubmitted)));
        // flagged out of date
        if (responseAurInfoResult.outOfDate != null) {
            flaggedOutOfDateLabelTextView.setVisibility(TextUtils.isEmpty(responseAurInfoResult.outOfDate) ?
                    View.GONE : View.VISIBLE);
            flaggedOutOfDateTextView.setVisibility(TextUtils.isEmpty(responseAurInfoResult.outOfDate) ?
                    View.GONE : View.VISIBLE);
            flaggedOutOfDateTextView.setText(String.format("%s", TextUtils.isEmpty(responseAurInfoResult.outOfDate) ?
                    responseAurInfoResult.outOfDate
                    : aurDroidUtils.getConvertedUnixTimeString(Integer.parseInt(responseAurInfoResult.outOfDate))));
        }
    }

    private void setUpMaintainer() {
        CardView cardView = (CardView) rootView.findViewById(R.id.aur_package_details_maintainer_card_view);
        cardView.setVisibility(View.VISIBLE);
        TextView maintainerLabelTextView = (TextView) rootView.findViewById(R.id.aur_package_details_maintainer_label_text_view);
        TextView maintainerTextView = (TextView) rootView.findViewById(R.id.aur_package_details_maintainer_text_view);
        TextView licenseTextView = (TextView) rootView.findViewById(R.id.aur_package_details_license_text_view);
        TextView upstreamUrlTextView = (TextView) rootView.findViewById(R.id.aur_package_details_upstream_url_text_view);
        // responseAurInfoResultMaintainer
        if (responseAurInfoResult.maintainer != null) {
            if (responseAurInfoResult.maintainer.isEmpty()) {
                maintainerTextView.setText(resources.getString(R.string.orphan));
                maintainerTextView.setTextColor(ContextCompat.getColor(context, R.color.text_alert_red));
                maintainerLabelTextView.setTextColor(ContextCompat.getColor(context, R.color.text_alert_red));
            } else {
                maintainerTextView.setText(responseAurInfoResult.maintainer);
            }
        } else {
            maintainerTextView.setText(resources.getString(R.string.orphan));
            maintainerTextView.setTextColor(ContextCompat.getColor(context, R.color.text_alert_red));
            maintainerLabelTextView.setTextColor(ContextCompat.getColor(context, R.color.text_alert_red));
        }
        // license
        if (responseAurInfoResult.license != null) {
            licenseTextView.setText(responseAurInfoResult.license.isEmpty() ?
                    resources.getString(R.string.not_available)
                    : getFormattedStringList(responseAurInfoResult.license, false, ", "));
        }
        // upstream url
        if (responseAurInfoResult.uRL != null) {
            upstreamUrlTextView.setText(TextUtils.isEmpty(responseAurInfoResult.uRL) ?
                    resources.getString(R.string.not_available)
                    : responseAurInfoResult.uRL);
        }
    }

    private void setUpDepends() {
        CardView dependsCardView = (CardView) rootView.findViewById(R.id.aur_package_details_depends_card_view);
        TextView dependsLabelTextView = (TextView) rootView.findViewById(R.id.aur_package_details_depends_label_text_view);
        TextView dependsTextView = (TextView) rootView.findViewById(R.id.aur_package_details_depends_text_view);
        if (responseAurInfoResult.depends != null && !responseAurInfoResult.depends.isEmpty()) {
            dependsCardView.setVisibility(View.VISIBLE);
            dependsLabelTextView.setText(resources.getQuantityString(R.plurals.plurals_formatted_depends,
                    responseAurInfoResult.depends.size(),
                    responseAurInfoResult.depends.size()));
            dependsTextView.setText(getFormattedStringList(responseAurInfoResult.depends, true, "\n"));
        } else {
            dependsCardView.setVisibility(View.GONE);
        }
    }

    private void setUpMakeDepends() {
        CardView makeDependsCardView = (CardView) rootView.findViewById(R.id.aur_package_details_make_depends_card_view);
        TextView makeDependsLabelTextView = (TextView) rootView.findViewById(R.id.aur_package_details_make_depends_label_text_view);
        TextView makeDependsTextView = (TextView) rootView.findViewById(R.id.aur_package_details_make_depends_text_view);
        if (responseAurInfoResult.makeDepends != null && !responseAurInfoResult.makeDepends.isEmpty()) {
            makeDependsCardView.setVisibility(View.VISIBLE);
            makeDependsLabelTextView.setText(resources.getQuantityString(R.plurals.plurals_formatted_make_depends,
                    responseAurInfoResult.makeDepends.size(),
                    responseAurInfoResult.makeDepends.size()));
            makeDependsTextView.setText(getFormattedStringList(responseAurInfoResult.makeDepends, true, "\n"));
        } else {
            makeDependsCardView.setVisibility(View.GONE);
        }
    }

    private void setUpOptionalDepends() {
        CardView optionalDependsCardView = (CardView) rootView.findViewById(R.id.aur_package_details_optional_depends_card_view);
        TextView optionalDependsLabelTextView = (TextView) rootView.findViewById(R.id.aur_package_details_optional_depends_label_text_view);
        TextView optionalDependsTextView = (TextView) rootView.findViewById(R.id.aur_package_details_optional_depends_text_view);
        if (responseAurInfoResult.optDepends != null && !responseAurInfoResult.optDepends.isEmpty()) {
            optionalDependsCardView.setVisibility(View.VISIBLE);
            optionalDependsLabelTextView.setText(resources.getQuantityString(R.plurals.plurals_formatted_optional_depends,
                    responseAurInfoResult.optDepends.size(),
                    responseAurInfoResult.optDepends.size()));
            optionalDependsTextView.setText(getFormattedStringList(responseAurInfoResult.optDepends, true, "\n"));
        } else {
            optionalDependsCardView.setVisibility(View.GONE);
        }
    }

    private void setUpProvides() {
        CardView providesCardView = (CardView) rootView.findViewById(R.id.aur_package_details_provides_card_view);
        TextView providesLabelTextView = (TextView) rootView.findViewById(R.id.aur_package_details_provides_label_text_view);
        TextView providesTextView = (TextView) rootView.findViewById(R.id.aur_package_details_provides_text_view);
        if (responseAurInfoResult.provides != null && !responseAurInfoResult.provides.isEmpty()) {
            providesCardView.setVisibility(View.VISIBLE);
            providesLabelTextView.setText(resources.getString(R.string.formatted_provides,
                    responseAurInfoResult.provides.size()));
            providesTextView.setText(getFormattedStringList(responseAurInfoResult.provides, true, "\n"));
        } else {
            providesCardView.setVisibility(View.GONE);
        }
    }

    private void setUpConflicts() {
        CardView conflictsCardView = (CardView) rootView.findViewById(R.id.aur_package_details_conflicts_card_view);
        TextView conflictsLabelTextView = (TextView) rootView.findViewById(R.id.aur_package_details_conflicts_label_text_view);
        TextView conflictsTextView = (TextView) rootView.findViewById(R.id.aur_package_details_conflicts_text_view);
        if (responseAurInfoResult.conflicts != null && !responseAurInfoResult.conflicts.isEmpty()) {
            conflictsCardView.setVisibility(View.VISIBLE);
            conflictsLabelTextView.setText(resources.getQuantityString(R.plurals.plurals_formatted_conflicts,
                    responseAurInfoResult.conflicts.size(),
                    responseAurInfoResult.conflicts.size()));
            conflictsTextView.setText(getFormattedStringList(responseAurInfoResult.conflicts, true, "\n"));
        } else {
            conflictsCardView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResponse(@NonNull Call<AurInfoObject> call, @NonNull Response<AurInfoObject> response) {
        if (response.isSuccessful() && response.code() == 200) {
            AurInfoObject aurInfoObject = response.body();
            if (aurInfoObject != null) {
                if (aurInfoObject.aurInfoResults != null
                        && TextUtils.equals(aurInfoObject.type, resources.getString(R.string.multiinfo).toLowerCase())
                        && !aurInfoObject.aurInfoResults.isEmpty()
                        && aurInfoObject.aurInfoResults.size() > 0) {
                    responseAurInfoResult = aurInfoObject.aurInfoResults.get(0);
                    processResponseAurInfoResult();
                } else {
                    Log.e(LOG_TAG, "onResponse: onResponse EMPTY successful.");
                    if (aurPackageDetailsFragmentCallback != null) {
                        aurPackageDetailsFragmentCallback.onAurPackageDetailsFragmentOnResponseEmptyResult(bundleAurPackageName);
                    }
                }
            } else {
                Log.e(LOG_TAG, "onResponse: NULL object");
                if (aurPackageDetailsFragmentCallback != null) {
                    aurPackageDetailsFragmentCallback.onAurPackageDetailsFragmentOnResponseNotSuccessful(bundleAurPackageName);
                }
            }
        } else {
            Log.e(LOG_TAG, "onResponse: onResponse NOT successful.");
            if (aurPackageDetailsFragmentCallback != null) {
                aurPackageDetailsFragmentCallback.onAurPackageDetailsFragmentOnResponseNotSuccessful(bundleAurPackageName);
            }
        }
    }

    @Override
    public void onFailure(@NonNull Call<AurInfoObject> call, @NonNull Throwable t) {
        if (aurPackageDetailsFragmentCallback != null) {
            aurPackageDetailsFragmentCallback.onAurPackageDetailsFragmentOnResponseOnFailure(bundleAurPackageName, t.getMessage());
        }
    }

    private Uri getResponseAurInfoResultUri() {
        if (responseAurInfoResult != null
                && responseAurInfoResult.name != null
                && !TextUtils.isEmpty(responseAurInfoResult.name)) {
            return Uri.parse(AUR_WEB_PACKAGES_BASE_URL)
                    .buildUpon()
                    .appendPath(responseAurInfoResult.name)
                    .build();
        } else {
            return null;
        }
    }

    private Uri getResponseAurInfoResultPkgbuildUri() {
        if (responseAurInfoResult != null
                && responseAurInfoResult.packageBase != null
                && !TextUtils.isEmpty(responseAurInfoResult.packageBase)) {
            return Uri.parse(AUR_PACKAGE_PKGBUILD_BASE_URL)
                    .buildUpon()
                    .appendQueryParameter("h", responseAurInfoResult.packageBase)
                    .build();
        } else {
            return null;
        }
    }

    private Uri getResponseAurInfoResultLogUri() {
        if (responseAurInfoResult != null
                && responseAurInfoResult.packageBase != null
                && !TextUtils.isEmpty(responseAurInfoResult.packageBase)) {
            return Uri.parse(AUR_PACKAGE_LOG_BASE_URL)
                    .buildUpon()
                    .appendQueryParameter("h", responseAurInfoResult.packageBase)
                    .build();
        } else {
            return null;
        }
    }

    private String getFormattedStringList(List<String> stringList, boolean bulletPoint, String separator) {
        StringBuilder stringBuilder = new StringBuilder();
        if (stringList.size() > 0) {
            for (int i = 0; i < stringList.size(); i++) {
                if (i > 0) {
                    stringBuilder.append(separator);
                }
                if (bulletPoint) {
                    stringBuilder
                            .append(resources.getString(R.string.char_bullet_point))
                            .append(resources.getString(R.string.char_whitespace))
                            .append(stringList.get(i).trim());
                } else {
                    stringBuilder.append(stringList.get(i).trim());
                }
            }
        }
        return stringBuilder.toString();
    }

    interface AurPackageDetailsFragmentCallback {
        void onAurPackageDetailsFragmentOnResponseNotSuccessful(String responseAurPackageName);

        void onAurPackageDetailsFragmentOnResponseEmptyResult(String responseAurPackageName);

        void onAurPackageDetailsFragmentOnResponseOnFailure(String responseAurPackageName, String throwableMessage);

        void onAurPackageDetailsFragmentBrowseMaintainer(String aurPackageMaintainer);
    }
}
