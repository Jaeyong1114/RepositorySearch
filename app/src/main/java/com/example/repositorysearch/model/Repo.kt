package com.example.repositorysearch.model

import com.google.gson.annotations.SerializedName

data class Repo(
    @SerializedName("id")
    var id:Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val descrpition: String,

    @SerializedName("language")
    val langua : String?,

    @SerializedName("stargazers_count")
    val starCount :Int,

    @SerializedName("forks_count")
    val forkCount : Int,

    @SerializedName("html_url")
    val htmlUrl : String,


)