package com.chrisgibson.mybank

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class ItemsAdapter (val items: ArrayList<Item>, val itemOptionsClickListener: ItemOptionsClickListener): RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_list_view,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bindItem(items[position])
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val itemName = itemView?.findViewById<TextView>(R.id.item_store_text)
        val itemPrice = itemView?.findViewById<TextView>(R.id.item_amount_text)
        val itemCategory = itemView?.findViewById<TextView>(R.id.item_category_text)
        val categoryIcon = itemView?.findViewById<ImageView>(R.id.item_categoryitem_image)
        val deleteIcon = itemView?.findViewById<Button>(R.id.item_delete_button)


        fun bindItem(item:Item){
            deleteIcon.visibility = View.INVISIBLE
            itemName?.text = item.itemName
            itemPrice?.text = "$ " + item.itemPrice
            itemCategory?.text = item.itemCategory
            categoryIcon?.setImageResource(item.categoryIcon.toInt())

            if (FirebaseAuth.getInstance().currentUser?.uid == item.user)
            deleteIcon.visibility = View.VISIBLE
            deleteIcon.setOnClickListener {
                itemOptionsClickListener.itemOptionsMenuClicked(item)
            }

        }
    }
}