package com.hzy.platinum.media.dmr

import android.content.Context
import android.content.Intent
import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.StringReader
import java.net.URI
import java.util.*

const val actionSetVideoAvTransport = "com.dlna.action.SetVideoAvTransport"
const val actionSetMusicAvTransport = "com.dlna.action.SetMusicAvTransport"
const val keyExtraCastAction = "extra.castAction"

interface RendererControl

// -------------------------------------------------------------------------------------------
// - AvTransport
// -------------------------------------------------------------------------------------------
interface AvTransportControl : RendererControl {
    val logger: Logger
    val applicationContext: Context
    fun setAVTransportURI(currentURI: String, currentURIMetaData: String?) {
        logger.i("setAVTransportURI: currentURI=$currentURI")
        currentURIMetaData?.let { logger.i("setAVTransportURI: currentURIMetaData=$it") }
        try {
            URI(currentURI)
        } catch (ex: Exception) {
//            throw AVTransportException(INVALID_ARGS, "CurrentURI can not be null or malformed")
        }
        // 通过分析得到的media类型，来生成或者调用不同的播放器
        val type = getMediaType(currentURIMetaData)
        if (type==MEDIA_TYPE.AUDIO) {
            startMusicCastActivity {
                this.currentURI = currentURI
                this.currentURIMetaData = currentURIMetaData
            }
        }else{
            startVideoCastActivity {
                this.currentURI = currentURI
                this.currentURIMetaData = currentURIMetaData
            }
        }


    }

    fun setNextAVTransportURI(nextURI: String, nextURIMetaData: String?) {
        logger.i("setNextAVTransportURI: nextURI=$nextURI")
        nextURIMetaData?.let { logger.i("setNextAVTransportURI: nextURIMetaData=$it") }

        // 通过分析得到的media类型，来生成或者调用不同的播放器
        val type = getMediaType(nextURIMetaData)
        if (type==MEDIA_TYPE.AUDIO) {
            startMusicCastActivity {
                this.currentURI = currentURI
                this.currentURIMetaData = currentURIMetaData
            }
        }else{
            startVideoCastActivity {
                this.currentURI = currentURI
                this.currentURIMetaData = currentURIMetaData
            }
        }
    }


    enum class MEDIA_TYPE {
        AUDIO, IMAGE, VIDEO
    }


    /**
     * 通过从DMC传递过来的media详细信息metadata，来分析需要播放的media的类型(video、audio或者image)，
     * metadata中通过upnp
     * :class来判断media类型，upnp:class中会包含audioItem、videoItem或者imageItem
     * 的字眼来判别media的类型(具体说明可以查看UPnP-av-ContentDirectory-v1-Service)中的2.4节.
     *
     * @param metadata
     * :通过DMC从DMS上获取到的关于media的详细信息，该详细信息是以XML的形式传递。
     * @return 返回媒体类型，audio、video或者image
     */
    fun getMediaType(metadata: String?): MEDIA_TYPE? {
        val VIDEO_PREFIX = "video"
        val AUDIO_PREFIX = "audio"
        var itemClass = ""
        try {
            val parser = Xml.newPullParser()
            parser.setInput(StringReader(metadata))
            var eventType = parser.eventType
            loop@ while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_DOCUMENT -> {}
                    XmlPullParser.START_TAG -> {
                        val tag = parser.name
                        if (tag.equals("class", ignoreCase = true)) {
                            itemClass = parser.nextText().lowercase(Locale.getDefault())
                            break@loop
                        }
                    }
                    XmlPullParser.END_TAG -> {}
                    else -> {}
                }
                eventType = parser.next()
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return if (itemClass.contains(AUDIO_PREFIX)) MEDIA_TYPE.AUDIO
        else if (itemClass.contains(VIDEO_PREFIX)) MEDIA_TYPE.VIDEO
        else MEDIA_TYPE.IMAGE
    }

    private fun startMusicCastActivity(content: CastAction.() -> Unit) {
        applicationContext.startActivity(Intent(actionSetMusicAvTransport).apply {
            val castAction = CastAction()
            content(castAction)
            this.putExtra(keyExtraCastAction, castAction)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // start from service content,should add 'FLAG_ACTIVITY_NEW_TASK' flag.
        })
    }

    private fun startVideoCastActivity(content: CastAction.() -> Unit) {
        applicationContext.startActivity(Intent(actionSetVideoAvTransport).apply {
            val castAction = CastAction()
            content(castAction)
            this.putExtra(keyExtraCastAction, castAction)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // start from service content,should add 'FLAG_ACTIVITY_NEW_TASK' flag.
        })
    }
    fun setPlayMode(newPlayMode: String?) {
        logger.i("setPlayMode: newPlayMode=$newPlayMode")
    }

    fun play(speed: String?) {
        logger.i("play: speed=$speed")
    }

    fun pause() {
        logger.i("pause")
    }

    fun seek(unit: String?, target: String?) {
        logger.i("seek: unit=$unit, target=$target")
    }

    fun previous() {
        logger.i("previous")
    }

    fun next() {
        logger.i("next")
    }

    fun stop() {
        logger.i("stop")
//        startCastActivity {
//            this.stop = "stop"
//        }
    }

    val currentTransportActions: Array<TransportAction>
}

