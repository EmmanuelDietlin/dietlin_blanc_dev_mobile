package com.dietlin_blanc.todo.tasklist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.dietlin_blanc.todo.data.Api
import com.dietlin_blanc.todo.databinding.FragmentTaskListBinding
import com.dietlin_blanc.todo.detail.TaskDetailActivity
import com.dietlin_blanc.todo.user.UserActivity
import kotlinx.coroutines.launch

interface TaskListListener {
    fun onClickDelete(task: Task)
    fun onClickEdit(task: Task)
}

class TaskListFragment : Fragment() {

    /*private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )*/
    val adapterListener : TaskListListener = object : TaskListListener {
        override fun onClickDelete(task: Task) {
            //adapter.submitList(taskList - task); taskList = taskList - task
            viewModel.remove(task)
        }

        override fun onClickEdit(task: Task) {
            val intent = Intent(context, TaskDetailActivity::class.java)
            intent.putExtra("task", task)
            editTask.launch(intent)
        }
    }

    private val adapter = TaskListAdapter(adapterListener)

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!
    private val createTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as Task?
        if (task != null) {
            //taskList = taskList + task
            viewModel.add(task);
            //adapter.submitList(taskList)
        }
    }

    private val editTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as Task?
        if (task != null) {
            println("pouic")
            //taskList = taskList.map { if (it.id == task.id) task else it}
            //adapter.submitList(taskList)
            viewModel.edit(task)
        }
    }

    private val viewModel: TasksListViewModel by viewModels()





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            _binding = FragmentTaskListBinding.inflate(layoutInflater)
            val rootView = binding.root
            //adapter.submitList(taskList)

            return rootView


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = this.adapter
        binding.addTask.setOnClickListener {
            /*val newTask =
                Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}")
            taskList = taskList + newTask
            adapter.submitList(taskList)*/
            //startActivity(intent)
            val intent = Intent(context, TaskDetailActivity::class.java)
            createTask.launch(intent)
        }
        lifecycleScope.launch {
            viewModel.tasksStateFlow.collect { newList ->
                //taskList = newList
                adapter.submitList(newList)
            }
        }

        /*adapter.onClickDelete = {task -> adapter.submitList(taskList - task); taskList = taskList - task }
        adapter.onClickEdit = {task ->
            val intent = Intent(context, TaskDetailActivity::class.java)
            intent.putExtra("task", task)
            editTask.launch(intent)
        }*/
    }

    override fun onResume() {
        super.onResume()
        var uri : String? = null
        lifecycleScope.launch {
            val user = Api.userWebService.fetchUser().body()!!
            binding.textInternet.text = user.name
            uri = user.avatar
            binding.avatar.load(user.avatar) {
                binding.avatar.load("https://lvdneng.rosselcdn.net/sites/default/files/dpistyles_v2/vdn_864w/2022/08/09/node_1214903/55550308/public/2022/08/09/B9731749502Z.1_20220809160423_000%2BG65L1O2VG.1-0.png?itok=eKn4dzbI1660053870") // image par défaut en cas d'erreur
            }
        }
        viewModel.refresh()
        binding.avatar.setOnClickListener {
            val intent = Intent(context, UserActivity::class.java)
            intent.putExtra("uri", uri)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}