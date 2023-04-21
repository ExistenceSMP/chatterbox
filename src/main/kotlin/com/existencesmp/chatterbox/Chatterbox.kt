package com.existencesmp.chatterbox

import io.papermc.paper.event.player.AsyncChatEvent
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.plainText
import net.axay.kspigot.main.KSpigot
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

class Chatterbox : KSpigot() {
    private val urlRegex = Regex("(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})")

    private fun formatMessage(message: Component): Component {
        val mm = MiniMessage.miniMessage()

        val sanitized = mm.deserialize(message.plainText()).plainText()
            .replace(
                urlRegex,
                "<hover:show_text:'Click to open <c:#5387e6><underlined>$1</underlined></c>'><click:open_url:'$1'>$1</click></hover>"
            )

        return mm.deserialize(sanitized)
    }

    companion object {
        lateinit var INSTANCE: Chatterbox
    }

    override fun load() {
        INSTANCE = this
    }

    override fun startup() {
        listen<AsyncChatEvent> {
            it.message(formatMessage(it.originalMessage()))
        }
    }

    override fun shutdown() { }
}