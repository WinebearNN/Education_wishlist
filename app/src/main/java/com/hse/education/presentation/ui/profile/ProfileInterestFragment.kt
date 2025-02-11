package com.hse.education.presentation.ui.profile

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hse.education.R
import com.hse.education.databinding.FragmentProfileInterestBinding
import com.hse.education.domain.entity.Interest
import com.hse.education.presentation.adapter.InterestAdapter
import com.hse.education.presentation.viewmodel.ProfileViewModel


class ProfileInterestFragment : Fragment() {
    private var _binding: FragmentProfileInterestBinding? = null
    private val binding get() = _binding!!

    private val viewModelP:ProfileViewModel by activityViewModels()

    private lateinit var adapter: InterestAdapter
    private lateinit var interestList: MutableSet<Int>

    private val updateObserver = androidx.lifecycle.Observer<Boolean> { isProcess ->
        if(isProcess) {
            //TODO animation
        }else{
            findNavController().navigate(R.id.action_profileInterest_to_profile)
        }
    }

    private fun showInterestSelectionDialog(
        context: Context,
        selectedInterests: MutableSet<Int>,
        onSelectionConfirmed: (MutableSet<Int>) -> Unit
    ) {
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_interest_selection, null)
        val container = dialogView.findViewById<LinearLayout>(R.id.interest_list_container)

        val checkBoxes = mutableSetOf<CheckBox>()

        Interest.entries.forEach { interest ->
            if (selectedInterests.contains(interest.value)) return@forEach

            val itemView = inflater.inflate(R.layout.interest_item, container, false)
            val imageView = itemView.findViewById<ImageView>(R.id.image_interest)
            val textView = itemView.findViewById<TextView>(R.id.text_view_interest)
            val checkBox = itemView.findViewById<CheckBox>(R.id.check_box_interest)
            itemView.findViewById<ImageView>(R.id.image_delete).visibility=View.GONE

            checkBox.visibility=View.VISIBLE


            imageView.setImageResource(interest.imageId)
            textView.text = interest.translation
            checkBox.tag = interest.value

            itemView.setOnClickListener {
                checkBox.isChecked = !checkBox.isChecked
            }

            container.addView(itemView)
            checkBoxes.add(checkBox)
        }

        AlertDialog.Builder(context)
            .setTitle("Выберите интересы")
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                val selectedValues = checkBoxes.filter { it.isChecked }.mapNotNull { it.tag as? Int }
                onSelectionConfirmed(mutableSetOf(*selectedValues.toTypedArray()))
            }
            .setNegativeButton("Отмена", null)
            .show()
    }


    private fun initUI() {
        val currentUser = viewModelP.user.value ?: throw Exception("Непредвиденная ошибка")

        interestList=currentUser.interest

        adapter = InterestAdapter(interestList) { interestValue ->
            interestList.remove(interestValue)
            adapter.notifyDataSetChanged()
        }
        binding.recycleViewInterest.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleViewInterest.adapter = adapter

        binding.imageButtonAdd.setOnClickListener{
            showInterestSelectionDialog(requireContext(), interestList){ ints: MutableSet<Int> ->
                Log.d(TAG,"Выбранные интересы: ${ints.toString()}")
                interestList.addAll(ints)
                adapter.updateInterestSet(interestList)
                adapter.notifyDataSetChanged()
            }
        }

        binding.buttonSave.setOnClickListener{
            val updatedUser = currentUser.copy(
                interest = interestList
            )
            viewModelP.updateUserData(updatedUser)
            viewModelP.loading.observe(viewLifecycleOwner, updateObserver)

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileInterestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    companion object {
        private const val TAG = "ProfileInterestFragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModelP.loading.removeObserver(updateObserver)
    }
}