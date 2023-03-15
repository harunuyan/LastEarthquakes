package com.volie.lastearthquakes.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.volie.lastearthquakes.databinding.AdapterItemEarthquakeBinding
import com.volie.lastearthquakes.model.Earthquake

class EarthquakeAdapter(
    val onItemClick: (earthquake: Earthquake) -> Unit
) : ListAdapter<Earthquake, EarthquakeAdapter.EarthquakeViewHolder>(
    BaseItemCallback()
) {
    inner class EarthquakeViewHolder(private val binding: AdapterItemEarthquakeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(position: Int) {
            val items = currentList[position]
            with(binding) {
                txtMag.text = items.magnitudeText
                txtName.text = items.name
                txtDate.text = items.date
                txtTime.text = items.time
                divider.setBackgroundColor(items.magnitudeColor)
                cardMag.setCardBackgroundColor(items.magnitudeColor)
                root.setBackgroundColor(items.magnitudeColorLight)
                root.setOnClickListener {
                    onItemClick(items)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EarthquakeViewHolder {
        val binding = AdapterItemEarthquakeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EarthquakeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EarthquakeViewHolder, position: Int) {
        holder.bindData(position)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }
}

class BaseItemCallback : DiffUtil.ItemCallback<Earthquake>() {
    override fun areItemsTheSame(oldItem: Earthquake, newItem: Earthquake): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Earthquake, newItem: Earthquake): Boolean {
        return oldItem == newItem
    }
}
