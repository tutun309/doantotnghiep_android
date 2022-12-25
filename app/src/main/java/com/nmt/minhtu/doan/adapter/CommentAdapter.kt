package com.nmt.minhtu.doan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.nmt.minhtu.doan.adapter.BookedTourAdaper.BookedTourViewHolder
import com.nmt.minhtu.doan.model.BookedTour

/*
class CommentAdapterprivate(
    private var employeeList: MutableList<Employee>
) : RecyclerView.Adapter<EmployeeListAdapter.EmployeeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val itemEmployeeBinding: ItemEmployeeBinding =
            ItemEmployeeBinding.inflate(inflater, parent, false);
        return EmployeeViewHolder(itemEmployeeBinding)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        holder.onBind(employeeList[holder.adapterPosition])
    }

    override fun getItemCount(): Int {
        return employeeList.size
    }

    inner class EmployeeViewHolder(private var itemEmployeeBinding: ItemEmployeeBinding) :
        RecyclerView.ViewHolder(itemEmployeeBinding.root) {
        fun onBind(employee: Employee) {
            itemEmployeeBinding.employee = employee
            itemEmployeeBinding.btnRemove.setOnClickListener {
                iClickItem.onClickRemoveItem(adapterPosition)
            }
            itemEmployeeBinding.btnEdit.setOnClickListener {
                iClickItem.onClickUpdateItem(employeeList[adapterPosition], adapterPosition)
            }
        }
    }

    fun addEmployee(employee: Employee) {
        var index = employeeList.size
        employeeList.add(employee)
        if (index > 0) {
            notifyItemChanged(index)
        } else {
            notifyItemChanged(0)
        }
    }

    fun removeEmployee(position: Int) {
        employeeList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateEmployee(employee: Employee, position: Int) {
        employeeList.set(position, employee)
        notifyItemChanged(position)
    }

    fun setListEmployee(employeeList: MutableList<Employee>) {
        this.employeeList = employeeList
        notifyDataSetChanged()
    }
}*/
