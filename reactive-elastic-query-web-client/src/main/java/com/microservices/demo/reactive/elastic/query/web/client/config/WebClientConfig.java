package com.microservices.demo.reactive.elastic.query.web.client.config;

import com.microservices.demo.config.ElasticQueryWebClientConfigData;
import com.microservices.demo.config.UserConfig;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    private final ElasticQueryWebClientConfigData.WebClient webClientData;

    private final UserConfig userConfigData;

    public WebClientConfig (final ElasticQueryWebClientConfigData webClientData, final UserConfig userConfigData) {
        this.webClientData = webClientData.getWebClient ();
        this.userConfigData = userConfigData;
    }

    @LoadBalanced
    @Bean ("webClient")
    WebClient webClient () {
        return WebClient.builder ()
                .baseUrl (webClientData.getBaseUrl ())
                .defaultHeader (HttpHeaders.CONTENT_TYPE, webClientData.getContentType ())
                .clientConnector (new ReactorClientHttpConnector (HttpClient.from (getTcpClient ())))
                .build ();

    }

    private TcpClient getTcpClient () {
        return TcpClient.create ()
                .option (ChannelOption.CONNECT_TIMEOUT_MILLIS, webClientData.getConnectTimeoutMs ())
                .doOnConnected (connection -> {
                    connection.addHandlerLast (
                            new ReadTimeoutHandler (webClientData.getReadTimeoutMs (),
                                    TimeUnit.MILLISECONDS));
                    connection.addHandlerLast (
                            new WriteTimeoutHandler (webClientData.getWriteTimeoutMs (),
                                    TimeUnit.MILLISECONDS));
                });
    }

}
