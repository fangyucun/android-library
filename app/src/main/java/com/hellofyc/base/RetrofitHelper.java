package com.hellofyc.base;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created on 2016/8/24.
 *
 * @author Yucun Fang
 */

public class RetrofitHelper {
    private static final String HOST = "";

    private static final OkHttpClient.Builder mHttpClientBuilder = new OkHttpClient.Builder();

    static {
        mHttpClientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(15, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
    }

    private static class DefaultAuthenticator implements Authenticator {

        @Override
        public Request authenticate(Route route, Response response) throws IOException {
            return response.request().newBuilder()
                    .addHeader("Authorization", "i am fyc1!")
                    .build();
        }
    }

    private static class TokenInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request authorised = originalRequest.newBuilder()
                    .header("Authorization", "i am fyc!")
                    .build();
            return chain.proceed(authorised);
        }
    }

    private static class EncryptInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request authorised = originalRequest.newBuilder()
                    .header("Authorization", "i am fyc!")
                    .method(originalRequest.method(), originalRequest.body())
                    .build();
            return chain.proceed(authorised);
        }
    }

    public static Retrofit.Builder mRetrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(HOST)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create()));

    public static <S> S createService(Class<S> serviceClass) {
        OkHttpClient client = mHttpClientBuilder.build();
        Retrofit retrofit = mRetrofitBuilder.client(client).build();
        return retrofit.create(serviceClass);
    }

    public static String readRequestBody(RequestBody requestBody) {
        if (requestBody == null) return "";
        try {
            final Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
