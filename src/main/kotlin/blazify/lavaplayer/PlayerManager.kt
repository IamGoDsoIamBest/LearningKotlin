package blazify.lavaplayer

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.TextChannel

class PlayerManager {
    private var INSTANCE: PlayerManager? = null;
    private var musicManagers: Map<Long, GuildMusicManager> = HashMap()
    private var audioPlayerManager: AudioPlayerManager = DefaultAudioPlayerManager()

    init {
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager)
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager)
    }

    fun musicManager(guild: Guild): GuildMusicManager {
        var guildMusicManager: GuildMusicManager = GuildMusicManager(this.audioPlayerManager)
        guild.audioManager.sendingHandler = guildMusicManager.sendHandler
        return this.musicManagers.getOrDefault(guild.idLong, guildMusicManager)
    }

    fun loadAndPlay(channel: TextChannel, trackUrl: String) {
        val musicManager: GuildMusicManager = this.musicManager(channel.guild)
        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, AudioLoadResultHandler(musicManager, channel))

    }
    fun instance(): PlayerManager? {
        if(INSTANCE == null) {
            INSTANCE = PlayerManager()
        }
        return INSTANCE;
    }
}
