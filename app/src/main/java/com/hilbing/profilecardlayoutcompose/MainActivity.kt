package com.hilbing.profilecardlayoutcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.hilbing.profilecardlayoutcompose.ui.theme.ProfileCardLayoutComposeTheme
import com.hilbing.profilecardlayoutcompose.ui.theme.lightGreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileCardLayoutComposeTheme {
                UsersApplication()
            }
        }
    }
}

@Composable
//Navigation for composables
fun UsersApplication(userProfiles: List<UserProfile> = userProfileList){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "users_list"){
        composable("users_list"){
            MainScreen(userProfiles, navController)
        }
        composable(
            route = "user_details/{userId}",
        arguments = listOf(navArgument("userId")
        {
            type = NavType.IntType
        }))
        { navBackStackEntry ->
            UserProfileDetailsScreen(navBackStackEntry.arguments!!.getInt("userId"))
        }
    }
}

@Composable
fun MainScreen(userProfiles: List<UserProfile>, navController: NavHostController?) {
    Scaffold(topBar = {AppBar()}) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ){
            LazyColumn() {
                items(userProfiles){
                    userProfile ->
                    ProfileCard(userProfile = userProfile){
                        navController?.navigate("user_details/${userProfile.id}")
                    }
                }
            }
        }
    }

}

@Composable
fun AppBar(){
    TopAppBar(
        navigationIcon = {
            Icon(
                Icons.Default.Home,
                "Content Description",
                Modifier.padding(horizontal = 12.dp))},
        title = {Text("Messaging Application Users")}
    )
}

@Composable
fun ProfileCard(userProfile: UserProfile, clickAction: () -> Unit){
    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top)
            .clickable(onClick = { clickAction.invoke() }),
        elevation = 8.dp,
        backgroundColor = Color.White)
    {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            ProfilePicture(userProfile.drawableId, userProfile.status, 72.dp)
            ProfileContent(userProfile.name, userProfile.status, Alignment.Start)
        }


    }
}

@Composable
fun ProfilePicture(drawableId: String, onlineStatus: Boolean, imageSize: Dp){
    Card(
        shape = CircleShape,
        border = BorderStroke(
            width = 2.dp,
            color = if(onlineStatus)
                MaterialTheme.colors.lightGreen
            else
                Color.Red
        ),
        modifier = Modifier.padding(16.dp),
        elevation = 4.dp
    ){
        Image(
            //Coil works async
            painter = rememberImagePainter(
                data = drawableId,
                builder = {
                    transformations(CircleCropTransformation())
                }
            ),
            contentDescription = "Image",
            modifier = Modifier.size(imageSize),
            contentScale = ContentScale.Crop
        )
    }

}

@Composable
fun ProfileContent(userName: String, onlineStatus: Boolean, alignment: Alignment.Horizontal){
    Column(
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = alignment
         //   .fillMaxWidth()
            ) {

        Text(
            text =  userName,
            style = MaterialTheme.typography.h5
        )

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {

            Text(
                text =  if(onlineStatus)
                    "Active now"
                else
                    "Offline",
                style = MaterialTheme.typography.body2
            )
        }

    }

}

@Composable
fun UserProfileDetailsScreen(userId: Int) {
    val userProfile = userProfileList.first { userProfile -> userId == userProfile.id }
    Scaffold(topBar = {AppBar()}) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ){
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                ProfilePicture(userProfile.drawableId, userProfile.status, 240.dp)
                ProfileContent(userProfile.name, userProfile.status, Alignment.CenterHorizontally)
            }
            
        }
    }

}

@Preview(showBackground = true)
@Composable
fun UserProfileDetailsPreview() {
    ProfileCardLayoutComposeTheme {
        UserProfileDetailsScreen(userId = 0)
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ProfileCardLayoutComposeTheme {
        MainScreen(userProfiles = userProfileList, null)
    }

}