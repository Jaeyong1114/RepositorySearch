package com.example.repositorysearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.repositorysearch.adapter.UserAdapter
import com.example.repositorysearch.databinding.ActivityMainBinding
import com.example.repositorysearch.model.Repo
import com.example.repositorysearch.model.UserDto
import com.example.repositorysearch.network.GithubService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build() //Retrofit 객체 생성

        val githubService = retrofit.create(GithubService::class.java) //인터페이스 객체 넣어줌     구현체.

        githubService.listRepos("square").enqueue(object: Callback<List<Repo>>{
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                Log.e("MainActivity","List Repo:${response.body().toString()}")
            }

            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {

            }

        })



        val userAdapter = UserAdapter()

        binding.userRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userAdapter
        }

        githubService.searchUsers("squar").enqueue(object: Callback<UserDto>{
            override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {
                Log.e("MainActivity","Search User: ${response.body().toString()}")

                userAdapter.submitList(response.body()?.items) //userAdpater 에 매핑

            }

            override fun onFailure(call: Call<UserDto>, t: Throwable) {

            }

        })






    }
}