package mansonheart.com.realmsandbox.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by alexandr on 29.10.16.
 */

open class Pet(
        @PrimaryKey open var id: Int = 0,
        open var name: String = "User's pet"
) : RealmObject()
