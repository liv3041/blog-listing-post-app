package com.virdapp.listingpost

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.virdapp.listingpost.data.Post
import com.virdapp.listingpost.data.news_post
import com.virdapp.listingpost.ui.theme.ListingpostTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListingpostTheme {
//                BlogListingApp()
            }
        }
    }
}
@Composable
fun BlogListingApp(){
    Scaffold(

    ) { it ->
        LazyColumn(contentPadding = it) {
            items(news_post){
                PostItem(
                    post = it,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

    }
}
@Composable
fun PostItem(
    post: Post,
    modifier: Modifier = Modifier
){
    Card(modifier = modifier,
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

            PostIcon(post.image)
            PostInformation(post.title,post.description)



        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
//                .background(MaterialTheme.colorScheme.primary.alpha)
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
//            Image(
//                painter = painterResource(R.drawable.ic_woof_logo),
//                contentDescription = null,
//                modifier = Modifier.size(16.dp)
//            )
            TextAndImages(likesNo.toString(),R.drawable.ic_woof_logo)


            Spacer(modifier = Modifier.padding(4.dp))
            TextAndImages(comments.toString(),R.drawable.ic_woof_logo)
//            Image(
//                painter = painterResource(R.drawable.ic_woof_logo),
//                contentDescription = null,
//                modifier = Modifier.size(16.dp)
//            )
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
    @DrawableRes image: Int,
    modifier: Modifier = Modifier) {
    Image(
        modifier = modifier
            .size(100.dp)
        .padding(8.dp)
            .clip(MaterialTheme.shapes.small),
        painter = painterResource(image),
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
        Text(text = description,
            style = MaterialTheme.typography.bodyMedium)

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ListingpostTheme {
    BlogListingApp()
    }
}