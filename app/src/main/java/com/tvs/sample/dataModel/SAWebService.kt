package app.sample.com.product.DataModels

import com.tvs.sample.entities.TableData
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by selvak
 */

interface SAWebService {

    /**
     * Get a list of data.
     */
    @POST("api/test_new/int/gettabledata.php")
    fun login(@Body aJsonObject: RequestBody, @Header("Content-Type") aType: String): Call<TableData>


}
