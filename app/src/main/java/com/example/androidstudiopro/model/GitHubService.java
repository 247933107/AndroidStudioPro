package com.example.androidstudiopro.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by zhangyue on 2016/3/10.
 */
public interface GitHubService {
    @GET("joke/content/list.from?key=0387ce49ebf86da430611dc3e7c1a668&page=2&pagesize=10&sort=asc")
    Call<List<Jokers>> listRepos(@Path("time") String user);
}
