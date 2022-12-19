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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import com.dietlin_blanc.todo.data.User
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
        //setContentView(R.layout.activity_user)

        lifecycleScope.launch {
            viewModel.userStateFlow.collect {
                    user ->
                if (user.avatar != null) uri = Uri.parse(user.avatar)
                else uri = defaultUri
                setContent {
                    var param : String? = intent.getStringExtra("uri")
                    /*
                    var bitmap: Bitmap? by remember { mutableStateOf(null) }
                    var uri: Uri? by remember { mutableStateOf(null) }*/


                    /*
                    val takePicture = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
                        bitmap = it
                        lifecycleScope.launch {
                            Api.userWebService.updateAvatar(bitmap!!.toRequestBody());
                        }
                    }*/

                    val takePicture = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                        if (success) {
                            viewModel.updateAvatar(capturedUri!!.toRequestBody())
                            /*
                            uri = capturedUri
                            lifecycleScope.launch {
                                Api.userWebService.updateAvatar(uri!!.toRequestBody());
                            }*/
                        }

                    }

                    val choosePicture = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
                        viewModel.updateAvatar(it!!.toRequestBody())
                        /*
                        bitmap = null
                        uri = it
                        lifecycleScope.launch {
                            Api.userWebService.updateAvatar(uri!!.toRequestBody());
                        }*/
                    }
                    /*
                    val askPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
                        if (it) takePicture.launch()
                    }*/

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
                                choosePicture.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            },
                            content = { Text("Pick photo") }
                        )
                        Detail(user = user, viewModel = viewModel)
                        /*
                        OutlinedTextField(value = , onValueChange = {newTask = newTask.copy(title = it)})
                        OutlinedTextField(value = , onValueChange = {newTask = newTask.copy(description = it)})*/
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
    var email = user.email
    var name = user.name
    Column(modifier = Modifier.padding(16.dp ), verticalArrangement = Arrangement.spacedBy(16.dp)) {

        Text(text = "Task detail", style = MaterialTheme.typography.h3)
        OutlinedTextField(value = name, onValueChange = {name = it})
        OutlinedTextField(value = email, onValueChange = {email = it})
        Button(onClick = { viewModel.edit(name, email)}) {

        }
    }

}