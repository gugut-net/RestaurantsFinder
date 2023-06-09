package com.example.yelpapp.model.reviews


import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("id")
    val id: String? = "",
    @SerializedName("image_url")
    val imageUrl: String? = "",
    @SerializedName("name")
    val name: String? = "",
    @SerializedName("profile_url")
    val profileUrl: String? = ""
)