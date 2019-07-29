package padm.ufabc.edu.saci

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_incident_list.*

class IncidentsListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    val incidents = arrayOf("Batida de carro", "Boeiro transbordando", "Assalto")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incident_list)

        val listView = findViewById<ListView>(R.id.publications_list)
        listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, incidents)
        listView.setOnItemClickListener(AdapterView.OnItemClickListener() {adapterView, view, i, l ->
            val intent = Intent(this, IncidentVisualizationActivityActivity::class.java)
            startActivity(intent)
        })

    }

}

class IncidentListItemAdapter(private val incidents: Array<String>) : RecyclerView.Adapter<IncidentListItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_incident_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return incidents.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.incidentTitleTextView.text = incidents[position]
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val incidentTitleTextView = itemView.findViewById<TextView>(R.id.incident_title)
    }


}
