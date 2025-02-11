package com.hse.education.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hse.education.R
import com.hse.education.domain.entity.Interest

class InterestAdapter(
    private var interestSet: MutableSet<Int>,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<InterestAdapter.InterestViewHolder>() {

    inner class InterestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageInterest: ImageView = view.findViewById(R.id.image_interest)
        val textViewInterest: TextView = view.findViewById(R.id.text_view_interest)
        val imageDelete: ImageView = view.findViewById(R.id.image_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.interest_item, parent, false)
        return InterestViewHolder(view)
    }

    override fun onBindViewHolder(holder: InterestViewHolder, position: Int) {
        val interestValue = interestSet.elementAt(position)
        val interest = Interest.fromValue(interestValue)

        if (interest != null) {
            // Устанавливаем изображение и текст
            holder.imageInterest.setImageResource(interest.imageId)
            holder.textViewInterest.text = interest.translation

            // Устанавливаем обработчик клика для удаления
            holder.imageDelete.setOnClickListener {
                onDeleteClick(interestValue)
            }
        } else {
            // Если значение не найдено в enum, скрываем элемент (или можно задать дефолтное значение)
            holder.itemView.visibility = View.GONE
        }
    }

    fun updateInterestSet(newInterests:MutableSet<Int>){
        interestSet.addAll(newInterests)
    }

    override fun getItemCount(): Int = interestSet.size
}