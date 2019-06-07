package ir.fanap.chattestapp.application.ui.log

import android.app.SearchManager
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.*
import ir.fanap.chattestapp.R
import ir.fanap.chattestapp.application.ui.MainViewModel
import ir.fanap.chattestapp.application.ui.TestListener
import kotlinx.android.synthetic.main.fragment_log.*
import android.content.Context
import com.fanap.podchat.model.ChatResponse
import com.fanap.podchat.model.ResultThreads
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import ir.fanap.chattestapp.application.ui.IOnBackPressed


class LogFragment : Fragment(), LogListener,IOnBackPressed {

    private lateinit var mainViewModel: MainViewModel
    private var logs: MutableList<String> = mutableListOf()
    private lateinit var logAdapter: LogAdapter
    private lateinit var searchView: SearchView

    companion object {
        fun newInstance(): LogFragment {
            return LogFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_log, container, false)
        setHasOptionsMenu(true)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerV_funcLog)
        val floatingActionButton: FloatingActionButton = view.findViewById(R.id.fActionButton)
        val Toolbar: Toolbar = view.findViewById(R.id.toolbarLog)
        (activity as AppCompatActivity).setSupportActionBar(toolbarLog)
        recyclerView.setHasFixedSize(true)

        logAdapter = LogAdapter(logs)
        recyclerView.adapter = logAdapter
        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && floatingActionButton.visibility == View.VISIBLE) {
                    floatingActionButton.hide()
                } else if (dy < 0 && floatingActionButton.visibility != View.VISIBLE) {
                    floatingActionButton.show()
                }

            }
        })

        floatingActionButton.setOnClickListener {
            recyclerView.scrollToPosition(logs.size - 1)
        }
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = activity.run {  ViewModelProviders.of(this!!).get(MainViewModel::class.java) }
        mainViewModel.setLogListener(this)
        setHasOptionsMenu(true)

    }
    override fun onBackPressed(): Boolean {
         if (searchView.isIconified) {
            searchView.setIconifiedByDefault(true)
             return true
        }
        return false
    }
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu?.clear()
        inflater?.inflate(R.menu.menu_log, menu)
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.maxWidth = Int.MAX_VALUE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                logAdapter.filter.filter(p0)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                logAdapter.filter.filter(p0)
                return false
            }

        })

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId

        if (id == R.id.action_search) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onLogEvent(log: String) {
        super.onLogEvent(log)
        logs.add(log)
        activity?.runOnUiThread {
            logAdapter.notifyItemInserted(logs.size - 1)
            logAdapter.notifyDataSetChanged()
        }
    }
}