package com.example.application.contactMap

import android.content.Context
import com.a65apps.pandaananass.tetsapplication.contactMap.ContactAddressData
import com.a65apps.pandaananass.tetsapplication.contactMap.ContactMapPresenter
import com.a65apps.pandaananass.tetsapplication.contactMap.GeocodingApi
import com.a65apps.pandaananass.tetsapplication.database.AppDatabase
import com.a65apps.pandaananass.tetsapplication.database.DatabaseOpenHelper
import com.example.domain.contactMap.ContactAddressOwner
import com.example.domain.contactMap.ContactDataOwner
import com.example.domain.contactMap.ContactMapInteractor
import com.example.domain.contactMap.ContactMapModel
import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ContactMapModule {
    @Provides
    @ContactMapScope
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://maps.googleapis.com/")
            .build()

    @Provides
    @ContactMapScope
    fun provideGeocodingApi(retrofit: Retrofit): GeocodingApi =
        retrofit.create(GeocodingApi::class.java)

    @Provides
    @ContactMapScope
    fun provideContactAddressOwner(
        context: Context,
        geocodingApi: GeocodingApi
    ): ContactAddressOwner =
        ContactAddressData(context, geocodingApi)

    @Provides
    @ContactMapScope
    fun provideContactDataOwner(database: AppDatabase): ContactDataOwner =
        DatabaseOpenHelper(database.contactDao())

    @Provides
    @ContactMapScope
    fun provideContactMapInteractor(
        contactAddressOwner: ContactAddressOwner,
        contactDataOwner: ContactDataOwner
    ): ContactMapInteractor = ContactMapModel(contactAddressOwner, contactDataOwner)

    @Provides
    @ContactMapScope
    fun provideContactMapPresenter(contactMapInteractor: ContactMapInteractor): ContactMapPresenter =
        ContactMapPresenter(contactMapInteractor)
}
