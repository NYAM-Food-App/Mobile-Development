package com.example.nyam.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.nyam.R
import com.example.nyam.data.local.entity.RecipesEntity
import com.example.nyam.databinding.CardFoodBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecipesAdapter :
    ListAdapter<RecipesEntity, RecipesAdapter.MyViewHolder>(DIFF_CALLBACK) {

    inner class MyViewHolder(private val binding: CardFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipes: RecipesEntity) {
            with(binding) {
                tvItemName.text = recipes.foodname
                Glide.with(itemView.context).load(recipes.image).apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error)
                ).into(ivFoodPhoto)
                tvCalories.text = recipes.calories

                itemView.setOnClickListener {
                    //TODO:Consider Animation
//                    val optionsCompat: ActivityOptionsCompat =
//                        ActivityOptionsCompat.makeSceneTransitionAnimation(
//                            itemView.context as Activity,
//                            Pair(ivProfile, "profile"),
//                            Pair(tvItemName, "name"),
//                            Pair(ivItemPhoto, "photo"),
//                            Pair(tvCreatedAt,"time")
//                        )
//                    val intent = Intent(itemView.context, DetailActivity::class.java)
//                    intent.putExtra(DetailActivity.STORY_ID, story.id)
//                    itemView.context.startActivity(intent,optionsCompat.toBundle())

                }
            }

        }

        private fun parseDateString(dateString: String): String? {
            val inputFormat = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()
            )
            val outputFormat = SimpleDateFormat("dd-MM-yyyy | HH:mm", Locale.getDefault())
            val date: Date? = inputFormat.parse(dateString)
            return date?.let { outputFormat.format(it) }
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
        val DIFF_CALLBACK: DiffUtil.ItemCallback<RecipesEntity> =
            object : DiffUtil.ItemCallback<RecipesEntity>() {
                override fun areItemsTheSame(
                    oldItem: RecipesEntity, newItem: RecipesEntity
                ): Boolean {
                    return oldItem.index == newItem.index
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: RecipesEntity, newItem: RecipesEntity
                ): Boolean {
                    return oldItem.equals(newItem)
                }
            }
    }
}