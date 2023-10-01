package com.example.repositorysearch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.repositorysearch.adapter.UserAdapter
import com.example.repositorysearch.databinding.ActivityMainBinding
import com.example.repositorysearch.model.UserDto
import com.example.repositorysearch.network.GithubService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var userAdapter : UserAdapter

    private val handler = Handler(Looper.getMainLooper())
    private var searchFor: String =""

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build() //Retrofit 객체 생성
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userAdapter = UserAdapter{
            val intent = Intent(this@MainActivity, RepoActivity::class.java)
            intent.putExtra("username",it.username)
            startActivity(intent)
        }

        binding.userRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userAdapter
        }

        val runnable = Runnable {
            searchUser()
        }

        binding.searchEditText.addTextChangedListener {

            searchFor = it.toString() // 다음에 검색할 문자가 들어감
            handler.removeCallbacks(runnable) // 기다리는동안 다음작업이 들어오면 앞에 작업을 지우는것
            handler.postDelayed( // 지우고나서 300미리 second 이후에 runnable 실행
                runnable,300,
            )


        }








    }

    private fun searchUser(){
        val githubService = retrofit.create(GithubService::class.java) //인터페이스 객체 넣어줌     구현체.
        githubService.searchUsers(searchFor).enqueue(object: Callback<UserDto>{
            override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {
                Log.e("MainActivity","Search User: ${response.body().toString()}")

                userAdapter.submitList(response.body()?.items) //userAdpater 에 매핑

            }

            override fun onFailure(call: Call<UserDto>, t: Throwable) {
                Toast.makeText(this@MainActivity, "에러가 발생했습니다.",
                    Toast.LENGTH_SHORT).show()
                t.printStackTrace()

            }

        })
    }
}