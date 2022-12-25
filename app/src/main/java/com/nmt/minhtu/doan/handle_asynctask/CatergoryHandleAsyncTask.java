package com.nmt.minhtu.doan.handle_asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.nmt.minhtu.doan.ImgFromGrallery;
import com.nmt.minhtu.doan.model.Category;

public class CatergoryHandleAsyncTask extends AsyncTask<Intent, Void, Category>{
    ProgressDialog progressDialog;
    Context context;
    ImageView imgAvt;
    EditText editName;

    public CatergoryHandleAsyncTask(Context context, ImageView imgAvt, EditText editName) {
        this.context = context;
        this.imgAvt = imgAvt;
        this.editName = editName;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Hiển thị Dialog khi bắt đầu xử lý.
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Dang xu ly...");
        progressDialog.show();
    }

    @Override
    protected Category doInBackground(Intent... intents) {
        Intent intent = intents[0];
        Category category = (Category) intent.getSerializableExtra("currentCategory");
        Log.d("TAG", "doInBackground: "+category.toString());
        return category;
    }

    @Override
    protected void onPostExecute(Category category) {
        super.onPostExecute(category);
        progressDialog.dismiss();
        imgAvt.setImageBitmap(ImgFromGrallery.deCodeToBase64(category.getImg()));
        editName.setText(category.getName());

    }
}
