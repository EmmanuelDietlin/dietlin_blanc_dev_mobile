package com.dietlin_blanc.todo.user

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import com.dietlin_blanc.todo.data.User
import com.dietlin_blanc.todo.detail.ui.theme.Todo_Emmanuel_LudovicTheme
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserActivity : AppCompatActivity() {

    private val capturedUri by lazy {
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues())
    }

    private val viewModel: UserViewModel by viewModels()

    private val defaultUri : Uri = Uri.parse("https://www.google.com/search?q=pingu&sxsrf=ALiCzsbnt9i3ICyW3Cr_SyB-lr59XYs5og:1670850468508&source=lnms&tbm=isch&sa=X&ved=2ahUKEwitrZeFk_T7AhWQU6QEHbVgBs8Q_AUoAXoECAIQAw&biw=851&bih=799&dpr=1.5#imgrc=YgPXA15974wF8M")
    private var uri : Uri ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewModel.userStateFlow.collect {
                    user ->
                if (user.avatar != null) uri = Uri.parse(user.avatar)
                else uri = defaultUri
                setContent {
                    Todo_Emmanuel_LudovicTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colors.background
                        ) {
                            val takePicture =
                                rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                                    if (success) {
                                        viewModel.updateAvatar(capturedUri!!.toRequestBody())
                                    }

                                }

                            val choosePicture =
                                rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
                                    viewModel.updateAvatar(it!!.toRequestBody())

                                }

                            Column {
                                AsyncImage(
                                    modifier = Modifier.fillMaxHeight(.2f),
                                    model = uri,
                                    contentDescription = null,
                                )
                                Button(
                                    onClick = {
                                        //takePicture.launch()
                                        //askPermission.launch(READ_EXTERNAL_STORAGE)
                                        takePicture.launch(capturedUri)
                                    },
                                    content = { Text("Take picture") }
                                )
                                Button(
                                    onClick = {
                                        choosePicture.launch(
                                            PickVisualMediaRequest(
                                                ActivityResultContracts.PickVisualMedia.ImageOnly
                                            )
                                        )
                                    },
                                    content = { Text("Pick photo") }
                                )
                                if (user.name.equals(""))
                                    Detail(user = User(intent.getStringExtra("name")!!, intent.getStringExtra("email")!!, intent.getStringExtra("uri")), viewModel = viewModel)
                                else
                                    Detail(user = user, viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
        viewModel.refresh()





    }

    private fun Bitmap.toRequestBody(): MultipartBody.Part {
        val tmpFile = File.createTempFile("avatar", "jpg")
        tmpFile.outputStream().use { // *use* se charge de faire open et close
            this.compress(Bitmap.CompressFormat.JPEG, 100, it) // *this* est le bitmap ici
        }
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "avatar.jpg",
            body = tmpFile.readBytes().toRequestBody()
        )
    }

    private fun Uri.toRequestBody(): MultipartBody.Part {
        val fileInputStream = contentResolver.openInputStream(this)!!
        val fileBody = fileInputStream.readBytes().toRequestBody()
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "avatar.jpg",
            body = fileBody
        )
    }
}

@Composable
fun Detail(user : User, viewModel : UserViewModel) {
    var newUser by remember {
        mutableStateOf(
            User(email = user.email,
            name = user.name,
            avatar = user.avatar
        ))
    }
    Column(modifier = Modifier.padding(16.dp ), verticalArrangement = Arrangement.spacedBy(16.dp)) {

        Text(text = "User informations", style = MaterialTheme.typography.h5)
        OutlinedTextField(value = newUser.name, onValueChange = {newUser = newUser.copy(name = it)})
        OutlinedTextField(value = newUser.email, onValueChange = {newUser = newUser.copy(email = it)})
        Button(onClick = { viewModel.edit(newUser)}) {

        }
    }

}

@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    Todo_Emmanuel_LudovicTheme {
        //Detail()
    }
}
