package com.microservices.demo.elastic.query.web.client.config;

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


import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;

import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;

@Configuration
public class WebClientConfig {

    private final ElasticQueryWebClientConfigData.WebClient webClientData;

    private final UserConfig userConfigData;

    public WebClientConfig (final ElasticQueryWebClientConfigData webClientData, final UserConfig userConfigData) {
        this.webClientData = webClientData.getWebClient ();
        this.userConfigData = userConfigData;
    }

    @LoadBalanced
    @Bean ("webClientBuilder")
    WebClient.Builder webClientBuilder () {


        return WebClient.builder ()
                .filter (ExchangeFilterFunctions
                        .basicAuthentication (userConfigData.getUsername (), userConfigData.getPassword ()))
                .baseUrl (webClientData.getBaseUrl ())
                .defaultHeader (HttpHeaders.CONTENT_TYPE, webClientData.getContentType ())
                .defaultHeader (HttpHeaders.ACCEPT, webClientData.getAcceptType ())
                .clientConnector (new ReactorClientHttpConnector (HttpClient.from (getTcpClient ())))
                .codecs (clientCodecConfigurer -> clientCodecConfigurer
                        .defaultCodecs ()
                        .maxInMemorySize (webClientData.getMaxInMemorySize ()));
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
