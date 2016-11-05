package mansonheart.com.realmsandbox.realm

import io.realm.Realm
import io.realm.RealmObject
import rx.AsyncEmitter
import rx.Emitter
import rx.Observable
import rx.functions.Action1

/**
 * Created by alexandr on 23.10.16.
 */
open class RealmRepository(val logger: (String) -> Unit = {}) {

    fun <T : RealmObject> get(clazz: Class<T>, task: (List<T>) -> Unit): RealmThread {
        val taskWrapper: (Realm) -> Unit = {
            realm ->
            do {
                logger.invoke("Get data on thread ${Thread.currentThread().name}")
                var results = realm.where(clazz).findAll()
                task.invoke(realm.copyFromRealm(results))
            } while (realm.waitForChange())
        }
        val realmThread = RealmThread(taskWrapper)
        realmThread.start()
        return realmThread
    }

    fun insert(obj: RealmObject) {
        val realm = getRealm()
        try {
            realm.executeTransaction {
                logger.invoke("Insert object on thread ${Thread.currentThread().name}")
                realm.insertOrUpdate(obj)
            }
        } catch (exc: Exception) {
            logger.invoke("Exception ${exc}")
        } finally {
            if (!realm.isClosed) {
                logger.invoke("Realm was closed on thread ${Thread.currentThread().name}")
                realm.close()
            }
        }
    }

    fun unsubscribe(realmThread: RealmThread) {
        logger.invoke("Unsubscribe on thread ${realmThread.name}")
        realmThread.shutdown()
        logger.invoke("Realm was closed on thread ${realmThread.name}")
    }

    fun <T : RealmObject> asObservable(clazz: Class<T>): Observable<List<T>> {
        return Observable.fromEmitter({ emitter ->
            logger.invoke("Emitter on thread ${Thread.currentThread().name}")
            val taskWrapper: (Realm) -> Unit = {
                realm ->
                do {
                    logger.invoke("Get data on thread ${Thread.currentThread().name}")
                    var results = realm.where(clazz).findAll()
                    emitter.onNext(realm.copyFromRealm(results))
                } while (realm.waitForChange())
            }
            val realmThread = RealmThread(taskWrapper)
            realmThread.start()
        }, AsyncEmitter.BackpressureMode.BUFFER)
    }

    private fun getRealm(): Realm {
        return Realm.getDefaultInstance();
    }

}