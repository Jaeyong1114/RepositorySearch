package com.example.repositorysearch

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.repositorysearch.adapter.RepoAdapter
import com.example.repositorysearch.databinding.ActivityRepoBinding
import com.example.repositorysearch.model.Repo
import com.example.repositorysearch.network.GithubService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RepoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRepoBinding
    private lateinit var repoAdapter : RepoAdapter

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build() //Retrofit 객체 생성

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val username = intent.getStringExtra("username") ?: return

        binding.usernameTextView.text= username

        repoAdapter= RepoAdapter()

        binding.repoRecyclerView.apply {
            layoutManager=LinearLayoutManager(context)
            adapter = repoAdapter
        }
        listRepo(username)

    }

    private fun listRepo(username : String) {
        val githubService = retrofit.create(GithubService::class.java)
        githubService.listRepos(username).enqueue(object: Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                Log.e("MainActivity","List Repo:${response.body().toString()}")

                repoAdapter.submitList(response.body())
            }

            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {

            }

        }) //메인 액티비티에서 현재 사용하지 않으므로 주석처리
    }
}