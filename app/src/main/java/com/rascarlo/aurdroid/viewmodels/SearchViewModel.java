/*
 *     Copyright (C) rascarlo  rascarlo@gmail.com
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.rascarlo.aurdroid.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.rascarlo.aurdroid.api.AurService;
import com.rascarlo.aurdroid.api.model.Search;
import com.rascarlo.aurdroid.data.AurRepository;
import com.rascarlo.aurdroid.util.AurdroidConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends ViewModel {
    private final AurService aurService;
    private final LiveData<Search> searchLiveData;
    private final MutableLiveData<String> messageMutableLiveData;

    SearchViewModel(int searchBy, String query) {
        AurRepository aurRepository = AurRepository.getAurRepositoryInstance();
        aurService = aurRepository.getAurService();
        searchLiveData = getSearchLiveData(searchBy, query);
        messageMutableLiveData = new MutableLiveData<>();
    }

    private LiveData<Search> getSearchLiveData(final int searchBy, final String query) {
        final MutableLiveData<Search> searchMutableLiveData = new MutableLiveData<>();
        Call<Search> searchCall;
        switch (searchBy) {
            case AurdroidConstants.SEARCH_BY_NAME_OR_DESCRIPTION:
                searchCall = aurService.searchByNameOrDescription(query);
                break;
            case AurdroidConstants.SEARCH_BY_NAME:
                searchCall = aurService.searchByName(query);
                break;
            case AurdroidConstants.SEARCH_BY_MAINTAINER:
                searchCall = aurService.searchByMaintainer(query);
                break;
            case AurdroidConstants.SEARCH_BY_DEPENDS:
                searchCall = aurService.searchByDepends(query);
                break;
            case AurdroidConstants.SEARCH_BY_MAKE_DEPENDS:
                searchCall = aurService.searchByMakeDepends(query);
                break;
            case AurdroidConstants.SEARCH_BY_OPT_DEPENDS:
                searchCall = aurService.searchByOptDepends(query);
                break;
            case AurdroidConstants.SEARCH_BY_CHECK_DEPENDS:
                searchCall = aurService.searchByCheckDepends(query);
                break;
            default:
                searchCall = aurService.searchByNameOrDescription(query);
                break;
        }
        searchCall.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(@NonNull Call<Search> call, @NonNull Response<Search> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getType() != null && !TextUtils.isEmpty(response.body().getType())) {
                            String returnType = response.body().getType();
                            if (TextUtils.equals(AurdroidConstants.RETURN_TYPE_SEARCH, returnType)) {
                                // response body type search
                                searchMutableLiveData.setValue(response.body());
                                messageMutableLiveData.setValue(null);
                            } else if (TextUtils.equals(AurdroidConstants.RETURN_TYPE_ERROR, returnType)) {
                                // response body type error
                                searchMutableLiveData.setValue(null);
                                if (response.body().error != null && !TextUtils.isEmpty(response.body().error)) {
                                    messageMutableLiveData.setValue(response.body().error);
                                } else {
                                    // response body error null, >dev/null
                                    messageMutableLiveData.setValue(AurdroidConstants.RETROFIT_FAILURE);
                                }
                            } else {
                                // response body type not search nor error, >dev/null
                                searchMutableLiveData.setValue(null);
                                messageMutableLiveData.setValue(AurdroidConstants.RETROFIT_FAILURE);
                            }
                        } else {
                            // response body type null, >dev/null
                            searchMutableLiveData.setValue(null);
                            messageMutableLiveData.setValue(AurdroidConstants.RETROFIT_FAILURE);
                        }
                    } else {
                        // response body null, >dev/null
                        searchMutableLiveData.setValue(null);
                        messageMutableLiveData.setValue(AurdroidConstants.RETROFIT_FAILURE);
                    }
                } else {
                    // response not successful, >dev/null
                    searchMutableLiveData.setValue(null);
                    messageMutableLiveData.setValue(AurdroidConstants.RETROFIT_FAILURE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Search> call, @NonNull Throwable t) {
                searchMutableLiveData.setValue(null);
                if (!TextUtils.isEmpty(t.getLocalizedMessage())) {
                    messageMutableLiveData.setValue(t.getLocalizedMessage());
                } else {
                    messageMutableLiveData.setValue(AurdroidConstants.RETROFIT_FAILURE);
                }
            }
        });
        return searchMutableLiveData;
    }

    public LiveData<Search> getSearchLiveData() {
        return searchLiveData;
    }

    public MutableLiveData<String> getMessageMutableLiveData() {
        return messageMutableLiveData;
    }
}
