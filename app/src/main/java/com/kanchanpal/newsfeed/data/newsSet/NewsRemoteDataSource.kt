package com.kanchanpal.newsfeed.data.newsSet

import com.kanchanpal.newsfeed.api.BaseDataSource
import com.kanchanpal.newsfeed.api.NewsListResponse
import com.kanchanpal.newsfeed.api.NewsService
import com.kanchanpal.newsfeed.common.COUNTRY
import com.kanchanpal.newsfeed.data.Result
import javax.inject.Inject

class NewsRemoteDataSource @Inject constructor(private val service: NewsService) : BaseDataSource() {

    suspend fun fetchNewsList(apiKey : String, page : Int, pageSize : Int ) : Result<NewsListResponse> {
        return getResult { service.getTopNewsList(apiKey, page,pageSize, COUNTRY) }
    }
}
