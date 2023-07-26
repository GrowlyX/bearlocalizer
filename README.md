# Localizer
A straightforward, fast, and extensible localization library for Kotlin on JVM.

## Features:
- Custom component mappers for classes.
- Config file generation tools.
- Support for custom placeholder replacement implementations.

## Usage

*Implement custom language files!*
```kotlin
interface CoreLang
{
    @Id("events.player-login")
    @Describe("Broadcasts this message to the server when a player logs in!")
    @DefaultsTo("%player% joined the game!")
    fun playerJoins(
        @Self
        @Component("username")
        player: Identity
    ): List<String>

    @Id("events.player-logout")
    @Describe("Broadcasts this message to the server when a player logs out!")
    @DefaultsTo("%player% left the game for %reason%!")
    fun playerLeaves(
        @Self
        @Component("username")
        player: Identity,
        reason: String
    ): List<String>
}
```
*Configure the platform to build resource buckets!*
```kotlin
Localizer
    .configure(
        object : IdentityImpl
        {
            override fun identity(uniqueId: UUID) = noOp
            override fun identity(username: String) = noOp
        },
        object : PlaceholderProcessor
        {
            override fun transform(identity: Identity?, message: String) = message
        }
    ) {
        PropertiesResourceBucket(it)
    }
```
*Build implementations of your language files!*
```kotlin
val coreLang = Localizer.build<CoreLang>()
println(coreLang.playerJoins(identity))
println(coreLang.playerLeaves(identity, "destroyed"))
```