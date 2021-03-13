package com.theapache64.stackzy.data.repo

import com.squareup.moshi.Moshi
import com.theapache64.stackzy.data.local.GradleInfo
import com.theapache64.stackzy.data.local.GradleInfoJsonAdapter
import com.theapache64.stackzy.data.remote.ApiInterface
import com.theapache64.stackzy.data.remote.Result
import javax.inject.Inject

class ResultRepo @Inject constructor(
    private val apiInterface: ApiInterface,
    private val moshi: Moshi
) {
    fun add(result: Result) = apiInterface.addResult(result)
    fun findResult(packageName: String, versionCode: Long) = apiInterface.getResult(packageName, versionCode)

    private val gradleInfoAdapter by lazy {
        GradleInfoJsonAdapter(moshi)
    }

    fun parseGradleInfo(gradleInfoJson: String): GradleInfo? {
        return gradleInfoAdapter.fromJson(gradleInfoJson)
    }

    fun jsonify(gradleInfo: GradleInfo): String {
        return gradleInfoAdapter.toJson(gradleInfo)
    }
}