package com.chrisgibson.mybank.Adapters




import android.graphics.Color
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.chrisgibson.mybank.Interface.ItemOptionsClickListener
import com.chrisgibson.mybank.Model.Item
import com.chrisgibson.mybank.R
import com.chrisgibson.mybank.Utilities.FunctionRecycler
import com.google.firebase.auth.FirebaseAuth

class ItemsAdapter (val items: ArrayList<Item>, val itemOptionsClickListener: ItemOptionsClickListener): RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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
        val itemCard = itemView?.findViewById<CardView>(R.id.item_card)
        val linktext = itemView?.findViewById<TextView>(R.id.linked_item_text)


        fun bindItem(item: Item){
            deleteIcon.visibility = View.INVISIBLE
            linktext.visibility = View.INVISIBLE
            itemName?.text = item.itemName
            itemPrice?.text = "$ " + item.itemPrice
            itemCategory?.text = item.itemCategory
            categoryIcon?.setImageResource(item.categoryIcon.toInt())

            if (FunctionRecycler.getcurrentUser() == item.user){
                deleteIcon.visibility = View.VISIBLE
                deleteIcon.setOnClickListener {
                itemOptionsClickListener.itemOptionsMenuClicked(item)
            }}else{
                linktext.visibility = View.VISIBLE
            }

        }
    }
}