package com.techno_3_team.task_manager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ListsSettingsAdapter(
    private val lists: ArrayList<ListOfTasks>
) : RecyclerView.Adapter<ListsSettingsAdapter.ListsSettingsViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListsSettingsAdapter.ListsSettingsViewHolder {
        return ListsSettingsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_of_lists, null)
        )
    }

    override fun onBindViewHolder(
        holder: ListsSettingsAdapter.ListsSettingsViewHolder,
        position: Int
    ) {
        holder.bind(lists[position])
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    class ListsSettingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val listName: TextView = itemView.findViewById(R.id.list_name)
        private val listSubname: TextView = itemView.findViewById(R.id.list_subname)
        private val upButton: ImageButton = itemView.findViewById(R.id.move_list_up_button)
        private val downButton: ImageButton = itemView.findViewById(R.id.move_list_down_button)
        private val deleteList: ImageButton = itemView.findViewById(R.id.delete_list_button)

        fun bind(list: ListOfTasks) {
            listName.text = list.name
            listSubname.text = list.completed.toString() + "из" + list.total.toString()
            upButton.setOnClickListener{
                Toast.makeText(upButton.context, "UP!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}