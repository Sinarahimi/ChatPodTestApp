package ir.fanap.chattestapp.application.ui.log

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.fanap.chattestapp.R
import ir.fanap.chattestapp.application.ui.MainViewModel
import ir.fanap.chattestapp.application.ui.TestListener

class LogFragment : Fragment(), TestListener {

    private lateinit var mainViewModel: MainViewModel
    private var logs: MutableList<String> = mutableListOf()
    private lateinit var logAdapter: LogAdapter

    companion object {
        fun newInstance(): LogFragment {
            return LogFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_log, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerV_funcLog)
        recyclerView.setHasFixedSize(true)

//        logs = arrayOf("Start Log")
        logAdapter = LogAdapter(logs)
        recyclerView.adapter = logAdapter
        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        linearLayoutManager.reverseLayout = true
//        mainViewModel.showLog()
//        mainViewModel.observableLog.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
//            logs = arrayOf(it)
//            logAdapter.notifyItemInserted(it.length - 1)
//        }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainViewModel.setTestListener(this)

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