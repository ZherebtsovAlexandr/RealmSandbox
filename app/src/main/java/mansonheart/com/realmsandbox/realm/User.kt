package mansonheart.com.realmsandbox.realm

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by alexandr on 24.10.16.
 */

open class User(
        @PrimaryKey open var id: Int = 0,
        open var name: String = ""
) : RealmObject() {
    var pets: RealmList<Pet> = RealmList()
}
