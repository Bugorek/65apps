package com.example.application.contactRoute

import android.content.Context
import com.a65apps.pandaananass.tetsapplication.contactRoute.ContactRouteData
import com.a65apps.pandaananass.tetsapplication.contactRoute.ContactRoutePresenter
import com.a65apps.pandaananass.tetsapplication.contactRoute.DirectionApi
import com.a65apps.pandaananass.tetsapplication.database.AppDatabase
import com.a65apps.pandaananass.tetsapplication.database.DatabaseOpenHelper
import com.example.domain.contactList.ContactListOwner
import com.example.domain.contactMap.ContactDataOwner
import com.example.domain.contactRoute.ContactRouteInteractor
import com.example.domain.contactRoute.ContactRouteModel
import com.example.domain.contactRoute.ContactRouteOwner
import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://maps.googleapis.com/"

@Module
class ContactRouteModule {
    @Provides
    @ContactRouteScope
    @ContactRouteRetrofit
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()

    @Provides
    @ContactRouteScope
    fun provideDirectionApi(@ContactRouteRetrofit retrofit: Retrofit): DirectionApi =
        retrofit.create(DirectionApi::class.java)

    @Provides
    @ContactRouteScope
    fun provideContactRouteOwner(context: Context, directionApi: DirectionApi): ContactRouteOwner =
        ContactRouteData(context, directionApi)

    @Provides
    @ContactRouteScope
    fun provideContactDataOwner(database: AppDatabase): ContactDataOwner =
        DatabaseOpenHelper(database.contactDao())

    @Provides
    @ContactRouteScope
    fun provideContactRouteModel(
        contactDataSource: ContactListOwner,
        contactDataOwner: ContactDataOwner,
        contactRouteOwner: ContactRouteOwner
    ): ContactRouteInteractor = ContactRouteModel(
        contactDataSource = contactDataSource,
        contactDataOwner = contactDataOwner,
        contactRouteData = contactRouteOwner
    )

    @Provides
    @ContactRouteScope
    fun provideContactRoutePresenter(contactRouteModel: ContactRouteInteractor) =
        ContactRoutePresenter(contactRouteModel)
}