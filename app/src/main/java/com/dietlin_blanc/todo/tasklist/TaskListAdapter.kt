package com.dietlin_blanc.todo.tasklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dietlin_blanc.todo.databinding.ItemTaskBinding

object TasksDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task) : Boolean {
        if (oldItem == newItem && !oldItem.id.equals(newItem.id)) return false
        return true
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task) : Boolean {
        if (oldItem == newItem) return true
        if (oldItem.title.equals(newItem.title) && oldItem.description.equals(newItem.description)) return true
        return false
    }
}


class TaskListAdapter(val listener: TaskListListener) : androidx.recyclerview.widget.ListAdapter<Task, TaskListAdapter.TaskViewHolder>(TasksDiffCallback) {

    /*var onClickDelete: (Task) -> Unit = {}
    var onClickEdit: (Task) -> Unit = {}*/

    inner class TaskViewHolder(var binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.taskTitle.text = task.title
            binding.taskTitle.setOnLongClickListener { listener.onClickShare(binding.taskTitle.text.toString())}
            binding.taskDescription.text = task.description
            binding.taskDescription.setOnLongClickListener {listener.onClickShare(binding.taskDescription.text.toString())}
            binding.delete.setOnClickListener { listener.onClickDelete(task) }
            binding.edit.setOnClickListener{listener.onClickEdit(task)}

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(this.currentList[position])
    }






}