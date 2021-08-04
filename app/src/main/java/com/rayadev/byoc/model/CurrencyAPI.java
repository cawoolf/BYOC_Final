package com.rayadev.byoc.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface CurrencyAPI {

    /*Convert this whole string into a query.
    convert?q=USD_PHP
    &compact=ultra
    &apiKey=882cc2509c2a6546a18c
    Three separate parts to the query. */

    String apiSearch = "convert?q=USD_PHP&compact=ultra&apiKey=882cc2509c2a6546a18c";

    @GET
    Call<Object> getCurrency(@Url String url);

//    @GET("{pair}&compact=ultra&apiKey=882cc2509c2a6546a18c")
//    Call<Object> getCurrencyPair(@Path("pair") String pair); //Inserts a path into the URL.
}
