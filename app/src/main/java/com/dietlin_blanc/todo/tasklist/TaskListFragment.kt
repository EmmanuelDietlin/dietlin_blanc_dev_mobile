package com.dietlin_blanc.todo.tasklist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.dietlin_blanc.todo.databinding.FragmentTaskListBinding
import com.dietlin_blanc.todo.detail.TaskDetailActivity

interface TaskListListener {
    fun onClickDelete(task: Task)
    fun onClickEdit(task: Task)
}

class TaskListFragment : Fragment() {

    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )
    val adapterListener : TaskListListener = object : TaskListListener {
        override fun onClickDelete(task: Task) {
            adapter.submitList(taskList - task); taskList = taskList - task
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
            taskList = taskList + task
            adapter.submitList(taskList)
        }
    }

    private val editTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as Task?
        if (task != null) {
            taskList = taskList.map { if (it.id == task.id) task else it}
            adapter.submitList(taskList)
        }
    }





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            _binding = FragmentTaskListBinding.inflate(layoutInflater)
            val rootView = binding.root
            adapter.submitList(taskList)

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
        /*adapter.onClickDelete = {task -> adapter.submitList(taskList - task); taskList = taskList - task }
        adapter.onClickEdit = {task ->
            val intent = Intent(context, TaskDetailActivity::class.java)
            intent.putExtra("task", task)
            editTask.launch(intent)
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}