package com.techno_3_team.task_manager.api

import com.techno_3_team.task_manager.structures.ListOfTasks
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface TasksAPILists {
    /**
     * Удаляет указанный список задач аутентифицированного пользователя.
     * https://developers.google.com/tasks/reference/rest/v1/tasklists/delete?hl=ru
     * @param taskListId идентификатор списка
     * @return идентификатор, что задача была выполнена типа Completable
     * */
    @DELETE("users/@me/lists/{taskList}")
    fun deleteListOfTasks(@Path("taskList") taskListId: String): Completable

    /**
     * Возвращает указанный список задач аутентифицированного пользователя.
     * https://developers.google.com/tasks/reference/rest/v1/tasklists/get?hl=ru
     * @param taskListId идентификатор списка
     * @return список
     * */
    @GET("users/@me/lists/{tasklist}")
    fun getListOfTasks(@Path("taskList") taskListId: String): Single<ListOfTasks>

    /**
     * Создаёт новый список задач и добавляет его в списки задач аутентифицированного пользователя.
     * https://developers.google.com/tasks/reference/rest/v1/tasklists/insert?hl=ru
     * @param taskListFields параметры списка
     * @return список
     * */
    @POST("users/@me/lists")
    fun insertListOfTasks(@Body taskListFields: ListOfTasks): Single<ListOfTasks>

    /**
     * Возвращает все списки задач аутентифицированного пользователя.
     * https://developers.google.com/tasks/reference/rest/v1/tasklists/list?hl=ru
     * @param ETag юзается для проверки, изменились ли данные на сервере
     * @return Single_response
     * */
    @GET("users/@me/lists")
    fun listListOfTaskss(@Header("If-None-Match") ETag: String): Flowable<ListOfTasks>

    /**
     * Лечит указанный список задач аутентифицированного пользователя.
     * https://developers.google.com/tasks/reference/rest/v1/tasklists/patch?hl=ru
     * @param taskListId идентификатор списка
     * @param taskListFields параметры списка
     * @return лист
     * */
    @PATCH("users/@me/lists/{taskList}")
    fun patchListOfTasks(
        @Path("taskList") taskListId: String,
        @Body taskListFields: ListOfTasks
    ): Single<ListOfTasks>

    /**
     * Обновляет указанный список задач аутентифицированного пользователя.
     * https://developers.google.com/tasks/reference/rest/v1/tasklists/update?hl=ru
     * @param taskListId идентификатор списка
     * @param taskListFields параметры списка
     * @return лист
     * */
    @PUT("users/@me/lists/{taskList}")
    fun updateListOfTasks(
        @Path("taskList") taskListId: String,
        @Body taskListFields: ListOfTasks
    ): Single<ListOfTasks>


}