package com.nmt.minhtu.doan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.nmt.minhtu.doan.ImgFromGrallery
import com.nmt.minhtu.doan.adapter.BookedTourAdaper.BookedTourViewHolder
import com.nmt.minhtu.doan.databinding.ItemCommentBinding
import com.nmt.minhtu.doan.model.BookedTour
import com.nmt.minhtu.doan.model.Comment


class CommentAdapter(
    private var listComment: List<Comment>
) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val itemEmployeeBinding: ItemCommentBinding =
            ItemCommentBinding.inflate(inflater, parent, false);
        return CommentViewHolder(itemEmployeeBinding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.onBind(listComment[holder.adapterPosition])
    }

    override fun getItemCount(): Int {
        return listComment.size
    }

    inner class CommentViewHolder(private var binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(comment: Comment) {
            binding.imgAvt.setImageBitmap(ImgFromGrallery.deCodeToBase64(comment.imgUser))
            binding.tvName.text = comment.userName
            binding.tvRating.rating = comment.rating
            binding.tvContent.text = comment.content
        }
    }

    fun setListEmployee(listComment: List<Comment>) {
        this.listComment = listComment
        notifyDataSetChanged()
    }

}
