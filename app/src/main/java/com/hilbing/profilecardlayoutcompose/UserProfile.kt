package com.hilbing.profilecardlayoutcompose

data class UserProfile constructor(
    val name: String,
    val status: Boolean,
    val drawableId: Int)

val userProfileList = arrayListOf<UserProfile>(
    UserProfile(name = "Gretel Hilbing", status = true, R.drawable.gretel),
    UserProfile(name = "Bata", status = false, R.drawable.bata),
    UserProfile(name = "Silvana", status = true, R.drawable.silvana)
)