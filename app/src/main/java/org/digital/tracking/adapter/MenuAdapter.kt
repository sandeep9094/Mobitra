package org.digital.tracking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.digital.tracking.R
import org.digital.tracking.databinding.AdapterMenuItemBinding

import org.digital.tracking.model.MenuModel

class MenuAdapter(
    private val menuList: List<MenuModel>,
    private val listener: Listener
) : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    private lateinit var binding: AdapterMenuItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_menu_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(menuList[position])
    }

    override fun getItemCount(): Int = menuList.size

    inner class ViewHolder(binding: AdapterMenuItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(menu: MenuModel) {
            binding.menu = menu
            binding.executePendingBindings()
            binding.menuRootLayout.setOnClickListener {
                listener.onMenuItemClicked(menu)
            }
        }

    }

    interface Listener {
        fun onMenuItemClicked(menuModel: MenuModel)
    }

}