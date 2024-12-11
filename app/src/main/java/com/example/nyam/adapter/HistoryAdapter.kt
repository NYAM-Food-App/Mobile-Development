package com.example.nyam.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.DecimalFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.nyam.R
import com.example.nyam.data.local.entity.HistoryEntity
import com.example.nyam.data.local.entity.RecipesEntity
import com.example.nyam.databinding.CardFoodBinding
import com.example.nyam.view.detail.FoodDetailActivity
import com.example.nyam.view.detail.FoodDetailHistoryActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter :
    ListAdapter<HistoryEntity, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    inner class MyViewHolder(private val binding: CardFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(history: HistoryEntity) {
            with(binding) {
                tvItemName.text = history.foodname
                Glide.with(itemView.context).load(history.image).apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error)
                ).into(ivFoodPhoto)
                val myFormat = DecimalFormat("#")
                tvCalories.text = myFormat.format(history.calories)

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, FoodDetailHistoryActivity::class.java)
                    intent.putExtra(FoodDetailHistoryActivity.FOOD_ID, history.id)
                    itemView.context.startActivity(intent)

                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: CardFoodBinding =
            CardFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val recipes = getItem(position)
        holder.bind(recipes)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<HistoryEntity> =
            object : DiffUtil.ItemCallback<HistoryEntity>() {
                override fun areItemsTheSame(
                    oldItem: HistoryEntity, newItem: HistoryEntity
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: HistoryEntity, newItem: HistoryEntity
                ): Boolean {
                    return oldItem.equals(newItem)
                }
            }
    }
}