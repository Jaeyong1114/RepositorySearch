package com.example.repositorysearch.network

import com.example.repositorysearch.model.Repo
import com.example.repositorysearch.model.UserDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {
    //첫번째 API
    @GET("/users/{username}/repos")
    fun listRepos(@Path("username") username: String,@Query("page") page : Int) : Call<List<Repo>>   //API 선언

    //2번째 API 유저 조회

    @GET("search/users")
    fun searchUsers(@Query("q") query: String): Call<UserDto>
}