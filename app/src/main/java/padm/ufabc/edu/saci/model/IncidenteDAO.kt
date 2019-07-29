package padm.ufabc.edu.saci.model

import java.util.*

class IncidenteDAOClass private constructor() {
    private var incidentes: MutableList<Incidente> = ArrayList()

    init {
         loadTestData()
    }

    companion object {
        val instance = IncidenteDAOClass()
    }

    private fun loadTestData() {
        var i: Incidente

        i = Incidente()

        i.titulo = "Chuva forte"
        i.latitude = -23.644192
        i.longitude = -46.529962
        i.data = Date(2019, 7, 28)
        i.avaliacoes_incidente_passado = 10
        i.avaliacoes_incidente_presente = 2

        incidentes.add(i)

        i.titulo = "Alagamento"
        i.latitude = -23.64401
        i.longitude = -46.526464
        i.data = Date(2019, 7, 27)
        i.avaliacoes_incidente_passado = 12
        i.avaliacoes_incidente_presente = 3

        incidentes.add(i)

    }

    fun add(incidente: Incidente) {
        incidentes.add(incidente)
    }

    fun size(): Int {
        return incidentes.size
    }

    fun getItemAt(pos: Int): Incidente {
        return incidentes[pos]
    }

}