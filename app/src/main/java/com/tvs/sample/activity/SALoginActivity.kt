package com.tvs.sample.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import app.sample.com.product.DataModels.SAWebService
import com.google.gson.Gson
import com.tvs.sample.R
import com.tvs.sample.apiHelper.SAApiClient
import com.tvs.sample.entities.Data
import com.tvs.sample.entities.TableData
import com.tvs.sample.entities.UserData
import com.tvs.sample.utilis.SAHelper.showSnackBar
import com.tvs.sample.utilis.SAHelper.verifyAvailableNetwork
import com.tvs.sample.utilis.SAProgressDialog
import com.tvs.sample.utilis.SASession
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class SALoginActivity : AppCompatActivity() {

    var mProgressDialog: SAProgressDialog? = null

    var mSession: SASession? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        mSession = SASession(this)

        clickListener();
    }


    private fun validation() {

        if (username.text.toString().equals("")) {
            showSnackBar(getString(R.string.username), this)
        } else if (password.text.toString().equals("")) {
            showSnackBar(getString(R.string.password), this)
        } else {
            if (verifyAvailableNetwork(this)) {
                login();
            } else {
                showSnackBar(getString(R.string.internet_check), this)
            }

        }
    }

    private fun login() {
        mProgressDialog = SAProgressDialog(this)
        mProgressDialog?.show()
        val body =
            RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), createUserDeviceDetailJson())
        SAApiClient.client.create(SAWebService::class.java)
            .login(body, "application/json")
            .enqueue(object :
                Callback<TableData> {
                override fun onResponse(call: Call<TableData>, response: Response<TableData>) {
                    if (response.isSuccessful && response.code() == 200) {
                        try {
                            mProgressDialog!!.dismiss()
                            val dataResponse = Gson().fromJson(response.body()?.tableData, Data::class.java)
                            val aUpList = dataResponse.data
                            val list = ArrayList<UserData>()
                            for (subList in aUpList!!) {
                                val dm = UserData()
                                dm.name = subList[0];
                                dm.post = subList[1];
                                dm.location = subList[2];
                                dm.code = subList[3];
                                dm.date_of_joining = subList[4];
                                dm.salary = subList[5];
                                list.add(dm)
                            }

                            saveData(list)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else {
                        mProgressDialog!!.dismiss()
                        showSnackBar(getString(R.string.valid_credential), this@SALoginActivity)
                    }
                }

                override fun onFailure(call: Call<TableData>, t: Throwable) {
                    mProgressDialog!!.dismiss()
                    showSnackBar(getString(R.string.server_error), this@SALoginActivity)
                }
            })
    }

    private fun saveData(aList: ArrayList<UserData>) {
        mSession?.saveListData(aList)
        mSession?.putLoginStatus(true)
        val intent = Intent(this@SALoginActivity, SAHomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun createUserDeviceDetailJson(): String {
        val objJSON = JSONObject()
        try {
            objJSON.put("username", username.text.toString())
            objJSON.put("password", password.text.toString())
        } catch (ex: Exception) {
            println("Error Occured - " + ex.message)
        }
        return objJSON.toString()
    }


    private fun clickListener() {
        login_btn.setOnClickListener {
            validation()
        }
    }

    private fun goToSignUpdScreen() {
        startActivity(Intent(this, SAHomeActivity::class.java))
    }


}
