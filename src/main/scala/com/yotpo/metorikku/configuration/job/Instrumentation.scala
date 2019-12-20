package com.yotpo.metorikku.configuration.job

import com.yotpo.metorikku.configuration.job.input.Elasticsearch
import com.yotpo.metorikku.configuration.job.instrumentation.InfluxDBConfig

case class Instrumentation(influxdb: Option[InfluxDBConfig],elasticsearch: Option[Elasticsearch])

