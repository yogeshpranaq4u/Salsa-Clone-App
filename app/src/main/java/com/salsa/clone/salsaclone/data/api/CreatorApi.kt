package com.salsa.clone.salsaclone.data.api


import com.salsa.clone.salsaclone.data.model.Creator
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface CreatorApi {
    @GET("creators")
    suspend fun getCreators(): retrofit2.Response<List<Creator>>

    companion object {
        fun create(): CreatorApi {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://mockapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.Builder().addInterceptor { chain ->
                    val fakeJson = """
                        [{"id":1,"name":"Name 1","profileImage":"https://static.vecteezy.com/system/resources/thumbnails/029/052/780/small/stylish-young-woman-dressed-in-spring-trendy-dress-photo.jpg","thumbImage":"https://res.cloudinary.com/worldpackers/image/upload/c_limit,f_auto,q_auto,w_1140/waunaxzgmyu3sbjxk8jx","views":46,"coins":260},
                         {"id":2,"name":"Name 2","profileImage":"https://res.cloudinary.com/worldpackers/image/upload/c_limit,f_auto,q_auto,w_1140/waunaxzgmyu3sbjxk8jx","thumbImage":"https://img.freepik.com/free-photo/female-tourists-spread-their-arms-held-their-wings_1150-7456.jpg?semt=ais_hybrid&w=740","views":294,"coins":260},
                         {"id":3,"name":"Name 3","profileImage":"https://res.cloudinary.com/worldpackers/image/upload/c_limit,f_auto,q_auto,w_1140/waunaxzgmyu3sbjxk8jx","thumbImage":"https://st.depositphotos.com/62628780/61583/i/450/depositphotos_615834254-stock-photo-black-woman-portrait-smile-travel.jpg","views":245,"coins":260},
                         {"id":4,"name":"Name 4","profileImage":"https://res.cloudinary.com/worldpackers/image/upload/c_limit,f_auto,q_auto,w_1140/waunaxzgmyu3sbjxk8jx","thumbImage":"https://static.vecteezy.com/system/resources/thumbnails/029/052/780/small/stylish-young-woman-dressed-in-spring-trendy-dress-photo.jpg","views":134,"coins":260},
                         {"id":5,"name":"Name 5","profileImage":"https://res.cloudinary.com/worldpackers/image/upload/c_limit,f_auto,q_auto,w_1140/waunaxzgmyu3sbjxk8jx","thumbImage":"https://images.pexels.com/photos/2033343/pexels-photo-2033343.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500","views":134,"coins":260},
                         {"id":6,"name":"Name 6","profileImage":"https://res.cloudinary.com/worldpackers/image/upload/c_limit,f_auto,q_auto,w_1140/waunaxzgmyu3sbjxk8jx","thumbImage":"https://thumbs.dreamstime.com/b/disco-girl-22727757.jpg","views":134,"coins":260},
                         {"id":7,"name":"Name 7","profileImage":"https://res.cloudinary.com/worldpackers/image/upload/c_limit,f_auto,q_auto,w_1140/waunaxzgmyu3sbjxk8jx","thumbImage":"https://static.vecteezy.com/system/resources/thumbnails/026/846/055/small/young-man-tourist-in-sunglasses-and-hat-taking-selfie-in-nature-by-the-lake-in-mountains-made-with-generative-ai-photo.jpg","views":134,"coins":260},
                         {"id":8,"name":"Name 8","profileImage":"https://res.cloudinary.com/worldpackers/image/upload/c_limit,f_auto,q_auto,w_1140/waunaxzgmyu3sbjxk8jx","thumbImage":"https://img.freepik.com/free-photo/portrait-cool-man-white-tee-looking-into-distance-smiling-brunette-guy-sunglasses-smiles-holds-passport-near-airport_197531-27051.jpg?semt=ais_hybrid&w=740","views":134,"coins":260},
                         {"id":9,"name":"Name 9","profileImage":"https://res.cloudinary.com/worldpackers/image/upload/c_limit,f_auto,q_auto,w_1140/waunaxzgmyu3sbjxk8jx","thumbImage":"https://as1.ftcdn.net/jpg/02/56/42/34/1000_F_256423484_Y3x3oDTmVbo0IvAKMfM6OwXopbZBTjZr.jpg","views":134,"coins":260},
                         {"id":10,"name":"Name 10","profileImage":"https://res.cloudinary.com/worldpackers/image/upload/c_limit,f_auto,q_auto,w_1140/waunaxzgmyu3sbjxk8jx","thumbImage":"https://media.istockphoto.com/id/1434780993/photo/young-tourist-using-a-map-on-a-smart-phone.jpg?s=612x612&w=0&k=20&c=93kU90iXp51mH5z17DZFYMoU-sonoTUIsQtP9J1aYb0=","views":134,"coins":260},
                         {"id":11,"name":"Name 11","profileImage":"https://res.cloudinary.com/worldpackers/image/upload/c_limit,f_auto,q_auto,w_1140/waunaxzgmyu3sbjxk8jx","thumbImage":"https://images.unsplash.com/photo-1660645073685-6d3e4804a6be?fm=jpg&q=60&w=3000&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8bWFuJTIwYW5kJTIwd29tYW4lMjBpbiUyMGxvdmV8ZW58MHx8MHx8fDA%3D","views":134,"coins":260},
                         {"id":12,"name":"Name 12","profileImage":"https://res.cloudinary.com/worldpackers/image/upload/c_limit,f_auto,q_auto,w_1140/waunaxzgmyu3sbjxk8jx","thumbImage":"https://images.unsplash.com/photo-1660645072527-73f1fc70aa83?fm=jpg&q=60&w=3000&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8N3x8bWFuJTIwYW5kJTIwd29tYW4lMjBpbiUyMGxvdmV8ZW58MHx8MHx8fDA%3D","views":134,"coins":260}]
                    """.trimIndent()
                    val response = Response.Builder()
                        .code(200)
                        .message("OK")
                        .protocol(Protocol.HTTP_1_1)
                        .request(chain.request())
                        .body(
                            fakeJson.toResponseBody("application/json".toMediaTypeOrNull())
                        )
                        .build()
                    response
                }.build())
                .build()
            return retrofit.create(CreatorApi::class.java)
        }
    }
}
