package com.nmt.minhtu.doan.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.nmt.minhtu.doan.R
import com.nmt.minhtu.doan.adapter.AdminListTourAdapter
import com.nmt.minhtu.doan.api.ApiService
import com.nmt.minhtu.doan.data_local.DataLocalManager
import com.nmt.minhtu.doan.model.Favorite
import com.nmt.minhtu.doan.model.Tour
import com.nmt.minhtu.doan.utils.Utils
import kotlinx.android.synthetic.main.fragment_notifi.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentFavorite : Fragment() {

    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(Utils.isLogin()) {
            loadData()
        }
        initListener()
    }


    private fun initListener() {
        swipe_reload.setOnRefreshListener { loadData() }

    }

    private fun loadData() {
        showProgress()
        val user = DataLocalManager.getUser()
        ApiService.apiService.getListFavorite(user.id)
            .enqueue(object : Callback<List<Favorite>> {
                override fun onResponse(
                    call: Call<List<Favorite>>,
                    response: Response<List<Favorite>>
                ) {
                    swipe_reload.isRefreshing = false
                    if (response.isSuccessful) {
                        val bookedTourList = mutableListOf<Tour>()
                        Log.d("TAG", "onResponse: ${response.body()?.size}")
                        if(!response.body().isNullOrEmpty()) {
                            response.body()?.forEach{
                                bookedTourList.add(it.tour)
                            }
                        }
                        val adapter = AdminListTourAdapter(
                            bookedTourList,
                            context
                        )
                        rvc_list_tour.adapter = adapter
                        rvc_list_tour.layoutManager = LinearLayoutManager(
                            context,
                            RecyclerView.VERTICAL,
                            false
                        )
                        progressDialog?.dismiss()
                    }
                }

                override fun onFailure(call: Call<List<Favorite>>, t: Throwable) {
                    swipe_reload.isRefreshing = false
                    Toast.makeText(requireContext(), "Có lỗi xảy ra", Toast.LENGTH_LONG)
                        .show()

                }
            })
    }

    private fun showProgress() {
        progressDialog = ProgressDialog(context)
        progressDialog?.apply {
            setMessage("Xin hãy đợi giây lát...!")
            show()
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

}