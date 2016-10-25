package mansonheart.com.realmexamples

import android.app.Application
import io.realm.Realm

/**
 * Created by alexandr on 25.10.16.
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}