package com.techno_3_team.task_manager.api

import com.techno_3_team.task_manager.data.model.TaskFields
import com.techno_3_team.task_manager.data.model.Taska
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface TasksAPITasks {
    /**
     * Удаляет все завершенные задачи из указанного списка задач.
     * https://developers.google.com/tasks/reference/rest/v1/tasks/clear?hl=ru
     * @param taskListId идентификатор списка
     * @return идентификатор, что задача была выполнена типа Completable
     */
    @POST("lists/{tasklist}/clear")
    fun clearCompletedTasks(@Path("taskList") taskListId: String): Completable

    /**
     * Удаляет указанную задачу из списка задач.
     * https://developers.google.com/tasks/reference/rest/v1/tasks/delete?hl=ru
     * @param taskListId идентификатор списка
     * @param taskId идентификатор задачи
     * @return идентификатор, что задача была выполнена типа Completable
     */
    @DELETE("lists/{taskList}/tasks/{task}")
    fun deleteTask(
        @Path("taskList") taskListId: String,
        @Path("task") taskId: String
    ): Completable

    /**
     * Возвращает указанную задачу.
     * https://developers.google.com/tasks/reference/rest/v1/tasks/get?hl=ru
     * @param taskListId идентификатор списка
     * @param taskId идентификатор задачи
     * @return таску
     */
    @GET("lists/{tasklist}/tasks/{task}")
    fun getTask(
        @Path("taskList") taskListId: String,
        @Path("task") taskId: String
    ): Single<Taska>

    /**
     * Создает новую задачу в указанном списке задач.
     * https://developers.google.com/tasks/reference/rest/v1/tasks/insert?hl=ru
     * @param taskListId идентификатор списка
     * @param taskFields параметры задачи
     * @return таску
     */
    @POST("lists/{taskList}/tasks")
    fun insertTask(
        @Path("taskList") taskListId: String,
        @Body taskFields: TaskFields
    ): Single<Taska>

    /**
     * Возвращает все задачи в указанном списке задач.
     * https://developers.google.com/tasks/reference/rest/v1/tasks/list?hl=ru
     * @param taskListId идентификатор списка
     * @param ETag юзается для проверки, изменились ли данные на сервере
     * @return список задач
     */
    @GET("lists/{taskList}/tasks")
    fun listTasks(
        @Path("taskList") taskListId: String,
        @Header("If-None-Match") ETag: String
    ): Flowable<Taska>

    /**
     * Перемещает указанную задачу в другую позицию в списке задач.
     * https://developers.google.com/tasks/reference/rest/v1/tasks/move?hl=ru
     * @param taskListId идентификатор списка
     * @param taskId идентификатор задачи
     * @param taskFields параметры задачи
     * @return таску
     */
    @POST("lists/{tasklist}/tasks/{task}/move")
    fun moveTask(
        @Path("taskList") taskListId: String,
        @Path("task") taskId: String,
        @Body taskFields: TaskFields
    ): Single<Taska>

    /**
     * Патчит указанную задачу.
     * https://developers.google.com/tasks/reference/rest/v1/tasks/patch?hl=ru
     * @param taskListId идентификатор списка
     * @param taskId идентификатор задачи
     * @param taskFields параметры задачи
     * @return таску
     */
    @PATCH("lists/{taskList}/tasks/{task}/")
    fun patchTask(
        @Path("taskList") taskListId: String,
        @Path("task") taskId: String,
        @Body taskFields: TaskFields
    ): Single<Taska>

    /**
     * Обновляет указанную задачу.
     * https://developers.google.com/tasks/reference/rest/v1/tasks/update?hl=ru
     * @param taskListId идентификатор списка
     * @param taskId идентификатор задачи
     * @param task сама таска
     * @return таску
     */
    @PUT("lists/{taskList}/tasks/{task}/")
    fun updateTask(
        @Path("taskList") taskListId: String,
        @Path("task") taskId: String,
        @Body task: Taska
    ): Single<Taska>

}
