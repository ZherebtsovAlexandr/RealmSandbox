package mansonheart.com.realmexamples.realm

import io.realm.Realm
import io.realm.RealmObject

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
        logger.invoke("Unsubscribe on thread ${Thread.currentThread().name}")
        realmThread.shutdown()
        logger.invoke("Realm was closed on thread ${Thread.currentThread().name}")
    }

    private fun getRealm(): Realm {
        return Realm.getDefaultInstance();
    }

}