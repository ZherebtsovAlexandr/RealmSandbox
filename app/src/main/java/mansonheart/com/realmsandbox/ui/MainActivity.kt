package mansonheart.com.realmsandbox.ui

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import mansonheart.com.realmsandbox.R
import mansonheart.com.realmsandbox.realm.RealmRepository
import mansonheart.com.realmsandbox.realm.RealmThread
import mansonheart.com.realmsandbox.realm.User
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    val logAdapter: ListAdapter = ListAdapter(this)
    val itemsAdapter: ListAdapter = ListAdapter(this)

    var realmThread: RealmThread? = null
    val realmRepository = RealmRepository({ log ->
        runOnUiThread {
            logAdapter.add(log)
            var countItem = 0
            countItem = rvLog?.adapter?.itemCount!!
            rvLog?.smoothScrollToPosition(countItem)
        }
    })

    var rvLog: RecyclerView? = null
    var rvItems: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar?
        rvLog = findViewById(R.id.rv_log) as RecyclerView?
        rvItems = findViewById(R.id.rv_items) as RecyclerView?
        setSupportActionBar(toolbar)

        initItemsRecyclerView()
        initLogRecyclerView()

        val task: (List<User>) -> Unit = { results ->
            runOnUiThread {
                var items: MutableList<String> = ArrayList()
                results.forEach {
                    r ->
                    items.add(r.name)
                }
                itemsAdapter.clear()
                itemsAdapter.add(items)
                rvItems?.smoothScrollToPosition(items.size)
            }
        }

        realmThread = realmRepository.get(User::class.java, task)

        val fabInsert = findViewById(R.id.fab_insert) as FloatingActionButton?
        fabInsert!!.setOnClickListener {
            view ->
            val random = Random()
            thread {
                realmRepository.insert(User(random.nextInt(Int.MAX_VALUE),
                        "User #${random.nextInt(Int.MAX_VALUE)}"))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_unsubscribe) {
            realmRepository.unsubscribe(realmThread!!)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun initLogRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        rvLog?.layoutManager = linearLayoutManager
        rvLog?.adapter = logAdapter
    }

    fun initItemsRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        rvItems?.layoutManager = linearLayoutManager
        rvItems?.adapter = itemsAdapter
    }
}
