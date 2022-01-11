package com.theapache64.stackzy.di.module

import com.github.theapache64.retrosheet.RetrosheetInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.theapache64.stackzy.data.remote.ApiInterface
import com.theapache64.stackzy.data.util.calladapter.flow.FlowResourceCallAdapterFactory
import com.toxicbakery.logging.Arbor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
class NetworkModule {

    companion object {
        const val TABLE_CATEGORIES = "categories"
        const val TABLE_LIBRARIES = "libraries"
        const val TABLE_UNTRACKED_LIBS = "untracked_libs"
        const val TABLE_RESULTS = "results"
        const val TABLE_CONFIG = "config"
        const val TABLE_FUN_FACTS = "fun_facts"
    }

    @Singleton
    @Provides
    fun provideRetrosheetInterceptor(): RetrosheetInterceptor {
        return RetrosheetInterceptor.Builder()
            .setLogging(true)
            .addSheet(
                sheetName = TABLE_CATEGORIES,
                "id", "name"
            )
            .addSheet(
                sheetName = TABLE_FUN_FACTS,
                "id", "fun_fact"
            )
            .addSheet(
                sheetName = TABLE_LIBRARIES,
                "id", "name", "package_name", "category", "website"
            )
            .addSheet(
                sheetName = TABLE_UNTRACKED_LIBS,
                "created_at", "package_names"
            )
            .addSheet(
                sheetName = TABLE_CONFIG,
                "should_consider_result_cache",
                "current_lib_version_code"
            )
            .addForm(
                TABLE_UNTRACKED_LIBS,
                "https://docs.google.com/forms/d/e/1FAIpQLSdWuRkjXqBkL-w5NfktA_ju_sI2bJTDVb4LoYco4mxEpskU9g/viewform?usp=sf_link", // TODO: Change this
            )
            .addSheet(
                sheetName = TABLE_RESULTS,
                "created_at",
                "app_name",
                "package_name",
                "version_name",
                "version_code",
                "stackzy_lib_version",
                "lib_packages",
                "platform",
                "apk_size_in_mb",
                "permissions",
                "gradle_info_json",
                "logo_image_url"
            )
            .addForm(
                endPoint = TABLE_RESULTS,
                formLink = "https://docs.google.com/forms/d/e/1FAIpQLSdTiqu2iS-dRLV106uvcKnrZUPnX2x-qF1FlWUjxsfmkNJ0-A/viewform?usp=sf_link"
            )
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(retrosheetInterceptor: RetrosheetInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(retrosheetInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://docs.google.com/spreadsheets/d/1DZ_s2aSJZ4WsgsfZnVQIB1us7Dq7WfxDdpbvLo07pMg/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(FlowResourceCallAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiInterface(retrofit: Retrofit): ApiInterface {
        Arbor.d("Creating new API interface")
        return retrofit.create(ApiInterface::class.java)
    }
}