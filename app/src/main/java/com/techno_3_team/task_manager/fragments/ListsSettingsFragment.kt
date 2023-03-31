package com.techno_3_team.task_manager.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout.VERTICAL
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.techno_3_team.task_manager.adapters.ListsSettingsAdapter
import com.techno_3_team.task_manager.databinding.FragmentListsSettingsBinding
import com.techno_3_team.task_manager.structures.ListOfLists
import com.techno_3_team.task_manager.support.LIST_LISTS_KEY
import com.techno_3_team.task_manager.support.SpacingItemDecorator


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
            val listOfLists = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getParcelable(LIST_LISTS_KEY, ListOfLists::class.java)!!
            }else{
                arguments?.getParcelable(LIST_LISTS_KEY)!!
            }
            val listNames = listOfLists.list
            val listSettingsAdapter = ListsSettingsAdapter(listNames)
            lists.adapter= listSettingsAdapter
            lists.layoutManager = GridLayoutManager(lists.context, 1)
            lists.addItemDecoration(SpacingItemDecorator(20))
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}
