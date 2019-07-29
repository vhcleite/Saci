package padm.ufabc.edu.saci

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import android.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.floatingactionbutton.FloatingActionButton as FloatingActionButton

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private val DEFAULT_ZOOM = 15f
    private lateinit var mMap: GoogleMap

    private val TAG = "MapActitvity"
    private val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    private val COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    private val LOCATION_PERMISSION_REQUEST_CODE = 1234
    private var mPermissionsLocationGranted = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var mSearchText: EditText
    private lateinit var novoIncidente: FloatingActionButton

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mSearchText = findViewById(R.id.map_search_EditText)
        getLocationPermission()

        novoIncidente = findViewById<FloatingActionButton>(R.id.IncidenteInsertButton)

        novoIncidente.setOnClickListener {
            val cadastroIncidenteActivityIntent = Intent(applicationContext, cadastroIncidenteActivity::class.java)
            startActivity(cadastroIncidenteActivityIntent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        return when (id) {
            R.id.my_publications -> {
                Toast.makeText(this, resources.getString(R.string.my_publications),Toast.LENGTH_SHORT).show()
                intent = Intent(this, IncidentsListActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.exit_app -> {
                Toast.makeText(this, resources.getString(R.string.exit_app),Toast.LENGTH_SHORT).show()
                intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.my_profile -> {
                Toast.makeText(this, resources.getString(R.string.my_profile),Toast.LENGTH_SHORT).show()
                intent = Intent(this, showProfileActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun init() {
        Log.d(TAG, "init: initializing")
        mSearchText.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, event ->

            if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE
                || event.action == KeyEvent.ACTION_DOWN || event.action == KeyEvent.KEYCODE_ENTER) {
                geoLocate()
            }
            false

        })
    }

    private fun geoLocate() {
        Log.d(TAG, "geoLocate: geoLocating")
        val searchString = mSearchText.text.toString()
        val geocoder = Geocoder(this)
        val list = geocoder.getFromLocationName(searchString, 1)

        if(list.size > 0) {
            val address = list.get(0)
            Log.d(TAG, "geoLocate: result found: " + address.toString())

            moveCamera(LatLng(address.latitude, address.longitude), DEFAULT_ZOOM)

        } else {
            Log.d(TAG, "geoLocate: no results for search: " + searchString)
            Toast.makeText(this, "No results", Toast.LENGTH_SHORT).show()
        }
    }

    private fun moveCamera(latlong: LatLng, zoom: Float) {
        Log.d(TAG, "moveCamera: moving camera to: lat: " + latlong.latitude + " long: " + latlong.longitude)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlong, zoom))

        val markerOptions = MarkerOptions().position(latlong).title("Location")
        mMap.addMarker(markerOptions)
    }

    private fun getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting current device location")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if(mPermissionsLocationGranted) {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                    if(location != null) {
                        Log.d(TAG, "getDeviceLocation: device location successfully picks up")
                        moveCamera(LatLng(location.latitude, location.longitude), DEFAULT_ZOOM)
                    } else {
                        Toast.makeText(this, "Couldn't catch current location", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: SecurityException) {
                Log.d(TAG, "getDeviceLocation: SecurityException: " + e.message)
            }
        }
    }

    private fun initMap() {
        Log.d(TAG, "initMap: initializing map")
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show()
        Log.d(TAG, "The map is ready")
        mMap = googleMap

        if(mPermissionsLocationGranted) {
            getDeviceLocation()
            mMap.isMyLocationEnabled = true
        } else {
            // Add a marker in Sydney and move the camera
            val sydney = LatLng(-34.0, 151.0)
            mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        }

        init()
    }

    private fun getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permission")
        val permissions = arrayOf(FINE_LOCATION, COARSE_LOCATION)

        if(hasLocationPermission()) {
                mPermissionsLocationGranted = true
            initMap()
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this.applicationContext, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.applicationContext, COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Log.d(TAG, "onRequestPermissionsResult: called")
        mPermissionsLocationGranted = false

        when(requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if(grantResults.size > 0) {
                    for(result in grantResults) {
                        if(result != PackageManager.PERMISSION_GRANTED) {
                            Log.d(TAG, "onRequestPermissionsResult: permission failed")
                            mPermissionsLocationGranted = false
                            return
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permisson granted")
                    mPermissionsLocationGranted = true
                    // initialize map
                    initMap()
                }
            }
        }
    }
}
