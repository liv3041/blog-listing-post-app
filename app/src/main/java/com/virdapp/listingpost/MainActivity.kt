package com.virdapp.listingpost

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.virdapp.listingpost.data.Post
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage


import com.virdapp.listingpost.ui.theme.ListingpostTheme

class MainActivity : ComponentActivity() {
    var navController: NavController? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = NavController(this)
        enableEdgeToEdge()
        setContent {
            ListingpostTheme {
                BlogListingApp(
                    navController = rememberNavController()
                )
                NavGraph()
            }
        }
    }
}
@Composable
fun BlogListingApp(navController: NavController){

    val viewModel: PostViewModel = viewModel()
    val data by viewModel.data
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage
    

    Scaffold(

    ) { it ->
        LazyColumn(contentPadding = it) {
            print(it)
            items(data){
                PostItem(
                    post = it,
                    modifier = Modifier.padding(16.dp),
                    navController = navController
                )
            }
        }

    }
}
@Composable
fun PostItem(
    post: Post,
    modifier: Modifier = Modifier
    ,navController: NavController
){
    Card(modifier = modifier
        .clickable {
            val encodedUrl = Uri.encode(post.blogPost.link)
            navController.navigate("webview/${encodedUrl}")
        },
        shape = MaterialTheme.shapes.large,
        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            PostAuthor(post.author,post.logo_of_author)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            PostIcon(post.blogPost.featuredMediaUrl)
            PostInformation(post.blogPost.title.rendered,post.description)



        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {


            PostAnalytics(post.likes_no,post.comments)
        }

    }
}

@Composable
fun PostAnalytics(likesNo: Int, comments: Int,modifier: Modifier = Modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 4.dp, bottom = 8.dp)
        ) {
            TextAndImages(likesNo.toString(),R.drawable.like)


            Spacer(modifier = Modifier.padding(8.dp))
            TextAndImages(comments.toString(),R.drawable.chat)
        }

}
@Composable
fun TextAndImages(text:String,@DrawableRes image: Int,modifier: Modifier = Modifier){
    Row(
        modifier = modifier
            .padding(8.dp)
            .background(Color.Transparent)

    ) {
        Image(
            painter = painterResource(image),
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall
        )
    }
}




@Composable
fun PostAuthor(author: String, @DrawableRes logoOfAuthor: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
    ) {
        Spacer(modifier = Modifier.padding(8.dp))
        Image(
            painter = painterResource(logoOfAuthor),
            contentDescription = null,
            modifier = Modifier.size(34.dp).padding(end = 16.dp),
        )
        Text(text = author,
            style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun PostIcon(
    image: String,
    modifier: Modifier = Modifier) {
    AsyncImage(
        modifier = modifier
            .size(100.dp)
        .padding(8.dp)
            .clip(MaterialTheme.shapes.small),
        model = image,
        contentScale = ContentScale.Crop,
        contentDescription = null
    )

}

@Composable
fun PostInformation(title: String,
                    description:String,
                    modifier: Modifier = Modifier) {

    Column(modifier = modifier) {
        Text(text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(text = cleanText(description),
            style = MaterialTheme.typography.bodyMedium)

    }

}

fun cleanText(description: String): String {
    val cleanDescription = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY).toString()
    return  cleanDescription

}

//@Preview(showBackground = true)
//@Composable
//fun BlogListingAppPreview() {
//    ListingpostTheme {
//    BlogListingApp(navController = NavController(this))
//    }
//}

@Composable
fun NavGraph(startDestination: String = "home") {
    val navController = rememberNavController()

    NavHost(navController, startDestination) {
        composable("home") {
            BlogListingApp(navController)
        }
        composable("webview/{url}") { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url") ?: ""

            WebViewScreen(url, navController)
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun WebViewScreen(url: String, navController: NavController) {
    var isLoading by remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    webChromeClient = WebChromeClient()
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false
                        }
                    }
                    loadUrl(url)
                }
            }
        )

        if (isLoading) {
            CircularProgressIndicator()
        }
    }
}


