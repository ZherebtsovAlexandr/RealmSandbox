package mansonheart.com.realmexamples.realm

import io.realm.Realm

/**
 * Created by alexandr on 23.10.16.
 */

class RealmThread : Thread {

    private val task: (Realm) -> Unit
    private lateinit var realm: Realm


    constructor(task: (Realm) -> Unit) {
        this.task = task
    }

    override fun run() {
        realm = Realm.getDefaultInstance()
        task(realm)
        synchronized(this) {
            if (!realm.isClosed()) {
                realm.close();
            }
        }
    }

    fun shutdown() {
        synchronized (this) {
            realm.stopWaitForChange()
        }
    }

}
