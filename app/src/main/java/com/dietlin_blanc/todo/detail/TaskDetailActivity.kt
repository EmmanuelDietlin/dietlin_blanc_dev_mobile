package com.dietlin_blanc.todo.detail

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dietlin_blanc.todo.detail.ui.theme.Todo_Emmanuel_LudovicTheme
import com.dietlin_blanc.todo.tasklist.Task
import java.util.*

class TaskDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            setContent {
                Todo_Emmanuel_LudovicTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        val lambda: (Task) -> Unit = { task ->
                            intent.putExtra("task", task)
                            setResult(RESULT_OK, intent)
                            finish()
                        }
                        if (intent?.action == Intent.ACTION_SEND) {
                            if ("text/plain" == intent.type) {
                                val task_text = intent.getStringExtra(Intent.EXTRA_TEXT)
                                val t = Task(id = UUID.randomUUID().toString(),
                                    title = "New Task ! ",
                                    description = task_text ?: "")
                                Detail(lambda, t)

                            }
                        } else {
                            val t = intent?.getSerializableExtra("task") as Task?
                            Detail(lambda, t)
                        }
                    }
                }
            }
        }
}

@Composable
fun Detail(onValidate: (Task)->Unit, task: Task?) {
            var newTask by remember {
                mutableStateOf(Task(id = task?.id ?: UUID.randomUUID().toString(),
                                    title = task?.title ?: "New Task ! ",
                                    description = task?.description ?: ""
                    ))
            }

            Column(modifier = Modifier.padding(16.dp ), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(text = "Task detail", style = MaterialTheme.typography.h3)
                OutlinedTextField(value = newTask.title, onValueChange = {newTask = newTask.copy(title = it)})
                OutlinedTextField(value = newTask.description, onValueChange = {newTask = newTask.copy(description = it)})
            Button(onClick = { onValidate(newTask)
            }) {

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