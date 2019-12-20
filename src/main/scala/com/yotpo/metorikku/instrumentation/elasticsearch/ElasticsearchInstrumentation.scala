package com.yotpo.metorikku.instrumentation.elasticsearch


import java.util

import com.yotpo.metorikku.configuration.job.input.Elasticsearch
import com.yotpo.metorikku.instrumentation.{InstrumentationFactory, InstrumentationProvider}
import org.apache.http.HttpHost
import org.apache.http.auth.{AuthScope, UsernamePasswordCredentials}
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder
import org.elasticsearch.client.{RestClient, RestHighLevelClient}
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback
import org.elasticsearch.action.index.IndexRequest

class ElasticsearchInstrumentation(val elasticsearch: RestHighLevelClient, val index: String, val measurement: String) extends InstrumentationProvider {
  override def count(name: String, value: Long, tags: Map[String, String] = Map(), time: Long): Unit = {
    writeToElasticsearch(time, name, value, tags)
  }

  override def gauge(name: String, value: Long, tags: Map[String, String] = Map(), time: Long): Unit = {
    writeToElasticsearch(time, name, value, tags)
  }

  private def writeToElasticsearch(time: Long, name: String, value: Long, tags: Map[String, String] = Map()): Unit = {
    val jsonMap = new util.HashMap[String, Any]()
    jsonMap.put("name", name)
    jsonMap.put("value", value)
    jsonMap.put("tags", tags)
    jsonMap.put("time", time)
    val indexRequest = new IndexRequest(index, "doc").source(jsonMap)
    elasticsearch.index(indexRequest)
  }

  override def close(): Unit = {
     elasticsearch.close()
  }
}

class ElasticsearchInstrumentationFactory(val measurement: String, val config: Elasticsearch) extends InstrumentationFactory {
  override def create(): InstrumentationProvider = {

    val credentialsProvider = new BasicCredentialsProvider
    credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(config.user.get, config.password.get))
    val ipPort = config.nodes.split(":");
    val builder = RestClient.builder(new HttpHost(ipPort(0), Integer.parseInt(ipPort(1))))

    builder.setHttpClientConfigCallback(new HttpClientConfigCallback() {
      override def customizeHttpClient(httpClientBuilder: HttpAsyncClientBuilder): HttpAsyncClientBuilder =
        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
    })

    val client = new RestHighLevelClient(builder)
    new ElasticsearchInstrumentation(client, config.index, measurement)
  }
}
