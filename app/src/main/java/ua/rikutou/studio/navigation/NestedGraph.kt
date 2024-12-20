package ua.rikutou.studio.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NestedGraph {
    @Serializable data object Auth : NestedGraph()
    @Serializable data object Studio : NestedGraph()
    @Serializable data object Location : NestedGraph()
    @Serializable data object Equipment : NestedGraph()
    @Serializable data object Actor : NestedGraph()
    @Serializable data object Profile : NestedGraph()
    @Serializable data object Department : NestedGraph()
    @Serializable data object Section : NestedGraph()
    @Serializable data object Transport : NestedGraph()
    @Serializable data object Film : NestedGraph()
    @Serializable data object Document : NestedGraph()
}