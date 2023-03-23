import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.util.*
import kotlinx.serialization.Serializable
import io.ktor.serialization.kotlinx.json.*
@Serializable
data class DiscordMessage(val content: String)

@OptIn(InternalAPI::class)
suspend fun sendMessageToDiscord(channelId: String, token: String) {
    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation){
            json()
        }

        }
    while (true) {
        val message = readLine() ?: ""
        if (message.equals("exit", true)) {
            httpClient.close()
            return
        }

        try {
            val discordMessage = DiscordMessage(message)
            httpClient.request("https://discord.com/api/v10/channels/$channelId/messages") {
                method = HttpMethod.Post
                header("Authorization", "Bot $token")
                contentType(ContentType.Application.Json)
                setBody(discordMessage)
            }
        } catch (e: Exception) {
            println("Error while sending: " + e.localizedMessage)
            httpClient.close()
            return
        }
    }




}

suspend fun main() {
    val channelId = "1087020601400635501" // Replace with your Discord channel ID
    val token = "MTA4NzAxOTI2ODQ4MDE5MjUxMg.GbK2ZY.ld3Jkx_I6Oh522z8FpG6EmSyv7ElDk8HgSOWmY" // Replace with your bot token

    sendMessageToDiscord(channelId, token)
}