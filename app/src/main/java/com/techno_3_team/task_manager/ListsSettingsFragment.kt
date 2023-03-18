package com.techno_3_team.task_manager

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.techno_3_team.task_manager.databinding.FragmentListsSettingsBinding


class ListsSettingsFragment : Fragment() {
    private var binding: FragmentListsSettingsBinding? = null
    private val _binding: FragmentListsSettingsBinding
        get() = binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListsSettingsBinding.inflate(inflater)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(_binding){
            val listNames = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getParcelableArrayList(LIST_LISTS_KEY, ListOfTasks::class.java)!!
            }else{
                arguments?.getParcelableArrayList(LIST_LISTS_KEY)!!
            }
            val listSettingsAdapter = ListsSettingsAdapter(listNames)
            lists.adapter= listSettingsAdapter
            lists.layoutManager = LinearLayoutManager(lists.context)
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}