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
import android.arch.lifecycle.ViewModel;

import com.rascarlo.aurdroid.api.model.Info;
import com.rascarlo.aurdroid.data.AurRepository;

public class InfoViewModel extends ViewModel {
    private final LiveData<Info> infoLiveData;

    InfoViewModel(String pkgname) {
        infoLiveData = AurRepository.getAurRepositoryInstance().getInfoLiveData(pkgname);
    }

    public LiveData<Info> getInfoLiveData() {
        return infoLiveData;
    }
}
