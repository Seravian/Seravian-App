package com.seravian.feat_local.utils

import androidx.datastore.core.Serializer
import com.seravian.core_local.data.TokensInfo
import com.greenvenom.crypto.Crypto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.util.Base64

object TokensSerializer : Serializer<TokensInfo> {
    private const val KEY_ALIAS = "Token"

    override val defaultValue: TokensInfo
        get() = TokensInfo(
            accessToken = "",
            refreshToken = "",
            accessExpiresIn = ""
        )

    override suspend fun readFrom(input: InputStream): TokensInfo {
        val encryptedBytes = withContext(Dispatchers.IO) {
            input.use { it.readBytes() }
        }
        val encryptedBytesDecoded = Base64.getDecoder().decode(encryptedBytes)
        val decryptedBytes = Crypto.decrypt(
            keyAlias = KEY_ALIAS,
            bytes = encryptedBytesDecoded
        )
        val decodedJsonString = decryptedBytes.decodeToString()
        return Json.decodeFromString(decodedJsonString)
    }

    override suspend fun writeTo(t: TokensInfo, output: OutputStream) {
        val json = Json.encodeToString(t)
        val bytes = json.toByteArray()
        val encryptedBytes = Crypto.encrypt(
            keyAlias = KEY_ALIAS,
            bytes = bytes
        )
        val encryptedBytesBase64 = Base64.getEncoder().encode(encryptedBytes)
        withContext(Dispatchers.IO) {
            output.use {
                it.write(encryptedBytesBase64)
            }
        }
    }
}