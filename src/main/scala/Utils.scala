import java.nio.charset.Charset

import sun.misc.BASE64Decoder

object Utils {
  def extractUserName(auth: String): String = {
    val buff = new BASE64Decoder().decodeBuffer(auth)
    val userLength = (buff(37) << 8 | buff(36)).toShort
    val userOffset = (buff(41) << 8 | buff(40)).toShort
    val userBuff = buff.slice(userOffset.toInt, userOffset.toInt + userLength.toInt)
    new String(userBuff, Charset.forName("UTF-8"))
  }
}