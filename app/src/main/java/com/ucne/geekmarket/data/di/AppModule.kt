package com.ucne.geekmarket.data.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.ucne.geekmarket.data.local.database.GeekMarketDb
import com.ucne.geekmarket.data.remote.ProductoApi
import com.ucne.geekmarket.data.remote.PromocionApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun providesMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun providesProductoApi(moshi: Moshi): ProductoApi {
        return Retrofit.Builder()
            .baseUrl("https://ap2ticket.azurewebsites.net/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ProductoApi::class.java)
    }

    @Provides
    @Singleton
    fun providePromocionApi(moshi: Moshi): PromocionApi {
        return Retrofit.Builder()
            .baseUrl("https://ap2ticket.azurewebsites.net/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(PromocionApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGeekMarketDb(@ApplicationContext appContext: Context): GeekMarketDb {
        return Room.databaseBuilder(
            appContext,
            GeekMarketDb::class.java,
            "GeekMarketDb"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideProductoDao(database: GeekMarketDb) = database.productoDao()
    @Provides
    fun provideItemDao(database: GeekMarketDb) = database.itemDao()

    @Provides
    fun provideFireBase() = FirebaseAuth.getInstance()

    @Provides
    fun provideCarritoDao(database: GeekMarketDb) = database.carritoDao()

    @Provides
    fun providePersona(database: GeekMarketDb) = database.personaDao()
    @Provides
    fun privedePromocionDao(database: GeekMarketDb) = database.promocionDao()

    @Provides
    fun provideWishListDao(database: GeekMarketDb) = database.wishListDao()

}