package com.dietlin_blanc.todo.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dietlin_blanc.todo.R
import com.dietlin_blanc.todo.databinding.FragmentTaskListBinding
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


class TaskListAdapter : androidx.recyclerview.widget.ListAdapter<Task, TaskListAdapter.TaskViewHolder>(TasksDiffCallback) {

    inner class TaskViewHolder(var binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.taskTitle.text = task.title
            binding.taskDescription.text = task.description
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