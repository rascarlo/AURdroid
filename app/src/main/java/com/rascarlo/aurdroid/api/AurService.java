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

package com.rascarlo.aurdroid.api;

import com.rascarlo.aurdroid.api.model.Info;
import com.rascarlo.aurdroid.api.model.Search;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AurService {

    @GET("rpc/?v=5&type=search&by=name-desc")
    Call<Search> searchByNameOrDescription(@Query("arg") String query);

    @GET("rpc/?v=5&type=search&by=name")
    Call<Search> searchByName(@Query("arg") String query);

    @GET("rpc/?v=5&type=search&by=maintainer")
    Call<Search> searchByMaintainer(@Query("arg") String query);

    @GET("rpc/?v=5&type=search&by=depends")
    Call<Search> searchByDepends(@Query("arg") String query);

    @GET("rpc/?v=5&type=search&by=makedepends")
    Call<Search> searchByMakeDepends(@Query("arg") String query);

    @GET("rpc/?v=5&type=search&by=optdepends")
    Call<Search> searchByOptDepends(@Query("arg") String query);

    @GET("rpc/?v=5&type=search&by=checkdepends")
    Call<Search> searchByCheckDepends(@Query("arg") String query);

    @GET("rpc/?v=5&type=info")
    Call<Info> searchInfo(@Query("arg") String pkgname);

}