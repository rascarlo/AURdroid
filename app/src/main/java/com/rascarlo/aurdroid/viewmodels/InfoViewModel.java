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

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.rascarlo.aurdroid.api.AurService;
import com.rascarlo.aurdroid.api.model.Info;
import com.rascarlo.aurdroid.data.AurRepository;
import com.rascarlo.aurdroid.util.AurdroidConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoViewModel extends ViewModel {
    private final AurService aurService;
    private final LiveData<Info> infoLiveData;
    private final MutableLiveData<String> messageMutableLiveData;

    InfoViewModel(String pkgname) {
        AurRepository aurRepository = AurRepository.getAurRepositoryInstance();
        aurService = aurRepository.getAurService();
        infoLiveData = getInfoLiveData(pkgname);
        messageMutableLiveData = new MutableLiveData<>();
    }

    private LiveData<Info> getInfoLiveData(final String pkgname) {
        final MutableLiveData<Info> infoMutableLiveData = new MutableLiveData<>();
        Call<Info> infoCall = aurService.searchInfo(pkgname);
        infoCall.enqueue(new Callback<Info>() {
            @Override
            public void onResponse(@NonNull Call<Info> call, @NonNull Response<Info> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getType() != null && !TextUtils.isEmpty(response.body().getType())) {
                            String returnType = response.body().getType();
                            if (TextUtils.equals(AurdroidConstants.RETURN_TYPE_MULTIINFO, returnType)) {
                                // response body type multiinfo
                                infoMutableLiveData.setValue(response.body());
                                messageMutableLiveData.setValue(null);
                            } else {
                                // response body type not multiinfo, >dev/null
                                infoMutableLiveData.setValue(null);
                                messageMutableLiveData.setValue(AurdroidConstants.RETROFIT_FAILURE);
                            }
                        } else {
                            // response body type null, >dev/null
                            infoMutableLiveData.setValue(null);
                            messageMutableLiveData.setValue(AurdroidConstants.RETROFIT_FAILURE);
                        }
                    } else {
                        // response body null, >dev/null
                        infoMutableLiveData.setValue(null);
                        messageMutableLiveData.setValue(AurdroidConstants.RETROFIT_FAILURE);
                    }
                } else {
                    // response not successful, >dev/null
                    infoMutableLiveData.setValue(null);
                    messageMutableLiveData.setValue(AurdroidConstants.RETROFIT_FAILURE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Info> call, @NonNull Throwable t) {
                infoMutableLiveData.setValue(null);
                if (!TextUtils.isEmpty(t.getLocalizedMessage())) {
                    messageMutableLiveData.setValue(t.getLocalizedMessage());
                } else {
                    messageMutableLiveData.setValue(AurdroidConstants.RETROFIT_FAILURE);
                }
            }
        });
        return infoMutableLiveData;
    }

    public LiveData<Info> getInfoLiveData() {
        return infoLiveData;
    }

    public MutableLiveData<String> getMessageMutableLiveData() {
        return messageMutableLiveData;
    }
}
