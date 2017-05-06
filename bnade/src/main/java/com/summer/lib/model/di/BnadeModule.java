package com.summer.lib.model.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.summer.lib.model.api.BnadeApi;
import com.summer.lib.model.entity.Auction;
import com.summer.lib.model.entity.AuctionHistory;
import com.summer.lib.model.entity.AuctionItem;
import com.summer.lib.model.entity.AuctionRealmItem;
import com.summer.lib.model.entity.WowTokens;
import com.summer.lib.model.gson.AuctionHistoryParser;
import com.summer.lib.model.gson.AuctionItemParser;
import com.summer.lib.model.gson.AuctionParser;
import com.summer.lib.model.gson.AuctionRealmItemParser;
import com.summer.lib.model.gson.WowTokensParser;

import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kevin.bai on 2017/4/3.
 */

@Module
public class BnadeModule {
    public static final String BNADE = "BNADE";

    @Singleton
    @Named(BNADE)
    @Provides
    Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapter(AuctionItem.class, AuctionItemParser.create())
                .registerTypeAdapter(WowTokens.class, WowTokensParser.create())
                .registerTypeAdapter(Auction.class, AuctionParser.create())
                .registerTypeAdapter(AuctionRealmItem.class, AuctionRealmItemParser.create())
                .registerTypeAdapter(AuctionHistory.class, AuctionHistoryParser.create())
                .create();
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient() {
        X509TrustManager trustManager;
        SSLSocketFactory sslSocketFactory;

        trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };

        try {
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new X509TrustManager[]{trustManager}, null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        return new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustManager)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .build();
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient client, @Named(BNADE) Gson gson) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BnadeApi.BASE_URL)
                .client(client)
                .build();
    }

    @Singleton
    @Provides
    BnadeApi provideBnadeApi(Retrofit retrofit) {
        return retrofit.create(BnadeApi.class);
    }

}
