package ua.rikutou.studio.data.remote.location.dto

data class LocationsReportRespons(
    val locations: List<LocationResponse>,
    val locationsCount: Int,
    val locationsCountTotal: Int,
)
